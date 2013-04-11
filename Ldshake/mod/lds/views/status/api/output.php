<?php

$result = $vars['result'];
$export = $result->export();

if(!$export->status)
    header(' ', true, 200);
else
    if(!$export->status)
        header(' ', true, 400);

