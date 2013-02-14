


var SelectLOsDialog = {

    iDontNeedThese: null,
    
    selectedLOs: null,
    
    open: function(iDontNeedThese, id){
        this.iDontNeedThese = iDontNeedThese;
        this.selectedLOs = new Array();
        this.load(id);
        DialogModaler.pushPopups();
    },
    
    load: function(id){
        var fullId = "SelectLOsList_" + id;
        dijit.byId(fullId).destroyDescendants();
        
        var content = dojo.byId(fullId);
        content.innerHTML = "";
        
        var callback = function(state){
            SelectLOsDialog.change(this.loid, state);
        };
        
        for (var i in LearningDesign.data.learningObjectives) {
            var lo = LearningDesign.data.learningObjectives[i];
            if (this.iDontNeedThese.indexOf(lo.id) < 0) {
                var checkbox = CheckBoxManager.createCheckBox(fullId + lo.id, content, lo.title, callback);
                checkbox.loid = lo.id;
            }
        }
    },
    
    change: function(loid, state){
        if (state) {
            if (this.selectedLOs.indexOf(loid) < 0) {
                this.selectedLOs.push(loid);
            }
        }
        else {
            var index = this.selectedLOs.indexOf(loid);
            if (index >= 0) {
                this.selectedLOs.splice(index, 1);
            }
        }
    }
};
