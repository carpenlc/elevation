/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.nga.to6;

import com.bbn.openmap.dataAccess.dted.DTEDFrame;
import com.bbn.openmap.omGraphics.OMGrid;
import com.vividsolutions.jts.algorithm.SimplePointInAreaLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mil.nga.elevation.BboxBean;
import mil.nga.elevation.ElevationBean;
import mil.nga.elevation.GeodeticCoordinateBean;
import mil.nga.elevation.GetElevationAt;
import mil.nga.elevation.GetElevationAtResponse;
import mil.nga.elevation.GetMinMaxElevations;
import mil.nga.elevation.GetMinMaxElevationsResponse;
import mil.nga.elevation.GetMinMaxElevationsWKT;
import mil.nga.elevation.GetMinMaxElevationsWKTResponse;
import mil.nga.elevation.HeightUnitType;
import mil.nga.elevation.TerrainDataType;
import mil.nga.security.SecurityElement;
import org.apache.log4j.Category;
import us.gov.ic.ism.v2.ClassificationType;

/**
 *
 * @author david
 */
public class ElevationQuery {

    // Logging
    static final Category log = Category.getInstance(ElevationQuery.class);
    // These variables used for direct database connection (Loaded from Configuration.properties)
    String m_connectionURL = "jdbc:oracle:thin:@c4ora:1521:TSH1";
    String m_userID = "WS";
    String m_userPassword = "WS";
    String m_TERRAIN_DATA_FILES_TABLE = " TERRAIN_DATA_FILES";
    String m_FILE_SEP = System.getProperty("file.separator");
    // Connection Pool
    String m_JNDI_Name = null;
    // Module variable for database connection and statement
    Connection m_con = null;
    Statement m_stmt = null;
    // Set From Properties File
    private boolean m_WIN_PATH = true;
    private float m_MAX_BBOX_SQ_DEG_LVL0 = 16.0f;
    private float m_MAX_BBOX_SQ_DEG_LVL1 = 4.0f;
    private float m_MAX_BBOX_SQ_DEG_LVL2 = 1.0f;
    private float m_MIN_LAT_LON_DIFF = 0.001f;
    private int m_MAX_NUM_POINTS_PLY = 200;
    private int m_MAX_NUM_POINTS_AT = 10;
    // Constants
    private final short INVALID_MAX_ELEV = -16958;
    private final short INVALID_MIN_ELEV = 16958;
    private double METERS_TO_FEET = 3.2808399;
    // Configuration Properties with several configurable paramters deployed with the application
    final static String STR_CONFIG_FILE = "/Configuration.properties";

    public ElevationQuery() {
    }

    /**
     *
     * Turns the stack trace into a string.
     * @param t An Exeception Type
     * @return String
     */
    private String getStackTrace(Throwable t) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * getJdbcWs
     *
     * Returns DataSource for Connection Pool
     * @return
     * @throws javax.naming.NamingException
     */
    private DataSource getJdbcWs() throws NamingException {
        Context c = new InitialContext();
        //return (DataSource) c.lookup("java:comp/env/" + m_JNDI_Name);
        return (DataSource) c.lookup(m_JNDI_Name);
    }

    /**
     * openConnection
     *
     * This function opens a connection to the database.
     * @return true if successful
     */
    private boolean openConnection() {


        boolean blnOK = false;
        try {
            log.debug("openConnection Begin");

            if (m_con == null || m_con.isClosed()) {
                // connect if needed
                InputStream pis = getClass().getResourceAsStream(STR_CONFIG_FILE);
                blnOK = loadPropertiesFromStream(pis);

                if (!blnOK) {
                    // failed to load properties
                    log.error("Failed to load properties");
                    throw new Exception("Failed to load properties");
                }

                if (m_JNDI_Name == null) {
                    // using the properties read from configuration create a connection to the database
                    Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                    m_con = DriverManager.getConnection(m_connectionURL, m_userID, m_userPassword);
                    blnOK = true;
                    log.debug("Using Direct Database Connection.");

                } else {
                    /* Use a Connection Pool on the Server */
                    DataSource ds = getJdbcWs();
                    m_con = ds.getConnection();
                    blnOK = true;
                    log.debug("Using Connection Pool.");
                }


                m_stmt = m_con.createStatement();
                m_stmt.setQueryTimeout(59);



            } else {
                // Connection is already open
                blnOK = true;
                log.debug("openConnection Connection Already Open");
            }

        } catch (Exception e) {
            // Something didn't work
            blnOK = false;
            log.error(getStackTrace(e));
        }
        log.debug("openConnection End");
        return blnOK;
    }

    /**
     * closeConnection
     *
     * Close the connection to the database.
     * @return true if close is successful.
     */
    private boolean closeConnection() {
        boolean blnOK = false;
        try {
            log.debug("closeConnection Begin");

            // Just try and close the connection

            try {
                m_stmt.close();

            } catch (Exception e) {
                // ignore e
            }

            m_con.close();

            m_con = null;



            log.debug("closeConnection End");
        } catch (Exception e) {
            // Connection close fails when connection is already closed
            blnOK = false;
            //log.debug(getStackTrace(e));
        }
        return blnOK;
    }

