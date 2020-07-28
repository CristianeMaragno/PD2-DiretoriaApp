package com.cristianerm.pd2diretoria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterUserRemoval extends RecyclerView.Adapter<AdapterUserRemoval.AdapterUserRemovalViewHolder> {

    private ArrayList<RemoverUserItem> mUsersList;

    public static class AdapterUserRemovalViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ImageView mImageView;

        public AdapterUserRemovalViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textViewUserRegistrado);
            mImageView = itemView.findViewById(R.id.imageViewDeleteUser);

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