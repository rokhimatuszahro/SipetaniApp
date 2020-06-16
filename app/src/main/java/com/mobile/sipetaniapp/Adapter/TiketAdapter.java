package com.mobile.sipetaniapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.sipetaniapp.R;

import java.util.ArrayList;
import java.util.List;

public class TiketAdapter extends RecyclerView.Adapter<TiketAdapter.TiketHolder> {
    private List<DataModel> mTiket = new ArrayList<>();

    public TiketAdapter(List<DataModel> mTiket){
        this.mTiket = mTiket;
    }

    private OnTiketClickListener listener;

    public interface OnTiketClickListener{
        public void onClick(View view, int position);
    }

    public void setListener(OnTiketClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public TiketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_tiket,parent,false);
        TiketHolder tiketHolder = new TiketHolder(layout);
        return tiketHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TiketHolder tiketHolder, int position){
        DataModel md = mTiket.get(position);
        tiketHolder.tvno.setText(md.getNo());
        tiketHolder.tvnama.setText(md.getNama());
        tiketHolder.tvtgl.setText(md.getTgl());
        tiketHolder.tvtot.setText(md.getTotal());
        tiketHolder.tviconprint.setImageResource(md.getIconprint());
    }

    @Override
    public int getItemCount(){
        return mTiket.size();
    }

    public class TiketHolder extends RecyclerView.ViewHolder{
        TextView tvno, tvnama, tvtgl, tvtot;
        ImageView tviconprint;

        public TiketHolder(@NonNull View view){
            super(view);
            tvno = (TextView)view.findViewById(R.id.noTiket);
            tvnama = (TextView)view.findViewById(R.id.namaTiket);
            tvtgl = (TextView)view.findViewById(R.id.tglTiket);
            tvtot = (TextView)view.findViewById(R.id.totTiket);
            tviconprint = (ImageView) view.findViewById(R.id.iconprint);

            tviconprint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, getAdapterPosition());
                }
            });
        }
    }
}
