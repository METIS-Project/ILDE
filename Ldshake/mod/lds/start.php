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
 * LdS controller
 */

define ('LDS_ENTITY_TYPE', 'LdS');
define ('LDS_LICENSE_CC_BY', '1');
define ('LDS_LICENSE_CC_BY_ND', '2');
define ('LDS_LICENSE_CC_BY_SA', '3');
define ('LDS_LICENSE_CC_BY_NC', '4');
define ('LDS_LICENSE_CC_BY_NC_ND', '5');
define ('LDS_LICENSE_CC_BY_NC_NA', '6');

function lds_init()
{
	global $CONFIG, $rest_services;

	///REST setup
    $CONFIG->rest_serializer = function($payload = array()) {
        return $payload;
    };

    //$CONFIG->webcollagerest_url= "{$CONFIG->url}services/dummy/";
    $CONFIG->webcollagerest_url= "http://pandora.tel.uva.es/~wic/wic2Ldshake/";
    $CONFIG->glueps_url = "http://pandora.tel.uva.es/METIS/GLUEPSManager/";

    $CONFIG->editor_templates["openglm"]["filename"] = "openglm_file.glm";
    $CONFIG->editor_templates["openglm"]["path"] = "vendors/openglm/EmptyOpenGLM.zip";
    $CONFIG->editor_templates["openglm"]["imsld"]["filename"] = "openglm_file.zip";
    $CONFIG->editor_templates["openglm"]["imsld"]["path"] = "vendors/openglm/EmptyOpenGLM.zip";

    //Load the model classes
	//TODO could include the whole directory...
    $time = microtime(true);

	require_once __DIR__.'/model/LdS.php';
	require_once __DIR__.'/model/LdSObject.php';
	require_once __DIR__.'/model/DocumentObject.php';
	require_once __DIR__.'/model/DocumentEditorObject.php';
	require_once __DIR__.'/model/DocumentRevisionObject.php';
	require_once __DIR__.'/model/DocumentEditorRevisionObject.php';
	require_once __DIR__.'/model/DeferredNotification.php';
//    require_once __DIR__.'/model/ImplementationObject.php';

	require_once __DIR__.'/editors/editorsFactory.php';
	require_once __DIR__.'/mode/mode.php';

    if($rest_services) {
        require_once __DIR__.'/rest.php';
        require_once(__DIR__ . '/../../vendors/httpful/bootstrap.php');
    }

    //require_once __DIR__.'/stadistics.php';

    //require_once __DIR__.'/rest.php';
    //require(__DIR__ . '/../../vendors/httpful/bootstrap.php');

    //Our css stuff
	extend_view('css','lds/css');
	
	//And our js scripts
	extend_view('page_elements/jsarea', 'lds/js');
	
	//We want the /lds/whatever urls to be ours
	register_page_handler('lds','lds_page_handler');


	//LdS actions:
    register_action("lds/clone", false, $CONFIG->pluginspath . "lds/actions/lds/clonelds.php");
    register_action("lds/cloneimplementation", false, $CONFIG->pluginspath . "lds/actions/lds/cloneimplementation.php");
    register_action("lds/register_deployment", false, $CONFIG->pluginspath . "lds/actions/lds/register_deployment.php");
    register_action("lds/implement", false, $CONFIG->pluginspath . "lds/actions/lds/implement.php");
    register_action("lds/manage_license", false, $CONFIG->pluginspath . "lds/actions/lds/manage_license.php");

    register_action("lds/manage_vle", false, $CONFIG->pluginspath . "lds/actions/lds/manage_vle.php");
    register_action("lds/admin/manage_vle", false, $CONFIG->pluginspath . "lds/actions/lds/admin/manage_vle.php", true);

    register_action("lds/debug/manage_debug", false, $CONFIG->pluginspath . "lds/actions/lds/debug/manage_debug.php");

    register_action("lds/pre_upload", false, $CONFIG->pluginspath . "lds/actions/lds/pre_upload.php");

    register_action("lds/save_glueps", false, $CONFIG->pluginspath . "lds/actions/lds/save_glueps.php");

	register_action("lds/save", false, $CONFIG->pluginspath . "lds/actions/lds/save.php");
	register_action("lds/save_editor", false, $CONFIG->pluginspath . "lds/actions/lds/save_editor.php");
	register_action("lds/delete", false, $CONFIG->pluginspath . "lds/actions/lds/delete.php");
	register_action("lds/recover", false, $CONFIG->pluginspath . "lds/actions/lds/recover.php");
	register_action("lds/upload", false, $CONFIG->pluginspath . "lds/actions/lds/upload.php");
	register_action("lds/publish", false, $CONFIG->pluginspath . "lds/actions/lds/publish.php");
	register_action("lds/publish_editor", false, $CONFIG->pluginspath . "lds/actions/lds/publish_editor.php");
	register_action("lds/share", false, $CONFIG->pluginspath . "lds/actions/lds/share.php");
    register_action("lds/shareng", false, $CONFIG->pluginspath . "lds/actions/lds/shareng.php");
	register_action("lds/ping_editing", false, $CONFIG->pluginspath . "lds/actions/lds/ping_editing.php");
	register_action("lds/pdf_export", false, $CONFIG->pluginspath . "lds/actions/lds/pdf_export.php");
	register_action("lds/pdf_export_editor", false, $CONFIG->pluginspath . "lds/actions/lds/pdf_export_editor.php");
	register_action("lds/ping_editing_editor", false, $CONFIG->pluginspath . "lds/actions/lds/ping_editing_editor.php");
	register_action("lds/file_export", false, $CONFIG->pluginspath . "lds/actions/lds/file_export.php");
	//register_action("lds/mass_add_user", false, $CONFIG->pluginspath . "lds/actions/lds/mass_add_user.php");
	register_action("lds/deferred_send", false, $CONFIG->pluginspath . "lds/actions/lds/deferred_send.php");
	register_action("lds/import_editor_file", false, $CONFIG->pluginspath . "lds/actions/lds/import_editor_file.php");
    register_action("lds/display_image", false, $CONFIG->pluginspath . "lds/actions/lds/display_image.php");
    register_action("lds/tree_lds_view", false, $CONFIG->pluginspath . "lds/actions/lds/tree_lds_view.php");

    //projects
    register_action("lds/projects/save", false, $CONFIG->pluginspath . "lds/actions/lds/projects/save.php");
    register_action("lds/projects/implement", false, $CONFIG->pluginspath . "lds/actions/lds/projects/implement.php");
    register_action("lds/projects/update_preview", false, $CONFIG->pluginspath . "lds/actions/lds/projects/update_preview.php");

    if (get_context() == 'admin')
        add_submenu_item(T("Manage VLEs"), $CONFIG->wwwroot . 'pg/lds/admin/vle/');

    require_once __DIR__.'/lds_viewTools.php';

	//Include the helper functions
    require_once __DIR__.'/lds_contTools.php';

    //Include templates config

    require_once __DIR__.'/templates/project_templates.php';


    extend_elgg_settings_page('lds/settings/help', 'usersettings/user');
    register_plugin_hook('usersettings:save','user','ldshake_contextual_help_settings_save');

}

register_elgg_event_handler('init','system','lds_init');

register_plugin_hook('permissions_check', 'object', 'lds_write_permission_check');

function lds_write_permission_check($hook, $entity_type, $returnvalue, $params)
{
    //$time=microtime(true);
    $subtype = $params['entity']->getSubtype();

    if ($subtype == 'LdS' || $subtype == 'LdS_implementation' || $subtype == 'LdSProject' || $subtype == 'LdSProject_implementation') {

        if($returnvalue)
            return $returnvalue;

        $result = lds_contTools::LdSCanEdit($params['entity']->guid, $params['user']);
        //echo microtime(true)-$time.' pf<br>';

        if($result)
            return $result;

        //check for project relationship
        if($rel_entities = $params['entity']->getEntitiesFromRelationship("lds_project_existent")) {
            foreach($rel_entities as $rel_entity) {
                if(lds_contTools::LdSCanEdit($rel_entity->guid, $params['user']))
                    return true;
            }
        }
    }
}

function LdSAnnotations($type, $subtype, $annotation, $threshold = 1, $owner_guid = 0, $count = false, $limit = 0, $offset = 0) {
    global $CONFIG;

    $query_limit = ($limit == 0) ? '' : "limit {$offset}, {$limit}";
    $subtype = get_subtype_id($type, $subtype);
    $owner = ($owner_guid == 0) ? '' : " AND owner_guid ='{$owner_guid}'";

    $annotation = get_metastring_id($annotation);

    $query = <<<SQL
SELECT * from {$CONFIG->dbprefix}entities e WHERE e.type = '{$type}' AND e.subtype = $subtype AND e.enabled = 'yes' AND (
	(
		(
			SELECT DISTINCT COUNT(id) FROM {$CONFIG->dbprefix}annotations ac WHERE ac.entity_guid = e.guid AND ac.name_id = {$annotation} {$owner}
		) >= {$threshold}

	)
) order by time_updated asc {$query_limit}
SQL;

    $entities = get_data($query, "entity_row_to_elggstar");

    if($count)
        return count($entities);

    return $entities;
}

function lds_page_handler ($page)
{
    global $CONFIG, $ldshake_jscache_break;

    //global $start_time;

    //sleep(10);


    $ldshake_dir = dirname(__FILE__);
    $js_source = array(
        '/ckeditor4/contents.css',
        '/ckeditor4/config.js',
        '/autoSuggest/jquery.autoSuggest.js',
    );

    $js_modified_string = '';
    foreach($js_source as $js) {
        $js_modified = filemtime($ldshake_dir . $js);
        $js_modified_string .= "{$js_modified}";
    }
    $ldshake_jscache_break["lds"] = hash("crc32b", $js_modified_string);

/*
    $multipart_serializer = function($payload = array()) {
        return $payload;
    };
*/
	//Nothing here will be exposed to non-logged users, so go away!
	//UPDATE: Except for the external view...


	if ($page[0] != 'viewext' && $page[0] != 'viewexteditor' && $page[0] != 'imgdisplay')
		gatekeeper();

	
	//Special case: my lds's url is short: doesn't have an explicit section, so we add it automatically
	if ($page[0] == '' || $page[0] == 'created-by-me' || $page[0] == 'shared-with-me' || $page[0] == 'created-with')
		array_unshift($page, 'main');
		
	//Sub_controller dispatcher
	if (function_exists("lds_exec_{$page[0]}"))
	{
		//If the user is new, we'll create an LdS for her

        //$user = get_loggedin_user();
/*
		if ($user->isnew == '1')
		{
			//Pau: Add a welcome LdS
			//present in actions/register.php and actions/useradd.php
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
			$doc->description = elgg_view('lds/welcome_lds/welcome_lds_'.$CONFIG->language);
			$doc->owner_guid = $user->guid;
			$doc->container_guid = $wl->guid;
			$doc->access_id = 2;
			$doc->save();
			//End add a welcome LdS

			$user->isnew = '0';
			$user->save();
		}
		*/
		set_context("lds_exec_{$page[0]}");
		call_user_func("lds_exec_{$page[0]}", $page);
	}
	else
		lds_exec_404 ();
}

function lds_exec_main ($params)
{
    global $ldshake_css, $start_time;
    //echo microtime(true) - $start_time.' start2<br />';
    $offset = get_input('offset') ?: '0';

    if ($params[1] == 'created-by-me')
    {
        $vars['count'] = get_entities('object', 'LdS', get_loggedin_userid(), '', 50, $offset, true);
        $entities = get_entities('object', 'LdS', get_loggedin_userid(), 'time_updated DESC', 50, $offset);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("LdS created by me");
    }
    elseif ($params[1] == 'shared-with-me')
    {
        $vars['count'] = lds_contTools::getUserSharedLdSWithMe(get_loggedin_userid(), true);
        $entities = lds_contTools::getUserSharedLdSWithMe(get_loggedin_userid(), false, 50, $offset);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("LdS shared with me");
    }
    elseif ($params[1] == 'created-with')
    {
        $vars['count'] = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), true, 0 , 0, "editor_type", $params[2]);
        $entities = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), false, 50, $offset, "editor_type", $params[2]);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("Created with").$params[2];
        $vars['editor_filter'] = $params[2];
    }
    else
    {
        //$time = microtime(true);
        $vars['count'] = lds_contTools::getUserEditableLdS(get_loggedin_userid(), true);
        //echo microtime(true) - $time.' bc<br />';
        //$time = microtime(true);
        $vars['list'] = lds_contTools::getUserEditableLdS(get_loggedin_userid(), false, 50, $offset, null, null, "time", true);
        //echo microtime(true) - $time.' be<br />';
        //$time = microtime(true);
        //$vars['list'] = lds_contTools::enrichLdS($entities);
        //echo microtime(true) - $time.' el<br />';

        $vars['title'] = T("All my LdS");
    }

    $ldshake_css['#layout_canvas']['min-height'] = $vars['count'] * 28;

    $vars['section'] = $params[1];


    //echo microtime(true) - $start_time.' start25<br />';
    $body = elgg_view('lds/mylds',$vars);
    //echo microtime(true) - $start_time.' start3<br />';

    //echo microtime(true) - $time.' brl<br />';
    $offset = get_input('offset') ?: '0';

    $time = microtime(true);
    page_draw($vars['title'], $body);
    //echo microtime(true) - $time.' draw<br />';
    //echo microtime(true) - $start_time.' start4<br />';
}

