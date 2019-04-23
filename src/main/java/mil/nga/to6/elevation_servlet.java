/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mil.nga.to6;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import mil.nga.elevation.BboxBean;
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
import org.json.JSONArray;
import org.json.JSONObject;
import us.gov.ic.ism.v2.ClassificationType;

/**
 *
 * @author david
 */
public class elevation_servlet extends HttpServlet {


    // log based on log4j properties
    static final Category log = Category.getInstance(elevation_servlet.class);



    private String getStackTrace(Throwable t) {
      // returns stack trace as a String
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      t.printStackTrace(pw);
      pw.flush();
      return sw.toString();
    }


    /**
     *
     */
    private boolean isKnownFormat(String strOutputFormat) {
        boolean known_format = false;
        if (strOutputFormat == null) {
            return false;
        }

        strOutputFormat = strOutputFormat.trim();

        if (strOutputFormat.equalsIgnoreCase("xml")) {
            known_format = true;
        }
        if (strOutputFormat.equalsIgnoreCase("json")) {
            known_format = true;
        }
        return known_format;
    }


    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Returning XML with a SOAP call this XML would be in the SOAP Body
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // strOutput will be used to hold the XML message before sending it back.
        String strOutput = "";
        // OutputFormat for JSON added at 2.0.1
        String strOutputFormat = null;


        log.debug("Start processRequest");

        try {


            String strOperation = null;
            String strParameters = null;

            // Populate the parameters ignoring case
            Enumeration paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                if (paramName.equalsIgnoreCase("operation")) {
                    strOperation = request.getParameter(paramName);
                } else if (paramName.equalsIgnoreCase("parameters")) {
                    strParameters = request.getParameter("parameters");
                } else if (paramName.equalsIgnoreCase("outputFormat")) {
                    strOutputFormat = request.getParameter(paramName);
                    log.debug("strOutputFormat = " + strOutputFormat);
                }
            }

            if (strOperation == null && strParameters == null) {
                // Both can't be null
                throw new InvalidParameterException("You must specify a value for either operation or parameters." +
                            " For operation, use a value specified in WSDL and additional paramters as " +
                            " needed; default return is XML. " +
                            " For parameters, use a JSON object for inputs as specified in WSDL; default return is JSON.");
            }

            if (strOperation != null && strParameters != null) {
                // Both can't be set
                throw new InvalidParameterException("You cannot specify both parameters and operation.");
            }

            log.debug("strOperation = " + strOperation);
            log.debug("strParameters = " + strParameters);

            JSONObject jsonObj = new JSONObject();
            boolean isParameters = false;

            if (strParameters != null) {
                // Set the operation based on the JSON object

                // Remove encapsulating parentheses
                char first_ch = strParameters.charAt(0);
                char last_ch = strParameters.charAt(strParameters.length()-1);

                while (first_ch == '(' && last_ch == ')') {
                    strParameters = strParameters.substring(1,strParameters.length());
                    System.out.println(strParameters);
                    first_ch = strParameters.charAt(0);
                    last_ch = strParameters.charAt(strParameters.length()-1);

                }

                //strParameters = strParameters.replaceAll("\\(", "");
                //strParameters = strParameters.replaceAll("\\)", "");


                isParameters = true;

                try {
                    jsonObj = new JSONObject(strParameters);
                } catch (Exception e) {
                    log.debug(e.getMessage());
                    throw new InvalidParameterException("Invalid JSON object: " + strParameters);
                }

                strOperation = JSONObject.getNames(jsonObj)[0];

                log.debug("jsonObj = " + jsonObj.toString());
            }

            log.debug("strOperation = " + strOperation);

            // Check Output Format
            if (strOutputFormat == null) {
                // Set a default OutputFormat
                if (strOperation != null ) {
                    // Both can't be set
                    strOutputFormat = "xml";

                } else {
                    strOutputFormat = "json";
                }
            }

            log.debug("strOutputFormat = " + strOutputFormat);

