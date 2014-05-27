<?php extract($vars); ?>
<div id="lds_editor_iframe">

<div id="ldproject_conceptualize_grid"> <!-- This will act as design container -->
    <div id="two_column" style="padding-bottom:0 !important">
        <div id="droppable_grid" class="ui-widget-header" type="conceptualize">
            <p>Project Design Conceptualize </p>
        </div>
        <div id="ldproject_toolBar">
            <div class="draggable ui-widget-content" title="Design Pattern">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/DP.png" toolname="Design Pattern" tooltype='doc' subtype="design_pattern" >
            </div>
            <div class="draggable ui-widget-content" title="CourseMap">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/CM.png" toolname="CourseMap" tooltype="doc" subtype="coursemap" >
            </div>
            <div class="draggable ui-widget-content" title="Design Narrative">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/DN.png" toolname="Design Narrative" tooltype="doc" subtype="MDN" >
            </div>
            <div class="draggable ui-widget-content" title="Persona Card">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/PC.png" toolname="Persona Card" tooltype="doc" subtype="PC" >
            </div>
            <div class="draggable ui-widget-content" title="Factors and Concerns">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/FAC.png" toolname="Factors and Concerns" tooltype="doc" subtype="FAC" >
            </div>
            <div class="draggable ui-widget-content" title="Heuristic Evaluation">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/HE.png" toolname="Heuristic Evaluation" tooltype="doc" subtype="HE" >
            </div>
            <div class="draggable ui-widget-content" title="CompendiumLD">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/CLD.png" toolname="CompendiumLD" tooltype="cld" >
            </div>
            <div class="draggable ui-widget-content" title="Image">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/image.png" toolname="Image" tooltype="image" >
            </div>
            <div class="draggable ui-widget-content" title="WebCollage">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/COLLAGE.png" toolname="WebCollage" tooltype="webcollagerest" >
            </div>
            <div class="draggable ui-widget-content" title="Open GLM">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/OGLM.png" toolname="OpenGLM" tooltype="openglm" >
            </div>
            <div class="draggable ui-widget-content" title="CADMOS">
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/CADMOS.png" toolname="CADMOS" tooltype="cadmos" >
            </div>
        </div>
    </div>
    <div style="clear:both"></div>
</div>

<div id="lds_attachment_popup" class="lds_att_popup">
</div>

<script type="text/javascript">

    /*  Here my Vars in order to send it to JSON --> */
    var ldsToBeListed =<?php echo $ldsToBeListed?>;
    var ldproject =<?php echo $ldproject?>;
    var totalToLoad = ldproject.length;
    var toolLoaded = 0;
    var $fmr = document.getElementById("droppable_grid").targetTop;

</script>
</div>