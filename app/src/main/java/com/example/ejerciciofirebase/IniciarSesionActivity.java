package com.example.ejerciciofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ejerciciofirebase.databinding.ActivityIniciarSesionBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class IniciarSesionActivity extends AppCompatActivity {

    private ActivityIniciarSesionBinding binding;
    private FirebaseAuth auth;
    private static final int rc = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIniciarSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.buttonSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = binding.etEmail.getText().toString();
                String pw = binding.etPassword.getText().toString();
                if ((em.isEmpty()) && (pw.isEmpty())) {
                    Toast.makeText(IniciarSesionActivity.this, "Rellena los campos", Toast.LENGTH_LONG).show();
                } else {
                    loginUser(em, pw);
                }
            }
        });

        binding.buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionGoogle();
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(IniciarSesionActivity.this, "Inicio de sesión realizado con éxito", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(IniciarSesionActivity.this, ContainerActivity.class));
                } else {
                    if (task.getException().getMessage() != null)
                        Toast.makeText(IniciarSesionActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(IniciarSesionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == rc) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(IniciarSesionActivity.this, "Inicio de sesión con Google", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(IniciarSesionActivity.this, ContainerActivity.class);
                            startActivity(intent);
                        } else {
                            if (task.getException().getMessage() != null)
                                Toast.makeText(IniciarSesionActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(IniciarSesionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void sessionGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSignInClient cliente = GoogleSignIn.getClient(this, gso);
        cliente.signOut(); //Util por si tenemos otra cuenta de google con la que hayamos iniciado sesion anteriormente
        startActivityForResult(cliente.getSignInIntent(), rc);
    }



}