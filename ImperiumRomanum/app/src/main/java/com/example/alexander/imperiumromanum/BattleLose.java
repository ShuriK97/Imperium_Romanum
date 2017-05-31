package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/*
 *  Поражение в войне, потеря ресурсов
 */
public class BattleLose extends AppCompatActivity
{
    boolean annexEvent, heirEvent, diseaseEvent, corruptEvent, catchEvent;
    int money, gold, seed, land, slaves, army;
    int moneyLoss, goldLoss, seedLoss, landLoss, slavesLoss, armyLoss;
    TextView txtMoneyWarLoss, txtGoldWarLoss, txtSeedWarLoss, txtLandWarLoss,
             txtSlavesWarLoss, txtArmyWarLoss;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_lose);

        // получение достоверностей возможных событий
        Bundle extras = getIntent().getExtras();
        annexEvent = extras.getBoolean("annexEvent");
        heirEvent = extras.getBoolean("heirEvent");
        diseaseEvent = extras.getBoolean("diseaseEvent");
        corruptEvent = extras.getBoolean("corruptEvent");
        catchEvent = extras.getBoolean("catchEvent");

        // получение ресурсов из БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        Cursor result = myDb.getData();
        result.moveToNext();
        money = result.getInt(4);
        gold = result.getInt(5);
        seed = result.getInt(6);
        land = result.getInt(7);
        slaves = result.getInt(8);
        army = result.getInt(9);
        result.close();

        // получение текстовых полей
        txtMoneyWarLoss = (TextView)findViewById(R.id.txt_money_war_loss);
        txtGoldWarLoss = (TextView)findViewById(R.id.txt_gold_war_loss);
        txtSeedWarLoss = (TextView)findViewById(R.id.txt_seed_war_loss);
        txtLandWarLoss = (TextView)findViewById(R.id.txt_land_war_loss);
        txtSlavesWarLoss = (TextView)findViewById(R.id.txt_slaves_war_loss);
        txtArmyWarLoss = (TextView)findViewById(R.id.txt_army_war_loss);

        // определение минимальных и максимальных возможных потерь
        Random rand = new Random();
        int minArmyLoss = (int)Math.round(army * 0.2);
        int maxArmyLoss = (int)Math.round(army * 0.7);
        int minSlavesLoss = (int)Math.round(slaves * 0.1);
        int maxSlavesLoss = (int)Math.round(slaves * 0.4);
        int maxMoneyLoss = (int)Math.round(money * 0.1);
        int maxGoldLoss = (int)Math.round(gold * 0.1);
        int maxSeedLoss = (int)Math.round(seed * 0.2);
        int maxLandLoss = (int)Math.round(land * 0.2);

        // определение потерь
        armyLoss = rand.nextInt(maxArmyLoss - minArmyLoss + 1) + minArmyLoss;
        slavesLoss = rand.nextInt(maxSlavesLoss - minSlavesLoss + 1) + minSlavesLoss;
        moneyLoss = rand.nextInt(maxMoneyLoss + 1);
        goldLoss = rand.nextInt(maxGoldLoss + 1);
        seedLoss = rand.nextInt(maxSeedLoss + 1);
        landLoss = rand.nextInt(maxLandLoss + 1);

        // заполнение текстовых полей
        txtMoneyWarLoss.setText(String.valueOf(moneyLoss));
        txtGoldWarLoss.setText(String.valueOf(goldLoss));
        txtSeedWarLoss.setText(String.valueOf(seedLoss));
        txtLandWarLoss.setText(String.valueOf(landLoss));
        txtSlavesWarLoss.setText(String.valueOf(slavesLoss));
        txtArmyWarLoss.setText(String.valueOf(armyLoss));

        // обновление ресурсов в БД
        myDb.updateResources(money - moneyLoss, gold - goldLoss, seed - seedLoss,
                land - landLoss, slaves - slavesLoss, army - armyLoss);
        myDb.close();
    }

    /*
     *  Обработчик кнопки: переход к следующему возможному случайному событию
     */
    public void acceptLosses(View view)
    {
        // переход на активность события "Присоединение" вне зависимости от того, произойдёт ли оно
        Intent intent = new Intent(this, Annexation.class);
        // передача достоверностей возможных событий
        intent.putExtra("annexEvent", annexEvent);
        intent.putExtra("heirEvent", heirEvent);
        intent.putExtra("diseaseEvent", diseaseEvent);
        intent.putExtra("corruptEvent", corruptEvent);
        intent.putExtra("catchEvent", catchEvent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
