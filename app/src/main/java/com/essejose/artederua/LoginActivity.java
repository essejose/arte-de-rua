package com.essejose.artederua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.essejose.artederua.dao.UserDAO;

public class LoginActivity extends AppCompatActivity {

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



       // ler();

        if(isConnected())
            initApp();

    }

    private boolean isConnected() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        Boolean cbContinuar = sp.getBoolean("cbContinuar", false);

        return cbContinuar;
    }


    public void logar (View v){

        if(isValidLogin()) {
            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            if (cbContinuar.isChecked()) {
                e.putString("userName", userName.getText().toString());
                e.putString("passWord", passWord.getText().toString());
                e.putBoolean("cbContinuar", cbContinuar.isChecked());
                Toast.makeText(this, "Login realizado com sucesso, seus dados foram salvos", Toast.LENGTH_SHORT).show();
            } else {
                e.putString("userName", "");
                e.putString("passWord", "");
                e.putBoolean("cbContinuar", false);
                Toast.makeText(this, "Login realizado com sucesso ", Toast.LENGTH_SHORT).show();
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
