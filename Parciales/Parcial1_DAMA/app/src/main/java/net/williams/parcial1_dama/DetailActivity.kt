package net.williams.parcial1_dama

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.appcompat.app.AppCompatActivity
import net.williams.parcial1_dama.models.ProfessionalModel

class DetailActivity : AppCompatActivity() {
    private lateinit var imgDetail: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtAbout: TextView
    private lateinit var btnCall: Button
    private lateinit var btnWhatsApp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imgDetail = findViewById(R.id.imageViewDetailProfessional)
        txtName = findViewById(R.id.txtDetailName)
        txtAbout = findViewById(R.id.txtDetailAbout)
        btnCall = findViewById(R.id.btnCalling)
        btnWhatsApp = findViewById(R.id.btnMsjWhatsApp)

        val professional = intent.getSerializableExtra("Professional") as? ProfessionalModel

        professional?.let { prof ->
            txtName.text = prof.pName
            txtAbout.text = prof.pAbout
            Glide.with(this).load(prof.pImage).into(imgDetail)

            btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${prof.pPhone}"))
                startActivity(intent)
            }

            btnWhatsApp.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/${prof.pPhone}"))
                startActivity(intent)
            }
        }
    }
}