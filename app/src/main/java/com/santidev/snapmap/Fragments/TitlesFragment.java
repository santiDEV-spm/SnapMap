package com.santidev.snapmap.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


import androidx.annotation.Nullable;

import com.santidev.snapmap.Model.DataManager;

public class TitlesFragment extends ListFragment {

    private Cursor mCursor;
    private ActivityCommunications mActivityCommunications;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //obtengo una referencia de las foto que contienen determinada tag
        String tag = getArguments().getString("tag");

        //obtenemos un DM para hacer la busqueda
        DataManager dm = new DataManager(getActivity().getApplicationContext());
        if(tag == "_NO_TAG"){
            //si el usuario no filtra para una tag completa
            //obtengo todos los titulos de la BD
            mCursor = dm.getTitles();
        }else{
            //si el usuario a seleccionado una tag
            //solo demo obtner los titulos de dicha tag
            mCursor = dm.getTitlesWithTag(tag);
        }

        //Adapter para rellenar una tabla..,
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                                            android.R.layout.simple_expandable_list_item_1,
                                            mCursor,
                                            new String[]{DataManager.TABLE_ROW_TITLE},
                                            new int[]{android.R.id.text1},
                                            0);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //al hacer click en la posicion indicada debo
        //mover el cursor a la fila indicada
        mCursor.moveToPosition(position);
        //cual es el id en db de este item
        @SuppressLint("Range") int dbID = mCursor.getInt(mCursor.getColumnIndex(DataManager.TABLE_ROW_ID));
        //Puedo notificar a la interface que se ha seleccionado dicho item
        mActivityCommunications.onTitlesListItemSelected(dbID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCommunications = (ActivityCommunications) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCommunications = null;
    }
}
