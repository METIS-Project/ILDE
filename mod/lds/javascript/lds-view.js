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
	$('#lds_action_publish,#lds_action_republish').click(function (){
		$('.publishbutton').css('visibility','hidden');
		$('.lds_loading').show();
		$.get(baseurl + "action/lds/publish", {doc:$(this).attr('data-guid'),action:'publish'}, function (data)
		{
			if (data.substring(0, 2) == 'ok') {
				$('.lds_publish_url').val(data.substring(3));
                $('.lds_publish_embed').val('<iframe height="600px" width="100%" frameborder="1" src="' + data.substring(3) + '"></iframe>');
            }
			
			$('#lds_publish_wrapper,#lds_republish_wrapper').hide();
			$('#lds_unpublish_wrapper').show();
			$('.publishbutton').css('visibility','visible');
			$('.lds_loading').hide();
		});
		return false;
	});
	
	
	$('#lds_action_unpublish,#lds_action_unpublish2').click(function (){
		if (confirm ('Are you sure you want to unpublish this document?\nAll the external links to the document will be broken!'))
		{
			$('.publishbutton').css('visibility','hidden');
			$('.lds_loading').show();
			$.get(baseurl + "action/lds/publish", {doc:$(this).attr('data-guid'),action:'unpublish'}, function (data)
			{
				$('.publishbutton').css('visibility','visible');
				$('.lds_loading').hide();
				$('#lds_unpublish_wrapper').hide();
				$('#lds_republish_wrapper').hide();
				$('#lds_publish_wrapper').show();
			});
		}
		return false;
	});
	
	$('#the_lds').dblclick(function ()
	{
		window.location = baseurl + 'pg/lds/edit/' + $('#lds_edit_guid').val() + '/';
	});
});