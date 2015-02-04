package glueps.core.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SimpleLFModelBuilder extends LFModelBuilder {


	public SimpleLFModelBuilder() throws MalformedURLException{
		
		//first, we create the tools and resources
		Resource expertIntA = new Resource("1", "Expert interview A", false, "http://www.ies.be/files/060313%20Interviews_VanAudenhove.pdf", null, null, null);
		Resource forum = new Resource("4", "Forum tool", true, "http://some.moodle.url", INTERNAL_TOOL_KIND, TOOL_TYPE_FORUM, null);

		resources.add(expertIntA);
		resources.add(forum);
		
		//then, we create the roles
		Role studentRole = new Role("1", "Learner: Jigsaw group", "This is the role of a student", false);
		
		roles.add(studentRole);
		
		//then, we create the activities
		Activity individualStudy = new Activity("1", "Individual study", "This is the first phase, where students study one expert interview to...", GROUP_MODE, null, null, new ArrayList<String>(Arrays.asList("1", "4")),  new ArrayList<String>(Arrays.asList("1")));
		
		//then, we create the design
		Design design = new Design("1", "Planet Game Scenario", "This is a design following the Planet Game scenario", "0", "Luis P. Prieto", new Date(), new ArrayList<String>(Arrays.asList("To acquire knowledge in the field of astronomy.", "To classify the planets with respect to their distance from the Sun (from the nearest one to the most distant)")), individualStudy, null, roles, resources);
		
		//We create the learning environment data
		LearningEnvironment learningEnvironment = new LearningEnvironment("1", "Local moodle", MOODLE_LE, "lprisan", new URL("http://some.url"),null);
		
		//Now, we create the deployment data
		//First, we create the participants
		participants.add(new Participant("1", "oagudia87@gmail.com", "1", "5;oagudia87@gmail.com;oagudia87@gmail.com;1301409082;",false));
		participants.add(new Participant("2", "juan.munoz", "1", "9;juan.munoz;juan.munoz@gsic.uva.es;1298990101;",false));
		participants.add(new Participant("3", "rmotmar87@hotmail.com", "1", "6;rmotmar87@hotmail.com;rmotmar87@hotmail.com;1302016192;",false));
		participants.add(new Participant("4", "lagfqn@hotmail.com", "1", "3;lagfqn@hotmail.com;lagfqn@hotmail.com;1302072762;",false));
		participants.add(new Participant("5", "juaase@tel.uva.es", "1", "7;juaase@tel.uva.es;juaase@tel.uva.es;1301651132;",true));
		
		//Then, we create the groups
		groups.add(new Group("1", "Group A", "1", new ArrayList<String>(Arrays.asList("1", "2"))));
		groups.add(new Group("2", "Group B", "1", new ArrayList<String>(Arrays.asList("3", "4"))));
		groups.add(new Group("3", "Teachers", "1", new ArrayList<String>(Arrays.asList("5"))));
		groups.add(new Group("4", "Class", "1", new ArrayList<String>(Arrays.asList("1", "2", "3", "4"))));
		
		//Then, we create the tool instances ... for internal tools the URL may not be necessary (they are created afterwards)
		toolInstances.add(new ToolInstance("2", "Forum A", "1", "4", new URL("http://some.moodle.chat.url")));

		toolInstances.add(new ToolInstance("3", "Forum B", "1", "4", new URL("http://some.other.moodle.chat.url")));
		
		//Then, we create the instanced activities
		//For the individual study
		instancedActivities.add(new InstancedActivity("1", "1", "1", "1", new ArrayList<String>(Arrays.asList("1")), new ArrayList<String>(Arrays.asList("2"))));
		instancedActivities.add(new InstancedActivity("2", "1", "1", "2", new ArrayList<String>(Arrays.asList("1")), new ArrayList<String>(Arrays.asList("3"))));
		
		//Finally, we create the deploy
		Deploy deploy = new Deploy("1", design, "Planet Game - local moodle", learningEnvironment, "lprisan", new Date(), null, instancedActivities, toolInstances, participants, groups);

		
		
	}

	
	
}
