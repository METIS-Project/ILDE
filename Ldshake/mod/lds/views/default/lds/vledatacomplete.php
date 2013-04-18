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
<?php
extract($vars);
?>

<div id="two_column_left_sidebar">
    <div id="owner_block">
        <ul id="lds_side_sections">
            <li><a<?php if ($section == '') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('mine') ?>"><?php echo T("All my LdS") ?></a></li>
            <li><a<?php if ($section == 'created-by-me') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('created-by-me') ?>"><?php echo T("Created by me") ?></a></li>
            <li><a<?php if ($section == 'shared-with-me') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('shared-with-me') ?>"><?php echo T("Shared with me") ?></a></li>
            <li><a<?php if ($section == 'trashed') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('trashed') ?>"><?php echo T("Trashed") ?></a></li>
            <li><a<?php if ($section == '') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations' ?>"><?php echo T("All my implementations") ?></a></li>
            <li><a<?php if ($section == 'created-by-me') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations/created-by-me' ?>"><?php echo T("Created by me") ?></a></li>
            <li><a<?php if ($section == 'shared-with-me') echo ' class="current"' ?> href="<?php echo $url . 'pg/lds/implementations/shared-with-me' ?>"><?php echo T("Shared with me") ?></a></li>
            <li><a<?php if ($section == 'trashed') echo ' class="current"' ?> href="<?php echo lds_viewTools::getUrl('trashed') ?>"><?php echo T("Trashed") ?></a></li>
        </ul>
    </div>
    <div id="owner_block_bottom"></div>
</div>

<div id="two_column_left_sidebar_maincontent">
    <div id="lds_vle_page">
        <?php
        $courses = (array)$vle_info->courses;
        ksort($courses);
        foreach($courses as $key => $fvle):?>

        <div id="vle_test_box">
            <div class="vle_info_title lds_form_title"><?php echo T('Participants for course') . " {$fvle}" ?></div>
            <div class="vle_info_box">
                <?php
                foreach($participants[$key] as $p_id => $p):?>
                    <div class="vle_info_element">
                        <span class="vle_info_key_data"><?php echo $p->id ?></span>
                        <span class="vle_info_name_data"><?php echo $p->name?></span>
                        <span class="vle_info_other_data"><?php echo $p->learningEnvironmentData?></span>
                    </div>
                <?php endforeach;?>
            </div>
        </div>
        <?php endforeach;?>

        <div id="vle_test_box">
            <div class="vle_info_title lds_form_title"><?php echo T('Internal tools') ?></div>
            <div class="vle_info_box">
                <?php
                foreach($internal_tools as $p_id => $p):?>
                    <div class="vle_info_element">
                        <span class="vle_info_key_data"><?php echo $p_id ?></span>
                        <span class="vle_info_name_data"><?php echo $p?></span>
                    </div>
                <?php endforeach;?>
            </div>
        </div>

        <div id="vle_test_box">
            <div class="vle_info_title lds_form_title"><?php echo T('External tools') ?></div>
            <div class="vle_info_box">
                <?php
                foreach($external_tools as $p_id => $p):?>
                    <div class="vle_info_element">
                        <span class="vle_info_key_data"><?php echo $p_id ?></span>
                        <span class="vle_info_name_data"><?php echo $p?></span>
                    </div>
                <?php endforeach;?>
            </div>
        </div>

    </div>
</div>