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
<?php if (is_array($vars['list']) && sizeof($vars['list']) > 0): ?>
<ul id="lds_list">
	<?php foreach($vars['list'] as $item): ?>
	<li class="lds_list_element">
		<div class="lds_actions">
			<?php if ($item->locked): ?>
				<div>
					<?php $fstword = explode(' ',$item->locked_by->name); $fstword = $fstword[0]; ?>
					<?php echo T("%1 is editing.") ?>
				</div>
			<?php else: ?>
				<?php if ($item->lds->canEdit() || $item->lds->owner_guid == get_loggedin_userid()): ?>
				<div>
					<?php if ($item->lds->canEdit()): ?><a href="<?php echo lds_viewTools::url_for($item->lds, 'edit') ?>"><?php echo T("Edit") ?></a><?php endif; ?>
					<?php if ($item->lds->owner_guid == get_loggedin_userid()): ?> | <a href="#" class="lds_action_delete" data-title="<?php echo htmlspecialchars($item->lds->title) ?>" data-id="<?php echo $item->lds->guid ?>"><?php echo T("Delete") ?></a><?php endif; ?>
					<div class="lds_loading"></div>
				</div>
				<?php endif; ?>
			<?php endif; ?>
			<div class="indicators">
				<a href="<?php echo lds_viewTools::url_for($item->lds, 'history') ?>" title="<?php echo T("View LdS history") ?>"><?php echo T("%1 revisions", $item->num_contributions) ?></a>
				<br />
				<a href="<?php echo lds_viewTools::url_for($item->lds, 'info') ?>" title="<?php echo T("View LdS info and comments") ?>"><?php echo T("%1 comments", $item->num_comments) ?></a>
				<br />
				<?php echo T("%1 documents", $item->num_documents) ?>
			</div>
		</div>
		<div class="lds_icon">
			<a href="<?php echo lds_viewTools::url_for($item->lds, 'view') ?>" ><img src="<?php echo $vars['url']; ?>mod/lds/images/lds-<?php echo $item->lds->external_editor ? 'exe' : 'doc' ?>-icon-64.png" /></a>
			<?php if ($item->lds->owner_guid == get_loggedin_userid()): ?>
			<br />
			<div class="lds_sticker mine"><?php echo T("Mine") ?></div>
			<?php elseif ($item->lds->canEdit()): ?>
			<br />
			<div class="lds_sticker canedit"><?php echo T("Can edit") ?></div>
			<?php endif; ?>
		</div>
		<div class="lds_info">
			<a href="<?php echo lds_viewTools::url_for($item->lds, 'view') ?>" class="lds_title"><?php echo $item->lds->title ?></a>
			<ul class="tagarea">
				<li><?php echo T("Discipline") ?>: <?php echo lds_viewTools::tag_display($item->lds, 'discipline') ?></li>
				<li><?php echo T("Pedag. approach") ?>: <?php echo lds_viewTools::tag_display($item->lds, 'pedagogical_approach') ?></li>
				<li><?php echo T("Free tags") ?>: <?php echo lds_viewTools::tag_display($item->lds, 'tags') ?></li>
			</ul>
			<div class="authory">
				<?php echo T("Started %1 by %2", friendly_time($item->lds->time_created), '<a href="'.$url.'pg/ldshakers/'.$item->starter->username.'">'.$item->starter->name.'</a>') ?>
				<?php if ($item->num_contributions > 1): ?>
				<br />
				<?php echo T("Latest revision %1 by %2", friendly_time($item->last_contribution_at), '<a href="'.$url.'pg/ldshakers/'.$item->last_contributor->username.'">'.$item->last_contributor->name.'</a>') ?>
				<?php endif; ?>
			</div>
		</div>
		<div class="clearfloat"></div>
	</li>
	<?php endforeach; ?>
</ul>
<?php else: ?>
	<p class="noresults"><?php echo T("Oops, no results here!") ?></p>
<?php endif; ?>