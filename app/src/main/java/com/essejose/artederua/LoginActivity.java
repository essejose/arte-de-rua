package com.essejose.artederua;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.essejose.artederua.dao.UserDAO;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {


    CallbackManager callbackManager;

    UserDAO userDAO = new UserDAO(this);
    private EditText userName;
    private EditText passWord;
    private CheckBox cbContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        cbContinuar = (CheckBox)  findViewById(R.id.cbContinuar);

        TextInputLayout tx = (TextInputLayout)findViewById(R.id.usernameWrapper);
        TextInputLayout txx = (TextInputLayout)findViewById(R.id.passwordWrapper);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/UrbanJungleDEMO.otf");

        tx.setTypeface(custom_font);
        txx.setTypeface(custom_font);
        cbContinuar.setTypeface(custom_font);


        // ler();

         if(isConnected())
            initApp();




        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton)
                findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (cbContinuar.isChecked()) {
                    SharedPreferences sp =  getSharedPreferences("ARTEDERUAinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean("cbContinuar", cbContinuar.isChecked());
                    // Toast.makeText(this, "Login realizado com sucesso, seus dados foram salvos", Toast.LENGTH_SHORT).show();
                    initApp();
                }else{
                    initApp();
                }
                //Toast.makeText(LoginActivity.this,"Sucesso",Toast.LENGTH_LONG);

            }

            @Override
            public void onCancel() {

                //Toast.makeText(LoginActivity.this,"Cancel",Toast.LENGTH_LONG);
            }

            @Override
            public void onError(FacebookException error) {

                //Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_LONG);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private boolean isConnected() {
        SharedPreferences sp = this.getSharedPreferences("ARTEDERUAinfo", Context.MODE_PRIVATE);
        Boolean cbContinuar = sp.getBoolean("cbContinuar", false);
//        if (AccessToken.getCurrentAccessToken() != null) {
//            return true;
//        }
        return cbContinuar;
    }


    public void logar (View v){

        if(isValidLogin()) {

            SharedPreferences sp = this.getSharedPreferences("ARTEDERUAinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            if (cbContinuar.isChecked()) {
                e.putString("userName", userName.getText().toString());
                e.putString("passWord", passWord.getText().toString());
                e.putBoolean("cbContinuar", cbContinuar.isChecked());
               // Toast.makeText(this, "Login realizado com sucesso, seus dados foram salvos", Toast.LENGTH_SHORT).show();
                initApp();
            } else {
                e.putString("userName", "");
                e.putString("passWord", "");
                e.putBoolean("cbContinuar", false);
                //Toast.makeText(this, "Login realizado com sucesso ", Toast.LENGTH_SHORT).show();
                initApp();
            }

            e.apply();
        }else{
            Toast.makeText(this, "Nao e valido", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean isValidLogin() {
        String user = userName.getText().toString();
        String pass = passWord.getText().toString();

        return userDAO.check_login(user,pass);


    }

    private void initApp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();

        Toast.makeText(this, "isConected", Toast.LENGTH_SHORT).show();
    }



}
