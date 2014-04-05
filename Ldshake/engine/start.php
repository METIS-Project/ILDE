<?php

	/**
	 * Elgg engine bootstrapper
	 * Loads the various elements of the Elgg engine
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

	/// LdShake change ///
	mb_internal_encoding("UTF-8");
	/// LdShake change ///

	/**
	 * Load important prerequisites
	 */
		if (!@include_once(dirname(__FILE__) . "/lib/exceptions.php")) {		// Exceptions
			echo "Error in installation: could not load the Exceptions library.";
			exit;
		}

		if (!@include_once(dirname(__FILE__) . "/lib/elgglib.php")) {		// Main Elgg library
			echo "Elgg could not load its main library.";
			exit;
		}

		if (!@include_once(dirname(__FILE__) . "/lib/system_log.php")) {		// Logging library
			echo "Error in installation: could not load the System Log library.";
			exit;
		}
	
		if (!@include_once(dirname(__FILE__) . "/lib/export.php")) {		// Export library
			echo "Error in installation: could not load the Export library.";
			exit;
		}
		
		if (!@include_once(dirname(__FILE__) . "/lib/languages.php")) {		// Languages library
			echo "Error in installation: could not load the languages library.";
			exit;
		}
		
		if (!@include_once(dirname(__FILE__) . "/lib/input.php")) {		// Input library
			echo "Error in installation: could not load the input library.";
			exit;
		}
/*
		if (!@include_once(dirname(__FILE__) . "/lib/install.php")) {		// Installation library
			echo "Error in installation: could not load the installation library.";
			exit;
		}
*/
		if (!@include_once(dirname(__FILE__) . "/lib/cache.php")) {		// Installation library
			echo "Error in installation: could not load the cache library.";
			exit;
		}
		
		if (!@include_once(dirname(__FILE__) . "/lib/sessions.php")) {
			echo ("Error in installation: Elgg could not load the Sessions library");
			exit;
		}

		// Use fallback view until sanitised
		$oldview = get_input('view');
		set_input('view', 'failsafe');
		
	/**
	 * Set light mode default
	 */
		$lightmode = false;
		
	/**
	 * Establish handlers
	 */
		
	// Register the error handler
		set_error_handler('__elgg_php_error_handler');
		set_exception_handler('__elgg_php_exception_handler');
	
	/// LdShake change ///	
	//Properly display errors	
	//restore_error_handler ();
	//error_reporting(E_ALL);
	//error_reporting(E_ALL ^ E_NOTICE);
	//ini_set("display_errors", 1);
	/// LdShake change ///		
		
	/**
	 * If there are basic issues with the way the installation is formed, don't bother trying
	 * to load any more files
	 */
		
		if ($sanitised = sanitised()) {	// Begin portion for sanitised installs only
	
		 /**
		 * Load the system settings
		 */
			
			if (!@include_once(dirname(__FILE__) . "/settings.php"))  		// Global settings
				throw new InstallationException("Elgg could not load the settings file.");
				
		/**
		 * Load and initialise the database
		 */
	
			if (!@include_once(dirname(__FILE__) . "/lib/database.php"))	// Database connection
				throw new InstallationException("Elgg could not load the main Elgg database library.");
				
		/**
		 * Load the remaining libraries from /lib/ in alphabetical order,
		 * except for a few exceptions
		 */
			
			if (!@include_once(dirname(__FILE__) . "/lib/actions.php")) {
				throw new InstallationException("Elgg could not load the Actions library");
			}	

				

		// We don't want to load or reload these files
	/*
			$file_exceptions = array(
										'.','..',
										'.DS_Store',
										'Thumbs.db',
										'.svn',
										'CVS','cvs',
										'settings.php','settings.example.php','languages.php','exceptions.php','elgglib.php','database.php','actions.php','sessions.php'
									);
	
		// Get the list of files to include, and alphabetically sort them
	
			$files = get_library_files(dirname(__FILE__) . "/lib",$file_exceptions);
			asort($files);
            */
            $files = array (
                20 => '/var/www/engine/lib/Utils.php',
                30 => '/var/www/engine/lib/access.php',
                10 => '/var/www/engine/lib/admin.php',
                27 => '/var/www/engine/lib/annotations.php',
                7 => '/var/www/engine/lib/api.php',
                35 => '/var/www/engine/lib/cache.php',
                36 => '/var/www/engine/lib/calendar.php',
                8 => '/var/www/engine/lib/configuration.php',
                15 => '/var/www/engine/lib/cron.php',
                32 => '/var/www/engine/lib/entities.php',
                3 => '/var/www/engine/lib/export.php',
                16 => '/var/www/engine/lib/extender.php',
                26 => '/var/www/engine/lib/filestore.php',
                6 => '/var/www/engine/lib/group.php',
                24 => '/var/www/engine/lib/input.php',
                0 => '/var/www/engine/lib/install.php',
                23 => '/var/www/engine/lib/location.php',
                25 => '/var/www/engine/lib/memcache.php',
                21 => '/var/www/engine/lib/metadata.php',
                22 => '/var/www/engine/lib/metastrings.php',
                11 => '/var/www/engine/lib/notification.php',
                19 => '/var/www/engine/lib/objects.php',
                28 => '/var/www/engine/lib/pagehandler.php',
                9 => '/var/www/engine/lib/pageowner.php',
                17 => '/var/www/engine/lib/pam.php',
                33 => '/var/www/engine/lib/plugins.php',
                29 => '/var/www/engine/lib/query.php',
                31 => '/var/www/engine/lib/relationships.php',
                5 => '/var/www/engine/lib/sites.php',
                1 => '/var/www/engine/lib/statistics.php',
                12 => '/var/www/engine/lib/system_log.php',
                4 => '/var/www/engine/lib/tags.php',
                13 => '/var/www/engine/lib/users.php',
                34 => '/var/www/engine/lib/usersettings.php',
                14 => '/var/www/engine/lib/version.php',
                18 => '/var/www/engine/lib/widgets.php',
                2 => '/var/www/engine/lib/xml.php',
            );

		// Include them
			foreach($files as $file) {
				if (isset($CONFIG->debug) && $CONFIG->debug) error_log("Loading $file..."); 
				if (!@include_once($file))
					throw new InstallationException("Could not load {$file}");
			}

		// Set default config
			set_default_config();
		
		} else {	// End portion for sanitised installs only
			
			throw new InstallationException(elgg_echo('installation:error:configuration'));
			
		}
		
		// Autodetect some default configuration settings
			//set_default_config();
