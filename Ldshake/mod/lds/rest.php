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

// The authentication token api

function rest_login() {
    $postdata = file_get_contents("php://input");

    $xmldoc = new DOMDocument();
    $xmldoc->loadXML($postdata);

    $xpathvar = new Domxpath($xmldoc);

    $queryResult = $xpathvar->query('//login/username');
    foreach($queryResult as $result){
        $username = $result->textContent;
    }

    $queryResult = $xpathvar->query('//login/password');
    foreach($queryResult as $result){
        $password = $result->textContent;
    }

    if($user = get_user_by_username($username)) {
        if ($user->password == generate_user_password($user, $password)) {
            $token = auth_gettoken($username, $password);

            $result = SuccessResult::getInstance(array(
                'token' => $token)
            );
        }
        else {
            $result = ErrorResult::getInstance("Wrong username or password.");
        }
    } else {
        $result = ErrorResult::getInstance("Wrong username or password.");
    }

    return $result;
}
expose_function("login", "rest_login", array(), elgg_echo('login'), "POST", false, false);

function rest_logout() {
    $result = logout();

    set_input('view', 'status');
    $result = SuccessResult::getInstance($result);


    return $result;
}
expose_function("logout", "rest_logout", array(), elgg_echo('logout'), "GET", false, false);

function lds_rest_ping($lds_id) {

    $lds = get_entity($lds_id);
    //Sets the editing timestamp to now
    $lds->editing_tstamp = time();
    //And saves who's editing it
    $lds->editing_by = get_loggedin_userid();
    $lds->save_ktu();
    $result = true;

    set_input('view', 'status');
    $result = SuccessResult::getInstance($result);

    return $result;
}
//expose_function("ping", "rest_ping", array(), elgg_echo('ping'), "GET", false, false);


function lds_data() {
    global $API_QUERY;

    if(!isset($_FILES['design']) || !isset($_FILES['properties']))
        return array();

    if($_FILES['design']['error'] || $_FILES['properties']['error'])
        return array();

    if(!($xmlproperties = file_get_contents($_FILES['properties']['tmp_name'])))
        return array();

    $xmldoc = new DOMDocument();
    $xmldoc->loadXML($xmlproperties);

    $xpathvar = new Domxpath($xmldoc);

    $queryResult = $xpathvar->query('//lds/id');
    $id = $queryResult->item(0)->nodeValue;

    $queryResult = $xpathvar->query('//lds/type');
    $type = $queryResult->item(0)->nodeValue;

    $queryResult = $xpathvar->query('//lds/title');
    $title = $queryResult->item(0)->nodeValue;

    $queryResult = $xpathvar->query('//lds/revision');
    $revision = $queryResult->item(0)->nodeValue;

    $queryResult = $xpathvar->query('//lds/tags');
    $elemtags = $queryResult->item(0);

    $tags = array('tag' => array());
    foreach($elemtags->childNodes as $tagnode) {
        if($tagnode instanceof DOMElement) {
            $tag = array();
            $queryResult = $xpathvar->query('category', $tagnode);
            $tagcategory = $queryResult->item(0)->nodeValue;

            $queryResult = $xpathvar->query('value', $tagnode);
            $tagvalue = $queryResult->item(0)->nodeValue;

            //$tags['tag'][$tagcategory] = $tagvalue;
            $tags['tag'][] = array(
                'category' => $tagcategory,
                'value' => $tagvalue);
        }
    }

    $lds_data = array(
        'type' => $type,
        'title' => $title,
        'tags' => $tags['tags'],
        'doc' =>    array(
            'file' => $_FILES['design']['tmp_name'],
            'filename' => $_FILES['design']['name']
        )
    );

    if(isset($_FILES['design_imsld']))
        if(!$_FILES['design_imsld']['error']) {
                $lds_data['doc']['file_imsld'] = $_FILES['design_imsld']['tmp_name'];
                $lds_data['doc']['filename_imsld'] = $_FILES['design_imsld']['name'];
        }

    $lds = LdSFactory::buildLdS($lds_data);


    $id = $lds->guid;
    $revision = $lds->getAnnotations('revised_docs_editor', 1, 0, 'desc');
    $revision = $revision[0]->id;

    $result = array(
        'lds' => array(
            'id' => $id,
            'type' => $type,
            'title' => $title,
            'revision' => $revision,
            'tags' => $tags
    ));


    $result = SuccessResult::getInstance($result);

    return $result;
}
expose_function("newlds", "lds_data", array(), elgg_echo('ldsdata'), "POST", true, false);

