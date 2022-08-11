package com.example.mydreams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mydreams.adapter.usuarioAdapter;
import com.example.mydreams.model.usuario;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class administraUs extends AppCompatActivity {
    private Button regresar;

    RecyclerView mRecycler;
    usuarioAdapter mAdapter;
    FirebaseFirestore mFirestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administra_us);

        regresar = findViewById(R.id.btnSalir);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(administraUs.this, vAdmin.class));
            }
        });

        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.reciclador);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = mFirestore.collection("users");

        FirestoreRecyclerOptions<usuario> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<usuario>().setQuery(query, usuario.class).build();

        mAdapter = new usuarioAdapter(firestoreRecyclerOptions, this);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}