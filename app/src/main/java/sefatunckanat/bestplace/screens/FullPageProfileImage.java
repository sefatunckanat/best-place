package sefatunckanat.bestplace.screens;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import sefatunckanat.bestplace.R;
import sefatunckanat.bestplace.libs.libImageLoader.ImageLoader;

public class FullPageProfileImage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_fullpageimage);

        String url = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("URL");
        }

        ImageView image = (ImageView) findViewById(R.id.fullpageimage_img);

        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
        imgLoader.DisplayImage(url,R.drawable.ic_launcher,image);
    }
}
