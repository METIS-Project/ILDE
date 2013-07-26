/*global dojo, IDPool, Context, i18n, GroupPatternManager */

var JigsawGroupNumberPatternFactory = {
    getTitle : function() {
        return i18n.get("grouppattern.jigsawgroup.title");
    },
    getId : function() {
        return "jigsawgrouppattern";
    },
    getInfo : function() {
        return i18n.get("grouppattern.jigsawgroup.info");
    },
    getDefinition : function() {
        return JigsawGroupNumberPattern;
    },
    newPattern : function(actId, instanceId) {
        return new JigsawGroupNumberPattern(actId, instanceId);
    }
};

var JigsawGroupNumberPattern = function(actId, instanceId) {
    /**
     * Indicador de tipo
     */
    this.type = "groupPattern";
    this.subtype = "gn";
    /**
     * Nombre del patrón
     */
    this.title = JigsawGroupNumberPatternFactory.getTitle();
    this.info = JigsawGroupNumberPatternFactory.getInfo();
    /**
     * Identificador del patrón
     */
    this.patternid = JigsawGroupNumberPatternFactory.getId();

    /**
     * Acto afectado por el patrón
     */
    this.actId = actId;
    this.instanceid = instanceId;
    /**
     * Clfp en el que está el acto
     */
    this.clfpId = LearningDesign.findClfpParentOf(actId).clfp.id;

    /**
     * Jigsaw individual phase (previous act)
     */
    this.expertActId = IDPool.getObject(this.clfpId).getFlow()[1].id;

    IDPool.registerNewObject(this);
};

JigsawGroupNumberPattern.prototype.getTitle = function() {
    return this.title;
};

JigsawGroupNumberPattern.prototype.getInfo = function() {
    return this.info;
};

JigsawGroupNumberPattern.prototype.getExternalActDependency = function() {
    return {
        //Depende de la fase de expertos
        actid : this.expertActId,
        type : "gn"
    };
};

/**
 * @param instanceId the group instance to which the patterns is applied
 * @param result the current group management state
 * @param proposed whether result has been proposed by another pattern or is the current group state
 */
JigsawGroupNumberPattern.prototype.check = function(instanceId, result, previousProposal) {
    var studentCount = Context.getAvailableStudents(this.clfpId, instanceId).length;

    var expertGroupNumber = GroupPatternManager.getStateGroupCount(instanceId, this.expertActId);

    //al menos dos grupos de expertos son necesarios
    if (expertGroupNumber < 2) {
        GroupPatternUtils.addAlert(result, i18n.get("grouppattern.jigsawgroup.fewexpertgroups"));
        result.ok = result.fixable = false;
        return;
    }

    //al menos dos estudiantes por cada grupo de expertos son necesarios
    var minStudents = 2 * expertGroupNumber;

    if (studentCount < minStudents) {
        var text = i18n.getReplaced2("grouppattern.jigsawgroup.fewstudents", studentCount, minStudents);
        GroupPatternUtils.addAlert(result, text);
        result.ok = result.fixable = false;
        return;
    }

    //redondeo hacia abajo del número de alumnos en cada grupo de expertos
    var expertNumber = Math.floor(studentCount / expertGroupNumber);
    var maxJigsawGroups = expertNumber;

    var rangeExplanation = i18n.getReplaced1("grouppattern.jigsawgroup.range", maxJigsawGroups);
    var prefExplanation = i18n.getReplaced3("grouppattern.jigsawgroup.best", studentCount, expertGroupNumber, maxJigsawGroups);

    GroupPatternUtils.compareMinMaxPrefGroupNumber(instanceId, result, previousProposal, this.actId, 2, maxJigsawGroups, maxJigsawGroups, rangeExplanation, prefExplanation);
};

GroupPatternManager.registerPatternFactory("gn", JigsawGroupNumberPatternFactory);

