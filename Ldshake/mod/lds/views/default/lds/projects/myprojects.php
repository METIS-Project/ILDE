<?php
/*********************************************************************************
 * LdShake is a platform for the social sharing and co-edition of learning designs
 * Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
 *
 * (Contributors, alpha. order) Abenia, P., Carralero, M.A., ChacÃ³n, J., HernÃ¡ndez-Leo, D., Moreno, P.
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
$edit_imp_label = $implement ? T("Implement") : T("Edit");
$edit_imp_url = isset($implement) ? "implement" : "edit";
$view_mode = isset($implement_list) ? "list" : "view";
$imp_action = isset($implement) ? "project_implement_action" : "lds_edit_action";
$is_implementation = !empty($is_implementation);
$is_workflow = !empty($is_workflow);
?>
<div id="two_column_left_sidebar">
    <div id="owner_block">
        <?php if($section != 'off'): ?>
            <ul id="lds_side_sections_prj">
                <li><a<?php if ($section == 'prj-') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/projects_implementations' ?>"><?php echo T("All my projects") ?></a></li>
                <li><a<?php if ($section == 'prj-created-by-me') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/projects_implementations/created-by-me' ?>"><?php echo T("Created by me") ?></a></li>
                <li><a<?php if ($section == 'prj-shared-with-me') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/projects_implementations/shared-with-me' ?>"><?php echo T("Shared with me") ?></a></li>
                <li><a<?php if ($section == 'prj-trashed') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/projects_implementations/trashed' ?>"><?php echo T("Trashed") ?></a></li>
            </ul>

            <ul id="lds_side_sections">
                <li><a<?php if ($section == '') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('mine') ?>"><?php echo T("All my LdS") ?></a></li>
                <li><a<?php if ($section == 'created-by-me') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('created-by-me') ?>"><?php echo T("Created by me") ?></a></li>
                <li><a<?php if ($section == 'shared-with-me') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('shared-with-me') ?>"><?php echo T("Shared with me") ?></a></li>
                <li><a<?php if ($section == 'trashed') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('trashed') ?>"><?php echo T("Trashed") ?></a></li>
            </ul>

            <ul id="lds_side_sections_imp">
                <li><a<?php if ($section == 'imp') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations' ?>"><?php echo T("All my implementations") ?></a></li>
                <li><a<?php if ($section == 'imp-created-by-me') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations/created-by-me' ?>"><?php echo T("Created by me") ?></a></li>
                <li><a<?php if ($section == 'imp-shared-with-me') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations/shared-with-me' ?>"><?php echo T("Shared with me") ?></a></li>
                <li><a<?php if ($section == 'imp-trashed') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations/trashed' ?>"><?php echo T("Trashed") ?></a></li>
            </ul>
        <?php endif; ?>
    </div>
    <div id="owner_block_bottom"></div>
</div>

<div id="two_column_left_sidebar_maincontent">
    <div id="content_area_user_title">
        <?php if ($filtering): ?>
            <h2><a href="<?php echo lds_viewTools::getUrl() ?>"><?php echo $title ?></a> Â» <span class="lds_tag <?php echo $tagk ?>"><?php echo $tagv ?></span></h2>
        <?php else: ?>
            <h2><?php echo $title ?></h2>
        <?php endif; ?>
    </div>

    <div class="filters">
    </div>

    <?php if (is_array($list) && sizeof($list) > 0): ?>
        <form method="post" action="#">
            <div id="my_lds_list_header">
                <?php if ($section != 'prj-trashed'): ?>
                    <input id="lds_select_all" class="lds_select" type="checkbox" name="lds_group_select" value="" />
                    <input type="submit" style="border-color:#999; margin:5px 0;" id="trash_some" name="trash_some" value="<?php echo T("Trash selected ".$list_type) ?>" />
                    <?php if ($is_workflow): ?>
                        <input type="button" style="border-color:#999; margin:5px 0;" id="duplicate_design" value="<?php echo T("Duplicate workflow") ?>" disabled="disabled" />
                    <?php endif; ?>
                <?php else: ?>
                    <input id="lds_select_all" class="lds_select" type="checkbox" name="lds_group_select" value="" />
                    <input type="submit" style="border-color:#999; margin:5px 0;" id="untrash_some" name="untrash_some" value="<?php echo T("Recover selected ".$list_type) ?>" />
                <?php endif; ?>

                <?php if($is_implementation): ?>
                    <input onclick="window.location='<?php echo lds_viewTools::url_for($implementation, 'view');?>'" type="button" style="border-color:#999; margin:5px 0;" id="view_workflow" name="view_workflow" value="<?php echo T("View project") ?>" />
                    <input onclick="window.location='<?php echo lds_viewTools::url_for($implementation, 'edit');?>'" type="button" style="border-color:#999; margin:5px 0;" id="view_workflow" name="view_workflow" value="<?php echo T("Edit project") ?>" />
                <?php endif; ?>

                <!--
            <input type="button" style="border-color:#999; margin:5px 0;" id="duplicate_design" value="<?php echo T("Duplicate design") ?>" disabled="disabled" />
            <input type="button" style="border-color:#999; margin:5px 0;" id="implementations_by_design" value="<?php echo T("See implementations") ?>" disabled="disabled" />
            <input type="button" style="border-color:#999; margin:5px 0;" id="implement_project" value="<?php echo T("Implement project") ?>" disabled="disabled" />
            -->
            </div>
            <ul id="my_lds_list">
                <?php foreach($list as $item): ?>
                    <?php if ($section != 'prj-trashed'): ?>
                        <li class="lds_list_element<?php if ($item->locked): ?> lds_locked<?php endif; ?><?php if ($item->new): ?> new<?php endif; ?>">
                            <?php if ($item->starter->guid == get_loggedin_userid()): ?>
                                <input class="lds_select lds_select_one" type="checkbox" name="lds_select" value="<?php echo $item->lds->guid ?>" />
                            <?php else: ?>
                                <!--<div class="lds_select_spacer"></div>-->
                                <input class="lds_select lds_select_one" type="checkbox" name="lds_select" value="<?php echo $item->lds->guid ?>" />
                            <?php endif; ?>
                            <a href="<?php echo lds_viewTools::url_for($item->lds, 'view') ?>" class="lds_icon"><img src="<?php echo $url ?>mod/lds/images/lds-<?php echo (lds_viewTools::iconSupport($item->lds->editor_type) ? $item->lds->editor_type : 'doc'); ?>-icon-20.png" alt="LdS" /></a>
                            <div class="lds_info">
					<span class="lds_title_tags">
       					<?php if (!$item->locked): ?>
                            <?php
                            $vle_implementation = (empty($item->glueps) and !empty($item->implementation)) ? " lds_select_implement_action": "";
                            ?>
                            <a class="<?php echo $imp_action.$vle_implementation; ?>" href="<?php echo lds_viewTools::url_for($item->lds, $edit_imp_url) ?>" lds="<?php echo $item->lds->guid;?>" project_guid="<?php echo $item->lds->guid;?>"><?php echo $edit_imp_label; ?></a>
                        <?php endif; ?>

                        <a class="lds_title<?php if ($item->new): ?> new<?php endif; ?><?php if ($item->locked): ?> lds_padded<?php endif; ?>" href="<?php echo lds_viewTools::url_for($item->lds, $view_mode) ?>"><?php echo $item->lds->title ?></a>
                        <?php if(ldshake_glueps_isimplementable($item->lds->editor_type) and !empty($vle_info)): ?>
                            <a class="lds_implement_action lds_edit_action" href="<?php echo lds_viewTools::url_for($item->lds, $edit_imp_url) ?>" project_guid="<?php echo $implementation->guid;?>" lds_id="<?php echo $item->lds->guid ?>" vle_id="<?php echo $vle_info->item->guid;?>" class="lds_implement_action" deploy="true" style="width: auto; float: none;"><?php echo T("Implement"); ?></a>
                        <?php endif; ?>

                        <?php echo lds_viewTools::all_tag_display ($item->lds) ?>
					</span>
                                <span class="lds_people"><?php echo $item->starter->name ?> <?php echo T('to');?> <?php echo $item->num_editors ?> editor<?php if ($item->num_editors != 1): ?><?php echo T('s');?><?php endif; ?>, <?php if($item->num_viewers == -1): ?><?php echo T('all');?><?php else: ?><?php echo $item->num_viewers ?><?php endif; ?> <?php if ($item->num_viewers != 1): ?><?php echo T("viewers"); ?><?php else: ?><?php echo T("viewer"); ?><?php endif; ?></span>
                                <?php if ($item->locked): ?>
                                    <span class="lds_editing_by"><?php echo $item->locked_by->name ?> is editing now</span>
                                <?php endif; ?>
                                <span class="lds_date"><?php echo friendly_time($item->lds->time_updated, false, true) ?></span>
                                <div class="clearfloat"></div>
                            </div>
                        </li>
                    <?php else: ?>
                        <li class="lds_list_element">
                            <input class="lds_select lds_select_one" type="checkbox" name="lds_select" value="<?php echo $item->lds->guid ?>" />
                            <a href="<?php echo lds_viewTools::url_for($item->lds, 'viewtrashed') ?>" class="lds_icon"><img src="<?php echo $url ?>mod/lds/images/lds-doc-icon-20.png" /></a>
                            <div class="lds_info">
					<span class="lds_title_tags">
						<a class="lds_title lds_padded" href="<?php echo lds_viewTools::url_for($item->lds, 'viewtrashed') ?>"><?php echo $item->lds->title ?></a>
                        <?php echo lds_viewTools::all_tag_display ($item->lds) ?>
					</span>
                                <span class="lds_people"><?php echo $item->starter->name ?> <?php echo T('to');?> <?php echo $item->num_editors ?> editor<?php if ($item->num_editors != 1): ?><?php echo T('s');?><?php endif; ?>, <?php if($item->num_viewers == -1): ?><?php echo T('all');?><?php else: ?><?php echo $item->num_viewers ?><?php endif; ?> <?php if ($item->num_viewers != 1): ?><?php echo T("viewers"); ?><?php else: ?><?php echo T("viewer"); ?><?php endif; ?></span>
                                <span class="lds_date"><?php echo friendly_time($item->lds->time_updated, false, true) ?></span>
                                <div class="clearfloat"></div>
                            </div>
                        </li>
                    <?php endif; ?>
                <?php endforeach; ?>
            </ul>
        </form>
    <?php else: ?>
        <p class="noresults"><?php echo T("Oops, no LdS here!") ?></p>
    <?php endif; ?>
    <div class="filters">
    </div>
</div>

<?php include (__DIR__.'/implementprojectlds_form.php');?>
<?php include (__DIR__.'/cloneworkflow_form.php');?>
<?php include (__DIR__.'/new_projectimplementation_form.php');?>
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