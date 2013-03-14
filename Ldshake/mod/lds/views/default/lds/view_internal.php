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
<div id="one_column">
	<div id="content_area_user_title">
		<h2><?php echo $lds->title ?></h2>
	</div>
	<div id="lds_view_actions">
		<input type="hidden" id="lds_edit_guid" name="guid" value="<?php echo $lds->guid ?>" />
		<input type="hidden" id="lds_base_url" name="guid" value="<?php echo $url ?>pg/lds/" />
		<?php if ($lds->canEdit()): ?>
			<?php if (!lds_contTools::isLockedBy($lds->guid)): ?>
			<?php if ($lds->owner_guid == get_loggedin_userid()): ?>
			<a class="rightbutton" id="lds_delete_button" href="#"><?php echo T("Trash this LdS") ?></a>
			<?php endif; ?>
			<a class="rightbutton" id="lds_share_button" href="#"><?php echo T("Sharing options...") ?></a>
			<a class="leftbutton" href="<?php echo lds_viewTools::url_for($lds, 'edit') ?>"><?php echo T("Edit this LdS") ?></a>
			<?php else: ?>
			<span><?php echo T("%1 is editing this LdS", lds_contTools::isLockedBy($lds->guid)->name) ?></span>
			<?php endif; ?>
		<?php endif; ?>
		<a class="leftbutton" href="<?php echo lds_viewTools::url_for($lds, 'history') ?>"><?php echo T("View revision history") ?></a>
	</div>
<div id="lds_view_tab_container" style="width:953px;" class="scrollable">
<?php /** Botonets de scroll **/ ?>
<div class="arrow right">►</div><div class="arrow left">◄</div>
<?php /** Aquí es importante no indentar líneas ni dejar código html con un newline al final, por el css inline-block. **/ ?>
<ul id="lds_view_tabs" class="content">
<?php if ($infoComments): ?>
<li class="activetab infotab"><?php echo T("Info and Comments") ?> (<?php echo $nComments ?>)</li><?php else: ?>
<li><a class="infotab" href="<?php echo lds_viewTools::url_for($lds, 'info') ?>"><?php echo T("Info and Comments") ?> (<?php echo $nComments ?>)</a></li><?php endif; ?>
<?php foreach ($ldsDocs as $doc): ?>
<?php if ($doc->guid == $currentDocId): ?>
<li class="activetab"><?php echo $doc->title ?></li><?php else: ?>
<li><a href="<?php echo lds_viewTools::url_for($lds, 'view').'doc/'.$doc->guid.'/'?>"><?php echo $doc->title ?></a></li><?php endif; ?>
<?php endforeach; ?>
</ul>
</div>


<?php if ($infoComments): ?>
	<div id="lds_info_wrapper">
		<ul class="paramarea">
			<li><?php echo T("Completeness") ?>: <?php echo $lds->completeness ?:'0' ?></li>
			<li><?php echo T("Granularity") ?>: <?php echo $lds->granularity ?:'0' ?></li>
		</ul>
		<ul class="tagarea">
			<li><?php echo T("Discipline") ?>: <?php echo lds_viewTools::tag_display($lds, 'discipline') ?></li>
			<li><?php echo T("Pedag. approach") ?>: <?php echo lds_viewTools::tag_display($lds, 'pedagogical_approach') ?></li>
			<li><?php echo T("Free tags") ?>: <?php echo lds_viewTools::tag_display($lds, 'tags') ?></li>
		</ul>
		
		<?php echo elgg_view_comments($lds) ?>
	</div>
