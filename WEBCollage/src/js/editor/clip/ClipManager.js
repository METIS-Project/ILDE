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
* @class Gestor de las acciones pendientes en el diseño
*/
var ClipManager = {

    /**
     * Acciones pendientes de realizar
    */
    todo : null,

    /**
     * Carga las acciones pendientes de realizar
     * @param data Acciones pendientes de realizar
    */
    load : function(data) {
        if (data) {
            this.todo = data;
        }else {
            this.clear();
        }
        ClipActions.update(true);
    },

    /**
     * Obtiene las acciones pendientes de realizar
     * @return Acciones pendientes de realizar
    */
    getTodo : function() {
        return this.todo;
    },

    /**
     * Borra las acciones pendientes de realizar
    */
    clear : function() {
        this.todo = new Array();
        this.actionsCarriedOut("new;");
    },

    actionsCarriedOut : function(str) {
        var actions = str.split(";");
        var alladded = new Array();
        for ( var i = 0; i < actions.length; i++) {
            var action = actions[i].split(":");
            this.clearMatches(action, alladded);
            var added = this.addRequiredActions(action);
            for ( var j = 0; j < added.length; j++) {
                alladded.push(added[j]);
            }
        }

        ClipActions.update(str != "new" && alladded.length > 0);
    },

    removeClearedItems : function() {
        for (var i = this.todo.length - 1; i >= 0; i--) {
            if (this.todo[i].state == "cleared") {
                this.todo.splice(i, 1);
            } else if (this.todo[i].modified) {
                delete this.todo[i].modified;
            }
        }
    },

    clearMatches : function(action) {
        if (action[0] == "delete") {
            this.deleteAction(action[1]);
        } else {
            for ( var i = 0; i < this.todo.length; i++) {
                var todo = this.todo[i];
                if (todo.state == "pending"
                    && this.matches(todo.action, action)) {
                    todo.state = "cleared";
                    todo.modified = "modified";
                }
            }
        }

        this.removeClearedItems();
    },

    /**
     * Borra la acción cuyo id se proporciona
     * @param id Identificador de la acción a borrar
    */
    deleteAction : function(id) {
        for ( var i = this.todo.length - 1; i >= 0; i--) {
            var item = this.todo[i];
            if (item.action.length > 1 && item.action[1] == id) {
                this.todo.splice(i, 1);
            }
        }
    },

    /**
     * Añade una nueva acción a las pendientes de realizar
     * @param action Acción pendiente a añadir
    */
    addRequiredActions : function(action) {
        var newActions = ClipManagerActions.getRequiredActions(action);
        var added = new Array();

        for ( var i = 0; i < newActions.length; i++) {
            var existing = this.find(newActions[i]);
            if (!existing) {
                var newaction = {
                    action : newActions[i],
                    state : "pending",
                    modified : "new"
                };
                this.todo.push(newaction);
                added.push(newaction);
            } else if (existing.state != "pending") {
                existing.state = "pending";
                existing.modified = "modified";
            }
        }

        return added;
    },

    /**
     * Busca si una acción está incluida entre las pendientes
     * @param action Acción a buscar
     * @return La acción o null si no ha sido encontrada
    */
    find : function(action) {
        for ( var i = 0; i < this.todo.length; i++) {
            var todo = this.todo[i];
            if (this.matches(todo.action, action, true)) {
                return this.todo[i];
            }
        }
        return null;
    },

    matches : function(todo, action, strict) {
        for ( var i = 0; i < action.length; i++) {
            if (todo[i] != action[i] && (strict || todo[i] != "*")) {
                return false;
            }
        }
        return true;
    },

    ignore: function(items) {
        for (var i in items) {
            this.todo.splice(this.todo.indexOf(items[i]), 1);
        }
    }
};

