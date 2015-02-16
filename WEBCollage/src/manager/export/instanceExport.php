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
* Exporta a IMS-LD el script de una instanciación 
*/
class IMSLDInstance {
    
    var $occurrenceid;
    var $instance;
    var $uolid;
    var $design;
    var $populationList;
    var $groupXml;
    
    function IMSLDInstance($instance, $design, $uolid) {
        $this->instance = $instance;
        $this->design = $design;
        $this->uolid = $uolid;
    }
    
    function filterInstance($instance, $parentid) {
        return isset($instance->idParent) ? 
            ($parentid === $instance->idParent) :
            ($parentid === NULL);            
    }
    
    function addSubgroups($parentid = NULL) {
        foreach ($this->instance->groups as $gk => $group) {
            $roleid = "LD_" . $group->roleid;
            foreach ($group->instances as $ik => $gi) {
                if ($this->filterInstance($gi, $parentid)) {
                    $occurrence = ++$this->occurrenceid;
                    $this->groupXml .= '<role id="' . $roleid . '" occurrence="' . $occurrence . '" cc-id="-1">';
                    $this->groupXml .= '<title>' . $gi->name . '</title>';
                    $this->addSubgroups($gi->id);
                    $this->groupXml .= '</role>';

                    foreach ($gi->participants as $pk => $participant) {
                        $occlist = isset($this->populationList[$participant]) ? $this->populationList[$participant] : "";
                        $occlist .= '<role-occurrence-ref ref="' . $occurrence . '" />';
                        $this->populationList[$participant] = $occlist;
                    }
                }
            }
        }
    }
    
    function createManifest($folder) {
        $this->populationList = array();
        $this->occurrenceid = 0;
        
        
        $this->groupXml = '<role-root>';
        $this->addSubgroups();
        $this->groupXml .= '</role-root>';
        
        $population = '<role-population>';
        foreach ($this->populationList as $participant => $list) {
            $population .= '<user identifier="' . $participant . '">';
            $population .= $list;
            $population .= '</user>';
        }
        $population .= '</role-population>';
        

        $manifest = '<?xml version="1.0" encoding="UTF-8"?>';
        $manifest .= '<icollage-manifest id="WIC-' . rand_str(8) . '">';
        $manifest .= '<uol-info id="' . $this->uolid . '" uri="imsmanifest.xml" />'; //package="D:\Users\evilfer\Desktop\icp\p.zip" 
        $manifest .= $this->groupXml;
        $manifest .= $population;
        $manifest .= '</icollage-manifest>';

        $handle = fopen("$folder/icmanifest.xml", "w");

        fwrite($handle, $manifest);
        fclose($handle);
    }
}




function IMSLDInstanceCreateManifest($folder, $instance, $design, $uolid) {
    $i = new IMSLDInstance($instance, $design, $uolid);
    $i->createManifest($folder);
}

?>