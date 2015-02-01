<?php
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
