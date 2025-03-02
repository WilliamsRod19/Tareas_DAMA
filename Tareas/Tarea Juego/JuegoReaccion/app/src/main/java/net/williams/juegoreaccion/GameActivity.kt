package net.williams.juegoreaccion

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var backgroundView: View
    private lateinit var resultText: TextView
    private lateinit var gameArea: RelativeLayout
    private lateinit var headerText: TextView

    private var isGameStarted = false
    private var isGreen = false
    private var gameHandler = Handler(Looper.getMainLooper())
    private var startTime: Long = 0
    private var currentLevel = 1
    // Almacenar los mejores tiempos de cada nivel
    private var level1Time: Long = -1
    private var level2Time: Long = -1

    // Configuración de niveles
    private val levelConfig = mapOf(
        1 to LevelSettings(
            timeLimit = 500L,    // 1 segundo para nivel 1
            minDelay = 2000L,     // 2-5 segundos de espera
            maxDelay = 5000L
        ),
        2 to LevelSettings(
            timeLimit = 300L,     // 0.5 segundos para nivel 2
            minDelay = 1000L,     // 1-3 segundos de espera
            maxDelay = 3000L
        )
    )

    // Clase para configuración de nivel
    data class LevelSettings(
        val timeLimit: Long,      // Tiempo permitido para reaccionar
        val minDelay: Long,       // Tiempo mínimo antes de cambiar a verde
        val maxDelay: Long        // Tiempo máximo antes de cambiar a verde
    )

    private val currentSettings: LevelSettings
        get() = levelConfig[currentLevel] ?: levelConfig[1]!!

    private val randomDelay: Long
        get() = (currentSettings.minDelay..currentSettings.maxDelay).random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        backgroundView = findViewById(R.id.backgroundView)
            ?: throw IllegalStateException("backgroundView not found in layout")
        resultText = findViewById(R.id.resultText)
            ?: throw IllegalStateException("resultText not found in layout")
        gameArea = findViewById(R.id.gameArea)
            ?: throw IllegalStateException("gameArea not found in layout")
        headerText = findViewById(R.id.headerText)
            ?: throw IllegalStateException("headerText not found in layout")

        updateLevelDisplay()
        setupGame()
    }

    private fun updateLevelDisplay() {
        headerText.text = "Nivel $currentLevel"
    }

    private fun setupGame() {
        gameArea.setOnClickListener {
            when {
                !isGameStarted -> startGame()
                isGreen -> handleCorrectTap()
                else -> handleWrongTap()
            }
        }
    }

    private fun startGame() {
        isGameStarted = true
        isGreen = false
        resultText.text = when(currentLevel) {
            1 -> "¡Espera a que cambie a verde!"
            2 -> "¡Sé más rápido esta vez!"
            else -> "¡Prepárate!"
        }
        backgroundView.setBackgroundColor(ContextCompat.getColor(this, R.color.redColor))

        gameHandler.postDelayed({
            if (isGameStarted) {
                backgroundView.setBackgroundColor(ContextCompat.getColor(this, R.color.greenColor))
                isGreen = true
                startTime = System.currentTimeMillis()

                gameHandler.postDelayed({
                    if (isGreen) {
                        handleTimeout()
                    }
                }, currentSettings.timeLimit)
            }
        }, randomDelay)
    }

    private fun handleCorrectTap() {
        val reactionTime = System.currentTimeMillis() - startTime
        isGameStarted = false
        isGreen = false

        when (currentLevel) {
            1 -> {
                level1Time = reactionTime
                resultText.text = "¡Excelente!\nTiempo: ${reactionTime}ms\n¡Avanzas al nivel 2!"
                currentLevel++
                updateLevelDisplay()
                gameHandler.postDelayed({
                    resultText.text = "¡Toca para empezar el nivel 2!"
                }, 2000)
            }
            2 -> {
                level2Time = reactionTime
                // Crear intent y pasar los tiempos como extras
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("LEVEL1_TIME", level1Time)
                    putExtra("LEVEL2_TIME", reactionTime)
                }
                startActivity(intent)
                finish() // Opcional: cierra GameActivity
            }
        }

        gameHandler.removeCallbacksAndMessages(null)
    }

    private fun handleWrongTap() {
        isGameStarted = false
        resultText.text = "¡Muy pronto!\nToca para intentar de nuevo"
        gameHandler.removeCallbacksAndMessages(null)
    }

    private fun handleTimeout() {
        isGameStarted = false
        isGreen = false
        resultText.text = "¡Muy tarde!\nToca para intentar de nuevo"
        backgroundView.setBackgroundColor(ContextCompat.getColor(this, R.color.redColor))
    }

    override fun onDestroy() {
        super.onDestroy()
        gameHandler.removeCallbacksAndMessages(null)
    }

    private fun showFinalResults() {
        setContentView(R.layout.activity_result)

        findViewById<TextView>(R.id.level1Result).text =
            "Nivel 1: ${level1Time}ms"

        findViewById<TextView>(R.id.level2Result).text =
            "Nivel 2: ${level2Time}ms"

        val bestTime = minOf(level1Time, level2Time)
        findViewById<TextView>(R.id.bestTimeText).text =
            "¡Mejor tiempo: ${bestTime}ms!"

        findViewById<Button>(R.id.returnButton).setOnClickListener {
            finish() // Vuelve a MainActivity
        }
    }
}