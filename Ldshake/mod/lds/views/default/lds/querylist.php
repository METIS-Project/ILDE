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
<ul id="wordnet_list">
    
     
    
	<?php 
        $contWord_aux = $vars['list'];
        $peso = 7;
        $arrayPesos = array();
        for ($i = 0; $i <= 7; ++$i) {

            $value = array_shift($vars['list']);

            $patron = array_search($value, $contWord_aux);
            
            $arrayPesos[$patron] = $peso;
            $contWord_aux = $vars['list'];

            while (in_array($value, $vars['list'])) {

                $valueA = array_shift($vars['list']);

                $patron = array_search($valueA, $contWord_aux);
                //echo $patron;
                //echo $contWord[$patron]. "<br>";
                $contWord_aux = $vars['list'];
                $arrayPesos[$patron] = $peso;
                // unset($contWord[$patron]);   
                //array_values($contWord);
            }
            --$peso;
        }
        $cont=0;
        for($i=0; $i<=count($arrayPesos)+1; ++$i):
                 $listaux=$arrayPesos;
                 $aux=array_shift($arrayPesos);
                 $patterns=array_search($aux, $listaux);
                 $peso=(int)$aux;
                 ++$cont;
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
    <div id="pattern_result"  align=<?php echo $aling ?> ><FONT SIZE= <?php echo $peso?> color="#800000"><?php echo $patterns ?></FONT> 
       <p <img align="center" src="<?php echo $vars['url']; ?>mod/lds/images/<?php echo $patterns?>.gif"   width='120' height='70'  tittle="<?php echo $patterns ?>"  /> </p>
    </div>
    
    
	<?php endfor ?>
</ul>
<?php else: ?>
	<p class="noresults"><?php echo T("Oops, no results here!") ?></p>
<?php endif; ?>