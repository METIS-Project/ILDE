var exe_advalign = {
	check : function(editorID,currentNode,command) {
		if (command=="JustifyLeft" || command=="JustifyCenter" || command=="JustifyRight") {
			exe_advalign.checkBlock(editorID,currentNode,command);
		}
		return false;		
	},
	checkBlock : function(editorID,currentNode,command){
	
		// return true if it's an eXe figure (we do nothing with that value)
		var c = jQuery(currentNode);
		var e = c.parents();
		var ed = tinyMCE.get(editorID);
		var node = ed.selection.getNode();
		if (node.nodeName!="IMG") return false;
		
		e.each(function(){
			if(this.className && this.className.indexOf("exe-figure")==0) {
			
				ed.controlManager.get("justifyleft").setActive(false);
				ed.controlManager.get("justifycenter").setActive(false);
				ed.controlManager.get("justifyright").setActive(false);				
				
				var currK = jQuery(this).attr("class");
				
				if (command=="JustifyLeft") {
					currK = currK.replace(" float-right","");					
					if (currK.indexOf(" position-right")!=-1) {
						currK = currK.replace(" position-right","");	
					} else if (currK.indexOf(" position-center")!=-1) {
						currK = currK.replace(" position-center","");
					} else {									
						if (currK.indexOf(" float-left")!=-1) {
							currK = currK.replace(" float-left","");
						} else {
							currK += " float-left";
						}
					}
				}
				
				else if (command=="JustifyCenter") {
					currK = currK.replace(" float-left","");
					currK = currK.replace(" float-right","");
					currK = currK.replace(" position-right","");
					if (currK.indexOf(" position-center")!=-1) {
						currK = currK.replace(" position-center","");
					} else {
						currK += " position-center";
					}
				}
				
				if (command=="JustifyRight") {
					currK = currK.replace(" position-center","");
					if (currK.indexOf(" float-left")!=-1) {
						currK = currK.replace(" float-left"," float-right");
					} else {					
						if (currK.indexOf(" position-right")!=-1) {
							currK = currK.replace(" float-right","");
							currK = currK.replace(" position-right"," float-right");
						} else {
							currK = currK.replace(" float-right","");
							currK += " position-right";
						}
					}
				}				

				this.className = currK;
				tinyMCE.activeEditor.dom.events.prevent();
				return true;
			}
		});
		
		return false;
	}
};

(function() {
	tinymce.PluginManager.requireLangPack('advalign');
	tinymce.create('tinymce.plugins.AdvAlign', {
		init : function(ed,url) {
			if (ed.settings.execcommand_callback && ed.editorId!="mce_fullscreen") {
				alert(tinyMCE.i18n[tinyMCE.settings.language+".advalign.execcommand_callback_error"]);
				return false;
			} else if (typeof(jQuery)=='undefined') {	
				alert(tinyMCE.i18n[tinyMCE.settings.language+".advalign.jquery_error"]);
				return false;
			}
			ed.settings.execcommand_callback = exe_advalign.check;
		},
		createControl : function(n, cm) {
			return null;
		},
		getInfo : function() {
			return {
				longname : 'Advanced Alignment',
				author : 'José Miguel Andonegi & Ignacio Gros',
				authorurl : 'http://www.ulhi.hezkuntza.net/web/guest/inicio1',
				infourl : '',
				version : "1.0"
			};
		}
	});
	tinymce.PluginManager.add('advalign', tinymce.plugins.AdvAlign);
})();