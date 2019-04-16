package io.github.arieldossantos.topcondo.app

import android.app.AlertDialog
import android.app.ProgressDialog
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import io.github.arieldossantos.topcondo.R
import io.github.arieldossantos.topcondo.app.controller.UserControl
import kotlinx.android.synthetic.main.dialog_reservar.*
import java.util.*
import kotlin.collections.HashMap


class Reservar: DialogFragment() {

    lateinit var idservico: String
    lateinit var progress: ProgressDialog
    var db = FirebaseFirestore.getInstance()
    var date: Long = 0


    companion object {
            fun newInstance(title: String, extras: Bundle): Reservar {
            val frag = Reservar()
            val args = extras
            args.putString("title", title)
            frag.setArguments(args)
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = activity!!.layoutInflater.inflate(R.layout.dialog_reservar, container)
        this.idservico = arguments!!.get("idservico") as String
        return view
    }

    fun checarDiasIndisponiveis() {
        db.collection("reservas")
                .whereEqualTo("service", this.idservico)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        if(it.result!!.isEmpty) {
                            this.inserirReserva()
                        }else{
                            this.progress.hide()
                            AlertDialog.Builder(activity)
                                    .setTitle("Opa!")
                                    .setMessage("Já existe uma reserva neste dia.")
                                    .show()
                            reservarButton.isEnabled = false

                        }
                    }
                }
    }

    fun inserirReserva() {
        var reserva = HashMap<String, Any>()
        reserva.put("date", this.date)
        reserva.put("service", this.idservico)
        reserva.put("usuario", UserControl.user.uid)

        db.collection("reservas")
                .add(reserva)
                .addOnCompleteListener {
                    this.progress.hide()
                    if(it.isSuccessful) {
                        Toast.makeText(activity, "Reserva inserida com sucesso", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }else{
                        Toast.makeText(activity, "Ops, falha na conexão", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var date = Calendar.getInstance()
            date.set(Calendar.HOUR_OF_DAY, 0)
            date.set(Calendar.MINUTE, 0)
            date.set(Calendar.SECOND, 0)

        this.date = date.timeInMillis

        val calendar = Calendar.getInstance()
        calendarView.minDate = calendar.timeInMillis

        this.progress = ProgressDialog(activity)

        dialog.setTitle("Reservar")

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth, 0, 0, 0)
            this.date = calendar.timeInMillis
        }

        reservarButton.setOnClickListener {
            reservarButton.isEnabled = false
            this.progress.setTitle("Reservando...")
            this.progress.setMessage("Estamos reservando o seu lugar!")
            this.progress.isIndeterminate = true
            this.progress.show()

            this.checarDiasIndisponiveis()
        }
    }
}