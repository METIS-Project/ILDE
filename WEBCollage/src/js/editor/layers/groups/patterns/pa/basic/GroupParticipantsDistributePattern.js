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

/*global IDPool, i18n */

var GroupParticipantsDistributePatternFactory = {
    getTitle : function() {
        return i18n.get("grouppattern.groupparticipantsdistribute.title");
    },
    getId : function() {
        return "groupparticipantsdistributepattern";
    },
    getInfo : function() {
        return i18n.get("grouppattern.groupparticipantsdistribute.info");
    },
    getDefinition : function() {
        return GroupParticipantsDistributePattern;
    },
    newPattern : function(actId, instanceId) {
        return new GroupParticipantsDistributePattern(actId, instanceId);
    }
};

var GroupParticipantsDistributePattern = function(actId, instanceId) {
    this.type = "groupPattern";
    this.subtype = "pa";
    this.title = GroupParticipantsDistributePatternFactory.getTitle();
    this.info = GroupParticipantsDistributePatternFactory.getInfo();
    this.patternid = GroupParticipantsDistributePatternFactory.getId();

    this.actId = actId;
    this.instanceid = instanceId;
    this.clfpId = LearningDesign.findClfpParentOf(actId).clfp.id;

    IDPool.registerNewObject(this);
};

GroupParticipantsDistributePattern.prototype.getTitle = function() {
    return this.title;
};

GroupParticipantsDistributePattern.prototype.getInfo = function() {
    return this.info;
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

    GroupPatternUtils.fixParticipantMovements(students, result, previousProposal, movements, i18n.get("grouppattern.groupparticipantsdistribute.title") + ": " + explanation);
};

GroupPatternManager.registerPatternFactory("pa", GroupParticipantsDistributePatternFactory);
