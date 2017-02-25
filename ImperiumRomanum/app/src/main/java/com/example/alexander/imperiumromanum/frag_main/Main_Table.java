package com.example.alexander.imperiumromanum.frag_main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.imperiumromanum.R;


public class Main_Table extends Fragment {
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View frag = inflater.inflate(R.layout.main_table,container,false);
        return frag;
    }
}