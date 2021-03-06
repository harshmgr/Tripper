package com.example.tripper.Common.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;

import com.example.tripper.Databases.SessionManager;
import com.example.tripper.LocationContributor.LocationContributorDashboard;
import com.example.tripper.R;

public class LocationContributorStartupScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_location_contributor);


        //Directly sent to dashboard if contributor value is stored in session
        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_REMEMBERME);
        if (sessionManager.checkLogin()){
            startActivity(new Intent(getApplicationContext(), LocationContributorDashboard.class));
        }

    }
    public void callLoginScreen(View view){

        Intent intent=new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
    }

    public void callSignUpScreen(View view){
        Intent intent=new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
    }
}