function lds_exec_implementable ($params)
{
    global $CONFIG;
    $offset = get_input('offset') ?: '0';
    $user = get_loggedin_user();

    if (strlen($params[1]))
    {
        $vars['count'] = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), true, 0 , 0, "editor_type", $params[1]);
        $entities = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), false, 50, $offset, "editor_type", $params[1]);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("Created with") . ' ' . $params[1];
        $vars['editor_filter'] = $params[1];
    }
    else
    {
        $vars['count'] = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), true, 0 , 0, "implementable", '1');
        $entities = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), false, 50, $offset, "implementable", '1');
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("All my LdS > implementable in VLE");
    }

    $vars['section'] = $params[1];

    if($vles = get_entities('object','user_vle', get_loggedin_userid(), '', 9999)) {
        $vle_data = array();
        foreach($vles as $vle) {
            $vlemanager = VLEManagerFactory::getManager($vle);

            if($vle_info = $vlemanager->getVleInfo()) {
                $vle_info->item = $vle;
                $vle_data[$vle->guid] = $vle_info;
            }
        }

        //no valid vles
        if(empty($vle_data)) {
            register_error(T("None of the registered VLEs are working."));
            forward($CONFIG->url.'pg/lds/vle');
        }

    } else {
        register_error(T("You need to register a VLE first."));
        forward($CONFIG->url.'pg/lds/vle');
    }

    $vars['vle_data'] = $vle_data;

    //$vars['vle_info'] = GluepsManager::getVleInfo();//lds_contTools::getVLECourses($vle);
    $body = elgg_view('lds/implementable',$vars);

    page_draw($vars['title'], $body);
}

function lds_exec_implementations ($params)
{
    $offset = get_input('offset') ?: '0';

    if ($params[1] == 'created-by-me')
    {
        $vars['count'] = get_entities('object', 'LdS_implementation', get_loggedin_userid(), '', 50, $offset, true);
        $entities = get_entities('object', 'LdS_implementation', get_loggedin_userid(), 'time_updated DESC', 50, $offset);
        $vars['list'] = lds_contTools::enrichImplementation($entities);
        $vars['title'] = T("Created by me > Implementations");
    }
    elseif ($params[1] == 'shared-with-me')
    {
        $vars['count'] = lds_contTools::getUserSharedImplementationWithMe(get_loggedin_userid(), true, 0 , 0);
        $entities = lds_contTools::getUserSharedImplementationWithMe(get_loggedin_userid(), false, 50, $offset);
        $vars['list'] = lds_contTools::enrichImplementation($entities);
        $vars['title'] = T("Shared with me > Implementations");
        $vars['editor_filter'] = $params[1];
    }
    elseif ($params[1] == 'design')
    {
        $design_guid = $params[2];
        $lds = get_entity($design_guid);
        //$vars['count'] = lds_contTools::getUserEditableImplementations(get_loggedin_userid(), true, null, null, null, null, $design_guid);
        //$entities = lds_contTools::getUserEditableImplementations(get_loggedin_userid(), false, 50, $offset, null, null, $design_guid);
        $vars['count'] = lds_contTools::getUserEditableImplementations(get_loggedin_userid(), true, null, null, 'lds_id', $design_guid);
        $entities = lds_contTools::getUserEditableImplementations(get_loggedin_userid(), false, 50, $offset, 'lds_id', $design_guid);
        $vars['list'] = lds_contTools::enrichImplementation($entities);
        $vars['title'] = T("All my LdS > Implementations > ") . $lds->title;
        $vars['editor_filter'] = $params[1];
    }
    elseif ($params[1] == 'trashed')
    {
        //We need to see all the entities, including the deleted ones.
        $access_status = access_get_show_hidden_status();
        access_show_hidden_entities(true);

        $offset = get_input('offset') ?: '0';

        //TODO take the owner into account
        $vars['list'] = get_entities_where('enabled = "no"', 'object', 'LdS_implementation', get_loggedin_userid(), 'time_updated DESC', 50, $offset);
        $vars['count'] = get_entities_where('enabled = "no"', 'object', 'LdS_implementation', get_loggedin_userid(), '', 0, $offset, true);
        $vars['list'] = lds_contTools::enrichImplementation($vars['list']);
        $vars['title'] = T("My trashed LdS");
        $vars['section'] = "imp-{$params[1]}";

        access_show_hidden_entities($access_status);
    }
    else
    {
        $vars['count'] = lds_contTools::getUserEditableImplementations(get_loggedin_userid(), true);
        $entities = lds_contTools::getUserEditableImplementations(get_loggedin_userid(), false, 50, $offset);
        $vars['list'] = lds_contTools::enrichImplementation($entities);
        $vars['title'] = T("All my LdS > Implementations");
    }

    $vars['designfilter'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), false, 9999, 0, 'implementable', '1');
    $vars['section'] = 'imp-'.$params[1];
    //$vars['courses'] = lds_contTools::getVLECourses($vle);
    $body = elgg_view('lds/implementations',$vars);

    page_draw($vars['title'], $body);
}

function lds_exec_search ($params) {
    ldshake_stats_log_event('search');
	$query = urldecode(get_input('q', ""));
    $offset = (int)get_input('offset', 0);
	
	$vars['query'] = $query;
    $vars['list'] = false;
    //if($list = lds_contTools::searchLdS($query, 11, $offset))
	//    $vars['list'] = lds_contTools::enrichLdS($list);

    $vars['list'] = lds_contTools::searchLdS($query, 11, $offset, get_loggedin_userid(), false, null, null, true);
    //$vars['count'] = count ($vars['list']) + $offset;
    $vars['count'] = lds_contTools::searchLdS($query, 0, 0, get_loggedin_userid(), true);
    //$vars['list'] = array_slice($vars['list'], $offset, 10);


	$body = elgg_view('lds/search',$vars);
	page_draw($query, $body);
}

function lds_exec_vle ($params)
{
    $vlelist = get_entities('object', 'user_vle', get_loggedin_userid(), '', 9999);
    $svlelist = get_entities('object', 'system_vle', 0, '', 9999);
    if(!$vlelist)
        $vlelist = array();

    if(!isset($params[1]) || !is_numeric($params[1])) {
        $vle = new ElggObject();
        $vle->subtype = 'user_vle';
        $vle->access_id = ACCESS_PUBLIC;
        $vle->owner_guid = get_loggedin_userid();
        $vle->name = '';
        $vle->username = '';
        $vle->password = '';
        $vle->vle_url = '';
        $vle->vle_type = '';
        $vle->new = 1;
        $id = 0;
        $vle_info = true;
    } else {
        $vle = get_entity($params[1]);
        $id = $vle->guid;
        $vlemanager = VLEManagerFactory::getManager($vle);
        $vle_info = $vlemanager->getVleInfo();
    }
    $svlelist_data = array();

    if(is_array($svlelist))foreach($svlelist as $svle) {
        $svle_data = array();
        $svle_data['name']=$svle->name;
        $svle_data['vle_type']=$svle->vle_type;
        $svle_data['vle_url']=$svle->vle_url;
        $svlelist_data[$svle->guid] = $svle_data;
    }

    $vars = array(
        'vle' => $vle,
        'vle_id' => $id,
        'vle_info' => $vle_info,
        'vlelist' => $vlelist,
        'svlelist' => $svlelist,
        'svlelist_data' => $svlelist_data,
    );
    $body = elgg_view('lds/vledata',$vars);
    page_draw('VLE', $body);
}

function lds_exec_vledata ($params)
{
    global $CONFIG;
    $user = get_loggedin_user();
    ldshake_stats_log_event('vledata');

    $vlelist = get_entities('object', 'user_vle', get_loggedin_userid(), '', 9999);
    if(!$vlelist) {
        register_error("You need to register a VLE first.");
        forward($CONFIG->url.'pg/lds/vle');
    }

    if(!isset($params[1]) || !is_numeric($params[1])) {
        if(count($vlelist))
            $vle = $vlelist[0];
        else {
            $vle = new ElggObject();
            $vle->subtype = 'user_vle';
            $vle->access_id = ACCESS_PUBLIC;
            $vle->owner_guid = get_loggedin_userid();
            $vle->name = '';
            $vle->username = '';
            $vle->password = '';
            $vle->vle_url = '';
            $vle->vle_type = '';
            $vle->new = 1;

            $vle = null;
        }
    } else {
        $vle = get_entity($params[1]);
    }

    if($vle) {
        $gluepsm = new GluepsManager($vle);
        $vle_info = $gluepsm->getVleInfo();

        $courses = (array)$vle_info->courses;
        ksort($courses);
        $participants = array();

        foreach($courses as $key => $fvle) {
            $participants[$key] = (array)$gluepsm->getCourseInfo($key)->participants;
            //uasort($participants[$key], function($a, $b) { return (int)$a->id - (int)$b->id; });
            uasort($participants[$key], function($a, $b) { return strnatcasecmp($a->name, $b->name);});
        }

        $external_tools = (array)$vle_info->externalTools;
        ksort($external_tools);

        $internal_tools = (array)$vle_info->internalTools;
        ksort($internal_tools);

        //$courses = lds_contTools::getVLECourses($vle);
        $vars = array(
            'vle' => $vle,
            'vlelist' => $vlelist,
            'vle_info' => $vle_info,
            'participants' => $participants,
            'internal_tools' => $internal_tools,
            'external_tools' => $external_tools,
            'section' => 'courses'
        );
    } else {
        $vars = array(
            'vle' => null,
            'vle_info' => null,
            'participants' => null,
            'internal_tools' => null,
            'external_tools' => null,
            'section' => 'courses'
        );
    }

    if(isset($params[2]))
        if(strlen($params[2]))
            $vars['section'] = $params[2];

    $body = elgg_view('lds/vledatacomplete',$vars);
    page_draw('VLE', $body);
}

function lds_exec_trashed ($params)
{
	//We need to see all the entities, including the deleted ones.
	$access_status = access_get_show_hidden_status();
	access_show_hidden_entities(true);
	
	$offset = get_input('offset') ?: '0';
	
	//TODO take the owner into account
	$vars['list'] = get_entities_where('enabled = "no"', 'object', LDS_ENTITY_TYPE, get_loggedin_userid(), 'time_updated DESC', 50, $offset);
	$vars['count'] = get_entities_where('enabled = "no"', 'object', LDS_ENTITY_TYPE, get_loggedin_userid(), '', 0, $offset, true);
	$vars['list'] = lds_contTools::enrichLdS($vars['list']);
	$vars['title'] = T("My trashed LdS");

	$vars['section'] = $params[0];
	$body = elgg_view('lds/mylds',$vars);
	page_draw($vars['title'], $body);
	
	access_show_hidden_entities($access_status);
}

function lds_exec_viewtrashed ($params)
{
	//We need to see all the entities, including the deleted ones.
	$access_status = access_get_show_hidden_status();
	access_show_hidden_entities(true);
	
	$lds = get_entity($params[1]);
	$vars['lds'] = $lds;
	
	$vars['ldsDocs'] = lds_contTools::getLdsDocuments($lds->guid);
	$vars['currentDocId'] = $params[3] ?: $vars['ldsDocs'][0]->guid;
	$vars['currentDoc'] = get_entity($vars['currentDocId']);
	
	//Securuty: we won't show a document unless it's part of this LdS
	if ($vars['currentDoc']->lds_guid != $lds->guid)
	{
		lds_exec_404();
		return;	
	}
	
	$vars['section'] = $params[0];
	$body = elgg_view('lds/viewtrashed',$vars);
	page_draw($vars['title'], $body);
	
	access_show_hidden_entities($access_status);
}

function lds_exec_browse_test ($params)
{
    $order = get_input('order') ?: 'newest';
    $offset = get_input('offset') ?: '0';

    //$vars['tags'] = lds_contTools::getAllTagsAndFrequenciesUsage (get_loggedin_userid());

    $tools = array();

    $vars['editor_subtype'] = array(
        'design_pattern' => 'Design Pattern',
        'MDN' => 'Design Narrative',
        'PC' => 'Persona Card',
        'FC' => 'Factors and Concerns',
        'HE' => 'Heuristic Evaluation',
        'coursemap' => 'Course Map',
    );

    $vars['editor_type'] = array(
        'cld' => 'CompendiumLD',
        'webcollagerest' => 'WebCollage',
        'openglm' => 'OpenGLM',
        'cadmos' => 'CADMOS',
        'image' => 'Image',
    );

    //$tools['editor_subtype'] =

    $vars['filtering'] = false;
    //If there is some filtering by tag
    if (get_input('tagk') && get_input('tagv'))
    {
        $vars['tagk'] = urldecode(get_input('tagk'));
        $vars['tagv'] = urldecode(get_input('tagv'));

        $title = T("LdS tagged %1",$vars['tagv']);
        //Keep them just in case we want to recover the old functionality of listing the LdS which are not mine.
        $vars['list'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), false, 10, $offset, $vars['tagk'], $vars['tagv'], "title");
        $vars['count'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), true, 0, 0, $vars['tagk'], $vars['tagv']);
        $vars['filtering'] = true;
    }
    //It's just a whole list
    else
    {
        $title = T("Browse LdS");

        $vars['list'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), false, 10, $offset, null, null, "title");
        $vars['count'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), true);
    }

    if(!is_array($vars['list'])) {
        $vars['list'] = array();
    }

    //$vars['list'] = lds_contTools::enrichLdS($vars['list']);

    //$body = elgg_view('lds/browse',$vars);
    //page_draw($title, $body);
}

