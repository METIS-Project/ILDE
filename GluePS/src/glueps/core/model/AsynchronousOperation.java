package glueps.core.model;

import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AsynchronousOperation {
	
	@XmlTransient
	private String id;
	private String operation;
	private String status;
	private String description;
	private String resource;
	
	private Date started;
	private Date finished;

	public static final String STATUS_OK = "ok";
	public static final String STATUS_ERROR = "error";
	public static final String STATUS_INPROGRESS = "in progress";
	public static final String STATUS_PENDING = "pending";
	public static final String STATUS_NOT_RETURNED = "not returned";
	
	public AsynchronousOperation(){
		
	}
	
	public AsynchronousOperation(String id, String operation, String status){
		this.id = id;
		this.operation = operation;
		this.status = status;
	}
	
	public AsynchronousOperation(String id, String operation, String status, String description, String resource, Date started, Date finished){
		this.id = id;
		this.operation = operation;
		this.status = status;
		this.description = description;
		this.resource = resource;
		this.started = started;
		this.finished = finished;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public Date getStarted() {
		return started;
	}
	public void setStarted(Date started) {
		this.started = started;
	}
	
	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}
	
	public static String generateOperationId(){
		return UUID.randomUUID().toString();
	}

}
