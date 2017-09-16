package com.essejose.artederua;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.essejose.artederua.dao.UserDAO;
import com.essejose.artederua.model.User;

public class UserActivity extends AppCompatActivity {

    UserDAO userDAO = new UserDAO(this);

    private EditText userName;
    private EditText passWord;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        TextInputLayout tx = (TextInputLayout)findViewById(R.id.usernameWrapper);
        TextInputLayout txx = (TextInputLayout)findViewById(R.id.passwordWrapper);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/UrbanJungleDEMO.otf");

        tx.setTypeface(custom_font);
        txx.setTypeface(custom_font);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerValid(view);


            }
        });


    }

    public void registerValid(View v){
        if (userName.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.user_validation_empty_user, Toast.LENGTH_SHORT).show();
            return;

        } else if (passWord.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.user_validation_empty_password, Toast.LENGTH_SHORT).show();
            return;

        }else if(userDAO.check_login(userName.getText().toString(),passWord.getText().toString())){
            Toast.makeText(this, R.string.user_validation_have, Toast.LENGTH_SHORT).show();
            return;
        }else{
            register(v);
        }
    }

    public void register(View v){

            User user = new User();

            user.setUsuario(userName.getText().toString());
            user.setSenha(passWord.getText().toString());

            userDAO.add(user);
            startActivity(new Intent(this, LoginActivity.class));
            finish();


    }


}
