
var UndoManager = {
	
    undoCount: 0,
    redoCount: 0,
    undoButton: null,
    redoButton: null,

    init: function() {
        this.undoButton = dijit.byId("toolbar.undo");
        this.redoButton = dijit.byId("toolbar.redo");

        dojo.connect(dojo.byId("toolbar.undo"), "onclick", function(){
            UndoManager.undo();
        });
        dojo.connect(dojo.byId("toolbar.redo"), "onclick", function(){
            UndoManager.redo();
        });
        this.reset();
    },
	
    reset: function() {
        this.undoCount = this.redoCount = 0;
        this.updateButtons();
    },

    updateButtons: function() {
        this.undoButton.set("disabled", this.undoCount == 0);
        this.redoButton.set("disabled", this.redoCount == 0);
    },
	
    newEditionAction: function() {
        this.undoCount++;
        this.redoCount = 0;
        this.updateButtons();
    },
	
    undo: function() {
        if (this.undoCount > 0) {
            Loader.undo();
        }
    },

    redo: function() {
        if (this.redoCount > 0) {
            Loader.redo();
        }
    },
	
    undone: function() {
        this.undoCount--;
        this.redoCount++;
        this.updateButtons();
    },

    redone: function() {
        this.undoCount++;
        this.redoCount--;
        this.updateButtons();
    }
};
