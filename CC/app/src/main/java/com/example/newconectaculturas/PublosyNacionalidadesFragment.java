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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PublosyNacionalidadesFragment extends Fragment {

    public PublosyNacionalidadesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publosy_nacionalidades, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController=Navigation.findNavController(view);
        GridView PyN=view.findViewById(R.id.ListaPueblos);
        ArrayList<String> titulos = new ArrayList();
        titulos.add("Nacionalidad Achuar");
        titulos.add("Nacionalidad Andoa");
        titulos.add("Nacionalidad Awa");
        titulos.add("Nacionalidad Chachi");
        titulos.add("Nacionalidad Cofán");
        titulos.add("Nacionalidad Éperara Siapidara");
        titulos.add("Nacionalidad Kichwa");
        titulos.add("Nacionalidad Sápara");
        titulos.add("Nacionalidad Sekoya");
        titulos.add("Nacionalidad Shiwiar");
        titulos.add("Nacionalidad Shuar");
        titulos.add("Nacionalidad Siona");
        titulos.add("Nacionalidad Tsáchila");
        titulos.add("Nacionalidad Waorani");
        titulos.add("Pueblo AFroecuatoriano");
        titulos.add("Pueblo Huancavilca");
        titulos.add("Pueblo Manta");
        titulos.add("Pueblo Montuvios");
        int[] imagenes = {
            R.drawable.nacionalidad_achuar,
            R.drawable.nacionalidad_andoa,
            R.drawable.nacionalidad_aw_,
            R.drawable.nacionalidad_chachi,
            R.drawable.nacionalidad_cof_n,
            R.drawable.nacionalidad__perara_siapidara,
            R.drawable.nacionalidad_kichwa,
            R.drawable.nacionalidad_s_para,
            R.drawable.nacionalidad_sekoya,
            R.drawable.nacionalidad_shiwiar,
            R.drawable.nacionalidad_shuar,
            R.drawable.nacionalidad_siona,
            R.drawable.nacionalidad_ts_chila,
            R.drawable.nacionalidad_waorani,
            R.drawable.pueblo_afroecuatoriano,
            R.drawable.pueblo_huancavilca,
            R.drawable.pueblo_manta,
            R.drawable.pueblo_montuvios
        };
        AdaptadorGrids adaptadorGrids=new AdaptadorGrids(getActivity().getApplicationContext(),R.layout.item_layout,titulos,imagenes);
        PyN.setAdapter(adaptadorGrids);
        PyN.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("NacionPueblo",titulos.get(position));
                Navigation.findNavController(view).navigate(R.id.tipoSaberFragment, bundle);
            }
        });
    }

}