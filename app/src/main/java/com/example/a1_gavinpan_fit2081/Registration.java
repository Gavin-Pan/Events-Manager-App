package com.example.a1_gavinpan_fit2081;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void onRegisterButtonClick(View view) {

        EditText editTextUsernameRegister = findViewById(R.id.etUsernameRegister);
        EditText editTextPasswordRegister = findViewById(R.id.etPasswordRegister);
        EditText editTextConfirmPassword = findViewById(R.id.etConfirmPassword);

        String newUsernameString = editTextUsernameRegister.getText().toString();
        String newPasswordString = editTextPasswordRegister.getText().toString();
        String newConfirmedPasswordString = editTextConfirmPassword.getText().toString();

        if (validateRegistration(newUsernameString,newPasswordString, newConfirmedPasswordString)) {
            saveDataToSharedPreference(newUsernameString, newPasswordString);

            Toast.makeText(this, "Registration Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        } else {
                Toast.makeText(this,"Registration Failure", Toast.LENGTH_LONG).show();
            }
    }

    private void saveDataToSharedPreference(String usernameVal, String passwordVal) {
        // initialise shared preference class variable to access Android's persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // save key-value pairs to the shared preference file
        editor.putString(KeyStore.KEY_USERNAME, usernameVal);
        editor.putString(KeyStore.KEY_PASSWORD, passwordVal);
        // save data to the file asynchronously (in background without freezing the UI)
        editor.apply();
    }

    public boolean validateRegistration(String username, String password, String confirmedPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty()) {
            Toast.makeText(this, "Please enter registration details.",Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password.equals(confirmedPassword)) {
            Toast.makeText(this, "Passwords don't match",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }

    }

    public void onLoginButtonClick(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}