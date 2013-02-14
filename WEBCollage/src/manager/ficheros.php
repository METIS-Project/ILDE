<?php


include_once("JSON.php");
include_once("manageLms.php");


/**
 * Funciones para el manejo de ficheros en Web instance Collage
 */

/**
 *  Sube un fichero al servidor
 *  @param string $username Usuario que sube el fichero al servidor
 *  @return mixed Array con información del resultado
 */
function uploadFile($username) {
    $nombre_archivo = $_FILES['archivo']['name'];
    $tipo_archivo = $_FILES['archivo']['type'];
    $tamano_archivo = $_FILES['archivo']['size'];
    $url = "http://" . $_SERVER['HTTP_HOST'] . $_SERVER['PHP_SELF'];
    $file = dirname($url);
    //Creamos un nombre de fichero único mediante la funcion time
    $destino = $file . "/../files/" . $username . "_" . time() . "_" . $nombre_archivo;
    //compruebo si las características del archivo son las que deseo 
    if (strcmp($tipo_archivo, "text/plain") != 0 || $tamano_archivo > 100000) {
        $result = array("ok" => false, "notSupportedFormat" => true);
    } else {
        if (move_uploaded_file($_FILES['archivo']['tmp_name'], "../files/" . $username . "_" . time() . "_" . $nombre_archivo)) {
            $result = array("ok" => true, "uploaded" => true, "path" => $destino);
        } else {
            $result = array("ok" => false, "unknownError" => true);
        }
    }
    return $result;
}

/**
 *  Sustituye un fichero del servidor por otro
 *  @param string $username Usuario que sube el fichero al servidor
 *  @param int idlms_installation Identificador de la instalación
 */
function changeFile($username, $idlms_installation) {
    $resultInst = obtenerLmsInstallation($idlms_installation);
    if ($resultInst['ok'] == true) {
        $installation = $resultInst['installation'];
        $pos = strrpos($installation['instance_identifier'], "/");
        $nombre = substr($installation['instance_identifier'], $pos + 1);
        $rutaFichero = "../files/" . $nombre;
        unlink($rutaFichero);
        $nombre_archivo = $_FILES['archivoEditar']['name'];
        $tipo_archivo = $_FILES['archivoEditar']['type'];
        $tamano_archivo = $_FILES['archivoEditar']['size'];
        $url = "http://" . $_SERVER['HTTP_HOST'] . $_SERVER['PHP_SELF'];
        $file = dirname($url);
        //Creamos un nombre de fichero único mediante la funcion time
        $destino = $file . "/../files/" . $username . "_" . time() . "_" . $nombre_archivo;
        //compruebo si las características del archivo son las que deseo 
        if (strcmp($tipo_archivo, "text/plain") != 0 || $tamano_archivo > 100000) {
            $result = array("ok" => false, "notSupportedFormat" => true);
        } else {
            if (move_uploaded_file($_FILES['archivoEditar']['tmp_name'], "../files/" . $username . "_" . time() . "_" . $nombre_archivo)) {
                $result = array("ok" => true, "updated" => true, "path" => $destino);
            } else {
                $result = array("ok" => false, "unknownError" => true);
            }
        }
    }
    return $result;
}

session_start();
$username = isset($_SESSION['username']) ? $_SESSION['username'] : null;
$task = isset($_REQUEST['task']) ? $_REQUEST['task'] : "";
if ($task === 'uploadFile') {
    $result = uploadFile($username);
} else if ($task === 'changeFile') {
    $idlms_installation = $_REQUEST['idlms_installation'];
    $result = changeFile($username, $idlms_installation);
} else {
    $result = array("ok" => false);
}



//IMPORTANTE. La respuesta json debe ir envuelta en un textarea. Si no se produce un error
$json = new Services_JSON();
echo "<html><body><textarea>";
echo $json->encode($result);
echo "</textarea></body></html>";
?>
