package net.williams.umbrellacorpapp.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.williams.umbrellacorpapp.DetailActivity
import net.williams.umbrellacorpapp.R

class NumberAdapter(private val context: Context, private val numberList: List<Number>) : RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.number_item, parent, false)
        return NumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        val number = numberList[position]
        holder.numberImage.setImageResource(number.imageNumberId)
        holder.numberName.text = number.nameNumber

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("NUMBER", number)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return numberList.size
    }

    class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numberImage: ImageView = itemView.findViewById(R.id.NumberImage)
        val numberName: TextView = itemView.findViewById(R.id.NumberName)
    }
}