            if (!isKnownFormat(strOutputFormat)) {
                throw new InvalidParameterException("Unknown Format: " +
                        strOutputFormat + ". Supported formats: xml or json.");

            }


            if (strOperation.equalsIgnoreCase("GetElevationAt")) {


                log.debug("Start GetElevationAt");
                String strPts = null;
                String strHeightType = null;
                String strTerrainDataType = null;

                if (isParameters) {
                    JSONArray jsonPts = new JSONArray();

                    try {
                        jsonPts = jsonObj.getJSONObject(strOperation).getJSONArray("pts");
                    } catch (Exception e) {
                        log.debug(e.getMessage());
                        throw new InvalidParameterException("You must specify points (pts)");
                    }

                    int numPts = jsonPts.length();
                    log.debug("numPts = " + numPts);

                    strPts = "";

                    JSONObject jsonPt = new JSONObject();
                    // Input for the GetCountryNames operation
                    int i = 0;
                    try {
                        while (i < numPts) {

                            jsonPt = jsonPts.getJSONObject(i);
                            String strLon = jsonPt.getString("lon");
                            String strLat = jsonPt.getString("lat");

                            strPts += strLon + "," + strLat;
                            i++;
                            if (i < numPts) {
                                strPts += ",";
                            }
                        }
                    } catch (Exception e) {
                        throw new InvalidParameterException("Problem with JSON Pts Objec: " +
                                jsonPts.toString());
                    }

                    log.debug("strPts = " + strPts);


                    try {
                        strHeightType = jsonObj.getJSONObject(strOperation).getString("heightType");
                    } catch (Exception e) {
                        // OK if not specified
                        log.debug("HeightType not specified");
                    }

                    log.debug("strHeightType = " + strHeightType);


                    try {
                        strTerrainDataType = jsonObj.getJSONObject(strOperation).getString("terrainDataType");
                    } catch (Exception e) {
                        // OK if not specified
                        log.debug("TerrainDataType not specified");
                    }

                    log.debug("strTerrainDataType = " + strTerrainDataType);


                } else {

                    // Populate the parameters ignoring case
                    paramNames = request.getParameterNames();
                    while (paramNames.hasMoreElements()) {
                        String paramName = (String) paramNames.nextElement();
                        if (paramName.equalsIgnoreCase("pts")) {
                            strPts = request.getParameter(paramName);
                            log.debug("strPts = " + strPts);
                        } else if (paramName.equalsIgnoreCase("HeightType")) {
                            strHeightType = request.getParameter(paramName);
                            log.debug("strHeightType = " + strHeightType);
                        } else if (paramName.equalsIgnoreCase("TerrainDataType")) {
                            strTerrainDataType = request.getParameter(paramName);
                            log.debug("strTerrainDataType = " + strTerrainDataType);
                        }
                    } // End While Parameters

                }


                // check parameters
                if (strPts == null) {
                    throw new InvalidParameterException("You must specify the pts parameters with a lon,lat,lon,lat,... values");
                }

                if (strPts.trim().equalsIgnoreCase("")) {
                    throw new InvalidParameterException("You must specify the pts parameters with a lon,lat,lon,lat,... values");
                }


                // Need to turn input lon,lat into CoordinatePairStr
                String pts[] = strPts.split(",");
                if (pts.length % 2 != 0) {
                    throw new InvalidParameterException("Incomplete pts (" +
                            strPts + ") You must enter longitude, latitude for each coordinate. ");
                }

                GetElevationAt params = new GetElevationAt();

                try {
                    int i = 0;
                    while (i < pts.length) {
                        GeodeticCoordinateBean cp = new GeodeticCoordinateBean();                        
                        cp.setLon(pts[i]);
                        cp.setLat(pts[i+1]);
                        i = i + 2;
                        params.getPts().add(cp);
                    }
                } catch (NumberFormatException e) {
                    log.debug(e.getMessage());
                    throw new InvalidParameterException("Invalid pts (" +
                            strPts + ") : " + e.getMessage());
                }

                log.debug("Set HeightType");
                // Set each of the parameters as needed
                HeightUnitType heightType = null;
                if (strHeightType != null) {
                    if (strHeightType.equalsIgnoreCase("METERS") || strHeightType.equalsIgnoreCase("")) {
                        heightType = HeightUnitType.METERS;  // default is RESULTS
                    } else if (strHeightType.equalsIgnoreCase("FEET")) {
                        heightType = HeightUnitType.FEET;
                    } else {
                        throw new InvalidParameterException("Invalid HeightType: " + strHeightType + ". See WSDL for valid inputs.");
                    }
                }

                log.debug("strHeightType " + strHeightType);


                log.debug("Set TerrainDataType");
                // Set each of the parameters as needed
                TerrainDataType tdt = null;
                if (strTerrainDataType != null) {
                    if (strTerrainDataType.equalsIgnoreCase("DTED0") || strTerrainDataType.equalsIgnoreCase("")) {
                        tdt = TerrainDataType.DTED_0; // default to DTED0
                    } else if (strTerrainDataType.equalsIgnoreCase("DTED1")) {
                        tdt = TerrainDataType.DTED_1;
                    } else if (strTerrainDataType.equalsIgnoreCase("DTED2")) {
                        tdt = TerrainDataType.DTED_2;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM1")) {
                        tdt = TerrainDataType.SRTM_1;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM1F")) {
                        tdt = TerrainDataType.SRTM_1_F;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM2")) {
                        tdt = TerrainDataType.SRTM_2;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM2F")) {
                        tdt = TerrainDataType.SRTM_2_F;
                    } else if (strTerrainDataType.equalsIgnoreCase("BEST")) {
                        tdt = TerrainDataType.BEST;
                    } else {
                        throw new InvalidParameterException("Invalid TerrainDataType: " + strTerrainDataType + ". See WSDL for valid inputs.");
                    }
                }

                log.debug("strTerrainDataType " + strTerrainDataType);


                // Here's the magic.  I Instatiate the class that implements the Web Service and use it
                ElevationServiceImpl t = new ElevationServiceImpl();

                // Set the parameters for the input
                params.setHeightType(heightType);
                params.setTerrainDataType(tdt);

                // Security Element should be set (Assumed to be Unclassified for inputs)
                SecurityElement sec = new SecurityElement();
                sec.setClassification(ClassificationType.U);
                sec.getOwnerProducer().add("USA");
                params.setSecurity(sec);

                log.debug("Call Operation: GetElevationAt");
                // Call the operation to get the response
                GetElevationAtResponse resp = t.getElevationAt(params);

                // Now we'll use an ObjectFactory generated by the generate Web Service From WSDL
                mil.nga.elevation.ObjectFactory ob = new mil.nga.elevation.ObjectFactory();

                // Feed object factory the response
                JAXBElement<GetElevationAtResponse> jaxbResp = ob.createGetElevationAtResponse(resp);

                // We'll need a JAXBContext object to turn the class data into XML
                JAXBContext context = JAXBContext.newInstance("mil.nga.elevation");
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                log.debug("Marshall results: GetElevationAt");

                // Send the output to the requester
                marshaller.marshal(jaxbResp, out);

                log.debug("End GetElevationAt");
            } else if (strOperation.equalsIgnoreCase("GetMinMaxElevations")) {


                log.debug("Start GetMinMaxElevations");
                String strBbox = null;
                String strHeightType = null;
                String strTerrainDataType = null;


                if (isParameters) {

                    try {
                        strBbox = jsonObj.getJSONObject(strOperation).getString("bbox");
                        log.debug("strBbox = " + strBbox);
                    } catch (Exception e) {
                        // OK if not specified
                        log.debug("bbox not specified");
                    }

                    try {
                        strHeightType = jsonObj.getJSONObject(strOperation).getString("heightType");
                        log.debug("heightType = " + strHeightType);
                    } catch (Exception e) {
                        // OK if not specified
                        log.debug("heightType not specified");
                    }

                    try {
                        strTerrainDataType = jsonObj.getJSONObject(strOperation).getString("terrainDataType");
                    } catch (Exception e) {
                        // OK if not specified
                        log.debug("TerrainDataType not specified");
                    }

                    log.debug("strTerrainDataType = " + strTerrainDataType);

                } else {

                    // Populate the parameters ignoring case
                    paramNames = request.getParameterNames();
                    while (paramNames.hasMoreElements()) {
                        String paramName = (String) paramNames.nextElement();
                        if (paramName.equalsIgnoreCase("bbox")) {
                            strBbox = request.getParameter(paramName);
                            log.debug("strBbox = " + strBbox);
                        } else if (paramName.equalsIgnoreCase("HeightType")) {
                            strHeightType = request.getParameter(paramName);
                            log.debug("strHeightType = " + strHeightType);
                        } else if (paramName.equalsIgnoreCase("TerrainDataType")) {
                            strTerrainDataType = request.getParameter(paramName);
                            log.debug("strTerrainDataType = " + strTerrainDataType);
                        }
                    } // End While Parameters
                }


                // Check parameters
                if (strBbox == null) {
                    throw new InvalidParameterException("You must specify bbox parameter lllon,lllat,urlon,urlat");
                }

                if (strBbox.trim().equalsIgnoreCase("")) {
                    throw new InvalidParameterException("You must specify bbox parameter lllon,lllat,urlon,urlat");
                }


                String pts[] = strBbox.split(",");

                if (pts.length != 4) {
                    throw new InvalidParameterException("Bbox parameter must have four inputs: lllon,lllat,urlon,urlat");

                }

                BboxBean bbox = new BboxBean();

                try {
                    bbox.setLllon(pts[0]);
                    bbox.setLllat(pts[1]);
                    bbox.setUrlon(pts[2]);
                    bbox.setUrlat(pts[3]);

                } catch (NumberFormatException e) {
                    throw new InvalidParameterException("Invalid bbox (" +
                            strBbox + ") Entries must be Decimal Degrees. ");
                }

                log.debug("Set HeightType");
                // Set each of the parameters as needed
                HeightUnitType heightType = null;
                if (strHeightType != null) {
                    if (strHeightType.equalsIgnoreCase("METERS") || strHeightType.equalsIgnoreCase("")) {
                        heightType = HeightUnitType.METERS;  // default is RESULTS
                    } else if (strHeightType.equalsIgnoreCase("FEET")) {
                        heightType = HeightUnitType.FEET;
                    } else {
                        throw new InvalidParameterException("Invalid HeightType: " + strHeightType + ". See WSDL for valid inputs.");
                    }
                }
                log.debug("heightType = " + strHeightType);

                log.debug("Set TerrainDataType");
                // Set each of the parameters as needed
                TerrainDataType tdt = null;
                if (strTerrainDataType != null) {
                    if (strTerrainDataType.equalsIgnoreCase("DTED0") || strTerrainDataType.equalsIgnoreCase("")) {
                        tdt = TerrainDataType.DTED_0; // default to DTED0
                    } else if (strTerrainDataType.equalsIgnoreCase("DTED1")) {
                        tdt = TerrainDataType.DTED_1;
                    } else if (strTerrainDataType.equalsIgnoreCase("DTED2")) {
                        tdt = TerrainDataType.DTED_2;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM1")) {
                        tdt = TerrainDataType.SRTM_1;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM1F")) {
                        tdt = TerrainDataType.SRTM_1_F;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM2")) {
                        tdt = TerrainDataType.SRTM_2;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM2F")) {
                        tdt = TerrainDataType.SRTM_2_F;
                    } else if (strTerrainDataType.equalsIgnoreCase("BEST")) {
                        tdt = TerrainDataType.BEST;
                    } else {
                        throw new InvalidParameterException("Invalid TerrainDataType: " + strTerrainDataType + ". See WSDL for valid inputs.");
                    }
                }

                log.debug("strTerrainDataType " + strTerrainDataType);


                log.debug("Set parameters for GetMinMaxElevations");
                GetMinMaxElevations params = new GetMinMaxElevations();

                params.setBbox(bbox);
                params.setHeightType(heightType);
                params.setTerrainDataType(tdt);

                // Security Element should be set (Assumed to be Unclassified for inputs)
                SecurityElement sec = new SecurityElement();
                sec.setClassification(ClassificationType.U);
                sec.getOwnerProducer().add("USA");
                params.setSecurity(sec);

                // Here's the magic.  I Instatiate the class that implements the Web Service and use it
                ElevationServiceImpl t = new ElevationServiceImpl();


                log.debug("Call Operation: GetElevationAt");
                // Call the operation to get the response
                GetMinMaxElevationsResponse resp = t.getMinMaxElevations(params);

                // Now we'll use an ObjectFactory generated by the generate Web Service From WSDL
                mil.nga.elevation.ObjectFactory ob = new mil.nga.elevation.ObjectFactory();

                // Feed object factory the response
                JAXBElement<GetMinMaxElevationsResponse> jaxbResp = ob.createGetMinMaxElevationsResponse(resp);

                // We'll need a JAXBContext object to turn the class data into XML
                JAXBContext context = JAXBContext.newInstance("mil.nga.elevation");
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();

                log.debug("Marshall results: GetMinMaxElevations");
                // Send the output to the requester
                marshaller.marshal(jaxbResp, out);

                log.debug("End GetMinMaxElevations");

            } else if (strOperation.equalsIgnoreCase("GetMinMaxElevationsWKT")) {


                log.debug("Start GetMinMaxElevationsWKT");
                String strWKT = null;
                String strHeightType = null;
                String strTerrainDataType = null;


                if (isParameters) {

                    try {
                        log.debug(jsonObj.toString());

                        strWKT = jsonObj.getJSONObject(strOperation).getString("wkt");
                        log.debug("wkt = " + strWKT);
                    } catch (Exception e) {
                        // OK if not specified
                        log.debug("wkt not specified");
                    }

                    try {
                        strHeightType = jsonObj.getJSONObject(strOperation).getString("heightType");
                        log.debug("heightType = " + strHeightType);
                    } catch (Exception e) {
                        // OK if not specified
                        log.debug("heightType not specified");
                    }

                    try {
                        strTerrainDataType = jsonObj.getJSONObject(strOperation).getString("terrainDataType");
                    } catch (Exception e) {
                        // OK if not specified
                        log.debug("TerrainDataType not specified");
                    }

                    log.debug("strTerrainDataType = " + strTerrainDataType);

                } else {

                    // Populate the parameters ignoring case
                    paramNames = request.getParameterNames();
                    while (paramNames.hasMoreElements()) {
                        String paramName = (String) paramNames.nextElement();
                        if (paramName.equalsIgnoreCase("wkt")) {
                            strWKT = request.getParameter(paramName);
                            log.debug("strWKT = " + strWKT);
                        } else if (paramName.equalsIgnoreCase("HeightType")) {
                            strHeightType = request.getParameter(paramName);
                            log.debug("strHeightType = " + strHeightType);
                        } else if (paramName.equalsIgnoreCase("TerrainDataType")) {
                            strTerrainDataType = request.getParameter(paramName);
                            log.debug("strTerrainDataType = " + strTerrainDataType);
                        }
                    } // End While Parameters
                }


                // Check parameters
                if (strWKT == null) {
                    throw new InvalidParameterException("You must specify WKT parameter.");
                }

                if (strWKT.trim().equalsIgnoreCase("")) {
                    throw new InvalidParameterException("You must specify WKT parameter.");
                }



                log.debug("Set HeightType");
                // Set each of the parameters as needed
                HeightUnitType heightType = null;
                if (strHeightType != null) {
                    if (strHeightType.equalsIgnoreCase("METERS") || strHeightType.equalsIgnoreCase("")) {
                        heightType = HeightUnitType.METERS;  // default is RESULTS
                    } else if (strHeightType.equalsIgnoreCase("FEET")) {
                        heightType = HeightUnitType.FEET;
                    } else {
                        throw new InvalidParameterException("Invalid HeightType: " + strHeightType + ". See WSDL for valid inputs.");
                    }
                }
                log.debug("heightType = " + strHeightType);

                log.debug("Set TerrainDataType");
                // Set each of the parameters as needed
                TerrainDataType tdt = null;
                if (strTerrainDataType != null) {
                    if (strTerrainDataType.equalsIgnoreCase("DTED0") || strTerrainDataType.equalsIgnoreCase("")) {
                        tdt = TerrainDataType.DTED_0; // default to DTED0
                    } else if (strTerrainDataType.equalsIgnoreCase("DTED1")) {
                        tdt = TerrainDataType.DTED_1;
                    } else if (strTerrainDataType.equalsIgnoreCase("DTED2")) {
                        tdt = TerrainDataType.DTED_2;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM1")) {
                        tdt = TerrainDataType.SRTM_1;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM1F")) {
                        tdt = TerrainDataType.SRTM_1_F;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM2")) {
                        tdt = TerrainDataType.SRTM_2;
                    } else if (strTerrainDataType.equalsIgnoreCase("SRTM2F")) {
                        tdt = TerrainDataType.SRTM_2_F;
                    } else if (strTerrainDataType.equalsIgnoreCase("BEST")) {
                        tdt = TerrainDataType.BEST;
                    } else {
                        throw new InvalidParameterException("Invalid TerrainDataType: " + strTerrainDataType + ". See WSDL for valid inputs.");
                    }
                }

                log.debug("strTerrainDataType " + strTerrainDataType);


                log.debug("Set parameters for GetMinMaxElevations");
                GetMinMaxElevationsWKT params = new GetMinMaxElevationsWKT();

                params.setWkt(strWKT);
                params.setHeightType(heightType);
                params.setTerrainDataType(tdt);

                // Security Element should be set (Assumed to be Unclassified for inputs)
                SecurityElement sec = new SecurityElement();
                sec.setClassification(ClassificationType.U);
                sec.getOwnerProducer().add("USA");
                params.setSecurity(sec);

                // Here's the magic.  I Instatiate the class that implements the Web Service and use it
                ElevationServiceImpl t = new ElevationServiceImpl();


                log.debug("Call Operation: GetElevationAt");
                // Call the operation to get the response
                GetMinMaxElevationsWKTResponse resp = t.getMinMaxElevationsWKT(params);

                // Now we'll use an ObjectFactory generated by the generate Web Service From WSDL
                mil.nga.elevation.ObjectFactory ob = new mil.nga.elevation.ObjectFactory();

                // Feed object factory the response
                JAXBElement<GetMinMaxElevationsWKTResponse> jaxbResp = ob.createGetMinMaxElevationsWKTResponse(resp);

                // We'll need a JAXBContext object to turn the class data into XML
                JAXBContext context = JAXBContext.newInstance("mil.nga.elevation");
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();

                log.debug("Marshall results: GetMinMaxElevationsWKT");
                // Send the output to the requester
                marshaller.marshal(jaxbResp, out);

                log.debug("End GetMinMaxElevationsWKT");

            } else {
                // Unknown operation send that info to the requester.
                strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
                strOutput += "<error>";
                strOutput += "Invalid operation: " + strOperation;
                strOutput += ". See WSDL for valid operations.";
                strOutput += "\n</error>";
                out.print(strOutput);

            }

        } catch (InvalidParameterException e) {
            // I don't expect the code to get here, but just in case.
            strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
            strOutput += "<error>";

            strOutput += e.getMessage();
            strOutput += "\n</error>";
            out.print(strOutput);


        } catch (Exception e) {
            // I don't expect the code to get here, but just in case.
            strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
            strOutput += "<error>";

            //strOutput +=  getStackTrace(e);
            strOutput +=  e.getMessage();
            strOutput += "\n</error>";
            out.print(strOutput);

        }

        log.debug("End processRequest");

        out.close();



    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
