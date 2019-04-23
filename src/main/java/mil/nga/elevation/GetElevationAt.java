
package mil.nga.elevation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import mil.nga.security.SecurityElement;


/**
 * <p>Java class for GetElevationAt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetElevationAt"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pts" type="{mil:nga:elevation}GeodeticCoordinateBean" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="terrainDataType" type="{mil:nga:elevation}TerrainDataType" minOccurs="0"/&gt;
 *         &lt;element name="heightType" type="{mil:nga:elevation}HeightUnitType" minOccurs="0"/&gt;
 *         &lt;element name="security" type="{mil:nga:security}SecurityElement" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetElevationAt", propOrder = {
    "pts",
    "terrainDataType",
    "heightType",
    "security"
})
public class GetElevationAt {

    protected List<GeodeticCoordinateBean> pts;
    @XmlSchemaType(name = "string")
    protected TerrainDataType terrainDataType;
    @XmlSchemaType(name = "string")
    protected HeightUnitType heightType;
    protected SecurityElement security;

    /**
     * Gets the value of the pts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GeodeticCoordinateBean }
     * 
     * 
     */
    public List<GeodeticCoordinateBean> getPts() {
        if (pts == null) {
            pts = new ArrayList<GeodeticCoordinateBean>();
        }
        return this.pts;
    }

    /**
     * Gets the value of the terrainDataType property.
     * 
     * @return
     *     possible object is
     *     {@link TerrainDataType }
     *     
     */
    public TerrainDataType getTerrainDataType() {
        return terrainDataType;
    }

    /**
     * Sets the value of the terrainDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TerrainDataType }
     *     
     */
    public void setTerrainDataType(TerrainDataType value) {
        this.terrainDataType = value;
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

}
