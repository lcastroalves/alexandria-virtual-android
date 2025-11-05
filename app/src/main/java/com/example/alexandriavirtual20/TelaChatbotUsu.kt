package com.example.alexandriavirtual20

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.MensagemAdapter
import com.example.alexandriavirtual20.model.Mensagem
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TelaChatbotUsu.newInstance] factory method to
 * create an instance of this fragment.
 */
class TelaChatbotUsu : Fragment() {
    // TODO: Rename and change types of parameters

    private val mensagens = mutableListOf<Mensagem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MensagemAdapter
    private lateinit var editMensagem: EditText
    private lateinit var btnEnviar: ImageButton
    private lateinit var generativeModel: GenerativeModel
    private lateinit var prePrompt: String
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
        return inflater.inflate(R.layout.tela_chatbot_usu, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referências
        recyclerView = view.findViewById(R.id.recyclerView)
        editMensagem = view.findViewById(R.id.edtMensagem)
        btnEnviar = view.findViewById(R.id.botaoEnviar)

        // Configura RecyclerView + Adapter
        adapter = MensagemAdapter(mensagens)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.adicionarMensagem(
            Mensagem("Olá! Eu sou a Hipátia. Como posso ajudar?", false)
        )

        generativeModel = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = "AIzaSyCb0iIhwqmICRCrOcT64gsVH4bzb9WRsfk")

        prePrompt = "A pergunta a seguir, responda como uma bibliotecária simpática que adora livros de ficção científica"
    }

    //AIzaSyCb0iIhwqmICRCrOcT64gsVH4bzb9WRsfk
    override fun onStart() {
        super.onStart()

        btnEnviar.setOnClickListener {
            val texto = editMensagem.text.toString().trim()
            if (texto.isNotEmpty()) {
                enviarMensagemUsuario(editMensagem.text.toString())
                editMensagem.text.clear()
            }

            //thread paralela
            lifecycleScope.launch {{
                val response = generativeModel.generateContent(editMensagem.text.toString())
                response.text ?: "Sem resposta do modelo"
            }
        }

    }
    private fun enviarMensagemUsuario(texto: String) {
        // Adiciona mensagem do usuário
        adapter.adicionarMensagem(Mensagem(texto, true))
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

}