package sefatunckanat.bestplace.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sefatunckanat.bestplace.R;
import sefatunckanat.bestplace.libs.parser.JSONParser;
import sefatunckanat.bestplace.utils.Variables;

/**
 * @author SefaTunckanat
 * @time 22 Nisan 2015 : 15:02:31
 * @project BestPlace 
 */

public class LoginScreen extends Activity {
	
	private RelativeLayout rel_main;
	private Handler mHandler;
	private float alpha = -1f;
	private TextView registerText;
    private EditText et_username,et_password;
    private Button bt_login;
    SharedPreferences sharedPreferences;
    JSONParser parser = new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_login);

        sharedPreferences = getSharedPreferences(Variables.SHARED_NAME, Context.MODE_PRIVATE);

		mHandler = new Handler();
		rel_main = (RelativeLayout) findViewById(R.id.login_rel_center);
        registerText = (TextView) findViewById(R.id.login_tv_register);
        et_password = (EditText)findViewById(R.id.login_et_pass);
        et_username = (EditText)findViewById(R.id.login_et_nick);
        bt_login = (Button)findViewById(R.id.login_bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Login().execute(et_username.getText().toString(),et_password.getText().toString());
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(LoginScreen.this,RegisterScreen.class);
            startActivity(i);
            }
        });

		rel_main.setAlpha(0);
		
		startAni();
	}
	
	private void startAni(){
		mHandler.removeCallbacks(ani);
		mHandler.postDelayed(ani, 100);
	}
	
	Runnable ani = new Runnable() {
		@Override
		public void run() {
			if(alpha<=1){
				alpha += 0.2f;
				rel_main.setAlpha(alpha);
				startAni();
			}
		}
	};

    class Login extends AsyncTask<String,String,String>{
        JSONObject json;
        int succes,ID;
        String message;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginScreen.this);
            progressDialog.setMessage("Bekleyiniz...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if(succes==1){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("00",ID);
                for (int i=1;i<3;i++) {
                    editor.putInt("0"+i,i*9);
                }
                editor.commit();

                Intent intent = new Intent(getBaseContext(),ProfileScreen.class);
                startActivity(intent);
                finish();
            }else{
                new AlertDialog.Builder(LoginScreen.this)
                        .setTitle(message)
                        .setPositiveButton("Tamam",null)
                        .show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            arg.add(new BasicNameValuePair("username",username));
            arg.add(new BasicNameValuePair("password",password));
            arg.add(new BasicNameValuePair("security_post",Variables.SECURITY_KEY));
            json = parser.makeHttpRequest(Variables.URL_LOGIN,"POST",arg);
            try{
                succes = json.getInt("succes");
                message = json.getString("message");
                ID = json.getInt("id");
            }catch (JSONException e){

            }

            return null;
        }
    }
}
