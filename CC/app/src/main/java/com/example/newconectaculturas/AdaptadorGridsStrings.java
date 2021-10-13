package com.example.newconectaculturas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newconectaculturas.R;

import java.util.List;

public class AdaptadorGridsStrings extends BaseAdapter {

    public Context context;
    private int Layout;
    private List<String> nombreItem;
    private List<String> tipoItem;
    private List<String> NopItem;
    public AdaptadorGridsStrings(Context context1,int layout,List<String> names,List<String> types,List<String> Nopis){
        this.context=context1;
        this.Layout=layout;
        this.nombreItem=names;
        this.tipoItem=types;
        this.NopItem=Nopis;
    }
    @Override
    public int getCount() {
        return nombreItem.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        v = layoutInflater.inflate(R.layout.string_item_layout, null);
        /*obteniendo los valore individuales para cada item*/
        String ActualName=nombreItem.get(position);
        String ActualType=tipoItem.get(position);
        String ActualNop=NopItem.get(position);
        /*obteniendo los elementos de la vista*/
        TextView nombreItem= v.findViewById(R.id.itemTitle);
        TextView NopItem= v.findViewById(R.id.itemNoP);
        TextView tipoItem= v.findViewById(R.id.itemType);
        /*poniendo el texto en la vista*/
        nombreItem.setText(ActualName);
        NopItem.setText(ActualNop);
        tipoItem.setText(ActualType);
        return v;
    }
}
