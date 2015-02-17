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
 *         &lt;element ref="{}HEADER"/>
 *         &lt;element ref="{}SECTIONS"/>
 *         &lt;element ref="{}USERS"/>
 *         &lt;element ref="{}GROUPS"/>
 *         &lt;element ref="{}GROUPINGS"/>
 *         &lt;element ref="{}GROUPINGSGROUPS"/>
 *         &lt;element ref="{}MODULES"/>
 *         &lt;element ref="{}FORMATDATA"/>
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
    "header",
    "sections",
    "users",
    "groups",
    "groupings",
    "groupingsgroups",
    "modules",
    "formatdata"
})
@XmlRootElement(name = "COURSE")
public class COURSE {

    @XmlElement(name = "HEADER", required = true)
    protected HEADER header;
    @XmlElement(name = "SECTIONS", required = true)
    protected SECTIONS sections;
    @XmlElement(name = "USERS", required = true)
    protected USERS users;
    @XmlElement(name = "GROUPS", required = true)
    protected GROUPS groups;
    @XmlElement(name = "GROUPINGS", required = true)
    protected GROUPINGS groupings;
    @XmlElement(name = "GROUPINGSGROUPS", required = true)
    protected GROUPINGSGROUPS groupingsgroups;
    @XmlElement(name = "MODULES", required = true)
    protected MODULES modules;
    @XmlElement(name = "FORMATDATA", required = true)
    protected FORMATDATA formatdata;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HEADER }
     *     
     */
    public HEADER getHEADER() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HEADER }
     *     
     */
    public void setHEADER(HEADER value) {
        this.header = value;
    }

    /**
     * Gets the value of the sections property.
     * 
     * @return
     *     possible object is
     *     {@link SECTIONS }
     *     
     */
    public SECTIONS getSECTIONS() {
        return sections;
    }

    /**
     * Sets the value of the sections property.
     * 
     * @param value
     *     allowed object is
     *     {@link SECTIONS }
     *     
     */
    public void setSECTIONS(SECTIONS value) {
        this.sections = value;
    }

    /**
     * Gets the value of the users property.
     * 
     * @return
     *     possible object is
     *     {@link USERS }
     *     
     */
    public USERS getUSERS() {
        return users;
    }

    /**
     * Sets the value of the users property.
     * 
     * @param value
     *     allowed object is
     *     {@link USERS }
     *     
     */
    public void setUSERS(USERS value) {
        this.users = value;
    }

    /**
     * Gets the value of the groups property.
     * 
     * @return
     *     possible object is
     *     {@link GROUPS }
     *     
     */
    public GROUPS getGROUPS() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value
     *     allowed object is
     *     {@link GROUPS }
     *     
     */
    public void setGROUPS(GROUPS value) {
        this.groups = value;
    }

    /**
     * Gets the value of the groupings property.
     * 
     * @return
     *     possible object is
     *     {@link GROUPINGS }
     *     
     */
    public GROUPINGS getGROUPINGS() {
        return groupings;
    }

    /**
     * Sets the value of the groupings property.
     * 
     * @param value
     *     allowed object is
     *     {@link GROUPINGS }
     *     
     */
    public void setGROUPINGS(GROUPINGS value) {
        this.groupings = value;
    }

    /**
     * Gets the value of the groupingsgroups property.
     * 
     * @return
     *     possible object is
     *     {@link GROUPINGSGROUPS }
     *     
     */
    public GROUPINGSGROUPS getGROUPINGSGROUPS() {
        return groupingsgroups;
    }

    /**
     * Sets the value of the groupingsgroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link GROUPINGSGROUPS }
     *     
     */
    public void setGROUPINGSGROUPS(GROUPINGSGROUPS value) {
        this.groupingsgroups = value;
    }

    /**
     * Gets the value of the modules property.
     * 
     * @return
     *     possible object is
     *     {@link MODULES }
     *     
     */
    public MODULES getMODULES() {
        return modules;
    }

    /**
     * Sets the value of the modules property.
     * 
     * @param value
     *     allowed object is
     *     {@link MODULES }
     *     
     */
    public void setMODULES(MODULES value) {
        this.modules = value;
    }

    /**
     * Gets the value of the formatdata property.
     * 
     * @return
     *     possible object is
     *     {@link FORMATDATA }
     *     
     */
    public FORMATDATA getFORMATDATA() {
        return formatdata;
    }

    /**
     * Sets the value of the formatdata property.
     * 
     * @param value
     *     allowed object is
     *     {@link FORMATDATA }
     *     
     */
    public void setFORMATDATA(FORMATDATA value) {
        this.formatdata = value;
    }

}
