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

//This temporally stores all the viewers that were in the list, but have are taken out because the user
//checks the "all can read" checkbox. By storing the ids here, we make this action undoable for the session.
var tempViewersList = new Array();

//If sharing options are edited but the LdS is not saved in the DB yet, we'll enable this flag.
var sharingOptionsPendingToSave = false;

//Returns the contact if found, or null otherguais.
function findContact (guid, findin)
{
	for (var i in friends[findin])
		if (friends[findin][i].guid == guid)
			return friends[findin][i];

	return null;
}

//Moves a contact from one sub-array of "friends" to another.
function moveContact (guid, from, to)
{
	for (var i in friends[from])
	{
		if (friends[from][i].guid == guid)
		{
            var obj = friends[from][i];
			friends[from].splice(i,1);
			
			friends[to].push(obj);
		}
	}
}

function getArrayIds (type)
{
	var ids = new Array ();
	for (var i in type)
		ids.push(type[i].guid);
	
	return ids;
}

//Does what its name claims to do.
function addContactToList (guid, type)
{
	var contact = findContact (guid, 'available');
	
	if (contact != null)
	{
		var licontents =
			'<img src="' + contact.pic + '" />' +
			'<span class="contactinfo" data-guid="' + contact.guid + '">' +
			contact.name +
			'</span>' +
			'<a href="#" class="remove_contributor" data-guid="' + contact.guid + '">×</a>' +
			'<span class="share_type_switch"><a href="#" class="can_view_option' + ((type == 'viewers') ? ' pressed' : '') + '">' + $('#copy_viewers').val() + '</a>' + 
			'<a href="#" class="can_edit_option' + ((type == 'editors') ? ' pressed' : '') + '">' + $('#copy_editors').val() + '</a></span>';
		
		moveContact (guid, 'available', type);
		
		$('#' + type).val('');
		var id = $('#dropdown_' + type).empty().hide();
		$('<li/>',
				{  
			html: licontents
				})
				.addClass (type)
				.appendTo($('#added_contacts'));
	}
}

//Adds to the contact list all the members of a given group which were not already present in it.
function addGroupToList (name, type)
{
  	for (var i in groups)
		if (groups[i].name == name)
			addContactToList (groups[i].guid, type);

}

function initContactList (type)
{
	for (var i in friends[type])
	{
		var contact = friends[type][i];
		
		if (am_i_starter)
		{
			var licontents =
				'<img src="' + contact.pic + '" />' +
				'<span class="contactinfo" data-guid="' + contact.guid + '">' +
				contact.name +
				'</span>' +
				'<a href="#" class="remove_contributor" data-guid="' + contact.guid + '">×</a>' +
				'<span class="share_type_switch"><a href="#" class="can_view_option' + ((type == 'viewers') ? ' pressed' : '') + '">' + $('#copy_viewers').val() + '</a>' + 
				'<a href="#" class="can_edit_option' + ((type == 'editors') ? ' pressed' : '') + '">' + $('#copy_editors').val() + '</a></span>';
		}
		else
		{
			var licontents =
				'<img src="' + contact.pic + '" />' +
				'<span class="contactinfo" data-guid="' + contact.guid + '">' +
				contact.name +
				'</span>' +
				'<span class="static_label">' + ((type == 'viewers') ? $('#copy_viewers').val() : $('#copy_editors').val()) + '</span>';
		}
		
		$('<li/>',
		{  
			html: licontents
		})
		.addClass (type)
		.appendTo($('#added_contacts'));
	}
}

function updateSharingLabel ()
{
	var add = am_i_starter ? 0 : 1;
	//Update of the edit label
	if (friends['editors'].length == 0)
	{
		$('#lds_edit_share_info_write').find('.one').show();
		$('#lds_edit_share_info_write').find('.more').hide();
	}
	else
	{
		$('#lds_edit_share_info_write').find('.one').hide();
		$('#lds_edit_share_info_write').find('.more').show().find('span').text(friends['editors'].length + add);
	}
	
	//Update of the read label
	if (allCanView)
	{
		$('#lds_edit_share_info_read').find('.one').hide();
		$('#lds_edit_share_info_read').find('.more').hide();
		$('#lds_edit_share_info_read').find('.all').show();
	}
	else
	{
		if (friends['viewers'].length == 0)
		{
			$('#lds_edit_share_info_read').find('.one').show();
			$('#lds_edit_share_info_read').find('.more').hide();
			$('#lds_edit_share_info_read').find('.all').hide();
		}
		else
		{
			$('#lds_edit_share_info_read').find('.one').hide();
			$('#lds_edit_share_info_read').find('.all').hide();
			$('#lds_edit_share_info_read').find('.more').show().find('span').text(friends['viewers'].length +add);
		}
	}
}

