
package mil.nga.elevation;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HeightUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HeightUnitType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="METERS"/&gt;
 *     &lt;enumeration value="FEET"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "HeightUnitType")
@XmlEnum
public enum HeightUnitType {

    METERS,
    FEET;

    public String value() {
        return name();
    }

    public static HeightUnitType fromValue(String v) {
        return valueOf(v);
    }

}
