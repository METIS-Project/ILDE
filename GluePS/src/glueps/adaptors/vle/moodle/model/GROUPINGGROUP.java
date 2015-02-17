/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
 *         &lt;element ref="{}GROUPINGID"/>
 *         &lt;element ref="{}GROUPID"/>
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
    "id",
    "groupingid",
    "groupid",
    "timeadded"
})
@XmlRootElement(name = "GROUPINGGROUP")
public class GROUPINGGROUP implements Cloneable{

    @XmlElement(name = "ID", required = true)
    protected BigInteger id;
    @XmlElement(name = "GROUPINGID", required = true)
    protected BigInteger groupingid;
    @XmlElement(name = "GROUPID", required = true)
    protected BigInteger groupid;
    @XmlElement(name = "TIMEADDED", required = true)
    protected TIMEADDED timeadded;

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
     * Gets the value of the groupingid property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGROUPINGID() {
        return groupingid;
    }

    /**
     * Sets the value of the groupingid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGROUPINGID(BigInteger value) {
        this.groupingid = value;
    }

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
