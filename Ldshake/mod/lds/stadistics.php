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

function lds_echocsv($header = null, $data, $title = "") {

    $title .= "-";
    $time = date('Y-m-d_H:i:s',time());
    header('Content-Description: File Transfer');
    header("Content-Disposition: attachment; filename=\"lds-{$title}tracking({$time}).csv\"");
    header("Content-Type: application/vnd.ms-excel; charset=UTF-8");
    header('Expires: 0');
    header('Cache-Control: must-revalidate, post-check=0, pre-check=0');

    $out = fopen("php://output", 'w');

    fwrite($out,"\xEF\xBB\xBF");

    //$header_t= array();
    //foreach($header as $h)
    //    $header_t[] = '="'.$h.'"';

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

function lds_tracking_viewed() {
    $header = array("username");

    $data = array();

    $ldss = get_entities('object','LdS', 0, "", 9999);
    $users = get_entities('user','',0,'',9999);
    if($ldss)
        foreach($users as $user) {
            $user_data = array();
            $user_data[] = $user->username;
            $header_comp = array();
            foreach($ldss as $lds) {
                $header_comp[] = $lds->guid."({$lds->title})";

                //count comments
                $comments = get_annotations($lds->guid, '','','viewed_LdS','',$user->guid, 9999);
                if(!$comments)
                    $comments = array();

                $user_data[] = count($comments);
            }
            $data[] = $user_data;
        }
    $header = array_merge($header, $header_comp);
    //getViewAccessUsers($lds_id);
    //getEditorsUsers($lds_id);

    lds_echocsv($header, $data,'viewed');
}

function lds_tracking_comments() {
    $header = array("username");


    $data = array();

    $ldss = get_entities('object','LdS', 0, "", 9999);
    $users = get_entities('user','',0,'',9999);
    if($ldss)
        foreach($users as $user) {
            $user_data = array();
            $user_data[] = $user->username;
            $header_comp = array();
            foreach($ldss as $lds) {
                $header_comp[] = $lds->guid."({$lds->title})";

            //count comments
                $comments = get_annotations($lds->guid, '','','generic_comment','',$user->guid, 9999);
                if(!$comments)
                    $comments = array();

                $user_data[] = count($comments);
            }
            $data[] = $user_data;
        }
    $header = array_merge($header, $header_comp);
    //getViewAccessUsers($lds_id);
    //getEditorsUsers($lds_id);

    lds_echocsv($header, $data,'comments');
}

function lds_tracking_implemented() {

    $ldss = lds_contTools::stat_getImplementedLdSs();

    $header = array("id", "title", "associated to a VLE", "number of implementations");

    $data = array();

    foreach($ldss as $lds) {
        $implementations = get_entities_from_metadata('lds_id', $lds->guid, 'object','LdS_implementation',0,9999,0,'',0,true);
        $data[] = array($lds->guid, $lds->title, "yes", $implementations);
    }

    lds_echocsv($header, $data, 'implemented');
}

function lds_tracking_implementations() {

    $ldss = get_entities('object','LdS_implementation',0,"",9999);

    $header = array("id", "title", "design id", "number of deployments", "creator id", "vle name", "course name");

    $data = array();

    foreach($ldss as $lds) {
        $row = array();
        $row[] = $lds->guid;
        $row[] = $lds->title;
        $row[] = $lds->lds_id;
        $nDeployments = $lds->countAnnotations('deployed_implementation');
        $row[] = $nDeployments;
        $row[] = $lds->owner_guid;
        $row[] = $lds->vle_name;
        $row[] = $lds->course_name;
        $data[] = $row;
    }

    lds_echocsv($header, $data, 'implementations');
}

function lds_tracking_deployments() {
    $header = array("deployment id", "implementation id", "user id", 'deployment date', "vle name", "course name");
    $data = array();

    if($deployments = get_annotations(0, 'object', 'LdS_implementation', 'deployed_implementation', "", 0, 9999)) {
        foreach($deployments as $d) {
            $row = array();
            $imp = get_entity($d->entity_guid);
            $row[] = $d->id;
            $row[] = $d->entity_guid;
            $row[] = $d->owner_guid;
            $row[] = date('d-m-Y_H:i:s', $d->time_created);
            $row[] = $imp->vle_name;
            $row[] = $imp->course_name;
            $data[] = $row;
        }
    }
    lds_echocsv($header, $data, 'deployments');
}

function lds_tracking_created_by_user() {

//$header = array("id", "title", "number of users", "users");

    $header = array('username', 'number of designs created', 'design list');
    $data = array();


    $users = get_entities('user','', 0, "", 9999);
    foreach($users as $user) {
        $ldss = get_entities('object','LdS', $user->guid, "", 9999);
        if(!$ldss)
            $ldss=array();

        $a_lds_list = array();
        foreach($ldss as $lds) {
            $a_lds_list[] = $lds->guid;
        }
        $a_lds_string = implode(',',$a_lds_list);

        $data[] = array($user->username, count($ldss), $a_lds_string);
    }
    lds_echocsv($header, $data, 'created_by_user');
}

function lds_tracking_sharing() {

//$header = array("id", "title", "number of users", "users");

$header = array('username');
$data = array();

$ldss = get_entities('object','LdS', 0, "", 9999);
$users = get_entities('user','', 0, "", 9999);
    foreach($users as $user) {
        $data_array = array();
        $lds_header = array();
        if($ldss)
            foreach($ldss as $lds) {
                $lds_header[] = $lds->guid . ' (' . $lds->title . ') shared number';
                $lds_header[] = $lds->guid . ' (' . $lds->title . ') shared list';

                $lds_header[] = $lds->guid . ' (' . $lds->title . ') edit number';
                $lds_header[] = $lds->guid . ' (' . $lds->title . ') edit list';

                if($lds->owner_guid == $user->guid){
                    $a_users = lds_contTools::getViewAccessUsers($lds->guid);
                    $e_users = lds_contTools::stat_getReadSharedUsers($lds->guid);

                    $a_user_list = array();
                    foreach($a_users as $a_u) {
                        if($user->guid != $a_u) {
                            $a_u = get_entity($a_u);
                            $a_user_list["{$a_u->username}"] = isset($a_user_list["{$a_u->username}"]) ? $a_user_list["{$a_u->username}"]+1 : 1;
                        }
                    }
                    $a_user_string = implode(',',array_keys($a_user_list));


                    $e_user_list = array();
                    foreach($e_users as $e_u) {
                        $e_u = get_entity($e_u);
                        $e_user_list["{$e_u->username}"] = isset($e_user_list["{$e_u->username}"]) ? $e_user_list["{$e_u->username}"]+1 : 1;
                    }
                    $e_user_string = implode(',',array_keys($e_user_list));
                }
                else{
                    $a_users = array();
                    $e_users = array();
                    $a_user_string = "";
                    $e_user_string = "";

                }

                $a_count = count($a_users);
                if($a_count)
                    $a_count--;

                $data_array = array_merge($data_array, array($a_count , $a_user_string, count($e_users), $e_user_string));
            }
        $data[] = array_merge(array($user->username), $data_array);
    }
//getViewAccessUsers($lds_id);
//getEditorsUsers($lds_id);

    $lds_header = array_merge($header, $lds_header);

    lds_echocsv($lds_header, $data, 'shared');


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

}

function lds_tracking_private() {

    $ldss = lds_contTools::getPrivateDesigns();

    $header = array("id", "title", "private");

    $data = array();

    foreach($ldss as $lds) {
        $data[] = array($lds->guid, $lds->title, "yes");
    }

    lds_echocsv($header, $data, 'private');

}

function lds_tracking_edit_list() {

    $header = array("lds id", "lds title", "number of reviews", "number of users editing", "user list(edits)");

    $data = array();

    $ldss = get_entities('object','LdS', 0, "", 9999);
    if($ldss)
        foreach($ldss as $lds) {
            $users = get_entities('user','',0,'',9999);

            //count edits
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

            $reviews_list = array();;

            foreach($user_reviews as $u_r_k => $u_r_v) {
                $user = get_entity($u_r_k);
                //$reviews_list .= "{$user->guid} ({$user->username}) $u_r_v, ";
                $reviews_list[] = "{$user->username}({$u_r_v})";
            }

            $reviews_list = implode(',', $reviews_list);
             $data[] = array($lds->guid, $lds->title, count($reviews), count($user_reviews), $reviews_list);
        }
    //getViewAccessUsers($lds_id);
    //getEditorsUsers($lds_id);

    lds_echocsv($header, $data, 'edits');
}

function lds_tracking_user_reviews() {
    $header = array("user id", "username", " total number of reviews");

    for($i=1; $i<=52; $i++) {
        $header[] = "2013W{$i} (count)";
        $header[] = "2014W{$i}";
    }

    $data = array();

    $users = get_entities('user','',0,'',9999);

    //count reviews
    foreach($users as $user) {
        $user_reviews = array();
        $user_total_count = 0;
        for($i=0; $i<52; $i++) {
            $end = $i+1;
            $reviews = lds_contTools::getModificationsLdSsByDate($user->guid, strtotime("2014W01+{$i}week"), strtotime("2014W01+{$end}week"));

            if(!$reviews)
                $reviews = array();

            $user_reviews[] = count($reviews);
            $user_total_count += count($reviews);

            $user_week_list = array();
            foreach($reviews as $review) {
                $user_week_list["{$review->guid}"] = isset($user_week_list["{$review->guid}"]) ? $user_week_list["{$review->guid}"]+1 : 1;
            }

            $user_reviews[] = implode(',',array_keys($user_week_list));
        }
        $data[] = array_merge(array($user->guid, $user->username, $user_total_count), $user_reviews);
    }

    lds_echocsv($header, $data, 'reviews-weeks');

}

function lds_tracking_user_reviews_days() {
    $header = array("user id", "username", " total number of reviews");

    for($i=0; $i<365; $i++) {
        $header[] = date("Y-m-d", strtotime("2014.001+{$i}days"))." (count)";
        $header[] = '="'.date("Y-m-d", strtotime("2014.001+{$i}days")).'"';
    }

    $data = array();

    $users = get_entities('user','',0,'',9999);

    //count reviews
    foreach($users as $user) {
        $user_reviews = array();
        $user_total_count = 0;
        for($i=0; $i<365; $i++) {
            $end = $i+1;
            $reviews = lds_contTools::getModificationsLdSsByDate($user->guid, strtotime("2014.001+{$i}days"), strtotime("2014.001+{$end}days"));

            if(!$reviews)
                $reviews = array();

            $user_reviews[] = count($reviews);
            $user_total_count += count($reviews);

            $user_week_list = array();
            foreach($reviews as $review) {
                $user_week_list["{$review->guid}"] = isset($user_week_list["{$review->guid}"]) ? $user_week_list["{$review->guid}"]+1 : 1;
            }

            $user_reviews[] = implode(',',array_keys($user_week_list));
        }
        $data[] = array_merge(array($user->guid, $user->username, $user_total_count), $user_reviews);
    }

    lds_echocsv($header, $data, 'reviews-days');

}

function lds_tracking_user_reviews_months() {
    $header = array("user id", "username", " total number of reviews");

    for($i=0; $i<12; $i++) {
        $header[] = date("Y-m-d", strtotime("2014.001+{$i}months"))." (count)";
        $header[] = '="'.date("Y-m-d", strtotime("2014.001+{$i}months")).'"';
    }

    $data = array();

    $users = get_entities('user','',0,'',9999);

    //count reviews
    foreach($users as $user) {
        $user_reviews = array();
        $user_total_count = 0;
        for($i=0; $i<12; $i++) {
            $end = $i+1;
            $reviews = lds_contTools::getModificationsLdSsByDate($user->guid, strtotime("2014.001+{$i}months"), strtotime("2014.001+{$end}months"));

            if(!$reviews)
                $reviews = array();

            $user_reviews[] = count($reviews);
            $user_total_count += count($reviews);

            $user_week_list = array();
            foreach($reviews as $review) {
                $user_week_list["{$review->guid}"] = isset($user_week_list["{$review->guid}"]) ? $user_week_list["{$review->guid}"]+1 : 1;
            }

            $user_reviews[] = implode(',',array_keys($user_week_list));
        }
        $data[] = array_merge(array($user->guid, $user->username, $user_total_count), $user_reviews);
    }

    lds_echocsv($header, $data, 'reviews-months');

}

function lds_tracking_user_tool() {

    $header = array("user id", "username", "Design Pattern (number)", "Design Pattern", "Design Narrative (number)", "Design Narrative", "Persona Card (number)", "Persona Card", "Factors and Concerns (number)", "Factors and Concerns","Heuristic Evaluation (number)","Heuristic Evaluation","Course Map (number)", "Course Map", "CompendiumLD (number)","CompendiumLD","Image (number)","Image","WebCollage (number)","WebCollage","OpenGLM (number)","OpenGLM", "CADMOS (number)", "CADMOS");

    $data = array();

    $users = get_entities('user','',0,'',9999);

    //count designs
    foreach($users as $user) {
        $editor_subtypes = array(
            'design_pattern',
            'MDN',
            'PC',
            'FC',
            'HE',
            'coursemap',
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

    lds_echocsv($header, $data, "user_tool");

}function lds_tracking_user_conceptualize_tool() {
    global $CONFIG;
    $header = array("user id", "username", "Design Pattern (number)", "Design Pattern", "Design Narrative (number)", "Design Narrative", "Persona Card (number)", "Persona Card", "Factors and Concerns (number)", "Factors and Concerns","Heuristic Evaluation (number)","Heuristic Evaluation","Course Map (number)", "Course Map", "CompendiumLD (number)","CompendiumLD","Image (number)","Image","WebCollage (number)","WebCollage","OpenGLM (number)","OpenGLM", "CADMOS (number)", "CADMOS");

    $tools = $CONFIG->project_templates['full'];
    $data = array();

    $users = get_entities('user','',0,'',9999);

    foreach($tools as $tool) {
        
    }
    //count designs
    foreach($users as $user) {
        $editor_subtypes = array(
            'design_pattern',
            'MDN',
            'PC',
            'FC',
            'HE',
            'coursemap',
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

    lds_echocsv($header, $data, "user_tool");
}

function lds_tracking_tool() {

    $header = array("tool", "number of designs");

    $data = array();


    //count designs
    $editor_subtypes = array(
        array('design_pattern', "Design Pattern"),
        array('MDN', "Design Narrative"),
        array('PC', "Persona Card"),
        array('FC', "Factors and Concerns"),
        array('HE', "Heuristic Evaluation"),
        array('coursemap', "Course Map"),
    );

    $editor_types = array(
        array('cld', "CompendiumLD"),
        array('image', "Image"),
        array('webcollagerest', "WebCollage"),
        array('openglm', "OpenGLM"),
        array('cadmos', "CADMOS"),
    );



    foreach($editor_subtypes as $e_s) {
        $row = array();
        $ldss = get_entities_from_metadata('editor_subtype',$e_s[0], 'object', 'LdS', 0, 9999);
        if(!$ldss)
            $ldss = array();

        $row[] = $e_s[1];
        $row[] = count($ldss);

        $data[] = $row;

    }

    foreach($editor_types as $e_t) {
        $row = array();
        $ldss = get_entities_from_metadata('editor_type',$e_t[0], 'object', 'LdS', $user->guid, 9999);
        if(!$ldss)
            $ldss = array();

        $row[] = $e_t[1];
        $row[] = count($ldss);

        $data[] = $row;
    }

    lds_echocsv($header, $data, "tool");
}


function lds_tracking_groups() {
    $header = array("group id", "group name", "members");


    $data = array();

    $groups = get_entities('group','',0,'',9999);
    if($groups)
        foreach($groups as $group) {
            $group_data = array();
            $group_data[] = $group->guid;
            $group_data[] = $group->name;
            $group_data[] = $group->getMembers(9999, 0, true);
            $data[] = $group_data;
        }
    lds_echocsv($header, $data,'groups');
}

function lds_tracking_id_name($type) {
    $header = array("{$type} id", "name");
    if($type == "user")
        $header[] = "username";
    if($type == "lds")
        $header[] = "tool";
    $data = array();

    if($type == "group")
        $entities = get_entities('group','',0,'',9999);
    if($type == "user")
        $entities = get_entities('user','',0,'',9999);
    if($type == "lds")
        $entities = get_entities('object','LdS',0,'',9999);


    if($entities)
        foreach($entities as $entity) {
            $e_data = array();
            $e_data[] = $entity->guid;
            if($type == "lds") {
                $e_data[] = $entity->title;
                $editor_subtypes = array(
                    'design_pattern' => "Design Pattern",
                    'MDN' => "Design Narrative",
                    'PC' => "Persona Card",
                    'FC' => "Factors and Concerns",
                    'HE' => "Heuristic Evaluation",
                    'coursemap' => "Course Map",
                );

                $editor_types = array(
                    'cld' => "CompendiumLD",
                    'image' => "Image",
                    'webcollagerest' => "WebCollage",
                    'openglm' => "OpenGLM",
                    'cadmos' => "CADMOS",
                );

                if($entity->editor_subtype)
                    $e_data[] = $editor_subtypes[$entity->editor_subtype];

                if($entity->editor_type)
                    $e_data[] = $editor_types[$entity->editor_type];
            }
            else
                $e_data[] = $entity->name;

            if($type == "user") {
                $e_data[] = $entity->username;
            }
            $data[] = $e_data;
        }
    lds_echocsv($header, $data,"id_{$type}");
}


function lds_csv_private () {



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









    //date("d-m-Y", strtotime("2011W01+51week"));
}