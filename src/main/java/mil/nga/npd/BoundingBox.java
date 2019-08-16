package mil.nga.npd;

import java.io.Serializable;

/**
 * Data structure containing the lower left and upper right corners of a 
 * rectangular bounding box.
 * 
 * Internally, the lower left and upper right coordinates are stored as 
 * <code>GeodeticCoordinate</code> objects to take advantage of the 
 * existing validation code.
 * 
 * @author L. Craig Carpenter
 */
public class BoundingBox implements Serializable {

	/**
	 * Eclipse-generated serialVersionUID
	 */
	private static final long serialVersionUID = -3906644493169409863L;
	
	private final GeodeticCoordinate upperRight;
	private final GeodeticCoordinate lowerLeft;
	
	/**
	 * Default constructor enforcing the builder creation pattern.
	 * @param upperRight The upper right coordinate.
	 * @param lowerLeft The lower left coordinate.
	 */
	protected BoundingBox (
			GeodeticCoordinate upperRight,
			GeodeticCoordinate lowerLeft) {
		this.upperRight = upperRight;
		this.lowerLeft  = lowerLeft;
	}
	
	/**
	 * Getter method for the upper right latitude value.
	 * @return The upper right latitude value of the geodetic coordinate.
	 */
	public double getUpperRightLat() {
		return upperRight.getLat();
	}
	
	/**
	 * Getter method for the lower right latitude value.
	 * @return The lower right latitude value of the geodetic coordinate.
	 */
	public double getLowerRightLat() {
		return lowerLeft.getLat();
	}
	
	/**
	 * Getter method for the upper right longitude value.
	 * @return The upper right longitude value of the geodetic coordinate.
	 */
	public double getUpperRightLon() {
		return upperRight.getLon();
	}
	
	/**
	 * Getter method for the lower right longitude value.
	 * @return The lower right longitude value of the geodetic coordinate.
	 */
	public double getLowerRightLon() {
		return upperRight.getLon();
	}
	
	/**
	 * Getter method for the lower left latitude value.
	 * @return The lower left latitude value of the geodetic coordinate.
	 */
	public double getLowerLeftLat() {
		return lowerLeft.getLat();
	}
	
	/**
	 * Getter method for the upper left latitude value.
	 * @return The upper left latitude value of the geodetic coordinate.
	 */
	public double getUpperLeftLat() {
		return upperRight.getLat();
	}
	
	/**
	 * Getter method for the lower left longitude value.
	 * @return The lower left longitude value of the geodetic coordinate.
	 */
	public double getLowerLeftLon() {
		return lowerLeft.getLon();
	}
	
	/**
	 * Getter method for the upper left latitude value.
	 * @return The upper left latitude value of the geodetic coordinate.
	 */
	public double getUpperLeftLon() {
		return lowerLeft.getLon();
	}
	
	/**
	 * Convert the bounding box to a human-readable String.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BoundingBox:  UR-Lat [ ");
		sb.append(getUpperRightLat());
		sb.append(" ], UR-Lon [ ");
		sb.append(getUpperRightLon());
		sb.append(" ], LL-Lat [ ");
		sb.append(getLowerLeftLat());
		sb.append(" ], LL-Lon [ ");
		sb.append(getLowerLeftLon());
		sb.append(" ].");
		return sb.toString();
	}
	
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>BoundingBox</code>.
     * 
     * @author L. Craig Carpenter
     */
	public static class BoundingBoxBuilder {
		
		private double upperRightLat;
		private double upperRightLon;
		private double lowerLeftLat;
		private double lowerLeftLon;
		
		/**
		 * Setter method for the lower left latitude value.
		 * @param value The lower left latitude value of the geodetic 
		 * coordinate.
		 * @return The builder object. 
		 */
		public BoundingBoxBuilder lowerLeftLat(double value) {
			lowerLeftLat = value;
			return this;
		}
		
		/**
		 * Setter method for the lower left longitude value.
		 * @param value The lower left longitude value of the geodetic 
		 * coordinate.
		 * @return The builder object. 
		 */
		public BoundingBoxBuilder lowerLeftLon(double value) {
			lowerLeftLon = value;
			return this;
		}
		
		/**
		 * Setter method for the upper right latitude value.
		 * @param value The upper right latitude value of the geodetic 
		 * coordinate.
		 * @return The builder object. 
		 */
		public BoundingBoxBuilder upperRightLat(double value) {
			upperRightLat = value;
			return this;
		}
		
		/**
		 * Setter method for the upper right longitude value.
		 * @param value The upper right longitude value of the geodetic 
		 * coordinate.
		 * @return The builder object. 
		 */
		public BoundingBoxBuilder upperRightLon(double value) {
			upperRightLon = value;
			return this;
		}
		
		/**
		 * Build the target <code>BoundingBox</code> object.
		 * 
		 * @return A constructed <code>BoundingBox</code> object.
		 * @throws IllegalStateException Thrown if the constructed object 
		 * fails any validation checks.
		 */
		public BoundingBox build() throws IllegalStateException {
			GeodeticCoordinate upperRight = 
					new GeodeticCoordinate.GeodeticCoordinateBuilder()
						.lat(upperRightLat)
						.lon(upperRightLon)
						.build();
			GeodeticCoordinate lowerLeft = 
					new GeodeticCoordinate.GeodeticCoordinateBuilder()
						.lat(lowerLeftLat)
						.lon(lowerLeftLon)
						.build();
			BoundingBox obj = new BoundingBox(upperRight, lowerLeft);
			validate(obj);
			return obj;
		}
		
		/**
		 * Make sure the lower-left is really lower than the upper 
		 * right.
		 * @param obj Object to test.
		 */
		public void validate(BoundingBox obj) {
			
		}
	}
}
