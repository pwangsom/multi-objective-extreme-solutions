//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0-b170531.0717 
//         See <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
//         Any modifications to this file will be lost upon recompilation of the source schema. 
//         Generated on: 2018.03.12 at 01:59:25 PM ICT 
//


package com.kmutt.sit.pegasus.xsd.element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Derivation of Plain filename, with added attributes for variable name recording.
 * 
 * <p>Java class for StdioType complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StdioType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://pegasus.isi.edu/schema/DAX}PlainFilenameType"&gt;
 *       &lt;attribute name="varname" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StdioType")
@XmlSeeAlso({
    com.kmutt.sit.pegasus.xsd.element.Adag.Job.Stdin.class,
    com.kmutt.sit.pegasus.xsd.element.Adag.Job.Stdout.class,
    com.kmutt.sit.pegasus.xsd.element.Adag.Job.Stderr.class
})
public class StdioType
    extends PlainFilenameType
{

    @XmlAttribute(name = "varname", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String varname;

    /**
     * Gets the value of the varname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVarname() {
        return varname;
    }

    /**
     * Sets the value of the varname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVarname(String value) {
        this.varname = value;
    }

}
