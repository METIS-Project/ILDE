<script type="text/javascript">
var baseurl = '<?php echo $vars['url'] ?>';
var language = '<?php echo $vars['config']->language ?>';
var t9nc = {
    deleteLdS : "<?php echo T("Are you sure you want to delete this LdS?") ?>"
};

</script>
<!--<script type="text/javascript" src="<?php echo $vars['url']; ?>vendors/jquery/jquery-1.4.3.min.js"></script>-->
<!--<script type="text/javascript" src="<?php echo $vars['url']; ?>vendors/jquery/jquery-1.7.2.min.js"></script>-->
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<!--<script src="http://code.jquery.com/jquery-migrate-1.2.1.js"></script>-->
<!--<script type="text/javascript" src="<?php echo $vars['url']; ?>vendors/jquery/jquery-ui-1.8.6.custom.min.js"></script>-->
<script type="text/javascript" src="<?php echo $vars['url']; ?>javascript/initialise_elgg.js"></script>
<script type="text/javascript" src="<?php echo $vars['url']; ?>javascript/common.js"></script>
<?php
if(get_context() == 'admin')
	echo elgg_view('metatags',$vars);
