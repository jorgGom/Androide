package com.example.jorge.agenda.providers;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;


public class EventsContract {


    public final static String AUTHORITY = "com.ccs.agenda.providers.EventsProvider";
    public static final String ACTIVIDAD = "tarea";
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + ACTIVIDAD;
    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + ACTIVIDAD;
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + ACTIVIDAD);
    public static final UriMatcher uriMatcher;
    public static final int ALLROWS = 1;
    public static final int SINGLE_ROW = 2;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ACTIVIDAD, ALLROWS);
        uriMatcher.addURI(AUTHORITY, ACTIVIDAD + "/#", SINGLE_ROW);
    }


    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String TITULO = "TITULO";
        public final static String DESCRIPCION = "DESCRIPTION";
        public final static String FECHA_EVENTO = "FECHA_EVENTO";
        public final static String HORA_EVENTO = "HORA_EVENTO";
        public final static String LOCALIZACION = "LOCALIZACION";


    }




}