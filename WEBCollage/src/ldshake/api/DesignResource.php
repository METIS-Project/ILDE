<?php

require_once '../manager/JSON.php';
require_once '../manager/db/db.php';
require_once '../manager/db/designsdb.php';
require_once '../ldshake/lib/Serializer.php';

/**
 *  Class that represents a Design from WebInstanceCollage as a REST resource
 *  @package ldshake/api
 *  @author Javier E. Hoyos Torio 
 */
class DesignResource {

    private $method;
    private $document_id;
    private $content_type;

    /**
     * Creates a DesignResource object
     * @param type $method HTTP method used in the request
     * @param type $document_id Identifier of the document 
     * @param type $content_type The type of content expected by the client as response
     */
    function __construct($method, $document_id, $content_type) {
        $this->method = $method;
        $this->document_id = $document_id;
        $this->content_type = $content_type;
    }

    /**
     * Call for the suitable method according to the REST request
     * @return ResponseData The response to the REST request
     */
    function exec() {
        //Check that exits a design with that identifier
        $link = connectToDB();
        $load = loadDesignDB($link, $this->document_id);
        if ($load == false) {
            return new ResponseData(404, 'The resource does not exists', 'text/html');
        }

        if (strcmp($this->method, "get") == 0) {
            if (strcmp($this->content_type, 'xml') == 0) {
                return $this->get_xml();
            } elseif (strcmp($this->content_type, 'json') == 0) {
                return $this->get_json();
            } else {
                return $this->get_binary();
            }
        } elseif (strcmp($this->method, "delete") == 0) {
            return $this->delete();           
        } else {
            return new ResponseData(501, '', 'text/html');
        }
    }

    /**
     * GET request of the design as a XML file
     * @return ResponseData The response containing the design as an XML and the status code. If a error is produced, the response includes instead the cause of the error
     */
    function get_xml() {
        //Get and check the sectoken associated to this document
        $sectoken_request = "";
        foreach (getallheaders() as $nombre => $valor) {
            if (strcmp($nombre, 'Authorization') == 0) {
                //the header containing the sectoken must be defined as 'Authorization: Bearer {sectoken}'
                if (strlen($valor) > strlen("Bearer") && strcmp(substr($valor, 0, strlen("Bearer")), "Bearer") == 0) {
                    $sectoken_request = substr($valor, strlen("Bearer") + 1);
                    break;
                } else {
                    return new ResponseData(400, 'The header Authorization does not follow the schema \'Authorization: Bearer {sectoken}\'', 'text/html');
                }
            }
        }
        if (strlen($sectoken_request) == 0) {
            return new ResponseData(400, 'The header \'Authorization: Bearer {sectoken}\' with the sectoken for this design is missing', 'text/html');
        }


        //Check the sectoken provided matches the one stored in the database for this design
        $link = connectToDB();
        $row = loadSectokenDB($link, $sectoken_request);
        if ($row == false) {
            return new ResponseData(500, 'The sectoken provided does not have a design associated', 'text/html');
        } else {
            $docid_database = $row['docid'];
            if (strcmp($this->document_id, $docid_database) != 0) {
                return new ResponseData(401, 'The sectoken provided for this design is not correct', 'text/html');
            }
        }


        //Check the user is the owner of the design and it exists in the database
        $username = 'ldshake_default_user';
        if (getAccessDB($link, $username, $this->document_id, 'read')) {
            $load = loadDesignDB($link, $this->document_id);
            if ($load != false) {
                $data = $this->generate_xml($load);
                if ($data != false) {
                    // Success!
                    return new ResponseData(200, $data, 'application/xml');
                } else {
                    return new ResponseData(500, 'Error while generating the design as an XML', 'text/html');
                }
            } else {
                return new ResponseData(500, 'Unable to load the design from the database', 'text/html');
            }
        } else {
            return new ResponseData(401, 'The user is not authorized to access this design', 'text/html');
        }

        return $response;
    }

    /**
     * GET request of the design as a JSON file
     * @return ResponseData The response containing the design as an JSON and the status code. If a error is produced, the response includes instead the cause of the error
     */
    function get_json() {

        //Get and check the sectoken associated to this document
        $sectoken_request = "";
        foreach (getallheaders() as $nombre => $valor) {
            if (strcmp($nombre, 'Authorization') == 0) {
                //the header containing the sectoken must be defined as 'Authorization: Bearer {sectoken}'
                if (strlen($valor) > strlen("Bearer") && strcmp(substr($valor, 0, strlen("Bearer")), "Bearer") == 0) {
                    $sectoken_request = substr($valor, strlen("Bearer") + 1);
                    break;
                } else {
                    return new ResponseData(400, 'The header Authorization does not follow the schema \'Authorization: Bearer {sectoken}\'', 'text/html');
                }
            }
        }
        if (strlen($sectoken_request) == 0) {
            return new ResponseData(400, 'The header \'Authorization: Bearer {sectoken}\' with the sectoken for this design is missing', 'text/html');
        }


        //Check the sectoken provided matches the one stored in the database for this design
        $link = connectToDB();
        $row = loadSectokenDB($link, $sectoken_request);
        if ($row == false) {
            return new ResponseData(500, 'The sectoken provided does not have a design associated', 'text/html');
        } else {
            $docid_database = $row['docid'];
            if (strcmp($this->document_id, $docid_database) != 0) {
                return new ResponseData(401, 'The sectoken provided for this design is not correct', 'text/html');
            }
        }


        //Check the user is the owner of the design and it exists in the database
        $username = 'ldshake_default_user';
        if (getAccessDB($link, $username, $this->document_id, 'read')) {
            $load = loadDesignDB($link, $this->document_id);
            if ($load != false) {
                $data = $this->generate_json($load);
                if ($data != false) {
                    // Success!
                    return new ResponseData(200, $data, 'application/json');
                } else {
                    return new ResponseData(500, 'Error while generating the design as a JSON', 'text/html');
                }
            } else {
                return new ResponseData(500, 'Unable to load the design from the database', 'text/html');
            }
        } else {
            return new ResponseData(401, 'The user is not authorized to access this design', 'text/html');
        }

        return $response;
    }

