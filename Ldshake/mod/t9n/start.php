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
 * Translations controller
 */

function t9n_init() {	
	global $CONFIG;
	
	$CONFIG->currentLang = 'en';
	//include_once "data/{$CONFIG->currentLang}.php";
	
	//$CONFIG->t9n = $translations;
	
	register_page_handler('translation','t9n_page_handler');
}

register_elgg_event_handler('init','system','t9n_init');


function t9n_page_handler ($page) {
	//Nothing here will be exposed to non-logged users, so go away!
	gatekeeper();
	
	//Sub_controller dispatcher
	if (function_exists("t9n_exec_{$page[0]}"))
	{
		set_context("t9n_exec_{$page[0]}");
		call_user_func("t9n_exec_{$page[0]}", $page);
	}
	else
		die ('404'); //TODO
}

function t9n_exec_parseViews ($page) {
	global $CONFIG;
	
	set_time_limit (0);
	ini_set('memory_limit', '512M');
	
	$paths = array (
		$CONFIG->path . 'mod/',
		$CONFIG->path . 'engine/',
		$CONFIG->path . 'views/',
		$CONFIG->path . 'actions/',
	);

	$copys = array ();
	//$hola = 0;
	foreach ($paths as $path) {
		$iterator = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($path), RecursiveIteratorIterator::CHILD_FIRST);
		foreach ($iterator as $path) {
			if (preg_match ('/\.svn/',$path->getPathname())) continue;
			
			if ($path->isFile()) {
				if (preg_match ('/\.php$/i', $path->getFilename())) {
					
					$code = file_get_contents ($path->getPathname());
					
					if (preg_match_all('/T\s*\("(.*?)"(,\s*.*?)?\)/', $code, $matches)) {
						foreach ($matches[1] as $match) {
							$copys[] = stripcslashes($match);
						}
						//$copys = array_merge($copys, $matches[1]);
					}
				}
			}
			
			//echo $path->getPathname()."<br />";
		}
	}
	
	$copys = array_unique($copys);
	dprint ($copys);

	$pattern = "<?php\r\n\r\n\$translations = array (\r\n\r\nç);\r\n";
	
	$contents = '';
	foreach ($copys as $copy) {
		$escapedCopy = str_replace('"', '\"', $copy);
		if (isset($CONFIG->t9n[$copy])) {
			$t = str_replace('"', '\"', $CONFIG->t9n[$copy]);
			$contents .= "\"{$escapedCopy}\"\r\n=> \"{$t}\",\r\n\r\n";
		} else {
			$contents .= "\"{$escapedCopy}\"\r\n=> \"\",\r\n\r\n";
		}
	}
	
	$contents = str_replace('ç', $contents, $pattern);
	
	$tstamp = time();
	file_put_contents ($CONFIG->path . "mod/t9n/data/ca$tstamp.php", $contents);
}

/**
 * Translation function
 * @param string $copy
 */
function T ($copy) {
 	global $CONFIG;
	
	$vars = func_get_args ();
	array_shift($vars);

    /*
	if (isset($CONFIG->t9n[$copy])) {
		$translation = $CONFIG->t9n[$copy]; 
	} else {
		$translation = $copy;
	}
    */

    $translation = $copy;
	
	foreach ($vars as $k=>$var) {
		$translation = str_replace('%'.($k + 1), $var, $translation);
	}
	
	return $translation;
}