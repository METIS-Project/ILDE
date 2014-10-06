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
<div id="toolbar_lds_types" class="menu">
    <ul>
        <?php
        if($CONFIG->editor_debug)
            if(get_loggedin_user()->editor):
                if($editor = get_entity(get_loggedin_user()->editor)):
                    ?>
                    <li id="tb_new_option_debug" class="menu_suboption"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/<?php echo $editor->internalname?>/"><?php echo $editor->name ?></a></li>
                <?php
                endif;
            endif;
        ?>
        <li id="tb_new_option_conceptualize" class="menu_suboption"><?php echo T("Conceptualize") ?></li>
        <li id="tb_new_option_author" class="menu_suboption"><?php echo T("Author") ?></li>
        <li id="tb_new_option_implement" class="menu_suboption" ><?php echo T("Implement") ?></li>
        <?php if(!$disable_projects):?><li id="tb_new_option_project" class="menu_suboption" ><?php echo T("Project") ?></li><?php endif;?>
    </ul>
</div>
<div id="new_menu_author" class="menu level2">
    <ul>
        <li id="tb_newlds_exe" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/exelearningrest/"><?php echo T("eXeLearning") ?></a></li>
        <li id="tb_newlds_wic" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/webcollagerest/"><?php echo T("WebCollage") ?></a></li>
        <li id="tb_newlds_ogm" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/upload/openglm"><?php echo T("OpenGLM (upload)") ?></a></li>
        <li id="tb_newlds_cmd" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/upload/cadmos"><?php echo T("CADMOS (upload)") ?></a></li>
    </ul>
</div>
<div id="new_menu_implement" class="menu level2">
    <ul>
        <li id="tb_implement_select" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/implementable/"><?php echo T("Select a design for implementation") ?></a></li>
        <li id="tb_implement_register" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/vle/"><?php echo T("Add VLE") ?></a></li>
        <li id="tb_implement_see" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/vledata/"><?php echo T("Configure VLE") ?></a></li>
    </ul>
</div>
<div id="new_menu_conceptualize" class="menu level2">
    <ul>
        <?php if($vars['url'] == "http://ilde.upf.edu/kek/"):?>
            <li id="tb_conceptualize_new11" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/template/kek_p1"><?php echo T("Πρότυπο Διδακτικού Σεναρίου") ?></a></li>
        <?php endif; ?>

        <?php if(isset($CONFIG->project_templates['msf'])):?>
            <?php foreach($CONFIG->project_templates['msf'] as $project_template_key => $project_template): ?>
                <li id="tb_conceptualize_<?php echo $project_template_key?>" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/<?php echo $project_template['type']?>/template/<?php echo $project_template['subtype']?>"><?php echo T($project_template['title']) ?></a></li>
            <?php endforeach; ?>
        <?php endif; ?>

        <li id="tb_newlds_pattern" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/template/coursemap"><?php echo T("Course Map") ?></a></li>
        <li id="tb_conceptualize_new1" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/template/design_pattern"><?php echo T("Design Pattern") ?></a></li>
        <li id="tb_conceptualize_new2" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/template/MDN"><?php echo T("Design Narrative") ?></a></li>
        <li id="tb_conceptualize_new3" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/template/PC"><?php echo T("Persona Card") ?></a></li>
        <li id="tb_conceptualize_new4" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/google_docs/template/PC"><?php echo T("Persona Card (Google Docs)") ?></a></li>
        <li id="tb_conceptualize_new5" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/google_docs/template/CF"><?php echo T("Course Features (Google Docs)") ?></a></li>
        <li id="tb_conceptualize_new6" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/template/FC"><?php echo T("Factors and Concerns") ?></a></li>
        <li id="tb_conceptualize_new7" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/template/HE"><?php echo T("Heuristic Evaluation") ?></a></li>
        <li id="tb_conceptualize_new8" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/upload/cld"><?php echo T("CompendiumLD (upload)") ?></a></li>
        <li id="tb_conceptualize_new9" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/upload/image"><?php echo T("Image (upload)") ?></a></li>
        <li id="tb_conceptualize_new10" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/"><?php echo T("For other conceptualizations") ?></a></li>
        <li id="tb_conceptualize_new4" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/google_spreadsheet"><?php echo T("Google Spreadsheet") ?></a></li>
    </ul>
</div>
<?php if(!$disable_projects):?>
    <div id="new_menu_project" class="menu level2">
        <ul>
            <li id="tb_newlds_manage_projects" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/projects/implement"><?php echo T("Select Workflow for my Project") ?></a></li>
            <li id="tb_newlds_add_project" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new_project"><?php echo T("Add Workflow") ?></a></li>
            <li id="tb_newlds_edit_project" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/projects"><?php echo T("Edit Workflow") ?></a></li>
        </ul>
    </div>
<?php endif; ?>
