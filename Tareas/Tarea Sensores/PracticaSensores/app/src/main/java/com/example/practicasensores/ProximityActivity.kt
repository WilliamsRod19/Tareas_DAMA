package com.example.practicasensores

import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProximityActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null
    private lateinit var txtValue: TextView
    private lateinit var rootView: android.view.View

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proximity)

        txtValue = findViewById(R.id.txtProximityValue)
        rootView = findViewById(R.id.myRelativeLayout)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            txtValue.text = "No hay sensor de proximidad"
        } else {
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val valor = event.values[0]
                    txtValue.text = "VALOR: $valor"

                    if (valor < proximitySensor!!.maximumRange) {
                        rootView.setBackgroundColor(Color.RED)
                        txtValue.setBackgroundColor(Color.WHITE)
                        txtValue.setTextColor(Color.RED)
                    } else {
                        rootView.setBackgroundColor(Color.CYAN)
                        txtValue.setBackgroundColor(Color.DKGRAY)
                        txtValue.setTextColor(Color.WHITE)
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }, proximitySensor, SensorManager.SENSOR_DELAY_UI)
        }
    }
}