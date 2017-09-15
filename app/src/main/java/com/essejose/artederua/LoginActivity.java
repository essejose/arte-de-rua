package com.essejose.artederua;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.essejose.artederua.dao.UserDAO;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

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
        loginButton.setReadPermissions("public_profile email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                executeGraphRequest(loginResult.getAccessToken().getUserId());


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

    private void executeGraphRequest(final String userId){
        GraphRequest request =new GraphRequest(AccessToken.getCurrentAccessToken(), userId, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {




                try {
                    Log.i("FACEBOOK", (String) response.getJSONObject().get("name"));
                    Log.i("FACEBOOK", (String) response.getJSONObject().get("email"));

                    SharedPreferences sp =  getSharedPreferences("ARTEDERUAinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();

                    e.putString("email", (String) response.getJSONObject().get("email"));
                    e.putString("userName", (String) response.getJSONObject().get("name"));

                    if (cbContinuar.isChecked()) {

                        e.putBoolean("cbContinuar", cbContinuar.isChecked());

                        initApp();
                    }else{


                        initApp();
                    }

                    e.apply();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("FACEBOOK", String.valueOf(response.getJSONObject()));
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,id, email");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private boolean isConnected() {
        SharedPreferences sp = this.getSharedPreferences("ARTEDERUAinfo", Context.MODE_PRIVATE);
        Boolean cbContinuar = sp.getBoolean("cbContinuar", false);
        if (AccessToken.getCurrentAccessToken() != null) {
            return true;
        }else{
            return cbContinuar;
        }
    }


    public void logar (View v){

        if(isValidLogin()) {

            SharedPreferences sp = this.getSharedPreferences("ARTEDERUAinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();

            e.putString("userName", userName.getText().toString());
            e.putString("passWord", passWord.getText().toString());
            e.putString("email", "");

            if (cbContinuar.isChecked()) {

                e.putBoolean("cbContinuar", cbContinuar.isChecked());
               // Toast.makeText(this, "Login realizado com sucesso, seus dados foram salvos", Toast.LENGTH_SHORT).show();
                initApp();
            } else {

                e.putBoolean("cbContinuar", false);
                //Toast.makeText(this, "Login realizado com sucesso ", Toast.LENGTH_SHORT).show();
                initApp();
            }

            e.apply();
        }else{
            Toast.makeText(this, R.string.user_validation, Toast.LENGTH_SHORT).show();
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

        //Toast.makeText(this, "isConected", Toast.LENGTH_SHORT).show();
    }



}
