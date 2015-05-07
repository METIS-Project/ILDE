<div id="toolbar_lds_types" class="menu">
    <ul>
        <li id="tb_conceptualize_new10" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/"><?php echo T("Document") ?></a></li>
        <li id="tb_conceptualize_new5" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/exelearningrest/"><?php echo T("eXeLearning") ?></a></li>
        <li id="tb_conceptualize_new4" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/webcollagerest"><?php echo T("WebCollage") ?></a></li>
    </ul>
</div>
<?php if(empty($CONFIG->disable_projects)):?>
    <div id="new_menu_project" class="menu level2">
        <ul>
            <li id="tb_newlds_manage_projects" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/projects/implement"><?php echo T("Select Workflow for my Project") ?></a></li>
            <li id="tb_newlds_add_project" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new_project"><?php echo T("Add Workflow") ?></a></li>
            <li id="tb_newlds_edit_project" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/projects"><?php echo T("Edit Workflow") ?></a></li>
        </ul>
    </div>
<?php endif; ?>