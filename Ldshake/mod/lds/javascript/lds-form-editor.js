/*********************************************************************************
 * LdShake is a platform for the social sharing and co-edition of learning designs
 * Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
 *
 * (Contributors, alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by the
 * Free Software Foundation with the addition of the following permission added
 * to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK
 * IN WHICH THE COPYRIGHT IS OWNED BY Universitat Pompeu Fabra (UPF), Barcelona,
 * UPF DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program; if not, see http://www.gnu.org/licenses.
 *
 * You can contact the Interactive Technologies Group (GTI), Universitat Pompeu Fabra, Barcelona.
 * headquarters at c/Roc Boronat 138, Barcelona, or at email address davinia.hernandez@upf.edu
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License version 3,
 * these Appropriate Legal Notices must retain the display of the "Powered by
 * LdShake" logo with a link to the website http://ldshake.upf.edu.
 * If the display of the logo is not reasonably feasible for
 * technical reasons, the Appropriate Legal Notices must display the words
 * "Powered by LdShake" with the link to the website http://ldshake.upf.edu.
 ********************************************************************************/

function register_deployment() {
    var submitData = {
        guid: $('#lds_edit_guid').val(),
        lds_id: lds_id,
        vle_id: vle_id,
        course_id: course_id
    }

    if(submitData.guid != '0') {
        $.ajax({
            type: "POST",
            url: baseurl + 'action/lds/register_deployment',
            data: submitData,
            dataType: "json",
            success: function (returnData) {
            },

            timeout: ajax_timeout * 1000,
            error: function() {
            }
        });
    }
}

//Controls the time the user has been idle. Might close the form i order to let other users edit the LdS
var activity;
var first_save;
var saving_started = false;
var hash = '0';
var timer = null;
var ping_interval = 30;
var edit_timeout = 60000;
var ajax_retry_max = 3;

var saving_started = false;
var ajax_timeout = 30;
if(ilde_debug)
    ajax_timeout = 19999;

var editor = null;

//Saves the current document tab, the one that we are editing
var currentTab = 0;

//Saves at which tab we have shown the menu (which may differ from the currentTab value!)
var currentTabMenu = 0;

//Saves a checksum of the form data when the form is loaded and after a save.
//We will compare this checsum when deciding to exit the form with or without a "save chages" warning.
var beforeEditChecksum = 0;

//Determines if the title of the first document will change with the LdS title (see the function initDocName)
var changeDocTitle = false;
//and this var is a reference to the tab we'll change (for slow browsers)
var $affectedTab = null;

//tells if the Ctrl key is pressed
var isCtrl = false; 
var lastKey = '';

//Basic checksum function to warn for unsaved things
function checksum(s)
{
    var chk = 0x12345678;
    for (var i = 0; i < s.length; i++)
        chk += (s.charCodeAt(i) * (i + 1));

    return chk;
}

function normalizeSpace (string)
{
    //Remove the firebug div 1st...
    string = string.replace(/<div.*_firebugConsole.*>\s*&nbsp;<\/div>/, '');
    return $.trim(string.replace(/\s+/g, ' '));
}

//Gets all the relevant fields of the form ang denerates a checksum for them
function calcChecksum ()
{
    var values = new Array();

    values.push ($('#lds_edit_title').val());
    values.push ($('#as-values-discipline').val());
    values.push ($('#as-values-pedagogical_approach').val());
    values.push ($('#as-values-tags').val());
    values.push ($('#completeness_input').val());
    values.push ($('#granularity_input').val());

    for (var i in documents)
    {
        values.push (documents[i].title);
        values.push (normalizeSpace(documents[i].body));
    }

    var longString = '';
    for (var i in values)
        longString += '-' + values[i]; //Dash to avoid rare cases where chars move from field to field...

    return checksum(longString);
}

