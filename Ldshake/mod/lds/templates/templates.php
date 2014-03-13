<?php
function ldshake_get_template($template){
    global $CONFIG;

    $templates = array(
        'MWD' => array('MWD'),
        'MDN' => array('mdn'),
        'EP' => array('EP'),
        'coursemap' => array('coursemap'),
        'design_pattern' => array('DP','DPS'),
        'DPS' => array('DPS'),
        'PC' => array('PC','yishay'),
        'FC' => array('FC','yishay'),
        'HE' => array('HE','HEs'),
        't1e' => array('e_text_integrat'),
        't2e' => array('e_equip'),
        't3e' => array('e_mapa'),
        't4e' => array('e_cronograma'),
        't5e' => array('e_brainstorming'),
    );
    if(isset($templates[$template])) {
        $doc = array();
        foreach($templates[$template] as $file) {

            if(file_exists(__DIR__.'/i18n/'.$CONFIG->language.'/'.$file.'.txt'))
                $filename = __DIR__.'/i18n/'.$CONFIG->language.'/'.$file.'.txt';
            else
                $filename = __DIR__.'/'.$file.'.txt';

            if($text = file_get_contents($filename))
                $doc[] = $text;
        }
        return $doc;
    }
    return '';
}