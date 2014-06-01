<?php

$forcedEnv = $argv[1];
$gdoc_cache_id = $argv[2];

//$forcedEnv = $_GET['env'];
//$gdoc_cache_id = $_GET['id'];

//Fool the thing and tell it I'm an admin
global $is_admin;
$is_admin = true;

require_once(__DIR__."/../../../engine/start.php");

if(!$gdoc_cache = get_entity($gdoc_cache_id))
    exit();

$data = unserialize(base64_decode($gdoc_cache->description));
$editor = editorsFactory::getTempInstance('google_docs');
$editor->cache_remote_gdoc($data);
$gdoc_cache->delete();

//perform GC
$thn = get_metastring_id("gdoc_hash");
$query = <<<SQL
SELECT entity_guid AS guid, 'object' AS `type`
FROM metadata m
JOIN entities e ON e.guid = m.entity_guid
JOIN objects_entity o ON e.guid = m.entity_guid
WHERE
name_id = {$thn}
AND m.enabled = 'no'
AND e.enabled = 'yes'
LIMIT 50
SQL;

if($gdocs = get_data($query, "entity_row_to_elggstar")) {
    foreach($gdocs as $gdocs_elem) {
        $gdocs_elem->delete();
    }
}