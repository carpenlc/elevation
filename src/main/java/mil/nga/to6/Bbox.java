/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mil.nga.to6;

import mil.nga.elevation.BboxBean;

/**
 *
 * @author david
 */
public class Bbox {

    private double lllon;
    private double lllat;
    private double urlon;
    private double urlat;

    public Bbox() {
        this.lllon = 0;
        this.lllat = 0;
        this.urlon = 0;
        this.urlat = 0;
    }

    public Bbox(float lllon, float lllat, float urlon, float urlat) {
        this.lllon = lllon;
        this.lllat = lllat;
        this.urlon = urlon;
        this.urlat = urlat;
    }

    public Bbox(BboxBean bn) throws Exception {
        CoordsParse parse = new CoordsParse();

        double dbl_lllon = parse.parseCoordString(bn.getLllon(), false);
        double dbl_lllat = parse.parseCoordString(bn.getLllat(), true);
        double dbl_urlon = parse.parseCoordString(bn.getUrlon(), false);
        double dbl_urlat = parse.parseCoordString(bn.getUrlat(), true);

        if (dbl_lllon < -1000) {
            throw new UnsupportedOperationException("Invalid Longitude Value: " + bn.getLllon() + " : " + parse.getParseErrorMsg(dbl_lllon));
        }

        if (dbl_lllat < -1000) {
            throw new UnsupportedOperationException("Invalid Latitude Value: " + bn.getLllat() + " : " + parse.getParseErrorMsg(dbl_lllat));
        }

        if (dbl_urlon < -1000) {
            throw new UnsupportedOperationException("Invalid Longitude Value: " + bn.getUrlon() + " : " + parse.getParseErrorMsg(dbl_urlon));
        }

        if (dbl_urlat < -1000) {
            throw new UnsupportedOperationException("Invalid Latitude Value: " + bn.getUrlat() + " : " + parse.getParseErrorMsg(dbl_urlat));
        }

        this.lllon = (float) dbl_lllon;
        this.lllat = (float) dbl_lllat;
        this.urlon = (float) dbl_urlon;
        this.urlat = (float) dbl_urlat;

    }


    public BboxBean getBboxBean() {
        BboxBean bn = new BboxBean();
        bn.setLllon(String.valueOf(this.lllon).trim());
        bn.setLllat(String.valueOf(this.lllat).trim());
        bn.setUrlon(String.valueOf(this.urlon).trim());
        bn.setUrlat(String.valueOf(this.urlat).trim());
        return bn;
    }

    public double getLllat() {
        return lllat;
    }

    public void setLllat(double lllat) {
        this.lllat = lllat;
    }

    public double getLllon() {
        return lllon;
    }

    public void setLllon(double lllon) {
        this.lllon = lllon;
    }

    public double getUrlat() {
        return urlat;
    }

    public void setUrlat(double urlat) {
        this.urlat = urlat;
    }

    public double getUrlon() {
        return urlon;
    }

    public void setUrlon(double urlon) {
        this.urlon = urlon;
    }



}
