
package mil.nga.elevation;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TerrainDataType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TerrainDataType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DTED2"/&gt;
 *     &lt;enumeration value="DTED1"/&gt;
 *     &lt;enumeration value="DTED0"/&gt;
 *     &lt;enumeration value="SRTM2"/&gt;
 *     &lt;enumeration value="SRTM1"/&gt;
 *     &lt;enumeration value="SRTM2F"/&gt;
 *     &lt;enumeration value="SRTM1F"/&gt;
 *     &lt;enumeration value="BEST"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TerrainDataType")
@XmlEnum
public enum TerrainDataType {

    @XmlEnumValue("DTED2")
    DTED_2("DTED2"),
    @XmlEnumValue("DTED1")
    DTED_1("DTED1"),
    @XmlEnumValue("DTED0")
    DTED_0("DTED0"),
    @XmlEnumValue("SRTM2")
    SRTM_2("SRTM2"),
    @XmlEnumValue("SRTM1")
    SRTM_1("SRTM1"),
    @XmlEnumValue("SRTM2F")
    SRTM_2_F("SRTM2F"),
    @XmlEnumValue("SRTM1F")
    SRTM_1_F("SRTM1F"),
    BEST("BEST");
    private final String value;

    TerrainDataType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TerrainDataType fromValue(String v) {
        for (TerrainDataType c: TerrainDataType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
