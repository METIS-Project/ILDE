/**
 * 
 */

var i18n = {

	init: function(){
		this.initDefault();
		this.internationalizeHtml();
		
		$("#earthEn").click(function(){
			i18n.changeLanguage("en");
			i18n.internationalizeHtml();
		});
		$("#earthEs").click(function(){
			i18n.changeLanguage("es");
			i18n.internationalizeHtml();
		});
	},
	
	initDefault: function(){
		jQuery.i18n.properties({
		    name:'Messages', 
		    path:'i18n/', 
		    mode:'map',
		    language: 'en',
		    callback: function() {
		        // Accessing a simple value through the map
		        //alert(jQuery.i18n.prop('disconnect'));
		    }
		});
	},
	
	changeLanguage: function(language){
		if (language=="en" || language=="es"){
			jQuery.i18n.properties({
			    name:'Messages', 
			    path:'i18n/', 
			    mode:'map',
			    language: language, 
			    callback: function() {
			        // Accessing a simple value through the map
			        //alert(jQuery.i18n.prop('disconnect'));
			    }
			});
		}else{
			this.initDefault();
		}
	},
	
	internationalizeHtml: function(){
		$("title").first().html($.i18n.prop('adapter.title'));
		$("#adapterName").html($.i18n.prop('adapter.name'));
		$("#username").prop("placeholder",$.i18n.prop('username'));
		$("#entrar").html($.i18n.prop('connect'));
		$("#desconectar").html($.i18n.prop('disconnect'));
		$("#welcomeSpan").html($.i18n.prop('welcome'));
		$("#courseSpan").html($.i18n.prop('course'));
		$("#toggleEarthMaps").html($.i18n.prop('change.map'));
		$("#targetEarth").prop("placeholder",$.i18n.prop('search.place'));
		$("#searchTargetEarth").html($.i18n.prop('go.place'));
		$("#target").prop("placeholder",$.i18n.prop('search.place'));
	}
}
