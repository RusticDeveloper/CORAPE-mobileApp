package com.example.newconectaculturas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


public class TipoSaberFragment extends Fragment {

    public TipoSaberFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tipo_saber, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*establece el metodo para navegar entre destinos*/
        final NavController navController= Navigation.findNavController(view);
        /*enlaza con la gridview de la vista*/
        GridView tipoSaber=view.findViewById(R.id.tipoSaber);
        /*datos para llenar el gridview*/
        ArrayList<String> tipos = new ArrayList();
        tipos.add("Audio");
        tipos.add("Video");
        tipos.add("Imagen");
        tipos.add("Texto");
        int[] imagenes = {
                R.drawable.aud,
                R.drawable.vid,
                R.drawable.img,
                R.drawable.txt,
        };
/*añade el adaptador a la vista de cuadricula griview*/
        AdaptadorGrids adaptadorGrids=new AdaptadorGrids(getActivity().getApplicationContext(),R.layout.item_layout,tipos,imagenes);
        tipoSaber.setAdapter(adaptadorGrids);
        tipoSaber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("NacionPueblo",getArguments().getString("NacionPueblo"));
                bundle.putString("TipoSaber",tipos.get(position));
                Navigation.findNavController(view).navigate(R.id.saberesFragment, bundle);

            }
        });
    }
}