<?php extract($vars); ?>
<?php echo T("%1 created a new LdS: \"%2\".", $fromName, $ldsTitle) ?>


<?php echo T("To view this LdS, click here:") ?>

	<?php echo $ldsUrl ?>

<?php echo T("To view %1's profile, click here:", $fromName) ?>

	<?php echo $senderUrl ?>

<?php echo T("You cannot reply to this email.") ?>