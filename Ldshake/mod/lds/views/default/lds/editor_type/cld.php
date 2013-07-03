<?php
global $CONFIG;
$doc = $vars['entity'];

$file = Editor::getFullFilePath($doc->file_guid);
$ext = pathinfo($doc->upload_filename, PATHINFO_EXTENSION);
?>
<div class="lds_image_viewer">
<?php if($ext == 'svg'): ?>

<?php echo file_get_contents($file); ?>

    <?php elseif(strcasecmp($ext,'jpg') ||
        strcasecmp($ext,'jpeg') ||
        strcasecmp($ext,'png') ||
        strcasecmp($ext,'gif')): ?>
    <img src="<?php echo $CONFIG->url.'action/lds/display_image?doc='.$doc->guid ?>" />
    <?php endif; ?>
</div>