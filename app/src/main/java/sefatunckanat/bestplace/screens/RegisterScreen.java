package sefatunckanat.bestplace.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sefatunckanat.bestplace.R;
import sefatunckanat.bestplace.libs.parser.JSONParser;
import sefatunckanat.bestplace.utils.Functions;
import sefatunckanat.bestplace.utils.Variables;

public class RegisterScreen extends Activity {

    public List<String> city_ids = new ArrayList<String>();
    public List<String> city_names = new ArrayList<String>();
    public Spinner sp_citys;
    public int city_index = -1;
    public EditText et_username,et_mail,et_password;
    public Button bt_submit;

    JSONParser parser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_register);

        et_username = (EditText)findViewById(R.id.register_et_username);
        et_mail = (EditText)findViewById(R.id.register_et_mail);
        et_password = (EditText)findViewById(R.id.register_et_pass);
        bt_submit = (Button)findViewById(R.id.register_bt_submit);

        new GetCitys().execute();

        sp_citys = (Spinner)findViewById(R.id.register_sp_sehirler);
        sp_citys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Register().execute(et_username.getText().toString(),et_password.getText().toString(),et_mail.getText().toString(),city_index+"");
            }
        });
    }

    class Register extends AsyncTask<String,String,String>{
        JSONObject jsonRegister;
        String username,password,city,mail,message;
        int succes;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterScreen.this);
            progressDialog.setMessage("Hesabınız Oluşturuluyor...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
            @Override
        protected void onPostExecute(String s) {
            new AlertDialog.Builder(RegisterScreen.this)
                .setTitle(message)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(succes==1){
                            finish();
                        }
                    }
                })
                .show();
            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            username = params[0];
            password = params[1];
            mail = params[2];
            city = city_ids.get(Integer.parseInt(params[3]));
            //city = "10";

            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            arg.add(new BasicNameValuePair("security_post",Variables.SECURITY_KEY));
            arg.add(new BasicNameValuePair("username",username));
            arg.add(new BasicNameValuePair("password",password));
            arg.add(new BasicNameValuePair("mail",mail));
            arg.add(new BasicNameValuePair("city",city));
            arg.add(new BasicNameValuePair("deviceID",Functions.getDeviceID(RegisterScreen.this)));

            jsonRegister = parser.makeHttpRequest(Variables.URL_REGISTER, "POST", arg);
            Functions.log(jsonRegister.toString());
            try {
                succes = jsonRegister.getInt("succes");
                message = jsonRegister.getString("message");
            } catch (JSONException e) {

            }
            return null;
        }
    }

    class GetCitys extends AsyncTask<String, String, String> {
        JSONObject json;

        @Override
        protected void onPostExecute(String s) {
            ArrayAdapter<String>adapter=new ArrayAdapter<String>(RegisterScreen.this,R.layout.spinner_item,city_names);
            sp_citys.setAdapter(adapter);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            json = parser.makeHttpRequest(Variables.URL_CITYS, "POST", arg);
            Functions.log(json.toString());
            try {
                JSONArray sehirler = json.getJSONArray("city");
                for (int i = 0; i < sehirler.length(); i++) {
                    JSONObject sehir_kod = sehirler.getJSONObject(i);
                    city_ids.add(sehir_kod.getString("kod"));
                    city_names.add(sehir_kod.getString("ad"));
                }
            } catch (JSONException e) {

            }
            return null;
        }
    }

}
