package com.essejose.artederua;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.essejose.artederua.adpter.EventAdpter;
import com.essejose.artederua.adpter.OnItemClickListner;
import com.essejose.artederua.dao.EventDAO;
import com.essejose.artederua.model.Event;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    public RecyclerView rvEvents;
    public EventAdpter EAdpter;


    TextView tvName1;
    TextView tvName2;
    TextView emptyView;
    private static final String TAG = "MyActivity";

    private FirebaseAnalytics mFirebaseAnalitics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//        File newdir = new File(dir);
//        newdir.mkdirs();
//

        mFirebaseAnalitics = FirebaseAnalytics.getInstance(this);

        FirebaseMessaging.getInstance().subscribeToTopic("arte");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        View hView =  navigationView.getHeaderView(0);
        tvName1 = (TextView)hView.findViewById(R.id.tvName);
        tvName2 = (TextView)hView.findViewById(R.id.tvName2);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/UrbanJungleDEMO.otf");


        SharedPreferences preferences =
                getSharedPreferences("ARTEDERUAinfo", MODE_PRIVATE);


        String name = preferences.getString("userName", "");
        String email = preferences.getString("email", "");
        tvName1.setText(name);
        tvName2.setText(email);


        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION  }, 0);

        }
        EAdpter = new EventAdpter(getApplicationContext(),new ArrayList<Event>(),
                new OnItemClickListner() {
                    @Override
                    public void OnItemClick(Event event) {


                        Intent telamapa = new Intent(
                                MainActivity.this,FullImageActivity.class

                        );
                        telamapa.putExtra("EVENT",event );

                        startActivity(telamapa);
                    }

                    @Override
                    public void OnItemClick2(Event event) {

                        Intent telamapa = new Intent(
                                // MainActivity.this,FotoActivity.class
                                MainActivity.this,MapsActivity.class
                        );
                        telamapa.putExtra("EVENT",event );

                        startActivity(telamapa);

                    }

                    @Override
                    public void OnItemClick3(Event event) {

                        Intent telamapa = new Intent(
                                MainActivity.this,FotoActivity.class

                        );
                        telamapa.putExtra("EVENT",event );

                        startActivity(telamapa);


                    }

                    @Override
                    public void shareFacebook(Event event) {

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, " ");
                        shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(event.getImage())));
                        shareIntent.setType("image/jpeg");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "send"));
                    }

                });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvEvents.setLayoutManager(layoutManager);

        emptyView = (TextView)  findViewById(R.id.empty_view);
        rvEvents.setAdapter(EAdpter);

        rvEvents.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration
                = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

        rvEvents.addItemDecoration(itemDecoration);



        EventDAO events  = new EventDAO(this);
        EAdpter.update(events.getAll());


          carregaDados();

    }



    private void captureImage() {

        Intent fotoActivty = new Intent( this, FotoActivity.class);
        startActivity(fotoActivty);

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



        if (events.getAll().isEmpty()) {
            rvEvents.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            rvEvents.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        EAdpter.update(events.getAll());

    }


    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.action_logout) {

            logout();

        }

        return super.onOptionsItemSelected(item);
    }


    public void logout(){

        SharedPreferences sp =  getSharedPreferences("ARTEDERUAinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("cbContinuar", false);
        e.putString("email", "");
        e.putString("userName", "");
        e.apply();

        Log.i("TAG", String.valueOf(AccessToken.getCurrentAccessToken()));
        if (AccessToken.getCurrentAccessToken() == null) {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }else{

            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    LoginManager.getInstance().logOut();


                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).executeAsync();

        }






        return;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            captureImage();
         } else if (id == R.id.nav_galery) {

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);


         } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);


//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
