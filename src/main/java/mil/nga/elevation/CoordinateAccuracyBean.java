
package mil.nga.elevation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CoordinateAccuracyBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CoordinateAccuracyBean"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="abshorzacc" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="absvertacc" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="relhorzacc" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="relvertacc" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CoordinateAccuracyBean", propOrder = {
    "abshorzacc",
    "absvertacc",
    "relhorzacc",
    "relvertacc"
})
public class CoordinateAccuracyBean {

    @XmlElement(required = true)
    protected String abshorzacc;
    @XmlElement(required = true)
    protected String absvertacc;
    @XmlElement(required = true)
    protected String relhorzacc;
    @XmlElement(required = true)
    protected String relvertacc;

    /**
     * Gets the value of the abshorzacc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbshorzacc() {
        return abshorzacc;
    }

    /**
     * Sets the value of the abshorzacc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbshorzacc(String value) {
        this.abshorzacc = value;
    }

    /**
     * Gets the value of the absvertacc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbsvertacc() {
        return absvertacc;
    }

    /**
     * Sets the value of the absvertacc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbsvertacc(String value) {
        this.absvertacc = value;
    }

    /**
     * Gets the value of the relhorzacc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelhorzacc() {
        return relhorzacc;
    }

    /**
     * Sets the value of the relhorzacc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelhorzacc(String value) {
        this.relhorzacc = value;
    }

    /**
     * Gets the value of the relvertacc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelvertacc() {
        return relvertacc;
    }

    /**
     * Sets the value of the relvertacc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelvertacc(String value) {
        this.relvertacc = value;
    }

}
