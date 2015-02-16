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
        GroupPatternUtils.addAlert(result, i18n.get("grouppattern.jigsawgroup.title") + ": "  + i18n.get("grouppattern.jigsawgroup.fewexpertgroups"));
        result.ok = result.fixable = false;
        return;
    }

    //al menos dos estudiantes por cada grupo de expertos son necesarios
    var minStudents = 2 * expertGroupNumber;

    if (studentCount < minStudents) {
        var text = i18n.getReplaced2("grouppattern.jigsawgroup.fewstudents", studentCount, minStudents);
        GroupPatternUtils.addAlert(result, i18n.get("grouppattern.jigsawgroup.title") + ": "  + text);
        result.ok = result.fixable = false;
        return;
    }

    //redondeo hacia abajo del número de alumnos en cada grupo de expertos
    var expertNumber = Math.floor(studentCount / expertGroupNumber);
    var maxJigsawGroups = expertNumber;

    var rangeExplanation = i18n.getReplaced1("grouppattern.jigsawgroup.range", maxJigsawGroups);
    var prefExplanation = i18n.getReplaced3("grouppattern.jigsawgroup.best", studentCount, expertGroupNumber, maxJigsawGroups);

    GroupPatternUtils.compareMinMaxPrefGroupNumber(instanceId, result, previousProposal, this.actId, 2, maxJigsawGroups, maxJigsawGroups, i18n.get("grouppattern.jigsawgroup.title") + ": "  + rangeExplanation, i18n.get("grouppattern.jigsawgroup.title") + ": " + prefExplanation);
};

GroupPatternManager.registerPatternFactory("gn", JigsawGroupNumberPatternFactory);

