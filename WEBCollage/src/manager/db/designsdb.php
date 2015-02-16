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
 * Consultas a la base de datos relativas a los diseños
 */

/**
 * Obtiene de la base de datos acceso al documento de diseño
 * @param resource $link Identificador del enlace MySQL
 * @param string $username Nombre del usuario
 * @param int $docid Identificador del documento
 * @param string $access Tipo de acceso al documento
 */
function getAccessDB($link, $username, $docid, $access) {
    $sql = "SELECT username, visibility FROM documents WHERE docid=" . mysql_real_escape_string($docid);
    $row = mysql_fetch_assoc(mysql_query($sql, $link));

    return $row &&
            (
            $username === $row['username'] || (
                    ($access === 'read' && $row['visibility'] !== 'private') ||
                            ($access === 'write' && $row['visibility'] === 'public')
            )
    );
}

/**
 * Crea un nuevo diseño en la base de datos
 * @param resource $link Identificador del enlace MySQL
 * @param string $username Nombre del usuario
 * @param string $title Nombre del diseño
 * @return mixed Identificador del diseño o false si no se ha podido crear
 */
function createDesignDB($link, $username, $title, $design, $instance, $todoDesign) {
    $sql = "INSERT INTO documents (username, title) values('" . mysql_real_escape_string($username) . "', '" . mysql_real_escape_string($title) . "')";
    if (mysql_query($sql, $link)) {
        $docid = mysql_insert_id($link);
        if ($docid !== false && saveDesignDB($link, $docid, $design, $instance, $todoDesign, NULL, NULL)) {
            return $docid;
        }
    }

    return false;
}

/**
 * Crea una copia de un diseño
 * @param int $docid Identificador del documento
 * @param string $username Nombre del usuario
 * @param string $title Nombre del documento
 * @return mixed Identificador del diseño o false si no se ha podido crear
 */
function cloneDesignDB($docid, $username, $title) {
    $link = connectToDB();
    $sql = "INSERT INTO documents (username, title) values('" . mysql_real_escape_string($username) . "', '" . mysql_real_escape_string($title) . "')";
    if (mysql_query($sql, $link)) {
        $nuevoDocid = mysql_insert_id($link);
        if ($nuevoDocid !== false) {
            $docversion = getLastValidVersionDB($link, $docid);
            $version = getVersionDB($docid, $docversion);
            saveDesignDB($link, $nuevoDocid, $version["design"], $version["instance"], $version["todo_design"], $version["todo_instance"], $version["actions"]);
            return $nuevoDocid;
        }
    }
    return false;
}

/**
 * Cambia la visibilidad de un documento
 * @param resource $link Identificador del enlace MySQL
 * @param string $username Nombre del usuario
 * @param int $docid Identificador del documento
 * @param string $visibility Visibilidad del documento
 * @return bool Booleano que indica si se ha cambiado la visibilidad
 */
function setVisibilityDB($link, $username, $docid, $visibility) {
    $sql = "UPDATE documents SET visibility='" . mysql_real_escape_string($visibility) . "' WHERE (docid='" . mysql_real_escape_string($docid) . "' AND username LIKE '" . mysql_real_escape_string($username) . "') LIMIT 1";
    mysql_query($sql, $link);
    $affected = mysql_affected_rows($link);
    if ($affected === 1) {
        return true;
    } else {
        return false;
    }
}

/**
 * Obtiene la última versión válida del documento
 * @param resource $link Identificador del enlace MySQL
 * @param int $docid Identificador del documento
 * @return mixed Última versión válida o -1 si no se ha encontrado
 */
function getLastValidVersionDB($link, $docid) {
    $sql = "SELECT MAX(version) AS max FROM versions WHERE docid='" . mysql_real_escape_string($docid) . "' AND valid";
    $result = mysql_query($sql, $link);
    if ($row = mysql_fetch_assoc($result)) {
        return $row['max'];
    } else {
        return -1;
    }
}

/**
 * Obtiene la próxima versión válida del documento
 * @param resource $link Identificador del enlace MySQL
 * @param int $docid Identificador del documento
 * @return mixed Última versión válida o 0 si no hay versiones anteriores
 */
function getNextVersionDB($link, $docid) {
    $sql = "SELECT MAX(version) AS max FROM versions WHERE docid='" . mysql_real_escape_string($docid) . "'";
    $result = mysql_query($sql, $link);
    if ($row = mysql_fetch_assoc($result)) {
        return $row['max'] + 1;
    } else {
        return 0;
    }
}

/**
 * Guarda un diseño en la base de datos
 * @param resource $link Identificador del enlace MySQL
 * @param int $docid Identificador del documento
 * @param mixed $data Información relativa al diseño
 * @param mixed $todo Información relativa a las tareas por hacer
 * @param mixed $actions Información relativa a las tares ya realizadas
 * @return bool Booleano que indica si el diseño se ha guardado correctamente
 */
function saveDesignDB($link, $docid, $design, $instance, $todo_design, $todo_instance, $actions) {
    $version = getNextVersionDB($link, $docid);
    $time = time();
    $sql = "INSERT INTO versions (docid, version, time, design, instance, todo_design, todo_instance, actions) VALUES ($docid, $version, $time, '" . mysql_real_escape_string($design) . "', '" . mysql_real_escape_string($instance) . "', '" . mysql_real_escape_string($todo_design) . "', '" . mysql_real_escape_string($todo_instance) . "', '" . mysql_real_escape_string($actions) . "')";
    if (mysql_query($sql, $link)) {
        return true;
    } else {
        return false;
    }
}

