package glueps.adaptors.ARbrowsers.service;

/* Copyright (c) GSIC-EMIC (see http://www.gsic.uva.es) */


import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.adaptors.ARbrowsers.adaptors.Utils;
import glueps.adaptors.ARbrowsers.model.ARbrowserResponse;
import glueps.adaptors.ARbrowsers.model.LoggedUser;
import glueps.adaptors.ARbrowsers.service.AuthService;
import glueps.adaptors.ARbrowsers.service.LogService;
import glueps.core.model.Deploy;
import glueps.core.model.LearningEnvironment;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.util.Series;

import bucket.model.Bucket;




public class Access
{
	
    public static ARbrowserResponse getLogin(String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress)
    {
		ARbrowserResponse response = null;
    	String username = options.getFirstValue("username");
 //   	String le = options.getFirstValue("vle");

	 
		 String uid = Utils.getDecodeAndEncode(options, "uid", null);
	
		 if ((username == null)&&(uid != null)){
			 //Initial login form is returned, to select a LE and insert a username. 
			 //If there are not learning environments, it is reported
			 
//			 List<LearningEnvironment> leList = new ArrayList<LearningEnvironment>();
//			 leList = AuthService.getLeList();
//			 if (leList != null){				 
				 
	             StringBuilder stringBuilder = new StringBuilder();
				 
			     stringBuilder.append("<HTML>");
			     stringBuilder.append("<HEAD>");
			     stringBuilder.append("<TITLE>GLUE!-PS AR Login page</TITLE>");
			     stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width\"/>");
			     stringBuilder.append("</HEAD>");
			     stringBuilder.append("<BODY>");
//			     stringBuilder.append("<img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"http://pandora.tel.uva.es/GLUEPSManager/gui/glueps/images/GLUEPSp.png\" alt=\"GLUE!-PS Logo\" />");
			     stringBuilder.append("<div><img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"" + Constants.SERVER_URL + "gui/glueps/arbrowsers/logo-gsic-alpha.png" + "\" alt=\"GLUE!-PS Logo\" /></div>");
			     stringBuilder.append("<br /><br /><br /><br />");
	
//			     stringBuilder.append("<H1>Welcome to the GLUE!-PS Login page for AR browsers!</H1>");
			     stringBuilder.append("<br /><br />");
	
//			     stringBuilder.append("<H4>Please, select a VLE and insert your username and password.</H4>");
//			     stringBuilder.append("<p><b>Por favor, selecciona la plataforma educativa e introduce tu nombre de usuario.</b></p>");
			     stringBuilder.append("<p><b>Por favor introduce tu nombre de usuario.</b></p>");
			     stringBuilder.append("<br />");
			     stringBuilder.append("<FORM ACTION=\"" + Constants.SERVER_URL + "arbrowser/" + appType + Constants.URL_LOGIN + "\" METHOD=\"POST\">");
//			     stringBuilder.append("Plataforma educativa: <SELECT NAME=\"vle\" SIZE=\"1\">");
////			     stringBuilder.append("Select a VLE: <SELECT NAME=\"vle\" SIZE=\"1\">");
//			     
//			     stringBuilder.append("<OPTION VALUE=\"\"></OPTION>");
//	
//			     for (LearningEnvironment leInList : leList)
//			     {
//			    	 String vleId = leInList.getId();
//			    	 String vleName = leInList.getName();
//			    	 if (leInList.getUserid() != null ){
//		    			 vleName = leInList.getName() + "(" + leInList.getUserid() + ")";
//			    	 }
//
//			    	 stringBuilder.append("<OPTION VALUE=\"" + vleId + "\">" + vleName + "</OPTION>");
//			     }
//			     
//			     
//			     stringBuilder.append("</SELECT><br /><br />");
			     
			     stringBuilder.append("<input type=\"hidden\" value=\"" + uid + "\" name=\"uid\" />");
			     
			     stringBuilder.append("<label>Usuario:</label> <input type=\"text\" name=\"username\" /><br />");
//			     stringBuilder.append("<label>Username:</label> <input type=\"text\" name=\"username\" /><br />");
			     stringBuilder.append("<input type=\"hidden\" value=\"glueps\" name=\"pwd\" />");
//			     stringBuilder.append("<p>Password:   <input type=\"password\" name=\"pwd\" /></p>");
			     stringBuilder.append("<BR>");
			     stringBuilder.append("<INPUT TYPE=\"submit\"><INPUT TYPE=\"Reset\">");
			     stringBuilder.append("</FORM>");
	
			     stringBuilder.append("</BODY>");
			     stringBuilder.append("</HTML>");
			     
			     String answer = stringBuilder.toString();
			     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML);	 
			     
//			 } else {
//				 //There are not learning environments in the database
//				 StringBuilder stringBuilder = new StringBuilder();
//				 
//			     stringBuilder.append("<HTML>");
//			     stringBuilder.append("<HEAD>");
//			     stringBuilder.append("<TITLE>GLUE!-PS AR Login page</TITLE>");
//			     stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width\"/>");
//			     stringBuilder.append("</HEAD>");
//			     stringBuilder.append("<BODY>");
//			     stringBuilder.append("<div><img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"" + Constants.SERVER_URL + "gui/glueps/arbrowsers/logo-gsic-alpha.png" + "\" alt=\"GLUE!-PS Logo\" /></div>");
//			     stringBuilder.append("<br /><br /><br /><br />");
//			     stringBuilder.append("<H2>Welcome to the GLUE!-PS Login page for AR browsers!</H1>");
//			     stringBuilder.append("<br /><br />");
//				 stringBuilder.append("There are not learning environments in the database<br /><br />");
//				 stringBuilder.append("<form action=\"" + Constants.SERVER_URL + "arbrowser/" + appType + Constants.URL_LOGIN + "\" METHOD=\"GET\">");
//				 stringBuilder.append("<input type=\"hidden\" value=\"" + uid + "\" name=\"uid\" />");
//				 stringBuilder.append("<input type=\"submit\" value=\"Return\">");
//				 stringBuilder.append("</form>");
//				 stringBuilder.append("</body>");
//				 stringBuilder.append("</html>");
//				 
//			     String answer = stringBuilder.toString();
//			     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML);
//			 
//			 }
			 
			 
			 //Creo que ya no se usa...
	/*	 } else if (uid != null){
			 //Logout window
			 StringBuilder stringBuilder = new StringBuilder();
		     stringBuilder.append("<HTML>");
		     stringBuilder.append("<HEAD>");
		     stringBuilder.append("<TITLE>GLUE!-PS AR Logout page</TITLE>");
		     stringBuilder.append("</HEAD>");
		     stringBuilder.append("<BODY>");
		     stringBuilder.append("<img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"http://pandora.tel.uva.es/GLUEPSManager/gui/glueps/images/GLUEPSp.png\" alt=\"GLUE!-PS Logo\" />");
		     stringBuilder.append("<br /><br />");
		     stringBuilder.append("<H2>Welcome to the GLUE!-PS Logout page for AR browsers!</H1>");
		     stringBuilder.append("<br /><br />");
			 stringBuilder.append("The user will be logged out of the device<br /><br />");
			 stringBuilder.append("<form action=\"" + Constants.configuration().getProperty("junaio.server.url", "http://localhost/GLUE_GLUEPSManager") + Constants.URL_LOGIN + "\" METHOD=\"POST\">");
			 stringBuilder.append("<input type=\"hidden\" value=\"" + uid + "\" name=\"uid\" />");
			 stringBuilder.append("<input type=\"submit\" value=\"Logout\">");
			 stringBuilder.append("</form>");
			 stringBuilder.append("</body>");
			 stringBuilder.append("</html>");
			 String answer = stringBuilder.toString();
			 ARbrowserResponse response = new ARbrowserResponse(answer, null, MediaType.TEXT_HTML); */
			 
			 
		 } else {
			 
			 //Error in get: missing parameters
//			 StringBuilder stringBuilder = new StringBuilder();
//			 stringBuilder.append("Error in HTTP GET: missing parameters");
//		     String answer = stringBuilder.toString();
//		     response = new ARbrowserResponse(answer, null, MediaType.TEXT_HTML); 
			 response = new ARbrowserResponse("Error in HTTP GET: missing parameters", Status.CLIENT_ERROR_BAD_REQUEST, null); 
		 }

		return response;
		 
	    }
	    
		
		
	 public static synchronized ARbrowserResponse postLogin (String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress) 
	 {
		 ARbrowserResponse response = null;
		 String deploy = options.getFirstValue( "deploy" );
		 String username = options.getFirstValue( "username" );
		 String password = options.getFirstValue( "pwd" );
	//	 String le = options.getFirstValue( "vle" );
		 String uid = Utils.getDecodeAndEncode(options, "uid", null);  
		 
		 if ((deploy != null)&&(deploy != "")&&(username != null)&&(uid != null)) {
			 
			 LoggedUser loggeduser = new LoggedUser();
			 HashMap<String,LoggedUser> loggedUsers = new HashMap<String,LoggedUser>();
			 loggedUsers = AuthService.readLoggedUsers();
			 if (loggedUsers != null){
				  loggeduser = loggedUsers.get(uid);
			 } else {
				  loggeduser = null;
					 
			 }
			 //OLD code, to remove:
//			 if (!AuthService.loggedUsers.isEmpty()){
//				 loggeduser = AuthService.loggedUsers.get(uid);
//			 } else {
//				 loggeduser = null;
//				 
//			 }
			 
		    	//debug problema uid
		   // 	System.out.println("LoginServlet añadir uid: " + uid);
		    	
			 if (loggeduser != null) {
				 StringBuilder stringBuilder = new StringBuilder();
			     
			     //debug:
				 //stringBuilder.append("El dispositivo ya está logado con la siguiente información: <br /> uid: " + loggeduser.getUid() + "<br />username: " + loggeduser.getUsername() + "<br />deploy ID: " + loggeduser.getDeployId() + "<br />timestamp: " + loggeduser.getTimestamp());
				 
			     stringBuilder.append("<HTML>");
			     stringBuilder.append("<HEAD>");
//			     stringBuilder.append("<TITLE>GLUE!-PS AR Login page</TITLE>");
			     stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width\"/>");
			     stringBuilder.append("</HEAD>");
			     stringBuilder.append("<BODY>");
			     stringBuilder.append("<div><img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"" + Constants.SERVER_URL + "gui/glueps/arbrowsers/logo-gsic-alpha.png" + "\" alt=\"GLUE!-PS Logo\" /></div>");
			     stringBuilder.append("<br /><br /><br /><br />");
//			     stringBuilder.append("<H2>Welcome to the GLUE!-PS Login page for AR browsers!</H1>");
			     stringBuilder.append("<br /><br />");
				 stringBuilder.append("<H4>Ya hay un usuario conectado al dispositivo. Por favor, salga primero (logout)</H4><br /><br />");
//				 stringBuilder.append("<H4>There is already a user logged in the device. Please, log out first</H4><br /><br />");
			//	 stringBuilder.append("<form action=\"" + "junaio://channel/switchChannel/?id=" + Security.JUNAIO_CHANNEL_ID + "\" METHOD=\"GET\">");
			//	 stringBuilder.append("<input type=\"submit\" value=\"OK\">");
			//	 stringBuilder.append("</form>");
				 stringBuilder.append("</body>");
				 stringBuilder.append("</html>");
				 
			     String answer = stringBuilder.toString();
			     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML);
				 
			 } else {
				 loggeduser = new LoggedUser(uid, username, deploy, Constants.POS_TYPE_GEOPOSITION);
				 AuthService.saveLoggedUser(loggeduser);
				// AuthService.loggedUsers.put(uid, loggeduser);
				 
	
				 //Log
				 String logfile = Constants.ACCESS_LOGFILE;
				 String action = "login";
				 LogService.writelog(appType, options, remoteIpAddress, uid, action, null, logfile);
				 
				 
				 StringBuilder stringBuilder = new StringBuilder();
			     
			     //debug:
			     //stringBuilder.append("Nuevo usuario añadido al registro con la siguiente informaci�n: <br /> uid: " + loggeduser.getUid() + "<br />username: " + loggeduser.getUsername() + "<br />deploy ID: " + loggeduser.getDeployId() + "<br />timestamp: " + loggeduser.getTimestamp());
			 	//     Writer out = response.getWriter();
			 	//    response.setContentType(Constants.CONTENT_TYPE_HTML);
			 	//	 stringBuilder.append("deploy ID: " + deploy + "<br />username: " + username + "<br />uid: " + uid);
			 	//	 out.close();
			     
			     stringBuilder.append("<HTML>");
			     stringBuilder.append("<HEAD>");
//			     stringBuilder.append("<TITLE>GLUE!-PS AR Login page</TITLE>");
			     stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width\"/>");
			     stringBuilder.append("</HEAD>");
			     stringBuilder.append("<BODY>");
			     stringBuilder.append("<div><img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"" + Constants.SERVER_URL + "gui/glueps/arbrowsers/logo-gsic-alpha.png" + "\" alt=\"GLUE!-PS Logo\" /></div>");
			     stringBuilder.append("<br /><br /><br /><br />");
