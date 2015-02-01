/**
 * @author Javier E. Hoyos Torio
 */
var TableGeneratorServer = {
	
    generateSummary: function(){
        var div = document.createElement("div");
        div.id = "SummaryTabContent";
        document.body.appendChild(div);
        div.innerHTML = "";
        this.generalInfo(div);
        this.flowInfo(div);

        AssessmentTableServer.appendAssessments(div);
    },
	
    generalInfo: function(where){
        var title = where.appendChild(document.createElement("h1"));
        title.innerHTML = 'General information: ';
	
        var element = where.appendChild(document.createElement("div"));
        this.addFieldAndValue(element, 'Title: ', data.design.title);
		
        element = where.appendChild(document.createElement("div"));
        this.addFieldAndValue(element, 'Prerrequisites: ', data.design.prerrequisites.replace(/\n/g, "<br/>"));
		
        element = where.appendChild(document.createElement("ul"));
        for (var i = 0; i < data.design.learningObjectives.length; i++) {
            var lo = data.design.learningObjectives[i];
            var li = element.appendChild(document.createElement("li"));
            li.innerHTML = lo.title + ": " + lo.description;
        }
    },
	
    flowInfo: function(where) {
        var title = where.appendChild(document.createElement("h1"));
        title.innerHTML = 'Learning activity flow: ';

        this.printFlowAct(where, data.design.flow);
    },
	
    printFlowAct: function(where, act, roles) {
        if (act.type == "act") {
            var rolepartsTable = where.appendChild(document.createElement("table"));
            var roleRow = rolepartsTable.appendChild(document.createElement("tr"));
            var activitiesRow = rolepartsTable.appendChild(document.createElement("tr"));

            for (var i in act.learners) {
                var roleid = act.learners[i];
                var td = roleRow.appendChild(document.createElement("td"));

                for (var j in roles){
                    var role = roles[j];
                    if (role.id == roleid){
                        var title = role.title;
                        break;
                    }
                }
                this.addLabel(td, title, "activitiestableheader", "images/students.png", true);
                td = activitiesRow.appendChild(document.createElement("td"));
                this.printFlowActivities(td, TableGeneratorServer.getActivitiesForRoleId(act, roleid));
            }

            for (i in act.staff) {
                roleid = act.staff[i];
                td = roleRow.appendChild(document.createElement("td"));
                for (var j in roles){
                    var role = roles[j];
                    if (role.id == roleid){
                        var title = role.title;
                        break;
                    }
                }
                this.addLabel(td, title, "activitiestableheader", "images/teacher.png", true);
                td = activitiesRow.appendChild(document.createElement("td"));
                this.printFlowActivities(td, TableGeneratorServer.getActivitiesForRoleId(act, roleid));
            }
        } else {
            for (i in act.clfps) {
                this.printFlowCLFP(where, act.clfps[i]);
            }
        }
    },
	
    printFlowActivities: function(where, activities) {
        for (var i in activities) {
            var activity = activities[i];
            var block = where.appendChild(document.createElement("div"));

            this.addLabel(block, activity.title, "activitysummarytitle");
            this.addLabel(block, activity.description, "activitysummarydescription");

            var isAssessment = this.printActivityAssessmentLinks(block, activity);
            block.className = (isAssessment ? "assessment" : activity.subtype) + "activitysummary";

        }
    },

    printActivityAssessmentLinks: function(where, activity) {
        var links = [];
        var isAssessment = false;

        for (var i = 0; i < data.design.assessments.length; i++) {
            var assessment = data.design.assessments[i];
            if (assessment.assessmentActivityId == activity.id) {
                links.push({
                    index: i,
                    type: "assessment",
                    weight: 0,
                    assessment: assessment
                });
                isAssessment = true;
            }
        
            if (assessment.assessedActivityId == activity.id) {
                links.push({
                    index: i,
                    type: "assessed",
                    weight: 1,
                    assessment: assessment
                });
            }

            for (var j = 0; j < assessment.functions.length; j++) {
                var assessmentfunction = assessment.functions[j];
                if (assessmentfunction.link == activity.id) {
                    links.push({
                        index: i,
                        type: assessmentfunction.subtype,
                        weight: assessmentfunction.subtype == "formative" ? 3 : 4,
                        assessment: assessment
                    });
                }
            }
        }

        links.sort(function(a, b) {
            return a.weight - b.weight;
        });

        for (i = 0; i < links.length; i++) {
            var link = links[i];
            var text = this.getAssessmentLinkText(link.type, link.index, TableGeneratorServer.getTitle(link.assessment));            
            this.addLabel(where, text, "summaryassessmentlink", "images/icons/" + link.type + ".png");
        }

        return isAssessment;
    },
    getAssessmentLinkText : function(linktype, index, title) {
        switch (linktype) {
            case "assessment":
                return "This is the 'assess task' in: <strong>" + (index + 1) + " - " + title + "</strong>.";
            case "assessed":
                return "This activity is assessed in: <strong>" + (index + 1) + " - " + title + "</strong>.";
            case "formative":
                return "This activity receives feedback from assessment: <strong>" + (index + 1) + " - " + title + "</strong>.";
            case "diagnosis":
                return "This activity can be modified according to results from assessment: <strong>" + (index + 1) + " - " + title + "</strong>.";
        }     
    },

    printFlowCLFP: function(where, clfp) {
        var outContainer = where.appendChild(document.createElement("div"));
        outContainer.setAttribute("class", "clfpcontainer clfpcontainer_" + clfp.patternid);
        var clfpContainer = outContainer.appendChild(document.createElement("div"));
        clfpContainer.className = "clfpcontainerinner";

        var iconurl = "images/clfps/" + clfp.patternid + ".png";
        this.addLabel(clfpContainer, clfp.title, "clfptitle", iconurl);
        var flowTable = clfpContainer.appendChild(document.createElement("table"));
        var acts = clfp.flow;
        var lastRowIsA = false;
        
        var roles = TableGeneratorServer.getRolesClfp(clfp);

        for (var i = 0; i < acts.length; i++) {
            var style = (lastRowIsA = !lastRowIsA) ? "actcellA" : "actcellB";

            var tr = flowTable.appendChild(document.createElement("tr"));
            tr.className = "clfpsummaryact";

            var td = tr.appendChild(document.createElement("td"));
            td.innerHTML = acts[i].title;
            td.setAttribute("class", "clfpsummaryphase");
            td.setAttribute("class", style);
            if (i < acts.length - 1) {
                td.setAttribute("class", "clfpsummaryphasenolast");
            }

            td = tr.appendChild(document.createElement("td"));
            td.setAttribute("class", style);
            if (i < acts.length - 1) {
                td.setAttribute("class", "clfpsummaryphasenolast");
            }
            this.printFlowAct(td, acts[i], roles);
        }
    },
	
	
    /* generic */
	
    addLabel: function(where, label, style, icon, newline) {
        var labelElement = where.appendChild(document.createElement("div"));
        if (icon) {
            var iconElement = labelElement.appendChild(document.createElement("img"));
            iconElement.className = "clfpsummaryicon";
            iconElement.src = icon;
            if (newline) {
                labelElement.appendChild(document.createElement("br"));
            }
        }
        labelElement.appendChild(document.createElement("span")).innerHTML = label;

        if (style) {
            labelElement.className = style;
        }
    },
	
    addFieldAndValue: function(where, field, value){
        var fieldName = where.appendChild(document.createElement("span"));
        fieldName.innerHTML = field;
        var fieldValue = where.appendChild(document.createElement("span"));
        fieldValue.innerHTML = value;
    },
	
    addTable: function(where, list, fields, title){
        if (title) {
            var titleElement = where.appendChild(document.createElement("div"));
            titleElement.innerHTML = title;
        }
        var table = where.appendChild(document.createElement("table"));
        for (var i in list) {
            var item = list[i];
            var tr = table.appendChild(document.createElement("tr"));
            for (var j in fields) {
                var td = tr.appendChild(document.createElement("td"));
                td.innerHTML = item[fields[j]];
            }
        }
    },
    
    
    /** 
     * Some auxiliar functions
     * 
     */
    getRolesClfp : function(clfp) {
        var roles = new Array();
        for(var r = 0; r < clfp.ownRoles.length; r++) {
            roles.push(clfp.ownRoles[r]);
            this.getRolesChildren(clfp.ownRoles[r].children, roles);
        }
        return roles;
    },

    getRolesChildren : function(children, roles) {
        for(var c = 0; c < children.length; c++) {
            roles.push(children[c]);
            this.getRolesChildren(children[c].children, roles);
        }
    },
    
    
    getActivitiesForRoleId: function(act, roleId) {
        for ( var i in act.roleparts) {
            if (act.roleparts[i].roleId == roleId) {
                return act.roleparts[i].activities;
            }
        }
        return null;
    },
    
    
    getTitle: function(assessment) {
        var activity = TableGeneratorServer.findActivity(assessment.assessmentActivityId);
        if (activity.title){
            return activity.title;
        }
        return "";
    },
    

    findActivity : function(activityId) {
        return TableGeneratorServer.helpFindingActivity(activityId, data.design.flow, null);
    },
        
    helpFindingActivity : function(activityId, act) {
        if(act.type == "act") {
            for(var i in act.roleparts) {
                for(var j in act.roleparts[i].activities) {
                    if(act.roleparts[i].activities[j].id == activityId) {
                        return act.roleparts[i].activities[j];
                    }
                }
            }

            return null;
        } else {
            /* is a clfpact */
            for(i in act.clfps) {
                for( j = 0; j < act.clfps[i].flow.length; j++) {
                    var result = this.helpFindingActivity(activityId, act.clfps[i].flow[j]);
                    if(result !== null) {
                        return result;
                    }
                }
            }
        }
        return null;
    },
    


    findActParentOfActivity : function(activityId) {
        return this.helpFindingActParentOfActivity(activityId, data.design.flow, null);
    },

    helpFindingActParentOfActivity : function(activityId, act, clfp, index) {
        if(act.type == "act") {
            for(var i in act.roleparts) {
                for(var j in act.roleparts[i].activities) {
                    if(act.roleparts[i].activities[j].id == activityId) {
                        return {
                            roleId : act.roleparts[i].roleId,
                            act : act,
                            clfp : clfp,
                            actIndex : index
                        };
                    }
                }
            }

            return null;
        } else {
            /* is a clfpact */

            for(i in act.clfps) {
                for( j = 0; j < act.clfps[i].flow.length; j++) {
                    var result = this.helpFindingActParentOfActivity(activityId, act.clfps[i].flow[j], act.clfps[i], j);
                    if(result !== null) {
                        return result;
                    }
                }
            }

        }

        return null;
    }
    
};
