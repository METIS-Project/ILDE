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

//Holds the instance of the CKeditor
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

//Controls the time the user has been idle. Might close the form i order to let other users edit the LdS
var timer = null;
var ping_interval = 30;
var edit_timeout = 600;

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
	}
	$('#lds_edit_tags_list').html(html);
}

function ajax_submit (redirect)
{
	$('#lds_edit_buttons').addClass('busy');
	
	//Put the current editor data in the array before sending it
	documents[currentTab].body = editor.getData();
	
	var submitData = 
	{
		guid: $('#lds_edit_guid').val(),
		revision: $('#lds_edit_revision').val(),
		title: $('#lds_edit_title').val(),
		discipline: $('#as-values-discipline').val(),
		pedagogical_approach: $('#as-values-pedagogical_approach').val(),
		tags: $('#as-values-tags').val(),
		completeness: $('#completeness_input').val(),
		granularity: $('#granularity_input').val(),
		documents: documents
    };

	$.post (baseurl + "action/lds/save", submitData, function (data)
	{
		if (redirect == false)
		{
			$('#lds_edit_buttons').removeClass('busy');
			
			//Assign all the returning guids to the documents
			var returnData = $.parseJSON (data);
	
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
			
			
			//Now we save the sharing options
			saveSharingOptions ();
		}
		else
		{
			//Save the sharing options and then leave
			if (sharingOptionsPendingToSave)
			{
				//Assign all the returning guids to the documents
				var returnData = $.parseJSON (data);

				//First, the main LdS guid:
				$('#lds_edit_guid').val(returnData.LdS);

				saveSharingOptions (function ()
				{
					window.location = $('#lds_edit_referer').val();
				});
			}
			else
			{
				window.location = $('#lds_edit_referer').val();
			}
		}
	});
	
	//Generate a before edit checksum
	beforeEditChecksum = calcChecksum ();
}

//Set editor height, depending on our browser
function resizeCK ()
{
	var wHeight = $(window).height() - $('#lds_edit_contents').offset().top - 30;
	wHeight = Math.max (wHeight, 300);
	editor.resize(null,wHeight);
}

function initCKED ()
{
	var options = {filebrowserUploadUrl: baseurl + 'actions/lds/upload'};
	
	$('#lds_edit_body').ckeditor(function ()
	{
		editor = this;
		resizeCK();
		loadData ();
		
		//Concurrence control
		ping_editing();
		inaction_trigger();
		
		//Stopped loading:
		$('#shade,#lds_loading_contents').hide();
		
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
	}, options);
	
	//And update it in case it is resized
	$(window).resize (function ()
	{
		if (editor !== null) resizeCK();
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
		
		//First, get the editor data and put it into the array.
		documents[currentTab].body = editor.getData();
		
		//Then put the clicked document data into the editor
		editor.setData (documents[pos].body);
		
		//Finally set the current tab
		currentTab = pos;
		
		//Styling: highlight the current tab
		$('#lds_edit_tabs li').removeClass('current');
		$(this).addClass('current');
	});
	
	//New tab
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
			$('#lds_edit_tabs li.lds_tab:last').after ('<li class="lds_tab"><span class="lds_tab_title">' + tabTitle + '</span> <span class="lds_tab_options">▲</span></li>');
			documents.push ({guid:'0',title:tabTitle,body:'',modified:'0'});
			
			//First, get the editor data and put it into the array.
			documents[currentTab].body = editor.getData();
			
			currentTab = documents.length - 1;
			$('#lds_edit_tabs li').removeClass('current');
			$('#lds_edit_tabs li:eq(' + currentTab + ')').addClass('current');
			editor.setData('');
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
				editor.setData(documents[currentTab].body);
			}
			else
			{
				documents = new Array ();
				documents.push ({guid:'0',title:t9n.untitledDoc,body:''});
				$('#lds_edit_tabs li:eq(0) .lds_tab_title').text (t9n.untitledDoc);
				editor.setData('');
			}
		}
		
		return false;
	});
}

//http://stackoverflow.com/questions/1912501/unescape-html-entities-in-javascript
function htmlDecode(input){
  var e = document.createElement('div');
  e.innerHTML = input;
  return e.childNodes[0].nodeValue;
}

//Initial data loading
function loadData ()
{
	$('#lds_edit_title').val(htmlDecode(initLdS.title));
	$('#granularity_input').val(initLdS.granularity);
	$('#completeness_input').val(initLdS.completeness);
	$('#lds_edit_guid').val(initLdS.guid);
	
	generateTagList ('init');
	
	//Build the tabs
	for (var i in documents)
		$('#lds_edit_tabs li.lds_newtab').before ('<li class="lds_tab"><span class="lds_tab_title">' + documents[i].title + '</span> <span class="lds_tab_options">▲</span></li>');
	
	//Highlight the first one and fill the ckeditor
	$('#lds_edit_tabs li.lds_tab:first').addClass('current');
	editor.setData (documents[0].body);
		
	initSliders ();
	initTags ();
	
	//Generate a before edit checksum
	beforeEditChecksum = calcChecksum ();
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
	//TODO $('#edit_lds').submit();
}

function inaction_trigger ()
{
	timer = setTimeout ('autosave_and_exit()', 1000 * edit_timeout);

	$('input').change (function ()
	{
		clearTimeout (timer);
	});

	//We're capturing keypresses here cos it's possible that the user spends a long time in the field.
	editor.on('instanceReady', function ()
	{
		editor.document.on ('keyup', function ()
		//CKEDITOR.instances['description'].document.on ('keyup', function ()
		{
			alert ('ei');
			clearTimeout (timer);
		});
	});
}

//We need to free the LdS resource for editing on any way the user exits the editing page
function free_lds ()
{
	$(window).unload (function ()
	{
		if ($('#lds_edit_guid').val() != '0')
		{
			//TODO: Add some visual feedback for the time the ajax request is made? (e.g. "Saving changes..." or "Freeing resource...")
			$.ajax({
		        type:	'POST',
		        url:	baseurl + 'action/lds/ping_editing',
		        data:	{entity_guid:$('#lds_edit_guid').val(), editing: false}, 
		        async:	false,
		        success: function(msg)
		        {
		        	//alert (msg);
		        }
			});
		}
	});
}

function initDocName ()
{
	$('#lds_edit_title').focus (function()
	{
		//If we haven't changed the title of the 1st document yet,
		//the LdS title will be the document title as well.
		changeDocTitle = (documents[0].title == t9n.untitledDoc);
		$affectedTab = $('#lds_edit_tabs li.lds_tab:eq(0)').find('.lds_tab_title');
	});
	
	$('#lds_edit_title').keyup (function()
	{
		if (changeDocTitle)
		{
			if ($.trim($(this).val()).length > 0)
			{
				documents[0].title = $(this).val();
				$affectedTab.text(documents[0].title);
			}
			else
			{
				documents[0].title = t9n.untitledDoc;
				$affectedTab.text(documents[0].title);
			}
		}
	});
}

$(document).ready(function()
{	
	initCKED ();
	initDocName ();
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
	
	//Save action
	$('#lds_edit_save').click (function () {
		ajax_submit (false);
		return false;
	});
	
	//Save and exit action
	$('#lds_edit_save_exit').click (function () {
		ajax_submit (true);
		return false;
	});
	
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
	
	//Before exit warning.
	//Doesn't really work w/firebug enabled. Will always warn.
	window.onbeforeunload = function ()
	{
		documents[currentTab].body = editor.getData();
		
		if (beforeEditChecksum != calcChecksum ())
			return t9n.confirmExit;
	};
});