/**
 * Deshace el último cambio realizado en el diseño
 * @param resource $link Identificador del enlace MySQL
 * @param int $docid Identificador del documento
 * @param int $setnext Especifica la próxima versión del diseño
 * @return mixed Versión anterior del diseño o false si no es posible
 */
function undoDesignActionDB($link, $docid, $setnext = -1) {
    $version = getLastValidVersionDB($link, $docid);
    if ($version >= 0) {
        $sql = "UPDATE versions SET valid=false WHERE (docid=" . mysql_real_escape_string($docid) . " AND version=" . mysql_real_escape_string($version) . ")";
        mysql_query($sql, $link);
        $affected = mysql_affected_rows($link);
        if ($affected === 1) {
            return loadDesignDB($link, $docid, $version);
        }
    }

    return false;
}

/**
 * Rehace el último cambio en el diseño
 * @param resource $link Identificador del enlace MySQL
 * @param int $docid Identificador del documento
 * @return mixed Versión posterior del diseño o false si no es posible
 */
function redoDesignActionDB($link, $docid) {
    $version = getLastValidVersionDB($link, $docid);
    if ($version >= 0) {
        $sql = "SELECT next FROM versions WHERE (docid=" . mysql_real_escape_string($docid) . " AND version=" . mysql_real_escape_string($version) . ")";

        $result = mysql_query($sql, $link);
        if ($row = mysql_fetch_assoc($result)) {
            $next = $row['next'];
            $sql = "UPDATE versions SET valid=true WHERE (docid=" . mysql_real_escape_string($docid) . " AND version=" . mysql_real_escape_string($next) . ")";
            mysql_query($sql, $link);
            $affected = mysql_affected_rows($link);
            if ($affected === 1) {
                return loadDesignDB($link, $docid);
            }
        }
    }
    return false;
}

/**
 * Carga un diseño de la base de datos
 * @param resource $link Identificador del enlace MySQL
 * @param int $docid Identificador del documento
 * @param int $setnext Especifica la próxima versión del diseño
 * @return mixed Versión del diseño cargada
 */
function loadDesignDB($link, $docid, $nextid = -1) {
    $version = getLastValidVersionDB($link, $docid);
    if (strlen($version) > 0) {
        if ($nextid >= 0) {
            $sql = "UPDATE versions SET next=" . mysql_real_escape_string($nextid) . " WHERE (docid=" . mysql_real_escape_string($docid) . " AND version=" . mysql_real_escape_string($version) . ")";
            if (!mysql_query($sql, $link)) {
                return false;
            }
        }

        $sql = "SELECT docid, design, instance, todo_design, todo_instance FROM versions WHERE (docid=" . mysql_real_escape_string($docid) . " AND version=" . mysql_real_escape_string($version) . ")";
        $result = mysql_query($sql, $link);
        if (mysql_num_rows($result) > 0 && $row = mysql_fetch_assoc($result)) {
            if ($row['design'] == NULL) {
                return array("docid" => $row['docid'], "empty" => true);
            } else {
                $json = new Services_JSON();
                return array("docid" => $row['docid'],
                        "design" => $json->decode($row['design']),
                        "instance" => json_decode($row['instance']),
                        "todoDesign" => $json->decode($row['todo_design']),
                        "todoInstance" => $json->decode($row['todo_instance'])
                );
            }
        }
    }

    return false;
}

/**
 * Obtiene todos los diseños de un usuario
 * @param string $username Usuario del que se van a obtener sus diseños
 * @return mixed Diseños del usuario
 */
function getUserDesignsDB($username) {
    $link = connectToDB();
    $sql = "select * from documents where username='" . mysql_real_escape_string($username) . "'";
    $sql = $sql . " order by docid desc";
    $result = mysql_query($sql, $link);
    $resultado = array();
    if ($result && mysql_num_rows($result) > 0) {
        while ($row = mysql_fetch_assoc($result)) {
            $resultado[] = $row;
        }
    }
    return $resultado;
}

/* Obtiene una versión de un documento de diseño
 * @param int $docid Identificador del documento
 * @param int $version Versión del diseño a obtener
 * @return mixed Versión del diseño especificada o false si no se encuentra
*/

function getVersionDB($docid, $docversion) {
    $link = connectToDB();
    $sql = "select * from versions where docid=" . mysql_real_escape_string($docid) . " and version=" . mysql_real_escape_string($docversion);
    $result = mysql_query($sql, $link);
    if ($result && mysql_num_rows($result) > 0) {
        $row = mysql_fetch_assoc($result);
        return $row;
    }
    return false;
}

/**
 * Obtiene todas las versiones de un documento de diseño
 * @param int $docid Identificador del documento
 * @param string $username Usuario del que se van a obtener las versiones del diseño
 * @return mixed Versiones del diseño
 */
function obtenerVersionesDB($docid, $username) {
    $link = connectToDB();
    $sql = "select * from versions where docid=" . mysql_real_escape_string($docid) . " and username='" . mysql_real_escape_string($username) . "'";
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
 * Borra un diseño de un usuario
 * @param int $docid Identificador del documento
 * @param string $username Usuario del que se va a borrar el diseño
 * @return bool Booleano que indica si ha sido posible borrar el diseño
 */
function deleteDesignDB($docid, $username) {
    $link = connectToDB();
    $sql = "delete from documents where docid=" . mysql_real_escape_string($docid) . " and username='" . mysql_real_escape_string($username) . "'";
    if (mysql_query($sql, $link)) {
        $sql = "delete from versions where docid=" . mysql_real_escape_string($docid);
        if (mysql_query($sql, $link)) {
            return true;
        }
    }
    return false;
}

?>
