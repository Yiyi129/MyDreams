package com.example.mydreams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    ConstraintLayout l1, l2, l3, l4, l5, l6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        l1 = findViewById(R.id.lib1);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, error.class));
            }
        });
        l2 = findViewById(R.id.lib2);
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, error.class));
            }
        });
        l3 = findViewById(R.id.lib3);
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, error.class));
            }
        });
        l4 = findViewById(R.id.lib4);
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, error.class));
            }
        });
        l5 = findViewById(R.id.lib5);
        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, error.class));
            }
        });
        l6 = findViewById(R.id.lib6);
        l6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, error.class));
            }
        });

    }
}