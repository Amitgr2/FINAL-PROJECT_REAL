package com.example.finalprojectamit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.finalprojectamit.R;
import com.example.finalprojectamit.models.AuthHelper;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    CheckBox cbRemember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidgets(); // connecting widgets
        new AuthHelper(this).ifRememberUserLogin(); //if the user is remembered -> logIN
    }



    public void initWidgets() { // connecting widgets
        etEmail = findViewById(R.id.tvAssists);
        etPassword = findViewById(R.id.etPassword);

        cbRemember = findViewById(R.id.cbRemember);
    }


    public void register(View view) { // moving to register activity
        startActivity(new Intent(this, RegisterActivity.class));
    }


    public void logIn(View view) { // login in
        new AuthHelper(this).login(etEmail.getText().toString().trim(),etPassword.getText().toString().trim(),cbRemember.isChecked()); // checks if the email and password are correct + if the user wants his user to be remembered
    }
}

