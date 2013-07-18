/*global IDPool, i18n */

var ExpertGroupParticipantsPatternFactory = {
    getTitle : function() {
        return i18n.get("grouppattern.expertgroupparticipants.title");
    },
    getId : function() {
        return "expertgroupparticipantspattern";
    },
    getDefinition : function() {
        return ExpertGroupParticipantsPattern;
    },
    newPattern : function(actId, instanceId) {
        return new ExpertGroupParticipantsPattern(actId, instanceId);
    }
};

var ExpertGroupParticipantsPattern = function(actId, instanceId) {
    this.type = "groupPattern";
    this.subtype = "pa";
    this.title = ExpertGroupParticipantsPatternFactory.getTitle();
    this.patternid = ExpertGroupParticipantsPatternFactory.getId();

    this.actId = actId;
    this.instanceid = instanceId;
    this.clfpId = LearningDesign.findClfpParentOf(actId).clfp.id;
    this.jigsawActId = IDPool.getObject(this.clfpId).getFlow()[0].id;

    IDPool.registerNewObject(this);
};

ExpertGroupParticipantsPattern.prototype.getTitle = function() {
    return this.title;
};

ExpertGroupParticipantsPattern.prototype.getExternalActDependency = function() {
    return {
        actid : this.jigsawActId,
        type : "pa"
    };
};

ExpertGroupParticipantsPattern.prototype.check = function(instanceId, result, previousProposal) {
    var participants = result.proposal;
    var jigsawparticipants = GroupPatternManager.getParticipantState(instanceId, this.jigsawActId);

    var ej = [];
    var eNotInJG = [];
    var egSize = [];
    var badJG = [];
    var toremove = [];
    var toadd = [];
    var availableInJG = [];
    var needToFix = false;
    var canFix = true;

    for (var j = 0; j < jigsawparticipants.getGroupCount(); j++) {
        availableInJG[j] = [];
        var g = jigsawparticipants.getGroup(j);
        for (var jp = 0; jp < g.length; jp++) {
            if (participants.find(g[jp]) < 0) {
                availableInJG[j].push(g[jp]);
            }
        }
    }

    /* find relationship between jigsaw and expert groups */
    for (var e = 0; e < participants.getGroupCount(); e++) {
        ej[e] = [];
        for (var j = 0; j < jigsawparticipants.getGroupCount(); j++) {
            ej[e][j] = [];
        }

        var expertGroup = participants.getGroup(e);
        egSize[e] = expertGroup.length;

        for (var ep = 0; ep < expertGroup.length; ep++) {
            var j = jigsawparticipants.find(expertGroup[ep]);
            if (j < 0) {
                eNotInJG.push(expertGroup[ep]);
                toremove.push({
                    group : e,
                    id : expertGroup[ep]
                });
                egSize[e]--;
            } else {
                ej[e][j].push(expertGroup[ep]);
            }
        }
    }

    if (eNotInJG.length > 0) {
        var text = i18n.GroupPatternUtils.addAlert(result, i1n8.get("grouppattern.expertgroupparticipants.notinjg"));
        needToFix = true;
    }

    /* count needed students */
    var neededFromJG = [];
    for (var j = 0; j < jigsawparticipants.getGroupCount(); j++) {
        neededFromJG[j] = 0;
        for (var e = 0; e < participants.getGroupCount(); e++) {
            if (ej[e][j] == 0) {
                neededFromJG[j]++;
            }
        }
    }

    /* release if possible Jigsaw group students from Expert groups */
    var removed = false;
    for (var j = 0; j < jigsawparticipants.getGroupCount(); j++) {

        while (neededFromJG[j] - availableInJG[j].length > 0) {
            var max = [0, 0], maxEG = -1;
            for (var e = 0; e < participants.getGroupCount(); e++) {
                if (ej[e][j].length > max[0] || (ej[e][j].length == max[0] && egSize[e] > max[1])) {
                    maxEG = e;
                    max = [ej[e][j].length, egSize[e]];
                }
            }
            if (max > 1) {
                var id = ej[e][j].splice(-1, 1);
                availableInJG[j].push(id);
                toremove.push({
                    group : maxEG,
                    id : id
                });
                egSize[maxEG]--;
            } else {
                badJG.push(j);
                break;
            }
        }
    }
    if (removed) {
        needToFix = true;
        GroupPatternUtils.addAlert(result, i18n.get("grouppattern.expertgroupparticipants.2manyfromjg"));
    }

    if (badJG.length > 0) {
        needToFix = true;
        canFix = false;
        GroupPatternUtils.addAlert(result, i18n.get("grouppattern.expertgroupparticipants.smalljg"));
    } else {
        /* add available students from JG to EG */
        var added = false;

        for (var j = 0; j < availableInJG.length; j++) {
            if (availableInJG[j].length > 0) {
                added = true;
                for (var jp = 0; jp < availableInJG[j].length; jp++) {
                    var min = [1e6, 1e6];
                    var minEG = -1;
                    for (var e = 0; e < ej.length; e++) {
                        if (ej[e][j].length < min[0] || (ej[e][j].length == min[0] && egSize[e] < min[1])) {
                            minEG = e;
                            min = [ej[e][j].length, egSize[e]];
                        }
                    }
                    var id = availableInJG[j][jp];
                    toadd.push({
                        group : minEG,
                        id : id
                    });
                    ej[minEG][j].push(id);
                    egSize[minEG]++;
                }
            }
        }
        if (added) {
            needToFix = true;
            GroupPatternUtils.addAlert(result, i18n.get("grouppattern.expertgroupparticipants.distribute"));
        }
    }

    if (needToFix) {
        result.ok = false;
        result.fixable = !previousProposal && canFix;
        if (result.fixable) {
            participants.moveIds(toremove, toadd);
        }
    } else {
        GroupPatternUtils.appendExplanation(result, i18n.get("grouppattern.expertgroupparticipants.ok"));
    }
};

GroupPatternManager.registerPatternFactory("pa", ExpertGroupParticipantsPatternFactory);
