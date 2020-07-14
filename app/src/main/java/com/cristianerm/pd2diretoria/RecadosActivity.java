package com.cristianerm.pd2diretoria;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class RecadosActivity extends AppCompatActivity {

    ImageButton voltar;
    EditText recados;
    Button button_enviar;
    ListView listVistos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recados);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarRecados);
        recados = (EditText) findViewById(R.id.editTextRecados);
        button_enviar = (Button) findViewById(R.id.button_recados);
        listVistos = (ListView) findViewById(R.id.listRecados);
    }
}