//Generates a tag list to display once finished editing tags
function generateTagList (source)
{
	var tagFields = new Array ('discipline', 'pedagogical_approach', 'tags');
	var tags = new Array();
	for (var n in tagFields)
	{
		if (source == 'popup')
			var elements = $('#as-values-' + tagFields[n]).val().split(',');
		else if (source == 'init')
			var elements = initLdS[tagFields[n]].split(',');
			
		for (var i in elements)
		{
			if ($.trim(elements[i]) != '') tags.push ($.trim(elements[i]) + ',' + tagFields[n]);
		}
	}
	
	tags.sort();
	
	if (tags.length > 0)
	{
		$('#lds_edit_tags .tooltip').hide();
		
		var html = '';
		for (var i in tags)
		{
			tag = tags[i].split(',');
			html+= '<span class="lds_tag ' + tag[1] + '">' + tag[0] + '</span> ';
		}
	}
	else
	{
		$('#lds_edit_tags .tooltip').show();
        html = "";
	}
	$('#lds_edit_tags_list').html(html);
}

function save_error() {
    $('#lds_edit_buttons').removeClass('busy');
    saving_started = false;
    alert("There is a network error, check your connection and try again!");
}

function save_lds(save_seq, params) {
    if(!save_seq) {
        if(saving_started)
            return false;

        saving_started = true;
        $('#lds_edit_buttons').addClass('busy');
        save_seq = new Object();
        save_seq.lds = false;
        save_seq.params = params;
        save_seq.share = false;
        save_seq.retry_count = 0;
        save_seq.stop = false;
        save_seq.save = arguments.callee;
        save_seq.cancel = function() {
            $('#lds_edit_buttons').removeClass('busy');
            saving_started = false;
            if(save_seq.timer)
                clearTimeout(save_seq.timer);
        }
        save_seq.timer = null;
    } else {
        params = save_seq.params;
    }

    if(save_seq.timer)
        clearTimeout(save_seq.timer);

    save_seq.timer = setTimeout ('save_error()', 1000 * ajax_timeout*1.4);

    if( save_seq.stop
        || save_seq.retry_count > ajax_retry_max) {
        $('#lds_edit_buttons').removeClass('busy');
        saving_started = false;
        if(save_seq.timer) {
            clearTimeout(save_seq.timer);
            save_seq.timer = null;
        }
        alert("There is a network error, check your connection and try again!");
        return true;
    }

    if(!save_seq.upload && upload) {
        save_seq.current = 'upload';
        upload_file(save_seq);
        return true;
    }

    if(!save_seq.lds) {
        save_seq.current = 'lds';
        ajax_submit(save_seq, params.redirect);
        return true;
    }

    if(!save_seq.share && sharingOptionsPendingToSave) {
        save_seq.current = 'share';
        saveSharingOptionsNG(save_seq);
        return true;
    }

    if( save_seq.lds
        &&(save_seq.share || !sharingOptionsPendingToSave)
        &&(save_seq.upload || !upload)
        ) {
        if(save_seq.timer) {
            clearTimeout(save_seq.timer);
            save_seq.timer = null;
        }

        if(params.redirect) {
            window.onbeforeunload = null;
            window.location = $('#lds_edit_referer').val();
        } else {
            $('#lds_edit_buttons').removeClass('busy');
            saving_started = false;
        }
        return true;
    }
}

function upload_file(save_seq) {
    if(!$("#file_input").val().length && $('#lds_edit_guid').val() == "0") {
        $("#form_file_input_empty").show();
        save_seq.cancel();
        return false;
    }

    $("#upload_result").unbind('load');
    $("#upload_result").load(function() {
        editor_id = $("#upload_result").contents().find("body").text();
        save_seq[save_seq.current] = true;
        save_seq.retry_count=0;
        save_seq.save(save_seq);
    });
    $("#file_upload_form").submit();
    return true;
}

