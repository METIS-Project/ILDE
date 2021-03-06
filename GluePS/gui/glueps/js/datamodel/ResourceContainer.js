/**
 * Funcionalidad asociada a los recursos
 */

var ResourceContainer = {

	getResource : function(resourceId) {
		var resources = JsonDB.deploy.design.resources;
		for ( var i = 0; i < resources.length; i++) {
			if (resources[i].id == resourceId && resources[i].instantiable==false) {
				return new Resource(resources[i]);
			}
		}
		return false;
	},
	
	getAllResources : function() {
		var resourceList = new Array();
		var resources = JsonDB.deploy.design.resources;
		if (resources){
			for ( var i = 0; i < resources.length; i++) {
				//Los recursos (no herramientas) no son instanciables
				if (resources[i].instantiable==false){
				resourceList.push(new Resource(resources[i]));
				}
			}
		}
		return resourceList;
	},
	
	addResource : function(name, location) {
		var data = new Object();
		data.id = this.getNextResourceId();
		data.instantiable = false; //Los recursos no son instanciables
		data.name = name;
		data.location = location;
		if (JsonDB.deploy.design.resources) {
			JsonDB.deploy.design.resources.push(data);
			//Ordena todos los recursos (documentos y herramientas)
			ResourceContainer.sortResources();
		} else {
			JsonDB.deploy.design.resources = new Array();
			JsonDB.deploy.design.resources.push(data);
		}
		return ResourceContainer.getResource(data.id);
	},
	
	deleteResource : function(resource) {
		if (JsonDB.deploy.design.resources) {
			var resources = JsonDB.deploy.design.resources;
			for ( var i = 0; i < resources.length; i++) {
				if (resources[i].id == resource.getId()) {
					resources.splice(i, 1);
					return true;
				}
			}
		}
		return false;
	},
	
	getNextResourceId : function() {
		var maxId = 0;
		if (JsonDB.deploy.design.resources) {
			var resources = JsonDB.deploy.design.resources;
			for ( var i = 0; i < resources.length; i++) {
				var id = resources[i].id;
				if (resources[i].id.length > 4
						&& resources[i].id.substring(0, 4) == "RID_") {
					var id = parseInt(resources[i].id.substring(4));
					if (id > maxId) {
						maxId = id;
					}
				}
			}
		}
		return "RID_" + parseInt(maxId + 1);
	},
	
	sortResources: function(){
		if (JsonDB.deploy.design.resources) {
			JsonDB.deploy.design.resources.sort(natcompareByName);
		}
	}
}