    /**
     * Loads parametes from an Inputstream
     * @param pis
     * @return true if the load was successful; otherwise false
     */
    private boolean loadPropertiesFromStream(InputStream pis) {

        // Loads the properties from the configuration file
        boolean ok = true;

        // Set some null strings to hold the properties for validation

        String strMAX_BBOX_SQ_DEG_LVL0 = null;
        String strMAX_BBOX_SQ_DEG_LVL1 = null;
        String strMAX_BBOX_SQ_DEG_LVL2 = null;

        String strMIN_LAT_LON_DIFF = null;

        String strMAX_NUM_POINTS_PLY = null;
        String strMAX_NUM_POINTS_AT = null;

        String strTERRAIN_DATA_FILES_TABLE = null;

        String strPATH_TYPE = null;


        String strJNDI_Name = null;

        String strServer = null;
        String strUser = null;
        String strPassword = null;
        String strSID = null;
        String strPort = null;


        try {

            log.debug("Begin loadPropertiesFromStream");


            // Get the properties
            Properties p = new Properties();
            p.load(pis);

            log.debug("Load Properities from Configuration.properties File");
            // Values read from the config file
            strMAX_BBOX_SQ_DEG_LVL0 = p.getProperty("MAX_BBOX_SQ_DEG_LVL0");
            strMAX_BBOX_SQ_DEG_LVL1 = p.getProperty("MAX_BBOX_SQ_DEG_LVL1");
            strMAX_BBOX_SQ_DEG_LVL2 = p.getProperty("MAX_BBOX_SQ_DEG_LVL2");

            strMAX_NUM_POINTS_PLY = p.getProperty("MAX_NUM_POINTS_PLY");

            strMAX_NUM_POINTS_AT = p.getProperty("MAX_NUM_POINTS_AT");

            strMIN_LAT_LON_DIFF = p.getProperty("MIN_LAT_LON_DIFF");

            strTERRAIN_DATA_FILES_TABLE = p.getProperty("TERRAIN_DATA_FILES_TABLE");

            strPATH_TYPE = p.getProperty("PATH_TYPE");

            strJNDI_Name = p.getProperty("JNDI_Name");

            strServer = p.getProperty("Server");
            strUser = p.getProperty("User");
            strPassword = p.getProperty("Password");
            strSID = p.getProperty("SID");
            strPort = p.getProperty("Port");


            log.debug("Check Properties");
            if (strTERRAIN_DATA_FILES_TABLE == null) {
                log.debug("TERRAIN_DATA_FILES_TABLE must be set in service Configuration.");
                throw new UnsupportedOperationException("TERRAIN_DATA_FILES_TABLE must be set in service Configuration. Contact Service Provider");
            } else {
                m_TERRAIN_DATA_FILES_TABLE = strTERRAIN_DATA_FILES_TABLE.trim();
            }


            if (strMAX_BBOX_SQ_DEG_LVL0 == null) {
                log.debug("MAX_BBOX_SQ_DEG_LVL0 must be set in service Configuration.");
                throw new UnsupportedOperationException("MAX_BBOX_SQ_DEG_LVL0 must be set in service Configuration. Contact Service Provider");
            } else {
                m_MAX_BBOX_SQ_DEG_LVL0 = Float.parseFloat(strMAX_BBOX_SQ_DEG_LVL0.trim());
            }

            if (strMAX_BBOX_SQ_DEG_LVL1 == null) {
                log.debug("MAX_BBOX_SQ_DEG_LVL1 must be set in service Configuration.");
                throw new UnsupportedOperationException("MAX_BBOX_SQ_DEG_LVL1 must be set in service Configuration. Contact Service Provider");
            } else {
                m_MAX_BBOX_SQ_DEG_LVL1 = Float.parseFloat(strMAX_BBOX_SQ_DEG_LVL1.trim());
            }

            if (strMAX_BBOX_SQ_DEG_LVL2 == null) {
                log.debug("MAX_BBOX_SQ_DEG_LVL2 must be set in service Configuration.");
                throw new UnsupportedOperationException("MAX_BBOX_SQ_DEG_LVL2 must be set in service Configuration. Contact Service Provider");
            } else {
                m_MAX_BBOX_SQ_DEG_LVL2 = Float.parseFloat(strMAX_BBOX_SQ_DEG_LVL2.trim());
            }

            if (strMAX_NUM_POINTS_PLY == null) {
                log.debug("MAX_NUM_POINTS_PLY must be set in service Configuration.");
                throw new UnsupportedOperationException("MAX_NUM_POINTS_PLY must be set in service Configuration. Contact Service Provider");
            } else {
                m_MAX_NUM_POINTS_PLY = Integer.parseInt(strMAX_NUM_POINTS_PLY.trim());

            }

            if (strMAX_NUM_POINTS_AT == null) {
                log.debug("MAX_NUM_POINTS_AT must be set in service Configuration.");
                throw new UnsupportedOperationException("MAX_NUM_POINTS_AT must be set in service Configuration. Contact Service Provider");
            } else {
                m_MAX_NUM_POINTS_AT = Integer.parseInt(strMAX_NUM_POINTS_AT.trim());

            }

            if (strMIN_LAT_LON_DIFF == null) {
                log.debug("MIN_LAT_LON_DIFF must be set in service Configuration.");
                throw new UnsupportedOperationException("MIN_LAT_LON_DIFF must be set in service Configuration. Contact Service Provider");
            } else {
                m_MIN_LAT_LON_DIFF = Float.parseFloat(strMIN_LAT_LON_DIFF.trim());
            }

            if (strJNDI_Name == null) {
                // Administrator must specify the database connection info in the config file
                if (strServer == null || strUser == null || strPassword == null ||
                        strSID == null || strPort == null) {
                    ok = false;
                } else {
                    m_connectionURL = "jdbc:oracle:thin:@" + strServer.trim() + ":" +
                            strPort.trim() + ":" + strSID.trim();
                    m_userID = strUser.trim();
                    m_userPassword = strPassword.trim();
                    m_JNDI_Name = null;
                }
            } else {
                // Administrator has setup an Connection Pool
                m_JNDI_Name = strJNDI_Name.trim();
            }

            if (strPATH_TYPE == null) {
                // Default to Windows Path
                m_WIN_PATH = true;
            } else {
                if (strPATH_TYPE.equalsIgnoreCase("WIN")) {
                    m_WIN_PATH = true;
                } else if (strPATH_TYPE.equalsIgnoreCase("UNIX")) {
                    m_WIN_PATH = false;
                } else {
                    log.debug("PATH_TYPE must be WIN or UNIX.");
                    throw new UnsupportedOperationException("PATH_TYPE must be WIN or UNIX.");

                }

            }

        } catch (Exception e) {
            m_connectionURL = "Error in loadPropertiesFromStream" + e.getMessage();
            ok = false;
            log.debug(getStackTrace(e));
        }

        log.debug("End loadPropertiesFromStream");
        return ok;
    }

