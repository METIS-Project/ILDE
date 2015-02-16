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
