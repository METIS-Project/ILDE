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