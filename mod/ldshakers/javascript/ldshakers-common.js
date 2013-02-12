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

function checkboxes ()
{
	$('#ldshakers_select_all').change (function ()
	{
		$('.ldshakers_select_one').attr('checked', $(this).is(':checked'));
		$(this).removeClass('some_selected');
	});
	
	$('.ldshakers_select_one').change (function ()
	{
		if ($('.ldshakers_select_one:checked').length == 0)
			$('#ldshakers_select_all').removeClass('some_selected').attr('checked', false);
		else if ($('.ldshakers_select_one:not(:checked)').length == 0)
			$('#ldshakers_select_all').removeClass('some_selected').attr('checked', true);
		else
			$('#ldshakers_select_all').addClass('some_selected').attr('checked', true);
	});
}

function createGroup ()
{
	$('#ldshakers_create_group').click (function ()
	{
		$('#shade').show();
		$('#ldshakers_add_group_popup').show();
		$('#new_group_name').focus();
		return false;
	});
	
	$('.close_popup').click (function ()
	{
		$('#shade').hide();
		$('#ldshakers_add_group_popup').hide();
		return false;
	});

    /*
	$('#new_group_name').keyup (function (ev)
	{
		if (ev.which == 13)
			$('#ldshakers_add_group_form').submit();
	});
	*/
	
	$('#ldshakers_add_group_form').submit (function ()
	{
		$(this).find('.errnote').slideUp('fast');
		$form = $(this);
		$.post (baseurl + "action/ldshakers/addgroup", {name: $('#new_group_name').val()}, function (data)
		{
			if (data == 'ok')
			{
				window.location.reload();
			}
			else
			{
				$form.find('.errnote').slideDown('fast');
			}
		});
		return false;
	});
}

function deleteGroup ()
{
	$('#delete_group').click (function ()
	{
		if (confirm ("Are you sure you want to delete this group?\n\nNOTE: Deleting this group will not affect the sharing options of any of your LdS."))
		{
			$.post (baseurl + "action/ldshakers/deletegroup", {id: $(this).attr('data-id')}, function (data)
			{
				if (data == 'ok')
				{
					window.location = baseurl + "pg/ldshakers/";
				}
			});
		}
		
		return false;
	});
}

function addToGroup ()
{
	$('#add_to_group').click (function ()
	{
		var sel = $('.ldshakers_select_one:checked').length;
		
		if (sel == 0)
		{
			$('#ldshakers_add_to_list ul').hide();
			$('.ldshakers_popup_unavailable').show();
			$('.ldshakers_popup_info').hide();
		}
		else
		{
			$('.ldshakers_popup_info strong').text(sel);
			$('#ldshakers_add_to_list ul').show();
			$('.ldshakers_popup_unavailable').hide();
			$('.ldshakers_popup_info').show();
		}
		
		$('#ldshakers_add_to_list')
			.css('top', $(this).offset().top + 27)
			.css('left', $(this).offset().left)
			.toggle ();
		
		return false;
	});
	
	$(document).click(function()
	{
		$('#ldshakers_add_to_list').hide();
	});
	
	$('.add_to_group_link').click (function ()
	{
		var ids = '';
		$('.ldshakers_select_one:checked').each (function ()
		{
			ids += $(this).val() + ',';
		});
		
		$.post (baseurl + "action/ldshakers/addtogroup", {group: $(this).attr('data-id'), ids:ids}, function (data)
		{
			if (data == 'ok')
			{
				window.location.reload();
			}
		});
		
		return false;
	});
}

function removeFromGroup ()
{
	$('#remove_from_group').click (function ()
	{
		var ids = '';
		$('.ldshakers_select_one:checked').each (function ()
		{
			ids += $(this).val() + ',';
		});
		
		$.post (baseurl + "action/ldshakers/removefromgroup", {group: $(this).attr('data-id'), ids:ids}, function (data)
		{
			if (data == 'ok')
			{
				window.location.reload();
			}
		});
		
		return false;
	});
}

$(document).ready(function()
{
	checkboxes();
	createGroup ();
	deleteGroup ();
	addToGroup ();
	removeFromGroup ();
});