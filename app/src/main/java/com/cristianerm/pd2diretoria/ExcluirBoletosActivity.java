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
import java.util.Calendar;

public class ExcluirBoletosActivity extends AppCompatActivity {

    Toolbar toolbar_excluir_boletos;
    Spinner spinner_alunos;
    ArrayList<ExcluirBoletosItem> array_list_boletos;
    ArrayList<String> array_list_alunos_escola;

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

        toolbar_excluir_boletos = (Toolbar) findViewById(R.id.tool_bar_excluir_boletos);
        setSupportActionBar(toolbar_excluir_boletos);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_excluir_boletos.setTitle("");
        toolbar_excluir_boletos.setSubtitle("");

        spinner_alunos = (Spinner) findViewById(R.id.spinner_aluno_excluir_boletos);
        mRecyclerView = findViewById(R.id.recycler_view_excluir_boletos);
        array_list_boletos = new ArrayList<>();
        array_list_alunos_escola = new ArrayList<String>();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("alunos");

        toolbar_excluir_boletos.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(ExcluirBoletosActivity.this, FinanceiroActivity.class);
                startActivity(i);
            }
        });

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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array_list_alunos_escola.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for(DataSnapshot ds2 : ds.getChildren()){
                        UserInformation uInfo = new UserInformation();
                        uInfo.setNome(ds2.getValue(UserInformation.class).getNome());

                        array_list_alunos_escola.add(uInfo.getNome());
                    }
                }

                ///// Spinner_aluno
                ArrayAdapter<String> adapter_alunos = new ArrayAdapter<String>(ExcluirBoletosActivity.this,  android.R.layout.simple_spinner_dropdown_item, array_list_alunos_escola);
                adapter_alunos.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                spinner_alunos.setAdapter(adapter_alunos);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        spinner_alunos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String aluno_selecionado = spinner_alunos.getItemAtPosition(spinner_alunos.getSelectedItemPosition()).toString();
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

                array_list_boletos.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ExcluirBoletosInformation bInfo = new ExcluirBoletosInformation();
                    bInfo.setMes(ds.getValue(ExcluirBoletosInformation.class).getMes());
                    bInfo.setAno(ds.getValue(ExcluirBoletosInformation.class).getAno());

                    String boleto_name = bInfo.getMes() + " " + bInfo.getAno();

                    array_list_boletos.add(new ExcluirBoletosItem(R.drawable.ic_delete, boleto_name, userId, aluno_selecionado));
                }

                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(ExcluirBoletosActivity.this);
                mAdapter = new AdapterExcluirBoletos(array_list_boletos);
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
