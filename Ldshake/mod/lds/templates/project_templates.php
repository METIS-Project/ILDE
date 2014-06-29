<?php

/*********************************************************************************
 * LdShake is a platform for the social sharing and co-edition of learning designs
 * Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
 *
 * (Contributors, alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by the
 * Free Software Foundation with the addition of the following permission added
 * to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK
 * IN WHICH THE COPYRIGHT IS OWNED BY Universitat Pompeu Fabra (UPF), Barcelona,
 * UPF DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program; if not, see http://www.gnu.org/licenses.
 *
 * You can contact the Interactive Technologies Group (GTI), Universitat Pompeu Fabra, Barcelona.
 * headquarters at c/Roc Boronat 138, Barcelona, or at email address davinia.hernandez@upf.edu
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License version 3,
 * these Appropriate Legal Notices must retain the display of the "Powered by
 * LdShake" logo with a link to the website http://ldshake.upf.edu.
 * If the display of the logo is not reasonably feasible for
 * technical reasons, the Appropriate Legal Notices must display the words
 * "Powered by LdShake" with the link to the website http://ldshake.upf.edu.
 ********************************************************************************/

global $CONFIG;

$CONFIG->project_templates['default'] = array(
    'coursemap' => array(
        'title' => T('Course Map'),
        'type'  => 'doc',
        'subtype'   => 'coursemap',
    ),

    'MDN' => array(
        'title' => T('Design Narrative'),
        'type'  => 'doc',
        'subtype'   => 'MDN',
    ),

    'design_pattern' => array(
        'title' => T('Design Pattern'),
        'type'  => 'doc',
        'subtype'   => 'design_pattern',
    ),

    'PC' => array(
        'title' => T('Persona Card'),
        'type'  => 'doc',
        'subtype'   => 'PC',
    ),

    'FC' => array(
        'title' => T('Factors and Concerns'),
        'type'  => 'doc',
        'subtype'   => 'FC',
    ),

    'HE' => array(
        'title' => T('Heuristic Evaluation'),
        'type'  => 'doc',
        'subtype'   => 'HE',
    ),

    'exe' => array(
        'title' => T('eXeLearning'),
        'type'  => 'exelearningrest',
        'subtype'   => null,
    ),

    'collage' => array(
        'title' => T('WebCollage'),
        'type'  => 'webcollagerest',
        'subtype'   => null,
    ),

    'openglm' => array(
        'title' => T('OpenGLM'),
        'type'  => 'openglm',
        'subtype'   => null,
    ),

    'cadmos' => array(
        'title' => T('Cadmos'),
        'type'  => 'cadmos',
        'subtype'   => null,
    ),

    'cld' => array(
        'title' => T('CompendiumLD'),
        'type'  => 'cld',
        'subtype'   => null,
    ),

    'image' => array(
        'title' => T('Image'),
        'type'  => 'image',
        'subtype'   => null,
    ),
);

if($CONFIG->url == 'http://ilde.upf.edu/kek/') {
    $CONFIG->project_templates['default']['kek_p1'] = array(
        'title' => T('Πρότυπο Διδακτικού Σεναρίου'),
        'type'  => 'doc',
        'subtype'   => 'kek_p1',
        'icon' => 'text',
    );

}

if(!empty($CONFIG->google_drive)) {
    $CONFIG->project_templates['default']['google_docs'] = array(
        'title' => T('Google Docs'),
        'type'  => 'google_docs',
        'subtype'   => null,
    );

    $CONFIG->project_templates['default']['google_spreadsheet'] = array(
        'title' => T('Google Spreadsheet'),
        'type'  => 'google_spreadsheet',
        'subtype'   => null,
    );
}

$CONFIG->project_templates['full'] = $CONFIG->project_templates['default'];

if($CONFIG->ldshake_mode == 'msf' and !empty($CONFIG->google_drive)) {
    $CONFIG->project_templates['msf'] = array(

        'msf_a01' => array(
            'title' => T('Activity Analysis'),
            'type'  => 'google_docs',
            'subtype'   => 'msf_a01',
            'icon' => 'msf',
        ),

        'msf_a02' => array(
            'title' => T('Task Analysis'),
            'type'  => 'google_spreadsheet',
            'subtype'   => 'msf_a02',
            'icon' => 'msf',
        ),

        'msf_d00' => array(
            'title' => T('Learning Task'),
            'type'  => 'google_docs',
            'subtype'   => 'msf_d00',
            'icon' => 'msf',
        ),

        'msf_d01c' => array(
            'title' => T('Course Objectives'),
            'type'  => 'google_docs',
            'subtype'   => 'msf_d01c',
            'icon' => 'msf',
        ),

        'msf_d01d' => array(
            'title' => T('Development'),
            'type'  => 'google_docs',
            'subtype'   => 'msf_d01d',
            'icon' => 'msf',
        ),

        'msf_d02' => array(
            'title' => T('Course Structure(ES)'),
            'type'  => 'google_docs',
            'subtype'   => 'msf_d02',
            'icon' => 'msf',
        ),

        'msf_d03' => array(
            'title' => T('Activities and Resources'),
            'type'  => 'google_docs',
            'subtype'   => 'msf_d03',
            'icon' => 'msf',
        ),
    );

    $CONFIG->project_templates['full'] = array_merge($CONFIG->project_templates['full'], $CONFIG->project_templates['msf']);
}