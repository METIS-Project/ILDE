<?php

	/**
	 * Elgg user display (small)
	 *
     * @file modified by Interactive Technologies Group (GTI)
     * @authors (alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P..
     * @Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
     * @based on:
	 * @package ElggProfile
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd <info@elgg.com>
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.com/
	 * 
	 * @uses $vars['entity'] The user entity
	 */
		$info = "";	 

	    //grab the users status message with metadata 'state' set to current if it exists
		if($get_status = get_entities_from_metadata("state", "current", "object", "status", $vars['entity']->guid)){
    		    
            foreach($get_status as $s) {
	            $info = elgg_view("status/friends_view", array('entity' => $s));
            }
    		    
		}


		$icon = elgg_view(
				"profile/icon", array(
										'entity' => $vars['entity'],
										'size' => 'small',
									  )
			);
	
		// Simple XFN
		$rel = "";
		if (page_owner() == $vars['entity']->guid)
			$rel = 'me';
		else if (check_entity_relationship(page_owner(), 'friend', $vars['entity']->guid))
			$rel = 'friend';

		/// LdShake change ///		
		$class='';
		if ($vars['entity']->admin)
			$class="ldshake-listing-admin";

		$info .= "<p><b><a href=\"" . $vars['entity']->getUrl() . "\" rel=\"$rel\" class=\". $class . \">" . $vars['entity']->name . "</a></b></p>";
		/// LdShake change ///

		$location = $vars['entity']->location;
		if (!empty($location)) {
			$info .= "<p class=\"owner_timestamp\">" . elgg_echo("profile:location") . ": " . elgg_view("output/tags",array('value' => $vars['entity']->location)) . "</p>";
		}
		
		echo elgg_view_listing($icon, $info);
			
?>
