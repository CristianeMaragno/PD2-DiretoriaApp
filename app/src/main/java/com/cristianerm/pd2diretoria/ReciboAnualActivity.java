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

public class ReciboAnualActivity extends AppCompatActivity {

    ImageButton voltar;
    ListView solicitacoesReciboAnual;
    ProgressBar progressBar;

    private static final String TAG = "Recibo Anual Activity";

    private FirebaseDatabase mFirebaseDatase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibo_anual);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarReciboAnual);
        solicitacoesReciboAnual = (ListView) findViewById(R.id.listReciboAnual);
        progressBar = (ProgressBar) findViewById(R.id.progressBarReciboAnual);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatase.getReference().child("sol_recibo_anual");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(ReciboAnualActivity.this, "User sighed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReciboAnualActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(ReciboAnualActivity.this, FinanceiroActivity.class);
                startActivity(i);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {

            ArrayList<String> array  = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReciboAnualActivity.this, android.R.layout.simple_list_item_1, array);

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ReciboAnualInformation rInfo = new ReciboAnualInformation();
                    rInfo.setData(ds.getValue(ReciboAnualInformation.class).getData());
                    rInfo.setNome(ds.getValue(ReciboAnualInformation.class).getNome());

                    Log.d(TAG, "showData: Data: " + rInfo.getData());
                    Log.d(TAG, "showData: Nome: " + rInfo.getNome());

                    array.add(rInfo.getNome() + " efetuou uma solicitação para o recibo anual na data: " + rInfo.getData());
                }

                Collections.reverse(array);
                progressBar.setVisibility(View.GONE);
                solicitacoesReciboAnual.setAdapter(adapter);
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
