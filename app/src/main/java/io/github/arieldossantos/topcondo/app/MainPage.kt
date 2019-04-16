package io.github.arieldossantos.topcondo.app

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import com.google.firebase.firestore.FirebaseFirestore
import io.github.arieldossantos.topcondo.R
import io.github.arieldossantos.topcondo.app.controller.ListViewAdapter
import kotlinx.android.synthetic.main.activity_main_page.*

class MainPage : AppCompatActivity() {
    val TAG = "MAINPAGE"

    val db = FirebaseFirestore.getInstance()
    var dados = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        val progress = ProgressDialog(this)
        progress.setTitle("Carregando serviços...")
        progress.setMessage("Aguarde enquanto os serviços são carregados")
        progress.isIndeterminate = true
        progress.show()

        db.collection("servicos")
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        for(document in it.result!!) {
                            Log.d("MAIN", document.data.toString())
                            var newData = document.data
                            newData.put("key", document.id)
                            dados.add(newData)
                        }

                        val adapter = ListViewAdapter(this, R.layout.list_lista_de_servicos, dados)
                        listaDeServicos.adapter = adapter
                        progress.dismiss()
                    } else {
                        Log.w(TAG, "Erro ao obter os serviços. ", it.exception)
                    }
                }
    }
}
