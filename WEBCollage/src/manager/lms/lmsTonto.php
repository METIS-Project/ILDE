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
 * Implementación del LSM tonto. Utilizado solamente para pruebas
 */
class lmsTonto implements lms {

    /**
     * Clases con las que trabaja el LMS
     */
    private $clases;

    /**
     * Constructor de la clase
     */
    function __construct() {
        $this->clases = array();
        $this->clases[0]["id"] = "InfGest1";
        $this->clases[0]["name"] = "Primero en Informática de Gestión";
        $this->clases[0]["participantes"] = array();
        $this->clases[0]["participantes"][0]["type"] = "student";
        $this->clases[0]["participantes"][0]["participantId"] = "ida1";
        $this->clases[0]["participantes"][0]["dni"] = "73999888A";
        $this->clases[0]["participantes"][0]["name"] = "NombreA1 ApellidosA1";
        $this->clases[0]["participantes"][1]["type"] = "student";
        $this->clases[0]["participantes"][1]["participantId"] = "ida2";
        $this->clases[0]["participantes"][1]["dni"] = "71124333B";
        $this->clases[0]["participantes"][1]["name"] = "NombreA2 ApellidosA2";
        $this->clases[0]["participantes"][2]["type"] = "student";
        $this->clases[0]["participantes"][2]["participantId"] = "ida3";
        $this->clases[0]["participantes"][2]["dni"] = "79012456L";
        $this->clases[0]["participantes"][2]["name"] = "NombreA3 ApellidosA3";
        $this->clases[0]["participantes"][3]["type"] = "student";
        $this->clases[0]["participantes"][3]["participantId"] = "ida4";
        $this->clases[0]["participantes"][3]["dni"] = "73111222C";
        $this->clases[0]["participantes"][3]["name"] = "NombreA4 ApellidosA4";
        $this->clases[0]["participantes"][4]["type"] = "teacher";
        $this->clases[0]["participantes"][4]["participantId"] = "idp1";
        $this->clases[0]["participantes"][4]["dni"] = "70999555J";
        $this->clases[0]["participantes"][4]["name"] = "NombreP1 ApellidosP1";


        $this->clases[1]["id"] = "InfGest2";
        $this->clases[1]["name"] = "Segundo Informática Gestión";
        $this->clases[1]["participantes"] = array();
        $this->clases[1]["participantes"][0]["type"] = "student";
        $this->clases[1]["participantes"][0]["participantId"] = "ida5";
        $this->clases[1]["participantes"][0]["dni"] = "73999888A";
        $this->clases[1]["participantes"][0]["name"] = "NombreA5 ApellidosA5";
        $this->clases[1]["participantes"][1]["type"] = "student";
        $this->clases[1]["participantes"][1]["participantId"] = "ida6";
        $this->clases[1]["participantes"][1]["dni"] = "71124333B";
        $this->clases[1]["participantes"][1]["name"] = "NombreA6 ApellidosA6";
        $this->clases[1]["participantes"][2]["type"] = "student";
        $this->clases[1]["participantes"][2]["participantId"] = "ida7";
        $this->clases[1]["participantes"][2]["dni"] = "79012456L";
        $this->clases[1]["participantes"][2]["name"] = "NombreA7 ApellidosA7";
        $this->clases[1]["participantes"][3]["type"] = "student";
        $this->clases[1]["participantes"][3]["participantId"] = "ida8";
        $this->clases[1]["participantes"][3]["dni"] = "73111222A";
        $this->clases[1]["participantes"][3]["name"] = "NombreA8 ApellidosA8";
        $this->clases[1]["participantes"][4]["type"] = "teacher";
        $this->clases[1]["participantes"][4]["participantId"] = "idp2";
        $this->clases[1]["participantes"][4]["dni"] = "73111222A";
        $this->clases[1]["participantes"][4]["name"] = "NombreP2 ApellidosP2";

        $this->clases[2]["id"] = "InfGest3";
        $this->clases[2]["name"] = "Tercero Informática Gestión";
        $this->clases[2]["participantes"] = array();

        $this->clases[3]["id"] = "TestClass1";
        $this->clases[3]["name"] = "TestingClass";
        $this->clases[3]["participantes"] = array();
        $this->clases[3]["participantes"][0]["type"] = "student";
        $this->clases[3]["participantes"][0]["participantId"] = "student_a11";
        $this->clases[3]["participantes"][0]["dni"] = "0";
        $this->clases[3]["participantes"][0]["name"] = "student a11";
        $this->clases[3]["participantes"][1]["type"] = "student";
        $this->clases[3]["participantes"][1]["participantId"] = "student_a12";
        $this->clases[3]["participantes"][1]["dni"] = "0";
        $this->clases[3]["participantes"][1]["name"] = "student a12";
        $this->clases[3]["participantes"][2]["type"] = "student";
        $this->clases[3]["participantes"][2]["participantId"] = "student_a21";
        $this->clases[3]["participantes"][2]["dni"] = "0";
        $this->clases[3]["participantes"][2]["name"] = "student a21";
        $this->clases[3]["participantes"][3]["type"] = "student";
        $this->clases[3]["participantes"][3]["participantId"] = "student_a22";
        $this->clases[3]["participantes"][3]["dni"] = "0";
        $this->clases[3]["participantes"][3]["name"] = "student a22";
        $this->clases[3]["participantes"][4]["type"] = "student";
        $this->clases[3]["participantes"][4]["participantId"] = "student_a31";
        $this->clases[3]["participantes"][4]["dni"] = "0";
        $this->clases[3]["participantes"][4]["name"] = "student a31";
        $this->clases[3]["participantes"][5]["type"] = "student";
        $this->clases[3]["participantes"][5]["participantId"] = "student_a32";
        $this->clases[3]["participantes"][5]["dni"] = "0";
        $this->clases[3]["participantes"][5]["name"] = "student a32";

        $this->clases[3]["participantes"][6]["type"] = "student";
        $this->clases[3]["participantes"][6]["participantId"] = "student_b11";
        $this->clases[3]["participantes"][6]["dni"] = "0";
        $this->clases[3]["participantes"][6]["name"] = "student b11";
        $this->clases[3]["participantes"][7]["type"] = "student";
        $this->clases[3]["participantes"][7]["participantId"] = "student_b12";
        $this->clases[3]["participantes"][7]["dni"] = "0";
        $this->clases[3]["participantes"][7]["name"] = "student b12";
        $this->clases[3]["participantes"][8]["type"] = "student";
        $this->clases[3]["participantes"][8]["participantId"] = "student_b21";
        $this->clases[3]["participantes"][8]["dni"] = "0";
        $this->clases[3]["participantes"][8]["name"] = "student b21";
        $this->clases[3]["participantes"][9]["type"] = "student";
        $this->clases[3]["participantes"][9]["participantId"] = "student_b22";
        $this->clases[3]["participantes"][9]["dni"] = "0";
        $this->clases[3]["participantes"][9]["name"] = "student b22";
        $this->clases[3]["participantes"][10]["type"] = "student";
        $this->clases[3]["participantes"][10]["participantId"] = "student_b31";
        $this->clases[3]["participantes"][10]["dni"] = "0";
        $this->clases[3]["participantes"][10]["name"] = "student b31";
        $this->clases[3]["participantes"][11]["type"] = "student";
        $this->clases[3]["participantes"][11]["participantId"] = "student_b32";
        $this->clases[3]["participantes"][11]["dni"] = "0";
        $this->clases[3]["participantes"][11]["name"] = "student b32";

        $this->clases[3]["participantes"][12]["type"] = "teacher";
        $this->clases[3]["participantes"][12]["participantId"] = "educator";
        $this->clases[3]["participantes"][12]["dni"] = "0";
        $this->clases[3]["participantes"][12]["name"] = "educator WS";
    }

    /**
     * Obtiene las clases
     * @return mixed Clases obtenidas por el LMS tonto
     */
    function getClases() {
        $clases = array();
        for ($i = 0; $i < count($this->clases); $i++) {
            $clases[$i]["id"] = $this->clases[$i]["id"];
            $clases[$i]["name"] = $this->clases[$i]["name"];
        }
        return $clases;
    }

    /**
     * Obtiene los participantes de una de las clases
     * @param int idclase Identificador de la clase de la que se desean obtener los participantes
     * @return mixed Participantes de la clase del LMS tonto
     */
    function getParticipantes($idclase) {
        $i = 0;
        while ($i < count($this->clases)) {
            if (strcmp($this->clases[$i]["id"], $idclase) == 0) {
                return $this->clases[$i]["participantes"];
            }
            $i++;
        }
        return false;
    }

}

?>
