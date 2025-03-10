package net.williams.umbrellacorpapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.williams.umbrellacorpapp.data.NumberAdapter
import net.williams.umbrellacorpapp.data.Number

class FragmentSpanish : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NumberAdapter
    private val numbersList = mutableListOf<Number>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_spanish, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewSpanish)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        numbersList.add(Number("Uno", "U-no", R.drawable.uno_one, R.raw.uno))
        numbersList.add(Number("Dos", "Dos", R.drawable.dos_two, R.raw.dos))
        numbersList.add(Number("Tres", "Tres", R.drawable.tres_three, R.raw.tres))
        numbersList.add(Number("Cuatro", "Cua-tro", R.drawable.cuatro_four, R.raw.cuatro))
        numbersList.add(Number("Cinco", "Cin-co", R.drawable.cinco_five, R.raw.cinco))
        numbersList.add(Number("Seis", "Se-is", R.drawable.seis_six, R.raw.seis))
        numbersList.add(Number("Siete", "Sie-te", R.drawable.siete_seven, R.raw.siete))
        numbersList.add(Number("Ocho", "O-cho", R.drawable.ocho_eigth, R.raw.ocho))
        numbersList.add(Number("Nueve", "Nue-ve", R.drawable.nueve_nine, R.raw.nueve))
        numbersList.add(Number("Diez", "Di-ez", R.drawable.diez_ten, R.raw.diez))

        adapter = NumberAdapter(requireContext(), numbersList)
        recyclerView.adapter = adapter

        return view
    }
}