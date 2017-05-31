package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

/*
 *  Формирование войска: дополнительно к армии можно завербовать рабов и наёмников
 */
public class WarBegin extends AppCompatActivity
{
    boolean annexEvent, heirEvent, diseaseEvent, corruptEvent, catchEvent;
    int enemyForces, money, slaves, army, happiness, happinessDecrease;
    int slavesToMobilise, slavesMobilised, killersPrice, killersToAcquire, killersAcquired, moneyLeft;
    TextView txtArmy, txtMobilSlaves, txtUnhappy, txtKillersAcquire, txtWarMoney, txtMobilise, txtKillers;
    SeekBar seekBarMobilise, seekBarKillers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.war_begin);

        // получение достоверностей возможных событий
        Bundle extras = getIntent().getExtras();
        annexEvent = extras.getBoolean("annexEvent");
        heirEvent = extras.getBoolean("heirEvent");
        diseaseEvent = extras.getBoolean("diseaseEvent");
        corruptEvent = extras.getBoolean("corruptEvent");
        catchEvent = extras.getBoolean("catchEvent");
        enemyForces = extras.getInt("enemy forces");

        // получение ресурсов из БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        Cursor result = myDb.getData();
        result.moveToNext();
        money = result.getInt(4);
        slaves = result.getInt(8);
        army = result.getInt(9);
        happiness = result.getInt(10);
        result.close();
        myDb.close();

        // получение текстовых полей
        txtArmy = (TextView)findViewById(R.id.txt_army);
        txtMobilSlaves = (TextView)findViewById(R.id.txt_mobil_slaves);
        txtUnhappy = (TextView)findViewById(R.id.txt_unhappy);
        txtKillersAcquire = (TextView)findViewById(R.id.txt_killers_acquire);
        txtWarMoney = (TextView)findViewById(R.id.txt_war_money);
        txtMobilise = (TextView)findViewById(R.id.txt_mobilise);
        txtKillers = (TextView)findViewById(R.id.txt_killers);

        // получение слайдеров
        seekBarMobilise = (SeekBar)findViewById(R.id.seek_bar_mobilise);
        seekBarKillers = (SeekBar)findViewById(R.id.seek_bar_killers);

        // определение возможного числа рабов и наёмников,
        // которых можно завербовать
        Random rand = new Random();
        int minSlavesToMobilise = (int)Math.round(slaves * 0.2);
        int maxSlavesToMobilise = (int)Math.round(slaves * 0.4);
        slavesToMobilise = rand.nextInt(maxSlavesToMobilise - minSlavesToMobilise + 1) + minSlavesToMobilise;
        slavesMobilised = 0;
        killersPrice = 500;
        killersToAcquire = money / killersPrice;
        killersAcquired = 0;

        // заполнение текстовых полей
        txtArmy.setText("      Численность Вашей армии составляет " + army + " сотен легионеров.");
        txtMobilSlaves.setText("      Вы можете мобилизировать " + slavesToMobilise
                              + " тыс. рабов, но в империи вырастет недовольство!");
        txtWarMoney.setText("      В казне " + String.valueOf(money) + " денариев.");
        txtKillers.setText("0");

        // настройка слайдеров
        seekBarMobilise.setMax(slavesToMobilise);
        seekBarKillers.setMax(killersToAcquire);
        seekBarMobilise.setProgress(0);
        seekBarKillers.setProgress(0);

        /*
         *  Обработчик положения слайдера: мобилизация рабов
         */
        seekBarMobilise.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                slavesMobilised = progress;
                txtMobilise.setText(String.valueOf(slavesMobilised));
                int slavesPercent = slavesMobilised * 100 / slavesToMobilise;
                happinessDecrease = slavesPercent / 10; // 10% завербованных рабов понижает довольство на 1%
                txtUnhappy.setText("      Недовольство вырастет на " + String.valueOf(happinessDecrease) + "%.");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        /*
         *  Обработчик положения слайдера: вербовка наёмников
         */
        seekBarKillers.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                killersAcquired = progress;
                txtKillers.setText(String.valueOf(killersAcquired));
                moneyLeft = money - killersPrice * killersAcquired;
                txtWarMoney.setText("      В казне " + String.valueOf(moneyLeft) + " денариев.");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    /*
     *  Обработчик кнопки: переход к боевым действиям
     */
    public void mobilise(View view)
    {
        // денег может не хватать
        if(moneyLeft < 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Нехватка денег");
            builder.setMessage("Вы не можете позволить себе завербовать столько наёмников.");
            builder.show();
            return;
        }

        // перерасчёт денег и настроения
        money = moneyLeft;
        happiness -= happinessDecrease;

        // обновление количества денег и настроения в БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        myDb.updateHappiness(happiness);
        myDb.updateMoney(money);
        myDb.close();

        // переход к боевым действиям
        Intent intent = new Intent(this, WarBattle.class);
        // передача достоверностей возможных событий
        intent.putExtra("annexEvent", annexEvent);
        intent.putExtra("heirEvent", heirEvent);
        intent.putExtra("diseaseEvent", diseaseEvent);
        intent.putExtra("corruptEvent", corruptEvent);
        intent.putExtra("catchEvent", catchEvent);
        // передача информации о войне
        intent.putExtra("enemy forces", enemyForces);
        intent.putExtra("army", army);
        intent.putExtra("slaves", slavesMobilised);
        intent.putExtra("killers", killersAcquired);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
