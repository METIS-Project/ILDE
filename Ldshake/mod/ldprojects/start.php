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
 * Ldprojects controller
 */


function ldprojects_init()
{	
	global $CONFIG;

	//Our css stuff
	extend_view('css','ldprojects/css');
	
	//We want the /ldprojects/whatever urls to be ours
	register_page_handler('ldprojects','ldprojects_page_handler');

	//LdShakers actions:
	//register_action("ldprojects/addgroup", false, $CONFIG->pluginspath . "ldprojects/actions/ldprojects/addgroup.php");

    //Adding our JS to the view
    extend_view('page_elements/jsarea', 'ldprojects/js');

}

register_elgg_event_handler('init','system','ldprojects_init');

register_plugin_hook('permissions_check', 'group', 'ldprojects_write_permission_check');

function ldprojects_write_permission_check($hook, $entity_type, $returnvalue, $params)
{
    $entity = $params['entity'];
    if($entity->type == 'group') {
        if(isadminloggedin())
            return true;

        if($entity->owner_guid == get_loggedin_userid())
            return true;
    }

}

function ldprojects_page_handler ($page)
{

	 //Nothing here will be exposed to non-logged users, so go away!
	gatekeeper();

	//Special case: my lds's url is short: doesn't have an explicit section, so we add it automatically
	if ($page[0] == '')
		array_unshift($page, 'main');

	//Sub_controller dispatcher
	if (function_exists("ldprojects_exec_{$page[0]}"))
	{
		set_context("ldprojects_exec_{$page[0]}");
		call_user_func("ldprojects_exec_{$page[0]}", $page);
	}
	else
		ldprojects_exec_404();
}

function ldprojects_exec_main ($params)
{

}


function ldprojects_exec_new ($params)
{
    echo elgg_view('ldprojects/ldsproject_editor',$vars);
}

function ldprojects_exec_404 ()
{
	header("HTTP/1.0 404 Not Found");
	$body = elgg_view("ldprojects/404",$vars);
	page_draw('Not found', $body);
}