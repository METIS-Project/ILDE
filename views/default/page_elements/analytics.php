<?php
//Pau: Google Analytics asynchronous tracking code
//If we are not running on a local machine
if (!preg_match('/localhost/', $_SERVER['HTTP_HOST'])):
	//And we are not in a UPF computer
	$excludedIPs = array (
		'^193\.145\.56\.241$',
		'^193\.145\.56\.242$',
		'^193\.145\.56\.243$',
		'^193\.145\.56\.244$',
		'^193\.145\.39', // Last part can be anything, WiFi connections from the UPF
		'^127\.0\.0', //Localhost
	);
	$regexp = implode('|', $excludedIPs);
	//if (!preg_match("/($regexp)/", $_SERVER['REMOTE_ADDR'])):
?>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-36286106-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
<?php 
	//endif;
endif;
?>
