<?php

function IMSLDSetActivities($imsld, $design) {
	$imsld -> activities .= '<imsld:activities>';
	$imsld -> environments .= '<imsld:environments>';

	IMSLDSetActivitiesRec($imsld, $design, $design -> flow);

	$imsld -> environments .= '</imsld:environments>';
	$imsld -> activities .= '</imsld:activities>';
}

function IMSLDSetActivitiesRec($imsld, $design, $clfpact) {
	foreach ($clfpact->clfps as $index => $clfp) {
		foreach ($clfp->flow as $actIndex => $act) {
			if ($act -> type === 'clfpact') {
				IMSLDSetActivitiesRec($imsld, $design, $act);
			} else {/* is an act */
                                $there_are_activities = false;
                                foreach ($act->roleparts as $rpIndex => $rolepart) {
                                    $activitycount = count($rolepart -> activities);
                                    if ($activitycount > 0 && $there_are_activities===false){
                                        $there_are_activities = true;
                                    }
                                }
                                if ($there_are_activities === true){ 
                                    IMSLDStartAct($imsld, $act);

                                    foreach ($act->roleparts as $rpIndex => $rolepart) {
                                            $roleId = $rolepart -> roleId;
                                            $activitycount = count($rolepart -> activities);

                                            if ($activitycount > 0) {
                                                    /*						if ($activitycount == 1) {
                                                     $activity = $rolepart -> activities[0];
                                                     $activityType = $activity -> subtype;
                                                     $activityId = IMSLDAddActivity($imsld, $design, $roleId, $activity);
                                                     } else {*/
                                                    $ids = array();
                                                    foreach ($rolepart->activities as $activityIndex => $activity) {
                                                            $ids[] = array("id" => IMSLDAddActivity($imsld, $design, $roleId, $activity), "type" => $activity -> subtype);
                                                    }
                                                    $activityType = "activity-structure";
                                                    $activityTitle = $imsld -> getRoleTitle($roleId);
                                                    $activityId = IMSLDAddActivityStructure($imsld, $ids, $activityTitle);
                                                    //	}
                                                    IMSLDAddRolePart($imsld, $imsld -> formatId($rolepart -> roleId), $activityType, $activityId);
                                            }
                                    }

                                    IMSLDEndAct($imsld);
                                }
			}
		}
	}
}

function IMSLDStartAct($imsld, $act) {
	$imsld -> acts .= '<imsld:act identifier="' . $imsld -> formatId($act -> id) . '">';
	$imsld -> acts .= '<imsld:title>' . $act -> title . '</imsld:title>';
}

function IMSLDAddRolePart($imsld, $roleId, $activityType, $activityId) {
	$id = $imsld -> getNewId();
	$imsld -> acts .= '<imsld:role-part identifier="' . $id . '">';
	$imsld -> acts .= '<imsld:title>' . $id . '</imsld:title>';
	$imsld -> acts .= '<imsld:role-ref ref="' . $roleId . '" />';
	$type = $activityType == "activity-structure" ? 'activity-structure-ref' : $activityType . '-activity-ref';

	$imsld -> acts .= '<imsld:' . $type . ' ref="' . $activityId . '" />';
	$imsld -> acts .= '</imsld:role-part>';
}

function IMSLDEndAct($imsld) {
	$imsld -> acts .= '</imsld:act>';
}

function IMSLDAddActivity($imsld, $design, $roleId, $activity) {
	$id = $imsld -> formatId($activity -> id);

	$imsld -> activities .= '<imsld:' . $activity -> subtype . '-activity identifier="' . $id . '">';
	$imsld -> activities .= '<imsld:title>' . $activity -> title . '</imsld:title>';
	$imsld -> activities .= '<imsld:environment-ref ref="' . IMSLDAddEnvironment($imsld, $design, $roleId, $activity) . '" />';
	$imsld -> activities .= '<imsld:activity-description>';
	$imsld -> activities .= IMSLDCreateItemAndResource($imsld, $activity -> description);
	$imsld -> activities .= '</imsld:activity-description>';
	$imsld -> activities .= '</imsld:' . $activity -> subtype . '-activity>';

	return $id;
}

function IMSLDAddActivityStructure($imsld, $list, $title) {
	$id = $imsld -> getNewId();
	$imsld -> activities .= '<imsld:activity-structure identifier="' . $id . '">';
	$imsld -> activities .= '<imsld:title>' . $title . '</imsld:title>';
	foreach ($list as $index => $value) {
		$imsld -> activities .= '<imsld:' . $value['type'] . '-activity-ref ref="' . $value['id'] . '" />';
	}
	$imsld -> activities .= '</imsld:activity-structure>';
	return $id;
}

function IMSLDAddEnvironment($imsld, $design, $roleId, $activity) {
	$id = $imsld -> getNewId();

	$imsld -> environments .= '<imsld:environment identifier="' . $id . '">';
	$imsld -> environments .= '<imsld:title>' . $id . '</imsld:title>';
	foreach ($design->activitiesAndResources as $index => $value) {
		$resid = -1;
		if ($value[0] == $activity -> id) {
			$resid = $value[1];
		} else if ($value[1] == $activity -> id) {
			$resid = $value[0];
		}
		if (isset($imsld -> restable[$resid])) {
			$resdata = $imsld -> restable[$resid];
			if ($resdata["type"] == "doc") {
				$imsld -> environments .= '<imsld:learning-object identifier="' . $imsld -> getNewId() . '">';
				$imsld -> environments .= '<imsld:title>' . $resdata["title"] . '</imsld:title>';
				$imsld -> environments .= '<imsld:item identifierref="' . $resdata["id"] . '"/>';
				$imsld -> environments .= '</imsld:learning-object>';
			} else if ($resdata["type"] == "tool") {
				$param = "";

				if ($resdata["url"] != null) {
					$param .= "toolURI=" . $resdata["url"] . ";";
				} else if ($resdata["ontoolquery"] != null) {
					$param .= "ontoolquery=" . $resdata["ontoolquery"] . ";";
				}

				if (isset($resdata["gluepsid"])) {
					if ($resdata["gluepsid"] != null) {
						$param .= "gluepsId=" . $resdata["gluepsid"] . ";";
					}
				}

				$imsld -> environments .= '<imsld:service identifier="' . $imsld -> getNewId() . '" parameters="' . $param . '">';
				$imsld -> environments .= '<imsld:conference conference-type="synchronous">';
				$imsld -> environments .= '<imsld:title>' . $resdata["title"] . '</imsld:title>';
				$imsld -> environments .= '<imsld:participant role-ref="' . $imsld -> formatId($roleId) . '" />';
				$imsld -> environments .= '<imsld:item />';
				$imsld -> environments .= '</imsld:conference>';
				$imsld -> environments .= '</imsld:service>';
			}
		}
	}

	$imsld -> environments .= '</imsld:environment>';

	return $id;
}
?>
