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

T('Enter your VLE data');
?>

<form action="<?php echo $CONFIG->url . 'action/lds/manage_vle' ?>">

    <span class="vle_form_label"><?php T('VLE name')?></span>
    <input class="vle_form_input" type="text" name="vle_name" value="<?php echo htmlspecialchars($vle->name)?>" />
    <span class="vle_form_label"><?php T('VLE type')?></span>
    <input class="vle_form_input" type="text" name="vle_type" value="<?php echo htmlspecialchars($vle->type)?>" />
    <span class="vle_form_label"><?php T('VLE url')?></span>
    <input class="vle_form_input" type="text" name="vle_url" value="<?php echo htmlspecialchars($vle->url)?>" />
    <span class="vle_form_label"><?php T('VLE username')?></span>
    <input class="vle_form_input" type="text" name="vle_username" value="<?php echo htmlspecialchars($vle->username)?>" />
    <span class="vle_form_label"><?php T('VLE password')?></span>
    <input class="vle_form_input" type="text" name="vle_password" value="<?php echo htmlspecialchars($vle->password)?>" />
    <input class="vle_form_submit" type="submit" name="vle_submit" />

</form>


<div>
    <?php echo T('Available VLE courses')?>
    <div>
        <?php foreach($courses as $fvle):?>
        <div class="vle_info_box">
            <div class="vle_info_field">
                <span class="vle_info_name_label"><?php echo T('VLE name')?></span>: <span class="vle_info_name_data"><?php echo $fvle->name?></span>
            </div>
            <div class="vle_info_field">
                <span class="vle_info_type_label"><?php echo T('VLE type')?></span>: <span class="vle_info_type_data"><?php echo $fvle->type?></span>
            </div>
            <div class="vle_info_field">
                <span class="vle_info_url_label"><?php echo T('VLE url')?></span>: <span class="vle_info_url_data"><?php echo $fvle->url?></span>
            </div>
            <div class="vle_info_field">
                <span class="vle_info_course_name_label"><?php echo T('VLE course_name')?></span>: <span class="vle_info_course_name_data"><?php echo $fvle->course_name?></span>
            </div>
        </div>
        <?php endforeach;?>
    </div>
</div>