package io.github.arieldossantos.topcondo.app.controller;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import io.github.arieldossantos.topcondo.R;

public class ListViewAdapter extends ArrayAdapter<Object> {
    private Context activity;
    private List<Object> listaServicos;
    private FirebaseStorage storage;

    public ListViewAdapter(@NonNull Context context, int resource, List<Object> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.listaServicos = objects;
        this.storage = FirebaseStorage.getInstance();
    }

    //Criação da View do Listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_lista_de_servicos, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, Object> valuesMap = (HashMap<String, Object>) getItem(position);

        //Insere imagem e texto no listview
        holder.servico.setText(valuesMap.get("nome").toString());
        StorageReference reference = this.storage.getReference().child("servicos/" + valuesMap.get("key").toString() + ".png");

        final Activity activity = (Activity) this.activity;
        //Cria arquivo temporário
        try {
            final File localFile = new File(getContext().getCacheDir() + "/" + valuesMap.get("key").toString() + ".png");
            if(localFile.exists()) { //Condição para a imagem não ficar atuualizando eternamente
                holder.imageView.setImageBitmap(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
            } else {
                reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        //Cria bitmap da imagem no listview
                        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "Ocorreu algum erro ao processar a imagem do serviço", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
