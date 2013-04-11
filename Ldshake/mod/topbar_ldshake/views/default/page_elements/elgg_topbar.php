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


/**
 * LdShake top toolbar
 * 
 */

if (isloggedin()) :
?>

<div id="ldshake_topbar">
	<div id="ldshake_topbar_wrapper">
		<div id="ldshake_topbar_container_left">
			<div id="ldshake_topbar_logo">
				<?php 
				$user = get_loggedin_user();
				if ($user->isExpert):
				?>
				<a href="<?php echo $vars['url']; ?>pg/lds/"><img src="<?php echo $vars['url']; ?>mod/topbar_ldshake/graphics/FINAL_LOGO.png" alt="LdShake" /></a>
				<?php else:	?>
				<a href="<?php echo $vars['url']; ?>pg/lds/firststeps/"><img src="<?php echo $vars['url']; ?>mod/topbar_ldshake/graphics/FINAL_LOGO.png" alt="LdShake" /></a>
				<?php endif; ?>
			</div>
			<div id="toolbarlinks">
				<ul id="toolbar_options">
					<li id="tb_newlds"><a href="<?php echo $vars['url']; ?>pg/lds/new/"><?php echo T("New LdS") ?></a></li>
					<!--li id="tb_newlds"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/exe/">New eXeLearning LdS</a></li>
					<li id="tb_newlds"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/webcollage/">New WebCollage LdS</a></li-->
					<li id="tb_mylds"><a href="<?php echo $vars['url']; ?>pg/lds/"><?php echo T("My LdS") ?></a></li>
					<li id="tb_browselds"><a href="<?php echo $vars['url']; ?>pg/lds/browse/"><?php echo T("Browse LdS") ?></a></li>
					<li id="tb_ldshakers"><a href="<?php echo $vars['url']; ?>pg/ldshakers/"><?php echo T("LdShakers") ?></a></li>
					<?php
					//allow people to extend this top menu
					echo elgg_view('elgg_topbar/extend', $vars);
					?>
				</ul>
				<div id="toolbar_lds_types" class="menu">
                    <ul>
                        <li id="tb_new_option_conceptualize" class="menu_option menu_suboption"><?php echo T("Conceptualize") ?></li>
                        <li id="tb_new_option_author" class="menu_option menu_suboption"><?php echo T("Author") ?></li>
                        <li id="tb_new_option_implement" class="menu_option menu_suboption" ><?php echo T("Implement") ?></li>
                    </ul>
			    </div>
                <div id="new_menu_author" class="menu level2">
                    <ul>
                        <!--<li id="tb_newlds_basic" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/"><?php echo T("Rich Text") ?></a></li>-->
                        <li id="tb_newlds_pattern" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/pattern/coursemap"><?php echo T("Course Map") ?></a></li>
                        <li id="tb_newlds_wic" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/webcollagerest/"><?php echo T("WebCollage") ?></a></li>
                        <li id="tb_newlds_wic" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/"><?php echo T("OpenGLM (upload)") ?></a></li>
                        <li id="tb_newlds_wic" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/"><?php echo T("CADMOS (upload)") ?></a></li>
                    </ul>
                </div>
                <div id="new_menu_implement" class="menu level2">
                    <ul>
                        <li id="tb_implement_select" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/implementable/"><?php echo T("Select a design to implement in a VLE through GLUEPS") ?></a></li>
                        <li id="tb_implement_see" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/pattern/coursemap"><?php echo T("See your VLE") ?></a></li>
                        <li id="tb_implement_register" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/vle/"><?php echo T("Register your VLE") ?></a></li>
                    </ul>
                </div>
            </div>
		</div>
		<div id="ldshake_topbar_container_right">
			<div id="ldshake_topbar_serach">
				<form id="searchform" action="<?php echo $vars['url']; ?>pg/lds/search/" method="get">
					<span id="ldshake_topbar_search_submit"><a href="#"><img src="<?php echo $vars['url']; ?>mod/topbar_ldshake/graphics/search.png" /></a></span>
					<input type="text" size="21" name="q" placeholder="Search" id="ldshake_topbar_search_input" />
				</form>
			</div>
			<div id="ldshake_topbar_avatar">
				<a href="<?php echo $vars['url']. 'pg/ldshakers/' . $_SESSION['user']->username ?>/"><img class="user_mini_avatar" src="<?php echo $_SESSION['user']->getIcon('tiny'); ?>" alt="<?php echo $_SESSION['user']->name ?>"/></a>
			</div>
			
			<div style="float:left;">
				<a href="#" id="ldshake_topbar_menu_switch">
					<span id="ldshake_topbar_user_options"><?php echo get_loggedin_user()->name; ?></span>
				</a>
				
				<ul id="ldshake_topbar_user_menu">
					<li><a href="<?php echo $vars['url']; ?>pg/settings/"><?php echo T("Account settings") ?></a></li>
<?php if ($vars['user']->admin || $vars['user']->siteadmin): ?>
					<li><a href="<?php echo $vars['url']; ?>pg/admin/"><?php echo T("Site administration") ?></a></li>
<?php endif; ?>
					<li><a href="<?php echo $vars['url']; ?>action/logout"><?php echo T("Log out") ?></a></li>
				</ul>
			</div>
		</div>
	</div>
</div>

<div style="clear:both;"></div>

<?php
endif;
?>