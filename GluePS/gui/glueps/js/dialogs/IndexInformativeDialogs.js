/**
 * 
 */

var IndexInformativeDialogs = {
		
	 init: function(){
		 dojo.connect(dojo.byId("dialogLogoutOk"), "onclick", function(){
			 IndexInformativeDialogs.hideLogoutDialog();
			 window.location.reload();
		 });
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
    }
    
}