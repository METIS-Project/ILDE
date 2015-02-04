<?php

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
