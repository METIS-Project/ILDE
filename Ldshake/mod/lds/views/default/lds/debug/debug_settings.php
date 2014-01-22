<div class="settings_form debug_settings">
<h3>Debug settings</h3><br />
<p>
    <?php echo T("Editor name"); ?>
    <?php
    extract($vars);


    echo elgg_view('input/text',array('internalname' => 'name', 'value' => $editor->name));
    echo T("Editor internal name (spaceless string with a-z characters)");
    echo elgg_view('input/text',array('internalname' => 'internalname', 'value' => $editor->internalname));
    echo T("base URI where the REST service is located (eg. http://mydomain/webservices/) ");
    echo elgg_view('input/text',array('internalname' => 'resturl', 'value' => $editor->resturl));
    echo T("REST authorization password ");
    echo '<br />';
    echo elgg_view('input/password',array('internalname' => 'restpass', 'value' => $editor->restpass));
    echo '<br />';
    echo T("URL for the web GUI ");
    echo elgg_view('input/text',array('internalname' => 'guiurl', 'value' => $editor->guiurl));
    $values = array();
    if($editor->preview) $values[] = 'preview';
    if($editor->imsld) $values[] = 'imsld';

    echo T("Optional features:");
    echo "<br />";

    echo elgg_view('input/checkboxes',
        array('internalname' => 'options',
        'value' => $values,
        'options' => array(
            'Preview' => 'preview',
            'IMS-LD' => 'imsld',
        )
        )
    );
    //echo elgg_view('input/checkboxes',array('internalname' => 'imsld', 'value' => $editor->imsld));
    //echo elgg_view('input/checkboxes',array('internalname' => 'preview', 'value' => $editor->preview));

    echo "<p>" . elgg_view('input/submit', array('value' => T("Save"))) . "</p>";
    ?>
</p>

</div>
