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
 *
 * LdS controller
 */

define ('LDS_ENTITY_TYPE', 'LdS');

function lds_init()
{	
	global $CONFIG;
	
	//Load the model classes
	//TODO could include the whole directory...
	require_once __DIR__.'/model/LdSObject.php';
	require_once __DIR__.'/model/DocumentObject.php';
	require_once __DIR__.'/model/DocumentEditorObject.php';
	require_once __DIR__.'/model/DocumentRevisionObject.php';
	require_once __DIR__.'/model/DocumentEditorRevisionObject.php';
	require_once __DIR__.'/model/DeferredNotification.php';
	
	require_once __DIR__.'/editors/editorsFactory.php';
	
	//Our css stuff
	extend_view('css','lds/css');
	
	//And our js scripts
	extend_view('page_elements/jsarea', 'lds/js');
	
	//We want the /lds/whatever urls to be ours
	register_page_handler('lds','lds_page_handler');
	
	//LdS actions:
	register_action("lds/save", false, $CONFIG->pluginspath . "lds/actions/lds/save.php");
	register_action("lds/save_editor", false, $CONFIG->pluginspath . "lds/actions/lds/save_editor.php");
	register_action("lds/delete", false, $CONFIG->pluginspath . "lds/actions/lds/delete.php");
	register_action("lds/recover", false, $CONFIG->pluginspath . "lds/actions/lds/recover.php");
	register_action("lds/upload", false, $CONFIG->pluginspath . "lds/actions/lds/upload.php");
	register_action("lds/publish", false, $CONFIG->pluginspath . "lds/actions/lds/publish.php");
	register_action("lds/publish_editor", false, $CONFIG->pluginspath . "lds/actions/lds/publish_editor.php");
	register_action("lds/share", false, $CONFIG->pluginspath . "lds/actions/lds/share.php");
	register_action("lds/ping_editing", false, $CONFIG->pluginspath . "lds/actions/lds/ping_editing.php");
	register_action("lds/pdf_export", false, $CONFIG->pluginspath . "lds/actions/lds/pdf_export.php");
	register_action("lds/pdf_export_editor", false, $CONFIG->pluginspath . "lds/actions/lds/pdf_export_editor.php");
	register_action("lds/ping_editing_editor", false, $CONFIG->pluginspath . "lds/actions/lds/ping_editing_editor.php");
	register_action("lds/file_export", false, $CONFIG->pluginspath . "lds/actions/lds/file_export.php");
	register_action("lds/mass_add_user", false, $CONFIG->pluginspath . "lds/actions/lds/mass_add_user.php");
	register_action("lds/deferred_send", false, $CONFIG->pluginspath . "lds/actions/lds/deferred_send.php");
	register_action("lds/import_editor_file", false, $CONFIG->pluginspath . "lds/actions/lds/import_editor_file.php");

	//Include the helper functions
	require_once __DIR__.'/lds_contTools.php';
	require_once __DIR__.'/lds_viewTools.php';
}

register_elgg_event_handler('init','system','lds_init');

register_plugin_hook('permissions_check', 'object', 'lds_write_permission_check');

function lds_write_permission_check($hook, $entity_type, $returnvalue, $params)
{
    $subtype = $params['entity']->getSubtype();

    //if ($subtype == 'LdS' || $params['entity']->getSubtype() == 'LdS_document' || $params['entity']->getSubtype() == 'LdS_document_editor') {

    if ($subtype == 'LdS') {
        return lds_contTools::LdSCanEdit($params['entity']->guid, $params['user']);

        /*
        $write_permission = $params['entity']->write_access_id ?: 0;
        $user = $params['user'];

        if (($write_permission) && ($user))
        {
            $list_ini=get_members_of_access_collection($write_permission, $idonly = false);
            $list=array();
            if (is_array($list_ini))
            {
                foreach ($list_ini as $member){
                    $list[]=$member[guid];
                }
            }

            if (
                (($write_permission!=0) && (in_array($user->guid,$list)))
                ||
                ($params['entity']->owner_guid==$user->guid)
                ||
                (($write_permission==1) && (isloggedin()))
                ||($write_permission==2)
                ){
                return true;
                }
        }
        */
    }
}

function lds_page_handler ($page)
{
	//Nothing here will be exposed to non-logged users, so go away!
	//UPDATE: Except for the external view...

	if ($page[0] != 'viewext' && $page[0] != 'viewexteditor' && $page[0] != 'imgdisplay')
		gatekeeper();
	
	//Special case: my lds's url is short: doesn't have an explicit section, so we add it automatically
	if ($page[0] == '' || $page[0] == 'created-by-me' || $page[0] == 'shared-with-me')
		array_unshift($page, 'main');
		
	//Sub_controller dispatcher
	if (function_exists("lds_exec_{$page[0]}"))
	{
		//If the user is new, we'll create an LdS for her
		$user = get_loggedin_user();
		if ($user->isnew == '1')
		{
			//Pau: Add a welcome LdS
			//present in actions/register.php and actions/useradd.php
			$wl = new LdSObject();
			$wl->acces_id = 0;
			$wl->write_access_id = 0;
			$wl->title = "My first LdS";
			$wl->owner_guid = $user->guid;
			$wl->container_guid = $user->guid;
			$wl->granularity = '0';
			$wl->completeness = '0';
			$wl->save ();
			
			$doc = new DocumentObject($wl->guid);
			$doc->title = "My first LdS";
			$doc->description = elgg_view('lds/welcome_lds');
			$doc->owner_guid = $user->guid;
			$doc->container_guid = $user->guid;
			$doc->access_id = '2';
			$doc->write_access_id = '2';
			$doc->save();
			//End add a welcome LdS
			
			if ($user->ispati == '1') {
				//Pau: Add a Pati LdS
				//present in actions/register.php and actions/useradd.php
				$wl = new LdSObject();
				$wl->acces_id = 0;
				$wl->write_access_id = 0;
				$wl->title = "Diseño de actividad con QuesTInSitu";
				$wl->owner_guid = $user->guid;
				$wl->container_guid = $user->guid;
				$wl->granularity = '0';
				$wl->completeness = '0';
				$wl->save ();
				
				$doc = new DocumentObject($wl->guid);
				$doc->title = "Diseño de actividad con QuesTInSitu";
				$doc->description = elgg_view('lds/QTIS_lds');
				$doc->owner_guid = $user->guid;
				$doc->container_guid = $user->guid;
				$doc->access_id = '2';
				$doc->write_access_id = '2';
				$doc->save();
				//End add a pati LdS

				$user->ispati = '0';
			}
			
			$user->isnew = '0';
			$user->save();
		}
		
		set_context("lds_exec_{$page[0]}");
		call_user_func("lds_exec_{$page[0]}", $page);
	}
	else
		lds_exec_404 ();
}

