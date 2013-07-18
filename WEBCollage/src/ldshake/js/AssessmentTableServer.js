/**
 * @author Javier E. Hoyos Torio
 */
var AssessmentTableServer = {

    appendAssessments: function(where) {
        var title = where.appendChild(document.createElement("h1"));
        title.innerHTML = "Assessment plan:";

        var table = where.appendChild(document.createElement("table"));

        for (var i = 0; i < data.design.assessments.length; i++) {
            if (i % 2 == 0) {
                var tr = table.appendChild(document.createElement("tr"));
            }

            var td = tr.appendChild(document.createElement("td"));
            this.appendAssessmentNice(i + 1, data.design.assessments[i], td);
        }
    },

    appendAssessmentNice : function(index, assessment, td) {
        var container = td.appendChild(document.createElement("div"));
        container.className = "assessmentcontainer";

        var where = container.appendChild(document.createElement("div"));
        where.className = "assessmentinnercontainer";

        var titleBlock = where.appendChild(document.createElement("div"));
        titleBlock.innerHTML = index + " - " + TableGeneratorServer.getTitle(assessment);
        titleBlock.className = "activitysummarytitle";

        var activity = TableGeneratorServer.findActivity(assessment.assessedActivityId);
        if (activity) {
            var location = TableGeneratorServer.findActParentOfActivity(activity.id);
            var roles = TableGeneratorServer.getRolesClfp(location.clfp);
            for (var j in roles){
                var role = roles[j];
                if (role.id == location.roleId){
                    var roleTitle = role.title;
                    break;
               }
            }
            var text = "This assessment assesses the activity: <strong>'" + activity.title + "</strong> (in <strong>" + location.act.title + "</strong>, at <strong>" + location.clfp.title + "</strong>.<br/>Carried out by <strong>" + roleTitle + "</strong>).";
        } else {
            text = "The assessed activity is not yet defined.";
        }
        TableGeneratorServer.addLabel(where, text, "assessmentsummaryblock", "images/icons/assessed.png");

        activity = TableGeneratorServer.findActivity(assessment.assessmentActivityId);
        if (activity) {
            var location = TableGeneratorServer.findActParentOfActivity(activity.id);
            var roles = TableGeneratorServer.getRolesClfp(location.clfp);
            for (var j in roles){
                var role = roles[j];
                if (role.id == location.roleId){
                    var roleTitle = role.title;
                    break;
               }
            }
            text = "The 'assess task' is: <strong>" + activity.title + "</strong> (in <strong>" + location.act.title + "</strong>, at <strong>" + location.clfp.title + "</strong>.<br/>Carried out by <strong>" + roleTitle + "</strong>).";
        } else {
            text = "The 'assess task' is not yet defined.";
        }
        TableGeneratorServer.addLabel(where, text, "assessmentsummaryblock", "images/icons/assessment.png");

        for (var i = 0; i < assessment.functions.length; i++) {
            var func = assessment.functions[i];
            if (func.subtype == "summative") {
                text = "This assessment is used to grade students: <strong>'" + "func.name" + "'</strong>, " + "func.description";
            } else {
                activity = TableGeneratorServer.findActivity(func.link);
                if (activity) {
                    location = LearningDesign.findActParentOfActivity(activity.id);
                    var key = func.subtype == "formative" ? "summary.function.formative" : "summary.function.diagnosis";
                    var roles = TableGeneratorServer.getRolesClfp(location.clfp);
                    for (var j in roles){
                        var role = roles[j];
                        if (role.id == location.roleId){
                            var roleTitle = role.title;
                            break;
                        }    
                    }
                    if (key == "summary.function.formative"){
                        text = "This assessment is used to give feedback in the activity: <strong>'" + activity.title + "'</strong> (in <strong>" + location.act.title + "</strong>, at <strong>" + location.clfp.title + "</strong>.<br/>Carried out by <strong>" + roleTitle + "</strong>).";
                    }else{
                        text = "The results of this assessment are used to modify the activity: <strong>'" + activity.title + "'</strong> (in <strong>" + location.act.title + "</strong>, at <strong>" + location.clfp.title + "</strong>.<br/>Carried out by <strong>" + roleTitle + "</strong>).";
                    }
                } else {
                    var key = func.subtype == "formative" ? "summary.function.formative.no" : "summary.function.diagnosis.no";
                    if (key == "summary.function.formative.no"){
                        text = "This assessment is used to give feedback (but it has not yet been configured in which activity).";
                    }
                    else{
                        text = "The results of this assessment are used to modify an activity (but this activity has not yet been chosen).";
                    }
                }
            }

            TableGeneratorServer.addLabel(where, text, "assessmentsummaryblock", "images/icons/" + func.subtype + ".png");
        }

        if (assessment.patterns.length == 1) {
            text = "The following pattern has been applied to this assessment: <strong>" + assessment.patterns[0].data.title + "</strong>.";
            TableGeneratorServer.addLabel(where, text, "assessmentsummaryblock");
        } else if (assessment.patterns.length > 1) {
            var names = new Array();
            for (i = 0; i < assessment.patterns.length; i++) {
                names.push(assessment.patterns[i].data.title);
            }
            text = "The following patterns have been applied to this assessment: <strong>" + names.join(", ") + "</strong>";
            TableGeneratorServer.addLabel(where, text, "assessmentsummaryblock");
        }
    }
};

