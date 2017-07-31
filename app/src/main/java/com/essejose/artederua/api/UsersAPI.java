package com.essejose.artederua.api;

import com.essejose.artederua.model.User;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jose on 28/07/2017.
 */

public interface UsersAPI {
    @GET("v2/58b9b1740f0000b614f09d2f")
    Call<User> getUser();
}
