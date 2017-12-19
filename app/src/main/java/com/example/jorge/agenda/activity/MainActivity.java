package com.example.jorge.agenda.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jorge.agenda.R;
import com.example.jorge.agenda.fragments.InsertFragment;
import com.example.jorge.agenda.fragments.ListFragment;
import com.example.jorge.agenda.fragments.NotasFragment;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String consulta;
    public TextView nom_person;

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAñadir);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InsertFragment()
                        .show(getSupportFragmentManager(), "dialog");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        View view = navigationView.getHeaderView(0);
        String tools = pref.getAll().toString();
        Log.d(TAG, "-------------------------------Preferencias: " + tools);
        String tools2 = pref.getString("example_text","");
        Log.d(TAG, "-------------------------------Nombre del dispositivo: " + tools2);

        String colors=pref.getString("example_list","");

        int listColor = Integer.parseInt(colors);

        nom_person =(TextView) view.findViewById(R.id.nombrePersonaTV);
        nom_person.setText(tools2);



        if (savedInstanceState == null) {

            ListFragment fragment = new ListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment, ListFragment.class.getSimpleName())
                    .commit();
        }
        switch (listColor){
            case 0:
                setTheme(R.style.AppTheme);
                return;
            case 1:
                setTheme(R.style.AppTheme2);

                return;
            case 2:

                return;
            case 3:

                return;
            case 4:

                return;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager=getSupportFragmentManager();


        if (id == R.id.nav_eventos) {
            // Handle the camera action
            FragmentTransaction ft= fragmentManager.beginTransaction();
            ft.replace(R.id.container, new ListFragment()).commit();
        } else if (id == R.id.nav_gallery) {
            FragmentTransaction ft= fragmentManager.beginTransaction();
            ft.replace(R.id.container, new NotasFragment()).commit();
        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,"Por favor, únete a nuestra comunidad de agendas. "+System.getProperty("line.separator")+
                    "Si te ha gustado comparte con tus amigos.");
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
