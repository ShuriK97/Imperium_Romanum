package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/*
 *  Боевые действия: оценка сил
 */
public class WarBattle extends AppCompatActivity
{
    boolean annexEvent, heirEvent, diseaseEvent, corruptEvent, catchEvent;
    int enemyPower, empirePower, enemyForces, army, slavesMobilised, killersAcquired;
    TextView txtBattleArmy, txtBattleSlaves, txtBattleKillers, txtBattleEnemies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.war_battle);

        // получение достоверностей возможных событий
        Bundle extras = getIntent().getExtras();
        annexEvent = extras.getBoolean("annexEvent");
        heirEvent = extras.getBoolean("heirEvent");
        diseaseEvent = extras.getBoolean("diseaseEvent");
        corruptEvent = extras.getBoolean("corruptEvent");
        catchEvent = extras.getBoolean("catchEvent");
        // получение информации о войне
        enemyForces = extras.getInt("enemy forces");
        army = extras.getInt("army");
        slavesMobilised = extras.getInt("slaves");
        killersAcquired = extras.getInt("killers");

        // расчёт сил, который будет влиять на исход войны
        Random rand = new Random();
        int minEnemyPower = (int)Math.round(enemyForces * 2 * 0.95);
        int maxEnemyPower = (int)Math.round(enemyForces * 2 * 1.05);
        enemyPower = rand.nextInt(maxEnemyPower - minEnemyPower + 1) + minEnemyPower;
        empirePower = army * 2 + slavesMobilised + killersAcquired;

        // нахождение текстовых полей
        txtBattleArmy = (TextView)findViewById(R.id.txt_battle_army);
        txtBattleSlaves = (TextView)findViewById(R.id.txt_battle_slaves);
        txtBattleKillers = (TextView)findViewById(R.id.txt_battle_killers);
        txtBattleEnemies = (TextView)findViewById(R.id.txt_battle_enemies);

        // заполнение текстовых полей
        txtBattleArmy.setText(String.valueOf(army) + " сотен легионеров");
        txtBattleSlaves.setText(String.valueOf(slavesMobilised) + " тыс. вооружённых рабов");
        txtBattleKillers.setText(String.valueOf(killersAcquired) + " сотен наёмников");
        txtBattleEnemies.setText(String.valueOf(enemyForces) + " тыс. варваров");
    }

    public void attack(View view)
    {
        if(empirePower >= enemyPower) // победа
        {
            // переход на активность "Победа"
            Intent intent = new Intent(this, BattleWin.class);
            // передача достоверностей возможных событий
            intent.putExtra("annexEvent", annexEvent);
            intent.putExtra("heirEvent", heirEvent);
            intent.putExtra("diseaseEvent", diseaseEvent);
            intent.putExtra("corruptEvent", corruptEvent);
            intent.putExtra("catchEvent", catchEvent);
            // передача информации о войне
            intent.putExtra("enemy forces", enemyForces);
            intent.putExtra("slaves", slavesMobilised);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else // поражение
        {
            // переход на активность "Поражение"
            Intent intent = new Intent(this, BattleLose.class);
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
}
