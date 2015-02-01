/**
 * Helper function for element.addEventListener/attachEvent
 */
function addDomListener(element, eventName, listener) {
  if (element.addEventListener)
    element.addEventListener(eventName, listener, false);
  else if (element.attachEvent)
    element.attachEvent('on' + eventName, listener);
}

/**
 * Helper function to get the rectangle for the given HTML element.
 */
function getElementRect(element) {
  var left = element.offsetLeft;
  var top = element.offsetTop;
  
  var p = element.offsetParent;
  while (p && p != document.body.parentNode) {
    if (isFinite(p.offsetLeft) && isFinite(p.offsetTop)) {
      left += p.offsetLeft;
      top += p.offsetTop;
    }
    
    p = p.offsetParent;
  }
  
  return { left: left, top: top,
           width: element.offsetWidth, height: element.offsetHeight };
}

/**
 * Create a custom button using the IFRAME shim and CSS sprite technique
 * at the given x, y offset from the top left of the plugin container.
 */
function createNativeHTMLButton(x, y, width, height, urlImage, id, title, rectId) {
  // create the button
  var button = document.createElement('a');
  button.id = id;
  button.href = '#';
  button.className = 'tri-button';
  button.style.display = 'block';
  button.style.backgroundImage = 'url(' + urlImage +')';
  button.style.backgroundColor = 'white';
  /*var button = document.createElement('input');
  button.id = id;
  button.type = 'image';
  button.src = urlImage;
  if (title){
	  button.title = title;
  }*/
  
  // create an IFRAME shim for the button
  var iframeShim = document.createElement('iframe');
  iframeShim.id = "iframe_" + id;
  iframeShim.frameBorder = 0;
  iframeShim.scrolling = 'no';
  iframeShim.src = (navigator.userAgent.indexOf('MSIE 6') >= 0) ?
      '' : 'javascript:void(0);';

  // position the button and IFRAME shim
  var pluginRect = getElementRect(document.getElementById(rectId));
  button.style.position = iframeShim.style.position = 'absolute';
  button.style.left = iframeShim.style.left = (pluginRect.left + x) + 'px';
  button.style.top = iframeShim.style.top = (pluginRect.top + y) + 'px';
  button.style.width = iframeShim.style.width = 0 + 'px';//width + 'px';
  button.style.height = iframeShim.style.height = 0 + 'px';//height + 'px';
  
  button.initialWidth = width + 'px';
  button.initialHeight = height + 'px';
  
  // set up z-orders
  button.style.zIndex = 10;
  iframeShim.style.zIndex = button.style.zIndex - 1;
  
  /*if (functionOnclick){
		  // set up click handler
	  addDomListener(button, 'click', function(evt) {
			functionOnclick();
		    if (evt.preventDefault) {
		      evt.preventDefault();
		      evt.stopPropagation();
		    }
		    return false;
	  });
  }*/
  
  // add the iframe shim and button
  document.body.appendChild(button);
  document.body.appendChild(iframeShim);
}

/**
 */
function moveNativeHTMLButton(x, y, id, rectId) {
  // get the button
  var button = document.getElementById(id);
  // get the IFRAME shim for the button
  var iframeShim = document.getElementById("iframe_" + id);
  var pluginRect = getElementRect(document.getElementById(rectId));
  button.style.position = iframeShim.style.position = 'absolute';
  button.style.left = iframeShim.style.left = (pluginRect.left + x) + 'px';
  button.style.top = iframeShim.style.top = (pluginRect.top + y) + 'px';
  // set up z-orders
  button.style.zIndex = 10;
}

function showNativeHTMLButton(id){
	var button = document.getElementById(id);
	if (button != null){
		button.style.width = button.initialWidth;
		button.style.height = button.initialHeight;
		var iframeShim = document.getElementById("iframe_" + id);
		iframeShim.style.width = button.initialWidth;
		iframeShim.style.height = button.initialHeight;
	}
}

function hideNativeHTMLButton(id){
	var button = document.getElementById(id);
	button.style.width = 0 + 'px';
	button.style.height = 0 + 'px';
	var iframeShim = document.getElementById("iframe_" + id);
	iframeShim.style.width = 0 + 'px';
	iframeShim.style.height = 0 + 'px';
}