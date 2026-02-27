package com.example.prueba1.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.prueba1.entiy.Producto
import com.example.prueba1.viewmodel.StoreViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: StoreViewModel, navController: NavController) {
    var productoABorrar by remember { mutableStateOf<Producto?>(null) }

    if (productoABorrar != null) {
        AlertDialog(
            onDismissRequest = { productoABorrar = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas borrar el producto '${productoABorrar?.nombre}'?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.borrarProducto(productoABorrar!!)
                    productoABorrar = null
                }) { Text("Borrar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { productoABorrar = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inventario de ${vm.currentUser?.username}") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                vm.limpiarFoto() // --- EL SECRETO: Limpiamos la cámara antes de entrar ---
                navController.navigate("crear")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(vm.listaProductos) { item ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Coil lee perfectamente los archivos físicos (File)
                        if (item.producto.fotoPath != null) {
                            AsyncImage(
                                model = File(item.producto.fotoPath), // Leemos desde el disco duro
                                contentDescription = null,
                                modifier = Modifier.size(80.dp)
                            )
                        } else {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(80.dp))
                        }

                        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                            Text(text = item.producto.nombre, style = MaterialTheme.typography.titleLarge)
                            Text(text = "Depto: ${item.producto.tipo}", color = MaterialTheme.colorScheme.primary)
                            Text(text = "Precio: $${item.producto.precio}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = item.producto.descripcion, style = MaterialTheme.typography.bodySmall)
                        }

                        Column {
                            IconButton(onClick = {
                                vm.limpiarFoto()
                                navController.navigate("editar/${item.producto.id}")
                            }) { Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary) }

                            IconButton(onClick = { productoABorrar = item.producto }) {
                                Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}