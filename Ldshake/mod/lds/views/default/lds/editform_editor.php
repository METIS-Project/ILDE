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
<div id="layout_canvas">
	<div id="one_column" style="padding-bottom:0 !important">
		<form id="lds_edit_form" action="<?php echo $url ?>" method="post">
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
                <div id="rich_text_box" style="display: none;">
                <textarea name="body" id="lds_edit_body" tabindex="2"></textarea>
				</div>
                <?php if ($editor == 'exe'): ?>
					<iframe id="lds_editor_body" src="/exelearning/<?php echo $editor_id ?>" width="958" height="616" style="border: 1px solid grey;"></iframe>
				<?php else: ?>
				<?php echo $CONFIG->root ?>
					<iframe id="lds_editor_body" scrolling="no" src="/editors/webcollage/main.php?ldid=<?php echo $editor_id ?>" width="958" height="600"></iframe>
				<?php endif; ?>
					
			</div>
            <div id="lds_edit_tabs" class="scrollable">
                <?php /** Botonets de scroll **/ ?>
                <div class="arrow right" style="top:4px !important">►</div><div class="arrow left" style="top:4px !important">◄</div>
                <ul id="lds_edit_tabs_scrolling" class="content">
                    <li class="lds_newtab">+ <?php echo T("Add document") ?></li>
                    <li class="lds_exetab"> <?php echo T("eXeLearning") ?></li>

                </ul>
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
			
			<?php include ('single_share_form.php') ?>
		</form>
	</div>
</div>

<div class="clearfloat"></div>
</div><!-- /#page_wrapper -->
</div><!-- /#page_container -->
	<script type="text/javascript">
		var t9n = {
			suggestEnterTags : "<?php echo T("Enter tags here") ?>"
		};
		var initLdS = <?php echo $initLdS ?>;
        var new_lds = (initLdS.guid == '0') ? true : false;
		var documents = "2";
		var editorType = '<?php echo $editor ?>';
		var mytags = <?php echo $tags ?>;
		var am_i_starter = <?php echo ($am_i_starter ? 'true' : 'false') ?>;
		var friends = new Array();
		var editor_id = <?php echo $editor_id ?>;
		var groups = <?php echo $groups ?>;
        var documents = <?php echo $initDocuments ?>;
		friends['available'] = <?php echo $jsonfriends ?>;
		friends['viewers'] = <?php echo $viewers ?>;
		friends['editors'] = <?php echo $editors ?>;

		//Is the LdS public for all LdShakers? (yes by default)
		var allCanView = <?php echo $all_can_read ?>;
	</script>
	<?php echo elgg_view('page_elements/jsarea', $vars); ?>
</body>
</html>