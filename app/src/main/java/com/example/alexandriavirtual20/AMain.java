package com.example.alexandriavirtual20;

import android.os.Bundle;
import android.renderscript.ScriptGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.alexandriavirtual20.databinding.AMainBinding;

public class AMain extends AppCompatActivity {

    AMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new TelaInicioUsu());

        binding.bottomNavigationView.setOnItemSelectedListener(menuItem -> {

            int id = menuItem.getItemId();

            if (id == R.id.inicio) {
                replaceFragment(new TelaInicioUsu());
            } else if (id == R.id.emprestimo) {

            } else if (id == R.id.chatbot) {

            } else if (id == R.id.reserva) {

            } else if (id == R.id.perfil) {
                replaceFragment(new TelaPerfilUsu());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
