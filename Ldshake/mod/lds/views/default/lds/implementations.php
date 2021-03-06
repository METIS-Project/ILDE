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

<?php extract($vars) ?>
<div id="two_column_left_sidebar">
    <div id="owner_block">
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
            <li><a<?php if ($section == 'imp-') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations' ?>"><?php echo T("All my implementations") ?></a></li>
            <li><a<?php if ($section == 'imp-created-by-me') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations/created-by-me' ?>"><?php echo T("Created by me") ?></a></li>
            <li><a<?php if ($section == 'imp-shared-with-me') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations/shared-with-me' ?>"><?php echo T("Shared with me") ?></a></li>
            <li><a<?php if ($section == 'imp-trashed') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations/trashed' ?>"><?php echo T("Trashed") ?></a></li>
        </ul>
    </div>
    <div id="owner_block_bottom"></div>
</div>

<div id="two_column_left_sidebar_maincontent">
    <div id="content_area_user_title">
        <?php if ($filtering): ?>
            <h2><a href="<?php echo lds_viewTools::getUrl() ?>"><?php echo $title ?></a> » <span class="lds_tag <?php echo $tagk ?>"><?php echo $tagv ?></span></h2>
        <?php else: ?>
            <h2><?php echo $title ?></h2>
        <?php endif; ?>
    </div>

    <?php echo T("Filter by design") ?>
    <select id="filter_by_design">
        <option value="0"><?php echo T("Select a design") ?></option>
        <?php foreach($designfilter as $df): ?>
            <?php if(strlen($df->title)):?>
            <option value="<?php echo $df->guid?>"><?php echo $df->title ?></option>
            <?php endif; ?>
        <?php endforeach; ?>
    </select>

    <div class="filters">
        <div class="paging">
            <?php echo lds_viewTools::pagination($count, $limit) ?>
        </div>
    </div>

    <?php if (is_array($list) && sizeof($list) > 0): ?>
        <?php if ($section != 'imp-trashed'): ?>
            <div class="<?php if(isset($design_filter)) echo "implementation-list-by-design"; else echo "implementation-list"; ?>">
            <?php echo elgg_view('lds/browselist', $vars) ?>
            </div>

        <?php else: ?>
            <form method="post" action="#">
                <div id="my_lds_list_header">
                    <input id="lds_select_all" class="lds_select" type="checkbox" name="lds_group_select" value="" />
                    <?php if ($section != 'imp-trashed'): ?>
                        <input type="submit" style="border-color:#999; margin:5px 0;" id="trash_some" name="trash_some" value="<?php echo T("Trash selected implementations") ?>" />
                    <?php else: ?>
                        <input type="submit" style="border-color:#999; margin:5px 0;" id="untrash_some" name="untrash_some" value="<?php echo T("Recover selected LdS") ?>" />
                    <?php endif; ?>
                    <!--<input type="button" style="border-color:#999; margin:5px 0;" id="duplicate_implementation" value="<?php echo T("Duplicate implementation") ?>" disabled="disabled" />-->
                    <input type="button" style="border-color:#999; margin:5px 0;" id="view_design" value="<?php echo T("View original design") ?>" disabled="disabled" />
                </div>
                <ul id="my_lds_list">
                        <?php foreach($list as $item): ?>
                        <li class="lds_list_element">
                            <input class="lds_select lds_select_one" type="checkbox" name="lds_select" value="<?php echo $item->implementation->guid ?>" />
                            <a href="#" class="lds_icon"><img src="<?php echo $url ?>mod/lds/images/lds-doc-icon-20.png" /></a>
                            <div class="lds_info">
                            <span class="lds_title_tags">
                                <a class="lds_title lds_padded" href="#"><?php echo $item->implementation->title ?></a>
                                <?php echo lds_viewTools::all_tag_display ($item->implementation) ?>
                            </span>
                            <span class="lds_people"><?php echo $item->starter->name ?> to <?php echo $item->num_editors ?> editor<?php if ($item->num_editors != 1): ?>s<?php endif; ?>, <?php if($item->num_viewers == -1): ?>all<?php else: ?><?php echo $item->num_viewers ?><?php endif; ?> viewer<?php if ($item->num_viewers != 1): ?>s<?php endif; ?></span>
                            <span class="lds_date"><?php echo friendly_time($item->lds->time_updated, false, true) ?></span>
                            </div>
                            <div class="clearfloat"></div>
                        </li>
                        <?php endforeach; ?>
                </ul>
            </form>
        <?php endif; ?>
    <?php else: ?>
        <p class="noresults"><?php echo T("Oops, no implementations here!") ?></p>
    <?php endif; ?>


    <div class="filters">
        <div class="paging">
            <?php echo lds_viewTools::pagination($count, $limit) ?>
        </div>
    </div>
</div>

<div id="cloneimplementation_popup" class="lds_popup">
    <a class="lds_close_popup" id="ldsimmplementation_popup_close" href="#"><?php echo T("Cancel") ?></a>
    <h3><?php echo T("Enter the implementation title") ?></h3>
    <input type="text" name="new_implementation_title" />
    <br />
    <div id="cloneimplementation_submit_incomplete" style="display:none;color:red"><?php echo T("You must introduce a title!");?></div>
    <input type="button" id="cloneimplementation_submit" value="<?php echo T('Duplicate')?>" />
</div>

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