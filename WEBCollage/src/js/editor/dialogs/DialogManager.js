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

/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

DialogManager = {
    dialogStack: null,

    init: function() {
        var dialogs = [AssessmentPatternSelector, EditActivityDialog, 
        EditAssessmentDialog, EditSummativeAssessmentDialog, 
        EditDocumentDialog, EditToolDialog, 
        EditRoleDialog, PatternSelector, EditToolVLEDialog, 
        OntoolsearchDialog, EditToolKeywordDialog, GroupPatternsDialog];

        this.reset();
    },

    reset: function() {
        this.dialogStack = new Array();
    },

    dialogOpened: function(dialog) {
        var previous = this.getCurrentDialog();
        if (previous) {
            DialogModaler.block(previous, dialog, this.dialogStack.length);
        } else {
            DialogModaler.setZIndex(dialog, this.dialogStack.length);
        }
        this.dialogStack.push(dialog);
    },

    dialogClosed: function() {
        this.dialogStack.pop();
    },

    getCurrentDialog: function() {
        return this.dialogStack.length > 0 ? this.dialogStack[this.dialogStack.length - 1] : null;
    }
};
