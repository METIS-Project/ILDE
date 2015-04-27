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
 * Set of helper functions for the LdS module 
 */

//include_once __DIR__.'/Java.inc';
//include_once __DIR__.'/query_repository.php';

function ldshake_glueps_isimplementation($editor_type) {
    $implementable = array("gluepsrest");

    if(in_array($editor_type, $implementable))
        return true;

    return false;
}

function ldshake_glueps_isimplementable($editor_type) {
    $implementable = array("webcollagerest", "openglm", "syncmeta");

    if(in_array($editor_type, $implementable))
        return true;

    return false;
}

function ldshake_query_design_implementated_list($userid, $params) {
    $lds_id_id = get_metastring_id("lds_id");
    $lds_implementation_id = get_subtype_id("object", "LdS_implementation");

    if(!$lds_id_id or !$lds_implementation_id) {
        return null;
    }

    $editable_implementations = lds_contTools::getUserEntities('object', 'LdS_implementation', 0, 0, 9999, 0, null, null, "time", true, null, null, null, true);
    if(empty($editable_implementations))
        return null;

    $editable_implementations_string = implode(',', $editable_implementations);
    $query['join'] = <<<SQL
JOIN (
 SELECT DISTINCT CAST(msf.string AS UNSIGNED) AS ms_guid FROM entities ef
 JOIN metadata mf ON ef.guid = mf.entity_guid
 JOIN metastrings msf ON msf.id = mf.value_id
 WHERE /*ef.subtype = {$lds_implementation_id}*/
 ef.guid IN ({$editable_implementations_string})
 AND mf.name_id = {$lds_id_id}
 AND ef.enabled = 'yes'
) AS imp on imp.ms_guid = e.guid
SQL;

    /*
    $query['where'] = <<<SQL
imp.guid IS NOT NULL
SQL;
*/
    return $query;
}

function ldshake_get_document_formats($doc) {
    global $CONFIG;
    if(!empty($doc->editorType)) {
        if(isset($CONFIG->rest_editor_list[$doc->editorType])) {
            if(!empty($CONFIG->rest_editor_list[$doc->editorType]['imsld'])) {
                $formats['ims-ld'] = array(
                    'urlsuffix' => 'imsld',
                    'mime'      => 'application/zip',
                    'description'   => 'IMS LD standard file.',
                    'tag'       => 'IMS LD',
                    'titlesuffix' => 'IMS LD'
                );
            }
        }
    }

    $formats['pdf'] = array(
        'urlsuffix' => 'pdf',
        'mime'      => 'application/pdf',
        'description'   => 'PDF standard file.',
        'titlesuffix' => 'PDF'
    );
    return $formats;
}

function ldshake_check_sanitize_filter_param($filter) {
    if(!empty($filter)) {
        $filter_error = false;
        if(!$binary_filter = base64_decode($filter, true)) {
            $filter_error = false;
        }

        if(!$filter_error) {
            $serialized_filter = bzdecompress($binary_filter);
            if($serialized_filter < 0 && $serialized_filter > -10) {
                $filter_error = true;
            }
        }

        if(!$filter_error) {
            if(!$deserialized_filter = unserialize($serialized_filter)) {
                $filter_error = true;
            }
        }

        if(!$filter_error) {
            if(!is_array($deserialized_filter)) {
                $filter_error = true;
            }
        }

        if(!$filter_error) {
            if(!isset($deserialized_filter['revised']) or !isset($deserialized_filter['filter']))
                $filter_error = true;
        }

        if(!$filter_error) {
            return true;
        } else {
            return false;
        }
    }
    return false;
}

function ldshake_custom_query_edited_project_lds($userid, $params) {
    $filter = $params['filter'];
    $revised = $params['revised'];
    $project_id = get_subtype_id('object', 'LdSProject_implementation');


    if(empty($project_id) or (empty($project_id))) {
        return null;
    }

    $query['select'] = "e.guid, 'object' AS type";

    $filter_query['where'] = "";
    $filter_query['join'] = "";
    if(!empty($filter)) {
        $i=0;
        foreach($filter as $fk => $fv) {
            foreach($fv as $tag) {
                if(!in_array($fk, array('editor_subtype', 'editor_type', 'tags', 'discipline', 'pedagogical_approach')))
                    continue;

                $val_id = get_metastring_id($tag);
                $tags_id = get_metastring_id($fk);
                if(!empty($val_id) and !empty($tags_id)) {
                    $filter_query['join'][] .= <<<SQL
JOIN metadata m{$i} ON m{$i}.entity_guid = e.guid
SQL;

                $filter_query['where'][] .= <<<SQL
m{$i}.name_id = {$tags_id} AND m{$i}.value_id = {$val_id}
SQL;

                if($fk == "editor_type" and in_array($tag, array("doc", "google_docs"))) {
                    $editor_subtype_id = get_metastring_id("editor_subtype");
                    $filter_query['join'][] .= <<<SQL
JOIN metadata msub{$i} ON msub{$i}.entity_guid = e.guid
LEFT JOIN (
  SELECT mnosub.entity_guid FROM metadata mnosub WHERE mnosub.name_id = {$editor_subtype_id}
) AS mnosub ON mnosub.entity_guid = e.guid
SQL;

                    $filter_query['where'][] .= <<<SQL
((msub{$i}.name_id = {$editor_subtype_id} AND msub{$i}.value_id = {$val_id}) OR (msub{$i}.name_id = {$tags_id} AND mnosub.entity_guid IS NULL))
SQL;

/*
                        $filter_query['join'][] .= <<<SQL
JOIN (
  SELECT msub.entity_guid FROM metadata msub WHERE msub.name_id = {$editor_subtype_id}
) AS msub ON msub.entity_guid = e.guid
SQL;
                        $filter_query['where'][] .= <<<SQL
msub.entity_guid IS NULL
SQL;
*/                  }

                    $i++;
                } else {
                    return null;
                }
            }
        }
    }

    $query['join'] = "";
    $query['where'] = "";
    if(!empty($filter_query['where']) and !empty($filter_query['join'])) {
        $query['join']  .= "\n";
        $query['where'] .= " \n";
        $query['join']  .= implode("\n", $filter_query['join']);
        $query['where'] .= implode(" AND \n", $filter_query['where']);
        $query['join']  .= "\n";
        $query['where'] .= "\n";

        if($revised == "true")
            $query['where'] .= " AND ";
    };

if($revised == "true") {
    $query['join'] .= <<<SQL
    JOIN
    (SELECT * FROM entities ep where ep.type = 'object' AND ep.subtype = {$project_id} AND ep.enabled = 'yes')
    AS projects
    ON projects.guid = e.container_guid
SQL;

        $query['where'] .= <<<SQL
    e.num_contributions > 0
SQL;
}
    if(empty($query['where']))
        $query['where'] = "1=1";
    return $query;
}

function ldshake_exelearning_get_newfile($params = null) {
    global $CONFIG;

    if($CONFIG->ldshake_mode != "msf")
        return $CONFIG->path.'vendors/exelearning/'.'empty_en_intef.elp';
    else
        return null;
}

function ldshake_recover_project_lds($guid) {
    $project = get_entity($guid);
    $data = json_decode($project->description);
    $tools = $data->tools;
    $lds_guids = array();

    if(empty($tools))
        return true;

    foreach($tools as $tool) {
        if(empty($tool->associatedLdS))
            continue;

        foreach($tool->associatedLdS as $tlds) {
            if($lds = get_entity($tlds->guid)) {
                $lds->deleted = '0';
                $lds->save_ktu();
                $lds->enable();
                $lds_guids[] = $lds->guid;
            }
        }
    }

}

function ldshake_lds_from_array($elements) {
    $elements_guids = ldshake_guid_from_array($elements);
    $custom = array(
        'build_callback' => 'ldshake_custom_query_from_guid',
        'params' => array(
            'guids' => $elements_guids
        ),
    );

    $result = lds_contTools::getUserEntities('object', 'LdS', 0, false, 9999, 0, null, null, "time", false, null, true, $custom);

    $index = array();
    foreach($result as $entity)
        $index[$entity->lds->guid] = $entity;

    $full_list = array();
    foreach($elements_guids as $guid)
        if(isset($index[$guid]))
            $full_list[] = $index[$guid];
        else
            $full_list[] = null;

    return $full_list;
}

function ldshake_guid_from_array($elements) {

    if(empty($elements))
        return array();

    $elements = array_values($elements);

    if(is_numeric($elements[0]))
        return $elements;

    if(isset($elements[0]->lds_guid))
        $gstring = 'lds_guid';
    elseif(isset($elements[0]->lds_id))
        $gstring = 'lds_id';
    elseif(isset($elements[0]->guid))
        $gstring = 'guid';
    else
        $gstring = 'entity_guid';

    $guids = array();
    foreach($elements as $element) {
        $guid = $element->$gstring;
        if(!empty($guid))
            $guids[] = $element->$gstring;
    }

    return $guids;
}

function ldshake_query_design_implementation_list($userid, $params) {
    $lds_id_id = get_metastring_id("lds_id");

    if(!$lds_id_id) {
        return null;
    }

    $query['select'] = "lds_guid.string AS guid, 'object' AS type";

    $query['join'] = <<<SQL
JOIN metadata i ON i.entity_guid = e.guid
JOIN metastrings lds_guid ON i.value_id = lds_guid.id
SQL;

    $query['where'] = <<<SQL
i.name_id = {$lds_id_id}
SQL;

    $query['group_by'] = <<<SQL
lds_guid.string
SQL;

    return $query;
}

function ldshake_custom_query_implemented_lds($userid, $params) {
    $lds_id_id = get_metastring_id("lds_id");

    if(!$lds_id_id) {
        return null;
    }

    $query['select'] = "e.guid, 'object' AS type";

    $query['join'] = "JOIN metadata i ON i.entity_guid = e.guid";

    $query['where'] = <<<SQL
i.name_id = {$lds_id_id}
SQL;

    $query['group_by'] = <<<SQL
i.value_id
SQL;

    return $query;
}

function ldshake_custom_query_from_guid($userid, $params) {
    if(!empty($params['guids']) and is_array($params)) {
        if(is_numeric($params['guids'][0]))
            $guids = implode(',', $params['guids']);
        else {
            $guids = array();
            foreach($params['guids'] as $entity)
                $guids[] = isset($entity->guid) ? $entity->guid : $entity->entity_guid;

            $guids = implode(',', $guids);
        }
    }
    else
        return false;

    $query['select'] = "e.guid, 'object' AS type";
    $query['where'] = <<<SQL
e.guid IN ({$guids})
SQL;

    return $query;
}

function ldshake_mode_view($view) {
    global $CONFIG;
    $vars['url'] = $CONFIG->url;
    $filepath = $CONFIG->path . 'mod/lds/mode/' . $CONFIG->ldshake_mode . '/theme/' . $view . '.php';
    if(file_exists($filepath))
        @include($filepath);
    else {
        $filepath = $CONFIG->path . 'mod/lds/mode/default/theme/' . $view . '.php';
        if(file_exists($filepath))
            @include($filepath);
    }
}

function ldshake_filter_by_date($entities, $start, $end) {
    $last = count($entities);
    for($i=0; $i < $last; $i++) {
        if($entities[$i]->time_created >= $start and $entities[$i]->time_created <= $end)
            continue;

        unset($entities[$i]);
    }

    return array_values($entities);
}

function ldshake_get_lds_authors($doc) {

    $lds = get_entity($doc->lds_guid);

    if(isset($lds->external_editor))
        $revisions = lds_contTools::getRevisionEditorList($lds);
    else
        $revisions = lds_contTools::getRevisionList($lds);

    $revision_id = (int)$doc->lds_revision_id;
    if(isset($doc->document_guid)) {
        $doc_guid = $doc->document_guid;
        $doc = get_entity($doc_guid);
    } else
        $doc_guid = $doc->guid;

    $doc_revisions = array();

    foreach($revisions as $revision) {
        if(!empty($revision->revised_documents )) {
            foreach($revision->revised_documents as $revised_document) {
                if($revised_document->document_guid == $doc_guid) {
                    if($revision_id > (int)$revised_document->lds_revision_id)
                        $doc_revisions[] = $revised_document;
                }
            }
        }
    }

    $authors = array();

    foreach($doc_revisions as $revision) {
        $owner = get_user($revision->owner_guid);

        if(empty($owner))
            continue;

        $authors[$owner->guid] = $owner;
    }

    $owner = get_user($doc->owner_guid);

    if($owner)
        $authors[$owner->guid] = $owner;

    return $authors;
}


function ldshake_filter_editorsubtype($entities, $type, $subtype = null) {

    $last = count($entities);
    for($i=0; $i < $last; $i++) {
        //both empty
        if(empty($entities[$i]->editor_subtype) and empty($subtype))
            continue;

        //same types
        if(!empty($entities[$i]->editor_subtype) and empty($subtype))
            if($entities[$i]->editor_subtype == $type)
            continue;

        //match
        if(!empty($entities[$i]->editor_subtype) and $entities[$i]->editor_subtype == $subtype)
            continue;

        unset($entities[$i]);
    }

    return array_values($entities);
}

function ldshake_filter_empty_subtype($entities) {
    $last = count($entities);
    for($i=0; $i < $last; $i++) {
        if(empty($entities[$i]->editor_subtype))
            continue;

        unset($entities[$i]);
    }

    return array_values($entities);
}

function ldshake_lds_referer($lds) {
    global $CONFIG;

    $referer = $CONFIG->url.'pg/lds/';

    if($container = get_entity($lds->container_guid)) {
        $subtype = $container->getSubtype();

        if($subtype == 'LdSProject_implementation') {
            $referer = $CONFIG->url.'pg/lds/project_implementation/' . $container->guid;
        }
    }

    return $referer;
}

function ldshake_stats_log_event($event, $data = null, $user = null) {
    if(!$user)
        $user = get_loggedin_user();

    if($data)
        $data = serialize($data);
    else
        $data = '';

    create_annotation($user->guid, $event, $data, 'text', $user->guid, ACCESS_PUBLIC);
}

function ldshake_contextual_help_settings_save() {
    $disable_contextual_help = get_input('disable_contextual_help');

    $user = get_loggedin_user();
    $disable_user_notifications = null;

    switch($disable_contextual_help) {
        case 'yes':
            $disable_contextual_help = true;
            break;
        case 'no':
            $disable_contextual_help = null;
            break;
        default:
            $disable_contextual_help = null;
    }

    $user->disable_contextual_help = $disable_contextual_help;
    $user->save();
}

function ldshake_get_filepath($file_guid)
{
    $file = get_entity($file_guid);
    $readfile = new ElggFile();
    $readfile->owner_guid = $file->owner_guid;
    $readfile->setFilename($file->originalfilename);
    return $readfile->getFilenameOnFilestore();
}

function ldshake_download_file($url, $filepath) {
    try {
        if(!($fd = fopen($filepath,'w')))
            throw new Exception("Cannot create file.");

        $options = array(
            CURLOPT_FILE    => $fd,
            CURLOPT_TIMEOUT =>  60,
            CURLOPT_URL     => $url,
            CURLOPT_SSL_VERIFYPEER => false
        );

        $ch = curl_init();
        curl_setopt_array($ch, $options);
        $result = curl_exec($ch);
    } catch (Exception $e) {
        register_error($e->getMessage());
    }

    if(!empty($fd))
        fclose($fd);

    return (!empty($result));
}

function ldshake_supports_pdf($document) {
    if(!$document)
        return false;

    $subtype = $document->getSubtype();
    $lds = get_entity($document->lds_guid);
    $supported_editors = array(
        'project_design',
        'webcollagerest'
    );

    if($subtype == 'LdS_document')
        return true;

    if(in_array($document->editorType, $supported_editors))
        return true;

    return false;
}

function ldshake_project_upgrade($pg_data) {
    if(!is_array($pg_data))
        return $pg_data;

    $project = new stdClass();
    $project->stickynotes = array();
    $project->tools = $pg_data;

    return $project;
}

function ldshake_isglobalenv($type, $subtype) {
    if(is_numeric($subtype))
        $subtype = get_subtype_from_id($subtype);

    if($type == 'object' and $subtype == 'LdSProject_implementation')
        return true;

    return false;
}

function ldshake_project_add_title_order_callback($a, $b) {
    return (int)$a->lds->workflow_order > (int)$b->lds->workflow_order;
}

function ldshake_project_add_title_order($lds_list, $pg_data) {
    if(!is_array($lds_list))
        return $lds_list;

    foreach($lds_list as &$rlds) {
        if(!$project_lds = ldshake_project_find_guid($pg_data, $rlds->lds->guid))
            continue;

        if(isset($project_lds->workflow_order)) {
            $rlds->lds->title = '['. $project_lds->workflow_order . '] '  . $rlds->lds->title;
            $rlds->lds->workflow_order = $project_lds->workflow_order;
        }
    }

    uasort($lds_list, "ldshake_project_add_title_order_callback");

    return $lds_list;
}

