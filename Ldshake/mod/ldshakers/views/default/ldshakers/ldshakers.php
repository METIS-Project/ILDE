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
?><?php extract($vars) ?>
<div id="two_column_left_sidebar">
	<div id="owner_block">
		<ul id="lds_side_sections">
			<li><a<?php if ($section == '') echo ' class="current"' ?> href="<?php echo ldshakers_viewTools::getUrl() ?>"><?php echo T("All users") ?></a></li>
			<?php 
			if (is_array($groups)):
				foreach ($groups as $g):
			?>
			<li><a<?php if ($g->id == $group->id) echo ' class="current"' ?> href="<?php echo ldshakers_viewTools::urlFor($g,'group') ?>"><?php echo $g->name ?> (<?php echo $g->nMembers ?>) <?php if ($g->canEdit) echo '*' ?></a></li>
			<?php
				endforeach;
			endif;
			?>
			<li><a id="ldshakers_create_group" href="#"><?php echo T("Create group...") ?></a></li>
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
	<input id="ldshake-interactive-search" type="text" value="<?php echo htmlspecialchars(T('Search for other users'))?>" />
	<div class="filters">
		<div class="paging">
			<?php echo ldshakers_viewTools::pagination($count, 50) ?>
		</div>
	</div>
	
	<form method="post" action=".">
<?php
    if($section != 'group' || $group->canEdit()) {
        ?>
		<div id="ldshakers_list_header">
			<input id="ldshakers_select_all" class="lds_select" type="checkbox" name="lds_group_select" value="" />
			<?php if ($section == 'group'): ?>
			<input type="button" style="border-color:#999; margin:5px; float:right;" id="delete_group" name="delete_group" data-id="<?php echo $group->guid ?>" value="<?php echo T("Delete this group") ?>" />
			<input type="button" style="border-color:#999; margin:5px 0;" id="remove_from_group" name="remove_from_group" data-id="<?php echo $group->guid ?>" value="<?php echo T("Remove selected LdShakers from %1", $group->name) ?>" />
			<?php else: ?>
			<input type="button" style="border-color:#999; margin:5px 0;" id="add_to_group" name="add_to_group" value="<?php echo T("Add selected LdShakers to...") ?>" />
			<?php endif; ?>
		</div>
        <?php
    }
        ?>


		<ul id="ldshakers_list">
			<?php if (is_array($ldshakers) && sizeof($ldshakers) > 0): ?>
			<?php foreach($ldshakers as $user): ?>
			<li class="ldshakers_list_element">
				<div class="starring_in"><?php echo ldshakers_viewTools::getGroupList($user->guid) ?></div>
                <?php
                if($section != 'group' || $group->canEdit()) {
                ?>
				<input class="ldshakers_select ldshakers_select_one" type="checkbox" name="ldshaker_select" value="<?php echo $user->guid ?>" />
                <?php
                }
                ?>

                <img class="profilepic" width="40" height="40" src="<?php echo $CONFIG->url.'pg/icon/'.$user->username.'/small?t='.hash("crc32b",$user->time_updated); ?>" />
				<div class="ldshakers_user_name_activity">
					<a class="ldshakers_user_name" href="<?php echo ldshakers_viewTools::urlFor($user,'user') ?>"><?php echo $user->name ?></a>
					<?php if(!empty($ldshakers_activity[$user->guid]['started']) or !empty($ldshakers_activity[$user->guid]['coedition'])):?>
						<div class="ldshakers_user_activity_box">
							<?php if(!empty($ldshakers_activity[$user->guid]['started'])):?>
							<a class="ldshakers_user_activity_item" href="<?php echo ldshakers_viewTools::urlFor($user,'user') ?>"><span class="ldshakers_user_activity"><?php echo T('%1 LdS created', $ldshakers_activity[$user->guid]['started'])?></a>
							<?php endif;?>
							<?php if(!empty($ldshakers_activity[$user->guid]['coedition'])):?>
							<a class="ldshakers_user_activity_item" href="<?php echo ldshakers_viewTools::urlFor($user,'user') ?>coedited"><span class="ldshakers_user_activity"><?php echo T('%1 LdS coedited', $ldshakers_activity[$user->guid]['coedition'])?></a>
							<?php endif;?>
						</div>
					<?php endif;?>
				</div>
				<div style="clear:both"></div>
			</li>
			<?php endforeach; ?>
			<?php else: ?>
			<li><?php echo T("No LdShakers to show here.") ?></li>
			<?php endif; ?>
		</ul>
	</form>
	
	
	
	<div class="filters">
		<div class="paging">
			<?php echo ldshakers_viewTools::pagination($count, 50) ?>
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
				<input type="submit" id="new_group_create" name="new_group_create" value="<?php echo T("Create group") ?>" />
				<div class="errnote"><?php echo T("This group already exists, please choose another name.") ?></div>
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
                    if ($g->canEdit):
			?>
			<li><a class="add_to_group_link" href="#" data-id="<?php echo $g->id ?>"><?php echo $g->name ?> (<?php echo $g->nMembers ?>)</a></li>
                        <?php
                        endif;
				endforeach;
			endif;
			?>
		</ul>
	</div>
</div>

/* javascript data*/
<script>
	var t9n = {
		search_field : "<?php echo htmlspecialchars(T('Search for other users')) ?>"
	};
	var offset = <?php echo $offset?>;
	var searchTimeout = null;
</script>