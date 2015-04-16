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

extract($vars);
    $project_stages = array(
        'conceptualization',
        'authoring',
        'implementation'
    );
?>
<div id="lds_editor_iframe">

<div id="ldproject_conceptualize_grid"> <!-- This will act as design container -->
    <div id="two_column" style="padding-bottom:0 !important">

        <!--<div id="ldproject_toolBar" style="width: <?php echo (18 + 67*(ceil(count($CONFIG->project_templates['full'])/14)))?>px;">-->
        <div id="ldproject_toolBar" style="width: 100%; height: <?php echo (18 + 67*(ceil(count($CONFIG->project_templates['full'])/14)))?>px;">
            <?php foreach($project_stages as $p_s):?>
                <?php foreach($CONFIG->project_templates['full'] as $project_template_key => $project_template): ?>
                    <?php if($project_template['stage'] == $p_s):?>
                        <div class="draggable_wrapper">
                            <div class="project-stage-<?php echo $p_s;?> draggable" title="<?php echo htmlspecialchars($project_template['title'])?>">
                                <div style="position:relative;width:inherit;height:inherit;">
                                    <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/<?php echo htmlspecialchars((isset($project_template['icon']) ? $project_template['icon'] : $project_template_key))?>.png" toolname="<?php echo htmlspecialchars($project_template['title'])?>" tooltype="<?php echo htmlspecialchars($project_template['type'])?>" <?php if(!empty($project_template['subtype'])):?>subtype="<?php echo htmlspecialchars($project_template['subtype']);?>"<?php endif;?>>
                                    <?php if(isset($project_template['icon'])): ?>
                                    <div unselectable="on" class="projects_tool_caption"><?php echo htmlspecialchars($project_template['title']);?></div>
                                    <?php endif; ?>
                                </div>
                            </div>
                        </div>
                    <?php endif;?>
                <?php endforeach; ?>
            <?php endforeach; ?>
            <div id="project-editor-legend">
                <ol>
                    <li id="project-editor-legend-conceptualization"><?php echo T('Conceptualization')?></li>
                    <li id="project-editor-legend-authoring"><?php echo T('Authoring')?></li>
                    <li id="project-editor-legend-implementation"><?php echo T('Implementation')?></li>
                </ol>
            </div>
        </div>
    </div>
    <div style="clear:both"></div>
    <!--<div id="droppable_grid" class="" type="conceptualize" style="width: <?php echo (934 - 95*(ceil(count($CONFIG->project_templates['full'])/7)))?>px;">-->
    <div id="droppable_grid" class="" type="conceptualize" style="width: 100%;">
        <div id="project_add_note"><?php echo T("sticky note"); ?></div>
    </div>
    <div style="clear:both"></div>
</div>

<div id="lds_attachment_popup" class="lds_att_popup">
</div>

<div id="editor_shade" style="display: none;"></div>

<script type="text/javascript">

    /*  Here my Vars in order to send it to JSON --> */
    var is_project_edit = true;
    var is_project_view = false;
    var is_implementation = <?php echo (!empty($is_implementation) ? 'true' : 'false')?>;
    var ldsToBeListed =<?php echo $ldsToBeListed?>;
    var ldproject =<?php echo json_encode(ldshake_project_upgrade(json_decode($ldproject)))?>;
    var totalToLoad = ldproject.length;
    var toolLoaded = 0;
    var $fmr = document.getElementById("droppable_grid").targetTop;

</script>
</div>