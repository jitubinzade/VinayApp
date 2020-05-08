package com.org.vinayapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JITU on 18/04/2020.
 */
public class SessionManager {
    public static final String LOGINPRE="login_pref";

    Context context;
    public SessionManager(Context context ){
        this.context=context;
    }


    public void setLoginPreference(String userid,String role){

        SharedPreferences sharedPreferences=context.getSharedPreferences(LOGINPRE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("userid",userid);
        editor.putString("role",role);
        editor.commit();
    }

    public boolean isLogin(){

        SharedPreferences sharedPreferences=context.getSharedPreferences(LOGINPRE,Context.MODE_PRIVATE);
        String userid= sharedPreferences.getString("userid",null);
        if(userid!=null){
            return true;
        }
        return false;
    }
    public String getUserid(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(LOGINPRE,Context.MODE_PRIVATE);
        String userid= sharedPreferences.getString("userid",null);
        return userid;
    }

    public String getRole(){

        SharedPreferences sharedPreferences=context.getSharedPreferences(LOGINPRE,Context.MODE_PRIVATE);
        String role= sharedPreferences.getString("role",null);
        return role;
    }

    public void clearLogin(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(LOGINPRE,Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

}
