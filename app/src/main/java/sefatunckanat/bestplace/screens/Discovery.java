package sefatunckanat.bestplace.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sefatunckanat.bestplace.R;
import sefatunckanat.bestplace.libs.parser.JSONParser;
import sefatunckanat.bestplace.adapters.DiscoveryAdapter;
import sefatunckanat.bestplace.utils.Variables;

public class Discovery extends Activity{
    int ID;
    SharedPreferences sharedPreferences;
    String[] oylar = {"enkotu","enkalite","enucuz","enpahali","enguzel","enrahat","ensessiz","enkalabalik"};
    String[] oylarS = {"En Kötü","En Kaliteli","En Ucuz","En Pahalı","En Güzel","En Rahat","En Sessiz","En Kalabalık"};

    String[] turler = {"kafe","cay","yemek"};
    String[] turlerS = {"Kafeler","Çay Bahçeleri ve Evleri","Yemek Restorantları"};
    String searchText,filtreOy,filtreTur,olay="hepsi";
    Button btnFiltre,btnSearch;
    JSONParser parser = new JSONParser();
    String [] isim,sehir,resim;
    int[] idler;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_discovery);
        sharedPreferences = getSharedPreferences(Variables.SHARED_NAME, Context.MODE_PRIVATE);
        ID = sharedPreferences.getInt("00", -1);
        btnFiltre = (Button)findViewById(R.id.discovery_btn_en);
        btnSearch = (Button)findViewById(R.id.discovery_btn_search);
        list = (ListView)findViewById(R.id.discovery_list);

        btnFiltre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SorTur();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search();
            }
        });

        new Mekanlar().execute(olay,searchText,filtreOy,filtreTur);
    }

    public void Search(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aramak istediğiniz mekanın adını giriniz");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Ara", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchText = input.getText().toString();
                olay="arama";
                new Mekanlar().execute(olay,searchText,filtreOy,filtreTur);
            }
        });
        builder.show();
    }

    public void SorTur(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aradığınız mekanlar nereler ?");
        final Spinner sp = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,turlerS);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spinnerArrayAdapter);
        builder.setView(sp);
        builder.setPositiveButton("Devam Et", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtreTur = turler[(int)sp.getSelectedItemId()];
                SorOy();
            }
        });
        builder.show();
    }

    public void SorOy(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Hangi türde en iyisi olsun ?");
        final Spinner sp = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,oylarS);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spinnerArrayAdapter);
        builder.setView(sp);
        builder.setPositiveButton("Listele", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtreOy = oylar[(int)sp.getSelectedItemId()];
                olay="filtre";
                new Mekanlar().execute(olay,searchText,filtreOy,filtreTur);
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_notif:
                Intent intent = new Intent(getBaseContext(),NotifsScreen.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
                finish();
                break;
            case R.id.action_discovery:
                Intent intent4 = new Intent(getBaseContext(),Discovery.class);
                intent4.putExtra("ID",ID);
                startActivity(intent4);
                finish();
                break;
            case R.id.action_home:
                Intent intent3 = new Intent(getBaseContext(),Homepage.class);
                intent3.putExtra("ID",ID);
                startActivity(intent3);
                finish();
                break;
            case R.id.action_profile:
                startActivity(new Intent(getBaseContext(),ProfileScreen.class));
                finish();
                break;
            case R.id.action_exit:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("00",-1);
                editor.commit();
                startActivity(new Intent(getBaseContext(),SplashScreen.class));
                finish();
                break;
            case R.id.action_developer:
                startActivity(new Intent(getBaseContext(),Developer.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Uygulamadan çıkmak için bir daha basın.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    class Mekanlar extends AsyncTask<String,String,String>{
        JSONObject json;
        ProgressDialog dialog;
        String olay,tur,arama,en;
        int success;
        List<String>    l_urls = new ArrayList<String>(),
                        l_names = new ArrayList<String>(),
                        l_city = new ArrayList<String>();
        List<Integer>   l_ids = new ArrayList<Integer>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Discovery.this);
            dialog.setTitle("Yükleniyor...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            olay = params[0];tur = params[3];arama=params[1];en=params[2];
            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            if(olay=="hepsi"){
                arg.add(new BasicNameValuePair("hepsi","hepsi"));
            }else if(olay=="arama"){
                arg.add(new BasicNameValuePair("arama",arama));
            }else if(olay=="filtre"){
                arg.add(new BasicNameValuePair("tur",tur));
                arg.add(new BasicNameValuePair("filtre",en));
            }
            json = parser.makeHttpRequest(Variables.URL_DISCOVERY,"GET",arg);
            try {
                success=json.getInt("succes");
                if(success==1){
                    JSONArray mekanlar=json.getJSONArray("mekanlar");
                    for (int i=0;i<mekanlar.length();i++){
                        JSONObject mekan = mekanlar.getJSONObject(i);
                        l_city.add(mekan.getString("sehir"));
                        l_ids.add(mekan.getInt("id"));
                        l_names.add(mekan.getString("name"));
                        l_urls.add(mekan.getString("picture"));
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
            if(success==1){
                MekanlariGoster(l_ids,l_names,l_city,l_urls);
            }
        }
    }

    private void MekanlariGoster(List<Integer> l_ids, List<String> l_names, List<String> l_city, List<String> l_urls) {
        isim = new String[l_names.size()];
        resim = new String[l_urls.size()];
        idler = new int[l_ids.size()];
        sehir = new String[l_city.size()];
        for (int i=0;i<l_names.size();i++){
            isim[i]    =   l_names.get(i);
            resim[i]     =   l_urls.get(i);
            idler[i]      =   l_ids.get(i);
            sehir[i]    =   l_city.get(i);
        }
        list.setAdapter(new DiscoveryAdapter(this,isim,idler,sehir,resim));
        olay="hepsi";
    }
}
