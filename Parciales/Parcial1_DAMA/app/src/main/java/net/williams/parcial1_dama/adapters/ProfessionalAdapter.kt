package net.williams.parcial1_dama.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.williams.parcial1_dama.DetailActivity
import net.williams.parcial1_dama.models.ProfessionalModel
import net.williams.parcial1_dama.R

class ProfessionalAdapter(private val context: Context, private var professionalList: List<ProfessionalModel>) :
    RecyclerView.Adapter<ProfessionalAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfessional: ImageView = view.findViewById(R.id.imageViewProfessional)
        val txtName: TextView = view.findViewById(R.id.txtName)
        val txtProfession: TextView = view.findViewById(R.id.txtProfession)
        val txtExperience: TextView = view.findViewById(R.id.txtExperience)
        val txtAbout: TextView = view.findViewById(R.id.txtAbout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.professional_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val professional = professionalList[position]

        holder.txtName.text = professional.pName
        holder.txtProfession.text = professional.pProfession
        holder.txtExperience.text = "Años de experiencia: \n" + professional.pExperience
        holder.txtAbout.text = "Acerca de El/Ella: \n" + professional.pAbout

        Glide.with(context).load(professional.pImage).into(holder.imgProfessional)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("Professional", professional)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = professionalList.size

}