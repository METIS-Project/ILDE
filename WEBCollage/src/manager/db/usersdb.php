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
* Consultas a la base de datos relativas a los usuarios
*/

/**
 * Crea un usuario en la base de datos
 * @param resource $link Conexión con la base de datos
 * @param string $username Nombre del usuario
 * @param string $password Password del usuario
 * @param string $email Email del usuario
 * Retorno: array que indica el resultado de la creación
 */
function createUserDB($link, $username, $password, $email) {
    $user = getUserDataDB($link, $username);
    if ($user !== false) {
        $result = array("ok" => false, "alreadyExists" => true, "username" => $username);
    } else {
        $query = "INSERT INTO users VALUES('" . mysql_real_escape_string($username) . "', '" . mysql_real_escape_string($password) . "', '" . mysql_real_escape_string($email) . "')";
        mysql_query($query, $link);
        $result = array("ok" => true);
    }

    return $result;
}

/**
 * Obtiene un usuario de la base de datos
 * @param resource $link Conexión con la base de datos
 * @param string $username Nombre del usuario
 * @param string $password Password del usuario
 * Retorno: mixed Array con los datos del usuario o false si no existe
 */
function getUserDataDB($link, $username, $password = null) {
    if ($password!=null) {
        $passwordCondition = "AND password = '" . mysql_real_escape_string($password) . "' ";
    } else {
        $passwordCondition = "";
    }
    $query = "SELECT * FROM users WHERE ( username = '" . mysql_real_escape_string($username) . "' $passwordCondition )";
    $result = mysql_query($query, $link);

    if (mysql_num_rows($result) > 0) {
        return mysql_fetch_assoc($result);
    } else {
        return false;
    }
}

?>