var appletcomUrl = null;

function getQuery() {
	if (opener.EditToolDialog != null) {
		var query = opener.EditToolDialog.getCurrentQuery();
		return query != null ? query.query : "";
	} else {
		return "";
	}
}

function setQuery(query, summary) {
	opener.OntoolsearchDialog.setResult(appletcomUrl, {
		query : query,
		summary : summary
	});
	appletcomUrl = null;
}

function setTool(ontoolURL) {
	appletcomUrl = ontoolURL;
}

function initApplet() {
	var attributes = {
		code : 'osclient.launch.OntoolsearchApplet.class',
		archive : 'osclient.jar',
		width : 1060,
		height : 730
	};
	var parameters = {
		jnlp_href : 'osclient.jnlp'
	};
	var version = '1.6';
	deployJava.runApplet(attributes, parameters, version);
}

