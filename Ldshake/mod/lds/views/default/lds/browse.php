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

<?php extract($vars); ?>
<?php
$disable_search_patterns = false;
$disable_external_repository = false;
$tools_term = "Tools";

if(function_exists("ldshake_mode_browselds")) {
    ldshake_mode_browselds($disable_search_patterns, $disable_external_repository, $tools_term);
}

$added_filter = "";
if(function_exists("ldshake_mode_browselds_filters")) {
    $added_filter = ldshake_mode_browselds_filters($filter);
}

if(ldshake_check_sanitize_filter_param($filter)) {
    $filter_param = "&filter=" . rawurlencode($filter);
} else {
    $filter = null;
    $vars['filter'] = null;
    $filter_param = "";
}

?>
<div id="two_column_left_sidebar">
	<div id="owner_block">
		<div id="left_filters">
            <div class="browse-filter-button">
                <?php echo '<a style="color: black!important; font-weight: bold" class="" href="'.$url.'pg/lds/browse/">' . htmlspecialchars(T('Clear all filters')) . '</a>'?>
            </div>

            <?php echo $added_filter; ?>
            <h3><?php echo T($tools_term) ?></h3>
            <ul class="tag_selector">

                <?php
                foreach ($CONFIG->project_templates['full'] as $template):
                ?><li><?php
                    if($template['subtype']):
                        ?><a class="lds_tag" href="<?php echo $url ?>pg/lds/browse/?tagk=editor_subtype&tagv=<?php echo urlencode($template['subtype']) ?>&filter=<?php echo rawurlencode($filter) ?>"><?php echo htmlspecialchars($template['title']) ?></a><?php
                    else:
                        ?><a class="lds_tag" href="<?php echo $url ?>pg/lds/browse/?tagk=editor_type&tagv=<?php echo urlencode($template['type']) ?>&filter=<?php echo rawurlencode($filter) ?>"><?php echo htmlspecialchars($template['title']) ?></a><?php
                    endif;
                ?></li><?php
                endforeach;
                ?></li>

            </ul>

            <?php if(!$disable_search_patterns):?>
            <h3><a href="<?php echo $url.'pg/lds/patterns'?>" style="color:black;"><?php echo T("Search patterns") ?></a></h3>
            <br />
            <?php endif; ?>

            <?php if(!$disable_external_repository):?>
            <h3><a href="<?php echo $url.'pg/lds/repository'?>" style="color:black;"><?php echo T("External repositories") ?></a></h3>
            <br />
            <?php endif; ?>

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
                                <a class="lds_tag <?php echo $classname ?>" href="<?php echo $url ?>pg/lds/browse/?tagk=<?php echo urlencode($classname) ?>&tagv=<?php echo urlencode($u_t->tag) ?>&filter=<?php echo rawurlencode($filter) ?>"><?php echo $u_t->tag ?></a>
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
        <?php
        $filter_orig = $filter;
        $filter = $key_params['filter']; ?>
        <?php
        if($key_params['revised'] == "true")
            $filter['revised'] = $key_params['revised'];
         ?>
		<?php if (!empty($filter)): ?>
            <?php
            $tagv_label = array();

            if(!empty($tagv))
                $tagv_label = $tagv;

            if(!empty($filter['editor_subtype'][0]) or !empty($filter['editor_type'][0])) {

                if(!empty($filter['editor_subtype'][0]))
                    $tool = $filter['editor_subtype'][0];
                if(!empty($filter['editor_type'][0]))
                    $tool = $filter['editor_type'][0];

                if(isset($CONFIG->project_templates['full'][$tool])) {
                    $tagv_label[] = array('tag_editor_subtype', $CONFIG->project_templates['full'][$tool]['title']);
                }
            }

            foreach(array('revised', 'tags', 'discipline', 'pedagogical_approach') as $tag_class) {
                if(!empty($filter[$tag_class])) {
                    foreach($filter[$tag_class] as $tag) {
                        $tagv_label[] = array($tag_class, $tag);
                    }
                }
            }
            if($key_params['revised'] == "true") {
                $reach = T("Edited Lds from projects");
                $reach_link = $url . 'pg/lds/browse/?revised=true';
            } else {
                $reach = T("All LdS");
                $reach_link = lds_viewTools::getUrl();
            }
            ?>
		<h2><a href="<?php echo $reach_link ?>"><?php echo $reach ?></a> » <?php
                foreach($tagv_label as $tagv):
            ?><span class="lds_tag <?php echo $tagv[0] ?>"><?php echo $tagv[1] ?></span><?php
                endforeach;
            ?></h2>
		<?php else: ?>
		<h2><?php echo T("All LdS") ?></h2>
		<?php endif; ?>
	</div>
	
	<div class="filters">
		<div class="paging">
			<?php echo lds_viewTools::pagination($count, 10, $filter) ?>
		</div>
        <div class="lds_order_by">
        <?php
        if($order == "time")
            echo T("sort by").' '.'<b>'.T("newest").'</b> / '.'<a href="'.lds_viewTools::getUrl()."?order=title{$filter_param}".'">'.T("title").'</a>';
        else
            echo T("sort by").' '.'<a href="'.lds_viewTools::getUrl()."?order=time{$filter_param}".'">'.T("newest").'</a> / <b>'.T("title").'</b>';
        ?>
        </div>
	</div>
	
	<?php echo elgg_view('lds/browselist', $vars) ?>
	
	<div class="filters">
		<div class="paging">
			<?php echo lds_viewTools::pagination($count, 10, $filter) ?>
		</div>
	</div>
</div>

<script>
    var t9n = {
        showTags : "<?php echo T('Show more tags') ?>",
        hideTags : "<?php echo T('Show less tags') ?>"
    }
</script>