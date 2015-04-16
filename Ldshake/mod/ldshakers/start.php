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
 * LdShakers controller
 */


function ldshakers_init()
{	
	global $CONFIG;

	//Our css stuff
	extend_view('css','ldshakers/css');
	
	//And our js scripts
	extend_view('page_elements/jsarea', 'ldshakers/js');
	
	//We want the /ldshakers/whatever urls to be ours
	register_page_handler('ldshakers','ldshakers_page_handler');
	
	//LdShakers actions:
	register_action("ldshakers/addgroup", false, $CONFIG->pluginspath . "ldshakers/actions/ldshakers/addgroup.php");
	register_action("ldshakers/deletegroup", false, $CONFIG->pluginspath . "ldshakers/actions/ldshakers/deletegroup.php");
	register_action("ldshakers/addtogroup", false, $CONFIG->pluginspath . "ldshakers/actions/ldshakers/addtogroup.php");
	register_action("ldshakers/removefromgroup", false, $CONFIG->pluginspath . "ldshakers/actions/ldshakers/removefromgroup.php");
	
	//Include the helper functions
	require_once __DIR__.'/ldshakers_contTools.php';
	require_once __DIR__.'/ldshakers_viewTools.php';
}

register_elgg_event_handler('init','system','ldshakers_init');

register_plugin_hook('permissions_check', 'group', 'ldshakers_write_permission_check');

function ldshakers_write_permission_check($hook, $entity_type, $returnvalue, $params)
{
    $entity = $params['entity'];
    if($entity->type == 'group') {
        if(isadminloggedin())
            return true;

        if($entity->owner_guid == get_loggedin_userid())
            return true;
    }

}

function ldshakers_page_handler ($page)
{
	//Nothing here will be exposed to non-logged users, so go away!
	gatekeeper();
	
	//Special case: my lds's url is short: doesn't have an explicit section, so we add it automatically
	if ($page[0] == '')
		array_unshift($page, 'main');
		
	//Sub_controller dispatcher
	if (function_exists("ldshakers_exec_{$page[0]}"))
	{
		set_context("ldshakers_exec_{$page[0]}");
		call_user_func("ldshakers_exec_{$page[0]}", $page);
	}
	else if (get_user_by_username ($page[0]))
	{
		ldshakers_exec_userprofile ($page);
	}
	else
		ldshakers_exec_404 ();
}

function ldshakers_exec_main ($params)
{
	$vars['section'] = $params[1];
	$offset = get_input('offset') ?: '0';
	
    $vars['ldshakers'] = ldshakers_contTools::getCommunityMembers(false, 50, $offset);
    $vars['count'] = ldshakers_contTools::getCommunityMembers(false, 50, $offset, true);

    $vars['ldshakers_activity'] = array();
    if(is_array($vars['ldshakers'])) {
        foreach($vars['ldshakers'] as $ldshaker) {
            $vars['ldshakers_activity'][$ldshaker->guid]['started'] = (int)lds_contTools::getUserEntities('object', 'LdS', 0, true, 9999, 0, null, null, "time", false, null, false, null, false, null, $ldshaker->guid);
            $vars['ldshakers_activity'][$ldshaker->guid]['coedition'] = (int)lds_contTools::getUserCoedition($ldshaker->guid, 9999, 0, true);
        }
    }

    $vars['offset'] = $offset;

    $groups = ldshakers_contTools::getUserGroups(get_loggedin_userid());
	$vars['groups'] = $groups;
	
	$vars['title'] = T("All users");
	$body = elgg_view('ldshakers/ldshakers',$vars);
	page_draw($vars['title'], $body);
}

