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

AssessmentFlow = function() {
    this.functions = new Array();
    this.type = "assessmentflow";
    this.assessedActivityId = null;
    this.assessmentActivityId = null;
    this.patterns = [];

    IDPool.registerNewObject(this);
};

AssessmentFlow.prototype.setAssessmentActivity = function(activity) {
    this.assessmentActivityId = activity == null ? "" : activity.id;
};

AssessmentFlow.prototype.getAssessmentActivity = function() {
    return IDPool.getObject(this.assessmentActivityId);
};

AssessmentFlow.prototype.setAssessedActivity = function(activity) {
    this.assessedActivityId = activity == null ? "" : activity.id;
};

AssessmentFlow.prototype.getAssessedActivity = function() {
    return IDPool.getObject(this.assessedActivityId);
};

AssessmentFlow.prototype.getFunctions = function() {
    return this.functions;
};

AssessmentFlow.prototype.getFunctionCount = function() {
    return this.functions.length;
};

AssessmentFlow.prototype.getFunction = function(i) {
    return this.functions[i];
};

AssessmentFlow.prototype.getPatterns = function() {
    return this.patterns;
};

AssessmentFlow.prototype.setPatterns = function(patterns) {
    this.patterns = patterns;
};

AssessmentFlow.prototype.getTitle = function() {
    var activity = this.getAssessmentActivity();
    return activity ? activity.title : i18n.get("assessment.title.empty");
};

AssessmentFlow.prototype.isDefined = function() {
    return this.getAssessmentActivity() != null;
};
