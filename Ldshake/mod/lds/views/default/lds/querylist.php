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

?>

<?php extract($vars) ?>
<?php if (is_array($vars['list']) && sizeof($vars['list']) > 0): ?>
<ul id="query_list">
    
     
    
	<?php 
        //Obtenemos el array de contador de palabras devueltos por searchPattern
        $contWord_aux = $vars['list'];
        //El peso mayor será 7, porque imprimiremos los 7 primeros patrones 
        $peso = 7;
        //Array donde guardaremos los patrones y sus pesos
        $arrayPesos = array();
        //Hacemos un barrido de los siete primeros del array
        for ($i = 0; $i <= 7; ++$i) {
            //Obtenemos su contador
            $value = array_shift($vars['list']);
            //Obtenemos el patrón al que pertenece
            $patron = array_search($value, $contWord_aux);
            //Lo introducimos en nuestro array donde guardarmemos los patrones y sus pesos
            $arrayPesos[$patron] = $peso;
            $contWord_aux = $vars['list'];
            //Comprobamos si tenemos otro patrón con el mismo cantador
            while (in_array($value, $vars['list'])) {
                //obtenemos el contador 
                $valueA = array_shift($vars['list']);
                //Obtenemos el patron
                $patron = array_search($valueA, $contWord_aux);
                $contWord_aux = $vars['list'];
                //Lo incluimos en el array
                $arrayPesos[$patron] = $peso;
            }
            //Disminuimos el peso
            --$peso;
        }
        $cont=0;
        for($i=0; $i<7; ++$i):
                 $listaux=$arrayPesos;
                 $aux=array_shift($arrayPesos);
                 $patterns=array_search($aux, $listaux);
                 $peso=(int)$aux;
                 //Codificamos el nombre del patrón devuelto para que se imprima de la manera correcta
                 $patternsaux="";
                 $patternsname="";
                 for($j=0; $j<=strlen($patterns); ++$j){
                     if (ctype_upper($patterns[$j])){
                         $patternsaux[$j]=" ".$patterns[$j];
                     }
                     else{
                     $patternsaux[$j]=$patterns[$j];
                     }
                     $patternsname=$patternsname.$patternsaux[$j];
                 }
                 //Ponemos la primera letra en mayúsculas
                 $patternsname=ucwords($patternsname);
                 ++$cont;
                 //Alineamos según vayan saliendo del array
                 switch($cont){
                     case 1:
                         $aling="center";
                         break;
                      case 2:
                         $aling="left";
                         break;
                      case 3:
                         $aling="right";
                          $cont=0;
                         break;
                 };
               
          ?>
            <?php
            $lhref="";
