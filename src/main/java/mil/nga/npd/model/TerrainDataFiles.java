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
public class TerrainDataFiles implements Serializable {

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
	
	@Column(name="LAT")
	String lat;
	
	@Column(name="LON")
	String lon;
	
	@Column(name="WIN_PATH")
	String windowsPath;
	
	@Column(name="UNIX_PATH")
	String unixPath;
	
	@Column(name="BEST")
	int best;
	
	@Column(name="MARKING")
	String marking;
	
}
