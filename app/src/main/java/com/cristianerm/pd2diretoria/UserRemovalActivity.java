package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Collections;

public class UserRemovalActivity extends AppCompatActivity {

    ImageButton voltar;
    Spinner status_users;
    ArrayList<RemoverUserItem> usersList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String TAG = "User Removal Activity";

    private FirebaseDatabase mFirebaseDatase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_removal);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarUserRemoval);
        status_users = (Spinner) findViewById(R.id.spinner_status_removal);
        mRecyclerView = findViewById(R.id.recycler_view_remover_users);
        usersList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatase = FirebaseDatabase.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(UserRemovalActivity.this, "User sighed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserRemovalActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(UserRemovalActivity.this, UsersActivity.class);
                startActivity(i);
            }
        });

        ///// Spinner status dos users
        ArrayAdapter<CharSequence> adapter_status = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_users.setAdapter(adapter_status);
        ////

        status_users.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String status_selecionado = status_users.getItemAtPosition(status_users.getSelectedItemPosition()).toString();
                recupera_users(status_selecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public void recupera_users(String status_selecionado){
        usersList.clear();

        if(status_selecionado.equals("Aluno(a)")){
            myRef = mFirebaseDatase.getReference().child("alunos");
        }else if(status_selecionado.equals("Professor(a)")){
            myRef = mFirebaseDatase.getReference().child("professores");
        }else if(status_selecionado.equals("Membro da direção")){
            myRef = mFirebaseDatase.getReference().child("diretoria");
        }

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usersList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for(DataSnapshot ds2 : ds.getChildren()){
                        UserRemovalInformation uInfo = new UserRemovalInformation();
                        uInfo.setNome(ds2.getValue(UserRemovalInformation.class).getNome());

                        usersList.add(new RemoverUserItem(R.drawable.ic_delete, uInfo.getNome()));
                    }
                }

                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(UserRemovalActivity.this);
                mAdapter = new AdapterUserRemoval(usersList);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
