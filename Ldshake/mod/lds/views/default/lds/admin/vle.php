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
        <div class="vle_list_add_item"><a href="<?php echo "{$url}pg/lds/vle/"?>"><?php echo T('Add a new VLE'); ?></a></div>

        <div class="ldshake_sidebar_block_title"><?php echo T("Your VLEs"); ?></div>
        <?php    foreach($vlelist as $vvle):?>
            <div class="vle_list_item <?php echo ($vvle->guid == $vle_id ? 'current' : '');?>"><a href="<?php echo "{$url}pg/lds/vle/{$vvle->guid}"?>"><?php echo $vvle->name ?></a></div>
        <?php endforeach;?>
    </div>
</div>

<div id="two_column_left_sidebar_maincontent">
    <div id="lds_vle_page">

        <!--<div class="lds_form_title">
            <?php if($vle->new)
                echo T('Enter your new VLE details');
            else
                echo T('Update ').'<i>'.$vle->name.'</i> configuration';
            ?>
        </div>-->


        <div id="content_area_user_title">
            <h2>            <?php if($vle->new)
                    echo T('Enter your new VLE details');
                else
                    echo T('Update ').'<i>'.$vle->name.'</i> configuration';
                ?>
            </h2>
        </div>

        <form id="lds_vle_form" action="<?php echo $CONFIG->url . 'action/lds/manage_vle' ?>" method="post" >

            <div class="lds_form_block">
                <span class="vle_form_label"><?php echo T('VLE name')?></span>
                <input class="vle_form_input" type="text" name="vle_name" value="<?php echo htmlspecialchars($vle->name)?>" />
            </div>
            <div class="lds_form_block">
                <span class="vle_form_label"><?php echo T('VLE type')?></span>
                <input class="vle_form_input" type="text" name="vle_type" value="<?php echo htmlspecialchars($vle->vle_type)?>" />
            </div>
            <div class="lds_form_block">
                <span class="vle_form_label"><?php echo T('VLE url')?></span>
                <input class="vle_form_input" type="text" name="vle_url" value="<?php echo htmlspecialchars($vle->vle_url)?>" />
            </div>
            <div class="lds_form_block">
                <span class="vle_form_label"><?php echo T('VLE username')?></span>
                <input class="vle_form_input" type="text" name="vle_username" value="<?php echo htmlspecialchars($vle->username)?>" />
            </div>
            <div class="lds_form_block">
                <span class="vle_form_label"><?php echo T('VLE password')?></span>
                <input class="vle_form_input" type="password" name="vle_password" value="<?php echo htmlspecialchars($vle->password)?>" />
            </div>
            <div class="lds_form_block">
                <span class="vle_form_label"><?php echo T('VLE version')?></span>
                <input class="vle_form_input" type="text" name="vle_version" value="<?php echo htmlspecialchars($vle->version)?>" />
            </div>
            <div class="lds_form_block">
                <span class="vle_form_label"><?php echo T('VLE wstoken')?></span>
                <input class="vle_form_input" type="text" name="vle_wstoken" value="<?php echo htmlspecialchars($vle->wstoken)?>" />
            </div>

            <input type="hidden" name="vle_id" value="<?php echo $vle_id;?>" />
            <input id="vle_delete_flag" type="hidden" name="vle_delete_flag" value="0" />

            <input class="vle_form_submit" type="submit" name="vle_submit" value="<?php echo T('Confirm')?>" />   <?php if(!$vle->new) {?><input id="vle_delete" class="vle_form_submit" type="submit" name="vle_delete" value="<?php echo T('Delete')?>" /><?php }?>


        </form>

        <div id="vledata_submit_incomplete" style="display:none;color:red"><?php echo T("You must fill every field!");?></div>

        <?php if($vle_info instanceof stdClass):?>
            <div id="vle_test_box">
                <div class="vle_info_working"><?php echo T('Your VLE configuration is working properly, these are the available courses')?></div>
                <div class="vle_info_box">
                    <?php
                    $courses = (array)$vle_info->courses;
                    ksort($courses);
                    foreach($courses as $key => $fvle):?>
                        <div class="vle_info_element">
                            <span class="vle_info_key_data"><?php echo $key ?></span>
                            <span class="vle_info_course_name_data"><?php echo $fvle?></span>
                        </div>
                    <?php endforeach;?>
                </div>
            </div>
        <?php elseif(!$vle_info): ?>
            <div class="vle_info_working" style="background:red;"><?php echo T('This configuration is not correct.')?></div>
        <?php elseif(!$vle->new): ?>
            <div class="vle_info_working"><?php echo T('There are no courses available with the provided VLE configuration.')?></div>
        <?php endif; ?>
    </div>
</div>