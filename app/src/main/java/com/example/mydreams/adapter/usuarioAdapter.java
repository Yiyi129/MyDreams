package com.example.mydreams.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydreams.R;
import com.example.mydreams.actualizaUs;
import com.example.mydreams.model.usuario;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class usuarioAdapter extends FirestoreRecyclerAdapter<usuario, usuarioAdapter.ViewHolder> {

    private FirebaseFirestore mfirestore = FirebaseFirestore.getInstance();
    Activity activity;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public usuarioAdapter(@NonNull FirestoreRecyclerOptions<usuario> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull usuario usuario) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.name.setText(usuario.getNombre());
        viewHolder.username.setText(usuario.getUsername());
        viewHolder.email.setText(usuario.getEmail());
        viewHolder.pass.setText(usuario.getContraseña());

        viewHolder.btn_editarU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, actualizaUs.class);
                i.putExtra("id_user", id);
                activity.startActivity(i);
            }
        });

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(id);
            }
        });
    }

    private void deleteUser(String id){
        mfirestore.collection("users").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_usuario_single, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, username, email, pass;
        ImageView btn_delete, btn_editarU;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nom);
            username = itemView.findViewById(R.id.us);
            email = itemView.findViewById(R.id.email);
            pass = itemView.findViewById(R.id.contra);
            btn_delete = itemView.findViewById(R.id.btn_eliminarU);
            btn_editarU = itemView.findViewById(R.id.btn_editarU);
        }
    }
}
