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
<div id="two_column_left_sidebar">
	<div id="owner_block">
		<div id="left_filters">
			<?php
			if (is_array($tags)):
			foreach ($tags as $classname=>$tagclass):
			?>
			<?php if ($classname == 'tags'): ?>
			<h3><?php echo T("Free tags") ?></h3>
			<?php elseif ($classname == 'discipline'): ?>
			<h3><?php echo T("Discipline") ?></h3>
			<?php elseif ($classname == 'pedagogical_approach'): ?>
			<h3><?php echo T("Pedagogical approach") ?></h3>
			<?php endif; ?>			
			<ul class="tag_selector">
				<?php foreach ($tagclass as $k=>$v): ?>
				<li>
					<span class="freq"><?php echo $v ?></span>
					<a class="lds_tag <?php echo $classname ?>" href="<?php echo $url ?>pg/lds/browse/?tagk=<?php echo urlencode($classname) ?>&tagv=<?php echo urlencode($k) ?>"><?php echo $k ?></a>
				</li>
				<?php endforeach; ?>
			</ul>
			<?php
			endforeach;
			endif;
			?>
		</div>
	</div>
	<div id="owner_block_bottom"></div>
</div>

<div id="two_column_left_sidebar_maincontent">
	<div id="content_area_user_title">
		<?php if ($filtering): ?>
		<h2><a href="<?php echo lds_viewTools::getUrl() ?>"><?php echo T("All LdS") ?></a> » <span class="lds_tag <?php echo $tagk ?>"><?php echo $tagv ?></span></h2>
		<?php else: ?>
		<h2><?php echo T("All LdS") ?></h2>
		<?php endif; ?>
	</div>
	
	<div class="filters">
		<div class="paging">
			<?php echo lds_viewTools::pagination($count) ?>
		</div>
	</div>
	
	<?php echo elgg_view('lds/browselist', $vars) ?>
	
	<div class="filters">
		<div class="paging">
			<?php echo lds_viewTools::pagination($count) ?>
		</div>
	</div>
</div>