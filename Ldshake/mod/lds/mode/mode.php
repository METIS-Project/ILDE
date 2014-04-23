<?php
function ldshake_theme($element) {
    global $CONFIG;

    $file = __DIR__ . "/{$CONFIG->ldshake_mode}/" . 'theme/' . $element . '.php';

    if(file_exists($file))
        return $file;
    elseif(file_exists(__DIR__ . "/default/" . 'theme/' . $element . '.php'))
        return $file;
}

function ldshake_theme_static($section) {
    global $CONFIG;

    $file = __DIR__ . "/default/" . 'theme/static/' . $section . '.php';
    include_once($file);

    $file = __DIR__ . "/{$CONFIG->ldshake_mode}/" . 'theme/static/' . $section . '.php';
    $resources_dir = $CONFIG->url . "_graphics/";

    if(file_exists($file)) {
        include_once($file);
    }

    $theme_elements_result = array();

    foreach($theme_elements["default"][$section] as $k_element => $v_element) {
        $theme_elements_result[$k_element] = $v_element;
    }

    foreach($theme_elements[$CONFIG->ldshake_mode][$section] as $k_element => $v_element) {
        $theme_elements_result[$k_element] = $v_element;
    }

    return $theme_elements_result;
}

function ldshake_theme_static_dir() {
    global $CONFIG;

    $resources_dir = $CONFIG->path . "_graphics/";

    return $resources_dir;
}