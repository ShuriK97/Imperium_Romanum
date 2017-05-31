package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/*
 *  Победа в войне, получение трофеев
 */
public class BattleWin extends AppCompatActivity
{
    boolean annexEvent, heirEvent, diseaseEvent, corruptEvent, catchEvent;
    int money, gold, seed, land, slaves, army, enemyForces, slavesMobilised;
    int moneyTrophy, goldTrophy, seedTrophy, landTrophy, slavesTrophy;
    int armyLoss, slavesLoss;
    TextView txtMoneyWarTrophy, txtGoldWarTrophy, txtSeedWarTrophy, txtLandWarTrophy,
             txtSlavesWarTrophy, txtWarLosses;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_win);

        // получение достоверностей возможных событий
        Bundle extras = getIntent().getExtras();
        annexEvent = extras.getBoolean("annexEvent");
        heirEvent = extras.getBoolean("heirEvent");
        diseaseEvent = extras.getBoolean("diseaseEvent");
        corruptEvent = extras.getBoolean("corruptEvent");
        catchEvent = extras.getBoolean("catchEvent");
        // получение информации о войне
        enemyForces = extras.getInt("enemy forces");
        slavesMobilised = extras.getInt("slaves");

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
        txtMoneyWarTrophy = (TextView)findViewById(R.id.txt_money_war_trophy);
        txtGoldWarTrophy = (TextView)findViewById(R.id.txt_gold_war_trophy);
        txtSeedWarTrophy = (TextView)findViewById(R.id.txt_seed_war_trophy);
        txtLandWarTrophy = (TextView)findViewById(R.id.txt_land_war_trophy);
        txtSlavesWarTrophy = (TextView)findViewById(R.id.txt_slaves_war_trophy);
        txtWarLosses = (TextView)findViewById(R.id.txt_war_losses);

        // определение минимальных и максимальных возможных трофеев и военных потерь
        Random rand = new Random();
        int maxMoneyTrophy = enemyForces * 1000;
        int maxSeedTrophy = enemyForces * 1000;
        int maxLandTrophy = enemyForces * 5;
        int maxSlavesTrophy = enemyForces * 5;
        int maxGoldTrophy = enemyForces;
        int minArmyLoss = Math.min(army, (int)Math.round(enemyForces * 0.05));
        int maxArmyLoss = Math.min(army, (int)Math.round(enemyForces * 0.4));
        int minSlavesLoss = Math.min(slavesMobilised, (int)Math.round(enemyForces * 0.05));
        int maxSlavesLoss = Math.min(slavesMobilised, (int)Math.round(enemyForces * 0.5));

        // определение трофеев и потерь
        moneyTrophy = rand.nextInt(maxMoneyTrophy + 1);
        seedTrophy = rand.nextInt(maxSeedTrophy + 1);
        landTrophy = rand.nextInt(maxLandTrophy + 1);
        slavesTrophy = rand.nextInt(maxSlavesTrophy + 1);
        goldTrophy = rand.nextInt(maxGoldTrophy + 1);
        armyLoss = rand.nextInt(maxArmyLoss - minArmyLoss + 1) + minArmyLoss;
        slavesLoss = rand.nextInt(maxSlavesLoss - minSlavesLoss + 1) + minSlavesLoss;

        // заполнение текстовых полей
        txtMoneyWarTrophy.setText(String.valueOf(moneyTrophy));
        txtSeedWarTrophy.setText(String.valueOf(seedTrophy));
        txtLandWarTrophy.setText(String.valueOf(landTrophy));
        txtSlavesWarTrophy.setText(String.valueOf(slavesTrophy));
        txtGoldWarTrophy.setText(String.valueOf(goldTrophy));
        txtWarLosses.setText("В ходе войны Вы потеряли " + String.valueOf(armyLoss)
                            + " сотен легионеров и " + String.valueOf(slavesLoss)
                            + " тысяч душ рабов" );

        // обновление ресурсов в БД
        myDb.updateResources(money + moneyTrophy, gold + goldTrophy, seed + seedTrophy,
                land + landTrophy, slaves + slavesTrophy - slavesLoss, army - armyLoss);
        myDb.close();
    }

    /*
     *  Обработчик кнопки: переход к следующему возможному случайному событию
     */
    public void getTrophies(View view)
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
