/*
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Glue!PS.

Glue!PS is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Glue!PS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * Menú que se muestra para la creación de una actividad
 */

var CreateActivityMenu = {
		
		init: function(){
		},		
		
		/**
		 * Menú de creación de una actividad
		 * 
		 * @param data
		 */
		getCreateActivityMenu : function(data) {
			var items = new Array();
			items.push({
				label : i18n.get("createActivityMenuAddBefore"),
				//icon: "edit",
				onClick : function(data) {
					CreateActivityDialog.showDialog(data.activity.getId(), false);
				},
				data : data,
				help : i18n.get("createActivityMenuAddBeforeHelp")
			});
			items.push({
				label : i18n.get("createActivityMenuAddAfter"),
				//icon: "edit",
				onClick : function(data) {
					CreateActivityDialog.showDialog(data.activity.getId(), true);
				},
				data : data,
				help : i18n.get("createActivityMenuAddAfterHelp")
			});
			items.push({
				label : i18n.get("createActivityMenuAddPeerReview"),
				//icon: "edit",
				onClick : function(data) {
					if (data.activity.getInstancedActivities().length < 2 )
					{
						InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("noGroups"));
					}
					else{
						if (data.activity.getResources()==0 &&  data.activity.getTools()==0)
						{
							InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("noResources"));
						}
						else{
							PeerReviewActivityDialog.showDialog(data.activity.getId());
						}
					}
				},
				data : data,
				help : i18n.get("createActivityMenuAddPeerReviewHelp")
			});
			return items;
		},

}