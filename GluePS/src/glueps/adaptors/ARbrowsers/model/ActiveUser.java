package glueps.adaptors.ARbrowsers.model;

import glueps.adaptors.ARbrowsers.service.AuthService;
import glueps.core.model.Deploy;
import glueps.core.model.Participant;
import glueps.core.persistence.JpaManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

public class ActiveUser extends LoggedUser {

	private String appType;
	private Double lat;
	private Double lon;
	private Double orientation;
	private boolean isStaff;
	private String currentActivityId;
	private Long activeTimestamp;



	public ActiveUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ActiveUser(String appType, Double lat, Double lon, Double orientation, String uid,
			String username, String deployId, String positionType) {
		super(uid, username, deployId, positionType);
		this.appType = appType;
		this.lat = lat;
		this.lon = lon;
		this.orientation = orientation;
		this.activeTimestamp = new Date().getTime();
		this.isStaff = usernameIsStaff(deployId, username);
	}

	public ActiveUser(String appType, Double lat, Double lon, String uid,
			String username, String deployId, String positionType) {
		super(uid, username, deployId, positionType);
		this.appType = appType;
		this.lat = lat;
		this.lon = lon;
		this.orientation = null;
		this.activeTimestamp = new Date().getTime();
		this.isStaff = usernameIsStaff(deployId, username);
		;
		// TODO Auto-generated constructor stub
	}

	public ActiveUser(String appType, Double lat, Double lon) {
		super();
		this.appType = appType;
		this.lat = lat;
		this.lon = lon;
		this.orientation = null;
		this.activeTimestamp = new Date().getTime();
		;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}
	
	public Double getOrientation() {
		return orientation;
	}

	public void setOrientation(Double orientation) {
		this.orientation = orientation;
	}
	
	public Long getActiveTimestamp() {
		return activeTimestamp;
	}

	public void setActiveTimestamp(Long activeTimestamp) {
		this.activeTimestamp = activeTimestamp;
	}



	public boolean isStaff() {
		return isStaff;
	}

	public void setStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}


	
	private boolean usernameIsStaff (String deployId, String username){
		boolean result = false;
    	JpaManager dbmanager = JpaManager.getInstance();
		Deploy deploy = null;
   		deploy = dbmanager.findDeployObjectById(deployId);
   		if (deploy!= null){
   			ArrayList<Participant> participants = deploy.getParticipants();
   			for (Participant participant : participants){
   				if (participant.getName().equals(username)){
   	   	   			if (participant.isStaff()){
   	   	   				result = true;
   	   	   				break;
   	   	   			}
   				}

   			}

   		}
   		return result;
	}

}
