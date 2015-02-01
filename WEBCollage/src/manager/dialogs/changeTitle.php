<?php
function getChangeTitleDialog() {
	$output = '<div id="LDTitleChangeDialog" dojoType="dijit.TooltipDialog" title="${general.editTitle}" execute="General.setTitle(arguments[0].title);">';
	$output .= '<table>';
	$output .= '<tr>';
	$output .= '<td>';
	$output .= '<input id="LDTitleChangeTextbox" dojoType="dijit.form.TextBox" name="title">';
	$output .= '</td>';
	$output .= '</tr>';
	$output .= '<tr>';
	$output .= '<td align="center">';
	$output .= '<button dojoType="dijit.form.Button" label="${common.ok}" type="submit">';
	$output .= '</button>';
	$output .= '</td>';
	$output .= '</tr>';
	$output .= '</table>';
	$output .= '</div>';
	
	return $output;
}



?>