function lds_exec_browse ($params)
{
    global $ldshake_css, $start_time;
	$order = get_input('order', 'time');
	$offset = get_input('offset') ?: '0';

    $vars['order'] = $order;
    $time = microtime(true);
    $vars['tags'] = lds_contTools::getAllTagsAndFrequenciesUsage (get_loggedin_userid());
    //echo microtime(true) - $time.' tf<br />';

    /*
    $time = microtime(true);
    $vars['tags'] = lds_contTools::getAllTagsAndFrequenciesUsage (get_loggedin_userid());
    echo microtime(true) - $time.' tfl<br />';
    */

    $tools = array();

    $vars['editor_subtype'] = array(
        'design_pattern' => 'Design Pattern',
        'MDN' => 'Design Narrative',
        'PC' => 'Persona Card',
        'FC' => 'Factors and Concerns',
        'HE' => 'Heuristic Evaluation',
        'coursemap' => 'Course Map',
    );

    $vars['editor_type'] = array(
        'cld' => 'CompendiumLD',
        'webcollagerest' => 'WebCollage',
        'openglm' => 'OpenGLM',
        'cadmos' => 'CADMOS',
        'image' => 'Image',
    );

    //$tools['editor_subtype'] =

	$vars['filtering'] = false;
	//If there is some filtering by tag
    if (get_input('tagk') && get_input('tagv'))
    {
        $vars['tagk'] = urldecode(get_input('tagk'));
        $vars['tagv'] = urldecode(get_input('tagv'));

        if(in_array($vars['tagk'], array('editor_type', 'editor_subtype'))) {
            ldshake_stats_log_event('browse_tool', $vars['tagv']);
        } elseif(in_array($vars['tagk'], array('tags', 'discipline', 'pedagogical_approach'))) {
            ldshake_stats_log_event('browse_tag_' . $vars['tagk'], $vars['tagv']);
        }

        $title = T("LdS tagged %1",$vars['tagv']);
        //Keep them just in case we want to recover the old functionality of listing the LdS which are not mine.
        $vars['list'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), false, 10, $offset, $vars['tagk'], $vars['tagv'], $order, true);
        $vars['count'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), true, 0, 0, $vars['tagk'], $vars['tagv']);
        $vars['filtering'] = true;
    }
    //It's just a whole list
    else
    {
        $title = T("Browse LdS");

        //$time = microtime(true);
        $vars['count'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), true);
        //echo microtime(true) - $time.' bc<br />';
        //$time = microtime(true);
        $vars['list'] = lds_contTools::getUserViewableLdSs(get_loggedin_userid(), false, 10, $offset, null, null, $order, true);
        //echo microtime(true) - $time.' be<br />';
    }

    if(!is_array($vars['list'])) {
        $vars['list'] = array();
    }

    $ldshake_css['#layout_canvas']['min-height'] = $vars['count'] * 137;


    //$time = microtime(true);
    $body = elgg_view('lds/browse',$vars);
    //echo microtime(true) - $time.' start3<br />';

    //$time = microtime(true);
    page_draw($title, $body);
    //echo microtime(true) - $time.' start4<br />';
    //echo microtime(true) - $start_time.' start_finish<br />';

}

function lds_exec_new ($params)
{
	global $CONFIG;

	//Get the page that we come from (if we come from an editing form, we go back to my lds)
	$vars['referer'] = $CONFIG->url.'pg/lds/';
	
	//Create an empty LdS object to initialize the form
	$vars['initLdS'] = new stdClass();
	$vars['initLdS']->title = T("Untitled LdS");
	$vars['initLdS']->granularity = '0';
	$vars['initLdS']->completeness = '0';
	$vars['initLdS']->tags = '';
	$vars['initLdS']->discipline = '';
	$vars['initLdS']->pedagogical_approach = '';
	$vars['initLdS']->guid = '0';
	
	$vars['am_i_starter'] = true;
	
	$vars['all_can_read'] = 'true';
	
	$vars['initLdS'] = json_encode($vars['initLdS']);

    $vars['editor_type'] = 'doc';

	//And also an empty Document
	$vars['initDocuments'][0] = new stdClass();
	$vars['initDocuments'][0]->title = T("Untitled Document");
	$vars['initDocuments'][0]->guid = '0';
	$vars['initDocuments'][0]->modified = '0';
	$vars['initDocuments'][0]->body = '';

	//And a support doc!
	$vars['initDocuments'][1] = new stdClass();
	$vars['initDocuments'][1]->title = T("Support Document");
	$vars['initDocuments'][1]->guid = '0';
	$vars['initDocuments'][1]->modified = '0';
	$vars['initDocuments'][1]->body = '<p> '.T("Write here any support notes for this LdS...").'</p>';

    $vars['editor_subtype'] = 0;


    if(count($params) == 3) {
        switch($params[1]) {
            case 'template':
                require_once __DIR__.'/templates/templates.php';
                if($templates = ldshake_get_template($params[2])) {
                    $i=0;
                    foreach($templates as $template) {
                        $vars['initDocuments'][$i++]->body = $template;
                    }
                    $vars['editor_subtype'] = $params[2];
                } else {
                    forward();
                }

                break;
            case 'upload':
                $vars['initDocuments'][0]->body = '';
                $vars['editor_type'] = $params[3];
                $vars['editor_subtype'] = $params[3];

                break;
        }
    }

    $vars['initDocuments'] = json_encode($vars['initDocuments']);

    //$time = microtime(true);
	$vars['tags'] = json_encode(lds_contTools::getMyTags());
    //echo microtime(true) - $time." f<br>";
    //$time = microtime(true);
    $available = lds_contTools::getAvailableUsers(null);
    //echo microtime(true) - $time." u<br>";

    //$time = microtime(true);
    $vars['jsonfriends'] = json_encode(lds_contTools::entitiesToObjects($available));
    //echo microtime(true) - $time." fo<br>";
    $vars['viewers'] = json_encode(array());
    $vars['editors'] = json_encode(array());
    $vars['groups'] = json_encode(array());

    $vars['starter'] = get_loggedin_user();

    $vars['title'] = T("New LdS");

    $vars['editor_type'] = implode(',', array($vars['editor_type'], $vars['editor_subtype']));
    //$time = microtime(true);

    ldshake_stats_log_event('new', array($vars['editor_type'], $vars['editor_subtype']));

    echo elgg_view('lds/editform',$vars);
    //echo microtime(true) - $time." form<br>";
    global $start_time;
    //echo microtime(true) - $start_time." start4<br>";
}

function lds_exec_upload ($params)
{
    global $CONFIG;

    $editor = editorsFactory::getTempInstance($params[1]);
    $vars = $editor->newEditor();

    //Get the page that we come from (if we come from an editing form, we go back to my lds)
    $vars['referer'] = $CONFIG->url.'pg/lds/';

    //Create an empty LdS object to initialize the form
    $vars['initLdS'] = new stdClass();
    $vars['initLdS']->title = T("Untitled LdS");
    $vars['initLdS']->granularity = '0';
    $vars['initLdS']->completeness = '0';
    $vars['initLdS']->tags = '';
    $vars['initLdS']->discipline = '';
    $vars['initLdS']->pedagogical_approach = '';
    $vars['initLdS']->guid = '0';

    $vars['am_i_starter'] = true;

    $vars['all_can_read'] = 'true';

    $vars['initLdS'] = json_encode($vars['initLdS']);

    //Add a support doc!
    $vars['initDocuments'][0] = new stdClass();
    $vars['initDocuments'][0]->title = T("Support Document");
    $vars['initDocuments'][0]->guid = '0';
    $vars['initDocuments'][0]->modified = '0';
    $vars['initDocuments'][0]->body = '<p> '.T("Write here any support notes for this LdS...").'</p>';

    $vars['editor_type'] = $params[1];
    $vars['editor_subtype'] = $params[1];
    $vars['upload'] = true;

    $vars['initDocuments'] = json_encode($vars['initDocuments']);

    $vars['tags'] = json_encode(lds_contTools::getMyTags ());

    $available = lds_contTools::getAvailableUsers(null);

    $vars['jsonfriends'] = json_encode(lds_contTools::entitiesToObjects($available));
    $vars['viewers'] = json_encode(array());
    $vars['editors'] = json_encode(array());
    $vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));

    $vars['starter'] = get_loggedin_user();

    $vars['title'] = T("New LdS");

    ldshake_stats_log_event('new', array($vars['editor_type'], $vars['editor_subtype']));
    $vars['editor_type'] = implode(',', array($vars['editor_type'], $vars['editor_subtype']));


    $vars['upload_link'] = "";

    if($params[1] == 'openglm')
        $vars['upload_link'] = "OpenGLM is a desktop tool, you need to <a href=\"http://sourceforge.net/projects/openglm/files/\" target=\"_blank\">download</a> it and install it in your computer. After creating the learning design you can upload it to the system, tag it, share it with other LdShakers, comment it, etc.";
    if($params[1] == 'cadmos')
        $vars['upload_link'] = "CADMOS is a desktop tool, you need to <a href=\"http://cosy.ds.unipi.gr/cadmos/\" target=\"_blank\">download</a> it and install it in your computer. After creating the learning design you can upload it to the system, tag it, share it with other LdShakers, comment it, etc.";
    if($params[1] == 'cld')
        $vars['upload_link'] = "CompendiumLD is a desktop tool, you need to <a href=\"http://compendiumld.open.ac.uk/\" target=\"_blank\">download</a> it and install it in your computer. After creating the learning design you can upload it to the system, tag it, share it with other LdShakers, comment it, etc.";
    if($params[1] == 'image')
        $vars['upload_link'] = "Upload a jpeg, png, gif or svg image.";


    echo elgg_view('lds/editform_editor',$vars);
}

function lds_exec_neweditor ($params)
{
	global $CONFIG;

    $template_html = null;
    $editor_subtype = null;

    switch($params[2]) {
        case 'template':
            require_once __DIR__.'/templates/templates.php';
            $preferred_formats = array('docx','xlsx', null);
            foreach($preferred_formats as $format){
                $template_format = $format;
                if($templates = ldshake_get_template($params[3], $format))
                    break;
            }

            if(empty($templates)) {
                $template = null;
                $template_format = null;
                break;
            }
            //$templates = ldshake_get_template($params[3]);
            $editor_subtype = $params[3];
            $template = $templates[0];

            $template_doc = new ElggObject();
            $template_doc->description = $template;
            $template_vars = array(
                'doc' => $template_doc,
                'title' => $params[3],
                'format' => $template_format
            );

            if(!$template_format)
                $template_html = elgg_view('lds/view_iframe', $template_vars);
            else
                $template_html = $template;
        break;
    }

	//Make an editor object according to the parameters received and create a new session
	$editor = editorsFactory::getTempInstance($params[1]);
	$vars = $editor->newEditor($template_html, $template_format);
    if(!$vars) {
        register_error("New document error");
        forward($CONFIG->url . 'pg/lds/');
    }

    $vars['editor_subtype'] = $editor_subtype;

	//Get the page that we come from (if we come from an editing form, we go back to my lds)
	$vars['referer'] = $CONFIG->url.'pg/lds/';
	
	//Create an empty LdS object to initialize the form
	$vars['initLdS'] = new stdClass();
	$vars['initLdS']->title = T('Untitled LdS');
	$vars['initLdS']->granularity = '0';
	$vars['initLdS']->completeness = '0';
	$vars['initLdS']->tags = '';
	$vars['initLdS']->discipline = '';
	$vars['initLdS']->pedagogical_approach = '';
	$vars['initLdS']->guid = '0';

    //And a support doc!
    if(!strstr($params[1], 'google')) {
        $vars['initDocuments'][0] = new stdClass();
        $vars['initDocuments'][0]->title = T("Support Document");
        $vars['initDocuments'][0]->guid = '0';
        $vars['initDocuments'][0]->modified = '0';
        $vars['initDocuments'][0]->body = '<p> '.T("Write here any support notes for this LdS...").'</p>';

        $vars['initDocuments'] = json_encode($vars['initDocuments']);
    } else {
        $vars['supportGoogleDoc'];

        $temp_support_doc = new ElggObject();
        $temp_support_doc->description = T("Write here any support notes for this LdS...").'<img border="0" width="0" height="0" src="http://ilde.upf.edu/mod/lds/templates/gdocs_fix/gdfix.png">';
        $temp_support_doc->title = T("Support Document");

        $support_vars = array(
            'doc' => $temp_support_doc,
            'title' => T("Support Document")
        );

        $support_html = elgg_view('lds/view_iframe', $support_vars);
        $support_editor = editorsFactory::getTempInstance('google_docs');
        $support_vars = $support_editor->newEditor($support_html);
        $vars['support_editor'] = $support_vars;
        $vars['initDocuments'] = json_encode(array());
        $vars['lds_title'] = T('Untitled LdS');
    }

	$vars['all_can_read'] = 'true';
	$vars['initLdS'] = json_encode($vars['initLdS']);
	$vars['tags'] = json_encode(lds_contTools::getMyTags ());

    /*
	$vars['jsonfriends'] = json_encode(lds_contTools::getFriendsArray(get_loggedin_userid()));
	$vars['viewers'] = json_encode(array());
	$vars['editors'] = json_encode(array());
	$vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));
	*/

    $available = lds_contTools::getAvailableUsers(null);

    $vars['jsonfriends'] = json_encode(lds_contTools::entitiesToObjects($available));
    $vars['viewers'] = json_encode(array());
    $vars['editors'] = json_encode(array());
    $vars['groups'] = json_encode(array());

	$vars['starter'] = get_loggedin_user();
	
	$vars['am_i_starter'] = true;

    $vars['editor_type'] = $params[1];

	$vars['title'] = T("New LdS");

    ldshake_stats_log_event('new', array($vars['editor_type'], $vars['editor_subtype']));
	
	echo elgg_view('lds/editform_editor',$vars);
}

/**
 * Pass the directory to import in the url
 */
