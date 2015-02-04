package glueps.adaptors.vle.mediawiki;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import glueps.core.model.Activity;
import glueps.core.model.Deploy;
import glueps.core.model.Group;
import glueps.core.model.InstancedActivity;
import glueps.core.model.Resource;
import glueps.core.model.Role;
import glueps.core.model.ToolInstance;

/**
 * This variant of the open batch deploy does not generate intermetiate activity pages, only leaf pages
 * Also, it generates two main pages, one with student activities only, and another with all activities (for the teachers)
 * @author lprisan
 *
 */

public class OpenMWBatchDeployer2 extends MWOperator implements IMWBatchDeployer {

	private static final String DEFAULT_MAIN_PAGE_NAME = "Principal";
	private static final String DEFAULT_STUDENT_MAIN_PAGE_NAME = "Alumnos";
	private static final String ACTIVITY_INSTACT_SEPARATOR = "--";
	//protected String wikiCookie = "";
	
	//TODO These two should be set somehow in the constructor. For now, we hardwire them for delfos
	private String wikiUser="gluepsuser";
	private String wikiPassword="G1u3psU$er";

	// We implement a mechanism for rolling back uncompleted deploys in case something goes wrong (store the created wiki pages until the process is finished)
	// This variable stores temporarily the created page titles, in case we have to roll them back
	private ArrayList<String> tmpCreatedPages = null;
	
	//TODO We should implement the "undeploy" feature (delete all created wiki pages)
	
	
	private String wikiNameSpaceGroupArticles;
	
	public OpenMWBatchDeployer2(URL wiki){
		
		this.wikiURL = wiki;
	}
	
	public OpenMWBatchDeployer2(URL wiki, String user, String pwd){
		
		this.wikiURL = wiki;
		this.wikiUser = user;
		this.wikiPassword = pwd;
	}
	
