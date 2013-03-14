<?php
/*********************************************************************************
 * LdShake is a platform for the social sharing and co-edition of learning designs
 * Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
 *
 * (Contributors, alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by the
 * Free Software Foundation with the addition of the following permission added
 * to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK
 * IN WHICH THE COPYRIGHT IS OWNED BY Universitat Pompeu Fabra (UPF), Barcelona,
 * UPF DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program; if not, see http://www.gnu.org/licenses.
 *
 * You can contact the Interactive Technologies Group (GTI), Universitat Pompeu Fabra, Barcelona.
 * headquarters at c/Roc Boronat 138, Barcelona, or at email address davinia.hernandez@upf.edu
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License version 3,
 * these Appropriate Legal Notices must retain the display of the "Powered by
 * LdShake" logo with a link to the website http://ldshake.upf.edu.
 * If the display of the logo is not reasonably feasible for
 * technical reasons, the Appropriate Legal Notices must display the words
 * "Powered by LdShake" with the link to the website http://ldshake.upf.edu.
 ********************************************************************************/

/**
 * Set of helper functions for the LdShakers module 
 *
 */

class ldshakers_contTools
{
	/**
	 * Gets the groups that the user has created. Each group contains the id, the name and the number of members.
	 * If Als
	 * @param unknown_type $userId
	 * @param unknown_type $alsoIncludeMembers If set to true, it also includes the list of members
	 */
	public static function getUserGroups ($userId, $alsoIncludeMembers = false)
	{
        global $CONFIG;

        //$groups = get_users_membership($userId);


        $query = <<<SQL
SELECT DISTINCT e.* from {$CONFIG->dbprefix}entities e JOIN {$CONFIG->dbprefix}entity_relationships r ON e.guid = r.guid_two WHERE e.type = 'group' AND ((r.relationship = 'member' AND r.guid_one = '{$userId}') OR e.owner_guid = '{$userId}')
SQL;
        if(isadminloggedin())
            $query = <<<SQL
SELECT DISTINCT e.* from {$CONFIG->dbprefix}entities e WHERE e.type = 'group'
SQL;

        $groups = get_data($query, "entity_row_to_elggstar");


        $groups_list = array();

        if($groups) {
            foreach($groups as $g) {
                $data = new stdClass();
                $data->id = $g->guid;
                $data->name = $g->name;
                $data->owner_guid = $g->owner_guid;
                $data->site_guid = $g->site_guid;
                $data->canEdit = $g->canEdit();

                $n = $g->getMembers(9999);
                if($n) {
                    $data->nMembers = count($n);
                } else
                    $data->nMembers = 0;
                $groups_list[] = $data;
            }
        }

        /*
		$groups = get_user_access_collections($userId);
		
		if (is_array($groups))
		{
			foreach ($groups as $g)
			{
				$n = get_members_of_access_collection($g->id, true);
				if ($n)
				{
					$g->nMembers = count($n);
					
					if ($alsoIncludeMembers)
						$g->members = get_members_of_access_collection($g->id);
				}
				else 
					$g->nMembers = 0;
			}
			Utils::osort($groups, 'name');
		}
        */
		
		return $groups_list;
	}
	
	/**
	 * Builds a minimal array og groups and members for a specified user, to be serialized w/json.
	 * @param unknown_type $groups
	 */
	public static function buildMinimalUserGroups ($userId)
	{
		//$groups = self::getUserGroups ($userId, true);
		$groups = get_users_membership($userId);
		
		$arr = array();
		
		if (is_array($groups))
		{
			foreach ($groups as $g)
			{
                $o = new stdClass();
                $o->guid = $g->guid;
                $o->name = $g->name;
                $o->pic = $g->getIcon('small');

                $arr[] = $o;
			}
		}
		
		return $arr;
	}
	
	public static function getGroupsUserIsMember ($userId)
	{
		$query = "SELECT am.access_collection_id FROM {$CONFIG->dbprefix}access_collection_membership am ";
		$query .= " LEFT JOIN {$CONFIG->dbprefix}access_collections ag ON ag.id = am.access_collection_id ";
		$query .= " WHERE am.user_guid = {$userId} AND ag.owner_guid = " . get_loggedin_userid();
		
		$groups = array ();
		$res = execute_query ($query, get_db_link('read'));
		while ($row = mysql_fetch_object($res))
			$groups[] = get_access_collection($row->access_collection_id);
		
		Utils::osort($groups, name);
				
		return $groups;
	}

    public static function groupExists($name)
    {
        global $CONFIG;

        $query = <<<SQL
SELECT e.* from {$CONFIG->dbprefix}entities e JOIN {$CONFIG->dbprefix}groups_entity ge ON e.guid = ge.guid WHERE ge.name ='{$name}'
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($entities)
            return true;

        return false;
    }
}