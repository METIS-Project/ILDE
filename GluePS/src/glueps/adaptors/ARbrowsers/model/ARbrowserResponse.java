package glueps.adaptors.ARbrowsers.model;

import org.restlet.data.MediaType;
import org.restlet.data.Status;

public class ARbrowserResponse {

	
	private String answer;
	private Status status;
	private MediaType mediaType;
	
	
	
	public ARbrowserResponse() {
		super();
		// TODO Auto-generated constructor stub
	}



	public ARbrowserResponse(String answer, Status status, MediaType mediaType) {
		super();
		this.answer = answer;
		this.status = status;
		this.mediaType = mediaType;
	}



	public String getAnswer() {
		return answer;
	}



	public void setAnswer(String answer) {
		this.answer = answer;
	}



	public Status getStatus() {
		return status;
	}



	public void setStatus(Status status) {
		this.status = status;
	}



	public MediaType getMediaType() {
		return mediaType;
	}



	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	
	
	
	
}
