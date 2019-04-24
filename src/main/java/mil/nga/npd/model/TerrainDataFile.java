package mil.nga.npd.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity represents a row in the <code>TERRAIN_DATA_FILES</code> table
 * which contains a listing of various DTED frame files, the lower-left 
 * lat/lon that they represent, and an indication of how good the backing 
 * frame data is.  The higher the number associated with the <code>BEST<code>
 * column, the lower quality the data is assumed to be. 
 * 
 * @author L. Craig Carpenter
 */
@Entity
@Table(name="TERRAIN_DATA_FILES")
public class TerrainDataFile implements Serializable {

	/**
	 * Eclipse-generated serialVersionUID
	 */
	private static final long serialVersionUID = -2599614551493373364L;

	/**
	 * The target table does not have a primary key.  In order to use 
	 * hibernate, we have to use the Oracle ROWID psuedo column.
	 */
	@Id
	@Column(name="ROWID")
	String rowId;
	
	/**
	 * The source terrain file type.
	 * @see mil.nga.elevation.TerrainDataType
	 */
	@Column(name="TYP")
	String type;
	
	/**
	 * Latitude value of lower-left coordinate of DEM frame.
	 */
	@Column(name="LAT")
	String lat;
	
	/**
	 * Longitude value of lower-left coordinate of DEM frame.
	 */
	@Column(name="LON")
	String lon;
	
	/**
	 * Windows path to the DEM file on the filesystem.
	 */
	@Column(name="WIN_PATH")
	String windowsPath;
	
	/**
	 * Unix/Linux path to the DEM file on the filesystem.
	 */
	@Column(name="UNIX_PATH")
	String unixPath;
	
	/**
	 * Indication of the relative quality of the target DEM frame.
	 */
	@Column(name="BEST")
	int best;
	
	/**
	 * Classification marking.
	 */
	@Column(name="MARKING")
	String marking;
	
	/**
	 * Default no-arg constructor.
	 */
	public TerrainDataFile() {}
	
	/**
	 * Getter method for the source terrain file type.
	 * @see mil.nga.elevation.TerrainDataType
	 * @return The source type.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Getter method for the latitude value of lower-left coordinate of 
	 * DEM frame.
	 * @return Lower-left latitude value of the DEM frame.
	 */
	public String getLat() {
	    return lat;
	}
	
	/**
	 * Getter method for the longitude value of lower-left coordinate of 
	 * DEM frame.
	 * @return Lower-left longitude value of the DEM frame.
	 */
	public String getLon() {
		return lon;
	}
	
	/**
	 * Getter method for the windows path to the DEM file on the 
	 * file system.
	 * @return Path on the file system.
	 */
	public String getWindowsPath() {
		return windowsPath;
	}
	
	/**
	 * Getter method for the unix/linux path to the DEM file on the 
	 * file system.
	 * @return Path on the file system.
	 */
	public String getUnixPath() {
		return unixPath;
	}
	
	/**
	 * Getter method for the indication of the relative quality of the target 
	 * DEM frame.  The higher the number the lower the quality.
	 * @return Quality indication.
	 */
	public int getQuality() {
		return best;
	}
	
	/**
	 * Getter method for the classification marking.
	 * @return The classification marking.
	 */
	public String getMarking() {
		return marking;
	}
	
	/**
	 * Getter method for the source terrain file type.
	 * @see mil.nga.elevation.TerrainDataType
	 * @param value The source type.
	 */
	public void setType(String value) {
		type = value;
	}
	
	/**
	 * Setter method for the latitude value of lower-left coordinate of 
	 * DEM frame.
	 * @param value Lower-left latitude value of the DEM frame.
	 */
	public void setLat(String value) {
	    lat = value;
	}
	
	/**
	 * Setter method for the longitude value of lower-left coordinate of 
	 * DEM frame.
	 * @param value Lower-left longitude value of the DEM frame.
	 */
	public void setLon(String value) {
		lon = value;
	}
	
	/**
	 * Setter method for the windows path to the DEM file on the 
	 * file system.
	 * @param value Path on the file system.
	 */
	public void setWindowsPath(String value) {
		windowsPath = value;
	}
	
	/**
	 * Setter method for the unix/linux path to the DEM file on the 
	 * file system.
	 * @param value Path on the file system.
	 */
	public void setUnixPath(String value) {
		unixPath = value;
	}
	
	/**
	 * Setter method for the indication of the relative quality of the target 
	 * DEM frame.  The higher the number the lower the quality.
	 * @param value Quality indication.
	 */
	public void setQuality(int value) {
		best = value;
	}
	
	/**
	 * Setter method for the classification marking.
	 * @param value The classification marking.
	 */
	public void setMarking(String value) {
		marking = value;
	}
	
	/**
	 * Convert the object to a printable String.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TerrainDataFile : type => [ ");
		sb.append(getType());
		sb.append(" ], lat => [ ");
		sb.append(getLat());
		sb.append(" ], lon => [ ");
		sb.append(getLon());
		sb.append(" ], windows path => [ ");
		sb.append(getWindowsPath());
		sb.append(" ], unix path => [ ");
		sb.append(getUnixPath());
		sb.append(" ], quality => [ ");
		sb.append(getQuality());
		sb.append(" ], classification marking => [ ");
		sb.append(getMarking());
		sb.append(" ].");
		return sb.toString();
	}
}
