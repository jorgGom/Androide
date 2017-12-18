package com.example.jorge.agenda.fragments;


import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.agenda.R;
import com.example.jorge.agenda.activity.DetailActivity;
import com.example.jorge.agenda.adapters.EventsCursorAdapter;
import com.example.jorge.agenda.providers.EventsContract;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView mRecyclerView;
    private EventsCursorAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    public String consulta;
    public TextView fecha;
    private Context context;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_reclycer, container, false);
        setHasOptionsMenu(true);
        context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_id);
        mRecyclerView.setHasFixedSize(true);


        configuraSwipe();

        fecha =(TextView) view.findViewById(R.id.fechaSelecionada);

        Date fechaActual = new Date();
        SimpleDateFormat date= new SimpleDateFormat("dd-MM-yyyy");
        String today =date.format(fechaActual);
        fecha.setText(today);


        String FechaSelect = fecha.getText().toString();
        consulta =
                EventsContract.Columnas.FECHA_EVENTO + " = '"+ FechaSelect + "' order by "+
                        EventsContract.Columnas.HORA_EVENTO + " asc ;";



        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new EventsCursorAdapter(
                new EventsCursorAdapter.AlClicarNoItem() {
                    @Override
                    public void itemFueClicado(Cursor cursor) {
                        int id  = cursor.getInt(
                                cursor.getColumnIndex(EventsContract.Columnas._ID));

                        Intent detail = new Intent(context.getApplicationContext(), DetailActivity.class);
                        detail.putExtra("id", id);

                        context.startActivity(detail);



                    }
                });
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
        return view;

    }

    private void configuraSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(
                        0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {

                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder
                                                 viewHolder,
                                         int swipeDir) {
                        final int x = viewHolder.getLayoutPosition();
                        Cursor cursor = mAdapter.getCursor();
                        cursor.moveToPosition(x);
                        final long id = cursor.getLong(
                                cursor.getColumnIndex(EventsContract.Columnas._ID));

                        Uri uriToDelete = Uri.withAppendedPath(
                                EventsContract.CONTENT_URI, String.valueOf(id));
                        getActivity().getContentResolver().delete(
                                uriToDelete,
                                null, null);

                    }
                };
        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                return true;

            case R.id.action_calendar:
                MainDatePickerFragment newFragment = new MainDatePickerFragment();
                newFragment.setHostingFragment(this);
                newFragment.show(getFragmentManager(), "datePicker");
                return true;
        }
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
         return new CursorLoader(getActivity(),
        EventsContract.CONTENT_URI,null, consulta, null,
    null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.setCursor(data);
    }


    public void setDate(String DateSelect){

        getLoaderManager().destroyLoader(0);
        consulta =
                EventsContract.Columnas.FECHA_EVENTO + " = '"+ DateSelect + "' order by "+
                        EventsContract.Columnas.HORA_EVENTO + " asc ;";

        getLoaderManager().initLoader(0,null,this);

    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setCursor(null);
    }
}
