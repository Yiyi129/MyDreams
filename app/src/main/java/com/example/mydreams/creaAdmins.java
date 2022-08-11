package com.example.mydreams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

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
import java.util.concurrent.Executor;

public class creaAdmins extends AppCompatActivity {
    private EditText nombre, email, psw;
    private Button agregar, cancelar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_admins);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nombre = findViewById(R.id.txtNombreAdmin);
        email = findViewById(R.id.txtEmailAdmin);
        psw = findViewById(R.id.txtPswAdmin);

        agregar = findViewById(R.id.btnRegistrarAdmin);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activarHuella();
            }
        });

        cancelar = findViewById(R.id.btnCancelarAdmin);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(creaAdmins.this, vAdmin.class));
            }
        });
    }

    public void activarHuella(){
        androidx.biometric.BiometricPrompt.PromptInfo promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Por favor verifica tu huella dactilar")
                .setDescription("Usar tu autenticacion biometrica es necesaria")
                .setNegativeButtonText("Cancelar")
                .build();
        getPrompt().authenticate(promptInfo);
    }

    private androidx.biometric.BiometricPrompt getPrompt(){
        Executor executor = ContextCompat.getMainExecutor(this);
        androidx.biometric.BiometricPrompt.AuthenticationCallback callback = new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                notifyUser(errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                notifyUser("Autenticacion exitosa!");
                Intent i = new Intent(getApplicationContext(), creaCuentos.class);
                startActivity(i);
                createAdmin();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                notifyUser("Autenticacion fallida!");
            }
        };
        androidx.biometric.BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }

    private void notifyUser(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void createAdmin(){
        String nom = nombre.getText().toString();
        String correo = email.getText().toString();
        String pass = psw.getText().toString();

        if(TextUtils.isEmpty(nom)){
            nombre.setError("Ups!, no has ingresado el nombre");
            nombre.requestFocus();
        }else if(TextUtils.isEmpty(correo)){
            email.setError("Ups!, no has ingresado un email");
            email.requestFocus();
        }else if(TextUtils.isEmpty(pass)){
            psw.setError("Ups!, no has ingresado una contraseña");
            psw.requestFocus();
        }else{
            auth.createUserWithEmailAndPassword(correo, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        userID = auth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("admins").document(userID);

                        Map<String, Object> admin = new HashMap<>();
                        admin.put("Nombre", nom);
                        admin.put("Email", correo);
                        admin.put("Contraseña", pass);

                        documentReference.set(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("TAG", "onSuccess: Datos registrados" + userID);
                            }
                        });
                        Toast.makeText(creaAdmins.this, "Administrador registrado", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(creaAdmins.this, "Administrador no registrado", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}