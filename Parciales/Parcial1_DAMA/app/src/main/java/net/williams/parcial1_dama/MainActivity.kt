package net.williams.parcial1_dama

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.williams.parcial1_dama.adapters.ProfessionalAdapter
import net.williams.parcial1_dama.models.ProfessionalModel

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerProfessionals: RecyclerView
    private lateinit var adapter: ProfessionalAdapter
    private lateinit var professionalList: List<ProfessionalModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerProfessionals = findViewById(R.id.containerRecyclersViewProfessionals)
        recyclerProfessionals.layoutManager = LinearLayoutManager(this)

        professionalList = listOf(
            ProfessionalModel("Ing. Carlos", "Ingeniero", "8 años", "Soy el Ing. Carlos, experto en software. ¡Un gusto estar aquí!", R.drawable.engineer, "60125832"),
            ProfessionalModel("Lic. Josue", "Matematico", "2 años", "Soy el Mat. Josue, especializado en teoría de números. ¡Un placer!", R.drawable.mathematical, "60125832"),
            ProfessionalModel("Dra. Ariel", "Doctorado", "5 años", "Soy la Dra. Ariel, médico en Cirugia dental. ¡Encantada!", R.drawable.doctor, "60125832"),
            ProfessionalModel("Arq. Aaron", "Arquitecto", "4 años", "Soy el Arq. Aaron, diseñador de espacios. ¡Un gusto!", R.drawable.architect, "60125832"),
            ProfessionalModel("Dr. Leo", "Cientifico", "10 años", "Soy el Dr. Leo, investigador en biotecnología. ¡Un honor!", R.drawable.scientist, "60125832")
        )

        adapter = ProfessionalAdapter(this, professionalList)
        recyclerProfessionals.adapter = adapter
    }
}