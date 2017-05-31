package com.example.alexander.imperiumromanum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/*
 *  Просто текстовая информация об игре
 */
public class Info extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
    }

    /*
     *  Обработчик кнопки: возврат на главную активность
     */
    public void backMain(View view)
    {
        finish();
    }
}


