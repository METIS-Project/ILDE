
INSTALLATION
==========================
Put into mod and enable in the admin panel. It will override the default email
notification handler. There are smtp settings and an end of line marker override
in the admin plugin settings.


SMTP AUTHENTICATION
===========================
By default, this plugin will turn on smtp authentication if smtp is turned on.
If your server does not require authentication, you'll want to edit mail.php
and turn off authentication.


TROUBLESHOOTING
===========================
If there are errors, they should be written to your server error log.


HOW TO CONFIRM THE PLUGIN IS WORKING
============================
Check the header of an email that was sent after the plugin was enabled. You
should see the sender as PHPMailer.


HOW TO USE THIS FROM ANOTHER PLUGIN
=============================
See the function phpmailer_send() in mail.php.


PHPMailer can be found at http://phpmailer.codeworxtech.com/