function lds_exec_importexe ($params)
{
	global $CONFIG;
	set_time_limit (0);
	ini_set('memory_limit', '512M');
	
	array_shift($params);
	$params = array_filter($params);
	
	$basedir = 'E:/importexe/';
	
	$dir = $basedir . implode('/', $params);
	
	echo "Importing $dir...<br /><br /><br />";
	
	$iterator = new DirectoryIterator($dir);
	foreach ($iterator as $fileinfo) {
		if ($fileinfo->isFile()) {
			$pathname = $fileinfo->getPathname();
			$filename = $fileinfo->getFilename();
			$title = substr($filename, 0, strrpos($filename, '.'));

			echo "Importing $filename...";
			
			
			// ----------- IMPORT THINGIE ---------------
			//Make an editor object according to the parameters received and create a new session
			$editor = editorsFactory::getTempInstance('exe');
			$vars = $editor->importEditor($pathname);
		
			//We're creating it from scratch. Construct a new obj.
			$lds = new LdSObject();
			$lds->owner_guid = get_loggedin_userid();
			$lds->external_editor = true;
			$user = get_loggedin_user();
			$lds->save();
		
			$document_editor = new DocumentEditorObject($lds->guid, 0);
			$document_editor->editorType = 'exe';
			$document_editor->lds_guid = $lds->guid;
			$document_editor->lds_revision_id = 0;
			$document_editor->save();
			
			$lds->title = $title;
			$lds->granularity = 6;
			$lds->completeness = 1;
			$lds->access_id = 1;
			$lds->write_access_id = 0;
			$lds->save();
			
			
			//TODO tags
			
			
			//We save for the first time in this edition session. So we create a revision.
			create_annotation($lds->guid, 'revised_docs_editor', '', 'text', get_loggedin_userid(), 1);
			
			//Save the exe document
			$editor = editorsFactory::getInstance($document_editor);
		
			list($document_editor, $resultIds_add) = $editor->saveDocument($vars['editor_id']);
		
			//We get the revision id to send it back to the form
			$editordocument = get_entities_from_metadata('lds_guid',$lds->guid,'object','LdS_document_editor', 0, 100);
			DocumentEditorRevisionObject::createRevisionFromDocumentEditor($editordocument[0]);
			
			// ----------- END OF IMPORT THINGIE ---------------
			echo " Done.<br />";			
		}
	}
	echo "Finished!";
}

function lds_exec_edit ($params)
{
    global $CONFIG;
	//Get the page that we come from (if we come from an editing form, we go back to my lds)
	//if (preg_match('/(new|edit)/',$_SERVER['HTTP_REFERER']))
		$vars['referer'] = $CONFIG->url.'pg/lds/';
	//else
	//	$vars['referer'] = $_SERVER['HTTP_REFERER'];
	
	$editLdS = get_entity($params[1]);

    $vars['referer'] = ldshake_lds_referer($editLdS);
	
	if (!$editLdS->canEdit())
	{
		register_error("You don't have permissions to edit this LdS.");
		header("Location: " . $_SERVER['HTTP_REFERER']);
		return '';
	}
	
	if ($user = lds_contTools::isLockedBy($params[1]))
	{
		$fstword = explode(' ',$user->name);
		$fstword = $fstword[0];
		register_error("{$user->name} is editing this LdS. You cannot edit it until {$fstword} finishes.");
		header("Location: " . $_SERVER['HTTP_REFERER']);
	}

    //create_annotation($editLdS->guid, 'viewed_lds', '1', 'text', get_loggedin_userid(), 2);
	//lds_contTools::markLdSAsViewed ($params[1]);

	//Pass the LdS properties to the form
	$vars['initLdS'] = new stdClass();
	$vars['initLdS']->title = $editLdS->title;
	$vars['initLdS']->granularity = $editLdS->granularity;
	$vars['initLdS']->completeness = $editLdS->completeness;
	
	$tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
	foreach ($tagtypes as $type)
	{
		if (is_array($editLdS->$type))
			$vars['initLdS']->$type = implode(',',$editLdS->$type);
		elseif (is_string($editLdS->$type) && strlen($editLdS->$type))
			$vars['initLdS']->$type = $editLdS->$type;
		else
			$vars['initLdS']->$type = '';
	}
	
	$vars['initLdS']->guid = $params[1];
	$vars['am_i_starter'] = (get_loggedin_userid() == $editLdS->owner_guid);

	$vars['all_can_read'] = ($editLdS->all_can_view == 'yes' || ($editLdS->all_can_view === null && $editLdS->access_id < 3 && $editLdS->access_id > 0)) ? 'true' : 'false';
	
	$vars['initLdS'] = json_encode($vars['initLdS']);
	
	//For each of the documents that this LdS has...
	$documents = get_entities_from_metadata('lds_guid',$params[1],'object','LdS_document', 0, 100);
	
	//Send their data to the form
	foreach ($documents as $doc)
	{
		$obj = new stdClass();
		$obj->title = $doc->title;
		$obj->guid = $doc->guid;
		$obj->body = $doc->description;
		$obj->modified = '0';
		$vars['initDocuments'][] = $obj;
	}
	
	Utils::osort($vars['initDocuments'], 'guid');
	
	$vars['initDocuments'] = json_encode($vars['initDocuments']);
	$vars['tags'] = json_encode(lds_contTools::getMyTags ()); 
	
	//These are all my friends
    $arrays = lds_contTools::buildObjectsArray($editLdS);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));
    $vars['starter'] = get_user($editLdS->owner_guid);

    $vars['title'] = T("Edit LdS");
    $vars['editor_type'] = $editLdS->editor_type;

    echo elgg_view('lds/editform',$vars);
}

function lds_exec_editeditor ($params)
{
	global $CONFIG;
	
	//Get the page that we come from (if we come from an editing form, we go back to my lds)
	if (preg_match('/(neweditor|editeditor)/',$_SERVER['HTTP_REFERER']))
		$vars['referer'] = $CONFIG->url.'pg/lds/';
	else
		$vars['referer'] = $_SERVER['HTTP_REFERER'];
	
	$editLdS = get_entity($params[1]);
	
	if (!$editLdS->canEdit())
	{
		register_error("You don't have permissions to edit this LdS.");
		header("Location: " . $_SERVER['HTTP_REFERER']);
	}
	
	if ($user = lds_contTools::isLockedBy($params[1]) && !lds_contTools::isSyncLdS($editLdS))
	{
		$fstword = explode(' ',$user->name);
		$fstword = $fstword[0];
		register_error("{$user->name} is editing this LdS. You cannot edit it until {$fstword} finishes.");
		header("Location: " . $_SERVER['HTTP_REFERER']);
	}

    //create_annotation($editLdS->guid, 'viewed_lds', '1', 'text', get_loggedin_userid(), 2);

	//Pass the LdS properties to the form
	$vars['initLdS'] = new stdClass();
	$vars['initLdS']->title = $editLdS->title;
	$vars['initLdS']->granularity = $editLdS->granularity;
	$vars['initLdS']->completeness = $editLdS->completeness;
	
	$tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
	foreach ($tagtypes as $type)
	{
		if (is_array($editLdS->$type))
			$vars['initLdS']->$type = implode(',',$editLdS->$type);
		elseif (is_string($editLdS->$type) && strlen($editLdS->$type))
			$vars['initLdS']->$type = $editLdS->$type;
		else
			$vars['initLdS']->$type = '';
	}

	$vars['initLdS']->guid = $params[1];

	//For each of the documents that this LdS has...
	$documents = get_entities_from_metadata('lds_guid',$params[1],'object','LdS_document', 0, 100);
	$vars['initDocuments'] = array();

	if(is_array($documents)) {
		//Send their data to the form
		foreach ($documents as $doc)
		{
			$obj = new stdClass();
			$obj->title = $doc->title;
			$obj->guid = $doc->guid;
			$obj->body = $doc->description;
			$obj->modified = '0';
			$vars['initDocuments'][] = $obj;
		}
	} else {
		$vars['initDocuments'][0] = new stdClass();
		$vars['initDocuments'][0]->title = T("Support Document");
		$vars['initDocuments'][0]->guid = '0';
		$vars['initDocuments'][0]->modified = '0';
		$vars['initDocuments'][0]->body = '<p> '.T("Write here any support notes for this LdS...").'</p>';
	}

	Utils::osort($vars['initDocuments'], 'guid');
	$vars['initDocuments'] = json_encode($vars['initDocuments']);

	$vars['am_i_starter'] = (get_loggedin_userid() == $editLdS->owner_guid);
	$vars['starter'] = get_user($editLdS->owner_guid);
	//$vars['all_can_read'] = ($editLdS->access_id == '1') ? 'true':'false';
    $vars['all_can_read'] = ($editLdS->all_can_view == 'yes' || ($editLdS->all_can_view === null && $editLdS->access_id < 3 && $editLdS->access_id > 0)) ? 'true' : 'false';
	$vars['initLdS'] = json_encode($vars['initLdS']);
	$vars['tags'] = json_encode(lds_contTools::getMyTags ());
    $vars['lds_title'] = $editLdS->title;
	
	//These are all my friends
    /*
	$friends = lds_contTools::getFriendsArray(get_loggedin_userid());
	$arrays = lds_contTools::buildFriendArrays($friends, $editLdS->access_id, $editLdS->write_access_id);

	$vars['jsonfriends'] = json_encode(array_values($arrays['available']));
	$vars['viewers'] = json_encode($arrays['viewers']);
	$vars['editors'] = json_encode($arrays['editors']);
	$vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));
    */
    //These are all my friends
    $arrays = lds_contTools::buildObjectsArray($editLdS);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));

	$vars['title'] = T("Edit LdS");
	
	//We're editing. Fetch it from the DB
	$editordocument_query = get_entities_from_metadata('lds_guid',$editLdS->guid,'object','LdS_document_editor', 0, 100);

    if(strstr($editLdS->editor_type, 'google')) {
        if(!$editordocument_query[0]->support){
            $editordocument = array($editordocument_query[0], $editordocument_query[1]);
        }
        else {
            $editordocument = array($editordocument_query[1], $editordocument_query[0]);
        }
    } else
        $editordocument = array($editordocument_query[0]);

	//make an editor object with the document that we want to edit
	$editor = EditorsFactory::getInstance($editordocument[0]);
	$vars_editor = $editor->editDocument();

    if(strstr($editLdS->editor_type, 'google')) {
        $editor_support = EditorsFactory::getInstance($editordocument[1]);
        $vars['support_editor'] = $editor_support->editDocument();
    }

    if(!$vars_editor) {
        register_error("Edit document error");
        forward($CONFIG->url . 'pg/lds/');
    }

	$vars = $vars + $vars_editor;
	
	echo elgg_view('lds/editform_editor',$vars);
}

