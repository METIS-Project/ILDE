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
 * Set of helper functions for the LdS module 
 */

include_once __DIR__.'/Java.inc';

class lds_contTools
{
	/**
	 * Performs a full-text search in the LdS (title for all and description for the plain text ones). 
	 * @param unknown_type $query
	 */


    public static function searchLdS($query, $offset = 0, $user_id = 0) {
        global $CONFIG;

        if(!$user_id)
            $user_id = get_loggedin_userid();

        $subtype = get_subtype_id('object', 'LdS');

        //$mj = '';
        $mw = '';
/*
        if($mk && $mv) {
            $mk_id = get_metastring_id($mk);
            $mv_id = get_metastring_id($mv);

            //$mj = "JOIN metadata m ON e.guid = m.entity_guid";
            $mw = "md.name_id='{$mk_id}' AND  md.value_id='{$mv_id}' AND";
        }
*/
        $tp_k = get_metastring_id("pedagogical_approach");
        $td_k = get_metastring_id("discipline");
        $tt_k = get_metastring_id("tags");
        $t_v = get_metastring_id($query);

        if($t_v)
            $tags_query = "OR ( md.name_id IN ($tp_k,$td_k,$tt_k) AND md.value_id = '{$t_v}' )";
        else
            $tags_query = "";

        $acv_k = get_metastring_id("all_can_view");
        $acv_v = get_metastring_id("yes");

        $query = mysql_real_escape_string($query);

        $query = <<<SQL
SELECT DISTINCT e.guid, e.type FROM {$CONFIG->dbprefix}entities e JOIN {$CONFIG->dbprefix}metadata m ON e.guid = m.entity_guid JOIN {$CONFIG->dbprefix}metadata md ON e.guid = md.entity_guid JOIN {$CONFIG->dbprefix}objects_entity o ON e.guid = o.guid  JOIN {$CONFIG->dbprefix}entities de ON e.guid = de.container_guid JOIN {$CONFIG->dbprefix}objects_entity do ON de.guid = do.guid WHERE e.type = 'object' AND e.subtype = $subtype AND e.enabled = 'yes' AND (
	(e.owner_guid = {$user_id})
	OR
	(
		m.name_id = '{$acv_k}' AND m.value_id = '{$acv_v}'
	)
	OR
	(
		e.guid NOT IN (
		    SELECT DISTINCT acvm.entity_guid FROM metadata acvm WHERE acvm.name_id = '{$acv_k}'
		)
		AND
		e.access_id < 3 AND e.access_id > 0
	)
	OR
	(
		e.guid IN (
			SELECT DISTINCT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.relationship IN ('lds_editor_group', 'lds_viewer_group') AND rg.guid_one IN (
				SELECT rug.guid_two FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
			)
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship IN ('lds_editor', 'lds_viewer') AND ru.guid_one = {$user_id}
		)
	)
) AND (
	  (o.title LIKE '%{$query}%' OR do.title LIKE '%{$query}%' OR do.description LIKE '%{$query}%')
	  {$tags_query}
	)
	 order by e.time_updated desc limit {$offset},11
SQL;

        if($entities = get_data($query, "entity_row_to_elggstar"))
            return self::enrichLdS($entities);

        return false;
    }

	public static function searchLdS_old ($query) {
        global $CONFIG;
		$query = addslashes($query);
		
		$sql = "SELECT guid FROM {$CONFIG->dbprefix}objects_entity WHERE MATCH (title,description) AGAINST ('$query')";
		$res = execute_query ($sql, get_db_link('read'));
		while ($row = mysql_fetch_object($res)) {
			$ids[] = $row->guid;
		}
		
		//Fast fast fast! Only the first 50 results
		$i = 0;
		if (is_array($ids)) {
			foreach ($ids as $id) {
				if ($i == 50) break;
				
				$aux = get_entity($id);
				if ($aux) {
					if ($aux->subtype == get_subtype_id('object', 'LdS') ||
                        $aux->subtype == get_subtype_id('object', 'LdS_document')) {
                        if($aux->subtype == get_subtype_id('object', 'LdS_document'))
                            $aux = get_entity($aux->lds_guid);

                        if($aux) {
                            $lds[] = $aux;
                            $i++;
                        }
					}
				}
			}
			
			return self::enrichLdS($lds);
		} else {
			return false;
		}
	}
	
	/**
	 * Marks an LdS as viewed for the logged user.
	 * THis data is stored in teh user's metadata.
	 * 
	 * @param unknown_type $lds_guid
	 */
	public static function markLdSAsViewed ($lds_guid)
	{
		$lds = get_entity($lds_guid);
		$user = get_user(get_loggedin_userid());
        if($lds && $user) {
            $seenLds = $user->seen_lds;
            if (is_null($seenLds)) $seenLds = array();
            if (is_string($seenLds)) $seenLds = array($seenLds);
            foreach ($seenLds as $k=>$v)
            {
                $parts = explode (':',$v);
                if ($parts[0] == $lds_guid)
                    unset($seenLds[$k]);
            }

            $seenLds[] = $lds_guid.':'.$lds->time_updated;
            $user->seen_lds = $seenLds;
            $user->save();
        }
	}
	
	/**
	 * Generates a list of RichLdS objects, containing some useful data for the view.
	 * 
	 * The reason for this is that the core doesn't allow me to create arbitrary fields in an ENtity object
	 * with complex data types (objects, arrays...)
	 * 
	 */
	public static function enrichLdS ($list)
	{
		if (!is_array($list) && !empty($list)) $list = array ($list);
		if (empty($list)) $list = array();
		
		$richList = array();
		foreach ($list as $lds)
		{
            $obj = new stdClass();
            if($lds->getSubtype() != 'LdS') {
                //$implementation = $lds;
                //$lds = get_entity($lds->container_guid);
                $obj->design = get_entity($lds->lds_guid);
                $obj->implementation = true;
            }
            if($lds) {
                //$obj = new stdClass();
                //$obj->implementation = $implementation;
                $lds->title = ($lds->title == '') ? T('Untitled LdS') : $lds->title;
                $obj->lds = $lds; //The LdS itself
                $obj->starter = get_entity($lds->owner_guid);

                $editor_types = explode(',', $lds->editor_type);
                $obj->editor_type = $editor_types[0];

                $revision_type = $lds->external_editor ? 'revised_docs_editor' : 'revised_docs';
                $latest = $lds->getAnnotations($revision_type, 1, 0, 'desc');
                $obj->last_contributor = get_entity($latest[0]->owner_guid);
                $obj->last_contribution_at = $latest[0]->time_created;
                $obj->num_contributions = $lds->countAnnotations($revision_type);

                //$obj->num_editors = count(get_members_of_access_collection($lds->write_access_id,true));
                //$obj->num_viewers = ($lds->access_id == 1) ? -1 : count(get_members_of_access_collection($lds->access_id,true));

                $obj->num_viewers = count(lds_contTools::getViewersIds($lds->guid));
                $obj->num_editors = count(lds_contTools::getEditorsIds($lds->guid));
                if($lds->all_can_view == 'yes')
                    $obj->num_viewers = -1;

                if($lds->all_can_view === null && $lds->access_id < 3 && $lds->access_id > 0)
                    $obj->num_viewers = -1;

                $obj->num_comments = $lds->countAnnotations('generic_comment');
                $obj->num_documents = get_entities_from_metadata ('lds_guid',$lds->guid,'object','LdS_document', 0, 10000, 0, '', 0, true);

                $obj->locked = ($lds->editing_tstamp > time() - 60 && $lds->editing_by != get_loggedin_userid());
                $obj->locked_by = get_user($lds->editing_by);

                //Add an is_new flag if the user hasn't seen it.
                $seenLds = get_user(get_loggedin_userid())->seen_lds;
                if (is_null($seenLds)) $seenLds = array();
                if (is_string($seenLds)) $seenLds = array($seenLds);

                $isnew = true;


                if($last_viewed = get_annotations($lds->guid, 'object','LdS','viewed_LdS','',get_loggedin_userid(), 1,"","time_created desc")) {
                    $last_viewed = $last_viewed[0]->time_created;
                }  else {
                    $last_viewed = 0;
                }

                if($obj->last_contributor->guid == get_loggedin_userid()) {
                    $isnew = false;
                } elseif($lds->time_updated - 15 < $last_viewed){
                    $isnew = false;
                }

                /*
                foreach ($seenLds as $sl)
                {
                    $sl = explode(':',$sl);
                    if ($sl[0] == $lds->guid && $sl[1] >= $lds->time_updated - 5)
                    {
                        $isnew = false;
                        break;
                    }
                }
                */

                $obj->new = $isnew;

                $richList[] = $obj;
            }
		}
		
		return $richList;
	}

