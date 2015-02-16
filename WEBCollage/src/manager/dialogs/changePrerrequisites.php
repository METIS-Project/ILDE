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

function getChangePrerrequisitesDialog() {
	$output = '<div id="LDPrerrequisitesChangeDialog" dojoType="dijit.TooltipDialog" title="${general.editPrerrequisites}" execute="General.setPrerrequisites(arguments[0].prerrequisites);">';
	$output .= '<table>';
        $output .= '<tr>';
	$output .= '<td>';
	$output .= '<input id="LDPrerrequisitesChangeTextarea" dojoType="dijit.form.SimpleTextarea" name="prerrequisites" style="min-width:350px;min-height:100px;overflow:auto;">';
	$output .= '</input>';
	$output .= '</td>';
	$output .= '</tr>';
	$output .= '<tr>';
	$output .= '<td align="center">';
	$output .= '<button dojoType="dijit.form.Button" type="submit" label="${common.ok}">';
	$output .= '</button>';
	$output .= '</td>';
	$output .= '</tr>';
	$output .= '</table>';
	$output .= '</div>';
	
	return $output;
}

?>
