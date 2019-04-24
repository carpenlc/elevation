package mil.nga.npd;

import java.text.DecimalFormat;

/**
 * This code was originally part of the NGA/S "Geospatial Research" (GR) tool
 * and was initially written in VB6.  It was ported to Java around 2010.  It's 
 * a mess of if/then statements designed to accept geographic coordinates in a 
 * variety of formats (decimal degree, DMS, etc.)  This is one of those classes 
 * that it's better just to leave it alone rather than try to make it elegant.
 *
 * Code was modified slightly to implement the Singleton design pattern.
 * 
 * @author jenningd
 */
public class CoordsParse {
   
	/**
	 * String formatter for use throughout the class.
	 */
    private final static DecimalFormat df3 = new DecimalFormat("###0.000");
    
    /**
     * Default no-arg constructor implementing the singleton design pattern.
     */
    private CoordsParse() { }
    
	/**
	 * Accessor method for the singleton instance of the 
	 * <code>CoordsParse</code> object.
	 * @return The singleton instance of <code>CoordsParse</code>.
	 */
    public static CoordsParse getInstance() {
    	return CoordsParseHolder.getInstance();
    }
    
    /**
     * Determine whether or not the input String is a valid numeric value.
     * 
     * @param s A potential numeric String.
     * @return True if the input String is numeric, false otherwise.
     */
    private static boolean isNumeric(final String s) {
        boolean isNum = true;
        try {
            Double.valueOf(s).doubleValue();
        } 
        catch (NumberFormatException e) {
            isNum = false;
        }
        return isNum;
    }
    
    
    private double parseDMSString(String strInput, boolean blnLat) {
        
        final String strDirChars = "SsNnEeWw";
        final String strFieldSep = "-/\\:;)(_ ";
        final String strValidChars = strFieldSep + ".0123456789";
        
        double dblDD = 0.0;
        
        try {
            String strUserInput = strInput;
            
            String strDir = "";
            
            int intNumDir = 0;
            
            String strChar = "";
            String strUserInputMod = "";
            
            // Negative or Positive direction indicator can only be first character
            strChar = strUserInput.substring(0,1);
            if (strChar.equals("+") || strChar.equals("-")) {
                strDir = strChar;
                intNumDir = intNumDir + 1;
                strUserInput = strUserInput.substring(1);
            }
            
            
            // Loop through the input string to see if there are any direction characters
            int i = 0;
            while (i < strUserInput.length()) {
                strChar = String.valueOf(strUserInput.charAt(i));
                /*
                if (strChar.equals("+") || strChar.equals("-")) {
                    return -1005;
                }
                */
                if (strDirChars.indexOf(strChar) != -1) {
                    // Found a direction char
                    strDir = strChar;
                    intNumDir = intNumDir + 1;
                    strUserInputMod = strUserInputMod + "";    
                } else {
                    strUserInputMod = strUserInputMod + strChar;
                }
                i = i + 1;
            }
            
            strUserInput = strUserInputMod.trim();
            
            // **** Can have only one direction indicator ****
            if (intNumDir > 1) {
                return -1004;
            }
            
            // **** Lat can't be WwEe and Lon can't be SsNn *****
            if (intNumDir > 0) {
                if (blnLat) {
                    if ("WwEe".indexOf(strDir) != -1) {
                        return -1018;
                    }
                } else {
                    if ("NnSs".indexOf(strDir) != -1) {
                        return -1019;
                    }
                }
            }
            
            // **** Eliminate the field seps and check for invalid chars ****
            int intNumPeriods = 0;
            
            // Loop through each char in strInput  
            
            strUserInputMod = "";
            
            char[] charUserInput = strUserInput.toCharArray();

            i = 0;
            while (i < charUserInput.length) {
                
                strChar = String.valueOf(charUserInput[i]);
                
                // check to make sure the character is valid
                if (strValidChars.indexOf(strChar) == -1) {
                    // This is not a valid character
                    if (blnLat) {
                        return -1001;
                    } else {
                        return -1002;
                    }
                }
                
                // Change the field seperators to spaces
                if (strFieldSep.indexOf(strChar) != -1) {
                    // This is a field seperator 
                    if (strUserInputMod.charAt(strUserInputMod.length() - 1) != ' ') {
                        strUserInputMod = strUserInputMod + " ";
                    }
                } else {
                    strUserInputMod = strUserInputMod + strChar;
                }
                
                
                if (strChar.equalsIgnoreCase(".")) {
                    intNumPeriods = intNumPeriods + 1;
                }
                
                i = i + 1;
                
            }
           
            // **** Should be only one decimal point
            
            if (intNumPeriods > 1) {
                return -1003;
            }
            
            strUserInput = strUserInputMod;
            
            // Read Degrees, Minutes, Seconds from userInput
            
            String strDeg;
            String strMin;
            String strSec;
            
            // Split the input based on spaces
            String[] strParts;
            
            if (isNumeric(strUserInput)) {
                
                // DMS without a delimiter
                String strTemp;
                
                if (strUserInput.indexOf(".") != -1) {
                    // String contains a decimal point
                    strParts = strUserInput.split("\\.");
                    strTemp = strParts[0];
                } else {
                    strTemp = strUserInput;
                }
                   
                switch (strTemp.length()) {
                    case 0:
                    case 1:
                    case 2:
                        strDeg = strUserInput;
                        strMin = "0";
                        strSec = "0";
                        break;
                    case 3:
                        if (blnLat) {
                            // DMM latitude
                            strDeg = strUserInput.substring(0, 1);
                            strMin = strUserInput.substring(1);
                            strSec = "0";
                        } else {
                            // DDD Longitude
                            strDeg = strUserInput;
                            strMin = "0";
                            strSec = "0";
                        }
                        break;
                    case 4:
                        // DDMM
                        strDeg = strUserInput.substring(0, 2);
                        strMin = strUserInput.substring(2);
                        strSec = "0";
                        break;
                    case 5:
                        
                        if (blnLat) {
                            // DMMSS Latitude   
                            strDeg = strUserInput.substring(0, 1);
                            strMin = strUserInput.substring(1, 3);
                            strSec = strUserInput.substring(3);                           
                        } else {
                            // DDDMM Longitude
                            strDeg = strUserInput.substring(0, 3);
                            strMin = strUserInput.substring(3);
                            strSec = "0";       
                        }
                        break;
                    case 6:
                        // DDMMSS
                        strDeg = strUserInput.substring(0,2);
                        strMin = strUserInput.substring(2,4);
                        strSec = strUserInput.substring(4);
                        break;
                    case 7:
                        if (blnLat) {
                            return -1008;
                        } else {
                            // DDDMMSS
                            strDeg = strUserInput.substring(0,3);
                            strMin = strUserInput.substring(3,5);
                            strSec = strUserInput.substring(5);
                        }
                        break;
                       
                    default:
                        if (blnLat) {
                            return -1008;
                        } else {
                            return -1009;
                        }
                           
                }
                
                
                
                
            } else {
               // DMS with delimiter
               strParts = strUserInput.split(" ");
               
               switch (strParts.length) {
                   case 2:
                       // D M
                       strDeg = strParts[0];
                       strMin = strParts[1];
                       strSec = "0";
                       
                       break;
                       
                   case 3:
                       // D M S               
                       strDeg = strParts[0];
                       strMin = strParts[1];
                       strSec = strParts[2];

                       // Minutes cannot be decimal in D M S format
                       if (strMin.indexOf(".") != -1) {
                            return -1011;
                       }


                       
                       break;
                   default:
                       // Error  
                       return -1007;
                                
               }

               // Degrees should never be decimal in a delimited input
               if (strDeg.indexOf(".") != -1) {
                    return -1010;
                }
               
               
            }
            
            
            
            double dblDeg = Double.valueOf(strDeg).doubleValue();
            double dblMin = Double.valueOf(strMin).doubleValue();
            double dblSec = Double.valueOf(strSec).doubleValue();
            
            if (dblMin >= 60) {
                return -1015;
            }
            
            if (dblSec >= 60) {
                return -1014;
            }
            
            dblDD = dblDeg + dblMin / 60.0 + dblSec / 3600.0;
            
            if (strDir != "" && "WwSs-".indexOf(strDir) != -1) {
                dblDD = -dblDD;
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dblDD;
        
    }
    
  
    /*
     * parseCoordString 
     *
     * Parses a coordinate and returns the decimal degree equivalent of the coordinate.
     *
     * Inputs:
     *      strUserInput: Any valid coordinate (DMS or DD)
     *      blnLat: true => strUserInput is latitude 
     *              false => strUserInput is longitude
     *
     * Returns: Decimal degree value of strUserInput.  Return value < -900 implies an error occured during the parse.
     *
     */


    private double Round(double Rval, int Rpl) {
  
        double p = (double)Math.pow(10,Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (double)tmp/p;
    }    
    
    /**
     * 
     * Program returns the decimal degree value cooresponding to the user input.
     * @param strUserInput User inputs a string with any <a href="http://localhost:9090/docs/EarthCoordFormats.html">valid Earth Coordinate format</a>.
     * @param blnLat true implies the coordinate is a latitude value; false implies a longitude.
     * @return returns a double number.  The number will be either the decimal degree value or an error code.  Return values < -900 are error codes.
     */
    public double parseCoordString(String strUserInput, boolean blnLat) {

        
        
        double dblDD = 0.0;
        double dblMax;
        dblMax = blnLat ? 90.0 : 360.0;
        
        try {
        
            dblDD = 0.0;

            // Trim off any extra spaces at beginning or end of user input
            strUserInput = strUserInput.trim();

            // Test to see if input is DMS or Decimal Degrees
            boolean blnDMS = false;

            // If the string starts with two or three zeros assume DMS
            if (blnLat && strUserInput.length() >= 2 && 
                    strUserInput.substring(0, 2).equals("00")) {
                blnDMS = true;
            } else if (blnLat && strUserInput.length() >= 3 && 
                    strUserInput.substring(0, 3).equals("000")) {
                blnDMS = true;
            } else if (!isNumeric(strUserInput)) {
                blnDMS = true;
            } else {
                double dblUserInput = Double.valueOf(strUserInput).doubleValue();
                // if the number is > dlbMax assume its DMS number

                if (Math.abs(dblUserInput) > dblMax) {
                    blnDMS = true;
                }
            }

            // Parse it

            if (blnDMS) {
                dblDD = parseDMSString(strUserInput, blnLat);
                // Limit the precision to 6 decimal places
                dblDD = Round(dblDD, 6);
            } else {
                dblDD = Double.valueOf(strUserInput).doubleValue();
                if (blnLat) {
                    if (dblDD > dblMax) {
                        dblDD = -1020;
                    } else if (dblDD < -90) {
                        dblDD = -1021;
                    }            
                } else {
                    if (dblDD > dblMax) {
                        dblDD = -1022;
                    } else if (dblDD < -180) {
                        dblDD = -1023;
                    }                        
                    
                }        

            
            }


            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (blnLat) {
            if (dblDD > dblMax) {
                // Latitude must be less than 90
                dblDD = -1024;
            } 
            if (dblDD < -90 && dblDD > -1000) {
                // Latitude has to be greater than -90; unless error detected
                dblDD = -1025;
            }
        } else {
            if (dblDD > dblMax) {
                // Latitude must be less than 90
                dblDD = -1026;
            } 
            if (dblDD < -180 && dblDD > -1000) {
                // Latitude has to be greater than -90; unless error detected
                dblDD = -1027;
            }
            
        }
        
        
        return dblDD;
    }
    
    public String dd2dms(double dd, boolean blnNSEW, String sep, boolean blnLat) {
        String dms = "";
        
        int deg = (int) dd;
        
        double dmin = (dd - deg) * 60.0;
        
        int min = (int) dmin;
        
        double dsec = (dmin - min) * 60.0;
        
        String sepChar = "";
        if (!sep.equalsIgnoreCase("")) {
            sepChar = String.valueOf(sep.charAt(0));
        }
        
        
        String strHemi = "";
        if (blnNSEW) {
            if (blnLat) {
                if (deg < 0) {
                    strHemi = sepChar + "S";
                    deg = Math.abs(deg);
                } else {
                    strHemi = sepChar + "N";
                }
                
            } else {
                if (deg < 0) {
                    strHemi = sepChar + "W";
                    deg = Math.abs(deg);
                } else {
                    strHemi = sepChar + "E";
                }                
                
            }
                
        }
        
        dms = String.valueOf(deg) + sepChar +  String.valueOf(min) + sepChar + String.valueOf(new Double(df3.format(dsec)).doubleValue()) + strHemi;
        
        
        return dms;
    }
    
    /**
     * Static inner class used to construct the singleton instance of the
     * <code>CoordsParse</code> object.  This class exploits the fact that
     * classes are not loaded until they are referenced therefore enforcing
     * thread safety without the performance hit associated with using 
     * <code>synchronized</code>.
     * 
     * @author L. Craig Carpenter
     */
    public static class CoordsParseHolder {
    	
    	/**
    	 * Reference to the singleton instance of <code>CoordsParse</code>
    	 */
    	private static CoordsParse instance = null;
    	
    	/**
    	 * Accessor method for the singleton instance of the 
    	 * <code>CoordsParse</code> object.
    	 * @return The singleton instance of <code>CoordsParse</code>.
    	 */
    	public static CoordsParse getInstance() {
    		if (instance == null) {
    			instance = new CoordsParse();
    		}
    		return instance;
    	}
    }
    
    
    /**
     * 
     * This main function is to be used for testing purposes only.
     * @param args Not used.  Any values entered are ignored.
     */
    public static void main(String[] args) {
        
        CoordsParse cp = new CoordsParse();

//        System.out.println(cp.dd2dms(277.50822, true, "   ", false));
        
        // Test a single point
        double dblDD;
        //dblDD = cp.parseCoordString("359.9999", false);
        dblDD = cp.parseCoordString("3595959.8", false);
        
        if (dblDD > -1000) {
            System.out.println(dblDD);
            //System.out.println(cp.getParseErrorMsg(dblDD));
                    
        } else {
            System.out.println(dblDD);
            //System.out.println(cp.getParseErrorMsg(dblDD));
        }
        
        /*
        try {
            FileReader fr = new FileReader("C:\\javagold\\CoordParse\\src\\mil\\nga\\to6\\InputCoords.txt");
    
            BufferedReader br = new BufferedReader(fr);
            
            String strLine = null;
            while ((strLine = br.readLine()) != null) {            
                strLine = strLine.trim();
                if (strLine.equalsIgnoreCase("")) {
                    // blank line skip
                }  else if (strLine.charAt(0) == '#') {
                    // comment line                                        
                        System.out.println("<tr><td colspan=\"4\">");
                        System.out.println("<b>" + strLine.substring(1) + "</b>");
                        System.out.println("</td></tr>");
                } else {
                    // This line has two field DMS value and (LAT or LON)
                    String strValues[]  = strLine.split("\t");
                    //System.out.println(strValues[0]);
                    //System.out.println(strValues[1]);                                                           
                    if (strValues.length != 2) {
                        System.out.println("<tr><td colspan=\"4\">");
                        System.out.println("Wrong number of fields => " + strLine);
                        System.out.println("</td></tr>");
                    } else {
                        // Right number of fields                        
                        String strDMS = strValues[0];
                        String strLat = strValues[1];
                        
                        if (strLat.equalsIgnoreCase("LAT")) {
                            double dblLat = cp.parseCoordString(strDMS, true);
                            String strErrorMsg = "&nbsp;";
                            if (dblLat < -1000) {
                                strErrorMsg = cp.getParseErrorMsg(dblLat);
                            }
                            
                            System.out.println("<tr><td>" + strDMS + "</td>");
                            System.out.println("<td>" + strLat + "</td>");
                            System.out.println("<td>" + dblLat + "</td>");
                            System.out.println("<td>" + strErrorMsg + "</td></tr>");                            
                        } else if (strLat.equalsIgnoreCase("LON")) {
                            double dblLon = cp.parseCoordString(strDMS, false);
                            String strErrorMsg = "&nbsp;";
                            if (dblLon < -1000) {
                                strErrorMsg = cp.getParseErrorMsg(dblLon);
                            }
                            System.out.println("<tr><td>" + strDMS + "</td>");
                            System.out.println("<td>" + strLat + "</td>");
                            System.out.println("<td>" + dblLon + "</td>");
                            System.out.println("<td>" + strErrorMsg + "</td></tr>");                            
                        } else {
                            System.out.println("<tr><td colspan=\"4\">");
                            System.out.println("Second Parameter must be LAT or LON =>" + strLine);
                            System.out.println("</td></tr>");
                            
                        }
                        
                    
                    }
                    
                }                          
            } 
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        */
        
        
        
        
        
        
       
    }
        
    
    
}
