var Glueps = {
		
    init : function() {
    	
        //Mostrar los textos de la aplicaci�n en el idioma correspondiente
        Glueps.internacionalizar();

        //Mostrar la pantalla de dise?os
        Design.showDesign();

        //Asocia a las banderas el cambio de idioma
        dojo.connect(dojo.byId("localeEs"), "onclick", function() {
            Glueps.asignarIdiomaEs();
        });
        
        dojo.connect(dojo.byId("localeEn"), "onclick", function() {
            Glueps.asignarIdiomaEn();
        });
        
        dojo.connect(dojo.byId("localeCa"), "onclick", function() {
            Glueps.asignarIdiomaCa();
        });
    },
    
    /**
     * Establece la interfaz de la aplicaci�n en castellano
     */
    asignarIdiomaEs : function() {
        dojo.style(dojo.byId("localeEs"), {
            "cursor" : "pointer"
        });
        djConfig.locale = "es";
        Glueps.internacionalizar();
    },
    
    /**
     * Establece la interfaz de la aplicaci�n en ingl�s
     */
    asignarIdiomaEn : function() {
        dojo.style(dojo.byId("localeEn"), {
            "cursor" : "pointer"
        });
        djConfig.locale = "en";
        Glueps.internacionalizar();
    },
    
    /**
     * Establece la interfaz de la aplicación en catal�n
     */
    asignarIdiomaCa : function() {
        dojo.style(dojo.byId("localeCa"), {
            "cursor" : "pointer"
        });
        djConfig.locale = "ca";
        Glueps.internacionalizar();
    },
    
    /**
     * Establece los textos de la aplicaci�n en el idioma actual
     */
    internacionalizar : function() {
    	//Subida de dise�os
        dijit.byId("newDesign").setAttribute("title", i18n.get("NewDesignPagetitle"));
        dojo.byId("labelNewDTitle").innerHTML = i18n.get("NewDesignLabelNewDtitle");
        dojo.byId("labelNewDImportDesign").innerHTML = i18n.get("NewDesignNewDImportDesign");
        dojo.byId("labelNewDDesigType").innerHTML = i18n.get("NewDesignLabelNewDDesigType");
        //dijit.byId("ppcTypeValue").setAttribute("value", i18n.get("NewDesignLabelNewDDesigTypePpcType"));
        dojo.byId("ppcType").innerHTML = i18n.get("NewDesignLabelNewDDesigTypePpcType");
        //dijit.byId("imsldTypeValue").setAttribute("value", i18n.get("NewDesignLabelNewDDesigTypeImsldType"));
        dojo.byId("imsldDesignType").innerHTML = i18n.get("NewDesignLabelNewDDesigTypeImsldType");
        //dijit.byId("T2TypeValue").setAttribute("value", i18n.get("NewDesignLabelNewDDesigTypeT2Type"));
        dojo.byId("T2Type").innerHTML = i18n.get("NewDesignLabelNewDDesigTypeT2Type");
        dojo.byId("gluepsType").innerHTML = i18n.get("NewDesignLabelNewDDesigTypeGluepsType");
        dijit.byId("newDButtonCreateDesign").setAttribute("label", i18n.get("NewDesignNewDButtonDesign"));
        //Subida de despliegues
        dijit.byId("importDeploy").setAttribute("title", i18n.get("ImportDeployPagetitle"));
        dojo.byId("labelImportDeployTitle").innerHTML = i18n.get("LabelImportDeployTitle");
        dojo.byId("labelImportDeploy").innerHTML = i18n.get("ImportDeployImportDeploy");
        dojo.byId("labelImportDeployType").innerHTML = i18n.get("ImportDeployLabelDeployType");
        dojo.byId("gluepsDeployType").innerHTML = i18n.get("ImportDeployLabelTypeGluepsType");
        //dojo.byId("labelImportDeployToolsConf").innerHTML = i18n.get("ImportDeployDeleteToolsConf");
        dojo.byId("importDeployVleSelect").innerHTML = i18n.get("ImportDeployLabelSelectVLE");
        dojo.byId("importDeployCourseSelect").innerHTML=i18n.get("ImportDeployLabelSelectCourse");
        dijit.byId("importDeployButton").setAttribute("label", i18n.get("NewDesignNewDButtonDesign"));
        dijit.byId("ttHelpCheckImport").setAttribute("label",i18n.get("helpCheckImportInfo"));
        var vle_Select=dojo.byId("importVleSelect");
        if (vle_Select.options.length > 0){
        	vle_Select.options[0].innerHTML=i18n.get("NewDeployLabelSelectVleDefault");
        }
        var course_select = dojo.byId("importCourseSelect");
        if (course_select.length > 0){
        	course_select.options[0].innerHTML=i18n.get("ImportDeployLabelSelectCourseDefault");
        }
        
        //Instalaciones de VLEs
        dojo.byId("manageVleButton").innerHTML = i18n.get("manageVlesTitle");
        dijit.byId("manageVles").setAttribute("title", i18n.get("manageVlesTitle"));
        dojo.byId("createVleInstallation").innerHTML = i18n.get("createVleInstallation");
        dijit.byId("divTableVleInst").setAttribute("title", i18n.get("vleInstallationList"));
        dojo.byId("acceptManageVles").innerHTML = i18n.get("okButton");
        dojo.byId("cancelManageVles").innerHTML = i18n.get("cancelButton");

        //Lista de dise�os
        dijit.byId("myListDesign").setAttribute("title", i18n.get("MyListDesignTitle"));
        for(var j=0; j<document.getElementsByName("enlaceDespliegueEstaticoPantalla1").length; j++ ){
           document.getElementsByName("enlaceDespliegueEstaticoPantalla1")[j].setAttribute("title", i18n.get("getDeployToolTip"));
        }
        
        for(var j=0; j<document.getElementsByName("enlaceDespliegueDinamicoPantalla1").length; j++ ){
           document.getElementsByName("enlaceDespliegueDinamicoPantalla1")[j].setAttribute("title", i18n.get("getDeployToolTip"));
        }

        //Nuevo despliegue
        dijit.byId("newDeploy").setAttribute("title", i18n.get("NewDeployTitle"));
        dojo.byId("spanNDeployDesignTitle").innerHTML = i18n.get("NewDeployLabelDesignTitle");
        dojo.byId("spanNDeployTitleDeploy").innerHTML = i18n.get("NewDeployLabelTitle");
        dojo.byId("legendParticipantes").innerHTML = i18n.get("NewDeployTitle");
        dojo.byId("legendLMS").innerHTML = i18n.get("NewDeployLabelSelectVLE");
        dojo.byId("newDeployPartSpanFormat").innerHTML = i18n.get("NewDeployLabelFormat");
        //dijit.byId("imsldTypeValueInst").setAttribute("value", i18n.get("NewDesignLabelNewDDesigTypeImsldType"));
        dojo.byId("imsldTypeInst").innerHTML = i18n.get("NewDesignLabelNewDDesigTypeImsldType");
        //dijit.byId("T2TypeValueInst").setAttribute("value", i18n.get("NewDesignLabelNewDDesigTypeT2Type"));
        dojo.byId("T2TypeInst").innerHTML = i18n.get("NewDesignLabelNewDDesigTypeT2Type");
        dojo.byId("newDeployPartSpanUploadFile").innerHTML = i18n.get("NewDeployLabelUploadFile");
        dojo.byId("newDeployVleSelect").innerHTML = i18n.get("NewDeployLabelSelectVLE");
        dojo.byId("newDeployCourseSelect").innerHTML=i18n.get("NewDeployLabelSelectCourse");
        dojo.byId("newDeployTeacherUsername").innerHTML = i18n.get("NewDeployLabelTeacherUsername");
        dojo.byId("labelMyCheck").innerHTML = i18n.get("NewDeployLabelCheckBoxAddInCourse");
        dojo.byId("buttonNewDeployBack").innerHTML = i18n.get("NewDeployLabelButtonBack");
        dojo.byId("buttonNewDeployNext").innerHTML = i18n.get("NewDeployLabelButtonNext");
    },
	
    /**
	 * Muestra la ventana de proceso con el t�tulo indicado
	 * @param info Informaci�n a mostrar en la ventana
	 */
    showLoadingDialog: function(info){
        if (info){
            dojo.byId("loadingInfo").innerHTML = info;
        }
        else{
            dojo.byId("loadingInfo").innerHTML = i18n.get("ProcesandoArchivos");
        }
        dijit.byId("loading").setAttribute("title", i18n.get("wait"));
        dijit.byId("loading").show();
    },
    
    /**
     * Oculta la cruz de cerrar de la ventana donde se muestra el gif animado para la espera
     */
    hideCloseLoadingDialog: function(){
        dojo.byId("loading").childNodes[1].childNodes[3].style.display = "none";
    },

    /**
	 * Especifica la informaci�n a mostrar en la ventana de proceso
	 * @param info Informaci�n a mostrar en la ventana de proceso
	 */
    setInfoLoadingDialog: function(info){
        dojo.byId("loadingInfo").innerHTML = info;
    },
	
    /**
	 * Obtiene la informaci�n a mostrar en la ventana de proceso
	 * @return Informaci�n a mostrar en la ventana de proceso
	 */
    getInfoLoadingDialog: function(){
        return dojo.byId("loadingInfo").innerHTML;
    },	
	
    /**
	 * Oculta la ventana de proceso
	 */
    hideLoadingDialog: function(){
        dijit.byId("loading").hide();
    },
	
    /**
     * Muestra un di�logo de alerta
     * @param txtTitle T�tulo del di�logo de alerta
     * @param txtContent Contenido del di�logo de alerta
     */
    showAlertDialog: function(txtTitle, txtContent){
        var dlg = dijit.byId("dialogAlert");
        dlg.titleNode.innerHTML = txtTitle;
        dojo.byId("dialogAlertInfo").innerHTML = txtContent;
        dlg.show();
    },
    
    /**
     * Oculta el di�logo de alerta
     */
    hideAlertDialog: function(){
        var dlg = dijit.byId("dialogAlert");
        dlg.hide();
    }
};
