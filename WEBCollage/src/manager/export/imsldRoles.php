<?php



class IMSLDRoles {
	var $learners;
	var $staff;
	
	function IMSLDRoles() {
		$this->learners = array();
		$this->staff = array();
	}
	
	function addRole($role, $parentId = null) {
		$newRole = clone($role);
		if ($newRole->subtype == 'learner') {
			$this->putRoleIn($role, $parentId, null);
		} else {
			$this->staff[] = $newRole;
		}
	}
	
	function putRoleIn($newRole, $id, $learners) {
		if ($id === null) {
			$this->learners[] = $newRole;
			return true;
		} else if ($learners === null) {
			$learners = $this->learners;
		}
		
		foreach ($learners as $index => $role) {
			if ($role->id == $id) {
				$role->children[] = $newRole;
				return true;
			} else {
				$newlearners = $role->children;
				if ($this->putRoleIn($newRole, $id, $newlearners)) {
					return true;
				}
			}
		
		}
		return false;
	}
}

function IMSLDGetRoleStructure($imsld, $design) {
	$roles = new IMSLDRoles();
	IMSLDGetRoleStructureRec($roles, $design->flow);
	
	$code = '<imsld:roles identifier="' . $imsld->getNewId() . '">';
	foreach ($roles->learners as $key => $role) {
		IMSLDAddRole($imsld, $role, $code);
	}
	foreach ($roles->staff as $key => $role) {
		IMSLDAddRole($imsld, $role, $code);
	}
	$code .= '</imsld:roles>';
		
	return $code;
}

function IMSLDAddRole($imsld, $role, &$code) {
	$imsld->recordRoleTitle($role->id, $role->title);
	$code .= '<imsld:' . $role->subtype . ' identifier="' . $imsld->formatId($role->id) . '">';
	$code .= '<imsld:title>' . $role->title . '</imsld:title>';
	$code .= '<imsld:information>';
	$code .= IMSLDCreateItemAndResource($imsld, $role->description);
	$code .= '</imsld:information>';
	/* TODO max & min */
	
	foreach ($role->children as $key => $subrole) {
		IMSLDAddRole($imsld, $subrole, $code);
	}
	
	$code .= '</imsld:' . $role->subtype . '>'; 
}

function IMSLDGetRoleStructureRec($roles, $clfpact, $parentId = NULL) {
	foreach($clfpact->clfps as $index => $clfp) {
		foreach($clfp->ownRoles as $roleIndex => $role) {
			$roles->addRole($role, $parentId);
		}
		
		foreach ($clfp->flow as $actIndex => $act) {
			if ($act->type === 'clfpact') {
				$roleid = $act->learners[0];
				IMSLDGetRoleStructureRec($roles, $act, $roleid);
			}			
		}
	}
}



?>
