$(document).ready(function()
{
	//Search input placeholder (remove when HTML5 works in IE!)
	/*
	$('#ldshake_topbar_search_input')
		.val($('#ldshake_topbar_search_input').attr('placeholder'))
		.focus (function ()
		{
			if ($(this).val() == $(this).attr('placeholder'))
			{
				$(this).val('');
				$(this).addClass('written');
			}
		})
		.blur (function ()
		{
			if ($(this).val() == '')
			{
				$(this).removeClass('written');
				$(this).val($(this).attr('placeholder'));
			}
		});
	*/

	//Topbar account options
	$('#ldshake_topbar_menu_switch').click (function (e)
	{
		$('#ldshake_topbar_user_menu').css('left', $('#layout_canvas').offset().left + $('#layout_canvas').width() - $('#ldshake_topbar_user_menu').width() - 20);
		$('#ldshake_topbar_user_menu').fadeToggle(200);
		$('#ldshake_topbar_user_options').toggleClass('menu_active');
		e.preventDefault();
		e.stopPropagation();
	});
	
	$('#tb_newlds').click (function (e)
	{
		$('#toolbar_lds_types').fadeToggle(200);
		$('#tb_newlds a').toggleClass('menu_active');
		e.preventDefault();
		e.stopPropagation();
	});

    $('#toolbar_lds_types').mouseenter(function(){
        $(this).find('li')
            .css('background-color', '')
            .css('color', '');
    });

    $('#tb_new_option_author').mouseenter(function (e)
    {
        var menuOffsetX = $(e.target.parentElement.parentElement).position().left;
        var menuOffsetY = $(e.target.parentElement.parentElement).position().top;
        $('#new_menu_author').css('top', $(e.target).position().top + menuOffsetY + 5);
        $('#new_menu_author').css('left', $(e.target).outerWidth() + menuOffsetX - 5);

        $('#new_menu_author').fadeIn(200);
    });

    $('#tb_new_option_author').mouseleave(function (e)
    {
        $nm = $('#new_menu_author');

        var evalX = (e.pageY >= $nm.offset().top && e.pageY <= $nm.offset().top + $nm.outerHeight());
        var evalY = (e.pageX >= $nm.offset().left - 1 && e.pageX <= $nm.offset().left + $nm.outerWidth());

        if(!evalX || !evalY) {
            $('#new_menu_author').fadeOut(200);
        } else {
            $(this)
                .css('background-color', 'rgb(56, 99, 47)')
                .css('color', '#fff');
        }
    });

    $('#new_menu_author').mouseleave(function (e)
    {
        $nm = $('#tb_new_option_author')
        var evalX = (e.pageY >= $nm.offset().top && e.pageY <= $nm.offset().top + $nm.outerHeight());
        var evalY = (e.pageX >= $nm.offset().left && e.pageX <= $nm.offset().left + $nm.outerWidth());
        //var evalY = (e.pageX >= $nm.offset().left && e.pageX < $(this).offset().left);

        if(!evalX || !evalY) {
            $(this).fadeOut(200);
            $('#tb_new_option_author')
                .css('background-color', '')
                .css('color', '');
        }
    });

    $('#tb_new_option_implement').mouseenter(function (e)
    {
        var menuOffsetX = $(e.target.parentElement.parentElement).position().left;
        var menuOffsetY = $(e.target.parentElement.parentElement).position().top;
        $('#new_menu_implement').css('top', $(e.target).position().top + menuOffsetY + 5);
        $('#new_menu_implement').css('left', $(e.target).outerWidth() + menuOffsetX - 5);

        $('#new_menu_implement').fadeIn(200);
    });

    $('#tb_new_option_implement').mouseleave(function (e)
    {
        $nm = $('#new_menu_implement')

        var evalX = (e.pageY >= $nm.offset().top && e.pageY <= $nm.offset().top + $nm.outerHeight());
        var evalY = (e.pageX >= $nm.offset().left - 1 && e.pageX <= $nm.offset().left + $nm.outerWidth());

        if(!evalX || !evalY) {
            $('#new_menu_implement').fadeOut(200);
        } else {
            $(this)
                .css('background-color', 'rgb(56, 99, 47)')
                .css('color', '#fff');
        }
    });

    $('#new_menu_implement').mouseleave(function (e)
    {
        $nm = $('#tb_new_option_implement')
        var evalX = (e.pageY >= $nm.offset().top && e.pageY <= $nm.offset().top + $nm.outerHeight());
        var evalY = (e.pageX >= $nm.offset().left && e.pageX <= $nm.offset().left + $nm.outerWidth());

        if(!evalX || !evalY) {
            $(this).fadeOut(200);
            $('#tb_new_option_implement')
                .css('background-color', '')
                .css('color', '');
        }
    });

	$('#toolbar_lds_types').click (function (e) {
		e.stopPropagation();
	});
	
	$('body').click(function() {
		$('#toolbar_lds_types').fadeOut(200);
        $('.menu').fadeOut(200);
		$('#tb_newlds a').removeClass('menu_active');
		
		$('#ldshake_topbar_user_menu').hide();
		$('#ldshake_topbar_user_options').removeClass('menu_active');
	});

	
	//Submit search form when clicked the icon
	/*
	$('#ldshake_topbar_search_submit').click (function ()
	{
		$('#searchform').submit();
		return false;
	});
	*/
	
});