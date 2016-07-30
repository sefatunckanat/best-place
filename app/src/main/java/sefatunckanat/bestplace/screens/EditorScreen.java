package sefatunckanat.bestplace.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sefatunckanat.bestplace.R;
import sefatunckanat.bestplace.libs.libCameraUpload.AndroidMultiPartEntity;
import sefatunckanat.bestplace.libs.libImageLoader.ImageLoader;
import sefatunckanat.bestplace.libs.parser.JSONParser;
import sefatunckanat.bestplace.utils.Functions;
import sefatunckanat.bestplace.utils.Variables;

public class EditorScreen extends Activity {
    int ID;
    EditText et_name,et_username,et_bio;
    ImageView picture;
    Button btn_edit;
    String url,city;
    static private ImageLoader imgLoader;
    JSONParser parser = new JSONParser();
    public List<String> city_ids = new ArrayList<String>();
    public List<String> city_names = new ArrayList<String>();
    public Spinner sp_citys;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int SELECT_PHOTO = 200;
    private Uri fileUri;
    public String changeImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_edit);

        et_name = (EditText)findViewById(R.id.edit_name);
        et_username = (EditText)findViewById(R.id.edit_username);
        et_bio = (EditText)findViewById(R.id.edit_bio);
        picture = (ImageView)findViewById(R.id.edit_picture);
        btn_edit = (Button)findViewById(R.id.edit_edit);
        sp_citys = (Spinner)findViewById(R.id.edit_sp_sehirler);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            ID = extras.getInt("ID");
            et_name.setText(extras.getString("name").trim());
            et_username.setText(extras.getString("username").trim());
            et_bio.setText(extras.getString("bio").trim());
            url = extras.getString("url");
            city = extras.getString("city");
        }else{
            finish();
        }

        imgLoader = new ImageLoader(getApplicationContext());
        imgLoader.DisplayImage(url,R.drawable.ic_launcher,picture);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Edit().execute(
                        et_name.getText().toString(),
                        et_username.getText().toString(),
                        et_bio.getText().toString(),
                        sp_citys.getSelectedItem().toString()
                );
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog builder = new AlertDialog.Builder(EditorScreen.this)
                        .setTitle("Profil Resmini Seç")
                        .setPositiveButton("Galeriden Seç", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                startActivityForResult(i,SELECT_PHOTO);
                            }
                        })
                        .setNegativeButton("Kameradan Çek", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                            }
                        })
                        .show();
            }
        });

        new GetCitys().execute();
    }

    public void sec(){
        int id = -1;
        for(int i=0;i<city_ids.size();i++){
            if(city_names.get(i).contains(city))
                id = i;
        }
        sp_citys.setSelection(id);
    }

    class Edit extends AsyncTask<String,String,String>{
        JSONObject json;
        int succes;
        String message;
        ProgressDialog pDialog;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if(succes==1){
                finish();
            }
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditorScreen.this);
            pDialog.setMessage("Profiliniz Güncelleniyor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String bio,username,name,city;
            name=params[0];
            username=params[1];
            bio=params[2];
            city = params[3];

            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            arg.add(new BasicNameValuePair("name",name));
            arg.add(new BasicNameValuePair("username",username));
            arg.add(new BasicNameValuePair("bio",bio));
            arg.add(new BasicNameValuePair("city",city));
            arg.add(new BasicNameValuePair("ID",ID+""));

            Functions.log(arg.toString());
            json = parser.makeHttpRequest(Variables.URL_SET_USER,"POST",arg);
            Functions.log(json.toString());
            try{
                succes = json.getInt("succes");
                message = json.getString("message");
            }catch (Exception e){

            }
            return null;
        }
    }

    class GetCitys extends AsyncTask<String, String, String> {
        JSONObject json;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditorScreen.this);
            progressDialog.setMessage("Yükleniyor...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(EditorScreen.this,R.layout.spinner_item,city_names);
            sp_citys.setAdapter(adapter);
            progressDialog.dismiss();
            sec();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> arg = new ArrayList<NameValuePair>();
            json = parser.makeHttpRequest(Variables.URL_CITYS, "POST", arg);
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

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"BestPlace"
                );
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                        changeImagePath=fileUri.getPath();
                    break;

                case SELECT_PHOTO:
                        changeImagePath = getPath(data.getData());
                    break;
            }
            new UploadProfileImage().execute();
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                changeImagePath=fileUri.getPath();
            }
        }else if(requestCode == SELECT_PHOTO){
            if (resultCode == RESULT_OK) {
                changeImagePath = getPath(data.getData());
            }
        }
    }

    int column_index;
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private class UploadProfileImage extends AsyncTask<Void, Integer, String> {
        ProgressDialog dialog;
        long totalSize = 0;
        int succes;
        JSONObject json;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(EditorScreen.this);
            dialog.setTitle("Yükleniyor...");
            dialog.setProgress(0);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            dialog.setProgress(progress[0]);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = uploadFile();
            Functions.log(result);
            try {
                json = new JSONObject(result);
                succes = json.getInt("succes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Variables.URl_CHANGE_IMAGE);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                File sourceFile = new File(changeImagePath);
                entity.addPart("image", new FileBody(sourceFile));

                entity.addPart("ID",new StringBody(ID+""));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "{'succes':'"+statusCode+"','message':'Hata'}";
                }

            } catch (ClientProtocolException e) {
                responseString = "{'succes':'"+-1+"','message':'"+e.toString()+"'}";
            } catch (IOException e) {
                responseString = "{'succes':'"+-1+"','message':'"+e.toString()+"'}";
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(succes==1){
                finish();
            }
            dialog.dismiss();
        }

    }
}
