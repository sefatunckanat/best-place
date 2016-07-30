package sefatunckanat.bestplace.utils;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class Functions {
    public static void GoPage(Activity a,Class c){
        Intent i = new Intent(a,c);
        a.startActivity(i);
    }

    public static String getDeviceID(Activity a){
        return Settings.Secure.getString(a.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public static void log(String m){
        Log.d("SefaTunçkanat",m);
    }
}
