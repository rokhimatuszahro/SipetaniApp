package com.mobile.sipetaniapp;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profil extends AppCompatActivity {

    private String gambar = "";
    TextView email, nama, pin, jenkel;
    EditText inpNama, inpEmail, inpPin, inpPassBaru, inpPassLama;
    RadioGroup groupJenkel;
    RadioButton Jenkel, Rb_Laki, Rb_Perempuan;
    CircleImageView ftprofile;
    ImageView newftprofile, iconImageProfile;
    Bitmap bitmap;
    Button btnEdit;
    SharedPreferenceHelper sp;
    ProgressDialog pd;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        email = (TextView)findViewById(R.id.isiEmailProfil);
        nama = (TextView)findViewById(R.id.isiNamaProfil);
        pin = (TextView)findViewById(R.id.isiPinProfil);
        jenkel = (TextView)findViewById(R.id.isiJenkelProfil);
        inpEmail = (EditText) findViewById(R.id.emailEdit);
        inpNama = (EditText) findViewById(R.id.namaEdit);
        inpPin = (EditText) findViewById(R.id.pinEdit);
        inpPassBaru = (EditText) findViewById(R.id.passBaruEdit);
        inpPassLama = (EditText) findViewById(R.id.passLamaEdit);
        Rb_Laki = (RadioButton) findViewById(R.id.rblakilaki);
        Rb_Perempuan = (RadioButton) findViewById(R.id.rbperempuan);
        ftprofile = (CircleImageView) findViewById(R.id.imgFotoProfil2);
        newftprofile = (ImageView) findViewById(R.id.imgFtProfil);
        iconImageProfile = (ImageView) findViewById(R.id.iconImageProfile);
        sp = new SharedPreferenceHelper(Profil.this);
        pd = new ProgressDialog(Profil.this);
        dialog = new AlertDialog.Builder(this);

        btnEdit = (Button)findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupJenkel = (RadioGroup) findViewById(R.id.jenKelEdit);
                int RadioButtonId = groupJenkel.getCheckedRadioButtonId();
                Jenkel = (RadioButton)findViewById(RadioButtonId);
                String ID_USER = String.valueOf(sp.getId_user(0));
                String EMAIL = sp.getEmail("email");
                String InpEMAIL = inpEmail.getText().toString();
                String InpNAMA = inpNama.getText().toString();
                String InpPIN = inpPin.getText().toString();
                String InpJENKEL = Jenkel.getText().toString();
                String InpPassLama = inpPassLama.getText().toString();
                String InpPassBaru = inpPassBaru.getText().toString();

                if (validasi(InpEMAIL, InpNAMA, InpPIN, InpPassLama, InpPassBaru) == true){
                    editprofile(ID_USER, EMAIL,InpEMAIL, InpNAMA, InpPIN, InpJENKEL, InpPassLama, InpPassBaru);
                }
            }
        });

        newftprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    protected boolean validasi(String InpEMAIL, String InpNAMA, String InpPIN, String InpPassLama, String InpPassBaru) {
        if (InpEMAIL.isEmpty()){
            inpEmail.setError("Email kosong, isikan Email Anda!");
            inpEmail.requestFocus();
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(InpEMAIL).matches()){
            inpEmail.setError("Format Email salah, isikan Email Anda!");
            inpEmail.requestFocus();
            return false;
        }else if (InpNAMA.isEmpty()){
            inpNama.setError("Nama tidak boleh kosong!");
            inpNama.requestFocus();
            return false;
        }else if (InpPIN.isEmpty()){
            inpPin.setError("PIN tidak boleh kosong!");
            inpPin.requestFocus();
            return false;
        }else if (InpPIN.length() != 3){
            inpPin.setError("PIN harus berjumlah 3 digit!");
            inpPin.requestFocus();
            return false;
        }else if (!InpPassLama.isEmpty()){
            if (InpPassBaru.isEmpty()){
                inpPassBaru.setError("Password baru tidak boleh kosong!", null);
                inpPassBaru.requestFocus();
                return false;
            }else if (InpPassBaru.length() < 4 || InpPassBaru.length() > 8){
                inpPassBaru.setError("Password baru harus 4 - 8 digit!", null);
                inpPassBaru.requestFocus();
                return false;
            }else{
                return true;
            }
        }else if (!InpPassBaru.isEmpty()){
            if (InpPassBaru.isEmpty()){
                inpPassBaru.setError("Password baru tidak boleh kosong!", null);
                inpPassBaru.requestFocus();
                return false;
            }else if (InpPassBaru.length() < 4 || InpPassBaru.length() > 8){
                inpPassBaru.setError("Password baru harus 4 - 8 digit!", null);
                inpPassBaru.requestFocus();
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        detail_profile();
    }

    private void detail_profile(){
        final String Email = sp.getEmail("email");

        pd.setMessage("Proses load Profile...");
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
                                        .into(ftprofile);
                                if (data.optString("jenkel").equals(Rb_Laki.getText().toString())) {
                                    Rb_Laki.setChecked(true);
                                } else {
                                    Rb_Perempuan.setChecked(true);
                                }
                                email.setText(data.getString("email"));
                                nama.setText(data.getString("nama"));
                                pin.setText(data.getString("pin"));
                                jenkel.setText(data.getString("jenkel"));
                                inpEmail.setText(data.getString("email"));
                                inpNama.setText(data.getString("nama"));
                                inpPin.setText(data.getString("pin"));
                            } else {
                                startActivity(new Intent(Profil.this, Navigation.class));
                                Toast.makeText(Profil.this, res.getString("message"), Toast.LENGTH_LONG).show();
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
                        startActivity(new Intent(Profil.this, MainActivity.class));
                        Toast.makeText(Profil.this, "pesan : Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", Email);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }

    private void editprofile(final String ID_USER, final String EMAIL, final String InpEMAIL, final String InpNAMA, final String InpPIN, final String InpJENKEL, final String InpPassLama, final String InpPassBaru){
        pd.setMessage("Proses Edit Profile...");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_EDIT_PROFILE,
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
                                        iconImageProfile.setVisibility(View.VISIBLE);
                                        newftprofile.setImageBitmap(null);
                                        detail_profile();
                                    }
                                });
                                dialog.show();
                            } else if (res.optString("success").equals("2")) {
                                Profil.this.inpPassLama.setError(res.getString("message"), null);
                                Profil.this.inpPassLama.requestFocus();
                            } else if (res.optString("success").equals("3")) {
                                dialog.setTitle(res.getString("title"));
                                dialog.setCancelable(false);
                                dialog.setMessage(res.getString("message"));
                                dialog.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sp.logout();
                                        startActivity(new Intent(Profil.this, MainActivity.class));
                                        finish();
                                    }
                                });
                                dialog.show();
                            }else{
                                dialog.setTitle(res.getString("title"));
                                dialog.setCancelable(false);
                                dialog.setMessage(res.getString("message"));
                                dialog.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sp.logout();
                                        startActivity(new Intent(Profil.this, MainActivity.class));
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
                        startActivity(new Intent(Profil.this, MainActivity.class));
                        Toast.makeText(Profil.this, "pesan : Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> map = new HashMap<>();
                map.put("id_user", ID_USER);
                map.put("email", EMAIL);
                map.put("passlama", InpPassLama);
                map.put("passbaru", InpPassBaru);
                map.put("inpemail", InpEMAIL);
                map.put("inpnama", InpNAMA);
                map.put("inppin", InpPIN);
                map.put("inpjenkel", InpJENKEL);
                map.put("foto", gambar);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(sendData);
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

            if (requestCode == 1){
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    bitmap = getResizedBitmap(bitmap, 720);
                    iconImageProfile.setVisibility(View.GONE);
                    newftprofile.setImageBitmap(bitmap);
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
}

    }
}
