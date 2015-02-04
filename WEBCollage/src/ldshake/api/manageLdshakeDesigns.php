<?php

/**
 *  Functions to manage the ldshake designs in webcollage
 *  @author Javier E. Hoyos Torio 
 */

require_once "../../manager/JSON.php";
require_once "../../manager/db/db.php";
require_once "../../manager/db/designsdb.php";
require_once "../db/sectokendb.php";


/**
 * Loads a design uploaded by ldshake
 * @param string $document_id Identifier of the design
 * @param string $sectoken The security token associated to the design
 * @param string $username The name of the user who loads the design 
 * @return mixed Array with information about the result
 */
function loadLdshakeDesign($document_id, $sectoken, $username) {
    $link = connectToDB();    
    //Check if the sectoken provided matches the one stored in the database for this design
    $row = loadSectokenDB($link, $sectoken);
    if ($row !== false) {
        $docid_database = $row['docid'];
        if (strcmp($document_id, $docid_database) == 0) {           
            if (getAccessDB($link, $username, $document_id, 'read')) {
                $result = loadDesignDB($link, $document_id);
                if ($result !== false) {
                    $result['ok'] = true;
                } else {
                    $result = array("ok" => false);
                }
            } else {
                $result = array("ok" => false, "noAccess" => true);
            }
            return $result;
        }
    }
    $result = array("ok" => false, "noAccess" => true);
    return $result;
}

/**
 * Save a design uploaded by ldshake
 * @param string $document_id Identifier of the design
 * @param string $sectoken The security token associated to the design
 * @param string $username The name of the user who saves the design 
 * @param mixed $design The design information as a json
 * @param mixed $instance The instance information as a json 
 * @param mixed $todoDesign The list of pending tasks to do in the design
 * @param mixed $todoInstance The list of pending tasks to do in the instance  
 * @param mixed $actions The actions done since the last time the design was saved
 * @return mixed Array with information about the result
 */
function saveLdShakeDesign($document_id, $sectoken, $username, $design, $instance, $todoDesign, $todoInstance, $actions) {
    $link = connectToDB();    
    //Check the sectoken provided matches the one stored in the database for this design
    $row = loadSectokenDB($link, $sectoken);
    if ($row !== false) {
        $docid_database = $row['docid'];
        if (strcmp($document_id, $docid_database) == 0) { 
            if (getAccessDB($link, $username, $document_id, 'write')) {
                $result = saveDesignDB($link, $document_id, $design, $instance, $todoDesign, $todoInstance, $actions) ? array("ok" => true) : array("ok" => false, "couldntSave" => true);
            } else {
                $result = array("ok" => false, "noAccess" => true);
            }
            return $result;
        }
    }
    $result = array("ok" => false, "noAccess" => true);
    return $result;
}

/**
 * Undo the last change done in the design uploaded by ldshake
 * @param string $document_id Identifier of the design
 * @param string $sectoken The security token associated to the design
 * @param string $username The name of the user who wants to undo the change
 * @return mixed Array with information about the result
 */
function undoActionLdshakeDesign($document_id, $sectoken, $username) {
    $link = connectToDB();    
    //Check the sectoken provided matches the one stored in the database for this design
    $row = loadSectokenDB($link, $sectoken);
    if ($row !== false) {
        $docid_database = $row['docid'];
        if (strcmp($document_id, $docid_database) == 0) {     
            if (getAccessDB($link, $username, $document_id, 'write')) {
                $result = undoDesignActionDB($link, $document_id);
                if ($result !== false) {
                    $result['ok'] = true;
                } else {
                    $result = array("ok" => false, "couldntUndo" => true);
                }
            } else {
                $result = array("ok" => false, "noAccess" => true);
            }
            return $result;
        }
    }
    $result = array("ok" => false, "noAccess" => true);
    return $result;
}

/**
 * Redo the last change undone in the design uploaded by ldshake
 * @param string $document_id Identifier of the design
 * @param string $sectoken The security token associated to the design
 * @param string $username The name of the user who wants to undo the change
 * @return mixed Array with information about the result
 */
function redoActionLdshakeDesign($document_id, $sectoken, $username) {
    $link = connectToDB();    
    //Check the sectoken provided matches the one stored in the database for this design
    $row = loadSectokenDB($link, $sectoken);
    if ($row !== false) {
        $docid_database = $row['docid'];
        if (strcmp($document_id, $docid_database) == 0) { 
            if (getAccessDB($link, $username, $document_id, 'write')) {
                $result = redoDesignActionDB($link, $document_id);
                if ($result !== false) {
                    $result['ok'] = true;
                } else {
                    $result = array("ok" => false, "couldntRedo" => true);
                }
            } else {
                $result = array("ok" => false, "noAccess" => true);
            }
            return $result;
        }
    }
    $result = array("ok" => false, "noAccess" => true);
    return $result;
}

$username = 'ldshake_default_user';

$task = isset($_REQUEST['task']) ? $_REQUEST['task'] : "";
if ($task === 'loadLdshakeDesign') {
    $document_id = $_REQUEST['document_id'];
    $sectoken = $_REQUEST['sectoken'];
    $result = loadLdshakeDesign($document_id, $sectoken, $username);
} else if ($task === 'saveLdshakeDesign') {
    $document_id = $_REQUEST['document_id'];
    $sectoken = $_REQUEST['sectoken'];
    $doc = $_REQUEST['design'];
    $instance = $_REQUEST['instance'];
    $todoDesign = $_REQUEST['todoDesign'];
    $todoInstance = $_REQUEST['todoInstance'];
    $actions = isset($_REQUEST['actions']) ? $_REQUEST['actions'] : "";
    $result = saveLdshakeDesign($document_id, $sectoken, $username, $doc, $instance, $todoDesign, $todoInstance, $actions);
}  else if ($task === 'undoLdshakeDesign') {
    $document_id = $_REQUEST['document_id'];
    $sectoken = $_REQUEST['sectoken'];
    $result = undoActionLdshakeDesign($document_id, $sectoken, $username);
}  else if ($task === 'redoLdshakeDesign') {
    $document_id = $_REQUEST['document_id'];
    $sectoken = $_REQUEST['sectoken'];
    $result = redoActionLdshakeDesign($document_id, $sectoken, $username);
} else {
    $result = array("ok" => false);
}

$json = new Services_JSON();
echo $json->encode($result);
?>

