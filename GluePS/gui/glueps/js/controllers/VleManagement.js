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

var VleManagement = {
		
	init: function(){
		
		dojo.connect(dojo.byId("manageVleButton"), "onclick", function(){
			VleManagement.openManageVlesDialog();
			VleManagement.displayLeUser();
	    });
		
		
		dojo.connect(dojo.byId("createVleInstallation"), "onclick", function(){			
			CreateLeDialog.showDialog();
	    });
		
		
		dojo.connect(dojo.byId("acceptManageVles"), "onclick", function(){
			VleManagement.closeManageVlesDialog();
	    });				
		
		dojo.connect(dojo.byId("cancelManageVles"), "onclick", function(){
			VleManagement.closeManageVlesDialog();
	    });
		
	},
		
	openManageVlesDialog: function(){
		dijit.byId("manageVles").show();
	},
	
	closeManageVlesDialog: function(){
		dijit.byId("manageVles").hide();
	},
	
    /**
	 * Actualización de la lista de LEs del usuario
	 */
	displayLeUser: function(){
		var baseUrl = window.location.href.split("/GLUEPSManager")[0];
		var xhrArgs = {
			url: baseUrl + "/GLUEPSManager/learningEnvironments",
	        load: function(data){
	        	VleManagement.showTableLeUser(data);
	        }
	    };
		dojo.xhrGet(xhrArgs);
	},
	    
	/**
	*  Mostrar la lista de instalaciones de LE en la tabla
	*/
	showTableLeUser: function(data){
	        var table = document.getElementById("tableVleInst");
	        var nodeTable = table.getElementsByTagName("table");
	        //Se eliminan los elementos de la tabla si existen
	        if (nodeTable.length!=0){
	            table.removeChild(nodeTable[0]);
	        }
	        var table1 = document.createElement("table");
	        table1.className="participantsTable";
	        var th = table1.appendChild(document.createElement("th"));
	        th.innerHTML = i18n.get("TableLeName");
	        th = table1.appendChild(document.createElement("th"));
	        th.innerHTML = i18n.get("TableLeUser");
	        th = table1.appendChild(document.createElement("th"));
	        th.innerHTML = i18n.get("TableLeType");
	        th = table1.appendChild(document.createElement("th"));
	        th.innerHTML = i18n.get("TableLeLocation");
	        
	    	var jsdom = dojox.xml.DomParser.parse(data);
	    	var entryList=jsdom.getElementsByTagName("entry");
	    	for(var i=0; i<entryList.length;i++){
	    		var tr = table1.appendChild(document.createElement("tr"));
	    		var td = tr.appendChild(document.createElement("td"));
	    		var td2 = tr.appendChild(document.createElement("td"));
	    		var td3 = tr.appendChild(document.createElement("td"));
	    		var td4 = tr.appendChild(document.createElement("td"));
	    		td.setAttribute("style", "min-width:160px;");
	    		td.innerHTML = entryList[i].getElementsByTagName("title")[0].childNodes[0].nodeValue;
	    		if (entryList[i].getElementsByTagName("glue:creduser")[0].childNodes.length > 0){
	    			td2.innerHTML = entryList[i].getElementsByTagName("glue:creduser")[0].childNodes[0].nodeValue;
	    		}else{
	    			td2.innerHTML = "";
	    		}
	    		td3.innerHTML = entryList[i].getElementsByTagName("glue:type")[0].childNodes[0].nodeValue;
	    		td4.innerHTML = entryList[i].getElementsByTagName("glue:vlelocation")[0].childNodes[0].nodeValue;
	            MenuManager.registerThing(td, {
	                getItems: function(data) {
	                    return LeUserMenu.getLeUserOptions(data);
	                },
	                data: {
	                    res: entryList[i]
	                }
	            });
		        MenuManager.registerThing(td2, {
		        	getItems: function(data) {
		        		return LeUserMenu.getLeUserOptions(data);
		            },
		            data: {
		            	res: entryList[i]
		            }
		       });
	    	}
	    	table.appendChild(table1);
	}

		
}