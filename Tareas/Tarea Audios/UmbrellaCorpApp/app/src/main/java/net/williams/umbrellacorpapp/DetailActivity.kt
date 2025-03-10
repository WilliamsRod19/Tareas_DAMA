package net.williams.umbrellacorpapp

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.williams.umbrellacorpapp.data.Number
import net.williams.umbrellacorpapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val number = intent.getParcelableExtra<Number>("NUMBER")
        if (number == null) {
            finish()
            return
        }

        binding.detailImageNumber.setImageResource(number.imageNumberId)
        binding.detailNameNumber.text = number.nameNumber
        binding.detailPronunciationNumber.text = number.pronunciationNumber

        binding.playButtonNumber.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, number.audioNumberId)
            }
            mediaPlayer?.start()
        }
    }
}