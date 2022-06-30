package com.abilitymap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tvText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        tvText = (TextView) findViewById(R.id.text);
//        tvText.setText(HelloKt.formatMessage("Android with Kotlin"));

    }
}