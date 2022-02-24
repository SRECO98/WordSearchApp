package com.example.wordsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    Context context;
    String[] letter;

    LayoutInflater inflater;

    public GridAdapter(Context context, String[] letter) {
        this.context = context;
        this.letter = letter;
    }

    @Override
    public int getCount() { //duzina niza
        return letter.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null){
            view = inflater.inflate(R.layout.grid_item, null);
        }

        TextView button = view.findViewById(R.id.button);
        button.setText(letter[position]);

        return view;
    }
}