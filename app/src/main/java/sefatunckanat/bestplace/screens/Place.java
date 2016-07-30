package sefatunckanat.bestplace.screens;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sefatunckanat.bestplace.R;
import sefatunckanat.bestplace.libs.libImageLoader.ImageLoader;
import sefatunckanat.bestplace.libs.parser.JSONParser;
import sefatunckanat.bestplace.utils.Functions;
import sefatunckanat.bestplace.adapters.PlaceAdapter;
import sefatunckanat.bestplace.utils.Variables;

public class Place extends Activity {
    public int ID,meID;
    JSONParser parser = new JSONParser();
    String isim,aciklama,picture;
    String[] olayTur,olayName,olayUrl,olayDate;
    int[] olayId;
    TextView tvİsim,tvAciklama;
    ImageView tvResim;
    ListView list;
    String[] oylar = {"enkotu","enkalite","enucuz","enpahali","enguzel","enrahat","ensessiz","enkalabalik"};
    String[] oylarS = {"En Kötü","En Kaliteli","En Ucuz","En Pahalı","En Güzel","En Rahat","En Sessiz","En Kalabalık"};
    Spinner spOyla;
    ArrayList<Integer> oylayanlar = new ArrayList<Integer>();
    Button btnOyla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_place);
        tvİsim=(TextView)findViewById(R.id.place_name);
        tvAciklama=(TextView)findViewById(R.id.place_des);
        tvResim=(ImageView)findViewById(R.id.place_picture);
        list=(ListView)findViewById(R.id.place_list);
        spOyla=(Spinner)findViewById(R.id.place_oyla);
        btnOyla=(Button)findViewById(R.id.place_btn_oyla);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ID = extras.getInt("ID");
        }
        SharedPreferences preferences=getSharedPreferences(Variables.SHARED_NAME, Context.MODE_PRIVATE);
        meID = preferences.getInt("00",-1);
        if(meID==-1){finish();}

        tvResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),FullPageProfileImage.class);
                intent.putExtra("URL",Variables.IP+picture);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item,oylarS);
        spOyla.setAdapter(dataAdapter);

        btnOyla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetPlace().execute();
            }
        });

        new GetPlace().execute();
    }

    void Update(){
        setTitle(isim);
        tvİsim.setText(isim);
        tvAciklama.setText(aciklama);

        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
        imgLoader.DisplayImage(Variables.IP+picture,R.drawable.ic_launcher,tvResim);

        list.setAdapter(new PlaceAdapter(this,olayName,olayId,olayDate,olayUrl,olayTur));

        if(oylayanlar.contains(meID)){
            btnOyla.setEnabled(false);
            btnOyla.setText("Bugünki oy hakkınızı kullandınız.");
            spOyla.setVisibility(View.INVISIBLE);
        }
    }

    public String getOy(){
        int i = spOyla.getSelectedItemPosition();
        return oylar[i];
    }

    class GetPlace extends AsyncTask<String,String,String>{
        JSONObject json;
        int succes;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Place.this);
            dialog.setTitle("Yükleniyor...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            arg.add(new BasicNameValuePair("ID",ID+""));
            json=parser.makeHttpRequest(Variables.URL_GET_PLACE,"GET",arg);
            try {
                succes=json.getInt("succes");
                if(succes==1){
                    isim = json.getString("isim");
                    aciklama = json.getString("aciklama");
                    picture = json.getString("picture");
                    JSONArray olay = json.getJSONArray("olaylar");
                    olayTur=new String[olay.length()];
                    olayName=new String[olay.length()];
                    olayUrl=new String[olay.length()];
                    olayDate=new String[olay.length()];
                    olayId=new int[olay.length()];
                    for (int i=0;i<olay.length();i++){
                        JSONObject o = olay.getJSONObject(i);
                        olayTur[i]=o.getString("olay");
                        olayName[i]=o.getString("kullanici");
                        olayUrl[i]=o.getString("url");
                        olayDate[i]=o.getString("date");
                        olayId[i]=o.getInt("id");
                    }
                    JSONArray a = json.getJSONArray("oylayanlar");
                    if (a != null) {
                        int len = a.length();
                        for (int i=0;i<len;i++){
                            oylayanlar.add(a.getInt(i));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(succes==1){
                Update();
            }
        }
    }

    class SetPlace extends AsyncTask<String,String,String>{
        JSONObject json;
        int succes;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(Place.this);
            dialog.setCancelable(false);
            dialog.setTitle("İşlem devam ediyor...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            arg.add(new BasicNameValuePair("PLACE",ID+""));
            arg.add(new BasicNameValuePair("ID",meID+""));
            arg.add(new BasicNameValuePair("OY",getOy()));
            json=parser.makeHttpRequest(Variables.URL_SET_PLACE,"GET",arg);
            Functions.log(json.toString());
            try {
                succes = json.getInt("succes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(succes==1){
                new GetPlace().execute();
            }else{
                Toast.makeText(Place.this,"Bir hata oluştu.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
