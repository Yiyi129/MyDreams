package com.example.mydreams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class creaCuentos extends AppCompatActivity {
    Button btnEN, btnCuento;
    private static final String CHANNEL_ID = "canal";
    private PendingIntent pendingIntent;

    ImageView foto_cuento;
    LinearLayout LL_imgbtn;
    EditText nombreC, categoria, nombreAuC;

    private FirebaseAuth auth;
    private FirebaseFirestore mfirestore;

    StorageReference storageReference;
    String storage_path = "cuentos/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String foto = "foto";
    String idd;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_cuentos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        String id = getIntent().getStringExtra("id_cuento");
        mfirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        foto_cuento = findViewById(R.id.foto_cuento);
        LL_imgbtn = findViewById(R.id.images_btn);
        nombreC = findViewById(R.id.txtNombreCuento);
        categoria = findViewById(R.id.txtCategoria);
        nombreAuC =  findViewById(R.id.txtNombreAutorC);

        btnCuento = findViewById(R.id.agregarCuento);
        btnEN = findViewById(R.id.btnSN);

        btnCuento = findViewById(R.id.agregarCuento);
        btnCuento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activarHuella();
            }
        });

        btnEN.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    showNewNotification();
                } else {
                    //Verdadero, sigifica que el dispositivo que ejecuta la aplicación  tiene Android SDK
                    //superior y se ejecutará el bloque de código dentro de nuestro dispositivo "if"
                    showNotification();
                }
            }
        });
    }

    public void activarHuella(){
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Por favor verifica tu huella dactilar")
                .setDescription("Usar tu autenticacion biometrica es necesaria")
                .setNegativeButtonText("Cancelar")
                .build();
        getPrompt().authenticate(promptInfo);
    }

    private BiometricPrompt getPrompt(){
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                notifyUser(errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                notifyUser("Autenticacion exitosa!");

                Intent i = new Intent(getApplicationContext(), creaCuentos.class);
                startActivity(i);

                String nomC, cate, nomAuc;
                nomC = nombreC.getText().toString().trim();
                cate = categoria.getText().toString().trim();
                nomAuc = nombreAuC.getText().toString().trim();

                if (nomC.isEmpty() && cate.isEmpty() && nomAuc.isEmpty()){
                    Toast.makeText(creaCuentos.this, "Ingresa los datos, por favor", Toast.LENGTH_SHORT).show();
                }else{
                    getCuento(idd);
                    postCuento(nomC, cate, nomAuc);
                }

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                notifyUser("Autenticacion fallida!");
            }
        };

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }

    private void notifyUser(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void uploadFoto(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("cuentos/*");

        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                subirPhoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri image_url) {
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String rute_storage_photo = storage_path + "" + foto + "" + auth.getUid() +""+ idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", download_uri);
                            mfirestore.collection("cuentos").document(idd).update(map);
                            Toast.makeText(creaCuentos.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(creaCuentos.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(){
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "NEW", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        showNewNotification();
    }

    private void showNewNotification(){
        setPendingIntent(home.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Hey!, llegaron nuevos cuentos")
                .setContentText("Da clic ahora para conocer los nuevos títulos agregados")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(1, builder.build());
    }

    private void setPendingIntent(Class<home> clsActivity) {
        Intent intent = new Intent(this, clsActivity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(clsActivity);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void getCuento(String id){
        mfirestore.collection("cuentos").document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nameCuento = documentSnapshot.getString("nombreC");
                String cateCuento = documentSnapshot.getString("categoria");
                String nameAutor = documentSnapshot.getString("nombreAuC");
                String foto_cuento1 = documentSnapshot.getString("foto");
                nombreC.setText(nameCuento);
                categoria.setText(cateCuento);
                nombreAuC.setText(nameAutor);
                try{
                    if(!foto_cuento1.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(),"Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();
                        Picasso.with(creaCuentos.this)
                                .load(String.valueOf(foto_cuento1))
                                .resize(150, 150)
                                .into(foto_cuento);
                    }
                }catch (Exception e){
                    Log.v("Error", "e: "+ e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postCuento(String nombreC, String categoria, String nombreAuC){
        Map<String, Object> map = new HashMap<>();
        map.put("name", nombreC);
        map.put("apellido", categoria);
        map.put("nombreAutor", nombreAuC);

        mfirestore.collection("cuentos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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