<?php if (get_context() == 'profile'): ?>
    <script>
    var t9n = {
    Preview : "<?php echo T("Preview") ?>"
    };
    </script>
<script type="text/javascript" src="<?php echo $vars['url']; ?>mod/profile/views/default/js/jquery.imgareaselect-0.4.2.js"></script>
<script type="text/javascript" src="<?php echo $vars['url']; ?>mod/profile/javascript/profile.js"></script>
<?php endif; ?>