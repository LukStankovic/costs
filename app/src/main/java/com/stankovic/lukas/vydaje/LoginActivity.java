package com.stankovic.lukas.vydaje;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stankovic.lukas.vydaje.Api.ApiReader.ApiReader;
import com.stankovic.lukas.vydaje.Api.ApiRequest.ApiLoginPostAsyncRequest;
import com.stankovic.lukas.vydaje.Api.ApiRequest.Base.ApiParamsBuilder;
import com.stankovic.lukas.vydaje.Api.ApiRequest.Base.ApiPostAsyncRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity {

    Button btnLogin;
    EditText etLoginName;
    EditText etPassword;

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean logged = settings.getBoolean("logged", false);

        if (logged) {
            Toast.makeText(LoginActivity.this, "Již jsi přihlášen", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        btnLogin = (Button)findViewById(R.id.btnLogin);
        etLoginName = (EditText)findViewById(R.id.etLoginName);
        etPassword = (EditText)findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginName = etLoginName.getText().toString();
                String password = etPassword.getText().toString();

                if (loginName.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Vyplň veškeré údaje", Toast.LENGTH_SHORT).show();
                } else {
                    validate(loginName, password);
                }
            }
        });

    }

    private void validate(String loginName, String password) {
        String hashedPassword = getMd5(password, "TAMZ2");
        HashMap<String, String> params = new HashMap<>();

        params.put("login", loginName);
        params.put("password", hashedPassword);
        ApiLoginPostAsyncRequest apiAsyncRequest = new ApiLoginPostAsyncRequest(this, settings);
        apiAsyncRequest.execute("users/login/", ApiParamsBuilder.buildParams(params));
    }

    public static String getMd5(String input, String salt)
    {
        input = salt + input;
        MessageDigest md = null;
        StringBuilder sb = null;
        try {
            md = MessageDigest.getInstance("MD5");

            byte[] hashInBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
