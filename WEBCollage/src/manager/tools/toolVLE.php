<?php

include_once("../JSON.php");

/**
 * Obtiene las herramientas del VLE a partir de la URI proporcionada
 * @param string $uri URI que contiene el XML con información de las herramientas
 * @return string URI base de las herramientas
 */
function getTools($uri, $user, $pass) {
    //cURL handler creation
    $ch = curl_init();
    
    //Autenticación
    curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC); 
    curl_setopt($ch, CURLOPT_USERPWD, $user.":".$pass); 
        
    // HTTP method
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "GET");
    //URL
    curl_setopt($ch, CURLOPT_URL, $uri);
    // set the handler for delivering answers in strings, instead of being directly printed on page
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
    // timeout
    curl_setopt($ch, CURLOPT_TIMEOUT, 5);
    // perform the HTTP request
    $xml = curl_exec($ch);
    // get answer HTTP code
    $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    // get cURL error code
    $curl_errno = curl_errno($ch);

    if (!$curl_errno && $status == 200) {

        $dom = new DOMDocument();
        $dom->loadXML($xml);
        $herramientas = array();
        //Obtener herramientas externas
        $tools = $dom->getElementsByTagName('externalTools')->item(0);
        $tools = $tools->getElementsByTagName('entry');
        $i = 0;
        foreach ($tools as $tool) {
            $herramientas[$i]["id"] = $tool->getElementsByTagName('key')->item(0)->nodeValue;
            $herramientas[$i]["name"] = $tool->getElementsByTagName('value')->item(0)->nodeValue;
            $i++;
        }
        //Obtenemos la URI base para las herramientas internas
        /* $uriBase = getUriInternalTools($uri);
          if (strlen($uriBase) > 0) {
          //Obtener herramientas internas
          $tools = $dom->getElementsByTagName('internalTools')->item(0);
          $tools = $tools->getElementsByTagName('entry');
          $i = count($herramientas);
          foreach ($tools as $tool) {
          $herramientas[$i]["id"] = $uriBase . "/" . $tool->getElementsByTagName('key')->item(0)->nodeValue . "/";
          $herramientas[$i]["name"] = $tool->getElementsByTagName('value')->item(0)->nodeValue;
          $i++;
          }
          } */
        //Obtener herramientas internas
        $tools = $dom->getElementsByTagName('internalTools')->item(0);
        $tools = $tools->getElementsByTagName('entry');
        $i = count($herramientas);
        foreach ($tools as $tool) {
            $herramientas[$i]["id"] = $tool->getElementsByTagName('key')->item(0)->nodeValue;
            $herramientas[$i]["name"] = $tool->getElementsByTagName('value')->item(0)->nodeValue;
            $i++;
        }


        curl_close($ch);
        return $result = array("ok" => true, "tools" => $herramientas);
    } else {
        curl_close($ch);
        return $result = array("ok" => false);
    }
}

/**
 * Crea la URI base de las herramientas internas a partir de la URI del VLE proporcionada
 * @param string $uri Crea la URI a partir de la proporcionada
 * @return string URI base de las herramientas
 */
function getUriInternalTools($uri) {
    $ext = ".xml";
    $extLength = strlen($ext);
    $uriLength = strlen($uri);
    $uriExt = substr($uri, $uriLength - $extLength, $extLength);
    if (strcmp($ext, $uriExt) == 0) {
        $uriit = substr($uri, 0, $uriLength - $extLength) . "/tools";
    } else {
        $uriit = "";
    }
    return $uriit;
}

session_start();

if (isset($_SESSION['username'])) {
    $username = $_SESSION['username'];
} else {
    $username = null;
}

$task = isset($_REQUEST['task']) ? $_REQUEST['task'] : "";
if ($task === 'get_tools') {
    $uri = $_REQUEST['uri'];
    $user = $_REQUEST['user'];
    $pass = $_REQUEST['pass'];
    $result = getTools($uri, $user, $pass);
}

$json = new Services_JSON();
echo $json->encode($result);
?>


