package mil.nga.npd;

import java.util.HashMap;
import java.util.Map;

public class TestCoordinates {

    /**
     * List of valid latitude coordinates and their double value.
     */
    public Map<String, Double> validLatitudes = new HashMap<String, Double>();
    
    /**
     * List of valid longitude coordinates and their double value.
     */
    public Map<String, Double> validLongitudes= new HashMap<String, Double>();
    
    /** 
     * List of invalid longitude coordinates.
     * NOTE: If the contents of this array is modified the subsequent jUnit
     * tests will fail.
     */
    public String[] invalidLongitudes = {
        "-180 00 01",
        "W180 10 10",
        "W123 01 56e",
        "N12 01 40",
        "23561536W",
        "123 / 01 / 89",
        "W123E123W",
        "-123-12-12-234"
    };
    
    /** 
     * List of invalid longitude coordinates.  
     * NOTE: If the contents of this array is modified the subsequent jUnit
     * tests will fail.
     */
    public String[] invalidLatitudes = {
        "-90.01",
        "90.1",
        "-123 n01 34",
        "N123 01 56S",
        "W12 01 40",
        "00000123",
        "2356153N",
        "23 / 01 / 89",
        "N23E123N",
        "-12-12-34-234",
        "z123",
        "12.34.45",
        "N180S",
        "45 30W15.3"
    };
    
    public TestCoordinates() {
        
        // Initialize the latitude test coords.
        validLatitudes.put("N23 01 25.2",      Double.valueOf(23.023668));
        validLatitudes.put("S1 1 1.5",         Double.valueOf(-1.017083));
        validLatitudes.put("S0 0 59.8",        Double.valueOf(-0.016611));
        validLatitudes.put("23 01 25.2N",      Double.valueOf(23.023668));
        validLatitudes.put("-13 30 30.8",      Double.valueOf(-13.508556));
        validLatitudes.put("+23/01/25.2",      Double.valueOf(23.023668));
        validLatitudes.put("230125.2",         Double.valueOf(23.023668));
        validLatitudes.put("+45 00 00-",       Double.valueOf(45.0));
        validLatitudes.put("9.87 N",           Double.valueOf(9.87));
        validLatitudes.put("9.87N",            Double.valueOf(9.87));
        validLatitudes.put("+12-13-45-",       Double.valueOf(12.229167));
        validLatitudes.put("23.5",             Double.valueOf(23.5));
        validLatitudes.put("-83.5",            Double.valueOf(-83.5));
        validLatitudes.put("0",                Double.valueOf(0.0));
        validLatitudes.put("-90",              Double.valueOf(-90.0));
        validLatitudes.put("90",               Double.valueOf(90.0));
        validLatitudes.put("0N",               Double.valueOf(0.0));
        validLatitudes.put("13N",              Double.valueOf(13.0));
        validLatitudes.put("135S",             Double.valueOf(-1.583333));
        validLatitudes.put("1356S",            Double.valueOf(-13.933333));
        validLatitudes.put("13551N",           Double.valueOf(1.5975));
        validLatitudes.put("134315N",          Double.valueOf(13.720833));
        validLatitudes.put("N23 01 25",        Double.valueOf(23.023612));
        validLatitudes.put("23 01 25S",        Double.valueOf(-23.023612));
        validLatitudes.put("23-01-25S",        Double.valueOf(-23.023612));
        validLatitudes.put("23/01/25N",        Double.valueOf(23.023612));
        validLatitudes.put("23\\01\\25N",      Double.valueOf(23.023612));
        validLatitudes.put("23:01:25S",        Double.valueOf(-23.023612));
        validLatitudes.put("23)01)25S",        Double.valueOf(-23.023612));
        validLatitudes.put("23(01(25S",        Double.valueOf(-23.023612));
        validLatitudes.put("23_01_25S",        Double.valueOf(-23.023612));
        validLatitudes.put("-23/01/25",        Double.valueOf(-23.023612));
        validLatitudes.put("23 / 01 / 59",     Double.valueOf(23.033056));
        validLatitudes.put("-23 // 01 // 59",  Double.valueOf(-23.033056));
        
        // Initialize the longitude test coords.
        validLongitudes.put("W123 01 25.2",    Double.valueOf(-123.023664));
        validLongitudes.put("E37 45 59.9",     Double.valueOf(37.76664));
        validLongitudes.put("W1 8 0.1",        Double.valueOf(-1.133361));
        validLongitudes.put("123 01 25.2W",    Double.valueOf(-123.023664));
        validLongitudes.put("179 59 59.999E",  Double.valueOf(180.0));
        validLongitudes.put("0 0 0.00000W",    Double.valueOf(0.0));
        validLongitudes.put("-123/01/25.2",    Double.valueOf(-123.023664));
        validLongitudes.put("1230125.2",       Double.valueOf(123.023664));
        validLongitudes.put("+45 00 00-",      Double.valueOf(45.0));
        validLongitudes.put("123.34E",         Double.valueOf(123.34));
        validLongitudes.put("123.34 E",        Double.valueOf(123.34));
        validLongitudes.put("+123-13-45-",     Double.valueOf(123.229168));
        validLongitudes.put("123 W01 34",      Double.valueOf(-123.026112));
        validLongitudes.put("133.5",           Double.valueOf(133.5));
        validLongitudes.put("-12.5",           Double.valueOf(-12.5));
        validLongitudes.put("-27.1",           Double.valueOf(-27.1));
        validLongitudes.put("180.0",           Double.valueOf(180.0));
        validLongitudes.put("-180.0",          Double.valueOf(-180.0));
        validLongitudes.put("0.0",             Double.valueOf(0.0));
        validLongitudes.put("0",               Double.valueOf(0.0));
        validLongitudes.put("W123 01 25",      Double.valueOf(-123.023608));
        validLongitudes.put("123 01 25E",      Double.valueOf(123.023608));
        validLongitudes.put("123-01-25E",      Double.valueOf(123.023608));
        validLongitudes.put("123/01/25E",      Double.valueOf(123.023608));
        validLongitudes.put("123\\01\\25E",      Double.valueOf(123.023608));
        validLongitudes.put("123:01:25E",      Double.valueOf(123.023608));
        validLongitudes.put("123)01)25E",      Double.valueOf(123.023608));
        validLongitudes.put("123(01(25E",      Double.valueOf(123.023608));
        validLongitudes.put("123_01_25E",      Double.valueOf(123.023608));
        validLongitudes.put("-123/01/25",      Double.valueOf(-123.023608));
        validLongitudes.put("123 / 01 / 59",   Double.valueOf(123.033056));
        validLongitudes.put("123 // 01 // 59", Double.valueOf(123.033056));
        validLongitudes.put("0E",              Double.valueOf(0.0));
        validLongitudes.put("13W",             Double.valueOf(-13.0));
        validLongitudes.put("135W",            Double.valueOf(-135.0));
        validLongitudes.put("1356W",           Double.valueOf(-13.933333));
        validLongitudes.put("13551W",          Double.valueOf(-135.85));
        validLongitudes.put("135615W",         Double.valueOf(-13.9375));
        validLongitudes.put("1355153W",        Double.valueOf(-135.86472));
    }
}
