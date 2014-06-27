/**
 * Funcionalidad asociada a las instancias de herramientas
 */

var ToolInstanceContainer = {

	getToolInstance : function(toolInstanceId) {
		var toolInstances = JsonDB.deploy.toolInstances;
		for ( var i = 0; i < toolInstances.length; i++) {
			if (toolInstances[i].id == toolInstanceId) {
				return new ToolInstance(toolInstances[i]);
			}
		}
		return false;
	},
	
	
	getAllToolInstances : function() {
		var toolInstanceList = new Array();
		if (JsonDB.deploy.toolInstances){
			var toolInstances = JsonDB.deploy.toolInstances;
			for ( var i = 0; i < toolInstances.length; i++) {
				toolInstanceList.push(new ToolInstance(toolInstances[i]));
			}
		}
		return toolInstanceList;
	},
	
	deleteToolInstance: function(toolInstance) {
		var toolInstances = JsonDB.deploy.toolInstances;
		if (JsonDB.deploy.toolInstances){
			for ( var i = 0; i < toolInstances.length; i++) {
				if (toolInstances[i].id == toolInstance.getId())
				{
					toolInstances.splice(i,1);
					return true;
				}
			}
		}
		return false;
	},
	
	/**
	 * Añade una nueva instancia de herramienta
	 */
	addToolInstance : function(name, tool) {
		var data = new Object();
		data.deployId = JsonDB.getDeploymentId();
		data.id = JsonDB.getDeploymentId() + "/toolInstances/" + this.getNextToolInstanceId();
		data.name = name;
		data.resourceId = tool.getId();
		if (JsonDB.deploy.toolInstances) {
			JsonDB.deploy.toolInstances.push(data);
		} else {
			JsonDB.deploy.toolInstances = new Array();
			JsonDB.deploy.toolInstances.push(data);
		}
		return ToolInstanceContainer.getToolInstance(data.id);
	},
	
	getNextToolInstanceId : function() {
		var maxId = 0;
		if (JsonDB.deploy.toolInstances) {
			var toolInstances = JsonDB.deploy.toolInstances;
			for ( var i = 0; i < toolInstances.length; i++) {
				var id = toolInstances[i].id;
				var string = toolInstances[i].id.split("/toolInstances/")[1];
				if (toolInstances[i].id.length > 4
						&& toolInstances[i].id.split("/toolInstances/")[1].substring(0, 4) == "TII_") {
					var id = parseInt(toolInstances[i].id.split("/toolInstances/")[1].substring(4));
					if (id > maxId) {
						maxId = id;
					}
				}
			}
		}
		return "TII_" + parseInt(maxId + 1);
	},
	
	getToolInstanceTool: function(tool){
		var tiTool = new Array();
		var activities = ActivityContainer.getFinalActivities();
		for (var i=0; i < activities.length; i++)
		{
			var instanced = activities[i].getInstancedActivities();
			for (var j=0; j < instanced.length; j++)
			{
				var toolInstances = instanced[j].getToolInstances();
				for (var k=0; k < toolInstances.length; k++)
				{
					//El ID de herramienta debe ser el mismo
					if (tool.getId()== toolInstances[k].getTool().getId())
					{
						tiTool.push(toolInstances[k]);
					}
				}
				
			}
		}
		return tiTool;
	},
	
	getToolInstancesWithMarkers : function() {
		var toolInstanceList = new Array();
		if (JsonDB.deploy.toolInstances){
			var toolInstances = JsonDB.deploy.toolInstances;
			for ( var i = 0; i < toolInstances.length; i++) {
				var toolInstance = new ToolInstance(toolInstances[i]);
				if (toolInstance.hasValidQrCodePosition() || toolInstance.getValidJunaioMarkerPosition() != null){
					toolInstanceList.push(toolInstance);
				}
			}
		}
		if (toolInstanceList.length >0){
			return toolInstanceList;
		} else {
			return null;
		}

	}
}