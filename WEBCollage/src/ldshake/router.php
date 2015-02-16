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

require_once 'rest.php';
require_once 'api/DesignResource.php';
require_once 'api/DesignListResource.php';

$data = RestUtils::processRequest();
if (isset($_REQUEST["_route_"]))
{ 
    $route = "/".$_REQUEST["_route_"];
    $request_vars = $data->getRequestVars();
    $route_array = explode("/", $route);
    array_shift($route_array);
    if (strcmp($route_array[count($route_array)-1],"")==0)
    {
        array_pop($route_array);
    }
    if (strcmp($route_array[0],"ldsdoc")==0)
    {
        if (count($route_array)==1)
        {
            //echo "/webcollage2/ldshake/ldsdoc";
            $method = $data->getMethod();
            $content_type = $data->getHttpAccept();
            $design_list_resource = new DesignListResource($method, $content_type);
            $response = $design_list_resource->exec();
            RestUtils::sendResponse($response->getStatus(), $response->getBody(), $response->getContentType());
        }
        if (count($route_array)==2)
        {
            if (is_numeric($route_array[1]))
            {
                //echo "/webcollage2/ldshake/ldsdoc/{document_id}";
                $method = $data->getMethod();
                $document_id = $route_array[1];
                $content_type = $data->getHttpAccept();
                $designResource = new DesignResource($method, $document_id, $content_type);
                $response = $designResource->exec();
                RestUtils::sendResponse($response->getStatus(), $response->getBody(), $response->getContentType());
            }
            else{
                $arguments = explode(".", $route_array[1]);
                if (count($arguments)==2 && is_numeric($arguments[0])){
                    $method = $data->getMethod();
                    $document_id = $arguments[0];
                    $format_value = $arguments[1];
                    $content_type = $data->getHttpAccept();
                    $designResource = new DesignResource($method, $document_id, $content_type, $format_value);
                    $response = $designResource->exec();
                    RestUtils::sendResponse($response->getStatus(), $response->getBody(), $response->getContentType());
                }
            }
        }
        if (count($route_array)==3)
        {
            if (is_numeric($route_array[1]))
            {
                if (strcmp($route_array[2],"summary")==0)
                {
                    //echo "/webcollage2/ldshake/ldsdoc/{document_id}/summary";
                    $method = $data->getMethod();
                    $document_id = $route_array[1];
                    $content_type = $data->getHttpAccept();
                    $designResource = new DesignResource($method, $document_id, $content_type, null, true);
                    $response = $designResource->exec();
                    RestUtils::sendResponse($response->getStatus(), $response->getBody(), $response->getContentType());
                }
                if (strcmp($route_array[2],"diff")==0)
                {
                    echo "/webcollage2/ldshake/ldsdoc{document_id}/diff";
                }
            }
        }
    }
    else{
        RestUtils::sendResponse(404, '', 'text/html');
    }

}
else{
    RestUtils::sendResponse(404, '', 'text/html');
}
?>
