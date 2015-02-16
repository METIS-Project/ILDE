/**
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Web Collage.

Web Collage is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Web Collage is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

var ExportManager = {
    init: function() {
        dojo.connect(dojo.byId("toolbar.imsld.zip"), "onclick", function() {
            ExportManager.showExportingDialog("imsld");
        });
        dojo.connect(dojo.byId("toolbar.workflow.zip"), "onclick", function() {
            ExportManager.showExportingDialog("workflow");
        });
    },

    showExportingDialog : function(exporttype) {
        var dlg = dijit.byId("ExportingDialog");
        if (!dlg) {
            dlg = new dijit.Dialog( {
                id : "ExportingDialog",
                title : i18n.get("export.message.title")
            });
        }
        dojo.byId("ExportContent").innerHTML = i18n.get("export.waiting") + "<br/><img src='images/loading.gif'/>";
        dlg.show();
        this.load(exporttype);
    },

    load : function(exporttype) {
        var content = {
            id : LearningDesign.ldid,
            type: exporttype
        };
        
        var bindArgs = {
            url : "manager/export.php",
            handleAs: "json",
            content : content,
            contentType : "application/json; charset=utf-8",
            load : function(data) {
                ExportManager.gotcha(data);
            },
            error : function(error) {
                ExportManager.error(error);
            }
        };
        dojo.xhrGet(bindArgs);
    },

    error: function(error) {
        console.log(error);
        dojo.byId("ExportContent").innerHTML = i18n.get("export.error");
    },

    gotcha: function(data) {
        dojo.byId("ExportContent").innerHTML = dojo.string.substitute(i18n.get("export.message"), [data.url]);
    },

    close: function() {
        dijit.byId("ExportingDialog").hide();
    }
};


