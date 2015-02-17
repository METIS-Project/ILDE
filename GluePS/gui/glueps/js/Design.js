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

var Design = {
		
	designList : "",

	init : function() {
		// Asignar Funcionalidad a botón de crear diseño
		dojo.connect(dijit.byId("newDButtonCreateDesign"), "onClick", Design, "postDesings");
		// Asignar Funcionalidad a botón de importar despligue
		dojo.connect(dijit.byId("importDeployButton"), "onClick", Design, "postDeploys");
	},
	
	showLoginIcon: function(){
		dojo.style(dojo.byId("loginImage"), {
			display : ""
		});
	},
	
	hideLoginIcon: function(){
		dojo.style(dojo.byId("loginImage"), {
			display : "none"
		});
	},
	
	showLogoutIcon: function(){
		dojo.style(dojo.byId("exitImage"), {
			display : ""
		});
	},
	
	hideLogoutIcon: function(){
		dojo.style(dojo.byId("exitImage"), {
			display : "none"
		});
	},
	
	/**
	 * Muestra la parte de creación de diseño y listado de diseñs
	 */
	mostrarPanelDesign : function() {
		dojo.style(dojo.byId("newDesign"), {
			display : ""
		});
		dojo.style(dojo.byId("importDeploy"), {
			display : ""
		});
		dojo.style(dojo.byId("myListDesign"), {
			display : ""
		});
	},
		
	/**
	 * Oculta la parte de creación de diseño, importación de despliegue y listado de diseños
	 */
	ocultarDesign : function() {
		dojo.style(dojo.byId("newDesign"), {
			display : "none"
		});
		dojo.style(dojo.byId("importDeploy"), {
			display : "none"
		});
		dojo.style(dojo.byId("myListDesign"), {
			display : "none"
		});
	},
	
	/**
	 *  Muestra los paneles relativos a la subida de diseños y mostrar diseños y despliegues
	 */
	showDesign : function() {
		// Ocultar el resto de paneles que no pertenecen al diseño
		Deploy.ocultarPanelNewDeploy();
		Design.showLoginIcon();
		Design.hideLogoutIcon();
		// Mostrar la pantalla de diseños
		Design.mostrarPanelDesign();
        // Hacemos get para obtener los vle en el formulario de subir despliegues
        ImportDeploy.getLEnvironments();
		// Rellenar el panel con el listado de diseños
		Design.getJsonDesigns();
	},
	
	/**
	 * Construye el listado de diseños
	 * @param data JSON con la información de los diseños
	 */
	construirListadoDisenos : function(data) {
		// Nodo ul, donde voy a insertar la lista de disenos
		var nodoUl;
		// Nodo div que contiene el nodo ul con la lista de disenos.
		var nodoDiv = document.getElementById("divUlDesign");
		// Guarda el titulo del diseno
		var design;

		// Listado de los tag entrys
		// Cada tag entry representa un diseno
		var entryList = data;

		// Comienzo a crear la lista de disenos
		// Primer paso: Eliminar lista anteriores, si las hubiera
		// Obtengo el tag ul
		nodoUl = nodoDiv.getElementsByTagName("ul");
		if (nodoUl.length != 0) {
			nodoDiv.removeChild(nodoUl[0]);
		}
		// La vuelvo a crear
		nodoUl = document.createElement("ul");
		nodoUl.setAttribute("id", "myListDesignUl");
		dojo.style(nodoUl, {display : "none"});
		// La incluyo en el div correspondiente.
		nodoDiv.appendChild(nodoUl);

		// Ordeno los diseños antes de crear el listado
		entryList.sort(natcompareName);
		var tabla=document.createElement("table");
		tabla.setAttribute("class", "tableDesigns");
		// Recorro los diseños
		for ( var i = 0; i < entryList.length; i++) {
			if (entryList[i].name) {
				design = entryList[i].name;
			} else {
				// Para controlar que en el diseno no exista el titulo y de
				// error
				design = i18n.get("generalDiseno") + " " + i;
			}

			var div = document.createElement("div");
			// Anado el id del diseño
			var contenidoLink = entryList[i].id;
			var id = Design.obtenerId(contenidoLink);
			var tr=tabla.appendChild(document.createElement("tr"));
			
			// Pongo nombre del diseño
			var td1=document.createElement("td");
			var span1=document.createElement("span");
			span1.setAttribute("id","span-li-"+id+"-ul");
			span1.setAttribute("style","padding-left:1.5em;");
            
			td1.appendChild(span1);
			var nodoUl3=document.createElement("ul");
			nodoUl3.setAttribute("id","li-"+id+"-ul");	
			td1.appendChild(nodoUl3);
			//Creo la tabla en la que aparecerán los despliegues de ese diseño
			var tablaDespliegues=document.createElement("table");
			tablaDespliegues.setAttribute("class", "tableInfo");
			nodoUl3.appendChild(tablaDespliegues);
			tr.appendChild(td1);

			span1.innerHTML = design;
			
			//Asociamos el menú al diseño
            MenuManager.registerThing(span1, {
                getItems: function(data) {
                    return DesignMenu.getDesignMenu(data);
                },
                data: {
					id: id,
					design: design
                },
                menuStyle: "default"
            });

			nodoUl.appendChild(tabla);
		}
		// Guardo la lista de los diseños
		Design.designList = entryList;		

	},

	/**
	 * Obtiene el identificador numérico del diseño
	 * @param link Url de la que se extrae el identificador numérico
	 * @returns id numérico del diseño
	 */
	obtenerId : function(link) {
		var search = 'designs/';
		return link.substring(link.lastIndexOf(search) + search.length);
	},
	
	/**
	 * Obtiene la información de los diseños y sus despliegues y contruye la lista de estos
	 * @param recentlyCreatedId Identificador del diseño que se acaba de crear (si ha sido creado)
	 */
	getJsonDesigns : function(recentlyCreatedId) {
		var baseUrl = window.location.href.split("/GLUEPSManager")[0];
		// Mostramos el gif animado de cargando	
		//Glueps.showLoadingDialog(i18n.get("loadingDesignList"));

		// The parameters to pass to xhrGet, the url, how to handle it, and the
		// callbacks.
		var xhrArgs = {
			url: baseUrl + "/GLUEPSManager/designs",
			handleAs : "json",// Tipo de dato de la respuesta del Get,
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			},  
			preventCache: false,
			load : function(data) {
				Glueps.showLoadingDialog(i18n.get("loadingDesignList"));
				// Tratamiento de la respuesta del get
				// Construimos la lista de los disenos
				Design.construirListadoDisenos(data);
				// Rellenamos los disenos con los despliegues que existen en la base de datos
				Deploy.addJsonListDeploys(recentlyCreatedId);
				Design.showLogoutIcon();
				Design.hideLoginIcon();
			},
			error : function(error, ioargs) {
				var message = "";
				message = ErrorCodes.errores(ioargs.xhr.status);
				Glueps.showAlertDialog(i18n.get("warning"), message);
				// cuando termine de cargar los despliegues de cada diseño
				dijit.byId("loading").hide();
				Design.showLoginIcon();
				Design.hideLogoutIcon();
			}
		};

		// Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
	},
	
	/**
	 * Post del formulario de creación de un diseño
	 */
	postDesings : function() {
		// Titulo del diseño NewDTextBox
		var designTitle = document.getElementById("NewDTextBox").value;
		// Archivo del diseño NewDImportDesign
		var designArchive = document.getElementById("NewDImportDesign").value;
		var errorForm = false;
		if (designTitle == "" || designArchive == "") {
			errorForm = true;
		}
		if (!errorForm) {
			var baseUrl = window.location.href.split("/GLUEPSManager")[0];
			var post = dojo.io.iframe
					.send({
						url : baseUrl + "/GLUEPSManager/designs",
						method : "post",
						form : "myform",
						handleAs : "xml",// Tipo de dato de la respuesta del Get,
						load : function(data) {
							recentlyCreatedId = Design.getDesignId(data);					
							// Obtener todos los disenos y generar el listado
							Design.getJsonDesigns(recentlyCreatedId);
							Design.resetFormDesign();
						},
						error : function(error, ioargs) {
							var message = "";
							var codigo = 1;
							message = ErrorCodes.errores(codigo);
							Glueps.showAlertDialog(i18n.get("warning"), message);
						}
					});
		} else {
			// Para el caso en que falte un campo por rellenar
			Glueps.showAlertDialog("warning", i18n.get("ErrorRellenarCampos"));
		}
	},
	
	/**
	 * Post del formulario de subir (importar) un despliegue
	 */
	postDeploys : function() {
		// Titulo del despliegue importDeployTextBox
		var deployTitle = document.getElementById("importDeployTextBox").value;
		// Archivo del diseño NewDImportDesign
		var deployArchive = document.getElementById("deployImport").value;
		var selectedVle = document.getElementById("importVleSelect");
		var selectedCourse = document.getElementById("importCourseSelect");
		var errorForm = false;
		if ((deployTitle == "") || (deployArchive == "") || selectedVle.options[selectedVle.selectedIndex].value=="0" || selectedCourse.options[selectedCourse.selectedIndex].value=="0"){
			errorForm = true;
		}
		if (!errorForm) {
			var baseUrl = window.location.href.split("/GLUEPSManager")[0];
			dojo.byId("importDeployForm").action = baseUrl + "/GLUEPSManager/deploys";
			Glueps.showLoadingDialog(i18n.get("creatingDeployment"));
			var post = dojo.io.iframe
					.send({
						method : "POST",
						form : dojo.byId("importDeployForm"),
						handleAs : "html",
						load : function(data) {
							var deployUrl = Deploy.getDeployId(data);
							Design.deployImportInprocess(deployUrl);
						},
						error : function(error, ioargs) {
							Glueps.hideLoadingDialog();
		                    var codigo = 2;
		                    var message = ErrorCodes.errores(codigo);
							Glueps.showAlertDialog(i18n.get("warning"), message);
						}
					});
		} else {
			// Para el caso en que falte un campo por rellenar
			Glueps.showAlertDialog("warning", i18n.get("ErrorRellenarCampos"));
		}
	},
	
	deployImportInprocess: function(deployUrl){
		var url = deployUrl;
        var xhrArgs = {
            url : url,
            handleAs : "xml",// Tipo de dato de la respuesta del Get,
            sync: true,
            load : function(data) {	
				// Obtener todos los disenos y generar el listado
            	var deployUrl = Deploy.getDeployId(data);
				Design.getJsonDesigns(deployUrl);
				Design.resetFormDeploy();
            },

            error : function(error, ioargs) { 
            	if (ioargs.xhr.status == 503 || error.dojoType=='cancel')
            	{
            		//Está en proceso
            		//Esperar un tiempo en milisegundos y volver a realizar el GET
            		window.setTimeout(function(){Design.deployImportInprocess(deployUrl);}, 5000);
            	}
            	else
            	{
	            	//Otro error en el proceso de despliegue
					Glueps.hideLoadingDialog()
                    var codigo = 2;
                    var message = ErrorCodes.errores(codigo);
					Glueps.showAlertDialog(i18n.get("warning"), message);
            	}
            }
        }
        // Call the asynchronous xhrGet
        var deferred = dojo.xhrGet(xhrArgs);			
	},
	
	/**
	 * Resetea el formulario de creación del diseño
	 */
	resetFormDesign: function(){
		document.forms['myform'].reset();
		//Marcamos el primer valor del checkbox
		dijit.byId("imsldTypeValue").attr("checked",true);	
	},
	
	/**
	 * Resetea el formulario de importación de despliegue
	 */
	resetFormDeploy: function(){
		document.forms['importDeployForm'].reset();
		//Marcamos el primer valor del checkbox
		dijit.byId("gluepsDeployTypeValue").attr("checked",true);
        ImportDeploy.getLEnvironments();
		var vleSelect=dojo.byId("importVleSelect");
		vleSelect.selectedIndex = 0;
        ImportDeploy.resetSelectClassVle();
        ImportDeploy.checkGetClassesVle();
	},
    
    /**
     * Obtiene el identificador del diseño
     * @param jsdom DOM del documento de diseño
     * @returns id del diseño o false si no se ha encontrado
     */
    getDesignId: function(jsdom){
		var designNode = jsdom.getElementsByTagName("design")[0];
		var atrib = designNode.attributes;
		for ( var k = 0; k < atrib.length; k++) {
			if (atrib[k].nodeName == "id") {
				return(atrib[k].nodeValue);
			}
		}
		return false;
    }
};
