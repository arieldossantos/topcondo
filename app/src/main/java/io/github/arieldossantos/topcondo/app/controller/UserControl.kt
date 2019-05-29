package io.github.arieldossantos.topcondo.app.controller

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.github.arieldossantos.topcondo.app.MainPage
import android.R.attr.bitmap
import java.io.ByteArrayOutputStream


class UserControl(private val usuario: String, private val senha: String) {
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseFireStore = FirebaseFirestore.getInstance()
    private var firebaseStorage = FirebaseStorage.getInstance()
    private val ref = firebaseStorage.reference
    private val TAG = "UserControl"


    /**
     * Função de logar no aplicativo
     *
     * @param context!! Contexto da aplicação
     *
     */
    fun logar(context: Context) {
        val dialog = ProgressDialog(context as Activity, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT)
            dialog.isIndeterminate = true
            dialog.setTitle("Por favor, aguarde!")
            dialog.setMessage("Estamos verificando o seu usuário e senha.")
            dialog.setCancelable(false)
            dialog.show()

        firebaseAuth.signInWithEmailAndPassword(usuario, senha)
                .addOnCompleteListener {
                    dialog.dismiss()
                    if(it.isSuccessful) {
                        Log.d(TAG, "Sucesso ao realizar login")
                        user = firebaseAuth.currentUser!!
                        val intent = Intent(context, MainPage::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
                        context.startActivity(intent)
                        (context as Activity).finish()
                    } else {
                        Log.w(TAG,"Falha ao realizar login", it.exception)
                        Toast.makeText(context, "Login ou senha inválidos", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    /**
     * Função de registro no app
     */
    fun registrar(context: Context, nomeUsuario: String, imagemUsuario: Bitmap) {
        val dialog = ProgressDialog(context)
            dialog.isIndeterminate = true
            dialog.setTitle("Por favor, aguarde!")
            dialog.setMessage("Estamos registrando você em nosso app.")
            dialog.show()

        val baos = ByteArrayOutputStream()
        imagemUsuario.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        firebaseAuth.createUserWithEmailAndPassword(usuario, senha)
                .addOnCompleteListener {
                    dialog.dismiss()
                    if(it.isSuccessful) {
                        val id = it.getResult()!!.user.uid
                        val refFromProfilePicture = ref.child("perfil/" + id + ".jpg")

                        var uploadTask = refFromProfilePicture.putBytes(data)
                        uploadTask.addOnFailureListener {
                            Toast.makeText(context, "Não foi possível inserir sua imagem, tente novamente mais tarde", Toast.LENGTH_LONG).show()
                        }.addOnSuccessListener {
                            Toast.makeText(context, "Imagem inserida com sucesso!", Toast.LENGTH_LONG).show()

                        }

                        var map = HashMap<String, Any>()
                        map.put("nome", nomeUsuario)
                        map.put("id", id)
                        firebaseFireStore.collection("user")
                                .add(map)
                                .addOnCompleteListener {
                                    if(it.isSuccessful) {
                                        Toast.makeText(context, "Dados gravados com sucesso!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        val intent = Intent(context, MainPage::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
                        context.startActivity(intent)
                        (context as Activity).finish()

                        Log.d(TAG, "Usuário criado")
                        Toast.makeText(context, "Usuário criado com sucesso!", Toast.LENGTH_LONG).show()
                    } else {
                        Log.w(TAG, "Erro", it.exception)
                        Toast.makeText(context, "Falha ao criar o usuário", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    fun sair () {
        firebaseAuth.signOut()
    }

    /**
     * Função responsável por checar se já existe usuário logado no app ou não
     */
    fun hasUser() : Boolean {
        if(firebaseAuth.currentUser != null) {
            user = firebaseAuth.currentUser!!
            return true
        }
        return false
    }

    companion object {
        lateinit var user: FirebaseUser
    }
}