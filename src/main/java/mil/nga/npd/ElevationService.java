package mil.nga.npd;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mil.nga.elevation.Elevation;
import mil.nga.elevation.GeodeticCoordinateBean;
import mil.nga.elevation.GetElevationAt;
import mil.nga.elevation.GetElevationAtResponse;
import mil.nga.elevation.GetMinMaxElevations;
import mil.nga.elevation.GetMinMaxElevationsResponse;
import mil.nga.elevation.GetMinMaxElevationsWKT;
import mil.nga.elevation.GetMinMaxElevationsWKTResponse;
import mil.nga.elevation.HeightUnitType;
import mil.nga.elevation.TerrainDataType;
import mil.nga.npd.exceptions.InvalidParameterException;
import mil.nga.npd.model.TerrainDataFile;

@WebService(serviceName = "elevation_service", 
            portName = "ElevationServicePort", 
            endpointInterface = "mil.nga.elevation.Elevation", 
            targetNamespace = "mil:nga:elevation", 
            wsdlLocation = "WEB-INF/wsdl/elevation_service.wsdl")
public class ElevationService implements Elevation, Constants {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ElevationService.class);
    

	
	@Override
	public GetElevationAtResponse getElevationAt(GetElevationAt parameters) {
		
		// Check any input parameters that could result in an NPE before
		// proceeding with further processing.
		if (parameters == null) {
			throw new UnsupportedOperationException(
					"No input parameters supplied.");
		}
		if ((parameters.getPts() != null) && 
	        (parameters.getPts().size() > 0)) {
			throw new UnsupportedOperationException(
					"Input list of geodetic points is null or empty.  "
					+ "At least one point must be specified.");
		}
		if (parameters.getPts().size() > MAX_ALLOWABLE_POINTS) {
			throw new UnsupportedOperationException(
					"[ "
					+ parameters.getPts().size()
					+ " ] points were requested.  The application currently "
					+ "supports a maximum of [ "
					+ MAX_ALLOWABLE_POINTS
					+ " ] points in a single request.");
		}
		
		// Initialize the optional params
		HeightUnitType  units  = Constants.getValueOrDefault(
				parameters.getHeightType(),
				HeightUnitType.METERS);
		TerrainDataType source = Constants.getValueOrDefault(
				parameters.getTerrainDataType(),
				TerrainDataType.DTED_0);
		
		// Initialize the list of points that will be used to construct the
		// return object.
		List<ElevationDataPoint> elevationPoints = 
				new ArrayList<ElevationDataPoint>();
		
		if (LOGGER.isDebugEnabled()) { 
			LOGGER.debug("Request for point elevation data received.  "
					+ "Calculating elevation for [ "
					+ parameters.getPts().size()
					+ " ] points using source type [ "
					+ source.name()
					+ " ] and returning the data in [ "
					+ units.name()
					+ " ] units.");
		}
		
		// Loop through the user-supplied list of coordinates.
		for(GeodeticCoordinateBean coordBean: parameters.getPts()) {
			
			// Since the WSDL specifies the coordinates are in String format, 
			// convert them to primitives for further processing.
			GeodeticCoordinate coord = 
					new GeodeticCoordinate.GeodeticCoordinateBuilder()
						.withGeodeticCoordinateBean(coordBean)
						.build();
			
			// Retrieve a list of source DEM files that can be used to 
			// calculate the requested data.
			List<TerrainDataFile> demFiles = new TerrainDataFileService().getTerrainDataFiles(
							coord, source);
					
			// The DEM files are returned in worst-to-best quality order.  
			// This short section just scans to the end of the list to use the 
			// best quality data matching the input parameters.
			if ((demFiles != null) && (demFiles.size() > 0)) { 
				for (TerrainDataFile demFile : demFiles) {
					try {
						
						elevationPoints.add(
								new ElevationDataFactory.ElevationDataFactoryBuilder()
								.filePath(demFile.getUnixPath())
								.sourceType(source)
								.units(units)
								.classificationMarking(demFile.getMarking())
								.build()
							.getElevationAt(coord));
						
					}
					catch (InvalidParameterException ipe) {
						LOGGER.error("InvalidParameterException "
								+ "encountered while calculating "
								+ "elevation data for point => [ "
								+ coord.toString()
								+ " ].   Exception message => [ "
								+ ipe.getMessage()
								+ " ].");
					}
				}
			}
		}


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
