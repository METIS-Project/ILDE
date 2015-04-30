<?php

	/**
	 * Elgg configuration library
	 * Contains functions for managing system configuration
	 *
     * @file modified by Interactive Technologies Group (GTI)
     * @authors (alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P..
     * @Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
     * @based on:
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

	
	/**
	 * Unset a config option.
	 *
	 * @param string $name The name of the field.
	 * @param int $site_guid Optionally, the GUID of the site (current site is assumed by default).
	 * @return mixed
	 */
		function unset_config($name, $site_guid = 0) 
		{
			global $CONFIG;
			
			$name = sanitise_string($name);
			$site_guid = (int) $site_guid;
			if ($site_guid == 0)
				$site_guid = (int) $CONFIG->site_id;
				
			return delete_data("delete from {$CONFIG->dbprefix}config where name='$name' and site_guid=$site_guid");
		}

	/**
	 * Sets a configuration value
	 *
	 * @param string $name The name of the configuration value
	 * @param string $value Its value
	 * @param int $site_guid Optionally, the GUID of the site (current site is assumed by default)
	 * @return false|int 1 or false depending on success or failure 
	 */
		function set_config($name, $value, $site_guid = 0) {
			
			global $CONFIG;
			
			// Unset existing
			unset_config($name,$site_guid);
			
			$name = sanitise_string($name);
			$value = sanitise_string($value);
			$site_guid = (int) $site_guid;
			if ($site_guid == 0)
				$site_guid = (int) $CONFIG->site_id;
			$CONFIG->$name = $value;
			$value = sanitise_string(serialize($value));
			
			return insert_data("insert into {$CONFIG->dbprefix}config set name = '{$name}', value = '{$value}', site_guid = {$site_guid}");
			
		}

	/**
	 * Gets a configuration value
	 *
	 * @param string $name The name of the config value
	 * @param int $site_guid Optionally, the GUID of the site (current site is assumed by default)
	 * @return mixed|false Depending on success
	 */
		function get_config($name, $site_guid = 0) {
			
			global $CONFIG;
			if (isset($CONFIG->$name))
				return $CONFIG->$name;
			$name = sanitise_string($name);
			$site_guid = (int) $site_guid;
			if ($site_guid == 0)
				$site_guid = (int) $CONFIG->site_id;
			if ($result = get_data_row("SELECT value from {$CONFIG->dbprefix}config where name = '{$name}' and site_guid = {$site_guid}")) {
				$result = $result->value;
				$result = unserialize($result->value);
				$CONFIG->$name = $result;
				return $result;
			}
			return false;
			
		}
		
		/**
		 * Gets all the configuration details in the config database for a given site.
		 *
		 * @param int $site_guid Optionally, the GUID of the site (current site is assumed by default)
		 */
		function get_all_config($site_guid = 0)
		{
			global $CONFIG;
			
			$site_guid = (int) $site_guid;
			
			if ($site_guid == 0)
				$site_guid = (int) $CONFIG->site_id;

            if(isset($_SESSION['site_config']) && is_array($_SESSION['site_config'])) {
                foreach ($_SESSION['site_config'] as $r)
                {
                    $name = $r->name;
                    $value = $r->value;
                    $CONFIG->$name = unserialize($value);
                }
                return true;
            } else {
                if ($result = get_data("SELECT * from {$CONFIG->dbprefix}config where site_guid = {$site_guid}")) {
                    foreach ($result as $r)
                    {
                        $name = $r->name;
                        $value = $r->value;
                        $CONFIG->$name = unserialize($value);
                    }
                    $_SESSION['site_config'] = $result;
                    return true;
                }
            }
			return false;
		}

	/**
	 * If certain configuration elements don't exist, autodetect sensible defaults 
	 * 
	 * @uses $CONFIG The main configuration global
	 *
	 */
		function set_default_config() {
			
			global $CONFIG;
			if (empty($CONFIG->path))
				$CONFIG->path = str_replace("\\","/",dirname(dirname(dirname(__FILE__)))) . "/";
				
			if (empty($CONFIG->viewpath))
				$CONFIG->viewpath = $CONFIG->path . "views/";	

			if (empty($CONFIG->pluginspath))
				$CONFIG->pluginspath = $CONFIG->path . "mod/";
				
			if (empty($CONFIG->wwwroot)) {
				/*
				$CONFIG->wwwroot = "http://" . $_SERVER['SERVER_NAME'];
				
				$request = $_SERVER['REQUEST_URI'];
				
				if (strripos($request,"/") < (strlen($request) - 1)) {
					// addressing a file directly, not a dir
					$request = substr($request, 0, strripos($request,"/")+1);
				}
				
				$CONFIG->wwwroot .= $request;
				*/
				$pathpart = str_replace("//","/",str_replace($_SERVER['DOCUMENT_ROOT'],"",$CONFIG->path));
				if (substr($pathpart,0,1) != "/") $pathpart = "/" . $pathpart; 
				$CONFIG->wwwroot = "http" . (($_SERVER['SERVER_PORT'] == 443) ? "s" : "") . "://" . $_SERVER['HTTP_HOST'] . $pathpart;
			}

            if(empty($CONFIG->ldshake_mode))
                $CONFIG->ldshake_mode = 'default';

			if (empty($CONFIG->url))
				$CONFIG->url = $CONFIG->wwwroot;
			
			if (empty($CONFIG->sitename))
				$CONFIG->sitename = "LdShake";
				
			if (empty($CONFIG->language))
				$CONFIG->language = "en";
    	}

function site_configuration() {
    global $CONFIG;
    if (isset($CONFIG->site) && ($CONFIG->site instanceof ElggSite)) {
/*        if(isset($_SESSION['site_configured'])){
            $CONFIG->sitename = $_SESSION['sitename'];
            $CONFIG->sitedescription = $_SESSION['sitedescription'];
            $CONFIG->siteemail = $_SESSION['siteemail'];
        } else {
*/
            $CONFIG->sitename = $CONFIG->site->name;
            $CONFIG->sitedescription = $CONFIG->site->description;
            $CONFIG->siteemail = $CONFIG->site->email;

            $_SESSION['site_configured'] = true;
            $_SESSION['sitename'] = $CONFIG->sitename;
            $_SESSION['sitedescription'] = $CONFIG->sitedescription;
            $_SESSION['siteemail'] = $CONFIG->siteemail;
//        }
    }
}

function ldshake_mode_configuration() {
    global $CONFIG;

    if(isset($CONFIG->ldshake_submode))
        $mode = $CONFIG->ldshake_submode;
    elseif(isset($CONFIG->ldshake_mode))
        $mode = $CONFIG->ldshake_mode;

    if(isset($mode)) {
        if(file_exists($CONFIG->path . 'mod/lds/mode/' . $mode . '/register_funcs.php'))
            include($CONFIG->path . 'mod/lds/mode/' . $mode . '/register_funcs.php');
    }
}

	/**
	 * Function that provides some config initialisation on system init
	 *
	 */
		
		function configuration_init() {
			global $CONFIG;

			$CONFIG->url = $CONFIG->wwwroot;
			/// LdShake change ///
			// Load default settings from database
			get_all_config(1);
			
			return true;
		}
		
	/**
	 * Register config_init
	 */

		//register_elgg_event_handler('boot','system','configuration_init',10);
		
?>