package mil.nga.npd;

import java.io.Serializable;

/**
 * Data structure used to hold the minimum and maximum elevation data points
 * over a specific geographic area.  This class takes advantage of the 
 * validation routines built into supporting classes.
 * 
 * @author L. Craig Carpenter 
 */
public class MinMaxElevation implements Serializable {

	/**
	 * Eclipse-generated serialVersionUID
	 */
	private static final long serialVersionUID = -7383654495831720013L;
	
	private final ElevationDataPoint maxElevation;
	private final ElevationDataPoint minElevation;
	
	/**
	 * Default constructor enforcing the builder creation pattern.
	 * @param builder The builder class.
	 */
	protected MinMaxElevation(MinMaxElevationBuilder builder) { 
		maxElevation = builder.maxElevation;
		minElevation = builder.minElevation;
	}
	
	/**
	 * Getter method for the maximum elevation point
	 * @return The maximum elevation point.
	 */
	public ElevationDataPoint getMaxElevation() {
		return maxElevation;
	}
	
	/**
	 * Getter method for the minimum elevation point
	 * @return The minimum elevation point.
	 */
	public ElevationDataPoint getMinElevation() {
		return minElevation;
	}
	
	/**
	 * Convert to printable String.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Maximum Elevation => [ ");
		sb.append(getMaxElevation().toString());
		sb.append("Minimum Elevation => [ ");
		sb.append(getMinElevation().toString());
		sb.append(" ].");
		return sb.toString();
	}
	
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>BoundingBox</code>.
     * 
     * @author L. Craig Carpenter
     */
	public static class MinMaxElevationBuilder {
		
		private ElevationDataPoint maxElevation;
		private ElevationDataPoint minElevation;
		
		/**
		 * Setter method for the maxElevationPoint.
		 * @param value The maximum elevation point.
		 * @return The builder object.
		 */
		public MinMaxElevationBuilder maxElevation(ElevationDataPoint value) {
			maxElevation = value;
			return this;
		}
		
		/**
		 * Setter method for the minElevationPoint.
		 * @param value The minimum elevation point.
		 * @return The builder object.
		 */
		public MinMaxElevationBuilder minElevation(ElevationDataPoint value) {
			minElevation = value;
			return this;
		}
		
		/**
		 * Build the target <code>MinMaxElevation</code> object.
		 * 
		 * @throws IllegalStateException Thrown if the constructed object 
		 * fails any validation checks.
		 */
		public MinMaxElevation build() {
			return new MinMaxElevation(this);
		}
	}
}
