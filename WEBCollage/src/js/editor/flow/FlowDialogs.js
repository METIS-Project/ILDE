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

var FlowDialogs = {

	onKeys : [],

	save : function(dlgId) {
		this.onKeys[dlgId] = dijit.byId(dlgId)._onKey;
		dijit.byId(dlgId)._onKey = null;
	},

	recover : function(dlgId) {
		this.onKeys[dlgId] = dijit.byId(dlgId)._onKey;
		dijit.byId(dlgId)._onKey = this.onKeys[dlgId];
		delete this.onKeys[dlgId];
	},

	prepareDialogs : function() {
		var activityDlg = dijit.byId("LDActivityEditDialog");

		var loSelectorDlg = dijit.byId("LDActivityLOSelector");
		loSelectorDlg.onOpen = function() {
			EditActivityDialog.loadLOSelector();
		};
		loSelectorDlg.execute = function() {
			EditActivityDialog.pickUpSelectedLOs();
                        //Save the selected learning objectives
                        //EditActivityDialog.save();
		};

		var resourceSelectorDlg = dijit.byId("LDActivityResourceSelector");
		resourceSelectorDlg.onOpen = function() {
			EditActivityDialog.loadResourceSelector();
		};
		resourceSelectorDlg.execute = function() {
			EditActivityDialog.pickUpSelectedResources();
                        //Save the selected resources
                        //EditActivityDialog.save();
		};
	},

	getPosition : function(who) {
		var T = 0, L = 0;
		while (who) {
			L += who.offsetLeft;
			T += who.offsetTop;
			who = who.offsetParent;
		}
		return [ L, T ];
	}
};
