/**
 * 
 */

var DeploymentPainter = {
		
	init: function(){
		this.onlyResources = true;
		this.onlyGroups = false;
		this.showLeyend = true;
		dojo.byId("showOnlyResources").setAttribute("src", "images/icons/window.png");
		dojo.byId("showOnlyGroups").setAttribute("src", "images/icons/window.png");
		dojo.byId("leyendTitle").innerHTML=i18n.get("leyendTitle");
    	this.showLeyendManager();
    	this.showGroupsManager();
	},
	
	paint: function(){
		//this.init();
    	var nodeCentral = document.getElementById("centralCP");
    	var previousScrollTop = nodeCentral.scrollTop;
    	var previousScrollLeft = nodeCentral.scrollLeft;
    	
		InformationPainter.paint();
		ResourcesPainter.paint();
		GroupsPainter.paint();
		ActivityPainter.paint();
		DeployButtomPainter.paint();
		
    	nodeCentral.scrollTop = previousScrollTop;
    	nodeCentral.scrollLeft = previousScrollLeft;
	},
	
	/**
	 *  Si el despliegue está completo muestra un mensaje informativo de que se está editando un despliegue ya completado
	 */
	showInformationCompleted: function(){
		if (JsonDB.deploy.staticDeployURL || JsonDB.deploy.liveDeployURL)
		{
			//LP: we delete this warning so that we can do the periodic reload of the deploy
			InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("editCompletedDeployment"));
		}
	},
	
	showResourcesManager: function(){
		if (this.onlyResources==false && this.onlyGroups == false)
		{
			this.showOnlyResources();
			this.onlyResources = true;
		}
		else{
			this.showResourcesAndGroups();
			this.onlyResources = false;
		}
		this.onlyGroups = false;
	},
	
	showGroupsManager: function(){
		if (this.onlyResources==false && this.onlyGroups == false)
		{
			this.showOnlyGroups();
			this.onlyGroups = true;
		}
		else{
			this.showResourcesAndGroups();
			this.onlyGroups = false;
		}
		this.onlyResources = false;
	},
	
	showLeyendManager: function(){
		if (this.showLeyend==true)
		{
			this.showLeyend = false;
			var leyendContainer = dojo.byId("leyendContainer");
			leyendContainer.style.height = "35px";
			dojo.byId("showLeyend").setAttribute("src", "images/icons/window.png");
			dojo.byId("showLeyend").setAttribute("title", i18n.get("maximizeLeyend"));
		}
		else{
			this.showLeyend = true;
			var leyendContainer = dojo.byId("leyendContainer");
			leyendContainer.style.height = "";
			dojo.byId("showLeyend").setAttribute("src", "images/icons/minimize.png");
			dojo.byId("showLeyend").setAttribute("title", i18n.get("minimizeLeyend"));
		}
		var cbContainer = dojo.byId("centralBorderContainer");
		cbContainer.style.top = parseInt(leyendContainer.offsetHeight + 5) + "px";
		var splitters = $('#dijit_layout__Splitter_1');
		for (var i=0; i<splitters.length; i++)
		{
			splitters[i].style.top = parseInt(leyendContainer.offsetHeight) + "px";
		}
	},
	
	showOnlyResources: function(){
		var cbContainer = dojo.byId("centralBorderContainer");
		var cpGroups = dojo.byId("groupsContentPane");
		var cpResources = dojo.byId("centralContentPane");
		cpResources.style.height= parseInt(cbContainer.offsetHeight - 138) + "px";
		cpGroups.style.top = parseInt(cpResources.offsetHeight + 5) + "px";
		var splitters = $('#dijit_layout__Splitter_2');
		for (var i=0; i<splitters.length; i++)
		{
			splitters[i].style.top = parseInt(cpResources.offsetHeight) + "px";
		}
		dojo.byId("showOnlyResources").setAttribute("src", "images/icons/restore.png");
		dojo.byId("showOnlyResources").setAttribute("title", i18n.get("restore"));
		dojo.byId("showOnlyGroups").setAttribute("src", "images/icons/restore.png");
		dojo.byId("showOnlyGroups").setAttribute("title", i18n.get("restore"));
	},
	
	showOnlyGroups: function(){
		var cpGroups = dojo.byId("groupsContentPane");
		var cpResources = dojo.byId("centralContentPane");
		cpResources.style.height=35 + "px";
		
		cpGroups.style.top = parseInt(cpResources.offsetHeight + 5) + "px";
		var splitters = $('#dijit_layout__Splitter_2');
		for (var i=0; i<splitters.length; i++)
		{
			splitters[i].style.top = parseInt(cpResources.offsetHeight) + "px";
		}
		dojo.byId("showOnlyResources").setAttribute("src", "images/icons/restore.png");
		dojo.byId("showOnlyResources").setAttribute("title", i18n.get("restore"));
		dojo.byId("showOnlyGroups").setAttribute("src", "images/icons/restore.png");
		dojo.byId("showOnlyGroups").setAttribute("title", i18n.get("restore"));
	},
	
	
	showResourcesAndGroups: function(){
		var cbContainer = dojo.byId("centralBorderContainer");
		var cpGroups = dojo.byId("groupsContentPane");
		var cpResources = dojo.byId("centralContentPane");
		cpResources.style.height= parseInt(cbContainer.offsetHeight/2 -2.5) + "px";
		var divResources = dojo.byId("divResources");
		var resourceTitle = dojo.byId("resourceTitle");
		
		cpGroups.style.top = parseInt(cpResources.offsetHeight + 5) + "px";
		var splitters = $('#dijit_layout__Splitter_2');
		for (var i=0; i<splitters.length; i++)
		{
			splitters[i].style.top = parseInt(cpResources.offsetHeight) + "px";
		}
		dojo.byId("showOnlyResources").setAttribute("src", "images/icons/window.png");
		dojo.byId("showOnlyResources").setAttribute("title", i18n.get("maximize"));
		dojo.byId("showOnlyGroups").setAttribute("src", "images/icons/window.png");
		dojo.byId("showOnlyGroups").setAttribute("title", i18n.get("maximize"));
	}
}