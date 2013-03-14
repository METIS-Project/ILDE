<?php

/**
 * Config file for the site
 * @author Pau Moreno
 *
 * APache config files for the editors instance:

 httpd.conf:
 
 #eXeLearning
ProxyRequests Off

<Proxy *>
Order deny,allow
Allow from all
</Proxy>

ProxyPass /exelearning http://127.0.0.1:8080/
ProxyPassReverse /exelearning http://127.0.0.1:8080/
 
 
 Habilitar:
 LoadModule proxy_module modules/mod_proxy.so
 LoadModule proxy_http_module modules/mod_proxy_http.so
 
 */

/**
 * List of environments, containing the regular expression of the server name that defines them.
 */
$envUrls = array (
	'devel' =>		'^localhost|^127\.0\.0\.1,|^web\.dev',
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
						'devel' =>		'localhost',
						'staging' =>	'localhost',
						'prod' =>		'localhost'
					),
	'dbname' =>		array (
						'devel' =>		'ldshake_trunk',
						'staging' =>	'ldshake',
						'prod' =>		'ldshake'
					),
	'dbuser' =>		array (
						'devel' =>		'user',
						'staging' =>	'user',
						'prod' =>		'user'
					),
	'dbpass' =>		array (
						'devel' =>		'pass',
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
		'debug',
		't9n',
		'about',
		/*'apiadmin',*/
		/*'blog',*/
		'diagnostics',
		'friends',
        'groups',
		/*'help',*/
		'logbrowser',
		'messageboard',
		'messages',
		'metatag_manager',
		/*'pages',*/
		/*'photo_cumulus',*/
		'profile',
		'river',
		'role',
		/*'status',*/
		/*'tagcloud',
		'tag_cumulus',*/
		'uservalidationbyemail',
		/*'walltowall',*/
		'phpmailer',
		'contacts',
		'wholeworldfriends',
		/*'wizardUoL',*/
		'topbar_ldshake',
		'lds',
		'ldshakers'
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
											'devel' =>		'/opt/wkhtmltox/wkhtmltopdf',
											'staging' =>	'/opt/wkhtmltox/wkhtmltopdf',
											'prod' =>		'/opt/wkhtmltox/wkhtmltopdf'
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

	
	//TODO Integrate w existing constants
	/**
	 * Temp data and exported files for the eXeLearning module 
	 * E:\python27exe\Python27\python.exe E:\exelearning\exe\exe\exe E:/editors-tmp 8080 /exelearning
	 */
	'exedata' => array (
						'devel' =>		'/var/local/ldshake/',
						'staging' =>	'/var/local/ldshake/',
						'prod' =>		'/var/local/ldshake/'
					),
	
	/**
	 * HTML previews of WebCollage & eXe
	 */
	'editors_content' => array (
						'devel' =>		'/var/www/ldshake/',
						'staging' =>	'/var/www/ldshake/',
						'prod' =>		'/var/www/ldshake/'
						),
	
	/**
	 * Location of the PED & img exporter
	 */
	'screenshot_generator' => array	(
								'devel' =>		'/opt/wkhtmltox/wkhtmltopdf',
								'staging' =>	'/opt/wkhtmltox/wkhtmltopdf',
								'prod' =>		'/opt/wkhtmltox/wkhtmltopdf'
							),		
							
	/**
	 * Location of the zip tool
	 */
	'zip_path' => array (
						'devel' =>		'zip',
						'staging' =>	'zip',
						'prod' =>		'zip'
					),
);
