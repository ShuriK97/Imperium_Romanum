package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/*
 *  Смерть старого правителя и начало правления его наследника
 */
public class Death extends AppCompatActivity
{
    boolean corruptEvent, catchEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.death);

        // обновление информации о наследнике и годе его правления
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        myDb.updateAge(1);
        myDb.updateHeir(0);
        myDb.close();

        // получение достоверностей возможных событий
        Bundle extras = getIntent().getExtras();
        corruptEvent = extras.getBoolean("corruptEvent");
        catchEvent = extras.getBoolean("catchEvent");
    }

    /*
     *  Обработчик кнопки: переход на следующую активность
     *  Это может быть "Коррупция", "Поимка" или сразу "Колизей"
     */
    public void acceptDeath(View view)
    {
        Intent intent;
        if(corruptEvent)
            intent = new Intent(this, Corruption.class);
        else if(catchEvent)
            intent = new Intent(this, CatchCorrupt.class);
        else
            intent = new Intent(this, Coliseum.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
