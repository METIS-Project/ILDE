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
