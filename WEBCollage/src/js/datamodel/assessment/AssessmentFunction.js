/*global i18n, IDPool */

var AssessmentFunction = function(type) {
    this.type = "assessmentfunction";
    this.subtype = type;
    IDPool.registerNewObject(this);
};

AssessmentFunction.prototype.getTitle = function() {
    switch(this.subtype) {
        case "summative":
            return i18n.get("assessment.edit.functions.summative");
        case "formative":
            return i18n.get("assessment.edit.functions.formative");
        case "diagnosis":
            return i18n.get("assessment.edit.functions.diagnosis");
        default:
            return "";
    }
};

AssessmentFunction.prototype.getLinkedActivity = function() {
    return IDPool.getObject(this.link);
};