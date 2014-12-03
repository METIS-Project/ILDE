/**
 * Funcionalidad asociada al learning environment
 */

var LearningEnvironment = {

	getLearningEnvironmentId: function() {
		return JsonDB.deploy.learningEnvironment.id;
	},
	
	getLearningEnvironmentName: function() {
		return JsonDB.deploy.learningEnvironment.name;
	},
	
	getLearningEnvironmentType: function() {
		return JsonDB.deploy.learningEnvironment.type;
	},
	
	getLearningEnvironmentParameters: function() {
		return JsonDB.deploy.learningEnvironment.parameters;
	},
	
	getCreduser: function() {
		return JsonDB.deploy.learningEnvironment.creduser;
	},
	
	getInternalTools : function() {
		var toolList = new Array();
		var internalTools = JsonDB.deploy.learningEnvironment.internalTools;
		for ( var it in internalTools) {
			var obj = {
					key: it,
					value: internalTools[it]
			};
			toolList.push(obj);
		}
		return toolList;
	},
	
	getExternalTools : function() {
		var toolList = new Array();
		var externalTools = JsonDB.deploy.learningEnvironment.externalTools;
		for ( var it in externalTools) {
			var obj = {
					key: it,
					value: externalTools[it]
			};
			toolList.push(obj);
		}
		return toolList;
	},
	
	getTool: function(toolId){
		var internalTools = this.getInternalTools();
		for (var i=0; i<internalTools.length;i++)
		{
			if (internalTools[i].key==toolId)
				{
				return internalTools[i];
				}
		}
		var externalTools = this.getExternalTools();
		for (var i=0; i<externalTools.length;i++)
		{
			if (externalTools[i].key==toolId)
				{
				return externalTools[i];
				}
		}
		return false;
	},
	
	isInternal: function(toolId){
		var internalTools = this.getInternalTools();
		for (var i=0; i<internalTools.length;i++)
		{
			if (internalTools[i].key==toolId)
				{
				return true;
				}
		}
		return false;
	},
	
	isExternal: function(toolId){
		var externalTools = this.getExternalTools();
		for (var i=0; i<externalTools.length;i++)
		{
			if (externalTools[i].key==toolId)
				{
				return true;
				}
		}
		return false;
	},
	
	getEnableAR: function(){
		if (typeof(JsonDB.deploy.learningEnvironment.enableAR) != "undefined")
		{		
			return JsonDB.deploy.learningEnvironment.enableAR;
		}
		else{
			//By default the functionality is disabled
			return false;
		}
	},
	
	getShowAR: function(){
		if (typeof(JsonDB.deploy.learningEnvironment.showAR) != "undefined")
		{		
			return JsonDB.deploy.learningEnvironment.showAR;
		}
		else{
			//By default the AR controls are hidden
			return false;
		}
	},
	
	getShowVG: function(){
		if (typeof(JsonDB.deploy.learningEnvironment.showVG) != "undefined")
		{		
			return JsonDB.deploy.learningEnvironment.showVG;
		}
		else{
			//By default the VG controls are hidden
			return false;
		}
	}
}