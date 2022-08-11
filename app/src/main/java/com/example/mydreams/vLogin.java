package com.example.mydreams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class vLogin extends AppCompatActivity {
    private Button access;
    EditText emailL, pswL;
    private TextView registroL, forgotpassL;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlogin);

        auth = FirebaseAuth.getInstance();

        emailL = findViewById(R.id.txtEmaill);
        pswL = findViewById(R.id.txtPswl);

        registroL = findViewById(R.id.lbRegistro);
        forgotpassL = findViewById(R.id.lbForgotPass);

        access = findViewById(R.id.btnLogin);

        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1 = emailL.getText().toString().trim();
                String psw1 = pswL.getText().toString().trim();

                if (email1.isEmpty() && psw1.isEmpty()){
                        Toast.makeText(vLogin.this, "Ingresa tus datos, por favor", Toast.LENGTH_SHORT).show();
                }else{
                        loginUser(email1, psw1);
                }
            }
        });

        registroL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(vLogin.this, vRegistro.class));
            }
        });

        forgotpassL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(vLogin.this, forgotPassword.class));
            }
        });
    }

    private void loginUser(String email1, String psw1){

        auth.signInWithEmailAndPassword(email1, psw1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    ProcessLogin();
                    Toast.makeText(vLogin.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                }else if(emailL.getText().toString().equals("jennigac3@gmail.com") && pswL.getText().toString().equals("1234567")){
                    ProcessLoginAdmin();
                    Toast.makeText(vLogin.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                }else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(vLogin.this, "ERROR EN CONTRASEÑA", Toast.LENGTH_SHORT).show();
                }else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(vLogin.this, "EMAIL NO ENCONTRADO", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    protected void onStart(){
        super.onStart();
        FirebaseUser usuario = auth.getCurrentUser();
        if (usuario != null){
            startActivity(new Intent(vLogin.this, home.class));
            finish();
        }
    }

    private void ProcessLoginAdmin() {
        SafetyNet.getClient(vLogin.this).verifyWithRecaptcha("6LcuTQkhAAAAALE2sPEnNs-urBAs7a6MHrYbvhdo")
                .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        String captchaToken = recaptchaTokenResponse.getTokenResult();

                        if(captchaToken != null){
                            if(!captchaToken.isEmpty()){
                                processLoginStep(captchaToken, emailL.getText().toString(), pswL.getText().toString());
                                //seguimiento a otra interfaz
                                startActivity(new Intent(vLogin.this, vAdmin.class));
                            }else{
                                Toast.makeText(vLogin.this, "Captcha Inválido", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(vLogin.this, "Falla al cargar el Captcha", Toast.LENGTH_SHORT);
                    }
                });
    }

    private void ProcessLogin() {
        SafetyNet.getClient(vLogin.this).verifyWithRecaptcha("6LcuTQkhAAAAALE2sPEnNs-urBAs7a6MHrYbvhdo")
                .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        String captchaToken = recaptchaTokenResponse.getTokenResult();

                        if(captchaToken != null){
                            if(!captchaToken.isEmpty()){
                                processLoginStep(captchaToken, emailL.getText().toString(), pswL.getText().toString());
                                //seguimiento a otra interfaz
                                startActivity(new Intent(vLogin.this, home.class));
                            }else{
                                Toast.makeText(vLogin.this, "Captcha Inválido", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(vLogin.this, "Falla al cargar el Captcha", Toast.LENGTH_SHORT);
                    }
                });
    }

    private void processLoginStep(String token, String email, String psw) {
        Log.d("CAPTCHA TOKEN", ""+token);
    }
}