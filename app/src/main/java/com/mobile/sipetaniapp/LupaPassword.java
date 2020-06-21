package com.mobile.sipetaniapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LupaPassword extends AppCompatActivity {
    TextView buatAkun, punyaAkun;
    EditText email, pin;
    Button btnReset;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        email = (EditText)findViewById(R.id.Lupa_email);
        pin = (EditText)findViewById(R.id.Lupa_pin);
        pd = new ProgressDialog(LupaPassword.this);

        buatAkun = (TextView)findViewById(R.id.akun);
        buatAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BuatAkun = new Intent(LupaPassword.this, BuatAkun.class);
                startActivity(BuatAkun);
                Toast.makeText(LupaPassword.this, "Buat Akun Baru", Toast.LENGTH_LONG).show();
            }
        });

        punyaAkun = (TextView)findViewById(R.id.punyaAkun);
        punyaAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PunyaAkun = new Intent(LupaPassword.this, MainActivity.class);
                startActivity(PunyaAkun);
                Toast.makeText(LupaPassword.this, "Login!", Toast.LENGTH_LONG).show();
            }
        });

        btnReset = (Button)findViewById(R.id.btnLupaPass);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Pin = pin.getText().toString();
                if (validasi(Email, Pin)){
                    reset(Email, Pin);
                }
            }
        });
    }

    protected boolean validasi(String Email, String Pin){
        if (Email.isEmpty()){
            email.setError("Email tidak boleh kosong!");
            email.requestFocus();
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Format Email salah, isikan Email Anda!");
            email.requestFocus();
            return false;
        }else if (Pin.isEmpty()){
            pin.setError("PIN tidak boleh kosong!");
            pin.requestFocus();
            return false;
        }else if (Pin.length() != 3){
            pin.setError("PIN harus berjumlah 3 digit!");
            pin.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    private void reset(final String Email, final String Pin){
        pd.setMessage("Proses Reset Akun...");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_RESET_AKUN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.optString("success").equals("1")) {
                                Intent intent = new Intent(LupaPassword.this.getBaseContext(), ResetPassword.class);
                                intent.putExtra("email", Email);
                                LupaPassword.this.startActivity(intent);
                            } else if (res.optString("success").equals("2")) {
                                pin.setError(res.getString("message"));
                                pin.requestFocus();
                            } else if (res.optString("success").equals("3")) {
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
                        Toast.makeText(LupaPassword.this, "pesan : Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", Email);
                map.put("pin", Pin);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }
}

