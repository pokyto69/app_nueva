package com.tuname.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.tuname.myapplication.data.FirestoreService
import com.tuname.myapplication.util.HapticService

import androidx.compose.ui.text.font.FontWeight
import androidx.wear.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Notifications

@Composable
fun ActiveAlertsScreen(
    firestoreService: FirestoreService,
    hapticService: HapticService,
    waiterName: String,
    onSummarySelected: () -> Unit,
    onBack: () -> Unit
) {
    val alerts by firestoreService.streamAlertas().collectAsState(initial = emptyList())
    var lastAlertCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(alerts) {
        if (alerts.size > lastAlertCount) {
            val lastAlert = alerts.lastOrNull()
            if (lastAlert?.tipo == "PAGO") {
                hapticService.vibratePedirCuenta()
            } else {
                hapticService.vibrateLlamarMesero()
            }
        }
        lastAlertCount = alerts.size
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
                        waiterName,
                        style = MaterialTheme.typography.caption2,
                        color = Color(0xFF00BCD4)
                    )
                    Text(
                        "Alertas Activas",
                        style = MaterialTheme.typography.title3.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            if (alerts.isEmpty()) {
                item {
                    Text("Sin alertas", modifier = Modifier.padding(top = 20.dp))
                }
            } else {
                items(alerts) { alert ->
                    Card(
                        onClick = { firestoreService.atenderAlerta(alert.id) },
                        backgroundPainter = CardDefaults.cardBackgroundPainter(
                            startBackgroundColor = Color(0xFF7B1111), // Dark Red
                            endBackgroundColor = Color(0xFF7B1111)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (alert.tipo == "PAGO") Icons.Default.AttachMoney else Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Mesa ${alert.mesaNumero} / ${if (alert.tipo == "PAGO") "PIDE CUENTA" else "LLAMADA"}",
                                style = MaterialTheme.typography.caption1,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onSummarySelected,
                        modifier = Modifier.size(ButtonDefaults.SmallButtonSize)
                    ) {
                        Text("📊")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    CompactChip(
                        onClick = onBack,
                        label = { Text("Atrás") }
                    )
                }
            }
        }
    }
}
