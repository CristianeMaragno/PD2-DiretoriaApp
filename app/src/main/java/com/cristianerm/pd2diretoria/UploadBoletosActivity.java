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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class UploadBoletosActivity extends AppCompatActivity {

    ImageButton voltar;
    Spinner alunos;
    Spinner mes;
    Button uploadBoleto;
    TextView nomeBoleto;
    Button enviar;
    TextView errorMessage;

    private static final String TAG = "Upload Boletos Activity";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private DatabaseReference myRefUserInfo;
    private DatabaseReference myRefAddBoleto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_boletos);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarUploadBoletos);
        alunos = (Spinner) findViewById(R.id.spinner_upload_boleto_aluno);
        mes = (Spinner) findViewById(R.id.spinner_upload_boleto_mes);
        uploadBoleto = (Button) findViewById(R.id.button_upload_boleto_escolher_arquivo);
        nomeBoleto = (TextView) findViewById(R.id.textView_upload_boleto_nome);
        enviar = (Button) findViewById(R.id.button_upload_boleto_enviar);
        errorMessage = (TextView) findViewById(R.id.textView_upload_boletos_error);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("alunos");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(UploadBoletosActivity.this, "User sighed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadBoletosActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(UploadBoletosActivity.this, FinanceiroActivity.class);
                startActivity(i);
            }
        });

        ///// Spinner mes
        ArrayAdapter<CharSequence> adapter_mes = ArrayAdapter.createFromResource(this,
                R.array.meses, android.R.layout.simple_spinner_item);
        adapter_mes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mes.setAdapter(adapter_mes);
        ////////

        final ArrayList<String> alunosEscola = new ArrayList<String>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alunosEscola.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for(DataSnapshot ds2 : ds.getChildren()){
                        UserInformation uInfo = new UserInformation();
                        uInfo.setNome(ds2.getValue(UserInformation.class).getNome());

                        alunosEscola.add(uInfo.getNome());
                    }
                }

                ///// Spinner_aluno
                ArrayAdapter<String> adapter_alunos = new ArrayAdapter<String>(UploadBoletosActivity.this,  android.R.layout.simple_spinner_dropdown_item, alunosEscola);
                adapter_alunos.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                alunos.setAdapter(adapter_alunos);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        uploadBoleto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelecionaBoleto();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verificar se algum arquivo foi selecionado
                //Adicionar informações do boleto no database
                InserirBoletoNoDatabase();
                //Adicionar boleto no Firebase Storage
            }
        });
    }

    private void SelecionaBoleto(){

    }
    private void InserirBoletoNoDatabase(){
        String aluno_selecionado = alunos.getItemAtPosition(alunos.getSelectedItemPosition()).toString();
        final String mes_selecionado = mes.getItemAtPosition(mes.getSelectedItemPosition()).toString();

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        final String ano = String.valueOf(year);

        myRefUserInfo = mFirebaseDatabase.getReference().child("alunos").child(aluno_selecionado);

        myRefUserInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserInformation uInfo = new UserInformation();
                    uInfo.setUser_id(ds.getValue(UserInformation.class).getUser_id());

                    Log.d(TAG, "showData: User_id: " + uInfo.getUser_id());
                    String userId = uInfo.getUser_id();

                    myRefAddBoleto = mFirebaseDatabase.getReference().child(userId).child("boletos").child(ano);

                    String key = myRefAddBoleto.push().getKey();
                    myRefAddBoleto.child(key).child("ano").setValue(ano);
                    myRefAddBoleto.child(key).child("mes").setValue(mes_selecionado);
                }

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
