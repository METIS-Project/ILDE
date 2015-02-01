/**
 * Gestión de la pestaña general
 */
var General = {
    
    tmpLo: null,
    
    /**
     * Función de inicialización
     */
    init: function() {
        this.prepareDialogs();
    },
	
    /**
     * Prepara las ventanas necesarias
     */ 
    prepareDialogs: function() {
        dojo.connect(dojo.byId("toolbar.exit"), "onclick", function() {
            //window.location.assign("index.php");
            window.close();
        });
        var dlg = dijit.byId("LDTitleChangeDialog");
        dlg.onOpen = function() {
            dijit.byId("LDTitleChangeTextbox").set("value", LearningDesign.data.title);
        };
        /*dlg = dijit.byId("LDPrerrequisitesChangeDialog");
        dlg.onOpen = function() {
            dijit.byId("LDPrerrequisitesChangeTextarea").set("value", LearningDesign.data.prerrequisites);
        };*/
        dojo.connect(dojo.byId("deleteLoDialogOk"), "onclick", function(){
            General.closeDeleteLoDialog();
            General.removeLO(General.tmpLo);
        });        
        dojo.connect(dojo.byId("deleteLoDialogCancel"), "onclick", function(){
            General.closeDeleteLoDialog();
        });
    },
    
     /**
     * Abre la ventana de editar requisitos previos
     */ 
    openEditPrerreqDialog : function() {
        dijit.byId('LDPrerrequisitesEditPrerrequisites').set("value", LearningDesign.data.prerrequisites);
        dijit.byId('LDPrerrequisitesEditDialog').show();
    },
    
    saveEditPrerreqDialog : function() {
        var prerrequisites = dijit.byId('LDPrerrequisitesEditPrerrequisites').get('value');
        General.setPrerrequisites(prerrequisites);
        dijit.byId('LDPrerrequisitesEditDialog').hide();
    },

    closeEditPrerreqDialog : function() {
        dijit.byId('LDPrerrequisitesEditDialog').hide();
    },
    
    /**
     * Abre la ventana de añadir objetivo
     */ 
    openAddLoDialog : function() {
        dijit.byId("LDLearningObjectiveAddName").set("value", "");
        dijit.byId("LDLearningObjectiveAddDescription").set("value", "");
        dojo.byId("LDLearningObjectiveAddDialogErrorReport").innerHTML = "";
        dijit.byId('LDLearningObjectiveAddDialog').show();
    },
    
    closeAddLoDialog: function(){
        dijit.byId('LDLearningObjectiveAddDialog').hide();
    },
    
    openDeleteLoDialog: function(lo){
        this.tmpLo = lo;
        dojo.byId("deleteLoDialogInfo").innerHTML = dojo.string.substitute(i18n.get("general.deleteLo.confirm"), [ lo.title ]);
        dijit.byId("deleteLoDialog").show();
    },
    
    closeDeleteLoDialog: function(){
        dijit.byId("deleteLoDialog").hide();
    },
    
    /**
     * Actualiza la pestaña
     */ 
    updateDisplay: function() {
        dojo.byId("LDTitleContent").innerHTML = LearningDesign.data.title;
        dojo.byId("LDPrerrequisitesContent").innerHTML = LearningDesign.data.prerrequisites.replace(/\n/g, "<br/>");
        this.generateLOTable();
        //this.updateAlerts();
    },

    /**
     * Actualiza las alertas
     */ 
    updateAlerts: function() {
        ClipRenderer.display("LDTitleAlert", "title");
        ClipRenderer.display("LDPrerrequisitesAlert", "prerrequisites");
        ClipRenderer.display("LDLosAlert", "los");
        ClipRenderer.display("EmptyFlowAlert", "flow");
        for (var i in LearningDesign.data.learningObjectives) {
            var lo = LearningDesign.data.learningObjectives[i];
            ClipRenderer.display("LoAlert_" + lo.id, lo.id);
        }
    },
    
    /**
     * Asigna el título
     * @param title Título a asignar
     */ 
    setTitle: function(title){
        var element = dojo.byId("LDTitleContent");
        element.innerHTML = title;
        LearningDesign.setTitle(title);
    },
    
    /**
     * Asigna los requisitos previos
     * @param prerrequisites Requisitos previos a asignar
     */ 
    setPrerrequisites: function(prerrequisites){
        var element = dojo.byId("LDPrerrequisitesContent");
        element.innerHTML = prerrequisites.replace(/\n/g, "<br/>");
        LearningDesign.setPrerrequisites(prerrequisites);
    },
    
    /**
     * Añade un objetivo de aprendizaje
     * @param data Objetivo a añadir
     */ 
    addLearningObjective: function(){
        var name = dojo.byId("LDLearningObjectiveAddName").value;
        var description = dojo.byId("LDLearningObjectiveAddDescription").value;
        if (name.length == 0){
            dojo.byId("LDLearningObjectiveAddDialogErrorReport").innerHTML = i18n.get("general.loName.error.empty");
        }else{
            var lo = new LearningObjective(name, description);
            LearningDesign.addLO(lo);
            General.closeAddLoDialog();
        }
    },
    
    /**
     * Genera la tabla de objetivos de aprendizaje
     */ 
    generateLOTable: function(){
        var table = dojo.byId("LDLearningOobjectivesContent");
        dojo.query("#LDLearningOobjectivesContent > *").orphan();       
        for (var i in LearningDesign.data.learningObjectives) {
            var lo = LearningDesign.data.learningObjectives[i];        
            var li = table.appendChild(document.createElement("li"));
            dojo.addClass(li, "loListItem");
            var name = li.appendChild(document.createElement("span"));
            name.innerHTML = lo.title;
            dojo.addClass(name, "loListItemName");
            name.setAttribute("title", lo.description);
            var alert = li.appendChild(document.createElement("span"));
            dojo.attr(alert, "id", "LoAlert_" + lo.id);
            MenuManager.registerThing(name, {
                getItems: function(data) {
                    return General.getLOMenu(data);
                },
                data: {
                    lo: lo
                }
            });

            /* now let's add the related activities */
			
            var activities = LearningDesign.activitiesAndLOsMatches.getMatchingObjectsFor(lo.id);
            for (var j in activities) {
                ElementFormat.formatElement(li, activities[j], {
                    activityTooltip: true,
                    classes: "elementInLo"
                });
            }
        }
    },
    
    /**
     * Refleja los cambios realizados en el objetivo
     */ 
    finishedEditingLO: function(){
        var name = dojo.byId("LDLearningObjectiveEditName").value;
        var description = dojo.byId("LDLearningObjectiveEditDescription").value;
        if (name.length == 0){
            dojo.byId("LDLearningObjectiveEditDialogErrorReport").innerHTML = i18n.get("general.loName.error.empty");
        }else{
            this.editedObject.title = name;
            this.editedObject.description = description;
            ChangeManager.loEdited(this.editedObject);
            this.closeEditLO();
        }
    },
    
    /**
     * Obtiene el menú de opciones para el objetivo
     */ 
    getLOMenu: function(data) {
        var items = new Array();
        items.push({
            label: i18n.get("general.editLo"),
            icon: "editlo",
            onClick: function(data) {
                General.editLO(data.lo);
            },
            data: data,
            help: i18n.get("help.general.editLo")
        });
        items.push({
            isSeparator: true
        });
        items.push({
            label: i18n.get("general.deleteLo"),
            icon: "delete",
            onClick: function(data) {
                General.openDeleteLoDialog(data.lo);
            },
            data: data,
            help: i18n.get("help.general.deleteLo")
        });
        return items;
    },

    /**
     * Abre la ventana de edición de un objetivo de aprendizaje
     */ 
    editLO: function(lo) {
        General.editedObject = lo;
        dijit.byId("LDLearningObjectiveEditDialog").show();
        dijit.byId("LDLearningObjectiveEditName").setValue(lo.title);
        dijit.byId("LDLearningObjectiveEditDescription").setValue(lo.description);
    },
    
    closeEditLO: function(){
        dijit.byId("LDLearningObjectiveEditDialog").hide();
    },

    /**
     * Elimina un objetivo de aprendizaje
     */ 
    removeLO: function(lo) {
            LearningDesign.removeLO(lo);
            this.tmpLo = null;
    }
};

