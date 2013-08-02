var FlowDialogs = {

	onKeys : [],

	save : function(dlgId) {
		this.onKeys[dlgId] = dijit.byId(dlgId)._onKey;
		dijit.byId(dlgId)._onKey = null;
	},

	recover : function(dlgId) {
		this.onKeys[dlgId] = dijit.byId(dlgId)._onKey;
		dijit.byId(dlgId)._onKey = this.onKeys[dlgId];
		delete this.onKeys[dlgId];
	},

	prepareDialogs : function() {
		var activityDlg = dijit.byId("LDActivityEditDialog");

		var loSelectorDlg = dijit.byId("LDActivityLOSelector");
		loSelectorDlg.onOpen = function() {
			EditActivityDialog.loadLOSelector();
		};
		loSelectorDlg.execute = function() {
			EditActivityDialog.pickUpSelectedLOs();
                        //Save the selected learning objectives
                        //EditActivityDialog.save();
		};

		var resourceSelectorDlg = dijit.byId("LDActivityResourceSelector");
		resourceSelectorDlg.onOpen = function() {
			EditActivityDialog.loadResourceSelector();
		};
		resourceSelectorDlg.execute = function() {
			EditActivityDialog.pickUpSelectedResources();
                        //Save the selected resources
                        //EditActivityDialog.save();
		};
	},

	getPosition : function(who) {
		var T = 0, L = 0;
		while (who) {
			L += who.offsetLeft;
			T += who.offsetTop;
			who = who.offsetParent;
		}
		return [ L, T ];
	}
};
