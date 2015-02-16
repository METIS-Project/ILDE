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
 * Interfaz que deben cumplir los tipos de LMS implementados
 */
interface lms {

    /**
     * Obtiene las clases del LMS
     * @return mixed Clases del LMS
     */
    public function getClases();

    /**
     * Obtiene los participantes de una clase
     * @param int $idclase Identificador de la clase de la que se desean obtener los participantes
     * @return mixed Participantes de la clase
     */
    public function getParticipantes($idclase);
}

?>
