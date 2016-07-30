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
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import sefatunckanat.bestplace.adapters.ActivitysAdapter;
import sefatunckanat.bestplace.utils.Functions;
import sefatunckanat.bestplace.utils.Variables;

public class ProfileScreen extends Activity {

    SharedPreferences sharedPreferences;
    JSONParser parser = new JSONParser();
    static public int cityID,ID,meID;
    public static String name,username,bio,follow,followers,city,url = "http://ozlemtulek.baun.edu.tr/sefatunckanat/image/user.png";
    static private TextView tvBio,tvName,tvUsername,tvFollow,tvFollowers,tvEdit,tvCity,
            tvBtnFollow;
    static private ImageLoader imgLoader;
    static ImageView image;
    static public List<Integer> takip_listesi = new ArrayList<Integer>();
    static public boolean Takip = false;
    static public ListView listView;
    static public ProfileScreen T;
    public static String[] olayName,olayDate,olayUrl,olayO;
    public static int[] olayId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_profile);
        sharedPreferences = getSharedPreferences(Variables.SHARED_NAME,Context.MODE_PRIVATE);
        T = this;

        tvBio = (TextView)findViewById(R.id.profil_bio);
        tvName = (TextView)findViewById(R.id.profil_name);
        tvUsername = (TextView)findViewById(R.id.profil_username);
        tvFollowers = (TextView)findViewById(R.id.profil_follewers);
        tvFollow = (TextView)findViewById(R.id.profil_follow);
        tvEdit = (TextView)findViewById(R.id.profil_edit);
        tvCity = (TextView)findViewById(R.id.profil_city);
        tvBtnFollow=(TextView)findViewById(R.id.profil_btn_follow);
        listView=(ListView)findViewById(R.id.profil_list);

        tvBtnFollow.setVisibility(View.INVISIBLE);
        ID = sharedPreferences.getInt("00", -1);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.containsKey("ME")) {
                tvEdit.setVisibility(View.INVISIBLE);
                tvBtnFollow.setVisibility(View.VISIBLE);
                meID = ID;
            }
            ID = extras.getInt("ID");

        }
        if(meID==ID){
            tvEdit.setVisibility(View.VISIBLE);
            tvBtnFollow.setVisibility(View.INVISIBLE);
            ID = meID;
        }
        Functions.log("Me ID : "+meID+" Gösterilen ID : "+ID);

        image = (ImageView) findViewById(R.id.profile_picture);

        imgLoader = new ImageLoader(getApplicationContext());
        imgLoader.DisplayImage(url,R.drawable.ic_launcher,image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),FullPageProfileImage.class);
                intent.putExtra("URL",url);
                startActivity(intent);
            }
        });

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EditorScreen.class);
                intent.putExtra("ID", ID);
                intent.putExtra("name",name);
                intent.putExtra("username",username);
                intent.putExtra("bio",bio);
                intent.putExtra("url",url);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });

        tvBtnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetFollow().execute();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetUserInfo().execute();
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
                startActivity(new Intent(ProfileScreen.this,ProfileScreen.class));
                finish();
                break;
            case R.id.action_exit:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("00",-1);
                editor.commit();
                startActivity(new Intent(ProfileScreen.this,SplashScreen.class));
                finish();
                break;
            case R.id.action_developer:
                startActivity(new Intent(getBaseContext(),Developer.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    static public void Update(){
        tvBio.setText(bio);
        tvName.setText(name);
        T.setTitle(name);
        tvUsername.setText("@"+username);
        tvFollow.setText("Takip Ettikleri : "+follow);
        tvFollowers.setText("Takipçileri : "+followers);
        tvCity.setText(city);

        url = Variables.URl_PROFILE_IMAGE+url;
        imgLoader.DisplayImage(url,R.drawable.ic_launcher,image);

        for(int i=0;i<takip_listesi.size();i++){
            if(takip_listesi.get(i) == meID){
                tvBtnFollow.setText("Takipten Çık");
                Takip = true;
            }else{
                tvBtnFollow.setText("Takip Et");
                Takip = false;
            }
        }

        listView.setAdapter(new ActivitysAdapter(T,olayName,olayId,olayDate,olayUrl,olayO));
    }

    class GetUserInfo extends AsyncTask<String,String,String>{
        JSONObject json;
        int succes,cityID;
        String message,username,bio,name,image,follow,followers,city;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProfileScreen.this);
            progressDialog.setMessage("Yükleniyor...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if(succes==1){
                ProfileScreen.name = name;
                ProfileScreen.username = username;
                ProfileScreen.bio = bio;
                ProfileScreen.url = image;
                ProfileScreen.follow = follow;
                ProfileScreen.followers = followers;
                ProfileScreen.city = city;
                ProfileScreen.cityID = cityID;
                ProfileScreen.Update();
            }else{
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                finish();
            }
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            arg.add(new BasicNameValuePair("USER",ID+""));
            json = parser.makeHttpRequest(Variables.URL_GET_USER,"GET",arg);
            try{
                succes = json.getInt("succes");
                if(succes==1){
                    username = json.getString("username");
                    name = json.getString("name");
                    bio = json.getString("bio");
                    image = json.getString("image");
                    follow = json.getInt("follow")+"";
                    followers = json.getInt("followers")+"";
                    city = json.getString("city");
                    cityID = json.getInt("cityID");
                    JSONArray takipciler = json.getJSONArray("takipciler");
                    takip_listesi.clear();
                    for(int i=0;i<takipciler.length();i++){
                        JSONObject o = takipciler.getJSONObject(i);
                        takip_listesi.add(o.getInt("id"));
                    }

                    JSONArray olayla = json.getJSONArray("olaylar");
                    olayDate=new String[olayla.length()];
                    olayName=new String[olayla.length()];
                    olayUrl=new String[olayla.length()];
                    olayId=new int[olayla.length()];
                    olayO=new String[olayla.length()];
                    for(int i=0;i<olayla.length();i++){
                        JSONObject o = olayla.getJSONObject(i);
                        olayDate[i]=o.getString("tarih");
                        olayName[i]=o.getString("name");
                        olayId[i]=o.getInt("id");
                        olayO[i]=o.getString("olay");
                        olayUrl[i]=o.getString("picture");
                    }
                }else{
                    message = json.getString("message");
                }
            }catch (JSONException e){

            }

            return null;
        }
    }

    class SetFollow extends AsyncTask<String,String,String>{
        ProgressDialog progressDialog;
        JSONObject json;
        int succes;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProfileScreen.this);
            progressDialog.setMessage("Değişiklikler uygulanıyor.");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            arg.add(new BasicNameValuePair("kim",meID+""));
            arg.add(new BasicNameValuePair("kimi",ID+""));
            arg.add(new BasicNameValuePair("ne",Takip?"0":"1"));

            json = parser.makeHttpRequest(Variables.URL_CHANGE_FOLLOW,"GET",arg);
            try {
                succes=json.getInt("succes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(succes==1){
                Toast.makeText(ProfileScreen.this,"İşlem başarılı.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ProfileScreen.this,"Birşeyler ters gitti.",Toast.LENGTH_SHORT).show();
            }
            finish();
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