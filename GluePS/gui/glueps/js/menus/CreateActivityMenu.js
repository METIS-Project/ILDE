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