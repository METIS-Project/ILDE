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

function ldshake_mode_open_register(&$user) {
    $highschool_value = get_input('sdfsdfgsduh544dsgdsgsse78gh5g',null);
    ldshake_highschool_register($user, $highschool_value, 'student');
}

function ldshake_mode_csv_register(&$user, $params) {
    if(!isset($params[0]) or !isset($params[1]))
        return false;
    $user->school   = $params[0];
    $user->role     = $params[1];
}

function ldshake_highschool_register(&$user, $highschool_value, $role) {
    $highschool_value = get_input('sdfsdfgsduh544dsgdsgsse78gh5g',null);
    if(isset($highschool_institutions[$highschool_value]))
        $user->school = $highschool_value;
    else
        throw new Exception("Invalid institution code");

    $user->role = $role;
}

function ldshake_mode_build_permissions($user_id, $writable_only, $isglobalenv, $query) {
    $role_msid = get_metastring_id('role');
    $student_msid = get_metastring_id('student');
    $teacher_msid = get_metastring_id('teacher');

    $user = get_user($user_id);
    if($user->role == 'teacher' and !$writable_only) {
        $query['join'] .= <<<SQL

LEFT JOIN metadata m_role ON m_role.entity_guid = e.owner_guid
SQL;

        $query['permission'] .= <<<SQL
OR (
  m_role.name_id = {$role_msid} AND m_role.value_id = {$student_msid}
)
SQL;
    }

    return $query;
}

function ldshake_mode_ldsnew($params) {
    $params['data']['all_can_read'] = 'false';

    if(isset($params['params'][1]) and isset($params['params'][2])) {
        if($params['params'][1] == 'wording' and is_numeric($params['params'][2])) {
            $template_lds = get_entity($params['params'][2]);
            $template_tags = explode(',', $template_lds->tags);

            if(in_array('wording', $template_tags)) {
                if($template_documents = get_entities_from_metadata('lds_guid', $template_lds->guid,'object','LdS_document', 0, 100)) {
                    $initDocuments = json_decode($params['data']['initDocuments']);
                    $initDocuments[0]->body = $template_documents[0]->description;
                    $params['data']['initDocuments'] = json_encode($initDocuments);
                }
            }
        }
    }

    return $params['data'];
}

function ldshake_mode_allow_read_all_sharing() {
    return false;
}

function ldshake_mode_view_minimal($params) {
    $lds = $params['lds'];
    $tags = explode(',', $lds->tags);
    if(in_array("wording", $tags))
        return true;

    return false;
}