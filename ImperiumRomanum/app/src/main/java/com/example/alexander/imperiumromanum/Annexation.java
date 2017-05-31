package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/*
 *  Присоединение новых земель с приобретением ресурсов
 *  Это случайное событие
 */
public class Annexation extends AppCompatActivity
{
    boolean annexEvent, heirEvent, diseaseEvent, corruptEvent, catchEvent;
    int year, money, gold, seed, land, slaves, army;
    int moneyTrophy, goldTrophy, seedTrophy, landTrophy, slavesTrophy, armyTrophy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // получение достоверностей возможных событий
        Bundle extras = getIntent().getExtras();
        annexEvent = extras.getBoolean("annexEvent");
        heirEvent = extras.getBoolean("heirEvent");
        diseaseEvent = extras.getBoolean("diseaseEvent");
        corruptEvent = extras.getBoolean("corruptEvent");
        catchEvent = extras.getBoolean("catchEvent");

        // если текущее событие не должно произойти, переход на активность следующего
        if(!annexEvent)
        {
            Intent intent = new Intent(this, Dynasty.class);
            // передача достоверностей возможных событий
            intent.putExtra("heirEvent", heirEvent);
            intent.putExtra("diseaseEvent", diseaseEvent);
            intent.putExtra("corruptEvent", corruptEvent);
            intent.putExtra("catchEvent", catchEvent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.annexation);

        // получение ресурсов из БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        Cursor result = myDb.getData();
        result.moveToNext();
        year = result.getInt(1);
        money = result.getInt(4);
        gold = result.getInt(5);
        seed = result.getInt(6);
        land = result.getInt(7);
        slaves = result.getInt(8);
        army = result.getInt(9);
        result.close();

        // определение значений приобретённых ресурсов
        Random rand = new Random();
        moneyTrophy = (rand.nextInt(year * money / 2 + 1));
        goldTrophy = (rand.nextInt(year * gold / 2 + 1));
        seedTrophy = (rand.nextInt(year * seed / 2 + 1));
        landTrophy = (rand.nextInt(year * land / 2 + 1));
        slavesTrophy = (rand.nextInt(year * slaves / 2 + 1));
        armyTrophy = (rand.nextInt(year * army / 2 + 1));

        // обновление ресурсов в БД
        myDb.updateResources(money + moneyTrophy, gold + goldTrophy, seed + seedTrophy,
                             land + landTrophy, slaves + slavesTrophy, army + armyTrophy);
        myDb.close();

        // получение текстовых полей
        TextView txtMoney = (TextView)findViewById(R.id.txt_money_trophy);
        TextView txtGold = (TextView)findViewById(R.id.txt_gold_trophy);
        TextView txtSeed = (TextView)findViewById(R.id.txt_seed_trophy);
        TextView txtLand= (TextView)findViewById(R.id.txt_land_trophy);
        TextView txtSlaves = (TextView)findViewById(R.id.txt_slaves_trophy);
        TextView txtArmy = (TextView)findViewById(R.id.txt_army_trophy);

        // заполнение текстовых полей
        txtMoney.setText(String.valueOf(moneyTrophy));
        txtGold.setText(String.valueOf(goldTrophy));
        txtSeed.setText(String.valueOf(seedTrophy));
        txtLand.setText(String.valueOf(landTrophy));
        txtSlaves.setText(String.valueOf(slavesTrophy));
        txtArmy.setText(String.valueOf(armyTrophy));
    }

    /*
     *  Обработчик кнопки: переход к следующему возможному случайному событию
     */
    public void annex(View view)
    {
        // переход на активность события "Наследник" вне зависимости от того, произойдёт ли оно
        Intent intent = new Intent(this, Dynasty.class);
        // передача достоверностей возможных событий
        intent.putExtra("heirEvent", heirEvent);
        intent.putExtra("diseaseEvent", diseaseEvent);
        intent.putExtra("corruptEvent", corruptEvent);
        intent.putExtra("catchEvent", catchEvent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
