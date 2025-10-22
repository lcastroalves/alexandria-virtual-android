package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdmTelaInicio.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdmTelaInicio : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnProd : Button
    private lateinit var btnUsuCad : Button
    private lateinit var btnEvent : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.adm_tela_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnProd = view.findViewById(R.id.btnProd)
        btnUsuCad = view.findViewById(R.id.btnUsuCad)
        btnEvent = view.findViewById(R.id.btnEventos)

        btnProd.setOnClickListener {
            val intent = Intent(requireContext(), AdmTelaProdutos::class.java)
            startActivity(intent)
        }

        btnUsuCad.setOnClickListener {
            val intent = Intent(requireContext(), AdmTelaUsuCadast::class.java)
            startActivity(intent)
        }

        btnEvent.setOnClickListener {
            val intent = Intent(requireContext(), AdmTelaEventos::class.java)
            startActivity(intent)
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdmTelaInicio().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}