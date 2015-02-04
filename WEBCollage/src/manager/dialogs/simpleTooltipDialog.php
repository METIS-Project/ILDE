<?php

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
