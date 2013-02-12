<?php

	global $autocomplete_js_loaded;
	
	$entities = $vars['entities'];
	$internalname = $vars['internalname'];
	
	if(!$internalname)
		$internalname = 'entity_selected_guid';
		
	$internalname_text = $vars['internalname_text'];	
	if(!$internalname_text)
		$internalname_text = 'selected_text';
		
	$value_id = $vars['value_id'];
	if(!$value_id)
		$value_id = 0;
		
	$value_text = $vars['value_text'];
	if(!$value_text)
		$value_text = '';
		
	
	$str_entities = '';
	if($entities) 
		foreach($entities as $entity){
			$members ='';
			if($entity instanceof ElggGroup){
				$members_count = get_group_members($entity->guid, 10, 0, 0, true);
				$members = ", members: '" . $members_count . "'";
			}
			
			$description = ' ';
			if($entity->description)
				$description = (strlen($entity->description) > 40) ? substr($entity->description, 0, 40) . '...' : $entity->description;
			if(empty($str_entities)){
				$str_entities .= "{id: '" . $entity->getGUID() . "', name: '" . $entity->name . "', description: '" . $description . "'{$members}}";
			}else{
				$str_entities .= ", {id: '" . $entity->getGUID() . "', name: '" . $entity->name . "', description: '" . $description . "'{$members}}";
			}
		}
		
	$time = time();
	$id_autocomplete = 'autocomplete_' . $time;
	
	if($vars['internal_id'])
		$id_autocomplete = $vars['internal_id'];
			
	if (!isset($autocomplete_js_loaded)) $autocomplete_js_loaded = false;

	if (!$autocomplete_js_loaded) {
	
?>
<!-- include autocomplete -->
<script language="javascript" type="text/javascript" src="<?php echo $vars['url']; ?>mod/autocomplete/vendors/autocomplete/jquery.autocomplete.pack.js"></script>


<?php

		$autocomplete_js_loaded = true;
	}

?>

<!-- show the input -->
	<input type="hidden" name="<?php echo $internalname; ?>" value='<?php echo $value_id; ?>' />
	<input type="text" class='autocomplete' id="<?php echo $id_autocomplete; ?>" name ='<?php echo $internalname_text; ?>' value='<?php echo $value_text?>' />


<script type="text/javascript">

$().ready(function() {

	var data<?php echo $id_autocomplete?> = [<?php echo $str_entities; ?>];

	$("#<?php echo $id_autocomplete; ?>").autocomplete(data<?php echo $id_autocomplete?>, {
		minChars: 0,
		width: 310,
		matchContains: true,
		autoFill: false,
		highlightItem: false,
		formatItem: function(row, i, max, term) {
			members = '';
			if(row.members != 'undefined')
				members = '<br /><strong>Members: </strong>'+ row.members;	
			description = '';
			if(row.description != '')
				description = "<br /><span style='font-size: 86%;'><strong>Description: </strong>" + row.description + members + "</span>";	
				
			return row.name.replace(new RegExp("(" + term + ")", "gi"), "<strong>$1</strong>") + description;
		},
		formatMatch: function(row, i, max) {
			return row.id + " " + row.name;
		},
		formatResult: function(row) {
			return row.name;
		}
	});


	$("#<?php echo $id_autocomplete; ?>").result(function(event, data, formatted) {
		var hidden = $(this).parent().next().find(">:input");
		$(this).prev().val(data.id);
	});
	
});
</script>