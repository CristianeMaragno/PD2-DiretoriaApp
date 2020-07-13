package com.cristianerm.pd2diretoria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarioActivity extends AppCompatActivity {

    ImageButton voltar;
    Spinner mes;
    EditText texto;
    Button enviar;
    ListView listView;
    TextView error_mensagem;

    private static final String TAG = "Calendario Activity";

    private FirebaseDatabase mFirebaseDatase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarCalendario);
        mes = (Spinner) findViewById(R.id.spinner_Calendario_mes);
        texto = (EditText) findViewById(R.id.editTextCalendario);
        enviar = (Button) findViewById(R.id.button_calendario);
        listView = (ListView) findViewById(R.id.listCalendar);
        error_mensagem = (TextView) findViewById(R.id.textErrorCalendario);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatase.getReference().child("calendario_pedagogico");

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

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(CalendarioActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        ///// Spinner adapter
        ArrayAdapter<CharSequence> adapter_mes = ArrayAdapter.createFromResource(this,
                R.array.meses, android.R.layout.simple_spinner_item);
        adapter_mes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mes.setAdapter(adapter_mes);
        ////////

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String evento = texto.getText().toString();

                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                String ano = String.valueOf(year);

                final String mes_selecionado = mes.getItemAtPosition(mes.getSelectedItemPosition()).toString();

                if(!evento.equals("")) {
                    error_mensagem.setText("");
                    Upload_evento(evento, ano, mes_selecionado);
                }else{
                    error_mensagem.setText("O campo de texto está vazio");
                }
            }
        });
    }

    private void Upload_evento(String evento, String ano, String mes_selecionado){
        String ref = mes_selecionado + "_" + ano;

        String key = myRef.push().getKey();
        myRef.child(ref).child(key).child("evento").setValue(evento);

        Toast.makeText(CalendarioActivity.this, "Upload de evento concluído", Toast.LENGTH_SHORT).show();
    }
}
