package com.annisarahmah02.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.annisarahmah02.myapplication.R;
import com.annisarahmah02.myapplication.menu;

public class setlokasi extends AppCompatActivity {

    private Spinner dropdown;
    private Button submitButton;
    private TextView welkum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setlokasi);

        dropdown = findViewById(R.id.spinner2);
        submitButton = findViewById(R.id.button2);
        welkum = findViewById(R.id.textView3);

        String email = getIntent().getStringExtra("email"); // Retrieve the email
        Log.d("DebugEmail", "Email: " + email);
        if (email != null) {
            welkum.setText("Hai " + email + " Silahkan pilih lokasi");
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.drop_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = dropdown.getSelectedItem().toString();
                Intent intent = new Intent(setlokasi.this, menu.class);
                intent.putExtra("selectedItem", selectedItem);
                intent.putExtra("email", email); // Pass the user's email
                startActivity(intent);
            }
        });
    }
}