if($url == "http://ilde.upf.edu/") {

    if(!strcmp("$patternsname","Design narrative")) {$lhref="{$url}pg/lds/view/2242/";$t_text = "At the UVA team, teacher workshops were held to promote learning design and implementation of collaborative learning activities. Following the most widely used format and technologies at our institution, the idea was to teach/practice design for blended learning (i.e. combining face-to-face and online activities) courses, and using Moodle VLEs as the main technological support (alongside other web tools). ";}
    if(!strcmp("$patternsname","Controlled Group Formation")) {$lhref="{$url}pg/lds/view/2228/";$t_text = "How can a group of students be formed when they are asked to work on an assignment to collaborative build knowledge?";}
    if(!strcmp("$patternsname","Free G F")) {$lhref="{$url}pg/lds/view/2224/";$t_text = "How can a group of students be formed when they are asked to work on a large demanding assignment?";}
    if(!strcmp("$patternsname","Facilitator")) {$lhref="{$url}pg/lds/view/2220/";$t_text = "Students might be guided towards greater independence (autonomous learning) in collaborative learning situations and, at the same time, towards effective collaboration.";}
    if(!strcmp("$patternsname","Guiding Questions")) {$lhref="{$url}pg/lds/view/2216/";$t_text = "A group of students that collaboratively perform a learning task are not sure on the criteria for deciding whether they have completed it or whether it fulfils the expected results.";}
    if(!strcmp("$patternsname","Management of On-Line Questionnaires")) {$lhref="{$url}pg/lds/view/2212/";$t_text = "How can web-based questionnaires be created, delivered and graded?";}
    if(!strcmp("$patternsname","Structured Space for Group Task")) {$lhref="{$url}pg/lds/view/2208/";$t_text = "Sometimes students require an online space that facilitates their collaboration.";}
    if(!strcmp("$patternsname","The Assessment Task As A Vehicle For Learning")) {$lhref="{$url}pg/lds/view/2204/";$t_text = "Assessment regimes which prioritize technical measurement issues, such as validity and reliability, may ignore the effects of the test on students’ approaches to learning. On the other hand, we do need to assess students’ work, and our approaches must be fair and  reasonable.";}
    if(!strcmp("$patternsname","Enriching Discussions by Generating Cognitive Conflicts")) {$lhref="{$url}pg/lds/view/2200/";$t_text = "Sometimes students are reluctant to challenge each other’s different views on a particular subject or the results from a particular activity during a discussion.";}
    if(!strcmp("$patternsname","Preparing Fruitful Discussions Using Surveys")) {$lhref="{$url}pg/lds/view/2196/";$t_text = "The exploration of contradictory views in a discussion can promote a deeper understanding of a subject. It can stimulate each participant to develop their own opinions and explore their reason for them.";}
    if(!strcmp("$patternsname","Discussion Group")) {$lhref="{$url}pg/lds/view/2189/";$t_text = "Discussion groups are the most common way of organizing activity in networked learning environments. The degree to which a discussion is structured, and the choice of structure, is key in determining how successfully the discussion will promote learning for the participants.";}
    if(!strcmp("$patternsname","Introductory Activity Learning Design Awareness")) {$lhref="{$url}pg/lds/view/2183/";$t_text = "Students may be aware of the collaborative learning process that they will perform so that their learning is potentially meaningful and so that positive interdependence among the members of the groups is encouraged. This pattern discusses how this might be accomplished. ";}
    if(!strcmp("$patternsname","Enriching The Learning Process")) {$lhref="{$url}pg/lds/view/2169/";$t_text = "How can the learning process be designed so that the (group of) students that perform some activities at faster rates can employ the time till the rest of the group finish (note that in collaborative learning synchronization of group activities is a key issue) to escalate the level and quality of the learning experiences?";}
    if(!strcmp("$patternsname","Thinking Aloud Pair Problem Solving")) {$lhref="{$url}pg/lds/view/2155/";$t_text = "If students face a series of problems whose solutions imply reasoning processes, an adequatecollaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Simulation")) {$lhref="{$url}pg/lds/view/2147/";$t_text = "If groups of students face a problem whose resolution implies the simulation of a situation in which several characters are involved, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Brainstorming")) {$lhref="{$url}pg/lds/view/2141/";$t_text = "If groups of students face the resolution of a problem whose solution requires the generation of a large number of possible answers/ideas in a short period of time, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","TPS")) {$lhref="{$url}pg/lds/view/2111/";$t_text = "If groups of students face resolution of a challenging or open-ended question, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Pyramid")) {$lhref="{$url}pg/lds/view/2100/";$t_text = "If groups of students face resolution of a complex problem/task, usually without a concrete solution, whose resolution implies the achievement of gradual consensus among all the students, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Jigsaw")) {$lhref="{$url}pg/lds/view/2890/";$t_text = "If groups of students face resolution of a complex problem/task that can be easily divided into sections or independent sub-problems, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Mapping Forces")) {$lhref="{$url}pg/lds/view/1766/";$t_text = "Design challenges are complex and intertwined. Many argue they are fundementally \"Wicked problems\" (Rittel, 1973). The first challenge a designer faces is identifying the design challenge: understanding what are the forces that define the context for which she designs, and what in the configuration of these forces does she wish to change.";}
    if(!strcmp("$patternsname","Structuredspace For Group Tasks")) {$lhref="{$url}pg/lds/view/2208/";$t_text = "Sometimes students require an online space that facilitates their collaboration.";}

}

