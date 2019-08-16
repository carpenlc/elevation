package mil.nga.npd;

import java.io.Serializable;

import mil.nga.elevation.HeightUnitType;

/**
 * Data structure used to hold the accuracy information associated with 
 * a DTED frame.
 * 
 * The accuracy data in the target DTED frame consists of 4 4-byte fields 
 * that can contain 0-9999 (in meters) or "NA".  If the accuracy field 
 * has "NA" or "N/A" the openmap API will set the field to -1.  If unit 
 * conversion is required, it is done in the build() method.
 * 
 * @author L. Craig Carpenter
 */
public class DEMFrameAccuracy implements Serializable, Constants {

	/**
	 * Eclipse-generated serialVersionUID
	 */
	private static final long serialVersionUID = 5928533960361307307L;
 	
	private final int absHorzAccuracy;
 	private final int absVertAccuracy;
 	private final int relHorzAccuracy;
 	private final int relVertAccuracy;
 	private final HeightUnitType units;
 	
	/**
	 * Default constructor enforcing the builder creation pattern.
	 * @param builder Object containing default values for the internal 
	 * member objects.
	 */
 	protected DEMFrameAccuracy(DEMFrameAccuracyBuilder builder) {
 		absHorzAccuracy = builder.absHorzAccuracy;
 		absVertAccuracy = builder.absVertAccuracy;
 		relHorzAccuracy = builder.relHorzAccuracy;
 		relVertAccuracy = builder.relVertAccuracy;
 		units           = builder.units;
 	}
 	
 	/**
 	 * Getter method for the absolute horizontal accuracy 
 	 * (i.e. circular error).
 	 * @return The absolute horizontal accuracy.
 	 */
 	public int getAbsHorzAccuracy() {
 		return absHorzAccuracy;
 	}
 	
 	/**
 	 * Getter method for the absolute vertical accuracy 
 	 * (i.e. linear error).
 	 * @return The absolute vertical accuracy.
 	 */
 	public int getAbsVertAccuracy() {
 		return absVertAccuracy;
 	}
 	
 	/**
 	 * Getter method for the relative horizontal accuracy 
 	 * (i.e. circular error).
 	 * @return The relative horizontal accuracy.
 	 */
 	public int getRelHorzAccuracy() {
 		return relHorzAccuracy;
 	}
 	
 	/**
 	 * Getter method for the relative vertical accuracy 
 	 * (i.e. linear error).
 	 * @return The relative vertical accuracy.
 	 */
 	public int getRelVertAccuracy() {
 		return relVertAccuracy;
 	}
 	
 	/**
 	 * Getter method for the length units.
 	 * @return The length units.
 	 */
 	public HeightUnitType getUnits() {
 		return units;
 	}
 	
 	/**
 	 * Convert to printable String
 	 */
 	@Override
 	public String toString() {
 		StringBuilder sb = new StringBuilder();
 		sb.append("Accuracy : abs_horz_acc => [ ");
 		sb.append(this.getAbsHorzAccuracy());
 		sb.append(" ], abs_vert_acc => [ ");
 		sb.append(this.getAbsVertAccuracy());
 		sb.append(" ], rel_horz_acc => [ ");
 		sb.append(this.getRelHorzAccuracy());
 		sb.append(" ], rel_vert_acc => [ ");
 		sb.append(this.getRelVertAccuracy());
 		sb.append(" ].");
 		return sb.toString();
 	}
 	
    /**
     * Static inner class implementing the builder creation pattern for 
     * objects of type <code>DEMFrameAccuracy</code>.
     * 
     * @author L. Craig Carpenter
     */
	public static class DEMFrameAccuracyBuilder {
		
	 	private int            absHorzAccuracy = -1;
	 	private int            absVertAccuracy = -1;
	 	private int            relHorzAccuracy = -1;
	 	private int            relVertAccuracy = -1;
	 	private HeightUnitType units           = HeightUnitType.METERS;
	 	
		/**
		 * Build the target <code>DEMFrameAccuracy</code> object.
		 * 
		 * @return A constructed <code>DEMFrameAccuracy</code>.
		 * @throws IllegalStateException Thrown if the constructed object 
		 * fails any validation checks.
		 */
		public DEMFrameAccuracy build() throws IllegalStateException {
			if (units == HeightUnitType.FEET) {
				if (absHorzAccuracy > -1) { 
					absHorzAccuracy = Constants.convertToFeet(absHorzAccuracy);
				}
			 	if (absVertAccuracy > -1) {
			 		absVertAccuracy = Constants.convertToFeet(absVertAccuracy);
			 	}
			 	if (relHorzAccuracy > -1) { 
			 		relHorzAccuracy = Constants.convertToFeet(relHorzAccuracy);
			 	}
			 	if (relVertAccuracy > -1) {
			 		relVertAccuracy = Constants.convertToFeet(relVertAccuracy);
			 	}
			}
			DEMFrameAccuracy obj = new DEMFrameAccuracy(this);
			validate(obj);
			return obj;
		}
		
	 	/**
	 	 * Setter method for the absolute horizontal accuracy 
	 	 * (i.e. circular error).
	 	 * @param value The absolute horizontal accuracy.
	 	 * @return The builder object.
	 	 */
		public DEMFrameAccuracyBuilder absHorzAccuracy(int value) {
			if (value > -1) {
				absHorzAccuracy = value;
			}
			return this;
		}
		
