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
		
		if (!@include_once(dirname(__FILE__) . "/lib/install.php")) {		// Installation library
			echo "Error in installation: could not load the installation library.";
			exit;
		}
		
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
			set_default_config();
	
		// Trigger events
			trigger_elgg_event('boot', 'system');
			
		// Load plugins
		
			$installed = is_installed();
			$db_installed = is_db_installed();
			
			// Determine light mode
			$lm = strtolower(get_input('lightmode'));
			if ($lm == 'true') $lightmode = true;
			
			// Load plugins, if we're not in light mode
			if (($installed) && ($db_installed) && ($sanitised) && (!$lightmode)) {
				load_plugins();
				
				trigger_elgg_event('plugins_boot', 'system');
			}
			
		// Forward if we haven't been installed
			if ((!$installed || !$db_installed) && !substr_count($_SERVER["PHP_SELF"],"install.php") && !substr_count($_SERVER["PHP_SELF"],"css.php") && !substr_count($_SERVER["PHP_SELF"],"action_handler.php")) {
					header("Location: install.php");
					exit;
			}
			
		// Trigger events
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

			
		// System booted, return to normal view
			set_input('view', $oldview);
?>