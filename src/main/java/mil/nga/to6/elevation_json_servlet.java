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
import mil.nga.elevation.HeightUnitType;
import mil.nga.security.SecurityElement;
import org.apache.log4j.Category;
import org.json.JSONArray;
import org.json.JSONObject;
import us.gov.ic.ism.v2.ClassificationType;

/**
 *
 * @author jenningd
 */
public class elevation_json_servlet extends HttpServlet {



    // log based on log4j properties
    static final Category log = Category.getInstance(elevation_json_servlet.class);

    static final int MAX_POINTS = 200;

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
        String strOutputFormat = "json";


        try {

            String strParameters = null;



            String strUnrecognizedParams = "";
            boolean hasUnrecognized = false;

            // Populate the parameters ignoring case
            Enumeration paramNames = request.getParameterNames();

            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                if (paramName.equalsIgnoreCase("parameters")) {
                    strParameters = request.getParameter(paramName);
                } else if (paramName.equalsIgnoreCase("outputFormat")) {
                    strOutputFormat = request.getParameter(paramName);
                    log.debug("strOutputFormat = " + strOutputFormat);
                } else {
                    if (hasUnrecognized) strUnrecognizedParams += ", ";
                    strUnrecognizedParams += paramName;
                    hasUnrecognized = true;

                }
            }


            // Check Output Format
            if (strOutputFormat == null) {
                throw new InvalidParameterException("You must specify  " +
                        strOutputFormat + ". Supported formats: xml or json.");
            }

            if (strOutputFormat.equalsIgnoreCase("")) {
                strOutputFormat = "json";  // default json
            }
            if (!isKnownFormat(strOutputFormat)) {
                throw new InvalidParameterException("Unknown Format: " +
                        strOutputFormat + ". Supported formats: xml or json.");
            }


            if (strParameters == null) {
                throw new InvalidParameterException("You must specify parameters in JSON format.  See WSDL.");
            }

            if (strParameters.trim().equalsIgnoreCase("")) {
                throw new InvalidParameterException("You must specify parameters in JSON format.  See WSDL.");
            }


            // get rid of the leading and closing ( or )
            strParameters = strParameters.replaceAll("\\(", "");
            strParameters = strParameters.replaceAll("\\)", "");

            JSONObject jsonObj = new JSONObject();

            try {
                jsonObj = new JSONObject(strParameters);
            } catch (Exception e) {
                throw new InvalidParameterException("Invalid JSON object: " + strParameters);
            }

            String strOperation = JSONObject.getNames(jsonObj)[0];

            if (strOperation.equalsIgnoreCase("GetElevationAt")) {

                log.debug("Start GetElevationAt");
                String strPts = null;
                String strHeightType = null;

                strUnrecognizedParams = "";
                hasUnrecognized = false;


                JSONArray jsonPts = new JSONArray();

                try {
                    jsonPts = jsonObj.getJSONObject(strOperation).getJSONArray("pts");
                } catch (Exception e) {
                    throw new InvalidParameterException("You must specify points (pts)");
                }

                int numPts = jsonPts.length();

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



                try {
                    strHeightType = jsonObj.getJSONObject(strOperation).getString("heightType");
                } catch (Exception e) {
                    // OK if not specified
                    log.debug("HeightType not specified");
                }



                // Check Output Format
                if (strOutputFormat == null) {
                    throw new InvalidParameterException("You must specify  " +
                            strOutputFormat + ". Supported formats: xml or json.");
                }

                if (strOutputFormat.equalsIgnoreCase("")) {
                    strOutputFormat = "json";  // default json
                }
                if (!isKnownFormat(strOutputFormat)) {
                    throw new InvalidParameterException("Unknown Format: " +
                            strOutputFormat + ". Supported formats: xml or json.");
                }


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
                    i = 0;
                    while (i < pts.length) {
                        GeodeticCoordinateBean cp = new GeodeticCoordinateBean();
                        cp.setLon(pts[i]);
                        cp.setLat(pts[i+1]);
                        i = i + 2;
                        params.getPts().add(cp);
                    }
                } catch (NumberFormatException e) {
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


                // Here's the magic.  I Instatiate the class that implements the Web Service and use it
                ElevationServiceImpl t = new ElevationServiceImpl();

                // Set the parameters for the input
                params.setHeightType(heightType);

                // Security Element should be set (Assumed to be Unclassified for inputs)
                SecurityElement sec = new SecurityElement();
                sec.setClassification(ClassificationType.U);
                sec.getOwnerProducer().add("USA");
                params.setSecurity(sec);

                // Call the operation to get the response
                GetElevationAtResponse resp = t.getElevationAt(params);

                // Now we'll use an ObjectFactory generated by the generate Web Service From WSDL
                mil.nga.elevation.ObjectFactory ob = new mil.nga.elevation.ObjectFactory();

                // Feed object factory the response
                JAXBElement<GetElevationAtResponse> jaxbResp = ob.createGetElevationAtResponse(resp);

                // We'll need a JAXBContext object to turn the class data into XML
                JAXBContext context = JAXBContext.newInstance("mil.nga.elevation");
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();

                // Send the output to the requester
                marshaller.marshal(jaxbResp, out);

                log.debug("Start GetElevationAt");


            } else if (strOperation.equalsIgnoreCase("GetMinMaxElevations")) {

                log.debug("Start GetMinMaxElevations");
                String strBbox = null;
                String strHeightType = null;

                strUnrecognizedParams = "";
                hasUnrecognized = false;

                try {
                    strBbox = jsonObj.getJSONObject(strOperation).getString("bbox");
                } catch (Exception e) {
                    // OK if not specified
                    log.debug("bbox not specified");
                }                

                try {
                    strHeightType = jsonObj.getJSONObject(strOperation).getString("heightType");
                } catch (Exception e) {
                    // OK if not specified
                    log.debug("heightType not specified");
                }


                // Check Output Format
                if (strOutputFormat == null) {
                    throw new InvalidParameterException("You must specify  " +
                            strOutputFormat + ". Supported formats: xml or json.");
                }

                if (strOutputFormat.equalsIgnoreCase("")) {
                    strOutputFormat = "json";  // default json
                }

                if (!isKnownFormat(strOutputFormat)) {
                    throw new InvalidParameterException("Unknown Format: " +
                            strOutputFormat + ". Supported formats: xml or json.");
                }


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

                GetMinMaxElevations params = new GetMinMaxElevations();

                params.setBbox(bbox);
                params.setHeightType(heightType);

                // Security Element should be set (Assumed to be Unclassified for inputs)
                SecurityElement sec = new SecurityElement();
                sec.setClassification(ClassificationType.U);
                sec.getOwnerProducer().add("USA");
                params.setSecurity(sec);

                // Here's the magic.  I Instatiate the class that implements the Web Service and use it
                ElevationServiceImpl t = new ElevationServiceImpl();


                // Call the operation to get the response
                GetMinMaxElevationsResponse resp = t.getMinMaxElevations(params);

                // Now we'll use an ObjectFactory generated by the generate Web Service From WSDL
                mil.nga.elevation.ObjectFactory ob = new mil.nga.elevation.ObjectFactory();

                // Feed object factory the response
                JAXBElement<GetMinMaxElevationsResponse> jaxbResp = ob.createGetMinMaxElevationsResponse(resp);

                // We'll need a JAXBContext object to turn the class data into XML
                JAXBContext context = JAXBContext.newInstance("mil.nga.elevation");
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();

                // Send the output to the requester
                marshaller.marshal(jaxbResp, out);

                log.debug("End GetMinMaxElevations");



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
