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
    document_resized_once = 0;
	function resizeViewport () {
		var top = $('#payload').offset().top;
        var height = image ? $(document).height() : $(window).height();
		var commentHeight = 0;//$('#comment_switcher').outerHeight();

        var lastItemBottom = $('#comment_switcher').offset().top + commentHeight;

		var h = Math.max(400, height - top - commentHeight + document_resized_once*commentHeight);
		if(!image || (image && lastItemBottom < height)) {
            $('#payload').height(h);
            $('#the_lds').height(h-2);
            $('#internal_iviewer').width($('#payload').width()-2);
            $('#internal_iviewer').height(h-2);
            //$('#the_lds').width($('#payload').width()-2);
            if(image) document_resized_once = 1;
        }
	}
	
	resizeViewport();
	
	$(window).resize(resizeViewport);
    $('#image_document_view').load(resizeViewport);
    $('#svg_document_view').load(resizeViewport);

    $(".lds_select_implement_action").click(function (event) {
        event.preventDefault();
        window.implement_lds = $(this).attr('lds');
        $("#editimplementation_popup").fadeToggle(200);
        $("#editimplementation_popup_close").click(function (){$("#editimplementation_popup").fadeOut(200);});
    });

    $("#editimplementation_popup a").click(function (event) {
        event.preventDefault();
        var editor = $(this).attr('href');
        window.location = editor+window.implement_lds;
    });


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

    $('#ldsclone_popup_close').click(function (event) {
        $('#clonelds_popup').fadeToggle(200);
    });

    $('#comment_switcher a').click (function () {
		$('#lds_info_wrapper').show ();
	});
	
	$('#lds_action_publish,#lds_action_republish').click(function (){
		$('.publishbutton').css('visibility','hidden');
		$('.lds_loading').show();
        var url_iseXe = iseXe ? "_editor" : "";
		$.get(baseurl + "action/lds/publish" + url_iseXe, {doc:$(this).attr('data-guid'),action:'publish'}, function (data)
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
            var url_iseXe = iseXe ? "_editor" : "";
			$.get(baseurl + "action/lds/publish" + url_iseXe, {doc:$(this).attr('data-guid'),action:'unpublish'}, function (data)
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

    $('form#editorfileupload').hide();
    $('#lds_import_button').click(function() {
        $('form#editorfileupload').show();
    });

});

var z_popup = 9000;
function project_popup_show(d){

    //check if the clicked element is the same where we binded the click event
    if(d.target != this)
        return false;
    //d = d.target;
    d = d.target;

    var popup_id = d.lds_guid;
    var tree_popup_random = Math.floor(Math.random()*1000000000);
    var popup_id_selector = "#tree_info_popup_shell_"+ popup_id + "_" + tree_popup_random + " ";
    var $popup, $popup_move;
    var tree_box_origin=this;
    var node = this.getBoundingClientRect();
    var X = this.getBoundingClientRect().left;
    var Y = this.getBoundingClientRect().top;
    var height = this.getBoundingClientRect().height - 32;
    var width = this.getBoundingClientRect().width - 32;
    var popup_width = 600;
    var popup_height = 400;
    var fadein_timing = 1;
    var popupX = X-100,
        popupY = Y+100;

    var transition_settings = "width "+fadein_timing+"s, height "+fadein_timing+"s, opacity "+fadein_timing+"s, top "+fadein_timing+"s, left "+fadein_timing+"s";
    transition_settings = "all 2.0s ease";

    $("#payload")
        //.empty()
        .append(project_lds_box);

    $popup = $("#tree_info_popup_shell_empty")
        .attr("id", "tree_info_popup_shell_"+ popup_id + "_"+ tree_popup_random)
        .css("z-index", z_popup++);

    $popup_move = $("#tree_info_popup_move_empty")
        .attr("id", "tree_info_popup_move_"+ popup_id + "_"+ tree_popup_random);

    if($(window).height() < Y + popup_height + 50)
        popupY = $(window).height() - popup_height - 50;

    if($(window).width() < X + popup_width + 50)
        popupX = $(window).height() - popup_width - 50;

    /*if(lds_guid == d.lds_guid)
        $popup.find(".tree_info_popup_control_button.diff").hide();
*/

    //$popup = $(popup_id_selector);
    //$popup_move= $(popup_id_selector + ".tree_info_popup_move");
    //$popup_move.show();

    $.post(
        baseurl+'action/lds/tree_lds_view',
        {
            guid: d.lds_guid,
            ref_id: d.lds_guid
        },
        function(data) {
            var $interal_viewer;
            var maximized= 0,
                minimized=0;
            var iframe_ready=false,
                iframe_diff=false;
            var tree_popup_zoom_level = 100*popup_width/957;
            var iframe_xsize,
                iframe_ysize;


            $popup.show();
            $(popup_id_selector+"#tree_info_popup").html(data.html);
            $interal_viewer = $(popup_id_selector+"#internal_iviewer");


            $popup.click(function(e) {
                    $popup.css("z-index", z_popup++);
                }
            );

            /*if(d.tags_html.length > 5)
                $interal_viewer.attr("height","79%");
                */

            var zoom_update = function(popup_frame_width) {
                tree_popup_zoom_level = 100*popup_frame_width/957;
                popup_box_zoom();
                if(iframe_ready)
                    popup_iframe_zoom();
            }

            var popup_iframe_zoom = function() {
                var iframe_tree_contents= $(popup_id_selector+"#internal_iviewer").contents().contents();
                var iframe_width = 957*tree_popup_zoom_level/100;

                if(!iframe_ready) {
                    //iframe_xsize = $(iframe_tree_contents).filter("html").outerWidth();
                    iframe_xsize = 880;
                    $(iframe_tree_contents).filter("html").css("width", iframe_xsize+"px");
                    iframe_ysize = $(iframe_tree_contents).filter("html").find("body").outerHeight();
                    $(iframe_tree_contents).filter("html").css("position", "relative");
                    $(iframe_tree_contents).css("transition","opacity "+fadein_timing+"s");
                    $(popup_id_selector+"#internal_iviewer").css("transition","opacity "+fadein_timing+"s");
                    setTimeout(function(){
                        $(iframe_tree_contents).css("transition",transition_settings);
                        $(popup_id_selector+"#internal_iviewer").css("transition",transition_settings);
                        $(iframe_tree_contents).filter("html").css("overflow-y","auto");
                    },fadein_timing*1000);

                    $(iframe_tree_contents).filter("html").find("body").click(function(e){
                        e.preventDefault();
                        $popup.click();
                    });

                    iframe_ready=true;
                }

                //$(popup_id_selector+"#internal_iviewer")
                //    .css("width", iframe_width+"px");

                //$(iframe_tree_contents).css("zoom",tree_popup_zoom_level+"%");
                var iframe_xmargin = iframe_xsize*(tree_popup_zoom_level/100 - 1)/2;
                var iframe_ymargin = iframe_ysize*(tree_popup_zoom_level/100 - 1)/2;

                $(iframe_tree_contents).css("left",iframe_xmargin+"px");
                $(iframe_tree_contents).css("top",iframe_ymargin+"px");
                $(iframe_tree_contents).filter("html").css("transform", "scale("+tree_popup_zoom_level/100+")");
                $(iframe_tree_contents).filter("html").css("-webkit-transform", "scale("+tree_popup_zoom_level/100+")");

                //$interal_viewer.css("transition","opacity 1s");
                //$interal_viewer.css("transition",transition_settings);
                $interal_viewer.css("opacity","1.0");

            }

            var popup_box_zoom = function() {
                $(popup_id_selector + "#tree_info_popup #content_area_user_title").css("zoom",tree_popup_zoom_level+"%");
                $(popup_id_selector + "#tree_info_popup > *").css("-webkit-user-select", "none");
                $(popup_id_selector + "#tree_info_popup > *").css("-moz-user-select", "none");
            }

            zoom_update(popup_width);
            $interal_viewer.load(popup_iframe_zoom);

            var popupXlast = 0;
            var popupYlast = 0;

            $(popup_id_selector + ".tree_info_popup_control_button.diff").click(function(e){
                iframe_ready=false;
                $interal_viewer.css("transition","opacity "+fadein_timing/3+"s");
                $interal_viewer.css("opacity","0.0");

                setTimeout(function(){
                    if(!iframe_diff) {
                        $interal_viewer.attr("src", baseurl+"pg/lds/view_iframe/"+data.doc+"/"+data.ref_doc+"/");
                    } else {
                        $interal_viewer.attr("src", baseurl+"pg/lds/view_iframe/"+data.doc+"/");
                    }
                    iframe_diff = !iframe_diff;
                },1000*fadein_timing/3);
            });

            $(popup_id_selector + ".tree_info_popup_control_button.maximize").click(function(e){
                var $tree = $("#page_wrapper");
                var tree_offset= $tree.offset();
                $popup.css("transition",transition_settings);

                if(!maximized) {
                    var popup_resize_height = $tree.height() - ($popup.outerHeight() - $popup.height());
                    $popup.css("top",tree_offset.top+"px");
                    $popup.css("left",tree_offset.left+"px");
                    $popup.css("width",$tree.outerWidth()+"px");
                    $popup.css("height",popup_resize_height+"px");
                    zoom_update($tree.outerWidth());
                    $(popup_id_selector + ".tree_info_popup_control_button.move, "+popup_id_selector+".tree_info_popup_control").unbind("mousedown");
                } else {
                    $popup.css("top",popupY+"px");
                    $popup.css("left",popupX+"px");
                    $popup.css("width",popup_width+"px");
                    $popup.css("height",popup_height+"px");
                    zoom_update(popup_width);
                    $(popup_id_selector + ".tree_info_popup_control_button.move, "+popup_id_selector+".tree_info_popup_control").on("mousedown", popup_move_enable);
                }

                minimized = false;
                maximized = !maximized;
            });

            $(popup_id_selector + ".tree_info_popup_control_button").on("mousedown", function(e){
                e.stopPropagation();
            });

            $(popup_id_selector + ".tree_info_popup_control_button.minimize").on("click", function(e){
                e.stopPropagation();
                var $tree = $(".tree");
                var tree_offset= $tree.offset();
                $popup.css("transition",transition_settings);

                if(!minimized) {
                    if(maximized) {
                        $popup.css("top",popupY+"px");
                        $popup.css("left",popupX+"px");
                        $popup.css("width",popup_width+"px");
                        $popup.css("height",popup_height+"px");
                        zoom_update(popup_width);
                        $(popup_id_selector + ".tree_info_popup_control_button.move, "+popup_id_selector+".tree_info_popup_control").on("mousedown", popup_move_enable);
                    } else {
                        $popup.css("top",popupY+"px");
                        $popup.css("left",popupX+"px");
                        $popup.css("width",popup_width*0.75+"px");
                        $popup.css("height",popup_height*0.75+"px");
                        zoom_update(popup_width*0.75);
                        minimized = true;
                    }

                } else {
                    $popup.css("top",tree_box_origin.getBoundingClientRect().top+"px");
                    $popup.css("left",tree_box_origin.getBoundingClientRect().left+"px");
                    $popup.css("width",width+"px");
                    $popup.css("height",height+"px");
                    $popup.css("opacity","0.0");
                    setTimeout(function(){$popup.remove()}, 2000);
                }

                maximized = !maximized;
            });

            $(popup_id_selector + ".tree_info_popup_control_button.close").on("click", function(e){
                e.stopPropagation();
                $popup.css("transition",transition_settings);
                $popup.css("top",tree_box_origin.getBoundingClientRect().top+"px");
                $popup.css("left",tree_box_origin.getBoundingClientRect().left+"px");
                $popup.css("width",width+"px");
                $popup.css("height",height+"px");
                $popup.css("opacity","0.0");
                setTimeout(function(){$popup.remove()}, 2000);
            });

            var popup_move_enable = function(e){
                //$(popup_id_selector + ".tree_info_popup_control_button.move").bind("mousedown", function(e){
                popupXlast = e.pageX;
                popupYlast = e.pageY;
                $popup_move.show();
                gecko_move_popup_mousedown = 1;
                console.log("FFshow ");
            }

            $(popup_id_selector + ".tree_info_popup_control_button.move, "+popup_id_selector+".tree_info_popup_control").on("mousedown", popup_move_enable);

            var gecko_move_popup_mousedown = 0;


            $popup_move.on("mouseup", function(e){
                gecko_move_popup_mousedown = 0;
            }).on("mouseout", function(e){
                gecko_move_popup_mousedown = 0;
                console.log("FFout ");
            });

            $popup_move.bind("mousemove", function(e){
                //e.stopPropagation();
                var button = 0;

                if(navigator.userAgent.indexOf("Gecko")>0 && navigator.userAgent.indexOf("WebKit")<0)
                    button += gecko_move_popup_mousedown;
                else if(navigator.userAgent.indexOf("MSIE")>0)
                    button += event.button;
                else
                    button += e.which;

                console.log("move "+button);

                if(button && popupXlast && popupYlast){

                    popupX -= popupXlast - e.pageX;
                    popupY -= popupYlast - e.pageY;

                    popupXlast = e.pageX;
                    popupYlast = e.pageY;

                    $popup.css("transition","all 0s");
                    $popup.css("left",popupX+"px");
                    $popup_move.css("left",popupX+"px");
                    $popup.css("top",popupY+"px");
                    $popup_move.css("top",popupY+"px");

                    zoom_update($popup.outerWidth());

                    //console.log("X: "+e.pageX+" Y: "+e.pageY+" buttons: "+ e.which);
                    //console.log("X: "+panX+" Y: "+panY);
                    //console.log("Xl: "+panXLast+" Yl: "+panYLast);
                }
                if(!button) {
                    $popup_move.hide();
                }
            });

        });

    $popup.css("width",width+"px");
    $popup.css("height",height+"px");
    $popup.css("top",Y+"px");
    $popup_move.css("top",Y+"px");
    $popup.css("left",X+"px");
    $popup_move.css("left",X+"px");
    $popup.css("opacity",0.4);

    $popup.show();
    $popup.css("transition",transition_settings);
    $popup.css("width",popup_width+"px");
    $popup.css("height",popup_height+"px");
    $popup.css("top",popupY+"px");
    $popup_move.css("top",popupY+"px");
    $popup.css("left",popupX+"px");
    $popup_move.css("left",popupX+"px");
    $popup.css("opacity",1.0);

    //$popup.hide();


    //console.log(node);
    //console.log(d);
}