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

$(document).ready(function()
{
	//Delete LdS
	$('.lds_action_delete').click (function ()
	{
		if (confirm ("Are you sure you want to delete the following LdS?\n\n" + $(this).attr('data-title')))
		{
			$(this).hide();
			$elem = $(this).parents('.lds_list_element');
			$elem.find('.lds_loading').show();
			
			$.get(baseurl + "action/lds/delete", {lds:$(this).attr('data-id')}, function ()
			{
				$elem.slideUp('fast');
			});
		}
		return false;
	});
	
	$('#lds_select_all').change (function ()
	{
		$('.lds_select_one').attr('checked', $(this).is(':checked'));
		$(this).removeClass('some_selected');
	});
	
	$('.lds_select_one').change (function ()
	{
		if ($('.lds_select_one:checked').length == 0)
			$('#lds_select_all').removeClass('some_selected').attr('checked', false);
		else if ($('.lds_select_one:not(:checked)').length == 0)
			$('#lds_select_all').removeClass('some_selected').attr('checked', true);
		else
			$('#lds_select_all').addClass('some_selected').attr('checked', true);
	});
	
	$('.lds_list_element').mouseenter (function ()
	{
		$(this).find('.lds_edit_action').css('visibility','visible');
		
		if ($(this).hasClass('lds_locked'))
		{
			$(this).find('.lds_people').hide();
			$(this).find('.lds_editing_by').show();
		}
	})
	.mouseleave (function ()
	{
		$(this).find('.lds_edit_action').css('visibility','hidden');
		
		if ($(this).hasClass('lds_locked'))
		{
			$(this).find('.lds_people').show();
			$(this).find('.lds_editing_by').hide();
		}
	});
	
	$('#trash_some').click (function ()
	{
		var ids = '';
		$('.lds_select_one:checked').each (function ()
		{
			ids += $(this).val() + ',';
		});
		
		if (ids.length > 0)
		{
			$.get(baseurl + "action/lds/delete", {lds:ids}, function ()
			{
				window.location.reload();
			});
		}
		
		return false;
	});
	
	$('#untrash_some').click (function ()
	{
		var ids = '';
		$('.lds_select_one:checked').each (function ()
		{
			ids += $(this).val() + ',';
		});
		
		if (ids.length > 0)
		{
			$.get(baseurl + "action/lds/recover", {lds:ids}, function ()
			{
				window.location.reload();
			});
		}
		
		return false;
	});
});