/* editors supporting implementation */
function lds_exec_implementeditor($params)
{
    global $CONFIG;

    //Get the page that we come from (if we come from an editing form, we go back to my lds)
    $vars['referer'] = $CONFIG->url.'pg/lds/implementations';

    $editLdS = get_entity($params[1]);
    if($lds = get_entity($editLdS->lds_id))
        if($lds->editor_type == 'openglm') {
            lds_exec_editglueps($params);
            return true;
        }

    if (!$editLdS->canEdit())
    {
        register_error("You don't have permissions to edit this LdS.");
        header("Location: " . $_SERVER['HTTP_REFERER']);
    }

    if($editLdS->getSubtype() == 'LdS_implementation')
        if ($user = lds_contTools::isLockedBy($params[1]))
        {
            $fstword = explode(' ',$user->name);
            $fstword = $fstword[0];
            register_error("{$user->name} is editing this LdS. You cannot edit it until {$fstword} finishes.");
            header("Location: " . $_SERVER['HTTP_REFERER']);
        }

    //Pass the LdS properties to the form
    if($editLdS->getSubtype() == 'LdS_implementation') {

        $vars['lds_id'] = $editLdS->lds_id;
        $name = $editLdS->title;
        $vars['course_id'] = ($editLdS->course_id ? $editLdS->course_id : 0);
        $vars['vle_id'] = $editLdS->vle_id;

        $vars['initLdS'] = new stdClass();

        $vars['initLdS']->title = $editLdS->title;
        $vars['initLdS']->granularity = $editLdS->granularity;
        $vars['initLdS']->completeness = $editLdS->completeness;



        $tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
        foreach ($tagtypes as $type)
        {
            if (is_array($editLdS->$type))
                $vars['initLdS']->$type = implode(',',$editLdS->$type);
            elseif (is_string($editLdS->$type) && strlen($editLdS->$type))
                $vars['initLdS']->$type = $editLdS->$type;
            else
                $vars['initLdS']->$type = '';
        }


        $vars['initLdS']->guid = $params[1];

    } else {

        $implementation_helper = $editLdS;
        $editLdS = get_entity($editLdS->lds_id);
        $vars['implementation_helper_id'] = $implementation_helper->guid;
        $name = $implementation_helper->title;
        $vars['lds_id'] = $editLdS->guid;
        $vars['course_id'] = ($implementation_helper->course_id ? $implementation_helper->course_id : 0);
        $vars['vle_id'] = $implementation_helper->vle_id;


        $vars['initLdS'] = new stdClass();
        $vars['initLdS']->title = $implementation_helper->title;
        //$vars['initLdS']->title = 'Untitled implementation';
        $vars['initLdS']->granularity = '0';
        $vars['initLdS']->completeness = '0';
        $vars['initLdS']->tags = '';
        $vars['initLdS']->discipline = '';
        $vars['initLdS']->pedagogical_approach = '';
        $vars['initLdS']->guid = '0';
    }

    //For each of the documents that this LdS has...
    $documents = get_entities_from_metadata('lds_guid',$params[1],'object','LdS_document', 0, 100);
    $vars['initDocuments'] = array();

    if(is_array($documents)) {
        //Send their data to the form
        foreach ($documents as $doc)
        {
            $obj = new stdClass();
            $obj->title = $doc->title;
            $obj->guid = $doc->guid;
            $obj->body = $doc->description;
            $obj->modified = '0';
            $vars['initDocuments'][] = $obj;
        }
    } else {
        $vars['initDocuments'][0] = new stdClass();
        $vars['initDocuments'][0]->title = T("Support Document");
        $vars['initDocuments'][0]->guid = '0';
        $vars['initDocuments'][0]->modified = '0';
        $vars['initDocuments'][0]->body = '<p> '.T("Write here any support notes for this implementation...").'</p>';
    }

    Utils::osort($vars['initDocuments'], 'guid');
    $vars['initDocuments'] = json_encode($vars['initDocuments']);

    $vars['am_i_starter'] = (get_loggedin_userid() == $editLdS->owner_guid);
    //$vars['all_can_read'] = ($editLdS->access_id == '1') ? 'true':'false';
    $vars['all_can_read'] = ($editLdS->all_can_view == 'yes' || ($editLdS->all_can_view === null && $editLdS->access_id < 3 && $editLdS->access_id > 0)) ? 'true' : 'false';
    $vars['initLdS'] = json_encode($vars['initLdS']);
    $vars['tags'] = json_encode(lds_contTools::getMyTags ());

    if($editLdS->getSubtype() == 'LdS_implementation') {
        $arrays = lds_contTools::buildObjectsArray($editLdS);
        $vars['starter'] = get_user($editLdS->owner_guid);
        $vars['jsonfriends'] = json_encode($arrays['available']);
        $vars['viewers'] = json_encode($arrays['viewers']);
        $vars['editors'] = json_encode($arrays['editors']);
        $vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));
    } else {
        $available = lds_contTools::getAvailableUsers(null);
        $vars['starter'] = get_user(get_loggedin_userid());
        $vars['jsonfriends'] = json_encode(lds_contTools::entitiesToObjects($available));
        $vars['viewers'] = json_encode(array());
        $vars['editors'] = json_encode(array());
        $vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));
        $vars['am_i_starter'] = true;
    }

    $vars['title'] = T("Edit implementation");

    //We're editing. Fetch it from the DB
    $editordocument = get_entities_from_metadata_multi(array(
            'lds_guid' => $editLdS->guid,
            'editorType' => 'webcollagerest'
        ),
        'object','LdS_document_editor', 0, 100);


    $vars['implementation'] = true;

    //make an editor object with the document that we want to edit
    $editor = EditorsFactory::getInstance($editordocument[0]);

    $user = get_loggedin_user();

    //TODO:deprecated
    if($vars['vle_id'] <= 30 ) {
        $vars['vle_id'] = $user->vle;
    }
    //

    if($vle = get_entity($vars['vle_id'])) {
        $vars_editor = $editor->putImplementation(array('course_id' => $vars['course_id'], 'vle' => $vle, 'lds' => $editLdS, 'name' => $name));
    } else {
        register_error("The associated VLE no longer exists.");
        forward($CONFIG->url.'pg/lds/implementations');
    }

    $vars = $vars + $vars_editor;

    //echo elgg_view('lds/editform_editor',$vars);
    echo elgg_view('lds/implementform_editor',$vars);
}

function lds_exec_editglueps($params)
{
    global $CONFIG;

    //Get the page that we come from (if we come from an editing form, we go back to my lds)
    //if (preg_match('/(neweditor|implementeditor)/',$_SERVER['HTTP_REFERER']))
        $vars['referer'] = $CONFIG->url.'pg/lds/implementations';
    //else
    //    $vars['referer'] = $_SERVER['HTTP_REFERER'];

    $editLdS = get_entity($params[1]);

    $vars['implementation_helper_id'] = '0';

    if($editLdS->getSubType() == 'LdS_implementation_helper'){
        if($helper_implemented = get_entities_from_metadata('helper_id', $editLdS->guid,'object','LdS_implementation',0,1))
            $editLdS = $helper_implemented[0];
            $vars['implementation_helper_id'] = $editLdS->guid;
    }

    if (!$editLdS->canEdit())
    {
        register_error("You don't have permissions to edit this LdS.");
        header("Location: " . $_SERVER['HTTP_REFERER']);
    }

    if($editLdS->getSubtype() == 'LdS_implementation')
        if ($user = lds_contTools::isLockedBy($params[1]))
        {
            $fstword = explode(' ',$user->name);
            $fstword = $fstword[0];
            register_error("{$user->name} is editing this implementation. You cannot edit it until {$fstword} finishes.");
            header("Location: " . $_SERVER['HTTP_REFERER']);
        }

    //lds_contTools::markLdSAsViewed ($params[1]);

    //Pass the LdS properties to the form
    $vars['initLdS'] = new stdClass();
    $vars['initLdS']->title = $editLdS->title;
    $vars['initLdS']->granularity = $editLdS->granularity;
    $vars['initLdS']->completeness = $editLdS->completeness;

    $tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
    foreach ($tagtypes as $type)
    {
        if (is_array($editLdS->$type))
            $vars['initLdS']->$type = implode(',',$editLdS->$type);
        elseif (is_string($editLdS->$type) && strlen($editLdS->$type))
            $vars['initLdS']->$type = $editLdS->$type;
        else
            $vars['initLdS']->$type = '';
    }

    if($editLdS->getSubtype() != 'LdS_implementation_helper')
        $vars['initLdS']->guid = $params[1];
    else
        $vars['initLdS']->guid = 0;

    //For each of the documents that this LdS has...
    $documents = get_entities_from_metadata('lds_guid',$params[1],'object','LdS_document', 0, 100);
    $vars['initDocuments'] = array();

    if(is_array($documents)) {
        //Send their data to the form
        foreach ($documents as $doc)
        {
            $obj = new stdClass();
            $obj->title = $doc->title;
            $obj->guid = $doc->guid;
            $obj->body = $doc->description;
            $obj->modified = '0';
            $vars['initDocuments'][] = $obj;
        }
    } else {
        $vars['initDocuments'][0] = new stdClass();
        $vars['initDocuments'][0]->title = T("Support Document");
        $vars['initDocuments'][0]->guid = '0';
        $vars['initDocuments'][0]->modified = '0';
        $vars['initDocuments'][0]->body = '<p> '.T("Write here any support notes for this LdS...").'</p>';
    }

    Utils::osort($vars['initDocuments'], 'guid');
    $vars['initDocuments'] = json_encode($vars['initDocuments']);

    $vars['am_i_starter'] = (get_loggedin_userid() == $editLdS->owner_guid);
    $vars['starter'] = get_user($editLdS->owner_guid);
    //$vars['all_can_read'] = ($editLdS->access_id == '1') ? 'true':'false';
    $vars['all_can_read'] = ($editLdS->all_can_view == 'yes' || ($editLdS->all_can_view === null && $editLdS->access_id < 3 && $editLdS->access_id > 0)) ? 'true' : 'false';
    $vars['initLdS'] = json_encode($vars['initLdS']);
    $vars['tags'] = json_encode(lds_contTools::getMyTags ());

    $arrays = lds_contTools::buildObjectsArray($editLdS);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));

    $vars['title'] = T("Edit implementation");

    //We're editing. Fetch it from the DB
    $editordocument = get_entities_from_metadata_multi(array(
            'lds_guid' => $editLdS->guid,
            'editorType' => 'gluepsrest'
        ),
        'object','LdS_document_editor', 0, 100);


    //make an editor object with the document that we want to edit
    //$editor = EditorsFactory::getInstance($editordocument[0]);

    $vars['lds_id'] = $editLdS->lds_id;

    $vars['course_id'] = ($editLdS->course_id ? $editLdS->course_id : 0);


    $user = get_loggedin_user();
    $vars['vle_id'] = $editLdS->vle_id;

    //TODO:DEPRECATED
    if($vars['vle_id'] <= 30 ) {
        $vars['vle_id'] = $user->vle;
    }
    //

    if(!(get_entity($vars['vle_id']))) {
        register_error(T("The associated VLE no longer exists."));
        forward($CONFIG->url.'pg/lds/implementations');
    }

    if($vle = get_entity($vars['vle_id'])) {
        if(!$editordocument) {
            $gluepsm = new GluepsManager($vle);
            $lds = get_entity($editLdS->lds_id);

            if($lds->editor_type == 'webcollagerest')
                $editordocument = get_entities_from_metadata_multi(array(
                        'lds_guid' => $editLdS->guid,
//                        'editorType' => $lds->editor_type//'webcollagerest'
                    ),
                    'object','LdS_document_editor', 0, 100);
            else
                $editordocument = get_entities_from_metadata_multi(array(
                        'lds_guid' => $editLdS->lds_id,
//                        'editorType' => $lds->editor_type//'webcollagerest'
                    ),
                    'object','LdS_document_editor', 0, 100);

            $vars_glueps = $gluepsm->newImplementation(array('course'=>$editLdS->course_id, 'title' => $editLdS->title, 'implementation' => $editLdS, 'document' => $editordocument[0]));
        } else {
            $gluepsm = new GluepsManager($vle, $editLdS, $editordocument[0]);
            $vars_glueps = $gluepsm->editDocument(array('course'=>$editLdS->course_id, 'title' => $editLdS->title));
        }
        $vars = $vars + $vars_glueps;
    } else {
        register_error(T("VLE error"));
        forward($CONFIG->url.'pg/lds/implementations');
    }

    echo elgg_view('lds/implementform_editor',$vars);
}

//TODO: deprecated
function lds_exec_newimplementglueps($params)
{
    global $CONFIG;

    //Get the page that we come from (if we come from an editing form, we go back to my lds)
    if (preg_match('/(neweditor|newimplementglueps)/',$_SERVER['HTTP_REFERER']))
        $vars['referer'] = $CONFIG->url.'pg/lds/';
    else
        $vars['referer'] = $_SERVER['HTTP_REFERER'];

    $implementation_helper = get_entity($params[1]);
    $lds = get_entity($implementation_helper->lds_id);

    $vars['referer'] = $CONFIG->url.'pg/lds/';

    //Create an empty LdS object to initialize the form
    $vars['initLdS'] = new stdClass();
    $vars['initLdS']->title = $implementation_helper->title;
    $vars['initLdS']->granularity = '0';
    $vars['initLdS']->completeness = '0';
    $vars['initLdS']->tags = '';
    $vars['initLdS']->discipline = '';
    $vars['initLdS']->pedagogical_approach = '';
    $vars['initLdS']->guid = 0;

    //And a support doc!
    $vars['initDocuments'][0] = new stdClass();
    $vars['initDocuments'][0]->title = T("Support Document");
    $vars['initDocuments'][0]->guid = '0';
    $vars['initDocuments'][0]->modified = '0';
    $vars['initDocuments'][0]->body = '<p> '.T("Write here any support notes for this implementation...").'</p>';

    $vars['initDocuments'] = json_encode($vars['initDocuments']);

    $vars['all_can_read'] = 'true';
    $vars['initLdS'] = json_encode($vars['initLdS']);
    $vars['tags'] = json_encode(lds_contTools::getMyTags ());

    $available = lds_contTools::getAvailableUsers(null);

    $vars['jsonfriends'] = json_encode(lds_contTools::entitiesToObjects($available));
    $vars['viewers'] = json_encode(array());
    $vars['editors'] = json_encode(array());
    $vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));

    $vars['starter'] = get_loggedin_user();

    $vars['am_i_starter'] = true;

    $vars['editor_type'] = $implementation_helper->editor_type;

    $vars['title'] = T("New implementation");

    $vars['lds_id'] = $lds->guid;

    $vars['course_id'] = $implementation_helper->course_id;
    $vars['implementation_helper_id'] = $implementation_helper->guid;

    $user = get_loggedin_user();
    if($user->vle) {
        $vle = get_entity($user->vle);
        $vars['vle_id'] = $vle->guid;
        $gluepsm = new GluepsManager($vle);
        //$vars['vle_info'] = $gluepsm->getVleInfo();
        $vars_glueps = $gluepsm->newImplementation(array('course'=>3, 'title' => $implementation_helper->title, 'lds' => $lds));
        $vars = $vars + $vars_glueps;
    }

    //$vars_glueps = GluepsManager::newImplementation(array('course'=>3, 'title' => $params[3]));
    //$vars = $vars + $vars_glueps;

    echo elgg_view('lds/implementform_editor',$vars);
}

function lds_exec_history ($params)
{
	$vars['lds'] = get_entity($params[1]);

	//We need to see all the entities, including the deleted ones.
	$access_status = access_get_show_hidden_status();
	access_show_hidden_entities(true);
	
	$ldsDocs = lds_contTools::getLdsDocuments($params[1]);
	
	$vars['ldsDocs'] = $ldsDocs;
	$vars['numDocs'] = count ($ldsDocs);
	
	//We get all the document ids of this lds
	foreach ($ldsDocs as $doc)
		$docList[] = (object)array
		(
			'guid' => $doc->guid,
			'lds_revision_id' => $doc->lds_revision_id,
			'enabled' => $doc->enabled,
		);
	$vars['docList'] = $docList;
	$vars['JSONdocList'] = json_encode($docList);

	//TODO permission / exist checks
	$vars['history'] = array_reverse(lds_contTools::getRevisionList($vars['lds']));
	$vars['numRevisions'] = count($vars['history']);
	$vars['JSONhistory'] = lds_contTools::getJSONRevisionList($vars['history']);
	
	$vars['title'] = T("LdS History");
	
	echo elgg_view('lds/history',$vars);
	
	access_show_hidden_entities($access_status);
}

