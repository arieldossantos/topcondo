package io.github.arieldossantos.topcondo.app.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import io.github.arieldossantos.topcondo.R;

public class ListViewAdapter extends ArrayAdapter<String> {
    private Context activity;
    private List<String> listaServicos;

    public ListViewAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.listaServicos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.list_lista_de_servicos, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.servico.setText(getItem(position));
        //get first letter of each String item
        String firstLetter = String.valueOf(getItem(position).charAt(0));

        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView servico;

        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.image_view);
            servico = (TextView) v.findViewById(R.id.text);
        }
    }


}
