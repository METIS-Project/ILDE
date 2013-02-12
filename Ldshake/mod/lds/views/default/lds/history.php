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
		<div id="content_area_user_title">
			<h2><?php echo $lds->title ?>: <?php echo T("Revision history") ?></h2>
		</div>
		<div id="lds_view_actions">
			<input type="hidden" id="lds_edit_guid" name="guid" value="<?php echo $lds->guid ?>" />
			<input type="hidden" id="lds_base_url" name="guid" value="<?php echo $url ?>pg/lds/" />
			<?php if (!lds_contTools::isLockedBy($lds->guid)): ?>
				<?php if ($lds->canEdit()): ?>
				<a class="leftbutton" href="<?php echo lds_viewTools::url_for($lds, 'edit') ?>"><?php echo T("Edit this LdS") ?></a>
				<?php endif; ?>
			<?php else: ?>
			<span><?php echo T("%1 is editing this LdS", lds_contTools::isLockedBy($lds->guid)->name) ?></span>
			<?php endif; ?>
			<a class="leftbutton" href="<?php echo lds_viewTools::url_for($lds, 'view') ?>"><?php echo T("View this LdS") ?></a>
		</div>
		<ul id="revision_graph_titles">
			<?php foreach ($ldsDocs as $doc): ?>
			<li><?php echo $doc->title ?></li>
			<?php endforeach; ?>
		</ul>
		<div id="revision_graph_wrapper">
			<canvas id="revision_graph" width="<?php echo (160 + 70 * $numDocs) ?>" height="<?php echo (100 * $numRevisions)?>">  
				<div class="browseralert">Your browser does not support HTML5 canvas.<br />Upgrade to a newer browser (e.g. <a href="http://www.firefox.com" target="_blank">Firefox</a>, <a href="http://www.opera.com" target="_blank">Opera</a>, <a href="http://www.google.com/chrome/" target="_blank">Chrome</a>, <a href="http://www.beautyoftheweb.com/" target="_blank">Internet Explorer 9</a>) for a better browsing experience.</div>

				<ul id="iefallback_list">
					<?php
					foreach ($history as $k=>$rev):
						$lastRev = $k == count($history) - 1; 
					?>
					<li>
						<div class="iefallback_revpill">
							<a class="icon" href="<?php echo $rev->author->getUrl() ?>"><img src="<?php echo $rev->author->getIcon('small') ?>" /></a>
							<div><?php echo T("Revision %1", $rev->revision_number) ?></div>
							<div><?php echo $rev->author->username ?></div>
							<div><?php echo date('j M y H:i', $rev->revision->time_created) ?></div>
						</div>
						<?php
						foreach ($docList as &$doc):
							if ($doc->lds_revision_id == $rev->revision->id):
								if ($lastRev):
									if ($doc->ballDrawn):
										echo '<a href="'. $url.'pg/lds/viewrevision/'.$doc->guid.'/"><div class="iefallback_box head lu">●</div></a>';
									else:
										echo '<a href="'. $url.'pg/lds/viewrevision/'.$doc->guid.'/"><div class="iefallback_box head l">●</div></a>';
									endif;
								else:
									if ($doc->ballDrawn):
										echo '<a href="'. $url.'pg/lds/viewrevision/'.$doc->guid.'/"><div class="iefallback_box head lud">●</div></a>';
									else:
										echo '<a href="'. $url.'pg/lds/viewrevision/'.$doc->guid.'/"><div class="iefallback_box head ld">●</div></a>';
									endif;
								endif;
								$doc->ballDrawn = true;
							else:
								$draw_blank = true;
								if (is_array($rev->revised_documents)):
									foreach ($rev->revised_documents as $rdoc):
										if ($rdoc->document_guid == $doc->guid):
											$draw_blank = false;
											if ($doc->ballDrawn):
												echo '<a href="'. $url.'pg/lds/viewrevision/'.$rdoc->guid.'/"><div class="iefallback_box rev lud">●</div></a>';
											else:
												echo '<a href="'. $url.'pg/lds/viewrevision/'.$rdoc->guid.'/"><div class="iefallback_box rev ld">●</div></a>';
											endif;
											$doc->ballDrawn = true;
										endif;
									endforeach;
								endif;
								if (is_array($rev->deleted_documents)):
									foreach ($rev->deleted_documents as $ddoc):
										if ($ddoc == $doc->guid):
											$draw_blank = false;
											if ($doc->ballDrawn):
												echo '<a href="'. $url.'"><div class="iefallback_box del lu">●</div></a>';
											else:
												echo '<a href="'. $url.'"><div class="iefallback_box del l">●</div></a>';
											endif;
											$doc->ballDrawn = false;
										endif;
									endforeach;
								endif;
								if ($lastRev):
									if ($doc->enabled == 'yes'):
										$draw_blank = false;
										if ($doc->ballDrawn):
											echo '<a href="'. $url.'pg/lds/view/'.$lds->guid.'/doc/'.$doc->guid.'/"><div class="iefallback_box ext lu">●</div></a>';
										else:
											echo '<a href="'. $url.'pg/lds/view/'.$lds->guid.'/doc/'.$doc->guid.'/"><div class="iefallback_box ext l">●</div></a>';
										endif;
									endif;
								endif;
								if ($draw_blank):
									if ($doc->ballDrawn):
										echo '<div class="iefallback_box lud"></div>';
									else:
										echo '<div class="iefallback_box l"></div>';
									endif;
								endif;
							endif;
						endforeach;
						?>
						<div style="clear:both"></div>
					</li>
					<?php endforeach; ?>
				</ul>
			</canvas>
			
			<div id="revision_graph_avatars">
				<?php foreach ($history as $rev): ?>
				<img id="revision_avatar_<?php echo $rev->revision->id ?>" src="<?php echo $rev->author->getIcon('small') ?>" />
				<?php endforeach; ?>
			</div>
		</div>
	</div>
</div>
<div class="clearfloat"></div>
</div><!-- /#page_wrapper -->
</div><!-- /#page_container -->
	<script type="text/javascript">
		var lds_id = <?php echo $lds->guid ?>;
		var rev_history = <?php echo $JSONhistory ?>;
		var doc_list = <?php echo $JSONdocList ?>;
	</script>
	<?php echo elgg_view('page_elements/jsarea', $vars); ?>
</body>
</html>