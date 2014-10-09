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
            $role = get_loggedin_user()->role;
            if(get_loggedin_user()->role != 'student' and get_loggedin_user()->role != 'teacher'): ?>
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
                <?php if(!$disable_projects):?><li id="tb_new_option_project" class="menu_suboption" ><?php echo T("Project") ?></li><?php endif;?>
                <li id="tb_conceptualize_new10" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/new/"><?php echo T("Document") ?></a></li>
                <li id="tb_conceptualize_new5" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/google_docs/"><?php echo T("Google Docs") ?></a></li>
                <li id="tb_conceptualize_new4" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/neweditor/google_spreadsheet"><?php echo T("Google Spreadsheet") ?></a></li>
                <li id="tb_conceptualize_new9" class="menu_option"><a href="<?php echo $vars['url']; ?>pg/lds/upload/image"><?php echo T("Upload image") ?></a></li>
            <?php else: ?>
                <?
                global $CONFIG;
                $list = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), false, 9999, 0, 'tags', 'answer_template', 'time', true);
                if(empty($list))
                $list = array();
                ?>
                <?php foreach($list as $element): ?>
                    <li class="menu_option"><a href="<?php echo $CONFIG->url.'pg/lds/new/wording/'.$element->lds->guid ?>"><?php echo $element->lds->title ?></a></li>
                <?php endforeach; ?>
            <?php endif; ?>
        </ul>
    </div>
