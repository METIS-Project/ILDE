<?php
	/**
	 * Whole world are friends plugin
	 * When a user register, automatically makes friend of all and all users makes friend of him.
	 * ALERT- The users registered before the plugin activation, are NOT automatically friends. 
	 * Only makes the relationship betwen the newuser and registered users.
	 *
	 * @author Miguel-Angel Carralero-Martínez
	 * GTI-Learning 
	 * Universitat Pompeu Fabra (Barcelona)
	 */


	 
// register create user event hook
register_elgg_event_handler ( 'create', 'user', 'wholeworldfriends_newuser' );


function wholeworldfriends_newuser($event, $object_type, $object) {
	// Load the configuration
			global $CONFIG;
	
	//obtain all user of the system 
	$result = get_data(" SELECT guid from {$CONFIG->dbprefix}entities where type = 'user' "); 									
	$user_guid=$object->getGUID();																
	
	//inicialice insert query
	$query="INSERT into {$CONFIG->dbprefix}entity_relationships (guid_one, relationship, guid_two) values ";
	
			foreach ($result as $registereduser){	   										  											 // foreach user{	    																			
				if($user_guid!=$registereduser->guid){																					// if the user_guids are not the same 																				
					$query.=" ({$user_guid}, 'friend', {$registereduser->guid}), ({$registereduser->guid}, 'friend', {$user_guid}) ,"; // add values (me, friend, you) and (you, friend, me)
				}
			}
			
			// delete the last coma " , " of the string 
			$query=substr($query, 0, -1); 
			
			 //make the insert
			insert_data($query);         
			
}
	 
?>