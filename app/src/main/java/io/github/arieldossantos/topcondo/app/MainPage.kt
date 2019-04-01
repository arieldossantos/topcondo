package io.github.arieldossantos.topcondo.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.firestore.FirebaseFirestore
import io.github.arieldossantos.topcondo.R
import kotlinx.android.synthetic.main.activity_main_page.*

class MainPage : AppCompatActivity() {
    val TAG = "MAINPAGE"

    val db = FirebaseFirestore.getInstance()
    var dados = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        db.collection("servicos")
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        for(document in it.result!!) {
                            Log.d("MAIN", document.data.toString())
                            dados.add(document.get("nome").toString())
                        }

                        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dados)
                        listaDeServicos.adapter = adapter
                    } else {
                        Log.w(TAG, "Erro ao obter os serviços. ", it.exception)
                    }
                }
    }
}