function ajax_submit (save_seq, redirect)
{
	//Put the current editor data in the array before sending it
	//documents[currentTab].body = editor.getData();
	var submitData = null;
    var save_url = "action/lds/save_editor";
    var goto_url = $('#lds_edit_referer').val();
    goto_url = baseurl + 'pg/lds';

    /*
    if(upload) {
        if(!$("#file_input").val().length && $('#lds_edit_guid').val() == "0") {
            $("#form_file_input_empty").show();
            $("#lds_edit_buttons").removeClass('busy');
            return false;
        }

        //upload the file first
        if(!parseInt(editor_id)) {
            $("#upload_result").unbind('load');
            $("#upload_result").load(function() {
                editor_id = $("#upload_result").contents().find("body").text();
                ajax_submit(true);
            });
            $("#file_upload_form").submit();
            return true;
        }
    }
    */

    if(editorType == 'gluepsrest')
    {
        save_url = "action/lds/save_glueps";

        documents[currentTab].body = editor.getData();

        submitData =
        {
            guid: $('#lds_edit_guid').val(),
            revision: $('#lds_edit_revision').val(),
            title: $('#lds_edit_title').val(),
            discipline: $('#as-values-discipline').val(),
            pedagogical_approach: $('#as-values-pedagogical_approach').val(),
            tags: $('#as-values-tags').val(),
            completeness: $('#completeness_input').val(),
            granularity: $('#granularity_input').val(),
            editor_id: editor_id,
            editorType: editorType,
            documents: documents,
            document_url: document_url,
            lds_id: lds_id,
            vle_id: vle_id,
            course_id: course_id,
            implementation_helper_id: implementation_helper_id
        };

        goto_url = baseurl + 'pg/lds/implementations';

    }
	else if(editorType == 'webcollage')
	{
		top.window.document.getElementById('lds_editor_iframe').contentWindow.TableGenerator.generateSummary();
		top.window.document.getElementById('lds_editor_iframe').contentWindow.Loader.save_ldshake();
		
		submitData = 
		{
			guid: $('#lds_edit_guid').val(),
			revision: $('#lds_edit_revision').val(),
			title: $('#lds_edit_title').val(),
			discipline: $('#as-values-discipline').val(),
			pedagogical_approach: $('#as-values-pedagogical_approach').val(),
			tags: $('#as-values-tags').val(),
			completeness: $('#completeness_input').val(),
			granularity: $('#granularity_input').val(),
			editor_id: editor_id,
			editorType: editorType,
			summary: encodeURIComponent(top.window.document.getElementById('lds_editor_iframe').contentWindow.document.getElementById('SummaryTabContent').innerHTML),
		};
	} else if(editorType == 'project_design')
    {
        save_url = "action/lds/projects/save";

        submitData =
        {
            guid: $('#lds_edit_guid').val(),
            revision: $('#lds_edit_revision').val(),
            title: $('#lds_edit_title').val(),
            discipline: $('#as-values-discipline').val(),
            pedagogical_approach: $('#as-values-pedagogical_approach').val(),
            tags: $('#as-values-tags').val(),
            completeness: $('#completeness_input').val(),
            granularity: $('#granularity_input').val(),
//            editor_id: editor_id,
            editorType: editorType,
            JSONData: JSON.stringify(ldproject)
//            summary: encodeURIComponent(top.window.document.getElementById('lds_editor_iframe').contentWindow.document.getElementById('SummaryTabContent').innerHTML),
        };
    } else {
        if(!google_docs)
            documents[currentTab].body = editor.getData();

        if(implementation) {
            save_url = "action/lds/save_glueps";
            submitData =
            {
                guid: $('#lds_edit_guid').val(),
                revision: $('#lds_edit_revision').val(),
                title: $('#lds_edit_title').val(),
                discipline: $('#as-values-discipline').val(),
                pedagogical_approach: $('#as-values-pedagogical_approach').val(),
                tags: $('#as-values-tags').val(),
                completeness: $('#completeness_input').val(),
                granularity: $('#granularity_input').val(),
                editor_id: editor_id,
                editorType: editorType,
                documents: documents,
                document_url: document_url,
                implementation_helper_id: implementation_helper_id,
                lds_id: lds_id,
                vle_id: vle_id,
                course_id: course_id
            };
            goto_url = baseurl + 'pg/lds/implementations';
        } else {
            submitData =
            {
                guid: $('#lds_edit_guid').val(),
                revision: $('#lds_edit_revision').val(),
                title: $('#lds_edit_title').val(),
                discipline: $('#as-values-discipline').val(),
                pedagogical_approach: $('#as-values-pedagogical_approach').val(),
                tags: $('#as-values-tags').val(),
                completeness: $('#completeness_input').val(),
                granularity: $('#granularity_input').val(),
                editor_id: editor_id,
                editorType: editorType,
                documents: documents,
                document_url: document_url
            };
            if(google_docs) {
                submitData['google_docs_support_id'] = google_docs_support_id;
                submitData['google_docs_support_title'] = t9n.supportTitle;
            }
        }
	}

    submitData.lds_recovery = lds_recovery;

    $.ajax({
        type: "POST",
        url: baseurl + save_url,
        data: submitData,
        dataType: "json",
        success: function (returnData) {
            if (redirect == false)
            {
                if(!returnData.requestCompleted) {
                    save_seq[save_seq.current] = false;
                    save_seq.retry_count++;
                    save_seq.save(save_seq);
                    return false;
                }

                //First, the main LdS guid:
                $('#lds_edit_guid').val(returnData.LdS);

                //Then the revision created
                $('#lds_edit_revision').val(returnData.revision);

                //Then all the documents
                for (var i = 0; i < returnData.docs.length; i++)
                {
                    documents[i].guid = returnData.docs[i].id;
                    //We only modify from 0 to 1 (not the other way round)
                    if (documents[i].modified == '0')
                        documents[i].modified = returnData.docs[i].modified;
                }

                if(first_save)
                {
                    ping_editing();
                    first_save = false;
                }

                /*
                if (sharingOptionsPendingToSave)
                    saveSharingOptions ();
                    */
            }
            else
            {
                $('#lds_edit_guid').val(returnData.LdS);

                /*
                //Save the sharing options and then leave
                if (sharingOptionsPendingToSave)
                {
                    //Assign all the returning guids to the documents
                    var returnData = $.parseJSON (data);

                    //First, the main LdS guid:
                    $('#lds_edit_guid').val(returnData.LdS);

                    saveSharingOptions (function ()
                    {
                        //window.location = $('#lds_edit_referer').val();
                        window.onbeforeunload = null;
                        window.location = goto_url;
                    });
                }
                else
                {
                    window.onbeforeunload = null;
                    //window.location = $('#lds_edit_referer').val();
                    window.location = goto_url;
                }
                */
            }
            save_seq[save_seq.current] = true;
            save_seq.retry_count=0;
            save_seq.save(save_seq);
        },

        timeout: ajax_timeout * 1000,
        error: function() {
            save_seq[save_seq.current] = false;
            save_seq.retry_count++;
            save_seq.save(save_seq);
        }
    });
}

