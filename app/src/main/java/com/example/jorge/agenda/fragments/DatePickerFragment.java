package com.example.jorge.agenda.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.agenda.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private SimpleDateFormat sdf;
    private ListFragment listFragment;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        sdf=new SimpleDateFormat("yyyy-MM-dd");

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {


        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf =new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(c.getTime());

        TextView tv1=(TextView) getActivity().findViewById(R.id.edtFecha);
        tv1.setText(formattedDate);

        Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_SHORT).show();

    }
}
