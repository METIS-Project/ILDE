<?php extract($vars); ?>
<div id="lds_editor_iframe">

<div id="ldproject_conceptualize_grid"> <!-- This will act as design container -->
    <div id="two_column" style="padding-bottom:0 !important">
        <div id="droppable_grid" class="" type="conceptualize">
            <p>Project Design Conceptualize </p>
        </div>
        <div id="ldproject_toolBar">
            <div class="draggable" title="Design Pattern">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/DP.png" toolname="Design Pattern" tooltype='doc' subtype="design_pattern" >
            </div>
            <div class="draggable" title="CourseMap">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/CM.png" toolname="CourseMap" tooltype="doc" subtype="coursemap" >
            </div>
            <div class="draggable" title="Design Narrative">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/DN.png" toolname="Design Narrative" tooltype="doc" subtype="MDN" >
            </div>
            <div class="draggable" title="Persona Card">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/PC.png" toolname="Persona Card" tooltype="doc" subtype="PC" >
            </div>
            <div class="draggable" title="Factors and Concerns">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/FAC.png" toolname="Factors and Concerns" tooltype="doc" subtype="FC" >
            </div>
            <div class="draggable" title="Heuristic Evaluation">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/HE.png" toolname="Heuristic Evaluation" tooltype="doc" subtype="HE" >
            </div>
            <div class="draggable" title="CompendiumLD">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/CLD.png" toolname="CompendiumLD" tooltype="cld" >
            </div>
            <div class="draggable" title="Image">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/image.png" toolname="Image" tooltype="image" >
            </div>
            <div class="draggable" title="WebCollage">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/COLLAGE.png" toolname="WebCollage" tooltype="webcollagerest" >
            </div>
            <div class="draggable" title="Open GLM">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/OGLM.png" toolname="OpenGLM" tooltype="openglm" >
            </div>
            <div class="draggable" title="CADMOS">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/CADMOS.png" toolname="CADMOS" tooltype="cadmos" >
            </div>
            <div class="draggable" title="eXeLearning">
                <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/exe.png" toolname="eXeLearning" tooltype="exelearningrest" >
            </div>
            <?php if(!empty($vars['config']->google_drive)): ?>
                <div class="draggable" title="Google Docs">
                    <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/docs-32.png" toolname="Google Docs" tooltype="google_docs" >
                </div>
                <div class="draggable" title="Google Spreadsheet">
                    <img src="<?php echo $vars['url']; ?>mod/lds/images/projects/spreadsheets-32.png" toolname="Google Spreadsheet" tooltype="google_spreadsheet" >
                </div>
            <?php endif; ?>
        </div>
    </div>
    <div style="clear:both"></div>
</div>

<div id="lds_attachment_popup" class="lds_att_popup">
</div>

<script type="text/javascript">

    /*  Here my Vars in order to send it to JSON --> */
    var is_implementation = <?php echo (!empty($is_implementation) ? 'true' : 'false')?>;
    var ldsToBeListed =<?php echo $ldsToBeListed?>;
    var ldproject =<?php echo $ldproject?>;
    var totalToLoad = ldproject.length;
    var toolLoaded = 0;
    var $fmr = document.getElementById("droppable_grid").targetTop;

</script>
</div>