function lds_query() {
    global $API_QUERY;

    $entities = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), false, 0, 0);

    $ldss = array();
    $lds = array();
    foreach($entities as $e) {

        if(!($revision = $e->getAnnotations('revised_docs_editor', 1, 0, 'desc')))
            $revision = $e->getAnnotations('revised_docs', 1, 0, 'desc');
        $revision = $revision[0]->id;

        $tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
        $tags = array();
        foreach ($tagtypes as $type)
        {
            $tag = array();
            $tag['category'] =$type;
            if (is_array($e->$type))
                $tag['value'] = implode(',',$e->$type);
            elseif (is_string($e->$type) && strlen($e->$type))
                $tag['value'] = $e->$type;
            else
                $tag['value'] = '';

            $tags['tag'][] = $tag;
        }

        $lds[] = array(
            'id' => $e->guid,
            'type' => $e->editor_type,
            'title' => $e->title,
            'revision' => $revision,
            'tags' => $tags
        );

    }

    $result = array(
        'lds_list' => array('lds' => $lds)
    );
    $result = SuccessResult::getInstance($result);

    return $result;
}
expose_function("ldseditorlist", "lds_query", array(), elgg_echo('ldsdata'), "GET", true, false);

function lds_view() {
    global $API_QUERY;

    $entities = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), false, 50, 0);

    $ldss = array();
    $lds = array();
    foreach($entities as $e) {

        if(!($revision = $e->getAnnotations('revised_docs_editor', 1, 0, 'desc')))
            $revision = $e->getAnnotations('revised_docs', 1, 0, 'desc');
        $revision = $revision[0]->id;

        $tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
        $tags = array();
        foreach ($tagtypes as $type)
        {
            $tag = array();
            $tag['category'] =$type;
            if (is_array($e->$type))
                $tag['value'] = implode(',',$e->$type);
            elseif (is_string($e->$type) && strlen($e->$type))
                $tag['value'] = $e->$type;
            else
                $tag['value'] = '';

            $tags['tag'][] = $tag;
        }

        $lds[] = array(
            'id' => $e->guid,
            'type' => $e->editor_type,
            'title' => $e->title,
            'revision' => $revision,
            'tags' => $tags
        );
    }

    $result = array(
        'lds_list' => array('lds' => $lds)
    );
    $result = SuccessResult::getInstance($result);

    return $result;
}
expose_function("ldsviewlist", "lds_view", array(), elgg_echo('ldsdata'), "GET", true, false);

function lds_update($params) {
    global $API_QUERY;

    if(isset($params[2])) {
        if(is_numeric($params[1]) && $params[2] == 'data')
            return lds_download($params);

        if(is_numeric($params[1]) && $params[2] == 'properties')
            return lds_view_properties($params[1]);

        if(is_numeric($params[1]) && $params[2] == 'ping')
            return lds_rest_ping($params[1]);
        //if($params[2] == 'imsld')
        //    return lds_download($params);
    }

    if(!isset($_FILES['design']) || !isset($_FILES['properties']))
        return array();

    if($_FILES['design']['error'] || $_FILES['properties']['error'])
        return array();

    if(!($xmlproperties = file_get_contents($_FILES['properties']['tmp_name'])))
        return array();

    $xmldoc = new DOMDocument();
    $xmldoc->loadXML($xmlproperties);

    $xpathvar = new Domxpath($xmldoc);

    $queryResult = $xpathvar->query('//lds/id');
    $id = $queryResult->item(0)->nodeValue;

    $queryResult = $xpathvar->query('//lds/type');
    $type = $queryResult->item(0)->nodeValue;

    $queryResult = $xpathvar->query('//lds/title');
    $title = $queryResult->item(0)->nodeValue;

    $queryResult = $xpathvar->query('//lds/revision');
    $revision = $queryResult->item(0)->nodeValue;

    $queryResult = $xpathvar->query('//lds/tags');
    $elemtags = $queryResult->item(0);

    $tags = array('tag' => array());
    foreach($elemtags->childNodes as $tagnode) {
        if($tagnode instanceof DOMElement) {
            $queryResult = $xpathvar->query('category', $tagnode);
            $tagcategory = $queryResult->item(0)->nodeValue;

            $queryResult = $xpathvar->query('value', $tagnode);
            $tagvalue = $queryResult->item(0)->nodeValue;

            //$tags['tags'][$tagcategory] = $tagvalue;
            $tags['tag'][] = array(
                'category' => $tagcategory,
                'value' => $tagvalue);
        }
    }

    $update = array(
        'id' => $id,
        'type' => $type,
        'title' => $title,
        'tags' => $tags['tags'],
        'doc' =>    array(
            'file' => $_FILES['design']['tmp_name'],
            'filename' => $_FILES['design']['name']
        ));

    if(isset($_FILES['design_imsld']))
        if(!$_FILES['design_imsld']['error']) {
            $update['doc']['file_imsld'] = $_FILES['design_imsld']['tmp_name'];
            $update['doc']['filename_imsld'] = $_FILES['design_imsld']['name'];
        }

    $lds = LdSFactory::updateLdS($update);

    $id = $lds->guid;
    $revision = $lds->getAnnotations('revised_docs_editor', 1, 0, 'desc');
    $revision = $revision[0];

    $tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
    $tags = array();
    foreach ($tagtypes as $type)
    {
        $tag = array();
        $tag['category'] =$type;
        if (is_array($lds->$type))
            $tag['value'] = implode(',',$lds->$type);
        elseif (is_string($lds->$type) && strlen($lds->$type))
            $tag['value'] = $lds->$type;
        else
            $tag['value'] = '';

        $tags['tag'][] = $tag;
    }

    $result = array('lds' => array(
        'id' => $id,
        'type' => $type,
        'title' => $title,
        'revision' => $revision->id,
        'tags' => $tags,
    ));


    $result = SuccessResult::getInstance($result);

    return $result;
}
expose_function("lds", "lds_update", array(), elgg_echo('ldsupdate'), "POST", true, false);

