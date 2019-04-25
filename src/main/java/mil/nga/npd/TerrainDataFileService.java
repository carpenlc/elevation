package mil.nga.npd;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mil.nga.elevation.TerrainDataType;
import mil.nga.npd.model.TerrainDataFile;

/**
 * Class responsible for interfacing with the backing data store to retrieve
 * a list of DTED frame files matching the input client request.
 * 
 * @author L. Craig Carpenter
 */
public class TerrainDataFileService {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(TerrainDataFileService.class);
    
    /**
     * Hibernate entity manager object.
     */
    private EntityManager em;
    
    /**
     * Latitude values are stored in the database as a truncated String 
     * containing a hemisphere designator.  This method converts the 
     * input double into the correct String format.
     * 
     * @param lat Target latitude value as a double.
     * @return String-based latitude data as it appears in the data store.
     */
    public static String convertLat(double lat) {
    	
    	StringBuilder  sb         = new StringBuilder();
    	String         hemisphere = (lat < 0) ? "s" : "n";
        String         latitude   = String.valueOf(Math.abs(lat));
        
        if (Math.abs(lat) < 10) {
        	sb.append(hemisphere);
        	sb.append("0");
        	sb.append(latitude);
        }
        else {
        	sb.append(hemisphere);
        	sb.append(latitude);
        }
        return sb.toString();
    }
    
    /**
     * Longitude values are stored in the database as a truncated String 
     * containing a hemisphere designator.  This method converts the 
     * input double into the correct String format.
     * 
     * @param lon Target longitude value as a double.
     * @return String-based longitude data as it appears in the data store.
     */
    public static String convertLon(double lon) {
    	
    	StringBuilder  sb         = new StringBuilder();
    	String         hemisphere = (lon < 0) ? "e" : "w";
    	String         longitude  = String.valueOf(Math.abs(lon));
        
    	if (Math.abs(lon) < 10) {
            sb.append(hemisphere);
         	sb.append("00");
         	sb.append(longitude);
        } else if (Math.abs(lon) < 100) {
         	sb.append(hemisphere);
         	sb.append("0");
         	sb.append(longitude);
        }
        else {
            sb.append(hemisphere);
          	sb.append(longitude); 
        }
    	return sb.toString();
    }
    
    /** 
     * Convenience method allowing callers to retrieve the list of terrain
     * data files associated with a <code>GeodeticCoordinate</code> object.
     * 
     * @param coord
     * @param source
     * @return
     */
    public List<TerrainDataFile> getTerrainDataFiles(
    		GeodeticCoordinate coord, 
    		TerrainDataType source) {
    	return getTerrainDataFiles(coord.getLat(), coord.getLon(), source);
    }
    /**
     * 
     * @param lat
     * @param lon
     * @param source
     * @return
     */
    public List<TerrainDataFile> getTerrainDataFiles(
    		double lat, 
    		double lon, 
    		TerrainDataType source) {
    	
    	long                  startTime = System.currentTimeMillis();
    	List<TerrainDataFile> result    = null;
    	
    	String latString = convertLat(lat);
    	String lonString = convertLon(lon);
    	
    	if (em != null) {
    		CriteriaBuilder cb = em.getCriteriaBuilder();
    		CriteriaQuery<TerrainDataFile> cq = cb.createQuery(TerrainDataFile.class);
    		Root<TerrainDataFile> root = cq.from(TerrainDataFile.class);
    		cq.select(root);
    		ParameterExpression<String> latExpression = cb.parameter(String.class, "LAT");
    		ParameterExpression<String> lonExpression = cb.parameter(String.class, "LON");
    		
    		if (source == TerrainDataType.BEST) {
    			cq.where(
    		        cb.like(root.get("LAT"), latExpression),
    				cb.like(root.get("LON"), lonExpression)
    		    );
    			cq.orderBy(cb.asc(root.get("TYP")));
    		}
    		else {
    			ParameterExpression<String> sourceExpression = cb.parameter(String.class, "TYP");
    			cq.where(
        		    cb.like(root.get("LAT"), latExpression),
        		    cb.like(root.get("LON"), lonExpression),
        		    cb.like(root.get("TYP"), sourceExpression)
        		);    			
    		}
    		Query query = em.createQuery(cq);
    		
    		// Set the query parameters
    		if (source == TerrainDataType.BEST) {
    			query.setParameter("LAT", latString);
    			query.setParameter("LON", lonString);
    		}
    		else {
    			query.setParameter("LAT", latString);
    			query.setParameter("LON", lonString);
    			query.setParameter("TYP", source.name());
    		}
    		
    		result = query.getResultList();
    		
    		if (LOGGER.isDebugEnabled()) {
    			if (result != null) {
    				LOGGER.debug("[ "
    						+ result.size() 
    						+ " ] TerrainDataFile records selected in [ "
    						+ (System.currentTimeMillis() - startTime)
    						+ " ] ms.");
    			}
    			
    		}
    	}
    	return result;
    }
}
