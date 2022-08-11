package com.example.mydreams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class creaAutores extends AppCompatActivity {
    private EditText nameA, lastname;
    private Button agregar, cancelar;

    private FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_autores);

        mfirestore = FirebaseFirestore.getInstance();

        nameA = findViewById(R.id.txtNombreAutor);
        lastname =  findViewById(R.id.txtApellidoAutor);

        agregar = findViewById(R.id.btnAgregarAu);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreAu = nameA.getText().toString().trim();
                String apellidoAu = lastname.getText().toString().trim();

                if (nombreAu.isEmpty() && apellidoAu.isEmpty()){
                    Toast.makeText(creaAutores.this, "Ingresa los datos, por favor", Toast.LENGTH_SHORT).show();
                }else{
                    postAuthor(nombreAu, apellidoAu);
                }
            }
        });

        cancelar = findViewById(R.id.btnCancelAu);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(creaAutores.this, vAdmin.class));
            }
        });
    }

    private void postAuthor(String nombreAu, String apellidoAu){
        Map<String, Object> map = new HashMap<>();
        map.put("name", nombreAu);
        map.put("apellido", apellidoAu);

        mfirestore.collection("autores").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Creado correctamente", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_LONG).show();
            }
        });
    }
}