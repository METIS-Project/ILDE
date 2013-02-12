$(document).ready(function () {
	// click function to toggle reply panel
	$('a.message_reply').click(function () {
		$('div#message_reply_form').slideToggle("medium");
		return false;
	}); 
});