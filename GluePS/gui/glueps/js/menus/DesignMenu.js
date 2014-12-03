
var DesignMenu = {
				
		init: function(){			
			// Asignar Funcionalidad a botón de confirmación de borrado de diseño
			dojo.connect(dojo.byId("dialogConfirmDeleteDesignOk"), "onclick", function(){
				var designId = dijit.byId("deleteDesignId").getValue();
				DesignMenu.deleteDesign(designId);
			});
			
			// Asignar Funcionalidad a botón de cancelar borrado de diseño
			dojo.connect(dojo.byId("dialogConfirmDeleteDesignCancel"), "onclick", function(){
				DesignMenu.hideDeleteDesign();
			});
		},		
		
	    /**
	     * Menú de opciones de un diseño
	     * @param data
	     */
	    getDesignMenu: function(data) {
	        var items = new Array();
	        items.push({
	            label: i18n.get("MyListDesignNewCreateDeployButton"),
	            icon: "edit",
	            onClick: function(data) {
	            	Deploy.anadirTitle(data.id, data.design, data.designtype);
	            },
	            data: data,
	            help: i18n.get("newDeployInfo")
	        });
	        items.push({
	            label: i18n.get("deleteDesign"),
	            icon: "delete",
	            onClick: function(data) {
	            	DesignMenu.showDeleteDesign(data.id);
	            },
	            data: data,
	            help: i18n.get("deleteDesignInfo")
	        });
	        items.push({
	            label: i18n.get("exportDesign"),
	            icon: "export_icon",
	            onClick: function(data) {
	            	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
	            	var url = baseUrl + "/GLUEPSManager/designs/" + data.id;
	            	window.open(url);
	            },
	            data: data,
	            help: i18n.get("exportDesignInfo")
	        }); 
	        return items;
	    },
				
		/**
		 * Muestra la ventana de confirmación del borrado de un diseño
		 * @param idDesign Identificador del diseño que se quiere borrar
		 */
		showDeleteDesign: function(idDesign)
		{
	        var dlg = dijit.byId("dialogConfirmDeleteDesign");
	        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
	        dojo.byId("dialogConfirmDeleteDesignInfo").innerHTML= i18n.get("confirmDeleteDesign");
	        dlg.show();
			dijit.byId("deleteDesignId").setValue(idDesign);
		},
		
		/**
		 * Oculta la ventana de confirmación del borrado de un diseño
		 */
		hideDeleteDesign: function()
		{
			dijit.byId("dialogConfirmDeleteDesign").hide();
		},
		
		
		/**
		 * Borra el diseño cuyo id se proporciona
		 * @param idDesign Identificador del diseño que se quiere borrar
		 */
		deleteDesign: function(idDesign)
		{
				var baseUrl = window.location.href.split("/GLUEPSManager")[0];
				DesignMenu.hideDeleteDesign();
				Glueps.showLoadingDialog(i18n.get("deletingDesign"));
		        var xhrArgs = {
		            url : baseUrl + "/GLUEPSManager/designs/" + idDesign,
		            handleAs : "text",
		            load : function(data) {
		        		dijit.byId("loading").hide();
		                //Se actualiza la pantalla de los diseños
		                Design.getJsonDesigns();
		            },
		            error : function() {
		        		dijit.byId("loading").hide();
		        		Glueps.showAlertDialog(i18n.get("warning"), i18n.get("notDeletedDesign"));
		            }
		        };
		        var deferred = dojo.xhrDelete(xhrArgs);    
		}

}