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

?><?php
global $jslibs;
extract ($vars);
echo elgg_view('page_elements/header', $vars);
echo elgg_view('messages/list', array('object' => system_messages(null,"")));

function encodeURIComponent($str) {
	$revert = array('%21'=>'!', '%2A'=>'*', '%27'=>"'", '%28'=>'(', '%29'=>')');
	return strtr(rawurlencode($str), $revert);
}

?>
<!-- canvas -->
<div id="layout_canvas">

<div id="one_column" style="padding-bottom:0 !important">
	<div id="content_area_user_title">
		<h2><?php echo $lds->title ?></h2>
	</div>
	<div id="lds_view_actions">
		<input type="hidden" id="lds_edit_guid" name="guid" value="<?php echo $lds->guid ?>" />
		<input type="hidden" id="lds_base_url" name="guid" value="<?php echo $url ?>pg/lds/" />
        <?php if (strstr($lds->editor_type, 'google')): ?>
        <a class="rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
        <?php endif; ?>
		<?php if ($lds->canEdit()): ?>
			<?php if (!lds_contTools::isLockedBy($lds->guid)): ?>
			<?php if ($lds->owner_guid == get_loggedin_userid()): ?>
			<a class="rightbutton" id="lds_delete_button" href="#"><?php echo T("Trash this %1", ldshake_env_category($lds)) ?></a>
			<?php endif; ?>
			<a class="rightbutton" id="lds_share_button" href="#"><?php echo T("Sharing options...") ?></a>


            <?php if(ldshake_supports_pdf($currentDoc)): ?>
                    <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
            <?php endif; ?>
            <?php if(isset($currentDoc->downloable)): ?>
                <a class="rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $currentDoc->file_guid . "&title=" . encodeURIComponent($lds->title . '.' . $currentDoc->downloable) ?>"><?php echo T("Download %1 file", $currentDoc->downloable) ?></a>
            <?php endif; ?>
            <?php if(isset($currentDoc->file_xlsx_guid)): ?>
                <a class="rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $currentDoc->file_xlsx_guid . "&title=" . encodeURIComponent($lds->title.".xlsx") ?>"><?php echo T("Download excel file") ?></a>
            <?php endif; ?>
            <?php if(isset($currentDoc->file_imsld_guid)): ?>
                <a class="rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $currentDoc->file_imsld_guid . "&title=" . encodeURIComponent($lds->title."(ims-lds).zip") ?>"><?php echo T("Download IMS-LD file") ?></a>
            <?php endif; ?>
            <?php if(isset($currentDoc->file_scorm_guid)): ?>
                <a class="rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $currentDoc->file_scorm_guid . "&title=" . encodeURIComponent($lds->title."(scorm).zip") ?>"><?php echo T("Download SCORM file") ?></a>
            <?php endif; ?>
            <?php if ($lds->editor_type == 'gluepsrest'): ?>
                <?php if (!$glueps): ?>
                    <a class="leftbutton lds_select_implement_action" lds="<?php echo $lds->guid?>" href="<?php echo lds_viewTools::url_for($lds, 'edit') ?>"><?php echo T("Edit implementation") ?></a>
                <?php else: ?>
                        <a class="leftbutton" lds="<?php echo $lds->guid?>" href="<?php echo $url.'pg/lds/editglueps/'.$lds->guid; ?>"><?php echo T("Edit implementation") ?></a>
                <?php endif; ?>
                <?php if (isadminloggedin()): ?>
                    <a class="leftbutton" href="<?php echo "{$url}pg/lds/vle/{$lds->vle_id}" ?>"><?php echo T("View VLE")." cid:".$lds->course_id ?></a>
                    <?php if($currentDoc->editorType == 'gluepsrest' || RestEditor::rest_enabled($currentDoc->editorType)): ?>
                        <a class="rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $currentDoc->file_guid . "&title=" . encodeURIComponent($lds->title."(impl).json") ?>"><?php echo T("Download impl file") ?></a>
                    <?php endif; ?>
                <?php endif; ?>
            <?php else: ?>
                <a class="leftbutton" href="<?php echo lds_viewTools::url_for($lds, 'edit') ?>"><?php echo T("Edit this %1", ldshake_env_category($lds)) ?></a>
            <?php endif; ?>
			<!--<a class="leftbutton" id="lds_import_button" href="#"><?php echo T("Import an eXe Learning file") ?></a>-->
			<?php else: ?>
			<span><?php echo T("%1 is editing this LdS",lds_contTools::isLockedBy($lds->guid)->name) ?></span>
			<?php endif; ?>
		<?php endif; ?>
		<a class="leftbutton" href="<?php echo lds_viewTools::url_for($lds, 'history') ?>"><?php echo T("View revision history") ?></a>
        <?php if ($lds->editor_type != 'gluepsrest' && $lds->editor_type != 'project_design'): ?>
            <a id="duplicate_design" class="leftbutton" href="#"><?php echo T("Duplicate this LdS") ?></a>
            <a class="leftbutton" href="<?php echo $CONFIG->url . 'pg/lds/tree/' . $lds->guid ?>"><?php echo T("View duplicates") ?></a>
        <?php endif; ?>
	</div>

	<!--<form id="editorfileupload" action="<?php echo $url ?>action/lds/import_editor_file" method="post" enctype="multipart/form-data">
		<label for="file">ELP file:</label>
		<input type="file" name="file" id="file" />
		<input type="hidden" name="guid" value="<?php echo $currentDoc->guid ?>" />
		<input type="hidden" name="lds_guid" value="<?php echo $lds->guid ?>" />
		<input type="submit" name="submit" value="Submit" />
	</form>-->

