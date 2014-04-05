<?php

	/**
	 * Elgg installation
	 * Various functions to assist with installing and upgrading the system
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

		/**
		 * Check that the installed version of PHP meets the minimum requirements (currently 5.2 or greater).
		 * 
		 * @return bool
		 */
		function php_check_version()
		{
			if ( // TODO: Remove this when Redhat pulls its finger out
				(version_compare(phpversion(), '5.1.6', '>=')) &&
				(version_compare(phpversion(), '5.2.0', '<'))
			)
				register_error(elgg_echo('configurationwarning:phpversion'));
			
			if (version_compare(phpversion(), '5.1.6', '>='))
				return true;
				
			return false;
		}
	
		/**
		 * Validate the platform Elgg is being installed on.
		 *
		 * @throws ConfigurationException if the validation fails.
		 * @return bool
		 */
		function validate_platform()
		{
			// Get database version
			if (!db_check_version())
				throw new ConfigurationException(elgg_echo('ConfigurationException:BadDatabaseVersion'));
			
			// Now check PHP
			if (!php_check_version())
				throw new ConfigurationException(elgg_echo('ConfigurationException:BadPHPVersion'));
				
			// TODO: Consider checking for installed modules etc
				
			return true;
		}

	/**
	 * Returns whether or not the database has been installed
	 *
	 * @return true|false Whether the database has been installed
	 */
		function is_db_installed() {
			
			global $CONFIG;

			if (isset($CONFIG->db_installed)) {
				return $CONFIG->db_installed;
			}
			
			//$tables = get_db_tables();
            $tables = array (
                0 => 'access_collection_membership',
                1 => 'access_collections',
                2 => 'annotations',
                3 => 'api_users',
                4 => 'config',
                5 => 'datalists',
                6 => 'entities',
                7 => 'entity_relationships',
                8 => 'entity_subtypes',
                9 => 'geocode_cache',
                10 => 'groups_entity',
                11 => 'hmac_cache',
                12 => 'metadata',
                13 => 'metastrings',
                14 => 'objects_entity',
                15 => 'objects_property',
                16 => 'private_settings',
                17 => 'sites_entity',
                18 => 'system_log',
                19 => 'users_apisessions',
                20 => 'users_entity',
                21 => 'users_sessions',
            );

			if (!$tables) {
				return false;
			}
			
			$CONFIG->db_installed = true; // Set flag if db is installed (if false then we want to check every time)
			
			return true;
			
		}
		
	/**
	 * Returns whether or not other settings have been set
	 *
	 * @return true|false Whether or not the rest of the installation has been followed through with
	 */
		function is_installed() {

            if(isset($_SESSION['installed']))
                return $_SESSION['installed'];

            $_SESSION['installed'] = datalist_get('installed');
			return $_SESSION['installed'];
		}
		
		/**
		 * Copy and create a new settings.php from settings.example.php, substituting the variables in
		 * $vars where appropriate.
		 * 
		 * $vars is an associate array of $key => $value, where $key is the variable text you wish to substitute (eg
		 * CONFIG_DBNAME will replace {{CONFIG_DBNAME}} in the settings file.
		 *
		 * @param array $vars The array of vars
		 * @param string $in_file Optional input file (if not settings.example.php)
		 * @return string The file containing substitutions.
		 */
		function create_settings(array $vars, $in_file="engine/settings.example.php")
		{
			$file = file_get_contents($in_file);
			
			if (!$file) return false; 
			
			foreach ($vars as $k => $v)
				$file = str_replace("{{".$k."}}", $v, $file);
			
			return $file;
		}
		
	/**
	 * Initialisation for installation functions
	 *
	 */
		function install_init() {
			register_action("systemsettings/install",true);			
		}
		
		register_elgg_event_handler("boot","system","install_init");
		
?>