/**
 * @author Eloy
 */
var AssessmentTable = {

    appendAssessments: function(where) {
        var title = where.appendChild(document.createElement("h1"));
        title.innerHTML = i18n.get("summary.assessment.head");

        var table = where.appendChild(document.createElement("table"));

        for (var i = 0; i < LearningDesign.data.assessments.length; i++) {
            if (i % 2 == 0) {
                var tr = table.appendChild(document.createElement("tr"));
            }

            var td = tr.appendChild(document.createElement("td"));
            this.appendAssessmentNice(i + 1, LearningDesign.data.assessments[i], td);
        }
    },

    appendAssessmentNice : function(index, assessment, td) {
        var container = td.appendChild(document.createElement("div"));
        container.className = "assessmentcontainer";

        var where = container.appendChild(document.createElement("div"));
        where.className = "assessmentinnercontainer";

        var titleBlock = where.appendChild(document.createElement("div"));
        titleBlock.innerHTML = index + " - " + assessment.getTitle();
        titleBlock.className = "activitysummarytitle";

        var activity = assessment.getAssessedActivity();
        if (activity) {
            var location = LearningDesign.findActParentOfActivity(activity.id);
            var text = dojo.string.substitute(i18n.get("summary.activity.asssessed"), {
                activity : activity.title,
                act : location.act.title,
                clfp : location.clfp.title,
                role : IDPool.getObject(location.roleId).title
            });
        } else {
            text = i18n.get("summary.activity.asssessed.no");
        }
        TableGenerator.addLabel(where, text, "assessmentsummaryblock", "images/icons/assessed.png");

        activity = assessment.getAssessmentActivity();
        if (activity) {
            location = LearningDesign.findActParentOfActivity(activity.id);
            text = dojo.string.substitute(i18n.get("summary.activity.asssessment"), {
                activity : activity.title,
                act : location.act.title,
                clfp : location.clfp.title,
                role : IDPool.getObject(location.roleId).title
            });
        } else {
            text = i18n.get("summary.activity.asssessment.no");
        }
        TableGenerator.addLabel(where, text, "assessmentsummaryblock", "images/icons/assessment.png");

        for (var i = 0; i < assessment.functions.length; i++) {
            var func = assessment.functions[i];
            if (func.subtype == "summative") {
                text = dojo.string.substitute(i18n.get("summary.function.summative"), {
                    name: "func.name",
                    description: "func.description"
                });
            } else {
                activity = IDPool.getObject(func.link);
                if (activity) {
                    location = LearningDesign.findActParentOfActivity(activity.id);
                    var key = func.subtype == "formative" ? "summary.function.formative" : "summary.function.diagnosis";
                    text = i18n.getReplaced(key, {
                        activity : activity.title,
                        act : location.act.title,
                        clfp : location.clfp.title,
                        role : IDPool.getObject(location.roleId).title
                    });
                } else {
                    var key = func.subtype == "formative" ? "summary.function.formative.no" : "summary.function.diagnosis.no";
                    text = i18n.get(key);
                }
            }

            TableGenerator.addLabel(where, text, "assessmentsummaryblock", "images/icons/" + func.subtype + ".png");
        }

        if (assessment.patterns.length == 1) {
            text = dojo.string.substitute(i18n.get("summary.patterns.one"), [assessment.patterns[0].getTitle()]);
            TableGenerator.addLabel(where, text, "assessmentsummaryblock");
        } else if (assessment.patterns.length > 1) {
            var names = new Array();
            for (i = 0; i < assessment.patterns.length; i++) {
                names.push(assessment.patterns[i].getTitle());
            }
            text = dojo.string.substitute(i18n.get("summary.patterns.several"), [names.join(", ")]);
            TableGenerator.addLabel(where, text, "assessmentsummaryblock");
        }
    },

    edit : function(id) {
        EditAssessmentDialog.open(IDPool.getObject(id));
    },

    remove : function(id) {
        alert("Not yet implemented.");
    }
};

AssessmentEditListener = function(id) {
    this.id = id;
};

AssessmentEditListener.prototype.edit = function() {
    AssessmentTab.edit(this.id);
};

AssessmentEditListener.prototype.remove = function() {
    AssessmentTab.remove(this.id);
};