function lds_exec_main ($params)
{
    $offset = get_input('offset') ?: '0';

    if ($params[1] == 'created-by-me')
    {
        $vars['count'] = get_entities('object', 'LdS', get_loggedin_userid(), '', 50, $offset, true);
        $entities = get_entities('object', 'LdS', get_loggedin_userid(), 'time_updated DESC', 50, $offset);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("LdS created by me");
    }
    elseif ($params[1] == 'shared-with-me')
    {
        //$vars['count'] = lds_contTools::countLdsSharedWithMe();
        //$entities = lds_contTools::getLdsSharedWithMe(50, $offset);
        $vars['count'] = lds_contTools::getUserSharedLdSWithMe(get_loggedin_userid(), true);
        $entities = lds_contTools::getUserSharedLdSWithMe(get_loggedin_userid(), false, 50, $offset);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("LdS shared with me");
    }
    else
    {
        //$vars['count'] = lds_contTools::countMyLds();
        //$entities = lds_contTools::getMyLds(50, $offset);
        $vars['count'] = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), true);
        $entities = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), false, 50, $offset);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("All my LdS");
    }

    $vars['section'] = $params[1];
    $body = elgg_view('lds/mylds',$vars);

    page_draw($vars['title'], $body);
}

function lds_exec_search ($params) {
	$query = urldecode(get_input('q'));
	
	$vars['query'] = $query;
	$vars['list'] = lds_contTools::searchLdS($query);
	$vars['count'] = count ($vars['list']);
	
	$body = elgg_view('lds/search',$vars);
	page_draw($query, $body);
}

function lds_exec_trashed ($params)
{
	//We need to see all the entities, including the deleted ones.
	$access_status = access_get_show_hidden_status();
	access_show_hidden_entities(true);
	
	$offset = get_input('offset') ?: '0';
	
	//TODO take the owner into account
	$vars['list'] = get_entities_where('enabled = "no"', 'object', LDS_ENTITY_TYPE, get_loggedin_userid(), 'time_updated DESC', 50, $offset);
	$vars['count'] = get_entities_where('enabled = "no"', 'object', LDS_ENTITY_TYPE, get_loggedin_userid(), '', 0, $offset, true);
	$vars['list'] = lds_contTools::enrichLdS($vars['list']);
	$vars['title'] = T("My trashed LdS");

	$vars['section'] = $params[0];
	$body = elgg_view('lds/mylds',$vars);
	page_draw($vars['title'], $body);
	
	access_show_hidden_entities($access_status);
}

function lds_exec_viewtrashed ($params)
{
	//We need to see all the entities, including the deleted ones.
	$access_status = access_get_show_hidden_status();
	access_show_hidden_entities(true);
	
	$lds = get_entity($params[1]);
	$vars['lds'] = $lds;
	
	$vars['ldsDocs'] = lds_contTools::getLdsDocuments($lds->guid);
	$vars['currentDocId'] = $params[3] ?: $vars['ldsDocs'][0]->guid;
	$vars['currentDoc'] = get_entity($vars['currentDocId']);
	
	//Securuty: we won't show a document unless it's part of this LdS
	if ($vars['currentDoc']->lds_guid != $lds->guid)
	{
		lds_exec_404();
		return;	
	}
	
	$vars['section'] = $params[0];
	$body = elgg_view('lds/viewtrashed',$vars);
	page_draw($vars['title'], $body);
	
	access_show_hidden_entities($access_status);
}

