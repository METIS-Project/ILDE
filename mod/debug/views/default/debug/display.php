<?php extract($vars) ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title><?php echo $entity->guid ?> - <?php echo get_class($entity) ?></title>
</head>

<body>
	<h1><?php echo $entity->guid ?> - <?php echo get_class($entity) ?> <?php if ($enable_disabled == 'enabled'){ ?><span style="color: #f00;">(disabled)</span><?php }?></h1>
	<?php if ($entity instanceof ElggUser): ?>
	<h2>User data:</h2>
	<ul>
		<li><strong>Username:</strong> <?php echo $entity->username ?></li>
		<li><strong>Name:</strong> <?php echo $entity->name ?></li>
		<li><strong>Email:</strong> <?php echo $entity->email ?></li>
	</ul>
	<?php endif; ?>
	<h2>Attributes:</h2>
	<ul>
		<li><strong>Title:</strong> <?php echo $entity->title ?></li>
		<li><strong>Type:</strong> <?php echo $entity->type ?></li>
		<li><strong>Subtype:</strong> <?php echo get_subtype_from_id($entity->subtype) ?></li>
		<li><strong>Owner GUID:</strong> <a href="<?php echo $url ?>pg/debug/display/<?php echo $entity->owner_guid ?>"><?php echo $entity->owner_guid ?></a></li>
		<li><strong>Container GUID:</strong> <a href="<?php echo $url ?>pg/debug/display/<?php echo $entity->container_guid ?>"><?php echo $entity->container_guid ?></a></li>
		<li><strong>Time created:</strong> <?php echo date('r', $entity->time_created) ?> (<?php echo $entity->time_created ?>)</li>
		<li><strong>Time updated:</strong> <?php echo date('r', $entity->time_updated) ?> (<?php echo $entity->time_updated ?>)</li>
		<li><strong>Acces id:</strong> <a href="<?php echo $url ?>pg/debug/displaygroup/<?php echo $entity->access_id ?>"><?php echo $entity->access_id ?></a></li>
		<li><strong>Enabled:</strong> <?php echo ($entity->enabled) ? 'yes' : 'no' ?></li>
	</ul>
	<h2>Metadata:</h2>
	<ul>
		<?php foreach ($metadata as $meta): ?>
		<?php if (preg_match('/guid/i', $meta->name)):?>
		<li><strong><?php echo $meta->name ?>:</strong> <a href="<?php echo $url ?>pg/debug/display/<?php echo $meta->value ?>"><?php echo $meta->value ?></a></li>
		<?php else: ?>
		<li><strong><?php echo $meta->name ?>:</strong> <?php echo $meta->value ?></li>
		<?php endif; ?>
		<?php endforeach; ?>
	</ul>
	<h2>Description:</h2>
	<p>
	<?php echo $entity->description ?>
	</p>
</body>