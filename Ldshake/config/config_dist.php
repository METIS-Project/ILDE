<?php
/**
 * Config file for the site
 **/


/*
 * Default language for the ILDE
 */
$defaultLang = array (
    'devel' =>		'en',
    'staging' =>	'en',
    'prod' =>		'en'
);

/*
 * Hexadecimal string containing the encription key for the VLE data (ie. CBEB2B8ED34385A22685FE2CB8279153).
 */
//$vle_key = "";


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
//$glueps_url = "";
//$glueps_password = '';

//$webcollagerest_url = "";
//$webcollagerest_password = '';

//$exelearningrest_url = "";
//$exelearningrest_password = '';


/**
 * List of environments, containing the regular expression of the server name that defines them.
 */
$envUrls = array (
    'devel' =>		'^localhost|^127\.0\.0\.1',
    'staging' =>	'^localhost|^127\.0\.0\.1',
    'prod' =>		'^localhost|^127\.0\.0\.1',
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
        'devel' =>		'localhost',
        'staging' =>	'localhost',
        'prod' =>		'localhost'
    ),
    'dbname' =>		array (
        'devel' =>		'ilde_db',
        'staging' =>	'ilde_db',
        'prod' =>		'ilde_db'
    ),
    'dbuser' =>		array (
        'devel' =>		'dbuser',
        'staging' =>	'dbuser',
        'prod' =>		'dbuser'
    ),
    'dbpass' =>		array (
        'devel' =>		'dbpass',
        'staging' =>	'dbpass',
        'prod' =>		'dbpass'
    ),
    /**
     * Do your LdShake tables have any prefix? This is useful if you put them in an existing db,
     * athough this is NOT recommended. If in doubt, leave blank.
     */
    'dbprefix' =>	array (
        'devel' =>		'',
        'staging' =>	'',
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
        'devel' =>		'python2',
        'staging' =>	'python2',
        'prod' =>		'python2'
    ),

    /**
     * Data path
     */
    'dataroot' =>	array (
        'devel' =>		'/var/lib/ilde/',
        'staging' =>	'/var/lib/ilde/',
        'prod' =>		'/var/lib/ilde/'
    ),

    /**
     * Temp path (temporal files will be created here)
     */
    'tmppath' =>	array (
        'devel' =>		'/var/local/ilde/',
        'staging' =>	'/var/local/ilde/',
        'prod' =>		'/var/local/ilde/'
    ),

    /**
     * Location of wkhtmltopdf http://wkhtmltopdf.org/
     */
    'pdf_converter_location' => array	(
        'devel' =>		'/opt/wkhtmltox/bin/wkhtmltopdf',
        'staging' =>	'/opt/wkhtmltox/bin/wkhtmltopdf',
        'prod' =>		'/opt/wkhtmltox/bin/wkhtmltopdf'
    ),

    /**
     * Location of wkhtmltoimage http://wkhtmltopdf.org/
     */
    'screenshot_generator' => array	(
        'devel' =>		'/opt/wkhtmltox/bin/wkhtmltoimage',
        'staging' =>	'/opt/wkhtmltox/bin/wkhtmltoimage',
        'prod' =>		'/opt/wkhtmltox/bin/wkhtmltoimage'
    ),
);