function lds_exec_browse ($params)
{
	$order = get_input('order') ?: 'newest';
	$offset = get_input('offset') ?: '0';

	//$vars['tags'] = lds_contTools::getBrowseTagsAndFrequencies (get_loggedin_userid());
	$vars['tags'] = lds_contTools::getAllTagsAndFrequencies (get_loggedin_userid());

	$vars['filtering'] = false;
	//If there is some filtering by tag
    if (get_input('tagk') && get_input('tagv'))
    {
        $vars['tagk'] = urldecode(get_input('tagk'));
        $vars['tagv'] = urldecode(get_input('tagv'));

        $title = T("LdS tagged %1",$vars['tagv']);
        //Keep them just in case we want to recover the old functionality of listing the LdS which are not mine.
        //$vars['list'] = lds_contTools::getBrowseLdsList('time_updated DESC', 10, $offset, $vars['tagk'], $vars['tagv']);
        //$vars['count'] = lds_contTools::getBrowseLdsList('time_updated DESC', 10, $offset, $vars['tagk'], $vars['tagv'], true);

        //$vars['list'] = get_entities_from_metadata($vars['tagk'], $vars['tagv'], 'object', LDS_ENTITY_TYPE, 0, 10, $offset, '');
        //$vars['list'] = get_entities_from_metadata($vars['tagk'], $vars['tagv'], 'object', LDS_ENTITY_TYPE, 0, 9999, 0, '');
        $vars['list'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), false, 10, $offset, $vars['tagk'], $vars['tagv']);
        //$vars['count'] = get_entities_from_metadata($vars['tagk'], $vars['tagv'], 'object', LDS_ENTITY_TYPE, 0, 0, 0, '', 0, true);
        $vars['count'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), true, 0, 0, $vars['tagk'], $vars['tagv']);
        $vars['filtering'] = true;
    }
    //It's just a whole list
    else
    {
        $title = T("Browse LdS");

        $vars['list'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), false, 0, 0);
        $vars['count'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), true, 0, 0);
    }

    if(!is_array($vars['list'])) {
        $vars['list'] = array();
    }
    Utils::osort($vars['list'], array('title' => true));
    $vars['list'] = array_slice($vars['list'], $offset, 10);

    $vars['list'] = lds_contTools::enrichLdS($vars['list']);

    $body = elgg_view('lds/browse',$vars);
    page_draw($title, $body);
}

function lds_exec_new ($params)
{
	global $CONFIG;
	
	//Get the page that we come from (if we come from an editing form, we go back to my lds)
	$vars['referer'] = $CONFIG->url.'pg/lds/';
	
	//Create an empty LdS object to initialize the form
	$vars['initLdS'] = new stdClass();
	$vars['initLdS']->title = T("Untitled LdS");
	$vars['initLdS']->granularity = '0';
	$vars['initLdS']->completeness = '0';
	$vars['initLdS']->tags = '';
	$vars['initLdS']->discipline = '';
	$vars['initLdS']->pedagogical_approach = '';
	$vars['initLdS']->guid = '0';
	
	$vars['am_i_starter'] = true;
	
	$vars['all_can_read'] = 'true';
	
	$vars['initLdS'] = json_encode($vars['initLdS']);
	
	//And also an empty Document
	$vars['initDocuments'][0] = new stdClass();
	$vars['initDocuments'][0]->title = T("Untitled Document");
	$vars['initDocuments'][0]->guid = '0';
	$vars['initDocuments'][0]->modified = '0';
	$vars['initDocuments'][0]->body = '';
	
	//And a support doc!
	$vars['initDocuments'][1] = new stdClass();
	$vars['initDocuments'][1]->title = T("Support Document");
	$vars['initDocuments'][1]->guid = '0';
	$vars['initDocuments'][1]->modified = '0';
	$vars['initDocuments'][1]->body = '<p> '.T("Write here any support notes for this LdS...").'</p>';
	
	$vars['initDocuments'] = json_encode($vars['initDocuments']);
	
	$vars['tags'] = json_encode(lds_contTools::getMyTags ());

    $available = lds_contTools::getAvailableUsers(null);

    $vars['jsonfriends'] = json_encode(lds_contTools::entitiesToObjects($available));
    $vars['viewers'] = json_encode(array());
    $vars['editors'] = json_encode(array());
    $vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));

    $vars['starter'] = get_loggedin_user();

    $vars['title'] = T("New LdS");
    echo elgg_view('lds/editform',$vars);
}

function lds_exec_neweditor ($params)
{
	global $CONFIG;

	//Make an editor object according to the parameters received and create a new session
	$editor = editorsFactory::getTempInstance($params[1]);
	$vars = $editor->newEditor();
	
	//Get the page that we come from (if we come from an editing form, we go back to my lds)
	$vars['referer'] = $CONFIG->url.'pg/lds/';
	
	//Create an empty LdS object to initialize the form
	$vars['initLdS'] = new stdClass();
	$vars['initLdS']->title = 'Untitled LdS';
	$vars['initLdS']->granularity = '0';
	$vars['initLdS']->completeness = '0';
	$vars['initLdS']->tags = '';
	$vars['initLdS']->discipline = '';
	$vars['initLdS']->pedagogical_approach = '';
	$vars['initLdS']->guid = '0';

    //And a support doc!
    $vars['initDocuments'][0] = new stdClass();
    $vars['initDocuments'][0]->title = T("Support Document");
    $vars['initDocuments'][0]->guid = '0';
    $vars['initDocuments'][0]->modified = '0';
    $vars['initDocuments'][0]->body = '<p> '.T("Write here any support notes for this LdS...").'</p>';

    $vars['initDocuments'] = json_encode($vars['initDocuments']);

	$vars['all_can_read'] = 'true';
	$vars['initLdS'] = json_encode($vars['initLdS']);
	$vars['tags'] = json_encode(lds_contTools::getMyTags ());

    /*
	$vars['jsonfriends'] = json_encode(lds_contTools::getFriendsArray(get_loggedin_userid()));
	$vars['viewers'] = json_encode(array());
	$vars['editors'] = json_encode(array());
	$vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));
	*/

    $available = lds_contTools::getAvailableUsers(null);

    $vars['jsonfriends'] = json_encode(lds_contTools::entitiesToObjects($available));
    $vars['viewers'] = json_encode(array());
    $vars['editors'] = json_encode(array());
    $vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));

	$vars['starter'] = get_loggedin_user();
	
	$vars['am_i_starter'] = true;
	
	$vars['title'] = T("New LdS");
	
	echo elgg_view('lds/editform_editor',$vars);
}


