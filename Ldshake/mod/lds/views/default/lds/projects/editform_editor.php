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

?><?php
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

<div id="layout_canvas">
	<div id="one_column" style="padding-bottom:0 !important">
		<div id="lds_edit_form" action="<?php echo $url ?>" method="post">
			<input type="hidden" id="lds_edit_guid" name="guid" value="0" />
			<input type="hidden" id="lds_edit_revision" name="revision" value="0" />
			<input type="hidden" id="lds_edit_referer" name="referer" value="<?php echo $referer ?>" />
			<div id="lds_edit_header">
				<input type="text" id="lds_edit_title" name="title" value="" tabindex="1" />
				<div id="lds_edit_share_info">
					<div id="lds_edit_share_info_write">
						<span class="one"><?php echo T("Only you can edit") ?></span>
						<span class="more hidden"><?php echo T("You and <span></span> others can edit") ?></span>
					</div>
					<div id="lds_edit_share_info_read">
						<span class="one hidden"><?php echo T("Only you can read") ?></span>
						<span class="more hidden"><?php echo T("You and <span></span> others can read") ?></span>
						<span class="all"><?php echo T("All LdShakers can read") ?></span>
					</div>
				</div>
				<div id="lds_edit_buttons">
					<input type="button" id="lds_share_button" name="action" value="<?php echo T("Share...") ?>" tabindex="3" />
					<input type="submit" id="lds_edit_save" name="action" value="<?php echo T("Save") ?>" tabindex="4" />
					<input type="submit" id="lds_edit_save_exit_editor" name="action" value="<?php echo T("Save & Exit") ?>" tabindex="5" />
					<?php echo T("or") ?> <a id="lds_edit_exit" href="<?php echo $referer ?>" tabindex="6"><?php echo T("Cancel") ?></a>
				</div>
				<div style="clear: both"></div>
				<div id="lds_edit_middlebar">
					<div class="slider_wrapper show_tooltip t_granularity">
						<div class="lds_slider" id="granularity_slider"></div>
						<input type="text" class="slider_input" id="granularity_input" name="granularity" />
						<label for="granularity"><?php echo T("Granularity") ?>:</label>
					</div>
					<div class="slider_wrapper show_tooltip t_completeness">
						<div class="lds_slider" id="completeness_slider"></div>
						<input type="text" class="slider_input" id="completeness_input" name="completeness" />
						<label for="completeness"><?php echo T("Completeness") ?>:</label>
					</div>
					<div id="lds_edit_tags">
						<?php echo T("Tags") ?>: <span class="tooltip"><?php echo T("Click here to add tags to the LdS") ?></span><span id="lds_edit_tags_list"></span>
					</div>
				</div>
            </div>
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
                                <div class="draggable ui-widget-content" toolname="Design Pattern" tooltype='doc' subtype="design_pattern">
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
			<br>

            <!-- Hidden stuff -->
			<div id="shade" style="display:block;"></div>
			
			<div id="lds_loading_contents"><?php echo T("Loading...") ?></div>
						
			<div id="lds_edit_tags_popup">
				<a id="lds_edit_tags_popup_close" href="#"><?php echo T("Done") ?></a>
				<div id="lds_edit_tags_popup_header"><?php echo T("Edit LdS tags") ?></div>
				<div class="lds_edit_tags_popup_section section_discipline">
					<div class="lds_edit_tags_popup_fieldname"><?php echo T("Discipline") ?>:</div>
					<input type="text" id="lds_edit_tags_input_discipline" name="discipline" value="" />
				</div>
				<div class="lds_edit_tags_popup_section section_pedagogical_approach">
					<div class="lds_edit_tags_popup_fieldname"><?php echo T("Pedagogical approach") ?>:</div>
					<input type="text" id="lds_edit_tags_input_pedagogical_approach" name="pedagogical_approach" value="" />
				</div>
				<div class="lds_edit_tags_popup_section section_tags">
					<div class="lds_edit_tags_popup_fieldname"><?php echo T("Free tags") ?>:</div>
					<input type="text" id="lds_edit_tags_input_tags" name="tags" value="" />
				</div>
			</div>
			
			<div class="tooltip_bl" id="t_granularity" style="width: 280px;">
				<div class="tooltip_bl_stem"></div>
				<div class="tooltip_bl_body">
					<h4><?php echo T("Granularity") ?></h4>
					<div>
						<p><strong><?php echo T("Which is the grain size of this activity?") ?></strong></p>
						<p><?php echo T("A Study Program has a large grain size, while a picture to be used within an activity has a small grain size.") ?></p>
						<p><strong><?php echo T("Guidance values") ?>:</strong></p>
						<ol start="0">
							<li><?php echo T("[Do not know, no answer]") ?></li>
							<li><?php echo T("Program") ?></li>
							<li><?php echo T("Module") ?></li>
							<li><?php echo T("Course") ?></li>
							<li><?php echo T("Group of activities") ?></li>
							<li><?php echo T("Activity") ?></li>
							<li><?php echo T("Tool (to be used within an activity)") ?></li>
							<li><?php echo T("Document (to be used within an activity)") ?></li>
							<li><?php echo T("Resource (image, table, schema, picture…)") ?></li>
						</ol>
					</div>
				</div>
			</div>
			
			<div class="tooltip_bl" id="t_completeness" style="width: 280px;">
				<div class="tooltip_bl_stem"></div>
				<div class="tooltip_bl_body">
					<h4><?php echo T("Completeness") ?></h4>
					<div>
						<p><strong><?php echo T("How ready to use is this activity?") ?></strong></p>
						<p><?php echo T("A fully complete didactic unit is an exemplar ready to use, while the structure of a group activity dynamics is an incomplete chunck which needs inputs to be used.") ?></p>
						<p><strong><?php echo T("Guidance values") ?>:</strong></p>
						<ol start="0">
							<li><?php echo T("[Do not know, no answer]") ?></li>
							<li><?php echo T("Exemplar (fully ready to use)") ?></li>
							<li><?php echo T("Chunk (a portion of an exemplar)") ?></li>
							<li><?php echo T("Template (an incomplete exemplar)") ?></li>
							<li><?php echo T("Building block (an incomplete chunk)") ?></li>
							<li><?php echo T("Almost from scratch") ?></li>
						</ol>
					</div>
				</div>
			</div>
			
			<?php include (__DIR__.'/../single_share_form.php') ?>
		</div>
	</div>

    <iframe id="upload_result" name="upload_result_name"></iframe>

