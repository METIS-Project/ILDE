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


/**
 * LdShake top toolbar
 * 
 */
global $CONFIG;

$disable_projects = $CONFIG->disable_projects;
$static_resources = ldshake_theme_static("shell");
if (isloggedin()) :
?>
<div id="ldshake_topbar">
	<div id="ldshake_topbar_wrapper">
        <div id="ldshake_topbar_container_right">
            <div style="float:right;">
                <a href="#" id="ldshake_topbar_menu_switch">
                    <?php $name = $vars['name']; ?>
                    <span id="ldshake_topbar_user_options"><?php echo (strlen($name) > 15 ? substr($name,0,13).'...' : $name); ?></span>
                </a>

                <ul id="ldshake_topbar_user_menu">
                    <?php if($CONFIG->editor_debug): ?>
                        <li><a href="<?php echo $vars['url']; ?>pg/lds/debug/"><?php echo T("Developer settings") ?></a></li>
                    <?php endif; ?>
                    <li><a href="<?php echo $vars['url']; ?>pg/settings/"><?php echo T("Account settings") ?></a></li>
                    <?php if ($vars['is_admin']): ?>
                        <li><a href="<?php echo $vars['url']; ?>pg/admin/"><?php echo T("Site administration") ?></a></li>
                    <?php endif; ?>
                    <li><a href="<?php echo $vars['url']; ?>action/logout"><?php echo T("Log out") ?></a></li>
                </ul>
            </div>
            <div id="ldshake_topbar_avatar">
                <a href="<?php echo $vars['url']. 'pg/ldshakers/' . $vars['username'] ?>/"><img height="25" width="25" class="user_mini_avatar" src="<?php echo $vars['url'].'pg/icon/'.$vars['username'].'/tiny?t='.hash("crc32b",$vars['user']->time_updated); ?>" alt="<?php echo $vars['name'] ?>"/></a>
            </div>
            <div id="ldshake_topbar_serach">
                <form id="searchform" action="<?php echo $vars['url']; ?>pg/lds/search/" method="get">
                    <span id="ldshake_topbar_search_submit"><a href="#"><img width="19" height="16" src="<?php echo $vars['url']; ?>mod/topbar_ldshake/graphics/search.png" /></a></span>
                    <input type="text" size="21" name="q" placeholder="<?php echo T('Search %1', 'LdS')?>" id="ldshake_topbar_search_input" />
                </form>
            </div>
        </div>
		<div id="ldshake_topbar_container_left">
			<div id="ldshake_topbar_logo">
				<a href="<?php echo $vars['url']; ?>pg/lds/"><?php echo $static_resources["topbar_logo"]; ?></a>
			</div>
			<div id="toolbarlinks">
				<ul id="toolbar_options">
					<li id="tb_newlds"><a href="<?php echo $vars['url']; ?>pg/lds/new/"><?php echo T("New LdS") ?></a></li>
					<!--<li id="tb_newlds"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/webcollage/">New WebCollage LdS</a></li-->
					<li id="tb_mylds"><a href="<?php echo $vars['url']; ?>pg/lds/"><?php echo T("My LdS") ?></a></li>
					<li id="tb_browselds"><a href="<?php echo $vars['url']; ?>pg/lds/browse/"><?php echo T("Browse LdS") ?></a></li>
					<li id="tb_ldshakers"><a href="<?php echo $vars['url']; ?>pg/ldshakers/"><?php echo T("LdShakers") ?></a></li>
					<?php
					//allow people to extend this top menu
					echo elgg_view('elgg_topbar/extend', $vars);
					?>
                    <li id="tb_about"><a href="<?php echo $vars['url']; ?>pg/lds/about/"><?php echo T("About") ?></a></li>

				</ul>
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
            </div>
		</div>
	</div>
</div>

<div style="clear:both;"></div>
<?php endif;?>

