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
            <h3><?php echo T("Tools") ?></h3>
            <ul class="tag_selector">
                <?php foreach ($editor_subtype as $k=>$v): ?>
                    <li>
                        <a class="lds_tag <?php echo $classname ?>" href="<?php echo $url ?>pg/lds/browse/?tagk=editor_subtype&tagv=<?php echo urlencode($k) ?>"><?php echo $v ?></a>
                    </li>
                <?php endforeach; ?>
                <?php foreach ($editor_type as $k=>$v): ?>
                    <li>
                        <a class="lds_tag <?php echo $classname ?>" href="<?php echo $url ?>pg/lds/browse/?tagk=editor_type&tagv=<?php echo urlencode($k) ?>"><?php echo $v ?></a>
                    </li>
                <?php endforeach; ?>

            </ul>
            <h3><a href="<?php echo $url.'pg/lds/patterns'?>" style="color:black;"><?php echo T("Search patterns") ?></a></h3>
            <br />
            <h3><a href="<?php echo $url.'pg/lds/repository'?>" style="color:black;"><?php echo T("External repositories") ?></a></h3>
            <br />

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
                    <ul class="tag_selector <?php echo $classname?>">
                        <?php
                        $tag_offset = 0;
                        $tag_length = 10;
                        $used_tags = array_slice($tagclass, $tag_offset, $tag_length, true);
                        ?>

                        <?php foreach ($used_tags as $u_t): ?>
                            <li>
                                <span class="freq"><?php echo $u_t->frequency ?></span>
                                <a class="lds_tag <?php echo $classname ?>" href="<?php echo $url ?>pg/lds/browse/?tagk=<?php echo urlencode($classname) ?>&tagv=<?php echo urlencode($u_t->tag) ?>"><?php echo $u_t->tag ?></a>
                            </li>
                        <?php endforeach; ?>

                        <?php
                        $non_used_tags = array_slice($tagclass, $tag_length, 99, true);
                        ?>
                        <?php if(count($tagclass) >= $tag_length): ?>
                            <?php foreach ($non_used_tags as $u_t): ?>
                                <li class="lds-browse-non-used-tags lds-browse-non-used-tags-hide">
                                    <span class="freq"><?php echo $u_t->frequency ?></span>
                                    <a class="lds_tag <?php echo $classname ?>" href="<?php echo $url ?>pg/lds/browse/?tagk=<?php echo urlencode($classname) ?>&tagv=<?php echo urlencode($u_t->tag) ?>"><?php echo $u_t->tag ?></a>
                                </li>
                            <?php endforeach; ?>
                        <li>
                            <a class="lds-browse-show-tags <?php echo $classname ?>" category="<?php echo $classname ?>"><?php echo T('Show more tags') ?></a>
                        </li>
                        <?php endif; ?>
                    </ul>
                <?php endforeach; ?>
            <?php endif; ?>
		</div>
	</div>
	<div id="owner_block_bottom"></div>
</div>

<div id="two_column_left_sidebar_maincontent">
	<div id="content_area_user_title">
		<?php if ($filtering): ?>
            <?php
            if(isset($editor_type[$tagv])) $tagv = $editor_type[$tagv];
            if(isset($editor_subtype[$tagv])) $tagv = $editor_subtype[$tagv];
            ?>
		<h2><a href="<?php echo lds_viewTools::getUrl() ?>"><?php echo T("All LdS") ?></a> » <span class="lds_tag <?php echo $tagk ?>"><?php echo $tagv ?></span></h2>
		<?php else: ?>
		<h2><?php echo T("All LdS") ?></h2>
		<?php endif; ?>
	</div>
	
	<div class="filters">
		<div class="paging">
			<?php echo lds_viewTools::pagination($count) ?>
		</div>
        <div class="lds_order_by">
        <?php
        if($order == "time")
            echo T("sort by").' '.'<b>'.T("newest").'</b> / '.'<a href="'.lds_viewTools::getUrl()."?order=title&tagk={$tagk}&tagv={$tagv}".'">'.T("title").'</a>';
        else
            echo T("sort by").' '.'<a href="'.lds_viewTools::getUrl()."?order=time&tagk={$tagk}&tagv={$tagv}".'">'.T("newest").'</a> / <b>'.T("title").'</b>';
        ?>
        </div>
	</div>
	
	<?php echo elgg_view('lds/browselist', $vars) ?>
	
	<div class="filters">
		<div class="paging">
			<?php echo lds_viewTools::pagination($count) ?>
		</div>
	</div>
</div>

<script>
    var t9n = {
        showTags : "<?php echo T('Show more tags') ?>",
        hideTags : "<?php echo T('Show less tags') ?>"
    }
</script>