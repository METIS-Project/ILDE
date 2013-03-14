<?php

	/**
	 * Elgg delete comment action
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

	// Ensure we're logged in
		if (!isloggedin()) forward();
		
	// Make sure we can get the comment in question
		$annotation_id = (int) get_input('annotation_id');
		if ($comment = get_annotation($annotation_id)) {
    		
    		$entity = get_entity($comment->entity_guid);
			
			if ($comment->canEdit()) {
				$comment->delete();
				system_message(elgg_echo("generic_comment:deleted"));
/// LdShake change ///
				//forward($entity->getURL());
				if ($entity->external_editor)
					forward ($CONFIG->url .'pg/lds/vieweditor/'.$entity->guid.'/'); 
				else
					forward ($CONFIG->url .'pg/lds/info/'.$entity->guid.'/'); 
/// LdShake change ///
			}
			
		} else {
			$url = "";
		}
		
		register_error(elgg_echo("generic_comment:notdeleted"));
		//forward($entity->getURL());
		if ($entity->external_editor)
			forward ($CONFIG->url .'pg/lds/vieweditor/'.$entity->guid.'/'); 
		else
			forward ($CONFIG->url .'pg/lds/info/'.$entity->guid.'/'); 

?>