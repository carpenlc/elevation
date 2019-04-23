
package mil.nga.elevation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import mil.nga.security.SecurityElement;


/**
 * <p>Java class for GetMinMaxElevationsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetMinMaxElevationsResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="security" type="{mil:nga:security}SecurityElement"/&gt;
 *         &lt;element name="heightType" type="{mil:nga:elevation}HeightUnitType"/&gt;
 *         &lt;element name="minElevation" type="{mil:nga:elevation}ElevationBean"/&gt;
 *         &lt;element name="maxElevation" type="{mil:nga:elevation}ElevationBean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetMinMaxElevationsResponse", propOrder = {
    "security",
    "heightType",
    "minElevation",
    "maxElevation"
})
public class GetMinMaxElevationsResponse {

    @XmlElement(required = true)
    protected SecurityElement security;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected HeightUnitType heightType;
    @XmlElement(required = true)
    protected ElevationBean minElevation;
    @XmlElement(required = true)
    protected ElevationBean maxElevation;

    /**
     * Gets the value of the security property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityElement }
     *     
     */
    public SecurityElement getSecurity() {
        return security;
    }

    /**
     * Sets the value of the security property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityElement }
     *     
     */
    public void setSecurity(SecurityElement value) {
        this.security = value;
    }

    /**
     * Gets the value of the heightType property.
     * 
     * @return
     *     possible object is
     *     {@link HeightUnitType }
     *     
     */
    public HeightUnitType getHeightType() {
        return heightType;
    }

    /**
     * Sets the value of the heightType property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeightUnitType }
     *     
     */
    public void setHeightType(HeightUnitType value) {
        this.heightType = value;
    }

    /**
     * Gets the value of the minElevation property.
     * 
     * @return
     *     possible object is
     *     {@link ElevationBean }
     *     
     */
    public ElevationBean getMinElevation() {
        return minElevation;
    }

    /**
     * Sets the value of the minElevation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElevationBean }
     *     
     */
    public void setMinElevation(ElevationBean value) {
        this.minElevation = value;
    }

    /**
     * Gets the value of the maxElevation property.
     * 
     * @return
     *     possible object is
     *     {@link ElevationBean }
     *     
     */
    public ElevationBean getMaxElevation() {
        return maxElevation;
    }

    /**
     * Sets the value of the maxElevation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElevationBean }
     *     
     */
    public void setMaxElevation(ElevationBean value) {
        this.maxElevation = value;
    }

}
