package com.santidev.snapmap.Fragments;

public interface ActivityCommunications {
    //se llamara cuando de seleccione un titulo de imagen de la lista disponible
    void onTitlesListItemSelected(int pos);
    //se llamara cuando se seleccione a una tag de la lista disponible
    void onTagListItemSelected(String tag);
}