function ldshake_project_find_guid($pg_data, $guid) {
    if(!is_array($pg_data->tools))
        return false;

    foreach($pg_data->tools as $tool) {
        if(!is_array($tool->associatedLdS))
            continue;

        foreach($tool->associatedLdS as $lds) {
            if($lds->guid == $guid)
                return $lds;
        }
    }

    return false;
}

function ldsshake_project_implement(&$pg_data, &$project_design) {
    $pd_guid = $project_design->guid;
    $title = $project_design->title;
    $preserved_lds = array();
    $lds_list = lds_contTools::getProjectLdSList($pd_guid, false, true);
    if(!$lds_list)
        $lds_list=array();

    foreach($pg_data as &$tool) {//tooltype
        $tool = (array)$tool;

        if(empty($tool['associatedLdS']) or !is_array($tool['associatedLdS'])) {
            $tool = (object)$tool;
            continue;
        }

        if($tool['tooltype'] == "vle") {
            $project_design->vle = $tool['editor_subtype'];
            $project_design->save();
            continue;
        }

        foreach($tool['associatedLdS'] as &$item) {//lds
            $item = (array)$item;
            $lds = false;
            if(!empty($item['guid'])) {
                $lds = get_entity($item['guid']);
            }
            if($lds) {
                $preserved_lds[] = $item['guid'];
                //$lds = get_entity($item['guid']);

                if(!in_array($item['guid'], $lds_list) && $lds) {
                    if(empty($item['clone'])) {
                        try {
                            add_entity_relationship($item['guid'], 'lds_project_existent', $pd_guid);
                        } catch (Exception $e) {
                        }
                    } else {
                        $lds = get_entity($item['guid']);
                        $ldsm = new richTextEditor(null, $lds);
                        $cloned_lds = $ldsm->cloneLdS("{$lds->title} ($title)");
                        $cloned_lds->project_design = $project_design->guid;
                        $cloned_lds->container_guid = $pd_guid;
                        $cloned_lds->save();
                        $item['original_guid'] = $item['guid'];
                        $item['guid'] = $cloned_lds->guid;
                        try {
                            add_entity_relationship($cloned_lds->guid, 'lds_project_nfe', $pd_guid);
                        } catch (Exception $e) {
                        }
                    }
                }
            } else {
                if($tool['tooltype'] == 'doc') {
                    $lds = new LdSObject();
                    $lds->project_design = $project_design->guid;
                    $lds->owner_guid = get_loggedin_userid();
                    $lds->container_guid = $pd_guid;
                    $lds->access_id = 2;
                    $lds->all_can_view = "yes";
                    $lds->title = "{$tool['toolName']} ($title)";
                    $lds->editor_type = $tool['tooltype'];
                    $item['guid'] = $lds->save();
                    try {
                        add_entity_relationship($lds->guid, 'lds_project_new', $pd_guid);
                    } catch (Exception $e) {
                    }

                    if(function_exists("ldshake_mode_ldsnew_project")) {
                        ldshake_mode_ldsnew_project($lds);
                        $lds->save();
                    }

                    $initDocuments = array();
                    $initDocuments[] = '';

                    if(isset($tool['editor_subtype'])) {
                        require_once __DIR__.'/templates/templates.php';
                        $lds->editor_subtype = $tool['editor_subtype'];
                        $templates = ldshake_get_template($lds->editor_subtype);
                        $i=0;
                        foreach($templates as $template) {
                            $initDocuments[$i++] = $template[0];
                        }
                        $lds->save();
                    }

                    if(count($initDocuments) == 1) {
                        $initDocuments[] = '<p> '.T("Write here any support notes for this LdS...").'</p>';
                    }
                    $i=0;
                    foreach($initDocuments as $initDocument) {
                        $docObj = new DocumentObject($lds->guid);
                        if(!$i++)
                            $docObj->title = $lds->title;
                        else
                            $docObj->title = T('Support Document');

                        $docObj->description = $initDocument; //We put it in ths description in order to use the objects_entity table of elgg db
                        $docObj->save();
                    }
                } else {
                    $lds = new LdSObject();
                    $lds->title = "{$tool['toolName']} ($title)";
                    //$lds->project_design = $pd_guid;
                    $lds->owner_guid = get_loggedin_userid();
                    $lds->container_guid = $pd_guid;
                    $lds->access_id = 2;
                    $lds->all_can_view = "yes";
                    $lds->editor_type = $tool['tooltype'];
                    $lds->external_editor = true;
                    if($lds->editor_type == "webcollagerest" || $lds->editor_type == "openglm" || $lds->editor_type == "cadmos" || $lds->editor_type == "exelearningrest")
                        $lds->implementable = '1';

                    $item['guid'] = $lds->save();
                    add_entity_relationship($lds->guid, 'lds_project_new', $pd_guid);

                    if(function_exists("ldshake_mode_ldsnew_project")) {
                        ldshake_mode_ldsnew_project($lds);
                        $lds->save();
                    }

                    $document_editor = new DocumentEditorObject($lds->guid, 0);
                    $document_editor->editorType = $lds->editor_type;
                    $document_editor->lds_guid = $lds->guid;
                    $document_editor->lds_revision_id = 0;
                    $document_editor->save();

                    $templates = null;
                    $template = null;
                    $template_format = null;

                    if(isset($tool['editor_subtype'])) {
                        require_once __DIR__.'/templates/templates.php';
                        $lds->editor_subtype = $tool['editor_subtype'];

                        /*
                        $preferred_formats = array('docx','xlsx', null);
                        foreach($preferred_formats as $format){
                            $template_format = $format;
                            if($templates = ldshake_get_template($lds->editor_subtype, $format))
                                break;
                        }*/

                        $templates = ldshake_get_template($lds->editor_subtype);

                        if(empty($templates)) {
                            $template = null;
                            $template_format = null;
                        } else {
                            $template = $templates[0][0];
                            $template_format = $templates[0][1];
                        }
                    }

                    $editor = editorsFactory::getInstance($document_editor);
                    if($template)
                        $editor_vars = $editor->newEditor($template, $template_format);
                    else
                        $editor_vars = $editor->newEditor();

                    if($save_result = $editor->saveDocument($editor_vars)) {
                        list($document_editor, $resultIds_add) = $save_result;
                    } else {
                        throw new Exception("Save failed");
                    }

                    if(strstr($lds->editor_type, 'google')) {
                        $google_support_doc = new DocumentEditorObject($lds->guid);
                        $google_support_doc->description = T("Write here any support notes for this LdS...").'<img border="0" width="0" height="0" src="http://ilde.upf.edu/mod/lds/templates/gdocs_fix/gdfix.png">';
                        $google_support_doc->title = T("Support Document");
                        $google_support_doc->editorType = 'google_docs';
                        $google_support_doc->support = true;
                        $google_support_doc->lds_revision_id = 0;
                        $google_support_doc->save();

                        $support_vars = array(
                            'doc' => $google_support_doc,
                            'title' => T("Support Document")
                        );

                        $support_html = elgg_view('lds/view_iframe', $support_vars);
                        $template_format = null;

                        if(isset($templates[1])) {
                            $template_format = $templates[1][1];
                            $support_html = $templates[1][0];
                        }
                        $support_editor = editorsFactory::getInstance($google_support_doc);
                        $editor_vars = $support_editor->newEditor($support_html, $template_format);
                        $editor_vars = array_merge($editor_vars, $support_vars);
                        if(!$support_editor->saveDocument($editor_vars)) {
                            throw new Exception(T("Save failed"));
                        }
                    } else {
                        $docObj = new DocumentObject($lds->guid);
                        $docObj->title = T('Support Document');
                        $docObj->description = T('Write support notes here...'); //We put it in ths desciption in order to use the objects_entity table of elgg db
                        $docObj->save();
                    }
                }
            }

            if($lds && !empty($item['title']) && !check_entity_relationship($lds->guid, 'lds_project_existent', $pd_guid)) {
                if($lds->title != $item['title'] . " ($title)") {
                    if(strstr($item['title'], "($title)"))
                        $lds->title = $item['title'];
                    else
                        $lds->title = $item['title'] . " ($title)";
                    $lds->save();
                }
/*
                if(!strstr($lds->title, "($title)")) {
                    $lds->title = $item['title'] . " ($title)";
                    $lds->save();
                }
*/
            } else if($lds) {
                $item['title'] = $lds->title;
            }
        }
        $tool = (object)$tool;
    }

    if($existent_lds = $project_design->getEntitiesFromRelationship('lds_project_existent')) {
        foreach($existent_lds as $fel)
            $preserved_lds[] = $fel->guid;

        $preserved_lds = array_unique($preserved_lds);
    }

    $delete_lds = array_diff($lds_list, $preserved_lds);

    foreach($delete_lds as $d) {
        if(!$lds_result = get_entity($d))
            continue;

        if(check_entity_relationship($lds_result->guid, 'lds_project_existent', $pd_guid)) {
            remove_entity_relationship($lds_result->guid, 'lds_project_existent', $pd_guid);
            continue;
        }

        $lds_result->disable();

    }
    return true;
}

function ldshake_check_user_guid($guid, $write = false, $user_id = null) {
    if(!$user_id)
        $user_id = get_loggedin_userid();

    $entity = get_entity($guid);

    $type = $entity->type;
    if($type != 'object')
        return false;

    $subtype = $entity->getSubtype();
    if($subtype != 'LdS') {
        if(empty($entity->lds_guid))
            return false;

        $lds_guid = $entity->lds_guid;
        if(!is_numeric($lds_guid))
            return false;

        $lds = get_entity($lds_guid);

        if(empty($lds))
            return false;

        if($lds->type != object || $lds->getSubtype() != 'LdS')
            return false;

        $entity = $lds;
    }
    $check = lds_contTools::getUserEntities('object', 'LdS', $user_id, true, 1, 0, null, null, "time", $write, null, false, null, false, $entity->guid);

    return !empty($check);
    //public static function getUserEntities($type, $subtype, $user_id, $count = false, $limit = 9999, $offset = 0, $mk = null, $mv = null, $order = "time", $writable_only = false, $search = null, $enrich = false, $custom = null, $guid_only = false, $check_guid = null) {
}

function ldshake_dummy_callback($row) {
    return $row;
}

function ldshake_query_custom_count_callback($row) {
    return (int)$row->total;
}

function ldshake_guid_callback($row) {
    return (int)$row->guid;
}

function ldshake_available_users_callback($row) {
    global $CONFIG;

    return $row;
}

function buildRichQuery($user_id, $elements, $can_edit = false) {
    global $CONFIG;

    $viewed_id = get_metastring_id('viewed_lds');

    $query['pre'] = array();
    $query['join'] = <<<SQL
SQL;
    $query['select'] = <<<SQL
e.*,
(
SELECT a.time_created FROM annotations a WHERE a.name_id = {$viewed_id} AND a.owner_guid = {$user_id} and a.entity_guid = e.guid order by id desc limit 1
) AS user_last_viewed_time
SQL;

    if($can_edit) {
        if(isadminloggedin()) {
            $query['select'] .= <<<SQL
,
1 AS can_edit
SQL;
        } else {
            $query['select'] .= <<<SQL
,
e.owner_guid = {$user_id} OR eshared.guid IS NOT NULL
AS can_edit
SQL;

            $query['join'] .= <<<SQL
NATURAL LEFT JOIN (
(SELECT rg.guid_two as guid FROM entity_relationships rg JOIN (
        SELECT rug.guid_two as guid FROM entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
        ) AS up ON up.guid = rg.guid_one WHERE rg.relationship = 'lds_editor_group'
        ) UNION (
        SELECT ru.guid_two as guid FROM entity_relationships ru WHERE ru.relationship = 'lds_editor' AND ru.guid_one = {$user_id}
        )
) AS eshared
SQL;
        }
    }

    $query['where'] = "e.guid IN({$elements})";

    return $query;
}

function buildPermissionsQuery($user_id, $writable_only = true, $isglobalenv = false, $context = null) {
    global $CONFIG;

    if(!$user_id)
        $user_id = get_loggedin_userid();

    if(!($user_id))
        throw new InvalidParameterException(elgg_echo('InvalidParameterException:UnrecognisedValue'));

    if(isadminloggedin()) {
        $myfirstlds = sanitise_string(T("My first LdS"));

        $query['join'] = "";
        $query['permission'] = "e.title <> '{$myfirstlds}' OR e.owner_guid = {$user_id}";

        return $query;
    }

    $acv_query = "";
    $group_permission = "'lds_editor_group', 'lds_viewer_group'";
    $user_permission = "'lds_editor', 'lds_viewer'";

    $query['pre'] = array();
    $query['join'] = "";
    $query['permission'] = "";

    if($writable_only) {
        $group_permission = "'lds_editor_group'";
        $user_permission = "'lds_editor'";
    }

    if(!$isglobalenv) {
        $projects = lds_contTools::getUserEntities('object', 'LdSProject_implementation', get_loggedin_userid(), false, 9999, 0, null, null, "time", $writable_only, null, false, null, true);
        if(!empty($projects)) {
            $projects_lds = implode(',', $projects);
            $query['permission'] = <<<SQL
{$query['permission']}
e.container_guid IN ({$projects_lds})
OR
SQL;
        }
    }

    if($writable_only == false) {
        /*$query['join'] = <<<SQL
JOIN objects_properties op ON e.guid = op.guid
SQL;*/

        $acv_p = <<<SQL
(
    e.all_can_view = 1
)
OR
SQL;

        if(function_exists("ldshake_mode_build_permissions_acv")) {
            $acv_p = ldshake_mode_build_permissions_acv($user_id, $writable_only, $isglobalenv, $acv_p, $context);
        }
        $query['permission'] .= $acv_p;
    }

    $group_permissions = "LEFT JOIN(
        (SELECT rg.guid_two as guid FROM entity_relationships rg JOIN (
        SELECT rug.guid_two as guid FROM entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
        ) AS up ON up.guid = rg.guid_one WHERE rg.relationship IN ({$group_permission})
        ) UNION (
        SELECT ru.guid_two as guid FROM entity_relationships ru WHERE ru.relationship IN ({$user_permission}) AND ru.guid_one = {$user_id}
        )) AS pm ON e.guid = pm.guid";

    $query['join'] .= " $group_permissions";

    $query['permission'] = <<<SQL
{$query['permission']}
(
e.owner_guid = {$user_id}
)
OR
(
    pm.guid IS NOT NULL
)
SQL;

    if(function_exists("ldshake_mode_build_permissions")) {
        $query = ldshake_mode_build_permissions($user_id, $writable_only, $isglobalenv, $query, $context);
    }
    return $query;
}

function ldshake_allow_read_all_sharing() {
    if(function_exists("ldshake_mode_allow_read_all_sharing")) {
        return ldshake_mode_allow_read_all_sharing();
    } else
        return true;
}

function ldshake_tag_callback($row) {
    $row->time_created = (int)$row->time_created;
    $row->frequency = (int)$row->frequency;
    return $row;
}

function ldshake_tag_latest_query($user_id, $category) {
    global $CONFIG;

    $category_id = get_metastring_id($category);

    $query['join'] = <<<SQL
LEFT JOIN metadata mf ON e.guid = mf.entity_guid
SQL;

    $query['select'] = <<<SQL
distinct mf.value_id as guid
SQL;

    $query['where'] = "mf.name_id = {$category_id}";

    //$query['group_by'] = "mf.value_id";
    $query['order']['by'] = "order by mf.time_created desc";

    return $query;
}

/*
function ldshake_tag_relevance_query($user_id) {
    global $CONFIG;

    $fields = array ('tags', 'discipline', 'pedagogical_approach');
    $tags = array();
    foreach($fields as $category) {
        $tags[$category] = lds_contTools::getUserEntities('object', 'LdS', $user_id, false, 40, 0, null, null, null, false, null, false, array("build_callback" => "ldshake_tag_latest_query", "callback" => "ldshake_guid_query", "params" => $category));
        $tags[$category] = implode(',', $tags[$category]);
    }

    $tag_id["discipline"] = get_metastring_id("discipline");
    $tag_id["pedagogical_approach"] = get_metastring_id("pedagogical_approach");
    $tag_id["tags"] = get_metastring_id("tags");

    $query['join'] = <<<SQL
LEFT JOIN metadata mf ON e.guid = mf.entity_guid
LEFT JOIN metastrings ms ON ms.id = mf.value_id
LEFT JOIN metastrings msc ON mf.name_id = msc.id
SQL;

    $query['select'] = <<<SQL
COUNT(DISTINCT mf.entity_guid) AS frequency,
ms.string AS tag,
MAX(mf.time_created) AS time_created,
msc.string AS category
SQL;

    $query['where'] = <<<SQL
    (mf.name_id = {$tag_id["tags"]} AND mf.value_id IN ({$tags["tags"]}))
    OR
    (mf.name_id = {$tag_id["discipline"]} AND mf.value_id IN ({$tags["discipline"]}))
    OR
    (mf.name_id = {$tag_id["pedagogical_approach"]} AND mf.value_id IN ({$tags["pedagogical_approach"]}))
SQL;

    $query['group_by'] = "mf.name_id, mf.value_id";

    return $query;
}
*/

