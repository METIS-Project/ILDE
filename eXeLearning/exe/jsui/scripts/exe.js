// ===========================================================================
// eXe
// Copyright 2012, Pedro Peña Pérez, Open Phoenix IT
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//===========================================================================

if (!window.translations)
    translations = {};

function _(msg) {
    return translations[msg] || msg;
}

Ext.Loader.setConfig({
    enabled: true,
    paths: { 'Ext.ux': 'jsui/extjs/examples/ux' },
    disableCaching: false
});

var conf = {
    onRender: function() {
        var me = this;
        
        me.callParent(arguments);
        if (me.tooltip) {
            Ext.tip.QuickTipManager.register(Ext.apply({
                target: me.el,
                text: me.tooltip
            }));
        }
    }
};

//Call authoring page when zindex is modified and consider problematic plugins with no zindex support
Ext.override(Ext.WindowManager, {
    bringToFront: function(comp) {
        var me = this, authoringPanel = Ext.ComponentQuery.query('#authoring')[0];

        me.callParent(arguments);
        if (authoringPanel.isVisible()) {
            var authoring = authoringPanel.getWin();
	        if (authoring && authoring.hideObjectTags)
	            authoring.hideObjectTags();
        }
    },
    onComponentHide: function(comp) {
        var me = this, authoringPanel = Ext.ComponentQuery.query('#authoring')[0];

        me.callParent(arguments);
        if (authoringPanel.isVisible()) {
            if (!this.getActive()) {
		        var authoring = authoringPanel.getWin();
		        if (authoring && authoring.showObjectTags)
		            authoring.showObjectTags();
            }
        }
    },
    _hideModalMask: function() {
        var me = this, authoringPanel = Ext.ComponentQuery.query('#authoring')[0];

        me.callParent(arguments);
		if (authoringPanel.isVisible()) {
            if (!this.getActive()) {
		        var authoring = authoringPanel.getWin();
		        if (authoring && authoring.showObjectTags)
		            authoring.showObjectTags();
            }
        }
    }
});
Ext.override(Ext.form.field.Text, conf);
Ext.override(Ext.form.field.TextArea, conf);
Ext.override(Ext.form.field.Checkbox, conf);
Ext.override(Ext.form.field.Hidden, conf);

Ext.application({
    name: 'eXe',

    stores: [
        'OutlineXmlTreeStore',
        'IdeviceXmlStore',
        'filepicker.DirectoryTree',
        'filepicker.File'
    ],

    models: [
    	'filepicker.File'
    ],
    
    controllers: [
    	'Idevice',
        'MainTab',
    	'Outline',
    	'Toolbar',
    	'filepicker.Directory',
    	'filepicker.File'
    ],
    
    getMaxHeight: function(height) {
        var vheight = Ext.ComponentQuery.query('#eXeViewport')[0].getHeight();

        return height >= vheight? vheight : height;
    },

    quitWarningEnabled: false,

    gotoUrl: function(location) {
        eXe.app.quitWarningEnabled = false;
        if (location == undefined)
            location = window.location.pathname;
        nevow_closeLive('window.location = "' + location + '";');
    },
    
    launch: function() {
        Ext.QuickTips.init();

        try {
            Ext.state.Manager.setProvider(new Ext.state.LocalStorageProvider());
        }
        catch (err) {
            console.log('Local Storage not supported');
            Ext.state.Manager.setProvider(new Ext.state.CookieProvider({expires: null}));
        }
        
        eXe.app = this;
        
        eXe.app.config = config;
        
        Ext.state.Manager.set('filepicker-currentDir', eXe.app.config.lastDir);

        window.onbeforeunload = function() {
            if (eXe.app.quitWarningEnabled)
                return _("If you leave this page eXe application continues to run." +
                        " Please use the menu File->Quit if you really want to exit the application.");
        };
		/*
		window.onunload = function() {
            nevow_clientToServerEvent('quit', '', '');
        };
		*/
        if (Ext.isGecko || Ext.isSafari)
        	window.addEventListener('keydown', function(e) {(e.keyCode == 27 && e.preventDefault())});

        var cmp1 = Ext.create('eXe.view.ui.eXeViewport', {
            renderTo: Ext.getBody()
        });
        cmp1.show();

        setTimeout(function(){
		    Ext.get('loading').hide();
		    Ext.get('loading-mask').fadeOut();
		  }, 250);
        
        if (eXe.app.config.showPreferences)
        	eXe.app.getController('Toolbar').toolsPreferences();
        
        if (!eXe.app.config.showIdevicesGrouped) {
        	var panel = Ext.ComponentQuery.query('#idevice_panel')[0],
        		button = panel.down('button');
        	
        	panel.view.features[0].disable();
        	button.setText(_('Group iDevices'));
        }
    },

    appFolder: "jsui/app"

});

window.ldshake_post_semaphore = false;

//LdShake mod
function post_submitLink(form) {
    var post, url, error, timeoutEvent;

    if(window.ldshake_post_semaphore)
        return false;

    window.ldshake_post_semaphore = true;

    if(!form)
        return false;

    uploadFormData = new FormData(form);

    url = form.attributes.action.value.split('#')[0];
    error = function() {
        post.abort();
    }

    timeoutEvent = setTimeout(error, 18000);

    post = new XMLHttpRequest();

    post.open("POST", url, true);
    post.responseType = "text";

    post.onreadystatechange = function() {
        if (post.readyState == 4 && post.status >= 200 && post.status <= 299) {
            var data = post.response;
            var formDoc = form.ownerDocument;
            var formWindow = form.win;
            var iw = document.querySelector("iframe");
            var loadlistener = function() {
                var doc = iw.contentWindow.document;

                iw.removeEventListener('load', loadlistener, false);

                data = data.replace('<head>','<head><base href="' + location.href + '/" />');

                doc.open();
                doc.write(data);
                doc.close();
                //iw.srcdoc=data; IE12?

                window.ldshake_post_semaphore = false;
                /*
                if(form.action.value == "done" && !form.rep) {
                    form.rep = true;
                    post_submitLink(form);
                }
                */
            }

            clearTimeout(timeoutEvent);

            iw.addEventListener('load', loadlistener, false);
            //formWindow.location = "about:blank";
            formWindow.location = "/jsui/blank.txt";

        } else if (post.readyState == 4) {
            //retry;
            try{
                clearTimeout(timeoutEvent);
            } catch (e) {}
            post_submitLink(form);
        }
    }

    post.send(uploadFormData);
}

function uploadFile(event, callback, preview) {
    var inputFileElement = event.target;
    var uploadFileObject;
    var uploadFormData;
    var post, url;

    if(!inputFileElement.files)
        return false;

    if(!inputFileElement.files.length)
        return false;


    uploadFormData = new FormData();
    //uploadFileObject = inputFileElement.files[0];
    for(var i=0; i < inputFileElement.files.length; i++) {
        uploadFileObject = inputFileElement.files[i];
        uploadFormData.append("file", uploadFileObject);
        uploadFormData.append("name", uploadFileObject.name);
    }

    if(preview)
        uploadFormData.append("preview", "true");

    url = location.href + "/upload";
    post = new XMLHttpRequest();

    post.open("POST", url, true);
    post.responseType = "json";
    post.onreadystatechange = function() {
        if (post.readyState == 4 && post.status == 200) {
            data = post.response;
            if(typeof data === 'string')//fix IE not supporting json responseType
                data = JSON.parse(data);
            callback(data);
        }
    }

    post.send(uploadFormData);
}