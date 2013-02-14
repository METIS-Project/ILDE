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