    public static function enrichImplementation ($list)
    {
        if (!is_array($list) && !empty($list)) $list = array ($list);
        if (empty($list)) $list = array();

        $richList = array();
        foreach ($list as $implementation)
        {
            $obj = new stdClass();
            $implementation->title = ($implementation->title == '') ? T('Untitled implementation') : $implementation->title;
            $obj->implementation = $implementation;
            $obj->lds_id = $implementation->lds_id; //The LdS itself
            $obj->starter = get_entity($implementation->owner_guid);
            $obj->glueps = get_entities_from_metadata_multi(array(
                    'lds_guid' => $implementation->guid,
                    'editorType' => 'gluepsrest'
                ),
                'object','LdS_document_editor', 0, 100);

            $latest = $implementation->getAnnotations('revised_docs_editor', 1, 0, 'desc');
            $obj->last_contributor = get_entity($latest[0]->owner_guid);
            $obj->last_contribution_at = $latest[0]->time_created;
            $obj->num_contributions = $implementation->countAnnotations('revised_docs_editor');

            //$obj->num_editors = count(get_members_of_access_collection($lds->write_access_id,true));
            //$obj->num_viewers = ($lds->access_id == 1) ? -1 : count(get_members_of_access_collection($lds->access_id,true));

            $obj->num_viewers = count(lds_contTools::getViewersIds($implementation->guid));
            $obj->num_editors = count(lds_contTools::getEditorsIds($implementation->guid));
            if($implementation->all_can_view == 'yes')
                $obj->num_viewers = -1;

            if($implementation->all_can_view === null)
                $obj->num_viewers = -1;

            $obj->num_comments = $implementation->countAnnotations('generic_comment');

            $obj->locked = ($implementation->editing_tstamp > time() - 60 && $implementation->editing_by != get_loggedin_userid());
            $obj->locked_by = get_user($implementation->editing_by);

            //Add an is_new flag if the user hasn't seen it.
            $seenImplementation = get_user(get_loggedin_userid())->seen_implementation;
            if (is_null($seenImplementation)) $seenImplementation = array();
            if (is_string($seenImplementation)) $seenImplementation = array($seenImplementation);

            $isnew = true;

            if($last_viewed = get_annotations($lds->guid, 'object','LdS','viewed_LdS','',get_loggedin_userid(), 1,"","time_created desc")) {
                $last_viewed = $last_viewed[0]->time_created;
            }  else {
                $last_viewed = 0;
            }

            if($obj->last_contributor->guid == get_loggedin_userid()) {
                $isnew = false;
            } elseif($lds->time_updated - 15 < $last_viewed){
                $isnew = false;
            }

            $obj->new = $isnew;

            $richList[] = $obj;
        }

        return $richList;
    }

	/**
	 * 
	 * Returns the user Object that is editing this LdS, or false if noone is editing it.
	 */
	public static function isLockedBy ($lds_guid)
	{
		$lds = get_entity($lds_guid);
		if ($lds->editing_tstamp > time() - 60 && $lds->editing_by != get_loggedin_userid())
			return get_user($lds->editing_by);
		else
			return false;
	}
	
	public static function getLdsDocuments ($lds_guid)
	{
		//Shitty limits.
		$arr = get_entities_from_metadata ('lds_guid',$lds_guid,'object','LdS_document', 0, 10000);
		if(is_array($arr))
			Utils::osort($arr, 'guid');
		
		return $arr;
	}
	
	/**
	 * Gets all the necessary data for an LdS revision history display.
	 */
	public static function getRevisionList ($lds)
	{	
		//Cutre això del limit, però què hi farem :(
		$revisions = $lds->getAnnotations('revised_docs', 10000, 0, 'asc');
		$history = array();
		$revNum = 1;
		if (is_array($revisions))
		{
			foreach ($revisions as $rev)
			{
				$obj = new stdClass();
				$obj->revision_number = $revNum++;
				$obj->revision = $rev;
				$obj->author = get_entity($rev->owner_guid);
				$obj->deleted_documents = explode (',', $rev->value);
				
				//IDEM! Limits suck.
				$documents = get_entities_from_metadata('lds_revision_id', $rev->id, 'object', 'LdS_document_revision', '', 10000);
				$obj->revised_documents = $documents;
				
				$history[] = $obj;
			}
		}
		
		return array_reverse($history);
	}

/**
 * Gets all the necessary data for an LdS revision history display.
 */
	public static function getRevisionEditorList ($lds)
	{	
		//Cutre això del limit, però què hi farem :(
		$revisions = $lds->getAnnotations('revised_docs_editor', 10000, 0, 'asc');
		
		$history = array();
		$revNum = 1;
		foreach ($revisions as $rev)
		{
			$obj = new stdClass();
			$obj->revision_number = $revNum++;
			$obj->revision = $rev;
			$obj->author = get_entity($rev->owner_guid);
			//$obj->deleted_documents = explode (',', $rev->value);
			
			//IDEM! Limits suck.
			$documents = get_entities_from_metadata('lds_revision_id', $rev->id, 'object', 'LdS_document_editor_revision', '', 10000);
			$obj->revised_documents = $documents;
			
			$history[] = $obj;
		}
		
		return array_reverse($history);
	}

	
	public static function getJSONRevisionList ($list)
	{
		//$list = array_reverse($list);
		
		$json = array();
		
		foreach ($list as $rev)
		{
			$docs = array();
			if (is_array($rev->revised_documents))
			{
				foreach ($rev->revised_documents as $doc)
				{
					$docs[] = (object) array (
						'guid' => $doc->guid,
						'document_guid' => $doc->document_guid,
					);
				}
			}
			
			$json[] = (object) array (
				'id' => $rev->revision->id,
				'revision_number' => $rev->revision_number,
				'author' => $rev->author->username,
				'date' => (int)$rev->revision->time_created,//date('j M y H:i',$rev->revision->time_created),
				'revised_docs' => $docs,
				'deleted_docs' => $rev->deleted_documents,
			);
		}
		
		return json_encode($json);
	}
	
	public static function encodeId ($id)
	{
		global $CONFIG;
		
		return base_convert($id + $CONFIG->salt, 10, 36);
	}
	
	public static function decodeId ($id)
	{
		global $CONFIG;
		
		return base_convert(strtolower($id), 36, 10) - $CONFIG->salt;
	}
	
	/**
	 * Return all the tags that I have access to.
	 */
	public static function getMyTags ()
	{
		$fields = array ('tags', 'discipline', 'pedagogical_approach');
		
		$returnObj = new stdClass();
		$aux = array();
		foreach ($fields as $field)
		{
			$returnObj->$field = array();
			$metadata =  find_metadata_noentity($field,'','object',LDS_ENTITY_TYPE,10000,0);
			if (is_string($metadata)) $metadata = array($metadata);
			if (is_array($metadata) && count($metadata))
			{
				
				//First remove duplicates
				foreach ($metadata as $m){
					$aux[] = $m->value;
				}
				$aux = array_unique($aux);
				
				foreach ($aux as $m)
				{
					$o = new stdClass();
					$o->value = $m;
					$returnObj->{$field}[] = $o;
				}
			}
			
			sort($returnObj->$field);
		}
		
		return $returnObj;
	}  
	
	/**
	 * Get the publication status of a given document GUID, along with the published id
	 * Possible statuses are:
	 * -1: Not published
	 * $document_guid: Published
	 * (an integer different than $document_guid): An old revision is published (republishable)
	 * 
	 * @param unknown_type $document_guid
	 */
	public static function getPublishedId ($document_guid)
	{
		$doc = get_entity($document_guid);
	
		if ($doc->published == '1')
		{
			return $doc->guid;
		}
		else
		{
			//Get all the revisions of this document
			$revisions = get_entities_from_metadata('document_guid',$doc->guid,'object','LdS_document_revision',0,10000,0,'time_created');
			
			//Check one of its revisions is marked as published.
			if (is_array($revisions) && count($revisions))
				foreach ($revisions as $rev)
					if ($rev->published == '1')
						return $rev->guid;
		}
		
		return -1;
	}
	
