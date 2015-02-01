<?php
/** 
* Funciones relacionadas con la interación con la base de datos
*/

/** 
* Conectar con la base de datos
* @return resource Identificador del enlace MySQL
*/
function connectToDB() {
    $link = mysql_connect("localhost", "wic", "password");
    mysql_select_db("wic2p1", $link);
    return $link;
}



?>
