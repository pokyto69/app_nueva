package com.tuname.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.tuname.myapplication.data.FirestoreService
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MonitorGlobalScreen(
    firestoreService: FirestoreService,
    waiterName: String,
    onTableSelected: (String) -> Unit,
    onSummarySelected: () -> Unit,
    onAlertsSelected: () -> Unit,
    onBack: () -> Unit
) {
    val tables by firestoreService.streamMesas().collectAsState(initial = emptyList())
    var currentTime by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while(true) {
            currentTime = SimpleDateFormat("h:mm", Locale.getDefault()).format(Date())
            kotlinx.coroutines.delay(1000)
        }
    }

    Scaffold(
        timeText = { TimeText() }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                ) {
                    Text(
                        currentTime,
                        style = MaterialTheme.typography.caption1,
                        color = Color.White
                    )
                    Text(
                        waiterName,
                        style = MaterialTheme.typography.caption2,
                        color = Color(0xFF00BCD4)
                    )
                    Text(
                        "MONITOR GLOBAL",
                        style = MaterialTheme.typography.title3,
                        color = Color.White
                    )
                }
            }

            items(tables) { table ->
                val cardColor = when (table.status.lowercase()) {
                    "libre" -> Color(0xFF388E3C) // Green
                    "ocupada" -> Color(0xFF8D3B0D) // Amber/Brown
                    "pago" -> Color(0xFF7B1111) // Red
                    else -> Color(0xFF8D3B0D)
                }

                Card(
                    onClick = { onTableSelected(table.id) },
                    backgroundPainter = CardDefaults.cardBackgroundPainter(
                        startBackgroundColor = cardColor,
                        endBackgroundColor = cardColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("M${table.numero}", style = MaterialTheme.typography.button)
                        Text(table.status.uppercase(), style = MaterialTheme.typography.caption2)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onAlertsSelected,
                        modifier = Modifier.size(ButtonDefaults.SmallButtonSize)
                    ) {
                        Text("🔔")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onSummarySelected,
                        modifier = Modifier.size(ButtonDefaults.SmallButtonSize)
                    ) {
                        Text("📊")
                    }
                }
            }

            item {
                CompactChip(
                    onClick = onBack,
                    label = { Text("Cerrar Sesión") },
                    modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
                )
            }
        }
    }
}
