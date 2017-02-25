package com.example.alexander.imperiumromanum.frag_main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.imperiumromanum.Info;
import com.example.alexander.imperiumromanum.R;


public class Main_Top extends Fragment {
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View frag = inflater.inflate(R.layout.main_top,container,false);
        return frag;
    }

    public void Info(View view) {
        Intent intent = new Intent(Main_Top.this,Info.class);
        startActivity(intent);
    }
}
