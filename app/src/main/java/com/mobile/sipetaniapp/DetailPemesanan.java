package com.mobile.sipetani;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mobile.sipetani.Helper.SharedPreferenceHelper;
import com.mobile.sipetani.Until.AppController;
import com.mobile.sipetani.Until.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailPemesanan extends AppCompatActivity {

    TextView nama, alamat, no, tgl, jumlah, total;
    Button btnUnggah;
    ImageView imgview, iconUploadBukti;
    Bitmap bitmap;
    SharedPreferenceHelper sp;
    AlertDialog.Builder dialog;
    ProgressDialog pd;
    private String gambar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesanan);

        nama = (TextView)findViewById(R.id.isinama);
        alamat = (TextView)findViewById(R.id.isialamat);
        no = (TextView)findViewById(R.id.isinomorhp);
        tgl = (TextView)findViewById(R.id.isiTglBerkunjung);
        jumlah = (TextView)findViewById(R.id.isiJumTiket);
        total = (TextView)findViewById(R.id.isiTotPembayaran);
        imgview = (ImageView)findViewById(R.id.imgUnggah);
        iconUploadBukti = (ImageView)findViewById(R.id.iconUploadBukti);
        dialog = new AlertDialog.Builder(this);
        pd = new ProgressDialog(DetailPemesanan.this);
        sp = new SharedPreferenceHelper(DetailPemesanan.this);

        btnUnggah = (Button)findViewById(R.id.btnUnggah);
        btnUnggah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gambar.equals("") || gambar.equals(null)){
                    Toast.makeText(DetailPemesanan.this, "Pilih Foto Bukti Pembayaran!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    SendDetail();
                }
            }
        });

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

            if (requestCode == 2){
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    bitmap = getResizedBitmap(bitmap, 720);
                    imgview.setImageBitmap(bitmap);
                    iconUploadBukti.setVisibility(View.GONE);
                    gambar = getStringImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getStringImage(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgbytearray = baos.toByteArray();
        String img = Base64.encodeToString(imgbytearray, Base64.DEFAULT);
        return img;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float)height;
        if (bitmapRatio > 1){
            width = maxSize;
            height = (int) (width / bitmapRatio);
        }else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void SendDetail(){
        pd.setMessage("Proses Unggah foto bukti pembayaran...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        final String Id_User = String.valueOf(sp.getId_user(0));
        final String Email = sp.getEmail("email");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.URL_UPLOAD_BUKTI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.cancel();
                            JSONObject res = new JSONObject(response);
                            String error_status = res.getString("success");
                            if (error_status.equals("1")) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailPemesanan.this);
                                alertDialogBuilder.setTitle(res.getString("title"));
                                alertDialogBuilder.setCancelable(false);
                                alertDialogBuilder.setMessage(res.getString("message"));
                                alertDialogBuilder.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        imgview.setImageBitmap(null);
                                        iconUploadBukti.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(DetailPemesanan.this, Navigation.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                alertDialogBuilder.show();
                            } else if (error_status.equals("0")){
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailPemesanan.this);
                                alertDialogBuilder.setTitle(res.getString("title"));
                                alertDialogBuilder.setCancelable(false);
                                alertDialogBuilder.setMessage(res.getString("message"));
                                alertDialogBuilder.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(DetailPemesanan.this, Navigation.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                alertDialogBuilder.show();
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
                        startActivity(new Intent(DetailPemesanan.this, MainActivity.class));
                        Toast.makeText(DetailPemesanan.this, "pesan : Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> map = new HashMap<>();
                map.put("id_user", Id_User);
                map.put("email", Email);
                map.put("foto", gambar);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    protected void onStart(){
        super.onStart();
        detail_pemesanan();
    }

    private void detail_pemesanan(){
        final String Id_User = String.valueOf(sp.getId_user(0));

        pd.setMessage("Proses Detail Pemesanan...");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_DETAIL_PEMESANAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.optString("success").equals("1")) {
                                JSONObject data = new JSONObject(res.getString("detail_pemesanan"));
                                nama.setText(data.getString("nama_pemesan"));
                                alamat.setText(data.getString("alamat"));
                                tgl.setText(data.getString("tanggal_berkunjung"));
                                jumlah.setText(data.getString("jumlah_tiket"));
                                total.setText("IDR "+NumberFormat.getNumberInstance(Locale.US).format(data.getInt("total_pembayaran")));
                                no.setText(data.getString("no_telp"));
                            } else if (res.optString("success").equals("2")) {
                                dialog.setTitle(res.getString("title"));
                                dialog.setCancelable(false);
                                dialog.setMessage(res.getString("message"));
                                dialog.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(DetailPemesanan.this, Navigation.class));
                                        finish();
                                    }
                                });
                                dialog.show();
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
                        startActivity(new Intent(DetailPemesanan.this, MainActivity.class));
                        Toast.makeText(DetailPemesanan.this, "pesan : Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> map = new HashMap<>();
                map.put("id_user", Id_User);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }
}
