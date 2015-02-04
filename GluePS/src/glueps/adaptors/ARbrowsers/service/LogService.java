package glueps.adaptors.ARbrowsers.service;

import glueps.adaptors.ARbrowsers.adaptors.Utils;
import glueps.adaptors.ARbrowsers.model.LoggedUser;
import glueps.core.model.Deploy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.restlet.data.Form;

public class LogService {

	   //Log generation
    public static void writelog(String appType, Form options, String remoteIpAddress, String uid, String action, String author, String logfile){
    	
    	
		 //Log
		 //Variables for log
		 String deployid = null;
		 Deploy deployobject = null;
		 String deployname = null;
		 String username = null;
		 
    	if (appType.equals("earth")){
			 deployid = Utils.getDecodeAndEncode(options, "deployid", null);
			 if (deployid != null){
				 deployobject = AuthService.getDeploybyID(deployid);
				 if (deployobject != null){
					 deployname = deployobject.getName();	
				 }
			 }

	 
			 username = Utils.getDecodeAndEncode(options, "username", null);
    	} else {
   		 LoggedUser loggeduser = null;
		 HashMap<String,LoggedUser> loggedUsers = AuthService.readLoggedUsers();
		 if (loggedUsers != null && !loggedUsers.isEmpty() && uid!=null){
			 if (loggedUsers.containsKey(uid)) {
				 loggeduser = loggedUsers.get(uid);
			 }
		 }


		 if (loggeduser != null) {
			 deployid = loggeduser.getDeployId();
			 deployobject = AuthService.getDeploybyID(deployid);
			 if (deployobject != null){
				 deployname = deployobject.getName();	
			 }
	 
			 username = loggeduser.getUsername();  //Login, Logout
		 }
    	}

		 Date date = new Date();
		 String log = null;
		 
		 //Log generation, depending on kind of log (action)
		 if ((action.equals("login")) || (action.equals("logout"))){
			 
			//Log: Date;action;deployid;deployname;username;deviceId (hash);IP address		
			 //TODO ipaddress in this case is always the internal ip address of the glueps-ar server, due to it triggers the login/logout HTTP request 
			 //It should be changed to report the remote ip address of the user
			 log = 	date + ";" +
			 	appType + ";" +
				action + ";" +
				deployid + ";" +
				deployname + ";" +
				username + ";" +
				uid.hashCode() + ";" +
				remoteIpAddress + "\r\n";	
		 } else {
			 //Currently, interaction logs in Junaio Servlet (junaio events)
			 
			 String lparameter = options.getFirstValue("l");
			 String id = Utils.getDecodeAndEncode(options, "id", null); 
			 if (id == null){
				 //Junaio AREL
				 id = Utils.getDecodeAndEncode(options, "filter_id", null);
			 }
			 String toolInstanceName = null;
			 if (deployobject != null){
				 if (deployobject.getToolInstanceByTrimmedId(id) != null){
					 toolInstanceName = deployobject.getToolInstanceByTrimmedId(id).getName();
				 }
			 }

			 boolean filtered = false;
			 if (author != null){
				 filtered = true;
			 }
			 
			//Log: Date;deployid;deployname;filtered;author;deviceId (hash);IP address;geoposition;ToolInstanceId;ToolInstanceName
			 log = 	date + ";" +
			 	appType + ";" +
				action + ";" +
				deployid + ";" +
				deployname + ";" +
				filtered + ";" +
				author + ";" +
				username + ";" +
				uid.hashCode() + ";" +
				remoteIpAddress + ";" +
				lparameter + ";" +
				id + ";" +
				toolInstanceName + "\r\n";
		 }
		 //debug
		 System.out.println("LOG: " + log);
		 
		 //Write log			 
		 if ((logfile != null) && (logfile != "")) {
			 writelogToFile (logfile, log);
		 } else {
			 System.out.println("Log file must be indicated to generate log");
		 }
	 
		 // End log
    	
    	

  	
  }
    
    private static void writelogToFile (String logfile, String log){
		 try {
			  // Create file 
			  FileWriter fstream = new FileWriter(logfile,true);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(log);
			  //Close the output stream
			  out.close();	

		}catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		}
    }
    
	
	
}
