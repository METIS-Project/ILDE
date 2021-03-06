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

<div id="implement_popup" class="lds_popup">
    <a class="lds_close_popup" id="ldsimplement_popup_close" href="#"><?php echo T("Cancel") ?></a>
    <h3><?php echo T("Enter the title of your new implementation") ?></h3>
    <input type="text" size="60" name="new_implementation_title" />
    <br />
    <br />

    <h3><?php echo T("Select one of your VLE spaces") ?></h3>

    <div style="max-height: 300px; overflow-y: auto;">
        <?php if(is_array($vle_data)): ?>
        <?php foreach($vle_data as $vle_id=>$vle): ?>
            <div class="vle_implement_form_vle_name"><?php echo $vle->item->name ?></div>
            <div class="vle_implement_form_vle_courses">
            <?php if(isset($vle->courses)): ?>
            <?php foreach($vle->courses as $key=>$course): ?>
                <div>
                    <input type="radio" name="course" value="<?php echo $key?>" vle_id="<?php echo $vle_id?>" vle_name="<?php echo htmlentities($vle->item->name) ?>" course_name="<?php echo htmlentities($course) ?>" /><span class="course-name"><?php echo $course;?></span>
                </div>
            <?php endforeach; ?>
            <?php endif; ?>
            </div>
        <?php endforeach; ?>
        <?php endif; ?>
    </div>

    <div id="impl_submit_incomplete" style="display:none;color:red"><?php echo T("You must introduce a title and select a VLE space!");?></div>
    <input type="hidden" name="lds_id" />

    <input type="button" id="implementlds_submit" value="<?php echo T('Implement')?>" />
</div>