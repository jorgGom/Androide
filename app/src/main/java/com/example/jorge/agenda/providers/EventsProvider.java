package com.example.jorge.agenda.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class EventsProvider extends ContentProvider {

    private static final String DATABASE_NAME = "agenda.db";

    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {

        databaseHelper = new DatabaseHelper(
                getContext(),
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {



        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = EventsContract.uriMatcher.match(uri);

        Cursor c;

        switch (match) {
            case EventsContract.ALLROWS:

                c = db.query(EventsContract.ACTIVIDAD, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        getContext().getContentResolver(),
                        EventsContract.CONTENT_URI);
                break;
            case EventsContract.SINGLE_ROW:

                long eventoId = ContentUris.parseId(uri);
                c = db.query(EventsContract.ACTIVIDAD, projection,
                        EventsContract.Columnas._ID + " = " + eventoId,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        getContext().getContentResolver(),
                        EventsContract.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        return c;

    }

    @Override
    public String getType(Uri uri) {
        switch (EventsContract.uriMatcher.match(uri)) {
            case EventsContract.ALLROWS:
                return EventsContract.MULTIPLE_MIME;
            case EventsContract.SINGLE_ROW:
                return EventsContract.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Tipo de actividad desconocida: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (EventsContract.uriMatcher.match(uri) != EventsContract.ALLROWS) {
            throw new IllegalArgumentException("URI desconocida : " + uri);
        }
        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = db.insert(EventsContract.ACTIVIDAD,
                null, contentValues);
        if (rowId > 0) {
            Uri uri_actividad =
                    ContentUris.withAppendedId(
                            EventsContract.CONTENT_URI, rowId);
            getContext().getContentResolver().
                    notifyChange(uri_actividad, null);
            return uri_actividad;
        }
        throw new SQLException("Falla al insertar fila en : " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = EventsContract.uriMatcher.match(uri);
        int affected;

        switch (match) {
            case EventsContract.ALLROWS:
                affected = db.delete(EventsContract.ACTIVIDAD,
                        selection,
                        selectionArgs);
                break;
            case EventsContract.SINGLE_ROW:
                long eventoId = ContentUris.parseId(uri);
                affected = db.delete(EventsContract.ACTIVIDAD,
                        EventsContract.Columnas._ID + "=" + eventoId
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                getContext().getContentResolver().
                        notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Elemento actividad desconocido: " +
                        uri);
        }
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int affected;
        switch (EventsContract.uriMatcher.match(uri)) {
            case EventsContract.ALLROWS:
                affected = db.update(EventsContract.ACTIVIDAD, values,
                        selection, selectionArgs);
                break;
            case EventsContract.SINGLE_ROW:
                String eventoId = uri.getPathSegments().get(1);
                affected = db.update(EventsContract.ACTIVIDAD, values,
                        EventsContract.Columnas._ID + "=" + eventoId
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }
}

