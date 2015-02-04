<?php
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