package com.example.mydreams;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.model.FieldIndex;

public class vAdmin extends AppCompatActivity {
    private Button cierre;
    private ConstraintLayout cuento, autor, usuario, admins;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vadmin);

        auth = FirebaseAuth.getInstance();

        cierre = findViewById(R.id.btnCerrarSesAdmin);
        cierre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(vAdmin.this, error.class));
            }
        });

        cuento = findViewById(R.id.aCuento);
        cuento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(vAdmin.this, creaCuentos.class));
            }
        });

        autor = findViewById(R.id.aAutores);
        autor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(vAdmin.this, creaAutores.class));
            }
        });

        usuario = findViewById(R.id.aUsuarios);
        usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(vAdmin.this, administraUs.class));
            }
        });

        admins = findViewById(R.id.aAdmins);
        admins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(vAdmin.this, creaAdmins.class));
            }
        });
    }
}