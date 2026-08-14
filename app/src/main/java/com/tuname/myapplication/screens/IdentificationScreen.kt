package com.tuname.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.tuname.myapplication.data.FirestoreService
import com.tuname.myapplication.model.User

@Composable
fun IdentificationScreen(
    firestoreService: FirestoreService,
    onAdminSelected: (String) -> Unit
) {
    val users by firestoreService.obtenerAdmins().collectAsState(initial = null)
    
    androidx.wear.compose.foundation.lazy.ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "IDENTIFICACIÓN",
                style = MaterialTheme.typography.caption1,
                color = Color(0xFFFFA000), // Amber
                modifier = Modifier.padding(bottom = 8.dp, top = 24.dp)
            )
        }

        if (users == null) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp).padding(top = 10.dp)
                )
            }
        } else if (users!!.isEmpty()) {
            item {
                Text(
                    "No se encontraron perfiles de Administrador.",
                    style = MaterialTheme.typography.caption2,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )
            }
        } else {
            items(users!!.size) { index ->
                val user = users!![index]
                Chip(
                    onClick = { onAdminSelected(user.nombre) },
                    label = { Text(user.nombre, color = Color.White) },
                    secondaryLabel = { Text(user.rol.uppercase(), color = Color(0xFF00BCD4)) },
                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = Color(0xFF20293A)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }
        }
    }
}
