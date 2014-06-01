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

    lds_submit_click = false;

    /*
    var height_correction = function () {
        $('#layout_canvas').css('min-height',
            window.innerHeight -
                $('#ldshake_topbar').outerHeight() -
                $('#layout_footer').outerHeight() -
                $('#footer_ending').outerHeight());
    }


    height_correction();
    $(window).resize(height_correction);
    */

    //new project implementation

    $('.lds_popup input[type="button"]').click(function() {
        $('.lds_popup .busy').show();
    });

    $('.project_implement_action').click(function (event) {
        event.preventDefault();
        //event.stopPropagation();

        lds_newprojectimplementation_guid = $(this).attr("project_guid");

        $('#implement_project_popup_close').click(function (e) {
            e.preventDefault();
            e.stopPropagation();
            $('#implement_project').fadeOut(200);
        });
        $('#implement_project').fadeToggle(200);
        $('input[name=new_projectimplementation_title]')
            .keypress(function(e) {
                if (e.keyCode == '13') {
                    e.preventDefault();
                    $('#projectimplementation_submit').click();
                }
            })
            .focus();
    });

    $("#projectimplementation_submit").click(function (event) {
        var submitData =
        {
            guid: lds_newprojectimplementation_guid,
            title: $('input[name=new_projectimplementation_title]').val()
        };

        if(submitData.title.length == 0)
            $('#projectimplementation_submit_incomplete').show();
        else {
            if(!lds_submit_click) {
                lds_submit_click = true;
                $.post (baseurl + "action/lds/projects/implement", submitData, function(data) {
                    data = parseInt(data, 10);
                    window.location = baseurl + 'pg/lds/project_implementation/'+data;
                });
            }
        }
    });

    $(".lds_select_implement_action").click(function (event) {
        event.preventDefault();
        window.implement_lds = $(this).attr('lds');
        $("#editimplementation_popup").fadeToggle(200);
    });

    $("#editimplementation_popup a").click(function (event) {
        event.preventDefault();
        var editor = $(this).attr('href');
        window.location = editor+window.implement_lds;
    });

    $('#filter_by_design').change(function (){
        var guid = $(this).val();
        window.location = baseurl + 'pg/lds/implementations/design/'+guid;
    });

    $('#implementlds_submit').click(function (){
        var submitData =
        {
            lds_id: $('#implement_popup input[name=lds_id]').val(),
            course_id: $('input:radio[name=course]:checked').val(),
            vle_id: $('input:radio[name=course]:checked').attr('vle_id'),
            vle_name: $('input:radio[name=course]:checked').attr('vle_name'),
            course_name: $('input:radio[name=course]:checked').attr('course_name'),
            title: $('input[name=new_implementation_title]').val()
        };

        if(submitData.title.length == 0 || !submitData.vle_id)
            $('#impl_submit_incomplete').show();
        else
            $.post (baseurl + "action/lds/implement", submitData, function(data) {
                data = parseInt(data, 10);
                if(data == -2)
                    window.location = baseurl + 'pg/lds/';
                else
                    window.location = baseurl + 'pg/lds/implementeditor/'+data;
            });
    });

    $('.lds_implement_action').click(function (){
        $('#implement_popup input[name=lds_id]').val($(this).attr('lds_id'));
        $('#implement_popup').fadeToggle(200);
        $('input[name=new_implementation_title]')
            .keypress(function(e) {
                if (e.keyCode == '13') {
                    $('#implementlds_submit').click();
                }
            })
            .focus();
    });

    $("input[name='lds_select']").change(function () {
        if($("input[name='lds_select']:checked").length != 1) {
            $("#duplicate_design").attr('disabled','disabled');
            $("#duplicate_implementation").attr('disabled','disabled');
            $("#implementations_by_design").attr('disabled','disabled');
            $("#view_design").attr('disabled','disabled');
        }
        else {
            //$("#implement_project").removeAttr('disabled');
            $("#duplicate_design").removeAttr('disabled');
            $("#implementations_by_design").removeAttr('disabled');
            $("#duplicate_implementation").removeAttr('disabled');
            $("#view_design").removeAttr('disabled');
        }
    });

    $("#implementations_by_design").click(function (event) {
        var lds = $("input[name='lds_select']:checked").val();
        window.location = baseurl + 'pg/lds/implementations/design/' + lds;
    });

    $("#cloneimplementation_submit").click(function (event) {
        var submitData =
        {
            guid: $("input[name='lds_select']:checked").val(),
            title: $('input[name=new_implementation_title]').val()
        };

        if(submitData.title.length == 0)
            $('#cloneimplementation_submit_incomplete').show();
        else {
            if(!lds_submit_click) {
                lds_submit_click = true;
                $.post (baseurl + "action/lds/cloneimplementation", submitData, function(data) {
                    window.location = baseurl + 'pg/lds/implementations';
                });
            }
        }
    });

    $('#duplicate_implementation').click(function (event) {
        $('#cloneimplementation_popup').fadeToggle(200);
        $('input[name=new_implementation_title]')
            .keypress(function(e) {
                if (e.keyCode == '13') {
                    $('#cloneimplementation_submit').click();
                }
            })
            .focus();
    });

    $("#view_design").click(function (event) {
        var implementation = $("input[name='lds_select']:checked").val();
        var lds = $("input#imp_design_" + implementation).val();
        window.location = baseurl + 'pg/lds/vieweditor/' + lds;
    });

    $('#clonelds_submit').click(function (){
        var submitData =
        {
            guid: $("input[name='lds_select']:checked").val(),
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

    $('.lds_close_popup').click(function () {
        $('#clonelds_popup, .lds_popup').fadeOut(200);
    });

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

    $('a.lds-browse-show-tags').click(function(e){
        e.preventDefault();
        var tagclass = $(this).attr('category');
        $('ul.'+tagclass+' li.lds-browse-non-used-tags').toggleClass('lds-browse-non-used-tags-hide');

        if($('ul.'+tagclass+' li.lds-browse-non-used-tags').hasClass('lds-browse-non-used-tags-hide'))
            $(this).text(t9n.showTags);
        else
            $(this).text(t9n.hideTags);

    });
});