package com.tuname.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.tuname.myapplication.data.FirestoreService
import com.tuname.myapplication.screens.*
import com.tuname.myapplication.theme.RestAppTheme
import com.tuname.myapplication.util.HapticService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        
        // Ensure authentication for Firestore rules
        FirebaseAuth.getInstance().signInAnonymously()

        setContent {
            val firestoreService = remember { FirestoreService() }
            val hapticService = remember { HapticService(this) }
            WearApp(firestoreService, hapticService)
        }
    }
}

@Composable
fun WearApp(firestoreService: FirestoreService, hapticService: HapticService) {
    val navController = rememberSwipeDismissableNavController()
    var currentWaiterName by remember { mutableStateOf("") }

    RestAppTheme {
        Scaffold(
            timeText = { TimeText() }
        ) {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "identification",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("identification") {
                    IdentificationScreen(
                        firestoreService = firestoreService,
                        onAdminSelected = { adminName ->
                            currentWaiterName = adminName
                            navController.navigate("monitor_global")
                        }
                    )
                }

                composable("monitor_global") {
                    MonitorGlobalScreen(
                        firestoreService = firestoreService,
                        waiterName = currentWaiterName,
                        onTableSelected = { mesaId ->
                            navController.navigate("table_detail/$mesaId")
                        },
                        onSummarySelected = {
                            navController.navigate("shift_summary")
                        },
                        onAlertsSelected = {
                            navController.navigate("active_alerts")
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("active_alerts") {
                    ActiveAlertsScreen(
                        firestoreService = firestoreService,
                        hapticService = hapticService,
                        waiterName = currentWaiterName,
                        onSummarySelected = {
                            navController.navigate("shift_summary")
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(
                    "table_detail/{mesaId}",
                    arguments = listOf(navArgument("mesaId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val mesaId = backStackEntry.arguments?.getString("mesaId") ?: ""
                    TableDetailScreen(
                        firestoreService = firestoreService,
                        mesaId = mesaId,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("shift_summary") {
                    ShiftSummaryScreen(
                        firestoreService = firestoreService,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
