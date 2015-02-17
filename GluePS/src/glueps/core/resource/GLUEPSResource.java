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
package glueps.core.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import glue.common.resources.GLUEResource;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Course;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.InstancedActivity;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.model.Participant;
import glueps.core.model.Resource;
import glueps.core.model.ToolInstance;

public abstract class GLUEPSResource extends GLUEResource {

	
	protected boolean doAuthentication = true;//selects whether the user authentication will be performed for this resource - it will be false for locally created resources (e.g. not coming from an HTTP request)
	
	protected String doGluepsUriSubstitution (String olduri){
		String newuri = null;
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
        //If the external GLUEPS uri was set (it is different than the one we are being accessed as, due to redirections), use it as a base
        if(app.getAppExternalUri()!=null && app.getAppExternalUri().length()>0){
        	int index = olduri.lastIndexOf("GLUEPSManager/")+14;
        	//Make sure the /ldshake substring is never included in the uri
        	if (olduri.lastIndexOf("GLUEPSManager/ldshake/")!=-1){
        		index = olduri.lastIndexOf("GLUEPSManager/ldshake/") + ("GLUEPSManager/ldshake/").length();
        	}
        	String trailing = olduri.substring(index);
        	newuri = app.getAppExternalUri()+trailing;       	
        } else return olduri; //If the uri was not set, we just do nothing
		return newuri;
	}
	
	