function lds_view_properties($lds_id) {
    global $API_QUERY;

    $e = get_entity($lds_id);
    if(!($revision = $e->getAnnotations('revised_docs_editor', 1, 0, 'desc')))
        $revision = $e->getAnnotations('revised_docs', 1, 0, 'desc');
    $revision = $revision[0]->id;

    $tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
    $tags = array();
    foreach ($tagtypes as $type)
    {
        $tag = array();
        $tag['category'] =$type;
        if (is_array($e->$type))
            $tag['value'] = implode(',',$e->$type);
        elseif (is_string($e->$type) && strlen($e->$type))
            $tag['value'] = $e->$type;
        else
            $tag['value'] = '';

        $tags['tag'][] = $tag;
    }

    $lds = array(
        'id' => $e->guid,
        'type' => $e->editor_type,
        'title' => $e->title,
        'revision' => $revision,
        'tags' => $tags
    );

    $result = array(
        'lds_list' => array('lds' => $lds)
    );
    $result = SuccessResult::getInstance($result);

    return $result;
}

function lds_download($params = null) {
    global $API_QUERY;

    set_input('view', 'binary');

    $editordocument = get_entities_from_metadata('lds_guid',$params[1],'object','LdS_document_editor', 0, 100);
    $document = $editordocument[0];

    $result = Editor::getFullFilePath($document->file_guid);

    $result = SuccessResult::getInstance($result);

    return $result;
}
expose_function("ldsdownload", "lds_download", array(), elgg_echo('ldsdownload'), "GET", true, false);

/**
 * This function serialises an object recursively into an XML representation.
 * The function attempts to call $data->export() which expects a stdClass in return, otherwise it will attempt to
 * get the object variables using get_object_vars (which will only return public variables!)
 * @param $data object The object to serialise.
 * @param $n int Level, only used for recursion.
 * @return string The serialised XML output.
 */
function ldshake_object_to_xml($data, $name = "", $n = 0)
{
    $classname = ($name=="" ? get_class($data) : $name);

    $vars = method_exists($data, "export") ? get_object_vars($data->export()) : get_object_vars($data);

    $output = "";

    if (($n==0) || ( is_object($data) && !($data instanceof stdClass))) $output = "<$classname>";

    foreach ($vars as $key => $value)
    {

        $o1 = "<$key>";
        if (is_array($value))
            if ($value !== array_values($value))
                $o1 .= "";

        $output .= $o1;

        if (is_object($value))
            $output .= ldshake_object_to_xml($value, $key, $n+1);
        else if (is_array($value))
            $output .= ldshake_array_to_xml($value, $n+1, $key);
        else
            $output .= htmlentities($value);

        $o2 = "</$key>\n";
        if (is_array($value))
            if ($value !== array_values($value))
                $o2 .= "";

        $output .= $o2;
    }

    if (($n==0) || ( is_object($data) && !($data instanceof stdClass))) $output .= "</$classname>\n";

    return $output;
}

/**
 * Serialise an array.
 *
 * @param array $data
 * @param int $n Used for recursion
 * @return string
 */
function ldshake_array_to_xml(array $data, $n = 0, $item = "array item")
{
    $output = "";

    foreach ($data as $key => $value)
    {
        //$item = "array_item";

        //if (is_numeric($key))
        if (!is_numeric($item) && is_array($value) && !($value !== array_values($value)))
            $output .= "";//"<$item>";
        else
        {
            if(!is_numeric($key))
                $item = $key;
            $output .= "<$item>";
        }

        if (is_object($value))
            $output .= ldshake_object_to_xml($value, "", $n+1);
        else if (is_array($value))
            $output .= ldshake_array_to_xml($value, $n+1, $key);
        else
            $output .= htmlentities($value);

        //if (is_numeric($key))
        if (!is_numeric($item) && is_array($value)  && !($value !== array_values($value)))
            $output .= "";//"</$item>\n";
        else
        {
            if(!is_numeric($key))
                $item = $key;
            $output .= "</$item>";
        }
    }

    return $output;
}

function ldshake_rest_params() {
    $path = explode('/', $_SERVER['PATH_INFO']);

    if (!count($path))
        throw new SecurityException(elgg_echo('SecurityException:APIAccessDenied'));

    $query = array();
    foreach($path as $ep)
        if(strlen($ep))
            $query[] = $ep;

    return $query;
}
