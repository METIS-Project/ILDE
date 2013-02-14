<?php

/**
 * Exporta el script de Web Instance Collage a IMS-LD
 */

error_reporting(E_ALL);
ini_set("display_errors", 1);

include_once("i18nParser.php");
include_once("JSON.php");
include_once("db/db.php");
include_once("db/designsdb.php");
//include_once("db/instancesdb.php");
include_once("export/imsld.php");
include_once("export/workflow.php");

session_start();
$username = isset($_SESSION['username']) ? $_SESSION['username'] : "anonymous";
//$format = $_REQUEST['format'];
$docid = $_REQUEST['id'];

$link = connectToDB();

if (getAccessDB($link, $username, $docid, 'read')) {
    $load = loadDesignDB($link, $docid);
    $design = $load['design'];
    $export="true";
    
    if (strcmp($export, "true") == 0) {
        $instance = $load['instance'];
        if (isset($_REQUEST['type']) && strcmp($_REQUEST['type'], 'workflow') == 0) {
        	$result = WorkflowInstanceExport($docid, $design, $instance);
        } else {
        	$result = IMSLDInstanceExport($docid, $design, $instance);
        }
    } else {
        $result = IMSLDexport($docid, $design);
    }
} else {
    echo "no access";
}

?>
