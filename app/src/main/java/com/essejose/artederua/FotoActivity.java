package com.essejose.artederua;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.essejose.artederua.dao.EventDAO;
import com.essejose.artederua.model.Event;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FotoActivity extends AppCompatActivity {

    Bundle extras;
    ImageView ivLogoEvent;
    Button btnSalve;
    EditText etitle;
    EditText etdescription;

    Button btnDelete;

    static int TAKE_PICTURE = 1;
    String mCurrentPhotoPath;
    String imageFileName;
    String timeStamp;
    File storageDir;
    Criteria criteria;
    Location location;

    private LocationManager locationManager;
    private String provider;
    private Event event;
    private double lat;
    private double lng;

    final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Arte/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);


        ivLogoEvent = (ImageView) findViewById(R.id.ivLogoEvent);
        btnSalve = (Button) findViewById(R.id.btnSalve);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        etitle = (EditText) findViewById(R.id.etitle);
        etdescription = (EditText) findViewById(R.id.etdescription);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);



        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            btnSalve.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION  }, 0);

        }


        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        }


        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Log.d("Localizacao","nao diposnivel");
        }


        if(getIntent().hasExtra("EVENT")){


            event = getIntent().getParcelableExtra("EVENT");

            etitle.setText(event.getTitle());
            etdescription.setText(event.getDescripion());
            mCurrentPhotoPath = event.getImage();


            File file = new File(mCurrentPhotoPath);
            Log.d("Vindo do banco file ", String.valueOf(file));

            Picasso.with(this).load(file).config(Bitmap.Config.RGB_565)
                    .fit()
                    .centerCrop()
                    .error(android.R.drawable.stat_notify_error)
                    .into(ivLogoEvent);


            btnDelete.setVisibility(View.VISIBLE);

        }

        ivLogoEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            criarImage();

            }
        });

        btnSalve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etitle.getText().toString().isEmpty()) {
                    return;

                } else if (etdescription.getText().toString().isEmpty()){
                     return;

            }else if( ivLogoEvent.getDrawable() == null){
                return;
            }else {
                createEvent();
            }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEvent();
            }
        });

    }


    public void onLocationChanged(Location location) {
         lat = (location.getLatitude());
         lng =  (location.getLongitude());

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        return image;
    }

    private void  criarImage(){

//        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        imageFileName = timeStamp + ".jpg";
//        storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//
//        mCurrentPhotoPath = storageDir.getAbsolutePath() + "/" + imageFileName;
//
//        File file = new File(mCurrentPhotoPath);
//        Uri outputFileUri = Uri.fromFile(file);

        try {
            Uri outputFileUri = FileProvider.getUriForFile(FotoActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    createImageFile());

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(cameraIntent, TAKE_PICTURE);

        } catch (IOException e) {
            e.printStackTrace();
        }





    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);

    }



    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnSalve.setEnabled(true);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       if (requestCode == TAKE_PICTURE && resultCode== RESULT_OK && data != null) {


           Uri imageUri = Uri.parse(mCurrentPhotoPath);
           File file = new File(imageUri.getPath());
           Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

           // Chame este método pra obter a URI da imagem
           Uri uri = getImageUri(getApplicationContext(), myBitmap);

           // Em seguida chame este método para obter o caminho do arquivo
          file = new File(getRealPathFromURI(uri));

           mCurrentPhotoPath = String.valueOf(file.getPath());

           Log.i("TAG", String.valueOf(mCurrentPhotoPath));
           Picasso.with(FotoActivity.this)
                   .load(file)
                   .config(Bitmap.Config.RGB_565)
                   .fit()
                   .centerCrop()
                   .error(android.R.drawable.stat_notify_error)
                   .noFade()
                   .into(ivLogoEvent);




//           Bundle extras = data.getExtras();
//           Bitmap imageBitmap = (Bitmap) extras.get("data");
//           ivLogoEvent.setImageBitmap(imageBitmap);




      //     File imgFile = new File(mCurrentPhotoPath);




           /*
           Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
           Bitmap photo = (Bitmap) data.getExtras().get("data");

           // Chame este método pra obter a URI da imagem
           Uri uri = getImageUri(getApplicationContext(), myBitmap);

           // Em seguida chame este método para obter o caminho do arquivo
           File file = new File(getRealPathFromURI(uri));

           mCurrentPhotoPath = String.valueOf(file.getPath());

           Picasso.with(FotoActivity.this)
                   .load(file)
                   .config(Bitmap.Config.RGB_565)
                   .fit()
                   .centerCrop()
                   .error(android.R.drawable.stat_notify_error)
                   .noFade()
                   .into(ivLogoEvent);

            }
            */

       }

    }


    private void createEvent(){
        EventDAO events  = new EventDAO(this);
        Event newEvent = new Event();
        newEvent.set_id_user(2);
        newEvent.setTitle(etitle.getText().toString());
        newEvent.setDescripion(etdescription.getText().toString());
        newEvent.setImage(mCurrentPhotoPath);
        newEvent.setLongitude(lat);
        newEvent.setLatiude(lng);

        if(getIntent().hasExtra("EVENT")){

            newEvent.set_id(event.get_id());
            events.updade(newEvent);

        }else{
            events.add(newEvent);
        }



        Intent mainback = new Intent( this, MainActivity.class);
        this.startActivity(mainback);
        finish();

  }

    private void deleteEvent() {

        final EventDAO events  = new EventDAO(this);
        new AlertDialog.Builder(this)
                .setTitle("Deletando imagem")
                .setMessage("Tem certeza que deseja deletar essa linda imagem?")
                .setPositiveButton("sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                Event deleteEvent = new Event();

                                deleteEvent.set_id(event.get_id());
                                events.delete(deleteEvent);

                                Intent mainback = new Intent( FotoActivity.this, MainActivity.class);
                                startActivity(mainback);
                                finish();
                            }
                        })
                .setNegativeButton("não", null)
                .show();


    }


}