<div id="lds_view_tab_container" style="width:953px;" class="scrollable">
	<?php /** Botonets de scroll **/ ?>
	<div class="arrow right">►</div><div class="arrow left">◄</div>
	<?php /** Aquí es importante no indentar líneas ni dejar código html con un newline al final, por el css inline-block. **/ ?>
	<ul id="lds_view_tabs" class="content">
        <?php if ($infoComments): ?>
            <li class="activetab infotab"><?php echo T("Info and Comments") ?> (<?php echo $nComments ?>)</li><?php else: ?>
            <li><a class="infotab" href="<?php echo lds_viewTools::url_for($lds, 'info') ?>"><?php echo T("Info and Comments") ?> (<?php echo $nComments ?>)</a></li><?php endif; ?>

        <?php foreach($ldsDocs as $doc): ?>
            <?php $title = empty($doc->title) ? $lds->title : $doc->title; ?>
            <?php if ($doc->guid == $currentDocId): ?>
                <li class="activetab"><?php echo $title ?></li>
            <?php else: ?>
                <li><a href="<?php echo lds_viewTools::url_for($lds, 'view').'doc/'.$doc->guid.'/'?>"><?php echo $title ?></a></li>
            <?php endif; ?>
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
    <?php if($lds->getSubtype() != 'LdS_implementation'): ?>
        <?php if ($lds->owner_guid == get_loggedin_userid()): ?>
            <div id="lds_license_wrapper" class="lds_view_tab_actions">
                <?php include('license_banner.php'); ?>
                <?php if($lds->license): ?>
                    <a id="manage_license" class="publishbutton rightbutton" href="#"><?php echo T("Manage license") ?></a>
                <?php else: ?>
                    <a id="manage_license" class="publishbutton rightbutton" href="#"><?php echo T("Add license") ?></a>
                <?php endif; ?>
                <div style="clear:both"></div>
            </div>

            <div id="lds_unpublish_wrapper" class="lds_view_tab_actions<?php if ($publishedId != $currentDocId) echo ' hidden' ?>">
                <div class="lds_loading" style="margin-top: 4px;"></div>
                <!--
                <?php if ($iseXe): ?>
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export_editor?docId=<?php echo $lds->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php else: ?>
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php endif; ?>
                -->
                <a class="publishbutton rightbutton" href="#" id="lds_action_unpublish" data-guid="<?php echo $currentDoc->guid ?>"><?php echo T("Unpublish this document") ?></a>
                <?php echo T("Public link:") ?>
                <?php if ($iseXe): ?>
                <input class="lds_publish_url autoselect" type="text" readonly="readonly" value="<?php echo $url ?>ve/<?php echo lds_contTools::encodeId($currentDoc->guid)?>" />
                <?php echo T("Embed link:") ?>
                <input class="lds_publish_embed autoselect" type="text" readonly="readonly" value="<?php echo htmlspecialchars('<iframe height="600px" width="100%" frameborder="1" src="' . "{$url}ve/" . lds_contTools::encodeId($currentDoc->guid) . '"></iframe>');?>" />
                <?php else: ?>
                <input class="lds_publish_url autoselect" type="text" readonly="readonly" value="<?php echo $url ?>v/<?php echo lds_contTools::encodeId($currentDoc->guid)?>" />
                <?php endif; ?>
                <div style="clear:both"></div>
            </div>
            <div id="lds_publish_wrapper" class="lds_view_tab_actions<?php if ($publishedId != -1) echo ' hidden' ?>">
                <div class="lds_loading" style="margin-top: 4px;"></div>
                <!--
                <?php if ($iseXe): ?>
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export_editor?docId=<?php echo $lds->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php else: ?>
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php endif; ?>
                -->
                <a class="publishbutton rightbutton" href="#" id="lds_action_publish" data-guid="<?php echo $currentDoc->guid ?>"><?php echo T("Publish this document") ?></a>
                <span style="padding-top:4px; display: block;"><?php echo T("This document is not published.") ?></span>
                <div style="clear:both"></div>
            </div>
            <div id="lds_republish_wrapper" class="lds_view_tab_actions<?php if ($publishedId == $currentDocId || $publishedId == -1) echo ' hidden' ?>">
                <div class="lds_loading" style="margin-top: 4px;"></div>
                <!--
                <?php if ($iseXe): ?>
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export_editor?docId=<?php echo $lds->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php else: ?>
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php endif; ?>
                -->
                <a class="publishbutton rightbutton" href="#" id="lds_action_republish" data-guid="<?php echo $currentDoc->guid ?>"><?php echo T("Republish this document") ?></a>
                <a class="publishbutton rightbutton" href="#" id="lds_action_unpublish2" data-guid="<?php echo $currentDoc->guid ?>"><?php echo T("Unpublish this document") ?></a>
                <?php echo T("Public link <strong>(older version published)</strong>:") ?>
                <?php if ($iseXe): ?>
                <input class="lds_publish_url autoselect" type="text" readonly="readonly" value="<?php echo $url ?>ve/<?php echo lds_contTools::encodeId($currentDoc->guid)?>" />
                <?php echo T("Embed link:") ?>
                <input class="lds_publish_embed autoselect" type="text" readonly="readonly" value="<?php echo htmlspecialchars('<iframe height="600px" width="100%" frameborder="1" src="' . "{$url}ve/" . lds_contTools::encodeId($currentDoc->guid) . '"></iframe>');?>" />
                <?php else: ?>
                <input class="lds_publish_url autoselect" type="text" readonly="readonly" value="<?php echo $url ?>v/<?php echo lds_contTools::encodeId($currentDoc->guid)?>" />
                <?php endif; ?>
                <div style="clear:both"></div>
            </div>
        <?php else: ?>
            <div id="lds_license_wrapper" class="lds_view_tab_actions">
                <?php include('license_banner.php'); ?>
                <div style="clear:both"></div>
            </div>
            <div id="lds_unpublish_wrapper" class="lds_view_tab_actions<?php if ($publishedId != $currentDocId) echo ' hidden' ?>">
            <!--
            <?php if ($iseXe): ?>
            <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export_editor?docId=<?php echo $lds->guid ?>"><?php echo T("Save as PDF") ?></a>
            <?php else: ?>
            <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
            <?php endif; ?>
            -->
            <?php echo T("Public link:") ?>
            <input class="lds_publish_url autoselect" type="text" readonly="readonly" value="<?php echo $url ?>v/<?php echo lds_contTools::encodeId($currentDoc->guid)?>" />
                <?php echo T("Embed link:") ?>
                <input class="lds_publish_embed autoselect" type="text" readonly="readonly" value="<?php echo htmlspecialchars('<iframe height="600px" width="100%" frameborder="1" src="' . "{$url}v/" . lds_contTools::encodeId($currentDoc->guid) . '"></iframe>');?>" />
                <div style="clear:both"></div>
            </div>
            <div id="lds_publish_wrapper" class="lds_view_tab_actions<?php if ($publishedId != -1) echo ' hidden' ?>">
                <span style="padding-top:4px; display: block;"><?php echo T("This document is not published.") ?></span>
                <!--
                <?php if ($iseXe): ?>
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export_editor?docId=<?php echo $lds->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php else: ?>
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php endif; ?>
                -->
                <div style="clear:both"></div>
            </div>
            <div id="lds_republish_wrapper" class="lds_view_tab_actions<?php if ($publishedId == $currentDocId || $publishedId == -1) echo ' hidden' ?>">
                <!--
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php if ($iseXe): ?>
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export_editor?docId=<?php echo $lds->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php else: ?>
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/pdf_export?docId=<?php echo $currentDoc->guid ?>"><?php echo T("Save as PDF") ?></a>
                <?php endif; ?>
                -->
                <?php echo T("Public link:") ?>
                <input class="lds_publish_url autoselect" type="text" readonly="readonly" value="<?php echo $url ?>v/<?php echo lds_contTools::encodeId($currentDoc->guid)?>" />
                <?php echo T("<strong>Warning:</strong> An older version of this document is published.") ?>
                <?php echo T("Embed link:") ?>
                <input class="lds_publish_embed autoselect" type="text" readonly="readonly" value="<?php echo htmlspecialchars('<iframe height="600px" width="100%" frameborder="1" src="' . "{$url}v/" . lds_contTools::encodeId($currentDoc->guid) . '"></iframe>');?>" />
                <div style="clear:both"></div>
            </div>
        <?php endif; ?>
    <?php else: ?>
        <br />
    <?php endif; ?>
        <!--
	<?php if ($iseXe): ?>
	<div id="lds_export">
		<?php if ($currentDoc->editorType == 'exe'): ?>
		<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $currentDoc->file_guid . "&title=" . encodeURIComponent($lds->title . ".elp") ?>" style="float: left;"><?php echo T("Save as eXe Learning file") ?></a>
		<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $currentDoc->scorm . "&title=" . encodeURIComponent($lds->title . "_scorm.zip") ?>" style="float: left;"><?php echo T("Save as SCORM") ?></a>
		<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $currentDoc->scorm2004 . "&title=" . encodeURIComponent($lds->title . ".zip") ?>" style="float: left;"><?php echo T("Save as SCORM 2004") ?></a>
		<?php endif; ?>
		<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $currentDoc->ims_ld . "&title=" . encodeURIComponent($lds->title . "_imsld.zip") ?>" style="float: left;"><?php echo T("Save as IMS-LD") ?></a>
		<a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $currentDoc->webZip . "&title=" . encodeURIComponent($lds->title . "_html.zip") ?>" style="float: left;"><?php echo T("Save as zipped web page") ?></a>
	</div>
	<?php endif; ?>
    -->

    <?php if(isset($upload)): ?>
        <?php if($upload): ?>
            <div id="lds_export">
                <a class="publishbutton rightbutton" href="<?php echo $url ?>action/lds/file_export?docId=<?php echo $uploadDoc->file_guid . "&title=" . encodeURIComponent($uploadDoc->upload_filename) ?>" style="float: left;"><?php echo T("Download binary file") ?></a>
            </div>
        <?php endif; ?>
    <?php endif; ?>

