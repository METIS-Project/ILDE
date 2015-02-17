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
package glueps.adaptors.vle.blogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.adaptors.vle.blogger.model.Blog;
import glueps.adaptors.vle.blogger.model.Post;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Activity;
import glueps.core.model.Deploy;
import glueps.core.model.Group;
import glueps.core.model.InstancedActivity;
import glueps.core.model.Participant;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * A VLE adaptor for Blogger.
 * Read the API documentation to understand better this code: https://developers.google.com/blogger/docs/3.0/using#RetrievingAUsersBlogs
 * @author Javier Hoyos Torio
 *
 */

public class BloggerAdaptor implements IDynamicVLEDeployer {
	
	private String bloggerAccessToken; //the access token to make the Oauth 2.0 requests
	 
 	private String clientId; //Client ID for web application
	private String apiKey; //API key for browser applications
	protected Map<String, String> parameters;
	  

	public BloggerAdaptor() {
		super();
	}
	
	public BloggerAdaptor(String bloggerAccessToken, String clientId, String apiKey, Map<String, String> parameters){
		super();
		this.bloggerAccessToken = bloggerAccessToken;
		this.clientId = clientId;
		this.apiKey = apiKey;
		this.parameters = parameters;
	}
	
	@Override
	public HashMap<String, Participant> getCourseUsers(String baseUri,
			String courseId) {
		return getUsers(baseUri);
	}

	@Override
	public HashMap<String, Group> getCourseGroups(String baseUri,
			String courseId) {
		return null;
	}

	
	@Override
	public HashMap<String, String> getCourses(String baseUri) {
		return getCourses(baseUri,null);
	}
	