if($url == "http://ilde.upf.edu/agora/") {
    /*AGORA*/if(!strcmp("$patternsname","Jigsaw")) {$lhref="{$url}pg/lds/view/689/";$t_text = "If groups of students face resolution of a complex problem/task that can be easily divided into sections or independent sub-problems, an adequate collaborative learning flow may be planned.";}
    /*AGORA*/if(!strcmp("$patternsname","Design narrative")) {$lhref="{$url}pg/lds/view/772/";$t_text = "At the UVA team, teacher workshops were held to promote learning design and implementation of collaborative learning activities. Following the most widely used format and technologies at our institution, the idea was to teach/practice design for blended learning (i.e. combining face-to-face and online activities) courses, and using Moodle VLEs as the main technological support (alongside other web tools). ";}

    if(!strcmp("$patternsname","Controlled Group Formation")) {$lhref="{$url}pg/lds/view/645/";$t_text = "How can a group of students be formed when they are asked to work on an assignment to collaborative build knowledge?";}
    if(!strcmp("$patternsname","Free G F")) {$lhref="{$url}pg/lds/view/677/";$t_text = "How can a group of students be formed when they are asked to work on a large demanding assignment?";}
    if(!strcmp("$patternsname","Facilitator")) {$lhref="{$url}pg/lds/view/673/";$t_text = "Students might be guided towards greater independence (autonomous learning) in collaborative learning situations and, at the same time, towards effective collaboration.";}
    if(!strcmp("$patternsname","Guiding Questions")) {$lhref="{$url}pg/lds/view/681/";$t_text = "A group of students that collaboratively perform a learning task are not sure on the criteria for deciding whether they have completed it or whether it fulfils the expected results.";}
    if(!strcmp("$patternsname","Management of On-Line Questionnaires")) {$lhref="{$url}pg/lds/view/693/";$t_text = "How can web-based questionnaires be created, delivered and graded?";}
    if(!strcmp("$patternsname","Structured Space for Group Task")) {$lhref="{$url}pg/lds/view/717/";$t_text = "Sometimes students require an online space that facilitates their collaboration.";}
    if(!strcmp("$patternsname","The Assessment Task As A Vehicle For Learning")) {$lhref="{$url}pg/lds/view/729/";$t_text = "Assessment regimes which prioritize technical measurement issues, such as validity and reliability, may ignore the effects of the test on students’ approaches to learning. On the other hand, we do need to assess students’ work, and our approaches must be fair and  reasonable.";}
    if(!strcmp("$patternsname","Enriching Discussions by Generating Cognitive Conflicts")) {$lhref="{$url}pg/lds/view/665/";$t_text = "Sometimes students are reluctant to challenge each other’s different views on a particular subject or the results from a particular activity during a discussion.";}
    if(!strcmp("$patternsname","Preparing Fruitful Discussions Using Surveys")) {$lhref="{$url}pg/lds/view/705/";$t_text = "The exploration of contradictory views in a discussion can promote a deeper understanding of a subject. It can stimulate each participant to develop their own opinions and explore their reason for them.";}
    if(!strcmp("$patternsname","Discussion Group")) {$lhref="{$url}pg/lds/view/661/";$t_text = "Discussion groups are the most common way of organizing activity in networked learning environments. The degree to which a discussion is structured, and the choice of structure, is key in determining how successfully the discussion will promote learning for the participants.";}
    if(!strcmp("$patternsname","Introductory Activity Learning Design Awareness")) {$lhref="{$url}pg/lds/view/685/";$t_text = "Students may be aware of the collaborative learning process that they will perform so that their learning is potentially meaningful and so that positive interdependence among the members of the groups is encouraged. This pattern discusses how this might be accomplished. ";}
    if(!strcmp("$patternsname","Enriching The Learning Process")) {$lhref="{$url}pg/lds/view/669/";$t_text = "How can the learning process be designed so that the (group of) students that perform some activities at faster rates can employ the time till the rest of the group finish (note that in collaborative learning synchronization of group activities is a key issue) to escalate the level and quality of the learning experiences?";}
    if(!strcmp("$patternsname","Thinking Aloud Pair Problem Solving")) {$lhref="{$url}pg/lds/view/733/";$t_text = "If students face a series of problems whose solutions imply reasoning processes, an adequatecollaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Simulation")) {$lhref="{$url}pg/lds/view/713/";$t_text = "If groups of students face a problem whose resolution implies the simulation of a situation in which several characters are involved, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Brainstorming")) {$lhref="{$url}pg/lds/view/637/";$t_text = "If groups of students face the resolution of a problem whose solution requires the generation of a large number of possible answers/ideas in a short period of time, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","TPS")) {$lhref="{$url}pg/lds/view/749/";$t_text = "If groups of students face resolution of a challenging or open-ended question, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Pyramid")) {$lhref="{$url}pg/lds/view/701/";$t_text = "If groups of students face resolution of a complex problem/task, usually without a concrete solution, whose resolution implies the achievement of gradual consensus among all the students, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Mapping Forces")) {$lhref="{$url}pg/lds/view/697/";$t_text = "Design challenges are complex and intertwined. Many argue they are fundementally \"Wicked problems\" (Rittel, 1973). The first challenge a designer faces is identifying the design challenge: understanding what are the forces that define the context for which she designs, and what in the configuration of these forces does she wish to change.";}
    if(!strcmp("$patternsname","Structuredspace For Group Tasks")) {$lhref="{$url}pg/lds/view/717/";$t_text = "Sometimes students require an online space that facilitates their collaboration.";}
}

