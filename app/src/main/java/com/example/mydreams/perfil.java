package com.example.mydreams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.atomic.AtomicReference;

public class perfil extends AppCompatActivity {
    private Button cerrarU;
    TextView perfil, email;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth auth;
    FirebaseUser userd;
    FirebaseFirestore fStore;
    private String idUser;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        idUser = auth.getCurrentUser().getUid();
        FirebaseUser users = auth.getCurrentUser();

        perfil = findViewById(R.id.viewUsername);
        email = findViewById(R.id.viewCorreo);

        //referenciar para obtener datos de las coleccines
        DocumentReference documentReference = fStore.collection("users").document(idUser);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot == null || auth.getCurrentUser() == null) return;
                perfil.setText(documentSnapshot.getString("username"));
            }
        });

        email.setText(users.getEmail());

        final EditText correo  = findViewById(R.id.txtEmaill);
        final EditText contra = findViewById(R.id.txtPswl);

        cerrarU = findViewById(R.id.btnCerrarUs);
        cerrarU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(perfil.this, vLogin.class));
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), perfil.class));
                        overridePendingTransition(0, 0);
                        return;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), home.class));
                        overridePendingTransition(0, 0);
                        return;
                }
            }
        });
    }


    /*private void deleteUser(String correo, String contra) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(correo, contra);

        if(user != null){
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("TAG", "Cuenta eliminada.");
                                        startActivity(new Intent(perfil.this, vLogin.class));
                                        Toast.makeText(perfil.this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
        }
    }*/

    private void deleteDoc(String idUser){
        fStore.collection("users").document(idUser).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Cuenta eliminada corectamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Cuenta no eliminada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}