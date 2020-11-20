package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class CalendarioActivity extends AppCompatActivity {

    Toolbar toolbar_calendario;
    Spinner spinner_mes;
    Spinner spinner_mes2;
    EditText edit_text_evento;
    Button button_enviar;
    ListView list_view;

    TextView error_mensagem;

    private static final String TAG = "Calendario Activity";

    private FirebaseDatabase mFirebaseDatase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        toolbar_calendario = (Toolbar) findViewById(R.id.tool_bar_calendario);
        setSupportActionBar(toolbar_calendario);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_calendario.setTitle("");
        toolbar_calendario.setSubtitle("");

        spinner_mes = (Spinner) findViewById(R.id.spinner_calendario_mes);
        spinner_mes2 = (Spinner) findViewById(R.id.spinner_calendario_mes2);
        edit_text_evento = (EditText) findViewById(R.id.edit_text_calendario);
        button_enviar = (Button) findViewById(R.id.button_calendario);
        list_view = (ListView) findViewById(R.id.list_view_calendario);
        error_mensagem = (TextView) findViewById(R.id.text_view_error_calendario);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatase.getReference().child("calendario_pedagogico");

        toolbar_calendario.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(CalendarioActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(CalendarioActivity.this, "User sighed in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CalendarioActivity.this, "User not sighed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        ///// Spinner mes do evento adapter
        ArrayAdapter<CharSequence> adapter_mes = ArrayAdapter.createFromResource(this,
                R.array.meses, android.R.layout.simple_spinner_item);
        adapter_mes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_mes.setAdapter(adapter_mes);
        ////////

        ///// Spinner mes de vizulização adapter
        ArrayAdapter<CharSequence> adapter_mes_2 = ArrayAdapter.createFromResource(this,
                R.array.meses, android.R.layout.simple_spinner_item);
        adapter_mes_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_mes2.setAdapter(adapter_mes_2);
        ////////

        button_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String evento = edit_text_evento.getText().toString();

                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                String ano = String.valueOf(year);

                final String mes_selecionado = spinner_mes.getItemAtPosition(spinner_mes.getSelectedItemPosition()).toString();

                if(!evento.equals("")) {
                    error_mensagem.setText("");
                    Upload_evento(evento, ano, mes_selecionado);
                }else{
                    error_mensagem.setText("O campo de texto está vazio");
                }
            }
        });

        spinner_mes2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Recuperar_eventos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void Upload_evento(String evento, String ano, String mes_selecionado){
        String ref = mes_selecionado + "_" + ano;

        String key = myRef.push().getKey();
        myRef.child(ref).child(key).child("evento").setValue(evento);

        edit_text_evento.getText().clear();
        Toast.makeText(CalendarioActivity.this, "Upload de evento concluído", Toast.LENGTH_SHORT).show();
    }

    private void Recuperar_eventos(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String ano = String.valueOf(year);

        String mes_selecionado = spinner_mes2.getItemAtPosition(spinner_mes2.getSelectedItemPosition()).toString();

        String ref = mes_selecionado + "_" + ano;

        myRef2 = mFirebaseDatase.getReference().child("calendario_pedagogico").child(ref);

        myRef2.addValueEventListener(new ValueEventListener() {

            ArrayList<String> array  = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CalendarioActivity.this, android.R.layout.simple_list_item_1, array);

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                array.clear();
                adapter.notifyDataSetChanged();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    CalendarioInformation cInfo = new CalendarioInformation();
                    cInfo.setEvento(ds.getValue(CalendarioInformation.class).getEvento());

                    Log.d(TAG, "showData: Evento: " + cInfo.getEvento());

                    array.add(cInfo.getEvento());
                }

                Collections.reverse(array);
                list_view.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
