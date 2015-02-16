<?php

/******************************************************************************
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
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
along with this program.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/


header('Content-Type:text/html; charset=UTF-8');
include_once ("manager/i18nParser.php");
?>
<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML//EN">
<html>
    <head>
        
       <script type="text/javascript" > 
            var currentPage = window.location.href;
            var topMostWindow = window.top.location.href;
            //Check if it is contained in an iframe
            if (currentPage != topMostWindow)
            {               
                var posInt = currentPage.lastIndexOf('?');
                //Get the parameters provided in the url
                var parameters = currentPage.substring(posInt+1);
                //Redirect to the suitable page including the parameters
                window.location.assign("main.php?"+parameters);
            }           
        </script>
        

        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="icon" type="image/png" href="images/wic2.png" /> 

        <link rel="stylesheet" type="text/css" href="../dojo/dojo/resources/dojo.css"/>
        <link rel="stylesheet" type="text/css" href="../dojo/dijit/themes/claro/claro.css"/>
        <link rel="stylesheet" type="text/css" href="css/loadingpane.css"/>
        <link rel="stylesheet" type="text/css" href="css/index.css"/>
        <link rel="stylesheet" type="text/css" href="css/general.css"/>

        <script type="text/javascript" src="../dojo/dojo/dojo.js"></script>

        <script type="text/javascript" src="js/main/dojopatch.js"></script>

        <script type="text/javascript" src="js/lib/JSON.js"></script>
        <script type="text/javascript" src="js/lib/md5-min.js"></script>
        <script type="text/javascript" src="js/lib/sha1.js"></script>

        <script type="text/javascript" src="js/main/i18n.js"></script>
        <script type="text/javascript" src="js/main/indexrequire.js"></script>
        <script type="text/javascript" src="js/main/index.js"></script>
        <script type="text/javascript" src="js/editor/Users.js"></script>
        <script type="text/javascript" src="js/editor/widgets/Icons.js"></script>
        <script type="text/javascript" src="js/editor/widgets/MenuManager.js"></script>
        <script type="text/javascript" src="js/editor/widgets/LoadingPane.js"></script>

        <script type="text/javascript" src="js/editor/layers/groups/GroupsAlerts.js"></script>
        <script type="text/javascript" src="js/editor/layers/groups/patterns/GroupPatternManager.js"></script>

        <script type="text/javascript" src="js/datamodel/Matches.js"></script>
        <script type="text/javascript" src="js/datamodel/Act.js"></script>
        <script type="text/javascript" src="js/datamodel/IDPool.js"></script>
        <script type="text/javascript" src="js/datamodel/LearningDesign.js"></script>
        <script type="text/javascript" src="js/datamodel/DesignInstance.js"></script>
        

        <?php
        echo getJavascriptMessages();
        ?>

        <title>Web Instance Collage </title>
    </head>
    <?php
    include_once ("manager/i18nParser.php");
    echo parseContentFile("pages/indexContent.html");
    ?>
</html>
