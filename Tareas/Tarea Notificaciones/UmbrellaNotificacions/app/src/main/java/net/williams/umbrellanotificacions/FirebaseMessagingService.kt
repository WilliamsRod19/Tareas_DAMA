package net.williams.umbrellanotificacions

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(mensajeRemoto: RemoteMessage){
        super.onMessageReceived(mensajeRemoto)
        mensajeRemoto.notification?.let {
            Log.d("FCM", "Mensake recibido: Titulo=${it.title}, Cuerpo=${it.body}")
            mostrarNotificacion(it.title ?: "Tituelo", it.body ?: "Cuerpo")
        }

        mensajeRemoto.data.isNotEmpty().let {
            val tipo = mensajeRemoto.data["tipo"]
            val mensaje = mensajeRemoto.data["mensaje"]
            Log.d("FCM", "Datos Recibidos: tipo=${tipo}, mensaje=${mensaje}")

        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo Token: $token")
    }

    private fun mostrarNotificacion(titulo: String, cuerpo: String){
        val idCanal = "canal_predeterminado"
        val idNotification = 1

        val administradorNotificaciones = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val canal = NotificationChannel(
                idCanal,
                "Canal Predeterminado",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            administradorNotificaciones.createNotificationChannel(canal)
        }

        val notification = NotificationCompat.Builder(this, idCanal)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(cuerpo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        administradorNotificaciones.notify(idNotification, notification)
    }

}