/**
 * Pass the directory to import in the url
 */
function lds_exec_importexe ($params)
{
	global $CONFIG;
	set_time_limit (0);
	ini_set('memory_limit', '512M');
	
	array_shift($params);
	$params = array_filter($params);
	
	$basedir = 'E:/importexe/';
	
	$dir = $basedir . implode('/', $params);
	
	echo "Importing $dir...<br /><br /><br />";
	
	$iterator = new DirectoryIterator($dir);
	foreach ($iterator as $fileinfo) {
		if ($fileinfo->isFile()) {
			$pathname = $fileinfo->getPathname();
			$filename = $fileinfo->getFilename();
			$title = substr($filename, 0, strrpos($filename, '.'));

			echo "Importing $filename...";
			
			
			// ----------- IMPORT THINGIE ---------------
			//Make an editor object according to the parameters received and create a new session
			$editor = editorsFactory::getTempInstance('exe');
			$vars = $editor->importEditor($pathname);
		
			//We're creating it from scratch. Construct a new obj.
			$lds = new LdSObject();
			$lds->owner_guid = get_loggedin_userid();
			$lds->external_editor = true;
			$user = get_loggedin_user();
			$lds->save();
		
			$document_editor = new DocumentEditorObject($lds->guid, 0);
			$document_editor->editorType = 'exe';
			$document_editor->lds_guid = $lds->guid;
			$document_editor->lds_revision_id = 0;
			$document_editor->save();
			
			$lds->title = $title;
			$lds->granularity = 6;
			$lds->completeness = 1;
			$lds->access_id = 1;
			$lds->write_access_id = 0;
			$lds->save();
			
			
			//TODO tags
			
			
			//We save for the first time in this edition session. So we create a revision.
			create_annotation($lds->guid, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);
			
			//Save the exe document
			$editor = editorsFactory::getInstance($document_editor);
		
			list($document_editor, $resultIds_add) = $editor->saveDocument($vars['editor_id']);
		
			//We get the revision id to send it back to the form
			$editordocument = get_entities_from_metadata('lds_guid',$lds->guid,'object','LdS_document_editor', 0, 100);
			DocumentEditorRevisionObject::createRevisionFromDocumentEditor($editordocument[0]);
			
			// ----------- END OF IMPORT THINGIE ---------------
			echo " Done.<br />";			
		}
	}
	echo "Finished!";
}

function lds_exec_edit ($params)
{
	//Get the page that we come from (if we come from an editing form, we go back to my lds)
	if (preg_match('/(new|edit)/',$_SERVER['HTTP_REFERER']))
		$vars['referer'] = $CONFIG->url.'pg/lds/';
	else
		$vars['referer'] = $_SERVER['HTTP_REFERER'];
	
	$editLdS = get_entity($params[1]);
	
	if (!$editLdS->canEdit())
	{
		register_error("You don't have permissions to edit this LdS.");
		header("Location: " . $_SERVER['HTTP_REFERER']);
		return '';
	}
	
	if ($user = lds_contTools::isLockedBy($params[1]))
	{
		$fstword = explode(' ',$user->name);
		$fstword = $fstword[0];
		register_error("{$user->name} is editing this LdS. You cannot edit it until {$fstword} finishes.");
		header("Location: " . $_SERVER['HTTP_REFERER']);
	}
	
	lds_contTools::markLdSAsViewed ($params[1]);

	//Pass the LdS properties to the form
	$vars['initLdS'] = new stdClass();
	$vars['initLdS']->title = $editLdS->title;
	$vars['initLdS']->granularity = $editLdS->granularity;
	$vars['initLdS']->completeness = $editLdS->completeness;
	
	$tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
	foreach ($tagtypes as $type)
	{
		if (is_array($editLdS->$type))
			$vars['initLdS']->$type = implode(',',$editLdS->$type);
		elseif (is_string($editLdS->$type) && strlen($editLdS->$type))
			$vars['initLdS']->$type = $editLdS->$type;
		else
			$vars['initLdS']->$type = '';
	}
	
	$vars['initLdS']->guid = $params[1];
	$vars['am_i_starter'] = (get_loggedin_userid() == $editLdS->owner_guid);

    //in_array("all_can_view", in_array("all_can_view", metadata_array_to_values(get_metadata_for_entity($editLdS))););
    //$metadata = metadata_array_to_values(get_metadata_for_entity($editLdS));
    ///$value = $editLdS->all_can_view;

	$vars['all_can_read'] = ($editLdS->all_can_view == 'yes' || ($editLdS->all_can_view === null && $editLdS->access_id < 3 && $editLdS->access_id > 0)) ? 'true' : 'false';
	
	$vars['initLdS'] = json_encode($vars['initLdS']);
	
	//For each of the documents that this LdS has...
	$documents = get_entities_from_metadata('lds_guid',$params[1],'object','LdS_document', 0, 100);
	
	//Send their data to the form
	foreach ($documents as $doc)
	{
		$obj = new stdClass();
		$obj->title = $doc->title;
		$obj->guid = $doc->guid;
		$obj->body = $doc->description;
		$obj->modified = '0';
		$vars['initDocuments'][] = $obj;
	}
	
	Utils::osort($vars['initDocuments'], 'guid');
	
	$vars['initDocuments'] = json_encode($vars['initDocuments']);
	$vars['tags'] = json_encode(lds_contTools::getMyTags ()); 
	
	//These are all my friends
    $arrays = lds_contTools::buildObjectsArray($editLdS);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));
    $vars['starter'] = get_user($editLdS->owner_guid);

    $vars['title'] = T("Edit LdS");
    echo elgg_view('lds/editform',$vars);
}

