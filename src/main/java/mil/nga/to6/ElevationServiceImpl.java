/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mil.nga.to6;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.jws.WebService;
import mil.nga.elevation.Elevation;
import mil.nga.elevation.GetElevationAtResponse;
import mil.nga.elevation.GetMinMaxElevationsResponse;
import mil.nga.elevation.GetMinMaxElevationsWKTResponse;
import org.apache.log4j.Category;

/**
 *
 * @author jenningd
 */
@WebService(serviceName = "elevation_service", portName = "ElevationServicePort", endpointInterface = "mil.nga.elevation.Elevation", targetNamespace = "mil:nga:elevation", wsdlLocation = "WEB-INF/wsdl/ElevationServiceImpl/elevation_service.wsdl")
public class ElevationServiceImpl implements Elevation {


    static final Category log = Category.getInstance(ElevationServiceImpl.class);

    private String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public mil.nga.elevation.GetElevationAtResponse getElevationAt(mil.nga.elevation.GetElevationAt parameters) {
        GetElevationAtResponse result = new GetElevationAtResponse();

        try {

            log.debug("getElevationAt");

            ElevationQuery t = new ElevationQuery();

            result = t.getElevationAt(parameters);

            log.debug("End getElevationAt");
        } catch (UnsupportedOperationException e ) {
            // Pass the captured error message back to caller
            throw new UnsupportedOperationException(e.getMessage());

        } catch (Exception e) {
            // Log the unexpected Error
            log.error(getStackTrace(e));
            // Pass unexpected error message back to the calle
        }

        return result;
    }

    public mil.nga.elevation.GetMinMaxElevationsResponse getMinMaxElevations(mil.nga.elevation.GetMinMaxElevations parameters) {
        GetMinMaxElevationsResponse result = new GetMinMaxElevationsResponse();

        try {

            log.debug("getMinMaxElevations");

            ElevationQuery t = new ElevationQuery();

            result = t.getMinMaxElevations(parameters);

            log.debug("End getMinMaxElevations");
        } catch (UnsupportedOperationException e ) {
            // Pass the captured error message back to caller
            throw new UnsupportedOperationException(e.getMessage());

        } catch (Exception e) {
            // Log the unexpected Error
            log.error(getStackTrace(e));
            // Pass unexpected error message back to the calle
        }

        return result;
    }

    public mil.nga.elevation.GetMinMaxElevationsWKTResponse getMinMaxElevationsWKT(mil.nga.elevation.GetMinMaxElevationsWKT parameters) {
        GetMinMaxElevationsWKTResponse result = new GetMinMaxElevationsWKTResponse();

        try {

            log.debug("getMinMaxElevationsWKT");

            ElevationQuery t = new ElevationQuery();

            result = t.getMinMaxElevationsWKT(parameters);

            log.debug("End getMinMaxElevationsWKT");
        } catch (UnsupportedOperationException e ) {
            // Pass the captured error message back to caller
            throw new UnsupportedOperationException(e.getMessage());

        } catch (Exception e) {
            // Log the unexpected Error
            log.error(getStackTrace(e));
            // Pass unexpected error message back to the calle
        }

        return result;
    }

}