function initSliders ()
{
	$('#granularity_slider').slider({
			value:$('#granularity_input').val(),
			min: 0,
			max: 8,
			step: 1,
			slide: function (event, ui) {
				$('#granularity_input').val( ui.value );
			}
	});
	$('#granularity_input').val($('#granularity_slider').slider('value'));
	
	$('#completeness_slider').slider({
			value:$('#completeness_input').val(),
			min: 0,
			max: 5,
			step: 1,
			slide: function (event, ui) {
				$('#completeness_input').val( ui.value );
			}
	});
	$('#completeness_input').val($('#completeness_slider').slider('value'));
}

function initTags ()
{
	//Hack to avoid a "bug" of the autocomplete plugin. If no tags are defined, no suggest was loaded...
	if (mytags.discipline.length == 0) mytags.discipline = [{'name':'','value':''}];
	if (mytags.pedagogical_approach.length == 0) mytags.pedagogical_approach = [{'name':'','value':''}];
	if (mytags.tags.length == 0) mytags.tags = [{'name':'','value':''}];
	
	$("#lds_edit_tags_input_discipline").autoSuggest(mytags.discipline, {asHtmlID: "discipline", preFill: initLdS.discipline, startText: t9n.suggestEnterTags, keyDelay: 0, neverSubmit: true});
	$("#lds_edit_tags_input_pedagogical_approach").autoSuggest(mytags.pedagogical_approach, {asHtmlID: "pedagogical_approach", preFill: initLdS.pedagogical_approach, startText: t9n.suggestEnterTags, keyDelay: 0, neverSubmit: true});
	$("#lds_edit_tags_input_tags").autoSuggest(mytags.tags, {asHtmlID: "tags", preFill: initLdS.tags, startText: t9n.suggestEnterTags, keyDelay: 0, neverSubmit: true});

	//Visibility toggle
	$('#lds_edit_tags,#lds_edit_tags_popup_close').click (function ()
	{
		$('#lds_edit_tags_popup').toggle();
		$('#shade').toggle();
		return false;
	});
	
	$('#lds_edit_tags_popup_close').click (function ()
	{
		generateTagList ('popup');
	});
	
	$('.as-input').blur (function ()
	{
		//var e = jQuery.Event("keydown");
		//e.which = 9; // # Some key code value
		//$(this).trigger(e);

		//$(this).keydown(9);
	});
}

