/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var TooltipFormat = {

    addTooltip: function(element, object) {
        var tooltipInfo = this.objectInfo(object);
        if (tooltipInfo) {
            new dijit.Tooltip( {
                label : tooltipInfo,
                connectId : [ element.id ],
                showDelay : 100
            });
        }
    },

    objectInfo: function(object) {
        if (object) {
            var str = '<div class="tooltipObjectInfo">';

            if (object.type == "activity") {
                str += this.activityInfo(object);
            } else if (object.type == "role") {
                str += this.roleInfo(object);
            }

            str += '</div>';
            return str;
        } else {
            return null;
        }
    },

    activityInfo: function(activity) {
        var str = "";
        str += this.getTitle(activity);
        str += this.getSeparator();
        str += this.getDescription(activity);
        return str;
    },

    roleInfo: function(role) {
        var str = "";
        str += this.getTitle(role);
        str += this.getSeparator();
        str += this.getDescription(role);
        return str;
    },

    getTitle: function(object) {
        return '<div class="tooltipObjectTitle">' + object.getTitle() + '</div>';
    },

    getDescription: function(object) {
        return '<div class="tooltipObjectDescription">' + object.description.replace(/\n/g, "<br/>") + '</div>';
    },

    getSeparator: function() {
        return '<div class="tooltipInfoSeparator"></div>';
    }
};