function ldshake_user_available_tags_query($user_id, $category) {
    global $CONFIG;

    $tag_id = get_metastring_id("$category");
    //$tag_id["pedagogical_approach"] = get_metastring_id("pedagogical_approach");
    //$tag_id["tags"] = get_metastring_id("tags");

    $query['join'] = <<<SQL
LEFT JOIN metadata mf ON e.guid = mf.entity_guid
LEFT JOIN metastrings ms ON ms.id = mf.value_id
SQL;

    $query['select'] = <<<SQL
DISTINCT
ms.string value
SQL;

    $query['where'] = "mf.name_id = {$tag_id}";

    //$query['group_by'] = "";

    $query['order']['by'] = "ORDER BY ms.string";

    return $query;
}

function ldshake_tag_relevance_query($user_id) {
    global $CONFIG;

    $tag_id["discipline"] = get_metastring_id("discipline");
    $tag_id["pedagogical_approach"] = get_metastring_id("pedagogical_approach");
    $tag_id["tags"] = get_metastring_id("tags");

    $query['join'] = <<<SQL
LEFT JOIN metadata mf ON e.guid = mf.entity_guid
LEFT JOIN metastrings ms ON ms.id = mf.value_id
LEFT JOIN metastrings msc ON mf.name_id = msc.id
SQL;

    $query['select'] = <<<SQL
COUNT(DISTINCT mf.entity_guid) AS frequency,
ms.string AS tag,
MAX(mf.time_created) AS time_created,
msc.string AS category
SQL;

    $query['where'] = "mf.name_id IN ({$tag_id["discipline"]}, {$tag_id["pedagogical_approach"]}, {$tag_id["tags"]})";

    $query['group_by'] = "mf.name_id, mf.value_id";

    return $query;
}

function ldshake_guid_query($row) {
    return $row->guid;
}

function ldshake_richlds_order($row) {
    return $row->guid;
}

function ldshake_richlds($row) {
    $obj = new stdClass();

    $obj->lds = new stdClass();
    $obj->lds->guid = (int)$row->guid;
    $obj->lds->subtype = $row->subtype;
    $obj->lds->subtype_string = $row->subtype_string;
    $obj->lds->editor_type = $row->editor_type;
    $obj->lds->editor_subtype = $row->editor_subtype;
    $obj->lds->title = $row->title;
    $obj->lds->owner_guid = (int)$row->owner_guid;
    $obj->lds->time_created = (int)$row->time_created;
    $obj->lds->time_updated = (int)$row->time_updated;
    $obj->lds->external_editor = $row->external_editor;
    if(empty($obj->lds->title))
        $obj->lds->title = T('Untitled LdS');

    if(isset($row->can_edit))
        $obj->lds->can_edit = $row->can_edit;

    $obj->starter = new stdClass();
    $obj->starter->name = $row->creator_name;

    $obj->starter->username = $row->creator_username;

    $obj->last_contributor = new stdClass();
    $obj->last_contributor->name = $row->last_editor_name;
    $obj->last_contributor->username = $row->last_editor_username;
    $obj->last_contribution_at = $row->last_edited_time ? (int)$row->last_edited_time : (int)$row->time_updated;
    $obj->num_contributions = (int)$row->num_contributions;
    $obj->num_comments = (int)$row->num_comments;
    $obj->num_documents = (int)$row->num_documents;

    $obj->num_viewers = $row->all_can_view ? -1 : (int)$row->viewer_count;
    $obj->num_editors = (int)$row->editor_count;
    $obj->all_can_view = $row->all_can_view;

    if(lds_contTools::isSyncLdS($obj->lds)) {
        $obj->locked = false;
        $obj->sync = true;
    } else {
        if($obj->locked = ($row->editing_tstamp > time() - 60 && $row->editing_by != get_loggedin_userid()))
            $obj->locked_by = get_user((int)$row->editing_by);
        $obj->sync = false;
    }

    $isnew = true;
    if((int)$row->last_edited_time && (int)$row->last_edited_time - 15 < (int)$row->user_last_viewed_time){
        $isnew = false;
    }
    $obj->new = $isnew;

    //tags
    $tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
    foreach ($tagtypes as $type) {
        if($row->$type)
            $obj->lds->$type = explode(',', $row->$type);
        else
            $obj->lds->$type = array();
    }

    return $obj;
}

class lds_contTools
{

    private static function buildRichQuery($user_id, $elements) {
        global $CONFIG;

        $viewed_string_id = get_metastring_id("viewed_LdS");

        $editor_type_id = get_metastring_id("editor_type");

        $edited_string_id = get_metastring_id("revised_docs");
        $editeded_string_id = get_metastring_id("revised_docs_editor");
        $generic_comment_id = get_metastring_id("generic_comment");

        $document_subtype_id = get_subtype_id('object', 'LdS_document');
        $document_editor_subtype_id = get_subtype_id('object', 'LdS_document_editor');

        $acv_k = get_metastring_id("all_can_view");
        $acv_v = get_metastring_id("yes");

        $ts = get_metastring_id("editing_tstamp");
        $editing_by = get_metastring_id("editing_by");
        $external_editor = get_metastring_id("external_editor");

        $tag_id["discipline"] = get_metastring_id("discipline");
        $tag_id["pedagogical_approach"] = get_metastring_id("pedagogical_approach");
        $tag_id["tags"] = get_metastring_id("tags");

        $query['pre'] = array();

        $query['join'] = <<<SQL
{$CONFIG->dbprefix}entities e
JOIN objects_entity oer ON e.guid = oer.guid
JOIN users_entity u ON e.owner_guid = u.guid
LEFT JOIN (SELECT e.guid, es.subtype FROM {$CONFIG->dbprefix}entities e JOIN entity_subtypes es WHERE e.guid IN ({$elements}) AND es.id = e.subtype) AS sbtp ON e.guid = sbtp.guid
LEFT JOIN (SELECT a.entity_guid, MAX(a.time_created) as last_viewed_time FROM {$CONFIG->dbprefix}annotations a WHERE a.entity_guid IN ({$elements}) AND a.name_id = {$viewed_string_id} AND a.owner_guid = {$user_id} GROUP BY a.entity_guid) AS lv ON e.guid = lv.entity_guid
LEFT JOIN (SELECT a.entity_guid, COUNT(DISTINCT a.id) as num_comments FROM {$CONFIG->dbprefix}annotations a WHERE a.entity_guid IN ({$elements}) AND a.name_id = {$generic_comment_id} GROUP BY a.entity_guid) AS cn ON e.guid = cn.entity_guid
LEFT JOIN (SELECT e.container_guid, COUNT(DISTINCT e.guid) as num_documents FROM {$CONFIG->dbprefix}entities e WHERE e.container_guid IN ({$elements}) AND e.type = 'object' AND e.subtype IN ({$document_subtype_id},{$document_editor_subtype_id}) GROUP BY e.container_guid) AS cd ON e.guid = cd.container_guid
LEFT JOIN (SELECT ed.entity_guid, MAX(ed.time_created) as last_edited_time, ed.owner_guid as last_editor_guid, COUNT(ed.entity_guid) AS num_contributions FROM {$CONFIG->dbprefix}annotations ed WHERE ed.entity_guid IN ({$elements}) AND ed.name_id IN ({$edited_string_id},{$editeded_string_id}) GROUP BY ed.entity_guid) AS ed ON e.guid = ed.entity_guid
LEFT JOIN users_entity ue ON ed.last_editor_guid = ue.guid
LEFT JOIN (SELECT ru.guid_two as lds_guid, COUNT(ru.guid_one) AS editor_count FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship IN ('lds_editor','lds_editor_group') GROUP BY ru.guid_two) AS ec ON e.guid = ec.lds_guid
LEFT JOIN (SELECT ru.guid_two as lds_guid, COUNT(ru.guid_one) AS viewer_count FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship IN ('lds_viewer','lds_viewer_group') GROUP BY ru.guid_two) AS vc ON e.guid = vc.lds_guid
LEFT JOIN (SELECT entity_guid, string FROM {$CONFIG->dbprefix}metadata met JOIN metastrings ms ON met.value_id = ms.id WHERE met.entity_guid IN ({$elements}) AND met.name_id = {$editor_type_id}) AS met ON e.guid = met.entity_guid
LEFT JOIN (SELECT entity_guid FROM {$CONFIG->dbprefix}metadata m WHERE m.entity_guid IN ({$elements}) AND m.name_id = {$acv_k} AND m.value_id = {$acv_v}) AS iavc ON e.guid = iavc.entity_guid
LEFT JOIN (SELECT entity_guid, GROUP_CONCAT(string SEPARATOR ',') AS string FROM {$CONFIG->dbprefix}metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.entity_guid IN ({$elements}) AND m.name_id = {$tag_id["discipline"]} GROUP BY m.entity_guid) AS t_discipline ON e.guid = t_discipline.entity_guid
LEFT JOIN (SELECT entity_guid, GROUP_CONCAT(string SEPARATOR ',') AS string FROM {$CONFIG->dbprefix}metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.entity_guid IN ({$elements}) AND m.name_id = {$tag_id["pedagogical_approach"]} GROUP BY m.entity_guid) AS t_pedagogic ON e.guid = t_pedagogic.entity_guid
LEFT JOIN (SELECT entity_guid, GROUP_CONCAT(string SEPARATOR ',') AS string FROM {$CONFIG->dbprefix}metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.entity_guid IN ({$elements}) AND m.name_id = {$tag_id["tags"]} GROUP BY m.entity_guid) AS t_tags ON e.guid = t_tags.entity_guid
LEFT JOIN (SELECT entity_guid, string AS editing_tstamp FROM {$CONFIG->dbprefix}metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.entity_guid IN ({$elements}) AND m.name_id = {$ts}) AS editing_tstamp ON e.guid = editing_tstamp.entity_guid
LEFT JOIN (SELECT entity_guid, string AS editing_by FROM {$CONFIG->dbprefix}metadata m LEFT JOIN metastrings ms ON ms.id = m.value_id WHERE m.entity_guid IN ({$elements}) AND m.name_id = {$editing_by}) AS editing_by ON e.guid = editing_by.entity_guid
LEFT JOIN (SELECT entity_guid, m.value_id AS external_editor FROM {$CONFIG->dbprefix}metadata m WHERE m.entity_guid IN ({$elements}) AND m.name_id = {$external_editor}) AS ex ON e.guid = ex.entity_guid
SQL;

        $query['select'] = <<<SQL
e.guid,
sbtp.subtype,
e.time_created,
e.time_updated,
oer.title,
e.owner_guid,
e.time_created,
ex.external_editor,
u.name as creator_name,
u.username as creator_username,
lv.last_viewed_time,
ue.name as last_editor_name,
ue.username as last_editor_username,
ed.last_edited_time as last_edited_time,
ed.num_contributions as num_contributions,
cn.num_comments,
cd.num_documents,
ec.editor_count,
vc.viewer_count,
iavc.entity_guid as all_can_view,
met.string as editor_type,
t_discipline.string as discipline,
t_pedagogic.string as pedagogical_approach,
t_tags.string as tags,
editing_tstamp.editing_tstamp as editing_tstamp,
editing_by.editing_by as editing_by
SQL;

        $query['where'] = "e.guid IN({$elements})";

        return $query;
    }

    private static function buildPermissionsQuery($user_id, $writable_only = true) {
        global $CONFIG;

        if(!$user_id)
            $user_id = get_loggedin_userid();

        if(!($user = get_user($user_id)))
            throw new InvalidParameterException(elgg_echo('InvalidParameterException:UnrecognisedValue'));

        if($user->admin) {
            $myfirstlds = sanitise_string(T("My first LdS"));

            $query['join'] = "JOIN objects_entity oep ON e.guid = oep.guid";
            $query['permission'] = "oep.title <> '{$myfirstlds}' OR e.owner_guid = {$user_id}";

            return $query;
        }

        $acv_query = "";
        $group_permission = "'lds_editor_group', 'lds_viewer_group'";
        $user_permission = "'lds_editor', 'lds_viewer'";

        $acv_k = get_metastring_id("all_can_view");
        $acv_v = get_metastring_id("yes");

        $query['pre'] = array();
        $query['join'] = "";
        $query['permission'] = "";

        if($writable_only) {
            $group_permission = "'lds_editor_group'";
            $user_permission = "'lds_editor'";
        }

        if($writable_only == false) {
            $query['join'] = <<<SQL
LEFT JOIN metadata mavc ON e.guid = mavc.entity_guid

SQL;

            $query['permission'] = <<<SQL
(
    mavc.name_id = {$acv_k} AND mavc.value_id = {$acv_v}
)

OR
SQL;
        }

        $group_permissions = "LEFT JOIN(
        (SELECT rg.guid_two as guid FROM entity_relationships rg JOIN (
        SELECT rug.guid_two as guid FROM entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
        ) AS up ON up.guid = rg.guid_one WHERE rg.relationship IN ({$group_permission})
        ) UNION (
        SELECT ru.guid_two as guid FROM entity_relationships ru WHERE ru.relationship IN ({$user_permission}) AND ru.guid_one = {$user_id}
        )) AS pm ON e.guid = pm.guid";

        $query['join'] .= " $group_permissions";

        $query['permission'] = <<<SQL
{$query['permission']}
(
e.owner_guid = {$user_id}
)
OR
(
    pm.guid IS NOT NULL
)
SQL;

