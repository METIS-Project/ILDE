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

/*global dojo, IDPool, Context, i18n */

var ExpertGroupNumberPatternFactory = {
    getTitle : function() {
        return i18n.get("grouppattern.expertgroup.title");
    },
    getId : function() {
        return "expertgrouppattern";
    },
    getInfo : function() {
        return i18n.get("grouppattern.expertgroup.info");
    },
    getDefinition : function() {
        return ExpertGroupNumberPattern;
    },
    newPattern : function(actId, instanceId) {
        return new ExpertGroupNumberPattern(actId, instanceId);
    }
};

var ExpertGroupNumberPattern = function(actId, instanceId) {
    /**
     * Indicador de tipo
     */
    this.type = "groupPattern";
    this.subtype = "gn";
    /**
     * Nombre del patrón
     */
    this.title = ExpertGroupNumberPatternFactory.getTitle();
    this.info = ExpertGroupNumberPatternFactory.getInfo();
    /**
     * Identificador del patrón
     */
    this.patternid = ExpertGroupNumberPatternFactory.getId();

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
    this.jigsawActId = IDPool.getObject(this.clfpId).getFlow()[0].id;

    this.subproblemCount = -1;

    IDPool.registerNewObject(this);
};

ExpertGroupNumberPattern.prototype.getTitle = function() {
    return this.title;
};

ExpertGroupNumberPattern.prototype.getInfo = function() {
    return this.info;
};

ExpertGroupNumberPattern.prototype.getMenuItems = function() {
    return [{
        label : i18n.getReplaced1("grouppattern.expertgroup.menuitem", this.subproblemCount > 1 ? this.subproblemCount : i18n.get("grouppattern.expertgroup.unsetsubproblems")),
        help : i18n.get("grouppattern.expertgroup.menuitem.help") + (this.subproblemCount > 1 ? i18n.getReplaced1("grouppattern.expertgroup.menuitem.help.set", this.subproblemCount) : i18n.get("grouppattern.expertgroup.menuitem.help.unset"))
    }];
};

ExpertGroupNumberPattern.prototype.menuItemClicked = function(index) {
    this.openDialog();
};
/**
 * Actualiza la información para alerta y/o configuraciones propuestas por el
 * patrón
 *
 * @param instanceIds
 *            Array con los identificadores de posibles instancias de las que
 *            puede depender el clfp
 */
ExpertGroupNumberPattern.prototype.check = function(instanceId, result, previousProposal) {
    if (this.subproblemCount > 1) {
        var text = i18n.get("grouppattern.expertgroup.title") + ": " + i18n.getReplaced1("grouppattern.expertgroup.number.alert", this.subproblemCount);
        GroupPatternUtils.compareExactGroupNumber(instanceId, result, previousProposal, this.actId, this.subproblemCount, text);
    } else {
        GroupPatternUtils.compareMinGroupNumber(instanceId, result, previousProposal, this.actId, 2, i18n.get("grouppattern.expertgroup.title") + ": " + i18n.get("grouppattern.expertgroup.nonumber.alert"));
    }
};

/**
 *
 */
ExpertGroupNumberPattern.prototype.openDialog = function() {
    var dlg = new dijit.Dialog({
        id : "ExpertGroupNumberPatternNumberOfSubproblemsDialog",
        title : i18n.get("grouppattern.expertgroup.dialog.title")
    });
    var initialValue = Math.max(this.subproblemCount, 2);

    var content = '<p>${0}</p>';
    content += '<div id="ExpertGroupNumberPatternNumberOfSubproblemsSlider" dojoType="dijit.form.HorizontalSlider" value="' + initialValue + '" minimum="2" maximum="6" discreteValues="5" intermediateChanges="true" style="width:300px;">';
    content += '<ol dojoType="dijit.form.HorizontalRuleLabels" container="topDecoration" style="height:20px;">';
    content += '<li>2</li><li>3</li><li>4</li><li>5</li><li>6</li>';
    content += '</ol></div>';
    content += '<div dojoType="dijit.layout.ContentPane" style="text-align: center;">';
    content += '<div dojoType="dijit.form.Button" id="ExpertGroupNumberPatternNumberOfSubproblemsOk">${1}</div><div dojoType="dijit.form.Button" id="ExpertGroupNumberPatternNumberOfSubproblemsCancel">${2}</div>';
    content += "</div>";
    content += "</div>";
    content = dojo.string.substitute(content, [i18n.get("grouppattern.expertgroup.dialog.text"), i18n.get("common.ok"), i18n.get("common.cancel")]);

    dlg.setContent(content);
    dojo.body().appendChild(dlg.domNode);
    dlg.show();
    dojo.connect(dlg, "hide", this, "closeDialog");
    dojo.connect(dojo.byId("ExpertGroupNumberPatternNumberOfSubproblemsCancel"), "onclick", this, "closeDialog");
    dojo.connect(dojo.byId("ExpertGroupNumberPatternNumberOfSubproblemsOk"), "onclick", this, "setSubproblemNumber");
    dlg.resize();
};

ExpertGroupNumberPattern.prototype.closeDialog = function() {
    dijit.byId("ExpertGroupNumberPatternNumberOfSubproblemsDialog").destroyRecursive();
};

ExpertGroupNumberPattern.prototype.setSubproblemNumber = function() {
    var value = dijit.byId("ExpertGroupNumberPatternNumberOfSubproblemsSlider").value;
    this.subproblemCount = value;
    this.closeDialog();
    Loader.save();
};

GroupPatternManager.registerPatternFactory("gn", ExpertGroupNumberPatternFactory);