function saveSharingOptions (callback)
{
	//If we've already saved the LdS, we save the sharing data
	if ($('#lds_edit_guid').val() != '0')
	{
		var submitData = 
		{
			guid: $('#lds_edit_guid').val(),
			editors: getArrayIds(friends['editors']).join(','),
			viewers: getArrayIds(friends['viewers']).join(','),
			allCanView: (allCanView) ? '1' : '0' 
		};
		
		$('#lds_edit_buttons').addClass('busy');
		$.post (baseurl + "action/lds/share", submitData, function (data)
		{
			$('#lds_edit_buttons').removeClass('busy');
			
			if (callback != null) callback();
		});
	}
	else
	{
		//We don't store the sharing options. We'll postpone the job for the first "save".
		sharingOptionsPendingToSave = true;
	}
}

function initSharingOptions ()
{
	initContactList('editors');
	initContactList('viewers');
	if (allCanView)
	{
		$('#add_viewer_wrapper').css('visibility','hidden');
		$('#read_all').attr('checked','checked');
	}
	else
	{
		$('#add_viewer_wrapper').css('visibility','visible');
		$('#read_all').removeAttr('checked');
	}
	updateSharingLabel();

    if(new_lds)
        sharingOptionsPendingToSave = true;

}

$(document).ready(function()
{	
	initSharingOptions ();
	
	$('#lds_share_button').click (function ()
	{
		$('#lds_single_share_popup').toggle();
		$('#shade').toggle();
	});
	
	$('#lds_single_share_popup_close').click (function ()
	{
		$('#lds_single_share_popup').toggle();
		$('#shade').toggle();
		if ($(this).hasClass('edit'))
		{
			updateSharingLabel();
			saveSharingOptions();
		}
	});
	
	//For each field with class "usersuggestbox" we'll create a hidden dropdown.
	$('.usersuggestbox').each (function (k, v)
	{
		$('<ul/>',
		{  
			id: 'dropdown_' + $(this).attr('id'),  
		})
		.addClass('suggest_dropdown')
		.addClass('hidden')
		.width($(this).width())
		.insertAfter($(this));
	});
	
	$('.can_view_option').live('click', function ()
	{
		if (!allCanView)
		{
			$(this).addClass('pressed')
			.closest('.share_type_switch').find('.can_edit_option').removeClass('pressed');
			
			$(this).closest('li').removeClass('editors').addClass('viewers');
			
			guid = $(this).closest('li').find('.contactinfo').attr('data-guid');
			moveContact (guid, 'editors', 'viewers');
		}
		else
		{
			$('#read_all_wrapper').effect("shake", { times:3, distance: 5 }, 50);
		}
		
		return false;
	});
	
	$('.can_edit_option').live('click', function ()
	{
		$(this).addClass('pressed')
		.closest('.share_type_switch').find('.can_view_option').removeClass('pressed');
		
		$(this).closest('li').removeClass('viewers').addClass('editors');
		
		guid = $(this).closest('li').find('.contactinfo').attr('data-guid');
		moveContact (guid, 'viewers', 'editors');
		
		return false;
	});
	
	$('#read_all').click(function ()
	{
		if ($(this).is(':checked'))
		{
			//We store the viewers guids to a temporary list, so we'll make the action undoable
			//Also, we move them to the available list
			$('li.viewers').each (function () {
				var id = $(this).find('.contactinfo').attr('data-guid');
				tempViewersList.push(id);
				moveContact (id, 'viewers', 'available');
			});
			
			allCanView = true;
			$('li.viewers').slideUp('fast', function (){$('li.viewers').remove();});
			$('#add_viewer_wrapper').css('visibility','hidden');
		}
		else
		{
			//We put back the temparary stored viewers to the list if they haven't been added as editors meanwhile
			for (var i in tempViewersList)
			{
				var contact = findContact(tempViewersList[i],'available');
				if (contact != null)
				{
					addContactToList (contact.guid, 'viewers');
				}
			}
			
			tempViewersList = new Array();
			
			allCanView = false;
			$('#add_viewer_wrapper').css('visibility','visible');
		}
	});
	
	$('.usersuggestbox').keydown (function (event)
	{
		//Up
		if (event.keyCode == '38')
		{
			var pos = $('#dropdown_' + $(this).attr('id') + '>li.dropdown_selected').prevAll('li').length;

			if (pos > 0)
			{
				$('#dropdown_' + $(this).attr('id') + '>li').removeClass ('dropdown_selected');
				$('#dropdown_' + $(this).attr('id') + '>li:eq(' + (pos - 1) + ')').addClass ('dropdown_selected');
			}
		}
		//Down
		else if (event.keyCode == '40')
		{
			var pos = $('#dropdown_' + $(this).attr('id') + '>li.dropdown_selected').prevAll('li').length;
			var count = $('#dropdown_' + $(this).attr('id') + '>li').length;
			
			if (pos < count - 1)
			{
				$('#dropdown_' + $(this).attr('id') + '>li').removeClass ('dropdown_selected');
				$('#dropdown_' + $(this).attr('id') + '>li:eq(' + (pos + 1) + ')').addClass ('dropdown_selected');
			}
		}
	});
	
	$('.suggest_dropdown>li').live('mouseenter', function ()
	{
		var id = $(this).closest('.suggest_dropdown').attr('id');
		var pos = $(this).prevAll('li').length;
		$('#' + id + '>li').removeClass ('dropdown_selected');
		$('#' + id + '>li:eq(' + (pos) + ')').addClass ('dropdown_selected');
	});
	
	
	//Select a result
	$('.suggest_dropdown>li').live('click', function (e)
	{
		e.stopPropagation();
		

			var contactid = $(this).find('.contactinfo').attr('data-guid');
			var type = $(this).closest('.suggest_dropdown').attr('id').substring(9);

			addContactToList (contactid, type);

	});
	
	$(document).click(function(e)
	{
		$('.suggest_dropdown').hide();
	});
	
	$('.remove_contributor').live ('click', function ()
	{
		var type = $(this).closest('li').hasClass('editors') ? 'editors' : 'viewers';
		var contactid = $(this).attr('data-guid');
		
		moveContact (contactid, type, 'available');
		
		$(this).closest('li').remove();
		
		return false;
	});
	
	$('.usersuggestbox').keypress (function (event)
	{
		//Enter
		if (event.keyCode == '13')
		{
			if ($('#dropdown_' + $(this).attr('id') + '>li.dropdown_selected').length)
			{
				if ($(this).nextAll('.suggest_dropdown').find('li.dropdown_selected').hasClass('group'))
				{
					var type = $(this).attr('id');
					var groupname = $(this).nextAll('.suggest_dropdown').find('li.dropdown_selected').find('.contactinfo').attr('data-group');

					addGroupToList (groupname, type);
				}
				else
				{
					var type = $(this).attr('id');
					var contactid = $(this).nextAll('.suggest_dropdown').find('li.dropdown_selected').find('.contactinfo').attr('data-guid');
					
					addContactToList (contactid, type);
				}
			}
			return false;
		}
	});
	
	$('.usersuggestbox').keyup (function (event)
	{
		if (event.keyCode != '37' &&event.keyCode != '38' &&event.keyCode != '39' &&event.keyCode != '40')
		{
			var value = $(this).val();
			
			//Empty the dropdown
			$('#dropdown_' + $(this).attr('id')).empty();
			
			//If we have something written
			if ($.trim(value).length > 0)
			{
				$('#dropdown_' + $(this).attr('id')).css('top', $(this).offset().top + 30);
				$('#dropdown_' + $(this).attr('id')).css('left', $(this).offset().left);
				$('#dropdown_' + $(this).attr('id')).show();
				
				//Controls the max number of results
				var num = 0;
                /*
				//For each of the groups
				for (var i in groups)
				{
					//if it matches the query
					var offset = groups[i].name.toLowerCase().indexOf(value.toLowerCase());
					if (offset > -1)
					{
						var licontents =
						'<img src="' + baseurl + 'mod/ldshakers/graphics/grouptiny.jpg" height="20" />' +
						'<span class="contactinfo" data-group="' + groups[i].name + '">' +
						groups[i].name.substring(0, offset) +
						'<strong>' +
						groups[i].name.substring(offset, offset + value.length) +
						'</strong>' +
						groups[i].name.substring(offset + value.length) +
						' (group)</span>';
						
						//Add a li item to the dropdown
						$('<li/>',
						{  
							//With the highlighted search query
							html: licontents
						})
						.addClass('group')
						.appendTo($('#dropdown_' + $(this).attr('id')));
						
						num++;
					}
					
					if (num == 6) break; 
				}*/
				
				//For each of the friends array
				for (var i in friends['available'])
				{
					//if it matches the query
					var offset = friends['available'][i].name.toLowerCase().indexOf(value.toLowerCase());
					if (offset > -1)
					{
						var licontents =
						'<img src="' + friends['available'][i].pic + '" height="20" />' +
						'<span class="contactinfo" data-guid="' + friends['available'][i].guid + '">' +
						friends['available'][i].name.substring(0, offset) +
						'<strong>' +
						friends['available'][i].name.substring(offset, offset + value.length) +
						'</strong>' +
						friends['available'][i].name.substring(offset + value.length) +
						'</span>';
						
						//Add a li item to the dropdown
						$('<li/>',
						{  
							//With the highlighted search query
							html: licontents
						})
						.appendTo($('#dropdown_' + $(this).attr('id')));
						num++;
					}
					
					if (num == 6) break; 
				}
				
				$('#dropdown_' + $(this).attr('id') + '>li:first').addClass('dropdown_selected');
				
				if (num == 0) $('#dropdown_' + $(this).attr('id')).hide();
			}
		}
		
		if ($.trim($(this).val()) == '') $('#dropdown_' + $(this).attr('id')).hide();
	});

	$('.usersuggestbox').click (function (e)
	{
		e.stopPropagation();
		return false;
	});
	
	$('.usersuggestbox').focus (function ()
	{
		var value = $(this).val();

		//If we have something written
		if ($.trim(value).length > 0)
			$('#dropdown_' + $(this).attr('id')).show();
	});
});