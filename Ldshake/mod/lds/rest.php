<?php
/**
 * Created by JetBrains PhpStorm.
 * User: saden
 * Date: 17/03/13
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */


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

    $token = auth_gettoken($username, $password);

    $result = SuccessResult::getInstance(array(
        'token' => $token)
    );

    return $result;
}
expose_function("login", "rest_login", array(), elgg_echo('login'), "POST", false, false);

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

    $queryResult = $xpathvar->query('//login/username');
    foreach($queryResult as $result){
        $username = $result->textContent;
    }

    $queryResult = $xpathvar->query('//login/password');
    foreach($queryResult as $result){
        $password = $result->textContent;
    }

    $token = 00;//auth_gettoken($username, $password);

    $result = SuccessResult::getInstance(array(
            'token' => $token)
    );

    return $result;
}
expose_function("ldsdata", "lds_data", array(), elgg_echo('ldsdata'), "POST", true, false);


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
        $output .= "<$key>";

        if (is_object($value))
            $output .= ldshake_object_to_xml($value, $key, $n+1);
        else if (is_array($value))
            $output .= ldshake_array_to_xml($value, $n+1);
        else
            $output .= htmlentities($value);

        $output .= "</$key>\n";
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
function ldshake_array_to_xml(array $data, $n = 0)
{
    $output = "";

    foreach ($data as $key => $value)
    {
        $item = "array_item";

        if (is_numeric($key))
            $output .= "<$item name=\"$key\" type=\"".gettype($value)."\">";
        else
        {
            $item = $key;
            $output .= "<$item>";
        }

        if (is_object($value))
            $output .= ldshake_object_to_xml($value, "", $n+1);
        else if (is_array($value))
            $output .= ldshake_array_to_xml($value, $n+1);
        else
            $output .= htmlentities($value);

        $output .= "</$item>\n";
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
