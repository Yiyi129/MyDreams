package com.example.mydreams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class actualizaUs extends AppCompatActivity {
    Button btn_actual;
    EditText nombre, username;
    private FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualiza_us);

        String id = getIntent().getStringExtra("id_user");
        mfirestore = FirebaseFirestore.getInstance();

        nombre = findViewById(R.id.txtNombreAc);
        username = findViewById(R.id.txtUsuarioAc);

        btn_actual = findViewById(R.id.btn_actualizar);

        getUser(id);
        btn_actual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nombre.getText().toString().trim();
                String usnm = username.getText().toString().trim();

                if (name.isEmpty() && usnm.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_LONG).show();
                }else{
                    updateUser(name,usnm,id);
                }
            }
        });
    }

    private void getUser(String id){
        mfirestore.collection("users").document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nameAc = documentSnapshot.getString("nombre");
                String usernameAc = documentSnapshot.getString("username");

                nombre.setText(nameAc);
                username.setText(usernameAc);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener datos", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUser(String name, String usnm, String id){
        Map <String, Object> map = new HashMap<>();
        map.put("nombre", name);
        map.put("username", usnm);

        mfirestore.collection("users").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Actualizado correctamente", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_LONG).show();
            }
        });
    }
}