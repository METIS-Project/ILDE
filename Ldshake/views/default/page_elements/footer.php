<?php extract($vars); ?>
<div class="clearfloat"></div>

<div id="layout_footer">
	<div id="footer-logos">
		<ul>
            <li><a href="http://metis-project.org/index.php" target="_blank"><img src="<?php echo $url ?>_graphics/metis/logo-footer-metis.png" alt="<?php echo T("METIS") ?>"></a></li>
            <li><a href="http://ec.europa.eu/education/lifelong-learning-programme/" target="_blank"><img src="<?php echo $url ?>_graphics/metis/logos-footer-llp.png" alt="<?php echo T("Lifelong Learning Programme") ?>"></a></li>
            <li><a href="http://ldshake.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/powered-by-ldshake.png" alt="<?php echo T("Powered by LdShake") ?>" /></a></li>
            <li><a href="http://www.gsic.uva.es/glueps/" target="_blank"><img src="<?php echo $url ?>_graphics/metis/glueps-inside.png" alt="<?php echo T("GLUEPS") ?>"></a></li>
            <li><a href="http://www.gsic.uva.es/webcollage/index.php?l=en" target="_blank"><img src="<?php echo $url ?>_graphics/metis/WC-inside.png" alt="<?php echo T("WebCollage") ?>"></a></li>
            <li><a href="http://sourceforge.net/projects/openglm/" target="_blank"><img src="<?php echo $url ?>_graphics/metis/openglm-supported.png" alt="<?php echo T("OpenGLM") ?>"></a></li>
            <li><a href="http://cosy.ds.unipi.gr/cadmos/" target="_blank"><img src="<?php echo $url ?>_graphics/metis/cadmos-supported.png" alt="<?php echo T("CADMOS") ?>"></a></li>
            <li><a href="http://www.open.ac.uk/blogs/OULDI/" target="_blank"><img src="<?php echo $url ?>_graphics/metis/ouldi-inside-supported.png" alt="<?php echo T("Ouldi tools") ?>"></a></li>
            <!--<li><a href="http://www.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/upf-logo.png" alt="<?php echo T("UPF logo") ?>"></a></li>
			<li><a href="http://gti.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/gti-small.png" alt="<?php echo T("GTI UPF logo") ?>" /></a></li>
            <li><a href="http://ldshake.upf.edu" target="_blank"><img src="<?php echo $url ?>_graphics/powered-by-ldshake.png" alt="<?php echo T("Powered by LdShake") ?>" /></a></li>-->
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
