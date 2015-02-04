/**
 * Funcionalidad asociada a un recurso
 */

var Resource = function(data)
{
	
	var data = data;
		
	this.getId =  function(){
		return data.id;
	},
	
	this.getName =  function(){
		return data.name;
	},
	
	this.getLocation = function(){
		return data.location;
	},
	
	this.setName = function(name){
		data.name = name;
	},
	
	this.setLocation = function(location){
		data.location = location;
	}
}