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
 * LdS notifications deferred sending. Meant to be executed via cron / schedule manager.
 */

set_time_limit (0);
ini_set('memory_limit', '512M');

$forcedEnv = $argv[1];
$debug = $argv[2];


if($debug == "true")
	$debug = true;
else
	$debug = false;

//Fool the thing and tell it I'm an admin
global $is_admin;               
$is_admin = true; 

require_once(__DIR__."/../../../../engine/start.php");
require_once __DIR__.'/../../lds_contTools.php';

//$dns = get_entities_from_metadata('notify', '2', 'object', 'LdS', 0, 1000);
$dns = get_entities_from_metadata('sent', '0', 'object', 'DeferredNotification', 0, 1000);


if (is_array($dns)) {
	foreach ($dns as $dn) {
		$send = true;

        /*
		if($debug) {
			$send = false;
			$user = get_user($dn->receiver_guid);
			if($user->username == "pablo")
				$send = true;
		}
        */

		if ($send) {
            /*
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
                $dn = new DeferredNotification();
                $dn->owner_guid = get_loggedin_userid();
                $dn->access_id = 2;
                $dn->write_access_id = 2;
                $dn->receiver_guid = $l->guid;
                $dn->sent = 0;
                $dn->title = T("New LdS in LdShake: %1", $lds->title);
                $dn->description = $body;
                $dn->save();
            }
            */
            $users_guids = lds_contTools::getViewAccessUsers($dn->content_guid);
            $notification_list = lds_contTools::build_notification($dn, $users_guids);

            /*
            $vars = array();
            $owner = get_user($lds->owner_guid);
            $vars['ldsTitle'] = $lds->title;
            $vars['fromName'] = $owner->name;
            $vars['ldsUrl'] = lds_viewTools::url_for($lds);
            $vars['senderUrl'] = ldshakers_viewTools::urlFor($owner, 'user');

            $description = elgg_view('emails/newlds',$vars);
            $title = T("New LdS in LdShake: %1", $lds->title);

            $owner_guid = $lds->owner_guid;
            */



            foreach ($notification_list as $nf) {
                if($nf['recipient_guid'] != $nf['sender_guid']) {

                    echo "Sending to {$nf['recipient_guid']} from {$nf['sender_guid']} ({$nf['title']})...\n";

                    notify_user (
                        $nf['recipient_guid'],
                        $nf['sender_guid'],
                        $nf['title'],
                        $nf['body']
                    );

                }
            }

            $dn->sent = 1;
            $dn->save();
            /*
            /$lds->notify = 3;
            $lds->save();
            */
        }
	}
}