	public static function getPublishedEditorId ($document_guid)
	{
		$doc = get_entity($document_guid);
	
		if ($doc->published == '1')
		{
			return $doc->guid;
		}
		else
		{
			//Get all the revisions of this document
			$revisions = get_entities_from_metadata('document_guid',$doc->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
			
			//Check one of its revisions is marked as published.
			if (is_array($revisions) && count($revisions))
				foreach ($revisions as $rev)
					if ($rev->published == '1')
						return $rev->guid;
		}
		
		return -1;
	}
	
	/**
	 * Shortcut for counting 
	 */
	public static function countMyLds()
	{
		return self::getMyLds(0, 0, true);
	}
	
	/**
	 * Gets a list of LdS of which:
	 *  - I am the owner
	 *  - I have write permission
	 *  
	 *  Taken from the pages module (world_canread.php - which should be "canedit", function pages_get_entity_metadata_joins)
	 */
	public static function getMyLds($limit = 10, $offset = 0, $count = false, $site_guid = 0, $container_guid = null)
	{
		$type = 'object';
		$subtype = 'LdS';
		$owner_guid = 0;
		$order_by = 'time_updated DESC';
		
		global $CONFIG;
		if ($subtype === false || $subtype === null || $subtype === 0)
			return false;
		
		if ($order_by == "") $order_by = "e.time_created desc";
		$order_by = sanitise_string($order_by);
		$limit = (int)$limit;
		$offset = (int)$offset;
		$site_guid = (int) $site_guid;
		if ($site_guid == 0)
			$site_guid = $CONFIG->site_guid;
			

						
		$where = array();
		$where[] = "e.enabled='yes'";
		if (is_array($type)) {			
			$tempwhere = "";
			if (sizeof($type))
			foreach($type as $typekey => $subtypearray) {
				foreach($subtypearray as $subtypeval) {
					$typekey = sanitise_string($typekey);
					if (!empty($subtypeval)) {
						$subtypeval = (int) get_subtype_id($typekey, $subtypeval);
					} else {
						$subtypeval = 0;
					}
					if (!empty($tempwhere)) $tempwhere .= " or ";
					$tempwhere .= "(type = '{$typekey}' and subtype = {$subtypeval})";
				}								
			}
			if (!empty($tempwhere)) $where[] = "({$tempwhere})";
			
		} else {
		
			$type = sanitise_string($type);
			$subtype = get_subtype_id($type, $subtype);
			
			if ($type != "")
				$where[] = "type='$type'";
			if ($subtype!=="")
				$where[] = "subtype=$subtype";
				
		}
			
		if ($owner_guid != "") {
			if (!is_array($owner_guid)) {
				$owner_array = array($owner_guid);
				$owner_guid = (int) $owner_guid;
			//	$where[] = "owner_guid = '$owner_guid'";
			} else if (sizeof($owner_guid) > 0) {
				$owner_array = array_map('sanitise_int', $owner_guid);
			//  Cast every element to the owner_guid array to int
			//	$owner_guid = array_map("sanitise_int", $owner_guid);
			//	$owner_guid = implode(",",$owner_guid);
			//	$where[] = "owner_guid in ({$owner_guid})";
			}
			if (is_null($container_guid)) {
				$container_guid = $owner_array;
			}
		}
		if ($site_guid > 0)
			$where[] = "site_guid = {$site_guid}";

		if (!is_null($container_guid)) {
			if (is_array($container_guid)) {
				foreach($container_guid as $key => $val) $container_guid[$key] = (int) $val;
				$where[] = "container_guid in (" . implode(",",$container_guid) . ")";
			} else {
				$container_guid = (int) $container_guid;
				$where[] = "container_guid = {$container_guid}";
			}
		}
		
		$write_access_idstring=get_metastring_id('write_access_id');
		$where[] = "d.name_id = '{$write_access_idstring}'";
		
		if (!$count) {
			$query = " SELECT e.*, s.string
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where ";
		} else {
			$query = "SELECT count(guid) as total 
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where";
		}

		
		foreach ($where as $w)
			$query .= " $w and ";
			$query .= " ((e.owner_guid = {$_SESSION['user']->getGUID()}) OR (";
			$query .= ' s.string IN ';
			
		$query .= get_access_list($_SESSION['user']->guid, $site_guid, false); // Add access controls
		$query .= " and ".get_access_sql_suffix('e');
		
		$query .= " )) ";
		if (!$count) {
			$query .= " order by $order_by";
			
			if ($limit) $query .= " limit $offset, $limit"; // Add order and limit
//echo "$query<br/>";
//die;
			$dt = get_data($query, "entity_row_to_elggstar");
			return $dt;
		} else {
//echo "$query<br/>";
			$total = get_data_row($query);
			return $total->total;
		}
	}
	
	
	/**
	 * Shortcut for counting 
	 */
	public static function countLdsSharedWithMe()
	{
		return self::getMyLds(0, 0, true);
	}
	
	/**
	 * Gets a list of LdS of which:
	 *  - I am the owner
	 *  - I have write permission
	 *  
	 *  Taken from the pages module (world_canread.php - which should be "canedit", function pages_get_entity_metadata_joins)
	 */
	public static function getLdsSharedWithMe($limit = 10, $offset = 0, $count = false, $site_guid = 0, $container_guid = null)
	{
		$type = 'object';
		$subtype = 'LdS';
		$owner_guid = 0;
		$order_by = 'time_updated DESC';
		
		global $CONFIG;
		if ($subtype === false || $subtype === null || $subtype === 0)
			return false;
		
		if ($order_by == "") $order_by = "e.time_created desc";
		$order_by = sanitise_string($order_by);
		$limit = (int)$limit;
		$offset = (int)$offset;
		$site_guid = (int) $site_guid;
		if ($site_guid == 0)
			$site_guid = $CONFIG->site_guid;
			

						
		$where = array();
		
		if (is_array($type)) {			
			$tempwhere = "";
			if (sizeof($type))
			foreach($type as $typekey => $subtypearray) {
				foreach($subtypearray as $subtypeval) {
					$typekey = sanitise_string($typekey);
					if (!empty($subtypeval)) {
						$subtypeval = (int) get_subtype_id($typekey, $subtypeval);
					} else {
						$subtypeval = 0;
					}
					if (!empty($tempwhere)) $tempwhere .= " or ";
					$tempwhere .= "(type = '{$typekey}' and subtype = {$subtypeval})";
				}								
			}
			if (!empty($tempwhere)) $where[] = "({$tempwhere})";
			
		} else {
		
			$type = sanitise_string($type);
			$subtype = get_subtype_id($type, $subtype);
			
			if ($type != "")
				$where[] = "type='$type'";
			if ($subtype!=="")
				$where[] = "subtype=$subtype";
				
		}
			
		if ($owner_guid != "") {
			if (!is_array($owner_guid)) {
				$owner_array = array($owner_guid);
				$owner_guid = (int) $owner_guid;
			//	$where[] = "owner_guid = '$owner_guid'";
			} else if (sizeof($owner_guid) > 0) {
				$owner_array = array_map('sanitise_int', $owner_guid);
			//  Cast every element to the owner_guid array to int
			//	$owner_guid = array_map("sanitise_int", $owner_guid);
			//	$owner_guid = implode(",",$owner_guid);
			//	$where[] = "owner_guid in ({$owner_guid})";
			}
			if (is_null($container_guid)) {
				$container_guid = $owner_array;
			}
		}
		if ($site_guid > 0)
			$where[] = "site_guid = {$site_guid}";

		if (!is_null($container_guid)) {
			if (is_array($container_guid)) {
				foreach($container_guid as $key => $val) $container_guid[$key] = (int) $val;
				$where[] = "container_guid in (" . implode(",",$container_guid) . ")";
			} else {
				$container_guid = (int) $container_guid;
				$where[] = "container_guid = {$container_guid}";
			}
		}
		
		$write_access_idstring=get_metastring_id('write_access_id');
		$where[] = "d.name_id = '{$write_access_idstring}'";
		
		if (!$count) {
			$query = " SELECT e.*, s.string
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where ";
		} else {
			$query = "SELECT count(guid) as total 
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where";
		}

		
		foreach ($where as $w)
			$query .= " $w and ";
			$query .= " ((e.owner_guid <> {$_SESSION['user']->getGUID()}) AND (";
			$query .= ' s.string IN ';
			
		$query .= get_access_list($_SESSION['user']->guid, $site_guid, false); // Add access controls
		$query .= " and ".get_access_sql_suffix('e');
		
		$query .= " )) ";
		if (!$count) {
			$query .= " order by $order_by";
			
			if ($limit) $query .= " limit $offset, $limit"; // Add order and limit
//echo "$query<br/>";
			$dt = get_data($query, "entity_row_to_elggstar");
			return $dt;
		} else {
//echo "$query<br/>";
			$total = get_data_row($query);
			return $total->total;
		}
	}
	
	
	/**
	 * Gets a list of LdS that accomplish the following rules:
	 *  - I am not the owner of them
	 *  - I don't have write permissions on them
	 *  - I have read permissions on them
	 */
	public static function getBrowseLdsList ($order_by = "", $limit = 10, $offset = 0, $tagname = '', $tagvalue = '', $count = false, $site_guid = 0, $container_guid = null)
	{
		$type = 'object';
		$subtype = 'LdS';
		$owner_guid = 0;
		
		global $CONFIG;
		if ($subtype === false || $subtype === null || $subtype === 0)
			return false;
		
		if ($order_by == "") $order_by = "e.time_created desc";
		$order_by = sanitise_string($order_by);
		$limit = (int)$limit;
		$offset = (int)$offset;
		$site_guid = (int) $site_guid;
		if ($site_guid == 0)
			$site_guid = $CONFIG->site_guid;
			

						
		$where = array();
		
		if (is_array($type)) {			
			$tempwhere = "";
			if (sizeof($type))
			foreach($type as $typekey => $subtypearray) {
				foreach($subtypearray as $subtypeval) {
					$typekey = sanitise_string($typekey);
					if (!empty($subtypeval)) {
						$subtypeval = (int) get_subtype_id($typekey, $subtypeval);
					} else {
						$subtypeval = 0;
					}
					if (!empty($tempwhere)) $tempwhere .= " or ";
					$tempwhere .= "(type = '{$typekey}' and subtype = {$subtypeval})";
				}								
			}
			if (!empty($tempwhere)) $where[] = "({$tempwhere})";
			
		} else {
		
			$type = sanitise_string($type);
			$subtype = get_subtype_id($type, $subtype);
			
			if ($type != "")
				$where[] = "type='$type'";
			if ($subtype!=="")
				$where[] = "subtype=$subtype";
				
		}
			
		if ($owner_guid != "") {
			if (!is_array($owner_guid)) {
				$owner_array = array($owner_guid);
				$owner_guid = (int) $owner_guid;
			//	$where[] = "owner_guid = '$owner_guid'";
			} else if (sizeof($owner_guid) > 0) {
				$owner_array = array_map('sanitise_int', $owner_guid);
			//  Cast every element to the owner_guid array to int
			//	$owner_guid = array_map("sanitise_int", $owner_guid);
			//	$owner_guid = implode(",",$owner_guid);
			//	$where[] = "owner_guid in ({$owner_guid})";
			}
			if (is_null($container_guid)) {
				$container_guid = $owner_array;
			}
		}
		if ($site_guid > 0)
			$where[] = "site_guid = {$site_guid}";

		if (!is_null($container_guid)) {
			if (is_array($container_guid)) {
				foreach($container_guid as $key => $val) $container_guid[$key] = (int) $val;
				$where[] = "container_guid in (" . implode(",",$container_guid) . ")";
			} else {
				$container_guid = (int) $container_guid;
				$where[] = "container_guid = {$container_guid}";
			}
		}
		
		$where[] = 'e.access_id <> 0';
		$where[] = 'e.owner_guid <> '.get_loggedin_userid();
		
		$write_access_idstring=get_metastring_id('write_access_id');
		$where[] = "d.name_id = '{$write_access_idstring}'";
		
		
		$anotherjoin = '';
		if ($tagname != '' && $tagvalue != '')
		{
			$anotherjoin = ' LEFT JOIN metadata d2 ON e.guid = d2.entity_guid '; 
			
			$k = get_metastring_id($tagname);
			$v = get_metastring_id($tagvalue);
			$where[] = "d2.name_id = '{$k}'";
			$where[] = "d2.value_id = '{$v}'";
		}
		
		
		if (!$count) {
			$query = " SELECT e.*, s.string
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						$anotherjoin
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where ";
		} else {
			$query = "SELECT count(guid) as total 
						FROM  {$CONFIG->dbprefix}entities e
						JOIN  {$CONFIG->dbprefix}metadata d
						$anotherjoin
						JOIN  {$CONFIG->dbprefix}metastrings s 
						ON d.value_id = s.id
						and e.guid=d.entity_guid
						where";
		}

		
		foreach ($where as $w)
			$query .= " $w and ";
			//$query .= " ((e.owner_guid = {$_SESSION['user']->getGUID()}) OR (";
			$query .= ' s.string NOT IN ';
			
		$query .= get_access_list($_SESSION['user']->guid, $site_guid, false); // Add access controls
		$query .= " and ".self::get_browse_lds_access_sql_suffix('e');
		
		//$query .= " )) ";
		if (!$count) {
			$query .= " order by $order_by";
			
			if ($limit) $query .= " limit $offset, $limit"; // Add order and limit
//echo "$query<br/>";
			$dt = get_data($query, "entity_row_to_elggstar");
			return $dt;
		} else {
//echo "$query<br/>";
			$total = get_data_row($query);
			return $total->total;
		}
	}
	
	
	private static function get_browse_lds_access_sql_suffix($table_prefix = "")
	{
		global $ENTITY_SHOW_HIDDEN_OVERRIDE;  
		
		$sql = "";
		
		if ($table_prefix)
				$table_prefix = sanitise_string($table_prefix) . ".";
		
			$access = get_access_list();
			
			global $is_admin;
			
			if (isset($is_admin) && $is_admin == true) {
				$sql = " (1 = 1) ";
			}

			if (empty($sql))
				$sql = " ({$table_prefix}access_id in {$access})";

		if (!$ENTITY_SHOW_HIDDEN_OVERRIDE)
			$sql .= " and {$table_prefix}enabled='yes'";
		
		return $sql;
	}
	
	/**
	 * Returns the ID of the anonymous access collection containing all and only the users specified int
	 * the array $userIds.
	 * 
	 * If such collection doesn't exist, this function creates it.
	 * 
	 * @param array $userIds
	 */
	public static function getUserCollection ($userIds)
	{
		//The ids are sorted and coined with commas.
		//The resulting string is hashed with an md5 and two underscores are prepended.
		//This will be the collection name, so queries that search by name (which is a calculation of the contained members) can be super-fast. 
		sort($userIds);
		$colname = "__".md5(implode(',', $userIds));
		
		$col = get_access_collection_by_name($colname);
		
		//DOesn't exist. We create it.
		if (!$col)
		{
			$colid = create_access_collection($colname);
			update_access_collection($colid, $userIds);
			$col = get_access_collection($colid);
		}
		
		return $col->id;
	}
	
	public static function getFriendsArray ($userId)
	{
		//stupid limits
		$users = get_user_friends($userId, '', 100000, 0);
		$jsonusers = array();
		if (is_array($users) && count($users))
		{
			foreach ($users as $u)
			{
				$o = new stdClass();
				$o->guid = $u->guid;
				$o->name = $u->name;
				$o->pic = $u->getIcon('small');
				$jsonusers[] = $o;
			}
		}
		
		return $jsonusers;
	}

    public static function buildFriendArrays ($friendsArray, $access_id, $write_access_id)
    {
        $returnData = array();
        $returnData['available'] = $friendsArray;
        $returnData['viewers'] = array ();
        $returnData['editors'] = array ();

        $view_ids = get_members_of_access_collection($access_id, true) ?: array();
        $edit_ids = get_members_of_access_collection($write_access_id, true) ?: array();

        $view_ids = array_diff($view_ids, $edit_ids);

        foreach ($view_ids as $id)
        {
            foreach ($returnData['available'] as $k=>$v)
            {
                if ($returnData['available'][$k]->guid == $id)
                {
                    $o = new stdClass();
                    $o->guid = $returnData['available'][$k]->guid;
                    $o->name = $returnData['available'][$k]->name;
                    $o->pic = $returnData['available'][$k]->pic;
                    $returnData['viewers'][] = $o;

                    unset($returnData['available'][$k]);
                }
            }
        }

        foreach ($edit_ids as $id)
        {
            foreach ($returnData['available'] as $k=>$v)
            {
                if ($returnData['available'][$k]->guid == $id)
                {
                    $o = new stdClass();
                    $o->guid = $returnData['available'][$k]->guid;
                    $o->name = $returnData['available'][$k]->name;
                    $o->pic = $returnData['available'][$k]->pic;
                    $returnData['editors'][] = $o;

                    unset($returnData['available'][$k]);
                }
            }
        }

        return $returnData;
    }

    public static function getTotalViewersIds($lds_id)
    {
        $query = "SELECT r.guid_one AS guid FROM {$CONFIG->dbprefix}entity_relationships r ";
        $query .= " WHERE r.guid_two = {$lds_id} AND r.relationship IN ('lds_viewer', 'lds_editor', 'lds_viewer_group', 'lds_editor_group')";

        $guids = array();
        $result = execute_query ($query, get_db_link('read'));
        while($row = mysql_fetch_object($result)) {
            $guids[] = $row->guid;
        }

        return $guids;
    }

    public static function getEditorsIds($lds_id)
    {
        $query = "SELECT r.guid_one AS guid FROM {$CONFIG->dbprefix}entity_relationships r ";
        $query .= " WHERE r.guid_two = {$lds_id} AND r.relationship IN ('lds_editor', 'lds_editor_group')";

        $guids = array();
        $result = execute_query ($query, get_db_link('read'));
        while($row = mysql_fetch_object($result)) {
            $guids[] = $row->guid;
        }

        return $guids;
    }

    public static function getViewersIds($lds_id)
    {
        $query = "SELECT r.guid_one AS guid FROM {$CONFIG->dbprefix}entity_relationships r ";
        $query .= " WHERE r.guid_two = {$lds_id} AND r.relationship IN ('lds_viewer', 'lds_viewer_group')";

        $guids = array();
        $result = execute_query ($query, get_db_link('read'));
        while($row = mysql_fetch_object($result)) {
            $guids[] = $row->guid;
        }

        return $guids;
    }

    public static function getEditorsUsers($lds_id)
    {
        global $CONFIG;

        $query = "SELECT * from {$CONFIG->dbprefix}entities e WHERE e.guid IN";
        $query .= "(SELECT r.guid_one AS guid FROM {$CONFIG->dbprefix}entity_relationships r ";
        $query .= " WHERE r.guid_two = {$lds_id} AND r.relationship IN ('lds_editor', 'lds_editor_group'))";

        $entities = get_data($query, "entity_row_to_elggstar");

        return $entities;
    }

    public static function getViewersUsers($lds_id)
    {
        global $CONFIG;

        $query = "SELECT * from {$CONFIG->dbprefix}entities e WHERE e.guid IN";
        $query .= "(SELECT r.guid_one AS guid FROM {$CONFIG->dbprefix}entity_relationships r ";
        $query .= " WHERE r.guid_two = {$lds_id} AND r.relationship IN ('lds_viewer', 'lds_viewer_group'))";

        $entities = get_data($query, "entity_row_to_elggstar");

        return $entities;
    }

    public static function getViewAccessUsers($lds_id) {
        $members = array();

        $lds = get_entity($lds_id);
        if($lds->all_can_view == 'yes') {
            $site_users = get_entities('user','',0,'',9999);

            $unique_user_guids = array();
            foreach($site_users as $u) {
                $unique_user_guids[] = $u->getGUID();
            }

            return $unique_user_guids;
        }



        $viewers = self::getViewersUsers($lds_id);
        $editors = self::getEditorsUsers($lds_id);

        if(is_array($viewers))
            $members = array_merge($viewers, $members);

        if(is_array($editors))
            $members = array_merge($editors, $members);

        $users = array();

        foreach($members as $member) {
            if($member instanceof ElggUser)
                $users[] = $member;
            elseif ($member instanceof ElggGroup) {
                $users = array_merge($users, $member->getMembers(9999));
            }
        }

        $user_guids = array();
        foreach($users as $user) {
            $user_guids[] = $user->getGUID();
        }

        $user_guids[] = $lds->owner_guid;
        $unique_user_guids = array_unique($user_guids);

        return $unique_user_guids;
    }

    public static function getAvailableUsers($lds = null)
    {
        global $CONFIG;

        if(!$lds) {
            $user_id = get_loggedin_userid();
            $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE (
	e.type IN ('user', 'group') AND e.enabled = 'yes' AND e.guid <> {$user_id}
)
SQL;
            $entities = get_data($query, "entity_row_to_elggstar");

            return $entities;
        }

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE (
	e.type IN ('user', 'group') AND e.enabled = 'yes' AND e.guid <> {$lds->owner_guid}
	AND (
		NOT EXISTS (SELECT * FROM {$CONFIG->dbprefix}entity_relationships r WHERE e.guid = r.guid_one AND r.guid_two = {$lds->guid} AND r.relationship IN ('lds_viewer', 'lds_editor', 'lds_viewer_group', 'lds_editor_group')))
    AND (
        NOT EXISTS (SELECT * FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = e.guid AND rug.guid_two IN (
        	SELECT rg.guid_one FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.guid_two = {$lds->guid} AND (rg.relationship IN ('lds_viewer_group', 'lds_editor_group'))
        	)
		)
	)
)
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        return $entities;
    }

    public static function getUserSharedLdSWithMe($user_id, $count = false, $limit = 0, $offset = 0) {
        global $CONFIG;

        $query_limit = ($limit == 0) ? '' : "limit {$offset}, {$limit}";
        $subtype = get_subtype_id('object', 'LdS');

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = $subtype AND e.owner_guid <> {$user_id} AND e.enabled = 'yes' AND (
	(
		e.guid IN (
			SELECT DISTINCT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.relationship = 'lds_editor_group' AND rg.guid_one IN (
				SELECT rug.guid_two FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
			)
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship = 'lds_editor' AND ru.guid_one = {$user_id}
		)
	)
) order by time_updated desc {$query_limit}
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;

    }

    public static function getUserEditableImplementations($user_id, $count = false, $limit = 0, $offset = 0, $m_key = null, $m_value = null, $container_guid = null) {
        global $CONFIG;

        if(isadminloggedin()) {
            /*
            $query_limit = ($limit == 0) ? '' : "9999";
            if($count)
                return get_entities('object', 'LdS', 0, '', $query_limit, $offset, true);

            return get_entities('object', 'LdS', 0, '', $query_limit, $offset);
            */
            $container_guid_query = "";
            if($container_guid)
                $container_guid_query = "AND e.container_guid = {$container_guid}";

            $query_limit = ($limit == 0 || $count) ? '' : "limit {$offset}, {$limit}";
            $subtype = get_subtype_id('object', 'LdS_implementation');

            $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e JOIN objects_entity oe ON e.guid = oe.guid WHERE e.type = 'object' AND e.subtype = $subtype $container_guid_query AND e.enabled = 'yes' order by time_updated desc {$query_limit}
SQL;
            $entities = get_data($query, "entity_row_to_elggstar");

            if($count)
                return count($entities);

            return $entities;
        }

        $query_limit = ($limit == 0 || $count) ? '' : "limit {$offset}, {$limit}";
        $subtype = get_subtype_id('object', 'LdS_implementation');

        $metadata_join = "";
        $metadata_query = "";

        if($m_key && $m_value) {
            $metadata_key_id = get_metastring_id($m_key);
            $metadata_value_id = get_metastring_id($m_value);

            $metadata_join = 'JOIN metadata m ON e.guid = m.entity_guid';
            $metadata_query = "AND m.name_id = '{$metadata_key_id}' AND m.value_id = '{$metadata_value_id}'";
        }

        $container_guid_query = "";
        if($container_guid)
            $container_guid_query = "AND e.container_guid = {$container_guid}";

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e {$metadata_join} WHERE e.type = 'object' AND e.subtype = $subtype $container_guid_query AND e.enabled = 'yes' {$metadata_query}
AND (
	(e.owner_guid = {$user_id})
	OR
	(
		e.guid IN (
			SELECT DISTINCT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.relationship = 'lds_editor_group' AND rg.guid_one IN (
				SELECT rug.guid_two FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
			)
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship = 'lds_editor' AND ru.guid_one = {$user_id}
		)
	)
) order by time_updated desc {$query_limit}
SQL;
        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;

    }

    public static function getUserSharedImplementationWithMe($user_id, $count = false, $limit = 0, $offset = 0) {
        global $CONFIG;

        $query_limit = ($limit == 0) ? '' : "limit {$offset}, {$limit}";
        $subtype = get_subtype_id('object', 'LdS_implementation');

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = $subtype AND e.owner_guid <> {$user_id} AND e.enabled = 'yes' AND (
	(
		e.container_guid IN (
			SELECT DISTINCT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.relationship = 'lds_editor_group' AND rg.guid_one IN (
				SELECT rug.guid_two FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
			)
		)
	)
	OR
	(
		e.container_guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship = 'lds_editor' AND ru.guid_one = {$user_id}
		)
	)
) order by time_updated asc {$query_limit}
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;

    }

