<?php

include_once("JSON.php");
include_once("db/db.php");
include_once("db/designsdb.php");

/**
 * Controlador que abstrae la gestión los diseños de su almacenamiento en una base de datos
 */

/**
 * Solicita la creación de un nuevo diseño
 * @param string $username Nombre del usuario
 * @param string $title Nombre del diseño
 * @return mixed Array con información del resultado
 */
function createLD($username, $title, $design, $instance) {
    if ($username) {
        $link = connectToDB();
        $docid = createDesignDB($link, $username, $title, $design, $instance, null);
        if ($docid === false) {
            $result = array("ok" => false, "noInsert" => true);
        } else {
            $result = array("ok" => true, "docid" => $docid);
        }
    } else {
        $result = array("ok" => false, "notLoggedIn" => true);
    }

    return $result;
}

/**
 * Solicita la creación de una copia de un diseño
 * @param int $docid Identificador del documento
 * @param string $username Nombre del usuario
 * @param string $title Nombre del documento
 * @return mixed Array con información del resultado
 */
function cloneDesign($docid, $username, $title) {
    $result = cloneDesignDB($docid, $username, $title);
    if ($result != false) {
        $result = array("ok" => true, "docid" => $docid);
    } else {
        $result = array("ok" => false);
    }
    return $result;
}

/**
 * Solicita cambiar la visibilidad de un documento
 * @param string $username Nombre del usuario
 * @param int $docid Identificador del documento
 * @param string $visibility Nueva visibilidad del documento
 * @return mixed Array con información del resultado
 */
function setVisibility($username, $docid, $visibility) {
    $link = connectToDB();
    if (setVisibilityDB($link, $username, $docid, $visibility)) {
        $result = array("ok" => true, "docid" => $docid, "visibility" => $visibility);
    } else {
        $result = array("ok" => false, "couldntChange" => true);
    }

    return $result;
}

/**
 * Solicita la carga un diseño
 * @param string $username Usuario que desea cargar el diseño
 * @param int $docid Identificador del documento a cargar
 * @return mixed Array con información del resultado
 */
function loadDesign($username, $docid) {
    $link = connectToDB();
    if (getAccessDB($link, $username, $docid, 'read')) {
        $result = loadDesignDB($link, $docid);
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

/**
 * Solicita guardar un diseño en la base de datos
 * @param string $username Usuario que desea guardar el diseño
 * @param int $docid Identificador del documento
 * @param mixed $data Información relativa al diseño
 * @param mixed $todo Información relativa a las tareas por hacer
 * @param mixed $actions Información relativa a las tares ya realizadas
 * @return mixed Array con información del resultado
 */
function saveDesign($username, $docid, $design, $instance, $todoDesign, $todoInstance, $actions) {
    if ($username) {
        $link = connectToDB();
        if (getAccessDB($link, $username, $docid, 'write')) {
            $result = saveDesignDB($link, $docid, $design, $instance, $todoDesign, $todoInstance, $actions) ?
                    array("ok" => true) :
                    array("ok" => false, "couldntSave" => true);
        } else {
            $result = array("ok" => false, "noAccess" => true);
        }
    } else {
        $result = array("ok" => false, "notLoggedIn" => true);
    }

    return $result;
}

/**
 * Solicita deshacer el último cambio realizado en el diseño
 * @param string $username Usuario que desea deshacer el cambio
 * @param int $docid Identificador del documento
 * @return mixed Array con información del resultado
 */
function undoAction($username, $docid) {
    $link = connectToDB();
    if (getAccessDB($link, $username, $docid, 'write')) {
        $result = undoDesignActionDB($link, $docid);
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

/**
 * Solicita rehacer el último cambio en el diseño
 * @param string $username Usuario que desea rehacer el cambio
 * @param int $docid Identificador del documento
 * @return mixed Array con información del resultado
 */
function redoAction($username, $docid) {
    $link = connectToDB();
    if (getAccessDB($link, $username, $docid, 'write')) {
        $result = redoDesignActionDB($link, $docid);
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

/**
 * SOlicita obtener todos los diseños de un usuario y sus versiones instanciadas
 * @param string $username Usuario del que se desean obtener todos los diseños y sus instancias
 * @return mixed Array con información del resultado
 */
function getUserDesigns($username) {
    if ($username != null) {
        $list = getUserDesignsDB($username);
        $result = array("username" => $username, "ok" => true, "list" => $list);
    } else {
        $result = array("username" => $username, "ok" => false);
    }
    return $result;
}

function deleteDesign($docid, $username) {
    $result = deleteDesignDB($docid, $username);
    if ($result === true) {
        $result = array("ok" => true);
    } else {
        $result = array("ok" => false);
    }
    return $result;
}

session_start();
$username = isset($_SESSION['username']) ? $_SESSION['username'] : null;
$task = isset($_REQUEST['task']) ? $_REQUEST['task'] : "";
if ($task === 'create') {
    $title = $_REQUEST['title'];
    $design = $_REQUEST['design'];
    $instance = $_REQUEST['instance'];
    $result = createLD($username, $title, $design, $instance);
} else if ($task === 'deleteDesign') {
    $docid = $_REQUEST['docid'];
    $result = deleteDesign($docid, $username);
} else if ($task === 'setvisibility') {
    $docid = $_REQUEST['id'];
    $visibility = $_REQUEST['visibility'];
    $result = setVisibility($username, $docid, $visibility);
} else if ($task === 'load') {
    $docid = $_REQUEST['id'];
    $result = loadDesign($username, $docid);
} else if ($task === 'save') {
    $docid = $_REQUEST['id'];
    $doc = $_REQUEST['design'];
    $instance = $_REQUEST['instance'];
    $todoDesign = $_REQUEST['todoDesign'];
    $todoInstance = $_REQUEST['todoInstance'];
    $actions = isset($_REQUEST['actions']) ? $_REQUEST['actions'] : "";
    $result = saveDesign($username, $docid, $doc, $instance, $todoDesign, $todoInstance, $actions);
} else if ($task === 'undo') {
    $docid = $_REQUEST['id'];
    $result = undoAction($username, $docid);
} else if ($task === 'redo') {
    $docid = $_REQUEST['id'];
    $result = redoAction($username, $docid);
} else if ($task === 'userDesigns') {
    $result = getUserDesigns($username);
} else if ($task === 'cloneDesign') {
    $docid = $_REQUEST['id'];
    $title = $_REQUEST['title'];
    $result = cloneDesign($docid, $username, $title);
} else {
    $result = array("ok" => false);
}

$json = new Services_JSON();
echo $json->encode($result);
?>
