package com.example.jorge.agenda.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorge.agenda.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordatorioFragment extends Fragment {


    public RecordatorioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.activity_recordatorio, null);

        return view;
    }

}
