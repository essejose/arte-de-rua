package com.essejose.artederua.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import  com.essejose.artederua.model.User;
/**
 * Created by jose on 29/07/2017.
 */

public class UserDAO {

    private DBOpenHelper banco ;

    public UserDAO(Context context) {
        banco = new DBOpenHelper(context);
    }

    public static final String TABELA_USER = "user" ;
    public static final String COLUNA_USUARIO = "usuario" ;
    public static final String COLUNA_SENHA = "senha" ;

    public String add(User user){
        long resultado;
        if(!check_login(user.getUsuario(), user.getSenha())){
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( COLUNA_USUARIO , user.getUsuario());
        values.put( COLUNA_SENHA , user.getSenha());

        resultado = db.insert( TABELA_USER ,
                null ,
                values);

        db.close();
        if (resultado == - 1 ) {
            Log.i("TAG","erro");
            return "Erro ao inserir registro" ;
        } else {
            Log.i("TAG","Sucesso");
            return "Registro inserido com sucesso" ;

        }
        }
        return "Já existe no banco";
    }

    public List<User> getAll() {
        List<User> users = new LinkedList<>();
        String rawQuery = "SELECT * FROM "+TABELA_USER;

        SQLiteDatabase db = banco .getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, null );
        User user;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setUsuario(cursor.getString( 1 ));
                user.setSenha(cursor.getString(2));

                users.add(user);
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
            } while (cursor.moveToNext());

        }
        return users;
    }

    public boolean check_login(String usuario, String senha) {

        SQLiteDatabase db = banco.getWritableDatabase();
        String select = "SELECT * FROM "+TABELA_USER+
                " WHERE "+COLUNA_USUARIO+" ='" + usuario + "' AND "+COLUNA_SENHA+" ='" + senha + "'";

        Cursor c = db.rawQuery(select, null);

        if (c.moveToFirst()) {
            Log.d("TAG","User exits");
            return true;
        }

        if(c!=null) {
            c.close();
        }
        db.close();
        return false;
    }
}
