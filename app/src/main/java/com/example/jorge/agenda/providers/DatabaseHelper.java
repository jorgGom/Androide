package com.example.jorge.agenda.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase database) {
        createTable(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTable(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + EventsContract.ACTIVIDAD + " (" +
                EventsContract.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EventsContract.Columnas.TITULO + " TEXT, " +
                EventsContract.Columnas.DESCRIPCION + " TEXT, " +
                EventsContract.Columnas.FECHA_EVENTO + " TEXT, " +
                EventsContract.Columnas.HORA_EVENTO + " TEXT, " +
                EventsContract.Columnas.LOCALIZACION + " TEXT);";
        database.execSQL(cmd);

    }


}
