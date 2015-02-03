/**
 * Encargado de cargar y guardar los diseños del usuario
 */

/*global dojo, dijit, window, hex_sha1, hex_md5, console, i18n, IDPool, Inheritance, Factory */
/*global LearningFlow, LearningDesign, DesignInstance, Act, Activity, LearningObjective, Role, Resource */
/*global AssessmentFlow, AssessmentPattern, AssessmentFunction, Participant, GroupInstance, Group, JigsawGroupPattern */
/*global UndoManager, ClipManager, ChangeManager*/

var Loader = {
    
    ldShakeMode: false,
   
    ldShakeInfo: {
        document_id: "",
        sectoken: ""
    },
    
    resetLdShakeInfo: function(){
        Loader.ldShakeInfo.document_id = "";
        Loader.ldShakeInfo.sectoken = "";
    },

    /**
     * Últimas acciones realizadas por el usuario
     */
    lastActions : null,

    /**
     * Función de inicialización
     */
    init : function() {
        dojo.connect(dojo.byId("toolbar.save"), "onclick", function() {
            Loader.save("! " + Loader.lastActions);
        });
        
        Loader.resetLdShakeInfo();
        //Loader.ldShakeMode = false;
        if (dojo.body().attributes.document_id.value.length > 0 && dojo.body().attributes.sectoken.value.length > 0){
            Loader.ldShakeMode = true;
            Loader.ldShakeInfo.document_id = dojo.body().attributes.document_id.value;
            Loader.ldShakeInfo.sectoken = dojo.body().attributes.sectoken.value;
        }
    },
    
    inheritance : {
        "act" : Act,
        "clfpact" : Act,
        "activity" : Activity,
        "lo" : LearningObjective,
        "role" : Role,
        "resource" : Resource,
        "assessmentflow" : AssessmentFlow,
        "assessmentpattern" : AssessmentPattern,
        "assessmentfunction" : AssessmentFunction,
        "participant" : Participant,
        "groupInstance" : GroupInstance,
        "group" : Group
    },
    
        /**
     * Carga de un diseño del usuario
     */
    loadLdshakeDesign : function() {
        var bindArgs = {
            url : "ldshake/api/manageLdshakeDesigns.php",
            content : {
                task : "loadLdshakeDesign",
                document_id : Loader.ldShakeInfo.document_id, //Identificador LdShake del diseño a cargar
                sectoken: Loader.ldShakeInfo.sectoken //Token used to make sure it is the owner of the document
            },
            handleAs : "json",
            contentType : "application/json; charset=utf-8",
            load : function(data) {
                Loader.gotcha(data);
                UndoManager.reset();
                Loader.hideLoadingDialog();
            },
            error : function(error) {
                console.log(error.message);
                console.log(error.stack);
                Loader.hideLoadingDialog();
//                alert(i18n.get("loader.load.error"));
            }
        };
        this.showLoadingDialog();
        dojo.xhrGet(bindArgs);
    },

    /**
     * Carga de un diseño del usuario
     */
    load : function() {
        if (!Loader.ldShakeMode){
            var bindArgs = {
                url : "manager/manageDesigns.php",
                content : {
                    task : "load",
                    id : LearningDesign.ldid //Identificador del diseño a cargar
                },
                handleAs : "json",
                contentType : "application/json; charset=utf-8",
                load : function(data) {
                    Loader.gotcha(data);
                    UndoManager.reset();
                    Loader.hideLoadingDialog();
                },
                error : function(error) {
                    console.log(error.message);
                    console.log(error.stack);
                    Loader.hideLoadingDialog();
    //                alert(i18n.get("loader.load.error"));
                }
            };
            this.showLoadingDialog();
            dojo.xhrGet(bindArgs);
        }else{
            Loader.loadLdshakeDesign();
        }
    },
    updateToLastVersion : function(data) {

    },
    
    /**
     * Deshace el último cambio cargando la versión anterior del diseño
     */
    undo : function() {
        if (!Loader.ldShakeMode){
            var bindArgs = {
                url : "manager/manageDesigns.php",
                content : {
                    task : 'undo',
                    id : LearningDesign.ldid
                },
                handleAs : "json",
                contentType : "application/json; charset=utf-8",
                load : function(data) {
                    UndoManager.undone();
                    Loader.gotcha(data);
                    Loader.hideLoadingDialog();
                },
                error : function(error) {
                    console.log(error);
                    Loader.hideLoadingDialog();
                    alert(i18n.get("loader.load.error"));
                }
            };
            this.showLoadingDialog();
            dojo.xhrGet(bindArgs);
        }
        else{
            Loader.undoLdshakeDesign();           
        }
    },
    
        /**
     * Deshace el último cambio cargando la versión anterior del diseño
     */
    undoLdshakeDesign : function() {
        var bindArgs = {
            url : "ldshake/api/manageLdshakeDesigns.php",
            content : {
                task : 'undoLdshakeDesign',
                document_id : Loader.ldShakeInfo.document_id, //Identificador LdShake del diseño a cargar
                sectoken: Loader.ldShakeInfo.sectoken //Token used to make sure it is the owner of the document
            },
            handleAs : "json",
            contentType : "application/json; charset=utf-8",
            load : function(data) {
                UndoManager.undone();
                Loader.gotcha(data);
                Loader.hideLoadingDialog();
            },
            error : function(error) {
                console.log(error);
                Loader.hideLoadingDialog();
                alert(i18n.get("loader.load.error"));
            }
        };
        this.showLoadingDialog();
        dojo.xhrGet(bindArgs);
    },
    
    
    /**
     * Rehace el último cambio
     */
    redo : function() {
        if (!Loader.ldShakeMode){
            var bindArgs = {
                url : "manager/manageDesigns.php",
                content : {
                    task : 'redo',
                    id : LearningDesign.ldid
                },
                handleAs : "json",
                contentType : "application/json; charset=utf-8",
                load : function(data) {
                    UndoManager.redone();
                    Loader.gotcha(data);
                    Loader.hideLoadingDialog();
                },
                error : function(error) {
                    console.log(error);
                    Loader.hideLoadingDialog();
                    alert(i18n.get("loader.load.error"));
                }
            };
            this.showLoadingDialog();
            dojo.xhrGet(bindArgs);
        }
        else{
            Loader.redoLdshakeDesign();
        }
    },
    
        /**
     * Rehace el último cambio
     */
    redoLdshakeDesign : function() {
        var bindArgs = {
            url : "ldshake/api/manageLdshakeDesigns.php",
            content : {
                task : 'redoLdshakeDesign',
                document_id : Loader.ldShakeInfo.document_id, //Identificador LdShake del diseño a cargar
                sectoken: Loader.ldShakeInfo.sectoken //Token used to make sure it is the owner of the document
            },
            handleAs : "json",
            contentType : "application/json; charset=utf-8",
            load : function(data) {
                UndoManager.redone();
                Loader.gotcha(data);
                Loader.hideLoadingDialog();
            },
            error : function(error) {
                console.log(error);
                Loader.hideLoadingDialog();
                alert(i18n.get("loader.load.error"));
            }
        };
        this.showLoadingDialog();
        dojo.xhrGet(bindArgs);
    },
    
    
    /**
     * Guarda un diseño del usuario
     * @param lastActions Últimas acciones realizadas por el usuario
     */
    save : function(lastActions) {
        if (!Loader.ldShakeMode){
            this.lastActions = lastActions;
            if(LearningDesign.ldid.length > 0) {
                DesignInstance.data.instanceid = LearningFlow.instanceid;
                var bindArgs = {
                    url : "manager/manageDesigns.php",
                    handleAs : "json",
                    content : {
                        task : 'save',
                        id : LearningDesign.ldid,
                        design : JSON.encode(LearningDesign.data),
                        instance : JSON.encode(DesignInstance.data),
                        todoDesign : JSON.encode(ClipManager.getTodo()),
                        todoInstance : "",
                        actions : lastActions
                    },
                    load : function(data) {
                        Loader.savedOk(data);
                    },
                    error : function(error) {
                        Loader.savedBad(error);
                    }
                };

                dojo.xhrPost(bindArgs);
                this.setSaveState("saving");
            }
        }else{
           Loader.saveLdshakeDesign(lastActions);
        }
    },
    
     /**
     * Guarda un diseño del usuario
     * @param lastActions Últimas acciones realizadas por el usuario
     */
    saveLdshakeDesign : function(lastActions) {
        this.lastActions = lastActions;
        if(LearningDesign.ldid.length > 0) {
            DesignInstance.data.instanceid = LearningFlow.instanceid;
            var bindArgs = {
                url : "ldshake/api/manageLdshakeDesigns.php",
                handleAs : "json",
                content : {
                    task : 'saveLdshakeDesign',
                    document_id : Loader.ldShakeInfo.document_id, //Identificador LdShake del diseño a cargar
                    sectoken: Loader.ldShakeInfo.sectoken, //Token used to make sure it is the owner of the document
                    design : JSON.encode(LearningDesign.data),
                    instance : JSON.encode(DesignInstance.data),
                    todoDesign : JSON.encode(ClipManager.getTodo()),
                    todoInstance : "",
                    actions : lastActions
                },
                load : function(data) {
                    Loader.savedOk(data);
                },
                error : function(error) {
                    Loader.savedBad(error);
                }
            };

            dojo.xhrPost(bindArgs);
            this.setSaveState("saving");
        }
    },
    
    /**
     * Acciones a realizar si el diseño se ha guardado correctamente
     * @param data Información sobre el resultado de la operación de almacenamiento
     */
    savedOk : function(data) {
        if(data.ok) {
            Loader.setSaveState("saved");
            UndoManager.newEditionAction();
            ChangeManager.updateDisplays();
        } else {
            this.savedBad(data);
        }
    },
    /**
     * Acciones a realizar si el diseño no se ha podido guardar
     * @param error Información sobre el error producido
     */
    savedBad : function(error) {
        Loader.setSaveState("save");
        console.log(error.message);
        console.log(error.stack);
        if(error.notLoggedIn) {
            //Informar de sesión expirada
            //Ofrecer la posibilidad de iniciar sesión y guardar
            /*var mensaje = i18n.get("loader.save.sessionExpired");
             if (confirm(mensaje)){
             Loader.openLoginDialog();
             }
             else{
             //Recargar la ventana de la que procede
             window.opener.location.reload();
             //Cerrar la ventana
             window.close();
             }*/
            Loader.openExpiredSessionDialog();
        } else {
            //Usuario sin acceso
            if(error.noAccess) {
                //Informar de que no tiene acceso al diseño
                alert(i18n.get("loader.save.noAccess"));
                LearningDesign.clear();
                //Recargar la página de la que procede
                window.opener.location.reload();
                //Cerrar la instancia
                window.close();
            } else {
                alert(i18n.get("loader.save.error"));
            }
        }
    },
    /**
     * Muestra la ventana de login
     */
    openLoginDialog : function() {
        Loader.closeExpiredSessionDialog();
        dojo.byId("LoginDialog").childNodes[1].childNodes[3].style.display = "none";
        dijit.byId("LoginDialog").show();
    },
    openExpiredSessionDialog : function() {
        dijit.byId("expiredSessionDialog").show();
    },
    closeExpiredSessionDialog : function() {
        dijit.byId("expiredSessionDialog").hide();
    },
    cancelExpiredSessionDialog : function() {
        //Recargar la ventana de la que procede
        window.opener.location.reload();
        //Cerrar la ventana
        window.close();
    },
    /**
     * Comprueba si el usuario y contraseña son correctos
     */
    doLogin : function() {
        var data = dijit.byId("LoginDialog").getValues();

        var bindArgs = {
            url : "manager/users.php",
            content : {
                task : "login",
                username : data.username,
                password : hex_sha1(hex_md5(data.password))
            },
            handleAs : "json",
            load : function(data) {
                Loader.doLoginResult(data);
            },
            error : function() {
                Loader.doLoginResult();
            }
        };
        dijit.byId("LoginDialogPassword").set("value", "");
        dojo.xhrPost(bindArgs);
    },
    /**
     * Acciones a realizar en función del resultado del login
     * @param result Información sobre el resultado del login
     */
    doLoginResult : function(result) {
        var error = false;
        var errortext = null;

        if(result && result.ok) {
            //Guardar los cambios
            Loader.save("");
        } else {
            error = true;
            errortext = i18n.get(result && result.badLoginPass ? "users.error.login" : "common.error.noServer");
        }

        if(error) {
            dojo.byId("LoginErrorReport").innerHTML = errortext;
        } else {
            dojo.byId("LoginErrorReport").innerHTML = "";
            this.closeLoginDialog();
        }
    },
    /**
     * Cierra la ventana de login
     */
    closeLoginDialog : function() {
        dijit.byId("LoginDialog").hide();
    },
    /**
     * Acciones a realizar cuando se cancela el login
     */
    cancelLoginDialog : function() {
        dijit.byId("LoginDialog").hide();
        //Resetar datos
        LearningDesign.clear();
        ClipManager.clear();
        //Recargar la página de la que procede
        window.opener.location.reload();
        //Cerramos el diseño
        window.close();
    },
    /**
     * Actualiza el icono save en función del estado
     * @param state Estado
     */
    setSaveState : function(state) {
        var button = dijit.byId("toolbar.save");
        var labelkey = null;
        switch (state) {
            case "saved":
                labelkey = "common.saved";
                break;
            case "saving":
                labelkey = "common.saving";
                break;
            default:
                labelkey = "common.save";
                break;
        }
        button.set('label', i18n.get(labelkey));
        button.set("disabled", state != "save");
    },
    gotcha : function(data) {
        IDPool.clear();
        if(data.empty) {
            LearningDesign.clear();
            DesignInstance.clear();
            ClipManager.clear();
        } else if(data.noAccess) {
            //Informar de que no tiene acceso al diseño
            alert(i18n.get("loader.load.noAccess"));
            //Resetar datos
            LearningDesign.clear();
            ClipManager.clear();
            
            if (!Loader.ldShakeMode){
                //Recargar la página de la que procede
                if(window.opener) {
                    window.opener.location.reload();
                } else {
                    window.location.assign("index.php");
                }
                //Cerramos el diseño
                window.close();
            }else{
                window.location.assign("blank.php");
            }
            
        } else {
            this.cast(data.design);
            this.cast(data.instance);
            LearningDesign.load(data.design);
            DesignInstance.load(data.instance);
            ClipManager.load(data.todoDesign);
            LearningFlow.setInstanceId(data.instance.instanceid);
        }

        LearningFlow.restart();

        ChangeManager.updateDisplays();
        if(this.onLoad) {
            this.onLoad();
        }

        this.setSaveState("saved");
    },
    cast : function(object) {
        if(object === null) {
            return;
        }

        var baseClass = this.inheritance[object.type];
        var isObject = false;

        if(baseClass) {
            Inheritance.inheritMethods(object, baseClass);
            isObject = true;
        } else if(object.type == "clfp" || object.type == "activityPattern" || object.type == "groupPattern") {
            Inheritance.inheritPatternMethods(object);
            isObject = true;
        }

        if(isObject) {
            IDPool.registerExistingObject(object);
        }

        for(var i in object) {
            if( typeof object[i] == "object") {
                this.cast(object[i]);
            }
        }
    },
    /**
     * Muestra la ventana de cargando diseño
     */
    showLoadingDialog : function() {
        var dlg = dijit.byId("WaitWhileLoadingDialog");
        if(!dlg) {
            dlg = new dijit.Dialog({
                id : "WaitWhileLoadingDialog",
                title : i18n.get("loader.title")
            });

            var content = i18n.get("loader.loading.wait") + "<img src='images/loading.gif'/>";

            dlg.setContent(content);
            dojo.body().appendChild(dlg.domNode);
        }
        dlg.show();
    },
    /**
     * Oculta la ventana de cargando diseño
     */
    hideLoadingDialog : function() {
        dijit.byId("WaitWhileLoadingDialog").hide();
    }
};
