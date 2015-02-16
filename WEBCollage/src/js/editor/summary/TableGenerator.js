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
 * @author Eloy
 */

/*global dojo, dijit, AssessmentTable, Nifty, window, i18n, LearningDesign, IDPool,  */
var TableGenerator = {

    init: function(){
        /*dojo.connect(dijit.byId("MainWindowTabContainer").tablist, "onButtonClick", function(){
            if (dijit.byId("MainWindowTabContainer").selectedChildWidget.id == "MainWindowSummaryTab") {
                TableGenerator.generateSummary();
            }
        });*/
    },
    
    updateDisplay: function(){
        TableGenerator.generateSummary();
    },
	
    generateSummary: function(){
        var element = dojo.byId("SummaryTabContent");
        element.innerHTML = "";
        this.generalInfo(element);
        this.flowInfo(element);

        AssessmentTable.appendAssessments(element);

        Nifty("div.clfpcontainer", "big transparent");
        Nifty("div.clfpcontainerinner", "transparent");
        Nifty("div.clfptitle", "small transparent");

        Nifty("div.assessmentcontainer", "transparent");
        Nifty("div.assessmentinnercontainer", "small transparent");
    },

    openNewWindow: function() {
        var newwindow = window.open('', "summary");
        newwindow.document.write("<html><head><title>");
        newwindow.document.write("</title>");
        newwindow.document.write("<link rel='stylesheet' href='lib/niftycube/niftyCorners.css'/>");
        newwindow.document.write("<link rel='stylesheet' href='css/summarypage.css'/>");
        newwindow.document.write("<link rel='stylesheet' href='css/summary.css'/>");
        newwindow.document.write("</head><body>");
        newwindow.document.write(dojo.byId("SummaryTabContent").innerHTML);
        newwindow.document.write("</body></html>");
        newwindow.document.close();
    },
	
    generalInfo: function(where){
        var title = where.appendChild(document.createElement("h1"));
        title.innerHTML = i18n.get("summary.general.head");
	
        var element = where.appendChild(document.createElement("div"));
        this.addFieldAndValue(element, i18n.get("summary.title"), LearningDesign.data.title);
		
        element = where.appendChild(document.createElement("div"));
        this.addFieldAndValue(element, i18n.get("summary.prerrequisites"), LearningDesign.data.prerrequisites.replace(/\n/g, "<br/>"));
		
        element = where.appendChild(document.createElement("ul"));
        for (var i = 0; i < LearningDesign.data.learningObjectives.length; i++) {
            var lo = LearningDesign.data.learningObjectives[i];
            var li = element.appendChild(document.createElement("li"));
            li.innerHTML = lo.title + ": " + lo.description;
        }
    },
	
    flowInfo: function(where) {
        var title = where.appendChild(document.createElement("h1"));
        title.innerHTML = i18n.get("summary.flow.head");

        this.printFlowAct(where, LearningDesign.data.flow);
    },
	
    printFlowAct: function(where, act) {
        if (act.type == "act") {
            var rolepartsTable = where.appendChild(document.createElement("table"));
            var roleRow = rolepartsTable.appendChild(document.createElement("tr"));
            var activitiesRow = rolepartsTable.appendChild(document.createElement("tr"));

            for (var i in act.learners) {
                var roleid = act.learners[i];
                var td = roleRow.appendChild(document.createElement("td"));
                this.addLabel(td, IDPool.getObject(roleid).title, "activitiestableheader", "images/students.png", true);
                td = activitiesRow.appendChild(document.createElement("td"));
                this.printFlowActivities(td, act.getActivitiesForRoleId(roleid));
            }

            for (i in act.staff) {
                roleid = act.staff[i];
                td = roleRow.appendChild(document.createElement("td"));
                this.addLabel(td, IDPool.getObject(roleid).title, "activitiestableheader", "images/teacher.png", true);

                td = activitiesRow.appendChild(document.createElement("td"));
                this.printFlowActivities(td, act.getActivitiesForRoleId(roleid));
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

        for (var i = 0; i < LearningDesign.data.assessments.length; i++) {
            var assessment = LearningDesign.data.assessments[i];
            if (assessment.getAssessmentActivity() == activity) {
                links.push({
                    index: i,
                    type: "assessment",
                    weight: 0,
                    assessment: assessment
                });
                isAssessment = true;
            }
        
            if (assessment.getAssessedActivity() == activity) {
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
            var text = this.getAssessmentLinkText(link.type, link.index, link.assessment.getTitle());
            this.addLabel(where, text, "summaryassessmentlink", "images/icons/" + link.type + ".png");
        }

        return isAssessment;
    },
    getAssessmentLinkText : function(linktype, index, title) {
        switch (linktype) {
            case "assessment":
                return i18n.getReplaced2("summary.assessmentlink.assessment", index + 1, title);
            case "assessed":
                return i18n.getReplaced2("summary.assessmentlink.assessed", index + 1, title);
            case "formative":
                return i18n.getReplaced2("summary.assessmentlink.formative", index + 1, title);
            case "diagnosis":
                return i18n.getReplaced2("summary.assessmentlink.diagnosis", index + 1, title);
        }     
    },

    printFlowCLFP: function(where, clfp) {
        var outContainer = where.appendChild(document.createElement("div"));
        dojo.query(outContainer).addClass("clfpcontainer clfpcontainer_" + clfp.patternid);
        var clfpContainer = outContainer.appendChild(document.createElement("div"));
        clfpContainer.className = "clfpcontainerinner";

        var iconurl = "images/clfps/" + clfp.patternid + ".png";
        this.addLabel(clfpContainer, clfp.title, "clfptitle", iconurl);
        var flowTable = clfpContainer.appendChild(document.createElement("table"));
        var acts = clfp.getFlow();
        var lastRowIsA = false;
        for (var i = 0; i < acts.length; i++) {
            var style = (lastRowIsA = !lastRowIsA) ? "actcellA" : "actcellB";

            var tr = flowTable.appendChild(document.createElement("tr"));
            tr.className = "clfpsummaryact";

            var td = tr.appendChild(document.createElement("td"));
            td.innerHTML = acts[i].title;
            dojo.query(td).addClass("clfpsummaryphase");
            dojo.query(td).addClass(style);
            if (i < acts.length - 1) {
                dojo.query(td).addClass("clfpsummaryphasenolast");
            }

            td = tr.appendChild(document.createElement("td"));
            dojo.query(td).addClass(style);
            if (i < acts.length - 1) {
                dojo.query(td).addClass("clfpsummaryphasenolast");
            }
            this.printFlowAct(td, acts[i]);
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
        labelElement.appendChild(document.createElement("span")).innerHTML = label.replace(/\n/g, "<br/>");

        if (style) {
            labelElement.className = style;
        }
    },
	
    addFieldAndValue: function(where, field, value){
        var fieldName = where.appendChild(document.createElement("span"));
        dojo.html.set(fieldName, field);
        var fieldValue = where.appendChild(document.createElement("span"));
        dojo.html.set(fieldValue, value);
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
    }
};
