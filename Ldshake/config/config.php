<?php
/**
 * Config file for the site
 **/


/*
 * Default language for the ILDE
 */
$defaultLang = array (
    'prod' =>		'en'
);


/**
 * Google tools configuration.
 */

/*
$google_drive = array(
    'client_id' => '',
    'service_account_name' => '',
    'key_file_location' => '',
    'application_name' => '',
);
*/


/**
 * URL and password of the integrated tools.
 */
//$glueps_url = "http://gluepsserver/";
//$glueps_password = 'gluepspassword';

//$webcollagerest_url = "http://webcolageserver/";
//$webcollagerest_password = 'webcollagepassword';

//$exelearningrest_url = "http://exelearningserver/";
//$exelearningrest_password = 'exelearningpassword';

/*
 * Hexadecimal string containing the encription key for the VLE data (for example CBEB2B8ED34385A22685FE2CB8279153).
 */
//$vle_key = "CBEB2B8ED34385A22685FE2CB8279153";


/**
 * List of environments, containing the regular expression of the server name that defines them.
 */
$envUrls = array (
    'prod' =>		'^mydomain|^192\.168\.1\.10|^example\.domain\.org',
);


/**
 * List of site properties. If a property is an array of 3 elements having 'devel', 'staging and 'prod'
 * as keys, it will automagically be environment-dependent.
 */
$confOptions = array (
    /**
     * Basic DB connection options
     */
    'dbhost' =>		array (
        'prod' =>		'localhost'
    ),
    'dbname' =>		array (
        'prod' =>		'ldshake_db'
    ),
    'dbuser' =>		array (
        'prod' =>		'ldshake_user'
    ),
    'dbpass' =>		array (
        'prod' =>		'dbpass'
    ),

    /**
     * Do your LdShake tables have any prefix? This is useful if you put them in an existing db,
     * athough this is NOT recommended. If in doubt, leave blank.
     */
    'dbprefix' =>	array (
        'prod' =>		''
    ),

    /**
     * Enabled plugin folder names
     * NOTE: They will be loaded in the order they are listed here!
     */
    'enabled_plugins' => array (
        't9n',
        'about',
        'friends',
        'groups',
        'messages',
        'profile',
        'uservalidationbyemail',
        'phpmailer',
        'wholeworldfriends',
        'topbar_ldshake',
        'lds',
        'ldshakers'
    ),

    'needed_dependency' => array (
        'lds' => array(
            't9n',
            'messages',
            'profile',
            'topbar_ldshake',
            'lds')
    ),

    /**
     * Base number for id 'obfuscation', esp. for the LdS document viewer. 13330 = aaa in thirtysixecimal (base 36)
     * This base covers all alphanumeric characters: 0123456789abcdefghijklmnopqrstuvwxyz
     */
    'salt' => 13330,

);

$confPaths = array (
    /**
     * Location of Python interpreter
     */
    'pythonpath' =>	array (
        'prod' =>		'python2'
    ),

    /**
     * Data path
     */
    'dataroot' =>	array (
        'prod' =>		'/var/lib/ldshake/'
    ),

    /**
     * Temp path (temporal files will be created here)
     */
    'tmppath' =>	array (
        'prod' =>		'/var/local/ldshake/'
    ),

    /**
     * Location of wkhtmltopdf http://wkhtmltopdf.org/
     */
    'pdf_converter_location' => array	(
        'prod' =>		'/opt/wkhtmltox/bin/wkhtmltopdf'
    ),

    /**
     * Location of wkhtmltoimage http://wkhtmltopdf.org/
     */
    'screenshot_generator' => array	(
        'prod' =>		'/opt/wkhtmltox/bin/wkhtmltoimage'
    ),
);