<?php

include_once("../JSON.php");
include_once("../db/db.php");
include_once("../db/lmsdb.php");
include_once ("lms.php");
include_once ("lmsTonto.php");
include_once ("lmsFichero.php");
include_once ("lmsPA.php");
include_once ("lmsGluePS.php");

define("FICHERO", 1); //Identificador asociado al tipo de LMS fichero
define("TONTO", 2); //Identificador asociado al tipo de LMS tonto
define("PA", 3); //Identificador asociado al tipo de LMS PA
define("GLUEPS", 4); //Identificador asociado al tipo de LMS GluePS

/**
 * Controlador que abstrae la forma concreta de obtención de información de cada LMS
 */

/**
 * Obtiene los LMS de una instalación en función del tipo de LMS de la misma
 * @param int idlms_installation Identificador de la instalación de la que se desean obtener los LMS
 * @return mixed Array con el resultado
 */
function obtenerLms($idlms_installation) {
    $installation = obtenerLmsInstallationDB($idlms_installation);
    switch ($installation["idlms"]) {
        case PA: {
                $url = $installation['instance_identifier'];
                $login = $installation['user'];
                $password = $installation['pass'];
                $toolid = $installation['others']->toolid;
                $instancia = new lmsPA($url, $login, $password, $toolid);
                $lms = $instancia->getLms();
                if ($lms == false) {
                    $result = array("ok" => false, "lms" => $lms);
                } else {
                    $result = array("ok" => true, "lms" => $lms);
                }
                break;
            }
        case GLUEPS: {
                $url = $installation['instance_identifier'];
                $login = $installation['user'];
                $password = $installation['pass'];
                $instancia = new lmsGluePS($url, $login, $password);
                $lms = $instancia->getLms();
                if ($lms == false) {
                    $result = array("ok" => false, "lms" => $lms);
                } else {
                    $result = array("ok" => true, "lms" => $lms);
                }
                break;
            }

        default: {
                //NO hay LMS asociados a la instalación
                $result = array("ok" => false);
            }
    }
    return $result;
}

/**
 * Obtiene las clases de un LMS en función del tipo de LMS de la instalación
 * @param int idlms_installation Identificador de la instalación de la que se desean obtener las clases
 * @param int idlmsSystem LMS del que se van a obtener las clases
 * @return mixed Array con el resultado
 */
function obtenerClasesLms($idlms_installation, $idlmsSystem) {
    $installation = obtenerLmsInstallationDB($idlms_installation);
    switch ($installation["idlms"]) {
        case PA: {
                $url = $installation['instance_identifier'];
                $login = $installation['user'];
                $password = $installation['pass'];
                $toolid = $installation['others']->toolid;
                $instancia = new lmsPA($url, $login, $password, $toolid);
                //$clases = $instancia->getClasesLms($idlmsSystem);
                //Fijamos el LMS con el que se va a trabajar
                $instancia->setIdlmsSystem($idlmsSystem);
                $clases = $instancia->getClases();
                if ($clases != false) {
                    $result = array("ok" => true, "clases" => $clases);
                } else {
                    $result = array("ok" => false);
                }
                break;
            }
        case GLUEPS: {
                $url = $installation['instance_identifier'];
                $login = $installation['user'];
                $password = $installation['pass'];
                $instancia = new lmsGluePS($url, $login, $password);
                //Fijamos el LMS con el que se va a trabajar
                $instancia->setIdlmsSystem($idlmsSystem);
                $clases = $instancia->getClases();
                if ($clases != false) {
                    $result = array("ok" => true, "clases" => $clases);
                } else {
                    $result = array("ok" => false);
                }
                break;
            }
        default: {
                $result = array("ok" => false);
            }
    }
    return $result;
}

/**
 * Obtiene los participantes de un lms y clase de glue
 * @param int idlms_installation Identificador de la instalación que tiene la información de glue
 * @param int idlmsSystem Identificador del lms de glue
 * @param int idlmsCourse Identificador de la clase
 * @return mixed Array con el resultado
 */
