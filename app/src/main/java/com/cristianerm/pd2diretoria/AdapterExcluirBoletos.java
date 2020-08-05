package com.cristianerm.pd2diretoria;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
        final ExcluirBoletosItem currentItem = mBoletosList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getText());

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String boletoName = currentItem.getText();
                String aluno = currentItem.getUserName();
                ConfirmarDelete(aluno, boletoName, view);
            }
        });
    }

    private void ConfirmarDelete(final String aluno, final String boletoName, View v){
        new AlertDialog.Builder(v.getContext())
                .setTitle("Comfirmação:")
                .setMessage("Você tem certeza que deseja deletar este boleto?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteBoleto(aluno, boletoName);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Não fazer nada
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void DeleteBoleto(String aluno, String boletoName){
        String nome_boleto = aluno + "_" + boletoName + ".pdf";
        Log.d("Adapter Excluir Boletos", nome_boleto);
        String url_file = "gs://pd2apps.appspot.com/Boletos/"+nome_boleto;
        Log.d("Adapter Excluir Boletos", url_file);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(url_file);


        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Adapter Excluir Boletos", "File deleted successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBoletosList.size();
    }

}