var ClipManagerActions = {
    getRequiredActions : function(action) {
        var requiredList = new Array();
        for ( var i = 0; i < this.data.length; i++) {
            var a = this.data[i];
            if (a.action == action[0]) {
                for ( var j = 0; j < a.requires.length; j++) {
                    var requiresAction = a.requires[j];
                    var required = new Array();
                    for ( var k = 0; k < requiresAction.length; k++) {
                        if (typeof requiresAction[k] == "number") {
                            required[k] = action[requiresAction[k]];
                        } else {
                            required[k] = requiresAction[k];
                        }
                    }
                    requiredList.push(required);
                }
            }
        }

        this.addAssessmentPatternActions(action, requiredList);
        return requiredList;
    },

    addAssessmentPatternActions: function(action, list) {
        if (action[0] == "addPattern") {
            var pattern = IDPool.getObject(action[1]);
            var assessment = IDPool.getObject(action[2]);
            
            var assessmentActivity = assessment.getAssessmentActivity();
            var actionKey = pattern.getAction("assessment");
            if (assessmentActivity && actionKey) {
                list.push(["editActivity", assessmentActivity.id, actionKey, pattern.id]);
            }

            var assessedActivity = assessment.getAssessedActivity();
            actionKey = pattern.getAction("assessed");
            if (assessedActivity && actionKey) {
                list.push(["editActivity", assessedActivity.id, actionKey, pattern.id]);
            }
        } else if (action[0] == "setAssessedActivity") {
            assessment = IDPool.getObject(action[1]);
            for (var i = 0; i < assessment.patterns.length; i++) {
                pattern = assessment.patterns[i];
                actionKey = pattern.getAction("assessed");
                if (actionKey) {
                    list.push(["editActivity", action[2], actionKey, pattern.id])
                }
            }
        } else if (action[0] == "setAssessmentActivity") {
            assessment = IDPool.getObject(action[1]);
            for (i = 0; i < assessment.patterns.length; i++) {
                pattern = assessment.patterns[i];
                actionKey = pattern.getAction("assessment");
                if (actionKey) {
                    list.push(["editActivity", action[2], actionKey, pattern.id])
                }
            }
        }
    },

    data:
    [
    {
        action : "new",
        requires : [ [ "setPrerrequisites" ],
        [ "addLO", "*" ], [ "addClfp", "*" ] ]
    },
    {
        action : "addLO",
        requires : [ [ "pursueLO", 1 ], [ "assessLO", 1 ] ]
    },
    {
        action : "addResource",
        requires : [ [ "useResource", 1 ] ]
    },
    {
        action : "addActivity",
        requires : [ [ "editActivity", 1, "general", "*" ] ]
    },
    {
        action : "addAssessment",
        requires : [ [ "setAssessmentActivity", 1, "*" ],
        [ "setAssessedActivity", 1, "*" ],
        [ "addAssessmentFunction", 1 ] ]
    }, {
        action : "setAssessedActivity",
        requires : [ [ "editActivity", 2, "assessed", 1] ]
    }, {
        action : "setAssessmentActivity",
        requires : [ [ "editActivity", 2, "assessment", 1] ]
    }, {
        action : "unsetAssessedActivity",
        requires : [ [ "setAssessedActivity", 1 ] ]
    }, {
        action : "unsetAssessmentActivity",
        requires : [ [ "setAssessmentActivity", 1 ] ]
    }, {
        action : "addSummativeAssessmentFunction",
        requires : [ [ "editSummativeAssessmentFunction", 1 ] ]
    }, {
        action : "addFormativeAssessmentFunction",
        requires : [ [ "editFormativeAssessmentFunction", 1, "*" ] ]
    }, {
        action : "editFormativeAssessmentFunction",
        requires : [ [ "editActivity", 2, "feedback", 1 ] ]
    }, {
        action : "addDiagnosisAssessmentFunction",
        requires : [ [ "editDiagnosisAssessmentFunction", 1, "*" ] ]
    }, {
        action : "editDiagnosisAssessmentFunction",
        requires : [ [ "editActivity", 2, "modify", 1 ] ]
    }
    ]
};