	 	/**
	 	 * Setter method for the absolute vertical accuracy 
	 	 * (i.e. linear error).
	 	 * @param value The absolute vertical accuracy.
	 	 * @return The builder object.
	 	 */
		public DEMFrameAccuracyBuilder absVertAccuracy(int value) {
			if (value > -1) {
				absVertAccuracy = value;
			}
			return this;
		}
	 	
	 	/**
	 	 * Setter method for the relative horizontal accuracy 
	 	 * (i.e. circular error).
	 	 * @param value The relative horizontal accuracy.
	 	 * @return The builder object.
	 	 */
		public DEMFrameAccuracyBuilder relHorzAccuracy(int value) {
			if (value > -1) {
				relHorzAccuracy = value;
			}
	 		return this;
	 	}
	 	
	 	/**
	 	 * Setter method for the relative vertical accuracy 
	 	 * (i.e. linear error).
	 	 * @param value The relative vertical accuracy.
	 	 * @return The builder object.
	 	 */
	 	public DEMFrameAccuracyBuilder relVertAccuracy(int value) {
			if (value > -1) {
				relVertAccuracy = value;
			}
	 		return this;
	 	}
	 	
	 	/**
	 	 * Setter method for the length units.
	 	 * @param value The length units.
	 	 * @return The builder object.
	 	 */
	 	public DEMFrameAccuracyBuilder units(HeightUnitType value) {
	 		if (value != null) {
	 			units = value;
	 		}
	 		return this;
	 	}
	 	
		/**
		 * Validate the <code>DEMFrameAccuracy</code>.
		 * @param obj Candidate <code>DEMFrameAccuracy</code> object.
		 */
	 	private void validate(DEMFrameAccuracy obj) throws IllegalStateException {
	 		if (units == HeightUnitType.FEET) { 
		 		if (obj.getAbsHorzAccuracy() > Constants.convertToFeet(MAX_ACCURACY_VALUE_METERS)) {
		 			throw new IllegalStateException("The value for the absolute "
		 					+ "horizontal accuracy is out of range [ "
		 					+ obj.getAbsHorzAccuracy()
		 					+ " ].  Must be [ 0..."
		 					+ Constants.convertToFeet(MAX_ACCURACY_VALUE_METERS)
		 					+ " ].");
		 		}
		 		if (obj.getAbsVertAccuracy() > Constants.convertToFeet(MAX_ACCURACY_VALUE_METERS)) {
		 			throw new IllegalStateException("The value for the absolute "
		 					+ "vertical accuracy is out of range [ "
		 					+ obj.getAbsVertAccuracy()
		 					+ " ].  Must be [ 0..."
		 					+ Constants.convertToFeet(MAX_ACCURACY_VALUE_METERS)
		 					+ " ].");
		 		}
		 		if (obj.getRelHorzAccuracy() > Constants.convertToFeet(MAX_ACCURACY_VALUE_METERS)) {
		 			throw new IllegalStateException("The value for the relative "
		 					+ "horizontal accuracy is out of range [ "
		 					+ obj.getRelHorzAccuracy()
		 					+ " ].  Must be [ 0..." 
		 					+ Constants.convertToFeet(MAX_ACCURACY_VALUE_METERS)
		 					+ " ].");
		 		}
		 		if (obj.getRelVertAccuracy() > Constants.convertToFeet(MAX_ACCURACY_VALUE_METERS)) {
		 			throw new IllegalStateException("The value for the relative "
		 					+ "vertical accuracy is out of range [ "
		 					+ obj.getRelVertAccuracy()
		 					+ " ].  Must be [ 0..."
		 					+ Constants.convertToFeet(MAX_ACCURACY_VALUE_METERS)
		 					+ " ].");
		 		}
	 		}
	 		else {
		 		if (obj.getAbsHorzAccuracy() > MAX_ACCURACY_VALUE_METERS) {
		 			throw new IllegalStateException("The value for the absolute "
		 					+ "horizontal accuracy is out of range [ "
		 					+ obj.getAbsHorzAccuracy()
		 					+ " ].  Must be [ 0..."
		 					+ MAX_ACCURACY_VALUE_METERS
		 					+ " ].");
		 		}
		 		if (obj.getAbsVertAccuracy() > MAX_ACCURACY_VALUE_METERS) {
		 			throw new IllegalStateException("The value for the absolute "
		 					+ "vertical accuracy is out of range [ "
		 					+ obj.getAbsVertAccuracy()
		 					+ " ].  Must be [ 0..."
		 					+ MAX_ACCURACY_VALUE_METERS
		 					+ " ].");
		 		}
		 		if (obj.getRelHorzAccuracy() > MAX_ACCURACY_VALUE_METERS) {
		 			throw new IllegalStateException("The value for the relative "
		 					+ "horizontal accuracy is out of range [ "
		 					+ obj.getRelHorzAccuracy()
		 					+ " ].  Must be [ 0..." 
		 					+ MAX_ACCURACY_VALUE_METERS
		 					+ " ].");
		 		}
		 		if (obj.getRelVertAccuracy() > MAX_ACCURACY_VALUE_METERS) {
		 			throw new IllegalStateException("The value for the relative "
		 					+ "vertical accuracy is out of range [ "
		 					+ obj.getRelVertAccuracy()
		 					+ " ].  Must be [ 0..."
		 					+ MAX_ACCURACY_VALUE_METERS
		 					+ " ].");
		 		}
	 		}
	 	}
	}
}
