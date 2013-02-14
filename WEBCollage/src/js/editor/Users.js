/**
 * Gestión del login y registro de usuarios
 */

/**
* Usuarios de la aplicación
*/
var Users = {
    /**
     * Longitud mínima del nombre de usuario
     */
    minUserNameLength: 3,
    /**
     * Longitud mínima de la contraseña del usuario
     */
    minPasswordLength: 4,
    
    /**
     * Función de inicialización
     */
    init: function(){
        dojo.connect(dojo.byId("RegisterUser"), "onclick", Users, "openNewUserDialog");
        
        dojo.connect(dojo.byId("RegisterNewUserDialogOk"), "onclick", Users, "checkNewUserData");
        dojo.connect(dojo.byId("RegisterNewUserDialogCancel"), "onclick", Users, "closeNewUserDialog");
        
        dojo.connect(dojo.byId("DoLogin"), "onclick", Users, "openLoginDialog");
        dojo.connect(dojo.byId("DoLogout"), "onclick", Users, "doLogout");
        //Eventos de LoginDialog
        dojo.connect(dojo.byId("LoginDialogOk"), "onclick", Users, "doLogin");
        dojo.connect(dojo.byId("LoginDialogCancel"), "onclick", Users, "closeLoginDialog");
    },
    
    /**
     * Abre la ventana de creación de nuevo usuario
     */  
    openNewUserDialog: function(){
        this.resetNewUser();
        dijit.byId("RegisterNewUserDialog").show();
    },
    
    /**
     * Cierra la ventana de creación del usuario
     */  
    closeNewUserDialog: function(){
        dijit.byId("RegisterNewUserDialog").hide();
    },

    /**
     * Resetea los campos de la ventana de nuevo usuario
     */  
    resetNewUser: function(){       
        dojo.byId("RegisterNewUserErrorReport").innerHTML="";
        dijit.byId("nickname").setValue("");
        dijit.byId("password").setValue("");
        dijit.byId("repeatpasswd").setValue("");
        dijit.byId("mail").setValue("");
    },
    
    /**
     * Comprueba los datos proporcionados por el usuario para su registro
     */  
    checkNewUserData: function(){     
        var data = dijit.byId("RegisterNewUserDialog").getValues();
        
        if (data.username.length < this.minUserNameLength) {
            var error = dojo.string.substitute(i18n.get("users.error.noUser"), [this.minUserNameLength]);
        }
        else 
        if (data.username.indexOf(' ') >= 0) {
            error = i18n.get("users.error.userBlank");
        }
        else
        if (data.password.length < this.minPasswordLength) {
            error = dojo.string.substitute(i18n.get("users.error.noPassword"), [this.minPasswordLength]);
        }
        else
        if (data.password != data.repeatpassword) {
            error = i18n.get("users.error.differentPassword");
        }
        
        if (error) {
            dojo.byId("RegisterNewUserErrorReport").innerHTML = error;
        }
        else {
            dojo.byId("RegisterNewUserErrorReport").innerHTML = "";
            this.callToCreateNewUser(data);
        }
    },
    
    /**
     * Llamada para crear un nuevo usuario
     * @param data Datos del usuario a crear
     */  
    callToCreateNewUser: function(data){
        var bindArgs = {
            url: "manager/users.php",
            content: {
                task: "new",
                username: data.username,
                password: hex_sha1(hex_md5(data.password)),
                email: data.email
            },
            handleAs: "json",
            load: function(data){
                Users.callToCreateNewUserResult(data);
            },
            error: function(){
                Users.callToCreateNewUserResult();
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Cierra la ventana de creación del usuario o informa de si se ha producido un error
     */  
    callToCreateNewUserResult: function(result){
        if (result && result.ok) {
            this.closeNewUserDialog();
            this.openRegisterResultDialog();
        }
        else{ 
            if (result && result.alreadyExists) {
                var error = dojo.string.substitute(i18n.get("users.error.userExists"), [result.username]);
            }
            else {
                error = i18n.get("common.error.noServer");
            }
        }
        
        if (error) {
            dojo.byId("RegisterNewUserErrorReport").innerHTML = error;
        }
        else {
            dojo.byId("RegisterNewUserErrorReport").innerHTML = "";
        }
    },
    
    openRegisterResultDialog: function(){
        var dlg =dijit.byId("registerResult");
        dlg.show();     
    },
    
    closeRegisterResultDialog: function(){
        dijit.byId("registerResult").hide();
    },
        
    /**
     * Abre la ventana de login
     */
    openLoginDialog: function(){
        this.resetLogin();
        dijit.byId("LoginDialog").show();
    },
    
    /**
     * Cierra la ventana de login
     */  
    closeLoginDialog: function(){
        dijit.byId("LoginDialog").hide();
    },
    
    /**
     * Resetea los campos de la ventana de nuevo usuario
     */  
    resetLogin: function(){       
        dojo.byId("LoginErrorReport").innerHTML="";
        dijit.byId("LoginDialogUsername").setValue("");
        dijit.byId("LoginDialogPassword").setValue("");       
    },
	
    /** 
     * Llamada para comprobar si el login es correcto
     */
    doLogin: function(){
        var data = dijit.byId("LoginDialog").getValues();
        
        var bindArgs = {
            url: "manager/users.php",
            content: {
                task: "login",
                username: data.username,
                password: hex_sha1(hex_md5(data.password))
            },
            handleAs: "json",
            load: function(data){
                Users.doLoginResult(data);
            },
            error: function(){
                Users.doLoginResult();
            }
        };
        dijit.byId("LoginDialogPassword").attr("value", "");
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Cierra la ventana de login o informa de si se ha producido un error
     */  
    doLoginResult: function(result){
        if (result && result.ok) {
            this.closeNewUserDialog();
        }
        else 
        if (result && result.badLoginPass) {
            var error = i18n.get("users.error.login");
        }
        else {
            error = i18n.get("common.error.noServer");
        }
        
        if (error) {
            dojo.byId("LoginErrorReport").innerHTML = error;
        }
        else {
            dojo.byId("LoginErrorReport").innerHTML = "";
            this.closeLoginDialog();
            LDManager.updateDisplay();
        }
    },
        
    /**
     * Cierra la sesión del usuario
     */
    doLogout: function(){
        var bindArgs = {
            url: "manager/users.php",
            content: {
                task: "logout"
            },
            handleAs: "json",
            load: function(data){
                Users.doLogoutResult(data);
            },
            error: function(){
                Users.doLogoutResult();
            }
        };
        dojo.xhrPost(bindArgs);
        
    },
    
    /**
     * Resultado de cerrar la sesión
     */
    doLogoutResult: function(result){
        if (result) {
            LDManager.updateDisplay();
        }
    }
}
