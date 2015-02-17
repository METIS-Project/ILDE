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

var i18n = {
		
		init: function(){
			dojo.connect(dojo.byId("localeSpanish"), "onclick", function() {
				djConfig.locale = "es";
				DeploymentPainter.paint();
			});

			dojo.connect(dojo.byId("localeEnglish"), "onclick", function() {
				djConfig.locale = "en";
				DeploymentPainter.paint();
			});
			
			dojo.connect(dojo.byId("localeCatalan"), "onclick", function() {
				djConfig.locale = "ca";
				DeploymentPainter.paint();
			});
			
			//if (LdShakeManager.ldShakeMode){
				var lang = gup("lang");
				if (lang != "" && (lang=="es" || lang=="en" || lang=="ca")){
					djConfig.locale = lang;
				} 
			//}
		},
		
		i18nStrings: new Object(),
		
		get: function(key){
			return i18n.i18nStrings[djConfig.locale][key];
		}

};






