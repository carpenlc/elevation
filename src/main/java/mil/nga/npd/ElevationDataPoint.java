package mil.nga.npd;

import java.io.Serializable;

import mil.nga.elevation.HeightUnitType;

public class ElevationDataPoint implements Serializable {

	/**
	 * Eclipse-generated serialVersionUID
	 */
	private static final long serialVersionUID = -2774969956804341499L;
	
	private final int                elevation;
	private final GeodeticCoordinate coordinate;
	private final DEMFrameAccuracy   accuracy;
    private final HeightUnitType     units;

	/**
	 * Default constructor enforcing the builder creation pattern.
	 * @param builder Object containing default values for the private final
	 * internal parameters.
	 */
	protected ElevationDataPoint(ElevationDataPointBuilder builder) {
		elevation  = builder.elevation;
		coordinate = builder.coordinate;
		accuracy   = builder.accuracy;
		units      = builder.units;
	}
	
	/**
	 * Getter method for the elevation value associated with a given lat/lon
	 * pair.
	 * @return The elevation value.
	 */
	public int getElevation() {
		return elevation;
	}
	
	public double getLat() {
		return coordinate.getLat();
	}
	
	public double getLon() {
		return coordinate.getLon();
	}
	
	/**
	 * Getter method for the length units associated with the elevation  value associated with a given lat/lon
	 * pair.
	 * @return The elevation value.
	 */
	public HeightUnitType getUnits() {
		return units;
	}
	
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
		sb.append(".  ");
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
		private GeodeticCoordinate coordinate;
		private DEMFrameAccuracy   accuracy;
		private HeightUnitType     units = HeightUnitType.METERS;
		
		public ElevationDataPointBuilder units(
				HeightUnitType value) {
			if (value != null) {
				units = value;
			}
			return this;
		}
		
		public ElevationDataPointBuilder withGeodeticCoordinate(
				GeodeticCoordinate value) {
			coordinate = value;
			return this;
		}
		
		public ElevationDataPointBuilder withDEMFrameAccuracy(
				DEMFrameAccuracy value) {
			accuracy = value;
			return this;
		}
		
		public ElevationDataPointBuilder elevation(int value) {
			elevation = value;
			return this;
		}
		
		public ElevationDataPoint build() {
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
			
		}
	}
}