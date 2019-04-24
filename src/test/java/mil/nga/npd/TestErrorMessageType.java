package mil.nga.npd;

import org.junit.Test;
import org.junit.Assert;

public class TestErrorMessageType {

    @Test
    public void testGetErrorMessage() {
        
        Assert.assertEquals(
                ErrorMessageType.UNKNOWN_ERROR.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("1"));
        
        Assert.assertEquals(
                ErrorMessageType.UNKNOWN_ERROR.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("3.14159"));
        
        Assert.assertEquals(
                ErrorMessageType.UNKNOWN_ERROR.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1000"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1001.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1001"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1002.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1002"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1003.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1003"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1004.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1004"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1005.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1005"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1006.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1006"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1007.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1007"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1008.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1008"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1009.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1009"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1010.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1010"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1011.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1011"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1012.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1012"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1013.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1013"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1014.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1014"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1015.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1015"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1016.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1016"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1017.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1017"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1018.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1018"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1019.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1019"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1020.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1020"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1021.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1021"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1022.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1022"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1023.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1023"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1024.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1024"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1025.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1025"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1026.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1026"));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1027.getErrorMessage(), 
                ErrorMessageType.getErrorMessage("-1027"));
    }
    
    @Test
    public void testGetErrorMessageDbl() {
        
        Assert.assertEquals(
                ErrorMessageType.UNKNOWN_ERROR.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(1));
        
        Assert.assertEquals(
                ErrorMessageType.UNKNOWN_ERROR.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(3.14159));
        
        Assert.assertEquals(
                ErrorMessageType.UNKNOWN_ERROR.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1000));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1001.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1001));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1002.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1002));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1003.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1003));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1004.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1004));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1005.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1005));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1006.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1006));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1007.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1007));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1008.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1008));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1009.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1009));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1010.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1010));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1011.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1011));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1012.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1012));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1013.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1013));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1014.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1014));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1015.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1015));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1016.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1016));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1017.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1017));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1018.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1018));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1019.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1019));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1020.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1020));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1021.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1021));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1022.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1022));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1023.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1023));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1024.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1024));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1025.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1025));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1026.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1026));
        
        Assert.assertEquals(
                ErrorMessageType.ERROR_MINUS_1027.getErrorMessage(), 
                ErrorMessageType.getErrorMessage(-1027));
    }
}
