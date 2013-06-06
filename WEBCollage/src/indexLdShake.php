<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML//EN">
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <script type="text/javascript" src="js/lib/JSON.js"></script>
        <title>Web Instance Collage </title>
        <script type="text/javascript" >
            
            //associate to the message event the function getEventMessage
            window.addEventListener('message', getEventMessage, false);
            
            function getEventMessage(event) {
                var  ldshakeFrameOrigin = ldshake_sectoken;
                if(event.origin !== ldshakeFrameOrigin){
                    return;
                }else{
                    var data = JSON.decode(event.data);
                    //Check the event type
                    if (data.type == "ldshake_sectoken"){
                        var sectoken = data.data;
                        var currentPage = window.location.href;
                        var posInt = currentPage.lastIndexOf('?');
                        //Get the parameters provided in the url
                        var parameters = currentPage.substring(posInt+1);
                        parameters = parameters + "&sectoken=" + sectoken;
                        //Redirect to the suitable page including the parameters
                        window.location.assign("main.php?"+parameters);
                    }
                    else{
                        return;
                    }
                }
            }
            
            /*
             * Post a message to the LdShake which contains this Web Collage inside a iframe in order to notify that Web Collage is ready to get message events
             */
            function postMessageIsReady(){
                var ready = {type: "ldshake_editor_ready", 
                             data: true};
                //parent.postMessage(JSON.encode(ready), "http://localhost");
                parent.postMessage(JSON.encode(ready), ldshake_sectoken);
            }
            
        </script>
    </head>
    <body>
        <?php
        $document_id = $_GET['document_id'];
        require_once 'manager/JSON.php';
        require_once 'manager/db/db.php';
        require_once 'manager/db/designsdb.php';
        $ldshake_frame_origin = 'http://localhost';
        $link = connectToDB();
        $data= loadDesignDB($link, $document_id);
        if ($data!=false){
            $instance = $data['instance'];
            if (isset($instance->ldshakeFrameOrigin)){
                //Get the value provided in the POST request when the design was created/updated
                $ldshake_frame_origin = $instance->ldshakeFrameOrigin;
            }
        }
        echo "<script type=\"text/javascript\">var ldshake_sectoken='$ldshake_frame_origin';";
        echo "postMessageIsReady()";
        echo "</script>";
        ?>
        
    </body>
</html>
