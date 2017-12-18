package com.example.jorge.agenda.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jorge.agenda.R;
import com.example.jorge.agenda.providers.EventsContract;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends DialogFragment
        implements DialogInterface.OnClickListener{

    private int id;
    private EditText descripcion,titulo,geo,hora,fecha;
    private static final String EXTRA_ID = "id";

    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_insert, null);

        titulo = (EditText) view.findViewById(R.id.edtTitulo);
        descripcion = (EditText) view.findViewById(R.id.edtDescripcion);
        fecha = (EditText) view.findViewById(R.id.edtFecha);
        hora = (EditText) view.findViewById(R.id.edtHora);
        geo = (EditText) view.findViewById(R.id.edtGeo);

        boolean nuevoMensage = true;
        if (getArguments() != null && getArguments().getLong(EXTRA_ID) != 0) {
            id = getArguments().getInt(EXTRA_ID);
            Uri uri = Uri.withAppendedPath(
                    EventsContract.CONTENT_URI, String.valueOf(id));

            Cursor cursor = getActivity().getContentResolver()
                    .query(uri, null, null, null, null);
            if (cursor.moveToNext()) {
                nuevoMensage = false;
                titulo.setText(cursor.getString(
                        cursor.getColumnIndex(EventsContract.Columnas.TITULO)));
                descripcion.setText(cursor.getString(
                        cursor.getColumnIndex(EventsContract.Columnas.DESCRIPCION)));
                fecha.setText(cursor.getString(
                        cursor.getColumnIndex(EventsContract.Columnas.FECHA_EVENTO)));
                hora.setText(cursor.getString(
                        cursor.getColumnIndex(EventsContract.Columnas.HORA_EVENTO)));
            }
            cursor.close();

        }
        fecha.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendario = Calendar.getInstance();
                int yy = calendario.get(Calendar.YEAR);
                int mm = calendario.get(Calendar.MONTH);
                int dd = calendario.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        int mes= monthOfYear+1;
                        String fechaT = String.valueOf(dayOfMonth)+"-"+String.valueOf(mes)
                                +"-"+String.valueOf(year);
                        fecha.setText(fechaT);

                    }
                }, yy, mm, dd);

                datePicker.show();
            }
        });
        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();
                int minute = calendario.get(Calendar.MINUTE);
                int hour = calendario.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int minute, int hour) {
                        String minHora = String.valueOf(minute)+":"+String.valueOf(hour);
                        hora.setText(minHora);
                    }
                },minute,hour,true);
                timePicker.show();
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setTitle(nuevoMensage ?
                         R.string.editar_evento :R.string.nuevo_evento )
                .setView(view)
                .setPositiveButton(R.string.guardar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateData();
                        startActivity(getActivity().getIntent());
                    }})
                .setNegativeButton(R.string.cancelar, null)
                .create();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert, container, false);
        }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getActivity().getIntent();
        id = intent.getIntExtra("id", -1);
        Log.d(TAG, "-------------------------------onClick() posicion del evento en el recyclerview con: " + id);
        updateView();
    }


    private void updateView() {

        Uri uri = ContentUris.withAppendedId(EventsContract.CONTENT_URI, id);
        Cursor c = getActivity().getContentResolver().query(
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
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {


    }
    private void updateData() {

        String dirs = geo.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        String resultado = null;
        try {
            List<Address> list = geocoder.getFromLocationName(dirs, 1);
            if (list != null && list.size() > 0) {
                Address address = list.get(0);

                resultado = address.getAddressLine(0) + ", " + address.getLocality();
            }
        }
        catch (IOException e) {
            Toast.makeText(getActivity(), "Error al recoger la dirrecion", Toast.LENGTH_SHORT).show();
        }

        Uri uri = ContentUris.withAppendedId(EventsContract.CONTENT_URI, id);

        ContentValues values = new ContentValues();
        values.put(EventsContract.Columnas.TITULO, titulo.getText().toString());
        values.put(EventsContract.Columnas.DESCRIPCION, descripcion.getText().toString());
        values.put(EventsContract.Columnas.FECHA_EVENTO, fecha.getText().toString());
        values.put(EventsContract.Columnas.HORA_EVENTO, hora.getText().toString());
        values.put(EventsContract.Columnas.LOCALIZACION, resultado);

        getActivity().getContentResolver().update(
                uri,
                values,
                null,
                null
        );
        Toast.makeText(getActivity(), "Evento actualizado", Toast.LENGTH_SHORT).show();
    }
}
