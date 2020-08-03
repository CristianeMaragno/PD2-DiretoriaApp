package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FinanceiroActivity extends AppCompatActivity {

    ImageButton voltar;
    Button uploadBoletos, excluirBoletos, solicitacoesReciboAnual;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financeiro);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarFinanceiro);
        uploadBoletos = (Button) findViewById(R.id.button_upload_boleto);
        excluirBoletos = (Button) findViewById(R.id.button_excluir_boleto);
        solicitacoesReciboAnual = (Button) findViewById(R.id.button_solicitar_recibo_anual);

        mAuth = FirebaseAuth.getInstance();

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

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(FinanceiroActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        uploadBoletos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(FinanceiroActivity.this, UploadBoletosActivity.class);
                startActivity(i);
            }
        });

        excluirBoletos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(FinanceiroActivity.this, ExcluirBoletosActivity.class);
                startActivity(i);
            }
        });

        solicitacoesReciboAnual.setOnClickListener(new View.OnClickListener() {
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
