/*
 * Author: Eloy
 */

var AssessmentConflicts = {

    check : function(assessmentDef, newpattern) {

        var conflicts = {
            patterns : []
        };
        this.checkAssessmentConflict(assessmentDef, newpattern, conflicts);
        this.checkAssessedConflict(assessmentDef, newpattern, conflicts);

        if (conflicts.patterns.length > 0 || conflicts.assessment.activity || conflicts.assessed.activity) {
            return conflicts;
        } else {
            return null;
        }
    },

    checkAssessmentConflict : function(assessmentDef, newpattern, conflicts) {
        conflicts.assessment = {};
        if (AssessmentPatternManager.definesAssessmentActivity(newpattern)) {
            var pattern = assessmentDef.assessmentPatterns.assessment;
            if (pattern) {
                conflicts.assessment.pattern = true;
                if (conflicts.patterns.indexOf(pattern) < 0) {
                    conflicts.patterns.push(pattern);
                }
            }

            if (assessmentDef.assessmentActivity) {
                conflicts.assessment.activity = assessmentDef.assessmentActivity;
            }
        }
    },

    checkAssessedConflict : function(assessmentDef, newpattern, conflicts) {
        conflicts.assessed = {};
        if (AssessmentPatternManager.definesAssessedActivity(newpattern)) {
            var pattern = assessmentDef.assessmentPatterns.assessed;
            if (pattern) {
                conflicts.assessed.pattern = true;
                if (conflicts.patterns.indexOf(pattern) < 0) {
                    conflicts.patterns.push(pattern);
                }
            }

            if (assessmentDef.assessedActivity) {
                conflicts.assessed.activity = assessmentDef.assessedActivity;
            }
        }
    }
};
