<?php

/**
 * Config file for the site
 **/

$defaultLang = array (
    'devel' =>		'en',
    'staging' =>	'en',
    'prod' =>		'en'
);

$editor_debug = false;
//$editor_debug = true;
//$debug = true;

$vle_key = "92EB2B8ED34885A22685FE2CB8739153";

$google_drive = array(
    'client_id' => '97389171535-6kve10eet4irq5d3o2mco5futkhoaop7.apps.googleusercontent.com',
    'service_account_name' => '97389171535-6kve10eet4irq5d3o2mco5futkhoaop7@developer.gserviceaccount.com',
    'key_file_location' => '/var/www/ilde/ea543845443947837b5c89843eb9c3fc935a693e-privatekey.p12',
    'application_name' => 'ilde-docs',
);

//$glueps_url = "http://pandora.tel.uva.es/METIS/GLUEPSManager/";


$glueps_url = "http://glueps-dev.gsic.uva.es/GLUEPSManager/";
$glueps_password = 'Ld$haK3';

$webcollagerest_url = "http://pandora.tel.uva.es/~wic/wic2Ldshake/";
$webcollagerest_password = 'LdS@k$1#';

$exelearningrest_url = "http://ldshake2.upf.edu:8080/";
$exelearningrest_password = 'LdS@k$1#';
/*
        //'url_rest' => "http://ilde:443/",
        //'url_gui' => "http://ilde:443/ldshakegui/",
        //'url_rest' => "http://192.168.1.219:51235/",
        //'url_gui' => "http://192.168.1.219:51235/ldshakegui/",

 */
$ldshake_mode = 'ilde';
//$ldshake_mode = 'msf';
//$ldshake_mode = 'ldshake';

/**
 * List of environments, containing the regular expression of the server name that defines them.
 */
$envUrls = array (
    'devel' =>		'^ilde|^127\.0\.0\.1,|^web\.dev|^192\.168\.137\.11|^95\.23\.158\.182',
    'staging' =>	'^ldshake3\.upf\.edu',
    'prod' =>		'^ldshake\.upf\.edu'
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
        'devel' =>		'192.168.26.150',
        'staging' =>	'localhost',
        'prod' =>		'localhost'
    ),
    'dbname' =>		array (
        'devel' =>		'ilde_perf',
        //'devel' =>		'ilde_patterns_v3',
        //'devel' =>		'metis_patterns',
        //'devel' =>		'ilde_uoc',
        'staging' =>	'ldshake',
        'prod' =>		'ldshake'
    ),
    'dbuser' =>		array (
        'devel' =>		'root',
        'staging' =>	'user',
        'prod' =>		'user'
    ),
    'dbpass' =>		array (
        'devel' =>		'a7',
        'staging' =>	'pass',
        'prod' =>		'pass'
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
        /*'apiadmin',*/
        /*'blog',*/
        /*'diagnostics',*/
        'friends',
        'groups',
        /*'help',*/
        /*'logbrowser',*/
        /*'messageboard',*/
        'messages',
        /*'metatag_manager',*/
        /*'pages',*/
        /*'photo_cumulus',*/
        'profile',
        /*'river',*/
        /*'role',*/
        /*'status',*/
        /*'tagcloud',*/
        /*'tag_cumulus',*/
        'uservalidationbyemail',
        /*'walltowall',*/
        'phpmailer',
        /*'contacts',*/
        'wholeworldfriends',
        /*'wizardUoL',*/
        'topbar_ldshake',
        'lds',
        'ldshakers'
    ),

    "needed_dependency" => array (
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
     * Location of the PDF conversion application: wkhtmltopdf ( https://github.com/antialize/wkhtmltopdf )
     */
    'pdf_converter_location' => array	(
        'devel' =>		'/opt/wkhtmltox/bin/wkhtmltopdf',
        'staging' =>	'/opt/wkhtmltox/bin/wkhtmltopdf',
        'prod' =>		'/opt/wkhtmltox/bin/wkhtmltopdf'
    ),

    /**
     * Uploaded data path
     */
    'dataroot' =>	array (
        'devel' =>		'/var/lib/ldshake/',
        'staging' =>	'/var/lib/ldshake/',
        'prod' =>		'/var/lib/ldshake/'
    ),

    /**
     * Temp path (temporal files will be created here)
     */
    'tmppath' =>	array (
        'devel' =>		'/var/local/ldshake/',
        'staging' =>	'/var/local/ldshake/',
        'prod' =>		'/var/local/ldshake/'
    ),

    /**
     * HTML previews of WebCollage & eXe
     */
    'editors_content' => array (
        'devel' =>		'/var/www/ilde/',
        'staging' =>	'/var/www/ilde/',
        'prod' =>		'/var/www/ilde/'
    ),

    /**
     * Location of the PED & img exporter
     */
    'screenshot_generator' => array	(
        'devel' =>		'/opt/wkhtmltox/bin/wkhtmltoimage',
        'staging' =>	'/opt/wkhtmltox/bin/wkhtmltoimage',
        'prod' =>		'/opt/wkhtmltox/bin/wkhtmltoimage'
    ),
);