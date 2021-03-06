package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsersActivity extends AppCompatActivity {

    Toolbar toolbar_users;
    Button button_cadastrar_user, button_remover_user;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        toolbar_users = (Toolbar) findViewById(R.id.tool_bar_users);
        setSupportActionBar(toolbar_users);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_users.setTitle("");
        toolbar_users.setSubtitle("");

        button_cadastrar_user = (Button) findViewById(R.id.button_cadastrar_usuario);
        button_remover_user = (Button) findViewById(R.id.button_remover_usuario);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        toolbar_users.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(UsersActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(UsersActivity.this, userID, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UsersActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        button_cadastrar_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(UsersActivity.this, UserCadastroActivity.class);
                startActivity(i);
            }
        });

        button_remover_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(UsersActivity.this, UserRemovalActivity.class);
                startActivity(i);
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
