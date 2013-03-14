<?php extract ($vars) ?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title><?php echo T("LdShake: Learning Desgin Solutions") ?></title>
	<link rel="icon" type="image/png" href="<?php echo $url ?>_graphics/favicon.ico" />
	<?php echo elgg_view('page_elements/analytics');?>
	<style>
		html, body
		{
			height: 100%;
			width: 100%;
			margin: 0;
			font-family: Helvetica,Arial,sans-serif;
		}
		
		a
		{
			outline: none;
		}
		
		h1, h2
		{
			font-size: 19px;
			margin: 10px 0;
			text-align: right;
		}
		
		#topbar
		{
			background-image: url(<?php echo $url ?>_graphics/bg-header-big.png);
			height: 80px;
		}
		
		#loginform
		{
			float: right;
			padding: 10px;
		}
		
		#middlecontents
		{
			text-align: center;
		}
		
		#bottombar
		{
			height: 75px;
			position: absolute;
			bottom: 0px;
			width: 100%;
		}
		
		#contentswrapper
		{
			position: absolute;
			top: 50%;
			left: 50%;
			margin-top: -175px;/* half elements height*/
			margin-left: -455px;/* half elements width*/
			width: 911px;
			height: 350px;
		}
		
		#logo
		{
			float: left;
			padding: 50px 30px 0 0;
			width: 400px;
		}
		
		#claims
		{
			float: left;
			padding-left: 30px;
			border-left: 1px solid #dadbdc;
		}
		
		#claims
		{
			text-align: left;
			width: 450px;
		}
		
		.home-pill
		{
			float: left;
			margin: 15px;
			padding: 5px;
			text-align: center;
			font-weight: bold;
			width: 120px;
			box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2), 0 10px 20px rgba(255, 255, 255, 0.6) inset, 0 1px 1px rgba(0, 0, 0, 0.3) inset;
			-moz-box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2), 0 10px 20px rgba(255, 255, 255, 0.6) inset, 0 1px 1px rgba(0, 0, 0, 0.3) inset;
			-webkit-box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2), 0 10px 20px rgba(255, 255, 255, 0.6) inset, 0 1px 1px rgba(0, 0, 0, 0.3) inset;
			font-size: 14px;
		}
		
		.home-pill p
		{
			margin: 3px 0;
		}
		
		.field
		{
			float: left;
			margin-left: 15px;
		}
		
		label
		{
			display: block;
			color: #C3DEC4;
			font-size: 11px;
			font-weight: bold;
		}
		
		input[type="submit"]
		{
			margin: 15px 15px 0 0;
			display: block;
			float: right;
			border-radius: 3px 3px 3px 3px;
			-moz-border-radius: 3px 3px 3px 3px;
			-webkit-border-radius: 3px 3px 3px 3px;
		    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#dfdfdf');
			background: -webkit-gradient(linear, center top, center bottom, from(#fafafa), to(#dfdfdf));
		    background: -moz-linear-gradient(center top , #fafafa, #dfdfdf) repeat scroll 0 0 #f6f6f6;
		    border: 1px solid #ccc;
		    color: #000;
		}
		
		.fieldoptions
		{
			display: block;
			font-size: 12px;
			color: #C3DEC4;
		}
		
		.fieldoptions div
		{
			margin-top: 8px;
			float: left;
		}
		
		input[type="checkbox"]
		{
			margin: 8px 5px 0 0;
			display: block;
			float: left;
		}
		
		a.underfield
		{
			margin-top: 8px;
			display: block;
			color: #C3DEC4;
		}
		
		#register
		{
			font-weight: bold;
			font-size: 14px;
			color: #fff;
			float: right;
			margin: 30px 25px 0 0;
		}
		
		#register a
		{
			color: #C3DEC4;
			font-weight: bold;
		}
		
		#footer
		{
			margin: 0 30px;
			border-top: 1px solid #dadbdc;
		}
		
		a img
		{
			border: 0;
		}
		
		#footer-logos {
			height: 66px;
			width: 824px;
			float: left;
		}
		
		#footer-logos ul {
			list-style-image: none;
			list-style-type: none;
			list-style-position: outside;
			padding: 0;
			margin: 5px 0 0 0;
		}
		
		#footer-logos li {
			padding: 5px;
			float: left;
		}
		
		#footer-contents
		{
			margin: 16px 0 0 30px;
			float: left;
		}

		ul.links {
			list-style-image: none;
			list-style-type: none;
			list-style-position: outside;
			padding: 0;
			margin: 0 0 10px 0;
			font-size: 13px;
		}
		
		ul.links a
		{
			color: #000;
		}
		
		.acknowledgements
		{
			color: #777;
			font-size: 12px;
		}
		
		.messages
		{
			background-color: #cfc;
		    border: 1px solid green;
		}

		.messages_error
		{
			background-color: #fcc;
		    border: 1px solid red;
		}

		.messages,.messages_error
		{
		    font-size: 13px;
		    margin: 3px;
		    padding: 3px;
		}
		
		.messages p,.messages_error p
		{
		    margin: 0;
		}
		
		.messages a,.messages_error a
		{
		    display: none;
		}
	</style>
