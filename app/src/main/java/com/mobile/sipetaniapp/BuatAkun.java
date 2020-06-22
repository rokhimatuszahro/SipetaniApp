package com.mobile.sipetaniapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import androidx.appcompat.app.AppCompatActivity;

public class BuatAkun extends AppCompatActivity {
    TextView lupaPass, punyaAkun;
    EditText email, username, password, pin, no, alamat;
    RadioGroup jenkel;
    RadioButton radioButton;
    Button btnRegis;
    ProgressDialog pd;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_akun);

        email = (EditText)findViewById(R.id.email);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.pass);
        pin = (EditText)findViewById(R.id.pin);
        no = (EditText)findViewById(R.id.noHp);
        alamat = (EditText)findViewById(R.id.alamat);
        jenkel = (RadioGroup)findViewById(R.id.jenKel);
        lupaPass = (TextView)findViewById(R.id.lpPass);
        punyaAkun = (TextView)findViewById(R.id.punyaAkun);
        btnRegis = (Button)findViewById(R.id.btnRegis);
        pd = new ProgressDialog(this);
        dialog = new AlertDialog.Builder(this);

        lupaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lpPass = new Intent(BuatAkun.this, LupaPassword.class);
                startActivity(lpPass);
                Toast.makeText(BuatAkun.this, "Lupa Password!", Toast.LENGTH_LONG).show();
            }
        });


        punyaAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PunyaAkun = new Intent(BuatAkun.this, MainActivity.class);
                startActivity(PunyaAkun);
                Toast.makeText(BuatAkun.this, "Login!", Toast.LENGTH_LONG).show();
            }
        });

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = jenkel.getCheckedRadioButtonId();
                radioButton = (RadioButton)findViewById(radioButtonId);
                String Email = email.getText().toString();
                String Username = username.getText().toString();
                String Password = password.getText().toString();
                String PIN = pin.getText().toString();
                String NO = no.getText().toString();
                String Alamat = alamat.getText().toString();
                String Jenkel = radioButton.getText().toString();

                if (validasi(Email, Username, Password, PIN, NO, Alamat) == true){
                    regisData(Email, Username, Password, PIN, NO, Alamat, Jenkel);
                }
            }
        });
    }

    protected boolean validasi(String Email, String Username, String Password, String PIN,String NO,String Alamat){
        if (Email.isEmpty()){
            email.setError("Email kosong, isikan Email Anda!");
            email.requestFocus();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Format Email salah, isikan Email Anda!");
            email.requestFocus();
            return false;
        }
        else if (Username.isEmpty()){
            username.setError("Username kosong, isikan Username Anda!");
            username.requestFocus();
            return false;
        }
        else if (Password.isEmpty()){
            password.setError("Password kosong, isikan Password Anda!");
            password.requestFocus();
            return false;
        }
        else if (Password.length()<4 || Password.length()>8){
            password.setError("Password harus berjumlah lebih dari 4 atau kurang dari 8 digit!");
            password.requestFocus();
            return false;
        }
        else if (PIN.isEmpty()){
            pin.setError("PIN kosong, isikan PIN Anda!");
            pin.requestFocus();
            return false;
        }
        else if (PIN.length() != 3){
            pin.setError("PIN harus berjumlah 3 digit!");
            pin.requestFocus();
            return false;
        }
        else if (NO.isEmpty()){
            no.setError("No Telp kosong, isikan No Telp Anda!");
            no.requestFocus();
            return false;
        }
        else if (NO.length()<11 || NO.length()>13){
            no.setError("No Telp harus berjumlah 11 atau 12 digit!");
            no.requestFocus();
            return false;
        }
        else if (Alamat.isEmpty()){
            alamat.setError("Alamat kosong, isikan Alamat Anda!");
            alamat.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    // Fungsi mengolah data dengan API
    private void regisData(final String Email, final String Username, final String Password, final String PIN, String NO, String Alamat, final String Jenkel) {
        pd.setMessage("Registrasi Akun...");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_REGISTRASI,
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
                                dialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(BuatAkun.this, MainActivity.class));
                                    }
                                });
                                dialog.show();
                            } else if (res.optString("success").equals("0")) {
                                email.setError(res.getString("message"));
                                email.requestFocus();
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
                        Toast.makeText(BuatAkun.this, "pesan : Periksa Koneksi Anda!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> map = new HashMap<>();
                map.put("nama", Username);
                map.put("email", Email);
                map.put("pin", PIN);
                map.put("password", Password);
                map.put("jenkel", Jenkel);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }
}

