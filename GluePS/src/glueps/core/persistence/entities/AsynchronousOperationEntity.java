package glueps.core.persistence.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * JPA Entity for the glueps_asynchronous_operations table that 
 * stores information about the current status of an asynchronous operation that is started as a result of a REST request 
 * that is accepted but its response takes a long time to be generated
 * @author Javier Enrique Hoyos Torio
 *
 */

@Entity
@Table(name ="glueps_asynchronous_operations")
@NamedQueries({@NamedQuery(name = AsynchronousOperationEntity.QUERY_FIND_BY_ID, query="SELECT asynop FROM AsynchronousOperationEntity asynop " + "where asynop.id = :" + AsynchronousOperationEntity.PARAM_ID),
			   @NamedQuery(name = AsynchronousOperationEntity.QUERY_FIND_BY_OPERATION, query="SELECT asynop FROM AsynchronousOperationEntity asynop " + "where asynop.operation = :" + AsynchronousOperationEntity.PARAM_OPERATION),
			   @NamedQuery(name = AsynchronousOperationEntity.QUERY_LIST_ALL, query="SELECT asynop FROM AsynchronousOperationEntity asynop "),
			   @NamedQuery(name = AsynchronousOperationEntity.QUERY_DELETE_BY_ID, query = "DELETE FROM AsynchronousOperationEntity asynop WHERE asynop.id=:id")
			})
public class AsynchronousOperationEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7861470310268476645L;
	public static final String QUERY_FIND_BY_ID = "AsynchronousOperationEntity.findById";
	public static final String QUERY_FIND_BY_OPERATION = "AsynchronousOperationEntity.findByOperation";
	public static final String QUERY_LIST_ALL = "AsynchronousOperationEntity.listAll";
	public static final String QUERY_DELETE_BY_ID = "AsynchronousOperationEntity.deleteById";
	
	public static final String PARAM_ID = "id";
	public static final String PARAM_OPERATION = "operation";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Basic (optional = false)
	@Column(name = "id")
	private long id;
	
	@Basic (optional = false)
	@Column (name = "operation", unique = true)
	private String operation;
	
	@Basic (optional = false)
	@Column (name = "status")
	private String status;
	
	@Basic (optional = true)
	@Column (name = "description")
	private String description;
	
	@Basic (optional = true)
	@Column (name = "resource")
	private String resource;
	
	@Basic (optional = false)
	@Column (name = "started")
	@Temporal(TemporalType.TIMESTAMP)
	private Date started;
	
	@Basic (optional = false)
	@Column (name = "finished")
	@Temporal (TemporalType.TIMESTAMP)
	private Date finished;
	/*@Basic (optional = true)
	@Column (name = "started")
	private long started;*/
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=1048576) 
	private byte[] file;
	
	public AsynchronousOperationEntity(){
		
	}
	
	public AsynchronousOperationEntity(long id){
		this.id = id;
	}
	
	public AsynchronousOperationEntity(long id, String operation, String status, String description, String resource, Date started, Date finished){
		this.id = id;
		this.operation = operation;
		this.status = status;
		this.description = description;
		this.resource = resource;
		this.started = started;
		this.finished = finished;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public void setFile(byte[] file) {
		this.file = file;
	}

	public byte[] getFile() {
		return file;
	}
}
