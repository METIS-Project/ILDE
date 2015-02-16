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


function printVerySimpleTooltipDialog($dialogId, $contentId, $dialogStyle = null) {
    $style = $dialogStyle != null ? ' style="' . $dialogStyle . '" ' : '';

    $output = '<div id="' . $dialogId . '" dojoType="dijit.TooltipDialog">';
    $output .= '<div id="' . $contentId . '" ' . $style . ' >';
    $output .= '</div>';
    $output .= '</div>';

    return $output;

}
function printSimpleTooltipDialog($dialogId, $contentId, $contentType, $titleId, $dialogStyle = null) {
    $style = $dialogStyle != null ? ' style="' . $dialogStyle . '" ' : '';
    $type = $contentType != null ? 'dojoType="' . $contentType . '" ' : '';

    $output = '<div id="' . $dialogId . '" dojoType="dijit.TooltipDialog">';
    $output .= '<div dojoType="dijit.TitlePane" title="${' . $titleId . '}"' . '>';
    $output .='<div ' . $style . '>';
    $output .= '<div ' . $type . 'id="' . $contentId . '"' . '>';
    $output .= '</div></div>';
    $output .= '</div>';
    $output .= '<div dojoType="dijit.layout.ContentPane" align="center">';
    $output .= '<button dojoType="dijit.form.Button" label="${common.ok}" type="submit"></button>';
    $output .= '</div>';

    $output .= '</div>';

    return $output;
}

function printResourceSelectorTooltipDialog($dialogId, $listId, $dialogStyle = null) {
    $style = $dialogStyle != null ? ' style="' . $dialogStyle . '" ' : '';

    $output = '<div id="' . $dialogId . '" dojoType="dijit.TooltipDialog">';
    $output .= '<div dojoType="dijit.TitlePane" title="${activity.edit.resources}"' . '>';

    $output .= '<div dojoType="dijit.layout.ContentPane" align="center">';
    $output .= '<div dojoType="dijit.form.Button" label="${resources.newDoc}" id="LDSelectResourcesNewDoc_' . $listId . '"></div>';
    $output .= '<div dojoType="dijit.form.Button" label="${resources.newTool}" id="LDSelectResourcesNewTool_' . $listId . '"></div>';
    $output .= '</div>';

    $output .='<div ' . $style . '>';
    $output .= '<div dojoType="dijit.layout.ContentPane" id="LDSelectResourcesList_' . $listId . '"' . '>';
    $output .= '</div></div>';

    $output .= '</div>';

    $output .= '<div dojoType="dijit.layout.ContentPane" align="center">';
    $output .= '<div dojoType="dijit.form.Button" label="${common.ok}" type="submit"></div>';
    $output .= '</div>';

    $output .= '</div>';

    return $output;
}

?>