if($url == "http://ilde.upf.edu/ou/" || $url == "http://ilde.upf.edu/uoc/") {
   /*OU*/if(!strcmp("$patternsname","Jigsaw")) {$lhref="{$url}pg/lds/view/767/";$t_text = "If groups of students face resolution of a complex problem/task that can be easily divided into sections or independent sub-problems, an adequate collaborative learning flow may be planned.";}
   /*OU*/if(!strcmp("$patternsname","Design narrative")) {$lhref="{$url}pg/lds/view/763/";$t_text = "At the UVA team, teacher workshops were held to promote learning design and implementation of collaborative learning activities. Following the most widely used format and technologies at our institution, the idea was to teach/practice design for blended learning (i.e. combining face-to-face and online activities) courses, and using Moodle VLEs as the main technological support (alongside other web tools). ";}

    if(!strcmp("$patternsname","Controlled Group Formation")) {$lhref="{$url}pg/lds/view/645/";$t_text = "How can a group of students be formed when they are asked to work on an assignment to collaborative build knowledge?";}
    if(!strcmp("$patternsname","Free G F")) {$lhref="{$url}pg/lds/view/677/";$t_text = "How can a group of students be formed when they are asked to work on a large demanding assignment?";}
    if(!strcmp("$patternsname","Facilitator")) {$lhref="{$url}pg/lds/view/673/";$t_text = "Students might be guided towards greater independence (autonomous learning) in collaborative learning situations and, at the same time, towards effective collaboration.";}
    if(!strcmp("$patternsname","Guiding Questions")) {$lhref="{$url}pg/lds/view/681/";$t_text = "A group of students that collaboratively perform a learning task are not sure on the criteria for deciding whether they have completed it or whether it fulfils the expected results.";}
    if(!strcmp("$patternsname","Management of On-Line Questionnaires")) {$lhref="{$url}pg/lds/view/693/";$t_text = "How can web-based questionnaires be created, delivered and graded?";}
    if(!strcmp("$patternsname","Structured Space for Group Task")) {$lhref="{$url}pg/lds/view/717/";$t_text = "Sometimes students require an online space that facilitates their collaboration.";}
    if(!strcmp("$patternsname","The Assessment Task As A Vehicle For Learning")) {$lhref="{$url}pg/lds/view/729/";$t_text = "Assessment regimes which prioritize technical measurement issues, such as validity and reliability, may ignore the effects of the test on students’ approaches to learning. On the other hand, we do need to assess students’ work, and our approaches must be fair and  reasonable.";}
    if(!strcmp("$patternsname","Enriching Discussions by Generating Cognitive Conflicts")) {$lhref="{$url}pg/lds/view/665/";$t_text = "Sometimes students are reluctant to challenge each other’s different views on a particular subject or the results from a particular activity during a discussion.";}
    if(!strcmp("$patternsname","Preparing Fruitful Discussions Using Surveys")) {$lhref="{$url}pg/lds/view/705/";$t_text = "The exploration of contradictory views in a discussion can promote a deeper understanding of a subject. It can stimulate each participant to develop their own opinions and explore their reason for them.";}
    if(!strcmp("$patternsname","Discussion Group")) {$lhref="{$url}pg/lds/view/661/";$t_text = "Discussion groups are the most common way of organizing activity in networked learning environments. The degree to which a discussion is structured, and the choice of structure, is key in determining how successfully the discussion will promote learning for the participants.";}
    if(!strcmp("$patternsname","Introductory Activity Learning Design Awareness")) {$lhref="{$url}pg/lds/view/685/";$t_text = "Students may be aware of the collaborative learning process that they will perform so that their learning is potentially meaningful and so that positive interdependence among the members of the groups is encouraged. This pattern discusses how this might be accomplished. ";}
    if(!strcmp("$patternsname","Enriching The Learning Process")) {$lhref="{$url}pg/lds/view/669/";$t_text = "How can the learning process be designed so that the (group of) students that perform some activities at faster rates can employ the time till the rest of the group finish (note that in collaborative learning synchronization of group activities is a key issue) to escalate the level and quality of the learning experiences?";}
    if(!strcmp("$patternsname","Thinking Aloud Pair Problem Solving")) {$lhref="{$url}pg/lds/view/733/";$t_text = "If students face a series of problems whose solutions imply reasoning processes, an adequatecollaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Simulation")) {$lhref="{$url}pg/lds/view/713/";$t_text = "If groups of students face a problem whose resolution implies the simulation of a situation in which several characters are involved, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Brainstorming")) {$lhref="{$url}pg/lds/view/637/";$t_text = "If groups of students face the resolution of a problem whose solution requires the generation of a large number of possible answers/ideas in a short period of time, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","TPS")) {$lhref="{$url}pg/lds/view/745/";$t_text = "If groups of students face resolution of a challenging or open-ended question, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Pyramid")) {$lhref="{$url}pg/lds/view/709/";$t_text = "If groups of students face resolution of a complex problem/task, usually without a concrete solution, whose resolution implies the achievement of gradual consensus among all the students, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Mapping Forces")) {$lhref="{$url}pg/lds/view/697/";$t_text = "Design challenges are complex and intertwined. Many argue they are fundementally \"Wicked problems\" (Rittel, 1973). The first challenge a designer faces is identifying the design challenge: understanding what are the forces that define the context for which she designs, and what in the configuration of these forces does she wish to change.";}
    if(!strcmp("$patternsname","Structuredspace For Group Tasks")) {$lhref="{$url}pg/lds/view/717/";$t_text = "Sometimes students require an online space that facilitates their collaboration.";}

    if(!strcmp("$patternsname","Enriching Discussion By Generating Cognitive Conflicts")) {$lhref="{$url}pg/lds/view/665/";$t_text = "Sometimes students are reluctant to challenge each other’s different views on a particular subject or the results from a particular activity during a discussion.";}
    if(!strcmp("$patternsname","Controlled G F")) {$lhref="{$url}pg/lds/view/645/";$t_text = "How can a group of students be formed when they are asked to work on an assignment to collaborative build knowledge?";}
}