<div id="payload" style="width: 100%;">
    <?php if ($currentDoc->getSubtype() == 'LdS_document_editor'): ?>
        <script>
            image = <?php echo ($editor == 'cld' || $editor == 'image') ? 'true':'false';?>;
        </script>
        <?php if ($editor == 'exe'): ?>
            <iframe id="internal_iviewer" src="<?php echo $CONFIG->url ?>content/exe/<?php echo $currentDoc->previewDir ?>/index.html?t=<?php echo rand(0, 1000) ?>" height="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;"></iframe>
        <?php elseif (RestEditor::rest_enabled($editor) && file_exists($CONFIG->editors_content.'content/webcollagerest/'.$currentDoc->previewDir)): ?>
            <iframe id="internal_iviewer" src="<?php echo $CONFIG->url ?>content/webcollagerest/<?php echo $currentDoc->previewDir?>/index.html?t=<?php echo rand(0, 1000) ?>" height="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;"></iframe>
        <?php elseif ($editor == 'cld' || $editor == 'image'): ?>
            <?php echo elgg_view('lds/editor_type/cld', array('entity' => $currentDoc)); ?>
        <?php elseif (strstr($editor, 'google') && file_exists($CONFIG->editors_content.'content/webcollagerest/'.$currentDoc->previewDir)): ?>
            <iframe id="internal_iviewer" src="<?php echo $CONFIG->url ?>content/webcollagerest/<?php echo $currentDoc->previewDir?>/index.html?t=<?php echo rand(0, 1000) ?>" height="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;"></iframe>
        <?php elseif (strstr($editor, 'google')): ?>
            <iframe id="internal_iviewer" src="<?php echo htmlentities($currentDoc->description);?>" height="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;"></iframe>
        <?php elseif ($editor == 'cld' || $editor == 'image'): ?>
            <?php echo elgg_view('lds/editor_type/cld', array('entity' => $currentDoc)); ?>

        <?php elseif ($editor == 'openglm' && is_array($ldsDocs) && count($ldsDocs) > 1): ?>
            <iframe id="internal_iviewer" src="<?php echo $url.'pg/lds/view_iframe/'. $ldsDocs[1]->guid ?>" height="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;">
            </iframe>
        <?php elseif ($editor == 'project_design'): ?>
            <?php
            $jslibs['project'] = true;
            $preview_lds_box = <<<HTML
    <div id="tree_info_popup_shell_empty" class="tooltip_bl_body" style="position:absolute;height:300px;width:400px;background-color: #FFF;overflow:hidden;display:none">
        <div class="tree_info_popup_control">

            <!--
            <div class="tree_info_popup_control_button move">
                <svg width="28px" height="20px"><g>
                    <line x1="8.0" y1="10" x2="20" y2="10" style="stroke:#FFF;stroke-width:2;stroke-linecap:round"></line>
                    <line x1="14" y1="4" x2="14" y2="16" style="stroke:#FFF;stroke-width:2;stroke-linecap:round"></line>
                </g></svg>
            </div>
            -->
            <div class="tree_info_popup_control_button close">
                <svg width="28px" height="20px"><g>
                    <line x1="10" y1="6" x2="18" y2="14" style="stroke:rgb(255,0,0);stroke-width:2;stroke-linecap:round"></line>
                    <line x1="10" y1="14" x2="18" y2="6" style="stroke:rgb(255,0,0);stroke-width:2;stroke-linecap:round"></line>
                </g></svg>
            </div>
            <div class="tree_info_popup_control_button maximize">
                <svg width="28px" height="20px"><g><rect rx="3" ry="3" x="4" y="4" width="20" height="12" style="stroke:#FFF;stroke-width:2;fill:transparent"></rect></g></svg>
            </div>
            <div class="tree_info_popup_control_button minimize">
                <svg width="28px" height="20px"><g><line x1="4.0" y1="16" x2="24" y2="16" style="stroke:#FFF;stroke-width:2;stroke-linecap:round"></line></g></svg>
            </div>
            <div class="tree_info_popup_control_button diff" style="display:none">
                <svg width="28px" height="20px"><g>
                    <line x1="4.0" y1="4" x2="24" y2="4" style="stroke:rgb(255,0,0);stroke-width:2;stroke-linecap:round"></line>
                    <line x1="4.0" y1="8" x2="24" y2="8" style="stroke:rgb(0,255,0);stroke-width:2;stroke-linecap:round"></line>
                    <line x1="4.0" y1="12" x2="24" y2="12" style="stroke:rgb(255,0,0);stroke-width:2;stroke-linecap:round"></line>
                    <line x1="4.0" y1="16" x2="24" y2="16" style="stroke:rgb(0,255,0);stroke-width:2;stroke-linecap:round"></line>
                </g></svg>
            </div>
        </div>
        <div id="tree_info_popup" style="width:100%;height:100%"></div>
    </div>
    <div id="tree_info_popup_move_empty" class="tree_info_popup_move"></div>
