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

/**
 * Author: Eloy
 * Gestor de cambios. Guarda información relativa a las acciones realizadas en la edición del diseño
 */

var ChangeManager = {

    groupDepth : 0,
    groupedChanges : "",

    titleSet : function() {
        this.save("setTitle;");
    },

    prerrequisitesSet : function() {
        this.save("setPrerrequisites;");
    },

    loAdded : function(lo) {
        this.save("addLO:" + lo.id + ";");
    },

    loDeleted : function(lo) {
        this.save(this.getDeleteString(this.deleteObjects(lo)));
    },

    loEdited : function(lo) {
        this.save("editLO:" + lo.id + ";");
    },

    resourceAdded : function(resource) {
        this.save("addResource:" + resource.id + ";");
    },

    resourceDeleted : function(resource) {
        this.save(this.getDeleteString(this.deleteObjects(resource)));
    },

    resourceEdited: function(resource) {
        this.save("editResource:" + resource.id + ";");
    },

    activityDeleted : function(activity) {
        this.save(this.getDeleteString(this.deleteObjects(activity)));
    },

    activityAdded : function(activity) {
        this.save("addActivity:" + activity.id + ";");
    },

    activityEdited : function(activity) {
        this.save("editActivity:" + activity.id + ";");
    },

    actEdited : function(act) {
        this.save("editAct:" + act.id + ";");
    },

    roleAdded : function(role) {
        this.save("addRole:" + role.id + ";");
    },
    roleDeleted : function(role) {
        this.save("deleteRole:" + role.id + ";");
    },
    roleEdited : function(role) {
        this.save("editRole:" + role.id + ";");
    },

    clfpAdded : function(clfp) {
        this.save("addClfp:" + clfp.id + ";");
    },

    clfpRemoved : function(clfp) {
        this.save(this.getDeleteString(this.deleteObjects(clfp)));
    },

    clfpMoved : function(clfp) {
        this.save("moveClfp:" + clfp.id + ";");
    },

    assessmentAdded : function(assessment) {
        this.save("addAssessment:" + assessment.id + ";");
    },

    assessmentDeleted : function(assessment) {
        this.save(this.getDeleteString(this.deleteObjects(assessment)));
    },

    assessmentActivitySet : function(assessment, activity) {
        if (activity) {
            this.save("setAssessmentActivity:" + assessment.id + ":"
                + activity.id + ";");
        } else {
            this.save("unsetAssessmentActivity:" + assessment.id + ";");
        }
    },

    assessedActivitySet : function(assessment, activity) {
        if (activity) {
            this.save("setAssessedActivity:" + assessment.id + ":"
                + activity.id + ";");
        } else {
            this.save("unsetAssessedActivity:" + assessment.id + ";");
        }
    },

    assessmentEdited : function(assessment) {
        this.save("editAssessment:" + assessment.id + ";");
    },

    assessmentFunctionAdded : function(assessmentFunction, assessment) {
        this.startGroup();
        this.save("addAssessmentFunction:" + assessment.id + ";");

        if (assessmentFunction.subtype == "summative") {
            this.save("addSummativeAssessmentFunction:" + assessmentFunction.id
                + ";");
        } else if (assessmentFunction.subtype == "formative") {
            this.save("addFormativeAssessmentFunction:" + assessmentFunction.id
                + ";");
        } else {
            this.save("addDiagnosisAssessmentFunction:" + assessmentFunction.id
                + ";");
        }

        this.endGroup();
    },

    assessmentFunctionDeleted : function(assessmentFunction) {
        this.save(this.getDeleteString(this.deleteObjects(assessmentFunction)));
    },

    assessmentFunctionEdited : function(assessmentFunction, activity) {
        if (assessmentFunction.subtype == "summative") {
            this.save("editSummativeAssessmentFunction:" + assessmentFunction.id
                + ";");
        } else if (assessmentFunction.subtype == "formative") {
            this.save("editFormativeAssessmentFunction:" + assessmentFunction.id
                + ":" + activity.id + ";");
        } else {
            this.save("editDiagnosisAssessmentFunction:" + assessmentFunction.id
                + ":" + activity.id + ";");
        }
    },

    patternAdded : function(pattern, assessment) {
        this.save("addPattern:" + pattern.id + ":" + assessment.id + ";");
    },

    patternDeleted : function(pattern) {
        this.save(this.getDeleteString(this.deleteObjects(pattern)));
    },

    /**
     * Guarda en la base de datos los cambios realizados
     * @param changes Cadena de texto que contiene los cambios realizados
    */
    save : function(changes) {
        if (this.groupDepth == 0) {
            console.log(changes);
            ClipManager.actionsCarriedOut(changes);
            Loader.save(changes);
            //this.updateDisplays();
        } else {
            this.groupedChanges += changes;
        }
    },

    deleteObjects : function(object, list) { /* complete! */
        if (!list) {
            list = new Array();
        }
        if (object) {
            if (object.id) {
                list.push(object.id);
                IDPool.removeObject(object.id);
            }

            for ( var i in object) {
                var child = object[i];
                if (typeof child == "object") {
                    this.deleteObjects(child, list);
                }
            }
        }

        return list;
    },

    getDeleteString : function(list) {
        var str = "";

        for ( var i = 0; i < list.length; i++) {
            str += "delete:" + list[i] + ";";
        }

        return str;
    },

    startGroup : function() {
        this.groupDepth++;
    },

    endGroup : function() {
        this.groupDepth--;
        if (this.groupDepth == 0) {
            this.save(this.groupedChanges);
            this.groupedChanges = "";
        }
    },

    updateDisplays: function() {
        ClipActions.update();
        General.updateDisplay();
        ParticipantManagement.updateDisplay();
        LearningFlow.updateDisplay();
        Resources.updateDisplay();
        TableGenerator.updateDisplay();
    }
};