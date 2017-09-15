package com.essejose.artederua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView etversion, logo, tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        PackageManager manager = this.getPackageManager();

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/UrbanJungleDEMO.otf");




        etversion = (TextView) findViewById(R.id.editText4);
        logo = (TextView) findViewById(R.id.editText2);
        tel = (TextView) findViewById(R.id.editText6);


        logo.setTypeface(custom_font);
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            if (info != null) {
                etversion.setText(info.versionName);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel.getText().toString().trim()));
                startActivity(intent);
            }
        });
    }
}