function lds_exec_editeditor ($params)
{
	global $CONFIG;
	
	//Get the page that we come from (if we come from an editing form, we go back to my lds)
	if (preg_match('/(neweditor|editeditor)/',$_SERVER['HTTP_REFERER']))
		$vars['referer'] = $CONFIG->url.'pg/lds/';
	else
		$vars['referer'] = $_SERVER['HTTP_REFERER'];
	
	$editLdS = get_entity($params[1]);
	
	if (!$editLdS->canEdit())
	{
		register_error("You don't have permissions to edit this LdS.");
		header("Location: " . $_SERVER['HTTP_REFERER']);
	}
	
	if ($user = lds_contTools::isLockedBy($params[1]))
	{
		$fstword = explode(' ',$user->name);
		$fstword = $fstword[0];
		register_error("{$user->name} is editing this LdS. You cannot edit it until {$fstword} finishes.");
		header("Location: " . $_SERVER['HTTP_REFERER']);
	}
	
	lds_contTools::markLdSAsViewed ($params[1]);

	//Pass the LdS properties to the form
	$vars['initLdS'] = new stdClass();
	$vars['initLdS']->title = $editLdS->title;
	$vars['initLdS']->granularity = $editLdS->granularity;
	$vars['initLdS']->completeness = $editLdS->completeness;
	
	$tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
	foreach ($tagtypes as $type)
	{
		if (is_array($editLdS->$type))
			$vars['initLdS']->$type = implode(',',$editLdS->$type);
		elseif (is_string($editLdS->$type) && strlen($editLdS->$type))
			$vars['initLdS']->$type = $editLdS->$type;
		else
			$vars['initLdS']->$type = '';
	}

	$vars['initLdS']->guid = $params[1];

	//For each of the documents that this LdS has...
	$documents = get_entities_from_metadata('lds_guid',$params[1],'object','LdS_document', 0, 100);
	$vars['initDocuments'] = array();

	if(is_array($documents)) {
		//Send their data to the form
		foreach ($documents as $doc)
		{
			$obj = new stdClass();
			$obj->title = $doc->title;
			$obj->guid = $doc->guid;
			$obj->body = $doc->description;
			$obj->modified = '0';
			$vars['initDocuments'][] = $obj;
		}
	} else {
		$vars['initDocuments'][0] = new stdClass();
		$vars['initDocuments'][0]->title = T("Support Document");
		$vars['initDocuments'][0]->guid = '0';
		$vars['initDocuments'][0]->modified = '0';
		$vars['initDocuments'][0]->body = '<p> '.T("Write here any support notes for this LdS...").'</p>';
	}

	Utils::osort($vars['initDocuments'], 'guid');
	$vars['initDocuments'] = json_encode($vars['initDocuments']);

	$vars['am_i_starter'] = (get_loggedin_userid() == $editLdS->owner_guid);
	$vars['starter'] = get_user($editLdS->owner_guid);
	//$vars['all_can_read'] = ($editLdS->access_id == '1') ? 'true':'false';
    $vars['all_can_read'] = ($editLdS->all_can_view == 'yes' || ($editLdS->all_can_view === null && $editLdS->access_id < 3 && $editLdS->access_id > 0)) ? 'true' : 'false';
	$vars['initLdS'] = json_encode($vars['initLdS']);
	$vars['tags'] = json_encode(lds_contTools::getMyTags ()); 
	
	//These are all my friends
    /*
	$friends = lds_contTools::getFriendsArray(get_loggedin_userid());
	$arrays = lds_contTools::buildFriendArrays($friends, $editLdS->access_id, $editLdS->write_access_id);

	$vars['jsonfriends'] = json_encode(array_values($arrays['available']));
	$vars['viewers'] = json_encode($arrays['viewers']);
	$vars['editors'] = json_encode($arrays['editors']);
	$vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));
    */
    //These are all my friends
    $arrays = lds_contTools::buildObjectsArray($editLdS);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));

	$vars['title'] = T("Edit LdS");
	
	//We're editing. Fetch it from the DB
	$editordocument = get_entities_from_metadata('lds_guid',$editLdS->guid,'object','LdS_document_editor', 0, 100);

	//make an editor object with the document that we want to edit
	$editor = EditorsFactory::getInstance($editordocument[0]);
	$vars_editor = $editor->editDocument();
	$vars = $vars + $vars_editor;
	
	echo elgg_view('lds/editform_editor',$vars);
}

function lds_exec_history ($params)
{
	$vars['lds'] = get_entity($params[1]);
	
	//We need to see all the entities, including the deleted ones.
	$access_status = access_get_show_hidden_status();
	access_show_hidden_entities(true);
	
	$ldsDocs = lds_contTools::getLdsDocuments($params[1]);
	
	$vars['ldsDocs'] = $ldsDocs;
	$vars['numDocs'] = count ($ldsDocs);
	
	//We get all the document ids of this lds
	foreach ($ldsDocs as $doc)
		$docList[] = (object)array
		(
			'guid' => $doc->guid,
			'lds_revision_id' => $doc->lds_revision_id,
			'enabled' => $doc->enabled,
		);
	$vars['docList'] = $docList;
	//dprint($vars['docList']);
	$vars['JSONdocList'] = json_encode($docList);

	//TODO permission / exist checks
	$vars['history'] = array_reverse(lds_contTools::getRevisionList($vars['lds']));
	//dprint($vars['history']);
	$vars['numRevisions'] = count($vars['history']);
	$vars['JSONhistory'] = lds_contTools::getJSONRevisionList($vars['history']);
	
	$vars['title'] = T("LdS History");
	
	echo elgg_view('lds/history',$vars);
	//page_draw('LdS History', $body);
	
	access_show_hidden_entities($access_status);
}

