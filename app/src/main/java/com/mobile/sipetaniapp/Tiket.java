package com.mobile.sipetaniapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mobile.sipetani.Adapter.TiketAdapter;
import com.mobile.sipetani.Model.DataModel;
import com.mobile.sipetani.Helper.SharedPreferenceHelper;
import com.mobile.sipetani.Until.AppController;
import com.mobile.sipetani.Until.ServerAPI;
import com.mobile.sipetaniapp.Adapter.TiketAdapter;
import com.mobile.sipetaniapp.Helper.SharedPreferenceHelper;
import com.mobile.sipetaniapp.Model.DataModel;
import com.mobile.sipetaniapp.R;
import com.mobile.sipetaniapp.Until.AppController;
import com.mobile.sipetaniapp.Until.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tiket extends AppCompatActivity implements TiketAdapter.OnTiketClickListener {

    TextView tiketKosong;
    RecyclerView rv;
    SharedPreferenceHelper sp;
    ProgressDialog pd;
    public TiketAdapter tiketAdapter;
    public RecyclerView.LayoutManager layoutManager;
    public List<DataModel> mTiket = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket);

        tiketKosong = (TextView)findViewById(R.id.tiketkosong);
        rv = (RecyclerView)findViewById(R.id.rvTiket);
        sp = new SharedPreferenceHelper(Tiket.this);
        pd = new ProgressDialog(Tiket.this);

        loadTiket();

        tiketAdapter = new TiketAdapter(mTiket);
        tiketAdapter.setListener(this);
        layoutManager = new LinearLayoutManager(getApplication());
        rv.setHasFixedSize(true);
        rv.setAdapter(tiketAdapter);
        rv.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View view, int position){
        DataModel dataTiket = mTiket.get(position);
        Intent intent = new Intent(Tiket.this, print.class);
        intent.putExtra("id_pemesanan", dataTiket.getId_pemesanan());
        startActivity(intent);
    }

    private void loadTiket(){
        final String ID_USER = String.valueOf(sp.getId_user(0));

        pd.setMessage("Load Tiket...");
        pd.setCancelable(false);
        pd.show();

        StringRequest reqData = new StringRequest(Request.Method.POST, ServerAPI.URL_TIKET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        Log.d("volley", "response : " + response.toString());
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.optString("success").equals("1")) {
                                JSONArray tiket = res.getJSONArray("tiket");
                                for (int i = 0; i < tiket.length(); i++) {
                                    try {
                                        JSONObject data = tiket.getJSONObject(i);
                                        DataModel md = new DataModel();
                                        md.setId_pemesanan(data.getString("id_pemesanan"));
                                        md.setNama(data.getString("nama_pemesan"));
                                        md.setTgl(data.getString("tanggal_pemesanan"));
                                        md.setTotal(data.getString("total"));
                                        md.setNo(String.valueOf(i + 1));
                                        md.setIconprint(R.drawable.print);
                                        mTiket.add(md);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                tiketAdapter.notifyDataSetChanged();
                            } else if (res.optString("success").equals("2")) {
                                tiketKosong.setVisibility(View.VISIBLE);
                                rv.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(Tiket.this, res.getString("message"), Toast.LENGTH_SHORT).show();
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
                        startActivity(new Intent(Tiket.this, MainActivity.class));
                        Toast.makeText(Tiket.this, "pesan : Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> map = new HashMap<>();
                map.put("id_user", ID_USER);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(reqData);
    }
}
