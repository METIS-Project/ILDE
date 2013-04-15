<?php

require_once '../manager/db/db.php';
require_once '../manager/db/usersdb.php';
require_once '../manager/db/designsdb.php';
require_once '../ldshake/db/sectokendb.php';

/**
 *  Class that represents a DesignList as a REST resource
 *  @package ldshake/api
 *  @author Javier E. Hoyos Torio 
 */
class DesignListResource {

    private $method;
    private $content_type;

    const DEFAULT_TITLE = 'ldshake_default_title';

    function __construct($method, $content_type) {
        $this->method = $method;
        $this->content_type = $content_type;
    }

    /**
     * Call for the suitable method according to the REST request
     * @return ResponseData The response to the REST request
     */
    function exec() {

        if (strcmp($this->method, "post") == 0) {
            return $this->post();
        } else {
            $response = new ResponseData(501, '', 'text/html');
            return $response;
        }
    }

    /**
     * POST request to create/upload a design
     * @return ResponseData The response containing the status code. If a error is produced, the response includes the cause of the error
     */
    function post() {

        if (!isset($_SERVER['PHP_AUTH_USER'])) {
            header('WWW-Authenticate: Basic realm="My Realm"');
            header('HTTP/1.0 401 Unauthorized');
            echo 'Authentication required';
            exit;
        }

        //Authenticate the user
        $username = $_SERVER['PHP_AUTH_USER'];
        $password = $_SERVER['PHP_AUTH_PW'];
        $password = sha1(md5($password));

        $link = connectToDB();
        if (getUserDataDB($link, $username, $password) == false) {

            return new ResponseData(401, 'The credentials provided are not correct', 'text/html');
        }

        //Get and check the sectoken associated to the document being created
        if (!isset($_POST["sectoken"])) {
            return new ResponseData(400, 'The parameter \'sectoken\' for the design is missing', 'text/html');
        } elseif (strlen($_POST["sectoken"]) == 0) {
            return new ResponseData(400, 'The parameter \'sectoken\' can not be an empty string', 'text/html');
        } else {
            $sectoken_request = $_POST["sectoken"];
        }

        $link = connectToDB();
        $row = loadSectokenDB($link, $sectoken_request);
        if ($row != false) {
            return new ResponseData(400, 'The identifier provided in the parameter \'sectoken\' is not unique. There is already a design with that sectoken associated', 'text/html');
        }

        //Optional parameter name containing the title for the design
        if (isset($_POST["name"])) {
            $title = $_POST["name"];
        } else {
            $title = self::DEFAULT_TITLE;
        }

        //Check if a design in webcollage format has been provided
        if (!isset($_FILES['document'])) {
            //Create the design from scratch
            return $this->post_create($sectoken_request, $title, $username);
        } elseif (!isset($_FILES['vle_info'])) {
            //Create the design from an existing design file
            return $this->post_upload($sectoken_request, $title, $username);
        } else {
            //Create the design from an existing design file and the vle, course, participant info
            return $this->post_upload_vle($sectoken_request, $title, $username);
        }
        return $response;
    }

    /**
     * Creation of a new design from scratch
     * @param string $sectoken The security token associated to the design
     * @param string $title The title of the document
     * @param string $username The name of the user owner of the document
     * @return ResponseData Response containing the code with the result
     */
    function post_create($sectoken, $title, $username) {
        $upload_directory = dirname(dirname(__FILE__)) . '/designs/';
        $destination = $upload_directory . "empty_design.json";
        $json_content = file_get_contents($destination);
        if ($json_content == false) {
            return new ResponseData(500, "Error while trying to get the content of the file containing the default design", 'text/html');
        } else {
            //Convert the json string into a php object
            $data_obj = json_decode($json_content);
            if ($data_obj == false) {
                return new ResponseData(500, 'Error while generating the default design as a PHP object', 'text/html');
            } else {
                //Set the proper design title
                $data_obj->design->title = $title;
                $design_json = json_encode($data_obj->design);
                $instance_json = json_encode($data_obj->instance);

                $link = connectToDB();
                $docid = createDesignDB($link, $username, $title, $design_json, $instance_json, null);
                if ($docid == false) {
                    return new ResponseData(500, 'Unable to insert the design in the database', 'text/html');
                } else {
                    $url_resource = $this->getDesignUrl($docid);
                    if (createSectokenDB($link, $sectoken, $docid) != false) {
                        //Success
                        return new ResponseData(201, $url_resource, 'text/html');
                    } else {
                        return new ResponseData(500, "Error while trying to save the sectoken for this document", 'text/html');
                    }
                }
            }
        }
    }

