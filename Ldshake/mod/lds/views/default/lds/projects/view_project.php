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

<?php
global $CONFIG;
extract ($vars);
echo elgg_view('page_elements/header', $vars);
echo elgg_view('messages/list', array('object' => $sysmessages));
?>

<style>
    #droppable_conteptualize, #droppable_author, #droppable_implement { width: 758px; height: 616px; padding: 0.5em; float: left; background-color: white !important; background-image: none;}
    #lds_edit_contents .draggable, #lds_edit_contents .draggable-nonvalid { width: 50px; height: 50px; padding: 0.5em; float: left; margin: 10px 10px 10px 0; background-color: rgb(157,31,94); color: white !important;}
    #lds_edit_contents .ui-widget-content {background-image: none; }
    #lds_edit_contents #ldproject_toolBar { margin-left: 780px !important; height: 616px;  border-style: solid; border-color: black; background-color: #d3d3d3 !important; background-image: none;}
    #lds_edit_contents #ldproject_conceptualize_grid, #ldproject_author_grid {padding: 0px !important; border-color: black}
    #lds_edit_contents .ui-widget-header {background-image: none; background-color: green !important; border-color: black}

</style>


<div id="lds_edit_contents">

    <div id="tabs">
        <ul>
            <li><a href="#ldproject_conceptualize_grid">Conceptualize</a></li>
            <li><a href="#ldproject_author_grid">Author</a></li>
            <!-- <li><a href="#ldproject_implement_grid">Implement</a></li> -->
        </ul>

        <div id="ldproject_conceptualize_grid"> <!-- This will act as design container -->
            <div id="two_column" style="padding-bottom:0 !important">
                <div id="droppable_conteptualize" class="ui-widget-header" type="conceptualize">
                    <p>Project Design Conceptualize </p>
                </div>
                <div id="ldproject_toolBar" class="ui-widget-header"><p>
                    <div class="draggable ui-widget-content" toolname="Design Pattern" tooltype='doc' tooltype="design_pattern">
                        Design Pattern
                    </div>
                    <div class="draggable ui-widget-content" toolname="CourseMap" tooltype="doc" subtype="coursemap">
                        CourseMap
                    </div>
                    <div class="draggable ui-widget-content"  toolname="Design Narrative" tooltype="doc" subtype="MDN">
                        Design Narrative
                    </div>
                    <div class="draggable ui-widget-content"  toolname="Persona Card" tooltype="doc" subtype="PC">
                        Persona Card
                    </div>
                    <div class="draggable ui-widget-content"  toolname="Factors and Concerns" tooltype="doc" subtype="FC">
                        Factors And Concerns
                    </div>
                    <div class="draggable ui-widget-content"  toolname="Heuristic Evaluation" tooltype="doc" subtype="HE">
                        Heuristic Evaluation
                    </div>
                    <div class="draggable ui-widget-content" toolname="CompendiumLD" tooltype="cld">
                        CompendiumLD
                    </div>
                    <div class="draggable ui-widget-content" toolname="Image" tooltype="image">
                        Image
                    </div>
                    </p></div>
            </div>
        </div>
        <div id="ldproject_author_grid"> <!-- This will act as design container -->
            <div id="two_column" style="padding-bottom:0 !important">
                <div id="droppable_author" class="ui-widget-header" type="author">
                    <p>Project Design Author</p>
                </div>
                <div id="ldproject_toolBar">
                    <div class="draggable ui-widget-content" toolname="WebCollage" tooltype="webcollagerest">
                        WebCollage
                    </div>
                    <div class="draggable ui-widget-content"  toolname="OpenGLM" tooltype="openglm">
                        OpenGLM
                    </div>
                    <div class="draggable ui-widget-content"  toolname="CADMOS" tooltype="cadmos">
                        CADMOS
                    </div>
                </div>
            </div>
        </div>
        <!-- <div id="ldproject_implement_grid">
            <div id="droppable_implement" class="ui-widget-header" type="implement">
                <p>Project Design Impl</p>
            </div>
        </div> -->
    </div>
</div>


<script type="text/javascript">
    var ldproject_view_only = true;
    var ldproject = <?php echo $jsondata ?>;
    var lds_list = <?php echo $list ?>;
</script>
<?php echo elgg_view('page_elements/jsarea', $vars); ?>
</body>
</html>