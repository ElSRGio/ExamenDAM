package com.example.prueba1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.prueba1.database.AppDatabase
import com.example.prueba1.entiy.Usuario
import com.example.prueba1.screens.CrearProductoScreen
import com.example.prueba1.screens.HomeScreen
import com.example.prueba1.screens.LoginScreen
import com.example.prueba1.screens.RegisterScreen
import com.example.prueba1.ui.theme.Prueba1Theme
import com.example.prueba1.viewmodel.StoreViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tienda_db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Insertar usuario por defecto
                    lifecycleScope.launch {
                        val appDb = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tienda_db").build()
                        val dao = appDb.appDao()
                        dao.insertUsuario(Usuario(username = "elsrg", password = "elsrg1", email = "elsrg@gmail.com"))
                        appDb.close() // Cerrar esta instancia para evitar conflictos
                    }
                }
            })
            .build()
        val dao = db.appDao()
        
        setContent {
            Prueba1Theme {
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
}