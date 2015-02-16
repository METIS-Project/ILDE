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
