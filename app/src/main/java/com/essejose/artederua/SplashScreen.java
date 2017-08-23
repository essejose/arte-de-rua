package com.essejose.artederua;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.essejose.artederua.model.User;
import com.essejose.artederua.dao.UserDAO;
import com.essejose.artederua.api.UsersAPI;
import com.essejose.artederua.api.APIUtils;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3500;
    private UsersAPI usersAPI;


    UserDAO userDAO = new UserDAO(this);
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        checkUsers();

        TextView tx = (TextView)findViewById(R.id.editText2);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/UrbanJungleDEMO.otf");

        tx.setTypeface(custom_font);

    }

    private void checkUsers(){
        usersAPI = APIUtils.getUsersAPI();
        usersAPI.getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Log.i("TAG",response.body().getUsuario());
                    Log.i("TAG",response.body().getSenha());

                        user.setUsuario(response.body().getUsuario());
                        user.setSenha(response.body().getSenha());

                        userDAO.add(user);
                         Log.d("TAG", String.valueOf(userDAO.getAll()));

                    Log.d("TAG", String.valueOf(userDAO.check_login(response.body().getUsuario(),
                            response.body().getSenha())));
                     animacaoLoad();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void animacaoLoad() {
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.animacao_splash);

        anim.reset();
        //Pegando o nosso objeto criado no layout
        TextView iv = (TextView) findViewById(R.id.editText2);
        if (iv != null) {
            iv.clearAnimation();
            iv.startAnimation(anim);
        }
        TextView ivv = (TextView) findViewById(R.id.editText3);
        if (ivv != null) {
            ivv.clearAnimation();
            ivv.startAnimation(anim);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,
                        LoginActivity.class);

                startActivity(intent);
                SplashScreen.this.finish();

                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


}