	@Override
	public HashMap<String, String> getCourses(String baseUri, String username) {
		HashMap<String, String> courses = null;
		try{
			String bloggerUserid = retrieveBloggerUserId(bloggerAccessToken);
			ArrayList<Blog> userBlogs = retrieveUserBlogs(bloggerUserid);
			for(Blog blog: userBlogs){
				String blogSubUrl = blog.getUrl().substring(0, blog.getUrl().lastIndexOf(".blogspot."));
				String baseUriSubUrl = baseUri.substring(0, blog.getUrl().lastIndexOf(".blogspot."));
				//Check that this is the blog we are searching for
				if (blog.getUrl().equals(baseUri) || blogSubUrl.equals(baseUriSubUrl)){
					if (courses == null){
						courses = new HashMap<String, String>();
					}
					courses.put(blog.getId(), blog.getName());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return courses;
	}

	@Override
	public HashMap<String, String> getInternalTools() {
		return null;
	}

	@Override
	public HashMap<String, Participant> getUsers(String baseUri) {
		String bloggerUserid = retrieveBloggerUserId(bloggerAccessToken);
		HashMap<String, Participant> users = new HashMap<String, Participant>();
		Participant user = new Participant(bloggerUserid, bloggerUserid, null,null, true);
		users.put(bloggerUserid, user);
		return users;
	}

	@Override
	public String getToolConfiguration(String toolId) {
		return null;
	}

	@Override
	public Deploy deploy(String baseUri, Deploy lfdeploy) {
		String blogId = lfdeploy.getCourse().getId();
		String blogTitle = lfdeploy.getName();
		String blogContent = generateActivitiesHtml(lfdeploy, "bloggerUser");
		String javascriptContent = generateJavascriptContent();
		try {
			String blogUrl = addPost(blogId, blogTitle, javascriptContent + "<p>This is the result of the deployment from Glue-!PS</p><p>" + blogContent + "</p>");
			String postId = getPostIdFromUrl(blogUrl);
			//make the new post private
			revertPost(lfdeploy.getCourse().getId(), postId);
			lfdeploy.setLiveDeployURL(new URL(blogUrl));
			return lfdeploy;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			//If something went wrong, we set location=null in case it has already a url
			lfdeploy.setLiveDeployURL(null);
			return lfdeploy;
		}catch (Exception e){
			//If something went wrong, we set location=null in case it has already a url
			lfdeploy.setLiveDeployURL(null);
			return lfdeploy;
		}
	}

	@Override
	public Deploy undeploy(String baseUri, Deploy lfdeploy) {
		return null;
	}

	@Override
	public Deploy redeploy(String baseUri, Deploy newDeploy) {
		String bloggerUserid = retrieveBloggerUserId(bloggerAccessToken);
		Blog blog = retrieveUserBlog(bloggerUserid, newDeploy.getCourse().getId());
		//get the url of the blog entry
		String entryUrl = newDeploy.getLiveDeployURL().toString();
		String postId = getPostIdFromUrl(entryUrl);
		Boolean wasPublic = true;
		Post blogPost = retrievePostById(newDeploy.getCourse().getId(), postId);
		if (blogPost==null){
			//make public the post just to be able to update it by using the blogger API
			publishPost(newDeploy.getCourse().getId(), postId);
			blogPost = retrievePostById(newDeploy.getCourse().getId(), postId);	
			wasPublic = false;
		}
		String newPostTitle = newDeploy.getName();
		//generate the html content
		String postContent = generateActivitiesHtml(newDeploy, "bloggerUser");
		//generate the javascript content
		String javascriptContent = generateJavascriptContent();
		try {
			String postUrl;
			//if the post already exists, we update it
			if (blogPost!=null){
				postUrl = updatePost(blog.getId(), blogPost, blogPost.getTitle(), javascriptContent + "<p>This is the result of the deployment from Glue-!PS</p><p>" + postContent + "</p>");
				if (!wasPublic){
					//make private it again
					revertPost(newDeploy.getCourse().getId(), postId);
				}
			}else{
				//if not we have to create a new one
				postUrl = addPost(blog.getId(), newPostTitle, javascriptContent + "<p>This is the result of the deployment from Glue-!PS</p><p>" + postContent + "</p>");
				//make the new post private
				revertPost(newDeploy.getCourse().getId(), postId);
			}
			newDeploy.setLiveDeployURL(new URL(postUrl));
			return newDeploy;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			//If something went wrong, we set location=null in case it has already a url
			newDeploy.setLiveDeployURL(null);
			return newDeploy;
		}catch (Exception e){
			//If something went wrong, we set location=null in case it has already a url
			newDeploy.setLiveDeployURL(null);
			return newDeploy;
		}
	}
    
    /**
     * Retrieve a blogger user's information by sending an HTTP GET request to the users resource URI
     * @param accessToken the access token required by the Blogger API
     * @return the blogger user's id
     */   
    public static String retrieveBloggerUserId(String accessToken){
    	String id = null;
		try {
			String json = doGetRestBloggerApi("https://www.googleapis.com/blogger/v3/users/self", accessToken);			
			Gson gson = new Gson();
			JsonElement element = gson.fromJson(json, JsonElement.class);
			JsonObject jobj = element.getAsJsonObject();
			id = jobj.get("id").getAsString();
			String kind = jobj.get("kind").getAsString();
			String displayName = jobj.get("displayName").getAsString();
			
		} catch (Exception e1) {
			return null;
		}
		return id;
    }
    
    /**
     * Retrieve a user's blog
     * @param userId the user's id
     * @param blogId the id of the blog
     * @return the blog or null if the blog is not found
     */
    private Blog retrieveUserBlog(String userId, String blogId){
    	ArrayList<Blog> userBlogs = retrieveUserBlogs(userId);
    	for (Blog blog: userBlogs){
    		if (blog.getId().equals(blogId)){
    			return blog;
    		}
    	}
    	return null;
    }
    
    /**
     * Retrieve a list of a user's blogs by sending an HTTP GET request to the blogs collection URI
     * @return the blogger user's id
     */
    private ArrayList<Blog> retrieveUserBlogs(String userId){
    	ArrayList<Blog> userBlogs = new ArrayList<Blog>();
		try {
			String json = doGetRestBloggerApi("https://www.googleapis.com/blogger/v3/users/" + userId + "/blogs", bloggerAccessToken);			
			Gson gson = new Gson();
			JsonElement element = gson.fromJson(json, JsonElement.class);
			JsonObject jobj = element.getAsJsonObject();
			String kind = jobj.get("kind").getAsString();
			JsonArray items = jobj.get("items").getAsJsonArray();
			for (int i = 0; i < items.size(); i++){
				JsonObject item = items.get(i).getAsJsonObject();
				String blogId = item.get("id").getAsString();
				String blogName = item.get("name").getAsString();
				String blogDescription = item.get("description").getAsString();
				String blogUrl = item.get("url").getAsString();
				Blog blog = new Blog(blogId, blogName, blogDescription, blogUrl);
				userBlogs.add(blog);
			}			
		} catch (Exception e) {
			return null;
		}
		return userBlogs;
    }
    
    /**
     * Retrieve a post from a blog by sending a GET request to the posts by path URI with a path parameter
     * @param blogId The blog id
     * @param postPath The path URI of the post
     * @return the post in the blog
     */
    private Post retrievePostByPath(String blogId, String postPath){
    	Post bloggerPost = null;
		try {
			String json = doGetRestBloggerApi("https://www.googleapis.com/blogger/v3/blogs/" + blogId + "/posts/bypath?path=" + postPath, bloggerAccessToken);			
			Gson gson = new Gson();
			JsonElement element = gson.fromJson(json, JsonElement.class);
			JsonObject jobj = element.getAsJsonObject();
			String kind = jobj.get("kind").getAsString();
			String id = jobj.get("id").getAsString();
			String published = jobj.get("published").getAsString();
			String updated = jobj.get("updated").getAsString();
			String url = jobj.get("url").getAsString();
			String selfLink = jobj.get("selfLink").getAsString();
			String title = jobj.get("title").getAsString();
			String content = jobj.get("content").getAsString();
			bloggerPost = new Post(id,url,selfLink,title,content);
			bloggerPost.setKind(kind);
			bloggerPost.setPublished(published);
			bloggerPost.setUpdated(updated);
		} catch (Exception e) {
			return null;
		}
		return bloggerPost;
    }
    
    /**
     * Retrieve a specific post from a blog by sending a GET request to the posts resource URI
     * @param blogId The blog id
     * @param postId The post id
     * @return the post in the blog
     */
    private Post retrievePostById(String blogId, String postId){
    	Post bloggerPost = null;
		try {
			String json = doGetRestBloggerApi("https://www.googleapis.com/blogger/v3/blogs/" + blogId + "/posts/" + postId, bloggerAccessToken);			
			Gson gson = new Gson();
			JsonElement element = gson.fromJson(json, JsonElement.class);
			JsonObject jobj = element.getAsJsonObject();
			String kind = jobj.get("kind").getAsString();
			String id = jobj.get("id").getAsString();
			String published = jobj.get("published").getAsString();
			String updated = jobj.get("updated").getAsString();
			String url = jobj.get("url").getAsString();
			String selfLink = jobj.get("selfLink").getAsString();
			String title = jobj.get("title").getAsString();
			String content = jobj.get("content").getAsString();
			bloggerPost = new Post(id,url,selfLink,title,content);
			bloggerPost.setKind(kind);
			bloggerPost.setPublished(published);
			bloggerPost.setUpdated(updated);
		} catch (Exception e) {
			return null;
		}
		return bloggerPost;
    }
    
    /**
     * Add a post for a blog by sending a POST request to the post collection URI with a post JSON body
     * @param blogId The blog id
     * @param postTitle The post title
     * @param postContent The post content
     * @return the url of the new post in the blog
     * @throws Exception 
     */
    private String addPost(String blogId, String postTitle, String postContent) throws Exception{
    	String postUrl, postId;
    	Gson gson = new Gson();
    	JsonObject objBlog = new JsonObject();
    	objBlog.addProperty("kind", "blogger#post");
    	JsonObject blog = new JsonObject();
    	blog.addProperty("id", blogId);
    	objBlog.add("blog", blog);
    	objBlog.addProperty("title", postTitle);
    	objBlog.addProperty("content", postContent);
    	String jsonBlog = gson.toJson(objBlog);
    	try {
    		String jsonResult = doPostRestBloggerApi("https://www.googleapis.com/blogger/v3/blogs/" + blogId + "/posts/", bloggerAccessToken, jsonBlog);
    		JsonElement element = gson.fromJson(jsonResult, JsonElement.class);
    		JsonObject jobj = element.getAsJsonObject();
    		//postUrl = jobj.get("url").getAsString();
    		postId = jobj.get("id").getAsString();
    		postUrl = generateEditionUrl(blogId, postId);
    		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return postUrl;
    	
    }
    
    /**
     * Update a blog post by sending a PUT request to the post collection URI with a post JSON body
     * @param blogId The blog id
     * @param post The post to be updated
     * @param newPostTitle The new title of the post
     * @param newPostContent The new content of the post
     * @return the url of the updated post
     * @throws Exception 
     */
    private String updatePost(String blogId, Post post, String newPostTitle, String newPostContent) throws Exception{
    	String putUrl = null;
    	Gson gson = new Gson();
    	JsonObject objPost = new JsonObject();
    	objPost.addProperty("kind", "blogger#post");
    	objPost.addProperty("id", post.getId());
    	JsonObject blog = new JsonObject();
    	blog.addProperty("id", blogId);
    	objPost.add("blog", blog);
    	objPost.addProperty("title", newPostTitle);
    	objPost.addProperty("content", newPostContent);
    	objPost.addProperty("url", post.getUrl());
    	objPost.addProperty("selfLink", post.getSelfLink());
    	String jsonPost = gson.toJson(objPost);
    	try {
    		String jsonResult = doPutRestBloggerApi("https://www.googleapis.com/blogger/v3/blogs/" + blogId + "/posts/" + post.getId(), bloggerAccessToken, jsonPost);
    		JsonElement element = gson.fromJson(jsonResult, JsonElement.class);
    		JsonObject jobj = element.getAsJsonObject();
    		//putUrl = jobj.get("url").getAsString();
    		putUrl = generateEditionUrl(blogId, post.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return putUrl;
    }
    
    /**
     * Revert a published or scheduled post to draft state, which removes the post from the publicly viewable content.
     * @param blogId The blog id
     * @param postId The id of the post to be reverted to draft state
     */	
    private boolean revertPost(String blogId, String postId){
    	Gson gson = new Gson();
    	String status;
    	try {
    		JsonObject objPost = new JsonObject();
    		String jsonPost = gson.toJson(objPost);
    		String jsonResult = doPostRestBloggerApi("https://www.googleapis.com/blogger/v3/blogs/" + blogId + "/posts/" + postId + "/revert", bloggerAccessToken, jsonPost);
    		JsonElement element = gson.fromJson(jsonResult, JsonElement.class);
    		JsonObject jobj = element.getAsJsonObject();
    		status = jobj.get("status").getAsString();
		} catch (Exception e) {
			return false;
		}
		return status.equals("DRAFT");
    }
    
    private String getPostIdFromUrl(String liveDeployUrl){
        return liveDeployUrl.substring(liveDeployUrl.indexOf("postID=") + "postID=".length());
    }    

    
    /**
     * Publish a draft post
     * @param blogId The blog id
     * @param postId The id of the post to be reverted to draft state
     * @return the post is public now
     */	
    private boolean publishPost(String blogId, String postId){
    	Gson gson = new Gson();
    	String status;
    	try {
    		JsonObject objPost = new JsonObject();
    		String jsonPost = gson.toJson(objPost);
    		String jsonResult = doPostRestBloggerApi("https://www.googleapis.com/blogger/v3/blogs/" + blogId + "/posts/" + postId + "/publish", bloggerAccessToken, jsonPost);
    		JsonElement element = gson.fromJson(jsonResult, JsonElement.class);
    		JsonObject jobj = element.getAsJsonObject();
    		status = jobj.get("status").getAsString();
		} catch (Exception e) {
			return false;
		}
		return status.equals("LIVE");
    }
    
    /**
     * Generate the javascript for the blog entry
     * @return the javascript source code
     */
    private String generateJavascriptContent(){
    	String content = "";
    	String jsFilePath = getAppPath() + "/oauth/oauthScript.js";
		content += "<script type=\"text/javascript\" language=\"javascript\">if ((typeof googleOauthClientId==\"undefined\") && (typeof googleOauthApiKey==\"undefined\")){var googleOauthClientId=\""+ clientId + "\";var googleOauthApiKey=\"" + apiKey + "\";var firstOauthScript = true;}</script>";
		content += "<script type=\"text/javascript\" language=\"javascript\">" + readJavascriptFile(jsFilePath) + "</script>";
    	return content;    	
    }
    
    /**
     * Read the content of the file and returns the javascript code that contains
     * @param path the path to the javascript file
     * @return the content of the file
     */
	private String readJavascriptFile(String path) {
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		StringBuilder contentFile = new StringBuilder();

		try {
			file = new File(path);
			fr = new FileReader(file);
			br = new BufferedReader(fr);
            String line;
			// read the file line by line
			while ((line = br.readLine()) != null){
				//line = line.replace("\\", "\\\\");
				//line = line.replace("\"", "\\\"");
				contentFile.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
				if (null != br) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return contentFile.toString();
	}
	
	private String generateEditionUrl(String blogId,String postId){
		String draftUrl = "https://www.blogger.com/blogger.g?blogID=" + blogId + "#editor/target=post;postID=" + postId;
		return draftUrl;
	}
    
	private String generateActivitiesHtml(Deploy lfdeploy, String addressee) {
		String content = "";
		
		//TODO All this content should be internationalized!!! For now, we do it in English
		
		if(lfdeploy.getDesign()!=null && lfdeploy.getDesign().getRootActivity()!=null && lfdeploy.getDesign().getRootActivity().getChildrenActivities()!=null){
			
			//We represent the activity tree
			content += "<ul>\n";
			for(Iterator<Activity> it = lfdeploy.getDesign().getRootActivity().getChildrenActivities().iterator(); it.hasNext();){
				Activity activity = it.next();
				
				content += writeActivityNode(activity, lfdeploy, addressee);
				
			}
			content += "</ul>\n";

		}
		return content;
	}


	private String writeActivityNode(Activity activity, Deploy deploy, String addressee) {
		
		String content = "";
		
		if(activity.isToDeploy()){
			//We create a list item for this activity
			content += "<li>";
			content += "<b>"+activity.getName()+"</b>";
			if(activity.getDescription()!=null && activity.getDescription().length()>0) content += ": "+activity.getDescription();
			content += "\n";
				
				if(activity.getChildrenActivities()==null || activity.getChildrenActivities().size()==0){//if it is a leaf node
					
					//add the instanced activities
					HashMap<String,InstancedActivity> instancedActs = deploy.getInstancedActivitiesForActivity(activity.getId());
					if(instancedActs!=null && instancedActs.size()>0){
						content += "<br/>Groups\n";
						content += "<ul>\n";
						
						for(String i : instancedActs.keySet()){
							content += writeInstancedActivityNode(instancedActs.get(i), deploy, addressee);
						}
						
						content += "</ul>\n";
					}
					content += "\n";
				}else{//it is not a leaf node
					content += "<ul>\n";
					//Add children activities
					for(Iterator<Activity> it = activity.getChildrenActivities().iterator(); it.hasNext();){
						
						content += writeActivityNode(it.next(), deploy, addressee);
						
					}
					content += "</ul>\n";
				}			
	
			content += "</li>\n";
		}
		return content;
		
	}


	
	
	private String writeInstancedActivityNode(
			InstancedActivity instancedActivity, Deploy deploy, String addressee) {

		String groupid = instancedActivity.getGroupId();
		Group group = deploy.findGroupById(groupid);
		String content = "";
		
		if(group!=null){
			content += "<li>"+group.getName();
			if(group.getParticipantIds()!=null && group.getParticipantIds().size()>0){
				content += " "+deploy.getGroupParticipantNamesAsString(group);
			}
			
			int totalItems = 0;
			if(instancedActivity.getResourceIds()!=null) totalItems += instancedActivity.getResourceIds().size();
			if(instancedActivity.getInstancedToolIds()!=null) totalItems += instancedActivity.getInstancedToolIds().size();
			
			if(totalItems>0){
				content += "<ul>\n";
				//We add the resources of this iAct
				if(instancedActivity.getResourceIds()!=null && instancedActivity.getResourceIds().size()>0){
					for(String resId : instancedActivity.getResourceIds()) content += "<li><a href=\""+deploy.getDesign().getResourceById(resId).getLocation()+"\" target=\"_blank\">"+deploy.getDesign().getResourceById(resId).getName()+"</a></li>\n";
				}
				
				//We add the tool instances of this iAct
				if(instancedActivity.getInstancedToolIds()!=null && instancedActivity.getInstancedToolIds().size()>0){
					//TODO We should use the addressee for the callerUser parameter!! by now, we put the deploy's username (from the LE credentials)
					for(String instId : instancedActivity.getInstancedToolIds()){
						content += "<li><a href=\"javascript:handleClientLoad('"+deploy.getToolInstanceById(instId).getLocationWithRedirects(deploy)+"?callerUser="+addressee+"')\" >"+deploy.getToolInstanceById(instId).getName()+"</a></li>\n";
					}
				}
				
				content += "</ul>\n";
			}
			content += "</li>\n";
			
		}
		
		
		return content;
	}
	
	protected static String doGetRestBloggerApi(String url, String accessToken) throws Exception{
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		httpget.addHeader("Authorization", accessToken);
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
	
	protected String doPostRestBloggerApi(String url, String accessToken, String json) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		StringEntity postee = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		try {
			System.out.println((new Date()).toString()+" - Trying to POST to "+url);
			httppost.addHeader("Authorization", accessToken);
			postee = new StringEntity(json,"UTF-8");
			postee.setContentType("application/json");
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
	 * PUT request to the blogger api
	 * @param url the PUT url
	 * @param accessToken the access token required by the blogger API
	 * @param json the JSON containing the content to send
	 * @return JSON containing the response
	 * @throws Exception
	 */
	protected String doPutRestBloggerApi(String url, String accessToken, String json) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(url);
		StringEntity postee = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		try {
			System.out.println((new Date()).toString()+" - Trying to PUT to "+url);
			httpput.addHeader("Authorization", accessToken);
			postee = new StringEntity(json,"UTF-8");
			postee.setContentType("application/json");
			httpput.setEntity(postee);
			response = httpclient.execute(httpput);
			
			int rc = response.getStatusLine().getStatusCode();

			entity = response.getEntity();
			if (rc != 201 && rc != 200) {
				throw new Exception("PUT unsuccessful. Returned code "+rc);
			}
				
			if (entity != null) {

				String content = EntityUtils.toString(entity, "UTF-8");
				System.out.println((new Date()).toString()+" - Got response from server: "+content);

				return content;
			} else throw new Exception("PUT unsuccessful. Null entity!");
			
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
			if(httpput!=null) httpput.abort();
			if(httpclient!=null) httpclient.getConnectionManager().shutdown();
		}
	}
	
	private String getAppPath() {
		return parameters.get("appPath");
	}

	@Override
	public boolean canBeDeployed(String baseUri, Deploy lfdeploy) {
		return true;
	}

    
	
	
}