    /**
     * Creation of a new design from a file containing an existing one
     * @param string $sectoken The security token associated to the design
     * @param string $title The title of the document
     * @param string $username The name of the user owner of the document
     * @return ResponseData Response containing the code with the result
     */
    function post_upload($sectoken, $title, $username) {
        //UPDATE of an existing design in LD-Shake but not in WebInstanceCollage
        $upload_directory = dirname(dirname(__FILE__)) . '/tmp/';
        $name = basename($_FILES['document']['name']);
        $destination = $upload_directory . time() . "_" . $name;
        if (move_uploaded_file($_FILES['document']['tmp_name'], $destination) == false) {
            return new ResponseData(500, "Error while trying to store the file containing the design to upload", 'text/html');
        } else {
            //Convert the json string into a php object
            $json_content = file_get_contents($destination);
            if ($json_content == false) {
                return new ResponseData(500, "Error while trying to get the content of the file containing the design to upload", 'text/html');
            } else {
                //Convert the json string into a php object
                $data_obj = json_decode($json_content);
                if ($data_obj == false) {
                    return new ResponseData(500, 'Error while generating the default design as a PHP object', 'text/html');
                } else {
                    //Set the proper design title
                    $data_obj->design->title = $title;
                    $design_json = json_encode($data_obj->design);
                    $instance_json = json_encode($data_obj->instance);
                    $todoDesign_json = json_encode($data_obj->todoDesign);
                    if (strcmp($todoDesign_json, "null") == 0) {
                        $todoDesign_json = "";
                    }
                    $todoInstance_json = json_encode($data_obj->todoInstance);
                    if (strcmp($todoInstance_json, "null") == 0) {
                        $todoInstance_json = "";
                    }

                    $link = connectToDB();
                    $docid = createDesignDB($link, $username, $title, $design_json, $instance_json, null);
                    if ($docid == false) {
                        return new ResponseData(500, 'Unable to insert the design in the database', 'text/html');
                    } else {
                        if (saveDesignDB($link, $docid, $design_json, $instance_json, $todoDesign_json, $todoInstance_json, "") == false) {
                            return new ResponseData(500, 'Unable to save the design in the database', 'text/html');
                        } else {
                            $url_resource = $this->getDesignUrl($docid);
                            if (createSectokenDB($link, $sectoken, $docid) != false) {
                                //Success
                                return new ResponseData(201, $url_resource, 'text/html');
                            } else {
                                return new ResponseData(500, "Error while trying to save the sectoken for this document", 'text/html');
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Creation of a new design from a file containing an existing one and another file containing the vle, course, participant information
     * @param string $sectoken The security token associated to the design
     * @param string $title The title of the document
     * @param string $username The name of the user owner of the document
     * @return ResponseData Response containing the code with the result
     */
    function post_upload_vle($sectoken, $title, $username) {
        //UPDATE of an existing design in LD-Shake but not in WebInstanceCollage
        //Store the document file 
        $upload_directory = dirname(dirname(__FILE__)) . '/tmp/';
        $name_document = basename($_FILES['document']['name']);
        $destination_document = $upload_directory . time() . "_document_" . $name_document;
        if (move_uploaded_file($_FILES['document']['tmp_name'], $destination_document) == false) {
            return new ResponseData(500, "Error while trying to store the file containing the design to upload", 'text/html');
        }

        //Store the vle_info file
        $name_vle_info = basename($_FILES['vle_info']['name']);
        $destination_vle_info = $upload_directory . time() . "_vle_" . $name_vle_info;
        if (move_uploaded_file($_FILES['vle_info']['tmp_name'], $destination_vle_info) == false) {
            return new ResponseData(500, "Error while trying to store the file containing the vle, course and participant info", 'text/html');
        }

        //Convert the json string into a php object
        $json_content_document = file_get_contents($destination_document);
        if ($json_content_document == false) {
            return new ResponseData(500, "Error while trying to get the content of the file containing the design to upload", 'text/html');
        }

        //Convert the json string into a php object
        $json_content_vle_info = file_get_contents($destination_vle_info);
        if ($json_content_vle_info == false) {
            return new ResponseData(500, "Error while trying to get the content of the file containing the vle, course and participant info", 'text/html');
        }

        //Convert the json string into a php object
        $document_obj = json_decode($json_content_document);
        if ($document_obj == false) {
            return new ResponseData(500, 'Error while generating the default design as a PHP object', 'text/html');
        }

        //Convert the json string into a php object
        $vle_info_obj = json_decode($json_content_vle_info);
        if ($vle_info_obj == false) {
            return new ResponseData(500, 'Error while generating the default VLE info as a PHP object', 'text/html');
        }

        //Set the proper design title
        $document_obj->design->title = $title;
        $design_json = json_encode($document_obj->design);

        if (isset($vle_info_obj->learningEnvironment->id)) {
            $document_obj->instance->lmsObj->id = $vle_info_obj->learningEnvironment->id;
        }
        if (isset($vle_info_obj->learningEnvironment->name)) {
            $document_obj->instance->lmsObj->name = $vle_info_obj->learningEnvironment->name;
        }

        if (isset($vle_info_obj->course->id)) {
            $document_obj->instance->classObj->id = $vle_info_obj->course->id;
        }
        if (isset($vle_info_obj->course->name)) {
            $document_obj->instance->classObj->name = $vle_info_obj->course->name;
        }

        foreach ($vle_info_obj->course->participants as $p) {
            $participant = new stdClass();
            $participant->participantId = $p->id;
            $participant->name = $p->name;
            if (isset($p->isStaff) && strcmp($p->isStaff, "") != 0) {
                $participant->participantType = "teacher";
            } else {
                $participant->participantType = "student";
            }
            array_push($document_obj->instance->participants, $participant);
        }

        //Add the internal tools to the instance information
        foreach ($vle_info_obj->learningEnvironment->internalTools as $toolId => $toolName){
            $tool = new stdClass();
            $tool->toolId = $toolId;
            $tool->toolName = $toolName;
            array_push($document_obj->instance->vleTools, $tool);
        }
        
        //Add the external tools to the instance information
        foreach ($vle_info_obj->learningEnvironment->externalTools as $toolId => $toolName){
            $tool = new stdClass();
            $tool->toolId = $toolId;
            $tool->toolName = $toolName;
            array_push($document_obj->instance->vleTools, $tool);
        }

        $instance_json = json_encode($document_obj->instance);

        $todoDesign_json = json_encode($document_obj->todoDesign);
        if (strcmp($todoDesign_json, "null") == 0) {
            $todoDesign_json = "";
        }
        $todoInstance_json = json_encode($document_obj->todoInstance);
        if (strcmp($todoInstance_json, "null") == 0) {
            $todoInstance_json = "";
        }

        $link = connectToDB();
        $docid = createDesignDB($link, $username, $title, $design_json, $instance_json, null);
        if ($docid == false) {
            return new ResponseData(500, 'Unable to insert the design in the database', 'text/html');
        } else {
            if (saveDesignDB($link, $docid, $design_json, $instance_json, $todoDesign_json, $todoInstance_json, "") == false) {
                return new ResponseData(500, 'Unable to save the design in the database', 'text/html');
            } else {
                $url_resource = $this->getDesignUrl($docid);
                if (createSectokenDB($link, $sectoken, $docid) != false) {
                    //Success
                    return new ResponseData(201, $url_resource, 'text/html');
                } else {
                    return new ResponseData(500, "Error while trying to save the sectoken for this document", 'text/html');
                }
            }
        }
    }

    /**
     * Gets the design url including the design identifier
     * @param integer $docid Identifier of the design
     * @return string Resource design URL
     */
    function getDesignUrl($docid) {
        $url = "http://" . $_SERVER['HTTP_HOST'] . ":" . $_SERVER['SERVER_PORT'] . $_SERVER['REQUEST_URI'];
        $url_resource = $url . "/" . $docid;
        return $url_resource;
    }

}

?>