function ldshakers_exec_group ($params)
{
	$group = get_entity($params[1]);
	if (!$group)
	{
		ldshakers_exec_404 ();
		return;	
	}
	$vars['group'] = $group;
	
	$vars['section'] = 'group';
	$offset = get_input('offset') ?: '0';
	
	$ldshakers = $group->getMembers(9999);
	@Utils::osort($ldshakers, name);
	$vars['ldshakers'] = $ldshakers;
	
	$groups = ldshakers_contTools::getUserGroups(get_loggedin_userid());
	$vars['groups'] = $groups;
	
	$vars['title'] = T("Members of %1", $group->name);
	$body = elgg_view('ldshakers/ldshakers',$vars);
	page_draw($vars['title'], $body);
}

function ldshakers_exec_userprofile ($params)
{
    $option = $params[1];
	$cuser = get_user_by_username($params[0]);

    $activity_counters = array();

    $offset = 0;
    $limit = 50;
    $activity_counters['started'] = lds_contTools::getUserEntities('object', 'LdS', 0, true, 9999, 0, null, null, "time", false, null, false, null, false, null, $cuser->guid);
    $activity_counters['comments'] = count_annotations_enabled(0, "object", "LdS", 'generic_comment', "", "", $cuser->guid);
    $activity_counters['coedition'] = lds_contTools::getUserCoedition($cuser->guid, 9999, $offset, true);
    $activity_counters['published'] = get_entities_from_metadata_owner('published', '1', 'object', 'LdS', $cuser->guid, 10, $offset, "", 0, true);
    $activity_counters['implemented'] = lds_contTools::getUserEntities('object', 'LdS_implementation', 0, false, 9999, 0, null, null, "time", false, null, false, $custom, true, null, $cuser->guid);
    if($activity_counters['implemented'])
        $activity_counters['implemented'] = count($activity_counters['implemented']);
    else
        $activity_counters['implemented'] = 0;

    $vars['activity_counters'] =  $activity_counters;

    switch($option) {
        case 'coedited':
            $coedition = lds_contTools::getUserCoedition($cuser->guid, 9999, $offset);
            $coedition_lds = ldshake_lds_from_array($coedition);
            $vars['list'] = $coedition_lds;
            $vars['section'] = 'coedited';
            $vars['title'] = T("LdS co-edited by %1", $cuser->name);
            break;
        case 'published':
            $published = get_entities_from_metadata_owner('published', '1', 'object', '', $cuser->guid, $limit, $offset);
            $published_lds = ldshake_lds_from_array($published);

            $vars['list'] = $published_lds;
            $vars['section'] = 'published';
            $vars['title'] = T("LdS published by %1", $cuser->name);
            break;
        case 'implemented':
            $custom = array(
                'build_callback' => 'ldshake_custom_query_implemented_lds',
                'params' => array(),
            );

            $implemented = lds_contTools::getUserEntities('object', 'LdS_implementation', 0, false, 9999, 0, null, null, "time", false, null, false, $custom, false, null, $cuser->guid);
            $implemented_lds = ldshake_lds_from_array($implemented);

            $vars['list'] = $implemented_lds;
            $vars['section'] = 'implemented';
            $vars['title'] = T("LdS implemented by %1", $cuser->name);
            break;
        case 'comments':
            $comments = get_annotations(0, "object", "LdS", 'generic_comment', "", $cuser->guid, 9999, $offset, "desc");
            $comments_lds = ldshake_lds_from_array($comments);
            $vars['list'] = $comments_lds;
            $vars['comments'] = $comments;
            $vars['section'] = 'comments';
            $vars['title'] = T("Comments posted by %1", $cuser->name);
            break;
        default:
            $started = lds_contTools::getUserEntities('object', 'LdS', 0, false, 9999, 0, null, null, "time", false, null, true, null, false, null, $cuser->guid);
            $vars['list'] = $started;
            $vars['section'] = 'default';
            $vars['title'] = T("LdS of %1", $cuser->name);
            break;
    }

	$vars['cuser'] = $cuser;

	$body = elgg_view('ldshakers/profile',$vars);
	page_draw($vars['title'], $body);
}

function ldshakers_exec_404 ()
{
	header("HTTP/1.0 404 Not Found");
	$body = elgg_view("ldshakers/404",$vars);
	page_draw('Not found', $body);
}