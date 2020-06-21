package com.mobile.sipetaniapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AkunFragment extends Fragment {
    TextView Akunemail, Akunnama;
    CardView profil, detailpemesanan, keluar, tiket;
    CircleImageView imgprofile;
    ProgressDialog pd;
    AlertDialog.Builder dialog;
    SharedPreferenceHelper sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewroot = inflater.inflate(R.layout.fragment_akun, container, false);

        imgprofile = (CircleImageView) viewroot.findViewById(R.id.imgprofil);
        Akunemail = (TextView) viewroot.findViewById(R.id.akunEmail);
        Akunnama = (TextView) viewroot.findViewById(R.id.akunUsername);
        pd = new ProgressDialog(getActivity());
        dialog = new AlertDialog.Builder(getActivity());
        sp = new SharedPreferenceHelper(getActivity());

        profil = (CardView) viewroot.findViewById(R.id.akunProfil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Profil.class));
            }
        });

        detailpemesanan = (CardView) viewroot.findViewById(R.id.akunDetailPemesanan);
        detailpemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DetailPemesanan.class));
            }
        });

        keluar = (CardView) viewroot.findViewById(R.id.logout);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Proses Logout...");
                pd.setCancelable(false);
                pd.show();
                logout();
            }
        });

        tiket = (CardView) viewroot.findViewById(R.id.akunTiket);
        tiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Tiket.class));
            }
        });

        return viewroot;
    }

    private void logout() {
        final String Id_User = String.valueOf(sp.getId_user(0));

        pd.setMessage("Proses Logout...");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.optString("success").equals("1")) {
                                sp.logout();
                                dialog.setTitle(res.getString("title"));
                                dialog.setCancelable(false);
                                dialog.setMessage(res.getString("message"));
                                dialog.setPositiveButton("Terima Kasih", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                        getActivity().onBackPressed();
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
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        Toast.makeText(getActivity(), "pesan : Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_user", Id_User);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }

    @Override
    public void onStart(){
        super.onStart();
        detail_profile();
    }

    private void detail_profile(){
        final String Email = sp.getEmail("email");

        pd.setMessage("Proses Load Profile...");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.optString("success").equals("1")) {
                                JSONObject data = new JSONObject(res.getString("profile"));
                                Picasso.get().load(ServerAPI.URL_FOTO_PROFILE+res.getString("foto"))
                                        .placeholder(R.drawable.ftprofil)
                                        .into(imgprofile);
                                Akunemail.setText(data.getString("email"));
                                Akunnama.setText(data.getString("nama"));
                            } else {
                                startActivity(new Intent(getActivity(), Navigation.class));
                                Toast.makeText(getActivity(), res.getString("message"), Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
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
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        Toast.makeText(getActivity(), "pesan : Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> map = new HashMap<>();
                map.put("email", Email);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }
}
