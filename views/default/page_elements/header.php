<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<?php echo elgg_view('page_elements/metadef', $vars); ?>
</head>

<body>
	<?php echo elgg_view('page_elements/elgg_topbar', $vars); ?>
	<div id="page_container">
		<div id="page_wrapper">
			<?php if (!isloggedin()): ?>
			<div id="layout_header">
				<a href="<?php echo $vars['url']; ?>"><img src="<?php echo $vars['url']; ?>_graphics/ldshake-logo.png" /></a>
			</div>
			<?php endif; ?>