package io.github.arieldossantos.topcondo.app.controller

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.github.arieldossantos.topcondo.app.MainPage

class UserControl(private val usuario: String, private val senha: String) {
    private var firebaseAuth = FirebaseAuth.getInstance()
    private val TAG = "UserControl"


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
                        val intent = Intent(context, MainPage::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
                        context.startActivity(intent)
                    } else {
                        Log.w(TAG,"Falha ao realizar login", it.exception)
                        Toast.makeText(context, "Login ou senha inválidos", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    /**
     * Função de registro no app
     */
    fun registrar(context: Context) {
        firebaseAuth.createUserWithEmailAndPassword(usuario, senha)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        Log.d(TAG, "Usuário criado")
                        Toast.makeText(context, "Usuário criado com sucesso!", Toast.LENGTH_LONG).show()
                    } else {
                        Log.w(TAG, "Erro", it.exception)
                        Toast.makeText(context, "Falha ao criar o usuário", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    /**
     * Função responsável por checar se já existe usuário logado no app ou não
     */
    fun hasUser() : Boolean {
        if(firebaseAuth.currentUser != null) {
            return true
        }
        return false
    }

    companion object {
        lateinit var user: FirebaseUser
    }
}