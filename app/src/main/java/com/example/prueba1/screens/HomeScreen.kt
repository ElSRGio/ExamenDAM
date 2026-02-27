package com.example.prueba1.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.prueba1.entiy.ProductoConDetalles
import com.example.prueba1.viewmodel.StoreViewModel

@Composable
fun HomeScreen(vm: StoreViewModel, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = vm.listaProductos.filter {
        it.producto.nombre.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("¡Bienvenido, ${vm.currentUser?.username ?: ""}!")
        
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar producto") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(modifier = Modifier.weight(1f).padding(vertical = 8.dp)) {
            items(filteredProducts) { productoConDetalles ->
                ProductoItem(producto = productoConDetalles)
            }
        }

        Button(onClick = { navController.navigate("crear") }) {
            Text("Crear Producto")
        }
    }
}

@Composable
fun ProductoItem(producto: ProductoConDetalles) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = producto.producto.fotoPath,
                contentDescription = "Foto del producto",
                placeholder = painterResource(id = android.R.drawable.ic_menu_camera),
                error = painterResource(id = android.R.drawable.ic_menu_report_image),
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = producto.producto.nombre)
                Text(text = producto.producto.descripcion)
            }
        }
    }
}