//			     stringBuilder.append("<H2>Welcome to the GLUE!-PS Login page for AR browsers!</H1>");
			     stringBuilder.append("<br /><br />");
			     stringBuilder.append("<H4>Usuario conectado!</H4><br /><br />");
//			     stringBuilder.append("<H4>User logged!</H4><br /><br />");
			//	 stringBuilder.append("<H4>User logged!<br />Please, reload the channel in the Junaio menu</H4><br /><br />");
			//	 stringBuilder.append("<form action=\"" + "junaio://channel/switchChannel/?id=" + Security.JUNAIO_CHANNEL_ID + "\" METHOD=\"GET\">");
			//	 stringBuilder.append("<input type=\"submit\" value=\"OK\">");
			//	 stringBuilder.append("</form>");
				 stringBuilder.append("</body>");
				 stringBuilder.append("</html>");
			     
			     
			     String answer = stringBuilder.toString();
			     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML); 
			 }
		 } else if (((deploy == null)||(deploy == ""))&&(username != null)&&(uid != null)) {
			 //Second form to select deploy is returned
			 

			 
			 
			 
			 List<Deploy> deployList = new ArrayList<Deploy>();
		//	 deployList = AuthService.getDeployListByUsername(username);
			 ArrayList<String> positionTypes = new ArrayList<String>();
			 positionTypes.add(Constants.POS_TYPE_JUNAIOMARKER);
			 positionTypes.add(Constants.POS_TYPE_GEOPOSITION);
			 deployList = AuthService.getDeployListByUsernamePositionType(username, positionTypes);
	//		 deployList = AuthService.getDeployListByUsernameAndLEnv(username, le);
			 
		 
			 if ((deployList != null)&&(deployList.toString() != "[]") && (password.equals(Constants.ARBROWSERS_PASSWORD))) {
				 
				 StringBuilder stringBuilder = new StringBuilder();
				 
			     stringBuilder.append("<HTML>");
			     stringBuilder.append("<HEAD>");
//			     stringBuilder.append("<TITLE>GLUE!-PS AR Login page</TITLE>");
			     stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width\"/>");
			     stringBuilder.append("</HEAD>");
			     stringBuilder.append("<BODY>");
			     stringBuilder.append("<div><img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"" + Constants.SERVER_URL + "gui/glueps/arbrowsers/logo-gsic-alpha.png" + "\" alt=\"GLUE!-PS Logo\" /></div>");
			     stringBuilder.append("<br /><br /><br /><br />");
	
//			     stringBuilder.append("<H2>Welcome to the GLUE!-PS Login page for AR browsers!</H1>");
			     stringBuilder.append("<br /><br />");
	
			     stringBuilder.append("<H4>Por favor, selecciona el curso.</H4>");
//			     stringBuilder.append("<H4>Please, select the course to view the AR resources.</H4>");
			     stringBuilder.append("<br />");
			     stringBuilder.append("<FORM ACTION=\"" + Constants.SERVER_URL + "arbrowser/" + appType + Constants.URL_LOGIN + "\" METHOD=\"POST\">");
//			     stringBuilder.append("Select a course: <SELECT NAME=\"deploy\" SIZE=\"1\">");
			     stringBuilder.append("Selecciona un curso: <SELECT NAME=\"deploy\" SIZE=\"1\">");
			     
			     stringBuilder.append("<OPTION VALUE=\"\"></OPTION>");
	
			     for (Deploy deployInList : deployList)
			     {
			    	 String deployId = deployInList.getTrimmedId();
			    	 String deployName = deployInList.getName();
			    	 stringBuilder.append("<OPTION VALUE=\"" + deployId + "\">" + deployName + "</OPTION>");
			     }
			     
			     
			     stringBuilder.append("</SELECT><br /><br />");
			     
			     stringBuilder.append("<input type=\"hidden\" value=\"" + uid + "\" name=\"uid\" />");
			     stringBuilder.append("<input type=\"hidden\" value=\"" + username + "\" name=\"username\" />");
//				 stringBuilder.append("<input type=\"hidden\" value=\"" + le + "\" name=\"vle\" />");
	
			     stringBuilder.append("<INPUT TYPE=\"submit\"><INPUT TYPE=\"Reset\">");
			     stringBuilder.append("</FORM>");
	
			     stringBuilder.append("</BODY>");
			     stringBuilder.append("</HTML>");
			     
			     String answer = stringBuilder.toString();
			     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML);
				 
			 } else {
				 //There are not deploys in database with instances activities for the username or username/password are incorrect
				 StringBuilder stringBuilder = new StringBuilder();
			     stringBuilder.append("<HTML>");
			     stringBuilder.append("<HEAD>");
//			     stringBuilder.append("<TITLE>GLUE!-PS AR Login page</TITLE>");
			     stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width\"/>");
			     stringBuilder.append("</HEAD>");
			     stringBuilder.append("<BODY>");
			     stringBuilder.append("<div><img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"" + Constants.SERVER_URL + "gui/glueps/arbrowsers/logo-gsic-alpha.png" + "\" alt=\"GLUE!-PS Logo\" /></div>");
			     stringBuilder.append("<br /><br /><br /><br />");
