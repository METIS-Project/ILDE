/**
 * 
 */

var InformationPainter = {
		
	paint: function(){
		dojo.byId("designNameLabel").innerHTML=i18n.get("designNameLabel");
		dojo.byId("deploymentNameLabel").innerHTML=i18n.get("deploymentNameLabel");
		dojo.byId("learningEnvironmentNameLabel").innerHTML=i18n.get("learningEnvironmentNameLabel");
		dojo.byId("courseNameLabel").innerHTML=i18n.get("courseNameLabel");
		dojo.byId("designName").innerHTML=JsonDB.getDesignName();
		dojo.byId("deploymentName").innerHTML= JsonDB.getDeploymentName();
		dojo.byId("learningEnvironmentName").innerHTML= LearningEnvironment.getLearningEnvironmentName();
		var courseName = JsonDB.getCourseName();
		if (courseName!=false)
		{
			dojo.byId("courseName").innerHTML=JsonDB.getCourseName();
		}		
		InformationPainter.internationalize();
	},
	
	internationalize: function(){		
		dojo.byId("leyendTitle").innerHTML=i18n.get("leyendTitle");
		dojo.byId("activityLeyend").innerHTML=i18n.get("activityLeyend");
		dojo.byId("emptyGroupLeyend").innerHTML=i18n.get("emptyGroupLeyend");
		dojo.byId("notEmptyGroupLeyend").innerHTML=i18n.get("notEmptyGroupLeyend");
		dojo.byId("documentLeyend").innerHTML=i18n.get("documentLeyend");
		dojo.byId("toolLeyend").innerHTML=i18n.get("toolLeyend");
		dojo.byId("groupResourcesLeyend").innerHTML=i18n.get("groupResourcesLeyend");
		dojo.byId("configuredCreatedLeyend").innerHTML=i18n.get("configuredCreatedLeyend");
		dojo.byId("reuseCreatedLeyend").innerHTML=i18n.get("reuseCreatedLeyend");
		dojo.byId("reusedNotCreatedLeyend").innerHTML=i18n.get("reusedNotCreatedLeyend");
	}
}