    public static function getUserEditableLdSs($user_id, $count = false, $limit = 0, $offset = 0, $m_key = null, $m_value = null) {
        global $CONFIG;

        if(isadminloggedin()) {
            /*
            $query_limit = ($limit == 0) ? '' : "9999";
            if($count)
                return get_entities('object', 'LdS', 0, '', $query_limit, $offset, true);

            return get_entities('object', 'LdS', 0, '', $query_limit, $offset);
            */
            $query_limit = ($limit == 0 || $count) ? '' : "limit {$offset}, {$limit}";
            $subtype = get_subtype_id('object', 'LdS');
            $user_id = get_loggedin_userid();

            $metadata_join = "";
            $metadata_query = "";

            if($m_key && $m_value) {
                $metadata_key_id = get_metastring_id($m_key);
                $metadata_value_id = get_metastring_id($m_value);

                $metadata_join = 'JOIN metadata m ON e.guid = m.entity_guid';
                $metadata_query = "AND m.name_id = '{$metadata_key_id}' AND m.value_id = '{$metadata_value_id}'";
            }

            $myfirstlds = mysql_real_escape_string(T("My first LdS"));

            $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e JOIN objects_entity oe ON e.guid = oe.guid {$metadata_join} WHERE e.type = 'object' AND e.subtype = $subtype AND e.enabled = 'yes' {$metadata_query} AND (
oe.title <> '{$myfirstlds}' OR e.owner_guid = {$user_id}
) order by time_updated desc {$query_limit}
SQL;
            $entities = get_data($query, "entity_row_to_elggstar");

            if($count)
                return count($entities);

            return $entities;
        }

        $query_limit = ($limit == 0 || $count) ? '' : "limit {$offset}, {$limit}";
        $subtype = get_subtype_id('object', 'LdS');

        $metadata_join = "";
        $metadata_query = "";

        if($m_key && $m_value) {
            $metadata_key_id = get_metastring_id($m_key);
            $metadata_value_id = get_metastring_id($m_value);

            $metadata_join = 'JOIN metadata m ON e.guid = m.entity_guid';
            $metadata_query = "AND m.name_id = '{$metadata_key_id}' AND m.value_id = '{$metadata_value_id}'";
        }

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e {$metadata_join} WHERE e.type = 'object' AND e.subtype = $subtype AND e.enabled = 'yes' {$metadata_query}
AND (
	(e.owner_guid = {$user_id})
	OR
	(
		e.guid IN (
			SELECT DISTINCT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.relationship = 'lds_editor_group' AND rg.guid_one IN (
				SELECT rug.guid_two FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
			)
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship = 'lds_editor' AND ru.guid_one = {$user_id}
		)
	)
) order by time_updated desc {$query_limit}
SQL;
        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;

    }

