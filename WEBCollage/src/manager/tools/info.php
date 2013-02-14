<?php

include_once ("../JSON.php");

$petition = $_REQUEST['petition'];
$url = $_REQUEST['url'];


$xml = simplexml_load_file($url);

$summary = (string)$xml->entry->summary;
$title = (string)$xml->entry->title;

$tooldata = array("title" => $title, "summary" => $summary);

$result = array("petition" => $petition, "tooldata" => $tooldata);

$json = new Services_JSON();
echo $json->encode($result);

?>
