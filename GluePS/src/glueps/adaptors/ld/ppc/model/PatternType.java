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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para PatternType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="PatternType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LearningOutcome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TeachingLearningActivity" type="{}TeachingLearningActivityType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PatternType", propOrder = {
    "learningOutcome",
    "teachingLearningActivity"
})
@XmlRootElement(name = "Pattern")
public class PatternType {

    @XmlElement(name = "LearningOutcome", required = true)
    protected String learningOutcome;
    @XmlElement(name = "TeachingLearningActivity", required = true)
    protected List<TeachingLearningActivityType> teachingLearningActivity;

    /**
     * Obtiene el valor de la propiedad learningOutcome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLearningOutcome() {
        return learningOutcome;
    }

    /**
     * Define el valor de la propiedad learningOutcome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLearningOutcome(String value) {
        this.learningOutcome = value;
    }

    /**
     * Gets the value of the teachingLearningActivity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the teachingLearningActivity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTeachingLearningActivity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TeachingLearningActivityType }
     * 
     * 
     */
    public List<TeachingLearningActivityType> getTeachingLearningActivity() {
        if (teachingLearningActivity == null) {
            teachingLearningActivity = new ArrayList<TeachingLearningActivityType>();
        }
        return this.teachingLearningActivity;
    }

}
