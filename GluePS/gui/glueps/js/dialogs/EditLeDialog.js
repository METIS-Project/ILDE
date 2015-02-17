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
 *  Diálogo de edición de un LE 
 */

var EditLeDialog = {
		
		dialogId: "dialogEditLe",
		
		/**
		 * Identificador del LE que se edita
		 */
		leId: null,
		
		init: function(){
			
			dojo.connect(dojo.byId("acceptEditLe"), "onclick", function(){
				EditLeDialog.check();
			});
				
			dojo.connect(dojo.byId("cancelEditLe"), "onclick", function(){
				EditLeDialog.hideDialog();
			});
			
		},
		
		showDialog: function(leId){
			
			this.leId = leId;
			this.internationalize();
			this.getLEInstallations();
			//Esperamos a que esté todo rellenado antes de mostrar el dialog
			//var dlg = dijit.byId(this.dialogId);
			//dlg.show();
		},
		
		hideDialog: function(){
			this.leId = null;
			var dlg = dijit.byId(this.dialogId);
			dlg.hide();
		},
		
	    showLeFields:function(dataLe){
			var leType = dataLe.type;
			if (leType!="Blogger"){
		    	dojo.byId("trEditLeUser").style.display="";
		    	dojo.byId("trEditLePassword").style.display="";
			}else{
		    	dojo.byId("trEditLeUser").style.display="none";
		    	dojo.byId("trEditLePassword").style.display="none";
			}
	    },
		
	    /**
		* Actualiza el listado de instalaciones de LE disponibles en la edición de un LE
	    */
		getLEInstallations: function(){
			var baseUrl = window.location.href.split("/GLUEPSManager")[0];
			var xhrArgs = {
				url: baseUrl + "/GLUEPSManager/learningEnvironmentInstallations",
				handleAs: "text",
				load: function(dataInstallations){
					EditLeDialog.updateSelectLe(dataInstallations);
					EditLeDialog.getLearningEnvironment(dataInstallations);
		    	},
				error : function(error, ioargs) {
		        	var message = "";
		        	message=ErrorCodes.errores(ioargs.xhr.status);
		        	Glueps.showAlertDialog(i18n.get("warning"), message);
				}
		    };
		    dojo.xhrGet(xhrArgs);
		},
		
		/** Obtiene la información completa del LE que se va a editar*/
	    getLearningEnvironment:function(dataInstallations) {
	        var xhrArgs = {
	        	url: EditLeDialog.leId,
				handleAs : "json", // Manejamos la respuesta del get como un json
				headers : { // Indicar en la cabecera que es json
					"Content-Type" : "application/json",
					"Accept" : "application/json"
				},
	            load: function(dataLe) {    
	            	EditLeDialog.resetElements(dataLe, dataInstallations);
	            	EditLeDialog.showLeFields(dataLe);
	            	dijit.byId(EditLeDialog.dialogId).show();
	            },
	            error: function(error, ioargs) {  
	         	   var message = "";
	         	   message=ErrorCodes.errores(ioargs.xhr.status);
	         	  Glueps.showAlertDialog(i18n.get("warning"), message);
	            }
	        }
	        //Call the asynchronous xhrGet
	        var deferred = dojo.xhrGet(xhrArgs);
	    },
		    
	    /**
	     * Añade las opciones al select del LE
	     * @param data XML con la información de los LE
	     */
		updateSelectLe: function(data){
	    	//Para almacenar el contenido parseado del xml recibido del get
	    	var jsdom = dojox.xml.DomParser.parse(data);
	    	//
	    	var entryList=jsdom.getElementsByTagName("entry");
	    	var vleSelect=dojo.byId("dialogEditLeInstallation");    	
	    	//Elimino options del select
	       	while (vleSelect.options.length != 0)
	    	{
	    		vleSelect.removeChild(vleSelect.options[0]);    	
	       	}
	       	//Añadir opción por defecto
			var option =document.createElement("option");
			option.innerHTML="Selecciona una instalación de LE";
			option.value="0";
			//option.setAttribute("selected","true");
			vleSelect.appendChild(option); 
	    	//Añado restantes opciones
	    	for(var i=0; i<entryList.length;i++){
	    		var title=entryList[i].getElementsByTagName("title")[0].childNodes[0].nodeValue;
	    		var option =document.createElement("option");
	    		option.innerHTML=title;
	    		option.value=entryList[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
	    		vleSelect.appendChild(option); 
	    	}
	    	
	    },
		
	    /**
	     * Función que comprueba si se han completado los campos necesarios para editar un LE
	     */
	    check: function(){
	    	
			var name = dijit.byId("dialogEditLeName");
			var user = dijit.byId("dialogEditLeUser");
			var password = dijit.byId("dialogEditLePassword");
			var leSelect=dojo.byId("dialogEditLeInstallation");
			if (name.value.length == 0 || user.value.length == 0 || password.value.length == 0 || leSelect.options[leSelect.selectedIndex].value=="0")
			{
				dojo.byId("dialogEditLeErrorReport").innerHTML = i18n.get("ErrorRellenarCampos");
			}
			else{
				EditLeDialog.callEditLe(name.value, leSelect.options[leSelect.selectedIndex].value, user.value, password.value);
			}
	    },
	    
	    /**
		 *  Realiza un PUT para actualizar el contenido del documento de despliegue
		 */
	    callEditLe : function(name, leInst, user, password) {

	        var xhrArgs = {
	            url : EditLeDialog.leId,
	            method : "PUT",
	            handleAs : "text",
	            headers : {
	                "Content-Type" : "text/xml"
	            },
	            content: {
	            	leName: name,
	            	leInstallation: leInst,
	            	leUser: user,
	                lePassword: password
	            },
	            load : function(data) {
	            	EditLeDialog.hideDialog();
	            	//Se actualiza la pantalla de los LE
	            	VleManagement.displayLeUser();
	            	//Se actualiza los LE de subir despliegue
	            	ImportDeploy.getLEnvironments();
	            	ImportDeploy.resetSelectClassVle();
	            },
	            error : function(error, ioargs) {
					var message = "";
					Glueps.showAlertDialog(i18n.get("warning"), message);
	            }
	        }
	        var deferred = dojo.xhrPut(xhrArgs);
	    },
	    
	    /**
	     * Establece los valores del formulario de forma acorde al LE que se edita
	     */
		resetElements: function(dataLe, dataInstallations){
			var name = dataLe.name;
			var creduser = dataLe.creduser;
			var credsecret = dataLe.credsecret;
			var accessLocation = dataLe.accessLocation;
			dojo.byId("dialogEditLeErrorReport").innerHTML = "";
			dijit.byId("dialogEditLeName").setValue(name);
			dijit.byId("dialogEditLeUser").setValue(creduser);
			dijit.byId("dialogEditLePassword").setValue(credsecret);
			
			var vleSelect=dojo.byId("dialogEditLeInstallation"); 
	    	var jsdom = dojox.xml.DomParser.parse(dataInstallations);
	    	var entryList=jsdom.getElementsByTagName("entry");
	    	for(var i=0; i<entryList.length;i++){
	    		var accessLocationInst=entryList[i].getElementsByTagName("glue:accessLocation")[0].childNodes[0].nodeValue;
	    		if (accessLocationInst == accessLocation)
	    		{
	    			var idInst = entryList[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
	    			vleSelect.selectedIndex = (i+1);
	    			break;
	    		}
	    	}
		},
		
		internationalize: function(){
			dijit.byId("dialogEditLe").titleNode.innerHTML = i18n.get("MenuEditLe");
			dojo.byId("labelEditLeInstallation").innerHTML = i18n.get("DialogCreateLeInstallation");
			dojo.byId("labelEditLeName").innerHTML = i18n.get("DialogCreateLeName");
			dojo.byId("labelEditLeUser").innerHTML = i18n.get("DialogCreateLeUser");
			dojo.byId("labelEditLePassword").innerHTML = i18n.get("DialogCreateLePassword");
		}	
}