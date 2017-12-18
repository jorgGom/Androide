package com.example.jorge.agenda.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.agenda.R;
import com.example.jorge.agenda.fragments.InsertFragment;
import com.example.jorge.agenda.fragments.UpdateFragment;
import com.example.jorge.agenda.providers.EventsContract;

import static android.content.ContentValues.TAG;

public class DetailActivity extends AppCompatActivity {

    public TextView descripcion, titulo, fecha, hora, geo;
    public int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        titulo = (TextView) findViewById(R.id.Titulo_text);
        descripcion = (TextView) findViewById(R.id.descripcion_text);
        hora = (TextView) findViewById(R.id.hora_label);
        fecha = (TextView) findViewById(R.id.fecha_label);
        geo = (TextView) findViewById(R.id.geo_label);

        Intent intent = getIntent();
        temp = intent.getIntExtra("id", -1);
        Log.d(TAG, "-------------------------------onClick() posicion del evento en el recyclerview con: " + temp);
        updateView(temp);
    }


    private void updateView(int id) {
        if (id == -1) {
            titulo.setText("");
            descripcion.setText("");
            fecha.setText("");
            hora.setText("");
            geo.setText("");
            return;
        }
        Uri uri = ContentUris.withAppendedId(EventsContract.CONTENT_URI, id);
        Cursor c = getContentResolver().query(
                uri,
                null, null, null, null);

        if (!c.moveToFirst())
            return;

        String titulo_text = c.getString(c.getColumnIndex(EventsContract.Columnas.TITULO));
        String descripcion_text = c.getString(c.getColumnIndex(EventsContract.Columnas.DESCRIPCION));
        String fecha_text = c.getString(c.getColumnIndex(EventsContract.Columnas.FECHA_EVENTO));
        String hora_text = c.getString(c.getColumnIndex(EventsContract.Columnas.HORA_EVENTO));
        String geo_text = c.getString(c.getColumnIndex(EventsContract.Columnas.LOCALIZACION));

        titulo.setText(titulo_text);
        descripcion.setText(descripcion_text);
        fecha.setText(fecha_text);
        hora.setText(hora_text);
        geo.setText(geo_text);

        c.close();
    }
    private void deleteData() {
        Uri uri = Uri.withAppendedPath(
                EventsContract.CONTENT_URI, String.valueOf(temp));
        getContentResolver().delete(
                uri,
                null,
                null
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_delete:
                deleteData();
                finish();
                Toast.makeText(this, "Evento eliminado", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_edit:
                beginUpdate();
                return true;
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"Titulo: "+titulo.getText().toString()+System.getProperty("line.separator")+
                        " Descripción: "+descripcion.getText().toString()+System.getProperty("line.separator")+
                        " Fecha del evento: "+ fecha.getText().toString()+System.getProperty("line.separator")+
                        " Hora del evento: "+hora.getText().toString()+System.getProperty("line.separator")+
                        " Localización: "+geo.getText().toString());
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);


        }

    }
    private void beginUpdate() {
        Intent detail = new Intent(getApplicationContext(), UpdateFragment.class);
            detail.putExtra(EventsContract.Columnas._ID, temp);
            detail.putExtra(EventsContract.Columnas.TITULO, titulo.getText());
            detail.putExtra(EventsContract.Columnas.DESCRIPCION, descripcion.getText());
            detail.putExtra(EventsContract.Columnas.FECHA_EVENTO, fecha.getText());
            detail.putExtra(EventsContract.Columnas.HORA_EVENTO, hora.getText());
            detail.putExtra(EventsContract.Columnas.LOCALIZACION, geo.getText());

        new UpdateFragment()
                .show(getSupportFragmentManager(), "dialog");
    }
}
