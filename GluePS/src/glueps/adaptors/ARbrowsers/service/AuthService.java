/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glueps.adaptors.ARbrowsers.service;

import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.adaptors.ARbrowsers.adaptors.Utils;
import glueps.adaptors.ARbrowsers.model.ActiveUser;
import glueps.adaptors.ARbrowsers.model.LoggedUser;
import glueps.core.model.Deploy;
import glueps.core.model.InstancedActivity;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.Participant;
import glueps.core.model.ToolInstance;
import glueps.core.persistence.JpaManager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.restlet.data.Form;

import bucket.model.ArtifactType;




public class AuthService
{
	private static List<LearningEnvironment> leList;
	public static HashMap<String,LoggedUser> loggedUsers = new HashMap<String,LoggedUser>();
	
	public static HashMap<String, ActiveUser> activeUsersDb = new HashMap<String, ActiveUser>();
	
	public static List<ActiveUser> getActiveUsersFromDeployId(String deployId){
		
		List<ActiveUser> list = null;
        Iterator it = AuthService.activeUsersDb.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            ActiveUser userIt = (ActiveUser) entry.getValue();
            
            //If user is logged in the deployId is added to the list
            if (userIt.getDeployId().equals(deployId)){
            	if (list == null){
            		list = new ArrayList<ActiveUser>();
            	}
            	list.add(userIt);            	
            }
        }	        

	
		return list;
	}
	
	//Method to obtain the list of virtual environments
	//TODO: add refresh to LEEntity in JPAManager to synchronize LEEntity with database in modifications from glueps GUI
	public static List<LearningEnvironment> getLeList() {
		JpaManager dbmanager = JpaManager.getInstance();
		
		try {
			if (dbmanager.listLEObjects() != null){
				leList = dbmanager.listLEObjects();
			} else {
				leList = null;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return leList;
	}
	
	//Method to obtain a List of deploy objects wherein a participant has instance activities, knowing the participant username
	//TODO: add refresh to DeployList in JPAManager to synchronize DeployListEntity with database in modifications from glueps GUI
	public static List<Deploy> getDeployListByUsername(String username){
		List<Deploy> deployList= new ArrayList<Deploy>();
		JpaManager dbmanager = JpaManager.getInstance();
		try {
			List<Deploy> allDeploys = dbmanager.listDeployObjects();
			if (allDeploys != null){
				for(Iterator<Deploy> it = allDeploys.iterator();it.hasNext();){
					Deploy dep = it.next();
					if (dep.getInstancedActivitiesForUsername(username) != null){
						deployList.add(dep);
					}
				}
			} else {
				deployList = null;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return deployList;
	}
	
	//Method to obtain a List of deploy objects wherein a participant has instance activities, knowing the participant username
	//TODO: add refresh to DeployList in JPAManager to synchronize DeployListEntity with database in modifications from glueps GUI
	public static List<Deploy> getDeployListByUsernamePositionType(String username, ArrayList<String> positionTypes){
		List<Deploy> deployList= new ArrayList<Deploy>();
		JpaManager dbmanager = JpaManager.getInstance();
		try {
			List<Deploy> allDeploys = dbmanager.listDeployObjects();
			if (allDeploys != null){
				for(Iterator<Deploy> it = allDeploys.iterator();it.hasNext();){
					Deploy dep = it.next();
					if (dep.getInstancedActivitiesForUsernamePositionType(username, positionTypes).size() > 0)
					{
						deployList.add(dep);
					}
				}
			} else {
				deployList = null;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return deployList;
	}
	
	//Method to obtain a List of deploy objects wherein a participant has instance activities, knowing the participant username
	//TODO: add refresh to DeployList in JPAManager to synchronize DeployListEntity with database in modifications from glueps GUI
	public static List<Deploy> getDeployListByUsernameAndLEnv(String username, String LEnv){
		List<Deploy> deployList= new ArrayList<Deploy>();
		JpaManager dbmanager = JpaManager.getInstance();
		try {
			if (dbmanager.listDeployObjects() != null){
				List<Deploy> allDeploys = dbmanager.listDeployObjects();
				for(Iterator<Deploy> it = allDeploys.iterator();it.hasNext();){
					Deploy dep = it.next();
					if (dep.getInstancedActivitiesForUsername(username) != null && dep.getLearningEnvironment().getId().equals(LEnv)){
						deployList.add(dep);
					}
				}
			} else {
				deployList = null;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return deployList;
	}
	
	public static Deploy getDeploybyID(String deployid){
		JpaManager dbmanager = JpaManager.getInstance();
		return dbmanager.findDeployObjectById(deployid);
	}
	
	
	public static String getStaffFromDeploy (Deploy deploy) {
		String username = null;
		if (deploy.getStaffUsernames() != null){
			String[] staffs = deploy.getStaffUsernames();
			username = staffs[0];
		}	
		
		return username;
	
	}
	public static String getStudentFromDeploy (Deploy deploy) {
		ArrayList<Participant> participants = deploy.getParticipants();
		String username = null;
		if (participants != null){
			for(Iterator<Participant> it = participants.iterator();it.hasNext();){
				Participant p = it.next();
				if (!p.isStaff()){
					username = p.getName();
					System.out.println("Student selected: " + username);
					break;
				}
			}
			
		}	
		
		return username;
	
	}
	
	public static List<Deploy> getDeploysfromAuthor (String author) {
		List<Deploy> deploys= new ArrayList<Deploy>();
		JpaManager dbmanager = JpaManager.getInstance();
			try {
				if (dbmanager.listDeployObjectsByUser(author) != null){
					 deploys = dbmanager.listDeployObjectsByUser(author);

				} else {
					deploys = null;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		return deploys;
	}
	
	public static String getUidByUsername(String username) {
		String uid = null;
		HashMap<String,LoggedUser> loggedUsers = new HashMap<String,LoggedUser>();
		loggedUsers = readLoggedUsers();
		if (loggedUsers != null){
			LoggedUser logged = new LoggedUser();
			 if ((!loggedUsers.isEmpty())){
					//A user is logged in the device
				 Iterator iter = loggedUsers.entrySet().iterator();
				 Map.Entry e;

				 while (iter.hasNext()) {
					e = (Map.Entry)iter.next();
					logged = (LoggedUser) e.getValue();
					if (logged.getUsername().equals(username)) {
						uid = (String) e.getKey();
					}
				 }
			 }
		}

		 
		 return uid;
	}
	
	
	  //Method for autologin a user. Returns true if autologin was needed
    public static boolean autologin(String appType, String getrequri, Form options, String remoteIpAddress, String author){
    	boolean logged = false;    	
    	
    	String uid = Utils.getDecodeAndEncode(options, "uid", null);   
    	if (getrequri.contains("junaio")){
    		uid = Utils.getDecodeAndEncode(options, "uid", null);  
    	} else if (getrequri.contains("layar")){
    		uid = Utils.getDecodeAndEncode(options, "userId", null);
    	}
    	
		 if (author != null) {
				String[] geturlparts;				 
				String delimiter = "/";
				geturlparts = getrequri.split(delimiter);
				int index = 0;
				for (int i=0; i < geturlparts.length; i++)
				{
					if (geturlparts[i].equals("deploy") || geturlparts[i].equals("author")) {
						index = i;
					}
				}
			  if (geturlparts[index].equals("deploy")) {
				  
				  String deployid = geturlparts[index + 1];
				  String username = "participant";
				  Deploy deploy = AuthService.getDeploybyID(deployid);
				  if (deploy != null){
					  String user = AuthService.getStudentFromDeploy(deploy);
					  if (user != null){
						  username = user;							  
					  }
					  
				  }
				  HashMap<String,LoggedUser> loggedUsers = new HashMap<String,LoggedUser>();
				  loggedUsers = readLoggedUsers();
				  if (loggedUsers != null){
					  if (!loggedUsers.isEmpty() && (loggedUsers.get(uid) != null)){
						  //If the device is logged in other deploy, autologin in the new deploy
						  if (!deployid.equals(loggedUsers.get(uid).getDeployId())) {
							  LoggedUser loggeduser = new LoggedUser(uid, username, deployid, Constants.POS_TYPE_GEOPOSITION);					
						//	  loggedUsers.put(uid, loggeduser);
							  saveLoggedUser(loggeduser);
							  logged = true;
							  
							  //Log
							  String logfile = Constants.ACCESS_LOGFILE;
							  String action = "login";
							  LogService.writelog(appType, options, remoteIpAddress, uid, action, author, logfile);
						  }
					  } else {
						  LoggedUser loggeduser = new LoggedUser(uid, username, deployid, Constants.POS_TYPE_GEOPOSITION);					
						//  AuthService.loggedUsers.put(uid, loggeduser);
						  saveLoggedUser(loggeduser);
						  logged = true;
						  
						  //Log
						  String logfile = Constants.ACCESS_LOGFILE;
						  String action = "login";
						  LogService.writelog(appType, options, remoteIpAddress, uid, action, author, logfile);
					  }
				  } else {
					  LoggedUser loggeduser = new LoggedUser(uid, username, deployid, Constants.POS_TYPE_GEOPOSITION);					
					//  AuthService.loggedUsers.put(uid, loggeduser);
					  saveLoggedUser(loggeduser);
					  logged = true;
					  
					  //Log
					  String logfile = Constants.ACCESS_LOGFILE;
					  String action = "login";
					  LogService.writelog(appType, options, remoteIpAddress, uid, action, author, logfile);

				  }




			  }else if (geturlparts[index].equals("author")) {					  
				  //TODO 					  
			  } else {
				  System.out.println("bad URL format in get request FILTER mismatch");					  
			  }
		 }
    	return logged;
    }
    
    
    
    
	//Methods for persistence (in a file) of logged users
	
	public static synchronized void saveLoggedUser(LoggedUser user){		

		HashMap<String, LoggedUser> users = new HashMap<String, LoggedUser>();
		users = readLoggedUsers();
		if (users == null){
			users = new HashMap<String, LoggedUser>();
		}
		users.put(user.getUid(), user);	
		storeLoggedUsers(users);
		
		
	}
	
	public static synchronized void removeLoggedUser(LoggedUser user){
		
		HashMap<String, LoggedUser> users = new HashMap<String, LoggedUser>();
		users = readLoggedUsers();
		if (users != null){
			users.remove(user.getUid());
			storeLoggedUsers(users);
		}

		
		
	}
	

	public static HashMap<String, LoggedUser> readLoggedUsers(){
		
		//JUAN: Removing the old file persistence
//		HashMap<String, LoggedUser> users = new HashMap<String, LoggedUser>();
		HashMap<String, LoggedUser> users = loggedUsers;
		
//		HashMap<String, LoggedUser> users = new HashMap<String, LoggedUser>();
//		String file = Constants.loggedUsersPersistenceFile;
//        FileInputStream fstream;
//		try {
//			fstream = new FileInputStream(file);
//	        // Get the object of DataInputStream
//	        DataInputStream in = new DataInputStream(fstream);
//	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//	        String strLine;
//	        //Read File Line By Line
//	        while ((strLine = br.readLine()) != null)   {        
//	        	Gson gson = new Gson();
//	        	LoggedUser user = gson.fromJson(strLine, LoggedUser.class);
//	    		users.put(user.getUid(), user);		    		
//	        }
//	        //Close the input stream
//	        in.close();
//		} catch (FileNotFoundException e) {	
//			users = null;
//		} catch (IOException e) {
//			users = null;
//		}	
		
		return users;
		
	}
	
	public static synchronized void storeLoggedUsers(HashMap<String, LoggedUser> users){
		
		HashMap<String, LoggedUser> usersResult = new HashMap<String, LoggedUser>();
		
		if (users != null){
	        Iterator it = users.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry entry = (Map.Entry)it.next();
	            LoggedUser userIt = (LoggedUser) entry.getValue();
	            
	            long msInADay = TimeUnit.DAYS.toMillis(1);
	            long expirationTime = msInADay * 2;
	            long currentTime = new Date().getTime();
	            //debug
	            
	            //If user has been logged more than 60 days, is logged out
	            if (userIt.getTimestamp() != null){
	            	if (currentTime <(userIt.getTimestamp() + expirationTime)){

	            		usersResult.put(userIt.getUid(), userIt);
	            	}
	            } else {
	            	System.out.println("Expiration in AR browsers for logged user: " + userIt.getUsername() + " in device ID: " + userIt.getUid());
	            }

	        }	        

		}
		loggedUsers=usersResult;
		
		//JUAN: Removing the old file persistence
//		String file = Constants.loggedUsersPersistenceFile;
//
//		if (users != null){
//			try {
//				ArrayList<String> fileLines = new ArrayList<String>();
//			    Writer fileOut;
//
//				fileOut = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
//		        BufferedWriter out = new BufferedWriter(fileOut);
//		        
//		        Iterator it = users.entrySet().iterator();
//		        while (it.hasNext()) {
//		            Map.Entry entry = (Map.Entry)it.next();
//		            LoggedUser userIt = (LoggedUser) entry.getValue();
//		            
//		            long msInADay = TimeUnit.DAYS.toMillis(1);
//		            long expirationTime = msInADay * 60;
//		            long currentTime = new Date().getTime();
//		            //debug
//		            
//		            //If user has been logged more than 60 days, is logged out
//		            if (userIt.getTimestamp() != null){
//		            	if (currentTime <(userIt.getTimestamp() + expirationTime)){
//				        	Gson gson = new Gson();
//				        	String json = gson.toJson(userIt);
//				        	fileLines.add(json);
//			//	            it.remove(); // avoids a ConcurrentModificationException
//		            	}
//		            } else {
//		            	System.out.println("Expiration in AR browsers for logged user: " + userIt.getUsername() + " in device ID: " + userIt.getUid());
//		            }
//
//		        }	        
//		        
//		        for (String entry : fileLines){
//		           out.write(entry);
//		           out.newLine();
//		        }
//		        out.close();
//
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}
		
		
		
	}
	
	
	public static void saveActiveUser(ActiveUser activeuser) {

		String username = activeuser.getUsername();
		AuthService.activeUsersDb.put(username,activeuser);
		
		//Remove expired users
        Iterator it = AuthService.activeUsersDb.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            ActiveUser userIt = (ActiveUser) entry.getValue();
            
            long expirationTime = 120000;
            long currentTime = new Date().getTime();
            //debug
            
            //If user has been logged more than 10 seconds, is logged out
            if (userIt.getTimestamp() != null){
            	if (currentTime >(userIt.getTimestamp() + expirationTime)){
            		it.remove();
            		System.out.println("User: " + userIt.getUsername() + " has become inactive");
            	}
            }

        }	        
	}
    
    
    
    
	
	
}