HTML;
            ?>

            <script>
                ldproject = <?php echo json_encode(ldshake_project_upgrade(json_decode($lds->description)));?>;
                project_lds_box = <?php echo json_encode($preview_lds_box);?>;
                is_implementation = <?php echo (($lds->getSubtype() != 'LdSProject') ? 'true' : 'false');?>;
                is_project_view = true;
                is_project_edit = false;
                var ldsToBeListed = <?php echo json_encode(lds_contTools::getUserEditableLdS(get_loggedin_userid(), false, 100, 0, null, null, "time", true));?>;
            </script>
            <div id="ldproject_view_grid">

                <?php foreach($CONFIG->project_templates['full'] as $project_template_key => $project_template): ?>
                    <div class="draggable" title="<?php echo htmlspecialchars($project_template['title'])?>">
                        <div style="position:relative;width:inherit;height:inherit;">
                            <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/<?php echo htmlspecialchars((isset($project_template['icon']) ? $project_template['icon'] : $project_template_key))?>.png" toolname="<?php echo htmlspecialchars($project_template['title'])?>" tooltype="<?php echo htmlspecialchars($project_template['type'])?>" <?php if(!empty($project_template['subtype'])):?>subtype="<?php echo htmlspecialchars($project_template['subtype']);?>"<?php endif;?>>
                            <?php if(isset($project_template['icon'])): ?>
                                <div unselectable="on" class="projects_tool_caption"><?php echo htmlspecialchars($project_template['title']);?></div>
                            <?php endif; ?>
                        </div>
                    </div>
                <?php endforeach; ?>

            </div>

            <div class="tooltip_bl" id="t_project_lds" style="width: 200px;">
                <div class="tooltip_bl_body">
                    <div><strong></strong></div>
                </div>
            </div>
        <?php else:?>
            <div id="the_lds" style="height: 380px;padding: 0px;margin: 0px;overflow:scroll;">
                <?php echo $currentDoc->description ?>
            </div>
        <?php endif; ?>
    <?php else:?>
        <script>
            image = false;
        </script>
        <div id="the_lds_wrapper">
            <iframe id="the_lds" src="<?php echo $url.'pg/lds/view_iframe/'. $currentDoc->guid ?>"></iframe>
        </div>
    <?php endif; ?>

