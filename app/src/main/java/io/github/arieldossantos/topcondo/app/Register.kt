package io.github.arieldossantos.topcondo.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.arieldossantos.topcondo.R

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)
    }

}
