// ===========================================================================
// eXe
// Copyright 2004-2005, University of Auckland
// Copyright 2004-2008 eXe Project, http://eXeLearning.org/
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
// ===========================================================================

// This module is for the common Javascript used in all webpages.

// Called upon loading the page this function clears the hidden
// action and object fields so they can be used by submitLink

// An array of js strings to evaluate on document load
var Ext = parent.Ext;
var eXe = parent.eXe;
var onLoadHandlers = [clearHidden, setWmodeToFlash, loadAuthoringPluginObjects, 
	enableAnchors, gotoAnchor, preventEscKey, preventHistoryBack, loadKeymap, hideObjectTags];
var beforeSubmitHandlers = new Array();

// Called on document load
function onLoadHandler() {
    runFuncArray(onLoadHandlers)
}

curr_edits_math_num = 1
// for unique mimetex images from exemath. will reset to #1 w/ each new edit, 
// but will create  unique math# within each edit session to the previews dir.

// Calls function in an array where each 'row' of the array is in the format:
// func
// or
// [func, arg]
function runFuncArray(handlers) {
    for (var i=0; i < handlers.length; i++) {
        var row = handlers[i] 
        if (typeof row=="function")
            row()
        else
            if (row) {
            // row[0] is a function, row[1] are its args
            row[0].apply(this, row[1]);
        }
    }
}

// Asks the user for an image, returns the path or an empty string
/*
function askUserForImage(multiple, fn, filter) {
    var fp, mode;
    if (multiple)
        mode = parent.eXe.view.filepicker.FilePicker.modeOpenMultiple;
    else
        mode = parent.eXe.view.filepicker.FilePicker.modeOpen;
    fp = parent.Ext.create("eXe.view.filepicker.FilePicker", {
        type: mode,
        title: multiple? parent._("Select one or more images") : parent._("Select an image"),
        modal: true,
        scope: this,
        callback: function(fp) {
            if (fp.status == parent.eXe.view.filepicker.FilePicker.returnOk) {
                if (multiple) {
		            var result = new String("");
                    for (f in fp.files) {
		                if (result != "") {
		                    result += "&";
		                }
		                result += escape(fp.files[f].path);
		            }
		            fn(result);
		        } else {
                    fn(fp.file.path);
                }
            }
            else
                fn("");
        }
    });
    fp.appendFilters([
        filter
        ? filter
        : { "typename": parent._("Image Files (.jpg, .jpeg, .png, .gif, .svg)"), "extension": "*.png", "regex": /.*\.(jpg|jpeg|png|gif|svg)$/i },
          { "typename": parent._("All Files"), "extension": "*.*", "regex": /.*$/ }
    ]);
    parent.window.focus();
    fp.show();
}
*/

// LdShake mod
function askUserFor(multiple, fn, filter, accept, preview) {
    var input = document.querySelector("[type=file]");
    var filename;

    if(!preview)
        preview = false;

    var callback = function(event) {
        uploadFile(event, function(data) {
            if(typeof data.filename === 'string')
                fn(data.filename);
            else
                fn(data.filename.join('&'));
        }, preview);
    }

    if(input) {
        if (multiple)
            input.setAttribute('multiple', 'multiple');
        if(accept)
            input.setAttribute('accept', accept);
        input.addEventListener("change", callback, false);
        input.click();
    }
}

function askUserForFile(event, multiple, fn, filter) {
    event.stopPropagation();
    event.preventDefault();
    askUserFor(multiple, fn);
}

function askUserForMedia(event, multiple, fn, filter, preview) {
    event.stopPropagation();
    event.preventDefault();
    askUserFor(multiple, fn, null, 'video/*', preview);
}

