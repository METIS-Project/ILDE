/**
 * @author Eloy
 */

var AssessmentManager = {

    init : function() {
    },

    /**
	 * 
	 * @param {Object}
	 *            activity
	 */
    createAssessment : function(activity) {
        var flow = new AssessmentFlow();
        flow.setAssessmentActivity(activity);
        LearningDesign.data.assessments.push(flow);

        ChangeManager.startGroup();
        ChangeManager.assessmentAdded(flow);
        ChangeManager.assessmentActivitySet(flow, activity);
        ChangeManager.endGroup();
    },

    deleteAssessment : function(assessment) {
        LearningDesign.data.assessments.splice(LearningDesign.data.assessments
            .indexOf(assessment), 1);

        ChangeManager.assessmentDeleted(assessment);
    },

    /**
	 * 
	 * @param {Object}
	 *            activity
	 */
    assessActivity : function(activity) {
        var flow = new AssessmentFlow();
        flow.setAssessedActivity(activity);
        LearningDesign.data.assessments.push(flow);

        ChangeManager.startGroup();
        ChangeManager.assessmentAdded(flow);
        ChangeManager.assessedActivitySet(flow, activity);
        ChangeManager.endGroup();
    },

    getAssessmentsFor : function(activity) {
        return this.getAssessmentsForActivityId(activity.id);
    },

    getAssessmentsForActivityId : function(id) {
        var assessments = new Array();
        for ( var i = 0; i < LearningDesign.data.assessments.length; i++) {
            var assessment = LearningDesign.data.assessments[i];
            if (assessment.assessmentActivityId == id) {
                assessments.push( {
                    rel : "assessment",
                    assessment : assessment,
                    activityId : id
                });
            }

            if (assessment.assessedActivityId == id) {
                assessments.push( {
                    rel : "assessed",
                    assessment : assessment,
                    activityId : id
                });
            }

            var functions = assessment.getFunctions();
            for ( var j = 0; j < functions.length; j++) {
                if (functions[j].link == id) {
                    assessments.push( {
                        rel : functions[j].subtype,
                        assessment : assessment,
                        activityId : id
                    });
                }
            }
        }
        return assessments;
    },

    /* utils */

    weightOfRel : function(rel) {
        if (rel == null) {
            return 0;
        } else if (rel == "formative" || rel == "diagnosis") {
            return 1;
        } else if (rel == "assessed") {
            return 2;
        } else if (rel == "assessment") {
            return 3;
        } else {
            return -1;
        }
    }
};
