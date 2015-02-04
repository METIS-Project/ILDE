package glueps.core.resource;

import glueps.adaptors.ARbrowsers.adaptors.junaio.JunaioAdaptor;
import glueps.adaptors.ARbrowsers.adaptors.junaio.JunaioArelAdaptor;
import glueps.adaptors.ARbrowsers.adaptors.layar.LayarAdaptor;
import glueps.adaptors.ARbrowsers.adaptors.mixare.MixareAdaptor;
import glueps.adaptors.ARbrowsers.adaptors.google.GoogleEarthAdaptor;
import glueps.adaptors.ARbrowsers.model.ARbrowserResponse;
import glueps.adaptors.ARbrowsers.service.Access;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

public class PoiListResource extends GLUEPSResource {


    @Get()
    public Representation getPOis() throws MalformedURLException  {
    	
    	Representation answer = null;

    	//Extraction of parameters from HTTP request
    //	Method method = getMethod();
    	Form options = getRequest().getResourceRef().getQueryAsForm();
    	String appType = (String) getRequest().getAttributes().get("appType");
    	String operation = (String) getRequest().getAttributes().get("operation");
    	String uri = getReference().toString();
    	Series<Parameter> headers = (Series<Parameter>) getRequest().getAttributes().get("org.restlet.http.headers");
    	//Get remote IP address including cases with redirection    	
    	String remoteIpAddress = getRequest().getClientInfo().getAddress();
    	List<String> addresses = getRequest().getClientInfo().getForwardedAddresses();
    	if (headers.getFirstValue("X-Forwarded-For") != null) {
    		String[] ips;				 
    		String delimiter = ",";
    		String remoteIpAddressess = headers.getFirstValue("X-Forwarded-For");
    		ips = remoteIpAddressess.split(delimiter);
    		remoteIpAddress = ips[0];
    	}
	   	 if (addresses.size() > 0) {
	   		remoteIpAddress = addresses.get(addresses.size()-1);
	   	 }
    	
    	
    	//debug
//    	System.out.println("apptype: " + appType);
//    	System.out.println("operation: " + operation);
//    	System.out.println("uri: " + uri);
//    	System.out.println("addresses: " + addresses);
//    	System.out.println("remoteIpAddress: " + remoteIpAddress);

   // 	System.out.println("method: " + method);

    	
		ARbrowserResponse arbrowserResponse = new ARbrowserResponse();
		
		// Operation is for login/logout
		if (operation.equals("login")){
			if (appType.equals("earth")){
				arbrowserResponse = GoogleEarthAdaptor.login(appType, uri, options, headers, remoteIpAddress);
			} else {
				arbrowserResponse = Access.getLogin(appType, uri, options, headers, remoteIpAddress);
			}
			
		} else 		if (operation.equals("logout")){
			arbrowserResponse = Access.getLogout(appType, uri, options, headers, remoteIpAddress);

		//Operation is search POIs
		} else if (operation.equals("search")){				
			//Selecting the ARbrowser adaptor
			if (appType.equals("junaio") || appType.equals("junaioglue")) {
				arbrowserResponse = JunaioAdaptor.getPois(appType, uri, options, headers, remoteIpAddress);					
			} else if (appType.equals("junaioarel") || appType.equals("junaioarelglue")) {
				arbrowserResponse = JunaioArelAdaptor.getPois(appType, uri, options, headers, remoteIpAddress);
			} else if (appType.equals("layar") || appType.equals("layarvision")) {
				arbrowserResponse = LayarAdaptor.getPois(appType, uri, options, headers, remoteIpAddress);	
			} else if (appType.equals("mixare")) {
				arbrowserResponse = MixareAdaptor.getPois(appType, uri, options, headers, remoteIpAddress);	
			} else if (appType.equals("earth")) {
				arbrowserResponse = GoogleEarthAdaptor.getPois(appType, uri, options, headers, remoteIpAddress);	
			} else {
				arbrowserResponse = new ARbrowserResponse("Error in the URL Path",Status.CLIENT_ERROR_NOT_FOUND, null);
			}
    	
		} else {
			arbrowserResponse = new ARbrowserResponse("Error in the URL Path",Status.CLIENT_ERROR_NOT_FOUND, null);			
		}
    	
    	
		//Generate response
		if (arbrowserResponse != null){
	    	System.out.println(arbrowserResponse.getStatus());
	    	if (arbrowserResponse.getStatus() == Status.SUCCESS_OK){
	    		answer = new StringRepresentation(arbrowserResponse.getAnswer(), arbrowserResponse.getMediaType());
	    		answer.setCharacterSet(CharacterSet.UTF_8);
	    		return answer;
	    	} else {
	    		
	    		throw new ResourceException(arbrowserResponse.getStatus(), arbrowserResponse.getAnswer());
	    	}
	    //	throw new ResourceException(arbrowserResponse.getStatus(), arbrowserResponse.getAnswer());
	    	
		} else {
			System.out.println("Nothing to return");
    		return answer;
		}

    }
    
