/*global LearningDesign */

var GroupPatternManager = {
  patternFactories: {
    gn: [],
    pa: []
  },
  actlist: [],
  actstate: {},
  actdependencies: {},
  registerPatternFactory: function(type, factory) {
    this.patternFactories[type].push(factory);
    Inheritance.registerPattern(factory.getId(), factory.getDefinition());
  },
  getFactories: function(type) {
    return this.patternFactories[type];
  },
  getFactory: function(type, index) {
    return this.patternFactories[type][index];
  },
  init: function() {
    this.resolveDepedencies();
  },
  getActTypeState: function(instanceid, actid, type) {
    return this.actstate[type + "_" + actid + "_" + instanceid];
  },
  getActState: function(instanceid, actid) {
    var key = actid + "_" + instanceid;
    return {
      gn: this.actstate["gn_" + key],
      pa: this.actstate["pa_" + key]
    };
  },
  getStateGroupCount: function(instanceid, actid) {
    var key = "gn_" + actid + "_" + instanceid;
    if (this.actstate[key]) {
      return this.actstate[key].proposal.getNumber();
    } else {
      var roleid = Context.getStudentRoles(actid)[0].id;
      return Context.getGroupNumber(roleid, instanceid);
    }
  },
  getParticipantState: function(instanceid, actid) {
    var key = "pa_" + actid + "_" + instanceid;
    if (this.actstate[key]) {
      return this.actstate[key].proposal;
    } else {
      return GroupPatternUtils.getGroupParticipants(instanceid, actid);
    }
  },
  check: function() {
    this.actstate = {};
    for (var i = 0; i < this.actlist.length; i++) {
      var actdef = this.actlist[i];

      var instances = DesignInstance.parentInstances(actdef.clfpid);

      if (instances == null) {
        instances = [{
            id: 0
          }];
      }

      for (var j = 0; j < instances.length; j++) {
        var instanceId = instances[j].id;
        var completekey = actdef.key + "_" + instanceId;

        var depkeys = this.actdependencies.get(actdef.key);

        var actpatterns = LearningDesign.data.groupPatterns[actdef.actid][actdef.type];

        if (actpatterns.length > 0) {
          var result = {
            data: {
              instanceid: instanceId,
              actid: actdef.actid,
              clfpid: actdef.clfpid,
              type: actdef.type,
              completekey: completekey
            },
            proposal: null,
            alerts: [],
            ok: true,
            fixable: true,
            toupdate: [],
            why: ""
          };

          var dependenciesok = true;
          for (var k = 0; k < depkeys.length; k++) {
            var completedepkey = depkeys[k] + "_" + instanceId;
            if (this.actstate[completedepkey] && !this.actstate[completedepkey].ok) {
              if (this.actstate[completedepkey].fixable) {
                result.toupdate.push(completedepkey);
              } else {
                dependenciesok = false;
                break;
                // cannot update 'cause dependencies haven't been
                // updated yet
              }
            }
          }

          if (dependenciesok) {
            if (actdef.type == "gn") {
              result.proposal = GroupPatternUtils.getFirstStudentRoleGroupNumber(instanceId, actdef.actid);
            } else {
              result.proposal = GroupPatternUtils.getGroupParticipants(instanceId, actdef.actid);
              var groupCount = this.getStateGroupCount(instanceId, actdef.actid);
              result.proposal.setGroupCount(groupCount);
            }

            var n = this.countPatternsToUpdate(result.toupdate);
            if (n > 0) {
              var text = i18n.getReplaced1("groupPattern.updatesNeeded.alert", n);
              GroupPatternUtils.appendExplanation(result, text);
              result.ok = false;
            }

            for (var k = 0; k < actpatterns.length; k++) {
              actpatterns[k].check(instanceId, result, k > 0);
              if (!result.ok && !result.fixable) {
                break;
              }
            }

          } else {
            GroupPatternUtils.addAlert(result, i18n.get("groupPattern.baddependencies"));
            result.ok = result.fixable = false;
          }
          this.actstate[completekey] = result;
        }
      }
    }
  },
  countPatternsToUpdate: function(updatelist) {
    var count = updatelist.length;
    for (var i = 0; i < updatelist.length; i++) {
      var state = this.actstate[updatelist[i]];
      if (state && !state.ok && state.fixable) {
        count += this.countPatternsToUpdate(state.toupdate);
      }
    }
    return count;
  },
  updatePattern: function(key) {
    var state = this.actstate[key];
    if (state && !state.ok && state.fixable) {
      ChangeManager.startGroup();
      for (var i = 0; i < state.toupdate.length; i++) {
        this.updatePattern(state.toupdate[i]);
      }
      switch (state.data.type) {
        case "gn":
          this.updateGroupNumberPattern(state);
          break;
        case "pa":
          this.updateParticipants(state);
          break;
        default:
          break;
      }
      ChangeManager.endGroup();
    }
  },
  updateGroupNumberPattern: function(state) {
    var clfp = IDPool.getObject(state.data.clfpid);
    var proposed = state.proposal.getNumber();

    var groupId = Context.getStudentRoles(state.data.actid)[0].id;
    var instances = ClfpsCommon.getInstances(clfp, groupId, LearningFlow);
    var numberInstances = instances.length;

    //Clono la  última instancia el número de veces necesario
    var instanceCloneId = instances[instances.length - 1].id;
    //Guardo los identificadores de las instancias a borrar
    var instanceDeleteIds = new Array();
    for (var i = instances.length - 1; i >= proposed; i--) {
      instanceDeleteIds.push(instances[i].id);
    }
    for (var i = numberInstances; i < proposed; i++) {
      clfp.clonar(instanceCloneId);
    }
    for (var i = 0; i < instanceDeleteIds.length; i++) {
      ClfpsCommon.deleteInstance(instanceDeleteIds[i], clfp);
    }
  },
  updateParticipants: function(state) {
    var clfp = IDPool.getObject(state.data.clfpid);
    var state = this.getActTypeState(state.data.instanceid, state.data.actid, "pa");
    var proposed = state.proposal.getGroups();

    var idGrupo = Context.getStudentRoles(state.data.actid)[0].id;
    var instances = ClfpsCommon.getInstances(clfp, idGrupo, LearningFlow);

    for (var i = 0; i < instances.length; i++) {
      var participants = instances[i].getParticipants();
      for (var j = 0; j < participants.length; j++) {
        clfp.borrar(instances[i], participants[j]);
      }
      for (var j = 0; j < proposed[i].length; j++) {
        clfp.asignar(instances[i], proposed[i][j]);
      }
    }
  },
  /* private dependencies */
  resolveDepedencies: function() {
    this.actdependencies = this.getActDependencies();
    this.actlist = this.getOrderedActList(this.actdependencies);
  },
  getActDependencies: function() {
    var dependencies = {
      // { key: [depkeys] }
      list: {},
      setActDependencies: function(actid, actpatterns) {
        this.addDependency(actid, "pa", actid, "gn");
        this.setDependencies(actid, "gn", actpatterns.gn);
        this.setDependencies(actid, "pa", actpatterns.pa);
      },
      setDependencies: function(actid, type, patterns) {
        for (var i = 0; i < patterns.length; i++) {
          var pattern = patterns[i];
          if (pattern.additionalAffectedActs) {
            var affectedactids = pattern.additionalAffectedActs();
            for (var j = 0; j < affectedactids.length; j++) {
              this.addDependency(affectedactids[j], type, actid, type);
            }
          }
          if (pattern.getExternalActDependency) {
            var actdep = pattern.getExternalActDependency();
            if (actdep) {
              this.addDependency(actid, type, actdep.actid, actdep.type);
            }
          }
        }
      },
      addDependency: function(actid, type, depactid, deptype) {
        var key = type + "_" + actid;
        var depkey = deptype + "_" + depactid;
        if (!this.list[key]) {
          this.list[key] = [];
        }

        this.list[key].push(depkey);
      },
      get: function(key) {
        if (!this.list[key]) {
          this.list[key] = [];
        }
        return this.list[key];
      }
    };

    for (var actid in LearningDesign.data.groupPatterns) {
      dependencies.addDependency(actid, "pa", actid, "gn");

      var actpatterns = LearningDesign.data.groupPatterns[actid];
      dependencies.setDependencies(actid, "gn", actpatterns.gn);
      dependencies.setDependencies(actid, "pa", actpatterns.pa);
    }

    return dependencies;
  },
  getOrderedActList: function(dependencies) {
    var list = [];
    var remaining = [];
    var remains = {};

    for (var actid in LearningDesign.data.groupPatterns) {
      var clfpid = LearningDesign.findClfpParentOf(actid).clfp.id;

      var key = "gn_" + actid;
      remaining.push({
        key: key,
        actid: actid,
        type: "gn",
        clfpid: clfpid
      });
      remains[key] = true;

      key = "pa_" + actid;
      remaining.push({
        key: key,
        actid: actid,
        type: "pa",
        clfpid: clfpid
      });
      remains[key] = true;
    }

    while (remaining.length > 0) {
      var notyet = [];
      var atleastone = false;
      for (var i = 0; i < remaining.length; i++) {
        var a = remaining[i];
        var ok = true;
        var deps = dependencies.get(a.key);

        for (var j = 0; j < deps.length; j++) {
          if (remains[deps[j]]) {
            ok = false;
            break;
          }
        }
        if (ok) {
          list.push(a);
          remains[a.key] = false;
          atleastone = true;
        } else {
          notyet.push(a);
        }
      }
      if (!atleastone) {
        console.log("something wrong with pattern dependencies...");
        break;
      }
      remaining = notyet;
    }
    return list;
  },
  /* generic */
  getPatternsForAct: function(actid) {
    if (!LearningDesign.data.groupPatterns[actid]) {
      LearningDesign.data.groupPatterns[actid] = {
        gn: [],
        pa: []
      };
    }

    return LearningDesign.data.groupPatterns[actid];
  },
  findPattern: function(pattern) {
    for (var actid in LearningDesign.data.groupPatterns) {
      var actpatterns = LearningDesign.data.groupPatterns[actid];
      var index = -1;
      switch (pattern.subtype) {
        case "gn":
          index = actpatterns.gn.indexOf(pattern);
          break;
        case "pa":
          index = actpatterns.pa.indexOf(pattern);
          break;
      }

      if (index >= 0) {
        return {
          actid: actid,
          patterns: actpatterns,
          index: index
        };
      }
    }
    return null;
  },
  /**
   * Borra los patrones asociados a un clfp y sus desciendientes
   */
  deleteClfpPatterns: function(clfp) {
    this.removeClfpPatterns(clfp);
    this.init();
  },
  removeClfpPatterns: function(clfp) {
    for (var i = 0; i < clfp.flow.length; i++) {
      if (clfp.flow[i].type == "clfpact") {
        for (var j = 0; j < clfp.flow[i].clfps.length; j++) {
          GroupPatternManager.deleteClfpPatterns(clfp.flow[i].clfps[j]);
        }
      }
    }
    for (i = 0; i < clfp.flow.length; i++) {
      var actid = clfp.flow[i].id;
      delete LearningDesign.data.groupPatterns[actid];
    }
  }
};
