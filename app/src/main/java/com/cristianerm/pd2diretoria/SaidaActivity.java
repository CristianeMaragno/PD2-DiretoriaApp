package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class SaidaActivity extends AppCompatActivity {

    ImageButton voltar;
    ListView listSaida;
    ProgressBar progressBar;

    private static final String TAG = "Saida Activity";

    private FirebaseDatabase mFirebaseDatase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saida);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarSaida);
        listSaida = (ListView) findViewById(R.id.listSaida);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSaida);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatase.getReference().child("solicitar_saida");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(SaidaActivity.this, "User sighed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SaidaActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(SaidaActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {

            ArrayList<String> array  = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SaidaActivity.this, android.R.layout.simple_list_item_1, array);

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                adapter.notifyDataSetChanged();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SaidaInformation sInfo = new SaidaInformation();
                    sInfo.setSolicitacao(ds.getValue(SaidaInformation.class).getSolicitacao());

                    Log.d(TAG, "showData: Solicitação: " + sInfo.getSolicitacao());

                    array.add(sInfo.getSolicitacao());
                }

                Collections.reverse(array);
                progressBar.setVisibility(View.GONE);
                listSaida.setAdapter(adapter);
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