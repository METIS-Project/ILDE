/*
 1.-La primera clase que se carga es la de glueps.js. Que es la encarga de mostrar la primera pantalla. En esta archivo se encuentran las funciones de inter
 nacionalizaci�n.
 2.-Desde la pantalla inicial. Una vez creado un nuevo dise�o nos iremos a pantalla 2. Cas� toda la funcionalidad esta pantalla est� implementada en el
 archivo Deploy.js y design.js.
      En design.js destaca la funci�n "construirListadoDisenos" encargada de crear listado de dise�os de pantalla 1 y a�adir bot�n nuevo despliegue
      En Deploy.js destaca la funci�n "addListDeploys" encargada de mostrar listado de despliegues de cada dise�o a�adir el Aqu� y poner bot�n editar
         para cada despliegue
*/

dojo.require("dijit.Dialog");
dojo.require("dijit.TitlePane");
dojo.require("dijit.Tooltip");
dojo.require("dijit.TooltipDialog");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.CheckBox");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dijit.form.ValidationTextBox");
dojo.require("dijit.form.Form");

dojo.require("dojo.parser");
dojo.require("dijit.layout.StackContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dojo.fx.easing");
dojo.require("dojo.string");
dojo.registerModulePath("glueps.dojoi18n", "../../../dojoi18n");
//dojo.requireLocalization("glueps.dojoi18n", "glueps");

function init(){

	dojo.connect(dojo.byId("loginImage"), "onclick", function() {
		//Obtener dise�os previa solicitud de usuario y contrase�a
		Design.getJsonDesigns();			
	});
	
	dojo.connect(dojo.byId("exitImage"), "onclick", function() {
		var baseUrl = window.location.href.split("/GLUEPSManager")[0];
		var bindArgs = {
				url : baseUrl + "/GLUEPSManager/logout",
			    headers: {
			        "Content-Type": "text/plain",
			        "Authorization": "Basic bG9nb3V0OmxvZ291dA==" //logout:logout codificado directamente en base64.
			      },
				load: function(data)
				{
					IndexInformativeDialogs.showLogoutDialog(i18n.get("logout"), i18n.get("logoutError"));
				},
				error : function(error, ioargs) {
					var baseUrl = window.location.href.split("/GLUEPSManager")[0];
					var bindArgs = {
							url : baseUrl + "/GLUEPSManager/logout",
						    user: "logout",
						    password: "logout",
							load: function(data)
							{
								IndexInformativeDialogs.showLogoutDialog(i18n.get("logout"), i18n.get("logoutError"));
							},
							error : function(error, ioargs) {
								IndexInformativeDialogs.showLogoutDialog(i18n.get("logout"), i18n.get("logoutOk"));	
							}
					}
					var xhr = dojo.xhrGet(bindArgs);						
				}
		}
		var xhr = dojo.xhrGet(bindArgs);
			
	});
	
    ImportDeploy.init();
	Glueps.init();
    Deploy.init(); 
    Design.init();
    LE.init();
    MenuManager.init();
    DesignMenu.init();
    DeployMenu.init();
    LeUserMenu.init();
    CreateLeDialog.init();
    EditLeDialog.init();
    IndexInformativeDialogs.init();
    VleManagement.init();
    MessageManager.init();
    CloneDeployDialog.init();
}


function gup (name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(window.location.href);
	if (results == null)
		return "";
	else
		return results[1];
}

function gupUrl (url, name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(url);
	if (results == null)
		return "";
	else
		return results[1];
}

dojo.addOnLoad(init);
