package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/*
 *  Поимка коррупционера, частичный возврат похищенных денег
 */
public class CatchCorrupt extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catch_corrupt);

        // получение текстового поля
        TextView txtMoneyReturned = (TextView)findViewById(R.id.txt_money_returned);

        // получение ресурсов из БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        Cursor result = myDb.getData();
        result.moveToNext();
        int money = result.getInt(4);
        int stolen = result.getInt(14);
        result.close();

        // определение возвращённых в казну денег
        Random rand = new Random();
        int minReturned = (int)Math.round(stolen * 0.01);
        int returned = rand.nextInt(stolen - minReturned + 1) + minReturned;

        // обновление ресурсов в БД
        myDb.updateCorruptAndStolen(0, 0);
        myDb.updateMoney(money + returned);
        myDb.close();

        // заполнение текстового поля
        txtMoneyReturned.setText("      В императорскую казну были возвращены "
                                + String.valueOf(returned) + " денариев.");
    }

    /*
     *  Обработчик кнопки: переход на активность "Колизей"
     */
    public void catchThief(View view)
    {
        Intent intent = new Intent(this, Coliseum.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
