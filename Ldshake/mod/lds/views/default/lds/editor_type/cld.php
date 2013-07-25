<?php
global $CONFIG;
$doc = $vars['entity'];

$file = Editor::getFullFilePath($doc->file_guid);
$ext = pathinfo($doc->upload_filename, PATHINFO_EXTENSION);
?>
<div class="lds_image_viewer">
<?php if($ext == 'svg'): ?>
        <iframe id="svg_document_view" src="<?php echo $CONFIG->url.'action/lds/display_image?doc='.$doc->guid ?>" style="border: 0px;width:100%;height:805px;" /></iframe>

    <?php elseif(strcasecmp($ext,'jpg') ||
        strcasecmp($ext,'jpeg') ||
        strcasecmp($ext,'png') ||
        strcasecmp($ext,'gif')): ?>
    <img id="image_document_view" src="<?php echo $CONFIG->url.'action/lds/display_image?doc='.$doc->guid ?>" />
    <?php endif; ?>
</div>