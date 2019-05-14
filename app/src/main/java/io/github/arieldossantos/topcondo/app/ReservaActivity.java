package io.github.arieldossantos.topcondo.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
                                        ReservaActivity.getDate(Long.parseLong(document.getData().get("date").toString()), "DD/MM/YYYY"));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    final ArrayAdapter<String> adaptador =
                            new ArrayAdapter <String>(
                                    context,
                                    android.R.layout.simple_list_item_1,
                                    arrayReserva
                            );
                    listareserva.setAdapter(adaptador);
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
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
