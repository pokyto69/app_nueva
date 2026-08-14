package com.tuname.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.tuname.myapplication.data.FirestoreService
import com.tuname.myapplication.model.Table

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DirectionsRun

@Composable
fun TableDetailScreen(
    firestoreService: FirestoreService,
    mesaId: String,
    onBack: () -> Unit
) {
    val tables by firestoreService.streamMesas().collectAsState(initial = emptyList())
    val table = tables.find { it.id == mesaId }

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (table != null) {
            item {
                Text(
                    "MESA ${table.numero}",
                    style = MaterialTheme.typography.title3,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
            }

            // Alerta de pago activa
            if (table.solicitudPago != null || table.status == "pago") {
                item {
                    Card(
                        onClick = {},
                        backgroundPainter = CardDefaults.cardBackgroundPainter(
                            startBackgroundColor = Color(0xFF7B1111), // Dark Red
                            endBackgroundColor = Color(0xFF7B1111)
                        ),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("PIDE CUENTA", style = MaterialTheme.typography.button)
                        }
                    }
                }
            }

            // Listado de platillos en la orden
            if (table.orden.isNotEmpty()) {
                item {
                    Text("Orden Actual:", style = MaterialTheme.typography.caption2, color = Color.Gray)
                }
                items(table.orden) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.cantidad}x ${item.nombre}", style = MaterialTheme.typography.caption1)
                        Text("$${(item.precio * item.cantidad).toInt()}", style = MaterialTheme.typography.caption2)
                    }
                }
                item {
                    Text("Total: $${table.totalCobrar?.toInt() ?: 0}", 
                        style = MaterialTheme.typography.title3, 
                        color = Color(0xFF00BCD4),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                item {
                    Text("Mesa sin pedidos", style = MaterialTheme.typography.caption2)
                }
            }

            if (table.solicitudPago != null || table.status == "pago") {
                item {
                    Button(
                        onClick = {
                            firestoreService.aprobarPago(mesaId)
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF388E3C))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsRun,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Aprobar Pago")
                        }
                    }
                }
            }
        } else {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(top = 20.dp))
            }
        }

        item {
            CompactChip(
                onClick = onBack,
                label = { Text("Atrás") },
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            )
        }
    }
}