function lds_exec_historyeditor ($params)
{
	$vars['lds'] = get_entity($params[1]);
	$editordocument = get_entities_from_metadata('lds_guid',$vars['lds']->guid,'object','LdS_document_editor', 0, 100);
	$document = $editordocument[0];
	$vars['editor'] = $document->editorType;
	$vars['document'] = $document;
	
//	//TODO permission / exist checks
	$vars['history'] = lds_contTools::getRevisionEditorList($vars['lds']);
	$vars['numRevisions'] = count($vars['history']);
	$vars['JSONhistory'] = lds_contTools::getJSONRevisionList($vars['history']);

	$vars['title'] = T("LdS History");
	
	echo elgg_view('lds/history_editor',$vars);
	//page_draw('LdS History', $body);
	
	access_show_hidden_entities($access_status);
}

function lds_exec_viewext ($params)
{
	global $CONFIG;

	//"Deobfuscate" the doc ID: from base 36 to base 10 - aaa(36)
	$id = base_convert(strtolower($params[1]), 36, 10) - $CONFIG->salt;
	
	$publishedId = lds_contTools::getPublishedId($id);

	//We didn't find any published thing, so 404.
	if ($publishedId == -1)
	{
		lds_exec_404('public');
		return;
	}

	$doc = get_entity($publishedId);
	$vars['doc'] = $doc;
	if (is_numeric($doc->document_guid))
		$vars['title'] = get_entity($doc->document_guid)->title;
	else
		$vars['title'] = $doc->title;
	
	echo elgg_view('lds/view_external',$vars); 
}

function lds_exec_viewexteditor ($params)
{
	global $CONFIG;

	//"Deobfuscate" the doc ID: from base 36 to base 10 - aaa(36)
	$id = base_convert(strtolower($params[1]), 36, 10) - $CONFIG->salt;
	
	$publishedId = lds_contTools::getPublishedEditorId($id);

	//We didn't find any published thing, so 404.
	if ($publishedId == -1)
	{
		lds_exec_404('public');
		return;
	}
	
	$vars['doc'] = get_entity($publishedId);
	$vars['lds'] = get_entity($vars['doc']->lds_guid);
	
	if(count($params) > 2)
	{
		//return the published file according to the second parameter
		if(strlen($params[2]) > 0)
		{
			switch ($params[2]) {
			    case 'ims_ld':
			        $file_guid = $vars['doc']->pub_ims_ld;
			        break;
			    case 'webZip':
			        $file_guid = $vars['doc']->pub_webZip;
			        break;
			    case 'scorm':
			        $file_guid = $vars['doc']->pub_scorm;
			        break;
			    case 'scorm2004':
			        $file_guid = $vars['doc']->pub_scorm2004;
			        break;
			    default:
			    	return '';
			    	break;
			}
			
			$file = get_entity($file_guid);
			$readfile = new ElggFile();
			$readfile->owner_guid = $file->owner_guid;
			$readfile->setFilename($file->originalfilename);
			$filename = $readfile->getFilenameOnFilestore();
			header('Content-Description: File Transfer');
		    header('Content-Type: application/octet-stream');
		    header('Content-Disposition: attachment; filename='.urlencode($vars['lds']->title.'_'.$params[2]).'.zip');
		    header('Content-Transfer-Encoding: binary');
		    header('Expires: 0');
		    header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
		    header('Pragma: public');
		    header('Content-Length: ' . filesize($filename));
		    ob_clean();
		    flush();
		    readfile($filename);
			return;
		}
	}

	echo elgg_view('lds/view_external_editor',$vars); 
}


//TODO rewrite urls.
function lds_exec_view ($params)
{
	$id = $params[1];
	$vars['lds'] = get_entity($id);
	//TODO permission / exist checks

	lds_contTools::markLdSAsViewed ($id);
	
	$vars['ldsDocs'] = lds_contTools::getLdsDocuments($id);
	$vars['currentDocId'] = $params[3] ?: $vars['ldsDocs'][0]->guid;
	$vars['currentDoc'] = get_entity($vars['currentDocId']);
	
	$vars['am_i_starter'] = (get_loggedin_userid() == $vars['lds']->owner_guid);
    $vars['starter'] = get_user($vars['lds']->owner_guid);

	//Securuty: we won't show a document unless it's part of this LdS
	if ($vars['currentDoc']->lds_guid != $id)
	{
		lds_exec_404();
		return;	
	}
		
	//Check if it's published
	$vars['publishedId'] = lds_contTools::getPublishedId($vars['currentDocId']);
	
	$vars['nComments'] = $vars['lds']->countAnnotations('generic_comment');
	
	//These are all my friends
    $arrays = lds_contTools::buildObjectsArray($vars['lds']);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    //$vars['jsonfriends'] = json_encode(array_values($arrays['available']));
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));

    //These are all my friends
    /*
    $arrays = lds_contTools::buildObjectsArray($editLdS);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));
	*/
    //These are all my friends
    $arrays = lds_contTools::buildObjectsArray($vars['lds']);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));
    $vars['starter'] = get_user($vars['lds']->owner_guid);

    //$vars['all_can_read'] = $vars['lds']->all_can_read;
    $vars['all_can_read'] = ($vars['lds']->all_can_view == 'yes' || ($vars['lds']->all_can_view === null && $vars['lds']->access_id < 3 && $vars['lds']->access_id > 0)) ? 'true' : 'false';

	$body = elgg_view('lds/view_internal',$vars);
	page_draw($vars['lds']->title. ': ' .$vars['currentDoc']->title, $body);
}

