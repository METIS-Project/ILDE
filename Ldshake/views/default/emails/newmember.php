<html><head></head><body>
<?php extract($vars); ?>
<?php echo T("Hello %1,", $new_user->name) ?>
<br>
<?php echo T("welcome to the community") ?> <a href="<?php echo $site_url ?>"><?php echo $site->name ?></a>.
<br>
<?php echo T("Your username is %1 ", $new_user->username) ?>
<?php echo T("and your password is %1,", $password) ?>
<br>
<?php echo T("Please, change your password as soon as possible. You can do it by going to \"Account Settings\". This option will appear if you click your username located at the left of the topbar menu.") ?>
<br>
<br>

<?php echo T("Come on!") ?>

</body></html>