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
		$('#ldshake_topbar_user_menu').toggle();
		$('#ldshake_topbar_user_options').toggleClass('menu_active');
		e.preventDefault();
		e.stopPropagation();
	});
	
	$('#tb_newlds').click (function (e)
	{
		$('#toolbar_lds_types').toggle();
		$('#tb_newlds a').toggleClass('menu_active');
		e.preventDefault();
		e.stopPropagation();
	});
	
	$('#toolbar_lds_types').click (function (e) {
		e.stopPropagation();
	});
	
	$('body').click(function() {
		$('#toolbar_lds_types').hide();
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