if($url == "http://ilde.upf.edu/uoc/") {
    /*UOC*/if(!strcmp("$patternsname","Jigsaw")) {$lhref="{$url}pg/lds/view/776/";$t_text = "If groups of students face resolution of a complex problem/task that can be easily divided into sections or independent sub-problems, an adequate collaborative learning flow may be planned.";}
    /*UOC*/if(!strcmp("$patternsname","Design narrative")) {$lhref="{$url}pg/lds/view/772/";$t_text = "At the UVA team, teacher workshops were held to promote learning design and implementation of collaborative learning activities. Following the most widely used format and technologies at our institution, the idea was to teach/practice design for blended learning (i.e. combining face-to-face and online activities) courses, and using Moodle VLEs as the main technological support (alongside other web tools). ";}
    /*UOC*/if(!strcmp("$patternsname","Enriching Discussion By Generating Cognitive Conflicts")) {$lhref="{$url}pg/lds/view/780/";$t_text = "Sometimes students are reluctant to challenge each other’s different views on a particular subject or the results from a particular activity during a discussion.";}
    /*UOC*/if(!strcmp("$patternsname","Controlled G F")) {$lhref="{$url}pg/lds/view/784/";$t_text = "How can a group of students be formed when they are asked to work on an assignment to collaborative build knowledge?";}
}