    public static function getUserViewableLdSs($user_id, $count = false, $limit = 0, $offset = 0, $mk = null, $mv = null) {
        global $CONFIG;

        if(isadminloggedin()) {
            if($mk && $mv) {
                $query_limit = ($limit == 0) ? '' : "9999";
                if($count)
                    return get_entities_from_metadata($mk, $mv, 'object', 'LdS', 0, $query_limit, $offset, '', 0, true);

                return get_entities_from_metadata($mk, $mv, 'object', 'LdS', 0, $query_limit, $offset, '', 0);
            }

            $query_limit = ($limit == 0) ? '' : "9999";
            if($count)
                return get_entities('object', 'LdS', 0, '', $query_limit, $offset, true);

            return get_entities('object', 'LdS', 0, '', $query_limit, $offset);
        }

        $query_limit = ($limit == 0 || $count) ? '' : "limit {$offset}, {$limit}";
        $subtype = get_subtype_id('object', 'LdS');

        //$mj = '';
        $mw = '';

        if($mk && $mv) {
            $mk_id = get_metastring_id($mk);
            $mv_id = get_metastring_id($mv);

            //$mj = "JOIN metadata m ON e.guid = m.entity_guid";
            $mw = "md.name_id='{$mk_id}' AND  md.value_id='{$mv_id}' AND";
        }

        $acv_k = get_metastring_id("all_can_view");
        $acv_v = get_metastring_id("yes");

        $query = <<<SQL
SELECT DISTINCT e.guid, e.type FROM {$CONFIG->dbprefix}entities e JOIN metadata m ON e.guid = m.entity_guid JOIN metadata md ON e.guid = md.entity_guid WHERE {$mw} e.type = 'object' AND e.subtype = $subtype AND e.enabled = 'yes' AND (
	(e.owner_guid = {$user_id})
	OR
	(
		m.name_id = '{$acv_k}' AND m.value_id = '{$acv_v}'
	)
	OR
	(
		e.guid NOT IN (
		    SELECT DISTINCT acvm.entity_guid FROM metadata acvm WHERE acvm.name_id = '{$acv_k}'
		)
		AND
		e.access_id < 3 AND e.access_id > 0
	)
	OR
	(
		e.guid IN (
			SELECT DISTINCT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.relationship IN ('lds_editor_group', 'lds_viewer_group') AND rg.guid_one IN (
				SELECT rug.guid_two FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id}
			)
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship IN ('lds_editor', 'lds_viewer') AND ru.guid_one = {$user_id}
		)
	)
) order by e.time_created asc {$query_limit}
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;

    }

    public static function getUsersEditedDesign($lds_id, $count = false) {
        global $CONFIG;

        $query = <<<SQL
SELECT DISTINCT e.guid, e.type FROM {$CONFIG->dbprefix}entities e JOIN {$CONFIG->dbprefix}annotations a ON a.owner_guid = e.guid WHERE a.entity_guid = {$lds_id} AND e.enabled = 'yes' AND e.access_id > 0
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;
    }

    public static function getModificationsByUser($lds_id, $user_id, $count = false) {
        global $CONFIG;

        $annotation_name_id = get_metastring_id('revised_docs');
        $annotation_name_id_2 = get_metastring_id('revised_docs_editor');

        $query = <<<SQL
SELECT DISTINCT e.guid, e.type FROM {$CONFIG->dbprefix}annotations a JOIN {$CONFIG->dbprefix}entities e ON a.entity_guid = e.guid WHERE (a.name_id = {$annotation_name_id} OR a.name_id = {$annotation_name_id_2}) AND a.owner_guid = {$user_id} AND a.entity_guid = {$lds_id} AND e.enabled = 'yes' AND e.access_id > 0
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;
    }

    public static function getModificationsLdSsByDate($user_id = 0, $start = null, $end = null, $count = false) {
        global $CONFIG;

        $annotation_name_id = get_metastring_id('revised_docs');
        $annotation_name_id_2 = get_metastring_id('revised_docs_editor');

        $user_q = '';
        if($user_id)
            $user_q = "AND a.owner_guid = {$user_id}";
        $query = <<<SQL
SELECT e.guid, e.type FROM {$CONFIG->dbprefix}entities e JOIN {$CONFIG->dbprefix}annotations a ON a.entity_guid = e.guid WHERE (a.name_id = {$annotation_name_id} OR a.name_id = {$annotation_name_id_2}) AND a.time_created >= {$start} AND a.time_created < {$end} AND e.enabled = 'yes' AND e.access_id > 0 {$user_q}
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return $entities;
    }


    public static function LdSCanEdit($lds_id, $user) {
        global $CONFIG;

        if($user->admin) {
            return true;
        }

        return self::LdSUserEditRights($lds_id, $user->guid);
    }

    public static function LdSUserEditRights($lds_id, $user_id) {
        global $CONFIG;

        $entity = get_entity($lds_id);
        //$subtype = get_subtype_id('object', 'LdS');
        $subtype = $entity->subtype;

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = $subtype AND e.guid = {$lds_id} AND (
	(e.owner_guid = {$user_id})
	OR
	(
		EXISTS (
			SELECT * FROM {$CONFIG->dbprefix}entity_relationships rug WHERE rug.relationship = 'member' AND rug.guid_one = {$user_id} AND rug.guid_two IN (
				SELECT rg.guid_one FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.guid_two = e.guid AND (rg.relationship = 'lds_editor_group')
			)
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE ru.relationship = 'lds_editor' AND ru.guid_one = {$user_id}
		)
	)
)
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($entities)
            return true;

        return false;

    }

    public static function stat_getSharedEditableLdSs($count = false) {
        global $CONFIG;

        $ms_yes = get_metastring_id("yes");
        $ms_no = get_metastring_id("no");
        $ms_all = get_metastring_id("all_can_view");
        $ms_welcome = get_metastring_id("welcome");
        if(!$ms_welcome)
            $ms_welcome = 0;
        $ms_true = get_metastring_id("1");

        $subtype = get_subtype_id('object', 'LdS');

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = $subtype AND e.enabled = 'yes' AND e.access_id > 0 AND (
	((
		EXISTS (
			SELECT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.guid_two = e.guid AND (rg.relationship = 'lds_editor_group')
		)
	)
	OR
	(
		e.guid IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE (ru.relationship = 'lds_editor') AND ru.guid_two = e.guid
		)
	))
	AND
	(
		NOT EXISTS (
			SELECT * FROM {$CONFIG->dbprefix}metadata m WHERE m.entity_guid = e.guid AND name_id = {$ms_welcome} AND value_id = {$ms_true}
		)
	)
	AND
	(
        NOT EXISTS (
          SELECT * FROM {$CONFIG->dbprefix}objects_entity o WHERE o.guid = e.guid AND o.title = 'My first LdS'
        )
	)
)
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return
            $entities;

    }

    public static function stat_getImplementedLdSs($count = false) {
        global $CONFIG;

        $m_lds_id = get_metastring_id("lds_id");
        $subtype = get_subtype_id('object', 'LdS');
        $subtype_implementation = get_subtype_id('object', 'LdS_implementation');

        $query = <<<SQL
SELECT DISTINCT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = {$subtype} AND e.enabled = 'yes' AND e.access_id > 0 AND (
	(
		EXISTS (
			SELECT DISTINCT ei.guid FROM {$CONFIG->dbprefix}entities ei JOIN metadata mi ON ei.guid = mi.entity_guid WHERE ei.subtype = {$subtype_implementation} AND ei.enabled = 'yes' AND ei.access_id > 0 AND mi.name_id = {$m_lds_id} AND mi.value_id IN (
		      SELECT DISTINCT msi.id FROM {$CONFIG->dbprefix}metastrings msi WHERE msi.string = e.guid
			)
		)
	)
)
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return
            $entities;

    }

    public static function getPrivateDesigns($user_id = 0, $count = false) {
        global $CONFIG;

        $ms_yes = get_metastring_id("yes");
        $ms_no = get_metastring_id("no");
        $ms_all = get_metastring_id("all_can_view");
        $ms_welcome = get_metastring_id("welcome");
        if(!$ms_welcome)
            $ms_welcome = 0;
        $ms_true = get_metastring_id("1");

        $user_id_query = "";
        if($user_id)
            $user_id_query = "AND e.owner_guid = {$user_id}";

        $subtype = get_subtype_id('object', 'LdS');

        $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = 'object' AND e.subtype = $subtype AND e.enabled = 'yes' AND e.access_id > 0 {$user_id_query} AND (
	(
		NOT EXISTS (
			SELECT rg.guid_two FROM {$CONFIG->dbprefix}entity_relationships rg WHERE rg.guid_two = e.guid AND (rg.relationship = 'lds_editor_group' OR rg.relationship = 'lds_viewer_group')
		)
	)
	AND
	(
		e.guid NOT IN (
			SELECT ru.guid_two FROM {$CONFIG->dbprefix}entity_relationships ru WHERE (ru.relationship = 'lds_editor' OR ru.relationship = 'lds_viewer') AND ru.guid_two = e.guid
		)
	)
	AND
	(
		NOT EXISTS (
			SELECT * FROM {$CONFIG->dbprefix}metadata m WHERE m.entity_guid = e.guid AND name_id = {$ms_all} AND value_id = {$ms_yes}
		)
	)
	AND
	(
		NOT EXISTS (
			SELECT * FROM {$CONFIG->dbprefix}metadata m WHERE m.entity_guid = e.guid AND name_id = {$ms_welcome} AND value_id = {$ms_true}
		)
	)
	AND
	(
        NOT EXISTS (
          SELECT * FROM {$CONFIG->dbprefix}objects_entity o WHERE o.guid = e.guid AND o.title = 'My first LdS'
        )
	)
)
SQL;

        $entities = get_data($query, "entity_row_to_elggstar");

        if($count)
            return count($entities);

        return
            $entities;

    }

    public static function stat_getReadSharedUsers($lds_id) {
        $members = array();

        $lds = get_entity($lds_id);

        $viewers = array();
        $editors = self::getEditorsUsers($lds_id);

        if(is_array($viewers))
            $members = array_merge($viewers, $members);

        if(is_array($editors))
            $members = array_merge($editors, $members);

        $users = array();

        foreach($members as $member) {
            if($member instanceof ElggUser)
                $users[] = $member;
            elseif ($member instanceof ElggGroup) {
                $users = array_merge($users, $member->getMembers(9999));
            }
        }

        $user_guids = array();
        foreach($users as $user) {
            $user_guids[] = $user->getGUID();
        }

        //$user_guids[] = $lds->owner_guid;
        $unique_user_guids = array_unique($user_guids);

        return $unique_user_guids;
    }

    public static function buildObjectsArray($lds = null)	{
        $objects = array();

        $available = self::getAvailableUsers($lds);
        $objects['available'] = self::entitiesToObjects($available);

        $viewers = self::getViewersUsers($lds->guid);
        $objects['viewers'] = self::entitiesToObjects($viewers);

        $editors = self::getEditorsUsers($lds->guid);
        $objects['editors'] = self::entitiesToObjects($editors);

        return $objects;
    }

    public static function entitiesToObjects($entities)	{
        $object_list = array();

        if($entities) {
            foreach($entities as $e) {
                $data = new stdClass();
                $data->guid = $e->guid;
                $data->name = $e->name;
                $data->pic = $e->getIcon('small');

                $object_list[] = $data;
            }
        }
        return $object_list;
    }
	
	/**
	 * Returns all the tags of the LdS I see in the browse section
	 * Use if if we filter out the LdS I can edit
	 */
	public static function getBrowseTagsAndFrequencies ($userId)
	{
		$fields = array ('tags', 'discipline', 'pedagogical_approach');
		
		$lds = self::getBrowseLdsList('', 10000, 0);

		if (!is_array($lds)) return false;
		
		$ids = array (); 
		foreach ($lds as $item)
			$ids[] = $item->guid;
		
		$tags = array();
		$fieldids = array();
		foreach ($fields as $field)
		{
			$fieldids[] = get_metastring_id($field);
			$tags[$field] = array();
		}
		
		if (count($ids))
		{
			$sql = "SELECT k.string AS k, v.string AS v FROM {$CONFIG->dbprefix}metadata m LEFT JOIN {$CONFIG->dbprefix}metastrings k ON m.name_id = k.id LEFT JOIN {$CONFIG->dbprefix}metastrings v ON m.value_id = v.id WHERE entity_guid IN (".implode(',',$ids).") AND m.name_id IN (".implode(',', $fieldids).")";
			$res = execute_query ($sql, get_db_link('read'));
			while ($row = mysql_fetch_object($res)) {
				if (isset ($tags[$row->k][$row->v]))
					$tags[$row->k][$row->v]++;
				else 
					$tags[$row->k][$row->v] = 1;
			}
			
			foreach ($tags as &$tagclass)
				arsort($tagclass);
			
			return $tags;
		}
		else
		{
			return false;
		}
		
	}
	
	public static function getAllTagsAndFrequencies ($userId)
	{
		$fields = array ('tags', 'discipline', 'pedagogical_approach');
		
		//$lds = get_entities('object','LdS',0,'',100000,0);
        $lds = self::getUserViewableLdSs(get_loggedin_userid());
		
		if (!is_array($lds)) return false;
		
		$ids = array (); 
		foreach ($lds as $item)
			$ids[] = $item->guid;
		
		$tags = array();
		$fieldids = array();
		foreach ($fields as $field)
		{
			$fieldids[] = get_metastring_id($field);
			$tags[$field] = array();
		}
		
		$fieldids = array_filter($fieldids);
		
		if (count($ids) && count($fieldids))
		{
			$sql = "SELECT DISTINCT entity_guid, k.string AS k, v.string AS v FROM {$CONFIG->dbprefix}metadata m LEFT JOIN {$CONFIG->dbprefix}metastrings k ON m.name_id = k.id LEFT JOIN {$CONFIG->dbprefix}metastrings v ON m.value_id = v.id WHERE entity_guid IN (".implode(',',$ids).") AND m.name_id IN (".implode(',', $fieldids).")";
			$res = execute_query ($sql, get_db_link('read'));
			while ($row = mysql_fetch_object($res)) {
				if (isset ($tags[$row->k][$row->v]))
					$tags[$row->k][$row->v]++;
				else 
					$tags[$row->k][$row->v] = 1;
			}
			
			foreach ($tags as &$tagclass)
				//arsort($tagclass);
				ksort($tagclass);
			
			return $tags;
		}
		else
		{
			return false;
		}
	}

    public static function build_notification($notification, $guid_list)
    {
        switch($notification->notification_type) {
            case "comment":
                return self::build_comment_notification($notification, $guid_list);
                break;

            case "newlds":
                return self::build_newlds_notification($notification, $guid_list);
                break;

        }
    }

    public static function getVLE()
    {
        if ($vles = get_entities('object', 'vle_data', get_loggedin_userid())) {
            return $vles[0];
        } else {
            $vle = new ElggObject();
            $vle->subtype = 'vle_data';
            $vle->url = '';
            $vle->username = '';
            $vle->password = '';
            $vle->type = '';
            $vle->save();
            return $vle;
        }

        return false;
    }

    public static function getVLECourses($vle)
    {
        ////REST CALLS
        ////
        $courses = array(
            array('id' => '2_34', 'name' => 'Agora VLE, English level A1'),
            array('id' => '2_74', 'name' => 'Agora VLE, English level A2'),
            array('id' => '2_82', 'name' => 'Agora VLE, Alphabetization course'),
        );

        return $courses;
    }

    private function build_comment_notification($notification, $guid_list) {
        $vars['ldsTitle'] = $notification->title;
        $vars['fromName'] = $notification->name;
        $vars['comment'] = $notification->comment_text;
        $vars['ldsUrl'] = $notification->lds_url;
        $vars['senderUrl'] = $notification->sender_url;

        $body = elgg_view('emails/newcomment',$vars);

        $notification_data = array();
        foreach ($guid_list as $guid) {
            $guid_data = array();
            $guid_data['sender_guid'] = $notification->owner_guid;
            $guid_data['recipient_guid'] = $guid;
            $guid_data['title'] = $notification->title;
            $guid_data['body'] = $body;
            $notification_data[] = $guid_data;
        }

        return $notification_data;
    }

    private function build_newlds_notification($notification, $guid_list) {
        $vars['ldsTitle'] = $notification->title;
        $vars['fromName'] = $notification->name;
        $vars['ldsUrl'] = $notification->lds_url;
        $vars['senderUrl'] = $notification->sender_url;

        $body = elgg_view('emails/newlds',$vars);

        $notification_data = array();
        foreach ($guid_list as $guid) {
            $guid_data = array();
            $guid_data['sender_guid'] = $notification->owner_guid;
            $guid_data['recipient_guid'] = $guid;
            $guid_data['title'] = $notification->title;
            $guid_data['body'] = $body;
            $notification_data[] = $guid_data;
        }

        return $notification_data;
    }

    ///PFC maria
    public static function filterWords($list){

        //Lista de palabras vacías de Google
        $stopWord=array("I", "a", "about", "an", "are", "as", "at", "be", "by", "com", "for", "from", "how", "in", "is", "it", 'of', "on", "or", "that", "the", "this", "to", "was", "what", "when", "where", "who", "will", "with", "the", "www");
        //comprobamos si la palabra esta en el array
        for($i=0; $i<=count($list); ++$i){
            if(in_array($list[$i], $stopWord)){
                unset($list[$i]);

            }
        }

        //Eliminamos palabras repetidas y posiciones vacias
        $listResult=array_values(array_unique($list));
        $listResult=array_values(array_filter($listResult));
        return $listResult;

    }

    public static function callOntology(){
        $listOntology=array();
        $numPatterns=16;
        $java_obj=new Java("parser.OwlPaser0513");
        $lista=java("java.util.HashMap");

        $lista=$java_obj->dameKeywords();

        $it=java("java.util.Iterator");
        //  echo $lista=java_values($lista->toArray());
        $cont=0;
        $it=$lista->entrySet()->iterator();
        while($cont<$numPatterns){
            //$it->hasNext()!=0){
            $cont++;
            $e=java("java.util.Map");
            $e=$it->next();
            //  $patron=$e->getKey();
            //$e=java_values($e);
            $patron=java_values($e->getKey());
            $patron=explode('_', $patron);
            $patron=$patron[1];
            $keywords=java_values($e->getValue());
            //echo $patron . "</br>". "</br>";
//echo $keywords . "</br>" . "</br>" . "</br>" . $cont;
            $listOntology[$patron]=$keywords;


        }
        return $listOntology;
    }
    public static function searchPatterns($query) {

        $contWord = array();
        $query = addslashes($query);
        //Llamamos a WordNet para que nos devuelva la lista con las palabras de la query y sus sinónimos e hièrónimos
        $totalList = lds_contTools::searchWordnet($query);
        //Filtramos tal lista para asegurarnos que no contenga ni palabras vacías ni repetidas
        $listQueryWord = lds_contTools::filterWords($totalList);
        //Llamamos a la ontología
        $listTextOntology= lds_contTools::callOntology();

        //$listTextOntology = array("JIGSAW" => "cat dog feline blabla to ", "PYRAMID" => "cat, cat", "TPS" => "dog, familiaris canine familiariss", "SIMULATION"=>"dog, canine");
        $listTextOntology=str_replace(",", " ", $listTextOntology);
        $listTextOntology_aux = $listTextOntology;
        $contWord = array();
        for ($i = 0; $i < count($listTextOntology_aux); ++$i) {
            $cont = 0;
            //Extraemos el primero
            $text = array_shift($listTextOntology);
            //El patrón del que pertenece
            $pattern = array_search($text, $listTextOntology_aux);

            $text = $text . " ";
            for ($j = 0; $j < count($listQueryWord); ++$j) {
                if (stristr($text, $listQueryWord[$j] . " ") != FALSE) {
                    ++$cont;
                }
            }

            if ($cont>0){
                $contWord[$pattern] = $cont;
            }
        }
        //ordenamos el array de mayor a menor
        arsort($contWord);

        return $contWord;
    }


    public static function searchSyns($text) {

        //Convertimos en array el bloque de texto, utilizando como delimitador los carácteres de espacio
        $list = preg_split('/[\s]+/', $text);
        //Creamos el array a rellenar con los sinonimos e hiperonimos
        $syns = array();
        //Ponemos el contador que nos determinará en el sentido que estamos a uno, el sentido 1.
        $cont = 1;

        //Recorremos el array resultado de dividir el bloque de texto
        for ($j = 0; $j <= count($list); ++$j) {

            //Si encontramos un sentido de la plabra y es de los tres primeros
            if (strcmp("Sense", $list[$j]) == 0 && $cont <= 3) {
                //Puntero auxiliar que se podiciona en la primera palabra del sentido correspondiente de la palabra
                $k = $j + 1;
                //Recorremos el resto de la lista
                while ($k <= (count($list))) {

                    //Eliminamos los antónimos
                    if (strcmp('(vs.', $list[$k]) == 0) {
                        $k = 2 + $k;
                    }
                    //Si sigue perteneciendo al mismo sentido lo guardamos
                    if (strcmp("Sense", $list[$k]) != 0) {
                        //Guardamos el string en nuestro array
                        array_push($syns, $list[$k]);
                        //Se aumenta el puntero
                        ++$k;
                    }
                    //Si cambiamos de sentido salimos del bucle while para buscar el siguiente sentido
                    else {
                        break 1;
                    }
                }
                //Aumentamos el contador
                $cont = $cont + 1;
            }
        }

        //Eliminamos numero, comas y demás texto no deseado
        $syns = preg_replace('/[0-9,;#=>()-]/', "", $syns);
        $syns = str_replace('prenominal', "", $syns);
        $syns = str_replace('predicate', "", $syns);

        //Eliminamos las palabras que empiecen por mayúsculas.
        for ($i = 0; $i <= count($syns); ++$i) {
            //Comprobamos si la palabra tiene todos sus carácteres en minúscula
            if (!(ctype_lower($syns[$i]))) {
                //Si tienen carácteres en mayúscula eliminamos la palabra del array
                unset($syns[$i]);
            }
        }

        //Eliminamos los terminos vacios
        $syns = array_filter($syns);
        //Reposicionamos el array
        $syns = array_values($syns);

        return $syns;
    }

    public static function searchWordnet($query) {

        //Pasamos a minúsculas todo el string de la query, para evitar problemas
        $query = strtolower($query);
        $listWord = array(); //lista a devolver con todas las palabras de la query y sus sinónimos e hiperónimos
        $dirWordNet = "/usr/local/WordNet-3.0/bin/wn ";
        $queryWord = explode(" ", $query);
        for ($i = 0; $i <= count($queryWord); ++$i) {
            $synonims[$i] = array();
            //Sinonimos de objeto
            $resultN[$i] = shell_exec($dirWordNet . $queryWord[$i] . " -synsn");
            $synsN[$i] = lds_contTools::searchSyns($resultN[$i]);
            //Sinonimo de verbo
            $resultV[$i] = shell_exec($dirWordNet . $queryWord[$i] . " -synsv");
            $synsV[$i] = lds_contTools::searchSyns($resultV[$i]);
            //Sinonimo del adverbio
            $resultR[$i] = shell_exec($dirWordNet . $queryWord[$i] . " -synsr");
            $synsR[$i] = lds_contTools::searchSyns($resultR[$i]);
            //Sinonimo del adjetivo
            $resultA[$i] = shell_exec($dirWordNet . $queryWord[$i] . " -synsa");
            $synsA[$i] = lds_contTools::searchSyns($resultA[$i]);

            //Unimos todos los sinónimos resultantes en un mismo array
            $synonims[$i] = array_merge($synsN[$i], $synsV[$i], $synsR[$i], $synsA[$i]);


            //Unir todos los sinónimos y las palabras query
            $listWord[] = $queryWord[$i];
            $listWord = array_merge($listWord, $synonims[$i]);
        }

        return $listWord;
    }

    public static function encrypt_password($plaintext) {
        global $CONFIG;
        $key = pack('H*', $CONFIG->vle_key);
        $iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_128, MCRYPT_MODE_CBC);
        $iv = mcrypt_create_iv($iv_size, MCRYPT_RAND);

        $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key,
            $plaintext, MCRYPT_MODE_CBC, $iv);

        $ciphertext = $iv . $ciphertext;

        return base64_encode($ciphertext);
    }

    public static function decrypt_password($ciphertext) {
        global $CONFIG;
        $key = pack('H*', $CONFIG->vle_key);
        $ciphertext_dec = base64_decode($ciphertext);

        $iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_128, MCRYPT_MODE_CBC);
        $iv_dec = substr($ciphertext_dec, 0, $iv_size);

        $ciphertext_dec = substr($ciphertext_dec, $iv_size);

        return rtrim(mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $key,
            $ciphertext_dec, MCRYPT_MODE_CBC, $iv_dec), "\0");
    }

    public static function tool_lang($tool, $lang) {
        $ilde_tools_lang = array(
            'webcollagerest' => array('ca','en','es'),
            'gluepsrest' => array('ca','en','es')
        );

        if(in_array($lang, $ilde_tools_lang[$tool]))
            return $lang;
        else
            return 'en';
    }
}