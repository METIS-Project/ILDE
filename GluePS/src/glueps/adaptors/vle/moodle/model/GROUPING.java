//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.12 at 04:14:55 PM CEST 
//


package glueps.adaptors.vle.moodle.model;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}ID"/>
 *         &lt;element ref="{}NAME"/>
 *         &lt;element ref="{}DESCRIPTION"/>
 *         &lt;element ref="{}CONFIGDATA"/>
 *         &lt;element ref="{}TIMECREATED"/>
 *         &lt;element ref="{}TIMEMODIFIED"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "name",
    "description",
    "configdata",
    "timecreated",
    "timemodified"
})
@XmlRootElement(name = "GROUPING")
public class GROUPING implements Cloneable{

    @XmlElement(name = "ID", required = true)
    protected BigInteger id;
    @XmlElement(name = "NAME", required = true)
    protected String name;
    @XmlElement(name = "DESCRIPTION", required = true)
    protected String description;
    @XmlElement(name = "CONFIGDATA", required = true)
    protected String configdata;
    @XmlElement(name = "TIMECREATED", required = true)
    protected TIMECREATED timecreated;
    @XmlElement(name = "TIMEMODIFIED", required = true)
    protected TIMEMODIFIED timemodified;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setID(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNAME() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNAME(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRIPTION() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRIPTION(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the configdata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONFIGDATA() {
        return configdata;
    }

    /**
     * Sets the value of the configdata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONFIGDATA(String value) {
        this.configdata = value;
    }

    /**
     * Gets the value of the timecreated property.
     * 
     * @return
     *     possible object is
     *     {@link TIMECREATED }
     *     
     */
    public TIMECREATED getTIMECREATED() {
        return timecreated;
    }

    /**
     * Sets the value of the timecreated property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIMECREATED }
     *     
     */
    public void setTIMECREATED(TIMECREATED value) {
        this.timecreated = value;
    }

    /**
     * Gets the value of the timemodified property.
     * 
     * @return
     *     possible object is
     *     {@link TIMEMODIFIED }
     *     
     */
    public TIMEMODIFIED getTIMEMODIFIED() {
        return timemodified;
    }

    /**
     * Sets the value of the timemodified property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIMEMODIFIED }
     *     
     */
    public void setTIMEMODIFIED(TIMEMODIFIED value) {
        this.timemodified = value;
    }
    public Object clone() 
    {
        Object clone = null;
         try 
         {
             clone = super.clone();
         }
         catch(CloneNotSupportedException e) 
         {
             // No debería ocurrir
         }
        return clone;
    }

}
