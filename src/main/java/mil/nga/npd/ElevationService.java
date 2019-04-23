package mil.nga.npd;

import javax.jws.WebService;

import mil.nga.elevation.Elevation;
import mil.nga.elevation.GetElevationAt;
import mil.nga.elevation.GetElevationAtResponse;
import mil.nga.elevation.GetMinMaxElevations;
import mil.nga.elevation.GetMinMaxElevationsResponse;
import mil.nga.elevation.GetMinMaxElevationsWKT;
import mil.nga.elevation.GetMinMaxElevationsWKTResponse;

@WebService(serviceName = "elevation_service", 
            portName = "ElevationServicePort", 
            endpointInterface = "mil.nga.elevation.Elevation", 
            targetNamespace = "mil:nga:elevation", 
            wsdlLocation = "WEB-INF/wsdl/elevation_service.wsdl")
public class ElevationService implements Elevation {

	@Override
	public GetElevationAtResponse getElevationAt(GetElevationAt parameters) {
		parameters.getPts();
		parameters.getTerrainDataType();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetMinMaxElevationsResponse getMinMaxElevations(GetMinMaxElevations parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetMinMaxElevationsWKTResponse getMinMaxElevationsWKT(GetMinMaxElevationsWKT parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
