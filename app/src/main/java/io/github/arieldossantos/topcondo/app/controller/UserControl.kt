package io.github.arieldossantos.topcondo.app.controller

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserControl(val usuario: String, val senha: String) {
    private var firebaseAuth = FirebaseAuth.getInstance()
    val TAG = "UserControl"


    /**
     * Função de logar no aplicativo
     *
     * @param context!! Contexto da aplicação
     *
     */
    fun logar(context: Context) {
        firebaseAuth.signInWithEmailAndPassword(usuario, senha)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        Log.d(TAG, "Sucesso ao realizar login")
                        user = firebaseAuth.currentUser!!
                    } else {
                        Log.w(TAG,"Falha ao realizar login", it.exception)
                        Toast.makeText(context, "Login ou senha inválidos", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    companion object {
        lateinit var user: FirebaseUser
    }
}