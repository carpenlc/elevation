/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mil.nga.to6;


import mil.nga.elevation.ElevationBean;

/**
 *
 * @author david
 */
public class MinMaxElevation {

    private final short INVALID_MAX_ELEV = -16958;

    public String getMaxSource() {
        return maxSource;
    }

    public void setMaxSource(String maxSource) {
        this.maxSource = maxSource;
    }

    public String getMinSource() {
        return minSource;
    }

    public void setMinSource(String minSource) {
        this.minSource = minSource;
    }
    private final short INVALID_MIN_ELEV = 16958;

    private short maxElevation;
    private GeodeticCoordinate maxCoordPair;
    private String maxSource;
    private String maxMarking;

    private short minElevation;
    private GeodeticCoordinate minCoordPair;
    private String minSource;
    private String minMarking;

    public String getMaxMarking() {
        return maxMarking;
    }

    public void setMaxMarking(String maxMarking) {
        this.maxMarking = maxMarking;
    }

    public String getMinMarking() {
        return minMarking;
    }

    public void setMinMarking(String minMarking) {
        this.minMarking = minMarking;
    }


    public MinMaxElevation() {
        this.maxElevation = INVALID_MAX_ELEV;
        this.maxCoordPair = new GeodeticCoordinate();
        this.maxSource = "NO DATA";

        this.minElevation = INVALID_MIN_ELEV;
        this.minCoordPair = new GeodeticCoordinate();
        this.minSource = "NO DATA";
    }


    
    public ElevationBean getMaxElevationBean() {
        ElevationBean maxBean = new ElevationBean();
        maxBean.setElevation(String.valueOf(this.maxElevation).trim());
        maxBean.setCoordinates(this.maxCoordPair.GetGeodeticCoordinateBean());
        maxBean.setSource(maxSource);
        maxBean.setMarking(this.maxMarking);
        return maxBean;
    }

    public ElevationBean getMinElevationBean() {
        ElevationBean minBean = new ElevationBean();
        minBean.setElevation(String.valueOf(this.minElevation).trim());
        minBean.setCoordinates(this.minCoordPair.GetGeodeticCoordinateBean());
        minBean.setSource(minSource);
        minBean.setMarking(this.minMarking);
        return minBean;
    }

    public GeodeticCoordinate getMaxCoordPair() {
        return maxCoordPair;
    }

    public void setMaxCoordPair(GeodeticCoordinate maxCoordPair) {
        this.maxCoordPair = maxCoordPair;
    }

    public short getMaxElevation() {
        return maxElevation;
    }

    public void setMaxElevation(short maxElevation) {
        this.maxElevation = maxElevation;
    }

    public GeodeticCoordinate getMinCoordPair() {
        return minCoordPair;
    }

    public void setMinCoordPair(GeodeticCoordinate minCoordPair) {
        this.minCoordPair = minCoordPair;
    }

    public short getMinElevation() {
        return minElevation;
    }

    public void setMinElevation(short minElevation) {
        this.minElevation = minElevation;
    }



}
