package com.essejose.artederua;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.essejose.artederua.adpter.EventAdpter;
import com.essejose.artederua.adpter.OnItemClickListner;
import com.essejose.artederua.dao.EventDAO;
import com.essejose.artederua.model.Event;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    public RecyclerView rvEvents;
    public EventAdpter EAdpter;

    private static final String TAG = "MyActivity";

    static int TAKE_PICTURE = 1;
    Bitmap bitMap;
    Bitmap originalBitmap;
    Bitmap resizedBitmap;
    ImageView ivTest;
    Bitmap _bitmap; // your bitmap


    final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/";
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        File newdir = new File(dir);
        newdir.mkdirs();


        ivTest = (ImageView) findViewById(R.id.ivTest);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();



                captureImage();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);



        EAdpter = new EventAdpter(new ArrayList<Event>(),
                new OnItemClickListner() {
                    @Override
                    public void OnItemClick(Event event) {
                        Intent telamapa = new Intent(
                                MainActivity.this,FotoActivity.class
                        );
                        telamapa.putExtra("LINHA",event );

                        startActivity(telamapa);
                    }

                });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvEvents.setLayoutManager(layoutManager);


        rvEvents.setAdapter(EAdpter);

        rvEvents.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration
                = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

        rvEvents.addItemDecoration(itemDecoration);



        EventDAO events  = new EventDAO(this);
        EAdpter.update(events.getAll());


        // carregaDados();

    }



    private void captureImage() {


        String file = dir+ DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg";
//
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        // start camera activity
//        startActivityForResult(cameraIntent, TAKE_PICTURE);


        File newfile = new File(file);

            try {
                newfile.createNewFile();
            } catch (IOException e) {}

            Uri outputFileUri =  FileProvider.getUriForFile(MainActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",newfile);


        mCurrentPhotoPath = "file:" + newfile.getAbsolutePath();

        ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            values.put("u", String.valueOf(outputFileUri));
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, TAKE_PICTURE);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == TAKE_PICTURE && resultCode== RESULT_OK && intent != null){
            // get bundle
            Bundle extras = intent.getExtras();

            Uri imageUri = Uri.parse(mCurrentPhotoPath);

           // File file = new File(imageUri.getPath());


            Log.d("Tag", String.valueOf(mCurrentPhotoPath));

            File file = new File(imageUri.getPath());
            try {
                InputStream ims = new FileInputStream(file);
                ivTest.setImageBitmap(BitmapFactory.decodeStream(ims));
            } catch (FileNotFoundException e) {
                return;
            }


            // get

//            bitMap  = (Bitmap) extras.get("data");
//
////            resizedBitmap = Bitmap.createScaledBitmap(
////                    bitMap, 400,400, true);
////
//
//            if(intent.hasExtra("uri")) {
//
//                Log.d("Tag", "tem name");
//
//            }
//
//          //  ivTest.setImageBitmap(bitMap);
//
//            Intent fotoActivty = new Intent( this, FotoActivity.class);
//            fotoActivty.putExtra("Foto",bitMap );
//            fotoActivty.putExtra("uri",imageUri);
//
//            this.startActivity(fotoActivty);

            //bitMap.recycle();



        }
    }
    private void carregaDados() {

        EventDAO events  = new EventDAO(this);
        Event fakeEvent = new Event();


//        fakeEvent.set_id(2);
//        fakeEvent.set_id_user(2);
//        fakeEvent.setTitle("Novo Registro");
//        fakeEvent.setDescripion("Novo  Description");
//        fakeEvent.setImage("blal/bllba");
//        fakeEvent.setLongitude(00.555);
//        fakeEvent.setLatiude(00.555);
//
//
//        events.add(fakeEvent);

        // Log.v("Cursor Object", String.valueOf(events.getAll()));

        EAdpter.update(events.getAll());

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        if (id == R.id.action_logout) {

            logout();

        }

        return super.onOptionsItemSelected(item);
    }


    public void logout(){

        SharedPreferences sp =  getSharedPreferences("ARTEDERUAinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("cbContinuar", false);
        e.apply();

        Log.i("TAG", String.valueOf(AccessToken.getCurrentAccessToken()));
        if (AccessToken.getCurrentAccessToken() == null) {
            Log.i("TAG", "'x");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            this.startActivity(intent);
            finish();
            return;
        }else{

            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    LoginManager.getInstance().logOut();



                }
            }).executeAsync();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }






        return;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
