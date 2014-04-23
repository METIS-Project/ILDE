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
	$project_design = new LdS();
    $project_design->subtype = 'LdSProject';
    $project_design->access_id = 2;
    $project_design->lds_recovery = $lds_recovery;
	$project_design->owner_guid = get_loggedin_userid();

	$isNew = true;
} 

$project_design->title = get_input('title');
$project_design->granularity = get_input('granularity');
$project_design->completeness = get_input('completeness');
$project_design->description = get_input('JSONData', null, false);
$project_design->editor_type = get_input('editorType');


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
	create_annotation($project_design->guid, 'revised_docs', '', 'text', get_loggedin_userid(), 1);
} else {
    $project_design->lds_recovery = 0;
}
//We get the revision id to send it back to the form
$revision = $project_design->getAnnotations('revised_docs', 1, 0, 'desc');
$revision = $revision[0];
$resultIds->revision = $revision->id;
$resultIds->requestCompleted = true;

$preserved_lds = array();
$items_to_implement = array();
$pg_data = json_decode($project_design->description, true);
$pd_guid = $project_design->guid;
$lds_list = lds_contTools::getProjectLdSList($pd_guid, true);

if($project_design->getSubtype() == 'LdSProject_implementation') {
    foreach($pg_data as &$item) {
        if(isset($item['guid'])) {
            $preserved_lds[] = $item['guid'];

            if(!in_array($item['guid'], $lds_list)) {
                if($item['creation'] == "existent") {
                    add_entity_relationship($lds->guid, 'lds_project_existent', $pd_guid);
                } else {
                    $ldsm = new richTextEditor(null, $lds);
                    $cloned_lds = $ldsm->cloneLdS("{$item['toolName']} ($title)");
                    $item['original_guid'] = $item['guid'];
                    $item['guid'] = $cloned_lds->guid;
                    add_entity_relationship($lds->guid, 'lds_project_nfe', $pd_guid);
                }
            }
        } else {
            if($item['editor_type'] == 'doc') {
                $lds = new LdSObject();
                $lds->project_design = $project_design->guid;
                $lds->owner_guid = get_loggedin_userid();
                $lds->access_id = 2;
                $lds->all_can_view = "no";
                $lds->title = "{$item['toolName']} ($title)";
                $lds->editor_type = $item['editor_type'];
                $item['guid'] = $lds->save();
                add_entity_relationship($lds->guid, 'lds_project_new', $pd_guid);

                $initDocuments = array();
                $initDocuments[] = '';

                if(isset($item['editor_subtype'])) {
                    require_once __DIR__.'/../../../templates/templates.php';
                    $lds->editor_subtype = $item['editor_subtype'];
                    $templates = ldshake_get_template($lds->editor_subtype);
                    $i=0;
                    foreach($templates as $template) {
                        $initDocuments[$i++] = $template;
                    }
                    $lds->save();
                }

                foreach($initDocuments as $initDocument) {
                    $docObj = new DocumentObject($lds->guid);
                    $docObj->title = 'default title';
                    $docObj->description = $initDocument; //We put it in ths desciption in order to use the objects_entity table of elgg db
                    $docObj->save();
                }
            } else {
                $lds = new LdSObject();
                $lds->title = "{$item['toolName']} ($title)";
                //$lds->project_design = $pd_guid;
                $lds->owner_guid = get_loggedin_userid();
                $lds->access_id = 2;
                $lds->all_can_view = "no";
                $lds->editor_type = $item['editor_type'];
                $lds->external_editor = true;
                $item['guid'] = $lds->save();
                add_entity_relationship($lds->guid, 'lds_project_new', $pd_guid);

                $docObj = new DocumentObject($lds->guid);
                $docObj->title = T('Support Document');
                $docObj->description = T('Write support notes here...'); //We put it in ths desciption in order to use the objects_entity table of elgg db
                $docObj->save();

                $document_editor = new DocumentEditorObject($lds->guid, 0);
                $document_editor->editorType = $lds->editor_type;
                $document_editor->lds_guid = $lds->guid;
                $document_editor->lds_revision_id = 0;
                $document_editor->save();

                $editor = editorsFactory::getInstance($document_editor);
                $editor_vars = $editor->newEditor();

                if($save_result = $editor->saveDocument($editor_vars)) {
                    list($document_editor, $resultIds_add) = $save_result;
                } else {
                    throw new Exception("Save failed");
                }
            }

        }
    }

    if($existent_lds = $project_design->getEntitiesFromRelationship('lds_project_existent')) {
        foreach($existent_lds as $fel)
            $preserved_lds[] = $fel->guid;

        $preserved_lds = array_unique($preserved_lds);
    }

    $delete_lds = array_diff($lds_list, $preserved_lds);

    foreach($delete_lds as $d) {
        if($lds_result = get_entity($d))
            $lds_result->disable();
    }

    $project_design->description = json_encode($pd_data);
    $project_design->save();
}

$resultIds->JSONData = $project_design->description;

header('Content-Type: application/json; charset=utf-8');
echo json_encode($resultIds);