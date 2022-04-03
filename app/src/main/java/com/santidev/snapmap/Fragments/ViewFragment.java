package com.santidev.snapmap.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.santidev.snapmap.Model.DataManager;
import com.santidev.snapmap.R;

public class ViewFragment extends Fragment {

    private Cursor mCursor;
    private ImageView mImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //tengo foto para mostrar
        int position = getArguments().getInt("position");
        // cargamos la foto adecuada desde la base de datos
        DataManager dm = new DataManager(getActivity().getApplicationContext());
        //obtenemos un cursor de la foto desde la BD
        mCursor = dm.getPhoto(position);
    }

    @SuppressLint("Range")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        TextView tv = view.findViewById(R.id.text_view_title);
        Button btnMap = (Button) view.findViewById(R.id.btn_show_map);
        mImageView = (ImageView) view.findViewById(R.id.image_view);

        //del cuarsor de la BD. obtenemos la referencia a la columna que nos interesa
        //forzamos en cada caso la obtencion de ltipo de datos pertinente, en nuestro caso String/Uri
        tv.setText(mCursor.getString(mCursor.getColumnIndex(DataManager.TABLE_ROW_TITLE)));
        mImageView.setImageURI(Uri.parse(mCursor.getString(mCursor.getColumnIndex(DataManager.TABLE_ROW_URI))));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //asegurarnos que liberamos la memoria de las imagenes,
        //reciclaremos el bitmap
        BitmapDrawable bmd = (BitmapDrawable) mImageView.getDrawable();
        bmd.getBitmap().recycle();
        mImageView.setImageBitmap(null);
    }
}
