package mil.nga.npd;

public interface Constants {

	/**
	 * Constant defining the number of feet in a meter.
	 */
	public static final double NUMBER_OF_FEET_IN_METER = 3.2808;
	
	/**
	 * The largest possible accuracy value based on the MIL-SPEC 
	 * document.
	 */
	public static final int MAX_ACCURACY_VALUE_METERS = 9999;
	
	/** 
	 * Constant used to define when an elevation value does not exist 
	 * for a given post.
	 */
	public static final int INVALID_ELEVATION_VALUE = -32767;
	
	/** 
	 * The largest possible value for an elevation post.
	 */
	public static final int MAX_ELEVATION = 32767;
	
	/**
	 * The maximum number of points that we allow a client to request in
	 * a single call.
	 */
	public static final int MAX_ALLOWABLE_POINTS = 20;
	
	/**
	 * If classification is not set, mark it limited distribution.
	 */
	public static final String DEFAULT_CLASSIFICATION_MARKING = "LIMDIS";
	
	/**
	 * Default method used to convert meters to feet.
	 * 
	 * @param meters The number of meters.
	 * @return Same length just in units of feet.
	 */
	public static int convertToFeet(int meters) {
    	return (int)((double)meters * NUMBER_OF_FEET_IN_METER);
	}
	
	/**
	 * Generic implementation of a ternary operator for setting values for 
	 * types that do not have to be supplied.
	 * @param value Input value.
	 * @param defaultValue Default value to use if the input is null.
	 * @return The value to utilize.
	 */
	public static <T> T getValueOrDefault(T value, T defaultValue) {
	    return value == null ? defaultValue : value;
	}
}
