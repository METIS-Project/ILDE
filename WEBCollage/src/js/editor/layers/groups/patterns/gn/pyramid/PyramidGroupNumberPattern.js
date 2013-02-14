/*global dojo, IDPool, Context, i18n, GroupPatternManager */

var PyramidGroupNumberPatternFactory = {
  getTitle: function() {
    return i18n.get("grouppattern.jigsawgroup.title");
  },
  getId: function() {
    return "jigsawgrouppattern";
  },
  getDefinition: function() {
    return PyramidGroupNumberPattern;
  },
  newPattern: function(actId) {
    return new PyramidGroupNumberPattern(actId);
  }
};

var PyramidGroupNumberPattern = function(actId) {
  /**
   * Indicador de tipo
   */
  this.type = "groupPattern";
  this.subtype = "gn";
  /**
   * Nombre del patrón
   */
  this.title = PyramidGroupNumberPatternFactory.getTitle();
  /**
   * Identificador del patrón
   */
  this.patternid = PyramidGroupNumberPatternFactory.getId();

  /**
   * Acto afectado por el patrón
   */
  this.actId = actId;
  /**
   * Clfp en el que está el acto
   */
  this.clfpId = LearningDesign.findClfpParentOf(actId).clfp.id;
  this.parentActId = false;
  var flow = IDPool.getObject(this.clfpId).getFlow();

  for (var i = 0; i < flow.length - 1; i++) {
    if (flow[i].id === this.actId) {
      this.parentActId = flow[i + 1].id;
    }
  }

  IDPool.registerNewObject(this);
};

PyramidGroupNumberPattern.prototype.getTitle = function() {
  return this.title;
};

PyramidGroupNumberPattern.prototype.getExternalActDependency = function() {
  return this.parentActId ? {
    actid: this.expertActId,
    type: "gn"
  } : false;
};

/**
 * @param instanceId the group instance to which the patterns is applied
 * @param result the current group management state
 * @param proposed whether result has been proposed by another pattern or is the current group state
 */
PyramidGroupNumberPattern.prototype.check = function(instanceId, result, previousProposal) {
  var studentCount = Context.getAvailableStudents(this.clfpId, instanceId).length;

  var expertGroupNumber = GroupPatternManager.getStateGroupCount(instanceId, this.expertActId);

  if (expertGroupNumber < 2) {
    GroupPatternUtils.addAlert(result, i18n.get("grouppattern.jigsawgroup.fewexpertgroups"));
    result.ok = result.fixable = false;
    return;
  }

  var minStudents = 2 * expertGroupNumber;

  if (studentCount < minStudents) {
    var text = i18n.getReplaced2("grouppattern.jigsawgroup.fewstudents", studentCount, minStudents);
    GroupPatternUtils.addAlert(result, text);
    result.ok = result.fixable = false;
    return;
  }

  var expertNumber = Math.floor(studentCount / expertGroupNumber);
  var maxJigsawGroups = expertNumber;

  var rangeExplanation = i18n.getReplaced1("grouppattern.jigsawgroup.range", maxJigsawGroups);
  var prefExplanation = i18n.getReplaced1("grouppattern.jigsawgroup.best", maxJigsawGroups);

  GroupPatternUtils.compareMinMaxPrefGroupNumber(instanceId, result, previousProposal, this.actId, 2, maxJigsawGroups, maxJigsawGroups, rangeExplanation, prefExplanation);
};

GroupPatternManager.registerPatternFactory("gn", PyramidGroupNumberPatternFactory);

