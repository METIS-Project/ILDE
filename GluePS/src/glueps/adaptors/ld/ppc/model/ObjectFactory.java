//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.5 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: PM.02.28 a las 06:36:45 PM CET 
//


package glueps.adaptors.ld.ppc.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Pattern_QNAME = new QName("", "Pattern");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PatternType }
     * 
     */
    public PatternType createPatternType() {
        return new PatternType();
    }

    /**
     * Create an instance of {@link ResourceType }
     * 
     */
    public ResourceType createResourceType() {
        return new ResourceType();
    }

    /**
     * Create an instance of {@link StudentActivityType }
     * 
     */
    public StudentActivityType createStudentActivityType() {
        return new StudentActivityType();
    }

    /**
     * Create an instance of {@link TeachingLearningActivityType }
     * 
     */
    public TeachingLearningActivityType createTeachingLearningActivityType() {
        return new TeachingLearningActivityType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PatternType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Pattern")
    public JAXBElement<PatternType> createPattern(PatternType value) {
        return new JAXBElement<PatternType>(_Pattern_QNAME, PatternType.class, null, value);
    }

}
