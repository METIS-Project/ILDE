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

include_once ("JSON.php");
include_once ("db/db.php");
include_once ("db/usersdb.php");

/** 
* Controlador que abstrae la gestión de los usuarios de su almacenamiento en una base de datos
*/

/**
 * Solicita crear un usuario en la base de datos
 * @param string $username Nombre del usuario
 * @param string $password Password del usuario
 * @param string $email Email del usuario
 * @return mixed Array con información del resultado
 */
function createUser($username, $password, $email) {
    $link = connectToDB();
    return createUserDB($link, $username, $password, $email);
}

/**
 * Comprueba si el usuario y contraseña son correctos e inicia la sesión del usuario
 * @param string $username Nombre del usuario que desea iniciar sesión
 * @param string $password Password del usuario que desea iniciar sesión
 * @return mixed Array con información del resultado
 */
function doLogin($username, $password) {
    $link = connectToDB();
    $user = getUserDataDB($link, $username, $password);
    if ($user === false) {
        $result = array("ok" => false, "badLoginPass" => true);
    } else {
        session_start();
//		session_register("session_username");
        $_SESSION['username'] = $username;
        $result = array("ok" => true, "username" => $username);
    }

    return $result;
}

/**
 * Cierra la sesión del usuario
 * @return mixed Array con información del resultado
 */
function doLogout() {
    session_start();
    session_destroy();
    return array("ok" => true);
}

/**
 * Llama a la función correspondiente a la tarea a realizar
 */
function doUserTask() {
    $task = $_REQUEST['task'];
    if ($task == "new") {
        $username = $_REQUEST['username'];
        $password = $_REQUEST['password'];
        $email = $_REQUEST['email'];

        $result = createUser($username, $password, $email);
    } else if ($task == "login") {

        $username = $_REQUEST['username'];
        $password = $_REQUEST['password'];
        $result = doLogin($username, $password);
    } else if ($task == "logout") {
        $result = doLogout();
    }

    $json = new Services_JSON();
    echo $json->encode($result);
}

doUserTask();
?>
