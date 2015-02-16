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

var LearningFlowSelectMode = {
  mode: null,
  selectData: null,
  isSelecting: function() {
    return this.mode != null;
  },
  getMenu: function(obj, data) {
    if (this._isSelectableObject(obj)) {
      var texts;
      switch (this.mode) {
        case "clfp":
          texts = ["flow.select.clfp", "help.flow.select.clfp"];
          break;
        case "act":
          texts = ["flow.select.activity", "help.flow.select.activity"];
          break;
        case "activity":
          texts = ["flow.select.activity", "help.flow.select.activity"];
          break;
        default:
          return false;
      }


      var items = new Array();
      items.push({
        label: i18n.getReplaced1(texts[0], obj.getTitle()),
        icon: "select",
        onClick: function(data) {
          LearningFlowSelectMode.activate(data);
        },
        data: obj,
        help: i18n.get(texts[1])
      });

      if (this.mode === "activity") {
        var isLearner = IDPool.getObject(data.roleid).subtype === "learner";

        items.push({
          isSeparator: true
        });
        items.push({
          icon: isLearner ? "addlearningactivity" : "addsupportactivity",
          label: i18n.get("flow.activities.add.relative")
        });

        items.push({
          label: i18n.get("flow.activities.add.before"),
          onClick: function(data) {
            ActivitiesPainter.createNewActivity(data.actid, data.roleid, data.index);
            /* before */
          },
          data: data,
          isSubMenu: true,
          help: i18n.get("help.flow.activities.add.before")
        });

        items.push({
          label: i18n.get("flow.activities.add.after"),
          onClick: function(data) {
            ActivitiesPainter.createNewActivity(data.actid, data.roleid, data.index);
            /* after */
          },
          data: {
            index: data.index + 1,
            roleid: data.roleid,
            actid: data.actid
          },
          isSubMenu: true,
          help: i18n.get("help.flow.activities.add.after")
        });

      }
      return items;
    } else {
      return null;
    }
  },
  _isSelectableObject: function(obj) {
    return this.mode === obj.type || (this.mode === 'act' && obj.type === 'clfpact');
  },
  activate: function(obj) {
    if (this._isSelectableObject(obj)) {
      if (this.selectData && this.selectData.callback) {
        this.selectData.callback(obj, this.selectData.callbackData);
      }
      this.stopSelect();
    }
  },
  startSelect: function(what, selectData) {
    this.mode = what;
    this.selectData = selectData;
    dojo.style(dojo.byId("FlowSelectModeAnnouncement"), "display", "block");
    dojo.byId("FlowSelectModeAnnouncementText").innerHTML = selectData.title;
    var canvaswidth = dojo.position(dojo.byId("FlowPaintContainer")).w;
    var width = dojo.position(dojo.byId("FlowSelectModeAnnouncement")).w;

    dojo.animateProperty({
      node: "FlowSelectModeAnnouncement",
      properties: {
        left: {
          start: -width,
          end: .5 * (canvaswidth - width)
        }
      },
      duration: 700
    }).play();
  },
  stopSelect: function() {
    this.mode = null;
    this.selectData = null;

    dojo.style(dojo.byId("FlowSelectModeAnnouncement"), "display", "none");
  },
  cancel: function() {
    if (this.selectData && this.selectData.callback) {
      this.selectData.callback(null);
    }

    this.stopSelect();
  }
};
