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

<?php extract($vars) ?>
<div id="one_column">
	<div id="fs_top_banner">
		<h3><?php echo T("Welcome to LdShake!") ?></h3>
		<p><?php echo T("A site where teachers <span style=\"color:#960001\">(LdShakers)</span> co-edit and share Learning design Solutions <span style=\"color:#960001\">(LdS)</span>") ?></p>
		<div id="fs_never_again"><a href="<?php echo $url ?>pg/lds/make_expert/<?php echo get_loggedin_userid() ?>/"><?php echo T("OK, don't show me this page again") ?></a></div>
	</div>
	<div id="fs_xul">
		<h2>What you need to do to be able to edit eXeLearning resources?</h2>
        To be able to edit or create eXeLearning resources you will need to install <strong>Firefox</strong> (<strong><a href="http://www.firefox.com/" target="_blank">download here</a></strong>).</p>
        <p>Finally you will need to install the <strong>add on
            <em>Remote XUL Manager</em></strong> (<strong><a href="https://addons.mozilla.org/en-us/firefox/addon/remote-xul-manager/" target="_blank">download here</a></strong>). Follow this step to install the add on:
   		<ul>
			<li>
				<img src="<?php echo $url ?>mod/lds/images/xul1.png" style="border: 1px solid #000" />
				<p>Install the add on<br />(browser restart needed)</p>
			</li>
			<li>
				<img src="<?php echo $url ?>mod/lds/images/xul2.png" style="border: 1px solid #000" />
				<p>Click the option <em>Remote XUL Manager</em><br />from the <em>Web developer</em> menu</p>
			</li>
			<li>
				<img src="<?php echo $url ?>mod/lds/images/xul3.png" style="border: 1px solid #000" />
				<p>Click <em>Add...</em></p>
			</li>
			<li>
				<img src="<?php echo $url ?>mod/lds/images/xul4.png" style="border: 1px solid #000" />
				<p>Add your domain or IP for example <strong>ldshake.upf.edu</strong></p>
			</li>
		</ul>
	</div>
	<ul id="fs_actions">
		<li class="show_tooltip t_s1">
			<a class="callforaction" href="<?php echo $url ?>pg/ldshakers/"><?php echo T("Start discovering other LdShakers") ?> ▶</a>
			<div class="fs-pill" style="background-color: #DDEFAA; transform: rotate(1.1deg); -o-transform: rotate(1.1deg); -moz-transform: rotate(1.1deg); -webkit-transform: rotate(1.1deg);">
				<img src="<?php echo $url ?>_graphics/ld-shake-1.png" height="60" />
			</div>
			<h4><?php echo T("Shake hands with other teachers!") ?></h4>
			<p><?php echo T("Manage de different groups of LdShakers with whom you are sharing LdS.") ?></p>
			<div class="fs-clearfloat"></div>
		</li>
		<li class="show_tooltip t_s2">
			<a class="callforaction" href="<?php echo $url ?>pg/lds/created-by-me/"><?php echo T("Publish your LdS") ?> ▶</a>
			<div class="fs-pill" style="background-color: #FEFBC2; transform: rotate(-0.9deg); -o-transform: rotate(-0.9deg); -moz-transform: rotate(-0.9deg); -webkit-transform: rotate(-0.9deg);">
				<img src="<?php echo $url ?>_graphics/ld-shake-3.png" height="60" />
			</div>
			<h4><?php echo T("Shake your students with the learning designs!") ?></h4>
			<p><?php echo T("Publish the LdS you created: they can be publicly available and linked with a shareable URL.") ?></p>
			<div class="fs-clearfloat"></div>
		</li>
		<li class="show_tooltip t_s3">
			<a class="callforaction" href="<?php echo $url ?>pg/lds/browse/"><?php echo T("Discover LdS") ?> ▶</a>
			<div class="fs-pill" style="background-color: #99DBE7; transform: rotate(-1.3deg); -o-transform: rotate(-1.3deg); -moz-transform: rotate(-1.3deg); -webkit-transform: rotate(-1.3deg);">
				<img src="<?php echo $url ?>_graphics/ld-shake-2.png" height="60" />
			</div>
			<h4><?php echo T("Shake different learning design solutions!") ?></h4>
			<p><?php echo T("Search for LdS proposed by other LdShakers, or collaborate with them in their co-edition.") ?></p>
			<div class="fs-clearfloat" style="border-bottom:1px solid #ccc;margin-left:130px"></div>
		</li>
		<li class="show_tooltip t_s4">
			<a class="callforaction" href="<?php echo $url ?>pg/lds/new/"><?php echo T("Create your first LdS") ?> ▶</a>
			<div class="fs-pill" style="background-color: #FFD9D4; transform: rotate(1.9deg); -o-transform: rotate(1.9deg); -moz-transform: rotate(1.9deg); -webkit-transform: rotate(1.9deg);">
				<img src="<?php echo $url ?>_graphics/ld-shake-4.png" height="60" />
			</div>
			<h4><?php echo T("Shake up your way of working!") ?></h4>
			<p><?php echo T("Create your LdS and share them with other LdShakers so that they can see, comment or co-edit them.") ?></p>
			<div class="clearfloat"></div>
		</li>
	</ul>
	

	<div class="tooltip_bl" id="t_s1" data-pos="#tb_ldshakers@-75,20">
		<div class="tooltip_bl_stem"></div>
		<div class="tooltip_bl_body">
			<div><strong><?php echo T("Discover other LdShakers here") ?></strong></div>
		</div>
	</div>
	
	<div class="tooltip_bl" id="t_s2" data-pos="#tb_mylds@-85,20" style="width:230px;">
		<div class="tooltip_bl_stem"></div>
		<div class="tooltip_bl_body">
			<div><strong><?php echo T("You can publish the LdS that you start, which you'll find in this section.") ?></strong></div>
		</div>
	</div>
	
	<div class="tooltip_bl" id="t_s3" data-pos="#tb_browselds@-73,20">
		<div class="tooltip_bl_stem"></div>
		<div class="tooltip_bl_body">
			<div><strong><?php echo T("Discover existing LdS here") ?></strong></div>
		</div>
	</div>
	
	<div class="tooltip_bl" id="t_s4" data-pos="#tb_newlds@-60,20">
		<div class="tooltip_bl_stem"></div>
		<div class="tooltip_bl_body">
			<div><strong><?php echo T("Create your LdS here") ?></strong></div>
		</div>
	</div>
</div>