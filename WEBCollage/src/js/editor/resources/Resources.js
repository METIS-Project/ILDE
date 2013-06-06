/**
 * Encargado de la pestaña de los recursos
 */
var Resources = {
	
    /**
    * Encargado de la parte gráfica de los recursos
    */
    updateDisplay: function() {
        Resources.generateResourceTable();
        //We only show the new tool button if a class and LMS has been provided by ldshake
        if (Loader.ldShakeMode==true && (DesignInstance.data.classObj.id =="" || DesignInstance.data.lmsObj.id=="")){
            dijit.byId("buttonNewTool").domNode.style.display="none";
            dojo.byId("toolsLi").style.display="none";
        }
    },

    /**
    * Añade a la tabla los recursos
    * @param list Lista de recursos a mostrar
    * @param subtype Tipo del recurso
    * @param extraField Campo adicional a mostrar
    * @param where Donde se dibuja la tabla
    */
    generateItemTable: function(list, subtype, where) {
        for (var i in list) {
            var res = list[i];
            if (res.subtype == subtype) {
                var row = where.appendChild(document.createElement("tr"));
				
                var name = row.appendChild(document.createElement("td"));
                name.innerHTML = res.title;
				
                var extra = row.appendChild(document.createElement("td"));
                extra.innerHTML = res.getDescription();

                MenuManager.registerThing(row, {
                    getItems: function(data) {
                        return Resources.getResourceMenu(data);
                    },
                    data: {
                        res: res
                    },
                    menuStyle: "default"
                });

            /* now let's add the related activities
	 
	 var activities = LearningDesign.activitiesAndLOsMatches.getMatchingObjectsFor(lo.id);
	 for (var j in activities) {
	 var item = document.createElement("span");
	 item.innerHTML = activities[j].title;
	 item.className = "activityLOListItem";
	 li.appendChild(item);
	 } */
            }
        }		
    },
    
        /**
    * Obtiene el menú del recurso
    * @param data Información del recurso
    */
    getResourceMenu: function(data) {
        var isTool = data.res.subtype == "tool";
        var items = new Array();

        items.push({
            label: i18n.get(isTool ? "resources.editTool" : "resources.editDoc"),
            icon: "editlo",
            onClick: function(data) {
                ResourceManager.edit(data.res);
            },
            data: data,
            help: i18n.get(isTool ? "help.resources.editTool" : "help.resources.editDocument")
        });
        items.push({
            isSeparator: true
        });
        items.push({
            label: i18n.get("resources.delete"),
            icon: "delete",
            onClick: function(data) {
                ResourceManager.openDeleteResourceDialog(data.res);
            },
            data: data,
            help: i18n.get("help.resources.delete")
        });

        return items;
    },
	
    /**
    * Generate resource table
    */
    generateResourceTable: function() {
        var table = dojo.byId("LDResourcesContent");
        
        while (table.childNodes.length >= 1) {
            table.removeChild(table.firstChild);
        }
		
        var mainList = table.appendChild(document.createElement("ul"));
		
        var documents = mainList.appendChild(document.createElement("li"));
        documents.className = "resourceTable";
        documents.innerHTML = i18n.get("resources.inActivity.docs");

        var documentTable = documents.appendChild(document.createElement("table"));
        documentTable.className = "resourceTable";
		
        this.generateItemTable(LearningDesign.data.resources, "doc", documentTable);

        var tools = mainList.appendChild(document.createElement("li"));
        tools.id = "toolsLi";
        tools.className = "resourceTable";
        tools.innerHTML = i18n.get("resources.inActivity.tools");
        var toolTable = tools.appendChild(document.createElement("table"));
        toolTable.className = "resourceTable";
        this.generateItemTable(LearningDesign.data.resources, "tool", toolTable);
    }
};
