package com.cristianerm.pd2diretoria;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterDiario extends RecyclerView.Adapter<AdapterDiario.AdapterDiarioViewHolder> {

    private ArrayList<DiarioItem> mDiarioList;

    public static class AdapterDiarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        public ImageView mImageView;
        public ImageView mImageViewImage;

        public AdapterDiarioViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textViewDiarioItem);
            mImageView = itemView.findViewById(R.id.imageViewDiarioItem);
            mImageViewImage = itemView.findViewById(R.id.image_view_image_diario_item);

            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Adapter Diario", "On click of image view!!!");
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
    public void onBindViewHolder(AdapterDiarioViewHolder holder, int position) {
        DiarioItem currentItem = mDiarioList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView.setText(currentItem.getText());
        Picasso.get()
                .load(currentItem.getImageUrl())
                .into(holder.mImageViewImage);
    }
    @Override
    public int getItemCount() {
        return mDiarioList.size();
    }
}
