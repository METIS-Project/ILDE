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
 * Implementación del LSM de tipo GLUEPS que accede a la información de lms, cursos y alumnos
 */
class lmsGluePS implements lms {

    /**
     *
     * Url base de las peticiones
     */
    private $urlGluePS;
    private $login;
    private $password;
    private $idlmsSystem;
    private $instType = 'IMS LD';

    function __construct($url, $login, $password) {
        $this->createUrl($url);
        $this->login = $login;
        $this->password = $password;
    }

    /**
     *  Obtiene todos los lms asociados a una instalación de GLUEPS
     */
    function getLms() {
        //cURL handler creation
        $ch = curl_init();
        
        //Autenticación
        curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC); 
        curl_setopt($ch, CURLOPT_USERPWD, $this->login.":".$this->password);
        
        // HTTP method
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "GET");
        //URL
        curl_setopt($ch, CURLOPT_URL, $this->urlGluePS);
        // set the handler for delivering answers in strings, instead of being directly printed on page
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        // timeout
        curl_setopt($ch, CURLOPT_TIMEOUT, 5);
        // perform the HTTP request
        $xml = curl_exec($ch);
        // get answer HTTP code
        $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        // get cURL error code
        $curl_errno = curl_errno($ch);
        if (!$curl_errno && $status == 200) {
            $dom = new DOMDocument();
            $dom->loadXML($xml);
            $entries = ($dom->getElementsByTagName('entry'));
            $lms = array();
            $i = 0;
            foreach ($entries as $entry) {
                $lms[$i]["id"] = $entry->getElementsByTagName('id')->item(0)->nodeValue;
                $lms[$i]["name"] = $entry->getElementsByTagName('title')->item(0)->nodeValue;
                $lms[$i]["type"] = $entry->getElementsByTagName('type')->item(0)->nodeValue;
                $i++;
            }
            curl_close($ch);
            return $lms;
        } else {
            curl_close($ch);
            return false;
        }
    }

    /**
     * Crea la URL de las peticiones a partir de la URL suministrada
     * @param string $url Crea la URL a partir de la proporcionada
     */
    private function createUrl($url) {
        if (strlen($url) > 0) {
            $this->urlGluePS = $url . "/learningEnvironments";
        } else {
            $this->urlGluePS = "";
        }
    }

    /**
     * Asigna un identificador de LMS con el que trabajará el objeto
     * @param int $idlmsSystem Identificador del LMS con el que se desea que trabaje el objeto
     */
    function setIdlmsSystem($idlmsSystem) {
        $this->idlmsSystem = $idlmsSystem;
    }

    function getClasesLms($idlmsSystem) {
        //cURL handler creation
        $ch = curl_init();
        
        //Autenticación
        curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC); 
        curl_setopt($ch, CURLOPT_USERPWD, $this->login.":".$this->password);
        
        // HTTP method
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "GET");
        //URL
        curl_setopt($ch, CURLOPT_URL, $idlmsSystem);
        // set the handler for delivering answers in strings, instead of being directly printed on page
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        // timeout
        curl_setopt($ch, CURLOPT_TIMEOUT, 5);
        // perform the HTTP request
        $xml = curl_exec($ch);
        // get answer HTTP code
        $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        // get cURL error code
        $curl_errno = curl_errno($ch);

        if (!$curl_errno && $status == 200) {
            $dom = new DOMDocument();
            $dom->loadXML($xml);
            $clases = array();
            $courses = $dom->getElementsByTagName('courses')->item(0);
            $courses = $courses->getElementsByTagName('entry');
            $i = 0;
            foreach ($courses as $course) {
                $clases[$i]["id"] = $course->getElementsByTagName('key')->item(0)->nodeValue;
                $clases[$i]["name"] = $course->getElementsByTagName('value')->item(0)->nodeValue;
                $i++;
            }
            curl_close($ch);
            return $clases;
        } else {
            curl_close($ch);
            return false;
        }
    }

    /**
     * Obtiene todas las clases del LMS de GluePS con el que está trabajando el objeto
     * @return Clases del LMS elegido de la instalación de GluePS con la que trabaja el objeto o false si no es posible
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
     * Obtiene los participantes de una de las clases de un LMS de la instalación de GLUEPS con la que está trabajando el objeto
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

    /**
     * Obtiene todos los participantes de un lms y clase de una instalación de glue
     * @param int $idlmsSystem Identificador del LMS del que se van a obtener los participantes
     * @param int $idlmsCourse Identificador de la clase de la que se van a obtener los participantes
     * 
     */
    function getParticipantesLms($idlmsSystem, $idlmsCourse) {
        //cURL handler creation
        $ch = curl_init();
        
        //Autenticación
        curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC); 
        curl_setopt($ch, CURLOPT_USERPWD, $this->login.":".$this->password);
                
        // HTTP method
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "GET");
        //URL
        curl_setopt($ch, CURLOPT_URL, $idlmsCourse);
        // set the handler for delivering answers in strings, instead of being directly printed on page
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        // timeout
        curl_setopt($ch, CURLOPT_TIMEOUT, 5);
        // perform the HTTP request
        $xml = curl_exec($ch);
        // get answer HTTP code
        $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        // get cURL error code
        $curl_errno = curl_errno($ch);

        if (!$curl_errno && $status == 200) {
            $dom = new DOMDocument();
            $dom->loadXML($xml);
            $participantes = array();
            $course = $dom->getElementsByTagName('course')->item(0);
            if ($course->getElementsByTagName('participants')->length > 0) {
                $participants = $course->getElementsByTagName('participants')->item(0);
                $participants = $participants->getElementsByTagName('entry');
                $i = 0;
                foreach ($participants as $participant) {
                    $participantes[$i]["participantId"] = $participant->getElementsByTagName('key')->item(0)->nodeValue; //$participant->getElementsByTagName('value')->item(0)->getElementsByTagName('name')->item(0)->nodeValue;
                    $participantes[$i]["name"] = $participant->getElementsByTagName('value')->item(0)->getElementsByTagName('name')->item(0)->nodeValue;
                    if (strcmp($participant->getElementsByTagName('value')->item(0)->getElementsByTagName('staff')->item(0)->nodeValue, "false") == 0) {
                        $participantes[$i]["type"] = "student";
                    } else {
                        $participantes[$i]["type"] = "teacher";
                    }
                    $i++;
                }
            }
            curl_close($ch);
            return $participantes; 
        } else {
            curl_close($ch);
            return false;
        }
    }

    function createDesign($url, $title, $zipUrl, $imslType, $user, $pass) {
        //cURL handler creation
        $ch = curl_init();

        //Autenticación
        curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC); 
        curl_setopt($ch, CURLOPT_USERPWD, $user.":".$pass);
        
        //URL
        curl_setopt($ch, CURLOPT_URL, $url);

        // HTTP method
        curl_setopt($ch, CURLOPT_POST, TRUE);

        $cwd = getcwd();

        $fullPath = $cwd . "/../../" . $zipUrl;

        $data = array('NewDTitle' => $title, 'NewDImportDesign' => '@' . $fullPath, 'imsldType' => $imslType);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

        // set the handler for delivering answers in strings, instead of being directly printed on page
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);

        // timeout
        curl_setopt($ch, CURLOPT_TIMEOUT, 15);

        // perform the HTTP request
        $xml = curl_exec($ch);

        // get answer HTTP code
        $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        // get cURL error code
        $curl_errno = curl_errno($ch);
        curl_close($ch);
        if (!$curl_errno && $status == 200) {
            $dom = new DOMDocument();
            $dom->loadXML($xml);
            $design = $dom->getElementsByTagName('design')->item(0);
            $idDesign = $design->getAttribute('id');
            return $idDesign;
        }
        return false;
    }

    function createDeploy($url, $deployTitle, $fileUrl, $vleSelect, $courseSelect, $teacherNames, $themeSelect, $user, $pass) {
        //cURL handler creation
        $ch = curl_init();
        
        //Autenticación
        curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC); 
        curl_setopt($ch, CURLOPT_USERPWD, $user.":".$pass); 
                
        //URL
        curl_setopt($ch, CURLOPT_URL, $url);

        // HTTP method
        curl_setopt($ch, CURLOPT_POST, TRUE);

        $cwd = getcwd();

        $fullPath = $cwd . "/../../" . $fileUrl;

        $data = array('NewDeployTitleName' => $deployTitle, 'archiveWic' => '@' . $fullPath, 'vleSelect' => $vleSelect, 'courseSelect' => $courseSelect,
            'newDeployTextBoxTeacher' => $teacherNames, 'instType' => $this->instType, 'temaSelect' => $themeSelect);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

        // set the handler for delivering answers in strings, instead of being directly printed on page
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);

        // timeout
        curl_setopt($ch, CURLOPT_TIMEOUT, 15);

        // perform the HTTP request
        $xml = curl_exec($ch);

        // get answer HTTP code
        $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        // get cURL error code
        $curl_errno = curl_errno($ch);
        curl_close($ch);
        if (!$curl_errno && $status == 200) {
            $dom = new DOMDocument();
            $dom->loadXML($xml);
            $design = $dom->getElementsByTagName('deploy')->item(0);
            $idDeploy = $design->getAttribute('id');
            return $idDeploy;
        }
        return false;
    }

}

?>
