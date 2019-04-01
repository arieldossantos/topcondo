package io.github.arieldossantos.topcondo.app

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import io.github.arieldossantos.topcondo.R
import io.github.arieldossantos.topcondo.app.controller.UserControl
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button.setOnClickListener {
            val usuario = email.text.toString()
            val password = senha.text.toString()
            val confirmPassword = confirmSenha.text.toString()

            if(confirmPassword != password) {
                var alert = AlertDialog.Builder(this)
                alert.setTitle("Ops")
                alert.setMessage("As duas senhas precisam ser idênticas.")
                alert.create()
                return@setOnClickListener

            }

            val user = UserControl(usuario.trim(), password)
            user.registrar(this)
        }
    }

}
