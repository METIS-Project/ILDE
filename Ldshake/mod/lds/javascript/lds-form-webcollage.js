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

//Controls the time the user has been idle. Might close the form i order to let other users edit the LdS
var activity;
var first_save;
var hash = '0';
var timer = null;
var ping_interval = 30;
var edit_timeout = 600;

//tells if the Ctrl key is pressed
var isCtrl = false; 
var lastKey = '';

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
	//documents[currentTab].body = editor.getData();
	var submitData = null;
	
	if(editorType == 'webcollage')
	{
		top.window.document.getElementById('lds_editor_body').contentWindow.TableGenerator.generateSummary();
		top.window.document.getElementById('lds_editor_body').contentWindow.Loader.save_ldshake();
		
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
			webcollage_id: webcollage_id,
			editorType: editorType,
			summary: encodeURIComponent(top.window.document.getElementById('lds_editor_body').contentWindow.document.getElementById('SummaryTabContent').innerHTML)
		};
	}
	
	if(editorType == 'exe')
	{
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
			webcollage_id: webcollage_id,
			editorType: editorType
		};
	}
	
	$.post (baseurl + "action/lds/save_editor", submitData, function (data)
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
			
			//Now we save the sharing options
			if(first_save)
			{
				ping_editing();
				first_save = false;
			}
			
			if (sharingOptionsPendingToSave)
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
			max: 8,
			step: 1,
			slide: function (event, ui) {
				$('#completeness_input').val( ui.value );
			}
	});
	$('#completeness_input').val($('#completeness_slider').slider('value'));
}

function initTags ()
{
	$("#lds_edit_tags_input_discipline").autoSuggest(mytags.discipline, {asHtmlID: "discipline", preFill: initLdS.discipline, startText: "Enter tags here", keyDelay: 0, neverSubmit: true});
	$("#lds_edit_tags_input_pedagogical_approach").autoSuggest(mytags.pedagogical_approach, {asHtmlID: "pedagogical_approach", preFill: initLdS.pedagogical_approach, startText: "Enter tags here", keyDelay: 0, neverSubmit: true});
	$("#lds_edit_tags_input_tags").autoSuggest(mytags.tags, {asHtmlID: "tags", preFill: initLdS.tags, startText: "Enter tags here", keyDelay: 0, neverSubmit: true});

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
	$.post (baseurl + 'action/lds/ping_editing_editor', {entity_guid:$('#lds_edit_guid').val(), editing: true}, function (data)
	{
		setTimeout ('ping_editing()', 1000 * ping_interval);
	});
}

function autosave_and_exit ()
{
	//TODO $('#edit_lds').submit();
}

function check_activity()
{
	if (activity == false)
	{
		ajax_submit (true);
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
		if ($('#lds_edit_guid').val() != '0')
		{
			//TODO: Add some visual feedback for the time the ajax request is made? (e.g. "Saving changes..." or "Freeing resource...")
			$.ajax({
		        type:	'POST',
		        url:	baseurl + 'action/lds/ping_editing_editor',
		        data:	{entity_guid:$('#lds_edit_guid').val(), editing: false, webcollage_id: webcollage_id, editorType: editorType}, 
		        async:	false,
		        success: function(msg)
		        {
		        	//alert (msg);
		        }
			});
		}
	});
}

$(document).ready(function()
{
	top.window.onclick = active;
	top.window.onkeypress = active;
	
	active();
	check_activity();

	
	loadData();
	
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
	
	//Save action
	$('#lds_edit_save').click (function () {
		ajax_submit (false);
		return false;
	});
	
	//Save and exit action
	$('#lds_edit_save_exit_editor').click (function () {
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
//	window.onbeforeunload = function ()
//	{
//		documents[currentTab].body = editor.getData();
//		
//		if (beforeEditChecksum != calcChecksum ())
//			return "You have unsaved data. Are you sure you want to leave the editor?";
//	};
});