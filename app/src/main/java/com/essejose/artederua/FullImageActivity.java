package com.essejose.artederua;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.essejose.artederua.model.Event;
import com.squareup.picasso.Picasso;

import java.io.File;

public class FullImageActivity extends AppCompatActivity {

    ImageView fullImage;
    private Event event;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);


        ZoomableImageView touch = (ZoomableImageView)findViewById(R.id.imageView);


        if(getIntent().hasExtra("EVENT")){
            event = getIntent().getParcelableExtra("EVENT");

            Bitmap bitmap =  BitmapFactory.decodeFile(event.getImage());
            touch.setImageBitmap(bitmap);
//

//            File file = new File(mCurrentPhotoPath);
//            Log.d("Vindo do banco file ", String.valueOf(file));

//            Picasso.with(this).load(file).config(Bitmap.Config.RGB_565)
//                    .fit()
//                    .centerCrop()
//                    .placeholder(android.R.drawable.ic_menu_camera)
//                    .error(android.R.drawable.stat_notify_error)
//                    .into(fullImage);



        }
    }
}
