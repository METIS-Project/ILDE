/*global IDPool, i18n */

var FixedNumberGroupPatternFactory = {
    getTitle : function() {
        return i18n.get("grouppattern.fixednumbergroups.title");
    },
    getId : function() {
        return "fixednumbergroups";
    },
    getInfo : function() {
        return i18n.get("grouppattern.fixednumbergroups.info");
    },   
    getDefinition : function() {
        return FixedNumberGroupPattern;
    },
    newPattern : function(actId, instanceId) {
        return new FixedNumberGroupPattern(actId, instanceId);
    }
};

var FixedNumberGroupPattern = function(actId, instanceId) {
    this.type = "groupPattern";
    this.subtype = "gn";
    this.title = FixedNumberGroupPatternFactory.getTitle();
    this.info = FixedNumberGroupPatternFactory.getInfo();
    this.patternid = FixedNumberGroupPatternFactory.getId();
    this.number = -1;

    this.actId = actId;
    this.instanceid = instanceId;
    this.clfpId = LearningDesign.findClfpParentOf(actId).clfp.id;

    IDPool.registerNewObject(this);
};

FixedNumberGroupPattern.prototype.getTitle = function() {
    return this.title;
};

FixedNumberGroupPattern.prototype.getInfo = function() {
    return this.info;
};

/**
 * @param instanceId the group instance to which the patterns is applied
 * @param result the current group management state
 * @param proposed whether result has been proposed by another pattern or is the current group state
 */
FixedNumberGroupPattern.prototype.check = function(instanceId, result, proposed) {
    if (this.number > 0) {
        var text = i18n.getReplaced1("grouppattern.fixednumbergroups.number", this.number)
        GroupPatternUtils.compareExactGroupNumber(instanceId, result, proposed, this.actId, this.number, text);
    } else {
        GroupPatternUtils.addAlert(result, i18n.get("grouppattern.fixednumbergroups.unspecified"));
    }
};

FixedNumberGroupPattern.prototype.getMenuItems = function() {
    //var label = this.number > 1 ? i18n.getReplaced1("grouppattern.fixednumbergroups.menuitem.number", this.number) : i18n.get("grouppattern.fixednumbergroups.menuitem.unspecified");
    var label = this.number >= 1 ? i18n.getReplaced1("grouppattern.fixednumbergroups.menuitem.number", this.number) : i18n.get("grouppattern.fixednumbergroups.menuitem.unspecified");
    return [{
        label : label,
        help : i18n.get("help.grouppattern.fixednumbergroups.menuitem")
    }];
};

FixedNumberGroupPattern.prototype.menuItemClicked = function(index) {            
    var groupId = Context.getStudentRoles(this.actId)[0].id;
    var clfp = IDPool.getObject(this.clfpId);
    var instances = ClfpsCommon.getInstances(clfp, groupId, LearningFlow);
    var idparents = new Array();
    for (var i = 0; i < instances.length; i++){
        if (idparents.indexOf(instances[i].idParent)==-1){
            idparents.push(instances[i].idParent);
        }
    }
    var canBeDeleted = 0;
    for (var i = 0; i < idparents.length; i++){
        canBeDeleted += (DesignInstance.instanciasGrupoMismoPadre(groupId, idparents[i]).length -1);
    }
    var minimum = instances.length - canBeDeleted;
    var maximum = Math.max(minimum,10);
    this.openDialog(minimum,maximum);
};

/**
 * Creates and opens the group number dialog.
 */
FixedNumberGroupPattern.prototype.openDialog = function(minimum, maximum) {
    var dlg = new dijit.Dialog({
        id : "FixedNumberGroupPatternNumberDialog",
        title : i18n.get("grouppattern.fixednumbergroups.title")
    });
    //var initialValue = Math.max(this.number, 2);
    //var minimum = 2;
    //var maximum = 10;
    var initialValue = Math.max(this.number, minimum);
    var discreteValues = (maximum - minimum) + 1;

    var content = '<p>${0}</p>';
    content += '<div id="FixedNumberGroupPatternSlider" dojoType="dijit.form.HorizontalSlider" value="' + initialValue + '" minimum="' + minimum + '" maximum="' + maximum + '" discreteValues="' + discreteValues + '" intermediateChanges="true" style="width:300px;">';
    content += '<ol dojoType="dijit.form.HorizontalRuleLabels" container="topDecoration" style="height:20px;">';
    //content += '<li>2</li><li>3</li><li>4</li><li>5</li><li>6</li><li>7</li><li>8</li><li>9</li><li>10</li>';
    for (var i = minimum; i <= maximum; i++){
        content += '<li>' + i + '</li>';
    }
    content += '</ol></div>';
    content += '<div dojoType="dijit.layout.ContentPane" style="text-align: center;">';
    content += '<div dojoType="dijit.form.Button" id="FixedNumberGroupPatternOk">${1}</div><div dojoType="dijit.form.Button" id="FixedNumberGroupPatternCancel">${2}</div>';
    content += "</div>";
    content += "</div>";
    content = dojo.string.substitute(content, [i18n.get("grouppattern.fixednumbergroups.dialog.title"), i18n.get("common.ok"), i18n.get("common.cancel")]);

    dlg.setContent(content);
    dojo.body().appendChild(dlg.domNode);
    dlg.show();
    dojo.connect(dlg, "hide", this, "closeDialog");
    dojo.connect(dojo.byId("FixedNumberGroupPatternCancel"), "onclick", this, "closeDialog");
    dojo.connect(dojo.byId("FixedNumberGroupPatternOk"), "onclick", this, "setNumber");
    dlg.resize();
};

FixedNumberGroupPattern.prototype.closeDialog = function() {
    dijit.byId("FixedNumberGroupPatternNumberDialog").destroyRecursive();
};

FixedNumberGroupPattern.prototype.setNumber = function() {
    this.number = dijit.byId("FixedNumberGroupPatternSlider").value;
    this.closeDialog();
    Loader.save();
};

GroupPatternManager.registerPatternFactory("gn", FixedNumberGroupPatternFactory);