if($url == "http://ilde.upf.edu/kek/") {
    /*KEK*/if(!strcmp("$patternsname","Jigsaw")) {$lhref="{$url}pg/lds/view/767/";$t_text = "If groups of students face resolution of a complex problem/task that can be easily divided into sections or independent sub-problems, an adequate collaborative learning flow may be planned.";}
    /*KEK*/if(!strcmp("$patternsname","Design narrative")) {$lhref="{$url}pg/lds/view/763/";$t_text = "At the UVA team, teacher workshops were held to promote learning design and implementation of collaborative learning activities. Following the most widely used format and technologies at our institution, the idea was to teach/practice design for blended learning (i.e. combining face-to-face and online activities) courses, and using Moodle VLEs as the main technological support (alongside other web tools). ";}

    if(!strcmp("$patternsname","Controlled Group Formation")) {$lhref="{$url}pg/lds/view/645/";$t_text = "How can a group of students be formed when they are asked to work on an assignment to collaborative build knowledge?";}
    if(!strcmp("$patternsname","Free G F")) {$lhref="{$url}pg/lds/view/677/";$t_text = "How can a group of students be formed when they are asked to work on a large demanding assignment?";}
    if(!strcmp("$patternsname","Facilitator")) {$lhref="{$url}pg/lds/view/673/";$t_text = "Students might be guided towards greater independence (autonomous learning) in collaborative learning situations and, at the same time, towards effective collaboration.";}
    if(!strcmp("$patternsname","Guiding Questions")) {$lhref="{$url}pg/lds/view/681/";$t_text = "A group of students that collaboratively perform a learning task are not sure on the criteria for deciding whether they have completed it or whether it fulfils the expected results.";}
    if(!strcmp("$patternsname","Management of On-Line Questionnaires")) {$lhref="{$url}pg/lds/view/693/";$t_text = "How can web-based questionnaires be created, delivered and graded?";}
    if(!strcmp("$patternsname","Structured Space for Group Task")) {$lhref="{$url}pg/lds/view/717/";$t_text = "Sometimes students require an online space that facilitates their collaboration.";}
    if(!strcmp("$patternsname","The Assessment Task As A Vehicle For Learning")) {$lhref="{$url}pg/lds/view/729/";$t_text = "Assessment regimes which prioritize technical measurement issues, such as validity and reliability, may ignore the effects of the test on students’ approaches to learning. On the other hand, we do need to assess students’ work, and our approaches must be fair and  reasonable.";}
    if(!strcmp("$patternsname","Enriching Discussions by Generating Cognitive Conflicts")) {$lhref="{$url}pg/lds/view/665/";$t_text = "Sometimes students are reluctant to challenge each other’s different views on a particular subject or the results from a particular activity during a discussion.";}
    if(!strcmp("$patternsname","Preparing Fruitful Discussions Using Surveys")) {$lhref="{$url}pg/lds/view/705/";$t_text = "The exploration of contradictory views in a discussion can promote a deeper understanding of a subject. It can stimulate each participant to develop their own opinions and explore their reason for them.";}
    if(!strcmp("$patternsname","Discussion Group")) {$lhref="{$url}pg/lds/view/661/";$t_text = "Discussion groups are the most common way of organizing activity in networked learning environments. The degree to which a discussion is structured, and the choice of structure, is key in determining how successfully the discussion will promote learning for the participants.";}
    if(!strcmp("$patternsname","Introductory Activity Learning Design Awareness")) {$lhref="{$url}pg/lds/view/685/";$t_text = "Students may be aware of the collaborative learning process that they will perform so that their learning is potentially meaningful and so that positive interdependence among the members of the groups is encouraged. This pattern discusses how this might be accomplished. ";}
    if(!strcmp("$patternsname","Enriching The Learning Process")) {$lhref="{$url}pg/lds/view/669/";$t_text = "How can the learning process be designed so that the (group of) students that perform some activities at faster rates can employ the time till the rest of the group finish (note that in collaborative learning synchronization of group activities is a key issue) to escalate the level and quality of the learning experiences?";}
    if(!strcmp("$patternsname","Thinking Aloud Pair Problem Solving")) {$lhref="{$url}pg/lds/view/733/";$t_text = "If students face a series of problems whose solutions imply reasoning processes, an adequatecollaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Simulation")) {$lhref="{$url}pg/lds/view/713/";$t_text = "If groups of students face a problem whose resolution implies the simulation of a situation in which several characters are involved, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Brainstorming")) {$lhref="{$url}pg/lds/view/637/";$t_text = "If groups of students face the resolution of a problem whose solution requires the generation of a large number of possible answers/ideas in a short period of time, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","TPS")) {$lhref="{$url}pg/lds/view/745/";$t_text = "If groups of students face resolution of a challenging or open-ended question, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Pyramid")) {$lhref="{$url}pg/lds/view/709/";$t_text = "If groups of students face resolution of a complex problem/task, usually without a concrete solution, whose resolution implies the achievement of gradual consensus among all the students, an adequate collaborative learning flow may be planned.";}
    if(!strcmp("$patternsname","Mapping Forces")) {$lhref="{$url}pg/lds/view/697/";$t_text = "Design challenges are complex and intertwined. Many argue they are fundementally \"Wicked problems\" (Rittel, 1973). The first challenge a designer faces is identifying the design challenge: understanding what are the forces that define the context for which she designs, and what in the configuration of these forces does she wish to change.";}
    if(!strcmp("$patternsname","Structuredspace For Group Tasks")) {$lhref="{$url}pg/lds/view/717/";$t_text = "Sometimes students require an online space that facilitates their collaboration.";}
}

            $tooltip_id = 't_'.str_replace(' ','_', strtolower($patternsname));

            ?>
            <?php if (!strlen($lhref)): ?>
     <div id="pattern_result"  align="<?php echo $aling ?>"><FONT SIZE= <?php echo $peso?> color="#800000"><?php echo $patternsname ?></FONT>
       <p> <!--<img align="center" src="<?php echo $vars['url']; ?>mod/lds/images/<?php echo $patterns?>.gif"   width='120' height='70'  tittle="<?php echo $patterns ?>" /> --></p>
    </div>
            <?php endif;?>

            <?php if (strlen($lhref)): ?>
            <div id="pattern_result" align="<?php echo (strlen($patternsname) < 25) ? $aling : "center"?>"><FONT SIZE= <?php echo $peso?> color="#800000"><span  class="show_tooltip <?php echo $tooltip_id ?>"><a href="<?php echo $lhref;?>"><?php echo $patternsname ?></a></span></FONT>
                <p> <!--<img align="center" src="<?php echo $vars['url']; ?>mod/lds/images/<?php echo $patterns?>.gif"   width='120' height='70'  tittle="<?php echo $patterns ?>" /> --></p>
            </div>
            <div class="tooltip_bl" id="<?php echo $tooltip_id ?>" style="width: 280px;">
                <div class="tooltip_bl_stem"></div>
                <div class="tooltip_bl_body"><?php echo $t_text ?></div>
            </div>

        <?php endif;?>

    
	<?php endfor;?>
</ul>
<?php else: ?>
	<p class="noresults"><?php echo T("Oops, no results here!") ?></p>
<?php endif; ?>