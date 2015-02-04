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
     * Muestra un di�logo de logout
     * @param txtTitle T�tulo del di�logo de logout
     * @param txtContent Contenido del di�logo de logout
     */
    showLogoutDialog: function(txtTitle, txtContent){
        var dlg = dijit.byId("dialogLogout");
        dlg.titleNode.innerHTML = txtTitle;
        dojo.byId("dialogLogoutInfo").innerHTML = txtContent;
        dlg.show();
    },
    
    /**
     * Oculta el di�logo de logout
     */
    hideLogoutDialog: function(){
        var dlg = dijit.byId("dialogLogout");
        dlg.hide();
    }
    
}