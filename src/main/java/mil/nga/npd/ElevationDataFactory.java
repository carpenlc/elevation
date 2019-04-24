package mil.nga.npd;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbn.openmap.dataAccess.dted.DTEDFrame;

import mil.nga.elevation.HeightUnitType;
import mil.nga.elevation.TerrainDataType;

/**
 * This class is responsible for retrieving the requested elevation data 
 * from a target DEM file.  This relies on 3rd-party APIs to parse and load 
 * the target DEM files.
 * 
 * @author L. Craig Carpenter
 */
public class ElevationDataFactory implements Constants {
	
    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ElevationDataFactory.class);
    
    private final String          filePath;
	private final TerrainDataType sourceType;
	private final HeightUnitType  units;
	
	/**
	 * Default constructor enforcing the builder creation pattern.
	 * @param builder Object containing default values for the private final
	 * internal parameters.
	 */
    protected ElevationDataFactory(ElevationDataFactoryBuilder builder) {
    	filePath   = builder.filePath;
    	sourceType = builder.sourceType;
    	units      = builder.units;
    }
    
	/**
	 * This method obtains the elevation and associated accuracy data 
	 * associated with the input geodetic coordinate.  The method uses 
	 * the third party com.bbn.openmap API for reading the target DEM.
	 * 
	 * @param coordinate The target coordinate that we wish to find the 
	 * elevation value for.
	 * @return A <code>ElevationDataPoint<code> data structure containing 
	 * the elevation information and associated accuracy data.
	 */
	public ElevationDataPoint getElevationAt(GeodeticCoordinate coordinate) {
		
		long      startTime = System.currentTimeMillis();
		DTEDFrame frame     = null;
		ElevationDataPoint result = null;
		
		try {
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Loading DEM frame file [ "
						+ getFilePath()
						+ " ].");
			}
			
			frame = new DTEDFrame(getFilePath());
			result = new ElevationDataPoint.ElevationDataPointBuilder()
					.units(getUnits())
					.withGeodeticCoordinate(coordinate)
					.withDEMFrameAccuracy(
							new DEMFrameAccuracy.DEMFrameAccuracyBuilder()
								.absHorzAccuracy(frame.acc.abs_horz_acc)
								.absVertAccuracy(frame.acc.abs_vert_acc)
								.relHorzAccuracy(frame.acc.rel_horz_acc)
								.relVertAccuracy(frame.acc.rel_vert_acc)
								.units(getUnits())
								.build())
					.elevation(frame.interpElevationAt(
							(float)coordinate.getLat(), 
							(float)coordinate.getLon()))
					.build();

		}
		finally {
			if (frame != null) {
				frame.close(true);
			}
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Elevation data for type [ "
					+ getSourceType().name()
					+ " ] retrieved in [ "
					+ (System.currentTimeMillis() - startTime)
					+ " ] ms.");
		}
		return result;
	}
	
	/**
	 * Getter method for the path to the target DEM file.
	 * @return The path to the target DEM file.
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/** 
	 * Getter method for the output length (height) units.
	 * @return The client-requested output units.
	 */
	public HeightUnitType getUnits() {
		return units;
	}
	
	/**
     * Getter method for the source DEM type data.
     * return The source DEM type.
	 */
	public TerrainDataType getSourceType() {
		return sourceType;
	}
	
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>ElevationDataFactory</code>.
     * 
     * @author L. Craig Carpenter
     */
	public static class ElevationDataFactoryBuilder {
		
		private String          filePath;
		private TerrainDataType sourceType;
		private HeightUnitType  units = HeightUnitType.METERS;
		
		/**
		 * Setter method for the path to the target DEM file.
		 * 
		 * @param value The path to the target DEM file.
		 * @return Reference to the builder object.
		 * @throws IllegalStateException Thrown if the input data is 
		 * invalid (null or empty).
		 */
		public ElevationDataFactoryBuilder filePath(String value) {
			if ((value == null) || (value.isEmpty())) {
				throw new IllegalStateException(
						"Invalid value for target file path.  Input value "
						+ "is null or empty.");
			}
			filePath = value;
			return this;
		}
		
		/**
		 * Setter method for the source DEM type data.
		 * @param value The source DEM type.
		 * @return Reference to the builder object.
		 */
		public ElevationDataFactoryBuilder sourceType(TerrainDataType value) {
			sourceType = value;
			return this;
		}
		
		/**
		 * Setter method for the units associated with the height and accuracy 
		 * data.
		 * @param value The units for the height and accuracy data.
		 * @return Reference to the builder object.
		 */
		public ElevationDataFactoryBuilder units(HeightUnitType value) {
			units = value;
			return this;
		}
		
		/**
		 * Build the target <code>ElevationDataFactory</code> object.
		 * 
		 * @return A constructed <code>ElevationDataFactory</code>.
		 * @throws IllegalStateException Thrown if the constructed object 
		 * fails any validation checks.
		 */
		public ElevationDataFactory build() throws IllegalStateException {
			ElevationDataFactory factory = new ElevationDataFactory(this);
			validate(factory);
			return factory;
		}
		
		/**
		 * Validate the required input date.
		 * @param coord Candidate <code>ElevationDataFactory</code> object.
		 */
		private void validate(ElevationDataFactory obj) {
			Path p = Paths.get(obj.getFilePath());
			if (!Files.exists(p)) {
				throw new IllegalStateException("Target DEM file => [ "
						+ obj.getFilePath()
						+ " ] does not exist.");
			}
			
			
		}
	}
	

}
