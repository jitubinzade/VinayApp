package com.org.vinayapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.org.vinayapp.fragment.HomeFragment;
import com.org.vinayapp.fragment.LoginFragment;
import com.org.vinayapp.utils.SessionManager;

public class MainActivity extends AppCompatActivity {


    Toolbar toolbar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        sessionManager= new SessionManager(this);
        if(sessionManager.isLogin()){
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content,new HomeFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content,new LoginFragment()).commit();
        }

    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()<1){

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Do you want to Exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
        else{
            getSupportFragmentManager().popBackStack();
        }
    }
}
