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

include_once("JSON.php");
include_once("db/db.php");
include_once("db/lmsdb.php");

/** 
* Controlador que abstrae la gestión de los LMS e instalaciones de su almacenamiento en una base de datos
*/

/**
 * Solicita crear un nuevo lms
 * @param string $name Nombre del lms
 * @param string $description Descripción del lms
 * @return mixed Array con información del resultado
 */
function crearLms($name, $description) {
    $idlms = crearLmsDB($name, $description);
    if ($idlms === false) {
        $result = array("ok" => false, "noInsert" => true);
    } else {
        $result = array("ok" => true, "idlms" => $instanceid);
    }
    return $result;
}

/**
 * Solicita crear una instalación de LMS
 * @param int $idlms Identificador del lms
 * @param int $instId Identificador de la instancia
 * @param string $title Nombre de la instalación
 * @param string $user Usuario de la instalación
 * @param string $pass Contraseña de la instalación
 * @param string $username Nombre del usuario de Web Instance Collage
 * @param mixed $other Otros parámetros adicionales de la instalación
 * @return mixed Array con información del resultado
 */
function crearLmsInstallation($idlms, $instId, $title, $user, $pass, $username, $others) {
    $idlmsInst = crearLmsInstallationDB($idlms, $instId, $title, $user, $pass, $username, $others);
    if ($idlmsInst === false) {
        $result = array("ok" => false, "noInsert" => true);
    } else {
        $result = array("ok" => true, "idlms_installation" => $idlmsInst);
    }
    return $result;
}

/**
 * Solicita obtener todas las instalaciones de LMS de un usuario concreto
 * @param string $username Nombre del usuario del que se quieren obtener sus instalaciones
 * @return mixed Array con información del resultado
 */
function obtenerLmsInstallationsUser($username) {
    $installations = obtenerLmsInstallationsDB($username);
    $result = array("ok" => true, "installations" => $installations);
    return $result;
}

/**
 * Solicita obtener todas las instalaciones de LMS de un usuario y las que no tienen un usuario asociado
 * @param string $username Nombre del usuario del que se quieren obtener sus instalaciones
 * @return mixed Array con información del resultado
 */
function obtenerLmsInstallations($username) {
    $installations = obtenerLmsInstallationsDB($username, true);
    $result = array("ok" => true, "installations" => $installations);
    return $result;
}

/**
 * Solicita obtener una instalación de LMS
 * @param int $idlms_installation Instalación de LMS que se desea obtener
 * @return mixed Array con información del resultado
 */
function obtenerLmsInstallation($idlms_installation) {
    $installation = obtenerLmsInstallationDB($idlms_installation);
    if ($installation == false) {
        $result = array("ok" => false);
    } else {
        $result = array("ok" => true, "installation" => $installation);
    }
    return $result;
}

/**
 * Solicita obtener una instalación de LMS y el LMS asociado
 * @param int $idlms_installation Instalación de LMS que se desea obtener
 * @return mixed Array con información del resultado
 */
function obtenerLmsInstallationLms($idlms_installation) {
    $installation = obtenerLmsInstallationLmsDB($idlms_installation);
    if ($installation == false) {
        $result = array("ok" => false);
    } else {
        $result = array("ok" => true, "installation" => $installation);
    }
    return $result;
}

/**
 * Solicita obtener un LMS
 * @param int $idlms Identificador del LMS que se desea obtener
 * @return mixed Array con información del resultado
 */
function obtenerUnLms($idlms) {
    $lms = obtenerUnLmsDB($idlms);
    if ($lms == false) {
        $result = array("ok" => false);
    } else {
        $result = array("ok" => true, "lms" => $lms);
    }
    return $result;
}

/**
 * Solicita eliminar una instalación de LMS
 * @param int $idlms_installation Identificador de la instalación a eliminar
 * @return mixed Array con información del resultado
 */
function eliminarLmsInstallation($idlms_installation) {
    $exito = eliminarLmsInstallationDB($idlms_installation);
    if ($exito == false) {
        $result = array("ok" => false);
    } else {
        $result = array("ok" => true);
    }
    return $result;
}

/**
 * Solicita actualizar una instalación de LMS
 * @param int $idlms_installation Identificador de la instalación
 * @param int $idlms Identificador del lms
 * @param int $instId Identificador de la instancia
 * @param string $title Nombre de la instalación
 * @param string $user Usuario de la instalación
 * @param string $pass Contraseña de la instalación
 * @param mixed $other Otros parámetros adicionales de la instalación
 * @return mixed Array con información del resultado
 */
function actualizaLmsInstallation($idlms_installation, $idlms, $instId, $title, $user, $pass, $others) {
    $result = actualizaLmsInstallationDB($idlms_installation, $idlms, $instId, $title, $user, $pass, $others);
    if ($result == true) {
        $result = array("ok" => true);
    } else {
        $result = array("ok" => false);
    }
    return $result;
}

/**
 * Solicita obtener todos los LMS
 * @return mixed Array con información del resultado
 */
function obtenerLms() {
    $lms = obtenerLmsDB();
    $result = array("lms" => $lms);
    return $result;
}

session_start();

if (isset($_SESSION['username'])) {
    $username = $_SESSION['username'];
} else {
    $username = null;
}
$task = isset($_REQUEST['task']) ? $_REQUEST['task'] : "";
if ($task === 'crear_lms') {
    $name = $_REQUEST['name'];
    $description = $_REQUEST['description'];
    $result = crearLms($name, $description);
} else if ($task === 'crear_lms_installation') {
    $idlms = $_REQUEST['idlms'];
    $instId = $_REQUEST['inst_id'];
    $title = $_REQUEST['title'];
    $user = $_REQUEST['user'];
    $pass = $_REQUEST['pass'];
    $others = $_REQUEST['others'];
    $result = crearLmsInstallation($idlms, $instId, $title, $user, $pass, $username, $others);
} else if ($task === 'actualiza_lms_installation') {
    $idlms_installation = $_REQUEST['idlms_installation'];
    $idlms = $_REQUEST['idlms'];
    $instId = $_REQUEST['inst_id'];
    $title = $_REQUEST['title'];
    $user = $_REQUEST['user'];
    $pass = $_REQUEST['pass'];
    $others = $_REQUEST['others'];
    $result = actualizaLmsInstallation($idlms_installation, $idlms, $instId, $title, $user, $pass, $others);
} else if ($task === 'obtener_installations_user') {
    $result = obtenerLmsInstallationsUser($username);
} else if ($task === 'obtener_installations') {
    $result = obtenerLmsInstallations($username);
} else if ($task === 'obtener_installation') {
    $idlms_installation = $_REQUEST['idlms_installation'];
    $result = obtenerLmsInstallation($idlms_installation);
} else if ($task === 'obtener_installation_lms') {
    $idlms_installation = $_REQUEST['idlms_installation'];
    $result = obtenerLmsInstallationLms($idlms_installation);
} else if ($task === 'obtener_un_lms') {
    $idlms = $_REQUEST['idlms'];
    $result = obtenerUnLms($idlms);
} else if ($task === 'eliminar_installation') {
    $idlms_installation = $_REQUEST['idlms_installation'];
    $result = eliminarLmsInstallation($idlms_installation);
} else if ($task === 'obtener_lms') {
    $result = obtenerLms();
} else {
    $result = array("ok" => false);
}


$json = new Services_JSON();
echo $json->encode($result);
?>
