package com.essejose.artederua.api;

/**
 * Created by jose on 28/07/2017.
 */

public class APIUtils {

    private APIUtils() {}
    public static final String BASE_URL = "http://www.mocky.io";

    public static UsersAPI getUsersAPI() {

        return RetrofitClient.getClient(BASE_URL).create(UsersAPI.class);
    }
}

