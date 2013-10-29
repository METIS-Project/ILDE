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

    function resizeViewport () {
        $('#footer_ending').css('display', 'none');
        var top = $('#the_lds').offset().top;
        var height = $(window).height();
        var commentHeight = $('#layout_footer').outerHeight();

        var h = Math.max(400, height - top - commentHeight);

        h = Math.max(h-2-20, $('#the_lds').contents().find('body').height());
        $('#the_lds').height(h);
        //$('#the_lds').height($('#the_lds').contents().find('body').height());
        $('#the_lds').width($('#one_column').width()-2);
    }

    resizeViewport();

    $('#the_lds').load(resizeViewport);
    $(window).resize(resizeViewport);


    $('#clonelds_submit').click(function (){
        var submitData =
        {
            guid: $("input#lds_edit_guid").val(),
            title: $('input[name=new_lds_title]').val()
        };

        if(submitData.title.length == 0)
            $('#clonelds_submit_incomplete').show();
        else {
            if(!lds_submit_click) {
                lds_submit_click = true;
                $.post (baseurl + "action/lds/clone", submitData, function(data) {
                    window.location = baseurl + 'pg/lds/vieweditor/' + data;
                });
            }
        }
    });

    $('#manage_license').click(function (event) {

        $('#license_popup_close').click(function() {
            $('#license_popup').fadeOut(200);
        });

        $('.license_cc_select_item[license_id="'+lds_license+'"]').css("background-color","lightgrey");
        $('.license_cc_select_item[license_id="'+lds_license+'"]').css("border","2px solid grey");
        $('.license_cc_select_item[license_id="'+lds_license+'"]').css("border-radius","6px");

        var cc_items = [];
        cc_items[0] = [];
        cc_items[1] = ['license_item_cc','license_item_cc_by'];
        cc_items[2] = ['license_item_cc','license_item_cc_by','license_item_cc_nd'];
        cc_items[3] = ['license_item_cc','license_item_cc_by','license_item_cc_sa'];
        cc_items[4] = ['license_item_cc','license_item_cc_by','license_item_cc_nc'];
        cc_items[5] = ['license_item_cc','license_item_cc_by','license_item_cc_nc','license_item_cc_nd'];
        cc_items[6] = ['license_item_cc','license_item_cc_by','license_item_cc_nc','license_item_cc_sa'];

        for(cc_item in cc_items[lds_license]) {
            $('#'+cc_items[lds_license][cc_item]).show();
        }

        $('#license_popup').fadeToggle(200);
        $('.license_cc_select_item').click(function() {
            var $item = $(this);
            var license = $(this).attr("license_id");

            var submitData =
            {
                guid: $("input#lds_edit_guid").val(),
                license_id: license
            };

            $.post (baseurl + "action/lds/manage_license", submitData, function(data) {
                if(data.ok) {
                    $('.cc_info_icon').hide();
                    lds_license = license;
                    $('.license_cc_select_item').css("background-color","");
                    $('.license_cc_select_item').css("border","");
                    $('.license_cc_select_item').css("border-radius","");

                    $item.css("background-color","lightgrey");
                    $item.css("border","2px solid grey");
                    $item.css("border-radius","6px");

                    $('.cc_info_icon').hide();

                    for(cc_item in cc_items[lds_license]) {
                        $('#'+cc_items[lds_license][cc_item]).show();
                    }

                    $('.license_banner').hide();
                    $('.license_banner[license_id="'+license+'"]').css("display", "inline");
                }
            });

        });
    });

    $('#duplicate_design').click(function (event) {
        $('#clonelds_popup').fadeToggle(200);
        $('input[name=new_lds_title]')
            .keypress(function(e) {
                if (e.keyCode == '13') {
                    $('#clonelds_submit').click();
                }
            })
            .focus();
    });

    $('#ldsclone_popup_close').click(function (event) {
        $('#clonelds_popup').fadeToggle(200);
    });

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
		if (confirm (t9n.unpublishConfirm1 + '\n' + t9n.unpublishConfirm2))
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