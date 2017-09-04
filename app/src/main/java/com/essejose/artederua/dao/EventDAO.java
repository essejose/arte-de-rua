package com.essejose.artederua.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.essejose.artederua.model.Event;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jose on 02/09/2017.
 */

public class EventDAO {

    private DBOpenHelper banco ;

    public EventDAO(Context context) {
        banco = new DBOpenHelper(context);
    }

    public static final String TABELA_EVENT = "event" ;
    public static final String COLUNA_ID = "_id" ;
    public static final String COLUNA_ID_USER = "_id_user" ;
    public static final String COLUNA_TITLE = "title" ;
    public static final String COLUNA_DESCRIION = "descripion" ;
    public static final String COLUNA_IMAGE = "image" ;
    public static final String COLUNA_LATIUDE = "latiude" ;
    public static final String COLUNA_LONITUDE = "longitude" ;

    public String add(Event event){
        long resultado;
        if(!check_if_exist(event.get_id(), event.get_id_user())){
            SQLiteDatabase db = banco.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put( COLUNA_ID , event.get_id());
            values.put( COLUNA_ID_USER , event.get_id_user());
            values.put( COLUNA_TITLE , event.getTitle());
            values.put( COLUNA_DESCRIION , event.getDescripion());
            values.put( COLUNA_IMAGE , event.getImage());
            values.put( COLUNA_LATIUDE , event.getLatiude());
            values.put( COLUNA_LONITUDE , event.getLongitude());


            resultado = db.insert( TABELA_EVENT ,
                    null ,
                    values);

            db.close();
            if (resultado == - 1 ) {
                Log.i("TAG","erro");
                return "Erro ao inserir registro de evento" ;
            } else {
                Log.i("TAG","Sucesso");
                return "Registro inserido com sucesso de evento" ;

            }
        }
        return "Já existe no banco";
    }

    public List<Event> getAll() {
        List<Event> events = new LinkedList<>();
        String rawQuery = "SELECT * FROM "+TABELA_EVENT;

        SQLiteDatabase db = banco .getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, null );
        Event event;

        if (cursor.moveToFirst()) {
            do {
                event = new Event();
                event.set_id(cursor.getInt( 0 ));
                event.set_id_user(cursor.getInt(1));
                event.setTitle(cursor.getString(2));
                event.setDescripion(cursor.getString(3));
                event.setImage(cursor.getString(4));
                event.setLongitude(cursor.getDouble(5));
                event.setLatiude(cursor.getDouble(6));

                events.add(event);
            //    Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
            } while (cursor.moveToNext());

        }
        return events;
    }

    public boolean check_if_exist(Integer _id, Integer _id_user) {

        SQLiteDatabase db = banco.getWritableDatabase();
        String select = "SELECT * FROM "+TABELA_EVENT+
                " WHERE "+COLUNA_ID+" =" + _id + " AND "+COLUNA_ID_USER+" =" + _id_user + "";

        Cursor c = db.rawQuery(select, null);

        if (c.moveToFirst()) {
            Log.d("TAG","Event exits");
            return true;
        }

        if(c!=null) {
            c.close();
        }
        db.close();
        return false;
    }
}

