package glueps.adaptors.ld.imsld.icollage;

import glueps.adaptors.ld.imsld.icollage.model.IcollageManifest;
import glueps.adaptors.ld.imsld.icollage.model.Role;
import glueps.adaptors.ld.imsld.icollage.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import glueps.core.model.Activity;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Group;
import glueps.core.model.InstancedActivity;
import glueps.core.model.LFModelBuilder;
import glueps.core.model.Participant;
import glueps.core.model.Resource;
import glueps.core.model.SimpleLFModelBuilder;
import glueps.core.model.ToolInstance;



public class InstanceCollageAdaptor {

	private HashMap<String, Participant> participants;
	
	private ArrayList<Group> groups;
	
	private final static String ROLE_GROUP_SEPARATOR = ";";
	
	private final static String USER_PARAMETER_SEPARATOR = ";";
	
	private final static String USER_FILE_SEPARATOR = "\t";

	private static final String ACTIVITY_INSTANCE_SEPARATOR = ";";

	private static final String TOOL_INSTANCE_SEPARATOR = ";";

	public static final String TOOL_ID_VARIABLE = "gluepsId";

	/**
	 * <p>Función encargada de generar objetos en LF a partir del fichero XML de instanceCollage</p>
	 * @param classes : Paquete cno las clases que se han generado con xjc a partir de de un esquema
	 * @param destinationXml : Nombre del xml que se va a generar a partir de  las clases del esquema
	 * @param origenXml: Nombre del archivo xml a partir del cual se van a rellenar las clases
	 * @throws JAXBException 
	 * @throws FileNotFoundException
	 * @throws MalformedURLException 
	 */
	//ESTE METODO YA NO SE USA!
/*	public void process(String classes,String deployId,String xmlFile,String textFile) throws JAXBException, FileNotFoundException, MalformedURLException{
		
		//TODO: refactor this method!
		
		Unmarshaller unmarshaller;
        //Rellenamos las clases java con la información procedente del xml.
        //unmarshaller = JAXBContext.newInstance(classes).createUnmarshaller();
        //Rellenamos las clases java con la información procedente del xml.
        unmarshaller = JAXBContext.newInstance(classes).createUnmarshaller();
        IcollageManifest icollageMF= new IcollageManifest();
        icollageMF =  (IcollageManifest)unmarshaller.unmarshal( new FileInputStream(xmlFile));	    
        //Procesamos
        HashMap participantList=ObtenerUsuarios(textFile);
        List<User> userList=icollageMF.getRolePopulation().getUser(); 
        //Creo objeto de tipo participant de LF
        participants = new ArrayList<Participant>();
        groups = new ArrayList<Group>();
        ArrayList<String> lista;
        
        //Rellenamos array de participantes.
       	for (int i=0; i<userList.size();i++){
       		lista= (ArrayList<String>) participantList.get(userList.get(i).getIdentifier());
       		
//       		  lista[4] = Id del usuario
//       		  lista[0] = username del usuario
//       		  "1" sera el DeployId
//       		  lista[1] = email
//       		  lista[5] = Primer acceso
//       		  lista[6] = isStaff
//       		   
       		
       		//Con esto indicamos si el participante es profesor(True) o alumno(False)
       		participants.add(new Participant(lista.get(4),lista.get(0),deployId,lista.get(4)+USER_PARAMETER_SEPARATOR+lista.get(0)+USER_PARAMETER_SEPARATOR+lista.get(1)+USER_PARAMETER_SEPARATOR+lista.get(5)+USER_PARAMETER_SEPARATOR ,Boolean.parseBoolean(lista.get(6).trim())));
       	}
       	
       	//Recogemos información para generar "El arbol"
       List<Role> listChildren=(List <Role>) icollageMF.getRoleRoot().getRole();
       List<Role> aux = null;
       ArrayList hijas;
       HashMap roles= new HashMap();
       Role rolPro;
       BigInteger fatherRol = null;
       //Procesamos hijos de roolRoot
       for(int i=0; i<listChildren.size();i++){
    	   roles.put(listChildren.get(i).getOccurrence(), new ArrayList<Role>(Arrays.asList(listChildren.get(i))));    	   
       }    
       
		//Empiezo a recorrer los hijos del raiz
		while (listChildren.size() !=0){
			hijas=new ArrayList();
			for(int j=0; j<listChildren.size();j++){
				if (listChildren.get(j).getOccurrence()!=null)
					fatherRol=listChildren.get(j).getOccurrence();
						
				for(int h=0; h<listChildren.get(j).getRoleOrTitle().size();h++){
					if (Role.class==listChildren.get(j).getRoleOrTitle().get(h).getClass()){		
						rolPro=(Role) listChildren.get(j).getRoleOrTitle().get(h);
						hijas.add(rolPro);
						aux= new ArrayList();
						aux.add(listChildren.get(j));
						aux.add(rolPro);
						roles.put(rolPro.getOccurrence(), aux);
					}			
				 }
			}
			listChildren=hijas;			
		}
       	
       	//Procesar listado de usuarios obtenido del xml
		List <User> usuarios=icollageMF.getRolePopulation().getUser();
		Iterator it = roles.entrySet().iterator();
		ArrayList alumnosGrupo;
		Role auxRole;
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			alumnosGrupo= new ArrayList();
			//System.out.println(e.getKey() + " " + e.getValue());
			for(int i=0; i<usuarios.size();i++){
				
				if (this.isChild(roles, usuarios.get(i).getRoleOccurrenceRef().get(0).getRef(), (BigInteger)e.getKey()))
					alumnosGrupo.add(this.getIdAlumn(participants, usuarios.get(i).getIdentifier()));
					
			}
			if  (((ArrayList <Role>)e.getValue()).size() > 1)
				auxRole=  ((ArrayList <Role>)e.getValue()).get(1);
			else
				auxRole=  ((ArrayList <Role>)e.getValue()).get(0);
			
			groups.add(new Group(auxRole.getId()+ROLE_GROUP_SEPARATOR+auxRole.getOccurrence().toString(), auxRole.getRoleOrTitle().get(0).toString(), deployId, alumnosGrupo));
		}
		  	
}	
	*/
	
	
	public HashMap ObtenerUsuarios(String usersFile){
		String sCadena;
		String[] participant;
		HashMap participantList= new HashMap();
		BufferedReader bf;
		ArrayList <String> lista = new ArrayList();
		try {
			//TODO this use of FileReader is potentially buggy for UTF8 characters! do it with fileinputstream
			//bf = new BufferedReader(new FileReader(usersFile));
			bf = new BufferedReader(new InputStreamReader(new FileInputStream(usersFile), "UTF-8"));
			while ((sCadena = bf.readLine())!=null) {
				if(!sCadena.startsWith("#")){//si no es un comentario
					//Creamos una lista con split
					participant=sCadena.split(USER_FILE_SEPARATOR);
					
					//Eliminamos del array las posiciones que contiene un ""
				    for(int i=0; i<participant.length;i++){
				    	if (!participant[i].equals("")){
				    		lista.add(participant[i]);
				    	}
				    }				
	
					if (participantList.get(participant[0])== null)
						participantList.put(participant[0],lista);
					//Pongo la lista a null para prepararla para el nuevo contenido
					participant=null;
					lista=new ArrayList();
				}
			} 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		return participantList;
	}
	
	public boolean isChild(HashMap roles,BigInteger idRoleAlumn,BigInteger idRole)
	{
		BigInteger padre=idRoleAlumn;
		BigInteger ind=idRoleAlumn;
		boolean result=false;
		Iterator it = roles.keySet().iterator();
		while ((!padre.equals(new BigInteger("0") )) && (!padre.equals(idRole))){
			//recorremos el hashMap
			if (((ArrayList)roles.get(ind)).size() > 1)
				padre=((Role)((ArrayList)roles.get(ind)).get(0)).getOccurrence();	
			else
				padre=new BigInteger("0");
			ind=padre;
		}
		if (padre.equals(idRole))
			result=true;
		return result;
	}
	
	public String getIdAlumn(HashMap<String,Participant> participants,String name){
		Participant p = participants.get(name);
		String result = null;
		result = p.getId();
		return result;
	}

	public void setParticipants(HashMap<String,Participant> participants) {
		this.participants = participants;
	}

	public HashMap<String,Participant> getParticipants() {
		return participants;
	}

	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public Deploy generateInstancesFromGroups(Deploy deploy) {
		//This method generates all the needed instanced activities and tool instances needed, taking into account the number of groups
		
		//Recursively traverse the activity tree, creating the instanced activities and tool instances for instantiable resources
		//For each instanced activity, we create a tool instance for each instantiable resource in the activity
		ArrayList<InstancedActivity> instancedActivities = null;
		instancedActivities = createInstancedActivities(deploy, deploy.getDesign().getRootActivity(), instancedActivities);
		deploy.setInstancedActivities(instancedActivities);
		
		ArrayList<ToolInstance> toolInstances = null;
		//create the tool instances
		toolInstances = createToolInstances(deploy);
		
		deploy.setToolInstances(toolInstances);
		
		return deploy;
	}

	private ArrayList<ToolInstance> createToolInstances(Deploy deploy) {
		//We traverse the instanced activities
		
		if(deploy==null) return null;
		
		ArrayList<ToolInstance> instances = null;
		if (deploy.getInstancedActivities()!=null){
			for(Iterator<InstancedActivity> it = deploy.getInstancedActivities().iterator();it.hasNext();){
				InstancedActivity instAct = it.next();
				//For each instanced activity, we look at its activities' instantiable resources and generate a (mostly empty) toolinstance
				Activity relevantActivity = deploy.getDesign().findActivityById(instAct.getActivityId());
				
				ArrayList<String> relevantToolResources = relevantActivity.getResourceIdsByInstantiable(deploy.getDesign(), true);
				
				if(relevantToolResources!=null){
					for(Iterator<String> it2 = relevantToolResources.iterator();it2.hasNext();){
						Resource toolResource = deploy.getDesign().findResourceById(it2.next());
						String toolInstanceIdentifier = toolResource.getId()+TOOL_INSTANCE_SEPARATOR+getRoleOcurrence(deploy.findGroupById(instAct.getGroupId()));
						ToolInstance instance = new ToolInstance(toolInstanceIdentifier, getToolInstanceName(toolResource, deploy.findGroupById(instAct.getGroupId())), deploy.getId(), toolResource.getId(), null);
						
						//we relate the tool instance with the instanced activity
						if(instAct.getInstancedToolIds()==null) instAct.setInstancedToolIds(new ArrayList<String>(Arrays.asList(toolInstanceIdentifier)));
						else instAct.getInstancedToolIds().add(toolInstanceIdentifier);
						
						if(instances==null) instances = new ArrayList<ToolInstance>();
						instances.add(instance);
					}
				}
			}
		}
		return instances;
	}

	private String getToolInstanceName(Resource toolResource,
			Group group, Deploy deploy) {
		// TODO Auto-generated method stub
		String participantList = null;
		
		if(participants == null || participants.isEmpty() || participants.size()==0 || group==null || group.getParticipantIds()==null || group.getParticipantIds().isEmpty() || group.getParticipantIds().size() == 0) participantList = "( -- )"; 
		else{
			participantList = "(";
			for(Iterator<String> it = group.getParticipantIds().iterator();it.hasNext();){
				String partId = it.next();
				Participant p = deploy.getParticipantById(partId);
				if(p==null) break;
				else participantList += p.getName(); 
				if(it.hasNext()) participantList += ",";
			}
			participantList+=")";
		}
		
		return toolResource.getName()+" "+participantList;
	}

	private String getToolInstanceName(Resource toolResource, Group group) {		
		//return toolResource.getName()+" ("+group.getName()+")";
		return toolResource.getName();
	}

	private ArrayList<InstancedActivity> createInstancedActivities(
			Deploy deploy, Activity activity, ArrayList<InstancedActivity> instancedActivities) {
		
		if(activity.getRoleIds()!=null){
			//There are roles associated with this activity
			for(Iterator<String> it = activity.getRoleIds().iterator();it.hasNext();){
				String roleId = it.next();
				//We search for the groups that are clones of this role
				ArrayList<Group> relevantGroups = findGroupsFromRoleId(deploy.getGroups(), roleId);
				if (relevantGroups!=null){
					//For each group, we create an instanced activity
					for(Iterator<Group> it2 = relevantGroups.iterator();it2.hasNext();){
						Group group = it2.next();
						
						//By now, we only set the non-instantiable resources. ToolInstances are created later
						InstancedActivity instAct = new InstancedActivity(activity.getId()+ACTIVITY_INSTANCE_SEPARATOR+getRoleOcurrence(group), deploy.getId(), activity.getId(), group.getId(), activity.getResourceIdsByInstantiable(deploy.getDesign(), false), null);
						
						if(instancedActivities == null) instancedActivities = new ArrayList<InstancedActivity>();
						instancedActivities.add(instAct);
					}
				}
				
				
			}
			
			
		}
		//we do the same for the children activities
		if(activity.getChildrenActivities()!=null){
			for(Iterator<Activity> it = activity.getChildrenActivities().iterator();it.hasNext();){
				instancedActivities = createInstancedActivities(deploy, it.next(), instancedActivities);
			}
		}
		
		
		return instancedActivities;
	}

	private String getRoleOcurrence(Group group) {
		
		if(group!=null && group.getId()!=null){
			return group.getId().split(ROLE_GROUP_SEPARATOR)[1];
		}
		return null;
	}

	private ArrayList<Group> findGroupsFromRoleId(ArrayList<Group> groups,
			String roleId) {
		
		ArrayList<Group> relevantGroups = null;
		
		if(groups!=null){
			for(Iterator<Group> it = groups.iterator();it.hasNext();){
				Group gr = it.next();
				if(gr.getId().split(";")[0].equals(roleId)){
					//This group is a role copy, we add it to the relevant groups
					if(relevantGroups == null) relevantGroups = new ArrayList<Group>();
					relevantGroups.add(gr);
				}
				
			}
			
		}
		
		return relevantGroups;
	}

	public void process(String classes, String deployId, String instFile,
			HashMap<String, Participant> vleUsers) throws JAXBException, FileNotFoundException {
		// TODO Refactor this method with the homonym
		
		Unmarshaller unmarshaller;

        //Rellenamos las clases java con la información procedente del xml.
        unmarshaller = JAXBContext.newInstance(classes).createUnmarshaller();
        IcollageManifest icollageMF= new IcollageManifest();
        icollageMF =  (IcollageManifest)unmarshaller.unmarshal( new FileInputStream(instFile));	    
        //Procesamos
        List<User> userList=icollageMF.getRolePopulation().getUser(); 
        //Creo objeto de tipo participant de LF
        participants = new HashMap<String,Participant>();
        groups = new ArrayList<Group>();
        Participant user;
        HashMap<String, String> randomIds = new HashMap<String, String>();
        
        //Rellenamos array de participantes.
       	for (int i=0; i<userList.size();i++){
       		user= (Participant) vleUsers.get(userList.get(i).getIdentifier());
       		// If I cannot find it, this is a unregistered user. We create it with empty fields
       		if (user==null){
           		// We have to generate a random, unique, numeric id which is neither in the VLE or in the LF object
           		String uniqueId = generateUniqueId(vleUsers,randomIds);
       			randomIds.put(uniqueId, uniqueId);
       			user = new Participant(uniqueId, userList.get(i).getIdentifier(), null, ""+Participant.USER_PARAMETER_SEPARATOR+Participant.USER_PARAMETER_SEPARATOR+Participant.USER_PARAMETER_SEPARATOR+Participant.USER_PARAMETER_SEPARATOR, false);
       		}
       		participants.put(userList.get(i).getIdentifier(), user);
       	}
       	
       	//Recogemos informaci�n para generar "El arbol"
       List<Role> listChildren=(List <Role>) icollageMF.getRoleRoot().getRole();
       List<Role> aux = null;
       ArrayList hijas;
       HashMap roles= new HashMap();//Here we will store all the role occurrences (groups) at the root level
       Role rolPro;
       BigInteger fatherRol = null;
       
       //Procesamos hijos de roolRoot
       for(int i=0; i<listChildren.size();i++){
    	   roles.put(listChildren.get(i).getOccurrence(), new ArrayList<Role>(Arrays.asList(listChildren.get(i))));    	   
       }    
       
		//Empiezo a recorrer los hijos del raiz
		while (listChildren.size() !=0){
			hijas=new ArrayList();
			for(int j=0; j<listChildren.size();j++){
				if (listChildren.get(j).getOccurrence()!=null)
					fatherRol=listChildren.get(j).getOccurrence();
						
				for(int h=0; h<listChildren.get(j).getRoleOrTitle().size();h++){
					if (Role.class==listChildren.get(j).getRoleOrTitle().get(h).getClass()){		
						rolPro=(Role) listChildren.get(j).getRoleOrTitle().get(h);
						hijas.add(rolPro);
						aux= new ArrayList();
						aux.add(listChildren.get(j));
						aux.add(rolPro);
						roles.put(rolPro.getOccurrence(), aux);
					}			
				 }
			}
			listChildren=hijas;			
		}
       	
       	//Procesar listado de usuarios obtenido del xml
		List <User> usuarios=icollageMF.getRolePopulation().getUser();
		Iterator it = roles.entrySet().iterator();
		ArrayList<String> alumnosGrupo;
		Role auxRole;
		while (it.hasNext()) {//For each role occurrence (group)...
			Map.Entry e = (Map.Entry)it.next();
			alumnosGrupo= new ArrayList();
			//System.out.println(e.getKey() + " " + e.getValue());
			for(int i=0; i<usuarios.size();i++){
				
				//Since each user can be in more than one role/group, we iterate through all the user role-ocs
				for(int j=0;j<usuarios.get(i).getRoleOccurrenceRef().size();j++){
					if (this.isChild(roles, usuarios.get(i).getRoleOccurrenceRef().get(j).getRef(), (BigInteger)e.getKey()))
						alumnosGrupo.add(this.getIdAlumn(participants, usuarios.get(i).getIdentifier()));
				}
					
			}
			if  (((ArrayList <Role>)e.getValue()).size() > 1)
				auxRole=  ((ArrayList <Role>)e.getValue()).get(1);
			else
				auxRole=  ((ArrayList <Role>)e.getValue()).get(0);
			
			//Temporary fix: sometimes this algorithm gives repeated participants in a group. We remove repetitions
			alumnosGrupo = ensureUniqueParticipants(alumnosGrupo);
			groups.add(new Group(auxRole.getId()+ROLE_GROUP_SEPARATOR+auxRole.getOccurrence().toString(), auxRole.getRoleOrTitle().get(0).toString(), deployId, alumnosGrupo));
		}

		//We add a small post-process to ensure that the group names are unique (good for presentation purposes)
		ensureUniqueGroupNames();
	}

	private ArrayList<String> ensureUniqueParticipants(
			ArrayList<String> alumnosGrupo) {
		
		if(alumnosGrupo == null) return null;
		else if(alumnosGrupo.size()==0) return (new ArrayList<String>());
		else{
			ArrayList<String> fixed = new ArrayList<String>();
			for(Iterator<String> it = alumnosGrupo.iterator();it.hasNext();){
				String elem = it.next();
				if(!fixed.contains(elem)) fixed.add(elem);
			}
			return fixed;
		}
		
	}

	private void ensureUniqueGroupNames() {
		//We iterate through the groups

		if(groups!=null){
			HashMap<String, Group> groupsByName = new HashMap<String, Group>();
			
			HashMap<String, Integer> countersByName = new HashMap<String, Integer>();
			
			for(Iterator<Group> it=groups.iterator();it.hasNext();){
				Group g = it.next();
				
				Group exist;
				
				String oldname = g.getName();
				
				if((exist = groupsByName.get(oldname))!=null){
					//The name already exists, we have to generate the new name
					Integer i;
					//We get the counter for that name
					if((i=countersByName.get(oldname))==null){
						//This should not happen, all names should have a counter
						i=new Integer(1);
						countersByName.put(oldname, i);
					}
					
					if(i.intValue()==1){
						//There is just one group with this name
						String newname1 = oldname+" 1";
						String newname2 = oldname+" 2";
						// we DO NOT delete the group existing in the HashMap, so that we know that it originally was there. But we have to delete them before the end
						// We check that the proposed new names do not already exist
						while(groupsByName.get(newname1)!=null) newname1 = newname1+" 1";
						while(groupsByName.get(newname2)!=null) newname2 = newname2+" 2";
						
						// we change the name for both the existing group "Group 1" and the new group "Group 2"
						exist.setName(newname1);
						g.setName(newname2);
						// we set the counter for "Group" to 2, and the counter for Group 1 and Group 2 to 1
						countersByName.put(oldname, new Integer(2));
						countersByName.put(exist.getName(), new Integer(1));
						countersByName.put(g.getName(), new Integer(1));
						
						// we put both groups in the groups hashmap
						groupsByName.put(exist.getName(), exist);
						groupsByName.put(g.getName(), g);
						
					}else{//That is, there is more than one group with this name
						i = new Integer(i.intValue()+1);
						String newname = oldname + " " + i.intValue();
						while(groupsByName.get(newname)!=null) newname = newname+" "+i.intValue();
						
						g.setName(newname);
						countersByName.put(oldname, i);
						countersByName.put(newname, new Integer(1));
						// we put the group in the groups hashmap
						groupsByName.put(g.getName(), g);
					}
					
				}else{
					//The name does not exist so far, we introduce the group in the HashMaps
					groupsByName.put(g.getName(), g);
					countersByName.put(g.getName(), new Integer(1));
				}
				
				
			}//end for
			
			//We populate the groups with the modified version
			//Apparently this is not needed, since the groups are references, and they have already been updated with the unique names
			//			groups = new ArrayList<Group>();
//			for(Iterator<Entry<String, Group>> it2 = groupsByName.entrySet().iterator();it2.hasNext();){
//				Entry<String, Group> entry = it2.next();
//				Group g = entry.getValue();
//				//We throw out the old groups (counter bigger than one)
//				if(countersByName.get(g.getName())!=null && countersByName.get(g.getName()).intValue()>1) continue;
//				else groups.add(g);
//			}
			
			
		}//end if
		
	}

	private String generateUniqueId(HashMap<String, Participant> vleUsers,
			HashMap<String, String> randomIds) {
		// TODO Auto-generated method stub
		
		String uniqueId = null;
		
		do{
		  uniqueId = ""+(new Random()).hashCode();
		}while(vleUsers.containsKey(uniqueId) || randomIds.containsKey(uniqueId));
		
		return uniqueId;
	}

	public Deploy mapRolesGroupsParticipants(Deploy deploy) {

		ArrayList<glueps.core.model.Role> staffRoles = deploy.getDesign().getStaffRoles();
		
		ArrayList<Participant> fixedParticipants = null;
		participants.entrySet().iterator();
		
		
		if(deploy.getParticipants()!=null){
			for(Iterator<Entry<String,Participant>> it = participants.entrySet().iterator(); it.hasNext();){
				//We iterate through the participants
				Participant part = it.next().getValue();
				
				//We get the participants' groups
				ArrayList<Group> groupsForPart = deploy.getGroupsForParticipant(part.getId());
				
				//If any of those participant's groups matches any of the staff roles, we set its role to staff
				if(groupsForPart!=null){
					for(Iterator<Group> it2 = groupsForPart.iterator(); it2.hasNext();){
						//does this group have in its ID the ID of a staff role?
						Group g = it2.next();
						if(staffRoles!=null){//are there teachers at all in the design?
							for(Iterator<glueps.core.model.Role> it3 = staffRoles.iterator(); it3.hasNext(); ){
								glueps.core.model.Role r = it3.next();
								if(g.getId().contains((CharSequence) r.getId())){
									//It is a teacher! We stop searching
									part.setStaff(true);
									break;
								}
								
							}
							
							if(part.isStaff()){//if we found out that he is a teacher, we stop searching
								break;
							} //Else, we search in the next group
							
						}else return deploy;//if there are no teachers, we just return the same deploy
						
					}
					//We have gone through all the participants groups. We add the participant to the fixed participant list
					if(fixedParticipants == null) fixedParticipants = new ArrayList<Participant>();
					fixedParticipants.add(part);
					
				}
				
			}
			
			deploy.setParticipants(fixedParticipants);
			return deploy;
			
		} else return deploy;
	}
	
}
