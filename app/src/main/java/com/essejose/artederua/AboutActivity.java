package com.essejose.artederua;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView etversion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        PackageManager manager = this.getPackageManager();

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/UrbanJungleDEMO.otf");




        etversion.setTypeface(custom_font);
        etversion = (TextView) findViewById(R.id.editText4);

        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            if (info != null) {
                etversion.setText(info.versionName);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }
}
