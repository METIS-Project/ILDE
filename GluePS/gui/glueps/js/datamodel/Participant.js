/**
 * Funcionalidad asociada a un participante
 */

var Participant = function(data)
{
	
	var data = data;
		
	this.getId =  function(){
		return data.id;
	},
	
	this.getName =  function(){
		return data.name;
	},
	
	this.getIsStaff = function(){
		return data.isStaff;
	},
	
	this.setIsStaff = function(isStaff){
		data.isStaff = isStaff;
	},
	
	this.setName = function(name){
		data.name = name;
	}
}