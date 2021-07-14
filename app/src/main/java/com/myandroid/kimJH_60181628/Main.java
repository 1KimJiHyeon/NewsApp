package com.myandroid.kimJH_60181628;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {
    ImageButton Politics, Economy,Society, IT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Politics = (ImageButton) findViewById(R.id.Politics);
        Economy = (ImageButton) findViewById(R.id.Economy);
        Society = (ImageButton) findViewById(R.id.Society);
        IT = (ImageButton) findViewById(R.id.IT);

        Politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListPolitics.class);
                startActivity(intent);
            }
        });

        Economy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListEconomy.class);
                startActivity(intent);
            }
        });

        Society.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListSociety.class);
                startActivity(intent);
            }
        });

        IT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListIT.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}