<div id="min_version" class="lds_popup">
    <a class="lds_close_popup" id="ldsimplement_popup_close" href="#"><?php echo T("Cancel") ?></a>

    <div class="browseralert">Your browser does not support some of the required features to run this webapp.<br />Upgrade to a newer browser (e.g. <a href="http://www.google.com/chrome/" target="_blank">Chrome</a>, <a href="http://www.microsoft.com/en-us/download/internet-explorer-11-details.aspx" target="_blank">Internet Explorer 11</a>), <a href="http://www.firefox.com" target="_blank">Firefox</a>, <a href="http://www.opera.com" target="_blank">Opera</a> for a better browsing experience.</div>
</div>

<?php
    $new_tooltips = array(
        'tb_new_option_conceptualize'   => T('Options for starting to think about your learning design.'),
        'tb_new_option_author'          => T('Options for creating learning activities (units of learning) and learning materials.'),
        'tb_new_option_implement'       => T('Options for being able to transfer the authored activities and materials to a VLE.'),
        'tb_new_option_project'         => T('Options to structure your learning design work.'),

        'tb_newlds_exe' => T('Create educational web content.'),
        'tb_newlds_wic' => T('Create a unit of learning including collaborative activities based on patterns.'),
        'tb_newlds_ogm' => T('Create/upload a unit of learning using the OpenGLM authoring tool.'),
        'tb_newlds_cmd' => T('Create/upload a unit of learning using the CADMOS authoring tool.'),

        'tb_implement_select' => T('Select an authored design to implement in a VLE.'),
        'tb_implement_register' => T('Add (and configure) a new VLE in which to deploy authored designs.'),
        'tb_implement_see' => T('Manage the list of you configured VLEs.'),

        'tb_newlds_add_project' => T('Define a new workflow.'),
        'tb_newlds_edit_project' => T('See list of workflows I can edit.'),
        'tb_newlds_manage_projects' => T('See list of suggested workflows to start your project.'),
    );
?>
<script>
    var ldshake_toolbar_tooltips = <?php echo json_encode($new_tooltips); ?>
</script>

<?php foreach($new_tooltips as $key => $value): ?>
<div class="tooltip_bl" id="t_<?php echo $key ?>" style="width: 180px;">
    <div class="tooltip_bl_body">
        <div><strong><?php echo $value ?></strong></div>
    </div>
</div>
<?php endforeach; ?>

<div class="tooltip_bl" id="t_s1t" data-pos="#tb_ldshakers@-60,29" style="width:180px">
    <div class="tooltip_bl_stem"></div>
    <div class="tooltip_bl_body">
        <div><strong><?php echo T("See list of members in the community.") ?></strong></div>
    </div>
</div>

<div class="tooltip_bl" id="t_s2t" data-pos="#tb_mylds@-60,29" style="width:180px">
    <div class="tooltip_bl_stem"></div>
    <div class="tooltip_bl_body">
        <div><strong><?php echo T("See list of projects, concepts, designs and implementations I can edit.") ?></strong></div>
    </div>
</div>

<div class="tooltip_bl" id="t_s3t" data-pos="#tb_browselds@-60,29" style="width:180px">
    <div class="tooltip_bl_stem"></div>
    <div class="tooltip_bl_body">
        <div><strong><?php echo T("See list of learning design solutions I can edit or view.") ?></strong></div>
    </div>
</div>

<div class="tooltip_bl" id="t_s4t" data-pos="#tb_newlds@-60,29" style="width:180px">
    <div class="tooltip_bl_stem"></div>
    <div class="tooltip_bl_body">
        <div><strong><?php echo T("Create a new learning design solution (project, concept, design, implementation).") ?></strong></div>
    </div>
</div>

<div class="tooltip_bl" id="t_s5t" data-pos="#tb_about@-70,29" style="width:180px">
    <div class="tooltip_bl_stem"></div>
    <div class="tooltip_bl_body">
        <div><strong><?php echo T("Information about the environment.") ?></strong></div>
    </div>
</div>

<div class="tooltip_bl" id="t_s6t" data-pos="#tb_messages@-80,29" style="width:180px">
    <div class="tooltip_bl_stem"></div>
    <div class="tooltip_bl_body">
        <div><strong><?php echo T("Private messages to community members.") ?></strong></div>
    </div>
</div>