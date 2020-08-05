package com.cristianerm.pd2diretoria;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterExcluirBoletos extends RecyclerView.Adapter<AdapterExcluirBoletos.AdapterExcluirBoletosViewHolder> {

    private ArrayList<ExcluirBoletosItem> mBoletosList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    public static class AdapterExcluirBoletosViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ImageView mImageView;

        public AdapterExcluirBoletosViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textViewExcluirBoletosItem);
            mImageView = itemView.findViewById(R.id.imageViewExcluirBoletosItem);
        }

    }

    public AdapterExcluirBoletos(ArrayList<ExcluirBoletosItem> boletosList) {
        mBoletosList = boletosList;
    }
    @Override
    public AdapterExcluirBoletos.AdapterExcluirBoletosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.excluir_boletos_item, parent, false);
        AdapterExcluirBoletos.AdapterExcluirBoletosViewHolder dvh = new AdapterExcluirBoletos.AdapterExcluirBoletosViewHolder(v);
        return dvh;
    }
    @Override
    public void onBindViewHolder(final AdapterExcluirBoletos.AdapterExcluirBoletosViewHolder holder, final int position) {
        ExcluirBoletosItem currentItem = mBoletosList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getText());

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String boletoName = currentItem.getText();
                String aluno = currentItem.getUserName();
                DeleteBoleto(aluno, boletoName);
            }
        });
    }

    private void DeleteBoleto(String aluno, String boletoName){

    }

    @Override
    public int getItemCount() {
        return mBoletosList.size();
    }

}


