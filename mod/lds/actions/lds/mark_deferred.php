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
 * LdS notifications deferred sending. Meant to be executed via CLI interface.
 *
 */

set_time_limit (0);
ini_set('memory_limit', '512M');

$forcedEnv = $argv[1];

//Fool the thing and tell it I'm an admin
global $is_admin;
$is_admin = true;

require_once(__DIR__."/../../../../engine/start.php");
require_once __DIR__.'/../../lds_contTools.php';

$dns = get_entities_from_metadata('sent', '0', 'object', 'DeferredNotification', 0, 1000);
if (is_array($dns)) {
	foreach ($dns as $dn) {

		echo "Sent flag {$dn->guid} ({$dn->title}) = 1\n";
		/*
		notify_user (
			$dn->receiver_guid,
			$dn->owner_guid,
			$dn->title,
			$dn->description
		);
		*/


		$dn->sent = 1;

		//Force the writing of metadata
		//create_metadata($dn->guid, 'sent', '1', 'integer', $dn->owner_guid, 2, false);
		$dn->save();
	}
}
