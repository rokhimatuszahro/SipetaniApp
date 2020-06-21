package com.mobile.sipetaniapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mobile.sipetaniapp.Helper.SharedPreferenceHelper;
import com.mobile.sipetaniapp.Until.AppController;
import com.mobile.sipetaniapp.Until.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailPembayaran extends AppCompatActivity {

    TextView tglPemesanan, jumTiket, totPembayaran, namaPembeli, noHpPembeli, alamatPembeli;
    Button btnPesanTiket, btnBatalPesanTiket;
    SharedPreferenceHelper sp;
    ProgressDialog pd;
    AlertDialog.Builder dialog;
    private String hari;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembayaran);

        namaPembeli = (TextView)findViewById(R.id.namaPembeli);
        noHpPembeli = (TextView)findViewById(R.id.noHpDetailPemesanan);
        alamatPembeli = (TextView)findViewById(R.id.alamatDetailPemesanan);
        tglPemesanan = (TextView)findViewById(R.id.isiTglBerkunjung);
        jumTiket = (TextView)findViewById(R.id.isiJumTiket);
        totPembayaran = (TextView)findViewById(R.id.isiTotPembayaran);
        sp = new SharedPreferenceHelper(DetailPembayaran.this);
        pd = new ProgressDialog(DetailPembayaran.this);
        dialog = new AlertDialog.Builder(this);

        // Menerima Parsing data dari fragment beranda
        Intent intent = getIntent();
        final String tanggal = intent.getStringExtra("tanggal");
        final String jumlah = intent.getStringExtra("jumlah");

        // Mengambil nama hari saat ini
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = df.parse(tanggal);
            DateFormat formathari = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            hari = formathari.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Menghitung total harga berdasarkan hari
        if (hari.equals("Saturday") || hari.equals("Sunday")){
            total = Integer.parseInt(jumlah) * 20000;
        }else if (hari.equals("Friday")){
            dialog.setTitle("Notifikasi");
            dialog.setCancelable(false);
            dialog.setMessage("Hari Jumat Libur");
            dialog.setPositiveButton("Terima kasih", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(DetailPembayaran.this, Navigation.class));
                    finish();
                }
            });
            dialog.show();
        }else{
            total = Integer.parseInt(jumlah) * 12000;
        }

        // Menghitung data sesuai data yang dikirim dari beranda
        tglPemesanan.setText(tanggal);
        jumTiket.setText(jumlah);
        totPembayaran.setText("IDR "+ NumberFormat.getNumberInstance(Locale.US).format(total));

        btnPesanTiket = (Button)findViewById(R.id.btnPesanTiket);
        btnPesanTiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nama = namaPembeli.getText().toString();
                String Email = sp.getEmail("email");
                String noHp = noHpPembeli.getText().toString();
                String Alamat = alamatPembeli.getText().toString();
                String Jumlah = jumTiket.getText().toString();
                String Tanggal_Pemesanan = tglPemesanan.getText().toString();
                String Total = String.valueOf(total);
                String UserId = String.valueOf(sp.getId_user(0));
                if (validasi(Nama, Email, noHp, Alamat, Jumlah, Tanggal_Pemesanan,Total, UserId) == true){
                    pesantiket(Nama, Email, noHp, Alamat, Jumlah, Tanggal_Pemesanan,Total, UserId);
                }
            }
        });

        btnBatalPesanTiket = (Button)findViewById(R.id.btnBatalPesanTiket);
        btnBatalPesanTiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailPembayaran.this, Navigation.class));
                finish();
            }
        });
    }

    protected boolean validasi(String Nama, String Email, String noHp, String Alamat, String Jumlah, String Tanggal_Pemesanan, String Total, String UserId){
        if (Nama.isEmpty()){
            namaPembeli.setError("Nama pemesan tidak boleh kosong");
            namaPembeli.requestFocus();
            return false;
        }
        if (noHp.isEmpty()){
            noHpPembeli.setError("No Hp tidak boleh kosong");
            noHpPembeli.requestFocus();
            return false;
        }
        if (noHp.length()<11 || noHp.length()>13){
            noHpPembeli.setError("No Hp harus 11 atau 12 digit");
            noHpPembeli.requestFocus();
            return false;
        }
        if (Alamat.isEmpty()){
            alamatPembeli.setError("Almat tidak boleh kosong");
            alamatPembeli.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    private void pesantiket(final String Nama, final String Email, final String noHp, final String Alamat, final String Jumlah, final String Tanggal_Pemesanan, final String Total, final String UserId){
        pd.setMessage("Proses Pemesanan Tiket....");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_PEMESANAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.optString("success").equals("1")) {
                                dialog.setTitle(res.getString("title"));
                                dialog.setCancelable(false);
                                dialog.setMessage(res.getString("message"));
                                dialog.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(DetailPembayaran.this, com.mobile.sipetaniapp.DetailPemesanan.class));
                                        finish();
                                    }
                                });
                                dialog.show();
                            } else if (res.optString("success").equals("2")) {
                                dialog.setTitle(res.getString("title"));
                                dialog.setCancelable(false);
                                dialog.setMessage(res.getString("message"));
                                dialog.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(DetailPembayaran.this, Navigation.class));
                                        finish();
                                    }
                                });
                                dialog.show();
                            } else {
                                startActivity(new Intent(DetailPembayaran.this, Navigation.class));
                                Toast.makeText(DetailPembayaran.this, res.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        sp.logout();
                        startActivity(new Intent(DetailPembayaran.this, MainActivity.class));
                        Toast.makeText(DetailPembayaran.this, "pesan: Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("nama", Nama);
                map.put("email", Email);
                map.put("no", noHp);
                map.put("alamat", Alamat);
                map.put("jumlah", Jumlah);
                map.put("tanggal_berkunjung", Tanggal_Pemesanan);
                map.put("total", Total);
                map.put("user_id", UserId);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }
}
