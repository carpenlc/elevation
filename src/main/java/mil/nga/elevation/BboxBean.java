
package mil.nga.elevation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BboxBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BboxBean"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="lllon" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="lllat" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="urlon" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="urlat" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BboxBean", propOrder = {
    "lllon",
    "lllat",
    "urlon",
    "urlat"
})
public class BboxBean {

    @XmlElement(required = true)
    protected String lllon;
    @XmlElement(required = true)
    protected String lllat;
    @XmlElement(required = true)
    protected String urlon;
    @XmlElement(required = true)
    protected String urlat;

    /**
     * Gets the value of the lllon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLllon() {
        return lllon;
    }

    /**
     * Sets the value of the lllon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLllon(String value) {
        this.lllon = value;
    }

    /**
     * Gets the value of the lllat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLllat() {
        return lllat;
    }

    /**
     * Sets the value of the lllat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLllat(String value) {
        this.lllat = value;
    }

    /**
     * Gets the value of the urlon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlon() {
        return urlon;
    }

    /**
     * Sets the value of the urlon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlon(String value) {
        this.urlon = value;
    }

    /**
     * Gets the value of the urlat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlat() {
        return urlat;
    }

    /**
     * Sets the value of the urlat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlat(String value) {
        this.urlat = value;
    }

}
