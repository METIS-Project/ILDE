<?php

include_once("rand.php");
include_once("imsldRoles.php");
include_once("imsldActivities.php");
include_once("instanceExport.php");

class IMSLD {
    var $counter;
    var $folder;

    var $general;
    var $roles;
    var $activities;
    var $environments;
    var $acts;
    var $resources;
    var $restable;
	var $roletitles;

    function IMSLD($folder) {
        $this->counter = 0;
        $this->folder = $folder;

        $this->general = "";
        $this->resources = "";
        $this->restable = null;
        $this->roles = "";
        $this->activities = "";
        $this->environments = "";
        $this->acts = "";
		$this->roletitles = array();
    }

    function getResourceId() {
        $this->counter ++;
        return "RES_{$this->counter}";
    }

    function formatId($id) {
        return "LD_$id";
    }

    function getNewId() {
        $this->counter ++;
        return "LDN_{$this->counter}";
    }
	
	function recordRoleTitle($roleid, $roletitle) {
		$this->roletitles[$roleid] = $roletitle;
	}
	
	function getRoleTitle($roleid) {
		return $this->roletitles[$roleid];
	}
}

function IMSLDCreateDesign($folder, $design, $uolid) {
    $imsld = new IMSLD($folder);

    IMSLDSetTitle($imsld, $design);
    IMSLDCreateResources($imsld, $design);
    IMSLDSetObjectives($imsld, $design);
    IMSLDSetPrerrequisites($imsld, $design);

    IMSLDSetRoleStructure($imsld, $design);
    IMSLDSetActivities($imsld, $design);

    $manifest = IMSLDStart($imsld, $uolid);
    $manifest .= $imsld->general;
    $manifest .= '<imsld:components>';
    $manifest .= $imsld->roles;
    $manifest .= $imsld->activities;
    $manifest .= $imsld->environments;
    $manifest .= '</imsld:components>';

    $manifest .= '<imsld:method>';
    $id = $imsld->getNewId();
    $manifest .= '<imsld:play identifier="' . $id . '">';
    $manifest .= '<imsld:title>' . $design->title . '</imsld:title>';
    $manifest .= $imsld->acts;
    $manifest .= '</imsld:play></imsld:method></imsld:learning-design></organizations>';
    $manifest .= '<resources>';
    $manifest .= $imsld->resources;
    $manifest .= '</resources></manifest>';

    IMSLDSaveManifest($folder, $manifest);
}

function IMSLDExport($ldid, $design) {
    $folder = "export/packages/" . rand_str(8) . "_$ldid";
    $uolid = "UOL_" . rand_str(12);
    IMSLDPrepareFolder($folder);
    IMSLDCreateDesign($folder, $design, $uolid);
    $url = IMSLDZipUgly($folder, $ldid);
    IMSLDPrepareDownload($url);
}

function IMSLDInstanceExport($ldid, $design, $instance) {
    $folder = "export/packages/" . rand_str(8) . "_$ldid";
    $uolid = "UOL_" . rand_str(12);
    
    IMSLDPrepareFolder($folder);
    IMSLDCreateDesign($folder, $design, $uolid);
    IMSLDInstanceCreateManifest($folder, $instance, $design, $uolid);
    $url = IMSLDZipUgly($folder, $ldid);
    IMSLDPrepareDownload($url);
}

function LdshakeIMSLDInstanceExport($ldid, $design, $instance) {
    $folder = "export/packages/" . rand_str(8) . "_$ldid";
    $uolid = "UOL_" . rand_str(12);
    
    $folder = dirname(dirname(__FILE__))."/export/packages/" . rand_str(8) . "_$ldid";
    
    IMSLDPrepareFolder($folder);
    IMSLDCreateDesign($folder, $design, $uolid);
    IMSLDInstanceCreateManifest($folder, $instance, $design, $uolid);
    $url = IMSLDZipUgly($folder, $ldid);
    //$pos = strpos($url, "manager");
    //$url = substr($url, $pos);
    return $url;
}



function IMSLDPrepareDownload($url) {
    $json = new Services_JSON();
    echo $json->encode(array("ok" => true, "url" => "manager/$url"));
}

function IMSLDZipUgly($folder, $name) {
    $filename = "$folder/$name.zip";
    $cmd = "cd $folder;zip -r $name.zip *";
    exec($cmd);
    return $filename;
}

function IMSLDZipAddFolder($zip, $folder, $path) {
    $fullpath = "$folder/$path";
    $dh = opendir($fullpath);
    while (($filename = readdir($dh)) !== false) {
        if ($filename !== '.' && $filename !== '..') {
            if (is_dir($fullpath . $filename)) {
                $zip->addEmptyDir($path . $filename);
                IMSLDZipAddFolder($zip, $folder, "$path$filename/");
            } else {
                $zip->addFile($fullpath . $filename, $path . $filename);
            }
        }
    }
}