        return $query;
    }
        /**
	 * Performs a full-text search in the LdS (title for all and description for the plain text ones). 
	 * @param unknown_type $query
	 */

    public static function searchLdS($query, $limit = 11, $offset = 0, $user_id = 0, $count = false, $m_key = null, $m_value = null, $enrich = false, $writable_only = false) {
        return self::getUserEntities('object', 'LdS', $user_id, $count, $limit, $offset, $m_key, $m_value, "time", $writable_only, $query, $enrich);
    }

    public static function searchLdS_deprecated($query, $limit = 11, $offset = 0, $user_id = 0, $m_key = null, $m_value = null, $enrich = true, $writable_only = false) {
        global $CONFIG;

        if(!$user_id)
            $user_id = get_loggedin_userid();

        $subtype = get_subtype_id('object', 'LdS');

        $metadata_join = "";
        $metadata_query = "";

        if($m_key && $m_value) {
            $metadata_key_id = get_metastring_id($m_key);
            $metadata_value_id = get_metastring_id($m_value);

            if($metadata_key_id && $metadata_value_id) {
                $metadata_join = 'JOIN metadata mt ON e.guid = mt.entity_guid';
                $metadata_query = "AND mt.name_id = '{$metadata_key_id}' AND mt.value_id = '{$metadata_value_id}'";
                //$metadata_join = '';
                //$metadata_query = "e.guid IN (SELECT DISTINCT m.entity_guid FROM metadata m WHERE m.name_id = '{$metadata_key_id}' AND m.value_id = '{$metadata_value_id}')";
            }
        }

        $tp_k = get_metastring_id("pedagogical_approach");
        $td_k = get_metastring_id("discipline");
        $tt_k = get_metastring_id("tags");
        $t_v = get_metastring_id($query);

        if($t_v)
            $tags_query = "OR ( md.name_id IN ($tp_k,$td_k,$tt_k) AND md.value_id = '{$t_v}' )";
        else
            $tags_query = "";

        $query = sanitise_string($query);
        $offset = sanitise_string((int)$offset);
        $limit = sanitise_string((int)$limit);

        $permissions_query = self::buildPermissionsQuery($user_id, $writable_only);

        $query = <<<SQL
SELECT DISTINCT e.guid, e.type FROM {$CONFIG->dbprefix}entities e JOIN {$CONFIG->dbprefix}metadata m ON e.guid = m.entity_guid JOIN {$CONFIG->dbprefix}metadata md ON e.guid = md.entity_guid JOIN {$CONFIG->dbprefix}objects_entity o ON e.guid = o.guid  JOIN {$CONFIG->dbprefix}entities de ON e.guid = de.container_guid JOIN {$CONFIG->dbprefix}objects_entity do ON de.guid = do.guid {$metadata_join} {$permissions_query['join']} WHERE e.type = 'object' AND e.subtype = $subtype AND e.enabled = 'yes' AND (
	{$permissions_query['permission']}
) AND (
	  (o.title LIKE '%{$query}%' OR do.title LIKE '%{$query}%' OR do.description LIKE '%{$query}%')
	  {$tags_query}
	) {$metadata_query}
	 order by e.time_updated desc limit {$offset},{$limit}
SQL;

        if($entities = get_data($query, "entity_row_to_elggstar")) {
            if ($enrich)
                return self::enrichLdS($entities);
            else
                return $entities;
        }

        return false;
    }

    /*
	public static function searchLdS_old ($query) {
        global $CONFIG;
		$query = addslashes($query);
		
		$sql = "SELECT guid FROM {$CONFIG->dbprefix}objects_entity WHERE MATCH (title,description) AGAINST ('$query')";
		$res = execute_query ($sql, get_db_link('read'));
		//while ($row = mysql_fetch_object($res)) {
		///	$ids[] = $row->guid;
		//}
        $ids = get_data($sql, "ldshake_guid_query");
		
		//Fast fast fast! Only the first 50 results
		$i = 0;
		if (is_array($ids)) {
			foreach ($ids as $id) {
				if ($i == 50) break;
				
				$aux = get_entity($id);
				if ($aux) {
					if ($aux->subtype == get_subtype_id('object', 'LdS') ||
                        $aux->subtype == get_subtype_id('object', 'LdS_document')) {
                        if($aux->subtype == get_subtype_id('object', 'LdS_document'))
                            $aux = get_entity($aux->lds_guid);

                        if($aux) {
                            $lds[] = $aux;
                            $i++;
                        }
					}
				}
			}
			
			return self::enrichLdS($lds);
		} else {
			return false;
		}
	}
	*/
	/**
	 * Marks an LdS as viewed for the logged user.
	 * THis data is stored in teh user's metadata.
	 * 
	 * @param unknown_type $lds_guid
	 */
	public static function markLdSAsViewed ($lds_guid)
	{
		$lds = get_entity($lds_guid);
		$user = get_user(get_loggedin_userid());
        if($lds && $user) {
            $seenLds = $user->seen_lds;
            if (is_null($seenLds)) $seenLds = array();
            if (is_string($seenLds)) $seenLds = array($seenLds);
            foreach ($seenLds as $k=>$v)
            {
                $parts = explode (':',$v);
                if ($parts[0] == $lds_guid)
                    unset($seenLds[$k]);
            }

            $seenLds[] = $lds_guid.':'.$lds->time_updated;
            $user->seen_lds = $seenLds;
            $user->save();
        }
	}


    /**
     * Determines if an LdS supports syncronous edition.
     *
     */
    public static function isSyncLdS ($lds){
        if(strstr($lds->editor_type, 'google'))
            return true;

        return false;
}

	/**
	 * Generates a list of RichLdS objects, containing some useful data for the view.
	 * 
	 * The reason for this is that the core doesn't allow me to create arbitrary fields in an ENtity object
	 * with complex data types (objects, arrays...)
	 * 
	 */
	public static function enrichLdS ($list)
	{
		if (!is_array($list) && !empty($list)) $list = array ($list);
		if (empty($list)) $list = array();
		
		$richList = array();
		foreach ($list as $lds)
		{
            $obj = new stdClass();

            if($lds) {
                $obj = new stdClass();
                //$obj->implementation = $implementation;

                if(($obj->title = $lds->title) == '')
                    $obj->title = T('Untitled LdS');

                $obj->lds = $lds; //The LdS itself
                $obj->starter = get_user($lds->owner_guid);

                //profile
                $editor_types = explode(',', $lds->editor_type);
                $obj->editor_type = $editor_types[0];

                $revision_type = $lds->external_editor ? 'revised_docs_editor' : 'revised_docs';
                if($latest = $lds->getAnnotations($revision_type, 1, 0, 'desc')) {
                    $obj->last_contributor = get_user($latest[0]->owner_guid);
                    $obj->last_contribution_at = $latest[0]->time_created;
                } else {
                    $obj->last_contributor = $obj->starter;
                    $obj->last_contribution_at = $lds->time_created;
                }


                $obj->num_editors = count(lds_contTools::getEditorsIds($lds->guid));
                if($lds->all_can_view == 'yes' || ($lds->all_can_view === null && $lds->access_id < 3 && $lds->access_id > 0)) {
                    $obj->num_viewers = -1;
                } else {
                    $obj->num_viewers = count(lds_contTools::getViewersIds($lds->guid));
                }


                $obj->num_comments = $lds->countAnnotations('generic_comment');
                $obj->num_documents = get_entities_from_metadata ('lds_guid',$lds->guid,'object','LdS_document', 0, 10000, 0, '', 0, true);
                $obj->num_contributions = $lds->countAnnotations($revision_type);


                if(self::isSyncLdS($lds)) {
                    $obj->locked = false;
                    $obj->sync = true;
                } else {
                    if($obj->locked = ($lds->editing_tstamp > time() - 60 && $lds->editing_by != get_loggedin_userid()))
                        $obj->locked_by = get_user($lds->editing_by);
                    $obj->sync = false;
                }

                $isnew = true;

                if($last_viewed = get_annotations($lds->guid, 'object','LdS','viewed_LdS','',get_loggedin_userid(), 1,"","time_created desc")) {
                    $last_viewed = $last_viewed[0]->time_created;
                }  else {
                    $last_viewed = 0;
                }

                if($obj->last_contributor->guid == get_loggedin_userid()) {
                    $isnew = false;
                } elseif($lds->time_updated - 15 < $last_viewed){
                    $isnew = false;
                }

                $obj->new = $isnew;

                $richList[] = $obj;
            } else {
                $richList[] = null;
            }
		}
		
		return $richList;
	}

    public static function enrichImplementation ($list)
    {
        if (!is_array($list) && !empty($list)) $list = array ($list);
        if (empty($list)) $list = array();

        $richList = array();
        foreach ($list as $implementation)
        {
            $obj = new stdClass();
            $implementation->title = ($implementation->title == '') ? T('Untitled implementation') : $implementation->title;
            $obj->implementation = $implementation;
            $obj->lds = $implementation;
            $obj->lds_id = $implementation->lds_id; //The LdS itself
            $obj->starter = get_entity($implementation->owner_guid);
            $obj->glueps = get_entities_from_metadata_multi(array(
                    'lds_guid' => $implementation->guid,
                    'editorType' => 'gluepsrest'
                ),
                'object','LdS_document_editor', 0, 100);

            /* determine if the status of the implementation and assign the appropiate icon
            */
            $obj->icon = 'gluepsrest';
            if(empty($obj->glueps)) {
                $pre_implementation = get_entities_from_metadata_multi(array(
                    'lds_guid' => $implementation->guid,
                ),
                    'object','LdS_document_editor', 0, 1);

                if($pre_implementation) {
                    $obj->icon = 'gluepsrest-wic';
                }
            }
            $latest = $implementation->getAnnotations('revised_docs_editor', 1, 0, 'desc');
            $obj->last_contributor = get_entity($latest[0]->owner_guid);
            $obj->last_contribution_at = $latest[0]->time_created;
            $obj->num_contributions = $implementation->countAnnotations('revised_docs_editor');

            //$obj->num_editors = count(get_members_of_access_collection($lds->write_access_id,true));
            //$obj->num_viewers = ($lds->access_id == 1) ? -1 : count(get_members_of_access_collection($lds->access_id,true));

            $obj->num_viewers = count(lds_contTools::getViewersIds($implementation->guid));
            $obj->num_editors = count(lds_contTools::getEditorsIds($implementation->guid));
            if($implementation->all_can_view == 'yes')
                $obj->num_viewers = -1;

            if($implementation->all_can_view === null)
                $obj->num_viewers = -1;

            $obj->num_comments = $implementation->countAnnotations('generic_comment');

            $obj->locked = ($implementation->editing_tstamp > time() - 60 && $implementation->editing_by != get_loggedin_userid());
            $obj->locked_by = get_user($implementation->editing_by);

            //Add an is_new flag if the user hasn't seen it.
            $seenImplementation = get_user(get_loggedin_userid())->seen_implementation;
            if (is_null($seenImplementation)) $seenImplementation = array();
            if (is_string($seenImplementation)) $seenImplementation = array($seenImplementation);

            $isnew = true;

            if($last_viewed = get_annotations($implementation->guid, 'object','LdS','viewed_LdS','',get_loggedin_userid(), 1,"","time_created desc")) {
                $last_viewed = $last_viewed[0]->time_created;
            }  else {
                $last_viewed = 0;
            }

            if($obj->last_contributor->guid == get_loggedin_userid()) {
                $isnew = false;
            } elseif($lds->time_updated - 15 < $last_viewed){
                $isnew = false;
            }

            $obj->new = $isnew;

            $richList[] = $obj;
        }

        return $richList;
    }

	/**
	 * 
	 * Returns the user Object that is editing this LdS, or false if noone is editing it.
	 */
	public static function isLockedBy ($lds_guid)
	{
		$lds = get_entity($lds_guid);
		if ($lds->editing_tstamp > time() - 60 && $lds->editing_by != get_loggedin_userid())
			return get_user($lds->editing_by);
		else
			return false;
	}
	
	public static function getLdsDocuments ($lds_guid)
	{
        $arr = array();
        if($doc = get_entities_from_metadata('lds_guid',$lds_guid,'object','LdS_document', 0, 10000))
            $arr = array_merge($arr, $doc);

        if($doc = get_entities_from_metadata('lds_guid',$lds_guid,'object','LdS_document_editor', 0, 10000))
            $arr = array_merge($arr, $doc);

        if(is_array($arr))
			Utils::osort($arr, 'guid');
		
		return $arr;
	}
	
	/**
	 * Gets all the necessary data for an LdS revision history display.
	 */
	public static function getRevisionList ($lds)
	{	
        $external = '';
        if($lds->external_editor)
            $external = '_editor';

		$revisions = $lds->getAnnotations('revised_docs'.$external, 10000, 0, 'asc');
		$history = array();
		$revNum = 1;
		if (is_array($revisions))
		{
			foreach ($revisions as $rev)
			{
				$obj = new stdClass();
				$obj->revision_number = $revNum++;
				$obj->revision = $rev;
				$obj->author = get_entity($rev->owner_guid);
				$obj->deleted_documents = explode (',', $rev->value);
				
				//IDEM! Limits suck.
				$documents = get_entities_from_metadata('lds_revision_id', $rev->id, 'object', 'LdS_document'.$external.'_revision', '', 10000);
				$obj->revised_documents = $documents;
				
				$history[] = $obj;
			}
		}
		
		return array_reverse($history);
	}

