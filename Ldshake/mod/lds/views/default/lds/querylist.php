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
   
     <div id="pattern_result"  align="<?php echo $aling ?>"><FONT SIZE= <?php echo $peso?> color="#800000"><?php echo $patternsname ?></FONT>
       <p> <img align="center" src="<?php echo $vars['url']; ?>mod/lds/images/<?php echo $patterns?>.gif"   width='120' height='70'  tittle="<?php echo $patterns ?>" /> </p>
    </div> 
    
	<?php endfor;?>
</ul>
<?php else: ?>
	<p class="noresults"><?php echo T("Oops, no results here!") ?></p>
<?php endif; ?>