function IMSLDSaveManifest($folder, $manifest) {
    $handle = fopen("$folder/imsmanifest.xml", "w");
    fwrite($handle, IMSLDGetXMLForKids($manifest));
    fclose($handle);
}

function IMSLDGetXMLForKids($xml) {
    return $xml;
}

function IMSLDSetTitle($imsld, $design) {
    $imsld->general .= '<imsld:title>' . $design->title . '</imsld:title>';
}

function IMSLDSetPrerrequisites($imsld, $design) {
    $content = $design->prerrequisites;

    $prerreq = '<imsld:prerequisites>';
    $prerreq .= IMSLDCreateItemAndResource($imsld, $content);
    $prerreq .= '</imsld:prerequisites>';

    $imsld->general .= $prerreq;
}

function IMSLDSetObjectives($imsld, $design) {
    $content = "";
	foreach($design->learningObjectives as $index => $lo) {
		$content .= "- {$lo->title}: {$lo->description}\r\n";
	}	

    $obj = '<imsld:learning-objectives>';
    $obj .= IMSLDCreateItemAndResource($imsld, $content);
    $obj .= '</imsld:learning-objectives>';

    $imsld->general .= $obj;
}

function IMSLDSetRoleStructure($imsld, $design) {
    $imsld->roles = IMSLDGetRoleStructure($imsld, $design);
}

function IMSLDStart($imsld, $uolid) {
    $ld = '<?xml version="1.0" encoding="UTF-8" ?>';
    $ld .= '<!-- This is a IMS-LD manifest generated by Web Collage -->';
    $ld .= '<manifest xmlns="http://www.imsglobal.org/xsd/imscp_v1p1" xmlns:imsmd="http://www.imsglobal.org/xsd/imsmd_v1p2" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:imsld="http://www.imsglobal.org/xsd/imsld_v1p0" identifier="' .
            $imsld->getNewId() .
            '" xsi:schemaLocation="http://www.imsglobal.org/xsd/imscp_v1p1 imscp_v1p1.xsd http://www.imsglobal.org/xsd/imsmd_v1p2 imsmd_v1p2p2.xsd http://www.imsglobal.org/xsd/imsld_v1p0 IMS_LD_Level_A.xsd">';
    $ld .= '<organizations>';
    $ld .= '<imsld:learning-design identifier="' . $uolid . '" uri="" level="A">';

    return $ld;
}


function IMSLDPrepareFolder($folder) {
    deltree($folder);
    mkdir($folder);
    mkdir("$folder/COLLAGE-magic-folder");
    mkdir("$folder/COLLAGE-hidden-resources");
}	

function deltree($folder) {
    if (is_dir($folder)) {
        foreach(glob($folder . '/*') as $sf) {
            if (is_dir($sf) && !is_link($sf)) {
                deltree($sf);
            } else {
                unlink($sf);
            }
        }

        rmdir($folder);
    }
}


function IMSLDCreateHiddenResourceFile($content, $filename) {
    $handle = fopen("$filename", "w");
    fwrite($handle, $content);
    fclose($handle);
    return true;
}

function IMSLDCreateHiddenResource($folder, $id, $content) {
    $href = "COLLAGE-hidden-resources/$id.txt";

    $res = '<resource identifier="' .$id . '" type="hiddentext" href="' . $href . '">';
    $res .= '<file href="' . $href . '" />';
    $res .= '</resource>';

    IMSLDCreateHiddenResourceFile($content, "$folder/$href");
    return $res;
}

function IMSLDCreateItemAndResource($imsld, $content) {
    $itemId = $imsld->getResourceId();
    $resId = $imsld->getResourceId();

    $item = '<imsld:item identifierref="' . $resId . '" identifier="' . $itemId . '" />';
    $res = IMSLDCreateHiddenResource($imsld->folder, $resId, $content);

    $imsld->resources .= $res;
    return $item;
}

function IMSLDCreateResources($imsld, $design) {
    $imsld->restable = array();

    foreach($design->resources as $index => $res) {
        $id = $imsld->getResourceId();

        if ($res->subtype == "doc") {
            $imsld->restable[$res->id] = array("type" => "doc", "id" => $id, "title" => $res->title);
            $resxml = '<resource identifier="' .$id . '" type="webcontent" href="' . $res->link . '">';
            $resxml .= '</resource>';
            $imsld->resources .= $resxml;
        } else {
            $imsld->restable[$res->id] = array("type" => "tool", "id" => $id, "title" => $res->title, "url" => $res->url, "ontoolquery" => $res->ontoolquery, "gluepsid"=> $res->gluepsid);
        }
    }
}


?>