//			     stringBuilder.append("<H2>Welcome to the GLUE!-PS Login page for AR browsers!</H1>");
			     stringBuilder.append("<br /><br />");
//				 stringBuilder.append("There are not courses in the selected learning environment for the username, or username/password are incorrect.<br /><br />");
				 stringBuilder.append("No hay cursos en la plataforma de aprendizaje seleccionada para el usuario introducido.<br /><br />");
				 stringBuilder.append("<form action=\"" + Constants.SERVER_URL + "arbrowser/" + appType + Constants.URL_LOGIN + "\" METHOD=\"GET\">");
				 stringBuilder.append("<input type=\"hidden\" value=\"" + uid + "\" name=\"uid\" />");
				 stringBuilder.append("<input type=\"submit\" value=\"Return\">");
				 stringBuilder.append("</form>");
				 
				 //	debug
			//	 stringBuilder.append("debug<br />username: " + username + "<br />uid: " + uid);
				 
				 stringBuilder.append("</body>");
				 stringBuilder.append("</html>");
				 
			     String answer = stringBuilder.toString();
			     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML); 
				 
			 }
			 
	
			 
			 
			 
		 } else {
			 
			 //Error in get: missing parameters
			 StringBuilder stringBuilder = new StringBuilder();
			 stringBuilder.append("Error in HTTP GET: missing parameters");
		     String answer = stringBuilder.toString();
		     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML); 
			 
		 }
		 
	
		 return response;
		 
	 }
	
		public static ARbrowserResponse getLogout(String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress)
	    {
		 ARbrowserResponse response = null;
		 String uid = Utils.getDecodeAndEncode(options, "uid", null);  
		 
		 
		 if (uid != null){
			 //Logout window
			 StringBuilder stringBuilder = new StringBuilder();
		     stringBuilder.append("<HTML>");
		     stringBuilder.append("<HEAD>");
		     stringBuilder.append("<TITLE>GLUE!-PS AR Logout page</TITLE>");
		     stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width\"/>");
		     stringBuilder.append("</HEAD>");
		     stringBuilder.append("<BODY>");
		     stringBuilder.append("<div><img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"" + Constants.SERVER_URL + "gui/glueps/arbrowsers/logo-gsic-alpha.png" + "\" alt=\"GLUE!-PS Logo\" /></div>");
		     stringBuilder.append("<br /><br /><br /><br />");
//		     stringBuilder.append("<H2>Welcome to the GLUE!-PS Logout page for AR browsers!</H1>");
		     stringBuilder.append("<br /><br />");
		//	 stringBuilder.append("<H4>The user will be logged out. Please, confirm!</H4><br /><br />");
		     stringBuilder.append("<H4>Por favor, confirme que quiere salir</H4><br /><br />");
			 stringBuilder.append("<form action=\"" + Constants.SERVER_URL + "arbrowser/" + appType + Constants.URL_LOGOUT + "\" METHOD=\"POST\">");
			 stringBuilder.append("<input type=\"hidden\" value=\"" + uid + "\" name=\"uid\" />");
		//	 stringBuilder.append("<input type=\"submit\" value=\"Logout\">");
			 stringBuilder.append("<input type=\"submit\" value=\"Salir\">");
			 stringBuilder.append("</form>");
			 stringBuilder.append("</body>");
			 stringBuilder.append("</html>");
		     String answer = stringBuilder.toString();
		     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML); 
			 
			 
		 } else {
			 
			 //Error in get: missing parameters
			 StringBuilder stringBuilder = new StringBuilder();
			 stringBuilder.append("Error in HTTP GET: missing parameters");
		     String answer = stringBuilder.toString();
		     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML); 
			 
		 }
		 
		 return response;
		 
	    }
		
		
		public static synchronized ARbrowserResponse postLogout(String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress)
	 {
		 
		 ARbrowserResponse response = null;
		 String uid = Utils.getDecodeAndEncode(options, "uid", null);  

		 
		 
		 if (uid != null) {
			 
			 HashMap<String,LoggedUser> loggedUsers = new HashMap<String,LoggedUser>();
			 loggedUsers = AuthService.readLoggedUsers();
			 if (loggedUsers != null){
				 if ((!loggedUsers.isEmpty())&&(loggedUsers.get(uid) != null)){			 
					 
					 //Log
					 String logfile = Constants.ACCESS_LOGFILE;
					 String action = "logout";
					 LogService.writelog(appType, options, remoteIpAddress, uid, action, null, logfile);
					 
					 //Logout
					 LoggedUser user = new LoggedUser();
					 user = loggedUsers.get(uid);
					 AuthService.removeLoggedUser(user);
			//		 AuthService.loggedUsers.remove(uid);
					 
					 //User logged out
					 StringBuilder stringBuilder = new StringBuilder();
				     stringBuilder.append("<HTML>");
				     stringBuilder.append("<HEAD>");
//				     stringBuilder.append("<TITLE>GLUE!-PS AR Logout page</TITLE>");
				     stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width\"/>");
				     stringBuilder.append("</HEAD>");
				     stringBuilder.append("<BODY>");
				     stringBuilder.append("<div><img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"" + Constants.SERVER_URL + "gui/glueps/arbrowsers/logo-gsic-alpha.png" + "\" alt=\"GLUE!-PS Logo\" /></div>");
				     stringBuilder.append("<br /><br /><br /><br />");
//				     stringBuilder.append("<H2>Welcome to the GLUE!-PS Logout page for AR browsers!</H1>");
				     stringBuilder.append("<br /><br />");
				     stringBuilder.append("<H4>Usuario desconectado!</H4><br /><br />");
//				     stringBuilder.append("<H4>User logged out!<br />Please, reload the channel in the menu if login control do not appear</H4><br /><br />");
				//	 stringBuilder.append("<H4>User logged out!<br />Please, reload the channel in the Junaio menu</H4><br /><br />");
				//     stringBuilder.append("<H4>Por favor, cierre esta ventana y recarge el canal de junaio</H4><br /><br />");
				//	 stringBuilder.append("<form action=\"" + "junaio://channel/switchChannel/?id=" + Security.JUNAIO_CHANNEL_ID + "\" METHOD=\"GET\">");
				//	 stringBuilder.append("<input type=\"submit\" value=\"OK\">");
				//	 stringBuilder.append("</form>");
					 stringBuilder.append("</body>");
					 stringBuilder.append("</html>");
				     String answer = stringBuilder.toString();
				     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML); 
					 
				 } else {
					 //User was not logged
					 StringBuilder stringBuilder = new StringBuilder();
				     stringBuilder.append("<HTML>");
				     stringBuilder.append("<HEAD>");
//				     stringBuilder.append("<TITLE>GLUE!-PS AR Logout page</TITLE>");
				     stringBuilder.append("<meta name=\"viewport\" content=\"width=device-width\"/>");
				     stringBuilder.append("</HEAD>");
				     stringBuilder.append("<BODY>");
				     stringBuilder.append("<div><img style=\"float: left;padding: 30px;\" title=\"GLUE!-PS Logo\" src=\"" + Constants.SERVER_URL + "gui/glueps/arbrowsers/logo-gsic-alpha.png" + "\" alt=\"GLUE!-PS Logo\" /></div>");
				     stringBuilder.append("<br /><br /><br /><br />");
//				     stringBuilder.append("<H2>Welcome to the GLUE!-PS Logout page for AR browsers!</H1>");
				     stringBuilder.append("<br /><br />");
					 stringBuilder.append("<H4>El usuario no estaba conectado en el dispositivo.</H4><br /><br />");
//					 stringBuilder.append("<H4>User was not logged in the device.</H4><br /><br />");
			//		 stringBuilder.append("<form action=\"" + "junaio://channel/switchChannel/?id=" + Security.JUNAIO_CHANNEL_ID + "\" METHOD=\"GET\">");
			//		 stringBuilder.append("<input type=\"submit\" value=\"OK\">");
			//		 stringBuilder.append("</form>");
					 stringBuilder.append("</body>");
					 stringBuilder.append("</html>");
				     String answer = stringBuilder.toString();
				     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML); 
					 
				 }

			 }
		 } else {
			 
			 //Error in get: missing parameters
			 StringBuilder stringBuilder = new StringBuilder();
			 stringBuilder.append("Error in HTTP GET: missing parameters");
		     String answer = stringBuilder.toString();
		     response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.TEXT_HTML); 
			 
		 }
		 
		 return response;
		 
	 }
		
	
	
}
