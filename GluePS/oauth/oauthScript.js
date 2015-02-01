if (firstOauthScript == true) {
	firstOauthScript = false;

	// We declare the scope of viewing the user email
	var googleOauthScopes = "https://www.googleapis.com/auth/userinfo.email";
	
	
	var urlToOpen = "";//the url of the resource which is going to be opened

	function handleClientLoad(url) {
		if (urlToOpen != ""){
			alert("Por favor, intente de nuevo abrir el recurso una vez que se termine de cargar el anterior");
		}else{
			urlToOpen = url;
			// Step 2: Reference the API key
			gapi.client.setApiKey(googleOauthApiKey);
			window.setTimeout(checkAuth, 1);
		}
	}

	function checkAuth() {
		gapi.auth.authorize({
			client_id : googleOauthClientId,
			scope : googleOauthScopes,
			immediate : true
		}, handleAuthResult);
	}

	function handleAuthResult(authResult) {
		if (authResult && !authResult.error) {
			makeApiCall();
		} else {
			var conf = window.confirm("Para ver este recurso es necesario que inicie sesi\u00f3n con su cuenta de Google para conocer su identidad");
			if (conf == true) {
				handleAuthClick();
			}
		}
	}

	function handleAuthClick(event) {
		// Step 3: get authorization to use private data
		gapi.auth.authorize({
			client_id : googleOauthClientId,
			scope : googleOauthScopes,
			immediate : false
		}, handleAuthResult);
		return false;
	}

	// Load the API and make an API call. Display the results on the screen.
	function makeApiCall() {
		// Step 4: Load the Blogger API
		gapi.client.load("oauth2", "v2", function() {
			// Step 5: Assemble the API request
			var request = gapi.client.oauth2.userinfo.get({});

			// Step 6: Execute the API request
			request.execute(function(resp) {
				var bloggerEmail = resp.email;
				var bloggerUserId = resp.id;
				var bloggerDisplayName = resp.displayName;
				var resourceWindow = window.open(urlToOpen.replace("callerUser=bloggerUser", "callerUser=" + encodeURIComponent(bloggerEmail)), "_blank");
				resourceWindow.focus();
				urlToOpen = ""; //reset the value
			});
		});
	}

	function updateLocations(bloggerEmail) {
		var links = document.getElementsByTagName("a");
		for ( var i = 0; i < links.length; i++) {
			var href = links[i].getAttribute("href");
			if (href != null && href.indexOf("/GLUEletManager/instance/") != -1) {
				links[i].setAttribute("href", href.replace("callerUser=bloggerUser", "callerUser="+ encodeURIComponent(bloggerEmail)));
				links[i].style.display = "";
			}
		}
	}

	function hideLocations() {
		var hide = false;
		var links = document.getElementsByTagName("a");
		for ( var i = 0; i < links.length; i++) {
			var href = links[i].getAttribute("href");
			if (href != null && href.indexOf("/GLUEletManager/instance/") != -1) {
				links[i].style.display = "none";
				hide = true;
			}
		}
		return hide;
	}

	// Step 1: Load JavaScript client library
	document.write("<script src=\"https://apis.google.com/js/client.js\"><\/script>");
}