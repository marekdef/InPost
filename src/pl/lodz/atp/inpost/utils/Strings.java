package pl.lodz.atp.inpost.utils;

/**
 * Utils class 
 * @author mario
 * 
 */
public final class Strings {
       
    private Strings() {}
    
    public static boolean isEmpty( String string ) {
        if (string == null || string.length() == 0){
            return true;
        }
        return false;
    }
    
}
