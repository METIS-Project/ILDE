<?php

     /**
	 * Elgg register form
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */
	 
	$username = get_input('u');
	$email = get_input('e');
	$name = get_input('n');
	$expectations = get_input('exp');

	$admin_option = false;
	if (($_SESSION['user']->admin) && ($vars['show_admin'])) 
		$admin_option = true;
		
	$form_body = "<br />";
	$form_body .= "<label class=\"sidelabel\">" . elgg_echo('name') . "</label>" . elgg_view('input/text' , array('internalname' => 'dfyhr745u5e4h45t3y545euy45hgtr', 'class' => "registerform", 'value' => $name));
	$form_body .= "<div class=\"annotation\" style=\"padding-left: 270px;margin-bottom: 20px;\">" . elgg_echo('name:label') . "</div>";
	
	$form_body .= "<p><label class=\"sidelabel\">" . elgg_echo('email') . "</label>" . elgg_view('input/text' , array('internalname' => 'hdg7867rehg54ht937hg398g4', 'class' => "registerform", 'value' => $email)) . "</p>";
	$form_body .= "<p style=\"display:none\"><label class=\"sidelabel\">" . elgg_echo('username') . "</label>" . elgg_view('input/text' , array('internalname' => 'ddyhryeryheryheyheueyhehgh5g', 'class' => "registerform", 'value' => '')) . "</p>";
	$form_body .= "<p style=\"display:none\"><label class=\"sidelabel\">" . elgg_echo('username') . "</label>" . elgg_view('input/text' , array('internalname' => 'dfgeireyherhyed78gh5g', 'class' => "registerform", 'value' => '')) . "</p>";
	$form_body .= "<p style=\"display:none\"><label class=\"sidelabel\">" . elgg_echo('username') . "</label>" . elgg_view('input/text' , array('internalname' => 'dfgeiyuhfdgeryeryrreh5g', 'class' => "registerform", 'value' => '')) . "</p>";
	$form_body .= "<p><label class=\"sidelabel\">" . elgg_echo('username') . "</label>" . elgg_view('input/text' , array('internalname' => 'dfgeiyuh544e78gh5g', 'class' => "registerform", 'value' => $username)) . "</p>";
	$form_body .= "<p><label class=\"sidelabel\">" . elgg_echo('password') . "</label>" . elgg_view('input/password' , array('internalname' => 'dgn45hygeihg5teh7ggg4e8gh', 'class' => "registerform")) . "</p>";
	$form_body .= "<p style=\"display:none\"><label class=\"sidelabel\">" . elgg_echo('username') . "</label>" . elgg_view('input/text' , array('internalname' => 'dfgeiyuhfdgetge544e78gh5g', 'class' => "registerform", 'value' => '')) . "</p>";
	$form_body .= "<p style=\"display:none\"><label class=\"sidelabel\">" . elgg_echo('username') . "</label>" . elgg_view('input/text' , array('internalname' => 'dfgeiyhdgfdgtge544e78gh5g', 'class' => "registerform", 'value' => '')) . "</p>";
	$form_body .= "<p style=\"display:none\"><label class=\"sidelabel\">" . elgg_echo('username') . "</label>" . elgg_view('input/text' , array('internalname' => 'dfkhsfhjeaeheehyeyey3gh5g', 'class' => "registerform", 'value' => '')) . "</p>";
	$form_body .= "<p style=\"display:none\"><label class=\"sidelabel\">" . elgg_echo('username') . "</label>" . elgg_view('input/text' , array('internalname' => 'dfgryryherseryer8gh5g', 'class' => "registerform", 'value' => '')) . "</p>";
	$form_body .= "<p><label class=\"sidelabel\">" . elgg_echo('passwordagain') . "</label>" . elgg_view('input/password' , array('internalname' => 'ndg874h78h5g08erhg854', 'class' => "registerform")) . "</p>";
	$form_body .= "<p style=\"display:none\"><label class=\"sidelabel\">" . elgg_echo('username') . "</label>" . elgg_view('input/text' , array('internalname' => 'dfgeiertyerty5e759025278gh5g', 'class' => "registerform", 'value' => '')) . "</p>";
	$form_body .= "<p style=\"display:none\"><label class=\"sidelabel\">" . elgg_echo('username') . "</label>" . elgg_view('input/text' , array('internalname' => 'dfgeireyeryeryreertere78gh5g', 'class' => "registerform", 'value' => '')) . "</p>";

	$form_body .= <<<HTML
	<div id="formvars">

	</div>
<script type="text/javascript">

	setTimeout("setUpRegistration()", 5000);


function setUpRegistration() {
	$('form').submit(function() {
		$('#formvars').append('<input type="hidden" name="dfgeyeyrehdfheheeryery4e78gh5g" />');
		$('#formvars').append('<input type="hidden" name="dfgeiyuhfdhteyeryheryerh5g" />');
		$('input[name=dfgeyeyrehdfheheeryery4e78gh5g]').val($('input[name=dgn45hygeihg5teh7ggg4e8gh]').val());
		$('input[name=dfgeiyuhfdhteyeryheryerh5g]').val($('input[name=ndg874h78h5g08erhg854]').val());
	});
}
</script>
HTML;


	//Pau: Get rid of the richtext thing
	//$form_body .= "<label>" . elgg_echo('whatsexpectatives') . "<br />" . elgg_view('input/longtext' , array('internalname' => 'whatsexpectatives', 'class' => "general-textarea", 'value' => $expectations)) . "</label><br />";
	$form_body .= "<p><label class=\"sidelabel\">" . elgg_echo('whatsexpectatives') . "</label>";
	$form_body .= "<textarea style=\"width: 400px;\" name=\"whatsexpectatives\">$expectations</textarea></p>";
	
	global $CONFIG;
	/*
	require_once($CONFIG->path .'vendors/captcha/recaptchalib.php');
	$publickey = $CONFIG->reCAPTCHA_public;
	$form_body .= '<div style="padding-left: 270px;">' . recaptcha_get_html($publickey) . '</div>';
	*/
	
	if ($admin_option)
		$form_body .= elgg_view('input/checkboxes', array('internalname' => "admin", 'options' => array(elgg_echo('admin_option'))));
	
	$form_body .= elgg_view('input/hidden', array('internalname' => 'friend_guid', 'value' => $vars['friend_guid']));
	$form_body .= elgg_view('input/hidden', array('internalname' => 'invitecode', 'value' => $vars['invitecode']));
	$form_body .= elgg_view('input/hidden', array('internalname' => 'action', 'value' => 'register'));
	$form_body .= "<p style=\"padding-left: 270px;\">".elgg_view('input/submit', array('internalname' => 'submit', 'value' => elgg_echo('register')))."</p>";
?>

	
	<div id="register-box">
	<h2><?php echo elgg_echo('register'); ?></h2>
	<?php echo elgg_view('input/form', array('action' => "{$vars['url']}action/register", 'body' => $form_body)) ?>
	</div>
