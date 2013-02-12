<?php

	/**
	 * Elgg 2 column left sidebar canvas layout
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

?>
<!-- left sidebar -->
<div id="two_column_left_sidebar">

    <?php

    	echo elgg_view('page_elements/owner_block',array('content' => $vars['area1']));
    
    ?>

    <?php if (isset($vars['area3'])) echo $vars['area3']; ?>

</div><!-- /two_column_left_sidebar -->

<!-- main content -->
<div id="two_column_left_sidebar_maincontent">

<?php if (isset($vars['area2'])) echo $vars['area2']; ?>

</div><!-- /two_column_left_sidebar_maincontent -->

<?php
if (DEBUG_SHOW_FOOTER_INFO) :
?>
<div class="clearfloat"></div>
<h2>Contents of $vars</h2>
<div>
	<?php if (isset($vars)) debug_print ($vars, true) ?>
</div>

<div class="clearfloat"></div>
<h2>Contents of $CONFIG</h2>
<div>
	<?php if (isset($CONFIG)) debug_print ($CONFIG) ?>
</div>
<?php
endif;
?>