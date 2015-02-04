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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{}TYPE" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{}ID"/>
 *           &lt;element ref="{}NAME"/>
 *           &lt;element ref="{}SHORTNAME"/>
 *         &lt;/sequence>
 *         &lt;choice minOccurs="0">
 *           &lt;element ref="{}ASSIGNMENTS"/>
 *           &lt;element ref="{}CAPABILITIES"/>
 *         &lt;/choice>
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
    "type",
    "id",
    "name",
    "shortname",
    "assignments",
    "capabilities"
})
@XmlRootElement(name = "ROLE")
public class ROLE {

    @XmlElement(name = "TYPE")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String type;
    @XmlElement(name = "ID")
    protected BigInteger id;
    @XmlElement(name = "NAME")
    protected String name;
    @XmlElement(name = "SHORTNAME")
    protected String shortname;
    @XmlElement(name = "ASSIGNMENTS")
    protected ASSIGNMENTS assignments;
    @XmlElement(name = "CAPABILITIES")
    protected CAPABILITIES capabilities;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTYPE() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTYPE(String value) {
        this.type = value;
    }

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
     * Gets the value of the shortname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHORTNAME() {
        return shortname;
    }

    /**
     * Sets the value of the shortname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHORTNAME(String value) {
        this.shortname = value;
    }

    /**
     * Gets the value of the assignments property.
     * 
     * @return
     *     possible object is
     *     {@link ASSIGNMENTS }
     *     
     */
    public ASSIGNMENTS getASSIGNMENTS() {
        return assignments;
    }

    /**
     * Sets the value of the assignments property.
     * 
     * @param value
     *     allowed object is
     *     {@link ASSIGNMENTS }
     *     
     */
    public void setASSIGNMENTS(ASSIGNMENTS value) {
        this.assignments = value;
    }

    /**
     * Gets the value of the capabilities property.
     * 
     * @return
     *     possible object is
     *     {@link CAPABILITIES }
     *     
     */
    public CAPABILITIES getCAPABILITIES() {
        return capabilities;
    }

    /**
     * Sets the value of the capabilities property.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPABILITIES }
     *     
     */
    public void setCAPABILITIES(CAPABILITIES value) {
        this.capabilities = value;
    }

}
