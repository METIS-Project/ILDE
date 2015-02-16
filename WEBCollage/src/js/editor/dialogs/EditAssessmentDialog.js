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

var EditAssessmentDialog = {
    dlg : null,
    assessment : null,
    elements : {
        assessed : null,
        analysis : null,
        functions : []
    },

    assessedActivity : null,
    assessmentActivity : null,
    assessmentFunctions : null,
    assessmentPatterns : null,

    loManager : new ListOfItemsManager("LDAssessmentLOsList"),

    functionsManager : new ListOfItemsManager("LDAssessmentFunctionList"),
    functionNames : null,
    
    tmpPattern: null,

    init : function() {
        this.functionNames = {
            "summative" : i18n.get("assessment.edit.functions.summative"),
            "formative" : i18n.get("assessment.edit.functions.formative"),
            "diagnosis" : i18n.get("assessment.edit.functions.diagnosis")
        };

        this.dlg = dijit.byId("LDAssessmentEditDialog");

        var assessmentEditDialog = dijit.byId("LDAssessmentLOSelector");
        assessmentEditDialog.onOpen = function() {
            EditAssessmentDialog.selectLOs();
        };
        assessmentEditDialog.execute = function() {
            EditAssessmentDialog.selectedLOs(SelectLOsDialog.selectedLOs);
        };

        EditAssessmentPainter.init();
        EditSummativeAssessmentDialog.init();
        
        dojo.connect(dojo.byId("deletePatternDialogOk"), "onclick", function(){
            EditAssessmentDialog.closeDeletePatternDialog();
            EditAssessmentDialog.deletePattern(EditAssessmentDialog.tmpPattern);
        });        
        dojo.connect(dojo.byId("deletePatternDialogCancel"), "onclick", function(){
            EditAssessmentDialog.closeDeletePatternDialog();
        });
    },
    open : function(assessment) {
        this.assessment = assessment;
        this.dlg.show();
        this.load();
    },
    load : function() {
        this.loManager.listOfItems = LearningDesign.activitiesAndLOsMatches.getMatchesFor(this.assessment.id);
        this.assessedActivity = this.assessment.getAssessedActivity();
        this.assessmentActivity = this.assessment.getAssessmentActivity();

        this.assessmentFunctions = this.assessment.getFunctions();
        this.assessmentPatterns = this.getPatterns();

        this.reopen();
        return;
    },
    getPatterns : function() {
        var patterns = {
            all : this.assessment.getPatterns()
        };

        for(var i = 0; i < patterns.all.length; i++) {
            var pattern = patterns.all[i];
            if(pattern.definesAssessmentActivity()) {
                patterns.assessment = pattern;
            }
            if(pattern.definesAssessedActivity()) {
                patterns.assessed = pattern;
            }
        }
        return patterns;
    },
    reopen : function() {
        dojo.byId("LDAssessmentTitle").innerHTML = this.assessment.getTitle();
        dojo.query("#LDAssessmentPaintSurface > div").orphan();

        EditAssessmentPainter.clear();

        this.displaySourceLink();
        this.displayAnalysisLink();
        this.loManager.buildList();
        this.buildFunctionList();

        EditAssessmentPainter.paint();
        this.displayPatterns();
    },
    displayPattern : function(pattern, center, where) {
        var patternShape = EditAssessmentPainter.paintPattern(center, pattern, .5, where);
        /*        var patternElements = ElementFormat.formatEmptyElement(where, pattern
		 .getTitle(), {
		 small : true,
		 centerAt : center
		 });*/

        MenuManager.registerThing(patternShape, {//patternElements.main, {
            getItems : function(data) {
                return EditAssessmentDialog.getPatternMenu(data);
            },
            data : {
                pattern : pattern
            },
            menuStyle : "default"
        });
    },
    displayPatterns : function() {

        var where = dojo.byId("PatternListInAssessmentContents");
        var center = {
            x : 70,
            y : where.offsetHeight / 2
        };
        for(var i = 0; i < this.assessmentPatterns.all.length; i++) {
            var pattern = this.assessmentPatterns.all[i];
            if(pattern != this.assessmentPatterns.assessment) {
                this.displayPattern(pattern, center, "secondary");
                center.x += 80;
            }
        }
    },
    displaySourceLink : function() {
        var where = dojo.byId("LDAssessmentPaintSurface");
        var center = {
            x : where.offsetWidth / 6,
            y : where.offsetHeight / 2
        };

        if(this.elements.assessed) {
            dojo.query(this.elements.assessed).orphan();
        }

        if(this.assessedActivity) {
            var elements = ElementFormat.formatElement(where, this.assessedActivity, {
                small : true,
                activityTooltip : true,
                centerAt : center
            });

            new EditAssessmentDialogActiveObjectListener(elements.main, this.assessedActivity.id);

            ClipRenderer.paint(EditAssessmentPainter.mainSurface, null, false, [this.assessedActivity.id], {
                x : center.x - 70,
                y : center.y - 25
            });
        } else {
            elements = ElementFormat.formatEmptyElement(where, i18n.get("assessment.link.choose"), {
                centerAt : center
            });
        }

        center.y -= 50;
        if(this.assessmentPatterns.assessed) {
            center.x += 20;
            this.displayPattern(this.assessmentPatterns.assessed, center, "main");
            center.x -= 40;
        }
        EditAssessmentPainter.paintAssessmentRole(center, this.assessedActivity);

        this.elements.assessed = elements.container;
        EditAssessmentPainter.assessedNode = elements.main;

        MenuManager.registerThing(elements.main, {
            getItems : function() {
                return EditAssessmentDialog.getAssessedMenu();
            },
            menuStyle : "default"
        });
    },
    displayAnalysisLink : function() {
        var where = dojo.byId("LDAssessmentPaintSurface");
        var center = {
            x : 3 * where.offsetWidth / 6,
            y : where.offsetHeight / 2
        };

        if(this.elements.analysis) {
            dojo.query(this.elements.analysis).orphan();
        }

        if(this.assessmentActivity) {
            var elements = ElementFormat.formatElement(where, this.assessmentActivity, {
                small : true,
                activityTooltip : true,
                centerAt : center
            });

            new EditAssessmentDialogActiveObjectListener(elements.main, this.assessmentActivity.id);

            ClipRenderer.paint(EditAssessmentPainter.mainSurface, null, false, [this.assessmentActivity.id], {
                x : center.x - 70,
                y : center.y - 25
            });
        } else {
            elements = ElementFormat.formatEmptyElement(where, i18n.get("assessment.link.choose"), {
                centerAt : center
            });
        }

        center.y -= 50;
        if(this.assessmentPatterns.assessment) {
            center.x += 20;
            this.displayPattern(this.assessmentPatterns.assessment, center, "main");
            center.x -= 40;
        }
        EditAssessmentPainter.paintAssessmentRole(center, this.assessmentActivity, true);

        this.elements.analysis = elements.container;
        EditAssessmentPainter.assessmentNode = elements.main;

        MenuManager.registerThing(elements.main, {
            getItems : function() {
                return EditAssessmentDialog.getAssessmentMenu();
            },
            menuStyle : "default"
        });
    },
    /* edit functions */
    selectLOs : function() {
        SelectLOsDialog.open(this.loManager.listOfItems, "assessment");
    },
    selectedLOs : function(list) {
        for(var i in list) {
            this.loManager.listOfItems.push(list[i]);
        }
        this.loManager.buildList();
    },
    /* function list */
    addFunction : function(type) {
        var newFunction = new AssessmentFunction(type);
        this.assessmentFunctions.push(newFunction);
        this.reopen();
        ChangeManager.assessmentFunctionAdded(newFunction, this.assessment);
    },
    removeFunction : function(assessmentFunction) {
        var oldFunction = this.assessmentFunctions.splice(this.assessmentFunctions.indexOf(assessmentFunction), 1);
        this.reopen();
        ChangeManager.assessmentFunctionDeleted(oldFunction);
    },
    buildFunctionList : function() {
        EditAssessmentPainter.functionNodes = [];
        EditAssessmentPainter.functionTypes = [];

        var where = dojo.byId("LDAssessmentPaintSurface");
        var center = {
            x : 5 * where.offsetWidth / 6,
            y : 35
        };

        for(var i = 0; i < this.elements.functions.length; i++) {
            dojo.query(this.elements.functions[i]).orphan();
        }

        for(i in this.assessmentFunctions) {
            var assessmentFunction = this.assessmentFunctions[i];
            var elements = ElementFormat.formatAssessmentFunctionElement(where, assessmentFunction, {
                centerAt : center
            });
            this.elements.functions.push(elements.container);
            EditAssessmentPainter.functionNodes.push(elements.main);
            EditAssessmentPainter.functionTypes.push(assessmentFunction.subtype);

            if(IDPool.getObject(assessmentFunction.link)) {
                ClipRenderer.paint(EditAssessmentPainter.mainSurface, null, false, [assessmentFunction.link], {
                    x : center.x - 70,
                    y : center.y - 25
                });

                new EditAssessmentDialogActiveObjectListener(elements.main, assessmentFunction.link);
            }

            center.y += 55;

            MenuManager.registerThing(elements.main, {
                getItems : function(data) {
                    return EditAssessmentDialog.getFunctionMenu(data);
                },
                data : {
                    assessmentFunction : assessmentFunction
                },
                menuStyle : "default"
            });
        }
        dojo.style("LDAssessmentPaintSurface", "height", Math.max(200, center.y - 20) + "px");
    },
    activate : function(id) {
        var obj = IDPool.getObject(id);
        if(obj.type == "activity") {
            EditActivityDialog.open(obj);
        }
    },
    editSummativeAssessment : function(assessmentFunction) {
        EditSummativeAssessmentDialog.open(assessmentFunction);
    },
    /* patterns */
    addAssessmentPattern : function(reference) {
        var callback = function(reference, pattern) {
            EditAssessmentDialog.chosenAssessmentPattern(reference, pattern);
        };
        var whatfor = reference.subtype ? reference.subtype : reference;

        AssessmentPatternSelector.open(callback, reference, whatfor, this.assessmentPatterns.all);
    },
    chosenAssessmentPattern : function(reference, patternDef) {
        
        var conflicts = AssessmentConflicts.check({
            assessmentActivity : this.assessmentActivity,
            assessedActivity : this.assessedActivity,
            assessmentPatterns : this.assessmentPatterns
        }, patternDef);
        
        ChangeManager.startGroup();

        if(conflicts!= null && conflicts.patterns) {
            for(var i = 0; i < conflicts.patterns.length; i++) {
                var pattern = conflicts.patterns[i];
                var index = this.assessmentPatterns.all.indexOf(pattern);
                this.assessmentPatterns.all.splice(index, 1);
                ChangeManager.patternDeleted(pattern);

                if(this.assessmentPatterns.assessment == pattern) {
                    this.assessmentPatterns.assessment = null;
                }

                if(this.assessmentPatterns.assessed == pattern) {
                    this.assessmentPatterns.assessed = null;
                }

            }
        }
        pattern = new AssessmentPattern(patternDef);
        this.assessmentPatterns.all.push(pattern);
        ChangeManager.patternAdded(pattern, this.assessment);

        if(pattern.definesAssessmentActivity()) {
            this.assessmentPatterns.assessment = pattern;
            //if(choices["assessment"] == "delete") {
             //   LearningDesign.removeActivity(this.assessmentActivity);
             //   this.assessment.setAssessmentActivity(this.assessmentActivity = null);
             //   ChangeManager.assessmentActivitySet(this.assessment, null);
            //} else if(choices["assessment"] == "replace") {
                this.configActivityAccordingToPattern("assessment", this.assessmentActivity, pattern);
            //}
        }

        if(pattern.definesAssessedActivity()) {
            this.assessmentPatterns.assessed = pattern;
            //if(choices["assessed"] == "delete") {
             //   LearningDesign.removeActivity(this.assessedActivity);
            //    this.assessment.setAssessedActivity(this.assessedActivity = null);
            //    ChangeManager.assessedActivitySet(this.assessment, null);
            //} else if(choices["assessed"] == "replace") {
                this.configActivityAccordingToPattern("assessed", this.assessedActivity, pattern);
           // }
        }

        ChangeManager.endGroup();
        this.reopen();
    },
    configActivityAccordingToPattern : function(rel, activity, pattern) {
        var title = rel == "assessment" ? pattern.getAssessmentActivityTitle() : pattern.getAssessedActivityTitle();
        activity.title = title;
        ChangeManager.activityEdited(activity);
    },
    
    
    openDeletePatternDialog: function(pattern){
        this.tmpPattern = pattern;
        dijit.byId("deletePatternDialog").show();
    },
    
    closeDeletePatternDialog: function(){
        dijit.byId("deletePatternDialog").hide();
    },
    
    deletePattern : function(pattern) {
        var index = this.assessmentPatterns.all.indexOf(pattern);
        this.assessmentPatterns.all.splice(index, 1);
        if(this.assessmentPatterns.assessment == pattern) {
            this.assessmentPatterns.assessment = null;
        }

        if(this.assessmentPatterns.assessed == pattern) {
            this.assessmentPatterns.assessed = null;
        }

        ChangeManager.patternDeleted(pattern);

        this.reopen();
        this.tmpPattern=null;
    },
    selectGetPatternMenu : function(data) {
        return EditAssessmentDialog.getPatternMenu(data);
    },
    /* menus */
    getPatternMenu : function(data) {
        var items = [];

        if(data) {
            items.push({
                label : i18n.get("assessment.patterns.delete"),
                onClick : function(data) {
                    EditAssessmentDialog.openDeletePatternDialog(data.pattern);
                },
                icon : "delete",
                data : data,
                help : i18n.get("help.assessment.patterns.delete")
            });

            items.push({
                label : dojo.string.substitute(i18n.get("common.pattern.doc"), [data.pattern.getTitle()]),
                icon : "help",
                onClick : function(data) {
                    AssessmentPatternSelector.showHelp(data.pattern.getName());
                },
                data : data,
                help : i18n.get("help.common.pattern.doc")
            });
        }
        return items;
    },
    getPatternMenuSee : function(data) {
        var items = [];

        if(data) {
            items.push({
                label : dojo.string.substitute(i18n.get("common.pattern.doc"), [data.pattern.getTitle()]),
                icon : "help",
                onClick : function(data) {
                    AssessmentPatternSelector.showHelp(data.pattern.getName());
                },
                data : data,
                help : i18n.get("help.common.pattern.doc")
            });
        }
        return items;
    },

    getFunctionMenu : function(data) {
        var items = [];

        if(data.assessmentFunction.subtype == "summative") {
            items.push({
                label : i18n.get("assessment.edit.summativeFunction.edit"),
                onClick : function(assessmentFunction) {
                    EditAssessmentDialog.editSummativeAssessment(assessmentFunction);
                },
                icon : "editassessment",
                data : data.assessmentFunction,
                help : i18n.get("help.assessment.edit.summativeFunction.edit")
            });
        } else if(data.assessmentFunction.subtype == "formative") {
            items.push({
                label : i18n.get("assessment.edit.formativeFunction.edit"),
                onClick : function(assessmentFunction) {
                    EditAssessmentDialog.selectActivity("formative", assessmentFunction);
                },
                icon : "editlearningactivity",
                data : data.assessmentFunction,
                help : i18n.get("help.assessment.edit.formativeFunction.edit")
            });
        } else {
            items.push({
                label : i18n.get("assessment.edit.diagnosisFunction.edit"),
                onClick : function(assessmentFunction) {
                    EditAssessmentDialog.selectActivity("diagnosis", assessmentFunction);
                },
                icon : "editsupportactivity",
                data : data.assessmentFunction,
                help : i18n.get("help.assessment.edit.diagnosisFunction.edit")
            });
        }

        items.push({
            isSeparator : true
        });

        items.push({
            label : i18n.get("assessment.edit.function.remove"),
            onClick : function(assessmentFunction) {
                EditAssessmentDialog.removeFunction(assessmentFunction);
            },
            icon : "delete",
            data : data.assessmentFunction,
            help : i18n.get("help.assessment.edit.function.remove")
        });

        return items;
    },
    getAssessmentMenu : function() {
        var items = new Array();

        var treeCallback = function(reference, node) {
            EditAssessmentDialog.activitySelected(reference, node);
        };
        if(this.assessmentActivity == null) {
            items.push({
                label : i18n.get("assessment.edit.analysis.choose"),
                onClick : function() {
                    EditAssessmentDialog.selectActivity("analisys");
                },
                icon : "select",
                help : i18n.get("help.assessment.edit.analysis.choose")
            });
        } else {
            items.push({
                label : i18n.get("assessment.edit.analysis.change"),
                onClick : function() {
                    EditAssessmentDialog.selectActivity("analysis");
                },
                icon : "select",
                help : i18n.get("help.assessment.edit.analysis.change")
            });

        }

        items.push({
            isSeparator : true
        });
        items.push({
            label : i18n.get("assessment.edit.analysis.configpattern"),
            onClick : function() {
                EditAssessmentDialog.addAssessmentPattern("assessment");
            },
            icon : "editassessment",
            help : i18n.get("help.assessment.edit.analysis.configpattern")
        });

        return items;
    },

    getAssessedMenu : function() {
        var items = new Array();

        if(this.assessedActivity == null) {
            items.push({
                label : i18n.get("assessment.edit.source.choose"),
                onClick : function() {
                    EditAssessmentDialog.selectActivity("source");
                },
                icon : "select",
                help : i18n.get("help.assessment.edit.source.choose")
            });
        } else {
            items.push({
                label : i18n.get("assessment.edit.source.change"),
                onClick : function() {
                    EditAssessmentDialog.selectActivity("source");
                },
                icon : "select",
                help : i18n.get("help.assessment.edit.source.change")
            });
        }

        items.push({
            isSeparator : true
        });
        items.push({
            label : i18n.get("assessment.edit.source.configpattern"),
            onClick : function() {
                EditAssessmentDialog.addAssessmentPattern("assessed");
            },
            icon : "editlearningactivity",
            help : i18n.get("help.assessment.edit.source.configpattern")
        });

        return items;
    },

    /* bye bye */
    ok : function() {
        this.save();
        this.close();
    },
    cancel : function() {
        this.close();
    },
    close : function() {
        this.dlg.hide();
    },
    save : function() {
        ChangeManager.startGroup();
        ChangeManager.assessmentEdited(this.assessment);
        if(this.assessedActivity != this.assessment.getAssessedActivity()) {
            this.assessment.setAssessedActivity(this.assessedActivity);
            ChangeManager.assessedActivitySet(this.assessment, this.assessedActivity);
        }
        if(this.assessmentActivity != this.assessment.getAssessmentActivity()) {
            this.assessment.setAssessmentActivity(this.assessmentActivity);
            ChangeManager.assessmentActivitySet(this.assessment, this.assessmentActivity);
        }

        LearningDesign.activitiesAndLOsMatches.setMatchesFor(this.assessment.id, this.loManager.listOfItems);

        ChangeManager.endGroup();
    },
    
    selectActivity : function(target, assessfunction) {
        var selectData = {
            callback : function(object, data) {
                EditAssessmentDialog.activitySelected(object, data);
            },
            callbackData: {
                target : target,
                assessfunction : assessfunction
            },
            title : i18n.get("assessment.selectActivity")
        };
        
        this.dlg.hide();
        LearningFlowSelectMode.startSelect("activity", selectData);
    },
    
    activitySelected : function(activity, data) {
        this.dlg.show();
        if (activity) {
            switch(data.target) {
                case "source":
                    this.assessedActivity = activity;
                    break;
                case "analysis":
                    this.assessmentActivity = activity;
                    break;
                case "diagnosis":
                case "formative":
                    data.assessfunction.link = activity.id;
                    ChangeManager.assessmentFunctionEdited(data.assessfunction, activity);
                    break;
                default:
                    break;
            }
        }
        this.reopen();        
    }
};

var EditAssessmentDialogActiveObjectListener = function(object, id) {
    this.id = id;
    dojo.connect(object, "onclick", this, "clicked");
};

EditAssessmentDialogActiveObjectListener.prototype.clicked = function() {
    EditAssessmentDialog.activate(this.id);
};
var EditSummativeAssessmentDialog = {
    dlg : null,
    assessmentFunction : null,

    init : function() {
        this.dlg = dijit.byId("LDSummativeAssessmentEditDialog");
    },
    open : function(assessmentFunction) {
        this.assessmentFunction = assessmentFunction;
        dijit.byId("SummativeAssessmentEditName").set("value", assessmentFunction.name);
        dijit.byId("SummativeAssessmentEditDescription").set("value", assessmentFunction.description);
        this.dlg.show();
    },
    save : function() {
        this.assessmentFunction.name = dijit.byId("SummativeAssessmentEditName").get("value");
        this.assessmentFunction.description = dijit.byId("SummativeAssessmentEditDescription").get("value");
        ChangeManager.assessmentFunctionEdited(this.assessmentFunction);
        this.close();
    },
    cancel : function() {
        this.close();
    },
    close : function() {
        this.dlg.hide();
    }
};