function loadCKData ()
{
    //Build the tabs
    for (var i in documents)
        $('#lds_edit_tabs li.lds_newtab').before ('<li class="lds_tab"><span class="lds_tab_title">' + documents[i].title + '</span> <span class="lds_tab_options">▲</span></li>');

    //Highlight the first one and fill the ckeditor
    if(!upload)
        $('#lds_edit_tabs li.lds_exetab').addClass('current');
    else
        $('#lds_edit_tabs li.lds_tab:first-child').addClass('current');

    editor.setData (documents[0].body);

    $('#lds_edit_tabs li.lds_tab').click(function () {

        $('#lds_editor_iframe').fadeOut(200, function() {
            $('#rich_text_box').show(200,function() {resizeCK();});
        });

    });
}
//Initial data loading
function loadData ()
{
	$('#lds_edit_title').val(initLdS.title);
	$('#granularity_input').val(initLdS.granularity);
	$('#completeness_input').val(initLdS.completeness);
	$('#lds_edit_guid').val(initLdS.guid);
	
	generateTagList ('init');


    initSliders ();
	initTags ();
}

//PAU: Basic concurrence control. Send editing signal to the server every <ping_interval> secs.
function ping_editing ()
{
    if ($('#lds_edit_guid').val() != '0')
    {
        $.post (baseurl + 'action/lds/ping_editing', {entity_guid:$('#lds_edit_guid').val(), editing: true}, function (data)
        {
            setTimeout ('ping_editing()', 1000 * ping_interval);
        });
    }
    else setTimeout ('ping_editing()', 1000 * ping_interval);
}

function autosave_and_exit ()
{
    save_lds (null,{redirect: true});
}

function check_activity()
{
	if (activity == false)
	{
        autosave_and_exit();
	}
	else
	{
		activity = false;
		timer = setTimeout ('check_activity()', 1000 * edit_timeout);
	}
}

function active()
{
	activity = true;
}

//We need to free the LdS resource for editing on any way the user exits the editing page
function free_lds ()
{
	$(window).unload (function ()
	{
		//TODO: Add some visual feedback for the time the ajax request is made? (e.g. "Saving changes..." or "Freeing resource...")
		$.ajax({
	        type:	'POST',
	        url:	baseurl + 'action/lds/ping_editing',
	        data:	{entity_guid:$('#lds_edit_guid').val(), editing: false, editor_id: editor_id, editorType: editorType}, 
	        async:	false,
	        success: function(msg)
	        {
	        	//alert (msg);
	        }
		});
	});
}

function initCKED ()
{
    var options = {
        filebrowserUploadUrl: baseurl + 'actions/lds/upload',
        customConfig: baseurl+'mod/lds/ckeditor4/config.js?t='+ldshake_cache.lds
    };

    $('#lds_edit_body').ckeditor(function ()
    {
        editor = this;
        resizeCK();
        loadCKData ();

        //Concurrence control
        ping_editing();

        //inaction_trigger();

        //Stopped loading:
        $('#shade,#lds_loading_contents').hide();

/*
        editor.on( 'contentDom', function( evt )
        {

            editor.document.on( 'keyup', function(event)
            {
                //Windows, FF Mac & Webkit Mac
                if(event.data.$.keyCode == 17 || event.data.$.keyCode == 224 || event.data.$.keyCode == 91) isCtrl=false;
            });

            editor.document.on( 'keydown', function(event)
            {
                //Windows, FF Mac & Webkit Mac
                if(event.data.$.keyCode == 17 || event.data.$.keyCode == 224 || event.data.$.keyCode == 91) isCtrl=true;
                if(event.data.$.keyCode == 83 && isCtrl == true)
                {
                    try {
                        event.data.$.preventDefault();
                    } catch(err) {}
                    ajax_submit (false);
                    return false;
                }
            });

        }, editor.element.$);
*/
        //$('#cke_lds_edit_body').hide();
        $("#lds_editor_iframe").attr("src", document_iframe_url);
    }, options);
}

