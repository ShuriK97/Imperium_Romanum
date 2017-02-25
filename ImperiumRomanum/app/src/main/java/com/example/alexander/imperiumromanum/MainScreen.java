package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
    }

    public void toForum(View view) {
        Intent intent=new Intent(MainScreen.this,Forum.class);
        startActivity(intent);
    }

    public void timeFar(View view) {
        Intent intent = new Intent(MainScreen.this,Ambar.class);
        startActivity(intent);
    }

    public void Exit(View view) {
        finish();
    }
}
