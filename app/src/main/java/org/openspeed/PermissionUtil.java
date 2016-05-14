package org.openspeed;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {

    public static String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";

    public static boolean isPermissionGranted(Activity act, String permission){
        int permissionCheck = ContextCompat.checkSelfPermission(act, permission);
        if(permissionCheck != 0){
            return false;
        } else {
            return true;
        }
    }

    public static void requestPermission(Activity act, Object obj, String permission){
        ActivityCompat.requestPermissions(act,new String[]{permission},0);
    }

}