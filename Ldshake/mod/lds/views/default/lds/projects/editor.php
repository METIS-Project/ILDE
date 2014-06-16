<?php extract($vars); ?>
<div id="lds_editor_iframe">

<div id="ldproject_conceptualize_grid"> <!-- This will act as design container -->
    <div id="two_column" style="padding-bottom:0 !important">
        <div id="droppable_grid" class="" type="conceptualize" style="width: <?php echo (934 - 95*(ceil(count($CONFIG->project_templates['full'])/7)))?>px;">
            <div id="project_add_note">sticky note</div>
        </div>
        <div id="ldproject_toolBar" style="width: <?php echo (8 + 95*(ceil(count($CONFIG->project_templates['full'])/7)))?>px;">

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
    </div>
    <div style="clear:both"></div>
</div>

<div id="lds_attachment_popup" class="lds_att_popup">
</div>

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