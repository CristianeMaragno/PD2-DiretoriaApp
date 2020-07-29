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

import java.util.ArrayList;

public class AdapterUserRemoval extends RecyclerView.Adapter<AdapterUserRemoval.AdapterUserRemovalViewHolder> {

    private ArrayList<RemoverUserItem> mUsersList;

    public static class AdapterUserRemovalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        public ImageView mImageView;

        private FirebaseDatabase mFirebaseDatase;
        private DatabaseReference myRef;
        private DatabaseReference myRefDeleteUser;
        private DatabaseReference myRefDeleteUserFromUsers;

        public AdapterUserRemovalViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textViewUserRegistrado);
            mImageView = itemView.findViewById(R.id.imageViewDeleteUser);

            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String name_user = mTextView.getText().toString();
            OpenConfirmarDelete(name_user, v);
            Log.d("Adapter User Removal", "On click of image view!! Nome: " + name_user);
        }

        public void OpenConfirmarDelete(final String name_user, View v){

            new AlertDialog.Builder(v.getContext())
                    .setTitle("Comfirmação:")
                    .setMessage("Você tem certeza que deseja deletar " + name_user + "?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteUserInfoFromDatabase(name_user);
                            //DeleteUser();
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

        public void DeleteUserInfoFromDatabase(final String name_user){

            mFirebaseDatase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatase.getReference().child("alunos").child(name_user);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        UserInformation uInfo = new UserInformation();
                        uInfo.setUser_id(ds.getValue(UserInformation.class).getUser_id());

                        String user_id = uInfo.getUser_id();

                        myRefDeleteUser = mFirebaseDatase.getReference().child(user_id);
                        myRefDeleteUserFromUsers = mFirebaseDatase.getReference().child("alunos").child(name_user);

                        myRefDeleteUser.removeValue();
                        myRefDeleteUserFromUsers.removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }
    public AdapterUserRemoval(ArrayList<RemoverUserItem> usersList) {
        mUsersList = usersList;
    }
    @Override
    public AdapterUserRemovalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.remover_user_item, parent, false);
        AdapterUserRemovalViewHolder rvh = new AdapterUserRemovalViewHolder(v);
        return rvh;
    }
    @Override
    public void onBindViewHolder(AdapterUserRemovalViewHolder holder, int position) {
        RemoverUserItem currentItem = mUsersList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getText());
    }
    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

}