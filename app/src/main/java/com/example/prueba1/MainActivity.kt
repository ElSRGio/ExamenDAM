package com.example.prueba1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.prueba1.database.AppDatabase
import com.example.prueba1.screens.CrearProductoScreen
import com.example.prueba1.screens.HomeScreen
import com.example.prueba1.screens.LoginScreen
import com.example.prueba1.screens.RegisterScreen
import com.example.prueba1.viewmodel.StoreViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicialización de la DB
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tienda_db").build()
        val dao = db.appDao()
        
        setContent {
            val navController = rememberNavController()
            val vm: StoreViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StoreViewModel(dao, applicationContext) as T
                }
            })

            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(vm, navController) }
                composable("register") { RegisterScreen(vm, navController) }
                composable("home") { HomeScreen(vm, navController) }
                composable("crear") { CrearProductoScreen(vm, navController) }
            }
        }
    }
}