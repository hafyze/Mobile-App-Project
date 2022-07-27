package com.ebookfrenzy.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick() {
        TextView username = findViewById(R.id.userName);
        TextView password = findViewById(R.id.password);

        Button loginBtn = findViewById(R.id.buttonLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //admin and admin


}