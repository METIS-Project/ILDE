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
<div id="content_area_user_title" style="margin-bottom:10px">
    <h2><a class="tree-box-title" href="<?php echo $url."pg/lds/view/".$lds->guid ?>"><?php echo $lds->title ?></a></h2>
</div>

<div style="margin-bottom:10px">
<?php echo lds_viewTools::all_tag_display ($lds); ?>
</div>


<?php if ($currentDoc->getSubtype() == 'LdS_document_editor'): ?>
    <script>
        image = <?php echo ($editor == 'cld' || $editor == 'image') ? 'true':'false';?>;
    </script>
    <?php if ($editor == 'exe'): ?>
        <iframe id="internal_iviewer" src="<?php echo $CONFIG->url ?>content/exe/<?php echo $currentDoc->previewDir ?>/index.html?t=<?php echo rand(0, 1000) ?>" height="100%" width="957px" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;"></iframe>
    <?php elseif (RestEditor::rest_enabled($editor) && file_exists($CONFIG->editors_content.'content/webcollagerest/'.$currentDoc->previewDir)): ?>
        <iframe id="internal_iviewer" src="<?php echo $CONFIG->url ?>content/webcollagerest/<?php echo $currentDoc->previewDir?>/index.html?t=<?php echo rand(0, 1000) ?>" height="85%" width="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;opacity:0.0;"></iframe>
    <?php elseif ($editor == 'cld' || $editor == 'image'): ?>
        <?php echo elgg_view('lds/editor_type/cld', array('entity' => $currentDoc)); ?>
    <?php elseif (strstr($editor, 'google') && file_exists($CONFIG->editors_content.'content/webcollagerest/'.$currentDoc->previewDir)): ?>
        <iframe id="internal_iviewer" src="<?php echo $CONFIG->url ?>content/webcollagerest/<?php echo $currentDoc->previewDir?>/index.html?t=<?php echo rand(0, 1000) ?>" height="85%" width="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;"></iframe>
    <?php elseif (strstr($editor, 'google')): ?>
        <iframe id="internal_iviewer" src="<?php echo htmlentities($currentDoc->description);?>" height="85%" width="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;"></iframe>
    <?php else:?>
        <div id="the_lds" style="height: 380px;padding: 0px;margin: 0px;overflow:scroll;">
            <?php echo $currentDoc->description ?>
        </div>
    <?php endif; ?>
<?php else:?>
    <script>
        image = false;
    </script>
    <iframe id="internal_iviewer" src="<?php echo $url.'pg/lds/view_iframe/'. $currentDoc->guid ?>" height="85%" width="100%" style="border: 1px solid #aaa;box-shadow: 2px 2px 1px #CCC;opacity:0.0;">
    </iframe>
<?php endif; ?>


<!-- Hidden stuff -->
<div id="shade"></div>