$time=microtime(true);

// Trigger events
			//trigger_elgg_event('boot', 'system');

$boot = array (
    0 => 'init_db',
    1 => 'session_init',
    2 => 'sites_init',
    10 => 'configuration_init',
    500 => 'install_init',
);
/*
$boot = array (
    0 => 'init_db',
    1 => 'session_init',

);*/

foreach($boot as $b) {
    $time=microtime(true);
    $b('boot', 'system', 'null');
    echo microtime(true) - $time.' '.$b.'<br />';
}

		// Load plugins

//exit;
			$installed = is_installed();
			//$db_installed = is_db_installed();
			$db_installed = true;

			// Determine light mode
			$lm = strtolower(get_input('lightmode'));
			if ($lm == 'true') $lightmode = true;
$time = microtime(true);
			// Load plugins, if we're not in light mode
			if (($installed) && ($db_installed) && ($sanitised) && (!$lightmode)) {
				load_plugins();

				//trigger_elgg_event('plugins_boot', 'system');
			}

		// Forward if we haven't been installed
/*
			if ((!$installed || !$db_installed) && !substr_count($_SERVER["PHP_SELF"],"install.php") && !substr_count($_SERVER["PHP_SELF"],"css.php") && !substr_count($_SERVER["PHP_SELF"],"action_handler.php")) {
					header("Location: install.php");
					exit;
			}
*/

		// Trigger events
/*
			if (!substr_count($_SERVER["PHP_SELF"],"install.php") &&
				!substr_count($_SERVER["PHP_SELF"],"setup.php") &&
				!$lightmode
				&& !(defined('upgrading') && upgrading == 'upgrading')) {
				// If default settings haven't been installed, forward to the default settings page
				trigger_elgg_event('init', 'system');
				//if (!datalist_get('default_settings')) {
					//forward("setup.php");
				//}
			}
*/
/*UNIVERSAL
$CONFIG->events['init']['system'] = array (
    0 => 'notification_init',
    1 => 'users_init',
    2 => 'profile_init',
//    100 => 'export_init',
    101 => 'filestore_init',
    500 => 'elgg_init',
    501 => 'input_init',
    502 => 'actions_init',
    503 => 'admin_init',
//    504 => 'api_init',
//    505 => 'cron_init',
    //506 => 'entities_init',
    507 => 'group_init',
//    508 => 'plugin_init',
//    509 => 'statistics_init',
    510 => 'usersettings_init',
//    511 => 'widgets_init',
    512 => 't9n_init',
    513 => 'messages_init',
    514 => 'topbar_ldshake_init',
    515 => 'lds_init',
    9999 => 'access_init',
//    10000 => 'profile_fields_setup',
);
*/
//LDS
$CONFIG->events['init']['system'] = array (
//    0 => 'notification_init',
    1 => 'users_init',
//    2 => 'lds_profile_init',
//    100 => 'export_init',
    101 => 'filestore_init',
    500 => 'elgg_init',
//    501 => 'input_init',
//    502 => 'actions_init',
    503 => 'lds_admin_init',
//    504 => 'api_init',
//    505 => 'cron_init',
//    506 => 'entities_init',
//    507 => 'group_init',
//    508 => 'plugin_init',
//    509 => 'statistics_init',
//    510 => 'usersettings_init',
//    511 => 'widgets_init',
    512 => 't9n_init',
    513 => 'lds_messages_init',
    514 => 'topbar_ldshake_init',
    515 => 'lds_init',
    9999 => 'access_init',
//    10000 => 'profile_fields_setup',
);
trigger_elgg_event('init', 'system');

$CONFIG->registered_entities =
    array (
    'user' =>
    array (
        0 => '',
    ),
    'group' =>
    array (
        0 => '',
    ),
);

		// System booted, return to normal view
			set_input('view', $oldview);
?>