package glueps.core.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LearningEnvironmentTypes")
@XmlAccessorType(XmlAccessType.FIELD)
public class LearningEnvironmentTypes {
	
	@XmlElement(name="LearningEnvironmentType")
	private List<LearningEnvironmentType> learningEnvironmentTypes;

	public LearningEnvironmentTypes(){
	}
	
	public LearningEnvironmentTypes(List<LearningEnvironmentType> learningEnvironmentTypes){
		this.learningEnvironmentTypes = learningEnvironmentTypes;
	}
	
	
	public List<LearningEnvironmentType> getLearningEnvironmentTypes() {
		return learningEnvironmentTypes;
	}

	public void setLearningEnvironmentTypes(List<LearningEnvironmentType> learningEnvironmentTypes) {
		this.learningEnvironmentTypes = learningEnvironmentTypes;
	}

}
