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
import sefatunckanat.bestplace.libs.parser.JSONParser;
import sefatunckanat.bestplace.adapters.NotifCustomAdapter;
import sefatunckanat.bestplace.utils.Variables;

public class NotifsScreen extends Activity {

    SharedPreferences sharedPreferences;
    public static String[] urls,names,dates;
    public static int[] ids;
    Context context;
    ListView listNotif;
    JSONParser parser = new JSONParser();
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_notif);
        sharedPreferences = getSharedPreferences(Variables.SHARED_NAME, Context.MODE_PRIVATE);
        listNotif = (ListView)findViewById(R.id.notif_listview);

        context = this;
        sharedPreferences = getSharedPreferences(Variables.SHARED_NAME,Context.MODE_PRIVATE);
        ID = sharedPreferences.getInt("00", -1);

        new GetNotifs().execute();
    }

    public void Display(List<String> name,List<String> date,List<String> url,List<Integer> id){
        names = new String[name.size()];
        urls = new String[date.size()];
        ids = new int[url.size()];
        dates = new String[id.size()];
        for (int i=0;i<name.size();i++){
            names[i]    =   name.get(i);
            urls[i]     =   url.get(i);
            ids[i]      =   id.get(i);
            dates[i]    =   date.get(i);
        }
        listNotif.setAdapter(new NotifCustomAdapter(this,names,ids,dates,urls));
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

    class GetNotifs extends AsyncTask<String,String,String>{
        int success;
        JSONObject notifs;
        ProgressDialog dialog;
        List<String>    l_urls = new ArrayList<String>(),
                        l_names = new ArrayList<String>(),
                        l_dates = new ArrayList<String>();
        List<Integer>   l_ids = new ArrayList<Integer>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(NotifsScreen.this);
            dialog.setTitle("Yükleniyor...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            arg.add(new BasicNameValuePair("USER",ID+""));
            notifs = parser.makeHttpRequest(Variables.URL_GET_NOTIFS, "GET", arg);
            try {
                success = notifs.getInt("succes");
                if(success==1){
                    JSONArray notifler = notifs.getJSONArray("notifs");
                    for (int i = 0; i < notifler.length(); i++) {
                        JSONObject notif = notifler.getJSONObject(i);
                        l_dates.add(notif.getString("tarih"));
                        l_names.add(notif.getString("uye_ad"));
                        l_urls.add(notif.getString("resim"));
                        l_ids.add(notif.getInt("uye_id"));
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
            Display(l_names,l_dates,l_urls,l_ids);
        }
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
}
