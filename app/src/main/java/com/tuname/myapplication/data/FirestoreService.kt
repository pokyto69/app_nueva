package com.tuname.myapplication.data

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.tuname.myapplication.model.Alert
import com.tuname.myapplication.model.OrderItem
import com.tuname.myapplication.model.PaymentRequest
import com.tuname.myapplication.model.Table
import com.tuname.myapplication.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun obtenerAdmins(): Flow<List<User>> {
        return db.collection("meseros").snapshots().map { snapshot ->
            snapshot.documents.map { doc ->
                val data = doc.data ?: emptyMap()
                Log.d("FirestoreService", "RAW DATA ID ${doc.id}: $data")
                User(
                    id = doc.id,
                    nombre = doc.get("nombre")?.toString() ?: "Sin nombre",
                    rol = doc.get("rol")?.toString() ?: "mesero",
                    pin = (data["pin"] ?: data["password"] ?: "").toString()
                )
            }.filter { it.rol == "admin" }
        }.catch { e ->
            Log.e("FirestoreService", "Error cargando admins: ${e.message}", e)
            emit(emptyList())
        }
    }

    fun streamMesas(): Flow<List<Table>> {
        return db.collection("mesas").snapshots().map { snapshot ->
            snapshot.documents.map { doc ->
                val ordenList = doc.get("orden") as? List<Map<String, Any>>
                val ordenItems = ordenList?.map { item ->
                    OrderItem(
                        nombre = item["nombre"] as? String ?: "",
                        precio = (item["precio"] as? Number)?.toDouble() ?: 0.0,
                        cantidad = (item["cantidad"] as? Number)?.toInt() ?: 1,
                        platilloId = item["platilloId"] as? String ?: ""
                    )
                } ?: emptyList()

                val solicitudPagoRaw = doc.get("solicitudPago") as? Map<String, Any>
                val solicitudPago = solicitudPagoRaw?.let {
                    PaymentRequest(
                        solicitadoPor = it["solicitadoPor"] as? String ?: "",
                        timestamp = it["timestamp"] as? com.google.firebase.Timestamp
                    )
                }

                Table(
                    id = doc.id,
                    numero = (doc.get("numero") as? Number)?.toInt() ?: 0,
                    status = doc.getString("status") ?: "libre",
                    orden = ordenItems,
                    solicitudPago = solicitudPago,
                    totalCobrar = (doc.get("totalCobrar") as? Number)?.toDouble(),
                    atendidoPor = doc.getString("atendidoPor")
                )
            }.sortedBy { it.numero }
        }
    }

    fun streamAlertas(): Flow<List<Alert>> {
        // Combinamos alertas de la colección 'alertas' y solicitudes de pago en 'mesas'
        val collectionAlerts = db.collection("alertas")
            .whereEqualTo("atendida", false)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.map { doc ->
                    Alert(
                        id = doc.id,
                        mesaId = doc.getString("mesaId") ?: "",
                        mesaNumero = (doc.get("mesaNumero") as? Number)?.toInt() ?: 0,
                        tipo = doc.getString("tipo") ?: "LLAMAR",
                        atendida = false
                    )
                }
            }

        val mesaPaymentAlerts = db.collection("mesas")
            .snapshots()
            .map { snapshot ->
                snapshot.documents.filter { doc ->
                    doc.get("solicitudPago") != null
                }.map { doc ->
                    Alert(
                        id = "payment_${doc.id}",
                        mesaId = doc.id,
                        mesaNumero = (doc.get("numero") as? Number)?.toInt() ?: 0,
                        tipo = "PAGO",
                        atendida = false
                    )
                }
            }

        return combine(collectionAlerts, mesaPaymentAlerts) { list1, list2 ->
            list1 + list2
        }
    }

    fun aprobarPago(mesaId: String) {
        // Siguiendo lógica de confirmarPago en Dart
        db.collection("mesas").document(mesaId).update(
            "solicitudPago", FieldValue.delete(),
            "status", "libre",
            "totalCobrar", FieldValue.delete(),
            "ocupadaDesde", FieldValue.delete(),
            "atendidoPor", FieldValue.delete(),
            "orden", FieldValue.delete()
        )
        
        // También marcamos cualquier alerta manual como atendida
        db.collection("alertas")
            .whereEqualTo("mesaId", mesaId)
            .whereEqualTo("atendida", false)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    document.reference.update("atendida", true)
                }
            }
    }

    fun atenderAlerta(alertaId: String) {
        if (alertaId.startsWith("payment_")) {
            val mesaId = alertaId.removePrefix("payment_")
            db.collection("mesas").document(mesaId).update("solicitudPago", FieldValue.delete())
        } else {
            db.collection("alertas").document(alertaId).update("atendida", true)
        }
    }

    fun generarAlerta(tipo: String, mesaId: String, mesaNumero: Int) {
        if (tipo == "PAGO") {
            db.collection("mesas").document(mesaId).update(
                "solicitudPago", mapOf(
                    "solicitadoPor" to "Cliente (Reloj)",
                    "timestamp" to FieldValue.serverTimestamp()
                )
            )
        } else {
            val alerta = Alert(
                mesaId = mesaId,
                mesaNumero = mesaNumero,
                tipo = tipo,
                atendida = false
            )
            db.collection("alertas").add(alerta)
        }
    }
}
