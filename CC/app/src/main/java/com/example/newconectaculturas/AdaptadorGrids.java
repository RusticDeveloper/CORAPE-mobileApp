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

public class AdaptadorGrids extends BaseAdapter {
    public Context context;
    private int Layout;
    private List<String> nombreItem;
    private int [] imagenes;

    public AdaptadorGrids(Context context,int layout,List<String> names,int[] images){
        this.context=context;
        this.Layout=layout;
        this.nombreItem=names;
        this.imagenes=images;


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
        v=layoutInflater.inflate(R.layout.item_layout,null);
        String ActualName=nombreItem.get(position);
        int ActualPhoto =imagenes[position];
        TextView nombreItem= v.findViewById(R.id.itemName);
        ImageView fotoItem=v.findViewById(R.id.itemPhoto);
        nombreItem.setText(ActualName);
        fotoItem.setImageResource(ActualPhoto);
        return v;
    }
}