//Set editor height, depending on our browser
function resizeCK ()
{
    var wHeight = $(window).height() - $('#lds_edit_contents').offset().top - 50;
    wHeight = Math.max (wHeight, 300);
    editor.resize(null,wHeight);
}

function resizeEditorFrame ()
{
    var wHeight = $(window).height() - $('#lds_edit_contents').offset().top - 50;
    wHeight = Math.max (wHeight, 300);
    $('#lds_editor_iframe').height(wHeight);
    $('#lds_support_editor_iframe').height(wHeight);
    $('#lds_edit_contents').height(wHeight);
    $('#rich_text_box').height(wHeight);

}

function initDocName ()
{
    $('#lds_edit_title').focus (function()
    {
        //If we haven't changed the title of the 1st document yet,
        //the LdS title will be the document title as well.
        //changeDocTitle = (documents[0].title == t9n.untitledDoc);
        changeDocTitle = true;
        if(google_docs)
            $affectedTab = $('.lds_exetab');
        else
            $affectedTab = $('#lds_edit_tabs li.lds_tab:eq(0)').find('.lds_tab_title');
    });

    $('#lds_edit_title').keyup (function()
    {
        if (changeDocTitle)
        {
            if ($.trim($(this).val()).length > 0)
            {
                if(!upload && !restapi && editorType != 'gluepsrest') {
                    if(!google_docs) documents[0].title = $(this).val();
                    $affectedTab.text($(this).val());
                }
                if(restapi)
                    postMessageWicChangeTitle($(this).val());
            }
            else
            {
                if(!upload && !restapi && editorType != 'gluepsrest') {
                    if(!google_docs) documents[0].title = t9n.untitledDoc;
                    $affectedTab.text(t9n.untitledDoc);
                }
                if(restapi)
                    postMessageWicChangeTitle(t9n.untitledDoc);
            }
        }

        function postMessageWicChangeTitle(title){
            var win = document.getElementById("lds_editor_iframe").contentWindow;
            var m_title = {
                "type": "ldshake_name",
                "data": title
            }
            win.postMessage(m_title, restapi_remote_domain);
        }
    });

    $('#lds_edit_title').blur(function(){
        if ($.trim($(this).val()).length == 0)
            $(this).val(t9n.untitledLdS)
    });
}

//http://stackoverflow.com/questions/1912501/unescape-html-entities-in-javascript
function htmlDecode(input){
    var e = document.createElement('div');
    e.innerHTML = input;
    return e.childNodes[0].nodeValue;
}

//Check if a given document name exists in the LdS collection
function docExists (title)
{
    for (var i in documents)
        if (documents[i].title == title) return true;

    return false;
}