</div>
<div id="comment_switcher">
<a href="#lds_info_wrapper">+ View and add comments (<?php echo $nComments ?>)</a>
</div>
<div id="lds_info_wrapper" class="hidden">
	<?php echo elgg_view_comments($lds) ?>
</div>
<?php endif; ?>
</div>
<div class="clearfloat"></div>
<!-- Hidden stuff -->
<div id="shade"></div>

<?php include ('single_share_form.php') ?>
<?php include ('clonelds_form.php') ?>
<?php include ('license_form.php') ?>
<div id="editimplementation_popup" class="lds_popup">
    <a class="lds_close_popup" id="editimplementation_popup_close" href="#"><?php echo T("Cancel") ?></a>
    <h3><?php echo T("Select the editor that you want to use to edit the implementation") ?></h3>

    <div>
        <a href="<?php echo $url.'pg/lds/implementeditor/'?>"><span class="editor-name">WebCollage</span></a>
    </div>
    <div>
        <a href="<?php echo $url.'pg/lds/editglueps/'?>"><span class="editor-name">GLUE!-PS</span></a>
    </div>

</div>
<div class="clearfloat"></div>


<script type="text/javascript">
	var am_i_starter = <?php echo ($am_i_starter ? 'true' : 'false') ?>;
    var new_lds = false;
	var friends = new Array();
	friends['available'] = <?php echo $jsonfriends ?>;
	friends['viewers'] = <?php echo $viewers ?>;
	friends['editors'] = <?php echo $editors ?>;

	var iseXe = <?php if($iseXe)
		echo "true";
	else echo "false";
	?>;

	var groups = <?php echo $groups ?>;

    var lds_license = <?php echo (($lds->license ? $lds->license : 0 )) ?>;

	//Is the LdS public for all LdShakers? (yes by default)
	var allCanView = <?php echo $all_can_read ?>;

    var t9n = {
        unpublishConfirm1 : "<?php echo T("Are you sure you want to unpublish this document?") ?>",
        unpublishConfirm2 : "<?php echo T("All the external links to the document will be broken!") ?>"
    };
</script>

	<?php echo elgg_view('page_elements/jsarea', $vars); ?>
</body>
</html>