function obtenerParticipantesLms($idlms_installation, $idlmsSystem, $idlmsCourse) {
    $installation = obtenerLmsInstallationDB($idlms_installation);
    switch ($installation["idlms"]) {
        case PA: {
                $url = $installation['instance_identifier'];
                $login = $installation['user'];
                $password = $installation['pass'];
                $toolid = $installation['others']->toolid;
                $instancia = new lmsPA($url, $login, $password, $toolid);
                //Fijamos el LMS con el que se va a trabajar
                $instancia->setIdlmsSystem($idlmsSystem);
                $participantes = $instancia->getParticipantes($idlmsCourse);
                if ($participantes != false) {
                    $result = array("ok" => true, "participantes" => $participantes);
                } else {
                    $result = array("ok" => false);
                }
                break;
            }
        case GLUEPS: {

                $url = $installation['instance_identifier'];
                $login = $installation['user'];
                $password = $installation['pass'];
                $instancia = new lmsGluePS($url, $login, $password);
                //Fijamos el LMS con el que se va a trabajar
                $instancia->setIdlmsSystem($idlmsSystem);
                $participantes = $instancia->getParticipantes($idlmsCourse);
                if ($participantes != false) {
                    $result = array("ok" => true, "participantes" => $participantes);
                } else {
                    $result = array("ok" => false);
                }
                break;
            }
        default: {
                $result = array("ok" => false);
            }
    }
    return $result;
}

/**
 * Obtiene las clases de una instalación
 * @param int idlms_installation Identificador de la instalación de la que se desean obtener las clases
 * @return mixed Array con el resultado
 */
function obtenerClases($idlms_installation) {
    $installation = obtenerLmsInstallationDB($idlms_installation);
    switch ($installation["idlms"]) {
        //Obtener las clases desde un fichero
        case FICHERO: {
                $parametros["fichero"] = $installation["instance_identifier"];
                $instancia = new lmsFichero($parametros);
                $clases = $instancia->getClases();
                if ($clases === false) {
                    $result = array("ok" => false);
                } else {
                    $result = array("ok" => true, "clases" => $clases);
                }
                break;
            }
        //Obtener las clases del adaptador "tonto"
        case TONTO: {
                $instancia = new lmsTonto();
                $clases = $instancia->getClases();
                if ($clases === false) {
                    $result = array("ok" => false);
                } else {
                    $result = array("ok" => true, "clases" => $clases);
                }
                break;
            }

        case PA: {
                $url = $installation['instance_identifier'];
                $login = $installation['user'];
                $password = $installation['pass'];
                $toolid = $installation['others']->toolid;
                $instancia = new lmsPA($url, $login, $password, $toolid);
                $clases = $instancia->getClases();
                $result = array("ok" => true, "clases" => $clases);
                break;
            }
        default: {
                $result = array("ok" => false);
            }
    }
    return $result;
}

/**
 * Obtiene los participantes de una clase de una instalación
 * @param int idlms_installation Identificador de la instalación 
 * @param int idclase Identificador de la clase
 * @return mixed Array con el resultado
 */
function obtenerParticipantes($idlms_installation, $idclase) {
    $installation = obtenerLmsInstallationDB($idlms_installation);
    switch ($installation["idlms"]) {
        //Obtener participantes desde un fichero
        case FICHERO: {
                $parametros["fichero"] = $installation["instance_identifier"];
                $instancia = new lmsFichero($parametros);
                $participantes = $instancia->getParticipantes($idclase);
                if ($participantes === false) {
                    $result = array("ok" => false);
                } else {
                    $result = array("ok" => true, "participantes" => $participantes);
                }
                break;
            }
        //Obtener participantes desde el adaptador tonto
        case TONTO: {
                $instancia = new lmsTonto();
                $participantes = $instancia->getParticipantes($idclase);
                if ($participantes === false) {
                    $result = array("ok" => false);
                } else {
                    $result = array("ok" => true, "participantes" => $participantes);
                }
                break;
            }
        //Obtener participantes desde el adaptador PA
        case PA: {
                $url = $installation['instance_identifier'];
                $login = $installation['user'];
                $password = $installation['pass'];
                $toolid = $installation['others']->toolid;
                $instancia = new lmsPA($url, $login, $password, $toolid);
                $participantes = $instancia->getParticipantes($idclase);
                if ($participantes === false) {
                    $result = array("ok" => false);
                } else {
                    $result = array("ok" => true, "participantes" => $participantes);
                }
                break;
            }
        default: {
                $result = array("ok" => false);
            }
    }
    return $result;
}

function createGluePSDesign($url, $title, $zipUrl, $imsldType) {
    $instancia = new lmsGluePS("", "", "");
    $url = "http://www.gsic.uva.es/GLUEPSManager/designs";
    $designid = $instancia->createDesign($url, $title, $zipUrl, $imsldType);
    if ($designid != false) {
        $result = array("ok" => true, "designid" => $designid);
    } else {
        $result = array("ok" => false);
    }
    return $result;
}

function createGluePSDeploy($designId, $deployTitle, $fileUrl, $vleSelect, $teacherNames, $themeSelect) {
    $instancia = new lmsGluePS("", "", "");
    $url = "http://www.gsic.uva.es/GLUEPSManager/designs";
    $url = $url . "/" . $designId . "/deploys";
    $deployid = $instancia->createDeploy($url, $deployTitle, $fileUrl, $vleSelect, $teacherNames, $themeSelect);
    if ($deployid != false) {
        $result = array("ok" => true, "deployid" => $deployid);
    } else {
        $result = array("ok" => false);
    }
    return $result;
}

