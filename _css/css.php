<?php

/**
 * Lazy CSS compilation. If it finds the compiled file, just dumps it. Saves about 1/3 sec of loading time.
 * WARNING: If you change any CSS rule, you should delete the compiled file manually! 
 * Alternatively you can disable the precompilation setting the CACHE_CSS constant to 0.


 */

define ('CACHE_CSS', 1);

if (!file_exists ('compiled.css') || !CACHE_CSS)
{
	require_once(dirname(dirname(__FILE__)) . "/engine/start.php");

	$default_css = elgg_view("css");
	file_put_contents('compiled.css', $default_css);
}
else $default_css = file_get_contents('compiled.css');

header("Content-type: text/css", true);
header('Expires: ' . date('r',time() + 864000), true);
header("Pragma: public", true);
header("Cache-Control: public", true);
header("Content-Length: " . strlen($default_css));

echo $default_css;