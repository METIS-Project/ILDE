<?php
function ldshake_get_template($template){
    $templates = array(
        'MWD' => 'MWD',
        'MDN' => 'mdn',
        'EP' => 'EP',
        'design_pattern' => 'DP',
        'DPS' => 'DPS',
        'PC' => 'PC',
        'FC' => 'FC',
        't1e' => 'e_text_integrat',
        't2e' => 'e_equip',
        't3e' => 'e_mapa',
        't4e' => 'e_cronograma',
        't5e' => 'e_brainstorming',
    );
    if(isset($templates[$template])) {
        $filename = __DIR__.'/'.$templates[$template].'.txt';
        if($text = file_get_contents($filename))
            return $text;
    }
    return '';
}