package com.essejose.artederua;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static com.essejose.artederua.R.id.imageView;

public class FotoActivity extends AppCompatActivity {

    Bundle extras;
    ImageView ivLogoEvent;
    Bitmap _bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        extras = getIntent().getExtras();
        ivLogoEvent = (ImageView) findViewById(R.id.ivLogoEvent);



            if(getIntent().hasExtra("uri")) {
///                ivLogoEvent.setImageBitmap((Bitmap) extras.get("Foto"));

                Picasso.with(this)
                        .load((Uri) extras.get("uri"))
                        .resize(50, 50)
                        .centerCrop()
                        .into(ivLogoEvent);

            }

        }

}
