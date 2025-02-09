package com.example.practicasensores

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LuminicActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private lateinit var txtLuminous: TextView
    private lateinit var btnSend: Button
    private var luminousValue: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_luminic)

        txtLuminous = findViewById(R.id.txtLuminous)
        btnSend = findViewById(R.id.btnSend)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (lightSensor == null) {
            txtLuminous.text = "No hay sensor de luz"
        } else {
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    luminousValue = event.values[0]
                    txtLuminous.text = "VALOR: $luminousValue LUX"
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }, lightSensor, SensorManager.SENSOR_DELAY_UI)
        }

        btnSend.setOnClickListener {
            enviarPorWhatsApp(luminousValue)
        }
    }

    private fun enviarPorWhatsApp(valor: Float) {
        val intent = Intent(Intent.ACTION_VIEW)
        val mensaje = "El sensor de luminosidad detectó: $valor LUX"
        val uri = "https://api.whatsapp.com/send?text=${Uri.encode(mensaje)}"
        intent.data = Uri.parse(uri)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp no está instalado", Toast.LENGTH_LONG).show()
        }
    }
}