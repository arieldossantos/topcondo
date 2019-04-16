package io.github.arieldossantos.topcondo.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.ContentLoadingProgressBar
import com.google.firebase.FirebaseApp
import io.github.arieldossantos.topcondo.R
import io.github.arieldossantos.topcondo.app.controller.UserControl
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Activity que realiza o login no app
 *
 * @author Ariel Reis
 */
class LoginActivity : Activity() {
    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_login)

        //Inicialização do firebase
        FirebaseApp.initializeApp(this)

        //Ação para abrir activity registrar-se
        botaoRegistrar.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        botaoLogar.setOnClickListener {
            val usuario = email.text.toString()
            val senha = senha.text.toString()

            var user = UserControl(usuario, senha)

            user.logar(this)
        }
    }

    override fun onStart() {
        super.onStart()
        var user = UserControl("", "")
        if(user.hasUser()) {
            var intent = Intent(this, MainPage::class.java)
            startActivity(intent)
            finish()
        }
    }
}