function tabs()
{
    //Show tab options menu
    $('.lds_tab_options').live ('click', function (event)
    {
        event.stopPropagation();

        //Where have we clicked?
        currentTabMenu = $(this).parent().prevAll('li').length;

        //Place and show the menu
        $('#lds_edit_tabs_popup')
            .css('top', $(this).offset().top - $('#lds_edit_tabs_popup').height() - 5)
            .css('left', $(this).offset().left)
            .toggle();

        return false;
    });

    //Hide tab options menu
    $('body').click(function()
    {
        $("#lds_edit_tabs_popup").hide();
    });

    //Clicking tab for doc switching
    $('#lds_edit_tabs li.lds_tab').live ('click', function ()
    {
        //What tab have we clicked?
        pos = $(this).prevAll('li.lds_tab').length;

        if(!google_docs) {
            //First, get the editor data and put it into the array.
            documents[currentTab].body = editor.getData();

            //Then put the clicked document data into the editor
            editor.setData (documents[pos].body);
        }

        //Finally set the current tab
        currentTab = pos;

        //Styling: highlight the current tab
        $('#lds_edit_tabs li').removeClass('current');
        $(this).addClass('current');
    });

    //New tab button
    if(!google_docs)
    $('#lds_edit_tabs li.lds_newtab').click (function ()
    {
        var tabTitle = prompt (t9n.newDocTitle);
        if (tabTitle != null) tabTitle = $.trim(tabTitle);

        //Basic checks: empty or repeated title
        error = false;
        if (tabTitle == '')
        {
            alert (t9n.newDocTitleEmpty);
            error = true;
        }
        else if (docExists(tabTitle))
        {
            alert (t9n.newDocTitleRepeated);
            error = true;
        }

        if (tabTitle != null && !error)
        {
            var doc_recovery = Math.floor((Math.random()*100000000000000000)+1);
            $('#lds_edit_tabs li.lds_tab:last').after ('<li class="lds_tab"><span class="lds_tab_title">' + tabTitle + '</span> <span class="lds_tab_options">▲</span></li>');
            documents.push ({guid:'0',title:tabTitle,body:'',modified:'0',doc_recovery:doc_recovery});

            //First, get the editor data and put it into the array.
            documents[currentTab].body = editor.getData();

            currentTab = documents.length - 1;
            $('#lds_edit_tabs li').removeClass('current');
            $('#lds_edit_tabs li:eq(' + currentTab + ')').addClass('current');
            editor.setData('');

            $('#lds_edit_tabs li.current').click(function () {
                $('#lds_editor_iframe').fadeOut(200, function() {
                    $('#rich_text_box').show(200,function() {resizeCK();});
                });
            }).click();
        }
    });

    //Tab option: rename
    $('#lds_edit_rename_tab').click (function ()
    {
        $('#lds_edit_tabs_popup').hide();

        var tabTitle = prompt (t9n.docSetTitle);
        if (tabTitle != null) tabTitle = $.trim(tabTitle);

        //Basic checks: empty or repeated title
        error = false;
        if (tabTitle == '')
        {
            alert (t9n.newDocTitleEmpty);
            error = true;
        }
        else if (docExists(tabTitle))
        {
            //If we write the same name for the same tab, no error occurs.
            if (tabTitle != documents[currentTabMenu].title)
            {
                alert (t9n.newDocTitleRepeated);
                error = true;
            }
        }

        if (tabTitle != null && !error)
        {
            //Rename the document in the documents array
            documents[currentTabMenu].title = tabTitle;

            //Rename tne actual tab
            $('#lds_edit_tabs li.lds_tab:eq(' + currentTabMenu + ')').children('.lds_tab_title').text(tabTitle);
        }

        return false;
    });

    //Tab option: delete
    $('#lds_edit_delete_tab').click (function ()
    {
        $('#lds_edit_tabs_popup').hide();

        if (confirm (t9n.docConfirmDelete + "\n\n" + documents[currentTabMenu].title))
        {
            if (documents.length > 1)
            {
                //Remove the document from the documents array
                documents.splice(currentTabMenu, 1);

                //Remove the tab
                $('#lds_edit_tabs li.lds_tab:eq(' + currentTabMenu + ')').remove();

                //Update the current tab, either the one on the left (if exists) or the one on the right.
                if (currentTabMenu != currentTab) documents[currentTab].body = editor.getData();
                currentTab = (currentTabMenu == 0) ? 0 : currentTabMenu - 1;
                $('#lds_edit_tabs li').removeClass('current');
                $('#lds_edit_tabs li:eq(' + currentTab + ')').addClass('current');

                if(!google_docs)
                    editor.setData(documents[currentTab].body);
            }
            else
            {
                documents = new Array ();
                documents.push ({guid:'0',title:t9n.untitledDoc,body:''});
                $('#lds_edit_tabs li:eq(0) .lds_tab_title').text (t9n.untitledDoc);

                if(!google_docs)
                    editor.setData('');
            }
        }

        return false;
    });

    //set up google docs support tab
    if(google_docs) {
        $('#lds_edit_tabs li.lds_exetab').before ('<li class="lds_tab lds_tab_google_support"><span class="lds_tab_title">' + t9n.supportTitle + '</span> <span class="lds_tab_options">▲</span></li>');
        currentTab = documents.length - 1;
        $('#lds_edit_tabs li').removeClass('current');
        //$('#lds_edit_tabs li:eq(' + currentTab + ')').addClass('current');

        $('.lds_tab_google_support').click(function () {
            var $google_support = $('#lds_support_editor_iframe');
            $google_support.attr('src', $google_support.attr('alt_src'));

            $('#lds_editor_iframe').fadeOut(200, function() {
                $('#rich_text_box').show(200,function() {});
            });
        });
    }

}

