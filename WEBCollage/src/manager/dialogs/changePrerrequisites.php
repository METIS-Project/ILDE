<?php
function getChangePrerrequisitesDialog() {
	$output = '<div id="LDPrerrequisitesChangeDialog" dojoType="dijit.TooltipDialog" title="${general.editPrerrequisites}" execute="General.setPrerrequisites(arguments[0].prerrequisites);">';
	$output .= '<table>';
    $output .= '<tr>';
	$output .= '<td>';
	$output .= '<input id="LDPrerrequisitesChangeTextarea" dojoType="dijit.form.Textarea" name="prerrequisites" style="width:300px;height:150px">';
	$output .= '</input>';
	$output .= '</td>';
	$output .= '</tr>';
	$output .= '<tr>';
	$output .= '<td colspan="2" align="center">';
	$output .= '<button dojoType="dijit.form.Button" type="submit" label="${common.ok}">';
	$output .= '</button>';
	$output .= '</td>';
	$output .= '</tr>';
	$output .= '</table>';
	$output .= '</div>';
	
	return $output;
}

?>
