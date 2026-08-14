package com.tuname.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.tuname.myapplication.data.FirestoreService

@Composable
fun ShiftSummaryScreen(
    firestoreService: FirestoreService,
    onBack: () -> Unit
) {
    val tables by firestoreService.streamMesas().collectAsState(initial = emptyList())
    val alerts by firestoreService.streamAlertas().collectAsState(initial = emptyList())

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                "RESUMEN TURNO",
                style = MaterialTheme.typography.title3,
                modifier = Modifier.padding(top = 20.dp, bottom = 12.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Mesas Indicator
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = 1f,
                            indicatorColor = Color(0xFF388E3C), // Green
                            strokeWidth = 4.dp
                        )
                        Text(tables.size.toString(), style = MaterialTheme.typography.title2)
                    }
                    Text("MESAS", style = MaterialTheme.typography.caption2)
                }

                // Alertas Indicator
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = 1f,
                            indicatorColor = Color(0xFFFFA000), // Amber
                            strokeWidth = 4.dp
                        )
                        Text(alerts.size.toString(), style = MaterialTheme.typography.title2)
                    }
                    Text("ALERTAS", style = MaterialTheme.typography.caption2)
                }
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
