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
 * Consultas a la base de datos relativas a los tipos de LMS e instalaciones
 */

/**
 * Crea un nuevo lms
 * @param string $name Nombre del lms
 * @param string $description Descripción del lms
 * @return mixed Identificador del lms o false si no se ha creado
 */
function crearLmsDB($name, $description) {
    $link = connectToDB();
    $sql = "insert into lms (name, description) values('" . mysql_real_escape_string($name) . "', '" . mysql_real_escape_string($description) . "'";
    if (mysql_query($sql, $link)) {
        //Obtenemos el id del lms generado por la columna auto_increment
        $idlms = mysql_insert_id($link);
        if ($idlms !== false) {
            return $idlms;
        }
    }
    return false;
}

/**
 * Crea una instalación de LMS
 * @param int $idlms Identificador del lms
 * @param int $instId Identificador de la instancia
 * @param string $title Nombre de la instalación
 * @param string $user Usuario de la instalación
 * @param string $pass Contraseña de la instalación
 * @param string $username Nombre del usuario de Web Instance Collage
 * @param mixed $other Otros parámetros adicionales de la instalación
 * @return mixedIdentificador de la instalación o false si no se ha podido crear
 */
function crearLmsInstallationDB($idlms, $instId, $title, $user, $pass, $username, $others) {
    $link = connectToDB();
    $sql = "insert into lms_installation (idlms, instance_identifier, title, user, pass, username, others) values(" . mysql_real_escape_string($idlms) . ", '" . mysql_real_escape_string($instId) . "',
        '" . mysql_real_escape_string($title) . "', '" . mysql_real_escape_string($user) . "', '" . mysql_real_escape_string($pass) . "', '" . mysql_real_escape_string($username) . "', '" . mysql_real_escape_string($others) . "')";
    if (mysql_query($sql, $link)) {
        //Obtenemos el id del lms_installation generado por la columna auto_increment
        $idlmsInstallation = mysql_insert_id($link);
        if ($idlmsInstallation !== false) {
            return $idlmsInstallation;
        }
    }
    return false;
}

/**
 * Elimina una instalación de LMS
 * @param int $idlms_installation Identificador de la instalación a eliminar
 * @return bool Booleano que indica si se ha podido eliminar la instalación
 */
function eliminarLmsInstallationDB($idlms_installation) {
    $link = connectToDB();
    $sql = "delete from lms_installation where idlms_installation=" . mysql_real_escape_string($idlms_installation);
    return (mysql_query($sql, $link));
}

/**
 * Actualiza una instalación de LMS
 * @param int $idlms_installation Identificador de la instalación
 * @param int $idlms Identificador del lms
 * @param int $instId Identificador de la instancia
 * @param string $title Nombre de la instalación
 * @param string $user Usuario de la instalación
 * @param string $pass Contraseña de la instalación
 * @param mixed $other Otros parámetros adicionales de la instalación
 * @return bool Booleano que indica si se ha podido actualizar la instalación
 */
function actualizaLmsInstallationDB($idlms_installation, $idlms, $instId, $title, $user, $pass, $others) {
    $link = connectToDB();
    $sql = "update lms_installation set idlms=" . mysql_real_escape_string($idlms) . ", instance_identifier='" . mysql_real_escape_string($instId) .
            "', title='" . mysql_real_escape_string($title) . "', user='" . mysql_real_escape_string($user) . "', pass='" . mysql_real_escape_string($pass) . "', others='" . mysql_real_escape_string($others) . "' where idlms_installation=" .
            mysql_real_escape_string($idlms_installation);
    return (mysql_query($sql, $link));
}

/**
 * Obtiene todas las instalaciones de LMS de un usuario concreto
 * @param string $username Nombre del usuario del que se quieren obtener sus instalaciones
 */
function obtenerLmsInstallationsDB($username, $public = null) {
    $link = connectToDB();
    $sql = "select * from lms_installation, lms  where lms_installation.idlms=lms.idlms";
    if ($public == null)
    {
        $sql = $sql." and username='" . mysql_real_escape_string($username) . "'";
    }
    else{
        $sql = $sql." and (username='" . mysql_real_escape_string($username) . "'". " or username='')";
    }
    $result = mysql_query($sql, $link);
    $resultado = array();
    if ($result && mysql_num_rows($result) > 0) {
        while ($row = mysql_fetch_assoc($result)) {
            $resultado[] = $row;
        }
    }
    return $resultado;
}

/**
 * Obtiene una instalación de LMS
 * @param int idlms_installation Instalación de LMS que se desea obtener
 * @return mixed Instalación obtenida o false si no se ha encontrado
 */
function obtenerLmsInstallationDB($idlms_installation) {
    $link = connectToDB();
    $sql = "select * from lms_installation where idlms_installation=" . mysql_real_escape_string($idlms_installation);
    $result = mysql_query($sql, $link);
    if ($result && mysql_num_rows($result) > 0) {
        $resultado = mysql_fetch_assoc($result);
        $json = new Services_JSON();
        $resultado['others'] = $json->decode($resultado['others']);
    } else {
        $resultado = false;
    }
    return $resultado;
}

/**
 * Obtiene una instalación de LMS y el LMS asociado
 * @param int idlms_installation Instalación de LMS que se desea obtener
 * @return mixed Instalación obtenida o false si no se ha encontrado
 */
function obtenerLmsInstallationLmsDB($idlms_installation) {
    $link = connectToDB();
    $sql = "select * from lms_installation, lms where idlms_installation=" . mysql_real_escape_string($idlms_installation)
            . " and lms_installation.idlms=lms.idlms";
    $result = mysql_query($sql, $link);
    if ($result && mysql_num_rows($result) > 0) {
        $resultado = mysql_fetch_assoc($result);
    } else {
        $resultado = false;
    }
    return $resultado;
}

/**
 * Obtiene un LMS
 * @param int idlms Identificador del LMS que se desea obtener
 * @return mixed Lms obtenido o false si no se ha encontrado
 */
function obtenerUnLmsDB($idlms) {
    $link = connectToDB();
    $sql = "select * from lms where idlms=" . mysql_real_escape_string($idlms);
    $result = mysql_query($sql, $link);
    if ($result && mysql_num_rows($result) > 0) {
        $resultado = mysql_fetch_assoc($result);
    } else {
        $resultado = false;
    }
    return $resultado;
}

/**
 * Obtiene todos los LMS
 * @return mixed Lms obtenidos
 */
function obtenerLmsDB() {
    $link = connectToDB();
    $sql = "select * from lms";
    $result = mysql_query($sql, $link);
    $resultado = array();
    if ($result && mysql_num_rows($result) > 0) {
        while ($row = mysql_fetch_assoc($result)) {
            $resultado[] = $row;
        }
    }
    return $resultado;
}

?>
