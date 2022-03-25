package com.santidev.snapmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.santidev.snapmap.Fragments.CaptureFragment;
import com.santidev.snapmap.Fragments.TagsFragment;
import com.santidev.snapmap.Fragments.TitlesFragment;


public class MainActivity extends AppCompatActivity {

    private ListView mNavDrawerListView;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavDrawerListView = (ListView) findViewById(R.id.nav_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navMenuTitles);
        mNavDrawerListView.setAdapter(mAdapter);

        setUpDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mNavDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemSelectd, long l) {
                switchFragment(itemSelectd);
            }
        });

        switchFragment(0);
    }

    private void switchFragment(int position){

        Fragment fragment = null;
        String fragmentId = "";

        switch (position){
            case 0:
                fragmentId = "TITLES";
                Bundle args = new Bundle();
                args.putString("Tag", "_NO_TAG");
                fragment = new TitlesFragment();
                fragment.setArguments(args);
                break;

            case 1:
                fragmentId = "TAGS";
                fragment = new TagsFragment();
                break;

            case 2:
                fragmentId = "CAPTURE";
                fragment = new CaptureFragment();
                break;

            default:
                break;
        }

        //Aqui ya hemos decidido que fragmento cargar...
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_holder, fragment, fragmentId)
                .commit();

        //al final, como la seleccion viene de un nav drawer, estamos obligados a cerrar
        this.mDrawerLayout.closeDrawer(mNavDrawerListView);
    }

    private void setUpDrawer(){

        this.mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                                                        R.string.drawer_open,
                                                        R.string.drawer_close){

            //se llama cuando el menu lateral se muestra
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle( getString(R.string.menu_choice) );

                //llama de forma automatica al metodo onPrepareOptionMenu
                invalidateOptionsMenu();
            }

            //se llama cuando el menu lateral se cierra
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);

                //llama de forma automatica al metodo onPrepareOptionMenu
                invalidateOptionsMenu();
            }
        };

        this.mDrawerToggle.setDrawerIndicatorEnabled(true);
        //@deprecated: setDrawerListener...
        this.mDrawerLayout.addDrawerListener(this.mDrawerToggle);



    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
       //checar si el Drawer esta abierto o no
       if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)){
           //aqui esta el drawer abierto
           this.mDrawerLayout.closeDrawer(this.mNavDrawerListView);
       }else{
           //aqui esta cerrado
           //tengo que volver al fragmeto inicial(titles)
           //si ya estoy en titles, debo cerrar la app
           Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_holder);
           if(currentFragment instanceof TitlesFragment){
               //es el fragmento inicil, entonce debo salir de la app
               finish();
               System.exit(0);
           }else {
               //no estamos el fragmento inicial, asi lo cargamos
               switchFragment(0);
           }
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        if (id == R.id.item_settings){
            Toast.makeText(this,"Se ha seleccionado el menu de opciones",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}