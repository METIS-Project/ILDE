package glueps.core.service.inprocess;

import glueps.core.model.Deploy;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Future;

/**
 * Class for the "in process" service. This service keeps basic information of the deploy process while the deploy is being deployed into a VLE.
 * The information is added when the deploy process starts and is removed when it finishes
 *
 * @author  Javier Enrique Hoyos Torio
 * @version 
 * @package glue.core.service.inprocess
 */

public class InProcessService {
	
	/**
	 * The InProcessInfo items that are "in process"
	 */
	private ArrayList<InProcessInfo> inprocessItems;
	/**
	 * The maximum number of milliseconds that are allowed without checking if a deploy is still in process
	 */
	private static final long MAX_WITHOUT_CHECKING = 300000;
	/**
	 * The maximum number of milliseconds that a deploy can be in process
	 */
	private static final long MAX_DURATION = 1200000;

	public InProcessService(){
		inprocessItems = new ArrayList<InProcessInfo>();
	}
	
	/**
	 * The deploy process starts
	 * @parameter info The info of the deploy that is going to be deployed
	 * @parameter process The result of the asynchronous computation
	 * @return the process was added to the list
	 */
	public synchronized boolean startsProcess(InProcessInfo info, Future<Deploy> process){
		if (info == null) return false;
		if (inprocessItems.add(info)){
			info.setStarted(String.valueOf(new Date().getTime()));
			info.setLastChecked(info.getStarted());
			info.setProcess(process);
			return true;
		}else{
			info.setStarted(null);
			info.setLastChecked(null);
			info.setProcess(null);
			return false;
		}
		
	}
	
	/**
	 * The deploy process finished rightly
	 * @parameter info The info of the deploy whose deploy process finished
	 * @return the process was removed from the list
	 */
	public synchronized boolean finishesProcess(InProcessInfo info){
		return inprocessItems.remove(info);
	}
	
	/**
	 * Ask if a deploy is in process
	 * @parameter info The info of the deploy process we are asking about
	 * @return the deploy process is in process or not
	 */
	public synchronized boolean askDeployInProcess(InProcessInfo info){
		//update first the process info in case the process timeout has expired
		updateInProcessItem(info);
		return isInProcess(info);
	}
	
	/**
	 * Check if there is an active deploy process in an specific VLE and course
	 * @parameter accessLocation the URL of the VLE
	 * @parameter courseid the course id 
	 * @return there is an active deploy in process in that VLE and course or not
	 */
	public synchronized boolean askCourseInProcess(String accessLocation, String courseid){
		updateInProcessItems(accessLocation, courseid);
		for(InProcessInfo item: inprocessItems){
			if (item.getAccessLocation().equals(accessLocation) && item.getCourseid().equals(courseid)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the current stored status of the whole info of a deploy in process
	 * @param info the deploy in process whose info wants to be gotten
	 * @return the current status of the info of the deploy in process
	 */
	public synchronized InProcessInfo getDeployInProcessInfo(InProcessInfo info){
		int index = inprocessItems.indexOf(info);
		if (index != -1){
			return inprocessItems.get(index);
		}else{
			return null;
		}
	}
	
	/**
	 * Get the current stored status of the whole info of a deploy process in a VLE and course 
	 * @param accessLocation the URL of the VLE
	 * @param courseid the course id
	 * @return the current status of the info of the deploy in process in that VLE and course
	 */
	public synchronized InProcessInfo getCourseInProcessInfo(String accessLocation, String courseid){
		for(InProcessInfo item: inprocessItems){
			if (item.getAccessLocation().equals(accessLocation) && item.getCourseid().equals(courseid)){
				return item;
			}
		}
		return null; 
	}
	
	/**
	 * Check if a deploy is in process and update its lastChecked value
	 * @parameter info The info of the deploy process to be checked
	 * @return the deploy process is in process or not
	 */
	public synchronized boolean checkDeployInProcess(InProcessInfo info){
		InProcessInfo deployInfo = getDeployInProcessInfo(info);
		if (deployInfo!=null){
			//update the last checked value
			deployInfo.setLastChecked(String.valueOf(new Date().getTime()));
			return true;
		}
		return false;
	}
	
	
	/**
	 * Returns whether a deploy process started but it hasn't finished yet
	 * @parameter deploy The info of the deploy
	 * @return the deploy process is in process or not
	 */
	private synchronized boolean isInProcess(InProcessInfo info){
		return inprocessItems.contains(info);
	}
	
	/**
	 * Update the deploy in process info finishing it if its timeout has expired
	 * @param info The info of the deploy to be updated
	 */
	private synchronized void updateInProcessItem(InProcessInfo info){
		if (inprocessItems.contains(info)){
			InProcessInfo deployInfo = getDeployInProcessInfo(info);
			long now = new Date().getTime();
			long lastChecked = Long.valueOf(deployInfo.getLastChecked());
			long started = Long.valueOf(deployInfo.getStarted());
			if ( ((now-lastChecked) >= MAX_WITHOUT_CHECKING) || ((now-started) >= MAX_DURATION) ){
				//We stop the process first if it hasn't finished yet
				deployInfo.getProcess().cancel(true);
				//then we remove the process info from the list
				inprocessItems.remove(info);
			}
		}
	}
	
	/**
	 * Update the deploy in process info in a VLE and course finishing it if its timeout has expired
	 * @param accessLocation the URL of the VLE
	 * @param courseid the course id
	 */
	private synchronized void updateInProcessItems(String accessLocation, String courseid){
		long now = new Date().getTime();
		Iterator<InProcessInfo> iterator = inprocessItems.iterator();
		while (iterator.hasNext()){
			InProcessInfo item = iterator.next();
			if (item.getAccessLocation().equals(accessLocation) && item.getCourseid().equals(courseid)){
				long lastChecked = Long.valueOf(item.getLastChecked());
				long started = Long.valueOf(item.getStarted());
				if ( ((now-lastChecked) >= MAX_WITHOUT_CHECKING) || ((now-started) >= MAX_DURATION) ){
					//We stop the process first if it hasn't finished yet
					item.getProcess().cancel(true);
					iterator.remove();
				}
			}
		}
	}
}
