package com.example.alexander.imperiumromanum.frag_main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.imperiumromanum.R;

/*
 *  Нижний фрагмент главной активности (кнопки перехода)
 */
public class Main_Bottom extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.main_bottom,container,false);
    }
}