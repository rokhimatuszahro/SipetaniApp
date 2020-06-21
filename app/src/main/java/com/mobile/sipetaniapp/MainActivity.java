package com.mobile.sipetaniapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.mobile.sipetaniapp.Helper.SharedPreferenceHelper;
import com.mobile.sipetaniapp.Until.AppController;
import com.mobile.sipetaniapp.Until.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView lupaPass, buatAkun;
    EditText email, password;
    Button btnlogin;
    ProgressDialog pd;
    AlertDialog.Builder dialog;
    SharedPreferenceHelper sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.Log_email);
        password = findViewById(R.id.Log_pass);
        pd = new ProgressDialog(this);
        dialog = new AlertDialog.Builder(this);
        sp = new SharedPreferenceHelper(this);

        lupaPass = (TextView)findViewById(R.id.lpPass);
        lupaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lpPass = new Intent(MainActivity.this, LupaPassword.class);
                startActivity(lpPass);
                Toast.makeText(MainActivity.this, "Lupa Password?", Toast.LENGTH_LONG).show();
            }
        });

        buatAkun = (TextView)findViewById(R.id.akun);
        buatAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BuatAkun = new Intent(MainActivity.this, BuatAkun.class);
                startActivity(BuatAkun);
                Toast.makeText(MainActivity.this, "Buat Akun Baru!", Toast.LENGTH_LONG).show();
            }
        });

        btnlogin = findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if (validasi(Email, Password) == true){
                    login(Email, Password);
                }
            }
        });
    }

    // Cek apakah sudah login
    @Override
    protected void onStart(){
        super.onStart();
        if (sp.getLogin(true)){
            startActivity(new Intent(MainActivity.this,Navigation.class));
            finish();
        }
    }

    protected boolean validasi(String Email, String Password){
        if (Email.isEmpty()){
            email.setError("Email kosong, isikan Email Anda!");
            email.requestFocus();
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Format Email salah, isikan Email Anda!");
            email.requestFocus();
            return false;
        }else if (Password.isEmpty()){
            password.setError("Password kosong, isikan Password Anda!");
            password.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    private void login(final String Email, final String Password){
        pd.setMessage("Proses Login...");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.optString("success").equals("1")) {
                                JSONObject data = res.getJSONObject("user_detail");
                                sp.setEmail(data.getString("email"));
                                sp.setId_akses(data.getInt("id_akses"));
                                sp.setId_user(data.getInt("id_user"));
                                sp.setLogin(true);
                                startActivity(new Intent(MainActivity.this, Navigation.class));
                            } else if (res.optString("success").equals("0")){
                                dialog.setTitle(res.getString("title"));
                                dialog.setCancelable(false);
                                dialog.setMessage(res.getString("message"));
                                dialog.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
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
                        Toast.makeText(MainActivity.this, "pesan : Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", Email);
                map.put("password", Password);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }
}

