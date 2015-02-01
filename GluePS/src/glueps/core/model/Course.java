package glueps.core.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course {

	public static final String PARAMETER_SEPARATOR=";";
	public static final String PARAMETER_VALUE_SEPARATOR="=";
	



	private String id;
	
	private String name;
	
	private URL relativeUrl;
	
	private String vleParameters;
	
	//private ArrayList<Participant> participants;
	private HashMap<String,Participant> participants;


	public Course() {
		super();
	}


	public Course(String id, String name, URL relativeUrl,
			String vleParameters, HashMap<String,Participant> participants) {
		super();
		this.id = id;
		this.name = name;
		this.relativeUrl = relativeUrl;
		this.vleParameters = vleParameters;
		this.participants = participants;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public URL getRelativeUrl() {
		return relativeUrl;
	}

	public void setRelativeUrl(URL relativeUrl) {
		this.relativeUrl = relativeUrl;
	}

	public String getVleParameters() {
		return vleParameters;
	}

	public void setVleParameters(String vleParameters) {
		this.vleParameters = vleParameters;
	}
	
	public String getVleParmeter(String parameter){
		
		if(this.vleParameters==null) return null;
		
		String[] parameters = this.vleParameters.split(PARAMETER_SEPARATOR);
		
		for(int i=0;i<parameters.length;i++){
			String[] keyvalue = parameters[i].split(PARAMETER_VALUE_SEPARATOR);
			
			if(keyvalue.length<2 || keyvalue.length>2) return null;
			else{
				if(keyvalue[0].equals(parameter)) return keyvalue[1];
			}
			
		}
		
		return null;
		
	}

	public void setParticipants(HashMap<String,Participant> participants) {
		this.participants = participants;
	}

	public HashMap<String,Participant> getParticipants() {
		return participants;
	}
	
	
	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", relativeUrl="
				+ relativeUrl + ", vleParameters=" + vleParameters
				+ ", participants=" + participants + "]";
	}

	
}
