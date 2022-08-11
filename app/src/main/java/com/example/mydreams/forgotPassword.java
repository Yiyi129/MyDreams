package com.example.mydreams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {
    private EditText txtForgotPass;
    private Button cancelar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();
        txtForgotPass = findViewById(R.id.txtEmailFP);
        cancelar = findViewById(R.id.btnCanFP);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forgotPassword.this, vLogin.class));
            }
        });
    }

    public void recuperar(View view){
        auth.setLanguageCode("es");

        String email = txtForgotPass.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            auth.setLanguageCode("es");
            Toast.makeText(getApplicationContext(), "Ingresa tu correo", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    auth.setLanguageCode("es");
                    startActivity(new Intent(forgotPassword.this, vLogin.class));
                    finish();
                    Toast.makeText(getApplicationContext(), "Se ha enviado un enlace a tu correo", Toast.LENGTH_SHORT).show();
                }else{
                        Toast.makeText(getApplicationContext(), "Ups! correo no registrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}