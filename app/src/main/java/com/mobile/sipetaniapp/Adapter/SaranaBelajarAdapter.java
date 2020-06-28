package com.mobile.sipetaniapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.sipetaniapp.Model.SaranaBelajar;
import com.mobile.sipetaniapp.R;

import java.util.ArrayList;
import java.util.List;

public class SaranaBelajarAdapter extends RecyclerView.Adapter<SaranaBelajarAdapter.SaranaBelajarViewHolder> {
    private List<SaranaBelajar> listSaranaBelajar = new ArrayList<>();

    public SaranaBelajarAdapter(List<SaranaBelajar> listSaranaBelajar){
        this.listSaranaBelajar = listSaranaBelajar;
    }

    private OnSaranaBelajarClickListener listener;

    public interface OnSaranaBelajarClickListener {
        public void onClick(View view, int position);
    }

    public void setListener(OnSaranaBelajarClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SaranaBelajarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vh = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_sarana_belajar,viewGroup,false);
        SaranaBelajarViewHolder viewHolder = new SaranaBelajarViewHolder(vh);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder (@NonNull SaranaBelajarViewHolder saranaBelajarViewHolder, int i) {
        SaranaBelajar item = listSaranaBelajar.get(i);
        saranaBelajarViewHolder.ketSaranaBelajar.setText(item.getKeterangan());
        saranaBelajarViewHolder.judulSaranaBelajar.setText(item.getJudul());
        saranaBelajarViewHolder.imgSaranaBelajar.setImageResource(item.getImgSaranaBelajar());
    }

    @Override
    public int getItemCount() {
        return listSaranaBelajar.size();
    }

    public class SaranaBelajarViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgSaranaBelajar;
        public TextView judulSaranaBelajar, ketSaranaBelajar;

        public SaranaBelajarViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSaranaBelajar = itemView.findViewById(R.id.imgSaranaBelajar);
            judulSaranaBelajar = itemView.findViewById(R.id.judulSaranaBelajar);
            ketSaranaBelajar = itemView.findViewById(R.id.ketSaranaBelajar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, getAdapterPosition());
                }
            });
        }
    }
}
