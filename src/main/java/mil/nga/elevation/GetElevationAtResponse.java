
package mil.nga.elevation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import mil.nga.security.SecurityElement;


/**
 * <p>Java class for GetElevationAtResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetElevationAtResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="security" type="{mil:nga:security}SecurityElement"/&gt;
 *         &lt;element name="heightType" type="{mil:nga:elevation}HeightUnitType"/&gt;
 *         &lt;element name="elevations" type="{mil:nga:elevation}ElevationBean" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetElevationAtResponse", propOrder = {
    "security",
    "heightType",
    "elevations"
})
public class GetElevationAtResponse {

    @XmlElement(required = true)
    protected SecurityElement security;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected HeightUnitType heightType;
    @XmlElement(required = true)
    protected List<ElevationBean> elevations;

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
     * Gets the value of the elevations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elevations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElevations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ElevationBean }
     * 
     * 
     */
    public List<ElevationBean> getElevations() {
        if (elevations == null) {
            elevations = new ArrayList<ElevationBean>();
        }
        return this.elevations;
    }

}
