package com.example.jorge.agenda.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jorge.agenda.R;

public class DetailActivity extends AppCompatActivity {

    private TextView descripcion, titulo, fecha, hora, geo;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titulo = (TextView) findViewById(R.id.Titulo_text);
        descripcion = (TextView) findViewById(R.id.descripcion_text);
        hora = (TextView) findViewById(R.id.hora_label);
        fecha = (TextView) findViewById(R.id.fecha_label);
        geo = (TextView) findViewById(R.id.geo_label);




    }
}
