package com.example.alexander.imperiumromanum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Info extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
    }

    public void backMain(View view) {
        finish();
    }
}