function lds_exec_historyeditor ($params)
{
	$vars['lds'] = get_entity($params[1]);
	$editordocument = get_entities_from_metadata('lds_guid',$vars['lds']->guid,'object','LdS_document_editor', 0, 100);
	$document = $editordocument[0];
	$vars['editor'] = $document->editorType;
	$vars['document'] = $document;
	
//	//TODO permission / exist checks
	$vars['history'] = lds_contTools::getRevisionEditorList($vars['lds']);
	$vars['numRevisions'] = count($vars['history']);
	$vars['JSONhistory'] = lds_contTools::getJSONRevisionList($vars['history']);

	$vars['title'] = T("LdS History");
	
	echo elgg_view('lds/history_editor',$vars);
	//page_draw('LdS History', $body);
	
	access_show_hidden_entities($access_status);
}

function lds_exec_view_iframe ($params)
{
    global $CONFIG;

    $doc = get_entity($params[1]);
    $vars['doc'] = $doc;

    if (is_numeric($doc->document_guid))
        $vars['title'] = get_entity($doc->document_guid)->title;
    else
        $vars['title'] = $doc->title;


    if($params[2]){
        $vars['ref_doc'] = get_entity($params[2]);
        $aname = $CONFIG->tmppath.'guid'.$vars['ref_doc']->guid.'_'.get_loggedin_userid().'.html';
        $bname = $CONFIG->tmppath.'guid'.$vars['doc']->guid.'_'.get_loggedin_userid().'.html';

        file_put_contents($aname, $vars['ref_doc']->description);
        file_put_contents($bname, $vars['doc']->description);

        $output = array();

        exec ("{$CONFIG->pythonpath} {$CONFIG->path}mod/lds/ext/diff.py $aname $bname", $output);

        $vars['diff'] = implode('', $output);

        unlink($aname);
        unlink($bname);
    }

    if($doc->editorType == "google_docs")
        echo $doc->description;
    else
        echo elgg_view('lds/view_iframe',$vars);
}

function lds_exec_viewext ($params)
{
	global $CONFIG;

	//"Deobfuscate" the doc ID: from base 36 to base 10 - aaa(36)
	$id = base_convert(strtolower($params[1]), 36, 10) - $CONFIG->salt;
	
	$publishedId = lds_contTools::getPublishedId($id);

	//We didn't find any published thing, so 404.
	if ($publishedId == -1)
	{
		lds_exec_404('public');
		return;
	}

	$doc = get_entity($publishedId);
	$vars['doc'] = $doc;
    $vars['lds'] = get_entity($doc->lds_guid);
	if (is_numeric($doc->lds_guid))
		$vars['title'] = $vars['lds']->title;
	else
		$vars['title'] = $doc->title;
	
	echo elgg_view('lds/view_external',$vars); 
}

function lds_exec_viewexteditor ($params)
{
	global $CONFIG;

	//"Deobfuscate" the doc ID: from base 36 to base 10 - aaa(36)
	$id = base_convert(strtolower($params[1]), 36, 10) - $CONFIG->salt;
	
	$publishedId = lds_contTools::getPublishedEditorId($id);

	//We didn't find any published thing, so 404.
	if ($publishedId == -1)
	{
		lds_exec_404('public');
		return;
	}
	
	$vars['doc'] = get_entity($publishedId);
	$vars['lds'] = get_entity($vars['doc']->lds_guid);
	
	if(count($params) > 2)
	{
		//return the published file according to the second parameter
		if(strlen($params[2]) > 0)
		{
            $down_filename = $vars['lds']->title.'_'.$params[2].'.zip';

            switch ($params[2]) {
			    case 'imsld':
			        $file_guid = $vars['doc']->file_imsld_guid;
			        break;
			    case 'webZip':
			        $file_guid = $vars['doc']->pub_webZip;
			        break;
			    case 'scorm':
			        $file_guid = $vars['doc']->pub_scorm;
			        break;
			    case 'scorm2004':
			        $file_guid = $vars['doc']->pub_scorm2004;
			        break;
                case 'binary':
                    $file_guid = $vars['doc']->file_guid;
                    $down_filename = $vars['doc']->upload_filename;
                    break;
			    default:
			    	return '';
			    	break;
			}

			$file = get_entity($file_guid);
			$readfile = new ElggFile();
			$readfile->owner_guid = $file->owner_guid;
			$readfile->setFilename($file->originalfilename);
			$filename = $readfile->getFilenameOnFilestore();
			header('Content-Description: File Transfer');
		    header('Content-Type: application/octet-stream');
		    header('Content-Disposition: attachment; filename="'.$down_filename.'"');
		    header('Content-Transfer-Encoding: binary');
		    header('Expires: 0');
		    header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
		    header('Pragma: public');
		    header('Content-Length: ' . filesize($filename));
		    ob_clean();
		    flush();
		    readfile($filename);
			return;
		}
	}

    echo elgg_view('lds/view_external_editor',$vars);
}


//TODO rewrite urls.
function lds_exec_view ($params)
{
	$id = $params[1];
	$vars['lds'] = get_entity($id);
    if($vars['lds']->external_editor) {
        set_context("lds_exec_vieweditor");
        lds_exec_vieweditor($params);
        return true;
    }

    create_annotation($vars['lds']->guid, 'viewed_lds', '1', 'text', get_loggedin_userid(), 2);

    //TODO permission / exist checks

	$vars['ldsDocs'] = lds_contTools::getLdsDocuments($id);
	$vars['currentDocId'] = $params[3] ?: $vars['ldsDocs'][0]->guid;
	$vars['currentDoc'] = get_entity($vars['currentDocId']);
	
	$vars['am_i_starter'] = (get_loggedin_userid() == $vars['lds']->owner_guid);
    $vars['starter'] = get_user($vars['lds']->owner_guid);

	//Security: we won't show a document unless it's part of this LdS
	if ($vars['currentDoc']->lds_guid != $id)
	{
		lds_exec_404();
		return;	
	}
		
	//Check if it's published
	$vars['publishedId'] = lds_contTools::getPublishedId($vars['currentDocId']);
	
	$vars['nComments'] = $vars['lds']->countAnnotations('generic_comment');
	
    //These are all my friends
    $arrays = lds_contTools::buildObjectsArray($vars['lds']);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(array());
    $vars['starter'] = get_user($vars['lds']->owner_guid);

    $vars['all_can_read'] = ($vars['lds']->all_can_view == 'yes' || ($vars['lds']->all_can_view === null && $vars['lds']->access_id < 3 && $vars['lds']->access_id > 0)) ? 'true' : 'false';

	$body = elgg_view('lds/view_internal',$vars);
	page_draw($vars['lds']->title. ': ' .$vars['currentDoc']->title, $body);
}

//TODO rewrite urls.
function lds_exec_vieweditor ($params)
{
    global $CONFIG;
	$id = $params[1];
	$lds = get_entity($id);
    if(!$lds->external_editor) {
        $url = $CONFIG->url;
        forward("{$url}pg/lds/view/{$id}");
    }

	$vars['lds'] = $lds;
    create_annotation($vars['lds']->guid, 'viewed_lds', '1', 'text', get_loggedin_userid(), 2);
	//TODO permission / exist checks

    $vars['ldsDocs'] = lds_contTools::getLdsDocuments($id);

    //the support document is LdS_document_editor
    if(strstr($lds->editor_type, 'google') and count($vars['ldsDocs']) >= 2 and !empty($vars['ldsDocs'][0]->support)){
        $vars['ldsDocs'] = array($vars['ldsDocs'][1], $vars['ldsDocs'][0]);
    }

    $vars['currentDocId']   = $vars['ldsDocs'][0]->guid;
    $vars['currentDoc']     = $vars['ldsDocs'][0];

    if(isset($params[3])) {
        foreach($vars['ldsDocs'] as $fdoc) {
            if($fdoc->guid == $params[3]) {
                $vars['currentDocId'] = $fdoc->guid;
                $vars['currentDoc'] = $fdoc;
            }
        }
    }

    $vars['editor'] = $vars['currentDoc']->editorType;

    $vars['iseXe'] = $vars['currentDoc']->getSubtype() == 'LdS_document_editor' ? true : false;

    if($vars['iseXe']) {
        //Check if it's published
        $vars['publishedId'] = lds_contTools::getPublishedEditorId($vars['currentDocId']);
    } else {
        //Check if it's published
        $vars['publishedId'] = lds_contTools::getPublishedId($vars['currentDocId']);
    }

    switch($vars['editor']) {
        case 'cld':
        case 'image':
        case 'openglm':
            $vars['upload'] = true;
            $vars['uploadDoc'] = $vars['ldsDocs'][0];
            break;
        default:
            $vars['upload'] = false;
    }

    $vars['glueps'] = get_entities_from_metadata_multi(array(
            'lds_guid' => $lds->guid,
            'editorType' => 'gluepsrest'
        ),
        'object','LdS_document_editor', 0, 1);

	$vars['nComments'] = $vars['lds']->countAnnotations('generic_comment');

    //These are all my friends
    $arrays = lds_contTools::buildObjectsArray($vars['lds']);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(array());
	
	$vars['starter'] = get_user($vars['lds']->owner_guid);
	
	$vars['am_i_starter'] = (get_loggedin_userid() == $vars['lds']->owner_guid);
	
    $vars['all_can_read'] = ($vars['lds']->all_can_view == 'yes' || ($vars['lds']->all_can_view === null && $vars['lds']->access_id < 3 && $vars['lds']->access_id > 0)) ? 'true' : 'false';

    //iframe view auth
    session_start();
    $_SESSION['editors_content'] = $CONFIG->editors_content;
    session_write_close();
	echo elgg_view('lds/view_internal_editor',$vars);
}

function lds_exec_info ($params)
{
	$vars['infoComments'] = true;
	$vars['lds'] = get_entity($params[1]);
	$vars['ldsDocs'] = lds_contTools::getLdsDocuments($params[1]);
	$vars['currentDoc'] = null;
	$vars['nComments'] = $vars['lds']->countAnnotations('generic_comment');
	
	$vars['am_i_starter'] = (get_loggedin_userid() == $vars['lds']->owner_guid);
	$vars['starter'] = get_user($vars['lds']->owner_guid);
	
	//These are all my friends
    /*
	$friends = lds_contTools::getFriendsArray(get_loggedin_userid());
	$arrays = lds_contTools::buildFriendArrays($friends, $vars['lds']->access_id, $vars['lds']->write_access_id);

	$vars['jsonfriends'] = json_encode(array_values($arrays['available']));
	$vars['viewers'] = json_encode($arrays['viewers']);
	$vars['editors'] = json_encode($arrays['editors']);
	$vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));
    */

    //These are all my friends
    $arrays = lds_contTools::buildObjectsArray($vars['lds']);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    //$vars['jsonfriends'] = json_encode(array_values($arrays['available']));
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(lds_contTools::buildMinimalUserGroups(get_loggedin_userid()));

    $vars['starter'] = get_user($vars['lds']->owner_guid);
	
	$vars['am_i_starter'] = (get_loggedin_userid() == $vars['lds']->owner_guid);
	
	//$vars['all_can_read'] = ($vars['lds']->access_id == '1') ? 'true':'false';
    $vars['all_can_read'] = ($vars['lds']->all_can_view == 'yes' || ($vars['lds']->all_can_view === null && $vars['lds']->access_id < 3 && $vars['lds']->access_id > 0)) ? 'true' : 'false';


    if(empty($vars['lds']->external_editor)) {
        $body = elgg_view('lds/view_internal',$vars);
        page_draw($vars['lds']->title, $body);
    }
    else
        echo elgg_view('lds/view_internal_editor',$vars);

}

function lds_exec_imgdisplay ($params)
{
	$file_guid = (int)$params['1'];
	$file = get_entity($file_guid);
	
	$readfile = new ElggFile();
	$readfile->owner_guid = $file->owner_guid;
	$readfile->setFilename($file->originalfilename);
	$filename = $readfile->getFilenameOnFilestore();
	$handle = fopen($filename, "r");
	$contents = fread($handle, filesize($filename));
	fclose($handle);
	$mime = $file->mimetype;
	$expires = 14 * 60*60*24;

	header("Content-Type: $mime");
	header("Content-Length: " . strlen($contents));
	header("Cache-Control: public", true);
	header("Pragma: public", true);
	header('Expires: ' . gmdate('D, d M Y H:i:s', time() + $expires) . ' GMT', true);
	
	echo $contents;
}