    @Post("form")
    public Representation postPOis(Form options) throws MalformedURLException  {
    	
    	Representation answer = null;   	

   // 	Method method = getMethod();
      	String appType = (String) getRequest().getAttributes().get("appType");
    	String operation = (String) getRequest().getAttributes().get("operation");
    	String uri = getReference().toString();
    	Series<Parameter> headers = (Series<Parameter>) getRequest().getAttributes().get("org.restlet.http.headers");
    	//Get remote IP address including cases with redirection    	
    	String remoteIpAddress = getRequest().getClientInfo().getAddress();
    	List<String> addresses = getRequest().getClientInfo().getForwardedAddresses();
    	if (headers.getFirstValue("X-Forwarded-For") != null) {
    		String[] ips;				 
    		String delimiter = ",";
    		String remoteIpAddressess = headers.getFirstValue("X-Forwarded-For");
    		ips = remoteIpAddressess.split(delimiter);
    		remoteIpAddress = ips[0];
    	}
	   	 if (addresses.size() > 0) {
	   		remoteIpAddress = addresses.get(addresses.size()-1);
	   	 }
    	
    	
    	//debug
//    	System.out.println("apptype: " + appType);
//    	System.out.println("operation: " + operation);
//    	System.out.println("uri: " + uri);
//    	System.out.println("remoteIpAddress: " + remoteIpAddress);
  //  	System.out.println("method: " + method);

    	ARbrowserResponse arbrowserResponse = new ARbrowserResponse();
    	
    	
    	
		// Operation is for login/logout
		if (operation.equals("login")){
			arbrowserResponse = Access.postLogin(appType, uri, options, headers, remoteIpAddress);
			
		} else if (operation.equals("logout")){
			//TODO change postLogout to use a DEL method, and the corresponding HTML form in getLogout for generating a DEL instead of a POST
			//(maybe using javascript, due to HTML Forms only accept GET/POST)
			arbrowserResponse = Access.postLogout(appType, uri, options, headers, remoteIpAddress);	
    	
		} else if (operation.equals("event")){
	
			//Selecting the ARbrowser adaptor (currently only junaio supports events)
			if (appType.equals("junaio") || appType.equals("junaioglue")) {
				JunaioAdaptor.createLog(appType, uri, options, headers, remoteIpAddress);
				arbrowserResponse = new ARbrowserResponse("Log entry created",Status.SUCCESS_OK, null);
			} else if (appType.equals("earth")){
				GoogleEarthAdaptor.createLog(appType, uri, options, headers, remoteIpAddress);
				arbrowserResponse = new ARbrowserResponse("Log entry created",Status.SUCCESS_OK, null);
			} else {
				arbrowserResponse = new ARbrowserResponse("Method not allowed",Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, null);
			}
		//Operation not supported
		} else {
			arbrowserResponse = new ARbrowserResponse("Error in the URL Path",Status.CLIENT_ERROR_NOT_FOUND, null);
			
		}

    	
		//Generate response
    	System.out.println(arbrowserResponse.getStatus());
    	if (arbrowserResponse.getStatus() == Status.SUCCESS_OK){
    		answer = new StringRepresentation(arbrowserResponse.getAnswer(), arbrowserResponse.getMediaType());
    		answer.setCharacterSet(CharacterSet.UTF_8);
    		return answer;
    	} else {
    		
    		throw new ResourceException(arbrowserResponse.getStatus(), arbrowserResponse.getAnswer());
    	}
    //	throw new ResourceException(arbrowserResponse.get
    	
    }
	


	
	
}
