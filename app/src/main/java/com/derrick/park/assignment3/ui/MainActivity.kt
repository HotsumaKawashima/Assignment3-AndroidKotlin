package com.derrick.park.assignment3.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.derrick.park.assignment3.R

class MainActivity : AppCompatActivity() {

    /**
     * Our MainActivity is only responsible for setting the content view that contains
     * the Navigation Host
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
