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

var CreateLeDialog = {
		
	dialogId: "dialogNewLe",
	
	xmlData: "",
		
	init: function(){
		
		dojo.connect(dojo.byId("acceptNewLe"), "onclick", function(){
			CreateLeDialog.check();
		});
			
		dojo.connect(dojo.byId("cancelNewLe"), "onclick", function(){
			CreateLeDialog.hideDialog();
		});
		
		dojo.connect(dojo.byId("dialogNewLeInstallation"), "onchange", function(){
			CreateLeDialog.changeLeName();
			CreateLeDialog.showLeFields();
			//CreateLeDialog.oauthField();
	    });
		
	},
	
	
	showDialog: function(){
		this.resetElements();
		this.internationalize();
		this.getLEInstallations();
		var dlg = dijit.byId(this.dialogId);
		dlg.show();
	},
	
	hideDialog : function(){
		var dlg = dijit.byId(this.dialogId);
		dlg.hide();
	},	
	    
	
    /** 
	* Resetea los campos de la Ventana 'Nuevo LE'
	*/
	resetElements: function(){
		dojo.byId("dialogNewLeErrorReport").innerHTML = "";
		dijit.byId("dialogNewLeName").setValue("");
		dijit.byId("dialogNewLeUser").setValue("");
		dijit.byId("dialogNewLePassword").setValue("");
		
    	dojo.byId("trNewLeUser").style.display="";
    	dojo.byId("trNewLePassword").style.display="";
		/*dojo.style(dojo.byId("trOauthLinkVle"), {
			display : "none"
		});*/
	},
	
	internationalize: function(){
		dijit.byId("dialogNewLe").titleNode.innerHTML = i18n.get("MenuEditLe");
		dojo.byId("labelNewLeInstallation").innerHTML = i18n.get("DialogCreateLeInstallation");
		dojo.byId("labelNewLeName").innerHTML = i18n.get("DialogCreateLeName");
		dojo.byId("labelNewLeUser").innerHTML = i18n.get("DialogCreateLeUser");
		dojo.byId("labelNewLePassword").innerHTML = i18n.get("DialogCreateLePassword");
	},	
	
    /**
	* Actualiza el listado de instalaciones de LE disponibles en la creación de un nuevo LE
    */
	getLEInstallations: function(){
		var baseUrl = window.location.href.split("/GLUEPSManager")[0];
		var xhrArgs = {
			url: baseUrl + "/GLUEPSManager/learningEnvironmentInstallations",
			handleAs: "text",
			load: function(data){
				CreateLeDialog.xmlData = data;
				CreateLeDialog.updateSelectLe(CreateLeDialog.xmlData);
	    	},
			error : function(error, ioargs) {
				CreateLeDialog.xmlData = "";
	        	var message = "";
	        	message=ErrorCodes.errores(ioargs.xhr.status);
	        	Glueps.showAlertDialog(i18n.get("warning"), message);
			}
	    };
	    dojo.xhrGet(xhrArgs);
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
    	var vleSelect=dojo.byId("dialogNewLeInstallation");    	
    	//Elimino options del select
       	while (vleSelect.options.length != 0)
    	{
    		vleSelect.removeChild(vleSelect.options[0]);    	
       	}
       	//Añadir opción por defecto
		var option =document.createElement("option");
		option.innerHTML=i18n.get("ImportDeployLabelSelectVleDefault");//"Selecciona una instalación de LE";
		option.value="0";
		option.setAttribute("selected","true");
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
     * Función que comprueba si se han completado los campos necesarios para crear un LE
     */
    check: function(){
    	
		var name = dijit.byId("dialogNewLeName");
		var user = dijit.byId("dialogNewLeUser");
		var password = dijit.byId("dialogNewLePassword");
		var leSelect=dojo.byId("dialogNewLeInstallation");
		
		var vleSelect = dojo.byId("dialogNewLeInstallation"); 
		var index = vleSelect.selectedIndex;
		var leId = vleSelect.options[index].value;
		var leType = CreateLeDialog.getLearningEnvironmentType(leId );
		
		if (leType!="Blogger" && (name.value.length == 0 || user.value.length == 0 || password.value.length == 0 || leSelect.options[leSelect.selectedIndex].value=="0"))
		{
			dojo.byId("dialogNewLeErrorReport").innerHTML = i18n.get("ErrorRellenarCampos");
		}
		else if (leType=="Blogger" && (name.value.length == 0 || leSelect.options[leSelect.selectedIndex].value=="0")){
			dojo.byId("dialogNewLeErrorReport").innerHTML = i18n.get("ErrorRellenarCampos");
		}else{
			CreateLeDialog.callCreateLe(name.value, leSelect.options[leSelect.selectedIndex].value, user.value, password.value);
		}
    },
    
    /**
     * Función que realiza el POST de creación de un LE
     */
    callCreateLe: function (name, leInst, user, password){
    	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
        var bindArgs = {
            url: baseUrl + "/GLUEPSManager/learningEnvironments",
            handleAs: "text",
            content: {
            	leName: name,
            	leInstallation: leInst,
            	leUser: user,
                lePassword: password
            },
            load: function(data){
            	//Cerrar el dialog
            	CreateLeDialog.resetElements();
            	CreateLeDialog.hideDialog();
            	//Se actualiza la pantalla de los LE
            	VleManagement.displayLeUser();
            	//Se actualiza los LE de subir despliegue
            	ImportDeploy.getLEnvironments();
            	ImportDeploy.resetSelectClassVle();
            },
            error: function(error, ioargs){
				var message = "";
				Glueps.showAlertDialog(i18n.get("warning"), message);
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    changeLeName: function(){
		var vleSelect=dojo.byId("dialogNewLeInstallation"); 
		var index = vleSelect.selectedIndex;
		if (index > 0)
		{
			dijit.byId("dialogNewLeName").setValue(vleSelect.options[index].text);
		}
    },
    
    /*oauthField: function(){
		var vleSelect = dojo.byId("dialogNewLeInstallation"); 
		var index = vleSelect.selectedIndex;
		var leId = vleSelect.options[index].value;
    	var leType = CreateLeDialog.getLearningEnvironmentType(leId);
    	if (leType == "Blogger" && OauthManager.oauthRequired(leId)){
    		var callerMethod = "newle";
    		//start the oauth process in order to have an updated access token
    		//we provide a name for the callerMethod and the id of the selected vle
    		OauthManager.startOauth("callerMethod=" + callerMethod);
    	}
    },*/
    
    showLeFields:function(){
		var vleSelect = dojo.byId("dialogNewLeInstallation"); 
		var index = vleSelect.selectedIndex;
		var leId = vleSelect.options[index].value;
		var leType = CreateLeDialog.getLearningEnvironmentType(leId );
		if (leType!="Blogger"){
	    	dojo.byId("trNewLeUser").style.display="";
	    	dojo.byId("trNewLePassword").style.display="";
		}else{
	    	dojo.byId("trNewLeUser").style.display="none";
	    	dojo.byId("trNewLePassword").style.display="none";
		}
    },
    
    getLearningEnvironmentType: function(leId){
    	var leType ="";
      	if (CreateLeDialog.xmlData!=""){
        	var jsdom = dojox.xml.DomParser.parse(CreateLeDialog.xmlData);
        	var entryList=jsdom.getElementsByTagName("entry");
        	for(var i=0; i<entryList.length;i++){
        		var id = entryList[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
        		if (leId == id){
        			leType = entryList[i].getElementsByTagName("glue:type")[0].childNodes[0].nodeValue;
        			break;
        		}
        	}     		
    	}
      	return leType;
      }

		
}