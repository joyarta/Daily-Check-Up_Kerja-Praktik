package com.annisarahmah02.myapplication;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;


import androidx.appcompat.app.AppCompatActivity;

public class menu extends AppCompatActivity {

    private TextView lokasi;
    private String username;
    private Button ta,mk,pkj,tm,tkdm,back;
    private static final String PREFS_NAME = "AppPrefs";
    private static final String ACCESS_COUNT_KEY = "accessCount";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        lokasi = findViewById(R.id.tvlokasi);

        String email = getIntent().getStringExtra("email"); // Retrieve the email
        String selectedItem = getIntent().getStringExtra("selectedItem");

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int accessCount = sharedPreferences.getInt(email, 0); // Use 'email' as the key
        accessCount++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(email, accessCount); // Use 'email' as the key
        editor.apply();

        if (email != null && selectedItem != null) {
            String message = "Selamat datang <b>" + email + "</b> kamu sedang berada di <b>" + selectedItem + "</b><br>Saat ini anda sudah mengakses aplikasi ini <b>" + accessCount + "</b> kali";
            lokasi.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT));
        } else {
            lokasi.setText("Error displaying message.");
        }

        ta=findViewById(R.id.btnta);
        mk =findViewById(R.id.btnmk);
        tm=findViewById(R.id.btntm);
        pkj =findViewById(R.id.btnpk);
        tkdm=findViewById(R.id.btntkjp);
        back=findViewById(R.id.btnback);

        ta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), formrutin.class);
                intent.putExtra("selectedItem", selectedItem); // Pass the selected location
                startActivity(intent);
            }
        });

        mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, formrutin.class);
                startActivity(intent);
            }
        });

        pkj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, formrutin.class);
                startActivity(intent);
            }
        });

        tm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, formrutin.class);
                startActivity(intent);
            }
        });

        tkdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, formrutin.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, setlokasi.class);
                startActivity(intent);
            }
        });

    }
}