
package mil.nga.elevation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ElevationBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ElevationBean"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="elevation" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="coordinates" type="{mil:nga:elevation}GeodeticCoordinateBean"/&gt;
 *         &lt;element name="accuracy" type="{mil:nga:elevation}CoordinateAccuracyBean"/&gt;
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="marking" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ElevationBean", propOrder = {
    "elevation",
    "coordinates",
    "accuracy",
    "source",
    "marking"
})
public class ElevationBean {

    @XmlElement(required = true)
    protected String elevation;
    @XmlElement(required = true)
    protected GeodeticCoordinateBean coordinates;
    @XmlElement(required = true)
    protected CoordinateAccuracyBean accuracy;
    @XmlElement(required = true)
    protected String source;
    protected String marking;

    /**
     * Gets the value of the elevation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElevation() {
        return elevation;
    }

    /**
     * Sets the value of the elevation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElevation(String value) {
        this.elevation = value;
    }

    /**
     * Gets the value of the coordinates property.
     * 
     * @return
     *     possible object is
     *     {@link GeodeticCoordinateBean }
     *     
     */
    public GeodeticCoordinateBean getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the value of the coordinates property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeodeticCoordinateBean }
     *     
     */
    public void setCoordinates(GeodeticCoordinateBean value) {
        this.coordinates = value;
    }

    /**
     * Gets the value of the accuracy property.
     * 
     * @return
     *     possible object is
     *     {@link CoordinateAccuracyBean }
     *     
     */
    public CoordinateAccuracyBean getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the value of the accuracy property.
     * 
     * @param value
     *     allowed object is
     *     {@link CoordinateAccuracyBean }
     *     
     */
    public void setAccuracy(CoordinateAccuracyBean value) {
        this.accuracy = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the marking property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarking() {
        return marking;
    }

    /**
     * Sets the value of the marking property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarking(String value) {
        this.marking = value;
    }

}
