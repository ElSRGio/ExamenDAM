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
import com.example.prueba1.viewmodel.StoreViewModel

@Composable
fun LoginScreen(vm: StoreViewModel, navController: NavController) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- LOGO ---
        Image(
            painter = painterResource(id = R.drawable.logo), // Asegúrate de que tu imagen se llame logo.png
            contentDescription = "Logo de la Tienda",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp)
        )

        Text("Acceso al Sistema", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = user, onValueChange = { user = it }, label = { Text("Usuario") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            vm.login(user, pass) { success ->
                if (success) navController.navigate("home")
            }
        }) {
            Text("Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}