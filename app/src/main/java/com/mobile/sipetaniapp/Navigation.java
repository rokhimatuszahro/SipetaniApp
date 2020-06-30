package com.mobile.sipetaniapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.sipetaniapp.Helper.SharedPreferenceHelper;
import com.mobile.sipetaniapp.Helper.SharedPreferenceHelper;

public class Navigation extends AppCompatActivity {

    SharedPreferenceHelper sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BerandaFragment()).commit();
    }

    // Cek apakah belum login pada bagian akun
    @Override
    public void onStart(){
        super.onStart();
        sp = new SharedPreferenceHelper(this);

        if (!sp.getLogin(true)){
            startActivity(new Intent(Navigation.this, MainActivity.class));
            finish();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_beranda:
                    selectedFragment = new BerandaFragment();
                    break;
                case R.id.nav_sarana:
                    selectedFragment = new SaranaFragment();
                    break;
                case R.id.nav_akun:
                    selectedFragment = new AkunFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

            return true;
        }
    };
}