</head>
<body>
	<div id="topbar">
		<form id="loginform" method="post" action="<?php echo $url ?>action/login">
			<div class="field">
				<label><?php echo T("Username") ?></label>
				<input id="username" type="text" name="username" tabindex="1" autofocus />
				<div class="fieldoptions">
					<input type="checkbox" value="true" name="persistent" />
					<div><?php echo T("Remember me") ?></div>
				</div>
			</div>
			<div class="field">
				<label><?php echo T("Password") ?></label>
				<input type="password" name="password"  tabindex="2" />
				<div class="fieldoptions">
					<a class="underfield" href="<?php echo $url ?>account/forgotten_password.php"><?php echo T("Forgot your password?") ?></a>
				</div>
			</div>
			<div class="field">
				<?php
					//Taken from views/default/input/form
					$ts = time();
					$token = generate_action_token($ts);
					echo elgg_view('input/hidden', array('internalname' => '__elgg_token', 'value' => $token));
					echo elgg_view('input/hidden', array('internalname' => '__elgg_ts', 'value' => $ts));
				?>
				<input type="submit" value="Login"  tabindex="3" />
			</div>
		</form>
		<div id="register"><?php echo T("Not an LdShaker yet?") ?> <a href="<?php echo $url ?>account/register.php"><?php echo T("Register here!") ?></a></div>
	</div>
	<?php echo elgg_view('messages/list', array('object' => $sysmessages)); ?>
	<div id="middlecontents">
		<div id="contentswrapper">
			<div id="logo">
				<h1>		
					<img src="<?php echo $url ?>_graphics/ldshake-logo-home.png" alt="LdShake logo" />
				</h1>
				<h2><?php echo T("A Web2.0 tool for the <span %1>social sharing and<br /> co-editing</span> of <span %2>learning design solutions</span>.", 'style="color: #709900;"', 'style="color: #960001;"') ?></h2>
			</div>
			<div id="claims">
				<div class="home-pill" style="background-color: #DDEFAA; transform: rotate(1.1deg); -o-transform: rotate(1.1deg); -moz-transform: rotate(1.1deg); -webkit-transform: rotate(1.1deg);">
					<img src="_graphics/ld-shake-1.png" height="60" />
					<p><?php echo T("Shake hands with other teachers!") ?></p>
				</div>
				
				<div class="home-pill" style="background-color: #99DBE7; transform: rotate(-1.3deg); -o-transform: rotate(-1.3deg); -moz-transform: rotate(-1.3deg); -webkit-transform: rotate(-1.3deg);">
					<img src="_graphics/ld-shake-2.png" height="60" />
					<p><?php echo T("Shake different learning design solutions!") ?></p>
				</div>
				
				<div class="home-pill" style="background-color: #FEFBC2; transform: rotate(-0.9deg); -o-transform: rotate(-0.9deg); -moz-transform: rotate(-0.9deg); -webkit-transform: rotate(-0.9deg);">
					<img src="_graphics/ld-shake-3.png" height="60" />
					<p><?php echo T("Shake your students with the learning designs!") ?></p>
				</div>
				
				<div class="home-pill" style="background-color: #FFD9D4; transform: rotate(1.9deg); -o-transform: rotate(1.9deg); -moz-transform: rotate(1.9deg); -webkit-transform: rotate(1.9deg);">
					<img src="_graphics/ld-shake-4.png" height="60" />
					<p><?php echo T("Shake up your way of working!") ?></p>
				</div>
				
				<div style="clear:both;"></div>
			</div>
		</div>
	</div>
	<div id="bottombar">
		<div id="footer">
			<div id="footer-logos">
				<ul>
					<li><a href="http://www.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/upf-logo.png" alt="<?php echo T("UPF logo") ?>"></a></li>
					<li><a href="http://gti.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/gti-small.png" alt="<?php echo T("GTI UPF logo") ?>"></a></li>
                    <li><a href="http://ldshake.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/powered-by-ldshake.png" alt="<?php echo T("Powered by LdShake") ?>" /></a></li>
                </ul>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	document.getElementById("username").focus();
	</script>
</body>
</html>	
