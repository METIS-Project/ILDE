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
 *         &lt;element ref="{}GROUPID"/>
 *         &lt;element ref="{}USERID"/>
 *         &lt;element ref="{}TIMEADDED"/>
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
    "groupid",
    "userid",
    "timeadded"
})
@XmlRootElement(name = "MEMBER")
public class MEMBER implements Cloneable{

    @XmlElement(name = "GROUPID", required = true)
    protected BigInteger groupid;
    @XmlElement(name = "USERID", required = true)
    protected BigInteger userid;
    @XmlElement(name = "TIMEADDED", required = true)
    protected TIMEADDED timeadded;

    /**
     * Gets the value of the groupid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGROUPID() {
        return groupid;
    }

    /**
     * Sets the value of the groupid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGROUPID(BigInteger value) {
        this.groupid = value;
    }

    /**
     * Gets the value of the userid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getUSERID() {
        return userid;
    }

    /**
     * Sets the value of the userid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setUSERID(BigInteger value) {
        this.userid = value;
    }

    /**
     * Gets the value of the timeadded property.
     * 
     * @return
     *     possible object is
     *     {@link TIMEADDED }
     *     
     */
    public TIMEADDED getTIMEADDED() {
        return timeadded;
    }

    /**
     * Sets the value of the timeadded property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIMEADDED }
     *     
     */
    public void setTIMEADDED(TIMEADDED value) {
        this.timeadded = value;
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
