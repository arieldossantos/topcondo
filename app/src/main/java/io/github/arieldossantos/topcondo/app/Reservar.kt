package io.github.arieldossantos.topcondo.app

import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.arieldossantos.topcondo.R
import kotlinx.android.synthetic.main.dialog_reservar.*
import java.util.*


class Reservar: DialogFragment() {

    companion object {
            fun newInstance(title: String): Reservar {
            val frag = Reservar()
            val args = Bundle()
            args.putString("title", title)
            frag.setArguments(args)
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = activity!!.layoutInflater.inflate(R.layout.dialog_reservar, container)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        calendarView.minDate = calendar.timeInMillis
        return view
    }
}