package com.example.prueba1.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prueba1.viewmodel.StoreViewModel

@Composable
fun HomeScreen(vm: StoreViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("¡Bienvenido!")
        Button(onClick = { navController.navigate("crear") }) {
            Text("Crear Producto")
        }
    }
}