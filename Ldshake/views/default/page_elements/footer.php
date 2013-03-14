<?php extract($vars); ?>
<div class="clearfloat"></div>

<div id="layout_footer">
	<div id="footer-logos">
		<ul>
			<li><a href="http://www.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/upf-logo.png" alt="<?php echo T("UPF logo") ?>"></a></li>
			<li><a href="http://gti.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/gti-small.png" alt="<?php echo T("GTI UPF logo") ?>" /></a></li>
            <li><a href="http://ldshake.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/powered-by-ldshake.png" alt="<?php echo T("Powered by LdShake") ?>" /></a></li>
		</ul>
	</div>
	<div class="clearfloat"></div>
</div><!-- /#layout_footer -->

<div class="clearfloat"></div>

</div><!-- /#page_wrapper -->
</div><!-- /#page_container -->
<div id="footer_ending">
</div>
	<?php echo elgg_view('page_elements/jsarea', $vars); ?>
</body>
</html>
