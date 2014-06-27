//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.12 at 04:14:55 PM CEST 
//


package glueps.adaptors.vle.moodle.model;

import java.math.BigDecimal;
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
 *         &lt;element ref="{}ID"/>
 *         &lt;element ref="{}SCOID"/>
 *         &lt;element ref="{}RULECONDITIONSID"/>
 *         &lt;element ref="{}REFRENCEDOBJECTIVE"/>
 *         &lt;element ref="{}MEASURETHRESHOLD"/>
 *         &lt;element ref="{}OPERATOR"/>
 *         &lt;element ref="{}COND"/>
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
    "scoid",
    "ruleconditionsid",
    "refrencedobjective",
    "measurethreshold",
    "operator",
    "cond"
})
@XmlRootElement(name = "SEQ_RULECOND_DATA")
public class SEQRULECONDDATA {

    @XmlElement(name = "ID", required = true)
    protected BigInteger id;
    @XmlElement(name = "SCOID", required = true)
    protected BigInteger scoid;
    @XmlElement(name = "RULECONDITIONSID", required = true)
    protected BigInteger ruleconditionsid;
    @XmlElement(name = "REFRENCEDOBJECTIVE", required = true)
    protected REFRENCEDOBJECTIVE refrencedobjective;
    @XmlElement(name = "MEASURETHRESHOLD", required = true)
    protected BigDecimal measurethreshold;
    @XmlElement(name = "OPERATOR", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String operator;
    @XmlElement(name = "COND", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String cond;

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
     * Gets the value of the scoid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSCOID() {
        return scoid;
    }

    /**
     * Sets the value of the scoid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSCOID(BigInteger value) {
        this.scoid = value;
    }

    /**
     * Gets the value of the ruleconditionsid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRULECONDITIONSID() {
        return ruleconditionsid;
    }

    /**
     * Sets the value of the ruleconditionsid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRULECONDITIONSID(BigInteger value) {
        this.ruleconditionsid = value;
    }

    /**
     * Gets the value of the refrencedobjective property.
     * 
     * @return
     *     possible object is
     *     {@link REFRENCEDOBJECTIVE }
     *     
     */
    public REFRENCEDOBJECTIVE getREFRENCEDOBJECTIVE() {
        return refrencedobjective;
    }

    /**
     * Sets the value of the refrencedobjective property.
     * 
     * @param value
     *     allowed object is
     *     {@link REFRENCEDOBJECTIVE }
     *     
     */
    public void setREFRENCEDOBJECTIVE(REFRENCEDOBJECTIVE value) {
        this.refrencedobjective = value;
    }

    /**
     * Gets the value of the measurethreshold property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMEASURETHRESHOLD() {
        return measurethreshold;
    }

    /**
     * Sets the value of the measurethreshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMEASURETHRESHOLD(BigDecimal value) {
        this.measurethreshold = value;
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOPERATOR() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOPERATOR(String value) {
        this.operator = value;
    }

    /**
     * Gets the value of the cond property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOND() {
        return cond;
    }

    /**
     * Sets the value of the cond property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOND(String value) {
        this.cond = value;
    }

}