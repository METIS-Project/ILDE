<script type="text/javascript">
<?php global $ldshake_jscache_break;?>
<?php global $CONFIG;?>
var baseurl = '<?php echo $vars['url'] ?>';
<?php
        $user = get_loggedin_user();

        if ((empty($language)) && (isset($user)) && ($user->language))
            $language = $user->language;

        if ((empty($language)) && (isset($CONFIG->language)))
            $language = $CONFIG->language;

        if(function_exists("ldshake_mode_selected_language")) {
            $language = ldshake_mode_selected_language();
        }
 ?>
var language = '<?php echo $language ?>';
var isadminloggedin = <?php echo (isadminloggedin() ? 'true' : 'false') ?>;
var t9nc = {
    deleteLdS : "<?php echo T("Are you sure you want to delete this LdS?") ?>"
};
var ldshake_cache = <?php echo json_encode($ldshake_jscache_break) ?>;
<?php if(isloggedin()): ?>
var contextual_help = <?php echo ((empty(get_loggedin_user()->disable_contextual_help ) and empty($CONFIG->disable_contextual_help)) ? 'true' : 'false'); ?>
<?php endif; ?>
</script>
<!--[if (!IE)|(gt IE 8)]><!-->
<script type="text/javascript" src="<?php echo $vars['url']?>vendors/jquery/2.1.1/jquery.min.js"></script>
<!--<![endif]-->

<!--[if lte IE 8]>
<script type="text/javascript" src="<?php echo $vars['url']?>vendors/jquery/1.11.1/jquery.min.js"></script>
<!--[endif]-->

<script src="<?php echo $vars['url']?>vendors/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script src="<?php echo $vars['url']; ?>vendors/moment/moment-with-langs.min.js"></script>
<script type="text/javascript" src="<?php echo $vars['url']; ?>javascript/initialise_elgg.js"></script>
<script type="text/javascript" src="<?php echo $vars['url']; ?>javascript/common.js"></script>
<script type="text/javascript">
//Unix time to language
$(function() {
    moment.lang(language);

    $(".timeago_timestamp, .text_date_same_year").each(function() {
        var $e          = $(this);
        var timestamp   = parseInt($e.attr("timestamp"), 10);
        var now         = moment();
        var lds_date    = moment.unix(timestamp);
        var date;

        if(now.diff(lds_date, 'days') < 7 && $e.hasClass("timeago_timestamp"))
            date = lds_date.fromNow();
        else {
            var format;
            var month_format = "MMM";
            var year_format = "YY";

            if($e.hasClass("long_month")) {
                month_format = "MMMM";
                year_format = "YYYY";
            }

            if(now.isSame(lds_date, 'year'))
                format = "D " + month_format;
            else
                format = "D " + month_format + " " + year_format;

            date = lds_date.format(format);
        }
        $e.text(date);
    });

    $("[friendly_timestamp]").each(function() {
        var $e          = $(this);
        var timestamp   = parseInt($e.attr("friendly_timestamp"), 10);
        var lds_date    = moment.unix(timestamp);

        var format = "D MMMM YYYY HH:mm:ss";
        var date = lds_date.format(format);

        $e.text(date);
    });


});
</script>
<?php
if(get_context() == 'admin')
	echo elgg_view('metatags',$vars);
