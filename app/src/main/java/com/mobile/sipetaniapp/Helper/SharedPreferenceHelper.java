package com.mobile.sipetaniapp.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    private SharedPreferences sharedPreferences;
    private Context context;
    public final static String email = "email", id_akses = "id_akses", id_user = "id_user", login = "login";

    public SharedPreferenceHelper(Context context){
        this.sharedPreferences = context.getSharedPreferences("logi_session", Context.MODE_PRIVATE);
        this.context = context;
    }

    public String getEmail(String s)
    {
        return sharedPreferences.getString(email, "");
    }

    public int getId_akses(int i)
    {
        return sharedPreferences.getInt(id_akses, 0);
    }

    public int getId_user(int i)
    {

        return sharedPreferences.getInt(id_user, 0);
    }

    public boolean getLogin(boolean b)
    {
        return sharedPreferences.getBoolean(login, false);
    }

    public void setEmail(String email)
    {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.email, email);
        edit.commit();
    }

    public void setId_akses(int id_akses)
    {
        SharedPreferences.Editor edit  = sharedPreferences.edit();
        edit.putInt(this.id_akses, id_akses);
        edit.commit();
    }

    public void setId_user(int id_user)
    {
        SharedPreferences.Editor edit  = sharedPreferences.edit();
        edit.putInt(this.id_user, id_user);
        edit.commit();
    }

    public void setLogin(boolean login)
    {
        SharedPreferences.Editor edit  = sharedPreferences.edit();
        edit.putBoolean(this.login, login);
        edit.commit();
    }

    public void logout()
    {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(email);
        edit.remove(id_user);
        edit.remove(id_akses);
        edit.remove(login);
        edit.commit();
    }
}
