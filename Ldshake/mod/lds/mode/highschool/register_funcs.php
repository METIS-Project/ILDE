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

global $CONFIG;

$CONFIG->schools = array(
"ies0001" => "INS La Ribera",
"ies0002" => "IES Els Arcs",
"ies0003" => "IES Claret",
"ies0004" => "IES Verdaguer",
"ies0005" => "IES Jesus i Maria",
"ies0006" => "Thau Sant Cugat",
"ies0007" => "Escola Projecte",
"ies0008" => "IES Marina",
"ies0009" => "International School of Barcelona (ISB)",
);

function ldshake_mode_open_register_validation() {
    global $CONFIG;

    $highschool_value = get_input('sdfsdfgsduh544dsgdsgsse78gh5g',null);

    if(isset($CONFIG->schools[$highschool_value]))
        return true;
    else {
        throw new Exception(T('Has de seleccionar una escola.'));
        return false;
    }
}

function ldshake_mode_open_register(&$user) {
    $highschool_value = get_input('sdfsdfgsduh544dsgdsgsse78gh5g',null);
    ldshake_highschool_register($user, $highschool_value, 'student');
    $user->isExpert = 1;
}

function ldshake_mode_csv_register(&$user, $params) {
    global $CONFIG;
    if(!isset($params[0]) or !isset($params[1]) or !isset($CONFIG->schools[$params[0]]))
        return false;
    $user->school   = $params[0];
    $user->role     = $params[1];
    $user->isExpert = 1;
}

function ldshake_highschool_register(&$user, $highschool_value, $role) {
    global $CONFIG;
    if(isset($CONFIG->schools[$highschool_value]))
        $user->school = $highschool_value;
    else
        throw new Exception("Invalid school code");

    $user->role = $role;
    $user->save();
}

function ldshake_mode_build_permissions($user_id, $writable_only, $isglobalenv, $query, $context = null) {
    $role_msid = get_metastring_id('role');
    $student_msid = get_metastring_id('student');
    $teacher_msid = get_metastring_id('teacher');
    $tags_msid = get_metastring_id('tags');
    $wording_msid = get_metastring_id('wording');
    $answer_template_msid = get_metastring_id('answer_template');

    if(!($role_msid
    or $student_msid
    or $teacher_msid
    or $tags_msid
    or $wording_msid
    or $answer_template_msid
    ))
        return $query;

    $user = get_user($user_id);
    if($user->role == 'teacher' and !$writable_only) {
        $myfirstlds = sanitise_string(T("La meva primera proposta"));
        $query['join'] .= <<<SQL

LEFT JOIN objects_entity oe_title ON e.guid = oe_title.guid
LEFT JOIN (
SELECT m_answer.entity_guid AS answer
FROM metadata m_answer
WHERE m_answer.name_id = {$role_msid} AND m_answer.value_id = {$student_msid}
) AS t_answer
ON t_answer.answer = e.owner_guid
SQL;

        $query['permission'] .= <<<SQL
OR (
   t_answer.answer IS NOT NULL AND oe_title.title <> '{$myfirstlds}'/*student owner*/
)
SQL;
    }

    if($user->role == 'student' and !$writable_only) {

    }

    return $query;
}

function ldshake_mode_build_permissions_acv($user_id, $writable_only, $isglobalenv, $query, $context = null) {
    $role_msid = get_metastring_id('role');
    $student_msid = get_metastring_id('student');
    $teacher_msid = get_metastring_id('teacher');
    $tags_msid = get_metastring_id('tags');
    $wording_msid = get_metastring_id('wording');
    $answer_template_msid = get_metastring_id('answer_template');

    if(!($role_msid
        or $student_msid
        or $teacher_msid
        or $tags_msid
        or $wording_msid
        or $answer_template_msid
    ))
        return $query;

    $user = get_user($user_id);

    if(($user->role == 'student' or $user->role == 'teacher') and !$writable_only and $context == 'viewlist') {
        $guids = ldshake_mode_wording_guids();
        if(!empty($guids)) {
            $guids = implode(',', $guids);
            $query = <<<SQL
(
    e.all_can_view = 1
    AND
    e.guid NOT IN({$guids})
) OR
SQL;
        }

    }

    return $query;
}

