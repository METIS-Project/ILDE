<?php

/**
 * Implementación del LSM de tipo fichero
 */
class lmsFichero implements lms {

    /**
     * Fichero con el que trabaja la clase
     */
    private $fichero;

    /**
     * Constructor de la clase
     * @param mixed $parametros Parámetros proporcionados al constructor
     */
    function __construct($parametros) {
        $this->fichero = $parametros["fichero"];
    }

    /**
     * Obtiene las clases contenidas en el fichero
     * @return mixed Clases contenidas en el fichero o false si se produjo un error
     */
    function getClases() {
        $clases = array();
        @$fich = fopen($this->fichero, "r");
        $i = 0;
        if ($fich) {
            while (!feof($fich)) {
                $linea = fgets($fich);
                if ($linea[0] != '#') {

                    $pos = strpos($linea, ';');
                    $tipo = substr($linea, 0, $pos);
                    if (strlen($tipo) == 1 && $tipo[0] == 'c') {
                        $tipo = "clase";
                        $pos = strpos($linea, ';');
                        $linea = substr($linea, $pos + 1);

                        $pos = strpos($linea, ';');
                        $idClase = substr($linea, 0, $pos);

                        $pos = strpos($linea, ';');
                        $linea = substr($linea, $pos + 1);
                        $nombreClase = substr($linea, 0, strlen($linea) - 2);
                        $clases[$i]["type"] = $tipo;
                        $clases[$i]["id"] = $idClase;
                        $clases[$i]["name"] = $nombreClase;
                        $i++;
                    }
                }
            }
            fclose($fich);
            return $clases;
        }
        return false; //No se pudo abrir el fichero
    }

    /**
     * Obtiene los participantes de una de las clases contenidas en el fichero
     * @param int idclase Identificador de la clase de la que se desean obtener los participantes
     * @return mixed Participantes de la clase o false si no se encontró la clase
     */
    function getParticipantes($idclase) {
        $participantes = array();
        $encontrada = false;
        $fich = fopen($this->fichero, "r");
        if ($fich) {
            $i = 0;
            while (!feof($fich) && $encontrada == false) {
                $linea = fgets($fich);
                if ($linea[0] != '#') {
                    $pos = strpos($linea, ';');
                    $tipo = substr($linea, 0, $pos);

                    if (strlen($tipo) == 1 && $tipo[0] == 'c') {
                        $pos = strpos($linea, ';');
                        $linea = substr($linea, $pos + 1);

                        $pos = strpos($linea, ';');
                        $idClaseFich = substr($linea, 0, $pos);
                        if (strcmp($idClaseFich, $idclase) == 0) {
                            $encontrada = true;
                            $finClase = false;
                            while (!feof($fich) && $finClase == false) {
                                $linea = fgets($fich);
                                if ($linea[0] != '#') {
                                    $pos = strpos($linea, ';');
                                    $tipo = substr($linea, 0, $pos);
                                    if (strlen($tipo) == 1 && $tipo[0] == 'a') {
                                        $tipo = "student";
                                    } else {
                                        if (strlen($tipo) == 1 && $tipo[0] == 'p') {
                                            $tipo = "teacher";
                                        } else {
                                            if (strlen($tipo) == 1 && $tipo[0] == 'c') {
                                                $finClase = true;
                                            }
                                        }
                                    }
                                    if (strcmp($tipo, "student") == 0 || strcmp($tipo, "teacher") == 0) {
                                        $pos = strpos($linea, ';');
                                        $linea = substr($linea, $pos + 1);

                                        $pos = strpos($linea, ';');
                                        $idparticipante = substr($linea, 0, $pos);

                                        $pos = strpos($linea, ';');
                                        $linea = substr($linea, $pos + 1);

                                        $pos = strpos($linea, ';');
                                        $mail = substr($linea, 0, $pos);

                                        $pos = strpos($linea, ';');
                                        $linea = substr($linea, $pos + 1);

                                        $pos = strpos($linea, ';');
                                        $apellidosNombre = substr($linea, 0, $pos);

                                        $pos = strpos($linea, ';');
                                        $linea = substr($linea, $pos + 1);
                                        $dni = substr($linea, 0, strlen($linea) - 2);
                                        $participantes[$i]["type"] = $tipo;
                                        $participantes[$i]["participantId"] = $idparticipante;
                                        $participantes[$i]["mail"] = $mail;
                                        $participantes[$i]["name"] = $apellidosNombre;
                                        $participantes[$i]["dni"] = $dni;
                                        $i++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            fclose($fich);
        }
        if ($encontrada == true) {
            return $participantes;
        } else {
            return false;
        }
    }

}

?>
