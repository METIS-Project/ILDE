function dismissMsg ()
{
	$('div.messages,div.messages_error').click(function () {
		$(this).slideUp('fast');
		return false;
    });
}

function autoselect ()
{
	$(".autoselect")
		.focus(function (){ $(this).select(); })
		.keyup(function(){ $(this).select(); })
		.click(function(){ $(this).select(); });
}

$(document).ready(function ()
{
	dismissMsg ();
	autoselect ();
});
