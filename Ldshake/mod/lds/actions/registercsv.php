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

global $CONFIG;

admin_gatekeeper();
action_gatekeeper();

$data = get_uploaded_file("filedata");
$data_array = preg_split("/\\r\\n|\\n|\\r/", $data);
$user_array = array_slice($data_array, 1);

$created=0;
$updated=0;
$invalid=0;

$welcome_document = elgg_view('lds/welcome_lds/welcome_lds_'.$CONFIG->language);

foreach($user_array as $user_row) {
    $user_fields = str_getcsv($user_row);
    $name = "{$user_fields[2]} {$user_fields[3]}";
    $username = "{$user_fields[0]}";
    $password = "{$user_fields[1]}";
    $email = "{$user_fields[4]}";

    try {
        validate_username($username);
        validate_email_address($email);
        validate_password($password);
        $name = preg_replace("/[^\pL\s\pNd]+/u", " ", $name);
    } catch (RegistrationException $e) {
        $invalid++;
        continue;
    }

    if($user = get_user_by_username($username)) {
        $user->name = $name;
        $user->email = $email;
        $user->password = $password;
        $user->save();
        $updated++;
    } else {
        if($guid = register_user($username, $password, $name, $email)) {
            set_user_validation_status($guid, true, 'admin');
            $user = get_user($guid);
            $user->admin_created = true;
            $user->isnew = '1';
            $user->save();

            //Create new document
            $wl = new LdSObject();
            $wl->access_id = 2;
            $wl->editor_type = 'doc';
            $wl->editor_subtype = 'doc';
            $wl->title = T("My first LdS");
            $wl->owner_guid = $user->guid;
            $wl->container_guid = $user->guid;
            $wl->granularity = '0';
            $wl->completeness = '0';
            $wl->all_can_view = "no";
            $wl->welcome = 1;
            $wl->save ();

            $doc = new DocumentObject($wl->guid);
            $doc->title = T("My first LdS");
            $doc->description = $welcome_document;
            $doc->owner_guid = $user->guid;
            $doc->container_guid = $wl->guid;
            $doc->access_id = 2;
            $doc->save();

            $created++;
        } else {
            $invalid++;
        }
    }
}

system_message(T("created: %1, updated: %2, invalid: %3", $created, $updated, $invalid));