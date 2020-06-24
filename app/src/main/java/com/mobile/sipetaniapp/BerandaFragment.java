package com.mobile.sipetaniapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BerandaFragment extends Fragment {
    EditText jumlah;
    Button btnPemesanan, tgl;
    ProgressDialog pd;
    private static String view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        jumlah = (EditText) view.findViewById(R.id.jumTiket);
        pd = new ProgressDialog(getActivity());

        tgl = (Button) view.findViewById(R.id.tglBerkunjung);
        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kalender();
            }
        });

        btnPemesanan = (Button) view.findViewById(R.id.btnpesan);
        btnPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parsingdata();
            }
        });

        Calendar defaultkalender = Calendar.getInstance();
        SimpleDateFormat defaultformat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tgl.setText(defaultformat.format(defaultkalender.getTime()));

        return view;
    }

    public void parsingdata(){
        final String TGL = tgl.getText().toString();
        final String JUMLAH = jumlah.getText().toString();

        if (JUMLAH.isEmpty()){
            jumlah.setError("Jumlah tiket harap diisi");
            jumlah.requestFocus();
        }else{
            try {
                pd.setMessage("Proses Pemesanan...");
                pd.setCancelable(false);
                pd.show();
                Intent intent = new Intent(getActivity().getBaseContext(), DetailPembayaran.class);
                intent.putExtra("tanggal", TGL);
                intent.putExtra("jumlah", JUMLAH);
                getActivity().startActivity(intent);
                pd.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void kalender(){
        Calendar kalenderbaru = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(tahun, bulan, hari);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                tgl.setText(dateFormat.format(newDate.getTime()));
            }
        },kalenderbaru.get(Calendar.YEAR), kalenderbaru.get(Calendar.MONTH), kalenderbaru.get(Calendar.DAY_OF_MONTH));
        DatePicker dp = datePickerDialog.getDatePicker();
        dp.setMinDate(kalenderbaru.getTimeInMillis());
        // dp.setMaxDate((kalenderbaru.getTimeInMillis())+(432000000));
        datePickerDialog.show();
    }
}



