package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroAdapter
import com.example.alexandriavirtual20.model.Livro
import kotlin.jvm.java


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TelaEmprestLivrosUsu.newInstance] factory method to
 * create an instance of this fragment.
 */
class TelaEmprestLivrosUsu : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.tela_emprest_livros_usu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerLivros)
        val btnConfirmar: Button = view.findViewById(R.id.btnConfirmar)
        val btnBack: ImageButton = view.findViewById(R.id.btnback)
        val spinnerAutor: Spinner = view.findViewById(R.id.spinnerAutor)
        val spinnerGenero: Spinner = view.findViewById(R.id.spinnerGenero)
        val spinnerMaisPopulares: Spinner = view.findViewById(R.id.spinnerMaisPopulares)
        val spinnerLancamento: Spinner = view.findViewById(R.id.spinnerLancamento)

        ArrayAdapter.createFromResource( requireContext(), R.array.autores_array, android.R.layout.simple_spinner_item ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAutor.adapter = adapter }
        ArrayAdapter.createFromResource( requireContext(), R.array.generos_array, android.R.layout.simple_spinner_item ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGenero.adapter = adapter }
        ArrayAdapter.createFromResource( requireContext(), R.array.populares_array, android.R.layout.simple_spinner_item ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMaisPopulares.adapter = adapter }
        ArrayAdapter.createFromResource( requireContext(), R.array.lancamentos_array, android.R.layout.simple_spinner_item ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerLancamento.adapter = adapter }

        val livros = listOf(

            Livro(
                "Ciência da Computação e Tecnologias Digitais",
                "Ernane Rosa Martins",
                R.drawable.livro1,
                "⭐ 130 avaliações"
            ),
            Livro("Java Como Programar",
                "Paul J. Deitel",
                R.drawable.livro2,
                "⭐ 105 avaliações"),
            Livro(
                "Java como Programar",
                "Paul J. Deitel",
                R.drawable.livro3,
                "⭐ 200 avaliações",

                ),
            Livro("Redes de Computadores",
                "Tanenbaum & Wetherall",
                R.drawable.livro4,
                "⭐ 250 avaliações"

            )
        )

        val adapter = LivroAdapter(livros) { livro ->
            val intent = Intent(requireContext(), TelaAvaliacoesUsu::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        btnConfirmar.setOnClickListener {
            val selecionados = adapter.getSelecionados()

            if (selecionados.isEmpty()) {
                Toast.makeText(requireContext(), "Nenhum livro selecionado!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val intent = Intent(requireContext(), TelaRetirarLivroUsu::class.java)
            intent.putParcelableArrayListExtra("livrosSelecionados", ArrayList(selecionados))

            startActivity(intent)
        }


        btnBack.setOnClickListener {
            val intent = Intent(requireContext(), TelaMenuEmprestUsu::class.java)
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
            TelaEmprestLivrosUsu().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