    /**
     *
     * @param Rval
     * @param Rpl
     * @return
     * @throws java.lang.Exception
     */
    public static float Round(float Rval, int Rpl)
            throws Exception {
        float p = (float) Math.pow(10, Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (float) tmp / p;
    } // End Round

    public ArrayList<ElevationFile> createPathSQL(int lon, int lat, TerrainDataType tdt)
            throws Exception {

        ArrayList<ElevationFile> elFiles = new ArrayList<ElevationFile>();

        ElevationFile elFile = new ElevationFile();

        log.debug("Begin createPathSQL");

        if (lon > 179) {
            throw new UnsupportedOperationException("Invalid Lon: Longitude must be less than 180");
        }

        if (lon < -180) {
            throw new UnsupportedOperationException("Invalid Lon: Longitude must be greater than or equal to -180");
        }

        if (lat > 89) {
            throw new UnsupportedOperationException("Invalid Lat: Latitude must be less than 90");
        }

        if (lat < -90) {
            throw new UnsupportedOperationException("Invalid Lat: Latitude must be greater than or equal to -90");
        }

        String lon_hemi = "e";
        if (lon < 0) {
            lon_hemi = "w";
        }

        String lat_hemi = "n";
        if (lat < 0) {
            lat_hemi = "s";
        }

        String strLat = String.valueOf(Math.abs(lat));
        if (Math.abs(lat) < 10) {
            strLat = "0" + strLat;
        }

        String strLon = String.valueOf(Math.abs(lon));
        if (Math.abs(lon) < 10) {
            strLon = "00" + strLon;
        } else if (Math.abs(lon) < 100) {
            strLon = "0" + strLon;
        }

        // Query the database to get the PATH_ROOT to the DTED
        if (!openConnection()) {
            log.debug("Failed to open database connection.  If problem persists contact the service provider");
            throw new UnsupportedOperationException("Failed to open database connection.  If problem persists contact the service provider");
        }

        ResultSet rset = null;


        try {
            String strSQL = "";

            // Get all if BEST
            //strSQL = "SELECT * FROM TERRAIN_DATA_FILES WHERE LAT = '";
            strSQL = "SELECT * FROM " + m_TERRAIN_DATA_FILES_TABLE + " WHERE LAT = '";
            strSQL += lat_hemi + strLat + "' AND LON = '";
            strSQL += lon_hemi + strLon + "'";

            if (tdt == TerrainDataType.BEST) {
                strSQL += " ORDER BY BEST";
            } else if (tdt == TerrainDataType.DTED_0) {
                strSQL += " AND TYP = 'DTED0'";
            } else if (tdt == TerrainDataType.DTED_1) {
                strSQL += " AND TYP = 'DTED1'";
            } else if (tdt == TerrainDataType.DTED_2) {
                strSQL += " AND TYP = 'DTED2'";
            } else if (tdt == TerrainDataType.SRTM_1) {
                strSQL += " AND TYP = 'SRTM1'";
            } else if (tdt == TerrainDataType.SRTM_2) {
                strSQL += " AND TYP = 'SRTM2'";
            } else if (tdt == TerrainDataType.SRTM_1_F) {
                strSQL += " AND TYP = 'SRTM1F'";
            } else if (tdt == TerrainDataType.SRTM_2_F) {
                strSQL += " AND TYP = 'SRTM2F'";
            }

            log.debug(strSQL);

            rset = m_stmt.executeQuery(strSQL);

            //System.out.println(strSQL);
            //System.out.println(rset.next());

            while (rset.next()) {
                elFile = new ElevationFile();
                if (m_WIN_PATH) {
                    elFile.setStrFilePath(rset.getString("WIN_PATH").trim());
                } else {
                    elFile.setStrFilePath(rset.getString("UNIX_PATH").trim());
                }
                elFile.setType(rset.getString("TYP").trim());
                String strMarking = rset.getString("MARKING");
                if (strMarking != null) {
                    strMarking = strMarking.trim();
                }
                elFile.setMarking(strMarking);

                elFiles.add(elFile);

            }

        } catch (SQLException e) {
            throw new UnsupportedOperationException("Database error. If problem persists contact the service provider: " + 
                    e.getMessage());
        } finally {
            try {
                rset.close();
            } catch (Exception e) {
                // ignore
            }
        }

        closeConnection();

        log.debug("End createPathSQL");


        return elFiles;
    }

    /**
     *
     * @param bbox1
     * @param bbox2
     * @return
     * @throws java.lang.Exception
     */
    public Bbox findBboxOverlap(Bbox bbox1, Bbox bbox2)
            throws Exception {

        Bbox result = new Bbox();

        log.debug("Begin findBboxOverlap");

        if (bbox2.getLllon() >= bbox1.getUrlon() ||
                bbox2.getLllat() >= bbox1.getUrlat() ||
                bbox2.getUrlon() <= bbox1.getLllon() ||
                bbox2.getUrlat() <= bbox1.getLllat()) {
            // no intersection
            result = null;

        } else {
            // The intersect
            result.setLllon(Math.max(bbox1.getLllon(), bbox2.getLllon()));
            result.setLllat(Math.max(bbox1.getLllat(), bbox2.getLllat()));
            result.setUrlon(Math.min(bbox1.getUrlon(), bbox2.getUrlon()));
            result.setUrlat(Math.min(bbox1.getUrlat(), bbox2.getUrlat()));
        }
        log.debug("End findBboxOverlap");
        return result;
    }

    /**
     * 
     * @param bbox
     * @param fullPathToFrame
     * @return
     * @throws java.lang.Exception
     */
    public MinMaxElevation findMinMaxElevationFrame(Bbox bbox, Polygon poly, String fullPathToFrame)
            throws Exception {
        log.debug("Begin MinMaxElevationFrame");




        MinMaxElevation minMaxElevation = new MinMaxElevation();

        log.debug("Open Frame");

        //System.out.println(fullPathToFrame);

        DTEDFrame frame = null;
        try {


            // Loads a DTED frame DTED0 are very quick to load DTED1 and DTED2 are slower
            frame = new DTEDFrame(fullPathToFrame);

            log.debug("Create OMG object");
            // This class provides some information about the frame
            OMGrid omg = frame.getOMGrid();

            log.debug("Find Frame corners");
            // find the ll and ur corners of the DTED Frame
            float lllon_cell = omg.getLongitude();
            float lllat_cell = omg.getLatitude();
            float urlon_cell = lllon_cell + 1.0f;
            float urlat_cell = lllat_cell + 1.0f;

            // Create a BboxBean from the frame corners
            Bbox bboxFrame = new Bbox();
            bboxFrame.setLllon(lllon_cell);
            bboxFrame.setLllat(lllat_cell);
            bboxFrame.setUrlon(urlon_cell);
            bboxFrame.setUrlat(urlat_cell);

            log.debug("BboxFrame = " + bboxFrame.getLllon() + "," + bboxFrame.getLllat() + "," + bboxFrame.getUrlon() + "," + bboxFrame.getUrlat());

            log.debug("Find overlap of bbox and frame");
            // If the bbox doesn't intersect the dted cell return error
            // or set the bbox to the overlap with the cell
            Bbox overlap = findBboxOverlap(bbox, bboxFrame);
            if (overlap == null) {
                // could just log this and move on
                throw new Exception("Bbox's don't overlap");
            }

            log.debug("overlap = " + overlap.getLllon() + "," + overlap.getLllat() + "," + overlap.getUrlon() + "," + overlap.getUrlat());

            // getElevations function uses upper left and lower right
            float ullat = (float) overlap.getUrlat();
            float ullon = (float) overlap.getLllon();
            float lrlat = (float) overlap.getLllat();
            float lrlon = (float) overlap.getUrlon();

            log.debug("upper left = " + ullon + "," + ullat);
            log.debug("lower right = " + lrlon + "," + lrlat);


            /*
            // Future may precalculate whole frame for lookup instead of scanning
            // Lookup would reduce the time for Level2 and Level1, but these
            // levels would still be slow compared to DTED0.

            //System.out.println("bbox: " + ullon + "," + ullat + "," + lrlon + "," + lrlat);
            if (1.0 - (lrlon-ullon)*(ullat-lrlat) < 0.0001){  // If Whole Frame
            //System.out.println("Whole Frame");

            // Look up minmax values from lookup table

            log.debug("Populate the return object");
            minMaxElevation.setMaxElevation(INVALID_MAX_ELEV);
            GeodeticCoordinate maxCP = new GeodeticCoordinate();
            maxCP.setLat(0.0f);
            maxCP.setLon(0.0f);
            minMaxElevation.setMaxCoordPair(maxCP);
            minMaxElevation.setMaxSource("");


            minMaxElevation.setMinElevation(INVALID_MIN_ELEV);
            GeodeticCoordinate minCP = new GeodeticCoordinate();
            minCP.setLat(0.0f);
            minCP.setLon(0.0f);
            minMaxElevation.setMinCoordPair(minCP);
            minMaxElevation.setMinSource("");



            } else {  // Not a whole farme
             *
             */
            // Search the frame

            log.debug("Get posts in the overlapping bbox");
            // Get the posts from the frame
            short el[][] = frame.getElevations(ullat, ullon, lrlat, lrlon);

            // Find the number of degrees per pixel
            float lat_per_pixel = omg.getVerticalResolution();
            float lon_per_pixel = omg.getHorizontalResolution();

            // The points are pixels relative to the lower left corner of the overlap
            float lllat = (float) overlap.getLllat();
            float lllon = (float) overlap.getLllon();

            // Don't think this is needed anymore.   Try to remove this code
            if (lllat < omg.getLatitude()) {
                lllat = omg.getLatitude();
            }
            if (lllon < omg.getLongitude()) {
                lllon = omg.getLongitude();
            }
            // Try to remove the code above


            log.debug("Find the min and max posts");
            // Set both min and max to point one


            int max_i = 0;
            int max_j = 0;
            //int max = el[max_i][max_j];
            int max = INVALID_MAX_ELEV;

            int min_i = 0;
            int min_j = 0;
            //int min = el[min_i][min_j];
            int min = INVALID_MIN_ELEV;

            // Iterate and update min and max when found
            int i = 0;
            int j = el[0].length - 1;

            //WKTReader rdr = new WKTReader();
            log.debug("el.length = " + el.length);
            log.debug("el[0].length = " + el[0].length);


            //long cntr = 0;
            //long time_elapsed = 0;

            while (j >= 0) {
                i = 0;
                while (i < el.length) {
                    //cntr++;
                    boolean okPoint = true;


                    double lat = 0.0;
                    double lon = 0.0;
                    if (poly != null) {

                        okPoint = false;
                        // Check to see if i,j is in polygon
                        lat = lllat + j * lat_per_pixel;
                        lon = lllon + i * lon_per_pixel;
                        //String strPt = "POINT (" + String.valueOf(lon) + " " + String.valueOf(lat) + ")";
                        //Point pt = (Point) (rdr.read(strPt));
                        Coordinate c = new Coordinate(lon, lat);
                        //long st = System.currentTimeMillis();

                        okPoint = SimplePointInAreaLocator.containsPointInPolygon(c, poly);

                        //time_elapsed += System.currentTimeMillis() - st;


                    }
                    if (okPoint) {
                        //log.debug("Point OK : " + el[i][j]);
                        //log.debug("max = " + max);

                        if (el[i][j] > max) {
                            //log.debug("MAX: " + String.valueOf(lon) + "," + String.valueOf(lat));
                            max = el[i][j];
                            max_i = i;
                            max_j = j;
                        }
                        if (el[i][j] < min && el[i][j] > -32767) {
                            // -32767 indicates no data
                            min = el[i][j];
                            min_i = i;
                            min_j = j;
                        }
                    }
                    i++;
                }
                j--;
            }

            //System.out.println("cntr = " + cntr);
            //System.out.println("time_elapsed = " + time_elapsed);

            //System.out.println("Vertical Resolution: " + omg.getVerticalResolution());
            //System.out.println("Horizontal Resolution: " + omg.getHorizontalResolution());

            log.debug("Find coorindates for the min and max posts");

            log.debug("Round off the results");
            // Round off the results 30 arcseconds or 900 meters; about 3 decimal points
            double min_lat = lllat + min_j * lat_per_pixel;
            double min_lon = lllon + min_i * lon_per_pixel;

            double max_lat = lllat + max_j * lat_per_pixel;
            double max_lon = lllon + max_i * lon_per_pixel;

            log.debug("Populate the return object");
            minMaxElevation.setMaxElevation((short) max);
            GeodeticCoordinate maxCP = new GeodeticCoordinate();
            maxCP.setLat(max_lat);
            maxCP.setLon(max_lon);
            minMaxElevation.setMaxCoordPair(maxCP);
            minMaxElevation.setMaxSource("");


            minMaxElevation.setMinElevation((short) min);
            GeodeticCoordinate minCP = new GeodeticCoordinate();
            minCP.setLat(min_lat);
            minCP.setLon(min_lon);
            minMaxElevation.setMinCoordPair(minCP);
            minMaxElevation.setMinSource("");

            log.debug("Max = " + max);
            log.debug("Min = " + min);
            //}  // End if Whole Frame




        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            frame.close(true);
        }



        log.debug("End MinMaxElevation");

        return minMaxElevation;
    }

    GetElevationAtResponse getElevationAt(GetElevationAt parameters)
            throws Exception {

        log.debug("Begin getElevationAt");

        InputStream pis = getClass().getResourceAsStream(STR_CONFIG_FILE);

        boolean blnOK = loadPropertiesFromStream(pis);

        if (!blnOK) {
            // failed to load properties
            log.error("Failed to load properties");
            throw new UnsupportedOperationException("Failed to load properties file. If problem persists contact serivce provider.");
        }


        GetElevationAtResponse result = new GetElevationAtResponse();


        log.debug("Check Parameters");

        if (parameters == null) {
            throw new UnsupportedOperationException("You must specify parameters: See WSDL");
        }

        log.debug("Check HeightType");

        HeightUnitType ht = HeightUnitType.METERS;
        // Default to Meters
        if (parameters.getHeightType() != null) {
            ht = parameters.getHeightType();
        }


        log.debug("Check TerrainDataType");

        TerrainDataType tdt = TerrainDataType.DTED_0;
        // default to DTED_0
        if (parameters.getTerrainDataType() != null) {
            tdt = parameters.getTerrainDataType();

        }

        log.debug("Check Pts");

        if (parameters.getPts() == null) {
            throw new UnsupportedOperationException("You must specify at least one point in pts: See WSDL");
        }

        if (parameters.getPts().size() > m_MAX_NUM_POINTS_AT) {
            throw new UnsupportedOperationException("You specified " + parameters.getPts().size() +
                    " points.  The maximum number of points allowed is " + m_MAX_NUM_POINTS_AT);
        }


        Iterator<GeodeticCoordinateBean> it = parameters.getPts().iterator();

        log.debug("Check At Least One Pt");

        if (!it.hasNext()) {
            throw new UnsupportedOperationException("You must specify at least one point in pts: See WSDL");
        }

        log.debug("Iterate through Pts");

        String strOverallMarking = "";

        while (it.hasNext()) {
            GeodeticCoordinateBean bn = it.next();
            GeodeticCoordinate cp = new GeodeticCoordinate(bn);


            log.debug("Processing: " + bn.getLon() + "," + bn.getLat());
            log.debug("Parsed: " + String.valueOf(cp.getLat()) + String.valueOf(cp.getLon()));

            double lat = cp.getLat();
            double lon = cp.getLon();

            // Decide which frame to open (Spatial Query or just use floor and ceil
            int cell_lat_floor = (int) Math.round(Math.floor(lat));
            int cell_lon_floor = (int) Math.round(Math.floor(lon));

            ArrayList<ElevationFile> elFiles = createPathSQL(cell_lon_floor, cell_lat_floor, tdt);

            Iterator<ElevationFile> itElFiles = elFiles.iterator();

            ElevationFile elFile = null;
            if (itElFiles.hasNext()) {
                elFile = itElFiles.next();
            }


            ElevationBean resultBn = new ElevationBean();

            String strPATH = "";
            String strTerrainType = "NO DATA";
            String strMarking = null;
            int elevation = INVALID_MAX_ELEV;


            while (elFile != null && elevation == INVALID_MAX_ELEV) {


                strPATH = elFile.getStrFilePath();

                strTerrainType = elFile.getType();

                log.debug("strPATH " + strPATH);

                //System.out.println(strPATH);

                boolean exists = (new File(strPATH)).exists();

                log.debug(exists);

                //System.out.println(i + ", " + j + ": " + exists);
                if (exists) {


                    DTEDFrame frame = null;
                    try {

                        frame = new DTEDFrame(strPATH);

                        elevation = frame.elevationAt((float) lat, (float) lon);

                        strMarking = elFile.getMarking();

                    } catch (Exception e) {
                        throw new UnsupportedOperationException(e.getMessage());
                    } finally {
                        frame.close(true);
                    }
                } else {
                    strTerrainType = "NO DATA";
                    log.error("TERRAIN FILE MISSING: " + strPATH);
                    elevation = INVALID_MAX_ELEV;
                    strMarking = null;
                }

                if (elevation <= INVALID_MAX_ELEV) {
                    strTerrainType = "NO DATA";
                    elevation = INVALID_MAX_ELEV;
                    strMarking = null;
                }

                if (elevation == INVALID_MAX_ELEV && itElFiles.hasNext()) {
                    
                    elFile = itElFiles.next();
                } else {
                    elFile = null;
                }


                //System.out.println(elevation);

            }

            if (elevation <= INVALID_MAX_ELEV) {
                strTerrainType = "NO DATA";
                elevation = INVALID_MAX_ELEV;
                strMarking = null;
            }


            if (strMarking != null) {
                resultBn.setMarking(strMarking);
                if (strMarking.trim().equalsIgnoreCase("LIMDIS")) {
                    // if any of the cells is LIMDIS set overall LIMDIS
                    strOverallMarking = "LIMDIS";
                }
            }

            resultBn.setSource(strTerrainType);

            log.debug("Elevation: " + elevation);

            if (elevation != INVALID_MAX_ELEV) {
                if (ht == HeightUnitType.FEET) {
                    double el_feet = (double) elevation * METERS_TO_FEET;
                    elevation = (int) Math.round(el_feet);
                }
            }

            log.debug("Add Bean to Result");

            resultBn.setElevation(String.valueOf(elevation).trim());
            resultBn.setCoordinates(bn);

            result.getElevations().add(resultBn);


        }

        log.debug("Add HeightType and Security to Results");

        result.setHeightType(ht);

        SecurityElement sec = new SecurityElement();
        sec.setClassification(ClassificationType.U);
        sec.getOwnerProducer().add("USA");

        if (strOverallMarking.equalsIgnoreCase("LIMDIS")) {
            sec.getNonICmarkings().add("LIMDIS");
        }

        result.setSecurity(sec);

        log.debug("End getElevationAt");

        return result;
    }

    GetMinMaxElevationsResponse getMinMaxElevations(GetMinMaxElevations parameters)
            throws Exception {

        log.debug("Being getMinMaxElevations");

        InputStream pis = getClass().getResourceAsStream(STR_CONFIG_FILE);

        boolean blnOK = loadPropertiesFromStream(pis);

        if (!blnOK) {
            // failed to load properties
            log.error("Failed to load properties");
            throw new UnsupportedOperationException("Failed to load properties file. If problem persists contact serivce provider.");
        }


        GetMinMaxElevationsResponse result = new GetMinMaxElevationsResponse();

        MinMaxElevation overallMinMax = new MinMaxElevation();


        log.debug("Check parameters");
        if (parameters == null) {
            throw new UnsupportedOperationException("You must specify parameters: See WSDL");
        }

        BboxBean bboxBn = parameters.getBbox();

        if (bboxBn == null) {
            throw new UnsupportedOperationException("You must specify a bbox: See WSDL");
        }

        Bbox bbox = new Bbox(bboxBn);

        HeightUnitType ht = HeightUnitType.METERS;
        if (parameters.getHeightType() != null) {
            ht = parameters.getHeightType();
        }

        log.debug("Check TerrainDataType");

        TerrainDataType tdt = TerrainDataType.DTED_0;
        // default to DTED_0
        if (parameters.getTerrainDataType() != null) {
            tdt = parameters.getTerrainDataType();
        }

        float max_bbox_sq_deq = 0.0f;
        // Set the max_bbox parameter based on the TerrainDataType
        if (tdt == TerrainDataType.BEST || tdt == TerrainDataType.SRTM_2 || tdt == TerrainDataType.SRTM_2_F) {
            max_bbox_sq_deq = m_MAX_BBOX_SQ_DEG_LVL2;
        } else if (tdt == TerrainDataType.SRTM_1 || tdt == TerrainDataType.SRTM_1_F) {
            max_bbox_sq_deq = m_MAX_BBOX_SQ_DEG_LVL1;
        } else {
            max_bbox_sq_deq = m_MAX_BBOX_SQ_DEG_LVL0;
        }

        double max_lat_lon_diff = Math.sqrt(max_bbox_sq_deq);
        //System.out.println(max_lat_lon_diff);

        // reject invalid bbox
        if (bbox.getUrlon() <= bbox.getLllon()) {
            throw new UnsupportedOperationException("Invalid BBox: Upper Right Lon must be greater than Lower Right Lon");
        }
        if (bbox.getUrlat() <= bbox.getLllat()) {
            throw new UnsupportedOperationException("Invalid BBox: Upper Right Lat must be greater than Lower Right Lat");
        }

        log.debug("Check size of the bbox");
        // reject bboxes greater than some configurable maximum
        double lat_diff = bbox.getUrlat() - bbox.getLllat();
        double lon_diff = bbox.getUrlon() - bbox.getLllon();

        //System.out.println(lat_diff);

        if (lat_diff * lon_diff > max_bbox_sq_deq) {
            throw new UnsupportedOperationException("Invalid BBox: The bbox exceeds maximum.  The maximum square decimal degrees for " + tdt.toString() + " is " + max_bbox_sq_deq);

        }

        if (lat_diff > max_lat_lon_diff) {
            throw new UnsupportedOperationException("Invalid BBox: The bbox exceeds maximum latitude difference for " + tdt.toString() + ": " + max_lat_lon_diff);
        }

        if (lon_diff > max_lat_lon_diff) {
            throw new UnsupportedOperationException("Invalid BBox: The bbox exceeds maximum longitude difference for " + tdt.toString() + ": " + max_lat_lon_diff);
        }

        if (lat_diff < m_MIN_LAT_LON_DIFF) {
            throw new UnsupportedOperationException("Invalid BBox: The bbox latitude difference must be greater than " + m_MIN_LAT_LON_DIFF);
        }

        if (lon_diff < m_MIN_LAT_LON_DIFF) {
            throw new UnsupportedOperationException("Invalid BBox: The bbox longitude difference must be greater than " + m_MIN_LAT_LON_DIFF);
        }


        log.debug("Find the intersecting frames");
        // determine what frames intersect the bbox specified
        int cell_lat_floor = Math.round((float) Math.floor(bbox.getLllat()));
        int cell_lat_ceil = Math.round((float) Math.ceil(bbox.getUrlat()));

        int cell_lon_floor = Math.round((float) Math.floor(bbox.getLllon()));
        int cell_lon_ceil = Math.round((float) Math.ceil(bbox.getUrlon()));

        overallMinMax.setMinElevation(INVALID_MIN_ELEV);
        overallMinMax.setMaxElevation(INVALID_MAX_ELEV);

        String strOverallMarking = "";

        log.debug("Loop through the frames");
        int i = cell_lat_floor;
        while (i < cell_lat_ceil) {
            int j = cell_lon_floor;
            while (j < cell_lon_ceil) {
                log.debug("Processing frame (lon,lat): " + String.valueOf(j) + "," + String.valueOf(i));

                String strPATH = "";
                String strTerrainType = "";
                String strMarking = "";


                ArrayList<ElevationFile> elFiles = createPathSQL(j, i, tdt);

                Iterator<ElevationFile> itElFiles = elFiles.iterator();

                ElevationFile elFile = null;
                if (itElFiles.hasNext()) {
                    elFile = itElFiles.next();
                }

                if (elFile != null) {

                    strPATH = elFile.getStrFilePath();
                    strTerrainType = elFile.getType();

                    boolean exists = (new File(strPATH)).exists();
                    //System.out.println(i + ", " + j + ": " + exists);
                    if (exists) {

                        MinMaxElevation thisMinMax = new MinMaxElevation();

                        strMarking = elFile.getMarking();


                        try {
                            thisMinMax = findMinMaxElevationFrame(bbox, null, strPATH);
                            if (thisMinMax.getMaxElevation() > overallMinMax.getMaxElevation()) {
                                overallMinMax.setMaxMarking(strMarking);
                                overallMinMax.setMaxElevation(thisMinMax.getMaxElevation());
                                overallMinMax.setMaxCoordPair(thisMinMax.getMaxCoordPair());
                                overallMinMax.setMaxSource(strTerrainType);
                                if (strMarking != null) {
                                    // if a point is used update LIMDIS is needed
                                    if (strMarking.trim().equalsIgnoreCase("LIMDIS")) {
                                        // set Overall to LIMDIS if the file is LIMDIS
                                        strOverallMarking = "LIMDIS";
                                    }
                                }


                            }
                            if (thisMinMax.getMinElevation() < overallMinMax.getMinElevation()) {
                                overallMinMax.setMinMarking(strMarking);
                                overallMinMax.setMinElevation(thisMinMax.getMinElevation());
                                overallMinMax.setMinCoordPair(thisMinMax.getMinCoordPair());
                                overallMinMax.setMinSource(strTerrainType);
                                if (strMarking != null) {
                                    // if a point is used update LIMDIS is needed
                                    if (strMarking.trim().equalsIgnoreCase("LIMDIS")) {
                                        // set Overall to LIMDIS if the file is LIMDIS
                                        strOverallMarking = "LIMDIS";
                                    }
                                }
                            }

                        } catch (Exception e) {
                            // log the exception and move on
                            log.error(getStackTrace(e));
                        }
                    } else {
                        // File doesn't exist
                        log.error("TERRAIN FILE MISSING: " + strPATH);
                    }


                } else {
                    strTerrainType = "NO DATA";
                    log.debug("NO DATA FOUND FOR CELL : " + String.valueOf(j) + "," + String.valueOf(i));

                }

                j++;
            }
            i++;
        }

        log.debug("Populate result object");
        GeodeticCoordinate gc = new GeodeticCoordinate();
        if (overallMinMax.getMaxElevation() == INVALID_MAX_ELEV) {
            // Nothing found over the entire BBOX
            overallMinMax.setMaxSource("NO DATA");
            overallMinMax.setMaxCoordPair(new GeodeticCoordinate());
            overallMinMax.setMaxMarking(null);
        } else {
            // move pixel position returned to the bbox cell if needed
            double lat;
            double lon;

            double max_lat = bbox.getUrlat();
            double max_lon = bbox.getUrlon();

            double min_lat = bbox.getLllat();
            double min_lon = bbox.getLllon();

            gc = overallMinMax.getMaxCoordPair();
            lat = gc.getLat();
            lon = gc.getLon();
            // move coord to edge of bbox if needed.
            if (lat > max_lat) {
                gc.setLat(max_lat);
                log.debug("adjusted max el lat to max");
            }
            if (lat < min_lat) {
                gc.setLat(min_lat);
                log.debug("adjusted max el lat to min");
            }
            if (lon > max_lon) {
                gc.setLon(max_lon);
                log.debug("adjusted max el  lon to max");
            }
            if (lon < min_lon) {
                gc.setLon(min_lon);
                log.debug("adjusted max el lon to min");
            }
            overallMinMax.setMaxCoordPair(gc);
            // OK populate the return
            if (ht == HeightUnitType.FEET) {

                double el_feet = (double) overallMinMax.getMaxElevation() * METERS_TO_FEET;
                overallMinMax.setMaxElevation((short) Math.round(el_feet));

            }

        }

        if (overallMinMax.getMinElevation() == INVALID_MIN_ELEV) {


            // Nothing found over the entire BBOX
            overallMinMax.setMinSource("NO DATA");
            overallMinMax.setMinCoordPair(new GeodeticCoordinate());
            overallMinMax.setMinMarking(null);
        } else {
            // move pixel position returned to the bbox cell if needed
            double lat;
            double lon;

            double max_lat = bbox.getUrlat();
            double max_lon = bbox.getUrlon();

            double min_lat = bbox.getLllat();
            double min_lon = bbox.getLllon();

            // move coord to edge of bbox if needed.
            gc = overallMinMax.getMinCoordPair();
            lat = gc.getLat();
            lon = gc.getLon();
            if (lat > max_lat) {
                gc.setLat(max_lat);
                log.debug("adjusted min el lat to max");
            }
            if (lat < min_lat) {
                gc.setLat(min_lat);
                log.debug("adjusted min el lat to min");
            }
            if (lon > max_lon) {
                gc.setLon(max_lon);
                log.debug("adjusted min el  lon to max");
            }
            if (lon < min_lon) {
                gc.setLon(min_lon);
                log.debug("adjusted min el lon to min");
            }
            overallMinMax.setMinCoordPair(gc);

            // OK populate the return
            if (ht == HeightUnitType.FEET) {

                double el_feet = (double) overallMinMax.getMinElevation() * METERS_TO_FEET;
                overallMinMax.setMinElevation((short) Math.round(el_feet));
            }
        }


        ElevationBean maxElBean = new ElevationBean();
        maxElBean = overallMinMax.getMaxElevationBean();
        maxElBean.setSource(overallMinMax.getMaxSource());

        ElevationBean minElBean = new ElevationBean();
        minElBean = overallMinMax.getMinElevationBean();
        minElBean.setSource(overallMinMax.getMinSource());

        result.setMaxElevation(maxElBean);
        result.setMinElevation(minElBean);

        result.setHeightType(ht);

        SecurityElement sec = new SecurityElement();
        sec.setClassification(ClassificationType.U);
        sec.getOwnerProducer().add("USA");

        if (strOverallMarking.equalsIgnoreCase("LIMDIS")) {
            sec.getNonICmarkings().add("LIMDIS");
        }

        result.setSecurity(sec);

        log.debug("End getMinMaxElevations");

        return result;
    }

    GetMinMaxElevationsWKTResponse getMinMaxElevationsWKT(GetMinMaxElevationsWKT parameters)
            throws Exception {

        log.debug("Being getMinMaxElevationsWKT");

        InputStream pis = getClass().getResourceAsStream(STR_CONFIG_FILE);

        boolean blnOK = loadPropertiesFromStream(pis);

        if (!blnOK) {
            // failed to load properties
            log.error("Failed to load properties");
            throw new UnsupportedOperationException("Failed to load properties file. If problem persists contact serivce provider.");
        }


        GetMinMaxElevationsWKTResponse result = new GetMinMaxElevationsWKTResponse();

        MinMaxElevation overallMinMax = new MinMaxElevation();


        log.debug("Check parameters");
        if (parameters == null) {
            throw new UnsupportedOperationException("You must specify parameters: See WSDL");
        }

        String strWKT = parameters.getWkt();

        if (strWKT == null) {
            throw new UnsupportedOperationException("You must specify a WKT: See WSDL");
        }

        // Check the WKT
        WKTReader rdr = new WKTReader();

        Polygon poly = null;

        try {
            poly = (Polygon) (rdr.read(strWKT));

            if (!poly.isValid()) {
                throw new UnsupportedOperationException("Invalid WKT Polygon: " + strWKT);
            }

        } catch (Exception e) {
            log.debug(e.getStackTrace());
            throw new UnsupportedOperationException(e.getMessage());
        }

        //System.out.println("NumPoints = " + poly.getNumPoints());
        //System.out.println("Area = " + poly.getArea());
        if (poly.getNumPoints() > m_MAX_NUM_POINTS_PLY) {
            throw new UnsupportedOperationException("Invalid Polygon: To Many Points (" + poly.getNumPoints() + ")The WKT Polygon must have no more than " + m_MAX_NUM_POINTS_PLY + " points.");
        }


        Envelope env = poly.getEnvelopeInternal();

        Bbox bbox = new Bbox();
        bbox.setLllat(env.getMinY());
        bbox.setLllon(env.getMinX());
        bbox.setUrlat(env.getMaxY());
        bbox.setUrlon(env.getMaxX());


        HeightUnitType ht = HeightUnitType.METERS;
        if (parameters.getHeightType() != null) {
            ht = parameters.getHeightType();
        }

        log.debug("Check TerrainDataType");

        TerrainDataType tdt = TerrainDataType.DTED_0;
        // default to DTED_0
        if (parameters.getTerrainDataType() != null) {
            tdt = parameters.getTerrainDataType();
        }

        float max_bbox_sq_deq = 0.0f;
        // Set the max_bbox parameter based on the TerrainDataType
        if (tdt == TerrainDataType.BEST || tdt == TerrainDataType.SRTM_2 || tdt == TerrainDataType.SRTM_2_F) {
            max_bbox_sq_deq = m_MAX_BBOX_SQ_DEG_LVL2;
        } else if (tdt == TerrainDataType.SRTM_1 || tdt == TerrainDataType.SRTM_1_F) {
            max_bbox_sq_deq = m_MAX_BBOX_SQ_DEG_LVL1;
        } else {
            max_bbox_sq_deq = m_MAX_BBOX_SQ_DEG_LVL0;
        }

        double max_lat_lon_diff = Math.sqrt(max_bbox_sq_deq);
        //System.out.println(max_lat_lon_diff);


        log.debug("Check size of the bbox");
        // reject bboxes greater than some configurable maximum
        double lat_diff = bbox.getUrlat() - bbox.getLllat();
        double lon_diff = bbox.getUrlon() - bbox.getLllon();

        //System.out.println(lat_diff);
        double area = poly.getArea();

        if (area > max_bbox_sq_deq) {
            throw new UnsupportedOperationException("Invalid Polygon: Polygon area " + area + " exceeds the maximum square decimal degrees for " + tdt.toString() + " is " + max_bbox_sq_deq);
        }

        if (lat_diff > max_lat_lon_diff) {
            throw new UnsupportedOperationException("Invalid Polygon: The envelope exceeds maximum latitude difference for " + tdt.toString() + ": " + max_lat_lon_diff);
        }

        if (lon_diff > max_lat_lon_diff) {
            throw new UnsupportedOperationException("Invalid Polygon: The envelope exceeds maximum longitude difference for " + tdt.toString() + ": " + max_lat_lon_diff);
        }

        if (lat_diff < m_MIN_LAT_LON_DIFF) {
            throw new UnsupportedOperationException("Invalid Polygon: The envelope latitude difference must be greater than " + m_MIN_LAT_LON_DIFF);
        }

        if (lon_diff < m_MIN_LAT_LON_DIFF) {
            throw new UnsupportedOperationException("Invalid Polygon: The envelope longitude difference must be greater than " + m_MIN_LAT_LON_DIFF);
        }


        log.debug("Find the intersecting frames");
        // determine what frames intersect the bbox specified
        int cell_lat_floor = Math.round((float) Math.floor(bbox.getLllat()));
        int cell_lat_ceil = Math.round((float) Math.ceil(bbox.getUrlat()));

        int cell_lon_floor = Math.round((float) Math.floor(bbox.getLllon()));
        int cell_lon_ceil = Math.round((float) Math.ceil(bbox.getUrlon()));

        overallMinMax.setMinElevation(INVALID_MIN_ELEV);
        overallMinMax.setMaxElevation(INVALID_MAX_ELEV);

        String strOverallMarking = "";

        log.debug("Loop through the frames");
        int i = cell_lat_floor;
        while (i < cell_lat_ceil) {
            int j = cell_lon_floor;
            while (j < cell_lon_ceil) {
                log.debug("Processing frame (lon,lat): " + String.valueOf(j) + "," + String.valueOf(i));

                String strPATH = "";
                String strTerrainType = "";
                String strMarking = "";

                ArrayList<ElevationFile> elFiles = createPathSQL(j, i, tdt);

                Iterator<ElevationFile> itElFiles = elFiles.iterator();

                ElevationFile elFile = null;
                if (itElFiles.hasNext()) {
                    elFile = itElFiles.next();
                }


                if (elFile != null) {

                    strPATH = elFile.getStrFilePath();
                    strTerrainType = elFile.getType();

                    boolean exists = (new File(strPATH)).exists();
                    //System.out.println(i + ", " + j + ": " + exists);
                    if (exists) {

                        MinMaxElevation thisMinMax = new MinMaxElevation();

                        strMarking = elFile.getMarking();

                        // Trim the poly by the frame
                        String strPolyFrame = "POLYGON ((" +
                                String.valueOf(j) + " " + String.valueOf(i) + "," +
                                String.valueOf(j + 1) + " " + String.valueOf(i) + "," +
                                String.valueOf(j + 1) + " " + String.valueOf(i + 1) + "," +
                                String.valueOf(j) + " " + String.valueOf(i + 1) + "," +
                                String.valueOf(j) + " " + String.valueOf(i) + "))";
                        Polygon polyFrame = (Polygon) (rdr.read(strPolyFrame));

                        Polygon polyIntersect = null;

                        try {
                            polyIntersect = (Polygon) poly.intersection(polyFrame);
                            //System.out.println(polyIntersect.toString());
                        } catch (Exception e) {
                            // They don't intersect
                            polyIntersect = null;
                        }


                        try {
                            if (polyIntersect != null) {
                                thisMinMax = findMinMaxElevationFrame(bbox, poly, strPATH);
                                if (thisMinMax.getMaxElevation() > overallMinMax.getMaxElevation()) {
                                    overallMinMax.setMaxMarking(strMarking);
                                    overallMinMax.setMaxElevation(thisMinMax.getMaxElevation());
                                    overallMinMax.setMaxCoordPair(thisMinMax.getMaxCoordPair());
                                    overallMinMax.setMaxSource(strTerrainType);
                                    if (strMarking != null) {
                                        // if a point is used update LIMDIS is needed
                                        if (strMarking.trim().equalsIgnoreCase("LIMDIS")) {
                                            // set Overall to LIMDIS if the file is LIMDIS
                                            strOverallMarking = "LIMDIS";
                                        }
                                    }
                                }
                                if (thisMinMax.getMinElevation() < overallMinMax.getMinElevation()) {
                                    overallMinMax.setMinMarking(strMarking);
                                    overallMinMax.setMinElevation(thisMinMax.getMinElevation());
                                    overallMinMax.setMinCoordPair(thisMinMax.getMinCoordPair());
                                    overallMinMax.setMinSource(strTerrainType);
                                    if (strMarking != null) {
                                        // if a point is used update LIMDIS is needed
                                        if (strMarking.trim().equalsIgnoreCase("LIMDIS")) {
                                            // set Overall to LIMDIS if the file is LIMDIS
                                            strOverallMarking = "LIMDIS";
                                        }
                                    }
                                }
                            }

                        } catch (Exception e) {
                            // log the exception and move on
                            log.error(getStackTrace(e));
                        }
                    } else {
                        // File doesn't exist
                        log.error("TERRAIN FILE MISSING: " + strPATH);
                    }


                } else {
                    strTerrainType = "NO DATA";
                    log.debug("NO DATA FOUND FOR CELL : " + String.valueOf(j) + "," + String.valueOf(i));

                }

                j++;
            }
            i++;
        }

        log.debug("Populate result object");
        if (overallMinMax.getMaxElevation() == INVALID_MAX_ELEV) {
            // Nothing found over the entire BBOX
            overallMinMax.setMaxSource("NO DATA");
        } else {

            // OK populate the return
            if (ht == HeightUnitType.FEET) {
                double el_feet = (double) overallMinMax.getMaxElevation() * METERS_TO_FEET;
                overallMinMax.setMaxElevation((short) Math.round(el_feet));
            }

        }

        if (overallMinMax.getMinElevation() == INVALID_MIN_ELEV) {
            // Nothing found over the entire BBOX
            overallMinMax.setMinSource("NO DATA");
        } else {
            // OK populate the return
            if (ht == HeightUnitType.FEET) {
                double el_feet = (double) overallMinMax.getMinElevation() * METERS_TO_FEET;
                overallMinMax.setMinElevation((short) Math.round(el_feet));
            }
        }

        // Populate Results
        ElevationBean maxElBean = new ElevationBean();
        maxElBean = overallMinMax.getMaxElevationBean();
        maxElBean.setSource(overallMinMax.getMaxSource());

        ElevationBean minElBean = new ElevationBean();
        minElBean = overallMinMax.getMinElevationBean();
        minElBean.setSource(overallMinMax.getMinSource());

        result.setMaxElevation(maxElBean);
        result.setMinElevation(minElBean);

        result.setHeightType(ht);

        SecurityElement sec = new SecurityElement();
        sec.setClassification(ClassificationType.U);
        sec.getOwnerProducer().add("USA");

        if (strOverallMarking.equalsIgnoreCase("LIMDIS")) {
            sec.getNonICmarkings().add("LIMDIS");
        }

        result.setSecurity(sec);

        log.debug("End getMinMaxElevations");

        return result;
    }

    private void testGetElevationAt() {

        try {



            // Test getElevationAt
            GetElevationAt param1 = new GetElevationAt();

            GeodeticCoordinateBean pt = new GeodeticCoordinateBean();

            pt.setLat("36:35:48N");
            pt.setLon("036:54:31E");
            param1.getPts().add(pt);

            /*
            pt = new GeodeticCoordinateBean();
            pt.setLat("-5.16");
            pt.setLon("23.183");
            param1.getPts().add(pt);
             */

            param1.setTerrainDataType(TerrainDataType.DTED_2);

            GetElevationAtResponse result1 = getElevationAt(param1);



            Iterator<ElevationBean> it1 = result1.getElevations().iterator();

            int i = 1;
            while (it1.hasNext()) {
                ElevationBean elBn = it1.next();
                System.out.println(i);
                System.out.println("Elevation: " + elBn.getElevation());
                System.out.println("Source: " + elBn.getSource());
                System.out.println("-----");
                i++;

            }


        } catch (Exception e) {
            log.error(getStackTrace(e));
        }

    }

    private void testGetMinMaxElevations1() {
        try {




            // Test getMinMaxElevations
            BboxBean bbox = new BboxBean();

            //-92.31009006500244,38.87439250946045,-92.29567050933838,38.88640880584717
            bbox.setLllon("-92.31009006500244");
            bbox.setLllat("38.87439250946045");

            bbox.setUrlon("-92.29567050933838");
            bbox.setUrlat("38.88640880584717");


            GetMinMaxElevations param2 = new GetMinMaxElevations();
            param2.setBbox(bbox);
            param2.setTerrainDataType(TerrainDataType.DTED_0);

            GetMinMaxElevationsResponse result2 = getMinMaxElevations(param2);

            ElevationBean maxBn = new ElevationBean();
            maxBn = result2.getMaxElevation();

            ElevationBean minBn = new ElevationBean();
            minBn = result2.getMinElevation();


            System.out.println("");

            if (maxBn == null) {
                System.out.println("No DTED0 elevation data found for specified BBOX");
            } else {
                System.out.println("Max = " + maxBn.getElevation());
                System.out.println("At: " + maxBn.getCoordinates().getLon() + ", " +
                        maxBn.getCoordinates().getLat());
                System.out.println("Source: " + maxBn.getSource());
                System.out.println();

                System.out.println("Min = " + minBn.getElevation());
                System.out.println("At: " + minBn.getCoordinates().getLon() + ", " +
                        minBn.getCoordinates().getLat());
                System.out.println("Source: " + minBn.getSource());

            }


        } catch (Exception e) {
            log.error(getStackTrace(e));
        }

    }

    private void testGetMinMaxElevations2() {
        try {


            // Test getMinMaxElevationsWKT
            String strPoly = "";

            // Polygon with a whole
            strPoly = "POLYGON (" +
                    "(7.5 7.5, 10.5 10.5, 10.5 10.5, 7.5 10.5, 7.5 7.5)" +
                    ",(7.8 7.8, 7.8 8.2, 8.2 8.2, 8.2 7.8, 7.8 7.8)" +
                    ")";

            // Polygon with 81 points circle
            WKTReader wkr = new WKTReader();
            Point center = (Point) wkr.read("POINT(10 10)");
            Polygon ply = (Polygon) center.buffer(1.5, 45);
            strPoly = ply.toString();

            // largest rectangle SRTM2
            strPoly = "POLYGON ((-107.2 36.8, -106.7 36.8, " +
                    "-106.7 37.1, -107.2 36.8))";


            strPoly = "POLYGON ((-92.3000693321228 38.88235330581665,-92.29843854904175 38.88235330581665,-92.29775190353393 38.88183832168579,-92.2965931892395 38.879992961883545,-92.29779481887817 38.87836217880249,-92.3006272315979 38.879992961883545,-92.3000693321228 38.88235330581665))";

            strPoly = "POLYGON ((-105.8426284790039 39.555931091308594,-105.74787139892578 39.59369659423828,-105.67577362060547 39.572410583496094,-105.7101058959961 39.45018768310547,-105.82752227783203 39.457054138183594,-105.8426284790039 39.555931091308594))";

            GetMinMaxElevationsWKT param3 = new GetMinMaxElevationsWKT();
            param3.setWkt(strPoly);
            param3.setTerrainDataType(TerrainDataType.SRTM_2);

            GetMinMaxElevationsWKTResponse result3 = getMinMaxElevationsWKT(param3);

            ElevationBean maxBn3 = new ElevationBean();
            maxBn3 = result3.getMaxElevation();

            ElevationBean minBn3 = new ElevationBean();
            minBn3 = result3.getMinElevation();


            System.out.println("");

            if (maxBn3 == null) {
                System.out.println("No DTED0 elevation data found for specified BBOX");
            } else {
                System.out.println("Max = " + maxBn3.getElevation());
                System.out.println("At: " + maxBn3.getCoordinates().getLon() + ", " +
                        maxBn3.getCoordinates().getLat());
                System.out.println("Source: " + maxBn3.getSource());
                System.out.println();

                System.out.println("Min = " + minBn3.getElevation());
                System.out.println("At: " + minBn3.getCoordinates().getLon() + ", " +
                        minBn3.getCoordinates().getLat());
                System.out.println("Source: " + minBn3.getSource());

            }


        } catch (Exception e) {
            log.error(getStackTrace(e));
        }

    }

    public static void main(String args[]) {

        try {
            ElevationQuery t = new ElevationQuery();
            t.testGetElevationAt();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
