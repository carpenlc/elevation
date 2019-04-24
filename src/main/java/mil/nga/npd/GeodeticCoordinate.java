package mil.nga.npd;

import java.io.Serializable;

import mil.nga.elevation.GeodeticCoordinateBean;

/**
 * Simple data structure holding a single geodetic coordinate (lat/lon).  
 * This class also has simple methods for converting to/from the WSDL 
 * generated <code>GeodeticCoordinateBean</code>.
 * 
 * @author L. Craig Carpenter
 */
public class GeodeticCoordinate implements Serializable {

	/**
	 * Eclipse-generated serialVersionUID
	 */
	private static final long serialVersionUID = -7545583693002322759L;
	
	private final double lat;
	private final double lon;
	
	/**
	 * Default constructor enforcing the builder creation pattern.
	 * @param builder Object containing default values for lat/lon.
	 */
	protected GeodeticCoordinate(GeodeticCoordinateBuilder builder) {
		lat = builder.lat;
		lon = builder.lon;
	}
	
	/**
	 * Create a <code>GeodeticCoordinateBean</code> object from the 
	 * data contained by this object.
	 * @return A constructed <code>GeodeticCoordinateBean</code> object.
	 */
	public GeodeticCoordinateBean getGeodeticCoordinateBean() {
		GeodeticCoordinateBean bean = new GeodeticCoordinateBean();
		bean.setLat(String.valueOf(getLat()));
		bean.setLon(String.valueOf(getLon()));
		return bean;
	}
	
	/**
	 * Getter method for the latitude value.
	 * @return The latitude value of the geodetic coordinate.
	 */
	public double getLat() {
		return lat;
	}
	
	/**
	 * Getter method for the longitude value.
	 * @return The longitude value of the geodetic coordinate.
	 */
	public double getLon() {
		return lon;
	}
	
	/**
	 * Convert to a human readable String.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Latitude => [ ");
		sb.append(lat);
		sb.append(" ], Longitude => [ ");
		sb.append(lon);
		sb.append(" ]");
		return sb.toString();
	}
	
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>GeodeticCoordinate</code>.
     * 
     * @author L. Craig Carpenter
     */
	public static class GeodeticCoordinateBuilder {
		
		private double lat;
		private double lon;
		
		/**
		 * Setter method for the latitude value.
		 * @param lat Double value for the latitude value.
		 * @return The builder object.
		 */
		public GeodeticCoordinateBuilder lat(double lat) {
			this.lat = lat;
			return this;
		}
		
		/**
		 * Setter method for the latitude value.
		 * @param lat String representing the latitude value.
		 * @return The builder object.
		 * @throws IllegalStateException Thrown if the input latitude 
		 * String data is null or empty.
		 */
		public GeodeticCoordinateBuilder lat(String lat) 
				throws IllegalStateException {
			if ((lat != null) && (!lat.isEmpty())) { 
				this.lat = CoordsParse.getInstance().parseCoordString(lat, true);
			}
			else {
				throw new IllegalStateException(
						"Input latitude String value is null or empty.");				
			}
			return this;
		}
		
		/**
		 * Setter method for the latitude value.
		 * @param lat Double value for the latitude value.
		 * @return The builder object.
		 */
		public GeodeticCoordinateBuilder lon(double lon) {
			this.lon = lon;
			return this;
		}
		
		/**
		 * Setter method for the longitude value.
		 * @param lat String representing the longitude value.
		 * @return The builder object.
		 * @throws IllegalStateException Thrown if the input longitude 
		 * String data is null or empty.
		 */
		public GeodeticCoordinateBuilder lon(String lon) 
				throws IllegalStateException {
			if ((lon != null) && (!lon.isEmpty())) { 
				this.lon = CoordsParse.getInstance().parseCoordString(lon, false);
			}
			else {
				throw new IllegalStateException(
						"Input longitude String value is null or empty.");
			}
			return this;
		}
		
		/**
		 * Allows you to construct a <code>GeodeticCoordinate</code> from the 
		 * contents of a <code>GeodeticCoordinateBean</code> object.
		 * @param bean The input <code>GeodeticCoordinateBean</code> object.
		 * @return the builder object.
		 * @throws IllegalStateException Thrown if the input object is null.
		 */
		public GeodeticCoordinateBuilder withGeodeticCoordinateBean(
				GeodeticCoordinateBean bean) throws IllegalStateException {
			if (bean != null) {
				this.lon(bean.getLon());
				this.lat(bean.getLat());
			}
			else {
				throw new IllegalStateException(
						"The input GeodeticCoordinateBean object is null.");
			}
			return this;
		}
		
		/**
		 * Build the target <code>GeodeticCoordinate</code> object.
		 * 
		 * @return A constructed <code>GeodeticCoordinate</code>.
		 * @throws IllegalStateException Thrown if the constructed object 
		 * fails any validation checks.
		 */
		public GeodeticCoordinate build() throws IllegalStateException {
			GeodeticCoordinate coord = new GeodeticCoordinate(this);
			validate(coord);
			return coord;
		}
		
		/**
		 * Validate the coordinate.
		 * @param coord Candidate <code>GeodeticCoordinate</code> object.
		 */
		private void validate(GeodeticCoordinate coord) {
			// First check to see if the coordinates failed conversion
	        if (coord.getLat() < -1000) {
	            throw new IllegalStateException(
	            		"Invalid latitude value => [ " 
	            	    + coord.getLat()
	            	    + " ].  Conversion error message => [ " 
	            	    + ErrorMessageType.getErrorMessage(coord.getLat())
	            	    + " ].");
	        }
	        if (coord.getLon() < -1000) {
	            throw new IllegalStateException(
	            		"Invalid longitude value => [ " 
	            	    + coord.getLon()
	            	    + " ].  Conversion error message => [ " 
	            	    + ErrorMessageType.getErrorMessage(coord.getLon())
	            	    + " ].");
	        }
	        // Next, make sure the coordinates are within valid ranges.
	        if (coord.getLon() > 179) {
	            throw new IllegalStateException(
	            		"Invalid longitude value => [ " 
	            	    + coord.getLon()
	            	    + " ].  Longitude must be less than 180."); 
	        }
	        if (coord.getLon() < -180) {
	            throw new IllegalStateException(
	            		"Invalid longitude value => [ " 
	            	    + coord.getLon()
	            	    + " ].  Longitude must be greater than -180."); 
	        }
	        if (coord.getLat() > 89) {
	            throw new IllegalStateException(
	            		"Invalid latitude value => [ " 
	            	    + coord.getLon()
	            	    + " ].  Latitude must be less than 90."); 
	        }
	        if (coord.getLat() < -90) {
	            throw new IllegalStateException(
	            		"Invalid latitude value => [ " 
	            	    + coord.getLon()
	            	    + " ].  Latitude must be greater than -90."); 
	        }
		}
	}
}
