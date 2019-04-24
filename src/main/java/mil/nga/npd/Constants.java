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
	 * Default method used to convert meters to feet.
	 * 
	 * @param meters The number of meters.
	 * @return Same length just in units of feet.
	 */
	static int convertToFeet(int meters) {
    	return (int)((double)meters * NUMBER_OF_FEET_IN_METER);
	}
}
