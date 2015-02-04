/**
 * 
 */

var InformativeDialogs = {
		
	 init: function(){
		 dojo.connect(dojo.byId("dialogAlertOk"), "onclick", function(){
			 InformativeDialogs.hideAlertDialog();
		 });
		 
		 dojo.connect(dojo.byId("dialogLogoutOk"), "onclick", function(){
			 InformativeDialogs.hideLogoutDialog();
             //Si permanece abierta, recargar la página de la que procede
			 if (window.opener)
			 {
				 window.opener.location.reload();
			 }
			 window.close();
		 });
	 },
		
	 /**
	 * Muestra la ventana de proceso con el título indicado
	 * @param info Información a mostrar en la ventana
	 */
	showLoadingDialog: function(info, title){
		if (info){
			dojo.byId("loadingInfo").innerHTML = info;
		}
		else{
			dojo.byId("loadingInfo").innerHTML = i18n.get("ProcesandoArchivos");
		}
		if (title){
			dijit.byId("loading").setAttribute("title", title);
		}else{
			dijit.byId("loading").setAttribute("title", i18n.get("wait"));
		}
		dojo.byId("loadingGif").style.display="";
		dijit.byId("loading").show();
	},

	/**
	 * Especifica la información a mostrar en la ventana
	 * @param info Información a mostrar en la ventana
	 */
	setInfoLoadingDialog: function(info, hideGif){
		dojo.byId("loadingInfo").innerHTML = info;
		if (hideGif==true)
		{
			dojo.byId("loadingGif").style.display="none";
		}
		else{
			dojo.byId("loadingGif").style.display="";
		}
	},
	
	/**
	 * Obtiene la información a mostrar en la ventana
	 * @return Información a mostrar en la ventana
	 */
	getInfoLoadingDialog: function(){
		return dojo.byId("loadingInfo").innerHTML;
	},
	
	
	/**
	 * Oculta la ventana de proceso
	 */
	hideLoadingDialog: function(){
		dijit.byId("loading").hide();
	},
	
    /**
     * Muestra un diálogo de alerta
     * @param txtTitle Título del diálogo de alerta
     * @param txtContent Contenido del diálogo de alerta
     */
    showAlertDialog: function(txtTitle, txtContent){
        var dlg = dijit.byId("dialogAlert");
        dlg.titleNode.innerHTML = txtTitle;
        dojo.byId("dialogAlertInfo").innerHTML = txtContent;
        dlg.show();
    },
    
    /**
     * Oculta el diálogo de alerta
     */
    hideAlertDialog: function(){
        var dlg = dijit.byId("dialogAlert");
        dlg.hide();
    },
    
    /**
     * Muestra un diálogo de logout
     * @param txtTitle Título del diálogo de logout
     * @param txtContent Contenido del diálogo de logout
     */
    showLogoutDialog: function(txtTitle, txtContent){
        var dlg = dijit.byId("dialogLogout");
        dlg.titleNode.innerHTML = txtTitle;
        dojo.byId("dialogLogoutInfo").innerHTML = txtContent;
        dlg.show();
    },
    
    /**
     * Oculta el diálogo de logout
     */
    hideLogoutDialog: function(){
        var dlg = dijit.byId("dialogLogout");
        dlg.hide();
    },
    
    /**
     * Muestra un diálogo de confirmación
     * @param txtTitle Título del diálogo de confirmación
     * @param txtContent Contenido del diálogo de confirmación
     */
    showConfirmDialog: function(txtTitle, txtContent, event, idDeploy){
        var dlg = dijit.byId("dialogConfirm");
        dlg.titleNode.innerHTML = i18n.get("confirmDeleteDeploy");
        dojo.byId("dialogConfirmInfo").innerHTML = txtContent;
        dojo.byId("dialogConfirmOk").setAttribute("onclick", event(idDeploy));
        dlg.show();
    },
    
    /**
     * Oculta el diálogo de confirmación
     */
    hideConfirmDialog: function(){
        var dlg = dijit.byId("dialogConfirm");
        dlg.hide();
    }
}