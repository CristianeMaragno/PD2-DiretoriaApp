package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import java.util.Date;

public class RecadosActivity extends AppCompatActivity {

    ImageButton voltar;
    EditText recados;
    Button button_enviar;
    ListView listVistos;
    TextView error_mensagem;

    private static final String TAG = "Recados Activity";

    private FirebaseDatabase mFirebaseDatase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private DatabaseReference myRefRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recados);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarRecados);
        recados = (EditText) findViewById(R.id.editTextRecados);
        button_enviar = (Button) findViewById(R.id.button_recados);
        listVistos = (ListView) findViewById(R.id.listRecados);
        error_mensagem = (TextView) findViewById(R.id.textErrorRecados);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatase.getReference().child("recados");
        myRefRec = mFirebaseDatase.getReference().child("recados_lidos");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(RecadosActivity.this, "User sighed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecadosActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(RecadosActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        button_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recado = recados.getText().toString();

                Date currentTime = Calendar.getInstance().getTime();
                String data_atual = currentTime.toString();
                String hora_e_data = "Hora: "+data_atual.substring(11,19) + " Data: " + data_atual.substring(4,10);

                if(!recado.equals("")) {
                    error_mensagem.setText("");
                    Upload_recado(recado, hora_e_data);
                }else{
                    error_mensagem.setText("O campo de texto está vazio");
                }
            }
        });

        myRefRec.addValueEventListener(new ValueEventListener() {

            ArrayList<String> array  = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecadosActivity.this, android.R.layout.simple_list_item_1, array);

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                adapter.notifyDataSetChanged();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    RecadosInformation rInfo = new RecadosInformation();
                    rInfo.setMensagem_lida(ds.getValue(RecadosInformation.class).getMensagem_lida());

                    Log.d(TAG, "showData: Recado lido: " + rInfo.getMensagem_lida());

                    array.add(rInfo.getMensagem_lida());
                }

                Collections.reverse(array);
                listVistos.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void Upload_recado(String recado, String data){
        String key = myRef.push().getKey();
        myRef.child(key).child("mensagem").setValue(recado);
        myRef.child(key).child("data").setValue(data);

        recados.getText().clear();

        Toast.makeText(RecadosActivity.this, "Upload do recado concluído", Toast.LENGTH_SHORT).show();
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
