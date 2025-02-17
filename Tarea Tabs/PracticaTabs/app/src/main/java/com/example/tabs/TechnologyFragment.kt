package com.example.tabs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class TechnologyFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container:
    ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_technology, container, false)
        val tvTecnologias = view.findViewById<TextView>(R.id.tvTecnologias)
        tvTecnologias.text = "-C# \n- Nodejs\n- Python\n- PHP\n-JavaScript \n- Kotlin\n- HTML\n" +
                "- CSS"

        view.findViewById<Button>(R.id.btnContactame).setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL,
                    arrayOf("wr19092001@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Me interesa")
            }
            startActivity(intent)
        }
        return view
    }
}
