<?php extract($vars); ?>
<?php echo T("%1 posted a new comment on the LdS \"%2\". It reads:", $fromName, $ldsTitle) ?>

			
<?php echo $comment ?>


<?php echo T("To reply or view the LdS, click here:") ?>

	<?php echo $ldsUrl ?>

<?php echo T("To view %1's profile, click here:", $fromName) ?>

	<?php echo $senderUrl ?>

<?php echo T("You cannot reply to this email.") ?>