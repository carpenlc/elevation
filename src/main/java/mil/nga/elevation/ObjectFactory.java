
package mil.nga.elevation;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mil.nga.elevation package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _NgaResource_QNAME = new QName("mil:nga:elevation", "nga_resource");
    private final static QName _DescriptionDocument_QNAME = new QName("mil:nga:elevation", "description_document");
    private final static QName _GetElevationAt_QNAME = new QName("mil:nga:elevation", "GetElevationAt");
    private final static QName _GetElevationAtResponse_QNAME = new QName("mil:nga:elevation", "GetElevationAtResponse");
    private final static QName _GetMinMaxElevations_QNAME = new QName("mil:nga:elevation", "GetMinMaxElevations");
    private final static QName _GetMinMaxElevationsResponse_QNAME = new QName("mil:nga:elevation", "GetMinMaxElevationsResponse");
    private final static QName _GetMinMaxElevationsWKT_QNAME = new QName("mil:nga:elevation", "GetMinMaxElevationsWKT");
    private final static QName _GetMinMaxElevationsWKTResponse_QNAME = new QName("mil:nga:elevation", "GetMinMaxElevationsWKTResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mil.nga.elevation
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetElevationAt }
     * 
     */
    public GetElevationAt createGetElevationAt() {
        return new GetElevationAt();
    }

    /**
     * Create an instance of {@link GetElevationAtResponse }
     * 
     */
    public GetElevationAtResponse createGetElevationAtResponse() {
        return new GetElevationAtResponse();
    }

    /**
     * Create an instance of {@link GetMinMaxElevations }
     * 
     */
    public GetMinMaxElevations createGetMinMaxElevations() {
        return new GetMinMaxElevations();
    }

    /**
     * Create an instance of {@link GetMinMaxElevationsResponse }
     * 
     */
    public GetMinMaxElevationsResponse createGetMinMaxElevationsResponse() {
        return new GetMinMaxElevationsResponse();
    }

    /**
     * Create an instance of {@link GetMinMaxElevationsWKT }
     * 
     */
    public GetMinMaxElevationsWKT createGetMinMaxElevationsWKT() {
        return new GetMinMaxElevationsWKT();
    }

    /**
     * Create an instance of {@link GetMinMaxElevationsWKTResponse }
     * 
     */
    public GetMinMaxElevationsWKTResponse createGetMinMaxElevationsWKTResponse() {
        return new GetMinMaxElevationsWKTResponse();
    }

    /**
     * Create an instance of {@link GeodeticCoordinateBean }
     * 
     */
    public GeodeticCoordinateBean createGeodeticCoordinateBean() {
        return new GeodeticCoordinateBean();
    }

    /**
     * Create an instance of {@link CoordinateAccuracyBean }
     * 
     */
    public CoordinateAccuracyBean createCoordinateAccuracyBean() {
        return new CoordinateAccuracyBean();
    }

    /**
     * Create an instance of {@link BboxBean }
     * 
     */
    public BboxBean createBboxBean() {
        return new BboxBean();
    }

    /**
     * Create an instance of {@link ElevationBean }
     * 
     */
    public ElevationBean createElevationBean() {
        return new ElevationBean();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:elevation", name = "nga_resource")
    public JAXBElement<String> createNgaResource(String value) {
        return new JAXBElement<String>(_NgaResource_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:elevation", name = "description_document")
    public JAXBElement<String> createDescriptionDocument(String value) {
        return new JAXBElement<String>(_DescriptionDocument_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetElevationAt }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetElevationAt }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:elevation", name = "GetElevationAt")
    public JAXBElement<GetElevationAt> createGetElevationAt(GetElevationAt value) {
        return new JAXBElement<GetElevationAt>(_GetElevationAt_QNAME, GetElevationAt.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetElevationAtResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetElevationAtResponse }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:elevation", name = "GetElevationAtResponse")
    public JAXBElement<GetElevationAtResponse> createGetElevationAtResponse(GetElevationAtResponse value) {
        return new JAXBElement<GetElevationAtResponse>(_GetElevationAtResponse_QNAME, GetElevationAtResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMinMaxElevations }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetMinMaxElevations }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:elevation", name = "GetMinMaxElevations")
    public JAXBElement<GetMinMaxElevations> createGetMinMaxElevations(GetMinMaxElevations value) {
        return new JAXBElement<GetMinMaxElevations>(_GetMinMaxElevations_QNAME, GetMinMaxElevations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMinMaxElevationsResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetMinMaxElevationsResponse }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:elevation", name = "GetMinMaxElevationsResponse")
    public JAXBElement<GetMinMaxElevationsResponse> createGetMinMaxElevationsResponse(GetMinMaxElevationsResponse value) {
        return new JAXBElement<GetMinMaxElevationsResponse>(_GetMinMaxElevationsResponse_QNAME, GetMinMaxElevationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMinMaxElevationsWKT }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetMinMaxElevationsWKT }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:elevation", name = "GetMinMaxElevationsWKT")
    public JAXBElement<GetMinMaxElevationsWKT> createGetMinMaxElevationsWKT(GetMinMaxElevationsWKT value) {
        return new JAXBElement<GetMinMaxElevationsWKT>(_GetMinMaxElevationsWKT_QNAME, GetMinMaxElevationsWKT.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMinMaxElevationsWKTResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetMinMaxElevationsWKTResponse }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:elevation", name = "GetMinMaxElevationsWKTResponse")
    public JAXBElement<GetMinMaxElevationsWKTResponse> createGetMinMaxElevationsWKTResponse(GetMinMaxElevationsWKTResponse value) {
        return new JAXBElement<GetMinMaxElevationsWKTResponse>(_GetMinMaxElevationsWKTResponse_QNAME, GetMinMaxElevationsWKTResponse.class, null, value);
    }

}
