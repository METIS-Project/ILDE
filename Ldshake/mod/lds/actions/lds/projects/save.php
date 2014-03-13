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
require_once __DIR__.'/../../../lds_contTools.php';

//Is the LdS new?
$isNew = false;

$lds_recovery = get_input('lds_recovery', "0");

//The object we'll return (jsonencoded) to the form.
$resultIds = new stdClass();
$resultIds->docs = array ();

if(get_input('guid') <= 0 && $recovered_lds = get_entities_from_metadata("lds_recovery", $lds_recovery, 'object', 'LdSProject', get_loggedin_userid(), 1))
    set_input('guid', $recovered_lds[0]->guid);

//We first create the LDS (or update it)
if (get_input('guid') > 0)
{
	//We're editing. Fetch it from the DB
    $project_design = get_entity(get_input('guid'));
}
else
{
	//We're creating it from scratch. Construct a new obj.
	$project_design = new ElggObject();
    $project_design->subtype = 'LdSProject';
    $project_design->lds_recovery = $lds_recovery;
	$project_design->owner_guid = get_loggedin_userid();

    //$editor_types = explode(',', get_input('editor_type'));
    //$editor_type = $editor_types[0];
    //$editor_subtype = $editor_types[1];
    //$project_design->editor_type = $editor_type;
    //$project_design->editor_subtype = $editor_subtype;
	$isNew = true;
} 

$project_design->title = get_input('title');
$project_design->granularity = get_input('granularity');
$project_design->completeness = get_input('completeness');
$project_design->description = get_input('JSONData', null, false);
$project_design->editor_type = get_input('editorType');

//If we save it for the first time, we're going to put some default value, which will be
//modified by an ajax call to share.php
if (get_input('guid') == 0)
{
	$project_design->access_id = 2;
}

//Now the tags. We'll delete the existing ones to save them again
$tagFields = array ('discipline', 'pedagogical_approach', 'tags');
foreach ($tagFields as $field)
{
	$newTags = explode(',', get_input($field));
	foreach ($newTags as $k=>$v) if(empty($v)) unset($newTags[$k]);
	$project_design->$field = $newTags;
}

$project_design->save();
$resultIds->LdS = $project_design->guid;

//We now determine if we already saved in the same edition session or not, in order to create a new revision.
if (get_input('revision') == 0)
{
	//We save for the first time in this edition session. So we create a revision.
	create_annotation($project_design->guid, 'revised_project_design', '', 'text', get_loggedin_userid(), 1);
} else {
    $project_design->lds_recovery = 0;
}
//We get the revision id to send it back to the form
$revision = $project_design->getAnnotations('revised_project_design', 1, 0, 'desc');
$revision = $revision[0];
$resultIds->revision = $revision->id;

/*
if ($isNew) {
    $project_design->notify = 1;
    $project_design->save();
}
*/

$resultIds->requestCompleted = true;

header('Content-Type: application/json; charset=utf-8');
echo json_encode($resultIds);