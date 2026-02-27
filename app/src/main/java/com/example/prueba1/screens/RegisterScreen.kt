package com.example.prueba1.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prueba1.entiy.Usuario
import com.example.prueba1.viewmodel.StoreViewModel

@Composable
fun RegisterScreen(vm: StoreViewModel, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val usuario = Usuario(username = username, password = password, email = email)
            vm.register(usuario) { success ->
                if (success) {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }
        }) {
            Text("Registrarse")
        }
    }
}