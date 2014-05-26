<?php extract($vars); ?>
<div id="lds_editor_iframe">
<style>
    #droppable_grid { width: 690px; height: 616px; padding: 0.5em; float: left; background-color: white !important; background-image: none; position: relative; z-index:0}
    #ldproject_toolBar { position: relative; width: 250px; height: 700px; float:left; border-style: solid; border-color: black; background-color: #d3d3d3 !important; background-image: none;}
    .draggable, .draggable-nonvalid { width: 90px; height: 90px; position: relative; float: left; margin: 10px 10px 10px 0; background-color: rgb(157,31,94); color: white !important; z-index: 10}
    .ui-widget-content {background-image: none; }

    .ui-widget-header {background-image: none; background-color: green !important; border-color: black}

    .lds_att_popup {
        top: 100px;
        left: 50%;
        z-index: 100000;
        position: fixed;
        background-color: #fff;
        padding: 10px;
        border: 10px solid rgba(120,120,120,0.42);
        -moz-border-radius: 5px 5px 5px 5px;
        -webkit-border-radius: 5px 5px 5px 5px;
        border-radius: 5px 5px 5px 5px;
        -webkit-background-clip: padding-box;
        -moz-background-clip: padding;
        background-clip: padding-box;
        display: none;
        font-size: 13px;
    }

    #lds_attachment_popup {
        width: 500px;
        top: 150px;
        left: 50%;
        margin-left: -260px;
    }

</style>

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
                <img src="<?php echo $vars['url']; ?>mod/ldprojects/images/COLLAGE.png" toolname="WebCollage" tooltype="webcollage" >
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

<script type="text/javascript">

    /*  Here my Vars in order to send it to JSON --> */
    var ldsToBeListed =<?php echo $ldsToBeListed?>;
    var ldproject =<?php echo $ldproject?>;
    var totalToLoad = ldproject.length;
    var toolLoaded = 0;
    var $fmr = document.getElementById("droppable_grid").targetTop;

</script>
</div>