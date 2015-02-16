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
