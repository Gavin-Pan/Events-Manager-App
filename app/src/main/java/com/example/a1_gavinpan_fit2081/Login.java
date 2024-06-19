package com.example.a1_gavinpan_fit2081;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText etUsernameLogin,etPasswordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsernameLogin = findViewById(R.id.etUsernameLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);

        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString(KeyStore.KEY_USERNAME, "");

        etUsernameLogin.setText(savedUsername);


    }

    public void onLoginButtonClick(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, Context.MODE_PRIVATE);

        String savedUsername = sharedPreferences.getString(KeyStore.KEY_USERNAME, "");
        String savedPassword = sharedPreferences.getString(KeyStore.KEY_PASSWORD, "");
        String inputUsername = etUsernameLogin.getText().toString();
        String inputPassword = etPasswordLogin.getText().toString();

        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Username and Password is required!", Toast.LENGTH_SHORT).show();
            return; // return the control and do not proceed further
        }

        if (inputUsername.equals(savedUsername) && inputPassword.equals(savedPassword)) {
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Authentication failure: Username or Password incorrect",Toast.LENGTH_LONG).show();
        }
    }

    public void onRegisterButtonClick(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }
}