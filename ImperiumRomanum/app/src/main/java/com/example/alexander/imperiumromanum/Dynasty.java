package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/*
 *  Появление наследника правителя
 */
public class Dynasty extends AppCompatActivity
{
    boolean heirEvent, diseaseEvent, corruptEvent, catchEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // получение достоверностей возможных событий
        Bundle extras = getIntent().getExtras();
        heirEvent = extras.getBoolean("heirEvent");
        diseaseEvent = extras.getBoolean("diseaseEvent");
        corruptEvent = extras.getBoolean("corruptEvent");
        catchEvent = extras.getBoolean("catchEvent");

        // если текущее событие не должно произойти, переход на активность следующего
        if(!heirEvent)
        {
            Intent intent = new Intent(this, Disease.class);
            // передача достоверностей возможных событий
            intent.putExtra("diseaseEvent", diseaseEvent);
            intent.putExtra("corruptEvent", corruptEvent);
            intent.putExtra("catchEvent", catchEvent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.dynasty);

        // обновление информации о наследнике
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        myDb.updateHeir(1);
        myDb.close();
    }

    /*
     *  Обработчик кнопки: переход к следующему возможному случайному событию
     */
    public void getHeir(View view)
    {
        Intent intent = new Intent(this, Disease.class);
        // передача достоверностей возможных событий
        intent.putExtra("diseaseEvent", diseaseEvent);
        intent.putExtra("corruptEvent", corruptEvent);
        intent.putExtra("catchEvent", catchEvent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
