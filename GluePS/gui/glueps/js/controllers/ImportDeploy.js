dojo.registerModulePath("glueps.dojoi18n", "../../../dojoi18n");
var ImportDeploy = {

    init: function(){
    	
        //Obtener las clases de un lms de glue
        dojo.connect(dojo.byId("importVleSelect"), "onchange", function(){
    		var leValue = dojo.byId("importVleSelect").value;
        	var leType = ImportDeploy.getLearningEnvironmentType(leValue);  		
    		if (leType=="Blogger" && OauthManager.oauthRequired(leValue)){
    			var callerMethod = "impdep";
    			//start the oauth process in order to have an updated access token which let us get the courses
    			//we provide a name for the callerMethod and the id of the selected vle
    			OauthManager.startOauth("callerMethod=" + callerMethod + "&leId=" + leValue);
    	    }else{
    	    	ImportDeploy.resetSelectClassVle();
    	    	ImportDeploy.checkGetClassesVle();
        	}
        });
        
        var tooltip = new dijit.Tooltip({
			connectId:  ["helpCheckImport"],
			label: i18n.get("helpCheckImportInfo"),
			id: "ttHelpCheckImport"
		});
    },
    
    leJson: "",
    
     /**
      * Obtiene los diferentes VLE
      */
     getLEnvironments:function() {
    	 var baseUrl = window.location.href.split("/GLUEPSManager")[0];
        //The parameters to pass to xhrGet, the url, how to handle it, and the callbacks.
        var xhrArgs = {
            url: baseUrl + "/GLUEPSManager/learningEnvironments",
			handleAs : "json",// Tipo de dato de la respuesta del Get,
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			}, 
            load: function(data) {
            	ImportDeploy.leJson = data;
            	//Tratamiento de la respuesta del get            		           	
            	ImportDeploy.rellenarSelectVle(data);
            },
            error: function(error, ioargs) {  	   
         	   var message = "";
         	  ImportDeploy.leJson = "";
         	   message=ErrorCodes.errores(ioargs.xhr.status);
         	   Glueps.showAlertDialog(i18n.get("warning"), message);
            }
        };

        //Call the asynchronous xhrGet
        var deferred = dojo.xhrGet(xhrArgs);
    },
    
    /**
     * Resetea el contenido del select de clase
     */
    resetSelectClassVle: function(){
        var class_select = dojo.byId("importCourseSelect");
        while (class_select.options.length > 0){
            class_select.remove(0);
        }
    	var option = new Option(i18n.get("ImportDeployLabelSelectCourseDefault"),"0");
    	option.selected = true;
    	class_select.options[0]=option;
		 //Inicialmente no se muestra la selección de clase 
		 dojo.style("importDeployCourseTr","display","none");
    },
    
    /**
     * Añade las opciones al select del lms
     * @param data la información de los LMS
     */
    rellenarSelectVle: function(data){
    	var entryList=data;
    	var vle_Select=dojo.byId("importVleSelect");    	
    	//Elimino options del select
       	while (vle_Select.options.length != 0)
    	{
    		vle_Select.removeChild(vle_Select.options[0]);    	
       	}
       	//Añadir opción por defecto
		var option =document.createElement("option");
		option.innerHTML=i18n.get("NewDeployLabelSelectVleDefault");
		option.value="0";
		option.setAttribute("selected","true");
		vle_Select.appendChild(option); 
    	//Añado restantes opciones
    	for(var i=0; i<entryList.length;i++){
    		var title=entryList[i].name;
    		var creduser = entryList[i].creduser;
    		var option =document.createElement("option");
    		option.innerHTML= title +  " (" + creduser + ")";
    		option.value=entryList[i].id;
    		vle_Select.appendChild(option); 
    	}
    	
    },
    
    /**
     * Obtiene las clases de un LMS
     */
      checkGetClassesVle: function(){
     	 if (dojo.byId("importVleSelect").value!=0){
     		 ImportDeploy.getClassesVle(dojo.byId("importVleSelect").value);
     	 }else{
     		dojo.style("importDeployCourseTr","display","none");
     	 }
      },
      
      /**
       * Obtiene las clases de un LMS y genera el select de la clase
       * @param lms Identificador del LMS del que se obtendrán las clases
       */
      getClassesVle: function(lms){
         var xhrArgs = {
              url: lms,
              handleAs: "text",//Tipo de dato de la respuesta del Get
              load: function(data) {
                 //Tratamiento de la respuesta del get
             	var clases = Deploy.getClasses(data);
                 var class_select = dojo.byId("importCourseSelect");
                 for (var i = 0; i < clases.length; i++)
                 {
                  	var option = new Option(clases[i].name, clases[i].id);
                  	class_select.options[class_select.options.length]=option;
                 }
                 dojo.style("importDeployCourseTr","display","");
              },
              error: function(error, ioargs) { 
            	  if (error.status == 403){
            		  //Mostrar mensaje
            		  Glueps.showAlertDialog(i18n.get("warning"), i18n.get("errorGetCourseListCredentials"));
            	  }
            	  else{
            		  Glueps.showAlertDialog(i18n.get("warning"), i18n.get("errorGetCourseList"));
            	  }
             }
         };
         //Call the asynchronous xhrGet
         var deferred = dojo.xhrGet(xhrArgs);
      },
      
      getLearningEnvironmentType: function(leId){
    	var leType ="";
      	if (ImportDeploy.leJson!=""){
    		var leType = "";
    		for (var i = 0; i < ImportDeploy.leJson.length; i++){
    			if (ImportDeploy.leJson[i].id==leId){
    				leType = ImportDeploy.leJson[i].type;
    				return leType;
    			}
    		}       		
    	}
      	return leType;
      }
};

