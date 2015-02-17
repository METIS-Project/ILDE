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

var DeployButtomPainter = {

		init: function(){
	        var checkCourse = dojo.byId("checkAddCourse");
	        var themeSelect = dojo.byId("themeSelect");
	        dojo.connect(checkCourse, "onchange", function(){
	        	if (checkCourse.checked)
	        	{
		        	var ss = JsonDB.getStartingSection();
		        	if (ss)
		        	{
			        	JsonDB.setStartingSection(ss);
		        	}
		        	else{
		        		//Por defecto, desde la 1
			        	JsonDB.setStartingSection("1");
		        	}
	        	}
	        	else{
	        		JsonDB.unsetStartingSection();
	        	}
	        	JsonDB.notifyChanges();
	        });
	        
	        dojo.connect(themeSelect, "onchange", function(){
	            var number = themeSelect.options[themeSelect.selectedIndex].value;
	        	JsonDB.setStartingSection(number);
	        	JsonDB.notifyChanges();
	        });		
		},
		
		paint: function(){
			
			dojo.style(dojo.byId("divDeploymentButtomTop"), {
				display : ""
			});
			dojo.style(dojo.byId("divDeploymentButtonBottom"), {
				display : ""
			});
			
			this.internationalize();
			if (this.undefinedTools() || this.webcontentLocationNull() || this.noActivitiesToDeploy() || this.repeatedGroups())
			{
				dijit.byId("deploymentButtonTop").setAttribute('disabled', true);
				dijit.byId("deploymentButtonBottom").setAttribute('disabled', true);
				var exclamation = dojo.byId("exclamationDeploymentButtonTop");
				exclamation.style.display="";
				var exclamationBottom = dojo.byId("exclamationDeploymentButtonBottom");
				exclamationBottom.style.display="";
				
				var tooltip = new dijit.Tooltip({
					connectId:  ["exclamationDeploymentButtonTop"],
					label: i18n.get("deploymentInfo")
				});
				
				var tooltip = new dijit.Tooltip({
					connectId:  ["exclamationDeploymentButtonBottom"],
					label: i18n.get("deploymentInfo")
				});
				
			}
			else{
				dijit.byId("deploymentButtonTop").setAttribute('disabled', false);
				dijit.byId("deploymentButtonBottom").setAttribute('disabled', false);
				dojo.byId("exclamationDeploymentButtonTop").style.display="none";
				dojo.byId("exclamationDeploymentButtonBottom").style.display="none";
			}
			this.showDeployLinks();
			this.addtemas(20);
			this.addOauth();
		},
		
		internationalize: function(){
			dijit.byId("deploymentButtonTop").setAttribute('label', i18n.get("deployButtom"));
			dijit.byId("deploymentButtonBottom").setAttribute('label', i18n.get("deployButtom"));
		},
		
		/**
		 * Devuelve true si no hay actividades finales marcadas para ser desplegadas
		 */
		noActivitiesToDeploy: function(){
			var finalActivities = ActivityContainer.getFinalActivities();
			for (var i = 0; i < finalActivities.length; i++)
			{
				if (finalActivities[i].getToDeploy())
				{
					return false;
				}
			}
			return true;
		},

		undefinedTools: function(){
			var tools = ToolContainer.getAllTools();
			for (var i=0; i < tools.length; i++)
			{
				if (tools[i].getToolVle()==false)
				{
					return true;
				}
			}
			return false;
		},
		
		repeatedGroups: function(){
			var groups = GroupContainer.getAllGroups();
			for (var i=0; i < groups.length; i++){
				for (var j = 0; j < i; j++){
					if ( groups[i].getName().toLowerCase()==groups[j].getName().toLowerCase()){
						return true;
					}
				}
			}
			return false;
			
		},
		
		webcontentLocationNull: function(){
			var toolInstances = ToolInstanceContainer.getAllToolInstances();
			for (var i=0; i < toolInstances.length; i++)
			{
				//JUAN: Añado los otros tipos de herramienta que deben estar creadas para poder desplegar
				if (toolInstances[i].getTool().getToolVle().value=="Web Content"
						|| toolInstances[i].getTool().getToolVle().value=="AR Image"
						|| toolInstances[i].getTool().getToolVle().value=="3D Model"
						|| toolInstances[i].getTool().getToolVle().value=="Google Forms (validation code)"
						|| toolInstances[i].getTool().getToolVle().value=="Picasa")
				{
					if (toolInstances[i].getLocation()==false)
					{
						return true;
					}
				}
			}
			return false;
		},
		
		/* Si es un despliegue ya completado se muestran los enlaces del despliegue est�tico o din�mico seg�n corresponda
		 * 
		 */
		showDeployLinks: function(){
			if (JsonDB.deploy.staticDeployURL) {
				// Muestro enlace
				dojo.style(dojo.byId("staticDeployLinkTop"), {
					display : ""
				});
				dojo.style(dojo.byId("staticDeployLinkBottom"), {
					display : ""
				});
				// Pongo direcci�n al enlace para el zip del deploy
				var staticDeployURL = JsonDB.deploy.staticDeployURL;
				if (LdShakeManager.ldShakeMode){
					var staticDeployURL = LdShakeManager.buildLdshakeUrl(staticDeployURL);
				}
				var nodoEnlace = dojo.byId("staticDeployLinkTop");
				nodoEnlace.setAttribute("href", staticDeployURL);
				nodoEnlace.childNodes[0].setAttribute("title", i18n.get("getDeployToolTip"));
				var nodoEnlaceBottom = dojo.byId("staticDeployLinkBottom");
				nodoEnlaceBottom.setAttribute("href", staticDeployURL);
				nodoEnlaceBottom.childNodes[0].setAttribute("title", i18n.get("getDeployToolTip"));
			}
			else{
				dojo.style(dojo.byId("staticDeployLinkTop"), {
					display : "none"
				});
				dojo.style(dojo.byId("staticDeployLinkBottom"), {
					display : "none"
				});
			}
			if (JsonDB.deploy.liveDeployURL) {
				// Muestro enlace
				dojo.style(dojo.byId("liveDeployLinkTop"), {
					display : ""
				});
				dojo.style(dojo.byId("liveDeployLinkBottom"), {
					display : ""
				});
				// Pongo direcci�n al enlace para la url del deploy
				var nodoEnlace = dojo.byId("liveDeployLinkTop");
				nodoEnlace.setAttribute("href", JsonDB.deploy.liveDeployURL);
				nodoEnlace.childNodes[0].setAttribute("title", i18n.get("LiveDeployVleHere"));
				var nodoEnlaceBottom = dojo.byId("liveDeployLinkBottom");
				nodoEnlaceBottom.setAttribute("href", JsonDB.deploy.liveDeployURL);
				nodoEnlaceBottom.childNodes[0].setAttribute("title", i18n.get("LiveDeployVleHere"));
			}
			else{
				// oculto el enlace
				dojo.style(dojo.byId("liveDeployLinkTop"), {
					display : "none"
				});
				dojo.style(dojo.byId("liveDeployLinkBottom"), {
					display : "none"
				});
			}
			
			//JUAN: Añadido para mostrar web con lista de marcadores
			
			toolInstancesWithMarkers = ToolInstanceContainer.getToolInstancesWithMarkers();
			if (toolInstancesWithMarkers != null){
				
				dojo.style(dojo.byId("markerListLinkTop"), {
					display : ""
				});
				dojo.style(dojo.byId("markerListLinkBottom"), {
					display : ""
				});
				// Pongo direcci�n al enlace
				var nodoEnlace = dojo.byId("markerListLinkTop");
				nodoEnlace.setAttribute("href", "javascript:DeployButtomPainter.openMarkerList();");
				nodoEnlace.childNodes[0].setAttribute("title", i18n.get("getListOfMarkers"));
				var nodoEnlaceBottom = dojo.byId("markerListLinkBottom");
				nodoEnlaceBottom.setAttribute("href", "javascript:DeployButtomPainter.openMarkerList();");
				nodoEnlaceBottom.childNodes[0].setAttribute("title", i18n.get("getListOfMarkers"));

				
				
			} else{
				dojo.style(dojo.byId("markerListLinkTop"), {
					display : "none"
				});
				dojo.style(dojo.byId("markerListLinkBottom"), {
					display : "none"
				});
			}
			
			
			
		},
		
	    addtemas : function(num) {
	    	dojo.byId("labelCheckAddCourse").innerHTML = i18n.get("NewDeployLabelCheckBoxAddInCourse");
	        var checkCourse = dijit.byId("checkAddCourse");
	        var themeSelect = dojo.byId("themeSelect");
	        var ss = JsonDB.getStartingSection();
	        if (ss==false)
	        {
	        	checkCourse.attr("checked", false);
	        	dojo.byId("themeSelect").disabled="disabled";	
	        }
	        else{
	        	checkCourse.attr("checked", true);
	        	dojo.byId("themeSelect").disabled="";
	        }
	        
	        // Elimino options del select
	        while (themeSelect.options.length != 0) {
	        	themeSelect.removeChild(themeSelect.options[0]);
	        }
	        // A�ado options al select
	        for ( var i = 1; i <= num; i++) {
	            var option = document.createElement("option");
	            option.innerHTML = i;
	            option.value = i;
	            if (ss == option.value) {
	                option.setAttribute("selected", "true");
	            }
	            themeSelect.appendChild(option);
	        }
	    },
	    
	    /**
	     * If it is necessary, it adds the oauth link to the page
	     */
	    addOauth: function(){
	    	/*if (LearningEnvironment.getLearningEnvironmentType()=="Blogger" && document.getElementById("oauthLink")==null){
		    	var buttonTop = dojo.byId("divDeploymentButtomTop");
		    	var a = document.createElement("a");
		    	a.setAttribute("id", "oauthLink");
		    	a.setAttribute("href","javascript:OauthManager.getOauthUrl();");
		    	a.setAttribute("target", "_blank");
		    	a.innerHTML = "Oauth autentication";
		    	buttonTop.appendChild(a);
	    	}*/
	    },
	    
		//JUAN: Añadido para mostrar web con lista de marcadores
		openMarkerList: function () {
			 newwindow=window.open();
			 newdocument=newwindow.document;
			 newdocument.write("<link rel=\"stylesheet\" href=\"css/markerlist.css\" />");
		     newdocument.write("<script type=\"text/javascript\" src=\"js/painters/DeployButtomPainter.js\"></script>");
		     newdocument.write("<h1>" + i18n.get("ListofMarkersTitle") + "</h1>");
		     newdocument.write("<h2>" + JsonDB.deploy.name + "</h2>");
	    	 newdocument.write("<TITLE>" + i18n.get("ListofMarkersTitle") + "</TITLE><STYLE type=\"text/css\">  H1 { text-align: center}	H2 { text-align: center} </STYLE><body>");

	    	 //QR code to course in VLE
	    	 newdocument.write("<div id=\"markerlist\" style=\"text-align:center;width:300px\" >");

	    	 var liveDeployURL = JsonDB.deploy.liveDeployURL;
	    	 if (liveDeployURL != null && liveDeployURL !== ""){
	    		 var qrliveDeployURL = "http://qrickit.com/api/qr?qrsize=200&d=" + liveDeployURL; 		 	
	//		     newdocument.write("<div style=\"text-align:center;\">");
				 newdocument.write("<b>" + i18n.get("AccessToCourseInVLE") + "</b><br>");
				 newdocument.write("<br><img src=\"" + qrliveDeployURL + "\" width=\"256\" height=\"258\"></img><br><br><br>");
				 
				 //If VLE is a wiki, QR code to students view of the course in VLE
				 var leType = JsonDB.deploy.learningEnvironment.type;
				 if (leType == "MediaWiki"){
					 var qrliveDeployURLStudents = qrliveDeployURL.replace("%3APrincipal","%3AAlumnos");
	//			     newdocument.write("<div style=\"text-align:center;\">");
					 newdocument.write("<b>" + i18n.get("AccessToCourseInVLEStudentView") + "</b><br>");
					 newdocument.write("<br><img src=\"" + qrliveDeployURLStudents + "\" width=\"256\" height=\"258\"></img><br><br><br>");
				 }

	    	 }
		     var activityList = ActivityContainer.getFinalActivities();
		     for ( var i = 0; i < activityList.length; i++) {
		     	var activity = activityList[i];
		     	var activityName = activity.getName();
		     	var instancesActivities = activity.getInstancedActivities();
		     	
		     	if (instancesActivities){
		     		for ( var j = 0; j < instancesActivities.length; j++) {
		     			var instancedActivity = instancesActivities[j];
		     			var toolinstances = instancedActivity.getToolInstances();
		     			if (toolinstances) {
		     				for ( var k = 0; k < toolinstances.length; k++) {
		     					var toolinstance = toolinstances[k];
		     					if (toolinstance.hasValidQrCodePosition() || toolinstance.getValidJunaioMarkerPosition() != null){
		     				    	 var position = toolinstance.getPosition();
		     	//			    	 newdocument.write("<div style=\"text-align:center;\">");
		     				    	 newdocument.write("<b>" + activityName + "</b><br>");
		     				    	 newdocument.write("<i>" + toolinstance.getName() + "</i>");
		     					     newdocument.write("<br><img src=\"" + position + "\" width=\"256\" height=\"258\"></img><br><br><br>");

		     					}
		     				}
		     			}
		     		}
		     	}

		     }
		     newdocument.write("</div></body>");	     
		     newdocument.close();
		     
		     
		     
		}
}
