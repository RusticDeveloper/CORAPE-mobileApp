package com.example.newconectaculturas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SaberesFragment extends Fragment {
    public SaberesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saberes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*creando varibles y relacionando con la vista*/
        ArrayList<String> nombres = new ArrayList();
        ArrayList<String> tipos = new ArrayList();
        ArrayList<String> nop = new ArrayList();
        ArrayList<String> ids = new ArrayList();
        GridView Saberes = view.findViewById(R.id.ListaSaberes);
        /*obtiene los datos de obtenidos de los anteriores fragments*/
        String NoP = getArguments().getString("NacionPueblo");
        String TpoArch = getArguments().getString("TipoSaber");
        /*pide los datos desde el servidor*/
        (Api.getClient().getKnowledges(NoP, TpoArch)).enqueue(new Callback<List<StringResults>>() {
            @Override
            public void onResponse(Call<List<StringResults>> call, Response<List<StringResults>> response) {
                if (response.body().get(0).getID().equals("no encontro")) {
                    Toast.makeText(getActivity().getApplicationContext(), "No hay saberes de este tipó", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Entro corectamente", Toast.LENGTH_SHORT).show();
                    int size = response.body().size();
                    for (int i = 0; i < size; i++) {
                        nombres.add(response.body().get(i).getTitulo());
                        tipos.add(response.body().get(i).getTipoArchivo());
                        nop.add(response.body().get(i).getNacionalidadoPueblo());
                        ids.add(response.body().get(i).getID());
                    }
                }
                AdaptadorGridsStrings adaptadorGrids = new AdaptadorGridsStrings(getActivity().getApplicationContext(),
                        R.layout.string_item_layout, nombres, tipos, nop);
                Saberes.setAdapter(adaptadorGrids);
            }

            @Override
            public void onFailure(Call<List<StringResults>> call, Throwable t) {
                Log.i("error", t.toString());
                Toast.makeText(getActivity().getApplicationContext(), "No funciono error " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Saberes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity().getApplicationContext(), vista_Individual.class);
                i.putExtra("Identificador", ids.get(position));
                startActivity(i);
            }
        });
    }
}