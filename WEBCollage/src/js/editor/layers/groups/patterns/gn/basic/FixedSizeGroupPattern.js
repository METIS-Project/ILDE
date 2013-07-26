/*global IDPool, i18n */

var FixedSizeGroupPatternFactory = {
    getTitle : function() {
        return i18n.get("grouppattern.fixedsizegroups.title");
    },
    getId : function() {
        return "fixedsizegroups";
    },
    getInfo : function() {
        return i18n.get("grouppattern.fixedsizegroups.info");
    },
    getDefinition : function() {
        return FixedSizeGroupPattern;
    },
    newPattern : function(actId, instanceId) {
        return new FixedSizeGroupPattern(actId, instanceId);
    }
};

var FixedSizeGroupPattern = function(actId, instanceId) {
    this.type = "groupPattern";
    this.subtype = "gn";
    this.title = FixedSizeGroupPatternFactory.getTitle();
    this.info = FixedSizeGroupPatternFactory.getInfo();
    this.patternid = FixedSizeGroupPatternFactory.getId();
    this.number = -1;

    this.actId = actId;
    this.instanceid = instanceId;
    this.clfpId = LearningDesign.findClfpParentOf(actId).clfp.id;

    IDPool.registerNewObject(this);
};

FixedSizeGroupPattern.prototype.getTitle = function() {
    return this.title;
};

FixedSizeGroupPattern.prototype.getInfo = function() {
    return this.info;
};

FixedSizeGroupPattern.prototype.check = function(instanceId, result, proposed) {
    if (this.number > 0) {
        var studentCount = Context.getAvailableStudents(this.clfpId, instanceId).length;
        var n = Math.max(1, Math.floor(studentCount / this.number));

        var text = i18n.getReplaced("grouppattern.fixedsizegroups.number", [(studentCount == 1 ? i18n.get("grouppattern.fixedsizegroups.students.one") : i18n.getReplaced1("grouppattern.fixedsizegroups.students.several", studentCount)), (n == 1 ? i18n.get("grouppattern.fixedsizegroups.group.one") : i18n.getReplaced1("grouppattern.fixedsizegroups.group.several", n)), this.number]);
        GroupPatternUtils.compareExactGroupNumber(instanceId, result, proposed, this.actId, n, text);
    } else {
        GroupPatternUtils.addAlert(result, i18n.get("grouppattern.fixedsizegroups.unspecified"));
    }
};

FixedSizeGroupPattern.prototype.getMenuItems = function() {
    var text = this.number > 1 ? i18n.getReplaced1("grouppattern.fixedsizegroups.menuitem.number", this.number) : i18n.get("grouppattern.fixedsizegroups.menuitem.unspecified");
    return [{
        label : text,
        help : i18n.get("help.grouppattern.fixedsizegroups.menuitem")
    }];
};



FixedSizeGroupPattern.prototype.menuItemClicked = function(index) {
    this.openDialog();
};

/**
 * Creates and opens the group size dialog.
 */
FixedSizeGroupPattern.prototype.openDialog = function() {
    var dlg = new dijit.Dialog({
        id : "FixedSizeGroupPatternNumberDialog",
        title : i18n.get("grouppattern.fixedsizegroups.title")
    });
    var initialValue = Math.max(this.number, 2);

    var content = '<p>${0}</p>';
    content += '<div id="FixedSizeGroupPatternSlider" dojoType="dijit.form.HorizontalSlider" value="' + initialValue + '" minimum="2" maximum="10" discreteValues="9" intermediateChanges="true" style="width:300px;">';
    content += '<ol dojoType="dijit.form.HorizontalRuleLabels" container="topDecoration" style="height:20px;">';
    content += '<li>2</li><li>3</li><li>4</li><li>5</li><li>6</li><li>7</li><li>8</li><li>9</li><li>10</li>';
    content += '</ol></div>';
    content += '<div dojoType="dijit.layout.ContentPane" style="text-align: center;">';
    content += '<div dojoType="dijit.form.Button" id="FixedSizeGroupPatternOk">${1}</div><div dojoType="dijit.form.Button" id="FixedSizeGroupPatternCancel">${2}</div>';
    content += "</div>";
    content += "</div>";
    content = dojo.string.substitute(content, [i18n.get("grouppattern.fixedsizegroups.dialog.title"), i18n.get("common.ok"), i18n.get("common.cancel")]);

    dlg.setContent(content);
    dojo.body().appendChild(dlg.domNode);
    dlg.show();
    dojo.connect(dlg, "hide", this, "closeDialog");
    dojo.connect(dojo.byId("FixedSizeGroupPatternCancel"), "onclick", this, "closeDialog");
    dojo.connect(dojo.byId("FixedSizeGroupPatternOk"), "onclick", this, "setNumber");
    dlg.resize();
};

FixedSizeGroupPattern.prototype.closeDialog = function() {
    dijit.byId("FixedSizeGroupPatternNumberDialog").destroyRecursive();
};

FixedSizeGroupPattern.prototype.setNumber = function() {
    this.number = dijit.byId("FixedSizeGroupPatternSlider").value;
    this.closeDialog();
    Loader.save();
};

GroupPatternManager.registerPatternFactory("gn", FixedSizeGroupPatternFactory);
