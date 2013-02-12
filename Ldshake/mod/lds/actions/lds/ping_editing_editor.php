<?php
/*
require_once __DIR__.'/../../lds_contTools.php';

$docSession = get_input('editor_id');
$editorType = get_input('editorType');
$editing = get_input('editing');

$guid = get_input('entity_guid');
if($guid != '0')
	$lds = get_entity($guid);
else
	$lds = null;

if ($editing == 'true')
{
	//Sets the editing timestamp to now
	$lds->editing_tstamp = time();
	//And saves who's editing it
	$lds->editing_by = get_loggedin_userid();
	$lds->save_ktu();
}
else
{
	if($lds)
	{
		$lds->editing_tstamp = 0;
		$lds->editing_by = null;
		$lds->save_ktu();
	}
	//unload the document
	$editor = EditorsFactory::getTempInstance($editorType);
	$editor->unload($docSession);
}

if($lds)
	lds_contTools::markLdSAsViewed ($lds->guid);
*/
echo "ok";
