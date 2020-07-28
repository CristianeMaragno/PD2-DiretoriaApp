package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserRemovalActivity extends AppCompatActivity {

    ImageButton voltar;

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
        mRecyclerView = findViewById(R.id.recycler_view_remover_users);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatase.getReference();

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

        ArrayList<RemoverUserItem> usersList = new ArrayList<>();
        usersList.add(new RemoverUserItem(R.drawable.ic_delete, "Evellyn Recco"));
        usersList.add(new RemoverUserItem(R.drawable.ic_delete, "Isadora Lucas"));
        usersList.add(new RemoverUserItem(R.drawable.ic_delete, "Vicente Alves"));
        usersList.add(new RemoverUserItem(R.drawable.ic_delete, "Anitta Almeida"));
        usersList.add(new RemoverUserItem(R.drawable.ic_delete, "Livia Cechinel"));

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AdapterUserRemoval(usersList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
