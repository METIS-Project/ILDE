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
		<ul id="lds_side_sections">
			<li><a<?php if ($section == '') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('mine') ?>"><?php echo T("All my LdS") ?></a></li>
			<li><a<?php if ($section == 'created-by-me') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('created-by-me') ?>"><?php echo T("Created by me") ?></a></li>
			<li><a<?php if ($section == 'shared-with-me') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('shared-with-me') ?>"><?php echo T("Shared with me") ?></a></li>
			<li><a<?php if ($section == 'trashed') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('trashed') ?>"><?php echo T("Trashed") ?></a></li>
		</ul>
	</div>
	<div id="owner_block_bottom"></div>
</div>

<div id="two_column_left_sidebar_maincontent">
	<div id="content_area_user_title">
		<?php if ($filtering): ?>
		<h2><a href="<?php echo lds_viewTools::getUrl() ?>"><?php echo $title ?></a> » <span class="lds_tag <?php echo $tagk ?>"><?php echo $tagv ?></span></h2>
		<?php else: ?>
		<h2><?php echo $title ?></h2>
		<?php endif; ?>
	</div>
	
	<div class="filters">
		<div class="paging">
			<?php echo lds_viewTools::pagination($count, 50) ?>
		</div>
	</div>
	
	<?php if (is_array($list) && sizeof($list) > 0): ?>
	<form method="post" action="#">
		<div id="my_lds_list_header">
			<input id="lds_select_all" class="lds_select" type="checkbox" name="lds_group_select" value="" />
			<?php if ($section != 'trashed'): ?>
			<input type="submit" style="border-color:#999; margin:5px 0;" id="trash_some" name="trash_some" value="<?php echo T("Trash selected LdS") ?>" />
			<?php else: ?>
			<input type="submit" style="border-color:#999; margin:5px 0;" id="untrash_some" name="untrash_some" value="<?php echo T("Recover selected LdS") ?>" />
			<?php endif; ?>
		</div>
		<ul id="my_lds_list">
			<?php foreach($list as $item): ?>
			<?php if ($section != 'trashed'): ?>
			<li class="lds_list_element<?php if ($item->locked): ?> lds_locked<?php endif; ?><?php if ($item->new): ?> new<?php endif; ?>">
				<?php if ($item->starter->guid == get_loggedin_userid()): ?>
				<input class="lds_select lds_select_one" type="checkbox" name="lds_select" value="<?php echo $item->lds->guid ?>" />
				<?php else: ?>
				<div class="lds_select_spacer"></div>
				<?php endif; ?>
				<a href="<?php echo lds_viewTools::url_for($item->lds, 'view') ?>" class="lds_icon"><img src="<?php echo $url ?>mod/lds/images/lds-<?php echo $item->lds->external_editor ? 'exe' : 'doc' ?>-icon-20.png" alt="LdS" /></a>
				<div class="lds_info">
					<?php if (!$item->locked): ?>
					<a class="lds_edit_action" href="<?php echo lds_viewTools::url_for($item->lds, 'edit') ?>"><?php echo T("Edit") ?></a>
					<?php endif; ?>
					<span class="lds_title_tags">
						<a class="lds_title<?php if ($item->locked): ?> lds_padded<?php endif; ?>" href="<?php echo lds_viewTools::url_for($item->lds, 'view') ?>"><?php echo $item->lds->title ?></a>
						<?php echo lds_viewTools::all_tag_display ($item->lds) ?>
					</span>
					<span class="lds_people"><?php echo $item->starter->name ?> to <?php echo $item->num_editors ?> editor<?php if ($item->num_editors != 1): ?>s<?php endif; ?>, <?php if($item->num_viewers == -1): ?>all<?php else: ?><?php echo $item->num_viewers ?><?php endif; ?> viewer<?php if ($item->num_viewers != 1): ?>s<?php endif; ?></span>
					<?php if ($item->locked): ?>
					<span class="lds_editing_by"><?php echo $item->locked_by->name ?> is editing now</span>
					<?php endif; ?>
					<span class="lds_date"><?php echo friendly_time($item->lds->time_updated, false, true) ?></span>
				</div>
				<div class="clearfloat"></div>
			</li>
			<?php else: ?>
			<li class="lds_list_element">
				<input class="lds_select lds_select_one" type="checkbox" name="lds_select" value="<?php echo $item->lds->guid ?>" />
				<a href="<?php echo lds_viewTools::url_for($item->lds, 'viewtrashed') ?>" class="lds_icon"><img src="<?php echo $url ?>mod/lds/images/lds-doc-icon-20.png" /></a>
				<div class="lds_info">
					<span class="lds_title_tags">
						<a class="lds_title lds_padded" href="<?php echo lds_viewTools::url_for($item->lds, 'viewtrashed') ?>"><?php echo $item->lds->title ?></a>
						<?php echo lds_viewTools::all_tag_display ($item->lds) ?>
					</span>
					<span class="lds_people"><?php echo $item->starter->name ?> to <?php echo $item->num_editors ?> editor<?php if ($item->num_editors != 1): ?>s<?php endif; ?>, <?php if($item->num_viewers == -1): ?>all<?php else: ?><?php echo $item->num_viewers ?><?php endif; ?> viewer<?php if ($item->num_viewers != 1): ?>s<?php endif; ?></span>
					<span class="lds_date"><?php echo friendly_time($item->lds->time_updated, false, true) ?></span>
				</div>
				<div class="clearfloat"></div>
			</li>
			<?php endif; ?>
			<?php endforeach; ?>
		</ul>
	</form>
	<?php else: ?>
		<p class="noresults"><?php echo T("Oops, no LdS here!") ?></p>
	<?php endif; ?>
	
	
	<div class="filters">
		<div class="paging">
			<?php echo lds_viewTools::pagination($count, 50) ?>
		</div>
	</div>
</div>