function ldshake_mode_wording_guids() {
    global $CONFIG;

    $tags_msid = get_metastring_id('tags');
    $wording_msid = get_metastring_id('wording');
    $answer_template_msid = get_metastring_id('answer_template');

    $query = "SELECT m.entity_guid AS guid FROM {$CONFIG->dbprefix}metadata m ";
    $query .= " WHERE m.name_id = {$tags_msid} AND ( m.value_id = {$wording_msid} OR m.value_id = {$answer_template_msid} )";

    $guids = array();
    $guids = get_data($query, "ldshake_guid_query");
    if(!$guids)
        $guids = array();
    return $guids;
}

function ldshake_mode_ldsnew($params) {
    global $CONFIG;
    $params['data']['all_can_read'] = 'false';

    if(isset($params['params'][1]) and isset($params['params'][2])) {
        if($params['params'][1] == 'wording' and is_numeric($params['params'][2])) {
            $template_lds = get_entity($params['params'][2]);
            $template_tags = explode(',', $template_lds->tags);

            if(in_array('answer_template', $template_tags)) {
                if($template_documents = get_entities_from_metadata('lds_guid', $template_lds->guid,'object','LdS_document', 0, 100)) {
                    $initDocuments = json_decode($params['data']['initDocuments']);
                    $initDocuments[0]->body = $template_documents[0]->description;
                    $params['data']['initDocuments'] = json_encode($initDocuments);
                    $params['data']['editor_type'] = 'doc,'.$template_lds->guid;
                    $params['data']['editor_subtype'] = $template_lds->guid;
                    $initLdS = json_decode($params['data']['initLdS']);
                    $user = get_loggedin_user();
                    if(isset($CONFIG->schools[$user->school]))
                        $initLdS->tags = $CONFIG->schools[$user->school];

                    $params['data']['initLdS'] = json_encode($initLdS);
                }
            }
        }
    }

    return $params['data'];
}

function ldshake_mode_allow_read_all_sharing() {
    $user = get_loggedin_user();

    if($user->role == 'student')
        return false;
    else
        return true;
}

function ldshake_mode_view_minimal($params) {
    $user = get_loggedin_user();
    $lds = $params['lds'];
    $tags = explode(',', $lds->tags);
    if(in_array("wording", $tags) and ($user->role == "student" or $user->role == "teacher"))
        return true;

    return false;
}

function ldshake_mode_save_lds(&$lds) {
    if(is_numeric($lds->editor_subtype)) {
        if($template_lds = get_entity($lds->editor_subtype)) {
            $template_tags = explode(',', $template_lds->tags);

            if(in_array('answer_template', $template_tags)) {
                $lds->parent = $lds->editor_subtype;
            }
        }
    }
}

function ldshake_mode_mylds(&$disable_projects, &$disable_implementations) {
    $disable_projects = true;
    $disable_implementations = true;
}

function ldshake_mode_browselds(&$disable_search_patterns, &$disable_external_repository, &$tools_term) {
    $disable_search_patterns = true;
    $disable_external_repository = true;
    $tools_term = "Problemes";
}

function ldshake_mode_extensions() {
    if(isloggedin()) {
        $user = get_loggedin_user();
        if(isset($user->role)) {
            extend_elgg_settings_page('lds/mode/highschool/settings/school', 'usersettings/user', 1);
            register_plugin_hook('usersettings:save','user','ldshake_mode_school_settings_save');
        }
    }
}

function ldshake_mode_school_settings_save() {
    global $CONFIG;

    $user = get_loggedin_user();
    $school_id = get_input('school', null);

    if(!$school_id)
        return false;

    if(isset($CONFIG->schools[$school_id])) {
        $user->school = $school_id;
    }
    $user->save();
}

function ldshake_mode_open_register_welcome(&$title, &$description) {
    global $CONFIG;
    $title = "La meva primera proposta";
    $description = elgg_view('lds/mode/'.$CONFIG->ldshake_mode.'/welcome_lds/welcome_lds_'.$CONFIG->language);
}