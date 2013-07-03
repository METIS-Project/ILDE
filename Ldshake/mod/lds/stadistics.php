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

function lds_echocsv($header = null, $data) {

    //header("Content-Disposition: attachment; filename=\"file.csv\"");
    //header("Content-Type: application/vnd.ms-excel; charset=UTF-8");

    $out = fopen("php://output", 'w');

    fwrite($out,"\xEF\xBB\xBF");

    fputcsv($out, $header, ';', '"');

    foreach($data as $d)
        fputcsv($out, $d, ';', '"');
    fclose($out);
}

function lds_echo_usernames($users) {

    if(!$users)
        return "";

    $data = array();
    foreach($users as $u) {
        $entity = get_user($u);
        $data[] = $entity->username;
    }
    return implode(',', $data);

}

function lds_csv_private () {
    /*
    $ldss = lds_contTools::getPrivateDesigns();

    $header = array("id", "title", "private");

    $data = array();

    foreach($ldss as $lds) {
        $data[] = array($lds->guid, $lds->title, "yes");
    }

    lds_echocsv($header, $data);
    */

    /*
    $header = array("id", "title", "number of users", "users");

    $data = array();

    $ldss = get_entities('object','LdS', 0, "", 9999);
    if($ldss)
        foreach($ldss as $lds) {
            $a_users = lds_contTools::getViewAccessUsers($lds->guid);

            $data[] = array($lds->guid, $lds->title, count($a_users));
        }
    //getViewAccessUsers($lds_id);
    //getEditorsUsers($lds_id);

    lds_echocsv($header, $data);
    */

/*
    $header = array("id", "title", "number of users", "users");

    $data = array();

    $ldss = get_entities('object','LdS', 0, "", 9999);
    if($ldss)
        foreach($ldss as $lds) {
            $e_users = lds_contTools::stat_getReadSharedUsers($lds->guid);

            $author = get_entity($lds->owner_guid);
            $data[] = array($author->guid, $author->username, lds_echo_usernames($e_users), $lds->guid, $lds->title, count($e_users));
        }
    //getViewAccessUsers($lds_id);
    //getEditorsUsers($lds_id);

    lds_echocsv($header, $data);
*/

    /*
    $ldss = lds_contTools::stat_getSharedEditableLdSs();

    $header = array("id", "title", "editable by others");

    $data = array();

    foreach($ldss as $lds) {
        $data[] = array($lds->guid, $lds->title, "yes");
    }

    lds_echocsv($header, $data);
    */

    /*
    $ldss = lds_contTools::stat_getImplementedLdSs();

    $header = array("id", "title", "associated to a VLE");

    $data = array();

    foreach($ldss as $lds) {
        $data[] = array($lds->guid, $lds->title, "yes");
    }

    lds_echocsv($header, $data);

    */

    /*
    $data = array();

    $users = get_entities('user','', 0, "", 9999);
    foreach($users as $u) {
        //$annotations = get_annotations(0, 'object', 'LdS', 'viewed_lds', '', $u->guid, 9999);
        $entities = get_entities_from_annotations('object', 'LdS', 'viewed_lds', '', $u->guid, 0, 9999);

        if($entities)
            foreach($entities as $e) {
                $annotations = get_annotations($e->guid, 'object', 'LdS', 'viewed_lds', '', $u->guid, 9999);

                $data[] = array($u->guid, $u->username, $e->guid, $e->title, count($annotations));
            }
    }

    $header = array("user_id", "username", "design id", "design title", "times viewed");

    lds_echocsv($header, $data);
    */

    /*
    $data = array();

    $ldss = get_entities_from_annotations('object', 'LdS', 'viewed_lds', '', 0, 0, 9999);
    foreach($ldss as $lds) {
        $users = get_entities('user','', 0, "", 9999);
        //$annotations = get_annotations(0, 'object', 'LdS', 'viewed_lds', '', $u->guid, 9999);
        //$entities = get_entities_from_annotations('object', 'LdS', 'viewed_lds', '', $u->guid, 0, 9999);

        foreach($users as $u) {
            $annotations = get_annotations($lds->guid, 'object', 'LdS', 'viewed_lds', '', $u->guid, 9999);

            if($annotations)
                $data[] = array($lds->guid, $lds->title, $u->guid, $u->username, count($annotations));
        }
    }

    $header = array("design id", "design title", "user_id", "username", "times viewed");

    lds_echocsv($header, $data);
    */


    /*
    $header = array("user id", "username","lds id", "lds title", "number of comments");

    $data = array();

    $ldss = get_entities('object','LdS', 0, "", 9999);
    if($ldss)
        foreach($ldss as $lds) {
            $users = get_entities('user','',0,'',9999);

            //count comments
            foreach($users as $user) {
                $comments = get_annotations($lds->guid, '','','generic_comment','',$user->guid, 9999);
                if(!$comments)
                    $comments = array();

                $data[] = array($user->guid, $user->username, $lds->guid, $lds->title, count($comments));
            }
        }
    //getViewAccessUsers($lds_id);
    //getEditorsUsers($lds_id);

    lds_echocsv($header, $data);
    */

    /*
    $header = array("user id", "username","lds id", "lds title", "number of comments");

    $data = array();

    $users = get_entities('user','',0,'',9999);

    //count comments
    foreach($users as $user) {
        for($i=0; $i<52; $i++) {
            $end = $i+1;
            $revisions = lds_contTools::getModifiedLdSsByDate($user->guid, strtotime("2013W01+{$i}week"), strtotime("2013W01+{$end}week"));

            if(!$revisions)
                $revisions = array();

            $data[] = array($user->guid, $user->username, count($revisions));
        }
    }

    lds_echocsv($header, $data);
    */

/*
    $header = array("user id", "username", "Design Pattern (number)", "Design Pattern", "Design Narrative (number)", "Design Narrative", "Persona Card (number)", "Persona Card", "Factors and Concerns (number)", "Factors and Concerns","CompendiumLD (number)","CompendiumLD","Image (number)","Image","WebCollage (number)","WebCollage","OpenGLM (number)","OpenGLM", "CADMOS (number)", "CADMOS");

    $data = array();

    $users = get_entities('user','',0,'',9999);

    //count comments
    foreach($users as $user) {
        $editor_subtypes = array(
            'design_pattern',
            'MDN',
            'PC',
            'FC',
        );

        $editor_types = array(
            'cld',
            'image',
            'webcollagerest',
            'openglm',
            'cadmos',
        );

        $row = array();

        $row[] = $user->guid;
        $row[] = $user->username;

        foreach($editor_subtypes as $e_s) {
            $ldss = get_entities_from_metadata('editor_subtype',$e_s, 'object', 'LdS', $user->guid, 9999);
            if(!$ldss)
                $ldss = array();

            $lds_list = array();

            foreach($ldss as $lds ) {
                $lds_list[] = $lds->guid;
            }
            $lds_list = implode(',', $lds_list);


            $row[] = count($ldss);
            $row[] = $lds_list;
        }

        foreach($editor_types as $e_t) {
            $ldss = get_entities_from_metadata('editor_type',$e_t, 'object', 'LdS', $user->guid, 9999);
            if(!$ldss)
                $ldss = array();

            $lds_list = array();

            foreach($ldss as $lds ) {
                $lds_list[] = (int)$lds->guid;
            }
            $lds_list = implode(',', $lds_list);


            $row[] = count($ldss);
            $row[] = $lds_list;
        }

        $data[] = $row;

    }

    lds_echocsv($header, $data);

*/

/*
    $header = array("lds id", "lds title", "number of reviews", "user list");

    $data = array();

    $ldss = get_entities('object','LdS', 0, "", 9999);
    if($ldss)
        foreach($ldss as $lds) {
            $users = get_entities('user','',0,'',9999);

            //count comments
            $reviews = get_annotations($lds->guid, '','','revised_docs','',0, 9999);
            if(!$reviews)
                $reviews = array();
            $reviews_editor = get_annotations($lds->guid, '','','revised_docs_editor','',0, 9999);
            if(!$reviews_editor)
                $reviews_editor = array();

            $reviews = $reviews + $reviews_editor;

            $user_reviews = array();

            foreach($reviews as $review) {
                $user_reviews["{$review->owner_guid}"] = isset($user_reviews["{$review->owner_guid}"]) ? $user_reviews["{$review->owner_guid}"]+1 : 1;
            }

            $reviews_list = "";

            foreach($user_reviews as $u_r_k => $u_r_v) {
                $user = get_entity($u_r_k);
                $reviews_list .= "{$user->guid} ({$user->username}) $u_r_v, ";
            }

             $data[] = array($lds->guid, $lds->title, count($reviews), $reviews_list);
        }
    //getViewAccessUsers($lds_id);
    //getEditorsUsers($lds_id);

    lds_echocsv($header, $data);
*/

    //date("d-m-Y", strtotime("2011W01+51week"));
}