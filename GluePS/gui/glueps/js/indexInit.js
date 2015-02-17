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
