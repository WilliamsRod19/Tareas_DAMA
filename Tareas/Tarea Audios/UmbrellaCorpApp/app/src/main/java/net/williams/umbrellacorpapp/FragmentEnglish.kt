package net.williams.umbrellacorpapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.williams.umbrellacorpapp.data.Number
import net.williams.umbrellacorpapp.data.NumberAdapter

class FragmentEnglish : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NumberAdapter
    private val numbersList = mutableListOf<Number>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_english, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewEnglish)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        numbersList.add(Number("One", "One", R.drawable.uno_one, R.raw.one))
        numbersList.add(Number("Two", "Two", R.drawable.dos_two, R.raw.two))
        numbersList.add(Number("Three", "Three", R.drawable.tres_three, R.raw.three))
        numbersList.add(Number("Four", "Fo-ur", R.drawable.cuatro_four, R.raw.four))
        numbersList.add(Number("Five", "Fi-ve", R.drawable.cinco_five, R.raw.five))
        numbersList.add(Number("Six", "Six", R.drawable.seis_six, R.raw.six))
        numbersList.add(Number("Seven", "Se-ven", R.drawable.siete_seven, R.raw.seven))
        numbersList.add(Number("Eight", "Eight", R.drawable.ocho_eigth, R.raw.eight))
        numbersList.add(Number("Nine", "Ni-ne", R.drawable.nueve_nine, R.raw.nine))
        numbersList.add(Number("Ten", "Ten", R.drawable.diez_ten, R.raw.ten))


        adapter = NumberAdapter(requireContext(), numbersList)
        recyclerView.adapter = adapter

        return view
    }
}