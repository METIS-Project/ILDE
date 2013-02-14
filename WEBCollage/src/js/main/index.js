

/**
 * Función de inicialización
 */
function init(){
    DojoPatch.patch();
	
    dojo.parser.parse();
    
    Users.init();
    LDManager.init();
    DesignVersionsInstances.init();
    LoadingPane.init();    
    MenuManager.init();
}

/**
 * Gestor de los diseños
 */
var LDManager = {
    currentVisibilityId: null,
    
    /**
     * Diseños con los que se trabaja
     */
    documents: {}, 
    /**
     * Tipos de visibilidad de un diseño
     */
    access: null,
    
    /**
     * Función de inicialización
     */
    init: function(){
    	
        this.access = {
            'public': i18n.get("design.type.public.short"),
            'visible': i18n.get("design.type.visible.short"),
            'private': i18n.get("design.type.private.short")
        };
    
        dojo.connect(dojo.byId("NewLdButton"), "onclick", function(){
            LDManager.createNew();
        });
        
        dojo.connect(dojo.byId("ClonarDisenoOk"), "onclick", LDManager, "comprobarClonarDiseno");
        dojo.connect(dojo.byId("ClonarDisenoCancel"), "onclick", LDManager, "cerrarDialogoClonarDiseno");
        
        dijit.byId("VisibilityDialog").onOpen = function(){
            dijit.byId("VisibilityOption_" + LDManager.documents[LDManager.currentVisibilityId].visibility).setChecked(true);
        };       
        this.showUserPanes(false);
        this.updateDisplay();
    },
    
    /**
     * Actualiza la pantalla de index donde se muestran los diseños
     */
    updateDisplay: function(){
        var bindArgs = {
            url: "manager/manageDesigns.php",
            handleAs: "json",
            content: {
                task: 'userDesigns',
                timestamp: new Date().getTime()
            },
            load: function(data){
                LDManager.updateDisplayResult(data);
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Borra una lista de nodos
     * @param listNode Lista de nodos a borrar
     */
    clearList: function(listNode){
        while (listNode.childNodes.length >= 1) {
            listNode.removeChild(listNode.firstChild);
        }
    },
    
    /**
     * Actualiza el listado de diseños del usuario
     * @param data Información sobre los diseños del usuario
     */
    updateDisplayResult: function(data){
        var myList = dojo.byId("MyLdList");      
        this.clearList(myList);        
        for (var i in this.documents) {
            var button = dijit.byId("visibility_" + this.documents[i].docid);
            if (button) {
                button.destroy();
            }
        }
        this.documents = {};

        this.updateDisplayForUser(data.username);

        for (i in data.list) {
            var ld = data.list[i];
            this.documents[ld.docid] = ld;
            var li = myList.appendChild(document.createElement("li"));
            dojo.addClass(li, "ldItem");
            dojo.attr(li, "id", "ld_"+ld.docid);          
            var name = li.appendChild(document.createElement("span"));
            name.innerHTML = ld.title;
            dojo.addClass(name, "ldItemName");
            
            MenuManager.registerThing(name, {
                getItems: function(data) {
                    return DesignVersionsInstances.getResourceMenu(data);
                },
                data: {
                    res: ld
                },
                menuStyle: "default"
            });
        }
    },
    
    /**
     * Solicita la apertura del diseño
     */
    play: function(){
        this.openLD();
    },
    
    /**
     * Comprueba el nombre del diseño y si es correcto solicita su creación
     */
    createNew: function(){
        var title = dijit.byId("NewLdName").getValue();
        if (title.replace(/^\s+/g,'').replace(/\s+$/g,'').length == 0) {
            dojo.byId("NewDesignPaneErrorReport").innerHTML = i18n.get("index.error.noName");
        }
        else {
            dojo.byId("NewDesignPaneErrorReport").innerHTML = "";
            LearningDesign.clear();
            var design = LearningDesign.data;
            design.title = title;
            DesignInstance.clear();
            var instance = DesignInstance.data;
            this.callForCreate(title, design, instance);
            dijit.byId("NewLdName").setValue("");
        }
    },
    
    /**
     * Llamada para la creación de un diseño
     * @param title Nombre que se dará al diseño
     */
    callForCreate: function(title, design, instance){
        var bindArgs = {
            url: "manager/manageDesigns.php",
            handleAS: "json",
            content: {
                task: "create",
                title: title, 
                design: JSON.encode(design),
                instance: JSON.encode(instance)
            },
            handleAs: "json",
            load: function(data){
                LDManager.callForCreateResult(data, title);
            },
            error: function(){
                LDManager.callForCreateResult();
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Abre el diseño creado o informa del motivo de error en su creación
     * @param result Información sobre la creación del diseño
     * @param title Título dado al diseño
     */
    callForCreateResult: function(result, title){
        if (result && result.ok) {
            this.openLD(result.docid, title);
        }
        else 
            if (result && result.notLoggedIn) {
                dojo.byId("NewDesignPaneErrorReport").innerHTML = i18n.get("index.error.noLogin");
            }
        else
            if (result && result.noInsert) {
                dojo.byId("NewDesignPaneErrorReport").innerHTML = i18n.get("index.error.noCreate");
            }
        else {
            dojo.byId("NewDesignPaneErrorReport").innerHTML = i18n.get("common.error.noServer");
        }
        this.updateDisplay();
    },
    
    /**
     * Actualiza la pantalla para el usuario
     * @param username Usuario del que se muestran los diseños e instancias
     */
    updateDisplayForUser: function(username){
        if (username == null) {
            dijit.byId("LoginMenuTabs").selectChild("LoginMenuTabsLogin");
            this.showUserPanes(false);
        }
        else {
            this.showUserPanes(true);
            dojo.byId("UserName").innerHTML = username;
            dijit.byId("LoginMenuTabs").selectChild("LoginMenuTabsLogged");
        }
    },
    
    /**
     * Muestra los paneles del usuario de nuevo diseño y de sus diseños
     * @param show Indicador de si se deben mostrar o no
     */
    showUserPanes: function(show){       
        if (show) {
            dojo.byId("NewLdRow").style.display = "";
            dojo.byId("MyLds").style.display = "";
        }
        else {
            dojo.byId("NewLdRow").style.display = "none";
            dojo.byId("MyLds").style.display = "none";
        }
    },
    
    /**
     * Llamada para cambiar la visibilidad de un diseño
     * @param id Identificador del diseño
     * @param visibility Visibilidad del diseño
     */
    callForChangeVisibility: function(id, visibility){
        var bindArgs = {
            url: "manager/manageDesigns.php",
            content: {
                task: "setvisibility",
                id: id,
                visibility: visibility
            },
            handleAs: "json",
            load: function(data){
                LDManager.callForChangeVisibilityResult(data);
            },
            error: function(){
                LDManager.callForChangeVisibilityResult();
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Cambia el estado mostrado de visibilidad del diseño
     * @param result Resultado de la llamada para cambiar la visibilidad
     */
    callForChangeVisibilityResult: function(result){
        if (result && result.ok) {
            dijit.byId("visibility_" + result.docid).set('label', result.visibility);
        }
    },    
    /* editing functions */
    openLDOld: function(id){
        document.openForm.ldid.value = id ? id : "";
        document.openForm.timestamp.value = new Date().getTime();
        document.openForm.submit();
    },

    /**
     * Abre un diseño
     * @param id Identificador del diseño a abrir
     */
    openLD: function(id, title) {
        window.open("main.php?ldid=" + id, id);
    },
    
    /**
     * Solicitud para cambiar la visibilidad de un diseño
     * @param visibility Visibilidad solicitada para el diseño
     */
    changedVisibility: function(visibility){
        this.callForChangeVisibility(this.currentVisibilityId, visibility);
    },

    resetCloneDesign: function(){       
        dojo.byId("ClonarDisenoError").innerHTML="";
        dijit.byId("clonarDisenoTitulo").setValue("");
    },

    /**
     * Abre el diálogo de solicitud de clonación de un diseño
     * @param docid Identificador del diseño a abrir
     */
    abrirDialogoClonarDiseno: function(docid){
        this.resetCloneDesign();
        dijit.byId("ClonarDiseno").show();
        //dojo.style(dijit.byId("ClonarDiseno"),"opacity",100);
        dijit.byId("clonarDisenoDocID").setValue(docid);
    },
    
    /**
     * Comprueba el nombre que se dará a la copia del diseño y si es correcto solicita su creación
     */
    comprobarClonarDiseno: function(){
        var data = dijit.byId("ClonarDiseno").getValues();
        if (data.titulo.replace(/^\s+/g,'').replace(/\s+$/g,'').length == 0) {
            dojo.byId("ClonarDisenoError").innerHTML = i18n.get("index.error.noName");
        }
        else {
            this.llamadaClonarDiseno(data.titulo, data.docid);
        }
    },
    
    /**
     * Llamada para clonar un diseño
     * @titulo Nombre que se dará a la copia del diseño
     * @docid Identificador del documento del que se hace la copia
     */
    llamadaClonarDiseno: function(titulo, docid){
        var bindArgs = {
            url: "manager/manageDesigns.php",
            content: {
                task: "cloneDesign",
                title: titulo,
                id: docid
            },
            handleAs: "json",
            load: function(data){
                LDManager.llamadaClonarDisenoResultado(data);
            }
        };
        dojo.xhrPost(bindArgs);
    },

    /**
     * Actualiza la pantalla en función del resultado de la llamada a clonar el diseño
     * @param result Información sobre el resultado de la llamada a clonar el diseño
     */
    llamadaClonarDisenoResultado: function(result){
        if (result && result.ok) {
            LDManager.updateDisplay();
        }
        else
        {
            var error = i18n.get("common.error.noServer");
        }

        if (error) {
            dojo.byId("ClonarDisenoError").innerHTML = error;
        }
        else {
            this.cerrarDialogoClonarDiseno();
        }
    },

    /**
     * Cierra la ventana de solicitud de clonación del diseño
     */
    cerrarDialogoClonarDiseno: function(){
        dijit.byId("ClonarDiseno").hide();
    }
};

/**
 * Asocia los eventos a los diseños
 * @param id Identificador del elemento
 * @param name Nombre del elemento
 * @param visibility Visibilidad del elemento
 */
var LDListener = function(id, name, visibility){
    this.id = id;
    dojo.connect(name, "onclick", this, "open");
    if (visibility) {
        dojo.connect(visibility, "onclick", this, "visibility");
    }
};

/**
 * Abre el diseño
 */
LDListener.prototype.open = function(){
    LDManager.openLD(this.id);
};


LDListener.prototype.visibility = function(){
    LDManager.currentVisibilityId = this.id;
};


/**
 * Gestor de las instancias del diseño
 */
var DesignVersionsInstances = {
    
    
    /**
     * Función de inicialización
     */
    init: function(){
        dojo.connect(dojo.byId("confirmacionEliminarDisenoCancel"), "onclick", DesignVersionsInstances, "cerrarConfirmacionEliminar");
        dojo.connect(dijit.byId("confirmacionEliminarDisenoOk"), "onClick", function(){
            DesignVersionsInstances.eliminaDiseno();
        });
    },
    
    /**
     * Actualiza la pantalla
     */
    updateDisplay: function() {
        DesignVersionsInstances.generateInstancesTree();
    },

    /**
     * Abre la ventana de confirmación de eliminación un diseño
     * @param docid Identificador del diseño que se desea eliminar
     * @param title Nombre del diseño
     */
    llamadaeliminaDiseno: function(docid, title){
        DesignVersionsInstances.abrirConfirmacionEliminar(docid);
        dijit.byId("ConfirmacionEliminarDiseno").set("title", title);
    },

    /**
     * Llamada para eliminar un diseño
     * @param docid Identificador del diseño que se desea eliminar
     */
    eliminaDiseno: function(){
        var docid = dijit.byId("eliminarDisenoDocID").getValue();
        var bindArgs = {
            url: "manager/manageDesigns.php",
            content: {
                task: "deleteDesign",
                docid: docid
            },
            handleAs: "json",
            load: function(data){
                LDManager.updateDisplay();
            }
        };
        this.cerrarConfirmacionEliminar();
        dojo.xhrPost(bindArgs);
    },
    /**
     * Abrir Ventana Confirmación de Eliminación de un diseño
     */
    abrirConfirmacionEliminar: function(docid){
        dijit.byId("ConfirmacionEliminarDiseno").show();
        dijit.byId("eliminarDisenoDocID").setValue(docid);
    },
    /**
     * Cerrar Ventana Confirmación de Eliminación de un diseño
     */
    cerrarConfirmacionEliminar: function(){
        dijit.byId("ConfirmacionEliminarDiseno").hide();
    },

    /**
     * Llamada para clonar un diseño
     * @param docid Identificador del diseño a clonar
     */
    clonaDiseno: function(docid){
        var bindArgs = {
            url: "manager/manageDesigns.php",
            content: {
                task: "clonarDiseno",
                docid: docid
            },
            handleAs: "json",
            load: function(data){
                LDManager.updateDisplay();
            }
        };
        dojo.xhrPost(bindArgs);
    },

    /**
     * Genera el árbol de diseños e instancias
     */
    generateInstancesTree: function() {
        for (var i in LDManager.documents){
            var label= dojo.byId("label_"+LDManager.documents[i].docid);
        }
    },

    /**
     * Obtiene el menú asociado al elemento
     * @param data
     */
    getResourceMenu: function(data) {
        var items = new Array();
        items.push({
            label: i18n.get("disenos.editar"),
            icon: "edit",
            onClick: function(data) {
                LDManager.openLD(data.res.docid);
            },
            data: data,
            help: i18n.get("disenos.editar.info")
        });
        items.push({
            isSeparator: true
        });  
        items.push({
            label: i18n.get("disenos.eliminar"),
            icon: "delete",
            onClick: function(data) {
                DesignVersionsInstances.llamadaeliminaDiseno(data.res.docid, data.res.title);
            },
            data: data,
            help: i18n.get("disenos.eliminar.info")
        });
        items.push({
            isSeparator: true
        });
        items.push({
            label: i18n.get("disenos.clonar"),
            icon: "copy",
            onClick: function(data) {
                LDManager.abrirDialogoClonarDiseno(data.res.docid);
            },
            data: data,
            help: i18n.get("disenos.clonar.info")
        });      
        return items;
    }
};

dojo.addOnLoad(init);
