package sefatunckanat.bestplace.screens;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sefatunckanat.bestplace.R;
import sefatunckanat.bestplace.adapters.HomeAdapter;
import sefatunckanat.bestplace.libs.parser.JSONParser;
import sefatunckanat.bestplace.utils.Functions;
import sefatunckanat.bestplace.utils.Variables;

public class Homepage extends Activity {
    int ID;
    SharedPreferences sharedPreferences;
    JSONParser parser = new JSONParser();
    String [] isim,resim,olay,mekan,tarih;
    int[] idler;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_home);
        sharedPreferences = getSharedPreferences(Variables.SHARED_NAME, Context.MODE_PRIVATE);
        ID = sharedPreferences.getInt("00", -1);
        list=(ListView)findViewById(R.id.home_list);

        new Home().execute();
    }

    private void UpdateHomePage(List<String> isim, List<String> resim, List<String> olay, List<String> mekan, List<String> tarih, List<Integer> idler) {
        this.isim = new String[isim.size()];
        this.resim = new String[resim.size()];
        this.olay = new String[isim.size()];
        this.mekan = new String[isim.size()];
        this.tarih = new String[isim.size()];
        this.idler = new int[idler.size()];
        for (int i=0;i<idler.size();i++){
            this.isim[i] = isim.get(i);
            this.resim[i] = resim.get(i);
            this.olay[i] = olay.get(i);
            this.mekan[i] = mekan.get(i);
            this.tarih[i] = tarih.get(i);
            this.idler[i] = idler.get(i);
        }
        list.setAdapter(new HomeAdapter(this,this.isim,this.resim,this.olay,this.mekan,this.tarih,this.idler));
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

    class Home extends AsyncTask<String,String,String>{
        JSONObject json;
        List<String> isim = new ArrayList<String>(),
                    resim = new ArrayList<String>(),
                    olay = new ArrayList<String>(),
                    mekan = new ArrayList<String>(),
                    tarih = new ArrayList<String>();
        List<Integer>   idler = new ArrayList<Integer>();
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(Homepage.this);
            dialog.setCancelable(false);
            dialog.setTitle("Yükleniyor...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            arg.add(new BasicNameValuePair("id",ID+""));
            json=parser.makeHttpRequest(Variables.URL_HOME,"GET",arg);
            try {
                JSONArray olaylar = json.getJSONArray("feed");
                for(int i=0;i<olaylar.length();i++){
                    JSONObject o = olaylar.getJSONObject(i);
                    isim.add(o.getString("name"));
                    resim.add(o.getString("url"));
                    olay.add(o.getString("olay"));
                    mekan.add(o.getString("place_name"));
                    tarih.add(o.getString("tarih"));
                    idler.add(o.getInt("other"));
                }
            }catch (JSONException e){}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            UpdateHomePage(isim,resim,olay,mekan,tarih,idler);
        }
    }
}