function inaction_trigger ()
{
    timer = setTimeout ('check_activity()', 1000 * edit_timeout);

    $('input').change (function ()
    {
        //clearTimeout (timer);
        activity = true;
    });

    //We're capturing keypresses here cos it's possible that the user spends a long time in the field.
    editor.on('instanceReady', function ()
    {
        editor.document.on ('keyup', function ()
            //CKEDITOR.instances['description'].document.on ('keyup', function ()
        {
            alert ('ei');
            activity = true;
        });
    });
}

/*editor iframe information exchange*/
$(document).ready(function() {
    if(restapi) {
        window.addEventListener('message',function(event) {
            if(event.origin !== restapi_remote_domain) return;
            var editor_iframe = document.querySelector("#lds_editor_iframe[auth=messaging]");
            if(event.data.type == 'ldshake_editor_ready') {
                var m_sectoken = {
                    "type": "ldshake_sectoken",
                    "data": editor_id
                }

                if(editorType == 'gluepsrest'
                    && $('#lds_edit_guid').val() == '0')
                    save_lds (null,{redirect: false});

                editor_iframe.contentWindow.postMessage(m_sectoken,restapi_remote_domain);
            }

            if(event.data.type == 'glueps_deployment') {
                register_deployment();
            }
        },false);
    }
});


$(document).ready(function()
{
    lds_recovery = Math.floor((Math.random()*100000000000000000)+1);
    for(var i=0; i<documents.length;i++) {
        if(documents[i].guid == "0")
            documents[i].doc_recovery = Math.floor((Math.random()*100000000000000000)+1);
    }

    resizeEditorFrame();
	top.window.onclick = active;
	top.window.onkeypress = active;

    //$('#lds_edit_body').hide();

    //active();
	//check_activity();

    if($.fn.ckeditor)
        initCKED ();

    //And update it in case it is resized
    $(window).resize (function ()
    {
        if (editor !== null)
            resizeCK();
        resizeEditorFrame();
    });

    initDocName ();

    loadData();
	
	$('#shade,#lds_loading_contents').hide();

    if($('#lds_edit_guid').val() != '0')
	{
		ping_editing ();
	}
	else
		first_save = true;
	
	free_lds();
	
	$('#lds_edit_title').focus (function ()
	{
		$(this).select();
	});
	
	$('#lds_edit_tags').mouseenter (function ()
	{
		$(this).addClass('over');
	})
	.mouseleave (function ()
	{
		$(this).removeClass('over');
	});

    tabs ();

    $("#file_input").change(function() {
       editor_id = 0;
    });

    //Save action
    $('#lds_edit_save').click (function () {
        save_lds (null,{redirect: false});
        return false;
    });

    //Save and exit action
    $('#lds_edit_save_exit_editor').click (function () {
        save_lds (null,{redirect: true});
        return false;
    });

    /*
	//Save shortcut: Ctrl+S
	$(document).keyup(function (e)
	{
		if(e.which == 17) isCtrl=false;
	})
	.keydown(function (e)
	{
		if(e.which == 17) isCtrl=true;
		if(e.which == 83 && isCtrl == true)
		{
			ajax_submit (false);
			return false;
		}
	});
	*/

    $('.lds_exetab').click(function () {

        $('#rich_text_box').fadeOut(200, function() {
            $('#lds_editor_iframe').show();
        });

        $('#lds_edit_tabs li').removeClass('current');
        $('#lds_edit_tabs li.lds_exetab').addClass('current');
    });

	//Before exit warning.
	//Doesn't really work w/firebug enabled. Will always warn.
    window.onbeforeunload = function ()
    {
        documents[currentTab].body = editor.getData();

        if (beforeEditChecksum != calcChecksum ())
            return t9n.confirmExit;
    };

    //$("#lds_editor_iframe").attr("src", document_iframe_url);

});
