//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.5 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: PM.02.28 a las 06:36:45 PM CET 
//


package glueps.adaptors.ld.ppc.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para StudentActivityType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="StudentActivityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ActivityText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Duration" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="GroupSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TeacherPresence" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Resource" type="{}ResourceType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentActivityType", propOrder = {
    "type",
    "activityText",
    "duration",
    "groupSize",
    "teacherPresence",
    "resource"
})
public class StudentActivityType {

    @XmlElement(name = "Type", required = true)
    protected String type;
    @XmlElement(name = "ActivityText", required = true)
    protected String activityText;
    @XmlElement(name = "Duration")
    protected int duration;
    @XmlElement(name = "GroupSize")
    protected int groupSize;
    @XmlElement(name = "TeacherPresence")
    protected boolean teacherPresence;
    @XmlElement(name = "Resource", required = true)
    protected List<ResourceType> resource;

    /**
     * Obtiene el valor de la propiedad type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Define el valor de la propiedad type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Obtiene el valor de la propiedad activityText.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivityText() {
        return activityText;
    }

    /**
     * Define el valor de la propiedad activityText.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivityText(String value) {
        this.activityText = value;
    }

    /**
     * Obtiene el valor de la propiedad duration.
     * 
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Define el valor de la propiedad duration.
     * 
     */
    public void setDuration(int value) {
        this.duration = value;
    }

    /**
     * Obtiene el valor de la propiedad groupSize.
     * 
     */
    public int getGroupSize() {
        return groupSize;
    }

    /**
     * Define el valor de la propiedad groupSize.
     * 
     */
    public void setGroupSize(int value) {
        this.groupSize = value;
    }

    /**
     * Obtiene el valor de la propiedad teacherPresence.
     * 
     */
    public boolean isTeacherPresence() {
        return teacherPresence;
    }

    /**
     * Define el valor de la propiedad teacherPresence.
     * 
     */
    public void setTeacherPresence(boolean value) {
        this.teacherPresence = value;
    }

    /**
     * Gets the value of the resource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceType }
     * 
     * 
     */
    public List<ResourceType> getResource() {
        if (resource == null) {
            resource = new ArrayList<ResourceType>();
        }
        return this.resource;
    }

}
