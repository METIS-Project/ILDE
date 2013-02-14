/*global IDPool, i18n */

var FixedNumberGroupPatternFactory = {
    getTitle : function() {
        return i18n.get("grouppattern.fixednumbergroups.title");
    },
    getId : function() {
        return "fixednumbergroups";
    },
    getDefinition : function() {
        return FixedNumberGroupPattern;
    },
    newPattern : function(actId) {
        return new FixedNumberGroupPattern(actId);
    }
};

var FixedNumberGroupPattern = function(actId) {
    this.type = "groupPattern";
    this.subtype = "gn";
    this.title = FixedNumberGroupPatternFactory.getTitle();
    this.patternid = FixedNumberGroupPatternFactory.getId();
    this.number = -1;

    this.actId = actId;
    this.clfpId = LearningDesign.findClfpParentOf(actId).clfp.id;

    IDPool.registerNewObject(this);
};

FixedNumberGroupPattern.prototype.getTitle = function() {
    return this.title;
};

FixedNumberGroupPattern.prototype.check = function(instanceId, result) {
    if(this.number > 0) {
        GroupPatternUtils.compareNumberFirstStudentRole(instanceId, result, this.actId, this.number, "_x_specified number is " + this.number);
    } else {
        GroupPatternUtils.addAlert(result, "_x_fixed group number not specified");
    }
};

FixedNumberGroupPattern.prototype.getMenuItems = function() {
    return [{
        label : "_x_ select group number (" + (this.number > 1 ? this.number : "undefined") + ")",
        help : "_x_ sets the group number"
    }];
};

FixedNumberGroupPattern.prototype.menuItemClicked = function(index) {
    this.openDialog();
};

/**
 *
 */
FixedNumberGroupPattern.prototype.openDialog = function() {
    var dlg = new dijit.Dialog({
        id : "FixedNumberGroupPatternNumberDialog",
        title : "the title"
    });
    var initialValue = Math.max(this.number, 2);

    var content = '<p>${0}</p>';
    content += '<div id="FixedNumberGroupPatternSlider" dojoType="dijit.form.HorizontalSlider" value="' + initialValue + '" minimum="2" maximum="10" discreteValues="9" intermediateChanges="true" style="width:300px;">';
    content += '<ol dojoType="dijit.form.HorizontalRuleLabels" container="topDecoration" style="height:20px;">';
    content += '<li>2</li><li>3</li><li>4</li><li>5</li><li>6</li><li>7</li><li>8</li><li>9</li><li>10</li>';
    content += '</ol></div>';
    content += '<div dojoType="dijit.layout.ContentPane" style="text-align: center;">';
    content += '<div dojoType="dijit.form.Button" id="FixedNumberGroupPatternOk">${1}</div><div dojoType="dijit.form.Button" id="FixedNumberGroupPatternCancel">${2}</div>';
    content += "</div>";
    content += "</div>";
    content = dojo.string.substitute(content, ["_x_ select group number", i18n.get("common.ok"), i18n.get("common.cancel")]);

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
