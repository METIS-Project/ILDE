<?php

/*Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Web Collage.

Web Collage is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Web Collage is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
