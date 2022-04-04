package com.santidev.snapmap.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.location.LocationListenerCompat;

import com.santidev.snapmap.Models.DataManager;
import com.santidev.snapmap.Models.Photo;
import com.santidev.snapmap.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CaptureFragment extends Fragment  implements LocationListenerCompat {


    private static final int CAMERA_REQUEST = 1234;
    private ImageView mImageView;

    //Este es el path donde se guardará la foto
    String mCurrentPhotoPath;

    //Cuando capturemos la imagen, donde la guardaremos nosotros
    private Uri mImageUri = Uri.EMPTY;

    //referencia a nuestro data manager para acceder a la base de datos
    private DataManager mDataManager;

    //referencia a la geoposicion
    private  Location mLocation = new Location("");
    private LocationManager mLocationManager;
    private String mProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDataManager = new DataManager(getActivity().getApplicationContext());

        //Inicializamos el manager del gps
        this.mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(criteria, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_capture, container, false);

        mImageView = (ImageView) view.findViewById(R.id.image_view);

        Button btnCapture = (Button) view.findViewById(R.id.button_capture);
        Button btnSave = (Button) view.findViewById(R.id.button_save);

        final EditText mEditTextTitle = (EditText) view.findViewById(R.id.edit_text_title);
        final EditText mEditTextTag1 = (EditText) view.findViewById(R.id.edit_text_tag_1);
        final EditText mEditTextTag2 = (EditText) view.findViewById(R.id.edit_text_tag_2);
        final EditText mEditTextTag3 = (EditText) view.findViewById(R.id.edit_text_tag_3);


        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File photoFile = null;

                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //Si tenemos fichero de foto, podemos proceder a guardarlo

                if(photoFile != null){

                    mImageUri = Uri.fromFile(photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }


            }
        }); //fin de la logica de capturar la foto

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mImageUri != null){ //hay fichero
                    if(!mImageUri.equals(Uri.EMPTY)) { //El fichero no esta vacio
                        Photo photo = new Photo();
                        photo.setTitle(mEditTextTitle.getText().toString());
                        photo.setStorageLocation(mImageUri);

                        photo.setGpsLocation(mLocation);

                        photo.setTag1(mEditTextTag1.getText().toString());
                        photo.setTag2(mEditTextTag2.getText().toString());
                        photo.setTag3(mEditTextTag3.getText().toString());

                        mDataManager.addPhoto(photo);

                        Toast.makeText(getActivity(), "Imagen Guardada correctamente en DB", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "No hay niguna imagen para guardar", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "ERROR GRAVE, URI nula", Toast.LENGTH_SHORT).show();
                }
            }
        });//FIN DE LA LOGICA DE GUARDADO DE LA FOTO

        return view;
    }


    private File createImageFile() throws IOException{

        //Creamos el nombre de la foto basado en la fecha en que ha sido tomada
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_"+timeStamp+"_";

        File storeageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName, //nombre del fichero a crear
                ".jpg", //extensión del fichero
                storeageDirectory //directorio o carpeta donde guardamos el fichero
        );

        //Guardamos para utilizarlo con el intent de Action View
        mCurrentPhotoPath = "file: "+image.getAbsolutePath();
        if (image == null){
            Log.i("FOTO", "createImageFile: Es null");
        }
        return image;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            try {
                mImageView.setImageURI(Uri.parse(mImageUri.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mImageUri = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BitmapDrawable bd = (BitmapDrawable) mImageView.getDrawable();
        bd.getBitmap().recycle();
        mImageView.setImageBitmap(null);

    }

    /**Metodos del LocationListener**/
    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLocation = location;
    }

    @Override
    public void onStatusChanged(@NonNull String provider, int status, @Nullable Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationManager.requestLocationUpdates(mProvider, 500, 1, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
    }
}

