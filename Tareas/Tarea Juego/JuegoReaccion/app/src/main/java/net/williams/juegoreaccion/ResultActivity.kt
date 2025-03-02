package net.williams.juegoreaccion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Obtener los tiempos del intent
        val level1Time = intent.getLongExtra("LEVEL1_TIME", -1)
        val level2Time = intent.getLongExtra("LEVEL2_TIME", -1)

        // Mostrar resultados
        findViewById<TextView>(R.id.level1Result).text =
            "Nivel 1: ${level1Time}ms"

        findViewById<TextView>(R.id.level2Result).text =
            "Nivel 2: ${level2Time}ms"

        val bestTime = minOf(level1Time, level2Time)
        findViewById<TextView>(R.id.bestTimeText).text =
            "¡Mejor tiempo: ${bestTime}ms!"

        findViewById<Button>(R.id.returnButton).setOnClickListener {
            // Crear un intent para volver al MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}