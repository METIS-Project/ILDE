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
 * Implementación del LSM de tipo Glue que accede a la información de lms, cursos y alumnos a través de las funciones del PA (Prototipo A)
 */
class lmsPA implements lms {

    /**
     * Identificador del LMS con el que trabaja la clase
     */
    private $idlmsSystem;
    /**
     * Url base de las peticiones
     */
    private $integrationmanagerurl;
    /**
     * Usuario 
     */
    private $integrationmanagerlogin;
    /**
     * Contraseña
     */
    private $integrationmanagerpassword;
    /**
     * Identificador de la herramienta
     */
    private $defaultauthoringtoolid;

    /**
     * Constructor de la clase
     * @param string $url Url de las peticiones
     * @param string $login Nombre del usuario
     * @param string $password Contraseña
     * @param int toolid Identificador de la herramienta
     */
    function __construct($url, $login, $password, $toolid) {
        $this->integrationmanagerlogin = $login;
        $this->integrationmanagerpassword = $password;
        $this->defaultauthoringtoolid = $toolid;
        $this->createUrl($url);
        $this->idlmsSystem = NULL;
    }

    /**
     * Asigna un identificador de LMS con el que trabajará el objeto
     * @param int $idlmsSystem Identificador del LMS con el que se desea que trabaje el objeto
     */
    function setIdlmsSystem($idlmsSystem) {
        $this->idlmsSystem = $idlmsSystem;
    }

    /**
     * Obtiene el identificador de LMS con el que trabaja el objeto
     * @return int Identificador del LMS con el que trabaja el objeto
     */
    function getIdlmsSystem() {
        return $this->idlmsSystem;
    }

    /**
     * Crea la URL de las peticiones a partir de la URL suministrada
     * @param string $url Crea la URL a partir de la proporcionada
     */
    private function createUrl($url) {
        if (strcmp($url, "http://delfos.tel.uva.es/") == 0 || strcmp($url, "delfos.tel.uva.es") == 0) {
            $this->integrationmanagerurl = "http://delfos.tel.uva.es/drupal/sites/all/modules/tool_manager_conference/authoringtool/";
        } else {
            $this->integrationmanagerurl = "";
        }
    }

    /**
     *  Obtiene todos los lms asociados a una instalación de glue 
     */
    function getLms() {
        $context = stream_context_create(array(
            'http' => array(
                'header' => "Authorization: Basic " . base64_encode($this->integrationmanagerlogin . ":" . $this->integrationmanagerpassword),
                'timeout' => 5 //Tiempo máximo de espera para obtener los lms
            )
                ));
        @$data = file_get_contents($this->integrationmanagerurl . "getLMSsAndCourses.php?authoringToolSystemId=" . $this->defaultauthoringtoolid, false, $context);
        if ($data == false) {
            return false;
        }
        $data = stripslashes($data);
        $json = new Services_JSON();
        $lms = array();
        $data = ($json->decode($data));
        $i = 0;
        foreach ($data as $id => $val) {
            $lms[$i]["id"] = $id;
            $lms[$i]["name"] = $val->lmsSystemName;
            $i++;
        }
        return $lms;
    }

    /**
     * Obtiene todas las clases de un lms de una instalación de glue
     * @param int idlmsSystem Identificador del LMS del que se van a obtener las clases
     */
    function getClasesLms($idlmsSystem) {
        $context = stream_context_create(array(
            'http' => array(
                'header' => "Authorization: Basic " . base64_encode($this->integrationmanagerlogin . ":" . $this->integrationmanagerpassword),
                'timeout' => 5 //Tiempo máximo de espera
            )
                ));
        $data = file_get_contents($this->integrationmanagerurl . "getLMSsAndCourses.php?authoringToolSystemId=" . $this->defaultauthoringtoolid, false, $context);
        $data = stripslashes($data);
        $json = new Services_JSON();
        $clases = array();
        $data = ($json->decode($data));
        $i = 0;
        foreach ($data as $id => $val) {
            //Si es lms indicado devuelve sus clases
            if ($id == $idlmsSystem) {
                foreach ($val->lmsCourse as $id2 => $val2) {
                    $clases[$i]["id"] = $id2;
                    $clases[$i]["name"] = $val2->lmsCourseName;
                    $i++;
                }
                return $clases;
            }
        }
        return false;
    }

    /**
     * Obtiene todos los participantes de un lms y clase de una instalación de glue
     * @param int $idlmsSystem Identificador del LMS del que se van a obtener los participantes
     * @param int $idlmsCourse Identificador de la clase de la que se van a obtener los participantes
     * 
     */
    function getParticipantesLms($idlmsSystem, $idlmsCourse) {
        $context = stream_context_create(array(
            'http' => array(
                'header' => "Authorization: Basic " . base64_encode($this->integrationmanagerlogin . ":" . $this->integrationmanagerpassword),
                'timeout' => 5 //Tiempo máximo de espera
            )
                ));
        $data = @file_get_contents($this->integrationmanagerurl . "getUsersFromLMS.php?authoringToolSystemId=" . $this->defaultauthoringtoolid . "&lmsSystemId=" . $idlmsSystem . "&lmsCourseId=" . $idlmsCourse, false, $context);
        if ($data != false) {
            $participantes = array();
            $data = explode("\n", $data);
            for ($i = 0; $i < count($data) - 1; $i++) {
                $participantes[$i]["type"] = "student";
                $participantes[$i]["participantId"] = $data[$i];
                $participantes[$i]["name"] = $data[$i];
            }
            return $participantes;
        }
        return false;
    }

    /**
     * Obtiene todas las clases del LMS de la instalación de glue con el que está trabajando el objeto
     * @return Clases del LMS elegido de la instalación de glue con la que trabaja el objeto o false si no es posible
     */
    function getClases() {
        //Para obtener las clases es necesario haber especificado previamente el LMS del que se van a obtener
        if (isset($this->idlmsSystem)) {
            return $this->getClasesLms($this->idlmsSystem);
        } else {
            return false;
        }
    }

    /**
     * Obtiene los participantes de una de las clases de un LMS de la instalación de glue con la que está trabajando el objeto
     * @param int idclase Identificador de la clase de la que se desean obtener los participantes
     * @return mixed Participantes del LMS elegido y la clase de este de la que se desean obtener los participantes
     */
    function getParticipantes($idclase) {
        //Para obtener los participantes es necesario haber especificado previamente el LMS del que se van a obtener
        if (isset($this->idlmsSystem)) {
            return $this->getParticipantesLms($this->idlmsSystem, $idclase);
        } else {
            return false;
        }
    }

}

?>
