/**
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Web Collage.

Web Collage is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Web Collage is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
