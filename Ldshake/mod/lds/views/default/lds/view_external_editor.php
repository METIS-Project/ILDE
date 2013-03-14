<?php
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

?>

<?php extract ($vars) ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><?php echo $lds->title ?> - LdShake</title>
	<link rel="icon" type="image/png" href="<?php echo $url ?>_graphics/favicon.ico" />
	<style type="text/css">
		body {
			margin:0;
			padding:0;
		}
		
		h1#doctitle {
			margin: 0;
			padding: 5px;
			background-color: #e7e8e9;
			border-bottom: 1px solid #aaa;
			font-family: Helvetica, Arial, sans-serif;
			font-size: 16px;
			color: #444;
		}
		
		#doc {
			padding: 20px;
			font-family: Helvetica, Arial, sans-serif;
			font-size: 18px;
			line-height: 1.6em;
		}
		
		iframe {
			min-height:700px;
			border: 0px;
			border-top: 1px solid #aaa;
			border-bottom: 1px solid #aaa;
		}
		
		a.exportbutton {
			display: block;
			float:left;
			border-radius: 3px 3px 3px 3px;
			-moz-border-radius: 3px 3px 3px 3px;
			-webkit-border-radius: 3px 3px 3px 3px;
			background-color: #eaeaea;
		    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fafafa', endColorstr='#dfdfdf');
			background: -webkit-gradient(linear, center top, center bottom, from(#fafafa), to(#dfdfdf));
		    background: -moz-linear-gradient(center top , #fafafa, #dfdfdf) repeat scroll 0 0 #f6f6f6;
		    border: 1px solid #ccc;
		    color: #000;
		    margin-left: 10px;
   		    margin-top: 5px;
   		    margin-bottom: 5px;
		    text-decoration: none;
		    font: 80%/1.4 sans-serif;
		    font-size: 13px;
		    padding: 3px 5px;
		}
		
		a.exportbutton:hover {
			text-decoration: none;
			border: 1px solid #999;
		}
		
		a.exportbutton:active {
			border: 1px solid #999;
			background-color: #dadada;
		    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#dfdfdf', endColorstr='#fafafa');
			background: -webkit-gradient(linear, center top, center bottom, from(#dfdfdf), to(#fafafa));
		    background: -moz-linear-gradient(center top , #dfdfdf, #fafafa) repeat scroll 0 0 #f6f6f6;
		}

		#exportcontainer {
		    vertical-align: middle;
    		display: table-cell;
		}
	
	</style>	
</head>
<body>
	<h1 id="doctitle"><?php echo $lds->title ?></h1>

<div id="exportcontainer">
	<?php if ($doc->editorType == 'exe'): ?>
	<a class="exportbutton" href="<?php echo $url ?>ve/<?php echo lds_contTools::encodeId($doc->guid)?>/scorm">Save as SCORM</a>
	<a class="exportbutton" href="<?php echo $url ?>ve/<?php echo lds_contTools::encodeId($doc->guid)?>/scorm2004">Save as SCORM 2004</a>
	<?php endif; ?>
	<a class="exportbutton" href="<?php echo $url ?>ve/<?php echo lds_contTools::encodeId($doc->guid)?>/ims_ld">Save as IMS-LD</a>
	<a class="exportbutton" href="<?php echo $url ?>ve/<?php echo lds_contTools::encodeId($doc->guid)?>/webZip">Save as zipped web page</a>
</div>
	<?php if ($doc->editorType == 'exe'): ?>
		<iframe src="<?php echo $CONFIG->url ?>content/exe/<?php echo $doc->pub_previewDir ?>/index.html?t=<?php echo rand(0, 1000) ?>" width="100%" height="100%"></iframe>
	<?php else: ?>
		<iframe src="<?php echo $CONFIG->url ?>content/webcollage/<?php echo $doc->pub_previewDir?>.html?t=<?php echo rand(0, 1000) ?>" width="100%" height="100%"></iframe>
	<?php endif; ?>
	

</body>
</html>
