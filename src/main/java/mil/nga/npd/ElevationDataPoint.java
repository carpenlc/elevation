package mil.nga.npd;

import java.io.Serializable;

import mil.nga.elevation.HeightUnitType;

public class ElevationDataPoint implements Serializable, Constants {

	/**
	 * Eclipse-generated serialVersionUID
	 */
	private static final long serialVersionUID = -2774969956804341499L;
	
	private final int                elevation;
	private final String             classificationMarking;
	private final GeodeticCoordinate coordinate;
	private final DEMFrameAccuracy   accuracy;
    private final HeightUnitType     units;

	/**
	 * Default constructor enforcing the builder creation pattern.
	 * @param builder Object containing default values for the private final
	 * internal parameters.
	 */
	protected ElevationDataPoint(ElevationDataPointBuilder builder) {
		classificationMarking = builder.classificationMarking;
		elevation             = builder.elevation;
		coordinate            = builder.coordinate;
		accuracy              = builder.accuracy;
		units                 = builder.units;
	}
	
	/**
	 * Getter method for the classification marking that was associated with
	 * the source DEM.
	 * @return The classification marking.
	 */
	public String getClassificationMarking() {
		return classificationMarking;
	}
	
	/**
	 * Getter method for the elevation value associated with a given lat/lon
	 * pair.
	 * @return The elevation value.
	 */
	public int getElevation() {
		return elevation;
	}
	
	/**
	 * Getter method for the latitude value.
	 * @return The latitude value.
	 */
	public double getLat() {
		return coordinate.getLat();
	}
	
	/** 
	 * Getter method for the longitude value.
	 * @return The longitude value.
	 */
	public double getLon() {
		return coordinate.getLon();
	}
	
	/**
	 * Getter method for the length units associated with the elevation  
	 * value associated with a given lat/lon pair.
	 * @return The elevation value.
	 */
	public HeightUnitType getUnits() {
		return units;
	}
	
	/**
	 * Convert to a printable String.
	 */
 	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ElevationDataPoint : Lat => [ ");
		sb.append(getLat());
		sb.append(" ], Lon => [ ");
		sb.append(getLon());
		sb.append(" ], elevation => [ ");
		sb.append(getElevation());
		sb.append(" ] ");
		sb.append(getUnits().name());
		sb.append(" ], classification marking => [ ");
		sb.append(getClassificationMarking());
		sb.append(" ] ");
		sb.append(accuracy.toString());
		return sb.toString();
	}
 	
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>ElevationDataPoint</code>.
     * 
     * @author L. Craig Carpenter
     */
	public static class ElevationDataPointBuilder {
		
		private int                elevation;
		private String             classificationMarking = "";
		private GeodeticCoordinate coordinate;
		private DEMFrameAccuracy   accuracy;
		private HeightUnitType     units = HeightUnitType.METERS;
		
		/**
		 * Setter method for the source classification marking.
		 * @param value The source classification marking.
		 * @return Reference to the builder object.
		 */
		public ElevationDataPointBuilder classificationMarking(String value) {
			classificationMarking = value;
			return this;
		}
		/**
		 * Setter method for the units associated with any length 
		 * data.
		 * @param value The unit information.
		 * @return The builder object.
		 */
		public ElevationDataPointBuilder units(
				HeightUnitType value) {
			if (value != null) {
				units = value;
			}
			return this;
		}
		
		/**
		 * Setter method for the data structure containing the lat/lon 
		 * geodetic point information.
		 * @param value The location data.
		 * @return The builder object.
		 */
		public ElevationDataPointBuilder withGeodeticCoordinate(
				GeodeticCoordinate value) {
			coordinate = value;
			return this;
		}
		
		/**
		 * Setter method for the data structure containing the accuracy 
		 * data associated with the overall DEM frame.
		 * @param value The accuracy data.
		 * @return The builder object.
		 */
		public ElevationDataPointBuilder withDEMFrameAccuracy(
				DEMFrameAccuracy value) {
			accuracy = value;
			return this;
		}
		
		/**
		 * Setter method for the elevation value associated with a given lat/lon
		 * pair.
		 * @param value The elevation value.
		 */
		public ElevationDataPointBuilder elevation(int value) {
			elevation = value;
			return this;
		}
		
		/**
		 * Method used to construct a <code>ElevationDataPoint</code>
		 * object,
		 * @return A constructed/validated object.
		 * @throws IllegalStateException Thrown if the object fails 
		 * any validation tests.
		 */
		public ElevationDataPoint build() {
			if (units == HeightUnitType.FEET) {
				elevation = Constants.convertToFeet(elevation);
			}
			if ((classificationMarking == null) || 
					classificationMarking.isEmpty()) {
				classificationMarking = DEFAULT_CLASSIFICATION_MARKING;
			}
			ElevationDataPoint point = new ElevationDataPoint(this);
			validate(point);
			return point;
		}
		
		/**
		 * The only thing we have to validate is the input elevation data.
		 * The rest of the objects were already validated by their respective 
		 * builder objects.
		 * 
		 * @throws IllegalStateException Thrown if the elevation value is out 
		 * of range.
		 */
		private void validate(ElevationDataPoint obj) throws IllegalStateException {
			if (units == HeightUnitType.FEET) {
				if (elevation > Constants.convertToFeet(MAX_ELEVATION)) { 
					throw new IllegalStateException("Invalid value for the "
							+ "elevation.  The elevation must be between [ 0.."
							+ Constants.convertToFeet(MAX_ELEVATION)
							+ " ].");
				}
				else {
					if (elevation > MAX_ELEVATION) { 
						throw new IllegalStateException("Invalid value for the "
								+ "elevation.  The elevation must be between [ 0.."
								+ MAX_ELEVATION
								+ " ].");
					}
				}
			}
		}
	}
}
