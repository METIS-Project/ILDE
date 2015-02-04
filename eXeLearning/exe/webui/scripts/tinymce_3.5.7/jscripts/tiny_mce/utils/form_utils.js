/**
 * form_utils.js
 *
 * Copyright 2009, Moxiecode Systems AB
 * Released under LGPL License.
 *
 * License: http://tinymce.moxiecode.com/license
 * Contributing: http://tinymce.moxiecode.com/contributing
 */

var themeBaseURL = tinyMCEPopup.editor.baseURI.toAbsolute('themes/' + tinyMCEPopup.getParam("theme"));

function getColorPickerHTML(id, target_form_element) {
	var h = "", dom = tinyMCEPopup.dom;

	if (label = dom.select('label[for=' + target_form_element + ']')[0]) {
		label.id = label.id || dom.uniqueId();
	}

	h += '<a role="button" aria-labelledby="' + id + '_label" id="' + id + '_link" href="javascript:;" onclick="tinyMCEPopup.pickColor(event,\'' + target_form_element +'\');" onmousedown="return false;" class="pickcolor">';
	h += '<span id="' + id + '" title="' + tinyMCEPopup.getLang('browse') + '">&nbsp;<span id="' + id + '_label" class="mceVoiceLabel mceIconOnly" style="display:none;">' + tinyMCEPopup.getLang('browse') + '</span></span></a>';

	return h;
}

function updateColor(img_id, form_element_id) {
	document.getElementById(img_id).style.backgroundColor = document.forms[0].elements[form_element_id].value;
}

function setBrowserDisabled(id, state) {
	var img = document.getElementById(id);
	var lnk = document.getElementById(id + "_link");

	if (lnk) {
		if (state) {
			lnk.setAttribute("realhref", lnk.getAttribute("href"));
			lnk.removeAttribute("href");
			tinyMCEPopup.dom.addClass(img, 'disabled');
		} else {
			if (lnk.getAttribute("realhref"))
				lnk.setAttribute("href", lnk.getAttribute("realhref"));

			tinyMCEPopup.dom.removeClass(img, 'disabled');
		}
	}
}

/*
function getBrowserHTML(id, target_form_element, type, prefix) {
	var option = prefix + "_" + type + "_browser_callback", cb, html;

	cb = tinyMCEPopup.getParam(option, tinyMCEPopup.getParam("file_browser_callback"));

	if (!cb)
		return "";

	html = "";
	html += '<a id="' + id + '_link" href="javascript:openBrowser(\'' + id + '\',\'' + target_form_element + '\', \'' + type + '\',\'' + option + '\');" onmousedown="return false;" class="browse">';
	html += '<span id="' + id + '" title="' + tinyMCEPopup.getLang('browse') + '">&nbsp;</span></a>';

	return html;
}*/

/* LdShake mod */
function getBrowserHTML(id, target_form_element, type, prefix) {
	var option = prefix + "_" + type + "_browser_callback", cb, html;

	cb = tinyMCEPopup.getParam(option, tinyMCEPopup.getParam("file_browser_callback"));

	if (!cb)
		return "";

	html = "";
	//html += '<a id="' + id + '_link" href="#" onclick="clickFileBrowse(event)" class="browse">';
	if(type != "image")
        html += '<a id="' + id + '_link" href="#" onclick="askUserForMedia(event, false, buildUploadCallback(\''+target_form_element+'\'), \''+type+'\', true)" class="browse">';
    else
        html += '<a id="' + id + '_link" href="#" onclick="askUserForImage(event, false, buildUploadCallback(\''+target_form_element+'\'), null, true)" class="browse">';
	html += '<span id="fileBrowse" title="' + tinyMCEPopup.getLang('browse') + '">&nbsp;</span></a>';
	html += '<input type="file" style="display: none;" />';

	return html;
}

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

function askUserForFile(multiple, fn, filter) {
    askUserFor(multiple, fn);
}

function askUserForMedia(event, multiple, fn, filter, preview) {
    event.stopPropagation();
    event.preventDefault();

    var mimefilter = {
        video: 'video/webm,video/mp4,video/ogg',
        flash: 'application/x-shockwave-flash,video/x-flv,audio/mp3',
        quicktime: 'video/quicktime,video/mp4,video/mpeg',
        windowsmedia: 'video/x-ms-wmv,audio/x-ms-wma',
        realmedia: 'audio/x-pn-realaudio',
        audio: 'audio/ogg,audio/mp3,audio/wav'
    }
    askUserFor(multiple, fn, null, mimefilter[filter], preview);
}

function askUserForImage(event, multiple, fn, filter, preview) {
    event.stopPropagation();
    event.preventDefault();
    askUserFor(multiple, fn, null, 'image/*', preview);
}

