<?php

function jon_init()
{
    global $CONFIG;

    register_page_handler('jon', 'jon_page_handler');

}

register_elgg_event_handler('init','system','jon_init');

function jon_page_handler($page){

    echo('hola');

}