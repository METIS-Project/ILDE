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

$project_design_id = get_input('guid');
$title = get_input('title');

$project_design_reference = get_entity($project_design_id);

$project_design_implementation = new LdS();
$project_design_implementation->access_id = 2;
$project_design_implementation->subtype = 'LdSProject_implementation';
$project_design_implementation->title = $title;
$project_design_implementation->description = $project_design_reference->description;
$project_design_implementation->editor_type = $project_design_reference->editor_type;
$project_design_implementation->project_design_reference = $project_design_reference->guid;
$project_design_implementation->save();

$pd_data = json_decode($project_design_reference->description, true);

foreach($pd_data as &$item) {
    if($item['editor_type'] == 'doc') {
        $lds = new LdSObject();
        $lds->project_design = $project_design_implementation->guid;
        $lds->owner_guid = get_loggedin_userid();
        $lds->access_id = 2;
        $lds->all_can_view = "no";
        $lds->title = "{$item['toolName']} ($title)";
        $lds->editor_type = $item['editor_type'];
        $item['guid'] = $lds->save();

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
            //$docObj->doc_recovery = $doc['doc_recovery'];
            $docObj->title = 'default title';
            $docObj->description = $initDocument; //We put it in ths desciption in order to use the objects_entity table of elgg db
            //$docObj->lds_revision_id = $revision->id;
            $docObj->save();
        }

    } else {
        $lds = new LdSObject();
        $lds->title = "{$item['toolName']} ($title)";
        $lds->project_design = $project_design_implementation->guid;
        $lds->owner_guid = get_loggedin_userid();
        $lds->access_id = 2;
        $lds->all_can_view = "no";
        $lds->editor_type = $item['editor_type'];
        $lds->external_editor = true;
        $item['guid'] = $lds->save();

        $docObj = new DocumentObject($lds->guid);
        $docObj->title = T('Support Document');
        $docObj->description = 'Write support notes here...'; //We put it in ths desciption in order to use the objects_entity table of elgg db
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

$project_design_implementation->description = json_encode($pd_data);
$project_design_implementation->save();

echo $project_design_implementation->guid;