var i18n = {
		
		init: function(){
			dojo.connect(dojo.byId("localeSpanish"), "onclick", function() {
				djConfig.locale = "es";
				DeploymentPainter.paint();
			});

			dojo.connect(dojo.byId("localeEnglish"), "onclick", function() {
				djConfig.locale = "en";
				DeploymentPainter.paint();
			});
			
			dojo.connect(dojo.byId("localeCatalan"), "onclick", function() {
				djConfig.locale = "ca";
				DeploymentPainter.paint();
			});
			
			//if (LdShakeManager.ldShakeMode){
				var lang = gup("lang");
				if (lang != "" && (lang=="es" || lang=="en" || lang=="ca")){
					djConfig.locale = lang;
				} 
			//}
		},
		
		i18nStrings: new Object(),
		
		get: function(key){
			return i18n.i18nStrings[djConfig.locale][key];
		}

};






