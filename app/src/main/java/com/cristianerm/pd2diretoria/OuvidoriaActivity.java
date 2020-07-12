package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class OuvidoriaActivity extends AppCompatActivity {

    ImageButton voltar;
    ListView listOuvidoria;
    ProgressBar progressBar;

    private static final String TAG = "Ouvidoria Activity";

    private FirebaseDatabase mFirebaseDatase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouvidoria);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarOuvidoria);
        listOuvidoria = (ListView) findViewById(R.id.listOuvidoria);
        progressBar = (ProgressBar) findViewById(R.id.progressBarOuvidoria);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatase.getReference().child("ouvidoria");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(OuvidoriaActivity.this, "User sighed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OuvidoriaActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(OuvidoriaActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        myRef.limitToLast(15).addValueEventListener(new ValueEventListener() {

            ArrayList<String> array  = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(OuvidoriaActivity.this, android.R.layout.simple_list_item_1, array);

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    OuvidoriaInformation oInfo = new OuvidoriaInformation();
                    oInfo.setData(ds.getValue(OuvidoriaInformation.class).getData());
                    oInfo.setMensagem(ds.getValue(OuvidoriaInformation.class).getMensagem());
                    oInfo.setProfessor(ds.getValue(OuvidoriaInformation.class).getProfessor());

                    Log.d(TAG, "showData: Data: " + oInfo.getData());
                    Log.d(TAG, "showData: Data: " + oInfo.getMensagem());
                    Log.d(TAG, "showData: Data: " + oInfo.getProfessor());


                    array.add(oInfo.getData() + "\n" + oInfo.getMensagem() + "\nAutor(a): " + oInfo.getProfessor());

                }

                Collections.reverse(array);
                progressBar.setVisibility(View.GONE);
                listOuvidoria.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
