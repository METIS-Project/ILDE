/*global IDPool, i18n */

var GroupParticipantsDistributePatternFactory = {
    getTitle : function() {
        return i18n.get("grouppattern.groupparticipantsdistribute.title");
    },
    getId : function() {
        return "groupparticipantsdistributepattern";
    },
    getDefinition : function() {
        return GroupParticipantsDistributePattern;
    },
    newPattern : function(actId) {
        return new GroupParticipantsDistributePattern(actId);
    }
};

var GroupParticipantsDistributePattern = function(actId) {
    this.type = "groupPattern";
    this.subtype = "pa";
    this.title = GroupParticipantsDistributePatternFactory.getTitle();
    this.patternid = GroupParticipantsDistributePatternFactory.getId();

    this.actId = actId;
    this.clfpId = LearningDesign.findClfpParentOf(actId).clfp.id;

    IDPool.registerNewObject(this);
};

GroupParticipantsDistributePattern.prototype.getTitle = function() {
    return this.title;
};

GroupParticipantsDistributePattern.prototype.check = function(instanceId, result, previousProposal) {
    var participants = result.proposal;
    var students = Context.getAvailableStudents(this.clfpId, instanceId);
    var studentCount = students.length;
    var remaining = studentCount;
    var groupCount = participants.getGroupCount();
    var movements = [];

    for (var i = 0; i < groupCount; i++) {
        var groupsLeft = groupCount - i;
        var group = participants.getGroup(i);
        var min = Math.floor(remaining / groupsLeft);
        if (remaining % groupsLeft == 0) {
            remaining -= GroupPatternUtils.compareExactParticipantNumber(group, i, min, movements);
        } else {
            remaining -= GroupPatternUtils.compareMinMaxParticipantNumber(group, i, min, min + 1, movements);
        }
    }

    var explanation;
    if (studentCount % groupCount == 0) {
        explanation = i18n.getReplaced1("grouppattern.groupparticipantsdistribute.exact", studentCount / groupCount);
    } else {
        var m = Math.floor(studentCount / groupCount);
        explanation = i18n.getReplaced2("grouppattern.groupparticipantsdistribute.noexact", m, m + 1);
    }

    GroupPatternUtils.fixParticipantMovements(students, result, previousProposal, movements, explanation);
};

GroupPatternManager.registerPatternFactory("pa", GroupParticipantsDistributePatternFactory);
