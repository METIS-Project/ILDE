<?php

/**
 * Elgg add comment action
 *
 * @file modified by Interactive Technologies Group (GTI)
 * @authors (alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P..
 * @Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
 * @based on:
 * @package Elgg
 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
 * @author Curverider <curverider.co.uk>
 * @copyright Curverider Ltd 2008
 * @link http://elgg.org/
 */

/// LdShake change ///
// Make sure we're logged in; forward to the front page if not
    global $CONFIG;
	gatekeeper();
	action_gatekeeper();
	
// Get input
	$entity_guid = (int) get_input('entity_guid');
	$comment_text = htmlspecialchars(get_input('generic_comment', '', false));
	
// Let's see if we can get an entity with the specified GUID
	if ($entity = get_entity($entity_guid)) {
		
        // If posting the comment was successful, say so
			if ($entity->annotate('generic_comment',$comment_text,$entity->access_id, $_SESSION['guid'])) {
				
				//if ($entity->owner_guid != $_SESSION['user']->getGUID()) {
					/*
					$vars['ldsTitle'] = $entity->title;
					$vars['fromName'] = $_SESSION['user']->name;
					$vars['comment'] = $comment_text;
					$vars['ldsUrl'] = lds_viewTools::url_for($entity);
					$vars['senderUrl'] = ldshakers_viewTools::urlFor($_SESSION['user'], 'user');
					
					$body = elgg_view('emails/newcomment',$vars);

					*/
                    $dn = new DeferredNotification();
                    $dn->owner_guid = get_loggedin_userid();
                    $dn->access_id = 2;
                    //$dn->write_access_id = 2;
                    $dn->notification_type = 'comment';
                    $dn->sent = 0;
                    $dn->content_guid = $entity->getGUID();

                    $dn->title = T("New comment in %1", $entity->title);
                    $dn->name = get_loggedin_user()->name;
                    $dn->comment_text = $comment_text;
                    $dn->url = $CONFIG->url;
                    $dn->lds_url = lds_viewTools::url_for($entity);
                    $dn->sender_url = ldshakers_viewTools::urlFor($_SESSION['user'], 'user');
                    $dn->save();

					/*
					//Previously we sent to $entity->owner_guid, now to everybody
					notify_user (
						$ids, 
						$_SESSION['user']->getGUID(), 
						T("New comment in %1", $entity->title), 
						$body
					);
					*/
				//}
				
				system_message(T("Your comment was successfully posted."));
/// LdShake change ///				
			} else {
				register_error(elgg_echo("generic_comment:failure"));
			}
			
	} else {
	
		register_error(elgg_echo("generic_comment:notfound"));
		
	}
/// LdShake change ///	
// Forward to the
global $CONFIG;

if ($entity->external_editor)
	forward ($CONFIG->url .'pg/lds/vieweditor/'.$entity->guid.'/'); 
else
	forward ($CONFIG->url .'pg/lds/info/'.$entity->guid.'/'); 
//forward($entity->getURL());
/// LdShake change ///