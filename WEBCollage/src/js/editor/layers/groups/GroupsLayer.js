/*global IDPool, GroupsRenderer ClfpsCommon, InstanceStudentManagement */

var GroupsLayer = {

    participantFlow : {
        acts : {},
        groups : {},
        links : [],
        participants : {}
    },

    flowRenderer : null,

    renderer : null,

    /*
     *  Inicia el proceso de obtención de la información
     */
    beginObtainParticipantFlow : function(flowRenderer) {
        this.participantFlow.acts = {};
        this.participantFlow.groups = {};
        this.participantFlow.links = [];
        this.participantFlow.participants = {};

        this.flowRenderer = flowRenderer;
        this.renderer = GroupsRenderer;
        this.obtainParticipantFlow(this.flowRenderer.renderData.mainBlock);
    },
    /*
     *  Fúnción recursiva que obtiene el flujo de participantes
     */
    obtainParticipantFlow : function(block, parentactid) {
        if(block.renderClfps.length > 0) {
            var groups = this.addAvailableParticipantsForBlock(block, parentactid);

            for(var i = 0; i < block.renderClfps.length; i++) {
                var renderClfp = block.renderClfps[i];
                var flow = renderClfp.clfp.flow;
                var paintdata = renderClfp.paintdata;
                for (var a = 0; a < flow.length; a++) {
                    var actid = flow[a].id;
                    var actpaintdata = paintdata.acts[actid];
                    groups = this.addParticipantsForAct(actid, actpaintdata, groups);
                }

                for(var j = 0; j < renderClfp.subBlocks.length; j++) {
                    var subblock = renderClfp.subBlocks[j];
                    var parentactid = flow[subblock.parentphase].id;

                    this.obtainParticipantFlow(subblock, parentactid);
                }
            }
        }
    },
    addAvailableParticipantsForBlock : function(block, parentactid) {
        var firstclfp = block.renderClfps[0].clfp;
        var clfpid = firstclfp.id;
        //id of first clfp in block

        var available = ClfpsCommon.obtainAvailableStudentsClfp(firstclfp);
        // will be the same for the whole block
        var group = {
            key: clfpid,
            x : this.flowRenderer.graphicElements.clfps[clfpid].x + this.flowRenderer.graphicElements.clfps[clfpid].width / 2,
            y : this.flowRenderer.graphicElements.clfps[clfpid].y,
            participants : available
        };
        this.createGroup(group);

        if(parentactid !== undefined) {
            var instid = this.flowRenderer.uiListener.instanceid[clfpid];
            var parentkey = parentactid + "_" + instid;
            var parentgroup = this.participantFlow.groups[parentkey];
            if(parentgroup) {
                this.createLink(parentgroup, group, available);
            }
        }

        return [clfpid];
    },
    addParticipantsForAct : function(actid, actpaintdata, previousgroups) {
        var actgroups = [];
        var maxx = 0;
        var accy = 0;
        var icount = 0;

        for(var idr in actpaintdata.roles) {
            for(var idi in actpaintdata.roles[idr].instances) {
                var asigned = InstanceStudentManagement.obtainAssignedStudents(idi);

                var x = this.flowRenderer.graphicElements.acts[actid].instances[idi].x + this.flowRenderer.graphicElements.acts[actid].instances[idi].width / 2;
                var y = this.flowRenderer.graphicElements.acts[actid].instances[idi].y + this.flowRenderer.graphicElements.acts[actid].instances[idi].height / 2;

                if(x > maxx) {
                    maxx = x;
                }
                accy += y;
                icount++;

                if(asigned.length > 0) {
                    var key = actid + "_" + idi;
                    var group = {
                        key: key,
                        x : x,
                        y : y,
                        participants : asigned,
                        act : actid,
                        instance : idi
                    };
                    this.createGroup(group);
                    actgroups.push(key);

                    if(IDPool.getObject(idr).subtype != "staff") {

                        for(var pgi = 0; pgi < previousgroups.length; pgi++) {
                            var pg = this.participantFlow.groups[previousgroups[pgi]];
                            var incommon = [];
                            for(var i = 0; i < group.participants.length; i++) {
                                for(var j = 0; j < pg.participants.length; j++) {
                                    if(group.participants[i] == pg.participants[j]) {
                                        incommon.push(group.participants[i]);
                                    }
                                }
                            }

                            if(incommon.length > 0) {
                                this.createLink(pg, group, incommon);
                            }
                        }
                    }
                }
            }
        }

        this.participantFlow.acts[actid] = {
            groups : actgroups,
            x : maxx,
            y : accy / icount
        };

        return actgroups;
    },
    createGroup: function(group) {
        this.participantFlow.groups[group.key] = group;
        for (var i = 0; i < group.participants.length; i++) {
            var p = group.participants[i];
            if (!this.participantFlow.participants[p]) {
                this.participantFlow.participants[p] = {groups: [], links: []};
            }
            this.participantFlow.participants[p].groups.push(group);
        } 
    },
    createLink : function(fromgroup, togroup, incommon) {
        var link = {
            from: fromgroup,
            to: togroup,
            key: fromgroup.key + "->" + togroup.key,
            x : 0.5 * (fromgroup.x + togroup.x),
            y : 0.5 * (fromgroup.y + togroup.y),
            x1 : fromgroup.x,
            y1 : fromgroup.y,
            x2 : togroup.x,
            y2 : togroup.y,
            participants : incommon
        };
        this.participantFlow.links.push(link);

        for (var i = 0; i < incommon.length; i++) {
            var p = incommon[i];
            if (!this.participantFlow.participants[p]) {
                this.participantFlow.participants[p] = {groups: [], links: []};
            }
            this.participantFlow.participants[p].links.push(link);
        } 
    }
};
