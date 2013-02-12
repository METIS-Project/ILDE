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
 * LdS save controller.
 */
require_once __DIR__.'/../../lds_contTools.php';

//Is the LdS new?
$isNew = false;

//The object we'll return (jsonencoded) to the form.
$resultIds = new stdClass();
$resultIds->docs = array ();

//We first create the LDS (or update it)
if (get_input('guid') > 0)
{
	//We're editing. Fetch it from the DB
	$lds = get_entity(get_input('guid'));
}
else
{
	//We're creating it from scratch. Construct a new obj.
	$lds = new LdSObject();
	$lds->owner_guid = get_loggedin_userid();
	$isNew = true;
} 

$lds->title = get_input('title');
$lds->granularity = get_input('granularity');
$lds->completeness = get_input('completeness');

//If we save it for the first time, we're going to put some default value, which will be
//modified by an ajax call to share.php
if (get_input('guid') == 0)
{
	$lds->access_id = 1;
	//$lds->write_access_id = 0;
}

//Now the tags. We'll delete the existing ones to save them again
$tagFields = array ('discipline', 'pedagogical_approach', 'tags');
foreach ($tagFields as $field)
{
	//if (get_input('guid') > 0) remove_metadata(get_input('guid'), $field);
	$newTags = explode(',', get_input($field));
	foreach ($newTags as $k=>$v) if(empty($v)) unset($newTags[$k]);
	$lds->$field = $newTags;
}

$lds->save();
lds_contTools::markLdSAsViewed ($lds->guid);
$resultIds->LdS = $lds->guid;

//We now determine if we already saved in the same edition session or not, in order to create a new revision.
if (get_input('revision') == 0)
{
	//We save for the first time in this edition session. So we create a revision.
	create_annotation($lds->guid, 'revised_docs', '', 'text', get_loggedin_userid(), 1);
}
//We get the revision id to send it back to the form
$revision = $lds->getAnnotations('revised_docs', 1, 0, 'desc');
$revision = $revision[0];

$deletedDocs = explode (',', $revision->value);
$resultIds->deletedDocs = implode(',', $deletedDocs);
$resultIds->revision = $revision->id;

$documents = get_entities_from_metadata('lds_guid',$lds->guid,'object','LdS_document');

//Here we have the document ids of the LDS.
$docIds = array();
if (is_array($documents))
	foreach ($documents as $doc)
		$docIds[] = $doc->guid;

//And now we save each document
if (is_array($_POST['documents']))
{
	foreach ($_POST['documents'] as $doc)
	{
		//The submitted doc exists in the db
		if (in_array($doc['guid'], $docIds))
		{
			//Edit
			$docObj = get_entity($doc['guid']);
			
			//First we check if we modified the body of the document
			if ($docObj->description != Utils::normalizeSpace($doc['body']) || $docObj->title!= trim($doc['title']))
			{	
				//Wasn't modified yet in previous saves of this form, so we create a revision
				if ($doc['modified'] == '0')
					DocumentRevisionObject::createRevisionFromDocument($docObj);
				
				$docObj->title = trim($doc['title']); 
				$docObj->description = Utils::normalizeSpace($doc['body']); //We put it in the desciption in order to use the objects_entity table of elgg db
				$docObj->lds_revision_id = $revision->id;
				
				$docObj->save();
				
				$resultIds->docs[] = (object) array ('id'=>$docObj->guid, 'modified' => '1');
			}
			else
			{
				$resultIds->docs[] = (object) array ('id'=>$docObj->guid, 'modified' => '0');
			}
		}
		//The submitted doc doesn't exist in the db
		else
		{
			//Create
			$docObj = new DocumentObject($lds->guid);
			$docObj->title = $doc['title']; 
			$docObj->description = Utils::normalizeSpace($doc['body']);; //We put it in ths desciption in order to use the objects_entity table of elgg db
			$docObj->lds_revision_id = $revision->id;
			$docObj->save();
			
			$resultIds->docs[] = (object) array ('id'=>$docObj->guid, 'modified' => '1');
		}
		
		//Remove the item from docIds (marking it as "done")
		unset($docIds[array_search($doc['guid'], $docIds)]);
	}
	
	//For the rest of the docIds, we'll disable them (if they were in the array it means that ths user has removed them in the form)
	if (count ($docIds))
	{
		foreach ($docIds as $docId)
		{
			$docObj = get_entity ($docId);
			
			//If we didn't create the document in this very same revision, we'll create a revision of it
			if ($docObj->lds_revision_id != $revision->id)
				DocumentRevisionObject::createRevisionFromDocument($docObj);
			
			$docObj->deleted = '1';
			$docObj->save();
			$docObj->disable();
		}
		
		$deletedDocs = array_filter(array_merge($deletedDocs, $docIds));
		update_annotation($revision->id, 'revised_docs', implode(',', $deletedDocs), 'text', get_loggedin_userid(), 1);
	}
}

/*
if ($isNew) {
	//Biologia: Notify everybody in the site :S
	$vars['ldsTitle'] = $lds->title;
	$vars['fromName'] = $_SESSION['user']->name;
	$vars['ldsUrl'] = lds_viewTools::url_for($lds);
	$vars['senderUrl'] = ldshakers_viewTools::urlFor($_SESSION['user'], 'user');				
	$body = elgg_view('emails/newlds',$vars);
	
	//Gets all the user IDs of the current user's friends:
	$ldshakers = get_loggedin_user()->getFriends('', 10000, 0);
	$ids = array ();
	foreach ($ldshakers as $l) {
		$ids[] = $l->guid;
	}
	$ids = array_unique($ids);
	
	notify_user (
		$ids, 
		$_SESSION['user']->getGUID(), 
		T("New LdS in LdShake: %1", $lds->title), 
		$body
	);
}
*/

if ($isNew) {

    $lds->notify = 1;
    $lds->save();
}

echo json_encode($resultIds);
