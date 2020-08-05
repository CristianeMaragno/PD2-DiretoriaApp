package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Calendar;

public class ExcluirBoletosActivity extends AppCompatActivity {

    ImageButton voltar;
    Spinner alunos;
    ArrayList<ExcluirBoletosItem> boletosList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String TAG = "Excluir Boletos Activity";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private DatabaseReference myRefGetUserID;
    private DatabaseReference myRefBoletosNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excluir_boletos);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarExcluirBoletos);
        alunos = (Spinner) findViewById(R.id.spinnerAlunoExcluirBoletos);
        mRecyclerView = findViewById(R.id.recycler_view_excluir_boletos);
        boletosList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("alunos");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(ExcluirBoletosActivity.this, "User sighed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ExcluirBoletosActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(ExcluirBoletosActivity.this, FinanceiroActivity.class);
                startActivity(i);
            }
        });

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
                ArrayAdapter<String> adapter_alunos = new ArrayAdapter<String>(ExcluirBoletosActivity.this,  android.R.layout.simple_spinner_dropdown_item, alunosEscola);
                adapter_alunos.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                alunos.setAdapter(adapter_alunos);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        alunos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String aluno_selecionado = alunos.getItemAtPosition(alunos.getSelectedItemPosition()).toString();
                GetUserID(aluno_selecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void GetUserID(final String aluno_selecionado){

        myRefGetUserID = mFirebaseDatabase.getReference().child("alunos").child(aluno_selecionado);

        myRefGetUserID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    UserInformation idInfo = new UserInformation();
                    idInfo.setUser_id(ds.getValue(UserInformation.class).getUser_id());

                    String userId = idInfo.getUser_id();
                    GetBoletosNames(userId, aluno_selecionado);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void GetBoletosNames(final String userId, final String aluno_selecionado){
        int ano_int = Calendar.getInstance().get(Calendar.YEAR);
        String ano = String.valueOf(ano_int);
        myRefBoletosNames = mFirebaseDatabase.getReference().child(userId).child("boletos").child(ano);

        myRefBoletosNames.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boletosList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ExcluirBoletosInformation bInfo = new ExcluirBoletosInformation();
                    bInfo.setMes(ds.getValue(ExcluirBoletosInformation.class).getMes());
                    bInfo.setAno(ds.getValue(ExcluirBoletosInformation.class).getAno());

                    String boleto_name = bInfo.getMes() + " " + bInfo.getAno();

                    boletosList.add(new ExcluirBoletosItem(R.drawable.ic_delete, boleto_name, userId, aluno_selecionado));
                }

                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(ExcluirBoletosActivity.this);
                mAdapter = new AdapterExcluirBoletos(boletosList);
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
