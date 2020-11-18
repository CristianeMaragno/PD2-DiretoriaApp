package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiarioActivity extends AppCompatActivity {

    Toolbar toolbar_diario;
    Spinner turma;
    ArrayList<DiarioItem> diarioList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String TAG = "Diario Activity";

    private FirebaseDatabase mFirebaseDatase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario);

        toolbar_diario = (Toolbar) findViewById(R.id.tool_bar_diario);
        setSupportActionBar(toolbar_diario);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_diario.setTitle("");
        toolbar_diario.setSubtitle("");

        turma = (Spinner) findViewById(R.id.spinner_turma_diario);
        mRecyclerView = findViewById(R.id.recycler_view_diario);
        diarioList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatase = FirebaseDatabase.getInstance();

        toolbar_diario.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(DiarioActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(DiarioActivity.this, "User sighed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DiarioActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        ///// Spinner turma
        ArrayAdapter<CharSequence> adapter_turma = ArrayAdapter.createFromResource(this,
                R.array.turmas_alunos, android.R.layout.simple_spinner_item);
        adapter_turma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        turma.setAdapter(adapter_turma);
        ////

        turma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String turma_selecionada = turma.getItemAtPosition(turma.getSelectedItemPosition()).toString();
                GetDiarioPosts(turma_selecionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void GetDiarioPosts(final String turma_selecionada){
        diarioList.clear();

        myRef = mFirebaseDatase.getReference().child("diario_professor").child(turma_selecionada);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                diarioList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DiarioInformation dInfo = new DiarioInformation();
                    dInfo.setDate(ds.getValue(DiarioInformation.class).getDate());
                    dInfo.setMensagem(ds.getValue(DiarioInformation.class).getMensagem());
                    dInfo.setImageUrl(ds.getValue(DiarioInformation.class).getImageUrl());

                    String post_text = dInfo.getDate() + "\n" + dInfo.getMensagem();
                    String mImageUrl = dInfo.getImageUrl();

                    diarioList.add(new DiarioItem(R.drawable.ic_delete, post_text, mImageUrl, turma_selecionada));
                }

                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(DiarioActivity.this);
                mAdapter = new AdapterDiario(diarioList);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

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
