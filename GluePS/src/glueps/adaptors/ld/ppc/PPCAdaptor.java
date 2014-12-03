package glueps.adaptors.ld.ppc;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

import glueps.adaptors.ld.ILDAdaptor;
import glueps.adaptors.ld.LDAdaptor;
import glueps.adaptors.ld.ppc.model.PatternType;
import glueps.adaptors.ld.ppc.model.ResourceType;
import glueps.adaptors.ld.ppc.model.StudentActivityType;
import glueps.adaptors.ld.ppc.model.TeachingLearningActivityType;
import glueps.core.model.Activity;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Group;
import glueps.core.model.Participant;
import glueps.core.model.Resource;

public class PPCAdaptor implements ILDAdaptor, LDAdaptor {

	private String designId = null;
	
	
	public PPCAdaptor(String designId) {
		this.designId = designId;
	}

	@Override
	public Design fromLDToLF(String filepath) {
		
		String xmlContent = null;
		
		//This is the PPC model main class
		PatternType pattern = null;
		
		File xmlFile = new File(filepath);
		
		if(!xmlFile.exists()) return null;
		
		//TODO: we might want to encode in UTF-8, but PPC seems to support only ANSI
		try {
			xmlContent = FileUtils.readFileToString(xmlFile, "UTF-8");
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
		//We create the deploy with the provided deploy XML
		try {
	        JAXBContext jc = JAXBContext.newInstance( glueps.adaptors.ld.ppc.model.PatternType.class );
	        Unmarshaller u = jc.createUnmarshaller();
	        pattern = (PatternType)u.unmarshal(new StringReader(xmlContent));
	        System.out.println( "\nThe unmarshalled objects are:\n" + pattern.toString());
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
		
		if(pattern==null) return null;
		
		Design design = convertPPCtoLF(pattern);
		//This field is optional, for now I do not see a use for putting the xml there
		//design.setOriginalDesignData(xmlContent);
		
		return design;
	}

	private Design convertPPCtoLF(PatternType pattern) {
		
		if(pattern == null) return null;
		
		ArrayList<String> objectives = null;
		if(pattern.getLearningOutcome()!=null && pattern.getLearningOutcome().length()>0){
			 objectives = new ArrayList<String>();
			 objectives.add(pattern.getLearningOutcome());
		}
		
		int actCounter = 1;
		int resCounter = 1;
		
		ArrayList<Resource> resources = null;
		
		Design design = new Design(this.designId, "Design from PPC", pattern.getLearningOutcome(), "PPC", null, new Date(), objectives, null, null, null, null);

		//We construct the activity tree, adding resources when needed
		Activity root = new Activity("0", "Root/Method", null, Activity.CLASS, null, null, null, null);
		root.setChildrenSequenceMode(Activity.PARALLEL);

		if(pattern.getTeachingLearningActivity()!=null && pattern.getTeachingLearningActivity().size()>0){
			
			ArrayList<Activity> children = new ArrayList<Activity>();
			
			for(TeachingLearningActivityType tla : pattern.getTeachingLearningActivity()){
				
				Activity child = new Activity(""+(actCounter++), tla.getTitle(), tla.getNotes(), null, null, "0", null, null);
				
				//We iterate through the tla's resources
				if(tla.getResource()!=null && tla.getResource().size()>0){
					ArrayList<String> resourceIds = new ArrayList<String>();
					if(resources==null) resources = new ArrayList<Resource>();
					
					for(ResourceType res : tla.getResource()){
						Resource lfres = new Resource(""+(resCounter++), res.getResourceName(), false, res.getResourceAddress(), null, null, null);
						
						resources.add(lfres);
						resourceIds.add(lfres.getId());
					}
					
					child.setResourceIds(resourceIds);
				}

				//We iterate through the tla's activities
				if(tla.getStudentActivity()!=null && tla.getStudentActivity().size()>0){
					ArrayList<Activity> grandchildren = new ArrayList<Activity>();
					
					for(StudentActivityType stAct : tla.getStudentActivity()){
						
						Activity lfact = new Activity(""+actCounter++, stAct.getActivityText(), null, null, null, child.getId(), null, null);
						
						//We iterate through the activity's resources
						if(stAct.getResource()!=null && stAct.getResource().size()>0){
							ArrayList<String> resourceIds = new ArrayList<String>();
							if(resources==null) resources = new ArrayList<Resource>();
							
							for(ResourceType res : stAct.getResource()){
								Resource lfres = new Resource(""+(resCounter++), res.getResourceName(), false, res.getResourceAddress(), null, null, null);
								
								resources.add(lfres);
								resourceIds.add(lfres.getId());
							}
							
							lfact.setResourceIds(resourceIds);
						}
						
						grandchildren.add(lfact);//We add the StudentActivity to the grandchildren's list
					}
					
					child.setChildrenActivities(grandchildren);
				}
				
				children.add(child);//We add the TLA to the children's list
			}
			
			root.setChildrenActivities(children);

			design.setResources(resources);
			design.setRootActivity(root);
		}
		
		return design;
	}

	@Override
	public Deploy processInstantiation(String filepath, Design design, HashMap<String, Group> vleGroups, HashMap<String, Participant> vleUsers) {	
		return null;
	}

	@Override
	public ILDAdaptor getLDAdaptor(Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
