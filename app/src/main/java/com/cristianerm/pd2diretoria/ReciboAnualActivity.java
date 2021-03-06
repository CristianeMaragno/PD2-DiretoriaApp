package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    Toolbar toolbar_recibo_anual;
    ListView list_view_solicitacoes_recibo_anual;

    private static final String TAG = "Recibo Anual Activity";

    private FirebaseDatabase mFirebaseDatase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibo_anual);

        toolbar_recibo_anual = (Toolbar) findViewById(R.id.tool_bar_recibo_anual);
        setSupportActionBar(toolbar_recibo_anual);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_recibo_anual.setTitle("");
        toolbar_recibo_anual.setSubtitle("");

        list_view_solicitacoes_recibo_anual = (ListView) findViewById(R.id.list_view_recibo_anual);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatase.getReference().child("sol_recibo_anual");

        toolbar_recibo_anual.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(ReciboAnualActivity.this, FinanceiroActivity.class);
                startActivity(i);
            }
        });

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
                list_view_solicitacoes_recibo_anual.setAdapter(adapter);
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
