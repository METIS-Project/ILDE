<?php

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
