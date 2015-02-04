package glueps.core.service.inprocess;

import glueps.core.model.Deploy;

import java.util.concurrent.Future;

/**
 * This class contains the information of a deploy that is being deployed into a VLE
 * @author Javier Enrique Hoyos Torio
 *
 */
public class InProcessInfo {
	/**
	 * The url of the VLE
	 */
	private String accessLocation;
	/**
	 * The course id
	 */
	private String courseid;
	/**
	 * The deploy id
	 */
	private String deployid;
	/**
	 * the time in milliseconds since January 1, 1970, 00:00:00 GMT when the process started
	 */
	private String started;
	/**
	 * the time in milliseconds since January 1, 1970, 00:00:00 GMT the last time the process was checked
	 */
	private String lastChecked;
	/**
	 * The result of the asynchronous computation made in the process
	 */
	private Future<Deploy> process;

	public InProcessInfo(String deployid, String accessLocation, String courseid){
		this.deployid = deployid;
		this.accessLocation = accessLocation;
		this.courseid = courseid;
		this.started = null;
		this.lastChecked = null;
		this.process = null;
	}

	public String getStarted() {
		return started;
	}

	protected void setStarted(String started) {
		this.started = started;
	}

	public String getAccessLocation() {
		return accessLocation;
	}

	public void setAccessLocation(String accessLocation) {
		this.accessLocation = accessLocation;
	}

	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	public String getDeployid() {
		return deployid;
	}

	public void setDeployid(String deployid) {
		this.deployid = deployid;
	}
	
	public String getLastChecked() {
		return lastChecked;
	}

	protected void setLastChecked(String lastChecked) {
		this.lastChecked = lastChecked;
	}
	
	public Future<Deploy> getProcess() {
		return process;
	}

	protected void setProcess(Future<Deploy> process) {
		this.process = process;
	}
	
	@Override
	public boolean equals(Object other){
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof InProcessInfo)) return false;
		InProcessInfo otherInf = (InProcessInfo) other;
		if (accessLocation.equals(otherInf.getAccessLocation()) && courseid.equals(otherInf.getCourseid()) && deployid.equals(otherInf.getDeployid())){
			return true;
		}else{
			return false;
		}
	}
	
	
	
}
