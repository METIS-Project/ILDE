<?php
header('Content-Type:text/html; charset=UTF-8');

include_once("../manager/i18nParser.php");

echo file_get_contents("clfps_$locale.txt");

?>
