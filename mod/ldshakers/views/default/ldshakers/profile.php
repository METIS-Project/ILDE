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
<div id="two_column_left_sidebar">
	<div id="owner_block">
		<img src="<?php echo $cuser->getIcon('large') ?>" width="190" />
		<ul id="profile_actions">
			<li><a href="<?php echo $url ?>mod/messages/send.php?send_to=<?php echo $cuser->guid ?>"><?php echo T("Send a message") ?></a></li>
		</ul>
	</div>
	<div id="owner_block_bottom"></div>
</div>

<div id="two_column_left_sidebar_maincontent">
	<div id="content_area_user_title">
		<?php if ($filtering): ?>
		<h2><a href="<?php echo ldshakers_viewTools::getUrl() ?>"><?php echo $title ?></a> » <span class="lds_tag <?php echo $tagk ?>"><?php echo $tagv ?></span></h2>
		<?php else: ?>
		<h2><?php echo $title ?></h2>
		<?php endif; ?>
	</div>
	
	<div class="filters">
		<div class="paging">
			<?php echo ldshakers_viewTools::pagination($count) ?>
		</div>
	</div>
	
	<?php echo elgg_view('lds/browselist', $vars) ?>
	
	<div class="filters">
		<div class="paging">
			<?php echo ldshakers_viewTools::pagination($count) ?>
		</div>
	</div>
	
	<!-- Hidden stuff -->
	<div id="shade"></div>
	
	<div id="ldshakers_add_group_popup">
		<a class="close_popup" href="#"><?php echo T("Close") ?></a>
		<div class="popup_header"><?php echo T("Create a new group") ?></div>
		<div class="ldshakers_add_group_popup_section">
			<form method="post" action="." id="ldshakers_add_group_form">
				<div class="ldshakers_add_group_popup_fieldname"><?php echo T("Enter the name of the new group:") ?></div>
				<input type="text" id="new_group_name" name="new_group_name" value="" />
				<input type="submit" id="new_group_create" name="new_group_create" value="Create group" />
			</form>
		</div>
	</div>
	
	<div id="ldshakers_add_to_list">
		<div class="ldshakers_popup_info"><?php echo T("Add %1 selected LdShakers to the following group:","<strong></strong>") ?></div>
		<div class="ldshakers_popup_unavailable"><?php echo T("You need to select some LdShakers first.") ?></div>
		<ul>
			<?php 
			if (is_array($groups)):
				foreach ($groups as $g):
			?>
			<li><a class="add_to_group_link" href="#" data-id="<?php echo $g->id ?>"><?php echo $g->name ?> (<?php echo $g->nMembers ?>)</a></li>
			<?php
				endforeach;
			endif;
			?>
		</ul>
	</div>
</div>