//TODO rewrite urls.
function lds_exec_vieweditor ($params)
{
	$id = $params[1];
	$lds = get_entity($id);
	$vars['lds'] = $lds;
	//TODO permission / exist checks

	lds_contTools::markLdSAsViewed ($id);

	$editordocument = get_entities_from_metadata('lds_guid',$lds->guid,'object','LdS_document_editor', 0, 100);

	$vars['editor'] = $editordocument[0]->editorType;

	$vars['ldsDocs'] = lds_contTools::getLdsDocuments($id);
	$vars['iseXe'] = $params[3] ? false : true;

	if($vars['iseXe']) {
		$vars['currentDocId'] = $editordocument[0]->guid;
		$vars['currentDoc'] = $editordocument[0];
		//Check if it's published
		$vars['publishedId'] = lds_contTools::getPublishedEditorId($vars['currentDocId']);
	} else {
		$vars['currentDocId'] = $params[3] ?: $vars['ldsDocs'][0]->guid;
		$vars['currentDoc'] = get_entity($vars['currentDocId']);
		//Check if it's published
		$vars['publishedId'] = lds_contTools::getPublishedId($vars['currentDocId']);
	}



	
	$vars['nComments'] = $vars['lds']->countAnnotations('generic_comment');

    /*
	//These are all my friends
	$friends = lds_contTools::getFriendsArray(get_loggedin_userid());
	$arrays = lds_contTools::buildFriendArrays($friends, $vars['lds']->access_id, $vars['lds']->write_access_id);

	$vars['jsonfriends'] = json_encode(array_values($arrays['available']));
	$vars['viewers'] = json_encode($arrays['viewers']);
	$vars['editors'] = json_encode($arrays['editors']);
	$vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));
    */
    //These are all my friends
    $arrays = lds_contTools::buildObjectsArray($vars['lds']);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    //$vars['jsonfriends'] = json_encode(array_values($arrays['available']));
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));
	
	$vars['starter'] = get_user($vars['lds']->owner_guid);
	
	$vars['am_i_starter'] = (get_loggedin_userid() == $vars['lds']->owner_guid);
	
	//$vars['all_can_read'] = ($vars['lds']->access_id == '1') ? 'true':'false';
    $vars['all_can_read'] = ($vars['lds']->all_can_view == 'yes' || ($vars['lds']->all_can_view === null && $vars['lds']->access_id < 3 && $vars['lds']->access_id > 0)) ? 'true' : 'false';


    //$body = elgg_view('lds/view_internal_editor',$vars);
	//page_draw($vars['lds']->title, $body);
	
	echo elgg_view('lds/view_internal_editor',$vars);
}

function lds_exec_info ($params)
{
	$vars['infoComments'] = true;
	$vars['lds'] = get_entity($params[1]);
	$vars['ldsDocs'] = lds_contTools::getLdsDocuments($params[1]);
	$vars['nComments'] = $vars['lds']->countAnnotations('generic_comment');
	
	$vars['am_i_starter'] = (get_loggedin_userid() == $vars['lds']->owner_guid);
	$vars['starter'] = get_user($vars['lds']->owner_guid);
	
	//These are all my friends
    /*
	$friends = lds_contTools::getFriendsArray(get_loggedin_userid());
	$arrays = lds_contTools::buildFriendArrays($friends, $vars['lds']->access_id, $vars['lds']->write_access_id);

	$vars['jsonfriends'] = json_encode(array_values($arrays['available']));
	$vars['viewers'] = json_encode($arrays['viewers']);
	$vars['editors'] = json_encode($arrays['editors']);
	$vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));
    */

    //These are all my friends
    $arrays = lds_contTools::buildObjectsArray($vars['lds']);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    //$vars['jsonfriends'] = json_encode(array_values($arrays['available']));
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(ldshakers_contTools::buildMinimalUserGroups(get_loggedin_userid()));

    $vars['starter'] = get_user($vars['lds']->owner_guid);
	
	$vars['am_i_starter'] = (get_loggedin_userid() == $vars['lds']->owner_guid);
	
	//$vars['all_can_read'] = ($vars['lds']->access_id == '1') ? 'true':'false';
    $vars['all_can_read'] = ($vars['lds']->all_can_view == 'yes' || ($vars['lds']->all_can_view === null && $vars['lds']->access_id < 3 && $vars['lds']->access_id > 0)) ? 'true' : 'false';


    $body = elgg_view('lds/view_internal',$vars);
	page_draw($vars['lds']->title, $body);
}

function lds_exec_imgdisplay ($params)
{
	$file_guid = (int)$params['1'];
	$file = get_entity($file_guid);
	
	$readfile = new ElggFile();
	$readfile->owner_guid = $file->owner_guid;
	$readfile->setFilename($file->originalfilename);
	$filename = $readfile->getFilenameOnFilestore();
	$handle = fopen($filename, "r");
	$contents = fread($handle, filesize($filename));
	fclose($handle);
	$mime = $file->mimetype;
	$expires = 14 * 60*60*24;

	header("Content-Type: $mime");
	header("Content-Length: " . strlen($contents));
	header("Cache-Control: public", true);
	header("Pragma: public", true);
	header('Expires: ' . gmdate('D, d M Y H:i:s', time() + $expires) . ' GMT', true);
	
	echo $contents;
}

