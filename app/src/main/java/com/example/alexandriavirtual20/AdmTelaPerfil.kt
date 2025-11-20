package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdmTelaPerfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdmTelaPerfil : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var btnEditarImagem: ImageButton
    private lateinit var btnInfoPessoais: LinearLayout
    private lateinit var fotoPerfil : ImageView
    private lateinit var nomeUsuario : TextView
    private lateinit var btnAddAdm: LinearLayout
    private lateinit var btnSair: LinearLayout
    private lateinit var abrirGaleria: ActivityResultLauncher<String>

    private lateinit var fireBase : FirebaseFirestore
    private lateinit var fbAuth: FirebaseAuth

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
        return inflater.inflate(R.layout.adm_tela_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnEditarImagem = view.findViewById(R.id.botaoEditarImagemAdm)
        btnInfoPessoais = view.findViewById(R.id.botaoInfoPes)
        btnAddAdm = view.findViewById(R.id.botaoAddAdm)
        btnSair = view.findViewById(R.id.botaoSair)
        fotoPerfil = view.findViewById(R.id.fotoPerfilAdm)
        nomeUsuario = view.findViewById(R.id.textView5)

        fireBase = FirebaseFirestore.getInstance()
        fbAuth = FirebaseAuth.getInstance()

        val idUsuario = fbAuth.currentUser?.uid

        if(idUsuario != null){
            fireBase.collection("usuario").document(idUsuario).get().addOnSuccessListener { doc ->
                if(doc != null && doc.exists()){
                    val nomeUsu = doc.getString("usuario")
                    nomeUsuario.text = nomeUsu
                }
            }
        }

        abrirGaleria = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){uri ->
            if(uri != null){
                // imagem capturada
                fotoPerfil.setImageURI(uri)

                // Converter para base64
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                val fotoPerfilBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)

                if (idUsuario != null) {
                    fireBase.collection("usuario").document(idUsuario).update("fotoPerfil", fotoPerfilBase64)

                }
            }
        }

        btnEditarImagem.setOnClickListener {
            abrirGaleria.launch("image/*")
        }

        btnInfoPessoais.setOnClickListener {
            val intent = Intent(requireContext(), AdmInfoPessoais::class.java)
            startActivity(intent)
        }
        btnAddAdm.setOnClickListener {
            val intent = Intent(requireContext(), AdmTelaAdicionarAdm::class.java)
            startActivity(intent)
        }
        btnSair.setOnClickListener {

            fbAuth.signOut()

            val intent = Intent(requireContext(), TelaLogin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK   //Para garantir que o usuário não volte para a tela anterior apertando o botão de voltar
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
            AdmTelaPerfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}