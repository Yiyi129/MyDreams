package com.example.mydreams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class vRegistro extends AppCompatActivity {
    private EditText name, username, email, psw;
    private Button regis, cancel;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vregistro);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.txtNombre);
        username = findViewById(R.id.txtUsuario);
        email = findViewById(R.id.txtEmaill);
        psw = findViewById(R.id.txtPswl);

        regis = findViewById(R.id.btnRegistro);
        cancel = findViewById(R.id.btnCancelar);

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = name.getText().toString().trim();
                String usna = username.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String pass = psw.getText().toString().trim();

                if (nombre.isEmpty() && usna.isEmpty() && email1.isEmpty() && pass.isEmpty()){
                    Toast.makeText(vRegistro.this, "Ingresa tus datos, por favor", Toast.LENGTH_SHORT).show();
                }else{
                    createUser();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(vRegistro.this, vLogin.class));
            }
        });
    }

    public void createUser(){
        String nombre = name.getText().toString();
        String usern = username.getText().toString();
        String email1 = email.getText().toString();
        String pass = psw.getText().toString();

        if (TextUtils.isEmpty(nombre)){
            name.setError("Ups!, no has ingresado tu nombre");
            name.requestFocus();
        }else if (TextUtils.isEmpty(usern)){
            username.setError("Ups!, no has ingresado tu username");
            username.requestFocus();
        }else if (TextUtils.isEmpty(email1)){
            email.setError("Ups!, no has ingresado un email");
            email.requestFocus();
        }else if (TextUtils.isEmpty(pass)){
            psw.setError("Ups!, no has ingresado una contraseña");
            psw.requestFocus();
        }else{
            auth.createUserWithEmailAndPassword(email1, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task){
                    if (task.isSuccessful()){
                        userID = auth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userID);

                        Map<String, Object> user=new HashMap<>();
                        user.put("nombre", nombre);
                        user.put("username", usern);
                        user.put("email", email1);
                        user.put("contraseña", pass);

                        documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("TAG", "onSuccess: Datos registrados"+ userID);
                            }
                        });
                        Toast.makeText(vRegistro.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(vRegistro.this, vLogin.class));
                    }else{
                        Toast.makeText(vRegistro.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}