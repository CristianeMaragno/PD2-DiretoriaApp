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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FinanceiroActivity extends AppCompatActivity {

    Toolbar toolbar_financeiro;
    Button button_upload_boletos, button_excluir_boletos, button_solicitacoes_recibo_anual;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financeiro);

        toolbar_financeiro = (Toolbar) findViewById(R.id.tool_bar_financeiro);
        setSupportActionBar(toolbar_financeiro);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_financeiro.setTitle("");
        toolbar_financeiro.setSubtitle("");

        button_upload_boletos = (Button) findViewById(R.id.button_upload_boleto);
        button_excluir_boletos = (Button) findViewById(R.id.button_excluir_boleto);
        button_solicitacoes_recibo_anual = (Button) findViewById(R.id.button_solicitar_recibo_anual);

        mAuth = FirebaseAuth.getInstance();

        toolbar_financeiro.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(FinanceiroActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(FinanceiroActivity.this, "User sighed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FinanceiroActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        button_upload_boletos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(FinanceiroActivity.this, UploadBoletosActivity.class);
                startActivity(i);
            }
        });

        button_excluir_boletos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(FinanceiroActivity.this, ExcluirBoletosActivity.class);
                startActivity(i);
            }
        });

        button_solicitacoes_recibo_anual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(FinanceiroActivity.this, ReciboAnualActivity.class);
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