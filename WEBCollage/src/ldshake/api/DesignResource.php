<?php

require_once '../manager/JSON.php';
require_once '../manager/db/db.php';
require_once '../manager/db/usersdb.php';
require_once '../manager/db/designsdb.php';
require_once '../ldshake/lib/Serializer.php';
include_once '../manager/export/imsld.php';

/**
 *  Class that represents a Design from WebInstanceCollage as a REST resource
 *  @package ldshake/api
 *  @author Javier E. Hoyos Torio 
 */
class DesignResource {

    private $method;
    private $document_id;
    private $content_type;
    private $format_value;
    private $summary;

    /**
     * Creates a DesignResource object
     * @param type $method HTTP method used in the request
     * @param type $document_id Identifier of the document 
     * @param type $content_type The type of content expected by the client as response
     */
    function __construct($method, $document_id, $content_type, $format_value = NULL, $summary = false) {
        $this->method = $method;
        $this->document_id = $document_id;
        $this->content_type = $content_type;
        $this->format_value = $format_value;
        $this->summary = $summary;
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
            if ($this->summary == true){
                return $this->get_summary();
            }
            if ($this->format_value == NULL) {
                if (strpos($this->content_type, 'application/xml') !== false) {
                    return $this->get_xml();
                } elseif (strpos($this->content_type, 'application/json') !== false) {
                    return $this->get_json();
                } else {
                    return $this->get_binary();
                }
            } else {
                if (strcmp(strtolower($this->format_value), "imsld") == 0) {
                    return $this->get_imsld();
                } else {
                    return new ResponseData(501, '', 'text/html');
                }
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


        //Get and check the sectoken associated to this document
        /* $sectoken_request = "";
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
          } */


        //Check the user is the owner of the design and it exists in the database
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

        //Get and check the sectoken associated to this document
        /* $sectoken_request = "";
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
          } */


        //Check the user is the owner of the design and it exists in the database
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

        //Get and check the sectoken associated to this document
        /* $sectoken_request = "";
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
          } */


        //Check the user is the owner of the design and it exists in the database
        if (getAccessDB($link, $username, $this->document_id, 'read')) {
            $load = loadDesignDB($link, $this->document_id);
            if ($load != false) {
                $data = $this->generate_json($load);
                if ($data != false) {
                    // Success!
                    //return new ResponseData(200, $data, 'application/force-download');
                    $upload_directory = dirname(dirname(__FILE__)) . '/tmp/';
                    $name = $this->document_id;
                    $destination = $upload_directory . time() . "_" . $name;
                    $file = fopen($destination, "w");
                    fwrite($file, $data);
                    fclose($file);
                    $file_path = $destination;
                    $filename = basename($file_path);

                    if (file_exists($file_path)) {
                        header('Content-Description: File Transfer');
                        header('Content-Type: application/octet-stream');
                        header('Content-Disposition: attachment; filename=' . $filename);
                        header('Content-Transfer-Encoding: binary');
                        header('Expires: 0');
                        header('Cache-Control: must-revalidate');
                        header('Pragma: public');
                        header('Content-Length: ' . filesize($file_path));
                        ob_clean();
                        flush();
                        readfile($file_path);
                        //We no longer need the file, so we delete it
                        unlink($file_path);
                        exit;
                    }
                       
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
     * GET request of the design as a zip file containing the design in IMSLD format
     * @return ResponseData The response containing the design as an file forcing its download and the status code. If a error is produced, the response includes instead the cause of the error
     */
    function get_imsld() {
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

        //Check the user is the owner of the design and it exists in the database
        if (getAccessDB($link, $username, $this->document_id, 'read')) {
            $load = loadDesignDB($link, $this->document_id);
            if ($load != false) {
                $design = $load['design'];
                $instance = $load['instance'];
                $data = LdshakeIMSLDInstanceExport($this->document_id, $design, $instance);
                if ($data != false) {
                    // Success!

                    $file_path = $data;
                    $filename = basename($file_path);

                    /* if (is_file($file_path)) {
                      header('Content-Type: application/force-download');
                      header('Content-Disposition: attachment; filename=' . $filename);
                      header('Content-Transfer-Encoding: binary');
                      header('Content-Length: ' . filesize($file_path));
                      readfile($file_path);
                      exit;
                      } */

                    if (file_exists($file_path)) {
                        header('Content-Description: File Transfer');
                        header('Content-Type: application/octet-stream');
                        header('Content-Disposition: attachment; filename=' . $filename);
                        header('Content-Transfer-Encoding: binary');
                        header('Expires: 0');
                        header('Cache-Control: must-revalidate');
                        header('Pragma: public');
                        header('Content-Length: ' . filesize($file_path));
                        ob_clean();
                        flush();
                        readfile($file_path);
                        //We no longer need the file, so we delete it
                        unlink($file_path);
                        exit;
                    }

                    //return new ResponseData(200, $data, 'application/force-download');
                } else {
                    return new ResponseData(500, 'Error while generating the IMSLD zip file', 'text/html');
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
     * GET request of a summary of the design as a ZIP file containing a html file
     * @return ResponseData The response containing the summary as an ZIP and the status code. If a error is produced, the response includes instead the cause of the error
     */
    function get_summary() {
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
        //Check the user is the owner of the design and it exists in the database
        if (getAccessDB($link, $username, $this->document_id, 'read')) {
            $load = loadDesignDB($link, $this->document_id);
            if ($load != false) {
                $data = $this->generate_json($load);
                if ($data != false) {
                    
                    
                    if (($zip_path = $this->generate_zip_summary($data))!==false){
                        $zip_name = basename($zip_path);

                        if (file_exists($zip_path)) {
                            header('Content-Description: File Transfer');
                            header('Content-Type: application/octet-stream');
                            header('Content-Disposition: attachment; filename=' . $zip_name);
                            header('Content-Transfer-Encoding: binary');
                            header('Expires: 0');
                            header('Cache-Control: must-revalidate');
                            header('Pragma: public');
                            header('Content-Length: ' . filesize($zip_path));
                            ob_clean();
                            flush();
                            readfile($zip_path);
                            //We no longer need the zip file, so we delete it
                            unlink($zip_path);
                            exit;
                        }
                    }
                    else{
                        return new ResponseData(500, 'Error while generating the summary as a ZIP file', 'text/html');
                    }
                    
                       
                } else {
                    return new ResponseData(500, 'Error while generating the design as a JSON', 'text/html');
                }
            } else {
                return new ResponseData(500, 'Unable to load the design from the database', 'text/html');
            }
        } else {
            return new ResponseData(401, 'The user is not authorized to access this design', 'text/html');
        }
        
        //return new ResponseData(500, 'Error while generating the summary', 'text/html');
     }

    /**
     * DELETE request that deletes a design from WebInstanceCollage
     * @return ResponseData Response containing the code with the result 
     */
    function delete() {

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

        //Get and check the sectoken associated to this document
        /* $sectoken_request = "";
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
          } */


        //Check the user is the owner of the design and it exists in the database
        if (getAccessDB($link, $username, $this->document_id, 'write')) {
            $deleted_design = deleteDesignDB($this->document_id, $username);
            if ($deleted_design == true) {
                $deleted_sectoken = deleteSectokenDocumentDB($link, $this->document_id);
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
     * Convert the design to a XML
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
     * Convert the design to a JSON
     * @param type $data The design content to convert to JSON
     * @return type The design as an JSON of false if there is an error
     */
    private function generate_json($data) {
        $json_data = json_encode($data);
        return $json_data;
    }
    
     /**
     * Generate a zip file containing a html file and its dependences with the summary of the design
     * @param type $data The design info as a JSON string
     * @return type A zip file with the summary of the design
     */
    private function generate_zip_summary($data){
    
        $html = '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">';
        $html .= '<html><head>';
        $html .= '<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">';
        $html .= '<link rel="stylesheet" type="text/css" href="css/summary.css"/>';
        $html .= '<script type="text/javascript" src="js/TableGeneratorServer.js"></script>';
        $html .= '<script type="text/javascript" src="js/AssessmentTableServer.js"></script>';
        $html .= '</head><body><script>';
        //Create the javascript data variable to be used in the javascript functions as a global var
        $html .= "var data = $data;";
        $html .= 'TableGeneratorServer.generateSummary();';
        $html .= '</script></body></html>';

        $upload_directory = dirname(dirname(__FILE__)) . '/tmp/';
        $name = $this->document_id;
        $zip_path = $upload_directory . time() . "_" . $name. ".zip";

        //Create the zip file
        $zip = new ZipArchive();
        if ($zip->open($zip_path, ZIPARCHIVE::CREATE) === true){
            //Create the directory hierarchy
            $zip->addEmptyDir("css");
            $zip->addEmptyDir("js");
            $zip->addEmptyDir("images");
            $zip->addEmptyDir("images/clfps");
            $zip->addEmptyDir("images/icons");
            //Add required javascript files
            $zip->addFile("../ldshake/js/TableGeneratorServer.js", "js/TableGeneratorServer.js");
            $zip->addFile("../ldshake/js/AssessmentTableServer.js", "js/AssessmentTableServer.js");
            //Add required css files
            $zip->addFile("../css/summary.css", "css/summary.css");

            //Add the html file                        
            $dest_sf = $upload_directory . time() . "_" . $name. "_summary.html";
            $file = fopen($dest_sf, "w");
            fwrite($file, $html);
            fclose($file);
            $zip->addFile($dest_sf, "index.html");

            //Add the student and teacher images
            $zip->addFile("../images/students.png", "images/students.png");
            $zip->addFile("../images/teacher.png", "images/teacher.png");

            //Add clfp images 
            $clfps_dir = opendir("../images/clfps"); 
            while (false !== $file = readdir($clfps_dir)){
                 $path = "../images/clfps/". $file;
                 //Check if it as image with png extension
                 if (strcmp(substr($path, strlen($path) - strlen(".png")), ".png")==0){
                     $zip->addFile($path, "images/clfps/".$file);
                 }
            }
            closedir($clfps_dir);

            //Add icons images
            $zip->addFile("../images/icons/assessed.png", "images/icons/assessed.png");
            $zip->addFile("../images/icons/assessment.png", "images/icons/assessment.png");
            $zip->addFile("../images/icons/diagnosis.png", "images/icons/diagnosis.png");
            $zip->addFile("../images/icons/formative.png", "images/icons/formative.png");
            $zip->addFile("../images/icons/summative.png", "images/icons/summative.png");

            //Close the zip file
            $zip->close();

            //Delete the source html file
            unlink($dest_sf);
            return $zip_path;
        }
        else{
            return false;
        }
    }

}

?>
