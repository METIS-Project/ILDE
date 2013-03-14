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
 * LdS share controller.
 *
 */
require_once __DIR__.'/../../lds_contTools.php';

global $CONFIG;
$guid = get_input('guid');
$editors = get_input('editors');
$viewers = get_input('viewers');
$allCanView = get_input('allCanView');

$lds = get_entity($guid);

$pre_viewers = lds_contTools::getViewersIds($guid);
$pre_editors = lds_contTools::getEditorsIds($guid);

//Set read access
$readAccessIds = array_filter(explode(',',"$viewers"));

$del_viewers = array_diff($pre_viewers, $readAccessIds);
$add_viewers = array_diff($readAccessIds, $pre_viewers);

foreach($del_viewers as $v) {
    $e = get_entity($v);
    $relationship = ($e->type == 'user') ? 'lds_viewer' : 'lds_viewer_group';
    remove_entity_relationship($v, $relationship, $guid);
}

foreach($add_viewers as $v) {
    $e = get_entity($v);
    $relationship = ($e->type == 'user') ? 'lds_viewer' : 'lds_viewer_group';
    add_entity_relationship($v, $relationship, $guid);
}

if ($allCanView != '0') {
    /*
     if(!check_entity_relationship($lds->owner_guid, 'lds_all_can_view', $guid))
        add_entity_relationship($lds->owner_guid, 'lds_all_can_view', $guid);
    */
    $lds->all_can_view = "yes";
} else {
    /*
    if(check_entity_relationship($lds->owner_guid, 'lds_all_can_view', $guid))
        remove_entity_relationship($lds->owner_guid, 'lds_all_can_view', $guid);
    */
    $lds->all_can_view = "no";
}
$lds->save();

//Set write access
$writeAccessIds = array_filter(explode(',',"$editors"));

$del_editors = array_diff($pre_editors, $writeAccessIds);
$add_editors = array_diff($writeAccessIds, $pre_editors);

foreach($del_editors as $e) {
    $en = get_entity($e);
    $relationship = ($en->type == 'user') ? 'lds_editor' : 'lds_editor_group';
    remove_entity_relationship($e, $relationship, $guid);
}

foreach($add_editors as $e) {
    $en = get_entity($e);
    $relationship = ($en->type == 'user') ? 'lds_editor' : 'lds_editor_group';
    add_entity_relationship($e, $relationship, $guid);
}

lds_contTools::markLdSAsViewed ($lds->guid);

if($lds->notify == '1') {
    $lds->notify = '2';

    /*
    $vars['ldsTitle'] = $notification->title;
    $vars['fromName'] = $notification->name;
    $vars['ldsUrl'] = $notification->lds_url;
    $vars['senderUrl'] = $notification->sender_url;
    */

    $dn = new DeferredNotification();
    $dn->owner_guid = get_loggedin_userid();
    $dn->access_id = 2;
    $dn->notification_type = 'newlds';
    $dn->sent = 0;
    $dn->content_guid = $lds->getGUID();

    $dn->title = T("New LdS in LdShake: %1", $lds->title);
    $dn->name = get_loggedin_user()->name;
    $dn->url = $CONFIG->url;
    $dn->lds_url = lds_viewTools::url_for($lds);
    $dn->sender_url = ldshakers_viewTools::urlFor($_SESSION['user'], 'user');
    $dn->save();

    $lds->save();
}

echo 'ok';