//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.5 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: PM.02.28 a las 06:36:45 PM CET 
//


package glueps.adaptors.ld.ppc.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ResourceType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ResourceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResourceName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ResourceAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceType", propOrder = {
    "resourceName",
    "resourceAddress"
})
public class ResourceType {

    @XmlElement(name = "ResourceName", required = true)
    protected String resourceName;
    @XmlElement(name = "ResourceAddress", required = true)
    protected String resourceAddress;

    /**
     * Obtiene el valor de la propiedad resourceName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Define el valor de la propiedad resourceName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceName(String value) {
        this.resourceName = value;
    }

    /**
     * Obtiene el valor de la propiedad resourceAddress.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceAddress() {
        return resourceAddress;
    }

    /**
     * Define el valor de la propiedad resourceAddress.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceAddress(String value) {
        this.resourceAddress = value;
    }

}