function createGluePSDesignDeploy($url, $title, $zipUrl, $imsldType, $deployTitle, $fileUrl, $vleSelect, $teacherNames, $themeSelect, $user, $pass) {
    //$instancia = new lmsGluePS("", "", "");
    $instancia = new lmsGluePS($url, $user, $pass);
    $url = $url . "/designs";
    $designId = $instancia->createDesign($url, $title, $zipUrl, $imsldType, $user, $pass);
    //nos quedamos con el identificador numérico del diseño
    $startPos= strpos($designId, "/designs/") + strlen("/designs/");
    $designId = substr($designId, $startPos);
    if ($designId != false) {
        $url = $url . "/" . $designId . "/deploys";
        $deployid = $instancia->createDeploy($url, $deployTitle, $fileUrl, $vleSelect, $teacherNames, $themeSelect, $user, $pass);
        //nos quedamos con el identificador numérico del despliegue
        $startPos= strpos($deployid, "/deploys/") + strlen("/deploys/");
        $deployid = substr($deployid, $startPos);
        if ($deployid != false) {
            $result = array("ok" => true, "deployid" => $deployid);
        } else {
            $result = array("ok" => false, "deployError" =>true);
        }
    } else {
        $result = array("ok" => false, "createDesignError" =>true);
    }
    return $result;
}

session_start();

if (isset($_SESSION['username'])) {
    $username = $_SESSION['username'];
} else {
    $username = null;
}

$task = isset($_REQUEST['task']) ? $_REQUEST['task'] : "";
if ($task === 'obtener_lms') {
    $idlms_installation = $_REQUEST['idlms_installation'];
    $result = obtenerLms($idlms_installation);
} else if ($task === 'obtener_clases_lms') {
    $idlms_installation = $_REQUEST['idlms_installation'];
    $idlmsSystem = $_REQUEST['idlmsSystem'];
    $result = obtenerClasesLms($idlms_installation, $idlmsSystem);
} else if ($task === 'obtener_clases') {
    $idlms_installation = $_REQUEST['idlms_installation'];
    $result = obtenerClases($idlms_installation);
} else if ($task === 'obtener_participantes_lms') {
    $idlms_installation = $_REQUEST['idlms_installation'];
    $idlmsSystem = $_REQUEST['idlmsSystem'];
    $idlmsCourse = $_REQUEST['idlmsCourse'];
    $result = obtenerParticipantesLms($idlms_installation, $idlmsSystem, $idlmsCourse);
} else if ($task === 'obtener_participantes') {
    $idlms_installation = $_REQUEST['idlms_installation'];
    $idclase = $_REQUEST['idclase'];
    $result = obtenerParticipantes($idlms_installation, $idclase);
} else if ($task === 'createGluePSDesign') {
    $title = $_REQUEST['title'];
    $zipUrl = $_REQUEST['zip_url'];
    $imsldType = $_REQUEST['imsld_type'];
    $result = createGluePSDesign($title, $zipUrl, $imsldType);
} else if ($task === 'createGluePSDeploy') {
    $designId = $_REQUEST['design_id'];
    $deployTitle = $_REQUEST['deploy_title'];
    $fileUrl = $_REQUEST['file_url'];
    $vleSelect = $_REQUEST['vle_select'];
    $teacherNames = $_REQUEST['teacher_names'];
    $themeSelect = $_REQUEST['theme_select'];
    $result = createGluePSDeploy($designId, $deployTitle, $fileUrl, $vleSelect, $teacherNames, $themeSelect);
} else if ($task === 'createGluePSDesignDeploy') {
    $url = $_REQUEST['url'];
    $title = $_REQUEST['title'];
    $zipUrl = $_REQUEST['zip_url'];
    $imsldType = $_REQUEST['imsld_type'];

    $deployTitle = $_REQUEST['deploy_title'];
    $fileUrl = $_REQUEST['file_url'];
    $vleSelect = $_REQUEST['vle_select'];
    $teacherNames = $_REQUEST['teacher_names'];
    $themeSelect = $_REQUEST['theme_select'];
    $user = $_REQUEST['user'];
    $pass = $_REQUEST['pass'];
    $result = createGluePSDesignDeploy($url, $title, $zipUrl, $imsldType, $deployTitle, $fileUrl, $vleSelect, $teacherNames, $themeSelect, $user, $pass);
} else {
    $result = array("ok" => false);
}

$json = new Services_JSON();
echo $json->encode($result);
?>