    /**
     * GET request of the design as a binary file
     * @return ResponseData The response containing the design as an file forcing its download and the status code. If a error is produced, the response includes instead the cause of the error
     */
    function get_binary() {

        //Get and check the sectoken associated to this document
        $sectoken_request = "";
        foreach (getallheaders() as $nombre => $valor) {
            if (strcmp($nombre, 'Authorization') == 0) {
                //the header containing the sectoken must be defined as 'Authorization: Bearer {sectoken}'
                if (strlen($valor) > strlen("Bearer") && strcmp(substr($valor, 0, strlen("Bearer")), "Bearer") == 0) {
                    $sectoken_request = substr($valor, strlen("Bearer") + 1);
                    break;
                } else {
                    return new ResponseData(400, 'The header Authorization does not follow the schema \'Authorization: Bearer {sectoken}\'', 'text/html');
                }
            }
        }
        if (strlen($sectoken_request) == 0) {
            return new ResponseData(400, 'The header \'Authorization: Bearer {sectoken}\' with the sectoken for this design is missing', 'text/html');
        }


        //Check the sectoken provided matches the one stored in the database for this design
        $link = connectToDB();
        $row = loadSectokenDB($link, $sectoken_request);
        if ($row == false) {
            return new ResponseData(500, 'The sectoken provided does not have a design associated', 'text/html');
        } else {
            $docid_database = $row['docid'];
            if (strcmp($this->document_id, $docid_database) != 0) {
                return new ResponseData(401, 'The sectoken provided for this design is not correct', 'text/html');
            }
        }


        //Check the user is the owner of the design and it exists in the database
        $username = 'ldshake_default_user';
        if (getAccessDB($link, $username, $this->document_id, 'read')) {
            $load = loadDesignDB($link, $this->document_id);
            if ($load != false) {
                $data = $this->generate_json($load);
                if ($data != false) {
                    // Success!
                    return new ResponseData(200, $data, 'application/force-download');
                } else {
                    return new ResponseData(500, 'Error while generating the design as a JSON', 'text/html');
                }
            } else {
                return new ResponseData(500, 'Unable to load the design from the database', 'text/html');
            }
        } else {
            return new ResponseData(401, 'The user is not authorized to access this design', 'text/html');
        }

        return $response;
    }

    /**
     * DELETE request that deletes a design from WebInstanceCollage
     * @return ResponseData Response containing the code with the result 
     */
    function delete() {

        //Get and check the sectoken associated to this document
        $sectoken_request = "";
        foreach (getallheaders() as $nombre => $valor) {
            if (strcmp($nombre, 'Authorization') == 0) {
                //the header containing the sectoken must be defined as 'Authorization: Bearer {sectoken}'
                if (strlen($valor) > strlen("Bearer") && strcmp(substr($valor, 0, strlen("Bearer")), "Bearer") == 0) {
                    $sectoken_request = substr($valor, strlen("Bearer") + 1);
                    break;
                } else {
                    return new ResponseData(400, 'The header Authorization does not follow the schema \'Authorization: Bearer {sectoken}\'', 'text/html');
                }
            }
        }
        if (strlen($sectoken_request) == 0) {
            return new ResponseData(400, 'The header \'Authorization: Bearer {sectoken}\' with the sectoken for this design is missing', 'text/html');
        }


        //Check the sectoken provided matches the one stored in the database for this design
        $link = connectToDB();
        $row = loadSectokenDB($link, $sectoken_request);
        if ($row == false) {
            return new ResponseData(500, 'The sectoken provided does not have a design associated', 'text/html');
        } else {
            $docid_database = $row['docid'];
            if (strcmp($this->document_id, $docid_database) != 0) {
                return new ResponseData(401, 'The sectoken provided for this design is not correct', 'text/html');
            }
        }


        //Check the user is the owner of the design and it exists in the database
        $username = 'ldshake_default_user';
        if (getAccessDB($link, $username, $this->document_id, 'write')) {
            $deleted_design = deleteDesignDB($this->document_id, $username);
            if ($deleted_design == true) {
                $deleted_sectoken = deleteSectokenDB($link, $sectoken_request);
                if ($deleted_sectoken == true) {
                    //success
                    return new ResponseData(200, '', 'text/html');
                } else {
                    return new ResponseData(500, 'Unable to delete the sectoken from the database', 'text/html');
                }
            } else {
                return new ResponseData(500, 'Unable to delete the design from the database', 'text/html');
            }
        } else {
            return new ResponseData(401, 'The user is not authorized to delete this design', 'text/html');
        }

        return $response;
    }
    
     /**
     * Converts the design to a XML
     * @param type $data The design content to convert to XML
     * @return type The design as an XML of false if there is an error
     */
    private function generate_xml($data) {
        $options = array(
            'addDecl' => TRUE,
            'encoding' => 'UTF-8',
            'indent' => '  ',
            'rootName' => 'wic',
            'mode' => 'simplexml'
        );
        $serializer = new XML_Serializer($options);
        if ($serializer->serialize($data)) {
            $xml_data = $serializer->getSerializedData();
            return $xml_data;
        }
        return false;
    }

    /**
     * Converts the design to a JSON
     * @param type $data The design content to convert to JSON
     * @return type The design as an JSON of false if there is an error
     */
    private function generate_json($data) {
        $json_data = json_encode($data);
        return $json_data;
    }

}

?>
