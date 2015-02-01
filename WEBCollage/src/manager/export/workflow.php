<?php


class WorkflowGroups {

	var $occurrenceid;
	var $instance;
	var $uolid;
	var $design;
	var $populationList;
	var $groupXml;
	var $processManifest;
	var $groupManifest;

	function WorkflowGroups($instance, $design, $uolid) {
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
			if (strcmp($this->getObject($group->roleid)->subtype, "learner") == 0) {

				$roleid = "group_" . $group->roleid;

				foreach ($group->instances as $ik => $gi) {
					if ($this->filterInstance($gi, $parentid)) {
						$activities = "";
						if ($parentid == NULL) {
							$activities = "<activity>Think</activity><activity>Share</activity>";
						} else {
							$activities = "<activity>Pair</activity>";
						}

						$occurrence = ++$this->occurrenceid;
						$groupid = $roleid . "_" . $occurrence;

						$this->groupXml .= '<group id="' . $groupid . '" name="' . $gi->name . '">';
						$this->groupXml .= $activities;
						foreach ($gi->participants as $pk => $participant) {
							$this->groupXml .= '<user login="' . $participant . '" />';
						}
						$this->groupXml .= '</group>';

						$this->addSubgroups($gi->id);

					}
				}
			}
		}
	}

	function createManifests() {
		$this->populationList = array();
		$this->occurrenceid = 0;


		$this->groupXml = '';
		$this->addSubgroups();

		$population = '<role-population>';
		$studentlist = '';
		foreach ($this->populationList as $participant => $list) {
			$studentlist .= '<user name="' . $participant . '" forename="' . $participant . '" login="' . $participant . '" />';
			$population .= '<user identifier="' . $participant . '">';
			$population .= $list;
			$population .= '</user>';
		}
		$population .= '</role-population>';


		$this->processManifest = '<?xml version="1.0" encoding="UTF-8"?>';
		$this->processManifest = '<?xml version="1.0" encoding="UTF-8" ?>';
		$this->processManifest .= "<process>";
		$this->processManifest .= "<pattern>Think-Pair-Share</pattern>";
		$this->processManifest .= "<instance-id>{$this->uolid}</instance-id>";
		$this->processManifest .= "<grouping-strategy name='learner-choice'>";
		$this->processManifest .= "<properties>";
		$this->processManifest .= "<property name='group-size'>3</property>";
		$this->processManifest .= "</properties>";
		$this->processManifest .= "</grouping-strategy>";
		$this->processManifest .= "<users>";
		$this->processManifest .= "<group>learners/licence3/computer-science-TP3</group>";
		$this->processManifest .= $studentlist;
		$this->processManifest .= "</users>";
		$this->processManifest .= "</process>";

		$this->groupManifest = '<?xml version="1.0" encoding="UTF-8"?>';
		$this->groupManifest .= '<grouping>';
		$this->groupManifest .= $this->groupXml;
		//		$this->groupManifest .= $population;
		$this->groupManifest .= '</grouping>';
	}

	function getObject($id) {
		return $this->getObjectFrom($this->design, $id);
	}

	function getObjectFrom($container, $id) {
		if (isset($container->id) && strcmp($container->id, $id) == 0) {
			return $container;
		} else {
			if (is_array($container) || is_object($container)) {
				foreach($container as $k => $v) {
					$result = $this->getObjectFrom($v, $id);
					if ($result !== NULL) {
						return $result;
					}
				}
			}
			return NULL;
		}
	}
}





function WorkflowInstanceExport($ldid, $design, $instance) {
	$folder = "export/packages/" . rand_str(8) . "_$ldid";
	$uolid = "wf_" . $ldid;


	WorkflowPrepareFolder($folder);
	WorkflowCreateManifests($folder, $instance, $design, $uolid);
        $url = WorkflowZipSmart($folder, $ldid);
	//$url = WorkflowZipUgly($folder, $ldid);
	WorkflowPrepareDownload($url);
}

function WorkflowPrepareFolder($folder) {
	deltree($folder);
	mkdir($folder);
}

function WorkflowZipUgly($folder, $name) {
	$filename = "$folder/$name.zip";
	$cmd = "cd $folder;zip -r $name.zip *";
	exec($cmd);
	return $filename;
}

function WorkflowZipSmart($folder, $name) {
    $filename = "$folder/$name.zip";
    //Create the zip file
    $zip = new ZipArchive();
    if ($zip->open($filename, ZIPARCHIVE::CREATE)==TRUE){
        //If it opens the file, there aren't any files with that name
        zipFolder($folder, $zip, $folder);
        $zip->close();
    }
    return $filename;
}

function WorkflowPrepareDownload($url) {
	$json = new Services_JSON();
	echo $json->encode(array("ok" => true, "url" => "manager/$url"));
}



function WorkflowCreateManifests($folder, $instance, $design, $uolid) {
	$ms = new WorkflowGroups($instance, $design, $uolid);
	$ms->createManifests();

	WorkflowPostGroupManifest($ms->processManifest, $ms->groupManifest);
	//WorkflowPost2($ms->processManifest, $ms->groupManifest);
	
	WorkflowSaveManifest($folder, $ms->processManifest);
	WorkflowSaveGroupManifest($folder, $ms->groupManifest);
}

function WorkflowSaveManifest($folder, $manifest) {
	$handle = fopen("$folder/processmanifest.xml", "w");
	fwrite($handle, $manifest);
	fclose($handle);
}

function WorkflowSaveGroupManifest($folder, $manifest) {
	$handle = fopen("$folder/groupmanifest.xml", "w");
	fwrite($handle, $manifest);
	fclose($handle);
}

function WorkflowPostGroupManifest($process, $groups) {
	$ch = curl_init();


	// HTTP method
	//curl_setopt($ch, CURLOPT_URL, "http://localhost/~evilfer/test.php");
	curl_setopt($ch, CURLOPT_URL, "http://134.206.18.117:8082/uploadGroup");
	curl_setopt($ch, CURLOPT_POST, TRUE);
	curl_setopt($ch, CURLOPT_POSTFIELDS, array("groups" => $groups));
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);

	$answer = curl_exec($ch);
	curl_close($ch);
}

function WorkflowPost2($process, $groups) {
	$url = "http://134.206.18.117:8082/uploadGroup";
	//$url = "http://localhost/~evilfer/test.php";
	
	$params = array('http' => array(
			'header' => array(),
			'method' => 'POST',
			'content' => array("groups" => $groups)
	));
	
	$json = new Services_JSON();
	echo $json->encode($params);
	

//	if ($optional_headers !== null) {
//		$params['http']['header'] = $optional_headers;
//	}

	$ctx = stream_context_create($params);
	$fp = @fopen($url, 'rb', false, $ctx);
	if (!$fp) {
		die("Problem with $url");//, $php_errormsg");
	}

	$response = @stream_get_contents($fp);
	if ($response === false) {
		die("Problem reading data from $url");//, $php_errormsg");
	}

	return $response;
}

?>