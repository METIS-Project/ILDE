dojo.registerModulePath("glueps.dojoi18n", "../../../dojoi18n");
dojo.require("dojo.io.iframe");
dojo.require("dojox.xml.DomParser");
dojo.require("dojox.xml.parser");
dojo.require("dojo.io.script");

var Deploy = {
		
    init : function() {
        // Inicializar el check de Deploy. Inicialmente desactivado
        var nodoCheck = dijit.byId("checkDeploy");
        nodoCheck.checked = false;
        // No mostrar lista asociada al check
        dojo.style(dojo.byId("temaSelect"), {
            display : "none"
        });
		
        // Asociar funcionalidad a los botones de anterior y siguiente
        dojo.connect(dijit.byId("buttonNewDeployBack"), "onClick", Deploy, "buttonNewDeployBack");
        dojo.connect(dijit.byId("buttonNewDeployNext"), "onClick", Deploy, "buttonNewDeployNext");
        
        //Obtener las clases de un lms de glue
        dojo.connect(dojo.byId("vleSelect"), "onchange", function(){
    		var leValue = dojo.byId("vleSelect").value;
        	var leType = LE.getLearningEnvironmentType(leValue);  		
    		if (leType=="Blogger" && OauthManager.oauthRequired(leValue)){
    			var callerMethod = "newdep";
    			//start the oauth process in order to have an updated access token which let us get the courses
    			//we provide a name for the callerMethod and the id of the selected vle
    			OauthManager.startOauth("callerMethod=" + callerMethod + "&leId=" + leValue);
    	    }else{
    	    	Deploy.resetSelectClassLms();
    	    	Deploy.checkGetClassesLms();
    	    }
        });
    },
    
	maxTemas: 20,
    
	/**
	 * Funcionalidad asociada al botón de nuevo despliegue de un diseño
	 * @param idDesign Identificador numérico del diseñó
	 * @param titleDesign Nombre del diseño
	 */
	anadirTitle : function(idDesign, titleDesign) {
		Deploy.resetFormDeploy();
		
		var nodoP = dojo.byId("newDeployDesignTitle");
		var nodoText = dojo.byId("NewDeployTitleName");
		nodoP.innerHTML = titleDesign;
		nodoText.value = titleDesign;
		var baseUrl = window.location.href.split("/GLUEPSManager")[0];
		// Anado el action del form del Panel Deploy
		dojo.byId("deployForm").action = baseUrl + "/GLUEPSManager/designs/" + idDesign + "/deploys";
		
		Deploy.showNewDeploy();
	},
	
	/**
	 * Resetea el formulario de creación del despliegue
	 */
	resetFormDeploy: function(){
		//Reinicio formulario de la pantalla 2.
		document.forms['deployForm'].reset();
		//Marcamos el primer valor del radio button
		dijit.byId("imsldTypeValueInst").attr("checked",true);
		//Desmarcamos el checkbox de añadir curso a partir de un tema
		dijit.byId("checkDeploy").attr("checked",false);
        // No mostrar lista asociada al check
        dojo.style(dojo.byId("temaSelect"), {
            display : "none"
        });
	},
    
    /**
     * Resetea el contenido del select de LMS
     */
    resetSelectClassLms: function(){
        var class_select = dojo.byId("courseSelect");
        while (class_select.options.length > 0){
            class_select.remove(0);
        }
    	var option = new Option(i18n.get("NewDeployLabelSelectCourseDefault"),"0");
    	option.selected = true;
    	class_select.options[0]=option;
		 //Inicialmente no se muestra la selección de clase 
		 dojo.style("divCourseSelect","display","none");
    },
    
    /**
    * Obtiene las clases de un LMS
    */
     checkGetClassesLms: function(){
    	 if (dojo.byId("vleSelect").value!=0){
    		 Deploy.getClassesLms(dojo.byId("vleSelect").value);
    	 }
     },
     
     /**
      * Obtiene las clases de un LMS y genera el select de la clase
      * @param lms Identificador del LMS del que se obtendrían las clases
      */
     getClassesLms: function(lms){
        var xhrArgs = {
             url: lms,
             handleAs: "text",//Tipo de dato de la respuesta del Get
             load: function(data) {
                //Tratamiento de la respuesta del get
            	var clases = Deploy.getClasses(data);
                var class_select = dojo.byId("courseSelect");
                for (var i = 0; i < clases.length; i++)
                {
                 	var option = new Option(clases[i].name, clases[i].id);
                 	class_select.options[class_select.options.length]=option;
                }
                dojo.style("divCourseSelect","display","");
             },
             error: function(error, ioargs) {  	   
            	 //Mostrar mensaje
            }
        };
        //Call the asynchronous xhrGet
        var deferred = dojo.xhrGet(xhrArgs);
     },
     
     /**
      * Dado el xml obtiene las clases y las devuelve en un array
      * @param xml XML con información de las clases
      * @return Array con el identificador y el nombre de cada clase
      */
     getClasses: function(xml)
     {
    	 var jsdom = dojox.xml.DomParser.parse(xml);
    	 var courses = new Array();
    	 var coursesNode = jsdom.getElementsByTagName("courses");
    	 if (coursesNode.length > 0)
    	 {
    		 var entries = coursesNode[0].getElementsByTagName("entry");
    		 for (var i = 0; i < entries.length; i++)
    		 {
    			 var course = new Object();
    			 course.id = entries[i].getElementsByTagName("key")[0].childNodes[0].nodeValue;
    			 course.name = entries[i].getElementsByTagName("value")[0].childNodes[0].nodeValue;
    			 courses.push(course);
    		 }
    		 
    	 }
    	 return courses;
     },
	
     /**
      * Funcionalidad del botón anterior
      */
    buttonNewDeployBack : function() {
		document.forms['myform'].reset();
		//Marcamos el primer valor del checkbox
		dijit.byId("imsldTypeValue").attr("checked",true);
        // Muestro Pantalla1
        Design.showDesign();
    },
	
    /**
	 * Funcionalidad asociada al botón siguiente en grupos y participantes
	 */
    buttonNewDeployNext : function() {
        Deploy.postDeploy();
    },
    
    /**
     * Oculta el panel de creación de un despliegue
     */
    ocultarPanelNewDeploy : function() {
        dojo.style(dojo.byId("newDeploy"), {
            display : "none"
        });
        
    },
    
    /**
     * Muestra el panel de creación de un despliegue
     */
    mostrarNewDeploy : function() {
        dojo.style(dojo.byId("newDeploy"), {
            display : ""
        });
    },

    /**
     * Genera el contenido del panel de creación de un despliegue
     */
    showNewDeploy : function() {
        // Ocultamos el resto de paneles que no pertenecen a Deploy
        // Diseño
        Design.ocultarDesign();

        // Mostramos la pantalla de nuevo despliege
        Deploy.mostrarNewDeploy();

        // Hacemos get para obtener los vle
        LE.getLEnvironments();
        // Relleno select de los temas
        // El número 20 de momento se lo meto a pedal
        Deploy.addtemas(Deploy.maxTemas);
    },
    
    /**
     * Comprueba los campos del formulario y crea el nuevo despliegue
     */
    postDeploy : function() {
        // Titulo del despliege NewDeployTitleName
        var titleDeploy = document.getElementById("NewDeployTitleName").value;
        // fichero archiveWic
        var archiveDeploy = document.getElementById("archiveWic").value;
        var lmsSelected = dojo.byId("vleSelect").value;
        var errorForm = false;
        if ((titleDeploy == "") || lmsSelected== "0") {
            errorForm = true;
        }
        if (dojo.byId("divCourseSelect").style.display!="none")
        {
        	var courseSelected = dojo.byId("courseSelect").value;
        	if (courseSelected=="0")
        	{
        		errorForm = true;
        	}
        }
        if (!errorForm) {
        	Glueps.showLoadingDialog();

            dojo.io.iframe.send({
                method : "POST",
                form : dojo.byId("deployForm"),
                handleAs : "html",
                load : function(data) {
                	window.location="deploy.html?deployId=" + data.getElementsByTagName('deploy')[0].getAttribute('id').split("/deploys/")[1];
                },
                error : function(error, ioargs) {
                    // Oculto gif animado puesto en marcha en putDeploys
                    dijit.byId("loading").hide();
                    var message = "";
                    var codigo = 2;
                    message = ErrorCodes.errores(codigo);
                    Glueps.showAlertDialog(i18n.get("info"),message);
                }
            });
        } else {
            // Para el caso en que falte un campo por rellenar
        	Glueps.showAlertDialog(i18n.get("warning"),i18n.get("ErrorRellenarCampos"));
        }
    },
    
    /**
     * Muestra el listado de despliegues de cada diseño
     * @param recentlyCreatedId Identificador del diseño que se acaba de crear
     */
    addJsonListDeploys : function(recentlyCreatedId) {
    	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
        var targetNode = dojo.byId("getLicenseHeaders");
        var listDe;
        // The parameters to pass to xhrGet, the url, how to handle it, and the
        // callbacks.
        var xhrArgs = {
            url : baseUrl + "/GLUEPSManager/deploys",
			handleAs : "json",// Tipo de dato de la respuesta del Get,
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			}, 
            load : function(data) {
                Deploy.construirListadoDep(data, recentlyCreatedId);
            },
            error : function(error, ioargs) {
                // Oculto gif animado puesto en marcha en putDeploys
                dijit.byId("loading").hide();
                var message = "";
                message = ErrorCodes.errores(ioargs.xhr.status);
                Glueps.showAlertDialog(i18n.get("warning"), message);
            }
        };

        // Call the asynchronous xhrGet
        var deferred = dojo.xhrGet(xhrArgs);
    },
    
	getDeployId : function(link) {
		var search = 'deploys/';
		return link.substring(link.lastIndexOf(search) + search.length);
	},
	
    /**
	 * Construye el listado de despliegues
	 * @param listDeploys array que contiene los despliegues
	 * @param recentlyCreatedId Identificador del diseño que se acaba de crear
	 */
    construirListadoDep : function(listDeploys, recentlyCreatedId) {
        var idDesign;
        var linkDesign;
        var nodoUl;
        var nodoLiDeploy;
        var nameDeploy;
        // Ordeno lista de deploys por titulo
        listDeploys.sort(natcompareName);
        for ( var i = 0; i < listDeploys.length; i++) {
            linkDesign = listDeploys[i].design;
            idDesign = linkDesign.substring(linkDesign.lastIndexOf("/") + 1, linkDesign.length);
            nameDeploy = listDeploys[i].name;
            idDeploy = Design.obtenerId(listDeploys[i].id);

            nodoUl = document.getElementById("li-" + idDesign + "-ul");
            if (nodoUl != null) {
                var tablaDespliegues=nodoUl.childNodes[0];
            	var tr = document.createElement("tr");
            	
                nodoLiDeploy = document.createElement("li");
                nodoLiDeploy.setAttribute("id", idDeploy);
                nodoLiDeploy.innerHTML = nameDeploy;
                if (recentlyCreatedId && recentlyCreatedId==idDeploy.substring(idDeploy.lastIndexOf("/")+1))
                {
                	nodoLiDeploy.setAttribute("style","color:blue;font-weight:bold");
                }
                
                var completed = (listDeploys[i].complete == "true");
                
                var td = document.createElement("td");
            	tr.appendChild(td);
            	td.appendChild(nodoLiDeploy);
            	
    			//Asociamos el menú al despliegue
                MenuManager.registerThing(td, {
                    getItems: function(data) {
                        return DeployMenu.getDeployMenu(data);
                    },
                    data: {
                    	idDeploy: Deploy.getDeployId(idDeploy),
                    	nameDeploy: Deploy.buildUniqueDeployName(listDeploys, listDeploys[i])
                    },
                    menuStyle: "default"
                });

                //Si el despliegue está completo
                if (completed) { 
	       			  //if it has a static deploy, we display the link
	       			  if(listDeploys[i].staticDeploy){
	       				  
		       			  var a =document.createElement("a");
		       			  a.setAttribute("name","enlaceDespliegueEstaticoPantalla1");
		       			  a.setAttribute("style","margin-left:3px;");
	       				  a.setAttribute("href",listDeploys[i].staticDeploy);
	       				  a.setAttribute("title", i18n.get("getDeployToolTip"));
	       				  
	                      var td = document.createElement("td");
	                      td.setAttribute("class", "wb");
	                  	  tr.appendChild(td);
	                  	  td.appendChild(a);
	       				  
		       			  var zipImage = document.createElement("img");
		       			  zipImage.setAttribute("src","images/icons/zip.png");
		       			  a.appendChild(zipImage);
	       			  }
					 
	       			  //if it has a live deploy, we display the link
	       			  if(listDeploys[i].liveDeploy){
	       				  
		       			  var a =document.createElement("a");
		       			  a.setAttribute("name","enlaceDespliegueDinamicoPantalla1");
		       			  a.setAttribute("style","margin-left:3px;");
	       				  a.setAttribute("href",listDeploys[i].liveDeploy);	     
	       				  a.setAttribute("title", i18n.get("LiveDeployVleHere"));
	       				  a.setAttribute("target", "_blank");
	                      
	                      var td = document.createElement("td");
	                      td.setAttribute("class", "wb");
	                  	  tr.appendChild(td);
	                  	  td.appendChild(a);
	                  	  
		       			  var linkImage = document.createElement("img");
		       			  linkImage.setAttribute("src","images/icons/link.png");
		       			  a.appendChild(linkImage);
	       			  }
                    
                    
                }
                tablaDespliegues.appendChild(tr);
            }
        }
        // Colapso menu
        var entryList = Design.designList;

        for ( var k = 0; k < entryList.length; k++) {
            var contenidoLink = entryList[k].id;
            var id = Design.obtenerId(contenidoLink);
            var name = entryList[k].name;
            var designType = entryList[k].designtype;
            var nodo = document.getElementById("span-li-" + id + "-ul");           
            
            nodo.setAttribute("style", "display:none");
            Deploy.colapsar("li-" + id + "-ul", name);
            
            var divNode = nodo.parentNode.childNodes[1];
            if (divNode.childNodes.length==1)
            {
    			var spanDesignType=document.createElement("span");
    			spanDesignType.setAttribute("class", "designType");
    			spanDesignType.innerHTML = designType;
    			divNode.appendChild(spanDesignType);
            }
            
            var nodoA = divNode.childNodes[0];
            var designId = entryList[k].id;
            //Resaltamos de una forma diferente el diseño que se acaba de crear
            if (recentlyCreatedId && recentlyCreatedId==designId)
            {
            	nodoA.setAttribute("style","color:blue;");
            }
			//Asociamos el menú al diseño
            MenuManager.registerThing(nodoA, {
                getItems: function(data) {
                    return DesignMenu.getDesignMenu(data);
                },
                data: {
					id: id,
					design: name
                },
                menuStyle: "default"
            });
        }
        // Vacio variable
        Design.designList = "";
        dijit.byId("loading").hide();
        
		var nodoUl = document.getElementById("myListDesignUl");
		dojo.style(nodoUl, {display : ""});
    },
    
    /**
     * Busca entre los nodos hijos de uno dado aquel que tiene el nombre deseado
     * @param nodo Nodo padre
     * @param tagBuscado Nombre a buscar
     * @returns Nodos con el nombre indicado
     */
    buscarTag : function(nodo, tagBuscado) {
        var listaNodos = nodo.childNodes;
        var salida = null;
        for ( var j = 0; j < listaNodos.length; j++) {
            if (listaNodos[j].localName == tagBuscado) {
                salida = listaNodos[j];
                break;
            }
        }
        return salida;
    },
    
    colapsar : function(id, name) {
        $("#" + id).collapsorz({
            minimum : 0,
            showText : "+ " + name,
            hideText : "- " + name,
            linkLocation : "before",
            defaultState : "expanded",
            wrapLink : '<div class="linkWrapper"></div>'
        });

    },
    
    /**
     * Genera el select del tema
     * @param num Número máximo de temas
     */
    addtemas : function(num) {
        var vle_Select = dojo.byId("temaSelect");
        // Elimino options del select
        while (vle_Select.options.length != 0) {
            vle_Select.removeChild(vle_Select.options[0]);
        }
        // Anado options al select
        for ( var i = 1; i <= num; i++) {
            var option = document.createElement("option");
            option.innerHTML = i;
            // El id es el nombre que se ha dado al archivo xml
            // de ahí que saquemos el id de la url de acceso al recurso.
            option.value = i;
            vle_Select.appendChild(option);
        }
    },
    
    buildUniqueDeployName: function(listDeploys, deployItem){
    	var designLink = deployItem.design;
    	var deployId = Design.obtenerId(deployItem.id);
    	var deployName = deployItem.name;
    	var designDeploysSameName;
    	var i = 1;
		do{
			designDeploysSameName = false;
			var newName = deployName + "_(" + i + ")";
	        for ( var j = 0; j < listDeploys.length; j++) {
	            cmpDesignLink = listDeploys[j].design;
	        	cmpDeployId = Design.obtenerId(listDeploys[j].id)
	            cmpDeployname = listDeploys[j].name;
	            if (cmpDeployname==newName && cmpDeployId!=deployId && cmpDesignLink==designLink){
	            	designDeploysSameName = true;
	            	break;
	            }
	        }
	        i++;
		}while(designDeploysSameName == true);
		return newName;
    }
	
};

