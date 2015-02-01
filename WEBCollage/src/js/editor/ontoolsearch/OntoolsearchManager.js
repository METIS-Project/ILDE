


var OntoolsearchDialog = {
    appletWnd: null,
    dlg : null,
	
    init: function() {
        this.dlg = dijit.byId("LDOntoolsearchWaitDialog");
    },
	
    open: function() {
        this.appletWnd = window.open("ontoolsearch/");
        this.dlg.show();
    },
	
    cancel: function() {
        this.close();
    },
	
    close: function() {
        this.appletWnd.close();
        this.dlg.hide();
    },
	
    setResult: function(url, query) {
        this.close();
        var type = url ? "ontoolurl" : "ontoolquery";
        EditToolDialog.toolSearchResult(type, url, query, null);
    }
};
