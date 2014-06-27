package glueps.adaptors.vle.mediawiki;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Random;

import glueps.core.model.Activity;
import glueps.core.model.Deploy;

public class SimpleMWBatchDeployer extends MWOperator implements IMWBatchDeployer {

	private static final String DEFAULT_MAIN_PAGE_NAME = "Principal";
	//protected String wikiCookie = "";
	
	//TODO These two should be set somehow in the constructor. For now, we hardwire them for delfos
	private String wikiUser="gluepsuser";
	private String wikiPassword="G1u3psU$er";

	
	public SimpleMWBatchDeployer(URL wiki){
		
		this.wikiURL = wiki;
	}
	
	public SimpleMWBatchDeployer(URL wiki, String user, String pwd){
		
		this.wikiURL = wiki;
		this.wikiUser = user;
		this.wikiPassword = pwd;
	}
	
	@Override
	public Deploy batchUndeploy(Deploy lfdeploy) throws Exception {
		
		throw new Exception("The simple deployer does not support undeploying!");
	}
		
	
	
	@Override
	public Deploy batchDeploy(Deploy lfdeploy) throws Exception {
		
		String urlToMainDesignArticle=null;
		
		Deploy updatedDeploy = lfdeploy;
		
		
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
					String wikiNameSpaceGroupArticles = lfdeploy.getName().replace(" ","_");
					String principalArticle = DEFAULT_MAIN_PAGE_NAME;
					
					//We create the main page
//					String mainArticleTitle = URLEncoder.encode(wikiNameSpaceGroupArticles + ":" + principalArticle,"UTF-8");
					String mainArticleTitle =wikiNameSpaceGroupArticles + ":" + principalArticle;
					urlToMainDesignArticle = this.createANewWikiPage(mainArticleTitle, this.wikiCookie);
					
					int i = 0;
					while(urlToMainDesignArticle == null && i < MAX_RETRIES){
						i ++;
						System.err.println("Trying to create a new wiki page. Try no. " + i);
						
						Random random = new Random();
						
						int pageNUM = random.nextInt();
						wikiNameSpaceGroupArticles = wikiNameSpaceGroupArticles + pageNUM;
						mainArticleTitle =wikiNameSpaceGroupArticles + ":" + principalArticle;
						urlToMainDesignArticle = this.createANewWikiPage(mainArticleTitle, this.wikiCookie);
					}
					if (urlToMainDesignArticle == null){
						throw new Exception("The wiki page could not be created after " + i + "tries");
					}
					System.out.println(urlToMainDesignArticle);
					
					// TODO We should create all the activities' pages first, with the GLUElets inserted as tags, and links to the resources and the parent and children activities
					// updatedDeploy = createActivityTreeAsWikiPages(updatedDeploy);
					
					// We add the contents to the main page
					// TODO There was some problem with the length of the content that can be added. Check this more thoroughly
					this.addContentIntoWikiPage(mainArticleTitle, URLEncoder.encode(generateDeployMainPage(updatedDeploy), "UTF-8"), this.wikiCookie, 0);
					
					// TODO Should we protect the main page once it is created, to prevent users from breaking it??
					
				} else{
					System.err.println("Failed to identify user in MediaWiki");
					throw new Exception("Error in MediaWiki authorization");
				}
				
				updatedDeploy.setLiveDeployURL(new URL(urlToMainDesignArticle));
				
				return updatedDeploy;
				
			} else {
				// No se ha encontrado conexcion con la wiki
				throw new Exception("Connection to wiki " + this.wikiURL + "has Failed");
			}
		
		} else {
			throw new Exception("Destination wiki or deploy data not defined");
		}
		

	}




	
	
	private String generateDeployMainPage(Deploy deploy) {

		String content = null;
		
		//TODO All this content should be internationalized!!!
		
		//We add the general deploy data
		content = "'''Author''': "+deploy.getAuthor()+"\n\n";
		content += "'''Based on the design "+deploy.getDesign().getName()+" by "+deploy.getDesign().getAuthor()+"'''\n\n";
		
		if(deploy.getDesign()!=null && deploy.getDesign().getRootActivity()!=null && deploy.getDesign().getRootActivity().getChildrenActivities()!=null){
			
		
			//We represent the activity tree
			content += "= Activities =\n\n";
			
			int nestLevel = 1;
			for(Iterator<Activity> it = deploy.getDesign().getRootActivity().getChildrenActivities().iterator(); it.hasNext();){
				Activity activity = it.next();
				
				content += writeActivityNode(activity, nestLevel);
				
			}

		}

		
		//TODO We add this page to the Deploy category?
		content += "[[Category:Deploy]]\n\n";
		
		return content;
	}
	
	
	

	private String writeActivityNode(Activity activity, int nestLevel) {
		
		String content = "";
		
		//TODO For now, we only put the tree in text, at some point we will have to link to activity wiki pages
		for(int i=0;i<nestLevel;i++) content += "*";
		content += activity.getName();
		content += "\n";
		
		//Add children activities, if any
		if(activity.getChildrenActivities()!=null && activity.getChildrenActivities().size()>0){
			
			for(Iterator<Activity> it = activity.getChildrenActivities().iterator(); it.hasNext();){
				
				content += writeActivityNode(it.next(), nestLevel+1);
				
			}
			
			
		}
		
		return content;
		
	}

	


	
	
}
