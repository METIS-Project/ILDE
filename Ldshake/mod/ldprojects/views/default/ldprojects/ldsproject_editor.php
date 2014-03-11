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
extract ($vars);
echo elgg_view('page_elements/header', $vars);
echo elgg_view('messages/list', array('object' => $sysmessages));
?>

<style>
    #droppable { width: 758px; height: 616px; padding: 0.5em; float: left; margin: 10px; }
    .draggable, .draggable-nonvalid { width: 100px; height: 100px; padding: 0.5em; float: left; margin: 10px 10px 10px 0; }
</style>

<div class="clearfloat"></div>
<div id="layout_canvas">
    <div id="lds_edit_form" action="<?php echo $url ?>" method="post">
	    <div id="two_column" style="padding-bottom:0 !important">
            <div id="ldproject_designGrid"> <!-- This will act as design container -->
                <div id="droppable" class="ui-widget-header">
                    <p>Project Design</p>
                </div>
            </div>

            <div id="ldproject_toolBar">
                <div class="draggable ui-widget-content" tooltype="coursemap">
                    <p>CourseMap</p>
                 </div>
                <div class="draggable ui-widget-content" tooltype='doc' tooltype="design_pattern">
                    <p>Design Pattern</p>
                </div>
                <div class="draggable ui-widget-content"  tooltype='doc' subtype="MDN">
                    <p>Design Narrative</p>
                </div>
                <div class="draggable ui-widget-content"  tooltype='doc' subtype="PC">
                    <p>Persona Card</p>
                </div>
                <div class="draggable ui-widget-content"  tooltype='doc' subtype="FC">
                    <p>Factor And Concerns</p>
                </div>
            </div>
        </div>
    </div>
</div> <!-- Layout Canvas ->

<div class="clearfloat"></div>
</div><!-- /#page_wrapper -->
</div><!-- /#page_container -->
	<script type="text/javascript">

        /*  Here my Vars in order to send it to JSON --> */
        var ldproject = new Array();

	</script>
	<?php echo elgg_view('page_elements/jsarea', $vars); ?>
</body>
</html>