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


function ldshake_search_user_by_name($search, $logged = false)
{
    $search = preg_replace("/[^\pL\s\pNd]+/u", " ", $search);
    $search = sanitise_string($search);

    if (!(strlen(trim($search)) > 0 && str_word_count(trim($search)) > 0))
        return false;

    $query = <<<SQL
SELECT * FROM users_entity u
NATURAL JOIN entities e
WHERE MATCH(u.name) AGAINST('>{$search}' '<{$search}*' IN BOOLEAN MODE)
AND e.enabled = 'yes'
AND e.guid <> 2
SQL;

    if(!$users = get_data($query, "entity_row_to_elggstar"))
        return false;

    foreach($users as $ldshaker) {
        $ldshakers_activity[$ldshaker->guid]['started'] = (int)lds_contTools::getUserEntities('object', 'LdS', 0, true, 9999, 0, null, null, "time", false, null, false, null, false, null, $ldshaker->guid);
        $ldshakers_activity[$ldshaker->guid]['coedition'] = (int)lds_contTools::getUserCoedition($ldshaker->guid, 9999, 0, true);
    }

    $ldshakers_info = array(
        'ldshakers' => $users,
        'ldshakers_activity' => $ldshakers_activity,
    );

    return $ldshakers_info;
}

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

        $query = <<<SQL
SELECT DISTINCT e.* from {$CONFIG->dbprefix}entities e JOIN {$CONFIG->dbprefix}entity_relationships r ON e.guid = r.guid_two WHERE e.type = 'group'  AND e.enabled = 'yes' AND ((r.relationship = 'member' AND r.guid_one = '{$userId}') OR e.owner_guid = '{$userId}')
SQL;
        if(isadminloggedin())
            $query = <<<SQL
SELECT DISTINCT e.* from {$CONFIG->dbprefix}entities e WHERE e.type = 'group' AND e.enabled = 'yes'
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

		return $groups_list;
	}
	
	public static function getGroupsUserIsMember ($userId)
	{
        if($groups = get_users_membership($userId))
		    Utils::osort($groups, "name");
				
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

    public static function getCommunityMembers($loggedin = false, $limit = 50, $offset = 0, $count = false)
    {
        global $CONFIG;

        $limit = (int)$limit;
        $offset = (int)$offset;
        $loggedin_sql = $loggedin ? '' : 'AND e.guid <> ' . get_loggedin_userid();
        $hide_admin = '';
        if(get_user(2))
            $hide_admin = 'AND e.guid <> 2';
        $select = "SELECT e.guid, 'user' AS 'type', e.subtype";
        if($count)
            $select = "SELECT COUNT(*) as 'total'";
        $query = <<<SQL
{$select} FROM entities e
NATURAL JOIN {$CONFIG->dbprefix}users_entity u
WHERE e.enabled = 'yes'
{$loggedin_sql}
{$hide_admin}
ORDER BY u.name
LIMIT {$offset}, {$limit}
SQL;

        if($count) {
            $query = <<<SQL
{$select} FROM entities e
NATURAL JOIN {$CONFIG->dbprefix}users_entity u
WHERE e.enabled = 'yes'
{$loggedin_sql}
{$hide_admin}
SQL;

            $row = get_data_row($query);
            return (int)$row->total;
        }

        $entities = get_data($query, "entity_row_to_elggstar");
        if(!empty($entities))
            return $entities;

        return false;
    }
}