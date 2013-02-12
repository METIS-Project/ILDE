<script type="text/javascript">
var baseurl = '<?php echo $vars['url'] ?>';
</script>
<script type="text/javascript" src="<?php echo $vars['url']; ?>vendors/jquery/jquery-1.4.3.min.js"></script>
<script type="text/javascript" src="<?php echo $vars['url']; ?>vendors/jquery/jquery-ui-1.8.6.custom.min.js"></script>
<script type="text/javascript" src="<?php echo $vars['url']; ?>javascript/initialise_elgg.js"></script>
<script type="text/javascript" src="<?php echo $vars['url']; ?>javascript/common.js"></script>
<?php
if(get_context() == 'admin')
	echo elgg_view('metatags',$vars);