function lds_exec_viewrevision ($params)
{
	global $CONFIG;
	
	//TODO access policies
	$revision = get_entity ($params[1]);

    if($revision->editorType == 'google_docs' || $revision->editor_type == 'google_docs' ||
        $revision->editorType == 'exelearningrest' || $revision->editor_type == 'exelearningrest'
    )
        return lds_exec_viewrevisioneditor($params);

	if ($revision->subtype == get_subtype_id('object', 'LdS_document_revision')
        || $revision->subtype == get_subtype_id('object', 'LdS_document_editor_revision'))
	{
		$document = get_entity ($revision->document_guid);
        $lds_annotation = get_annotation($revision->lds_revision_id);
		$revDate = $lds_annotation->time_created;
	}
	else
	{
		$document = $revision;
		$revDate = $document->time_updated;
	}
	$lds = get_entity ($document->lds_guid);

    $external = '';
    if($lds->external_editor)
        $external = '_editor';

	//Get all the revisions of this document
	$revisions = get_entities_from_metadata('document_guid',$document->guid,'object','LdS_document'.$external.'_revision',0,10000,0,'time_created');
	
	//And get the link to the next one and the previons one
	if ($revision->subtype == get_subtype_id('object', 'LdS_document'.$external.'_revision'))
	{
		$prevId = $nextId = null;
		if (is_array ($revisions))
		{
			foreach ($revisions as $k=>$rev)
			{
				if ($rev->guid == $revision->guid)
				{
					$prevId = $revisions[$k - 1]->guid;;
					if ($k == count($revisions) - 1)
					{
						if (!is_null($document))
							$nextId = $document->guid; 
						else
							$nextId = null;
					}
					else $nextId = $revisions[$k + 1]->guid;	
				}
			}
		}
	}
	else
	{
		$nextId = null;
		$prevId = $revisions[count($revisions) - 1]->guid;
	}

	//Now we create a diff string if the previous version is available
	if (!is_null($prevId))
	{
		$aname = $CONFIG->tmppath.'rev'.$prevId.'.html';
		$bname = $CONFIG->tmppath.'rev'.$revision->guid.'.html';

		file_put_contents($aname, get_entity($prevId)->description);
		file_put_contents($bname, $revision->description);
		
		$output = array();
		exec ("{$CONFIG->pythonpath} {$CONFIG->path}mod/lds/ext/diff.py $aname $bname", $output);
		$diff = implode('', $output);
		
		unlink($aname);
		unlink($bname);
	}
	
	$vars['prevId'] = $prevId;
	$vars['nextId'] = $nextId;
	$vars['revision'] = $revision;
	$vars['revDate'] = $revDate;
	$vars['prevRevision'] = get_entity($prevId);
	$vars['prevReviser'] = get_user($vars['prevRevision']->owner_guid);
	$vars['reviser'] = get_user($revision->owner_guid);
	$vars['document'] = $document;
	$vars['lds'] = $lds;
	$vars['diff'] = $diff;
	
	$body = elgg_view('lds/view_revision',$vars);
	page_draw($document->title . ': '. T("Revision"). ' - ' . date('j M Y H:i', $revDate), $body);
}

function lds_exec_viewrevisioneditor ($params)
{
	global $CONFIG;
	
	//TODO access policies
	$revision = get_entity ($params[1]);

	if ($revision->subtype == get_subtype_id('object', 'LdS_document_editor_revision'))
	{
		$document = get_entity ($revision->document_guid);
        $lds_annotation = get_annotation($revision->lds_revision_id);
		$revDate = $lds_annotation->time_created;
	}
	else
	{
		$document = $revision;
		$revDate = $document->time_updated;
	}
	$lds = get_entity ($document->lds_guid);

	//Get all the revisions of this document
	$revisions = get_entities_from_metadata('document_guid',$document->guid,'object','LdS_document_editor_revision',0,10000,0,'time_created');
	
	//And get the link to the next one and the previons one
	if ($revision->subtype == get_subtype_id('object', 'LdS_document_editor_revision'))
	{
		$prevId = $nextId = null;
		if (is_array ($revisions))
		{
			foreach ($revisions as $k=>$rev)
			{
				if ($rev->guid == $revision->guid)
				{
					$prevId = $revisions[$k - 1]->guid;
					if ($k == count($revisions) - 1)
					{
						if (!is_null($document))
							$nextId = $document->guid; 
						else
							$nextId = null;
					}
					else $nextId = $revisions[$k + 1]->guid;	
				}
			}
		}
	}
	else
	{
		$nextId = null;
		$prevId = $revisions[count($revisions) - 1]->guid;
	}
	
	$vars['prevId'] = $prevId;
	$vars['nextId'] = $nextId;
	$vars['revision'] = $revision;
	$vars['revDate'] = $revDate;
	$vars['prevRevision'] = get_entity($prevId);
	$vars['prevReviser'] = get_user($vars['prevRevision']->owner_guid);
	$vars['reviser'] = get_user($revision->owner_guid);
	$vars['document'] = $document;
	$vars['lds'] = $lds;

    if(lds_viewTools::detailedHistorySupport($lds)) {
        //Now we create a diff string if the previous version is available
        if (!is_null($prevId))
        {
            $aname = $CONFIG->tmppath.'rev'.$prevId.'.html';
            $bname = $CONFIG->tmppath.'rev'.$revision->guid.'.html';

            file_put_contents($aname, get_entity($prevId)->description);
            file_put_contents($bname, $revision->description);

            $output = array();
            exec ("{$CONFIG->pythonpath} {$CONFIG->path}mod/lds/ext/diff.py $aname $bname", $output);
            $diff = implode('', $output);

            unlink($aname);
            unlink($bname);
        }
        $vars['diff'] = $diff;
        $vars['google_docs'] = true;
        $body = elgg_view('lds/view_revision',$vars);
    } else {
        $body = elgg_view('lds/view_revision_editor',$vars);
    }

	page_draw($lds->title. ': '. T("Revision"). ' - ' . date('j M Y H:i', $revDate), $body);
}

function lds_exec_firststeps ()
{
	$body = elgg_view('lds/firststeps',$vars);
	page_draw(T("Welcome to LdShake!"), $body);
}

function lds_exec_make_expert ($params)
{
	$id = $params[1];

	if ($user = get_user($id))
	{
		$user->isExpert = 1;
		$user->save();
	}
	
	forward('pg/lds/');
}

function lds_exec_make_newbie ($params)
{
	$id = $params[1];

	if ($user = get_user($id))
	{
		$user->isExpert = 0;
		$user->save();
	}
	
	forward('pg/lds/');
}

function lds_treebuilder($lds_id, $target) {
    $tree = array();
    $lds = get_entity($lds_id);
    $user = get_user($lds->owner_guid);
    if($target_guid = array_pop($target)) {
        $tree['target'] = $target_guid;
    } else
        $tree['target'] = 0;
    $tree['title'] = $lds->title;
    $tree['name'] = $user->name;
    $tree['username'] = $user->username;
    $tree['enabled'] = $lds->isEnabled();
    $tree['lds_guid'] = $lds->guid;
    $tree['owner_icon'] = $user->getIcon('small');
    $tree['tags_html'] = lds_viewTools::all_tag_display ($lds);
    if($children = get_entities_from_metadata('parent', $lds->guid, 'object', 'LdS', 0 , 9999)) {
        $tree['children'] = array();

        foreach($children as $c) {
            if($c->isEnabled() || get_entities_from_metadata('parent', $c->guid, 'object', 'LdS', 0 , 9999))
                $tree['children'][] = lds_treebuilder($c->guid, $target);
        }
    }
    return $tree;
}

function lds_exec_tree ($params)
{

    //We need to see all the entities, including the deleted ones.
    $access_status = access_get_show_hidden_status();
    access_show_hidden_entities(true);

    $vars = array();

    $lds = get_entity($params[1]);
    $vars['lds'] = $lds;

    $i=0;
    $target = array();
    while($lds->parent && ($i++)<2) {
        $target[] = $lds->guid;
        $lds = get_entity($lds->parent);
    }

    $tree = lds_treebuilder($lds->guid, $target);

    $tree_json = json_encode($tree);

    $vars['tree'] = $tree_json;

    access_show_hidden_entities($access_status);

    $body = elgg_view('lds/tree',$vars);

    page_draw($lds->title, $body);
}


function lds_exec_datatracking ($params) {
    global $CONFIG;

    require_once __DIR__.'/stadistics.php';

    $vars['nUsers'] = get_entities('user','',0,'',9999, 0, true);
    $vars['nGroups'] = get_entities('group','',0,'',9999, 0, true);

    $vars['nPublished']    = get_entities_from_metadata('published','1','object','LdS_document',0,9999,0,'', 0, true)
                        + get_entities_from_metadata('published','1','object','LdS_document_editor',0,9999,0,'', 0, true);


    /*
    $vars['uVisits'] = $CONFIG->site->countAnnotations('unique_visit');
    $vars['pVisits'] = $CONFIG->site->countAnnotations('page_visit');
    */
    $body = elgg_view('lds/tracking', $vars);
    echo page_draw(T('Tracking'), $body);

}

function lds_exec_tracking ($params) {
    include_once(__DIR__.'/stadistics.php');

    switch($params[1]) {
        case 'implemented':
            lds_tracking_implemented();
            break;
        case 'implementations':
            lds_tracking_implementations();
                break;
        case 'deployments':
            lds_tracking_deployments();
            break;
        case 'iduser':
            lds_tracking_id_name("user");
            break;
        case 'idgroup':
            lds_tracking_id_name("group");
            break;
        case 'idlds':
            lds_tracking_id_name("lds");
            break;
        case 'tool':
            lds_tracking_tool();
            break;
        case 'groups':
            lds_tracking_groups();
            break;
        case 'userweeks':
            lds_tracking_user_reviews();
            break;
        case 'userdays':
            lds_tracking_user_reviews_days();
            break;
        case 'usermonths':
            lds_tracking_user_reviews_months();
            break;
        case 'ldsedits':
            lds_tracking_edit_list();
            break;
        case 'ldsprivate':
            lds_tracking_private();
            break;
        case 'ldssharing':
            lds_tracking_sharing();
            break;
        case 'ldsimplemented':
            lds_tracking_implemented();
            break;
        case 'ldscomments':
            lds_tracking_comments();
            break;
        case 'ldsviewed':
            lds_tracking_viewed();
            break;
        case 'created_by_user':
            lds_tracking_created_by_user();
            break;
        case 'user_tool':
            lds_tracking_user_tool();
            break;

    }

    //lds_csv_private();
}

//external repository search
function lds_exec_repository ($params)
{
    $body = elgg_view('lds/repository_search');
    page_draw("", $body);
}

function lds_exec_404 ($view = '')
{
	header("HTTP/1.0 404 Not Found");
	$body = elgg_view("lds/404{$view}",$vars);
	page_draw('Not found', $body);
}

///PFC maria
function lds_exec_patterns ($params)
{
    $body = elgg_view('lds/patterns');
    page_draw("", $body);
}

function lds_exec_query ($params) {
    include_once __DIR__.'/Java.inc';

    ldshake_stats_log_event('search_patterns');

    $query = urldecode(get_input('q'));
    $vars['query'] = $query;

    $vars['list'] = lds_contTools::searchPatterns($query);
    $vars['count'] = count ($vars['list']);

    $body = elgg_view('lds/query', $vars);
    page_draw($query, $body);

}

function lds_exec_about ($params) {
    $body = elgg_view('lds/about.en', array());
    page_draw(T("About"), $body);
}

function lds_exec_help ($params) {
    $body = elgg_view('lds/help.en', array());
    page_draw(T("Help"), $body);
}

function lds_exec_test2 ($params) {
    /*
    $vle = get_entity($params[1]);
    $mm = new MoodleManager($vle);
    $res = var_export($mm->getVleInfo(), true);
    //$res = print_r($mm->addScorm(2,'new scorm 4'.' '. date("D M Y  H:m:s")), true);
    page_draw("test", '<pre>'.$res.'</pre>');
    //echo "test";
    */
    include_once(__DIR__.'/stadistics.php');
    lds_tracking_user_authoring_tool_saved();
}

function lds_exec_admin ($params) {
    admin_gatekeeper();

    switch($params[1]) {
        case 'vle':
            lds_admin_vle($params);
            break;
    }
}

function lds_admin_vle ($params) {
    admin_gatekeeper();

    set_context("admin_vle");

    $vlelist = get_entities('object', 'system_vle', 0, '', 9999);
    if(!$vlelist)
        $vlelist = array();

    if(!isset($params[2]) || !is_numeric($params[2])) {
        $vle = new ElggObject();
        $vle->subtype = 'system_vle';
        $vle->access_id = ACCESS_PUBLIC;
        $vle->owner_guid = get_loggedin_userid();
        $vle->name = '';
        $vle->username = '';
        $vle->password = '';
        $vle->vle_url = '';
        $vle->vle_type = '';
        $vle->new = 1;
        $id = 0;
        $vle_info = true;
    } else {
        $vle = get_entity($params[2]);
        $id = $vle->guid;
        //$gluepsm = new GluepsManager($vle);
        //$vle_info = $gluepsm->getVleInfo(true);
    }

    $vars = array(
        'vle' => $vle,
        'vle_id' => $id,
        'vle_info' => $vle_info,
        'vlelist' => $vlelist,
        'vle_admin' => true,
    );

    $body = elgg_view('lds/vledata',$vars);
    page_draw('VLE', $body);
}

function lds_exec_debug($params) {
    global $CONFIG;

    if(!$CONFIG->editor_debug)
        forward();

    set_context("debug");

    if($editor_id = get_loggedin_user()->editor) {
        $editor = get_entity($editor_id);
    } else {
        $editor = new ElggObject();
    }

    $form_body = elgg_view('lds/debug/debug_settings', array('editor' => $editor));
    $body = elgg_view('input/form', array('action' => "{$CONFIG->url}action/lds/debug/manage_debug", 'body' => $form_body));
    page_draw('Debug', $body);
}

