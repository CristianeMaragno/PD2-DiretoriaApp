package com.cristianerm.pd2diretoria;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterDiario extends RecyclerView.Adapter<AdapterDiario.AdapterDiarioViewHolder> {

    private ArrayList<DiarioItem> mDiarioList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private DatabaseReference myRefDeletePost;

    public static class AdapterDiarioViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ImageView mImageView;
        public ImageView mImageViewImage;

        public AdapterDiarioViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textViewDiarioItem);
            mImageView = itemView.findViewById(R.id.imageViewDiarioItem);
            mImageViewImage = itemView.findViewById(R.id.image_view_image_diario_item);
        }

    }

    public AdapterDiario(ArrayList<DiarioItem> diarioList) {
        mDiarioList = diarioList;
    }
    @Override
    public AdapterDiarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.diario_item, parent, false);
        AdapterDiarioViewHolder dvh = new AdapterDiarioViewHolder(v);
        return dvh;
    }
    @Override
    public void onBindViewHolder(final AdapterDiarioViewHolder holder, final int position) {
        holder.mImageView.setTag(position);

        DiarioItem currentItem = mDiarioList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getText());
        Picasso.get()
                .load(currentItem.getImageUrl())
                .into(holder.mImageViewImage);

        final String turmaSelecionada = currentItem.getmTurmaSelecionada();

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = holder.mTextView.getText().toString();
                DeleteDiarioPost(turmaSelecionada, view, text.substring(0,27));
            }
        });
    }

    private void DeleteDiarioPost(final String turmaSelecionada, View v, final String text){
        new AlertDialog.Builder(v.getContext())
                .setTitle("Comfirmação:")
                .setMessage("Você tem certeza que deseja deletar este post?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DeletePost(turmaSelecionada, text);
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

    private void DeletePost(final String turmaSelecionada, final String data_post){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("diario_professor").child(turmaSelecionada);
        Log.d("Adapter Diario", turmaSelecionada);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DiarioInformation dInfo = new DiarioInformation();
                    dInfo.setDate(ds.getValue(DiarioInformation.class).getDate());

                    String data = dInfo.getDate();

                    if(data.equals(data_post)){
                        String database_key = ds.getKey();
                        myRefDeletePost = mFirebaseDatabase.getReference().child("diario_professor").child(turmaSelecionada).child(database_key);
                        myRefDeletePost.removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDiarioList.size();
    }

}
