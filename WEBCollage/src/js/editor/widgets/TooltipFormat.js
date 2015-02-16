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


