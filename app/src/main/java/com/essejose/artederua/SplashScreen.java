package com.essejose.artederua;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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

                    Log.d("TAG", String.valueOf(userDAO.check_login(response.body().getUsuario(),response.body().getSenha())));
                     //animacaoLoad();
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
        ImageView iv = (ImageView) findViewById(R.id.splash);
        if (iv != null) {
            iv.clearAnimation();
            iv.startAnimation(anim);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,
                        LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


}

