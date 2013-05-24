<<<<<<< .mine
var ClipActions = {

    actionsInGeneralGroup : {
        "setTitle" : "title",
        "setPrerrequisites" : "prerrequisites",
        "addLO" : "los",
        "addClfp" : "flow"
    },

    update : function(thereAreNew) {
        var data = this.getActionData(ClipManager.todo);
        this.setTexts(data.groups);
        this.setStates(data.groups);
        ClipDisplay.update(data, thereAreNew);
    },
    getActionData : function(items) {
        var groups = {};
        var actions = {};

        for(var i = 0; i < items.length; i++) {
            var item = items[i];
            if(item != null && item.state != "ignored") {

                var actionData = null;

                var needToSet = true;

                if(item.action[0] in this.actionsInGeneralGroup) {
                    actionData = {
                        group : "general",
                        link : this.actionsInGeneralGroup[item.action[0]]
                    };
                } else if(item.action[0] == "pursueLO" || item.action[0] == "assessLO") {
                    if(item.action[1] in actions) {
                        actionData = actions[item.action[1]];
                        needToSet = false;
                    } else {
                        actionData = {
                            group : "los",
                            link : item.action[1],
                            reasons : new Array()
                        };
                    }

                    if(item.state == "pending") {
                        actionData.reasons.push(item.action[0]);
                    }
                } else if(item.action[0] == "useResource") {
                    actionData = {
                        group : "resources",
                        link : item.action[1]
                    };
                } else if(item.action[0] == "editActivity") {
                    if(item.action[1] in actions) {
                        actionData = actions[item.action[1]];
                        needToSet = false;
                    } else {
                        var pos = LearningDesign.findActParentOfActivity(item.action[1]);
                        if(pos) {
                            actionData = {
                                group : pos.clfp.id,
                                link : item.action[1],
                                reasons : new Array()
                            };
                        }
                    }

                    if(actionData && item.state == "pending") {
                        if(item.action[3] == "*" || IDPool.getObject(item.action[3])) {
                            actionData.reasons.push([item.action[2], item.action[3]]);
                        }
                    }
                } else if(item.action[0] == "setAssessmentActivity" || item.action[0] == "setAssessedActivity" || item.action[0] == "addAssessmentFunction") {
                    if(item.action[1] in actions) {
                        actionData = actions[item.action[1]];
                        needToSet = false;
                    } else {
                        actionData = {
                            group : "assessments",
                            link : item.action[1],
                            reasons : new Array()
                        };
                    }

                    if(item.state == "pending") {
                        actionData.reasons.push(item.action[0]);
                    }
                } else if(item.action[0] == "editSummativeAssessmentFunction" || item.action[0] == "editFormativeAssessmentFunction" || item.action[0] == "editDiagnosisAssessmentFunction") {
                    pos = LearningDesign.findAssessmentParentOfFunction(item.action[1]);
                    if(pos) {
                        var actionId = pos.id;

                        if( actionId in actions) {
                            actionData = actions[actionId];
                            needToSet = false;
                        } else {
                            actionData = {
                                group : "assessments",
                                link : actionId,
                                reasons : new Array()
                            };
                        }

                        if(item.state == "pending" && actionData.reasons.indexOf(item.action[0]) < 0) {
                            actionData.reasons.push(item.action[0]);
                        }
                    }
                }

                if(actionData) {
                    if(needToSet) {
                        this.getGroup(groups, actionData.group).contents.push(actionData);
                        actions[actionData.link] = actionData;
                        actionData.items = new Array();
                    }

                    actionData.items.push(item);
                }
            }
        }

        return {
            groups : groups,
            actions : actions
        };
    },
    getGroup : function(groups, groupId) {
        if(!( groupId in groups)) {
            groups[groupId] = {
                text : this.getGroupText(groupId),
                contents : new Array()
            };
        }

        return groups[groupId];
    },
    /********* texts **************/

    setTexts : function(groups) {
        for(var key in groups) {
            var group = groups[key];
            group.text = this.getGroupText(key);

            for(var i = 0; i < group.contents.length; i++) {
                var action = group.contents[i];
                this.setActionText(action);
            }
        }
    },
    getActionText : function(link) {
        switch(link) {
            case "title":
                return i18n.get("clip.actions.text.title");
            case "prerrequisites":
                return i18n.get("clip.actions.text.prerrequisites");
            case "los":
                return i18n.get("clip.actions.text.los");
            case "flow":
                return i18n.get("clip.actions.text.flow");
        }
    },
    getActionObjectText : function(type) {
        switch(type) {
            case "activity":
                return i18n.get("clip.actions.text.object.activity");
            case "resource":
                return i18n.get("clip.actions.text.object.resource");
            case "lo":
                return i18n.get("clip.actions.text.object.lo");
            case "assessmentflow":
                return i18n.get("clip.actions.text.object.assessmentflow");
        }
    },
    getActionObjectRefText : function(type, title) {
        switch(type) {
            case "activity":
                return i18n.getReplaced1("clip.actions.text.object.ref.activity", title);
            case "resource":
                return i18n.getReplaced1("clip.actions.text.object.ref.resource", title);
            case "lo":
                return i18n.getReplaced1("clip.actions.text.object.ref.lo", title);
            case "assessmentflow":
                return i18n.getReplaced1("clip.actions.text.object.ref.assessmentflow", title);
        }
    },
    getReasonText : function(reason, extra) {
        var key = "";
        switch(reason) {
            case "setAssessmentActivity":
                key = "clip.actions.reason.setAssessmentActivity";
                break;
            case "setAssessedActivity":
                key = "clip.actions.reason.setAssessedActivity";
                break;
            case "addAssessmentFunction":
                key = "clip.actions.reason.addAssessmentFunction";
                break;
            case "editSummativeAssessmentFunction":
                key = "clip.actions.reason.editSummativeAssessmentFunction";
                break;
            case "editFormativeAssessmentFunction":
                key = "clip.actions.reason.editFormativeAssessmentFunction";
                break;
            case "editDiagnosisAssessmentFunction":
                key = "clip.actions.reason.editDiagnosisAssessmentFunction";
                break;

            case "general":
                key = "clip.actions.reason.general";
                break;
            case "feedback":
                key = "clip.actions.reason.feedback";
                break;
            case "modify":
                key = "clip.actions.reason.modify";
                break;
            case "assessed":
                key = "clip.actions.reason.assessed";
                break;
            case "assessment":
                key = "clip.actions.reason.assessment";
                break;
            case "pursueLO":
                key = "clip.actions.reason.pursueLO";
                break;
            case "assessLO":
                key = "clip.actions.reason.assessLO";
                break;

            case "assessedAsTest":
                key = "clip.actions.reason.assessedAsTest";
                break;
            case "assessmentAsTest":
                key = "clip.actions.reason.assessmentAsTest";
                break;
            case "assessmentAsObservation":
                key = "clip.actions.reason.assessmentAsObservation";
                break;
            case "assessedAsReportreview":
                key = "clip.actions.reason.assessedAsReportreview";
                break;
            case "assessmentAsReportreview":
                key = "clip.actions.reason.assessmentAsReportreview";
                break;
            case "assessedAsPresentation":
                key = "clip.actions.reason.assessedAsPresentation";
                break;
            case "assessmentAsPresentation":
                key = "clip.actions.reason.assessmentAsPresentation";
                break;
            case "assessedAsPerformance":
                key = "clip.actions.reason.assessedAsPerformance";
                break;
            case "assessmentAsPerformance":
                key = "clip.actions.reason.assessmentAsPerformance";
                break;
            case "assessedAsPortfolio":
                key = "clip.actions.reason.assessedAsPortfolio";
                break;
            case "assessmentAsPortfolio":
                key = "clip.actions.reason.assessmentAsPortfolio";
                break;

            case "assessmentAsRoles":
                key = "clip.actions.reason.assessmentAsRoles";
                break;
            case "assessmentAsRubric":
                key = "clip.actions.reason.assessmentAsRubric";
                break;
            case "assessedAsShared":
                key = "clip.actions.reason.assessedAsShared";
                break;
            case "assessmentAsShared":
                key = "clip.actions.reason.assessmentAsShared";
                break;
            case "assessmentAsSelf":
                key = "clip.actions.reason.assessmentAsSelf";
                break;
            case "assessmentAsAnonymous":
                key = "clip.actions.reason.assessmentAsAnonymous";
                break;
            case "assessmentAsIA":
                key = "clip.actions.reason.assessmentAsIA";
                break;
            case "assessmentAsRandom":
                key = "clip.actions.reason.assessmentAsRandom";
                break;
        }

        return extra === undefined ? i18n.get(key) : i18n.getReplaced1(key, extra);
    },
    setActionText : function(data) {//type, link) {
        if(data.items[0].action[0] in this.actionsInGeneralGroup) {
            data.text = this.getActionText(data.link);
        } else {
            var obj = IDPool.getObject(data.link);
            data.text = this.getActionObjectText(obj.type);
            data.reftext = this.getActionObjectRefText(obj.type, obj.getTitle());
        }

        if(data.reasons && data.reasons.length > 0) {
            var reasons = "<ul>";
            for(var i = 0; i < data.reasons.length; i++) {
                var reason = data.reasons[i];
                var text = null;

                if(data.group == "assessments" || data.group == "los") {
                    text = this.getReasonText(reason);
                } else if(reason[0] == "general" || reason[0] == "assessment") {
                    text = this.getReasonText(reason[0]);
                } else {
                    var assessment = (reason[0] == "feedback" || reason[0] == "modify") ? LearningDesign.findAssessmentParentOfFunction(reason[1]) : IDPool.getObject(reason[1]);
                    text = this.getReasonText(reason[0], assessment.getTitle());
                }
                reasons += "<li>" + text + "</li>";
            }
            reasons += "</ul>";

            data.reasonText = reasons;
        }
    },
    getGroupText : function(key) {
        switch (key) {
            case  "general":
                return i18n.get("clip.actions.groups.general");
            case "los":
                return i18n.get("clip.actions.groups.los");
            case "resources":
                return i18n.get("clip.actions.groups.resources");
            case "assessments":
                return i18n.get("clip.actions.groups.assessments");
            default:
                var obj = IDPool.getObject(key);
                return obj.type == "clfp" ? i18n.getReplaced1("clip.actions.groups.clfp", obj.getTitle()) : null;
        }
    },
    /******** states *************/

    setStates : function(groups) {
        for(var i in groups) {
            var group = groups[i];

            for(var j in group.contents) {
                this.setState(group.contents[j].items, group.contents[j]);
            }

            this.setState(group.contents, group);
        }
    },
    setState : function(list, object) {
        object.state = "pending";
        object.modified = "";
        for(var i in list) {
            var item = list[i];

            if(object.modified == "") {
                if(item.modified) {
                    object.modified = item.modified;
                }
            } else if(object.modified == "modified") {
                if(item.modified == "new") {
                    object.modified = "new";
                }
            }
        }
    },
    /********** ignore ************/

    ignoreAllInGroup : function(group) {
        this.ignoreAllThese(group.contents);
    },
    ignoreAction : function(action) {
        ClipManager.ignore(action.items);
        ChangeManager.updateDisplays();
    },
    ignoreAllInArray : function(ids) {
        var actions = new Array();
        for(var i = 0; i < ids.length; i++) {
            actions.push(ClipDisplay.data.actions[ids[i]]);
        }

        this.ignoreAllThese(actions);
    },
    ignoreAllThese : function(actions) {
        for(var i = 0; i < actions.length; i++) {
            ClipManager.ignore(actions[i].items);
        }

        ChangeManager.updateDisplays();
    },
    /******** activate ***************/

    activate : function(action) {
        var currentDialog = DialogManager.getCurrentDialog();
        var obj = IDPool.getObject(action.link);

        if(currentDialog == null) {
            var tab = "";
            if(action.group == "general") {
                tab = action.link == "flow" ? "MainWindowFlowTab" : "MainWindowGeneralTab";
            } else if(action.group == "los") {
                tab = "MainWindowGeneralTab";
            } else if(action.group == "resources") {
                tab = "MainWindowResourcesTab";
            } else {
                tab = "MainWindowFlowTab";
            }

            dijit.byId("MainWindowTabContainer").selectChild(tab);

            if(action.link == "los") {
                dijit.byId('LDLearningObjectiveAddDialog').show();
            } else if(action.link == "flow") {
                LearningFlow.includePattern(-1);
            }
        }

        if(obj) {
            if(obj.type == "assessmentflow") {
                if(currentDialog == null) {
                    EditAssessmentDialog.open(obj);
                }
            } else if(obj.type == "activity") {
                if(currentDialog == null) {
                    EditActivityDialog.open(obj);
                }
            }
        }
    }
};
=======
var ClipActions = {

    actionsInGeneralGroup : {
        "setTitle" : "title",
        "setPrerrequisites" : "prerrequisites",
        "addLO" : "los",
        "addClfp" : "flow"
    },

    update : function(thereAreNew) {
        var data = this.getActionData(ClipManager.todo);
        this.setTexts(data.groups);
        this.setStates(data.groups);
        ClipDisplay.update(data, thereAreNew);
    },
    getActionData : function(items) {
        var groups = {};
        var actions = {};

        for(var i = 0; i < items.length; i++) {
            var item = items[i];
            if(item != null && item.state != "ignored") {

                var actionData = null;

                var needToSet = true;

                if(item.action[0] in this.actionsInGeneralGroup) {
                    actionData = {
                        group : "general",
                        link : this.actionsInGeneralGroup[item.action[0]]
                    };
                } else if(item.action[0] == "pursueLO" || item.action[0] == "assessLO") {
                    if(item.action[1] in actions) {
                        actionData = actions[item.action[1]];
                        needToSet = false;
                    } else {
                        actionData = {
                            group : "los",
                            link : item.action[1],
                            reasons : new Array()
                        };
                    }

                    if(item.state == "pending") {
                        actionData.reasons.push(item.action[0]);
                    }
                } else if(item.action[0] == "useResource") {
                    actionData = {
                        group : "resources",
                        link : item.action[1]
                    };
                } else if(item.action[0] == "editActivity") {
                    if(item.action[1] in actions) {
                        actionData = actions[item.action[1]];
                        needToSet = false;
                    } else {
                        var pos = LearningDesign.findActParentOfActivity(item.action[1]);
                        if(pos) {
                            actionData = {
                                group : pos.clfp.id,
                                link : item.action[1],
                                reasons : new Array()
                            };
                        }
                    }

                    if(actionData && item.state == "pending") {
                        if(item.action[3] == "*" || IDPool.getObject(item.action[3])) {
                            actionData.reasons.push([item.action[2], item.action[3]]);
                        }
                    }
                } else if(item.action[0] == "setAssessmentActivity" || item.action[0] == "setAssessedActivity" || item.action[0] == "addAssessmentFunction") {
                    if(item.action[1] in actions) {
                        actionData = actions[item.action[1]];
                        needToSet = false;
                    } else {
                        actionData = {
                            group : "assessments",
                            link : item.action[1],
                            reasons : new Array()
                        };
                    }

                    if(item.state == "pending") {
                        actionData.reasons.push(item.action[0]);
                    }
                } else if(item.action[0] == "editSummativeAssessmentFunction" || item.action[0] == "editFormativeAssessmentFunction" || item.action[0] == "editDiagnosisAssessmentFunction") {
                    pos = LearningDesign.findAssessmentParentOfFunction(item.action[1]);
                    if(pos) {
                        var actionId = pos.id;

                        if( actionId in actions) {
                            actionData = actions[actionId];
                            needToSet = false;
                        } else {
                            actionData = {
                                group : "assessments",
                                link : actionId,
                                reasons : new Array()
                            };
                        }

                        if(item.state == "pending" && actionData.reasons.indexOf(item.action[0]) < 0) {
                            actionData.reasons.push(item.action[0]);
                        }
                    }
                }

                if(actionData) {
                    if(needToSet) {
                        this.getGroup(groups, actionData.group).contents.push(actionData);
                        actions[actionData.link] = actionData;
                        actionData.items = new Array();
                    }

                    actionData.items.push(item);
                }
            }
        }

        return {
            groups : groups,
            actions : actions
        };
    },
    getGroup : function(groups, groupId) {
        if(!( groupId in groups)) {
            groups[groupId] = {
                text : this.getGroupText(groupId),
                contents : new Array()
            };
        }

        return groups[groupId];
    },
    /********* texts **************/

    setTexts : function(groups) {
        for(var key in groups) {
            var group = groups[key];
            group.text = this.getGroupText(key);

            for(var i = 0; i < group.contents.length; i++) {
                var action = group.contents[i];
                this.setActionText(action);
            }
        }
    },
    getActionText : function(link) {
        switch(link) {
            case "title":
                return i18n.get("clip.actions.text.title");
            case "prerrequisites":
                return i18n.get("clip.actions.text.prerrequisites");
            case "los":
                return i18n.get("clip.actions.text.los");
            case "flow":
                return i18n.get("clip.actions.text.flow");
        }
    },
    getActionObjectText : function(type) {
        switch(type) {
            case "activity":
                return i18n.get("clip.actions.text.object.activity");
            case "resource":
                return i18n.get("clip.actions.text.object.resource");
            case "lo":
                return i18n.get("clip.actions.text.object.lo");
            case "assessmentflow":
                return i18n.get("clip.actions.text.object.assessmentflow");
        }
    },
    getActionObjectRefText : function(type, title) {
        switch(type) {
            case "activity":
                return i18n.getReplaced1("clip.actions.text.object.ref.activity", title);
            case "resource":
                return i18n.getReplaced1("clip.actions.text.object.ref.resource", title);
            case "lo":
                return i18n.getReplaced1("clip.actions.text.object.ref.lo", title);
            case "assessmentflow":
                return i18n.getReplaced1("clip.actions.text.object.ref.assessmentflow", title);
        }
    },
    getReasonText : function(reason, extra) {
        var key = "";
        switch(reason) {
            case "setAssessmentActivity":
                key = "clip.actions.reason.setAssessmentActivity";
                break;
            case "setAssessedActivity":
                key = "clip.actions.reason.setAssessedActivity";
                break;
            case "addAssessmentFunction":
                key = "clip.actions.reason.addAssessmentFunction";
                break;
            case "editSummativeAssessmentFunction":
                key = "clip.actions.reason.editSummativeAssessmentFunction";
                break;
            case "editFormativeAssessmentFunction":
                key = "clip.actions.reason.editFormativeAssessmentFunction";
                break;
            case "editDiagnosisAssessmentFunction":
                key = "clip.actions.reason.editDiagnosisAssessmentFunction";
                break;

            case "general":
                key = "clip.actions.reason.general";
                break;
            case "feedback":
                key = "clip.actions.reason.feedback";
                break;
            case "modify":
                key = "clip.actions.reason.modify";
                break;
            case "assessed":
                key = "clip.actions.reason.assessed";
                break;
            case "assessment":
                key = "clip.actions.reason.assessment";
                break;
            case "pursueLO":
                key = "clip.actions.reason.pursueLO";
                break;
            case "assessLO":
                key = "clip.actions.reason.assessLO";
                break;

            case "assessedAsTest":
                key = "clip.actions.reason.assessedAsTest";
                break;
            case "assessmentAsTest":
                key = "clip.actions.reason.assessmentAsTest";
                break;
            case "assessmentAsObservation":
                key = "clip.actions.reason.assessmentAsObservation";
                break;
            case "assessedAsReportreview":
                key = "clip.actions.reason.assessedAsReportreview";
                break;
            case "assessmentAsReportreview":
                key = "clip.actions.reason.assessmentAsReportreview";
                break;
            case "assessedAsPresentation":
                key = "clip.actions.reason.assessedAsPresentation";
                break;
            case "assessmentAsPresentation":
                key = "clip.actions.reason.assessmentAsPresentation";
                break;
            case "assessedAsPerformance":
                key = "clip.actions.reason.assessedAsPerformance";
                break;
            case "assessmentAsPerformance":
                key = "clip.actions.reason.assessmentAsPerformance";
                break;
            case "assessedAsPortfolio":
                key = "clip.actions.reason.assessedAsPortfolio";
                break;
            case "assessmentAsPortfolio":
                key = "clip.actions.reason.assessmentAsPortfolio";
                break;

            case "assessmentAsRoles":
                key = "clip.actions.reason.assessmentAsRoles";
                break;
            case "assessmentAsRubric":
                key = "clip.actions.reason.assessmentAsRubric";
                break;
            case "assessedAsShared":
                key = "clip.actions.reason.assessedAsShared";
                break;
            case "assessmentAsShared":
                key = "clip.actions.reason.assessmentAsShared";
                break;
            case "assessmentAsSelf":
                key = "clip.actions.reason.assessmentAsSelf";
                break;
            case "assessmentAsAnonymous":
                key = "clip.actions.reason.assessmentAsAnonymous";
                break;
            case "assessmentAsIA":
                key = "clip.actions.reason.assessmentAsIA";
                break;
            case "assessmentAsRandom":
                key = "clip.actions.reason.assessmentAsRandom";
                break;
        }

        return extra === undefined ? i18n.get(key) : i18n.getReplaced1(key, extra);
    },
    setActionText : function(data) {//type, link) {
        if(data.items[0].action[0] in this.actionsInGeneralGroup) {
            data.text = this.getActionText(data.link);
        } else {
            var obj = IDPool.getObject(data.link);
            data.text = this.getActionObjectText(obj.type);
            data.reftext = this.getActionObjectRefText(obj.type, obj.getTitle());
        }

        if(data.reasons && data.reasons.length > 0) {
            var reasons = "<ul>";
            for(var i = 0; i < data.reasons.length; i++) {
                var reason = data.reasons[i];
                var text = null;

                if(data.group == "assessments" || data.group == "los") {
                    text = this.getReasonText(reason);
                } else if(reason[0] == "general" || reason[0] == "assessment") {
                    text = this.getReasonText(reason[0]);
                } else {
                    var assessment = (reason[0] == "feedback" || reason[0] == "modify") ? LearningDesign.findAssessmentParentOfFunction(reason[1]) : IDPool.getObject(reason[1]);
                    text = this.getReasonText(reason[0], assessment.getTitle());
                }
                reasons += "<li>" + text + "</li>";
            }
            reasons += "</ul>";

            data.reasonText = reasons;
        }
    },
    getGroupText : function(key) {
        switch (key) {
            case  "general":
                return i18n.get("clip.actions.groups.general");
            case "los":
                return i18n.get("clip.actions.groups.los");
            case "resources":
                return i18n.get("clip.actions.groups.resources");
            case "assessments":
                return i18n.get("clip.actions.groups.assessments");
            default:
                var obj = IDPool.getObject(key);
                return obj.type == "clfp" ? i18n.getReplaced1("clip.actions.groups.clfp", obj.getTitle()) : null;
        }
    },
    /******** states *************/

    setStates : function(groups) {
        for(var i in groups) {
            var group = groups[i];

            for(var j in group.contents) {
                this.setState(group.contents[j].items, group.contents[j]);
            }

            this.setState(group.contents, group);
        }
    },
    setState : function(list, object) {
        object.state = "pending";
        object.modified = "";
        for(var i in list) {
            var item = list[i];

            if(object.modified == "") {
                if(item.modified) {
                    object.modified = item.modified;
                }
            } else if(object.modified == "modified") {
                if(item.modified == "new") {
                    object.modified = "new";
                }
            }
        }
    },
    /********** ignore ************/

    ignoreAllInGroup : function(group) {
        this.ignoreAllThese(group.contents);
    },
    ignoreAction : function(action) {
        ClipManager.ignore(action.items);
        ChangeManager.updateDisplays();
    },
    ignoreAllInArray : function(ids) {
        var actions = new Array();
        for(var i = 0; i < ids.length; i++) {
            actions.push(ClipDisplay.data.actions[ids[i]]);
        }

        this.ignoreAllThese(actions);
    },
    ignoreAllThese : function(actions) {
        for(var i = 0; i < actions.length; i++) {
            ClipManager.ignore(actions[i].items);
        }

        ChangeManager.updateDisplays();
    },
    /******** activate ***************/

    activate : function(action) {
        var currentDialog = DialogManager.getCurrentDialog();
        var obj = IDPool.getObject(action.link);

        if(currentDialog == null) {
            var tab = "";
            if(action.group == "general") {
                tab = action.link == "flow" ? "MainWindowFlowTab" : "MainWindowGeneralTab";
            } else if(action.group == "los") {
                tab = "MainWindowGeneralTab";
            } else if(action.group == "resources") {
                tab = "MainWindowResourcesTab";
            } else {
                tab = "MainWindowFlowTab";
            }

            dijit.byId("MainWindowTabContainer").selectChild(tab);

            if(action.link == "los") {
                dijit.byId('LDLearningObjectiveAddDialog').show();
            } else if(action.link == "flow") {
                LearningFlow.includePattern(-1);
            } else if (action.link == "prerrequisites") {
                dijit.byId('LDPrerrequisitesEditDialog').show();
            }
        }

        if(obj) {
            if(obj.type == "assessmentflow") {
                if(currentDialog == null) {
                    EditAssessmentDialog.open(obj);
                }
            } else if(obj.type == "activity") {
                if(currentDialog == null) {
                    EditActivityDialog.open(obj);
                }
            }
        }
    }
};
>>>>>>> .r438
