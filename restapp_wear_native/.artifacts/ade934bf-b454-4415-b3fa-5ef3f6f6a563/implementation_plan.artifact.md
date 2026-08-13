# Plan: Módulo Exclusivo para Administrador (Versión Final)

Bajo este nuevo esquema, la aplicación tendrá **6 pantallas principales** diseñadas para el control total del Administrador.

## Estructura de Pantallas (6 Interfaces)

1.  **Pantalla de Identificación**: Lista filtrada de usuarios. Solo mostrará a quienes tengan el rol de `admin`.
2.  **Pantalla de PIN (Nueva)**: Interfaz de seguridad con teclado numérico circular para que el Administrador valide su acceso.
3.  **Monitor Global (Dashboard)**: La vista principal tras autenticarse. Muestra todas las mesas del restaurante y su estado actual en tiempo real.
4.  **Alertas Activas**: Lista centralizada de todas las notificaciones (ej: "Mesa 4 pide cuenta", "Mesa 2 llama mesero"). El Admin puede ver quién necesita atención.
5.  **Detalle de Mesa (Liberación)**: Al seleccionar una mesa, el Admin ve la comanda y el botón crítico de **"Liberar Mesa / Confirmar Pago"**.
6.  **Resumen del Restaurante**: Vista estadística del estado general (cuántas mesas ocupadas, cuántas alertas pendientes en total).

## Mejoras de Seguridad y Control

### Autenticación
- El PIN se validará directamente contra el campo `pin` en Firestore.
- Si el Administrador cierra la sesión o reinicia la app, deberá ingresar el PIN nuevamente.

### Acciones Administrativas
- **Liberación Inmediata**: El Admin tendrá la autoridad de limpiar una mesa y borrar sus pedidos con un solo toque, sincronizando instantáneamente las tablets y teléfonos del personal.

## Verificación Plan
- Probar el flujo: Selección de Admin -> Teclado PIN -> Monitor Global.
- Verificar que las alertas de los meseros lleguen al reloj del Administrador.
- Confirmar que la liberación de mesa funcione correctamente.

## User Review Required
> [!IMPORTANT]
> He incluido la **Pantalla de PIN** como paso obligatorio después de seleccionar el nombre. Esto garantiza que nadie más que el Administrador pueda usar el reloj para liberar pagos.

¿Estás de acuerdo con estas 6 interfaces? Si es así, comenzaré con la programación del teclado numérico y el filtrado de roles.