</div>

<div class="clearfloat"></div>
</div><!-- /#page_wrapper -->
</div><!-- /#page_container -->
	<script type="text/javascript">
        var t9n = {
            supportTitle: "<?php echo T("Support Document") ?>",
            suggestEnterTags : "<?php echo T("Enter tags here") ?>",
            newDocTitle : "<?php echo T("Please write the new document's title:") ?>",
            newDocTitleEmpty : "<?php echo T("Oops! The document title cannot be empty.") ?>",
            newDocTitleRepeated : "<?php echo T("Oops! There is already one document with this title in the LdS.") ?>",
            docSetTitle : "<?php echo T("Please write a new title for the document:") ?>",
            docConfirmDelete : "<?php echo T("Are you sure you want to delete the following document?") ?>",
            untitledDoc : "<?php echo T("Untitled Document") ?>",
            untitledLdS : "<?php echo T("Untitled LdS") ?>",
            confirmExit : "<?php echo T("You have unsaved data. Are you sure you want to leave the editor?") ?>"
        };

		var initLdS = <?php echo $initLdS ?>;
        var new_lds = (initLdS.guid == '0') ? true : false;
		var documents = "2";
		var editorType = '<?php echo $editor_type ?>';
		var mytags = <?php echo $tags ?>;
		var am_i_starter = <?php echo ($am_i_starter ? 'true' : 'false') ?>;
		var friends = new Array();
		var groups = <?php echo $groups ?>;

        var implementation = false;
        var upload = <?php echo (isset($upload) ? 'true' : 'false')?>;
        var restapi = <?php echo ($vars['restapi'] ? 'true' : 'false')?>;
        <?php if($vars['restapi']): ?>
        var restapi_remote_domain = "<?php echo $vars['restapi_remote_domain']; ?>";
        <?php endif; ?>
        var google_docs = <?php echo (($editor == 'google_docs') ? 'true' : 'false')?>;
        <?php if($editor == 'google_docs'): ?>
        var google_docs_support_id = "<?php echo $support_editor['editor_id']; ?>";
        <?php endif; ?>
        var ilde_debug = <?php echo ($CONFIG->editor_debug ? 'true' : 'false')?>;

        var ldproject_view_only = true;
        var ldproject = <?php echo $jsondata ?>;
        var lds_list = <?php echo json_encode($list, true) ?>;
        var vle_list = <?php echo json_encode($vle_list, true) ?>;


        friends['available'] = <?php echo $jsonfriends ?>;
		friends['viewers'] = <?php echo $viewers ?>;
		friends['editors'] = <?php echo $editors ?>;

		//Is the LdS public for all LdShakers? (yes by default)
		var allCanView = <?php echo $all_can_read ?>;
	</script>
	<?php echo elgg_view('page_elements/jsarea', $vars); ?>
</body>
</html>