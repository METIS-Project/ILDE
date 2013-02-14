<?php
header('Content-Type:text/html; charset=UTF-8');
include_once("../manager/i18nParser.php");
if (isset($_REQUEST['pattern'])) {
    $pattern = $_REQUEST['pattern'];
    header( "Location: $locale/$pattern.html" );
} else if (isset($_REQUEST['clfp'])) {
    $pattern = $_REQUEST['clfp'];
    header( "Location: $locale/$pattern/" );
}
?>
