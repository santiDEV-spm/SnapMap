package com.santidev.snapmap.Fragments;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;

import com.santidev.snapmap.Models.DataManager;

public class TagsFragment extends ListFragment {

    private ActivityCommunications mActivityCommunications;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataManager dm = new DataManager(getActivity().getApplicationContext());

        Cursor cursor = dm.getTags();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{DataManager.TABLE_ROW_TAG},
                new int[]{android.R.id.text1},
                0
        );

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //cual ha sido la etiqueta que se ha seleccionado
        Cursor cursor = ((SimpleCursorAdapter)l.getAdapter()).getCursor();
        cursor.moveToPosition(position);

        @SuppressLint("Range") String clickedTag = cursor.getString(cursor.getColumnIndex(DataManager.TABLE_ROW_TAG));
        Log.d("Etiqueta = ", clickedTag);
        mActivityCommunications.onTagListItemSelected(clickedTag);
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
