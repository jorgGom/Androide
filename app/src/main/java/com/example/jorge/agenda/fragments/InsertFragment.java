package com.example.jorge.agenda.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import com.example.jorge.agenda.providers.EventsProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsertFragment extends DialogFragment
        implements DialogInterface.OnClickListener {

    private static final String EXTRA_ID = "id";

    EditText mEdtTitulo, mEdtDescipcion, mEdtFecha, mEdtHora, mEdtGeo;
    long id;

    public static InsertFragment newInstance(long id){
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_ID, id);

        InsertFragment insertFragment = new InsertFragment();
        insertFragment.setArguments(bundle);
        return insertFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_insert, null);

        mEdtTitulo = (EditText)view.findViewById(R.id.edtTitulo);
        mEdtDescipcion = (EditText)view.findViewById(R.id.edtDescripcion);
        mEdtFecha = (EditText) view.findViewById(R.id.edtFecha);
        mEdtHora = (EditText) view.findViewById(R.id.edtHora);
        mEdtGeo = (EditText) view.findViewById(R.id.edtGeo);

        Date fechaActual = new Date();
        SimpleDateFormat date= new SimpleDateFormat("dd-MM-yyyy");
        String today =date.format(fechaActual);

        mEdtFecha.setText(today);

        mEdtFecha.setOnClickListener( new View.OnClickListener() {
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
                        String fecha = String.valueOf(dayOfMonth)+"-"+String.valueOf(mes)
                                +"-"+String.valueOf(year);
                        mEdtFecha.setText(fecha);

                    }
                }, yy, mm, dd);

                datePicker.show();
            }
        });

        Date horas=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String formattedTime=sdf.format(horas);

        mEdtHora.setText(formattedTime);

        mEdtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();
                int minute = calendario.get(Calendar.MINUTE);
                int hour = calendario.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int minute, int hour) {
                        String minHora = String.valueOf(minute)+":"+String.valueOf(hour);
                        mEdtHora.setText(minHora);
                    }
                },minute,hour,true);
                timePicker.show();
            }
        });
                boolean nuevoMensage = true;
        if (getArguments() != null && getArguments().getLong(EXTRA_ID) != 0){
            id = getArguments().getLong(EXTRA_ID);
            Uri uri = Uri.withAppendedPath(
                    EventsContract.CONTENT_URI, String.valueOf(id));

            Cursor cursor = getActivity().getContentResolver()
                    .query( uri, null, null, null, null);
            if (cursor.moveToNext()) {
                nuevoMensage = false;
                mEdtTitulo.setText(cursor.getString(
                        cursor.getColumnIndex(EventsContract.Columnas.TITULO)));
                mEdtDescipcion.setText(cursor.getString(
                        cursor.getColumnIndex(EventsContract.Columnas.DESCRIPCION)));
                mEdtFecha.setText(cursor.getString(
                        cursor.getColumnIndex(EventsContract.Columnas.FECHA_EVENTO)));
                mEdtHora.setText(cursor.getString(
                        cursor.getColumnIndex(EventsContract.Columnas.HORA_EVENTO)));
            }
            cursor.close();
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(nuevoMensage ?
                        R.string.nuevo_evento : R.string.editar_evento)
                .setView(view)
                .setPositiveButton(R.string.guardar, this)
                .setNegativeButton(R.string.cancelar, null)
                .create();
    }

    public InsertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert, container, false);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {


        String dirs = mEdtGeo.getText().toString();
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


        ContentValues values = new ContentValues();
        values.put(EventsContract.Columnas.TITULO, mEdtTitulo.getText().toString());
        values.put(EventsContract.Columnas.DESCRIPCION, mEdtDescipcion.getText().toString());
        values.put(EventsContract.Columnas.FECHA_EVENTO, mEdtFecha.getText().toString());
        values.put(EventsContract.Columnas.HORA_EVENTO, mEdtHora.getText().toString());
        values.put(EventsContract.Columnas.LOCALIZACION, resultado);

        getActivity().getContentResolver().insert(
                EventsContract.CONTENT_URI,
                values
        );
        Toast.makeText(getActivity(), "Evento insertado", Toast.LENGTH_SHORT).show();

    }


}