<?php else: ?>
	<?php if ($lds->owner_guid == get_loggedin_userid()): ?>
		<div id="lds_unpublish_wrapper" class="lds_view_tab_actions<?php if ($publishedId != $currentDocId) echo ' hidden' ?>">
			<div class="lds_loading" style="margin-top: 4px;"></div>
			<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
			<a class="publishbutton rightbutton" href="#" id="lds_action_unpublish" data-guid="<?php echo $currentDoc->guid ?>"><?php echo T("Unpublish this document") ?></a>
			<?php echo T("Public link:") ?>
			<input class="lds_publish_url autoselect" type="text" readonly="readonly" value="<?php echo $url ?>v/<?php echo lds_contTools::encodeId($currentDoc->guid)?>" />
            <?php echo T("Embed link:") ?>
            <input class="lds_publish_embed autoselect" type="text" readonly="readonly" value="<?php echo htmlspecialchars('<iframe height="600px" width="100%" frameborder="1" src="' . "{$url}v/" . lds_contTools::encodeId($currentDoc->guid) . '"></iframe>');?>" />
            <div style="clear:both"></div>
		</div>
		<div id="lds_publish_wrapper" class="lds_view_tab_actions<?php if ($publishedId != -1) echo ' hidden' ?>">
			<div class="lds_loading" style="margin-top: 4px;"></div>
			<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
			<a class="publishbutton rightbutton" href="#" id="lds_action_publish" data-guid="<?php echo $currentDoc->guid ?>"><?php echo T("Publish this document") ?></a>
			<span style="padding-top:4px; display: block;"><?php echo T("This document is not published.") ?></span>
			<div style="clear:both"></div>
		</div>
		<div id="lds_republish_wrapper" class="lds_view_tab_actions<?php if ($publishedId == $currentDocId || $publishedId == -1) echo ' hidden' ?>">
			<div class="lds_loading" style="margin-top: 4px;"></div>
			<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
			<a class="publishbutton rightbutton" href="#" id="lds_action_republish" data-guid="<?php echo $currentDoc->guid ?>"><?php echo T("Republish this document") ?></a>
			<a class="publishbutton rightbutton" href="#" id="lds_action_unpublish2" data-guid="<?php echo $currentDoc->guid ?>"><?php echo T("Unpublish this document") ?></a>
			<?php echo T("Public link <strong>(older version published)</strong>:") ?>
			<input class="lds_publish_url autoselect" type="text" readonly="readonly" value="<?php echo $url ?>v/<?php echo lds_contTools::encodeId($currentDoc->guid)?>" />
            <?php echo T("Embed link:") ?>
            <input class="lds_publish_embed autoselect" type="text" readonly="readonly" value="<?php echo htmlspecialchars('<iframe height="600px" width="100%" frameborder="1" src="' . "{$url}v/" . lds_contTools::encodeId($currentDoc->guid) . '"></iframe>');?>" />
            <div style="clear:both"></div>
		</div>
	<?php else: ?>
		<div id="lds_unpublish_wrapper" class="lds_view_tab_actions<?php if ($publishedId != $currentDocId) echo ' hidden' ?>">
			<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
			<?php echo T("Public link:") ?>
			<input class="lds_publish_url autoselect" type="text" readonly="readonly" value="<?php echo $url ?>v/<?php echo lds_contTools::encodeId($currentDoc->guid)?>" />
            <?php echo T("Embed link:") ?>
            <input class="lds_publish_embed autoselect" type="text" readonly="readonly" value="<?php echo htmlspecialchars('<iframe height="600px" width="100%" frameborder="1" src="' . "{$url}v/" . lds_contTools::encodeId($currentDoc->guid) . '"></iframe>');?>" />
            <div style="clear:both"></div>
		</div>
		<div id="lds_publish_wrapper" class="lds_view_tab_actions<?php if ($publishedId != -1) echo ' hidden' ?>">
			<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
			<span style="padding-top:4px; display: block;"><?php echo T("This document is not published.") ?></span>
			<div style="clear:both"></div>
		</div>
		<div id="lds_republish_wrapper" class="lds_view_tab_actions<?php if ($publishedId == $currentDocId || $publishedId == -1) echo ' hidden' ?>">
			<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
			<?php echo T("Public link:") ?>
			<input class="lds_publish_url autoselect" type="text" readonly="readonly" value="<?php echo $url ?>v/<?php echo lds_contTools::encodeId($currentDoc->guid)?>" />
			<?php echo T("<strong>Warning:</strong> An older version of this document is published.") ?>
            <?php echo T("Embed link:") ?>
            <input class="lds_publish_embed autoselect" type="text" readonly="readonly" value="<?php echo htmlspecialchars('<iframe height="600px" width="100%" frameborder="1" src="' . "{$url}v/" . lds_contTools::encodeId($currentDoc->guid) . '"></iframe>');?>" />
            <div style="clear:both"></div>
		</div>
	<?php endif; ?>
	<div id="the_lds">
		<?php echo $currentDoc->description ?>
	</div>
<?php endif; ?>
</div>

<!-- Hidden stuff -->
<div id="shade"></div>

<?php include ('single_share_form.php') ?>

<script type="text/javascript">
	var am_i_starter = <?php echo ($am_i_starter ? 'true' : 'false') ?>;
	var friends = new Array()
    var new_lds = false;
	friends['available'] = <?php echo $jsonfriends ?>;
	friends['viewers'] = <?php echo $viewers ?>;
	friends['editors'] = <?php echo $editors ?>;

	var groups = <?php echo $groups ?>;

	//Is the LdS public for all LdShakers? (yes by default)
	var allCanView = <?php echo $all_can_read ?>;
</script>