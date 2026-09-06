package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.core.navigation.AppNavGraph
import com.example.core.theme.ElectriciansAppTheme
import com.example.data.remote.FirebaseAppCheckManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseAppCheckManager.initialize(applicationContext)
        setContent {
            ElectriciansAppTheme {
                AppNavGraph()
            }
        }
    }
}

