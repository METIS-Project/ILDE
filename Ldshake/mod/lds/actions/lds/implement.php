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
 * LdS save controller.
 */
require_once __DIR__.'/../../lds_contTools.php';

$lds_id = get_input('lds_id');
$title = get_input('title');
$vle_id = get_input('vle_id');
$course_id = get_input('course_id');
$vle_name = get_input('vle_name', null);
$course_name = get_input('course_name', null);
$project_guid = get_input('project_guid', null);

$lds = get_entity($lds_id);

if($lds->editor_type == 'exelearningrest') {
    $vle = get_entity($vle_id);
    $editordocument_query = get_entities_from_metadata('lds_guid',$lds->guid,'object','LdS_document_editor', 0, 1);
    $mm = new MoodleManager($vle, $editordocument_query[0]);
    $res = $mm->addScorm($course_id, $title . ' ' . date("D M Y  H:i:s"));
    if($res) {
        system_message(T("Successfully deployed the SCORM package."));
        echo '-2';
    } else {
        echo '-2';
    }
} else {
    $implementation = new ElggObject();
    $implementation->subtype = 'LdS_implementation_helper';
    $implementation->access_id = ACCESS_PUBLIC;
    $implementation->owner_guid = get_loggedin_userid();
    $implementation->container_guid = $lds->guid;
    $implementation->title = $title;
    $implementation->vle_id = $vle_id;
    $implementation->course_id = $course_id;
    $implementation->vle_name = $vle_name;
    $implementation->course_name = $course_name;
    $implementation->lds_id = $lds->guid;
    $implementation->editor_type = 'gluepsrest';

    if(!empty($project_guid))
        $implementation->project_design = $project_guid;

    $implementation->save();

    echo $implementation->guid;
}