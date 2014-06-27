package glueps.core.model;
import java.util.ArrayList;


public class Group {

	private String id;
	
	private String name;
	
	private String deployId;
	
	private ArrayList<String> participantIds;
	

	public Group() {
		super();
	}

	public Group(String id, String name, String deployId,
			ArrayList<String> participantIds) {
		super();
		this.id = id;
		this.name = name;
		this.deployId = deployId;
		this.participantIds = participantIds;
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

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public ArrayList<String> getParticipantIds() {
		return participantIds;
	}

	public void setParticipantIds(ArrayList<String> participantIds) {
		this.participantIds = participantIds;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", deployId=" + deployId
				+ ", participantIds=" + participantIds + "]";
	}

	
	
}
