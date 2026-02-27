package com.example.prueba1.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prueba1.R
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- LOGO ---
        Image(
            painter = painterResource(id = R.drawable.logo), // Asegúrate de que tu imagen se llame logo.png
            contentDescription = "Logo de la Tienda",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 8.dp)
        )

        Text("Registro de Empleado", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val usuario = Usuario(username = username, password = password, email = email)
                vm.register(usuario) { success ->
                    if (success) {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                }
            },
            enabled = username.isNotBlank() && password.isNotBlank()
        ) {
            Text("Registrarse")
        }
    }
}