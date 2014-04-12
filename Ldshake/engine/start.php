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
    $engine_dir = dirname(__FILE__);
	/// LdShake change ///

	/**
	 * Load important prerequisites
	 */
		if (!@include_once("{$engine_dir}/lib/exceptions.php")) {		// Exceptions
			echo "Error in installation: could not load the Exceptions library.";
			exit;
		}

		if (!@include_once("{$engine_dir}/lib/elgglib.php")) {		// Main Elgg library
			echo "Elgg could not load its main library.";
			exit;
		}

		if (!@include_once("{$engine_dir}/lib/system_log.php")) {		// Logging library
			echo "Error in installation: could not load the System Log library.";
			exit;
		}
	
		if (!@include_once("{$engine_dir}/lib/export.php")) {		// Export library
			echo "Error in installation: could not load the Export library.";
			exit;
		}
		
		if (!@include_once("{$engine_dir}/lib/input.php")) {		// Input library
			echo "Error in installation: could not load the input library.";
			exit;
		}
/*
		if (!@include_once("{$engine_dir}/lib/install.php")) {		// Installation library
			echo "Error in installation: could not load the installation library.";
			exit;
		}
*/
		if (!@include_once("{$engine_dir}/lib/cache.php")) {		// Installation library
			echo "Error in installation: could not load the cache library.";
			exit;
		}
		
		if (!@include_once("{$engine_dir}/lib/sessions.php")) {
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
			
			if (!@include_once("{$engine_dir}/settings.php"))  		// Global settings
				throw new InstallationException("Elgg could not load the settings file.");
				
		/**
		 * Load and initialise the database
		 */
	
			if (!@include_once("{$engine_dir}/lib/database.php"))	// Database connection
				throw new InstallationException("Elgg could not load the main Elgg database library.");
				
		/**
		 * Load the remaining libraries from /lib/ in alphabetical order,
		 * except for a few exceptions
		 */
			
			if (!@include_once("{$engine_dir}/lib/actions.php")) {
				throw new InstallationException("Elgg could not load the Actions library");
			}

            if (!@include_once("{$engine_dir}/lib/configuration.php")) {
                throw new InstallationException("Elgg could not load the Configuration library");
            }

            setup_db_connections();
            configuration_init();

            if (!@include_once("{$engine_dir}/lib/languages.php")) {
                throw new InstallationException("Elgg could not load the Languages library");
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
	
			$files = get_library_files("{$engine_dir}/lib",$file_exceptions);
			asort($files);
            */
            $files = array (
                //8 => "{$engine_dir}/lib/configuration.php",
                //90 => "{$engine_dir}/lib/languages.php",
                20 => "{$engine_dir}/lib/Utils.php",
                30 => "{$engine_dir}/lib/access.php",
                10 => "{$engine_dir}/lib/admin.php",
                27 => "{$engine_dir}/lib/annotations.php",
                7 => "{$engine_dir}/lib/api.php",
                35 => "{$engine_dir}/lib/cache.php",
                36 => "{$engine_dir}/lib/calendar.php",
                15 => "{$engine_dir}/lib/cron.php",
                32 => "{$engine_dir}/lib/entities.php",
                3 => "{$engine_dir}/lib/export.php",
                16 => "{$engine_dir}/lib/extender.php",
                26 => "{$engine_dir}/lib/filestore.php",
                6 => "{$engine_dir}/lib/group.php",
                24 => "{$engine_dir}/lib/input.php",
                0 => "{$engine_dir}/lib/install.php",
                23 => "{$engine_dir}/lib/location.php",
                25 => "{$engine_dir}/lib/memcache.php",
                21 => "{$engine_dir}/lib/metadata.php",
                22 => "{$engine_dir}/lib/metastrings.php",
                11 => "{$engine_dir}/lib/notification.php",
                19 => "{$engine_dir}/lib/objects.php",
                28 => "{$engine_dir}/lib/pagehandler.php",
                9 => "{$engine_dir}/lib/pageowner.php",
                17 => "{$engine_dir}/lib/pam.php",
                33 => "{$engine_dir}/lib/plugins.php",
                29 => "{$engine_dir}/lib/query.php",
                31 => "{$engine_dir}/lib/relationships.php",
                5 => "{$engine_dir}/lib/sites.php",
                1 => "{$engine_dir}/lib/statistics.php",
                12 => "{$engine_dir}/lib/system_log.php",
                4 => "{$engine_dir}/lib/tags.php",
                13 => "{$engine_dir}/lib/users.php",
                34 => "{$engine_dir}/lib/usersettings.php",
                14 => "{$engine_dir}/lib/version.php",
                18 => "{$engine_dir}/lib/widgets.php",
                2 => "{$engine_dir}/lib/xml.php",
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
    //echo microtime(true) - $time.' '.$b.'<br />';
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
if($plugin_handler == 'lds') {
    $CONFIG->events['init']['system'] = array (
        1 => 'users_init',
        2 => 'lds_profile_init',
        101 => 'filestore_init',
        500 => 'elgg_init',
        503 => 'lds_admin_init',
        512 => 't9n_init',
        513 => 'lds_messages_init',
        514 => 'topbar_ldshake_init',
        515 => 'lds_init',
/*        516 => 'LdSObject_init',
        517 => 'DocumentObject_init',
        518 => 'DocumentEditorObject_init',
        519 => 'DocumentRevisionObject_init',
        520 => 'DocumentEditorRevisionObject_init',
        521 => 'DeferredNotification_init',
*/        9999 => 'access_init',
    );
}
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