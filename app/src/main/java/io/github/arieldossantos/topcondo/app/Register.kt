package io.github.arieldossantos.topcondo.app

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import io.github.arieldossantos.topcondo.R
import io.github.arieldossantos.topcondo.app.controller.UserControl
import kotlinx.android.synthetic.main.activity_register.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.app.Activity


class Register : AppCompatActivity() {
    companion object {
        val IMAGE_REQUEST_CODE = 524
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imageViewImagem.setOnClickListener {
            getImageFromCamera()
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (IMAGE_REQUEST_CODE === requestCode && resultCode === Activity.RESULT_OK) {
            val bitmap = data!!.getExtras().get("data") as Bitmap
            imageViewImagem.setImageBitmap(bitmap)
        }
    }

    fun getImageFromCamera(){
        val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(photoCaptureIntent, IMAGE_REQUEST_CODE)
    }

}
