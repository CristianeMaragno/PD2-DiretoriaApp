package com.cristianerm.pd2diretoria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class UsersActivity extends AppCompatActivity {

    ImageButton voltar;
    Button cadastrar_user;
    Button remover_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        voltar = (ImageButton) findViewById(R.id.buttonVoltarUser);
        cadastrar_user = (Button) findViewById(R.id.button_cadastrar_usuario);
        remover_user = (Button) findViewById(R.id.button_remover_usuario);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(UsersActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        cadastrar_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(UsersActivity.this, UserCadastroActivity.class);
                startActivity(i);
            }
        });

        remover_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(UsersActivity.this, UserRemovalActivity.class);
                startActivity(i);
            }
        });
    }
}
