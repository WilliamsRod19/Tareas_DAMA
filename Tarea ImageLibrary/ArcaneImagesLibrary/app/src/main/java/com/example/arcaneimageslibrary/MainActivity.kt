package com.example.arcaneimageslibrary

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewFlipper = findViewById<ViewFlipper>(R.id.viewFlipper)

        val images = listOf(
            R.drawable.img_arcane1,
            R.drawable.img_arcane2,
            R.drawable.img_arcane3,
            R.drawable.img_arcane4,
            R.drawable.img_arcane5
        )

        images.forEach { imageResource ->
            val imageView = ImageView(this)
            imageView.setImageResource(imageResource)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            viewFlipper.addView(imageView)
        }

        viewFlipper.apply {
            isAutoStart = true
            flipInterval = 3000
        }

        val btnWebSite = findViewById<Button>(R.id.btnWebSite)
        btnWebSite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.netflix.com/sv/title/81435684"))
            startActivity(intent)
        }
    }
}