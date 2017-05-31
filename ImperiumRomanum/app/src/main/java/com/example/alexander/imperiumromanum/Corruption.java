package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/*
 *  Кража денег из казны
 */
public class Corruption extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corruption);

        // получение текстового поля
        TextView txtStolen = (TextView)findViewById(R.id.txt_stolen);

        // получение ресурсов из БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        Cursor result = myDb.getData();
        result.moveToNext();
        int money = result.getInt(4);
        result.close();

        // определение минимальной и максимальной возможной кражи
        Random rand = new Random();
        int minStolen = (int)Math.round(money * 0.05);
        int maxStolen = (int)Math.round(money * 0.5);

        // фактическая кража
        int stolen = rand.nextInt(maxStolen - minStolen + 1) + minStolen;

        // обновление ресурсов в БД
        myDb.updateCorruptAndStolen(5, stolen); // счётчик на 5 лет для поимки
        myDb.updateMoney(money - stolen);
        myDb.close();

        // заполнение текстового поля
        txtStolen.setText("      Он похитил из императорской казны " + String.valueOf(stolen)
                         + " денариев и скрылся.");
    }

    /*
     *  Обработчик кнопки: переход на активность "Колизей"
     */
    public void corrupt(View view)
    {
        Intent intent = new Intent(this, Coliseum.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
