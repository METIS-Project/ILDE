


var SelectResourcesDialog = {

    iDontNeedThese: null,
    
    selectedResources: null,
	
    open: function(iDontNeedThese, id){
        this.iDontNeedThese = iDontNeedThese;
        this.selectedResources = new Array();
		
        this.load(id);
        DialogModaler.pushPopups();
    },
    
    load: function(id){
        var fullId = "LDSelectResourcesList_" + id;
        dijit.byId(fullId).destroyDescendants();

        var content = dojo.byId(fullId)
        content.innerHTML = "";
			
        var callback = function(state) {
            SelectResourcesDialog.change(this.resourceid, state);
        };
		
        for (var i in LearningDesign.data.resources) {
            var resource = LearningDesign.data.resources[i];
            if (this.iDontNeedThese.indexOf(resource.id) < 0) {
                var checkbox = CheckBoxManager.createCheckBox(fullId + resource.id, content, resource.title, callback);
                checkbox.resourceid = resource.id;
            }
        }
    },
	
    change: function(resourceid, state) {
        if (state) {
            if (this.selectedResources.indexOf(resourceid) < 0) {
                this.selectedResources.push(resourceid);
            }
        } else {
            var index = this.selectedResources.indexOf(resourceid);
            if (index >= 0) {
                this.selectedResources.splice(index, 1);
            }
        }
    }
};
