package com.example.ejerciciofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ejerciciofirebase.databinding.RegistrarUsuarioBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private RegistrarUsuarioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RegistrarUsuarioBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                String password2 = binding.editTextPassword2.getText().toString();
                if ((correo.isEmpty()) && (password.isEmpty()) && (password2.isEmpty())) {
                    Toast.makeText(RegistrarUsuarioActivity.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                } else if ((!password.equals(password2))) {
                    Toast.makeText(RegistrarUsuarioActivity.this, "Las contraseñas deben ser iguales", Toast.LENGTH_LONG).show();
                } else {
                    createAccount(correo, password);
                }
            }
        });

        binding.buttonInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrarUsuarioActivity.this, IniciarSesionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null)
            reload();
    }

    private void reload() { }

    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //Loggeo completado, actualiza la UI con la informacion del usuario
                            //HashMap<String, String> mapa = new HashMap<>();
                            //mapa.put("email", email);
                            //mapa.put("password", password);
                            //FirebaseUser user = auth.getCurrentUser();
                            //La contraseña debe tener minimo 6 caracteres
                            Toast.makeText(RegistrarUsuarioActivity.this, "Usuario registrado con exito", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrarUsuarioActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}