package io.github.arieldossantos.topcondo.app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.arieldossantos.topcondo.R;
import io.github.arieldossantos.topcondo.app.controller.UserControl;

public class ReservaActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "ReservaActivity";
    Context context;

    private void criarListView (){
        final ListView listareserva = (ListView)
                findViewById(R.id.listareserva);



        //Criando a referência para a collection reservas
        CollectionReference reservaRef = db.collection("reservas");

    // Create a query against the collection.
        Query query = reservaRef.whereEqualTo("usuario", UserControl.user.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> arrayReserva = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        arrayReserva.add(
                                "Reservado para o dia: " +
                                        ReservaActivity.getDate(Long.parseLong(document.getData().get("date").toString()), "dd/MM/yyyy"));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    final ArrayAdapter<String> adaptador =
                            new ArrayAdapter <String>(
                                    context,
                                    android.R.layout.simple_list_item_1,
                                    arrayReserva
                            );

                    listareserva.setAdapter(adaptador);

                    listareserva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String texto = (String) parent.getItemAtPosition(position);

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, "Olá, estou com um horário " + texto.toLowerCase() + " no TopCondo!\n\rJuntos e shallow now \uD83C\uDF1F!");
                            intent.setType("text/plain");
                            startActivity(Intent.createChooser(
                                    intent,
                                    "Compartilhar via..."
                            ));
                        }
                    });
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);
        context = this;
        this.criarListView();
    }
    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
