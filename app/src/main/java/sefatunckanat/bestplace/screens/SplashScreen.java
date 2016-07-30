package sefatunckanat.bestplace.screens;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.widget.TextView;
import android.widget.Toast;

import sefatunckanat.bestplace.R;
import sefatunckanat.bestplace.libs.parser.JSONParser;
import sefatunckanat.bestplace.utils.Functions;
import sefatunckanat.bestplace.utils.Variables;

/**
 * @author SefaTunckanat
 * @time 23 Nisan 2015 : 12:22:32
 * @project BestPlace 
 */

public class SplashScreen extends Activity {

    private TextView tv_loading;
    private Handler mHandler = new Handler();
    private float alpha = 0f;
    private int kat = 1, ani = 1, time = 0;
    JSONParser parser = new JSONParser();
    static public String android_id;
    SharedPreferences sharedPreferences;
    static public int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        setContentView(R.layout.screen_splash);

        tv_loading = (TextView) findViewById(R.id.splash_tv_loading);

        sharedPreferences = getSharedPreferences(Variables.SHARED_NAME, Context.MODE_PRIVATE);

        new ServerStat().execute();
    }

    private void startAnimation() {
        mHandler.removeCallbacks(mUpdate);
        mHandler.postDelayed(mUpdate, 100);
    }

    private Runnable mUpdate = new Runnable() {
        @Override
        public void run() {
            alpha += 0.1f * kat;
            if (alpha > 1.9f) kat = -1;
            if (alpha < 0.1f) kat = 1;
            tv_loading.setAlpha(alpha);
            time += 1;
            if (time == 25) {
                ani = 0;
            }
            if (ani == 1) {
                startAnimation();
            } else {
                ID = sharedPreferences.getInt("00", -1);
                if (ID != -1) {
                    Intent intent = new Intent(getBaseContext(), ProfileScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    Functions.GoPage(SplashScreen.this, LoginScreen.class);
                    finish();
                }
            }
        }
    };

    class ServerStat extends AsyncTask<String, String, String> {
        private JSONObject json;
        private int online = 0;
        private String message = "";

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            arg.add(new BasicNameValuePair("deviceID", android_id));

            json = parser.makeHttpRequest(Variables.URL_SERVER_STAT, "POST", arg);
            try {
                online = json.getInt("status");
                message = json.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (online == 1) {
                startAnimation();
            } else {
                Toast.makeText(
                        getApplicationContext(), message,
                        Toast.LENGTH_LONG
                ).show();
                finish();
            }
        }
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}