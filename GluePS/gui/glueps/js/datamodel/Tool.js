/**
 * Funcionalidad asociada a una herramienta
 */

var Tool = function(data)
{
	
	var data = data;
		
	this.getId =  function(){
		return data.id;
	},
	
	this.getName =  function(){
		return data.name;
	},	
	
	this.getToolKind =  function(){
		return data.toolKind;
	},
	
	this.getToolType =  function(){
		return data.toolType;
	},
	
	this.getToolVle = function(){
		return LearningEnvironment.getTool(data.toolType);
	},
	
	this.setName = function(name){
		data.name = name;
	},
	
	this.setToolKind = function(toolKind){
		data.toolKind = toolKind;
	},
	
	this.setToolType = function(toolType){
		data.toolType = toolType;
	},
		
	this.isExternal = function(){
		return (data.toolKind=="external");
	},
	
	this.isInternal = function(){
		return (data.toolKind=="internal");
	}
	
}





