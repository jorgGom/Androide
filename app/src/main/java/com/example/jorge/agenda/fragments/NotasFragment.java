package com.example.jorge.agenda.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jorge.agenda.R;
import com.example.jorge.agenda.activity.DetailActivity;
import com.example.jorge.agenda.activity.SettingsActivity;
import com.example.jorge.agenda.adapters.NotasCursorAdapter;
import com.example.jorge.agenda.providers.EventsContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotasFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private RecyclerView mRecyclerView;
    private NotasCursorAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    public String consulta;
    private Context context;
    private RecyclerView.LayoutManager rmLayoutManager;

    public NotasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.activity_recordatorio, null);
        setHasOptionsMenu(true);
        context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_id);
        mRecyclerView.setHasFixedSize(true);


        //mLayoutManager = new LinearLayoutManager(getActivity());
        //int numberOfColumns = 4;
        rmLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(rmLayoutManager);

        mAdapter = new NotasCursorAdapter(
                new NotasCursorAdapter.AlClicarNoItem() {
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
        //mRecyclerView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                Intent i = new Intent(getActivity(),SettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.grid:
                rmLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(rmLayoutManager);
                return true;
            case R.id.list:
                rmLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(rmLayoutManager);
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


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setCursor(null);
    }
}

