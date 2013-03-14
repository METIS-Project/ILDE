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

<?php
extract ($vars);
echo elgg_view('page_elements/header', $vars);
echo elgg_view('messages/list', array('object' => $sysmessages));
?>
<div id="layout_canvas" style="min-height: 600px;">
	<div id="one_column" style="padding-bottom:0 !important">
		<div id="content_area_user_title">
			<h2><?php echo $lds->title ?>: Revision history</h2>
		</div>
		<div id="lds_view_actions">
			<input type="hidden" id="lds_edit_guid" name="guid" value="<?php echo $lds->guid ?>" />
			<input type="hidden" id="lds_base_url" name="guid" value="<?php echo $url ?>pg/lds/" />
			<?php if (!lds_contTools::isLockedBy($lds->guid)): ?>
				<?php if ($lds->canEdit()): ?>
				<a class="leftbutton" href="<?php echo lds_viewTools::url_for($lds, 'edit') ?>">Edit this LdS</a>
				<?php endif; ?>
			<?php else: ?>
			<span><?php echo lds_contTools::isLockedBy($lds->guid)->name ?> is editing this LdS</span>
			<?php endif; ?>
			<a class="leftbutton" href="<?php echo lds_viewTools::url_for($lds, 'view') ?>">View this LdS</a>
		</div>

<?php if ($editor == 'webcollage'): ?>
	<ul>
		<?php foreach ($history as $rev): ?>
		<li><div style="clear: both; width: 100%;">
		<br>
			<div id="lds_revision_details" style="border:1px solid green;margin-bottom: 3px;margin-top: 12px;">
			<a href="<?php echo $rev->author->getUrl() ?>"><img src="<?php echo $rev->author->getIcon('tiny') ?>" /></a> <strong>Revision <?php echo $rev->revision_number ?></strong> by <a href="<?php echo $rev->author->getUrl() ?>"><?php echo $rev->author->name ?></a> on <?php echo date('j M Y H:i', $rev->revision->time_created) ?>
			</div>
			</div>
			<div style="width:630px; height: 500px;float:left;border:1px solid #ccc; overflow: auto">
			<?php if ($document->lds_revision_id != $rev->revision->id): ?>
				<img src="<?php echo $CONFIG->url ?>content/webcollage/rev_<?php echo $document->lds_guid; ?>_<?php echo $document->guid; ?>_<?php echo $rev->revision->id; ?>.jpg?t=<?php echo rand(0, 1000)?>" width="600"></img>
			<?php else: ?>
				<img src="<?php echo $CONFIG->url ?>content/webcollage/rev_<?php echo $document->lds_guid; ?>_<?php echo $document->guid; ?>.jpg?t=<?php echo rand(0, 1000)?>" width="600"></img>
			<?php endif; ?>

			</div>
			<div style="width:300px; height: 500px;float:right;border:1px solid #ccc; overflow: auto">
			<?php if ($document->lds_revision_id != $rev->revision->id): ?>
				<?php  echo $rev->revised_documents[0]->changelog; ?>
			<?php else: ?>
				<?php  echo $document->changelog; ?>
			<?php endif; ?>
			</div>
		</li>
		<br>
		<?php endforeach; ?>
	</ul>
<?php endif; ?>

<?php if ($editor == 'exe'): ?>
<br>
	<ul>
		<?php foreach ($history as $rev): ?>
		<li>		
			<div id="lds_revision_details" style="border:1px solid green;">
				<a href="<?php echo $rev->author->getUrl() ?>"><img src="<?php echo $rev->author->getIcon('tiny') ?>" /></a>
				<?php if ($document->lds_revision_id != $rev->revision->id): ?>
					<a href="<?php echo $vars['url']?>pg/lds/viewrevisioneditor/<?php echo $rev->revised_documents[0]->guid ?>">
				<?php else: ?>
					<a href="<?php echo $vars['url']?>pg/lds/viewrevisioneditor/<?php echo $document->guid ?>">
				<?php endif; ?>	
						<strong>Revision <?php echo $rev->revision_number ?></strong>
					</a>by <a href="<?php echo $rev->author->getUrl() ?>"><?php echo $rev->author->name ?></a> on <?php echo date('j M Y H:i', $rev->revision->time_created) ?>
			</div>
		</li>
		<?php endforeach; ?>
	</ul>
<?php endif; ?>

	</div>
</div>
<script type="text/javascript">
</script>
	<?php echo elgg_view('page_elements/jsarea', $vars); ?>
</body>
</html>