	//TODO HttpClient reuses connections. This way of doing things is wasteful, to be optimized!!
	protected String doGetFromURL(String url) throws Exception{
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = null;
		HttpEntity entity = null;
		try {
			System.out.println((new Date()).toString()+" - Trying to GET "+url);
			
			response = httpclient.execute(httpget);
			
			int rc = response.getStatusLine().getStatusCode();

			entity = response.getEntity();
     
			if (rc != 200) {
				throw new Exception("GET unsuccessful. Returned code "+rc);
			}
				
			if (entity != null) {

				String content = EntityUtils.toString(entity, "UTF-8");
				System.out.println((new Date()).toString()+" - Got response from server: "+content);

				return content;
			} else throw new Exception("GET unsuccessful. Null entity!");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(entity!=null){
				try {
					EntityUtils.consume(entity);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(httpget!=null) httpget.abort();
			if(httpclient!=null) httpclient.getConnectionManager().shutdown();
		}
		

		
		
	}
	
	
	//TODO HttpClient reuses connections. This way of doing things is wasteful, to be optimized!!
	protected int doDeleteFromURL(String url) throws Exception{
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpDelete httpdelete = new HttpDelete(url);
		HttpResponse response = null;
		HttpEntity entity = null;
		try {
			System.out.println((new Date()).toString()+" - Trying to DELETE "+url);
			
			response = httpclient.execute(httpdelete);
			
			int rc = response.getStatusLine().getStatusCode();

			entity = response.getEntity();
     
			System.out.println((new Date()).toString()+" - DELETE answered: "+EntityUtils.toString(entity));
			return rc;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(entity!=null){
				try {
					EntityUtils.consume(entity);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(httpdelete!=null) httpdelete.abort();
			if(httpclient!=null) httpclient.getConnectionManager().shutdown();
		}

		
		
	}
	
	
	


	
	protected String doPostToUrl(String url, String entityToSend, String contentType) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		StringEntity postee = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		try {
			System.out.println((new Date()).toString()+" - Trying to POST to "+url);
			
			//postee = new StringEntity(entityToSend, "UTF-8");
			postee = new StringEntity(entityToSend);
			//postee.setContentType(contentType);
			httppost.setEntity(postee);
			response = httpclient.execute(httppost);
			
			int rc = response.getStatusLine().getStatusCode();

			entity = response.getEntity();
			if (rc != 201 && rc != 200) {
				throw new Exception("GET unsuccessful. Returned code "+rc);
			}
				
			if (entity != null) {

				String content = EntityUtils.toString(entity, "UTF-8");
				System.out.println((new Date()).toString()+" - Got response from server: "+content);

				return content;
			} else throw new Exception("GET unsuccessful. Null entity!");
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(entity!=null){
				try {
					EntityUtils.consume(entity);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(httppost!=null) httppost.abort();
			if(httpclient!=null) httpclient.getConnectionManager().shutdown();
		}
	}

	
	/**
	 * Convenience method that trims an id of the type "http://.../id.extension" to just "id"
	 * @param id
	 * @return
	 */
    protected String trimId(String id) {
    	
    	if(id==null) return null;
    	String trimmedId=null;
    	//we get out the trailing query parameters ?blabla=...
    	if(id.indexOf("?")!=-1){
    		trimmedId = id.substring(0, id.indexOf("?"));
    	}else{
    		trimmedId = id;
    	}
    	//We remove everything except the last bit .../xxxx.xxx
    	if(trimmedId.lastIndexOf("/")!=-1){
        	trimmedId = trimmedId.substring(trimmedId.lastIndexOf("/")+1);
    	}
    	//if it has a extension, we remove it
        if(trimmedId.lastIndexOf(".")!=-1){
        	trimmedId = trimmedId.substring(0, trimmedId.lastIndexOf("."));
        }
    	
        return trimmedId;
	}
	
    /**
	 * Convenience method that trims an path of the type "/.../name.extension" to just "name.extension"
	 * @param name
	 * @return
	 */
    protected String trimPath(String name) {
    	
    	if(name==null) return null;
    	String trimmedPath=null;
    	//We remove everything except the last bit .../xxxx.xxx
    	if(name.lastIndexOf("/")!=-1){
        	trimmedPath = name.substring(name.lastIndexOf("/")+1);
    	}else{
    		trimmedPath = name;
    		
    	}
    	//We remove everything except the last bit ...\xxxx.xxx
    	if(trimmedPath.lastIndexOf("\\")!=-1){
        	trimmedPath = trimmedPath.substring(trimmedPath.lastIndexOf("\\")+1);
    	}
    	
        return trimmedPath;
        
    }
    
	protected String generateXML(Object o, Class classname){
		
		//We create the LE XML
		String xml = null;
		JAXBContext context;
		try {
			StringWriter writer = new StringWriter();
			context = JAXBContext.newInstance(classname);
			  Marshaller m = context.createMarshaller();	
			  //Esta Propiedad formatea el código del XML
	          m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
	          m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	          m.marshal(o, writer);
		      xml = writer.toString();
	          return xml;
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
    
    protected Object generateObject(String xmlfile,
			Class classname) {
		
    	Object o = null;
		//We create the learning environment with the provided XML
		try {
	        JAXBContext jc = JAXBContext.newInstance( classname );
	        Unmarshaller u = jc.createUnmarshaller();
	        o = u.unmarshal(new StringReader(xmlfile));
	        System.out.println( "\nThe unmarshalled objects are:\n" + o.toString());
			return o;
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
    	
    	
    	
	}

    
    
    


   	protected Deploy deURLifyDeploy(Deploy d) throws Exception {
    	Deploy updatedDeploy = null;
    	if(d==null) return null;

    	//We start with the urlified deploy
    	updatedDeploy = d;
    	//deurlify deploy id
    	if(d.getId()!=null) updatedDeploy.setId(trimId(d.getId()));
    	
    	//deurlify design
    	if(d.getDesign()!=null) updatedDeploy.setDesign(deURLifyDesign(d.getDesign()));
    	
    	//deurlify learningEnvironment
    	if(d.getLearningEnvironment()!=null) updatedDeploy.setLearningEnvironment(deURLifyLearningEnvironment(d.getLearningEnvironment()));
    	
    	if(d.getCourse()!=null) updatedDeploy.setCourse(new Course(trimId(d.getCourse().getId()), d.getCourse().getName(), d.getCourse().getRelativeUrl(), d.getCourse().getVleParameters(), d.getCourse().getParticipants()));
    	
    	//deurlify toolinstances
    	if(updatedDeploy.getToolInstances()!=null){
    		for(ToolInstance inst : updatedDeploy.getToolInstances()){
    			inst.setId(trimId(inst.getId()));

    			//deurlify internal toolinstance references in toolinstance locations
    			if(inst.getLocation()!=null){
	    	    	if(inst.getLocation().toString().contains("/GLUEPSManager/") && inst.getLocation().toString().contains("/toolInstances/")){
	    	    		inst.setInternalReference(trimId(inst.getLocation().toString()));
	    	    		inst.setLocation(null);
	    	    	}
    			}    	    	
    	    	inst.setDeployId(trimId(inst.getDeployId()));
    		}	
    	}
    	
    	//deurlify toolinstance references in instancedactivities
    	if(updatedDeploy.getInstancedActivities()!=null){
    		for(InstancedActivity instAct : updatedDeploy.getInstancedActivities()){
    			if(instAct.getInstancedToolIds()!=null){
    				ArrayList<String> newInsts = new ArrayList<String>();
    				for(String ref : instAct.getInstancedToolIds()) newInsts.add(trimId(ref));
    				instAct.setInstancedToolIds(newInsts);
    			}
    			
    			instAct.setDeployId(trimId(instAct.getDeployId()));
    		}
    	}
    	
    	//deurlify participant deployIds
    	if(updatedDeploy.getParticipants()!=null){
    		for(Participant part : updatedDeploy.getParticipants()){
    			part.setDeployId(trimId(part.getDeployId()));
    		}
    	}
    	
    	//we check that the deploy is still valid, just in case
    	if(!updatedDeploy.isValid()) throw new Exception("The deploy is not valid anymore!");
    	
    	return updatedDeploy;
   	}

    

    protected LearningEnvironment deURLifyLearningEnvironment(
			LearningEnvironment le) {
		LearningEnvironment updatedLE = null;
		if(le==null) return null;
		
		updatedLE = le;
		
    	//deurlify LE id
    	updatedLE.setId(trimId(le.getId()));
    	//deurlify course ids
    	if(le.getCourses()!=null){
    		HashMap<String, String> newCourses = new HashMap<String, String>();
    		for(Entry<String,String> courseentry : le.getCourses().entrySet()){
    			newCourses.put(trimId(courseentry.getKey()), courseentry.getValue());
    		}
    		updatedLE.setCourses(newCourses);
    	}
    	
    	//deurlify internal tool ids
    	if(le.getInternalTools()!=null){
    		HashMap<String, String> newIntTools = new HashMap<String, String>();
    		for(Entry<String,String> toolentry : le.getInternalTools().entrySet()){
    			newIntTools.put(trimId(toolentry.getKey()), toolentry.getValue());
    		}
    		updatedLE.setInternalTools(newIntTools);
    	}
    	
    	//urlify external tool ids
    	if(le.getExternalTools()!=null){
    		HashMap<String, String> newExtTools = new HashMap<String, String>();
    		for(Entry<String,String> toolentry : le.getExternalTools().entrySet()){
    			newExtTools.put(trimId(toolentry.getKey()), toolentry.getValue());
    		}
    		updatedLE.setExternalTools(newExtTools);
    	}
    	return updatedLE;
	}
    
    protected LearningEnvironmentInstallation deURLifyLearningEnvironmentInstallation(LearningEnvironmentInstallation leInst) {
		LearningEnvironmentInstallation updatedLEInst = null;
		if(leInst==null) return null;
		
		updatedLEInst = leInst;
		
    	//deurlify LE id
    	updatedLEInst.setId(trimId(leInst.getId()));
    	return updatedLEInst;
	}

	protected Design deURLifyDesign(Design d) {
    	Design updatedDesign = null;
    	if(d==null) return null;

    	//We start with the urlified design
    	updatedDesign = d;
    	//deurlify deploy id
    	updatedDesign.setId(trimId(d.getId()));
    	
    	//deurlify tooltypes in resources
    	if(updatedDesign.getResources()!=null){
    		for(Resource res : updatedDesign.getResources()){
    			res.setToolType(trimId(res.getToolType()));
    		}
    	}
    	
    	return updatedDesign;		
	}

	//The base URL here should be http://.../GLUEPSManager/
    protected Deploy URLifyDeploy(Deploy d, String baseUrl) throws Exception {
    	Deploy updatedDeploy = null;
    	if(d==null) return null;
    	
    	//We start with the non-urlified deploy
    	updatedDeploy = deURLifyDeploy(d);
    	
    	//urlify design
    	if(d.getDesign()!=null) updatedDeploy.setDesign(URLifyDesign(updatedDeploy.getDesign(), updatedDeploy.getLearningEnvironment().getId(), baseUrl));
    	
    	//urlify learningEnvironment
    	//if(d.getLearningEnvironment()!=null) updatedDeploy.setLearningEnvironment(URLifyLearningEnvironment(updatedDeploy.getLearningEnvironment(), baseUrl));
    	if(d.getLearningEnvironment()!=null && d.getLearningEnvironment().getId()!=null) updatedDeploy.setLearningEnvironment(URLifyLearningEnvironment(updatedDeploy.getLearningEnvironment(), baseUrl));
    	
    	if(d.getCourse()!=null) updatedDeploy.setCourse(new Course(updatedDeploy.getLearningEnvironment().getId()+"/courses/"+trimId(d.getCourse().getId()), d.getCourse().getName(), d.getCourse().getRelativeUrl(), d.getCourse().getVleParameters(), d.getCourse().getParticipants()));
    	
    	//urlify toolinstances
    	if(updatedDeploy.getToolInstances()!=null){
    		for(ToolInstance inst : updatedDeploy.getToolInstances()){
    			inst.setId(baseUrl+"deploys/"+updatedDeploy.getId()+"/toolInstances/"+inst.getId());
    			
    			//urlify toolinstance references in toolinstance locations
    			if(inst.getInternalReference()!=null){
    				inst.setLocation(new URL(baseUrl+"deploys/"+updatedDeploy.getId()+"/toolInstances/"+inst.getInternalReference()));
    				inst.setInternalReference(null);
    			}
    			
    			inst.setDeployId(baseUrl+"deploys/"+updatedDeploy.getId());
    		}	
    	}
    	
    	//urlify toolinstance references in instancedactivities
    	if(updatedDeploy.getInstancedActivities()!=null){
    		for(InstancedActivity instAct : updatedDeploy.getInstancedActivities()){
    			if(instAct.getInstancedToolIds()!=null){
    				ArrayList<String> newInsts = new ArrayList<String>();
    				for(String ref : instAct.getInstancedToolIds()) newInsts.add(baseUrl+"deploys/"+updatedDeploy.getId()+"/toolInstances/"+ref);
    				instAct.setInstancedToolIds(newInsts);
    			}
    			instAct.setDeployId(baseUrl+"deploys/"+updatedDeploy.getId());
    		}
    	}
   	
    	//urlify participant deployIds
    	if(updatedDeploy.getParticipants()!=null){
    		for(Participant part : updatedDeploy.getParticipants()){
    			part.setDeployId(baseUrl+"deploys/"+updatedDeploy.getId());
    		}
    	}

    	//urlify deploy id
    	updatedDeploy.setId(baseUrl+"deploys/"+updatedDeploy.getId());

    	//we check that the deploy is still valid, just in case
    	if(!updatedDeploy.isValid()) throw new Exception("The deploy is not valid anymore!");

    	return updatedDeploy;
	}


	protected Design URLifyDesign(Design d, String leId, String baseUrl) {
    	Design updatedDesign = null;
    	if(d==null) return null;

    	//We start with the non-urlified deploy
    	updatedDesign = deURLifyDesign(d);

    	//urlify tooltypes in resources, and references to internal-static ones
    	if(updatedDesign.getResources()!=null){
    		for(Resource res : updatedDesign.getResources()){
    			if(!res.isInstantiable() && !res.getLocation().startsWith("http")){
    				//We add the route to the internal files decompressed in the upload
    				String url = baseUrl+"uploaded/"+d.getId()+"/"+res.getLocation();
    				res.setLocation(url);
    			}
    			
    			if (leId!=null){
    				if(res.getToolType()!=null)
    					res.setToolType(baseUrl+"learningEnvironments/"+trimId(leId)+"/tools/"+trimId(res.getToolType()));
    			}
    		}
    	}
    	
    	
    	
    	//urlify deploy id
    	updatedDesign.setId(baseUrl+"designs/"+updatedDesign.getId());

    	return updatedDesign;
	}

	protected LearningEnvironment URLifyLearningEnvironment(
			LearningEnvironment le, String baseUrl) {
		
		LearningEnvironment updatedLE = null;
		if(le==null) return null;
		
		updatedLE = deURLifyLearningEnvironment(le);
		
    	//urlify course ids
    	if(updatedLE.getCourses()!=null){
    		HashMap<String, String> newCourses = new HashMap<String, String>();
    		for(Entry<String,String> courseentry : updatedLE.getCourses().entrySet()){
    			newCourses.put(baseUrl+"learningEnvironments/"+updatedLE.getId()+"/courses/"+courseentry.getKey(), courseentry.getValue());
    		}
    		updatedLE.setCourses(newCourses);
    	}
    	
    	//urlify internal tool ids
    	if(updatedLE.getInternalTools()!=null){
    		HashMap<String, String> newIntTools = new HashMap<String, String>();
    		for(Entry<String,String> toolentry : updatedLE.getInternalTools().entrySet()){
    			newIntTools.put(baseUrl+"learningEnvironments/"+updatedLE.getId()+"/tools/"+toolentry.getKey(), toolentry.getValue());
    		}
    		updatedLE.setInternalTools(newIntTools);
    	}
    	
    	//urlify external tool ids
    	if(updatedLE.getExternalTools()!=null){
    		HashMap<String, String> newExtTools = new HashMap<String, String>();
    		for(Entry<String,String> toolentry : updatedLE.getExternalTools().entrySet()){
    			String trimmedGMToolId = null;
    			if(toolentry.getKey().lastIndexOf("/tools/")!=-1) trimmedGMToolId = toolentry.getKey().substring(toolentry.getKey().lastIndexOf("/tools/")+7);
    			else trimmedGMToolId = toolentry.getKey();
    			newExtTools.put(baseUrl+"learningEnvironments/"+updatedLE.getId()+"/tools/"+trimmedGMToolId, toolentry.getValue());
    		}
    		updatedLE.setExternalTools(newExtTools);
    	}
    	
    	//urlify LE id
    	updatedLE.setId(baseUrl+"learningEnvironments/"+updatedLE.getId());

    	return updatedLE;
	}
	
	protected LearningEnvironmentInstallation URLifyLearningEnvironmentInstallation(LearningEnvironmentInstallation leInst, String baseUrl) {
		
		LearningEnvironmentInstallation updatedLEInst = null;
		if(leInst==null) return null;
		
		updatedLEInst = deURLifyLearningEnvironmentInstallation(leInst);
    	
    	//urlify LE Installation id
		updatedLEInst.setId(baseUrl+"learningEnvironmentInstallations/"+updatedLEInst.getId());

    	return updatedLEInst;
	}

}