function lds_exec_viewrevision ($params)
{
	global $CONFIG;
	
	//TODO access policies
	$revision = get_entity ($params[1]);

	if ($revision->subtype == get_subtype_id('object', 'LdS_document_revision'))
	{
		$document = get_entity ($revision->document_guid);
		$revDate = $revision->time_created;
	}
	else
	{
		$document = $revision;
		$revDate = $document->time_updated;
	}
	$lds = get_entity ($document->lds_guid);

	//Get all the revisions of this document
	$revisions = get_entities_from_metadata('document_guid',$document->guid,'object','LdS_document_revision',0,10000,0,'time_created');
	
	//And get the link to the next one and the previons one
	if ($revision->subtype == get_subtype_id('object', 'LdS_document_revision'))
	{
		$prevId = $nextId = null;
		if (is_array ($revisions))
		{
			foreach ($revisions as $k=>$rev)
			{
				if ($rev->guid == $revision->guid)
				{
					$prevId = $revisions[$k - 1]->guid;;
					if ($k == count($revisions) - 1)
					{
						if (!is_null($document))
							$nextId = $document->guid; 
						else
							$nextId = null;
					}
					else $nextId = $revisions[$k + 1]->guid;	
				}
			}
		}
	}
	else
	{
		$nextId = null;
		$prevId = $revisions[count($revisions) - 1]->guid;
	}
	
	//Now we create a diff string if the previous version is available
	if (!is_null($prevId))
	{
		$aname = $CONFIG->tmppath.'rev'.$prevId.'.html';
		$bname = $CONFIG->tmppath.'rev'.$revision->guid.'.html';

		file_put_contents($aname, get_entity($prevId)->description);
		file_put_contents($bname, $revision->description);
		
		$output = array();
		exec ("{$CONFIG->pythonpath} {$CONFIG->path}mod/lds/ext/diff.py $aname $bname", $output);
		$diff = implode('', $output);
		
		unlink($aname);
		unlink($bname);
	}
	
	$vars['prevId'] = $prevId;
	$vars['nextId'] = $nextId;
	$vars['revision'] = $revision;
	$vars['revDate'] = $revDate;
	$vars['prevRevision'] = get_entity($prevId);
	$vars['prevReviser'] = get_user($vars['prevRevision']->owner_guid);
	$vars['reviser'] = get_user($revision->owner_guid);
	$vars['document'] = $document;
	$vars['lds'] = $lds;
	$vars['diff'] = $diff;
	
	$body = elgg_view('lds/view_revision',$vars);
	page_draw($document->title . ': '. T("Revision"). ' - ' . date('j M Y H:i', $revDate), $body);
}

function lds_exec_viewrevisioneditor ($params)
{
	global $CONFIG;
	
	//TODO access policies
	$revision = get_entity ($params[1]);

	if ($revision->subtype == get_subtype_id('object', 'LdS_document_editor_revision'))
	{
		$document = get_entity ($revision->document_guid);
		$revDate = $revision->time_created;
	}
	else
	{
		$document = $revision;
		$revDate = $document->time_updated;
	}
	$lds = get_entity ($document->lds_guid);

	//Get all the revisions of this document
	$revisions = get_entities_from_metadata('document_guid',$document->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
	
	//And get the link to the next one and the previons one
	if ($revision->subtype == get_subtype_id('object', 'LdS_document_editor_revision'))
	{
		$prevId = $nextId = null;
		if (is_array ($revisions))
		{
			foreach ($revisions as $k=>$rev)
			{
				if ($rev->guid == $revision->guid)
				{
					$prevId = $revisions[$k - 1]->guid;
					if ($k == count($revisions) - 1)
					{
						if (!is_null($document))
							$nextId = $document->guid; 
						else
							$nextId = null;
					}
					else $nextId = $revisions[$k + 1]->guid;	
				}
			}
		}
	}
	else
	{
		$nextId = null;
		$prevId = $revisions[count($revisions) - 1]->guid;
	}
	
	$vars['prevId'] = $prevId;
	$vars['nextId'] = $nextId;
	$vars['revision'] = $revision;
	$vars['revDate'] = $revDate;
	$vars['prevRevision'] = get_entity($prevId);
	$vars['prevReviser'] = get_user($vars['prevRevision']->owner_guid);
	$vars['reviser'] = get_user($revision->owner_guid);
	$vars['document'] = $document;
	$vars['lds'] = $lds;
	$vars['diff'] = $diff;
	
	$body = elgg_view('lds/view_revision_editor',$vars);
	page_draw($lds->title. ': '. T("Revision"). ' - ' . date('j M Y H:i', $revDate), $body);
}


function lds_exec_firststeps ()
{
	$body = elgg_view('lds/firststeps',$vars);
	page_draw(T("Welcome to LdShake!"), $body);
}

function lds_exec_contact ()
{
	$body = <<<HTML
<br /><br />
<h3 style="margin-left: 15px;">
Qualsevol problema tècnic contactar amb Jonathan Chacón "jonathan.chacon@upf.edu"
<br /><br />
Qualsevol dubte que tingueu contactar amb Sílvia Lope "slope@xtec.cat"
</h3>
HTML;


	page_draw(T("Informació de contacte"), $body);
}


function lds_exec_make_expert ($params)
{
	$id = $params[1];

	if ($user = get_user($id))
	{
		$user->isExpert = 1;
		$user->save();
	}
	
	forward('pg/lds/');
}

function lds_exec_make_newbie ($params)
{
	$id = $params[1];

	if ($user = get_user($id))
	{
		$user->isExpert = 0;
		$user->save();
	}
	
	forward('pg/lds/');
}

function lds_exec_404 ($view = '')
{
	header("HTTP/1.0 404 Not Found");
	$body = elgg_view("lds/404{$view}",$vars);
	page_draw('Not found', $body);
}