function lds_exec_projects ($params)
{
    $offset = get_input('offset') ?: '0';
    set_context("lds_exec_main");

    if ($params[1] == 'created-by-me')
    {
        $vars['count'] = get_entities('object', 'LdSProject', get_loggedin_userid(), '', 50, $offset, true);
        $entities = get_entities('object', 'LdSProject', get_loggedin_userid(), 'time_updated DESC', 50, $offset);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("LdS created by me");
    }
    elseif ($params[1] == 'shared-with-me')
    {
        $vars['count'] = lds_contTools::getUserSharedLdSWithMe(get_loggedin_userid(), true);
        $entities = lds_contTools::getUserSharedLdSWithMe(get_loggedin_userid(), false, 50, $offset);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("LdS shared with me");
    }
    elseif ($params[1] == 'created-with')
    {
        $vars['count'] = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), true, 0 , 0, "editor_type", $params[2]);
        $entities = lds_contTools::getUserEditableLdSs(get_loggedin_userid(), false, 50, $offset, "editor_type", $params[2]);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("Created with").$params[2];
        $vars['editor_filter'] = $params[2];
    }
    else
    {
        if(isset($params[1]) && $params[1] == 'implement') {
            $check = lds_contTools::getUserEntities('object', 'LdSProject', get_loggedin_userid(), false, 9999, 0, null, null, "time", false, null, false, null, true);
            $vars['implement'] = true;
            $vars['count'] = lds_contTools::getUserViewableProjects(get_loggedin_userid(), true);
            $entities = lds_contTools::getUserViewableProjects(get_loggedin_userid(), false, 50, $offset);
            $vars['list'] = lds_contTools::enrichLdS($entities);
        } else {
            $vars['count'] = lds_contTools::getUserEditableProjects(get_loggedin_userid(), true);
            $entities = lds_contTools::getUserEditableProjects(get_loggedin_userid(), false, 50, $offset);
            $vars['list'] = lds_contTools::enrichLdS($entities);
        }
        $vars['title'] = T("All my Workflows");
    }

    $vars['list_type'] = T('workflows');
    $vars['section'] = 'off';
    $body = elgg_view('lds/projects/myprojects',$vars);

    page_draw($vars['title'], $body);
}

function lds_exec_projects_implementations ($params)
{
    $offset = get_input('offset') ?: '0';
    set_context("lds_exec_main");

    if ($params[1] == 'created-by-me')
    {
        $vars['count'] = get_entities('object', 'LdSProject_implementation', get_loggedin_userid(), '', 50, $offset, true);
        $entities = get_entities('object', 'LdSProject_implementation', get_loggedin_userid(), 'time_updated DESC', 50, $offset);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("LdS projects created by me");
    }
    elseif ($params[1] == 'shared-with-me')
    {
        $vars['count'] = lds_contTools::getUserSharedObjectsWithMe('object', 'LdSProject_implementation', get_loggedin_userid(), true);
        $entities = lds_contTools::getUserSharedObjectsWithMe('object', 'LdSProject_implementation', get_loggedin_userid(), false, 50, $offset);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("LdS projects shared with me");
    }
    elseif ($params[1] == 'trashed')
    {
        //We need to see all the entities, including the deleted ones.
        $access_status = access_get_show_hidden_status();
        access_show_hidden_entities(true);

        $offset = get_input('offset') ?: '0';

        //TODO take the owner into account
        $vars['list'] = get_entities_where('enabled = "no"', 'object', 'LdSProject_implementation', get_loggedin_userid(), 'time_updated DESC', 50, $offset);
        $vars['count'] = get_entities_where('enabled = "no"', 'object', 'LdSProject_implementation', get_loggedin_userid(), '', 0, $offset, true);
        $vars['list'] = lds_contTools::enrichLdS($vars['list']);
        $vars['title'] = T("My trashed LdS projects");
        $vars['section'] = "imp-{$params[1]}";

        access_show_hidden_entities($access_status);
    }
    else
    {
        $vars['count'] = lds_contTools::getUserEditableProjectImplementations(get_loggedin_userid(), true);
        $entities = lds_contTools::getUserEditableProjectImplementations(get_loggedin_userid(), false, 50, $offset);
        $vars['list'] = lds_contTools::enrichLdS($entities);
        $vars['title'] = T("All my Projects");
    }


    $vars['implement_list'] = true;
    $vars['list_type'] = T('project');
    $vars['section'] = 'prj-'.$params[1];
    $body = elgg_view('lds/projects/myprojects',$vars);

    page_draw($vars['title'], $body);
}

function lds_exec_project_implementation ($params)
{
    global $CONFIG;
    $offset = get_input('offset') ?: '0';
    set_context("lds_exec_main");
    if(!is_numeric($params[1]))
        forward($CONFIG->url . 'pg/lds/');

    $project_implementation = get_entity($params[1]);
    //TODO:check privileges

    /*$vars['count'] = $lds_list = lds_contTools::getProjectLdSList($pd_guid, true);*/
    $vars['list'] = $lds_list = lds_contTools::getProjectLdSList($project_implementation->guid, false, false, true);
    $vars['title'] = $project_implementation->title;
    $vars['jsondata'] = $project_implementation->jsondata;
    $pg_data = json_decode($project_implementation->description);
    $vars['list'] = ldshake_project_add_title_order($vars['list'], $pg_data);

    $vars['list_type'] = T('LdS');
    $vars['section'] = 'on';
    $vars['is_implementation'] = true;
    $vars['implementation_guid'] = $project_implementation->guid;
    $vars['implementation'] = $project_implementation;
    $body = elgg_view('lds/projects/myprojects',$vars);

    page_draw($vars['title'], $body);
}

function lds_exec_project_preview($params)
{
    $offset = get_input('offset') ?: '0';
    set_context("lds_exec_main");
    $project = get_entity($params[1]);
    //TODO:check privileges

    $preview = get_entity($project->preview);

    page_draw($project->title, $preview->description);
}

function lds_exec_project_preview2($params)
{
    global $CONFIG;
    $id = $params[1];
    $lds = get_entity($id);

    $vars['lds'] = $lds;
    create_annotation($vars['lds']->guid, 'viewed_lds', '1', 'text', get_loggedin_userid(), 2);
    //TODO permission / exist checks

    if(!empty($lds->preview)) {
        $vars['ldsDocs'][] = get_entity($lds->preview);
    }
    $vars['ldsDocs'][] = lds_contTools::getLdsDocuments($id);

    $vars['currentDocId']   = $vars['ldsDocs'][0]->guid;
    $vars['currentDoc']     = $vars['ldsDocs'][0];

    if(!empty($params[3])) {
        foreach($vars['ldsDocs'] as $fdoc) {
            if($fdoc->guid == $params[3]) {
                $vars['currentDocId'] = $fdoc->guid;
                $vars['currentDoc'] = $fdoc;
            }
        }
    }

    $vars['editor'] = $lds->editor_type;

    $vars['iseXe'] = $vars['currentDoc']->getSubtype() == 'LdS_document_editor' ? true : false;

    if($vars['iseXe']) {
        //Check if it's published
        $vars['publishedId'] = lds_contTools::getPublishedEditorId($vars['currentDocId']);
    } else {
        //Check if it's published
        $vars['publishedId'] = lds_contTools::getPublishedId($vars['currentDocId']);
    }

    $vars['upload'] = false;

    $vars['glueps'] = get_entities_from_metadata_multi(array(
            'lds_guid' => $lds->guid,
            'editorType' => 'gluepsrest'
        ),
        'object','LdS_document_editor', 0, 1);

    $vars['nComments'] = $vars['lds']->countAnnotations('generic_comment');

    //These are all my friends
    $arrays = lds_contTools::buildObjectsArray($vars['lds']);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(array());

    $vars['starter'] = get_user($vars['lds']->owner_guid);

    $vars['am_i_starter'] = (get_loggedin_userid() == $vars['lds']->owner_guid);

    $vars['all_can_read'] = ($vars['lds']->all_can_view == 'yes' || ($vars['lds']->all_can_view === null && $vars['lds']->access_id < 3 && $vars['lds']->access_id > 0)) ? 'true' : 'false';

    //iframe view auth
    session_start();
    $_SESSION['editors_content'] = $CONFIG->editors_content;
    session_write_close();
    echo elgg_view('lds/view_internal_editor',$vars);
}

function lds_exec_new_project ($params)
{
    global $CONFIG, $jslibs;

    $jslibs['project'] = true;

    //Get the page that we come from (if we come from an editing form, we go back to my lds)
    $vars['referer'] = $CONFIG->url.'pg/lds/projects/';

    //Create an empty LdS object to initialize the form
    $vars['initLdS'] = new stdClass();
    $vars['initLdS']->title = T("Untitled Workflow");
    $vars['initLdS']->granularity = '0';
    $vars['initLdS']->completeness = '0';
    $vars['initLdS']->tags = '';
    $vars['initLdS']->discipline = '';
    $vars['initLdS']->pedagogical_approach = '';
    $vars['initLdS']->guid = '0';

    $vars['am_i_starter'] = true;

    $vars['all_can_read'] = 'false';

    $vars['initLdS'] = json_encode($vars['initLdS']);

    //support doc
    $vars['initDocuments'][0] = new stdClass();
    $vars['initDocuments'][0]->title = T("Support Document");
    $vars['initDocuments'][0]->guid = '0';
    $vars['initDocuments'][0]->modified = '0';
    $vars['initDocuments'][0]->body = '<p> '.T("Write here any support notes for this LdS...").'</p>';

    $vars['initDocuments'] = json_encode($vars['initDocuments']);

    $vars['editor'] = 'project_design';
    $vars['jsondata'] = json_encode(array(), true);

    $vars['tags'] = json_encode(lds_contTools::getMyTags());

    $available = lds_contTools::getAvailableUsers(null);

    $vars['jsonfriends'] = json_encode(lds_contTools::entitiesToObjects($available));
    $vars['viewers'] = json_encode(array());
    $vars['editors'] = json_encode(array());
    $vars['groups'] = json_encode(array());

    $vars['starter'] = get_loggedin_user();

    $vars['title'] = T("New Workflow");

    $vars['project']['ldproject'] = '[]';
    $vars['project']['ldsToBeListed'] = json_encode(lds_contTools::getUserEditableLdS(get_loggedin_userid(), false, 100, 0, null, null, "time", true));
    $vars['project']['vle_list'] = array();
    $vars['lds'] = new ElggObject();
    $vars['lds']->subtype = 'LdSProject';

    $vars['editor_label'] = 'Workflow editor';

    echo elgg_view('lds/editform_editor',$vars);
}

function lds_exec_edit_project ($params)
{
    global $CONFIG, $jslibs;

    $jslibs['project'] = true;

    set_context("lds_exec_new_project");

    $editLdS = get_entity($params[1]);
    //Get the page that we come from (if we come from an editing form, we go back to my lds)
    if($editLdS->getSubtype() != 'LdSProject_implementation')
        $vars['referer'] = $CONFIG->url.'pg/lds/projects/';
    else
        $vars['referer'] = $CONFIG->url.'pg/lds/project_implementation/'.$editLdS->guid;

    if (!$editLdS->canEdit())
    {
        register_error("You don't have permissions to edit this workflow.");
        header("Location: " . $_SERVER['HTTP_REFERER']);
        return '';
    }

    if ($user = lds_contTools::isLockedBy($params[1]))
    {
        $fstword = explode(' ',$user->name);
        $fstword = $fstword[0];
        register_error("{$user->name} is editing this LdS. You cannot edit it until {$fstword} finishes.");
        header("Location: " . $_SERVER['HTTP_REFERER']);
    }

    //Pass the LdS properties to the form
    $vars['initLdS'] = new stdClass();
    $vars['initLdS']->title = $editLdS->title;
    $vars['initLdS']->granularity = $editLdS->granularity;
    $vars['initLdS']->completeness = $editLdS->completeness;

    $tagtypes = array ('tags', 'discipline', 'pedagogical_approach');
    foreach ($tagtypes as $type)
    {
        if (is_array($editLdS->$type))
            $vars['initLdS']->$type = implode(',',$editLdS->$type);
        elseif (is_string($editLdS->$type) && strlen($editLdS->$type))
            $vars['initLdS']->$type = $editLdS->$type;
        else
            $vars['initLdS']->$type = '';
    }

    $vars['initLdS']->guid = $params[1];
    $vars['am_i_starter'] = (get_loggedin_userid() == $editLdS->owner_guid);

    $vars['all_can_read'] = ($editLdS->all_can_view == 'yes' || ($editLdS->all_can_view === null && $editLdS->access_id < 3 && $editLdS->access_id > 0)) ? 'true' : 'false';

    $vars['initLdS'] = json_encode($vars['initLdS']);

//    $vars['list'] = lds_contTools::getUserEditableLdS(get_loggedin_userid(), false, 500, 0, null, null, "time", true);

    $vars['tags'] = json_encode(lds_contTools::getMyTags ());

    //These are all my friends
    $arrays = lds_contTools::buildObjectsArray($editLdS);
    $vars['jsonfriends'] = json_encode($arrays['available']);
    $vars['viewers'] = json_encode($arrays['viewers']);
    $vars['editors'] = json_encode($arrays['editors']);
    $vars['groups'] = json_encode(array());
    $vars['starter'] = get_user($editLdS->owner_guid);

    $vars['title'] = T("Edit %1", ldshake_env_category($editLdS));
    $vars['editor'] = $editLdS->editor_type;
    $vars['lds'] = $editLdS;

    //For each of the documents that this LdS has...
    $documents = get_entities_from_metadata('lds_guid',$params[1],'object','LdS_document', 0, 100);

    //Send their data to the form
    foreach ($documents as $doc)
    {
        $obj = new stdClass();
        $obj->title = $doc->title;
        $obj->guid = $doc->guid;
        $obj->body = $doc->description;
        $obj->modified = '0';
        $vars['initDocuments'][] = $obj;
    }

    Utils::osort($vars['initDocuments'], 'guid');

    $vars['initDocuments'] = json_encode($vars['initDocuments']);

    if($editLdS->getSubtype() == 'LdSProject_implementation')
        $vars['project']['is_implementation'] = true;

    $vle_data = array();

    $vars['project']['ldproject'] = $editLdS->description;
    $vars['project']['ldsToBeListed'] = json_encode(lds_contTools::getUserEditableLdS(get_loggedin_userid(), false, 100, 0, null, null, "time", true));
    $vars['project']['vle_list'] = array();

    $vars['project']['vle_list'] = json_encode($vle_data);

    $vars['editor_label'] = ldshake_env_category($editLdS) . ' editor';

    echo elgg_view('lds/editform_editor',$vars);
}