function askUserForImage(event, multiple, fn, filter, preview) {
    event.stopPropagation();
    event.preventDefault();
    askUserFor(multiple, fn, null, 'image/*', preview);
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

    url = ldshake_top().location.href + "/upload";
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

/* LdShake mod */
//finds the top iframe
function ldshake_top() {
    var top_iframe = window;
    var up = true;

    while(up) {
        try {
            if(!top_iframe.eXe)
                top_iframe = top_iframe.parent;
            else
                up = false;
        } catch(e) {
            up = false;
        }
    }

    return top_iframe.parent;
}

// Asks the user for a media file, returns the path or an empty string
function askUserForMedia(fn) {
    var fp = parent.Ext.create("eXe.view.filepicker.FilePicker", {
        type: parent.eXe.view.filepicker.FilePicker.modeOpen,
        title: parent._("Select a file"),
        modal: true,
        scope: this,
        callback: function(fp) {
            if (fp.status == parent.eXe.view.filepicker.FilePicker.returnOk)
                fn(fp.file.path);
            else
                fn("");
        }
    });
    fp.appendFilters([
        { "typename": parent._("All Files"), "extension": "*.*", "regex": /.*$/ },
        { "typename": parent._("Flash Movie (.flv)"), "extension": "*.png", "regex": /.*\.flv$/i },
        { "typename": parent._("Flash Object (.swf)"), "extension": "*.png", "regex": /.*\.swf$/i },
        { "typename": parent._("Quicktime Files (.mov, .qt, .mpg, .mp3, .mp4, .mpeg)"), "extension": "*.png", "regex": /.*\.(mov|qt|mpg|mp3|mp4|mpeg)$/i },
        { "typename": parent._("Windows Media Player Files (.avi, .wmv, .wm, .asf, .asx, .wmx, .wvx)"), "extension": "*.png", "regex": /.*\.(avi|wmv|wm|asf|asx|wmx|wvx)$/i },
        { "typename": parent._("RealMedia Audio Files (.rm, .ra, .ram, .mp3)"), "extension": "*.png", "regex": /.*\.(rm|ra|ram|mp3)$/i }
    ]);
    parent.window.focus();
    fp.show();
}

// Called by the user to provide an image or flash file name to add to the package
function addImage(event, elementId, filter) {
    var fn = function(imagePath) {
	    if (imagePath != "") {
	        var image = document.getElementById('img'+elementId);
	        image.removeAttribute('width');
	        image.removeAttribute('height');
	        var path  = document.getElementById('path'+elementId);
	        path.value = imagePath;
	        //image.src  = 'file://'+imagePath;
	        image.src  = ldshake_top().location.href + '/resources/' + imagePath;
	        var theForm = getContentForm();
	        theForm.action.value = "addImage";
	        theForm.object.value = elementId;
	        theForm.submit();
	    }
    }
    askUserForImage(event, false, fn, filter);
}

// Called by the user to provide an image feedback to add to a case study idevice question
function addFeedbackImage(event, elementId) {
    var fn = function(imagePath) {
	    if (imagePath != "") {
	        var image = document.getElementById('img'+elementId);
	        image.removeAttribute('width');
	        image.removeAttribute('height');
	        var path  = document.getElementById('path'+elementId);
	        path.value = imagePath;
	        image.src  = 'file://'+imagePath;
	        var theForm = getContentForm();
	        theForm.action.value = "addImage"
	        theForm.object.value = elementId 
	        var width = document.getElementById('width'+elementId);
	        var height = document.getElementById('height'+elementId);
	        width.value = "100";
	        height.value = "";
	        theForm.submit();
	        changeImageWidth(elementId);
	        theForm.submit();
	    }
    }
    askUserForImage(event, false, fn);
}

// Called by the user to provide an image or flash file name to add to the package
function addJpgImage(elementId) {
    var filter = { "typename": parent._("JPEG Files (.jpg, .jpeg)"), "extension": "*.png", "regex": /.*\.(jpg|jpeg)$/i };
    addImage(elementId, filter);
}

// Called by the user to provide one or more image files name to add to the package
function addGalleryImage(event, galleryId) {
    var fn = function(imagePath) {
	    if (imagePath != "") {
	        // Save the change
	        submitLink("gallery.addImage."+imagePath, galleryId, true);
	    }
    }
    askUserForImage(event, true, fn);
}

// Called by the user to change an existing gallery image
function changeGalleryImage(event, galleryId, imageId) {
    var fn = function(imagePath) {
	    if (imagePath != "") {
	        // Save the change
	        submitLink("gallery.changeImage."+imageId+"."+imagePath, galleryId, true);
	    }
    }
    askUserForImage(event, false, fn);
}

// Called by the tinyMCE (as per the user's request) to provide an 
// image file name to add to the package's field and idevice
function chooseImage_viaTinyMCE(field_name, url, type, win) {
    var fn = function(local_imagePath) {
        win.focus();
    
        // if the user hits CANCEL, then bail "immediately",
        // i.e., after bringing the tinyMCE image dialog back into focus, above.
        if (local_imagePath == "") {
           return;
        }
    
        // UNescape, to remove the %20's for spaces, etc.:
        var unescaped_local_imagePath = unescape(local_imagePath);
        var oldImageStr = new String(unescaped_local_imagePath);
    
        // and replace path delimiters (':', '\', or '/') or '%', ' ', or '&' 
        // with '_':
        var RegExp1 = /[\ \\\/\:\%\&]/g;
        var ReplaceStr1 = new String("_");
        var newImageStr = oldImageStr.replace(RegExp1, ReplaceStr1);
    
        // For simplicity across various file encoding schemes, etc.,
        // just ensure that the TinyMCE media window also gets a URI safe link, 
        // for doing its showPreview():
        var early_preview_imageName = encodeURIComponent(newImageStr);
        // and one more escaping of the '%'s to '_'s, to flatten for simplicity:
        var preview_imageName  = early_preview_imageName.replace(RegExp1, ReplaceStr1);
        var full_previewImage_url = "/previews/"+preview_imageName;
    
        // pass the file information on to the server,
        // to copy it into the server's "previews" directory:
        window.parent.nevow_clientToServerEvent('previewTinyMCEimage', this, 
                      '', win, win.name, field_name, unescaped_local_imagePath, 
                      preview_imageName)
    
        // first, clear out any old value in the tinyMCE image filename field:
        win.document.forms[0].elements[field_name].value = ""; 
    
        // PreviewImage is only available for images:
        if (type == "image") {
           win.showPreviewImage(" ");
        }
        else if (type == "media") {
           win.generatePreview(" ");
        }
    
    
        // set the tinyMCE image filename field:
        win.document.forms[0].elements[field_name].value = full_previewImage_url;
        // then force its onchange event:
    
        // PreviewImage is only available for images:
        if (type == "image") {
           win.showPreviewImage(full_previewImage_url);
        }
        else if (type == "media") {
           win.generatePreview(full_previewImage_url);
        }
    
        // this onchange works, but it's dirty because it is hardcoding the 
        // onChange=".." event of that field, and if that were to ever change 
        // in tinyMCE, then this would be out of sync.
    
        // and finally, be sure to update the tinyMCE window's image data:
        if (win.getImageData) {
            win.getImageData();
        }
        else {
            if (window.tinyMCE.getImageData) {
               window.tinyMCE.getImageData();
            }
        }
    }
    // ask user for image or media, depending on type requested:
    if (type == "image") {
       askUserForImage(false, fn);
    }
    else if (type == "media") {
       askUserForMedia(fn);
    }
    else if (type == "file") {
       // new for advlink plugin, to link ANY resource into text:
       // re-use the Media browser, which defaults to All file types (*.*)
       askUserForMedia(fn);
    }
    else if (type == "image2insert" || type == "media2insert" || type == "file2insert") {
        if (type == "file2insert" && url.indexOf('#') >= 0) {
            // looks like a link to an internal anchor due to the #, so do
            // not proceed any further, since there is no more action necessary:
            return;
            // UNLESS this causes problems with embedding real filenames w/ #!!
            // But this will only be for links or filenames typed by hand;
            // those textlink URLs inserted via its file browser will use 
            // type=file rather than type=file2insert
        }
        // new direct insert capabilities, no file browser needed.
        // just copy the passed-in URL directly, no browser necessary:
        fn(url);
    }
}

// Called by the tinyMCE (as per the user's request) to generate an 
// image file of the specified math (LaTeX source, compiled by mimetex)
// to add to the package's field and idevice
function makeMathImage_viaTinyMCE(field_name, src_latex, font_size, type, win) {

    var local_imagePath = ""

    if (src_latex == "") {
       return;
    }

    // to help unique-ify each previewed math image:
    var preview_basename = "eXe_LaTeX_math_"+curr_edits_math_num
    var preview_math_imagefile = preview_basename+".gif"
    // Simplify the subsequent file-lookup process,  by just appending 
    // the ".tex" to the full image name, as such:
    var preview_math_srcfile = preview_math_imagefile+".tex"
   
    curr_edits_math_num += 1

    // pass the file information on to the server,
    // to generate the image into the server's "previews" directory:
    window.parent.nevow_clientToServerEvent('generateTinyMCEmath', this, 
                  '', win, win.name, field_name, 
                  src_latex, font_size, preview_math_imagefile, 
                  preview_math_srcfile)

    // once the image has been generated, it SHOULD be sitting here:
    var full_preview_url = "/previews/"+preview_math_imagefile;

    win.focus();

    // clear out any old value in the tinyMCE image filename field:
    win.document.forms[0].elements[field_name].value = ""; 
    // PreviewImage is only available for images:
    if (type == "image") {
       win.showPreviewImage(" ");
    }
    // the above two commands are the only way to really 
    // ensure that we can trigger the onchange event below:

    // set the tinyMCE image filename field:
    win.document.forms[0].elements[field_name].value = full_preview_url;
    // then force its onchange event:
    // PreviewImage is only available for images:
    if (type == "image") {
       win.showPreviewImage(full_preview_url);
    }

}


// Called by the user to provide a flash movie file name to add to the package
function addFlashMovie(blockId) {
    var filter = [
        { "typename": parent._("Flash Movie (.flv)"), "extension": "*.*", "regex": /.*\.flv$/i },
        { "typename": parent._("All Files"), "extension": "*.*", "regex": /.*$/ }
    ];
    addFile(blockId, parent._("Select a file"), filter);
}

// Called by the user to provide a flash file name to add to the package
function addFlash(blockId) {
    var filter = [
        { "typename": parent._("Flash Object (.swf)"), "extension": "*.*", "regex": /.*\.swf$/i },
        { "typename": parent._("All Files"), "extension": "*.*", "regex": /.*$/ }
    ];
    addFile(blockId, parent._("Select a file"), filter);
}

function addMp3(blockId) {
    var filter = [
        { "typename": parent._("MP3 Audio (.mp3)"), "extension": "*.*", "regex": /.*\.mp3$/i },
        { "typename": parent._("All Files"), "extension": "*.*", "regex": /.*$/ }
    ];
    addFile(blockId, parent._("Select an mp3 file"), filter);
}

// Called by the user to provide a package name in order to get the user created idevices
// used by idevice editor
/*function addFile(blockId, title, filter) {
    var fp = parent.Ext.create("eXe.view.filepicker.FilePicker", {
        type: parent.eXe.view.filepicker.FilePicker.modeOpen,
        title: title? title : parent._("Select a package"),
        modal: true,
        scope: this,
        callback: function(fp) {
            if (fp.status == parent.eXe.view.filepicker.FilePicker.returnOk) {
		        var path  = document.getElementById('path'+blockId);
                path.type  = 'text';
		        path.value = fp.file.path;
            }
        }
    });
    fp.appendFilters( filter? filter : [{ "typename": parent._("All Files"), "extension": "*.*", "regex": /.*$/ }]);
    parent.window.focus();
    fp.show();
}*/

// LdShake mod
function addFile(event, blockId, title, filter) {
    event.stopPropagation();
    event.preventDefault();
    /*title = title? title : parent._("Select a package");*/

    askUserFor(false, function(filename) {
            var path  = document.getElementById('path'+blockId);
            path.type  = 'text';
            path.value = filename;
    });
}

function getContentForm() {
    var theForm;

    theForm = document.getElementById('contentForm');
    var top_window = ldshake_top();
    if (!theForm) {
        if (top_window["authoringIFrame1-frame"] && top_window["authoringIFrame1-frame"].document)
            theForm = top_window["authoringIFrame1-frame"].document.getElementById('contentForm');
    }
    if (!theForm) {
        if (document.getElementsByName('authoringIFrame1-frame')[0] && document.getElementsByName('authoringIFrame1-frame')[0].contentDocument)
            theForm = document.getElementsByName('authoringIFrame')[0].contentDocument.getElementById('contentForm');
    }

    return theForm;
}
// Called upon loading the page this function clears the hidden
// action and object fields so they can be used by submitLink
function clearHidden()
{
    var theForm = getContentForm();

    if (theForm) {
	    theForm.action.value = "";
	    theForm.object.value = "";
    }
}

// Sets the hidden action and object fields, then submits the
// contentForm to the server
/*
function execute_submitLink(action, object, changed, currentNode) 
{
    var theForm = getContentForm();

    if (theForm) {
	    theForm.action.value    = action;
	    theForm.object.value    = object;
	    theForm.isChanged.value = changed;
        if (currentNode)
            theForm.currentNode.value = currentNode;
        theForm.clientHandleId.value = ldshake_top().nevow_clientHandleId;
	    runFuncArray(beforeSubmitHandlers)
	
	    theForm.submit();
    }
}
*/
function execute_submitLink(action, object, changed, currentNode)
{
    var theForm = getContentForm();
    var submitEvent;

    if (theForm) {
	    theForm.action.value    = action;
	    theForm.object.value    = object;
	    theForm.isChanged.value = changed;
        if (currentNode)
            theForm.currentNode.value = currentNode;
        theForm.clientHandleId.value = ldshake_top().nevow_clientHandleId;
	    runFuncArray(beforeSubmitHandlers)

        //LdShake mod
        theForm.addEventListener("submit", function(e){
            e.preventDefault();
            theForm.win = window;
            ldshake_top().post_submitLink(theForm);
        });

        submitEvent = document.createEvent("CustomEvent");
        submitEvent.initEvent("submit", false, true);
        theForm.dispatchEvent(submitEvent);

	    //theForm.submit();

    }
}

//LdShake mod
document.addEventListener("DOMContentLoaded", function(event) {
    var submitLinks = document.querySelectorAll("a > img.submit");

    if(!submitLinks.length)
        return false;

    for(var i=0; i<submitLinks.length;i++)
        (function(elem) {
            elem.addEventListener("click", function(elemEvent) {
                elemEvent.preventDefault();
            });
        })(submitLinks[i]);
});

function submitLink(action, object, changed, currentNode)
{
    // Close full screen
    if(action=='changeNode') {
        var ed = "";
        if (typeof(tinyMCE)!='undefined' && tinyMCE.activeEditor) ed = tinyMCE.activeEditor;
        if (ed!="" && ed.id=="mce_fullscreen") {
            ed.execCommand('mceFullScreen');
            setTimeout(function(){
                execute_submitLink(action, object, changed, currentNode) 
            },500);
        } else {
            execute_submitLink(action, object, changed, currentNode) 
        }
    } else {    
        execute_submitLink(action, object, changed, currentNode) 
    }
}

//change applet type on appletblock
function submitChange(action, selectId) 
{
    var theForm = getContentForm();

    if (theForm) {
        theForm.action.value    = action;
        var select = document.getElementById(selectId) 
        theForm.object.value    = select.value;
        theForm.isChanged.value = 1;
        theForm.clientHandleId.value = ldshake_top().nevow_clientHandleId;
        runFuncArray(beforeSubmitHandlers)

        theForm.submit();
    }
}

// Check the user really really wants to do this before submitting
function confirmThenSubmitLink(message, action, object, changed)
{
    parent.Ext.Msg.confirm("", message, function(button) {
        if (button == "yes")
	        submitLink(action, object, changed);
    });
}

//List to track problematic browser plugins with no zindex support on authoring page
authoringPluginObjects = [];

//Activate zindex support on flash plugin objects on authoring page
//http://helpx.adobe.com/flash/kb/flash-object-embed-tag-attributes.html#main_Browser_support_for_Window_Mode__wmode__values/fplayer10.1_hardware_acceleration_04.html
function setWmodeToFlash() {
    var flashobjects = parent.Ext.DomQuery.select('object[type=application/x-shockwave-flash]', document);
    for (i in flashobjects) {
        parent.Ext.DomHelper.append(flashobjects[i], { tag: "param",  name: "wmode", value: "opaque" });
    }
}

//Load the list of problematic browser plugins tags
//Java applets not suports zindex: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4858528
function loadAuthoringPluginObjects() {
    authoringPluginObjects = authoringPluginObjects.concat(parent.Ext.DomQuery.select('object[type!=application/x-shockwave-flash]', document));
    authoringPluginObjects = authoringPluginObjects.concat(parent.Ext.DomQuery.select('applet', document));
}

function hideObjectTags() {
	if (Ext.WindowManager.getActive()) {
	    for (var i=0; i < authoringPluginObjects.length; i++)
	        authoringPluginObjects[i].style.visibility = "hidden";
	}
}

function showObjectTags() {
    for (var i=0; i < authoringPluginObjects.length; i++)
        authoringPluginObjects[i].style.visibility = "visible";
}

function enableAnchors() {
	var exenodes = parent.Ext.DomQuery.select('a[href^=exe-node]', document);

	for (var i=0; i < exenodes.length; i++)
        exenodes[i].onclick = function() {
            var outline = parent.eXe.app.getController('Outline'),
                outline_tree = outline.getOutlineTreePanel(),
                node_anchor = this.href.split("#"),
                path = node_anchor[0].replace(/exe-node/, ':Root'),
                selected;
            outline_tree.selectPath(unescape(path), 'text', ':');
            selected = outline_tree.getSelectionModel().getSelection()[0];
            outline.onNodeClick(null, selected);
            if (node_anchor[1] != "auto_top")
                outline.hash = node_anchor[1];
            return false;
        };
}

function gotoAnchor() {
    if (typeof(parent.eXe)!='undefined' && typeof(parent.eXe.app)!='undefined') {
        var outline = parent.eXe.app.getController('Outline');

        if (outline.hash) {
            location.hash = outline.hash;
            outline.hash = null;
        }
    }
}

function preventEscKey() {
        if (parent.Ext.isGecko || parent.Ext.isSafari)
            window.addEventListener('keydown', function(e) {(e.keyCode == 27 && e.preventDefault())});
}

function preventHistoryBack() {
	for (var i=0; i<=20; i++)
    	History.pushState(null, null, '?state=' + i);
}

function loadKeymap() {
	var toolbar = eXe.app.getController('Toolbar'),
	    authoring = Ext.ComponentQuery.query('#authoring')[0],
	    keymap = new Ext.util.KeyMap(authoring.getBody(), toolbar.keymap_config);
}
/* *********************************** */
/* WYSIWYG Editor and common settings */
/* ********************************* */

// Common settings
var eXeLearning_settings = {
    wysiwyg_path : "/scripts/tinymce_3.5.7/jscripts/tiny_mce/tiny_mce.js",
    wysiwyg_settings_path : "/scripts/tinymce_3.5.7_settings.js"
}

// browse the specified URL in system browser
function browseURL(e) {
    /* Links with rel="lightbox" */
    if (typeof(e)=='object') {
        if (typeof(e.rel)=='string' && e.rel.indexOf('lightbox')==0) {
            return false;
        }
        e = e.href;
    }
    window.parent.nevow_clientToServerEvent('browseURL', this, '', e);
}

//TinyMCE
function getTinyMCELang(lang){
    if (lang=="ca@valencia") lang = "ca";
	var defaultLang = "en";
	for (var i=0;i<tinyMCE_languages.length;i++) {
		if (tinyMCE_languages[i]===lang) defaultLang = lang;
	}
	return defaultLang;
}

//TinyMCE file_browser_callback
var exe_tinymce = {
	
	chooseImage : function(field_name, url, type, win) {
		
		var fn = function(local_imagePath) {
			win.focus();
		
			// if the user hits CANCEL, then bail "immediately",
			// i.e., after bringing the tinyMCE image dialog back into focus, above.
			if (local_imagePath == "") {
			   return;
			}
		
			// UNescape, to remove the %20's for spaces, etc.:
			var unescaped_local_imagePath = unescape(local_imagePath);
			var oldImageStr = new String(unescaped_local_imagePath);
			
			/* HTML 5 */
			exe_tinymce.uploaded_file_1_name = "";
			
			var last_uploaded_file_path = unescaped_local_imagePath.split("\\");
			var last_uploaded_file_name = last_uploaded_file_path[last_uploaded_file_path.length-1];
			/* Main file */
			if (field_name=="src") {
				exe_tinymce.uploaded_file_1_name = last_uploaded_file_name;
			}		
			/* /HTML5 */		
		
			// and replace path delimiters (':', '\', or '/') or '%', ' ', or '&' 
			// with '_':
			var RegExp1 = /[\ \\\/\:\%\&]/g;
			var ReplaceStr1 = new String("_");
			var newImageStr = oldImageStr.replace(RegExp1, ReplaceStr1);
		
			// For simplicity across various file encoding schemes, etc.,
			// just ensure that the TinyMCE media window also gets a URI safe link, 
			// for doing its showPreview():
			var early_preview_imageName = encodeURIComponent(newImageStr);
			// and one more escaping of the '%'s to '_'s, to flatten for simplicity:
			var preview_imageName  = early_preview_imageName.replace(RegExp1, ReplaceStr1);
			var full_previewImage_url = "/previews/"+preview_imageName;
		
			// pass the file information on to the server,
			// to copy it into the server's "previews" directory:
			window.parent.nevow_clientToServerEvent('previewTinyMCEimage', this, '', win, win.name, field_name, unescaped_local_imagePath, preview_imageName)
		
			// first, clear out any old value in the tinyMCE image filename field:
			win.document.forms[0].elements[field_name].value = ""; 
		
			// PreviewImage is only available for images:
			if (type == "image") {
			   win.ImageDialog.showPreviewImage(" ");
			} else if (type == "media") {
			   win.window.Media.preview();
			}    
		
			// set the tinyMCE image filename field:
			win.document.forms[0].elements[field_name].value = full_previewImage_url;
			// then force its onchange event:
		
			// PreviewImage is only available for images:
			if (type == "image") {
			   win.ImageDialog.showPreviewImage(full_previewImage_url);
			}
			else if (type == "media") {
			   win.window.Media.preview();
			}
		
			// this onchange works, but it's dirty because it is hardcoding the 
			// onChange=".." event of that field, and if that were to ever change 
			// in tinyMCE, then this would be out of sync.
		
			// and finally, be sure to update the tinyMCE window's image data:
			if (win.getImageData) {
				win.getImageData();
			} else {
				if (window.tinyMCE.getImageData) {
				   window.tinyMCE.getImageData();
				}
			}
		}
		// ask user for image or media, depending on type requested:
		if (type == "image") {
		   askUserForImage(false, fn);
		} else if (type == "media") {
		   askUserForMedia(fn);
		} else if (type == "file") {
		   // new for advlink plugin, to link ANY resource into text:
		   // re-use the Media browser, which defaults to All file types (*.*)
		   askUserForMedia(fn);
		} else if (type == "image2insert" || type == "media2insert" || type == "file2insert") {
			if (type == "file2insert" && url.indexOf('#') >= 0) {
				// looks like a link to an internal anchor due to the #, so do
				// not proceed any further, since there is no more action necessary:
				return;
				// UNLESS this causes problems with embedding real filenames w/ #!!
				// But this will only be for links or filenames typed by hand;
				// those textlink URLs inserted via its file browser will use 
				// type=file rather than type=file2insert
			}
			// new direct insert capabilities, no file browser needed.
			// just copy the passed-in URL directly, no browser necessary:
			fn(url);
		}
	}//chooseImage
	
}

var $exeAuthoring = {
    changeFlowPlayerPathInIE : function(){
        var objs = document.getElementsByTagName("OBJECT");
        var i = objs.length;
        while (i--) {
            if(objs[i].type=="application/x-shockwave-flash" && objs[i].data.indexOf("/flowPlayer.swf")!=-1) {
                objs[i].style.display="none";
                var h = objs[i].height;
                var w = objs[i].width;
                var s = objs[i].data;
                var e = document.createElement("DIV");
                var o = objs[i].innerHTML;
                o = o.replace("'playlist': [ { 'url': 'resources/","'playlist': [ {'url':'http://"+window.location.host+"/"+exe_package_name+"/resources/");
                e.innerHTML = '<object data="'+s+'" width="'+w+'"height="'+h+'">'+o+'</object>';
                objs[i].parentNode.insertBefore(e,objs[i]);                                
            }
        }        
    },
    setYoutubeWmode : function(){
        var v = document.getElementsByTagName("IFRAME");
        for (i=0;i<v.length;i++) {
            var s = v[i].src;
            if (s.indexOf("http://www.youtube.com")==0 && s.indexOf("wmode=")==-1) {
                var c = "?";
                if (s.indexOf("?")!=-1) c = "&";
                s += c+"wmode=transparent";
                v[i].src = s;
            }
        }
    },
    disableSVGInMediaElement : function(){
        $(document.body).addClass("no-svg");
    },
    ready : function(){
        if (ldshake_top().Ext) {
            $exeAuthoring.disableSVGInMediaElement();
            $exeAuthoring.setYoutubeWmode();
            if (ldshake_top().Ext.isIE) {
                $exeAuthoring.changeFlowPlayerPathInIE();
            }
        }
    }
}
//new functions from common.js
function magnifierImageChanged(event) {
    var id = event.currentTarget.getAttribute('id');
    var elementId = id.substring(3, id.length);
    var image  = document.getElementById('img'+elementId);
    var width  = document.getElementById('width'+elementId);
    var height = document.getElementById('height'+elementId);
	    image.removeAttribute('height');
		image.removeAttribute('width');
}
function imageChanged(event) {
    var id = event.currentTarget.getAttribute('id');
    var elementId = id.substring(3, id.length);
    var image  = document.getElementById('img'+elementId);
    var width  = document.getElementById('width'+elementId);
    var height = document.getElementById('height'+elementId);
    width.value  = image.width;
    height.value = image.height;
}

function changeImageWidth(elementId) {
    var image  = document.getElementById('img'+elementId);
    var width  = document.getElementById('width'+elementId);
    var height = document.getElementById('height'+elementId);
    image.removeAttribute('height');
    if (width.value) {
        image.width  = width.value;
    } else if (image.hasAttribute('src')) {
        image.removeAttribute('width');
        width.value = image.width;
    } else {
        width.value = "";
    }
    height.value = image.height;
}

function changeImageHeight(elementId) {
    var image  = document.getElementById('img'+elementId);
    var width  = document.getElementById('width'+elementId);
    var height = document.getElementById('height'+elementId);
    image.removeAttribute('width');
    if (height.value) {
        image.height = height.value;
    } else if (image.hasAttribute('src')) {
        image.removeAttribute('height');
        height.value = image.height;
    } else {
        height.value = "";
    }
    width.value  = image.width;
}
function changeMagnifierImageWidth(elementId) {
    var image  = document.getElementById('img'+elementId);
    var width  = document.getElementById('width'+elementId);
    var height = document.getElementById('height'+elementId);
    image.removeAttribute('height');
    if (width.value) {
        image.width  = width.value;
    } else {
        image.removeAttribute('width');
    }
    
}
function changeMagnifierImageHeight(elementId) {
    var image  = document.getElementById('img'+elementId);
    var width  = document.getElementById('width'+elementId);
    var height = document.getElementById('height'+elementId);
    image.removeAttribute('width');
    if (height.value) {
        image.height = height.value;
    } else {
        image.removeAttribute('height');
    }
    
}
