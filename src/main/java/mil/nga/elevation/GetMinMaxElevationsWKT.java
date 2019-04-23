
package mil.nga.elevation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import mil.nga.security.SecurityElement;


/**
 * <p>Java class for GetMinMaxElevationsWKT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetMinMaxElevationsWKT"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="wkt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "GetMinMaxElevationsWKT", propOrder = {
    "wkt",
    "terrainDataType",
    "heightType",
    "security"
})
public class GetMinMaxElevationsWKT {

    protected String wkt;
    @XmlSchemaType(name = "string")
    protected TerrainDataType terrainDataType;
    @XmlSchemaType(name = "string")
    protected HeightUnitType heightType;
    protected SecurityElement security;

    /**
     * Gets the value of the wkt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWkt() {
        return wkt;
    }

    /**
     * Sets the value of the wkt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWkt(String value) {
        this.wkt = value;
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