function buildUploadCallback(id) {
    var updatefield = function(filename) {
        var input = document.getElementById(id);
        var url = "/previews/" + filename;
        var change;

        input.value = url;

        if(document.createEvent && document.dispatchEvent) {
            change = document.createEvent("HTMLEvents");
            change.initEvent("change", false, true);
            input.dispatchEvent(change);
        } else {//IE8
            input.onchange();
        }
    }
    return updatefield;
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

function clickFileBrowse(event) {
    var input = document.querySelector("[type=file]");
    if(input)
        input.click();

    event.preventDefault();
}

/*function uploadFile(event, callback) {
    var inputFileElement = event.srcElement;
    var uploadFileObject;
    var uploadFormData;
    var post, url;

    if(!inputFileElement.files)
        return false;

    if(!inputFileElement.files.length)
        return false;

    uploadFormData = new FormData();
    uploadFileObject = inputFileElement.files[0];
    uploadFormData.append("file", uploadFileObject);
    uploadFormData.append("name", uploadFileObject.name);
    uploadFormData.append("preview", "true");

    url = ldshake_top().location.href + "/upload";
    post = new XMLHttpRequest();

    post.open("POST", url, true);
    post.responseType = "json";
    post.onreadystatechange = function() {
        if (post.readyState == 4 && post.status == 200) {
            callback(post.response);
        }
    }

    post.send(uploadFormData);

    console.log(uploadFileObject);
}*/

/* LdShake mod */
//finds the top iframe
function ldshake_top()
{
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

function openBrowser(img_id, target_form_element, type, option) {
	var img = document.getElementById(img_id);

	if (img.className != "mceButtonDisabled")
		tinyMCEPopup.openBrowser(target_form_element, type, option);
}

function selectByValue(form_obj, field_name, value, add_custom, ignore_case) {
	if (!form_obj || !form_obj.elements[field_name])
		return;

	if (!value)
		value = "";

	var sel = form_obj.elements[field_name];

	var found = false;
	for (var i=0; i<sel.options.length; i++) {
		var option = sel.options[i];

		if (option.value == value || (ignore_case && option.value.toLowerCase() == value.toLowerCase())) {
			option.selected = true;
			found = true;
		} else
			option.selected = false;
	}

	if (!found && add_custom && value != '') {
		var option = new Option(value, value);
		option.selected = true;
		sel.options[sel.options.length] = option;
		sel.selectedIndex = sel.options.length - 1;
	}

	return found;
}

function getSelectValue(form_obj, field_name) {
	var elm = form_obj.elements[field_name];

	if (elm == null || elm.options == null || elm.selectedIndex === -1)
		return "";

	return elm.options[elm.selectedIndex].value;
}

function addSelectValue(form_obj, field_name, name, value) {
	var s = form_obj.elements[field_name];
	var o = new Option(name, value);
	s.options[s.options.length] = o;
}

function addClassesToList(list_id, specific_option) {
	// Setup class droplist
	var styleSelectElm = document.getElementById(list_id);
	var styles = tinyMCEPopup.getParam('theme_advanced_styles', false);
	styles = tinyMCEPopup.getParam(specific_option, styles);

	if (styles) {
		var stylesAr = styles.split(';');

		for (var i=0; i<stylesAr.length; i++) {
			if (stylesAr != "") {
				var key, value;

				key = stylesAr[i].split('=')[0];
				value = stylesAr[i].split('=')[1];

				styleSelectElm.options[styleSelectElm.length] = new Option(key, value);
			}
		}
	} else {
		tinymce.each(tinyMCEPopup.editor.dom.getClasses(), function(o) {
			styleSelectElm.options[styleSelectElm.length] = new Option(o.title || o['class'], o['class']);
		});
	}
}

function isVisible(element_id) {
	var elm = document.getElementById(element_id);

	return elm && elm.style.display != "none";
}

function convertRGBToHex(col) {
	var re = new RegExp("rgb\\s*\\(\\s*([0-9]+).*,\\s*([0-9]+).*,\\s*([0-9]+).*\\)", "gi");

	var rgb = col.replace(re, "$1,$2,$3").split(',');
	if (rgb.length == 3) {
		r = parseInt(rgb[0]).toString(16);
		g = parseInt(rgb[1]).toString(16);
		b = parseInt(rgb[2]).toString(16);

		r = r.length == 1 ? '0' + r : r;
		g = g.length == 1 ? '0' + g : g;
		b = b.length == 1 ? '0' + b : b;

		return "#" + r + g + b;
	}

	return col;
}

function convertHexToRGB(col) {
	if (col.indexOf('#') != -1) {
		col = col.replace(new RegExp('[^0-9A-F]', 'gi'), '');

		r = parseInt(col.substring(0, 2), 16);
		g = parseInt(col.substring(2, 4), 16);
		b = parseInt(col.substring(4, 6), 16);

		return "rgb(" + r + "," + g + "," + b + ")";
	}

	return col;
}

function trimSize(size) {
	return size.replace(/([0-9\.]+)(px|%|in|cm|mm|em|ex|pt|pc)/i, '$1$2');
}

function getCSSSize(size) {
	size = trimSize(size);

	if (size == "")
		return "";

	// Add px
	if (/^[0-9]+$/.test(size))
		size += 'px';
	// Sanity check, IE doesn't like broken values
	else if (!(/^[0-9\.]+(px|%|in|cm|mm|em|ex|pt|pc)$/i.test(size)))
		return "";

	return size;
}

function getStyle(elm, attrib, style) {
	var val = tinyMCEPopup.dom.getAttrib(elm, attrib);

	if (val != '')
		return '' + val;

	if (typeof(style) == 'undefined')
		style = attrib;

	return tinyMCEPopup.dom.getStyle(elm, style);
}
