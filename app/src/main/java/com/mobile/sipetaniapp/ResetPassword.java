package com.mobile.sipetaniapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mobile.sipetaniapp.Until.AppController;
import com.mobile.sipetaniapp.Until.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {
    TextView buatAkun, punyaAkun;
    EditText pass1, pass2;
    Button reset;
    ProgressDialog pd;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        pass1 = (EditText)findViewById(R.id.pass1Reset);
        pass2 = (EditText)findViewById(R.id.pass2Reset);
        pd = new ProgressDialog(this);
        dialog = new AlertDialog.Builder(this);

        buatAkun = (TextView)findViewById(R.id.akun);
        buatAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BuatAkun = new Intent(ResetPassword.this, BuatAkun.class);
                startActivity(BuatAkun);
                Toast.makeText(ResetPassword.this, "Buat Akun Baru", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        punyaAkun = (TextView)findViewById(R.id.punyaAkun);
        punyaAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PunyaAkun = new Intent(ResetPassword.this, MainActivity.class);
                startActivity(PunyaAkun);
                Toast.makeText(ResetPassword.this, "Login!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        reset = (Button)findViewById(R.id.btnResetPass);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String Email = intent.getStringExtra("email");
                String Pass1 = pass1.getText().toString();
                String Pass2 = pass2.getText().toString();
                if (validasi(Pass1, Pass2) == true){
                    resetpass(Email, Pass1);
                }
            }
        });

    }

    private void resetpass(final String Email, final String Pass1) {
        pd.setMessage("Proses Reset Akun...");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_RESET,
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
                                dialog.setPositiveButton("Terima kasih", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ResetPassword.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                dialog.show();
                            } else if (res.optString("success").equals("0")) {
                                dialog.setTitle(res.getString("title"));
                                dialog.setCancelable(false);
                                dialog.setMessage(res.getString("message"));
                                dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent gagal = new Intent(ResetPassword.this, MainActivity.class);
                                        startActivity(gagal);
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
                        Toast.makeText(ResetPassword.this, "pesan : Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", Email);
                map.put("password", Pass1);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }

    protected boolean validasi(String Pass1, String Pass2){
        if (Pass1.isEmpty()){
            pass1.setError("Password Baru tidak boleh kosong!",null);
            pass1.requestFocus();
            return false;
        }else if (Pass2.isEmpty()){
            pass2.setError("Password Baru tidak boleh kosong!", null);
            pass2.requestFocus();
            return false;
        }else if (Pass1.length() < 4){
            pass1.setError("Password Minimal 4 karakter!", null);
            pass1.requestFocus();
            return false;
        }else if (Pass1.length() > 8){
            pass1.setError("Password Maksimal 8 karakter!", null);
            pass1.requestFocus();
            return false;
        }else if (Pass2.length() < 4){
            pass2.setError("Password Minimal 4 karakter!", null);
            pass2.requestFocus();
            return false;
        }else if (Pass2.length() > 8){
            pass2.setError("Password Maksimal 8 karakter!", null);
            pass2.requestFocus();
            return false;
        }else{
            return true;
        }
    }
}