/**
 * Gets all the necessary data for an LdS revision history display.
 */
	public static function getRevisionEditorList ($lds)
	{	
		//Cutre això del limit, però què hi farem :(
		$revisions = $lds->getAnnotations('revised_docs_editor', 10000, 0, 'asc');
		
		$history = array();
		$revNum = 1;
		foreach ($revisions as $rev)
		{
			$obj = new stdClass();
			$obj->revision_number = $revNum++;
			$obj->revision = $rev;
			$obj->author = get_entity($rev->owner_guid);
			//$obj->deleted_documents = explode (',', $rev->value);
			
			//IDEM! Limits suck.
			$documents = get_entities_from_metadata('lds_revision_id', $rev->id, 'object', 'LdS_document_editor_revision', '', 10000);
			$obj->revised_documents = $documents;
			
			$history[] = $obj;
		}
		
		return array_reverse($history);
	}

	
	public static function getJSONRevisionList ($list)
	{
		//$list = array_reverse($list);
		
		$json = array();
		
		foreach ($list as $rev)
		{
			$docs = array();
			if (is_array($rev->revised_documents))
			{
				foreach ($rev->revised_documents as $doc)
				{
					$docs[] = (object) array (
						'guid' => $doc->guid,
						'document_guid' => $doc->document_guid,
					);
				}
			}
			
			$json[] = (object) array (
				'id' => $rev->revision->id,
				'revision_number' => $rev->revision_number,
				'author' => $rev->author->username,
				'date' => (int)$rev->revision->time_created,//date('j M y H:i',$rev->revision->time_created),
				'revised_docs' => $docs,
				'deleted_docs' => $rev->deleted_documents,
			);
		}
		
		return json_encode($json);
	}
	
	public static function encodeId ($id)
	{
		global $CONFIG;
		
		return base_convert($id + $CONFIG->salt, 10, 36);
	}
	
	public static function decodeId ($id)
	{
		global $CONFIG;
		
		return base_convert(strtolower($id), 36, 10) - $CONFIG->salt;
	}
	
	/**
	 * Return all the tags that I have access to.
	 */
	public static function getMyTags ()
	{
		$fields = array ('tags', 'discipline', 'pedagogical_approach');

        $returnObj = new stdClass();

        foreach ($fields as $f) {
            $time = microtime(true);
            $returnObj->$f = lds_contTools::getUserTagsAvailable($f);
            if(!$returnObj->$f)
                $returnObj->$f = array();
            //echo microtime(true) - $time.' tl<br />'.'<br>';
        }
        /*
		$returnObj = new stdClass();
		$aux = array();
		foreach ($fields as $field)
		{
			$returnObj->$field = array();
			$metadata =  find_metadata_noentity($field,'','object',LDS_ENTITY_TYPE,10000,0);
			if (is_string($metadata)) $metadata = array($metadata);
			if (is_array($metadata) && count($metadata))
			{
				
				//First remove duplicates
				foreach ($metadata as $m){
					$aux[] = $m->value;
				}
				$aux = array_unique($aux);
				
				foreach ($aux as $m)
				{
					$o = new stdClass();
					$o->value = $m;
					$returnObj->{$field}[] = $o;
				}
			}
			
			sort($returnObj->$field);
		}
		*/
		return $returnObj;
	}  
	
	/**
	 * Get the publication status of a given document GUID, along with the published id
	 * Possible statuses are:
	 * -1: Not published
	 * $document_guid: Published
	 * (an integer different than $document_guid): An old revision is published (republishable)
	 * 
	 * @param unknown_type $document_guid
	 */
	public static function getPublishedId ($document_guid)
	{
		$doc = get_entity($document_guid);
	
		if ($doc->published == '1')
		{
			return $doc->guid;
		}
		else
		{
			//Get all the revisions of this document
			$revisions = get_entities_from_metadata('document_guid',$doc->guid,'object','LdS_document_revision',0,10000,0,'time_created');
			
			//Check one of its revisions is marked as published.
			if (is_array($revisions) && count($revisions))
				foreach ($revisions as $rev)
					if ($rev->published == '1')
						return $rev->guid;
		}
		
		return -1;
	}
	
	public static function getPublishedEditorId ($document_guid)
	{
		$doc = get_entity($document_guid);
	
		if ($doc->published == '1')
		{
			return $doc->guid;
		}
		else
		{
			//Get all the revisions of this document
			$revisions = get_entities_from_metadata('document_guid',$doc->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
			
			//Check one of its revisions is marked as published.
			if (is_array($revisions) && count($revisions))
				foreach ($revisions as $rev)
					if ($rev->published == '1')
						return $rev->guid;
		}
		
		return -1;
	}

    /**
     * Builds a minimal array og groups and members for a specified user, to be serialized w/json.
     * @param unknown_type $groups
     */
    public static function buildMinimalUserGroups ($userId)
    {/*
        global $CONFIG;
        $url = sanitise_string($CONFIG->url);

        $query = <<<SQL
SELECT e.guid, ge.name, CONCAT('{$url}pg/icon/',e.guid,'/small') AS pic from entities e
LEFT JOIN groups_entity ge ON ge.guid = e.guid
WHERE (
    e.type = 'group' AND e.enabled = 'yes'
)
SQL;
        //echo '<pre>'.$query.'</pre><br />';
        $entities = get_data($query, "ldshake_dummy_callback");

        return $entities;
        */
        //$groups = self::getUserGroups ($userId, true);
        $groups = get_users_membership($userId);

        $arr = array();

        if (is_array($groups))
        {
            foreach ($groups as $g)
            {
                $o = new stdClass();
                $o->guid = $g->guid;
                $o->name = $g->name;
                $o->pic = $g->getIcon('small');

                $arr[] = $o;
            }
        }

        return $arr;
    }

	/**
	 * Shortcut for counting 
	 */
	public static function countMyLds()
	{
		return self::getMyLds(0, 0, true);
	}
	
	/**
	 * Gets a list of LdS of which:
	 *  - I am the owner
	 *  - I have write permission
	 *  
	 *  Taken from the pages module (world_canread.php - which should be "canedit", function pages_get_entity_metadata_joins)
	 */
	public static function getMyLds($limit = 10, $offset = 0, $count = false, $site_guid = 0, $container_guid = null)
	{
		$type = 'object';
		$subtype = 'LdS';
		$owner_guid = 0;
		$order_by = 'time_updated DESC';
		
		global $CONFIG;
		if ($subtype === false || $subtype === null || $subtype === 0)
			return false;
		
		if ($order_by == "") $order_by = "e.time_created desc";
		$order_by = sanitise_string($order_by);
		$limit = (int)$limit;
		$offset = (int)$offset;
		$site_guid = (int) $site_guid;
		if ($site_guid == 0)
			$site_guid = $CONFIG->site_guid;
			

						
		$where = array();
		$where[] = "e.enabled='yes'";
		if (is_array($type)) {			
			$tempwhere = "";
			if (sizeof($type))
			foreach($type as $typekey => $subtypearray) {
				foreach($subtypearray as $subtypeval) {
					$typekey = sanitise_string($typekey);
					if (!empty($subtypeval)) {
						$subtypeval = (int) get_subtype_id($typekey, $subtypeval);
					} else {
						$subtypeval = 0;
					}
					if (!empty($tempwhere)) $tempwhere .= " or ";
					$tempwhere .= "(type = '{$typekey}' and subtype = {$subtypeval})";
				}								
			}
			if (!empty($tempwhere)) $where[] = "({$tempwhere})";
			
		} else {
		
			$type = sanitise_string($type);
			$subtype = get_subtype_id($type, $subtype);
			
			if ($type != "")
				$where[] = "type='$type'";
			if ($subtype!=="")
				$where[] = "subtype=$subtype";
				
		}
			
		if ($owner_guid != "") {
			if (!is_array($owner_guid)) {
				$owner_array = array($owner_guid);
				$owner_guid = (int) $owner_guid;
			//	$where[] = "owner_guid = '$owner_guid'";
			} else if (sizeof($owner_guid) > 0) {
				$owner_array = array_map('sanitise_int', $owner_guid);
			//  Cast every element to the owner_guid array to int
			//	$owner_guid = array_map("sanitise_int", $owner_guid);
			//	$owner_guid = implode(",",$owner_guid);
			//	$where[] = "owner_guid in ({$owner_guid})";
			}
			if (is_null($container_guid)) {
				$container_guid = $owner_array;
			}
		}
		if ($site_guid > 0)
			$where[] = "site_guid = {$site_guid}";

		if (!is_null($container_guid)) {
			if (is_array($container_guid)) {
				foreach($container_guid as $key => $val) $container_guid[$key] = (int) $val;
				$where[] = "container_guid in (" . implode(",",$container_guid) . ")";
			} else {
				$container_guid = (int) $container_guid;
				$where[] = "container_guid = {$container_guid}";
			}
		}
		
		$write_access_idstring=get_metastring_id('write_access_id');
		$where[] = "d.name_id = '{$write_access_idstring}'";
		
		if (!$count) {
			$query = " SELECT e.*, s.string
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where ";
		} else {
			$query = "SELECT count(guid) as total 
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where";
		}

		
		foreach ($where as $w)
			$query .= " $w and ";
			$query .= " ((e.owner_guid = {$_SESSION['user']->getGUID()}) OR (";
			$query .= ' s.string IN ';
			
		$query .= get_access_list($_SESSION['user']->guid, $site_guid, false); // Add access controls
		$query .= " and ".get_access_sql_suffix('e');
		
		$query .= " )) ";
		if (!$count) {
			$query .= " order by $order_by";
			
			if ($limit) $query .= " limit $offset, $limit"; // Add order and limit
//echo "$query<br/>";
//die;
			$dt = get_data($query, "entity_row_to_elggstar");
			return $dt;
		} else {
//echo "$query<br/>";
			$total = get_data_row($query);
			return $total->total;
		}
	}
	
	
	/**
	 * Shortcut for counting 
	 */
	public static function countLdsSharedWithMe()
	{
		return self::getMyLds(0, 0, true);
	}
	
	/**
	 * Gets a list of LdS of which:
	 *  - I am the owner
	 *  - I have write permission
	 *  
	 *  Taken from the pages module (world_canread.php - which should be "canedit", function pages_get_entity_metadata_joins)
	 */
	public static function getLdsSharedWithMe($limit = 10, $offset = 0, $count = false, $site_guid = 0, $container_guid = null)
	{
		$type = 'object';
		$subtype = 'LdS';
		$owner_guid = 0;
		$order_by = 'time_updated DESC';
		
		global $CONFIG;
		if ($subtype === false || $subtype === null || $subtype === 0)
			return false;
		
		if ($order_by == "") $order_by = "e.time_created desc";
		$order_by = sanitise_string($order_by);
		$limit = (int)$limit;
		$offset = (int)$offset;
		$site_guid = (int) $site_guid;
		if ($site_guid == 0)
			$site_guid = $CONFIG->site_guid;
			

						
		$where = array();
		
		if (is_array($type)) {			
			$tempwhere = "";
			if (sizeof($type))
			foreach($type as $typekey => $subtypearray) {
				foreach($subtypearray as $subtypeval) {
					$typekey = sanitise_string($typekey);
					if (!empty($subtypeval)) {
						$subtypeval = (int) get_subtype_id($typekey, $subtypeval);
					} else {
						$subtypeval = 0;
					}
					if (!empty($tempwhere)) $tempwhere .= " or ";
					$tempwhere .= "(type = '{$typekey}' and subtype = {$subtypeval})";
				}								
			}
			if (!empty($tempwhere)) $where[] = "({$tempwhere})";
			
		} else {
		
			$type = sanitise_string($type);
			$subtype = get_subtype_id($type, $subtype);
			
			if ($type != "")
				$where[] = "type='$type'";
			if ($subtype!=="")
				$where[] = "subtype=$subtype";
				
		}
			
		if ($owner_guid != "") {
			if (!is_array($owner_guid)) {
				$owner_array = array($owner_guid);
				$owner_guid = (int) $owner_guid;
			//	$where[] = "owner_guid = '$owner_guid'";
			} else if (sizeof($owner_guid) > 0) {
				$owner_array = array_map('sanitise_int', $owner_guid);
			//  Cast every element to the owner_guid array to int
			//	$owner_guid = array_map("sanitise_int", $owner_guid);
			//	$owner_guid = implode(",",$owner_guid);
			//	$where[] = "owner_guid in ({$owner_guid})";
			}
			if (is_null($container_guid)) {
				$container_guid = $owner_array;
			}
		}
		if ($site_guid > 0)
			$where[] = "site_guid = {$site_guid}";

		if (!is_null($container_guid)) {
			if (is_array($container_guid)) {
				foreach($container_guid as $key => $val) $container_guid[$key] = (int) $val;
				$where[] = "container_guid in (" . implode(",",$container_guid) . ")";
			} else {
				$container_guid = (int) $container_guid;
				$where[] = "container_guid = {$container_guid}";
			}
		}
		
		$write_access_idstring=get_metastring_id('write_access_id');
		$where[] = "d.name_id = '{$write_access_idstring}'";
		
		if (!$count) {
			$query = " SELECT e.*, s.string
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where ";
		} else {
			$query = "SELECT count(guid) as total 
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where";
		}

		
		foreach ($where as $w)
			$query .= " $w and ";
			$query .= " ((e.owner_guid <> {$_SESSION['user']->getGUID()}) AND (";
			$query .= ' s.string IN ';
			
		$query .= get_access_list($_SESSION['user']->guid, $site_guid, false); // Add access controls
		$query .= " and ".get_access_sql_suffix('e');
		
		$query .= " )) ";
		if (!$count) {
			$query .= " order by $order_by";
			
			if ($limit) $query .= " limit $offset, $limit"; // Add order and limit
//echo "$query<br/>";
			$dt = get_data($query, "entity_row_to_elggstar");
			return $dt;
		} else {
//echo "$query<br/>";
			$total = get_data_row($query);
			return $total->total;
		}
	}
	
	
	/**
	 * Gets a list of LdS that accomplish the following rules:
	 *  - I am not the owner of them
	 *  - I don't have write permissions on them
	 *  - I have read permissions on them
	 */
	public static function getBrowseLdsList ($order_by = "", $limit = 10, $offset = 0, $tagname = '', $tagvalue = '', $count = false, $site_guid = 0, $container_guid = null)
	{
		$type = 'object';
		$subtype = 'LdS';
		$owner_guid = 0;
		
		global $CONFIG;
		if ($subtype === false || $subtype === null || $subtype === 0)
			return false;
		
		if ($order_by == "") $order_by = "e.time_created desc";
		$order_by = sanitise_string($order_by);
		$limit = (int)$limit;
		$offset = (int)$offset;
		$site_guid = (int) $site_guid;
		if ($site_guid == 0)
			$site_guid = $CONFIG->site_guid;
			

						
		$where = array();
		
		if (is_array($type)) {			
			$tempwhere = "";
			if (sizeof($type))
			foreach($type as $typekey => $subtypearray) {
				foreach($subtypearray as $subtypeval) {
					$typekey = sanitise_string($typekey);
					if (!empty($subtypeval)) {
						$subtypeval = (int) get_subtype_id($typekey, $subtypeval);
					} else {
						$subtypeval = 0;
					}
					if (!empty($tempwhere)) $tempwhere .= " or ";
					$tempwhere .= "(type = '{$typekey}' and subtype = {$subtypeval})";
				}								
			}
			if (!empty($tempwhere)) $where[] = "({$tempwhere})";
			
		} else {
		
			$type = sanitise_string($type);
			$subtype = get_subtype_id($type, $subtype);
			
			if ($type != "")
				$where[] = "type='$type'";
			if ($subtype!=="")
				$where[] = "subtype=$subtype";
				
		}
			
		if ($owner_guid != "") {
			if (!is_array($owner_guid)) {
				$owner_array = array($owner_guid);
				$owner_guid = (int) $owner_guid;
			//	$where[] = "owner_guid = '$owner_guid'";
			} else if (sizeof($owner_guid) > 0) {
				$owner_array = array_map('sanitise_int', $owner_guid);
			//  Cast every element to the owner_guid array to int
			//	$owner_guid = array_map("sanitise_int", $owner_guid);
			//	$owner_guid = implode(",",$owner_guid);
			//	$where[] = "owner_guid in ({$owner_guid})";
			}
			if (is_null($container_guid)) {
				$container_guid = $owner_array;
			}
		}
		if ($site_guid > 0)
			$where[] = "site_guid = {$site_guid}";

		if (!is_null($container_guid)) {
			if (is_array($container_guid)) {
				foreach($container_guid as $key => $val) $container_guid[$key] = (int) $val;
				$where[] = "container_guid in (" . implode(",",$container_guid) . ")";
			} else {
				$container_guid = (int) $container_guid;
				$where[] = "container_guid = {$container_guid}";
			}
		}
		
		$where[] = 'e.access_id <> 0';
		$where[] = 'e.owner_guid <> '.get_loggedin_userid();
		
		$write_access_idstring=get_metastring_id('write_access_id');
		$where[] = "d.name_id = '{$write_access_idstring}'";
		
		
		$anotherjoin = '';
		if ($tagname != '' && $tagvalue != '')
		{
			$anotherjoin = ' LEFT JOIN metadata d2 ON e.guid = d2.entity_guid '; 
			
			$k = get_metastring_id($tagname);
			$v = get_metastring_id($tagvalue);
			$where[] = "d2.name_id = '{$k}'";
			$where[] = "d2.value_id = '{$v}'";
		}
		
		
		if (!$count) {
			$query = " SELECT e.*, s.string
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						$anotherjoin
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where ";
		} else {
			$query = "SELECT count(guid) as total 
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						$anotherjoin
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where";
		}

		
		foreach ($where as $w)
			$query .= " $w and ";
			//$query .= " ((e.owner_guid = {$_SESSION['user']->getGUID()}) OR (";
			$query .= ' s.string NOT IN ';
			
		$query .= get_access_list($_SESSION['user']->guid, $site_guid, false); // Add access controls
		$query .= " and ".self::get_browse_lds_access_sql_suffix('e');
		
		//$query .= " )) ";
		if (!$count) {
			$query .= " order by $order_by";
			
			if ($limit) $query .= " limit $offset, $limit"; // Add order and limit
//echo "$query<br/>";
			$dt = get_data($query, "entity_row_to_elggstar");
			return $dt;
		} else {
//echo "$query<br/>";
			$total = get_data_row($query);
			return $total->total;
		}
	}
	
	
	private static function get_browse_lds_access_sql_suffix($table_prefix = "")
	{
		global $ENTITY_SHOW_HIDDEN_OVERRIDE;  
		
		$sql = "";
		
		if ($table_prefix)
				$table_prefix = sanitise_string($table_prefix) . ".";
		
			$access = get_access_list();
			
			global $is_admin;
			
			if (isset($is_admin) && $is_admin == true) {
				$sql = " (1 = 1) ";
			}

			if (empty($sql))
				$sql = " ({$table_prefix}access_id in {$access})";

		if (!$ENTITY_SHOW_HIDDEN_OVERRIDE)
			$sql .= " and {$table_prefix}enabled='yes'";
		
		return $sql;
	}
	
	/**
	 * Returns the ID of the anonymous access collection containing all and only the users specified int
	 * the array $userIds.
	 * 
	 * If such collection doesn't exist, this function creates it.
	 * 
	 * @param array $userIds
	 */
	public static function getUserCollection ($userIds)
	{
		//The ids are sorted and coined with commas.
		//The resulting string is hashed with an md5 and two underscores are prepended.
		//This will be the collection name, so queries that search by name (which is a calculation of the contained members) can be super-fast. 
		sort($userIds);
		$colname = "__".md5(implode(',', $userIds));
		
		$col = get_access_collection_by_name($colname);
		
		//DOesn't exist. We create it.
		if (!$col)
		{
			$colid = create_access_collection($colname);
			update_access_collection($colid, $userIds);
			$col = get_access_collection($colid);
		}
		
		return $col->id;
	}
	
	public static function getFriendsArray ($userId)
	{
		//stupid limits
		$users = get_user_friends($userId, '', 100000, 0);
		$jsonusers = array();
		if (is_array($users) && count($users))
		{
			foreach ($users as $u)
			{
				$o = new stdClass();
				$o->guid = $u->guid;
				$o->name = $u->name;
				$o->pic = $u->getIcon('small');
				$jsonusers[] = $o;
			}
		}
		
		return $jsonusers;
	}

    public static function buildFriendArrays ($friendsArray, $access_id, $write_access_id)
    {
        $returnData = array();
        $returnData['available'] = $friendsArray;
        $returnData['viewers'] = array ();
        $returnData['editors'] = array ();

        $view_ids = get_members_of_access_collection($access_id, true) ?: array();
        $edit_ids = get_members_of_access_collection($write_access_id, true) ?: array();

        $view_ids = array_diff($view_ids, $edit_ids);

        foreach ($view_ids as $id)
        {
            foreach ($returnData['available'] as $k=>$v)
            {
                if ($returnData['available'][$k]->guid == $id)
                {
                    $o = new stdClass();
                    $o->guid = $returnData['available'][$k]->guid;
                    $o->name = $returnData['available'][$k]->name;
                    $o->pic = $returnData['available'][$k]->pic;
                    $returnData['viewers'][] = $o;

                    unset($returnData['available'][$k]);
                }
            }
        }

        foreach ($edit_ids as $id)
        {
            foreach ($returnData['available'] as $k=>$v)
            {
                if ($returnData['available'][$k]->guid == $id)
                {
                    $o = new stdClass();
                    $o->guid = $returnData['available'][$k]->guid;
                    $o->name = $returnData['available'][$k]->name;
                    $o->pic = $returnData['available'][$k]->pic;
                    $returnData['editors'][] = $o;

                    unset($returnData['available'][$k]);
                }
            }
        }

        return $returnData;
    }

    public static function getTotalViewersIds($lds_id)
    {
        global $CONFIG;
        $query = "SELECT r.guid_one AS guid FROM {$CONFIG->dbprefix}entity_relationships r ";
        $query .= " WHERE r.guid_two = {$lds_id} AND r.relationship IN ('lds_viewer', 'lds_editor', 'lds_viewer_group', 'lds_editor_group')";

        //$guids = array();
        $guids = get_data($query, "ldshake_guid_query");
        //$result = execute_query ($query, get_db_link('read'));
        //while($row = mysql_fetch_object($result)) {
        //    $guids[] = $row->guid;
        ///}
        if(!$guids)
            $guids = array();
        return $guids;
    }

    public static function getEditorsIds($lds_id)
    {
        $query = "SELECT r.guid_one AS guid FROM {$CONFIG->dbprefix}entity_relationships r ";
        $query .= " WHERE r.guid_two = {$lds_id} AND r.relationship IN ('lds_editor', 'lds_editor_group')";

        $guids = array();
        //$result = execute_query ($query, get_db_link('read'));
        //while($row = mysql_fetch_object($result)) {
        //    $guids[] = $row->guid;
        //}
        $guids = get_data($query, "ldshake_guid_query");
        if(!$guids)
            $guids = array();
        return $guids;
    }

    public static function getViewersIds($lds_id)
    {
        $query = "SELECT r.guid_one AS guid FROM {$CONFIG->dbprefix}entity_relationships r ";
        $query .= " WHERE r.guid_two = {$lds_id} AND r.relationship IN ('lds_viewer', 'lds_viewer_group')";

        //$guids = array();
        //$result = execute_query ($query, get_db_link('read'));
        //while($row = mysql_fetch_object($result)) {
        //    $guids[] = $row->guid;
        //}
        $guids = get_data($query, "ldshake_guid_query");
        if(!$guids)
            $guids = array();
        return $guids;
    }

    public static function getEditorsUsers($lds_guid)
    {
        global $CONFIG;
        $url = sanitise_string($CONFIG->url);
        $user_id = get_loggedin_userid();

        $query = <<<SQL
(SELECT e.guid, e.type, ue.name, CONCAT('{$url}pg/icon/',ue.username,'/small') AS pic from {$CONFIG->dbprefix}entities e
LEFT JOIN users_entity ue ON ue.guid = e.guid
WHERE (
	e.type = 'user'
	AND e.enabled = 'yes'
	AND e.guid <> {$user_id}
	AND e.guid IN(
	SELECT r.guid_one as guid FROM {$CONFIG->dbprefix}entity_relationships r WHERE r.guid_two = {$lds_guid} AND r.relationship IN ('lds_editor')
	)
)
) UNION (
SELECT e.guid, e.type, ue.name, CONCAT('{$url}pg/groupicon/',e.guid,'/small') AS pic from {$CONFIG->dbprefix}entities e
LEFT JOIN groups_entity ue ON ue.guid = e.guid
WHERE (
	e.type = 'group'
	AND e.enabled = 'yes'
	AND e.guid IN (
	SELECT r.guid_one as guid FROM {$CONFIG->dbprefix}entity_relationships r WHERE r.guid_two = {$lds_guid} AND r.relationship IN ('lds_editor_group')
	)
)
)
SQL;

        $entities = get_data($query);
        if(!$entities)
            $entities = array();

        return $entities;
    }

    public static function getViewersUsers($lds_guid)
    {
        global $CONFIG;
        $url = sanitise_string($CONFIG->url);
        $user_id = get_loggedin_userid();

        $query = <<<SQL
        (SELECT e.guid, e.type, ue.name, CONCAT('{$url}pg/icon/',ue.username,'/small') AS pic from {$CONFIG->dbprefix}entities e
LEFT JOIN users_entity ue ON ue.guid = e.guid
WHERE (
	e.type = 'user'
	AND e.enabled = 'yes'
	AND e.guid <> {$user_id}
	AND e.guid IN(
	SELECT r.guid_one as guid FROM {$CONFIG->dbprefix}entity_relationships r WHERE r.guid_two = {$lds_guid} AND r.relationship IN ('lds_viewer')
	)
)
) UNION (
SELECT e.guid, e.type, ue.name, CONCAT('{$url}pg/groupicon/',e.guid,'/small') AS pic from {$CONFIG->dbprefix}entities e
LEFT JOIN groups_entity ue ON ue.guid = e.guid
WHERE (
	e.type = 'group'
	AND e.enabled = 'yes'
	AND e.guid IN (
	SELECT r.guid_one as guid FROM {$CONFIG->dbprefix}entity_relationships r WHERE r.guid_two = {$lds_guid} AND r.relationship IN ('lds_viewer_group')
	)
)
)
SQL;

        $entities = get_data($query);

        if(!$entities)
            $entities = array();

        return $entities;
    }

    public static function getViewAccessUsers($lds_id) {
        $members = array();

        $lds = get_entity($lds_id);
        if($lds->all_can_view == 'yes') {
            $site_users = get_entities('user','',0,'',9999);

            $unique_user_guids = array();
            foreach($site_users as $u) {
                $unique_user_guids[] = $u->getGUID();
            }

            return $unique_user_guids;
        }



        $viewers = self::getViewersUsers($lds_id);
        $editors = self::getEditorsUsers($lds_id);

        if(is_array($viewers))
            $members = array_merge($viewers, $members);

        if(is_array($editors))
            $members = array_merge($editors, $members);

        $users = array();

        foreach($members as $member) {
            if($member instanceof ElggUser)
                $users[] = $member;
            elseif ($member instanceof ElggGroup) {
                $users = array_merge($users, $member->getMembers(9999));
            }
        }

        $user_guids = array();
        foreach($users as $user) {
            $user_guids[] = $user->getGUID();
        }

        $user_guids[] = $lds->owner_guid;
        $unique_user_guids = array_unique($user_guids);

        return $unique_user_guids;
    }

    public static function getAvailableUsers($lds = null)
    {
        global $CONFIG;
        $url = sanitise_string($CONFIG->url);
        $user_id = get_loggedin_userid();

        if(!$lds)
            $lds_guid = 0;
        else
            $lds_guid = $lds->guid;

            $query = <<<SQL
(SELECT e.guid, e.type, ue.name, CONCAT('{$url}pg/icon/',ue.username,'/small') AS pic from {$CONFIG->dbprefix}entities e
LEFT JOIN users_entity ue ON ue.guid = e.guid
WHERE (
	e.type = 'user'
	AND e.enabled = 'yes'
	AND e.guid <> {$user_id}
	AND e.guid NOT IN(
	SELECT r.guid_one as guid FROM {$CONFIG->dbprefix}entity_relationships r WHERE r.guid_two = {$lds_guid} AND r.relationship IN ('lds_viewer', 'lds_editor')
	)
)
) UNION (
SELECT e.guid, e.type, ue.name, CONCAT('{$url}pg/groupicon/',e.guid,'/small') AS pic from {$CONFIG->dbprefix}entities e
LEFT JOIN groups_entity ue ON ue.guid = e.guid
WHERE (
	e.type = 'group'
	AND e.enabled = 'yes'
	AND e.guid NOT IN (
	SELECT r.guid_one as guid FROM {$CONFIG->dbprefix}entity_relationships r WHERE r.guid_two = {$lds_guid} AND r.relationship IN ('lds_viewer_group', 'lds_editor_group')
	)
)
)
SQL;
            $entities = get_data($query, "ldshake_available_users_callback");

            return $entities;
    }

    public static function getUserSharedLdSWithMe($user_id, $count = false, $limit = 0, $offset = 0) {
        return self::getUserSharedObjectsWithMe('object', 'LdS', $user_id, $count, $limit, $offset);
    }

    public static function getUserSharedObjectsWithMe($type, $subtype, $user_id, $count = false, $limit = 0, $offset = 0) {
        global $CONFIG;

        $query_limit = ($limit == 0) ? '' : "limit {$offset}, {$limit}";
        $subtype = get_subtype_id($type, $subtype);

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = '{$type}' AND e.subtype = $subtype AND e.owner_guid <> {$user_id} AND e.enabled = 'yes' AND (
	(
		e.guid IN (
			SELECT DISTINCT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.relationship = 'lds_editor_group' AND rg.guid_one IN (
				SELECT rug.guid_two FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
			)
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship = 'lds_editor' AND ru.guid_one = {$user_id}
		)
	)
) order by time_updated desc {$query_limit}
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;

    }

    public static function getUserEditableImplementations($user_id, $count = false, $limit = 0, $offset = 0, $m_key = null, $m_value = null, $container_guid = null) {
        global $CONFIG;

        if(isadminloggedin()) {
            if($count)
                return get_entities_from_metadata($m_key, $m_value, 'object', 'LdS_implementation', 0, 9999, 0, "time", 0, true);
            else
                return get_entities_from_metadata($m_key, $m_value, 'object', 'LdS_implementation', 0, $limit, $offset, "time_updated desc");
        }

        $query_limit = ($limit == 0 || $count) ? '' : "limit {$offset}, {$limit}";
        $subtype = get_subtype_id('object', 'LdS_implementation');

        $metadata_join = "";
        $metadata_query = "";

        if($m_key && $m_value) {
            $metadata_key_id = get_metastring_id($m_key);
            $metadata_value_id = get_metastring_id($m_value);

            $metadata_join = 'JOIN metadata m ON e.guid = m.entity_guid';
            $metadata_query = "AND m.name_id = '{$metadata_key_id}' AND m.value_id = '{$metadata_value_id}'";
        }

        $container_guid_query = "";
        if($container_guid)
            $container_guid_query = "AND e.container_guid = {$container_guid}";

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e {$metadata_join} WHERE e.type = 'object' AND e.subtype = $subtype $container_guid_query AND e.enabled = 'yes' {$metadata_query}
AND (
	(e.owner_guid = {$user_id})
	OR
	(
		e.guid IN (
			SELECT DISTINCT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.relationship = 'lds_editor_group' AND rg.guid_one IN (
				SELECT rug.guid_two FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
			)
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship = 'lds_editor' AND ru.guid_one = {$user_id}
		)
	)
) order by time_updated desc {$query_limit}
SQL;
        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;

    }

    public static function getUserSharedImplementationWithMe($user_id, $count = false, $limit = 0, $offset = 0) {
        global $CONFIG;

        $query_limit = ($limit == 0) ? '' : "limit {$offset}, {$limit}";
        $subtype = get_subtype_id('object', 'LdS_implementation');

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = $subtype AND e.owner_guid <> {$user_id} AND e.enabled = 'yes' AND (
	(
		e.guid IN (
			SELECT DISTINCT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.relationship = 'lds_editor_group' AND rg.guid_one IN (
				SELECT rug.guid_two FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
			)
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship = 'lds_editor' AND ru.guid_one = {$user_id}
		)
	)
) order by time_updated desc {$query_limit}
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;
    }

    public static function getUserEditableLdSs($user_id, $count = false, $limit = 0, $offset = 0, $m_key = null, $m_value = null) {
        return self::getUserEditableEntities('object', 'LdS', $user_id, $count, $limit, $offset, $m_key, $m_value);
    }

    public static function getUserEditableProjects($user_id, $count = false, $limit = 0, $offset = 0, $m_key = null, $m_value = null) {
        return self::getUserEntities('object', 'LdSProject', $user_id, $count, $limit, $offset, $m_key, $m_value, 'time', true);
    }

    public static function getUserViewableProjects($user_id, $count = false, $limit = 0, $offset = 0, $m_key = null, $m_value = null) {
        return self::getUserEntities('object', 'LdSProject', $user_id, $count, $limit, $offset, $m_key, $m_value, 'time', false);
    }

    public static function getUserEditableProjectImplementations($user_id, $count = false, $limit = 0, $offset = 0, $m_key = null, $m_value = null) {
        return self::getUserEntities('object', 'LdSProject_implementation', $user_id, $count, $limit, $offset, $m_key, $m_value, 'time', true);
    }


    public static function getUserEditableEntities($type = 'object', $subtype = 'LdS', $user_id = 0, $count = false, $limit = 0, $offset = 0, $m_key = null, $m_value = null) {
        global $CONFIG;

        $offset = sanitise_string((int)$offset);
        $limit = sanitise_string((int)$limit);
        $type = sanitise_string($type);

        if(!$user_id)
            $user_id = get_loggedin_userid();

        $query_limit = ($limit == 0 || $count) ? '' : "limit {$offset}, {$limit}";
        $subtype = get_subtype_id($type, $subtype);

        $metadata_join = "";
        $metadata_query = "";

        if($m_key && $m_value) {
            $metadata_key_id = get_metastring_id($m_key);
            $metadata_value_id = get_metastring_id($m_value);

            $metadata_join = 'JOIN metadata m ON e.guid = m.entity_guid';
            $metadata_query = "AND m.name_id = '{$metadata_key_id}' AND m.value_id = '{$metadata_value_id}'";
        }

        if ($count) {
            $count_query = "count(distinct e.guid) as total";
        } else {
            $count_query = "e.*";
        }

        $permissions_query = self::buildPermissionsQuery($user_id, true);

        $query = <<<SQL
SELECT {$count_query} from {$CONFIG->dbprefix}entities e {$metadata_join} {$permissions_query['join']}
WHERE e.type = '{$type}' AND e.subtype = {$subtype} AND e.enabled = 'yes' {$metadata_query}
AND ({$permissions_query['permission']})
order by time_updated desc {$query_limit}
SQL;

        if($count) {
            $row = get_data_row($query);
            return $row->total;
        } else {
            $entities = get_data($query, "entity_row_to_elggstar");
            return $entities;
        }
    }

    public static function getProjectLdSList($pd_guid, $only_exclusive = false, $guid_only = false, $vle_implementation = false, $enrich = false) {
        global $CONFIG;

        $callback = "entity_row_to_elggstar";
        $table = "objects_property";

        if($enrich)
            $callback = "ldshake_richlds";
        else
            $table = "entities";

        if($guid_only)
            $callback = "ldshake_guid_callback";

        if($enrich and $guid_only)
            throw new Exception("Invalid function parameters");

        $rel = "'lds_project_existent','lds_project_nfe','lds_project_new'";
        if($only_exclusive)
            $rel = "'lds_project_nfe','lds_project_new'";
        if($vle_implementation)
            $rel = "'lds_project_vle_implementation'";

        $query = <<<SQL
SELECT *, 'object' as type FROM {$table} e NATURAL JOIN entities en WHERE guid IN (
			SELECT DISTINCT rg.guid_one FROM entity_relationships rg WHERE
			rg.relationship IN ({$rel})
			AND rg.guid_two = {$pd_guid}
		) AND en.enabled = 'yes'
SQL;

        $entities = get_data($query, $callback);

        return $entities;
    }

    public static function getUserTagsAvailable($category) {
        return self::getUserEntities('object', 'LdS', get_loggedin_userid(), false, 0, 0, null, null, null, false, null, false, array("build_callback" => "ldshake_user_available_tags_query", "callback" => "ldshake_dummy_callback", "params" => $category));
    }

    public static function getUserTagFrequency($category) {
        return self::getUserEntities('object', 'LdS', get_loggedin_userid(), false, 0, 0, null, null, null, false, null, false, array("build_callback" => "ldshake_tag_relevance_query", "callback" => "ldshake_tag_callback", "params" => $category), false, false, 0, 'viewlist');
    }

    public static function getUserViewableLdSs($user_id, $count = false, $limit = 9999, $offset = 0, $mk = null, $mv = null, $order = "time", $enrich = false, $custom = null, $context = null) {
        return self::getUserEntities('object', 'LdS', $user_id, $count, $limit, $offset, $mk, $mv, $order, false, null, $enrich, $custom = null, false, false, 0, $context);
    }

    public static function getUserViewableImplementedLdSs($user_id, $count = false, $limit = 9999, $offset = 0, $mk = null, $mv = null, $order = "time", $enrich = false, $custom = null, $context = null) {

        $custom = array(
            'build_callback' => 'ldshake_query_design_implementated_list',
            'params' => array()
        );

        return self::getUserEntities('object', 'LdS', $user_id, $count, $limit, $offset, $mk, $mv, $order, false, null, $enrich, $custom, false, false, 0, $context);
    }

    public static function getUserEditableLdS($user_id, $count = false, $limit = 9999, $offset = 0, $mk = null, $mv = null, $order = "time", $enrich = false, $custom = null, $guid_only = false) {
        return self::getUserEntities('object', 'LdS', $user_id, $count, $limit, $offset, $mk, $mv, $order, true, null, $enrich, $custom, $guid_only);
    }

    public static function getUserEntities($type, $subtype, $user_id, $count = false, $limit = 9999, $offset = 0, $mk = null, $mv = null, $order = "time", $writable_only = false, $search = null, $enrich = false, $custom = null, $guid_only = false, $check_guid = null, $owner_guid = 0, $context = null) {
        global $CONFIG;

        $table = "objects_property";

        if($count)
            $enrich = false;
        $offset = sanitise_string((int)$offset);
        $limit = sanitise_string((int)$limit);
        $type = sanitise_string($type);

        $query_limit = ($limit == 0 || $count) ? '' : "limit {$offset}, {$limit}";
        $subtype = get_subtype_id($type, $subtype);
        if(empty($subtype)) {
            if($count)
                return 0;
            else
                return false;
        }

        $callback = "entity_row_to_elggstar";

        $mj = '';
        $mw = '';

        if($mk && $mv) {
            $mk_id = get_metastring_id($mk);
            $mv_id = get_metastring_id($mv);

            if($mk_id && $mv_id) {
                $mj = "JOIN metadata md ON e.guid = md.entity_guid";
                $mw = "md.name_id = {$mk_id} AND md.value_id = {$mv_id} AND";
            } else {
                return false;
            }
        }

        //$permissions_query = self::buildPermissionsQuery($user_id, $writable_only);
        $permissions_query = buildPermissionsQuery($user_id, $writable_only, ldshake_isglobalenv($type, $subtype), $context);

        $rich_query['columns'] = '';
        $rich_query['join'] = '';
        $rich_query['pre'] = array();

        $e_base = "";
        if($enrich) {
            $callback = "ldshake_richlds";
        }

        if($guid_only) {
            $callback = "ldshake_guid_callback";
        }

        if($owner_guid) {
            $owner_guid_query = "AND e.owner_guid = {$owner_guid}";
        } else {
            $owner_guid_query = "";
        }

        if ($count) {
            $count_query = "COUNT(*) as total";
        } else {
            $count_query = "DISTINCT e.guid, 'object' AS type";
        }

        $order_query['join'] = "";
        $order_query['query'] = "";

        if($order == "title") {
            $order_query['join'] = "FORCE INDEX(title)";
            $order_query['by'] = "ORDER BY e.title";
        } elseif($order == "time") {
            if(!$count) {
            $order_query['join'] = "";
            $order_query['by'] = "ORDER BY e.last_edited_time DESC";
            }
        } else {
            $order_query['join'] = "";
            $order_query['by'] = "";
        }

        $search_query['join'] = "";
        $search_query['query'] = "";

        if(is_string($search)) {
            $l_ds = get_subtype_id('object', 'LdS');
            $l_doc = get_subtype_id('object', 'LdS_document');

            $search = preg_replace("/[^\pL\s\pNd]+/u", " ", $search);
            $search = sanitise_string($search);

            if(strlen(trim($search)) > 0 && str_word_count(trim($search)) > 0) {
                $search_query['join'] = "LEFT JOIN objects_property op ON op.container_guid = e.guid LEFT JOIN {$CONFIG->dbprefix}objects_entity do ON op.guid = do.guid";
                $search_query['query'] = <<<SQL
(
(op.subtype = {$l_doc} AND MATCH(do.title,do.description) AGAINST('{$search}'))
OR
(e.subtype = {$l_ds} AND (MATCH(e.title) AGAINST('{$search}'))
OR(MATCH(e.tags,e.discipline,e.pedagogical_approach) AGAINST('+({$search})' IN BOOLEAN MODE)))
) AND
SQL;
            } else {
                return $count ? 0 : false;
            }
        }

        $custom_join = '';
        $custom_where = '';
        $custom_group_by = '';
        if($custom) {
            if(isset($custom['callback']))
                $callback = $custom['callback'];

            $build_callback = $custom['build_callback'];
            $custom_query = $build_callback($user_id, $custom['params']);

            if($custom_query === null) {
                if($count)
                    return 0;
                else
                    return false;
            }

            if(!$count)
                $count_query = isset($custom_query['select']) ? $custom_query['select'] : $count_query;

            $order_query = isset($custom_query['order']) ? $custom_query['order'] : $order_query;
            $custom_join = isset($custom_query['join']) ? $custom_query['join'] : '';
            $custom_where = isset($custom_query['where']) ? "AND ({$custom_query['where']})" : '';
            $custom_group_by = isset($custom_query['group_by']) ? 'GROUP BY '.$custom_query['group_by'] : '';
        }

        $guid_where = "";
        if(is_numeric($check_guid)) {
            $check_guid = (int)$check_guid;
            if($check_guid > 0)
                $guid_where = <<<SQL
 AND e.guid = {$check_guid}
SQL;
        }

$query = <<<SQL
SELECT {$count_query} FROM {$CONFIG->dbprefix}{$table} e
{$order_query['join']}
{$custom_join}
{$mj}
{$search_query['join']}
{$permissions_query['join']}
WHERE {$search_query['query']} {$mw} e.subtype = {$subtype}
AND ({$permissions_query['permission']})
{$owner_guid_query}
{$custom_where}
{$guid_where}
{$custom_group_by}
{$order_query['by']}
{$query_limit}
SQL;
        if($enrich) {
            $query_size = 300;
            $query_start = (floor((int)$offset / $query_size)) * $query_size;
            //$query_limit = "LIMIT {$query_start},{$query_size}";
            //$query_end = (int)$offset < 150 ? 0 : (int)$offset - 150;

            $query_base = <<<SQL
SELECT DISTINCT e.guid FROM {$CONFIG->dbprefix}objects_property e
{$order_query['join']}
{$custom_join}
{$mj}
{$search_query['join']}
{$permissions_query['join']}
WHERE {$search_query['query']} {$mw} e.subtype = {$subtype}
AND ({$permissions_query['permission']})
{$owner_guid_query}
{$custom_where}
{$order_query['by']}
{$query_limit}
SQL;

            $result_order = get_data("{$query_base}", "ldshake_richlds_order");

            $slice_start = (int)$offset - $query_start;
            $slice_size = $limit;

            if(!$result_order)
                return array();

            if(sizeof($result_order) - 1 < $slice_start + $slice_size)
                $slice_end = sizeof($result_order) - 1;

            //$result_order = array_slice($result_order, $slice_start, $slice_size);

            /*

            $refreshButtonPressed = isset($_SERVER['HTTP_CACHE_CONTROL']) &&
                            $_SERVER['HTTP_CACHE_CONTROL'] === 'max-age=0';
            $wro = $writable_only ? 'wro' : '';
            $query_string = hash("crc32b", "{$type}_{$subtype}_{$user_id}_{$mk}_{$mv}_{$order}_{$wro}_{$search}hkkk");
            $query_start = (int)$offset;

            if(isset($_SESSION['cache']['query_guid'][$query_string])
                && $_SESSION['cache']['query_guid'][$query_string]['start'] <= $query_start
                && $_SESSION['cache']['query_guid'][$query_string]['end'] >= $query_start + (int)$limit) {
                //guids cache hit
                echo 'hit<br>';
                $cached_ids = $_SESSION['cache']['query_guid'][$query_string];
                $slice_start = $query_start - $cached_ids['start'];
                $slice_size = (int)$limit;
                if(sizeof($cached_ids['guids']) < $slice_size)
                    $slice_size = sizeof($cached_ids['guids']) - 1 - $slice_start;

                $result_order = array_slice($cached_ids['guids'], $slice_start, $slice_size);
            } else {
                $query_limit = "LIMIT $offset, 300";

                $query_base = <<<SQL
SELECT DISTINCT e.guid FROM {$CONFIG->dbprefix}entities e
{$mj}
{$search_query['join']}
{$permissions_query['join']}
{$order_query['join']}
WHERE {$search_query['query']} {$mw} e.type = '{$type}' AND e.subtype = $subtype AND e.enabled = 'yes'
AND ({$permissions_query['permission']})
{$order_query['by']} {$query_limit}
SQL;
//echo '<pre>'.$query_base.'</pre>'.'<br>';
            $time = microtime(true);
            $result_order = get_data("{$query_base}", "ldshake_richlds_order");
            echo microtime(true) - $time.' ce<br />';
                $_SESSION['cache']['query_guid'][$query_string] = array(
                  'start' => (int)$offset,
                  'end' => (int)$offset+300,
                  'guids' => $result_order
                );
            }*/

            $result_order_string = implode(',', $result_order);

            if($enrich) {
                $custom_query = buildRichQuery($user_id, $result_order_string, !$writable_only);
            }
            else {
                $build_callback = $custom['build_callback'];
                $custom_query = $build_callback($user_id, $result_order_string);
            }

            $custom_order = isset($custom['order']) ? $custom['order'] : $order_query;
            $custom_where = isset($custom_query['where']) ? 'WHERE '.$custom_query['where'] : '';
            $custom_group_by = isset($custom_query['group_by']) ? 'GROUP BY '.$custom_query['group_by'] : '';

            $query = <<<SQL
SELECT {$custom_query['select']} FROM objects_property e
{$custom_order['join']}
{$custom_query['join']}

{$custom_where}
{$custom_order['by']}
{$custom_group_by}
SQL;
        }

        $query_type = $writable_only ? "writable_only" : "viewable";
        //$pre_query = implode(";\n",$permissions_query['pre']).";\n";
        /*
        if(!$CONFIG->pre_query[$query_type]) {
            foreach($permissions_query['pre'] as $pre_query) {
                $time = microtime(true);
                update_data($pre_query);
                //echo microtime(true) - $time.' p<br />';
            }
            $CONFIG->pre_query[$query_type] = true;
        }

        if(!$CONFIG->pre_query['enrich'] && $enrich) {
            foreach($rich_query['pre'] as $pre_query) {
                $time = microtime(true);
                update_data($pre_query);
                //echo microtime(true) - $time.' p<br />';
                //echo $pre_query.'<br />';
            }
            $CONFIG->pre_query['enrich'] = true;
        }
*/

        if($count) {
            $time = microtime(true);
            $row = get_data_row($query);
            //echo '<pre>'.$query.'</pre>'.'<br>';
            //echo microtime(true) - $time.' c<br />'.'<br>';
            return $row->total;
        } else {
            $time = microtime(true);
            $query .= " /* {$callback} */";
            $entities = get_data($query, $callback);
            //echo '<pre>'.$query.'</pre>'.'<br>';
            //echo microtime(true) - $time.' e<br />';
            return $entities;
        }
    }

    public static function getUsersEditedDesign($lds_id, $count = false) {
        global $CONFIG;

        $query = <<<SQL
SELECT DISTINCT e.guid, e.type FROM {$CONFIG->dbprefix}entities e JOIN {$CONFIG->dbprefix}annotations a ON a.owner_guid = e.guid WHERE a.entity_guid = {$lds_id} AND e.enabled = 'yes' AND e.access_id > 0
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;
    }

    public static function getModificationsByUser($lds_id, $user_id, $count = false) {
        global $CONFIG;

        $annotation_name_id = get_metastring_id('revised_docs');
        $annotation_name_id_2 = get_metastring_id('revised_docs_editor');

        $query = <<<SQL
SELECT DISTINCT e.guid, e.type FROM {$CONFIG->dbprefix}annotations a JOIN {$CONFIG->dbprefix}entities e ON a.entity_guid = e.guid WHERE (a.name_id = {$annotation_name_id} OR a.name_id = {$annotation_name_id_2}) AND a.owner_guid = {$user_id} AND a.entity_guid = {$lds_id} AND e.enabled = 'yes' AND e.access_id > 0
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;
    }

    public static function getModificationsLdSsByDate($user_id = 0, $start = null, $end = null, $count = false) {
        global $CONFIG;

        $annotation_name_id = get_metastring_id('revised_docs');
        $annotation_name_id_2 = get_metastring_id('revised_docs_editor');

        $user_q = '';
        if($user_id)
            $user_q = "AND a.owner_guid = {$user_id}";
        $query = <<<SQL
SELECT e.guid, e.type FROM {$CONFIG->dbprefix}entities e JOIN {$CONFIG->dbprefix}annotations a ON a.entity_guid = e.guid WHERE (a.name_id = {$annotation_name_id} OR a.name_id = {$annotation_name_id_2}) AND a.time_created >= {$start} AND a.time_created < {$end} AND e.enabled = 'yes' AND e.access_id > 0 {$user_q}
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;
    }

    public static function getUserCoedition($user_id = 0, $limit = 10, $offset = 0, $count = false) {
        global $CONFIG;

        $annotation_name_id = get_metastring_id('revised_docs');
        $annotation_name_id_2 = get_metastring_id('revised_docs_editor');
        $subtype_id = get_subtype_id('object', 'LdS');

        if(empty($user_id))
            $user_id = get_loggedin_userid();

        $user_q = '';
        if($user_id)
            $user_q = "AND a.owner_guid = {$user_id} AND e.owner_guid <> {$user_id}";
        $query = <<<SQL
SELECT a.*, e.guid, e.type
FROM {$CONFIG->dbprefix}entities e
JOIN {$CONFIG->dbprefix}annotations a ON a.entity_guid = e.guid
WHERE (a.name_id = {$annotation_name_id} OR a.name_id = {$annotation_name_id_2})
AND e.enabled = 'yes'
AND e.subtype = {$subtype_id}
AND e.access_id > 0
{$user_q}
GROUP BY e.guid
LIMIT {$offset}, {$limit}
SQL;

        $annotations = get_data($query, "row_to_elggannotation");

        if($count) {
            if(empty($annotations))
                return 0;
            else
                return count($annotations);
        }

        return $annotations;
    }


    public static function LdSCanEdit($lds_id, $user) {
        global $CONFIG;

        if($user->admin) {
            return true;
        }

        return self::LdSUserEditRights($lds_id, $user->guid);
    }

    public static function LdSUserEditRights($lds_id, $user_id) {
        global $CONFIG;

        $entity = get_entity($lds_id);
        //$subtype = get_subtype_id('object', 'LdS');
        $subtype = $entity->subtype;

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = $subtype AND e.guid = {$lds_id} AND (
	(e.owner_guid = {$user_id})
	OR
	(
		EXISTS (
			SELECT * FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id} AND rug.guid_two IN (
				SELECT rg.guid_one FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.guid_two = e.guid AND (rg.relationship = 'lds_editor_group')
			)
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship = 'lds_editor' AND ru.guid_one = {$user_id}
		)
	)
)
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($entities)
            return true;

        return false;

    }

    public static function stat_getSharedEditableLdSs($count = false) {
        global $CONFIG;

        $ms_yes = get_metastring_id("yes");
        $ms_no = get_metastring_id("no");
        $ms_all = get_metastring_id("all_can_view");
        $ms_welcome = get_metastring_id("welcome");
        if(!$ms_welcome)
            $ms_welcome = 0;
        $ms_true = get_metastring_id("1");

        $subtype = get_subtype_id('object', 'LdS');

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = $subtype AND e.enabled = 'yes' AND e.access_id > 0 AND (
	((
		EXISTS (
			SELECT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.guid_two = e.guid AND (rg.relationship = 'lds_editor_group')
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE (ru.relationship = 'lds_editor') AND ru.guid_two = e.guid
		)
	))
	AND
	(
		NOT EXISTS (
			SELECT * FROM {$CONFIG->dbprefix}metadata m WHERE m.entity_guid = e.guid AND name_id = {$ms_welcome} AND value_id = {$ms_true}
		)
	)
	AND
	(
        NOT EXISTS (
          SELECT * FROM {$CONFIG->dbprefix}objects_entity o WHERE o.guid = e.guid AND o.title = 'My first LdS'
        )
	)
)
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return
            $entities;

    }

    public static function stat_getImplementedLdSs($count = false) {
        global $CONFIG;

        $m_lds_id = get_metastring_id("lds_id");
        $subtype = get_subtype_id('object', 'LdS');
        $subtype_implementation = get_subtype_id('object', 'LdS_implementation');

        $query = <<<SQL
SELECT DISTINCT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = {$subtype} AND e.enabled = 'yes' AND e.access_id > 0 AND (
	(
		EXISTS (
			SELECT DISTINCT ei.guid FROM {$CONFIG->dbprefix}entities ei JOIN metadata mi ON ei.guid = mi.entity_guid WHERE ei.subtype = {$subtype_implementation} AND ei.enabled = 'yes' AND ei.access_id > 0 AND mi.name_id = {$m_lds_id} AND mi.value_id IN (
		      SELECT DISTINCT msi.id FROM {$CONFIG->dbprefix}metastrings msi WHERE msi.string = e.guid
			)
		)
	)
)
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return
            $entities;

    }

    public static function getPrivateDesigns($user_id = 0, $count = false) {
        global $CONFIG;

        $ms_yes = get_metastring_id("yes");
        $ms_no = get_metastring_id("no");
        $ms_all = get_metastring_id("all_can_view");
        $ms_welcome = get_metastring_id("welcome");
        if(!$ms_welcome)
            $ms_welcome = 0;
        $ms_true = get_metastring_id("1");

        $user_id_query = "";
        if($user_id)
            $user_id_query = "AND e.owner_guid = {$user_id}";

        $subtype = get_subtype_id('object', 'LdS');

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = $subtype AND e.enabled = 'yes' AND e.access_id > 0 {$user_id_query} AND (
	(
		NOT EXISTS (
			SELECT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.guid_two = e.guid AND (rg.relationship = 'lds_editor_group' OR rg.relationship = 'lds_viewer_group')
		)
	)
	AND
	(
		e.guid NOT IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE (ru.relationship = 'lds_editor' OR ru.relationship = 'lds_viewer') AND ru.guid_two = e.guid
		)
	)
	AND
	(
		NOT EXISTS (
			SELECT * FROM {$CONFIG->dbprefix}metadata m WHERE m.entity_guid = e.guid AND name_id = {$ms_all} AND value_id = {$ms_yes}
		)
	)
	AND
	(
		NOT EXISTS (
			SELECT * FROM {$CONFIG->dbprefix}metadata m WHERE m.entity_guid = e.guid AND name_id = {$ms_welcome} AND value_id = {$ms_true}
		)
	)
	AND
	(
        NOT EXISTS (
          SELECT * FROM {$CONFIG->dbprefix}objects_entity o WHERE o.guid = e.guid AND o.title = 'My first LdS'
        )
	)
)
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return
            $entities;

    }

    public static function stat_getReadSharedUsers($lds_id) {
        $members = array();

        $lds = get_entity($lds_id);

        $viewers = array();
        $editors = self::getEditorsUsers($lds_id);

        if(is_array($viewers))
            $members = array_merge($viewers, $members);

        if(is_array($editors))
            $members = array_merge($editors, $members);

        $users = array();

        foreach($members as $member) {
            if($member instanceof ElggUser)
                $users[] = $member;
            elseif ($member instanceof ElggGroup) {
                $users = array_merge($users, $member->getMembers(9999));
            }
        }

        $user_guids = array();
        foreach($users as $user) {
            $user_guids[] = $user->getGUID();
        }

        //$user_guids[] = $lds->owner_guid;
        $unique_user_guids = array_unique($user_guids);

        return $unique_user_guids;
    }

    public static function buildObjectsArray($lds = null)	{
        $objects = array();

        $available = self::getAvailableUsers($lds);
        $objects['available'] = self::entitiesToObjects($available);

        $viewers = self::getViewersUsers($lds->guid);
        $objects['viewers'] = self::entitiesToObjects($viewers);

        $editors = self::getEditorsUsers($lds->guid);
        $objects['editors'] = self::entitiesToObjects($editors);

        return $objects;
    }

    public static function entitiesToObjects($entities)	{
        return $entities;
        $object_list = array();

        if($entities) {
            foreach($entities as $e) {
                $data = new stdClass();
                $data->guid = $e->guid;
                $data->name = $e->name;
                $data->pic = $e->getIcon('small');

                $object_list[] = $data;
            }
        }
        return $object_list;
    }
	
	/**
	 * Returns all the tags of the LdS I see in the browse section
	 * Use if if we filter out the LdS I can edit
	 */
    /*
	public static function getBrowseTagsAndFrequencies ($userId)
	{
		$fields = array ('tags', 'discipline', 'pedagogical_approach');
		
		$lds = self::getBrowseLdsList('', 10000, 0);

		if (!is_array($lds)) return false;
		
		$ids = array (); 
		foreach ($lds as $item)
			$ids[] = $item->guid;
		
		$tags = array();
		$fieldids = array();
		foreach ($fields as $field)
		{
			$fieldids[] = get_metastring_id($field);
			$tags[$field] = array();
		}
		
		if (count($ids))
		{
			$sql = "SELECT k.string AS k, v.string AS v FROM {$CONFIG->dbprefix}metadata m LEFT JOIN {$CONFIG->dbprefix}metastrings k ON m.name_id = k.id LEFT JOIN {$CONFIG->dbprefix}metastrings v ON m.value_id = v.id WHERE entity_guid IN (".implode(',',$ids).") AND m.name_id IN (".implode(',', $fieldids).")";
			$res = execute_query ($sql, get_db_link('read'));
			while ($row = mysql_fetch_object($res)) {
				if (isset ($tags[$row->k][$row->v]))
					$tags[$row->k][$row->v]++;
				else 
					$tags[$row->k][$row->v] = 1;
			}
			
			foreach ($tags as &$tagclass)
				arsort($tagclass);
			
			return $tags;
		}
		else
		{
			return false;
		}
		
	}
	
	public static function getAllTagsAndFrequencies ($userId)
	{
        global $CONFIG;
		$fields = array ('tags', 'discipline', 'pedagogical_approach');
		
		//$lds = get_entities('object','LdS',0,'',100000,0);
        $lds = self::getUserViewableLdSs(get_loggedin_userid());
		
		if (!is_array($lds)) return false;
		
		$ids = array (); 
		foreach ($lds as $item)
			$ids[] = $item->guid;
		
		$tags = array();
		$fieldids = array();
		foreach ($fields as $field)
		{
			$fieldids[] = get_metastring_id($field);
			$tags[$field] = array();
		}
		
		$fieldids = array_filter($fieldids);
		
		if (count($ids) && count($fieldids))
		{
			$sql = "SELECT DISTINCT entity_guid, time_created, k.string AS k, v.string AS v FROM {$CONFIG->dbprefix}metadata m LEFT JOIN {$CONFIG->dbprefix}metastrings k ON m.name_id = k.id LEFT JOIN {$CONFIG->dbprefix}metastrings v ON m.value_id = v.id WHERE entity_guid IN (".implode(',',$ids).") AND m.name_id IN (".implode(',', $fieldids).")";
			$res = execute_query ($sql, get_db_link('read'));
			while ($row = mysql_fetch_object($res)) {
				if (isset ($tags[$row->k][$row->v]))
					$tags[$row->k][$row->v]++;
				else 
					$tags[$row->k][$row->v] = 1;
			}
			
			foreach ($tags as &$tagclass)
				//arsort($tagclass);
				ksort($tagclass);
			
			return $tags;
		}
		else
		{
			return false;
		}
	}
*/
    public static function getAllTagsAndFrequenciesUsage ($userId)
    {
        global $CONFIG;
        $fields = array ('tags', 'discipline', 'pedagogical_approach');

        $tags = lds_contTools::getUserTagFrequency('tags');

        foreach($fields as $f)
            $category[$f] = array();

        foreach($tags as $tag) {
            $tag_category = $tag->category;
            $category[$tag_category][] = $tag;
        }

        foreach ($category as &$c) {
            uasort($c, 'ldshake_tags_cmp');
        }

        return $category;
/*
        //$lds = get_entities('object','LdS',0,'',100000,0);
        $lds = self::getUserViewableLdSs(get_loggedin_userid());

        if (!is_array($lds)) return false;

        $ids = array ();
        foreach ($lds as $item)
            $ids[] = $item->guid;

        $tags = array();
        $fieldids = array();
        foreach ($fields as $field)
        {
            uasort($tagclass, 'ldshake_tags_cmp');
            foreach ($tagclass as &$tag_value)
                $tag_value = $tag_value['count'];

        }

        $fieldids = array_filter($fieldids);

        if (count($ids) && count($fieldids))
        {
            $sql = "SELECT DISTINCT entity_guid, time_created, k.string AS k, v.string AS v FROM {$CONFIG->dbprefix}metadata m LEFT JOIN {$CONFIG->dbprefix}metastrings k ON m.name_id = k.id LEFT JOIN {$CONFIG->dbprefix}metastrings v ON m.value_id = v.id WHERE entity_guid IN (".implode(',',$ids).") AND m.name_id IN (".implode(',', $fieldids).")";
            $res = execute_query ($sql, get_db_link('read'));
            while ($row = mysql_fetch_object($res)) {
                if (!isset ($tags[$row->k][$row->v]))
                    $tags[$row->k][$row->v] = array();

                if (!isset ($tags[$row->k][$row->v]['count']))
                    $tags[$row->k][$row->v]['count'] = 0;

                if (!isset ($tags[$row->k][$row->v]['time_created']))
                    $tags[$row->k][$row->v]['time_created'] = 0;

                $tags[$row->k][$row->v]['count']++;

                if($row->time_created > $tags[$row->k][$row->v]['time_created'])
                    $tags[$row->k][$row->v]['time_created'] = $row->time_created;
            }

            foreach ($tags as &$tagclass) {
                uasort($tagclass, 'ldshake_tags_cmp');
                foreach ($tagclass as &$tag_value)
                    $tag_value = $tag_value['count'];
            }
            return $tags;
        }
        else
        {
            return false;
        }
*/
    }

    public static function build_notification($notification, $guid_list)
    {
        switch($notification->notification_type) {
            case "comment":
                return self::build_comment_notification($notification, $guid_list);
                break;

            case "newlds":
                return self::build_newlds_notification($notification, $guid_list);
                break;

        }
    }

    public static function getVLE()
    {
        if ($vles = get_entities('object', 'vle_data', get_loggedin_userid())) {
            return $vles[0];
        } else {
            $vle = new ElggObject();
            $vle->subtype = 'vle_data';
            $vle->url = '';
            $vle->username = '';
            $vle->password = '';
            $vle->type = '';
            $vle->save();
            return $vle;
        }

        return false;
    }

    public static function getVLECourses($vle)
    {
        ////REST CALLS
        ////
        $courses = array(
            array('id' => '2_34', 'name' => 'Agora VLE, English level A1'),
            array('id' => '2_74', 'name' => 'Agora VLE, English level A2'),
            array('id' => '2_82', 'name' => 'Agora VLE, Alphabetization course'),
        );

        return $courses;
    }

    private function build_comment_notification($notification, $guid_list) {
        $vars['ldsTitle'] = $notification->title;
        $vars['fromName'] = $notification->name;
        $vars['comment'] = $notification->comment_text;
        $vars['ldsUrl'] = $notification->lds_url;
        $vars['senderUrl'] = $notification->sender_url;

        $body = elgg_view('emails/newcomment',$vars);

        $notification_data = array();
        foreach ($guid_list as $guid) {
            $guid_data = array();
            $guid_data['sender_guid'] = $notification->owner_guid;
            $guid_data['recipient_guid'] = $guid;
            $guid_data['title'] = $notification->title;
            $guid_data['body'] = $body;
            $notification_data[] = $guid_data;
        }

        return $notification_data;
    }

    private function build_newlds_notification($notification, $guid_list) {
        $vars['ldsTitle'] = $notification->title;
        $vars['fromName'] = $notification->name;
        $vars['ldsUrl'] = $notification->lds_url;
        $vars['senderUrl'] = $notification->sender_url;

        $body = elgg_view('emails/newlds',$vars);

        $notification_data = array();
        foreach ($guid_list as $guid) {
            $guid_data = array();
            $guid_data['sender_guid'] = $notification->owner_guid;
            $guid_data['recipient_guid'] = $guid;
            $guid_data['title'] = $notification->title;
            $guid_data['body'] = $body;
            $notification_data[] = $guid_data;
        }

        return $notification_data;
    }

    ///PFC maria
    public static function filterWords($list){

        //Lista de palabras vacías de Google
        $stopWord=array("I", "a", "about", "an", "are", "as", "at", "be", "by", "com", "for", "from", "how", "in", "is", "it", 'of', "on", "or", "that", "the", "this", "to", "was", "what", "when", "where", "who", "will", "with", "the", "www");
        //comprobamos si la palabra esta en el array
        for($i=0; $i<=count($list); ++$i){
            if(in_array($list[$i], $stopWord)){
                unset($list[$i]);

            }
        }

        //Eliminamos palabras repetidas y posiciones vacias
        $listResult=array_values(array_unique($list));
        $listResult=array_values(array_filter($listResult));
        return $listResult;

    }

    public static function callOntology(){
        $listOntology=array();
        $numPatterns=16;
        $java_obj=new Java("parser.OwlPaser0513");
        $lista=java("java.util.HashMap");

        $lista=$java_obj->dameKeywords();

        $it=java("java.util.Iterator");
        //  echo $lista=java_values($lista->toArray());
        $cont=0;
        $it=$lista->entrySet()->iterator();
        while($cont<$numPatterns){
            //$it->hasNext()!=0){
            $cont++;
            $e=java("java.util.Map");
            $e=$it->next();
            //  $patron=$e->getKey();
            //$e=java_values($e);
            $patron=java_values($e->getKey());
            $patron=explode('_', $patron);
            $patron=$patron[1];
            $keywords=java_values($e->getValue());
            //echo $patron . "</br>". "</br>";
//echo $keywords . "</br>" . "</br>" . "</br>" . $cont;
            $listOntology[$patron]=$keywords;


        }
        return $listOntology;
    }
    public static function searchPatterns($query) {

        $contWord = array();
        $query = addslashes($query);
        //Llamamos a WordNet para que nos devuelva la lista con las palabras de la query y sus sinónimos e hièrónimos
        $totalList = lds_contTools::searchWordnet($query);
        //Filtramos tal lista para asegurarnos que no contenga ni palabras vacías ni repetidas
        $listQueryWord = lds_contTools::filterWords($totalList);
        //Llamamos a la ontología
        $listTextOntology= lds_contTools::callOntology();

        //$listTextOntology = array("JIGSAW" => "cat dog feline blabla to ", "PYRAMID" => "cat, cat", "TPS" => "dog, familiaris canine familiariss", "SIMULATION"=>"dog, canine");
        $listTextOntology=str_replace(",", " ", $listTextOntology);
        $listTextOntology_aux = $listTextOntology;
        $contWord = array();
        for ($i = 0; $i < count($listTextOntology_aux); ++$i) {
            $cont = 0;
            //Extraemos el primero
            $text = array_shift($listTextOntology);
            //El patrón del que pertenece
            $pattern = array_search($text, $listTextOntology_aux);

            $text = $text . " ";
            for ($j = 0; $j < count($listQueryWord); ++$j) {
                if (stristr($text, $listQueryWord[$j] . " ") != FALSE) {
                    ++$cont;
                }
            }

            if ($cont>0){
                $contWord[$pattern] = $cont;
            }
        }
        //ordenamos el array de mayor a menor
        arsort($contWord);

        return $contWord;
    }


    public static function searchSyns($text) {

        //Convertimos en array el bloque de texto, utilizando como delimitador los carácteres de espacio
        $list = preg_split('/[\s]+/', $text);
        //Creamos el array a rellenar con los sinonimos e hiperonimos
        $syns = array();
        //Ponemos el contador que nos determinará en el sentido que estamos a uno, el sentido 1.
        $cont = 1;

        //Recorremos el array resultado de dividir el bloque de texto
        for ($j = 0; $j <= count($list); ++$j) {

            //Si encontramos un sentido de la plabra y es de los tres primeros
            if (strcmp("Sense", $list[$j]) == 0 && $cont <= 3) {
                //Puntero auxiliar que se podiciona en la primera palabra del sentido correspondiente de la palabra
                $k = $j + 1;
                //Recorremos el resto de la lista
                while ($k <= (count($list))) {

                    //Eliminamos los antónimos
                    if (strcmp('(vs.', $list[$k]) == 0) {
                        $k = 2 + $k;
                    }
                    //Si sigue perteneciendo al mismo sentido lo guardamos
                    if (strcmp("Sense", $list[$k]) != 0) {
                        //Guardamos el string en nuestro array
                        array_push($syns, $list[$k]);
                        //Se aumenta el puntero
                        ++$k;
                    }
                    //Si cambiamos de sentido salimos del bucle while para buscar el siguiente sentido
                    else {
                        break 1;
                    }
                }
                //Aumentamos el contador
                $cont = $cont + 1;
            }
        }

        //Eliminamos numero, comas y demás texto no deseado
        $syns = preg_replace('/[0-9,;#=>()-]/', "", $syns);
        $syns = str_replace('prenominal', "", $syns);
        $syns = str_replace('predicate', "", $syns);

        //Eliminamos las palabras que empiecen por mayúsculas.
        for ($i = 0; $i <= count($syns); ++$i) {
            //Comprobamos si la palabra tiene todos sus carácteres en minúscula
            if (!(ctype_lower($syns[$i]))) {
                //Si tienen carácteres en mayúscula eliminamos la palabra del array
                unset($syns[$i]);
            }
        }

        //Eliminamos los terminos vacios
        $syns = array_filter($syns);
        //Reposicionamos el array
        $syns = array_values($syns);

        return $syns;
    }

    public static function searchWordnet($query) {

        //Pasamos a minúsculas todo el string de la query, para evitar problemas
        $query = strtolower($query);
        $listWord = array(); //lista a devolver con todas las palabras de la query y sus sinónimos e hiperónimos
        $dirWordNet = "/usr/local/WordNet-3.0/bin/wn ";
        $queryWord = explode(" ", $query);
        for ($i = 0; $i <= count($queryWord); ++$i) {
            $synonims[$i] = array();
            //Sinonimos de objeto
            $resultN[$i] = shell_exec($dirWordNet . $queryWord[$i] . " -synsn");
            $synsN[$i] = lds_contTools::searchSyns($resultN[$i]);
            //Sinonimo de verbo
            $resultV[$i] = shell_exec($dirWordNet . $queryWord[$i] . " -synsv");
            $synsV[$i] = lds_contTools::searchSyns($resultV[$i]);
            //Sinonimo del adverbio
            $resultR[$i] = shell_exec($dirWordNet . $queryWord[$i] . " -synsr");
            $synsR[$i] = lds_contTools::searchSyns($resultR[$i]);
            //Sinonimo del adjetivo
            $resultA[$i] = shell_exec($dirWordNet . $queryWord[$i] . " -synsa");
            $synsA[$i] = lds_contTools::searchSyns($resultA[$i]);

            //Unimos todos los sinónimos resultantes en un mismo array
            $synonims[$i] = array_merge($synsN[$i], $synsV[$i], $synsR[$i], $synsA[$i]);


            //Unir todos los sinónimos y las palabras query
            $listWord[] = $queryWord[$i];
            $listWord = array_merge($listWord, $synonims[$i]);
        }

        return $listWord;
    }

    public static function encrypt_password($plaintext) {
        global $CONFIG;
        $key = pack('H*', $CONFIG->vle_key);
        $iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_128, MCRYPT_MODE_CBC);
        $iv = mcrypt_create_iv($iv_size, MCRYPT_RAND);

        $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key,
            $plaintext, MCRYPT_MODE_CBC, $iv);

        $ciphertext = $iv . $ciphertext;

        return base64_encode($ciphertext);
    }

    public static function decrypt_password($ciphertext) {
        global $CONFIG;
        $key = pack('H*', $CONFIG->vle_key);
        $ciphertext_dec = base64_decode($ciphertext);

        $iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_128, MCRYPT_MODE_CBC);
        $iv_dec = substr($ciphertext_dec, 0, $iv_size);

        $ciphertext_dec = substr($ciphertext_dec, $iv_size);

        return rtrim(mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key,
            $ciphertext_dec, MCRYPT_MODE_CBC, $iv_dec), "\0");
    }

    public static function tool_lang($tool, $lang) {
        $ilde_tools_lang = array(
            'webcollagerest' => array('ca','en','es'),
            'gluepsrest' => array('ca','en','es')
        );

        if(!$ilde_tools_lang[$tool])
            return 'en';

        if(in_array($lang, $ilde_tools_lang[$tool]))
            return $lang;
        else
            return 'en';
    }
}

function ldshake_tags_cmp($a, $b) {
   $a_score = $a->frequency - (time() - $a->time_created)*5.79E-7;
   $b_score = $b->frequency - (time() - $b->time_created)*5.79E-7;

   return ($b_score - $a_score);
}