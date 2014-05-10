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

var tooltip_timer_ids = new Array();
var lds_submit_click = false;

function tooltips ()
{
    var height_correction = function () {
        $('#layout_canvas').css('min-height',
            window.innerHeight -
                $('#ldshake_topbar').outerHeight() -
                $('#layout_footer').outerHeight() -
                $('#footer_ending').outerHeight());
    }
    height_correction();
    $(window).resize(height_correction);

    $('.show_tooltip').mouseenter (function ()
	{
		var myclass = $(this).attr('class').split(' ');
		for (var i in myclass)
		{
			if (myclass[i].substr(0, 2) == 't_')
			{
				var id = '#' + myclass[i];
				var $caller = $(this);
				
				tooltip_timer_ids[id] = setTimeout (function ()
					{
						var pos = $(id).attr('data-pos');
						var selector = pos;
						var left = 0;
						var top = 0;

                        if($caller.attr('data-pos')) {
                            pos = $caller.attr('data-pos');
                        }

						if (typeof pos !== 'undefined' && pos !== false)
						{
							if (pos.indexOf('@') != -1)
							{
								selector = pos.substring(0, pos.indexOf('@'));
								left = parseInt(pos.substring(pos.indexOf('@') + 1, pos.indexOf(',',pos.indexOf('@'))));
								top = parseInt(pos.substring(pos.indexOf(',',pos.indexOf('@')) + 1)); 
							}
							
							$(id)
							.css('top',$(selector).offset().top + top)
							.css('left', function() {
                                    if($caller.attr('data-pos')) {
                                        return $(selector).offset().left
                                            + $(selector).outerWidth() + left;
                                    } else {
                                        return $(selector).offset().left + left;
                                    }
                                })
							.fadeIn(70);
						}
						else
						{
                            var halfWidth = $(id).outerWidth()/2;
							$(id)
							.css('top',$caller.offset().top + $caller.outerHeight())
							.css('left',$caller.offset().left + $caller.outerWidth()/2 - halfWidth)
							.fadeIn('fast');
						}
					}, 200);
			}
		}
	});
	
	$('.show_tooltip').mouseleave (function ()
	{	
		var myclass = $(this).attr('class').split(' ');
		for (var i in myclass)
		{
			if (myclass[i].substr(0, 2) == 't_')
			{
				var id = '#' + myclass[i];
				
				clearTimeout(tooltip_timer_ids[id]);
				
				$(id)
					.fadeOut(70);
				
/*				setTimeout (function ()
				{
					$(id)
					.fadeOut('fast');
				}, 100);*/
			}
		}
	});
}

$(document).ready(function()
{
    $("span.utc-time").each(function(){
        var $span=$(this);
        var time=parseInt($span.text());
        var span_date = new Date(time*1000);

        $span.removeClass("utc-time");

        /*if(span_date.toLocaleFormat)
            $span.text(span_date.toLocaleFormat("%c"));
        else
            $span.text(span_date.toLocaleString());
            */
        $span.text(span_date.toDateString()+' '+span_date.toLocaleTimeString());
    });

    $('#lds_delete_button').click (function ()
	{
		if (confirm (t9nc.deleteLdS + '\n\n'))
		{
			$(this).css('visibility','hidden');
			
			$.get(baseurl + "action/lds/delete", {lds:$('#lds_edit_guid').val()}, function ()
			{
				window.location = $('#lds_base_url').val();
			});
		}
	});
	
	$('#lds_recover_button').click (function ()
	{
		$(this).css('visibility','hidden');
		
		$.get(baseurl + "action/lds/recover", {lds:$('#lds_edit_guid').val()}, function ()
		{
			window.location = $('#lds_base_url').val();
		});
	});
	
	//Document tab scrolling: left button
	$('.arrow.left').click(function()
	{
		var content = $(".scrollable .content"),
			pos = content.position().left,
			diff = pos + 200;
		
		if (diff >= 0) diff = 0;
		content.animate({ left: diff}, 200);
	});
	
	//Document tab scrolling: right button
	$('.arrow.right').click(function()
	{
		var content = $(".scrollable .content"),
			pos = content.position().left,
			listwidth = 0,
			parentWidth = content.parent().width(),
			diff = pos - 200,
			limit;
			
		$(".scrollable .content li").each(function (){
			listwidth += $(this).outerWidth(true);
		});
		
		limit = (listwidth - parentWidth) * (-1);
		if (diff < limit) diff = limit;
		content.animate({ left: diff}, 200);
	});
	
	//Document tab scrolling: do we need to show the scroll buttons?
	$('.scrollable').mouseenter (function (){
		var listwidth = 0,
			parentWidth = $(".scrollable .content").parent().width();

		$(".scrollable .content li").each(function (){
			listwidth += $(this).outerWidth(true);
		});
		
		if (listwidth > parentWidth)
			$(this).find('.arrow').show();
	})
	.mouseleave(function(){
		$(this).find('.arrow').hide();
	});
	
	tooltips ();
});