	@Override
	public Deploy batchDeploy(Deploy lfdeploy) throws Exception {
		
		Deploy updatedDeploy = lfdeploy;
		
		this.tmpCreatedPages = new ArrayList<String>();
		
		if (lfdeploy != null && this.wikiURL != null){
			
			URLConnection conn = null;
			try {
				conn = this.wikiURL.openConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
			

			if (conn!= null){
				// We did connect with the wiki

				//We start the wiki page creation
				//For now, the deploy creates a main page (in the <deploy_name> namespace) where the activity tree is represented by a nested bullet list, with links to the activity pages
				//TODO What happens if we have deploys with the same name?
				
				// Authentication with MediaWiki
				if (mediaWikiAuth(this.wikiUser, this.wikiPassword)){

					try{
					
						//TODO the namespace could be the name of the deploy, to make it more legible?
						wikiNameSpaceGroupArticles = lfdeploy.getName().replace(" ","_");
	
						// Create the main index page, both for the teacher and students (the second one does not seem to work well)
						updatedDeploy = createMainDeployPage(updatedDeploy, wikiNameSpaceGroupArticles, this.wikiCookie, true);
						updatedDeploy = createMainDeployPage(updatedDeploy, wikiNameSpaceGroupArticles, this.wikiCookie, false);
						
						// We should create all the needed internal toolinstances (wiki pages)
						updatedDeploy = createInternalToolInstancePages(updatedDeploy, wikiNameSpaceGroupArticles, this.wikiCookie);
						
						// We should create all the instancedActivities' pages first, with the GLUElets inserted as tags, and links to the resources and the parent and children activities
						updatedDeploy = createInstancedActivityWikiPages(updatedDeploy, wikiNameSpaceGroupArticles, this.wikiCookie);
						
						// We should create all the activities' pages first, with the links to resources and children activities
						updatedDeploy = createActivityWikiPages(updatedDeploy, wikiNameSpaceGroupArticles, this.wikiCookie);
						
						updatedDeploy = addContentToActivityPages(updatedDeploy, wikiNameSpaceGroupArticles, this.wikiCookie);
						
						
						// Modify this page to have links to the activity pages
						updatedDeploy = addContentToMainDeployPage(updatedDeploy, wikiNameSpaceGroupArticles, this.wikiCookie, true);
						updatedDeploy = addContentToMainDeployPage(updatedDeploy, wikiNameSpaceGroupArticles, this.wikiCookie, false);

						//RollbackTest
//						System.out.println("We will delete everything! - nothing should be created in the wiki!");
//						if(tmpCreatedPages != null && tmpCreatedPages.size()>0){
//							
//							//We delete all created pages
//							for(Iterator<String> it = tmpCreatedPages.iterator();it.hasNext();){
//								String title = it.next();
//								try {
//									if(!deleteWikiPage(title, this.wikiCookie)) System.err.println("Error while trying to roll back page "+title+". we continue...");
//								} catch (Exception e2) {
//									System.err.println("Error while trying to roll back page "+title+". we continue...");
//								}
//							}
//							
//						}
//						System.out.println("Rollback complete! Check namespace "+wikiNameSpaceGroupArticles);
						
					}catch (Exception e) {
						// In case there is an exception in deploying, we roll back the created pages
						if(tmpCreatedPages != null && tmpCreatedPages.size()>0){
							
							//We delete all created pages
							for(Iterator<String> it = tmpCreatedPages.iterator();it.hasNext();){
								String title = it.next();
								try {
									if(!deleteWikiPage(title, this.wikiCookie)) System.err.println("Error while trying to roll back page "+title+". we continue...");
								} catch (Exception e2) {
									System.err.println("Error while trying to roll back page "+title+". we continue...");
								}
							}
							
						}
						throw new Exception("Some error occurred while deploying "+lfdeploy.getId());
					}
					
				} else{
					System.err.println("Failed to identify user in MediaWiki");
					throw new Exception("Error in MediaWiki authorization");
				}
				
				return updatedDeploy;
				
			} else {
				// No se ha encontrado conexcion con la wiki
				throw new Exception("Connection to wiki " + this.wikiURL + "has Failed");
			}
		
		} else {
			throw new Exception("Destination wiki or deploy data not defined");
		}
		

	}



	private Deploy addContentToMainDeployPage(Deploy deploy,
			String namespace, String cookie, boolean staffVersion) throws UnsupportedEncodingException {

		String mainArticleTitle;
		
		if(staffVersion) mainArticleTitle = namespace + ":" + DEFAULT_MAIN_PAGE_NAME;
		else mainArticleTitle = namespace + ":" + DEFAULT_STUDENT_MAIN_PAGE_NAME;
		

		System.out.println("Adding content to main page ("+mainArticleTitle+") in "+deploy.getLiveDeployURL().toString());
		
		// We add the contents to the main page
		// TODO There was some problem with the length of the content that can be added. Check this more thoroughly
		this.addContentIntoWikiPage(mainArticleTitle, URLEncoder.encode(generateDeployMainPage(deploy, staffVersion), "UTF-8"), cookie, 0);
		
		return deploy;

	}

	private Deploy addContentToActivityPages(Deploy deploy,
			String namespace, String cookie) throws UnsupportedEncodingException {
		//Supposedly, the root activity always has children??
		if(deploy==null||deploy.getDesign()==null||deploy.getDesign().getRootActivity()==null||deploy.getDesign().getRootActivity().getChildrenActivities()==null||deploy.getDesign().getRootActivity().getChildrenActivities().size()==0) return deploy;

		for(Iterator<Activity> it = deploy.getDesign().getRootActivity().getChildrenActivities().iterator(); it.hasNext(); ){
			
			Activity act = it.next();
			
			deploy = addContentToActivityPage(act, deploy, namespace, cookie);
			
		}
		
		return deploy;
	}

	private Deploy createActivityWikiPages(Deploy deploy,
			String namespace, String cookie) throws UnsupportedEncodingException, MalformedURLException, Exception {
		//Supposedly, the root activity always has children??
		if(deploy==null||deploy.getDesign()==null||deploy.getDesign().getRootActivity()==null||deploy.getDesign().getRootActivity().getChildrenActivities()==null||deploy.getDesign().getRootActivity().getChildrenActivities().size()==0) return deploy;

		for(Iterator<Activity> it = deploy.getDesign().getRootActivity().getChildrenActivities().iterator(); it.hasNext(); ){
			
			Activity act = it.next();
			
			deploy = createActivityPage(act, deploy, namespace, cookie);
			
		}
		
		return deploy;
	}
	
	
	private Deploy addContentToActivityPage(Activity activity, Deploy deploy, String namespace, String cookie) throws UnsupportedEncodingException{
		
		
		//If it has not a (valid) location, it may be an intermediate page, we look for children pages...
		if(activity.getLocation()==null || activity.getLocation().toString().lastIndexOf("/index.php/")==-1){
			
			//If it has no children, we quit
			if(activity.getChildrenActivities()==null || activity.getChildrenActivities().size()==0) return deploy;
			
			else{//it has children, we continue recursively
				
				//We go through the children activities
				for(Iterator<Activity> it = activity.getChildrenActivities().iterator(); it.hasNext();){
					
					Activity act = it.next();
					
					deploy = addContentToActivityPage(act, deploy, namespace, cookie);
					
				}
				
			}
			
			return deploy;
		}else{//it has a (valid) location, so it is a leaf node, so we add content to it
			
			String articleTitle = activity.getLocation().toString().substring(activity.getLocation().toString().lastIndexOf("/index.php/")+11);
			
			// We add the contents to the main page
			// TODO There was some problem with the length of the content that can be added. Check this more thoroughly
			this.addContentIntoWikiPage(articleTitle, URLEncoder.encode(generateActivityPage(activity, deploy), "UTF-8"), cookie, 0);
			
			return deploy;
		}
		
	}
	
	private Deploy createActivityPage(Activity activity, Deploy deploy, String namespace, String cookie) throws UnsupportedEncodingException, MalformedURLException, Exception{
		
		
		//We only create leaf nodes in the activity tree (those with no children)
		if(activity.getChildrenActivities()==null || activity.getChildrenActivities().size()==0){
		
			String articleTitle = namespace + ":" + activity.getName().replace(" ","_");
			String urlToArticle = this.createANewWikiPage(articleTitle, cookie);
			
			// TODO this security mechanism is a bit flimsy, we could revise it
			int i = 1;
			while(urlToArticle == null && i < MAX_RETRIES){
				i ++;
				
				System.err.println("Trying to create a new wiki page. Try no. " + i);
				
				articleTitle = articleTitle+"_v"+i;
				urlToArticle = this.createANewWikiPage(articleTitle, cookie);
			}
			if (urlToArticle == null){
				throw new Exception("The wiki page could not be created after " + i + "tries");
			}
			System.out.println(urlToArticle);
			
			Activity updatedActivity = deploy.getDesign().findActivityById(activity.getId());
			updatedActivity.setLocation(new URL(urlToArticle));
			tmpCreatedPages.add(articleTitle);
			
			//If it has no children, we quit
			 return deploy;
		 
		}
		else{
			//If it has children, we go through the children activities
			for(Iterator<Activity> it = activity.getChildrenActivities().iterator(); it.hasNext();){
				
				Activity act = it.next();
				
				deploy = createActivityPage(act, deploy, namespace, cookie);
				
			}
			
			return deploy;
		}
		
	}

	private String generateActivityPage(Activity activity, Deploy deploy) {
		
		String content = null;
		
		//TODO All this content should be internationalized!!!
		
		//We add the description from the corresponding activity
		content = "=Actividad=\n\n";
		if(activity.getDescription()!=null) content += "'''Descripción''': "+activity.getDescription()+"\n\n";

		if(activity.getResourceIds()!=null && activity.getResourceIds().size()>0){
			// We add the resources title
			content += "=Recursos=\n\n";

			// We add the links
			for(Iterator<String> it = activity.getResourceIds().iterator();it.hasNext();){
				String resId = it.next();
				Resource res = deploy.getDesign().getResourceById(resId);
				if(!res.isInstantiable()) content += "* ["+res.getLocation()+" "+res.getName()+"]\n\n";
				else content += "* "+res.getName()+" (véase abajo para los distintos grupos)\n\n";
			}
		}

//		if(activity.getChildrenActivities()!=null && activity.getChildrenActivities().size()>0){
//			//We show the tree for this activity's children (this should not be used in this mode, but we leave it here anyway, just in case)
//			content += "=Sub-actividades=\n\n";
//
//			int nestLevel = 1;
//			for(Iterator<Activity> it = activity.getChildrenActivities().iterator(); it.hasNext();){
//				Activity child = it.next();
//				
//				content += writeActivityNode(child, nestLevel, deploy, true);
//				
//			}
//
//		}
		
		HashMap<String, InstancedActivity> instances = deploy.getInstancedActivitiesForActivity(activity.getId());
		if(instances!=null && instances.size()>0){
			content += "=Grupos=\n\n";

			Map<String, String> unorderedLinks = new HashMap<String, String>();
			
			for(Iterator<Map.Entry<String, InstancedActivity>> it = instances.entrySet().iterator(); it.hasNext();){
				Map.Entry<String, InstancedActivity> entry = it.next();
				InstancedActivity instAct = entry.getValue();
				
				Group group = deploy.findGroupById(instAct.getGroupId());
				
				//Old, unordered version
				//content += "* [["+instAct.getLocation().toString().substring(instAct.getLocation().toString().lastIndexOf("/index.php/")+11)+"|"+group.getName()+"]] "+deploy.getGroupParticipantNamesAsString(group)+"\n";
			
				//We put the contents in an unordered map, to be ordered by group name later
				unorderedLinks.put(group.getName(), "* [["+instAct.getLocation().toString().substring(instAct.getLocation().toString().lastIndexOf("/index.php/")+11)+"|"+group.getName()+"]] "+deploy.getGroupParticipantNamesAsString(group)+"\n");
				
			}

			//We order the links alphabetically, by group name
			Map<String, String> sortedMap = new TreeMap<String, String>(unorderedLinks);
			
			for(Iterator<Map.Entry<String, String>> it = sortedMap.entrySet().iterator(); it.hasNext();){
				content += it.next().getValue();
			}

		}
		
		
		//we add the page to the Activity category??
		//content += "[[Category:Activity]]\n\n";
		content += "----\n";
		content += "[["+wikiNameSpaceGroupArticles+":"+DEFAULT_STUDENT_MAIN_PAGE_NAME+"|Volver al inicio]]\n";
		
		return content;
		
		
		
	}

	private Deploy createMainDeployPage(Deploy deploy,
			String namespace, String cookie, boolean staffVersion) throws UnsupportedEncodingException, MalformedURLException, Exception {

		//We create the main page
		String principalArticle;
		if(staffVersion) principalArticle = DEFAULT_MAIN_PAGE_NAME;
		else principalArticle = DEFAULT_STUDENT_MAIN_PAGE_NAME;
		
		String urlToMainDesignArticle=null;
		
		
		//		String mainArticleTitle = URLEncoder.encode(wikiNameSpaceGroupArticles + ":" + principalArticle,"UTF-8");
		String mainArticleTitle =namespace + ":" + principalArticle;
		urlToMainDesignArticle = this.createANewWikiPage(mainArticleTitle, cookie);
		
		// TODO this security mechanism operates
		int i = 1;
		while(urlToMainDesignArticle == null && i < MAX_RETRIES){
			i ++;
			System.err.println("Trying to create a new wiki page. Try no. " + i);
			
			String newNamespace = namespace+"_v"+i;
			mainArticleTitle = newNamespace + ":" + principalArticle;
			urlToMainDesignArticle = this.createANewWikiPage(mainArticleTitle, cookie);
			
			//if we had to change the deploy namespace and it worked, we update it
			if(urlToMainDesignArticle != null) this.wikiNameSpaceGroupArticles = newNamespace;
		}
		if (urlToMainDesignArticle == null){
			throw new Exception("The wiki page could not be created after " + i + "tries");
		}
		System.out.println(urlToMainDesignArticle);
		
		// TODO Should we protect the main page once it is created, to prevent users from breaking it??
		// We set the live deploy URL to the staff version of the deploy page (which has links to the student version)
		if(staffVersion) deploy.setLiveDeployURL(new URL(urlToMainDesignArticle));
		tmpCreatedPages.add(mainArticleTitle);
		
		return deploy;
	}

	private Deploy createInternalToolInstancePages(Deploy deploy,
			String namespace, String cookie) throws MalformedURLException, UnsupportedEncodingException, Exception {
		
		if(deploy==null||deploy.getToolInstances()==null||deploy.getToolInstances().size()==0) return deploy; //if there are no toolinstances, we do nothing, and return the deploy as it is
		
		for(Iterator<ToolInstance> it = deploy.getToolInstances().iterator();it.hasNext();){
			ToolInstance inst = it.next();

			if(deploy.getDesign().findResourceById(inst.getResourceId()).getToolKind().equals(Resource.TOOL_KIND_INTERNAL)){//if the instance is internal
				
				//We check that it is not a redirection (the location at this point should be null unless it is a redirection)
				if(inst.getLocation()==null){

					String articleTitle = namespace + ":" + inst.getName().replace(" ","_");
					String urlToArticle = this.createANewWikiPage(articleTitle, cookie);
	
					//This security mechanism is probably not needed since the name will be already unique. Anyway...
					int i = 1;
					while(urlToArticle == null && i < MAX_RETRIES){
						i++;
						System.err.println("Trying to create a new wiki page. Try no. " + i);
						
						articleTitle = articleTitle+"_v"+i;
						urlToArticle = this.createANewWikiPage(articleTitle, cookie);
					}
					if (urlToArticle == null){
						throw new Exception("The wiki page could not be created after " + i + "tries");
					}
					System.out.println("Created wiki page: "+urlToArticle);
					
					// We add the contents to the main page
					// TODO There was some problem with the length of the content that can be added. Check this more thoroughly
					String content = "="+inst.getName()+"=\n\n";
					//content += "[[Category:ToolInstance]]\n\n";		
					
					content += "----\n";
					content += "[["+namespace+":"+DEFAULT_STUDENT_MAIN_PAGE_NAME+"|Volver al inicio]]\n";
					
					this.addContentIntoWikiPage(articleTitle, URLEncoder.encode(content, "UTF-8"), cookie, 0);
	
					inst.setLocation(new URL(urlToArticle));
					tmpCreatedPages.add(articleTitle);
				}
			}
			

		}
		
		return deploy;

	}

	private Deploy createInstancedActivityWikiPages(Deploy deploy, String namespace, String cookie) throws UnsupportedEncodingException, MalformedURLException, Exception {
		
		if(deploy==null||deploy.getInstancedActivities()==null||deploy.getInstancedActivities().size()==0) return deploy; //if there are no instanced activities, we do nothing, and return the deploy as it is
		
		for(Iterator<InstancedActivity> it = deploy.getInstancedActivities().iterator();it.hasNext();){
			InstancedActivity instAct = it.next();
			
			String articleTitle = namespace + ":" + deploy.getDesign().findActivityById(instAct.getActivityId()).getName().replace(" ","_")+ACTIVITY_INSTACT_SEPARATOR+deploy.findGroupById(instAct.getGroupId()).getName().replace(" ","_");
			String urlToArticle = this.createANewWikiPage(articleTitle, cookie);

			//This security mechanism is probably not needed since the name will be already unique. Anyway...
			int i = 0;
			while(urlToArticle == null && i < MAX_RETRIES){
				i ++;
				System.err.println("Trying to create a new wiki page. Try no. " + i);
				
				articleTitle = articleTitle+"_v"+i;
				urlToArticle = this.createANewWikiPage(articleTitle, cookie);
			}
			if (urlToArticle == null){
				throw new Exception("The wiki page could not be created after " + i + "tries");
			}
			System.out.println("Created wiki page: "+urlToArticle);
			
			// We add the contents to the main page
			// TODO There was some problem with the length of the content that can be added. Check this more thoroughly
			this.addContentIntoWikiPage(articleTitle, URLEncoder.encode(generateInstancedActivityPage(instAct, deploy), "UTF-8"), cookie, 0);

			instAct.setLocation(new URL(urlToArticle));
			tmpCreatedPages.add(articleTitle);

		}
		
		return deploy;
	}

	private String generateInstancedActivityPage(InstancedActivity instAct, Deploy deploy) {
		
		String content = null;
		
		//TODO All this content should be internationalized!!!
		
		//We add the description from the corresponding activity
		content = "=Actividad=\n\n";
		if(deploy.getDesign().findActivityById(instAct.getActivityId()).getDescription()!=null) content += "'''Descripción''': "+deploy.getDesign().findActivityById(instAct.getActivityId()).getDescription()+"\n\n";
		if(deploy.findGroupById(instAct.getGroupId())!=null) content += "'''Grupo''': "+deploy.findGroupById(instAct.getGroupId()).getName()+" "+deploy.getGroupParticipantNamesAsString(deploy.findGroupById(instAct.getGroupId()))+" \n\n";
		
		if((instAct.getResourceIds()!=null && instAct.getResourceIds().size()>0) || (instAct.getInstancedToolIds()!=null && instAct.getInstancedToolIds().size()>0)){
			
			
			// We add the resources title
			content += "=Recursos=\n\n";

			// We add the links
			if(instAct.getResourceIds()!=null && instAct.getResourceIds().size()>0){
				
				for(Iterator<String> it = instAct.getResourceIds().iterator();it.hasNext();){
					String resId = it.next();
					Resource res = deploy.getDesign().getResourceById(resId);
					content += "=="+res.getName()+"==\n\n";
					content += "* ["+res.getLocation()+" "+res.getName()+"]\n\n";
				}
				
			}
			
			// We add the instances
			if(instAct.getInstancedToolIds()!=null && instAct.getInstancedToolIds().size()>0){
				
				for(Iterator<String> it = instAct.getInstancedToolIds().iterator();it.hasNext();){
					String instId = it.next();
					ToolInstance inst = deploy.getToolInstanceById(instId);
					//if it is internal, we add and internal link to the wiki page
					if(deploy.getDesign().findResourceById(inst.getResourceId()).getToolKind().equals(Resource.TOOL_KIND_INTERNAL)){
						content += "=="+inst.getName()+"==\n\n";
						content += "* [["+inst.getLocationWithRedirects(deploy).toString().substring(inst.getLocationWithRedirects(deploy).toString().indexOf("/index.php/")+11)+"|"+inst.getName()+"]]\n\n";
					}
					//if it is external, we add a gluelet tag
					else content += "<gluelettag title=\""+inst.getName()+"\">"+inst.getLocationWithRedirects(deploy).toString().substring(inst.getLocationWithRedirects(deploy).toString().indexOf("/instance/"))+"</gluelettag>\n\n";
				}
				
			}
		}

		//we add the page to the InstancedActivity category??
		//content += "[[Category:InstancedActivity]]\n\n";
		content += "----\n";
		content += "[["+wikiNameSpaceGroupArticles+":"+DEFAULT_STUDENT_MAIN_PAGE_NAME+"|Volver al inicio]]\n\n";
		//TODO BUG! the activity locations do not get updated in the previous functions. Check that out!
		//content += "[["+extractTitleFromWikiURL(deploy.getDesign().findActivityById(instAct.getActivityId()).getLocation(), deploy)+"|Volver a la actividad]]\n";
		
		return content;
	}

	private String generateDeployMainPage(Deploy deploy, boolean staffVersion) {

		String content = "";
		
		//TODO All this content should be internationalized!!!
		
		//We add the general deploy data
		//if(deploy.getAuthor()!=null && deploy.getAuthor().length()>0) content += "'''Autor''': "+deploy.getAuthor()+"\n\n";
		
		if(deploy.getDesign().getObjectives()!=null && deploy.getDesign().getObjectives().size()>0){
			content += "'''Objetivos de aprendizaje:'''\n";
			for(Iterator<String> it = deploy.getDesign().getObjectives().iterator();it.hasNext();){
				content += "* "+it.next()+"\n";
			}
			content += "\n\n";
		}
		
		//Eliminated this info (not really interesting)
		//content += "'''Based on the design "+deploy.getDesign().getName()+" by "+deploy.getDesign().getAuthor()+"'''\n\n";
		
		if(deploy.getDesign()!=null && deploy.getDesign().getRootActivity()!=null && deploy.getDesign().getRootActivity().getChildrenActivities()!=null){
			
		
			//We represent the activity tree
			content += "= Actividades =\n\n";
			
			int nestLevel = 1;
			for(Iterator<Activity> it = deploy.getDesign().getRootActivity().getChildrenActivities().iterator(); it.hasNext();){
				Activity activity = it.next();
				
				//If this is the student version, we do not show the teacher activities
				if(!staffVersion){
					if(activity.isPerformedOnlyByStaff(deploy.getDesign())){
						//we do not show it
					}else content += writeActivityNode(activity, nestLevel, deploy, true, staffVersion);//if it is performed by students (or we do not have enough role info), we show it
					
				}else content += writeActivityNode(activity, nestLevel, deploy, true, staffVersion);
				
			}

		}

		//If this is the teacher version, we add a link to the student version
		if(staffVersion){
			content += "'''Nota:''' Esta es la versión del profesor de las actividades a realizar. Existe una versión de esta página que incluye [["+wikiNameSpaceGroupArticles+":"+DEFAULT_STUDENT_MAIN_PAGE_NAME+"|s�lo las actividades de los alumnos]].\n\n";
		}
		
		
		//We add this page to the Deploy category?
		//content += "[[Category:Deploy]]\n\n";
		
		return content;
	}
	
	
	

	private String writeActivityNode(Activity activity, int nestLevel, Deploy deploy, boolean showRoles, boolean staffVersion) {
		
		String content = "";
		
		if(activity.isPerformedOnlyByStaff(deploy.getDesign()) && !staffVersion){//if it is a student version and the activity is for teachers, we do not show it
			//... and we assume the children activities will also be only for teachers!! this may not be the case?
			
			
			
		}else{
			
			//We link to activity wiki pages, but only for leaf activities
			for(int i=0;i<nestLevel;i++) content += "*";
			
			if(activity.getChildrenActivities()==null || activity.getChildrenActivities().size()==0){//if it is a leaf node
				content += "[["+activity.getLocation().toString().substring(activity.getLocation().toString().indexOf("/index.php/")+11)+"|"+activity.getName()+"]]";
				
				// If needed, we show the roles that do the activity next to the link
				if(showRoles){
					if(activity.getRoleIds()!=null && activity.getRoleIds().size()>0){
						content += " ( ";
						Iterator<String> it = activity.getRoleIds().iterator();
						content += deploy.getDesign().findRoleById(it.next()).getName();
						
						for(;it.hasNext();){
							content += " , ";
							Role rol = deploy.getDesign().findRoleById(it.next());
						}
						
						content += " )";
					}
				}
				content += "\n";
			}else{
				content += activity.getName()+"\n";
				//Add children activities, if any
				for(Iterator<Activity> it = activity.getChildrenActivities().iterator(); it.hasNext();){
					
					content += writeActivityNode(it.next(), nestLevel+1, deploy, showRoles, staffVersion);
					
				}
				
				
			}			
			
		}
		

		
		return content;
		
	}

	
	/**
	 * This method undeploys the wiki as it was deployed. Warning! it operates on a "best-effort" policy. If we delete only some pages, the undeploy process will continue!
	 */	
	@Override
	public Deploy batchUndeploy(Deploy lfdeploy) throws Exception {
	
		
		if (lfdeploy != null && this.wikiURL != null){
		
			URLConnection conn = null;
			try {
				conn = this.wikiURL.openConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			if (conn!= null){
				// We did connect with the wiki
	
				//We start the wiki page creation
				//For now, the deploy creates a main page (in the <deploy_name> namespace) where the activity tree is represented by a nested bullet list, with links to the activity pages
				//TODO What happens if we have deploys with the same name?
				
				// Authentication with MediaWiki
				if (mediaWikiAuth(this.wikiUser, this.wikiPassword)){

					ArrayList<String> createdPages = getCreatedWikiPages(lfdeploy);
					boolean errors = false;
					
					// In case there is an exception in deploying, we roll back the created pages
					if(createdPages != null && createdPages.size()>0){
						
						//We delete all created pages
						for(Iterator<String> it = createdPages.iterator();it.hasNext();){
							String title = it.next();
							try {
								if(!deleteWikiPage(title, this.wikiCookie)){
									System.err.println("Error while trying to undeploy page "+title+". we continue...");
									errors = true;
								}
							} catch (Exception e2) {
								System.err.println("Error while trying to undeploy page "+title+". we continue...");
								errors = true;
							}
						}
					}
			
					if(errors) System.err.println("Some error occurred while undeploying. Some orphan pages may remain in the wiki");
					if(lfdeploy.getDesign().getRootActivity()!=null) lfdeploy.clearActivityLocations(lfdeploy.getDesign().getRootActivity());
					lfdeploy.clearInstancedActivityLocations();
					lfdeploy.clearInternalToolInstanceLocations();
					lfdeploy.setLiveDeployURL(null);
					
				} else{
					System.err.println("Failed to identify user in MediaWiki");
					throw new Exception("Error in MediaWiki authorization");
				}
					
				return lfdeploy;
					
			}else {
				// No se ha encontrado conexcion con la wiki
				throw new Exception("Connection to wiki " + this.wikiURL + "has Failed");
			}
				
		} else {
			throw new Exception("Destination wiki or deploy data not defined");
		}
		
	}

	private ArrayList<String> getCreatedWikiPages(Deploy lfdeploy) {
		
		ArrayList<String> createdPages = new ArrayList<String>();
		
		//We add the internal toolInstance pages
		if(lfdeploy.getToolInstances()!=null && lfdeploy.getToolInstances().size()>0){
			for(Iterator<ToolInstance> it = lfdeploy.getToolInstances().iterator(); it.hasNext();){
				ToolInstance inst = it.next();
				if((inst.getLocation()!=null)&&(inst.getLocationWithRedirects(lfdeploy)!=null)&&(!inst.isRedirection(lfdeploy))) //if the location is not null and it is not a redirection 
					createdPages.add(extractTitleFromWikiURL(inst.getLocation(), lfdeploy));
			}
		}
		
		//We add the instancedActivity pages
		if(lfdeploy.getInstancedActivities()!=null && lfdeploy.getInstancedActivities().size()>0){
			for(Iterator<InstancedActivity> it = lfdeploy.getInstancedActivities().iterator(); it.hasNext();){
				InstancedActivity inst = it.next();
				if(inst.getLocation()!=null) createdPages.add(extractTitleFromWikiURL(inst.getLocation(), lfdeploy));
			}
		}
		
		//We add the activity pages
		//Supposedly, the root activity always has children??
		if(lfdeploy==null||lfdeploy.getDesign()==null||lfdeploy.getDesign().getRootActivity()==null||lfdeploy.getDesign().getRootActivity().getChildrenActivities()==null||lfdeploy.getDesign().getRootActivity().getChildrenActivities().size()==0);//if no activities, we do nothing
		else{
			for(Iterator<Activity> it = lfdeploy.getDesign().getRootActivity().getChildrenActivities().iterator(); it.hasNext(); ){
				
				Activity act = it.next();
				if(act.getLocation()!=null) createdPages.add(extractTitleFromWikiURL(act.getLocation(), lfdeploy));
				createdPages = getChildrenCreatedPages(act, createdPages, lfdeploy);
				
			}
		}

		//We add the main page
		//We add the students main page
		if(lfdeploy.getLiveDeployURL()!=null){
			createdPages.add(extractTitleFromWikiURL(lfdeploy.getLiveDeployURL(), lfdeploy));
			createdPages.add(extractTitleFromWikiURL(lfdeploy.getLiveDeployURL(), lfdeploy).replace(DEFAULT_MAIN_PAGE_NAME, DEFAULT_STUDENT_MAIN_PAGE_NAME));
		}
		
		
		return createdPages;
		
	}

	private ArrayList<String> getChildrenCreatedPages(Activity act, ArrayList<String> createdPages, Deploy lfdeploy) {
		
		if(act.getChildrenActivities()!=null && act.getChildrenActivities().size()>0){
			
			for(Iterator<Activity> it = act.getChildrenActivities().iterator(); it.hasNext();){
				Activity child = it.next();
				
				if(child.getLocation()!=null) createdPages.add(extractTitleFromWikiURL(child.getLocation(), lfdeploy));
				createdPages = getChildrenCreatedPages(child, createdPages, lfdeploy);
				
			}
			
			return createdPages;
			
		}else return createdPages;
		
	}

	private String extractTitleFromWikiURL(URL location, Deploy lfdeploy) {
		
		//TODO: We could check that the address starts with the same base URL as the deploy
		//String baseurl = lfdeploy.getLiveDeployURL().toString();
		
		if(location!=null){
			String url = location.toString();
			
			String title = url.substring(url.lastIndexOf("/index.php/")+11);
			
			return title;
		}else return null;
		
		
	}

	


	
	
}
