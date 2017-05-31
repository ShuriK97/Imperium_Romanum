package com.example.alexander.imperiumromanum;

import  android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/*
 *  Колизей: выделение денег на повышение настроения
 */
public class Coliseum extends AppCompatActivity
{
    int money, gold, seed, land, slaves, army, happiness, totalCapital;
    int goldPrice, seedPrice, landPrice, slavesPrice, armyPrice;
    int coliseumMoney, happinessIncrease, maxColiseumMoney;
    TextView txtColiseumHappiness, txtColiseumMoney;
    SeekBar seekBarColiseum;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coliseum);

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
        happiness = result.getInt(10);
        result.close();
        myDb.close();

        // получение текстовых полей
        txtColiseumHappiness = (TextView)findViewById(R.id.txt_coliseum_happiness);
        txtColiseumMoney = (TextView)findViewById(R.id.txt_coliseum_money);
        seekBarColiseum = (SeekBar)findViewById(R.id.seek_bar_coliseum);

        // цены ресурсов по 100% курсу
        goldPrice = 1000;
        seedPrice = 10;
        landPrice = 500;
        slavesPrice = 50;
        armyPrice = 100;

        // подсчёт стоимости всего капитала
        totalCapital = gold * goldPrice + seed * seedPrice + land * landPrice
                + seed * seedPrice + slaves * slavesPrice + army * armyPrice + money;

        // Вообще тут уровень настроения может быть и ниже 0,
        // чтобы особо запущенное состояние империи нельзя было спасти только
        // мероприятиями на Колизее.
        // Поэтому позже в БД сохраним истинное значение, которое может быть < 0,
        // но в текстовом поле отрицательные значения заменим нулём.
        int displayedHappiness = happiness;
        if(happiness < 0)
            displayedHappiness = 0;

        // заполнение текстового поля
        txtColiseumHappiness.setText("      Уровень довольства в Вашей империи "
                                    + String.valueOf(displayedHappiness) + "%");

        // определение максимума денег, которые можно выделить
        maxColiseumMoney = totalCapital * 11 / 2000; // максимум можно поднять на 10%

        // начальные состояния переменных
        happinessIncrease = -1;
        coliseumMoney = 0;

        // состояние слайдера
        seekBarColiseum.setMax(maxColiseumMoney);
        seekBarColiseum.setProgress(0);

        /*
         *  Обработчик положения слайдера выделения денег на колизей
         */
        seekBarColiseum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                coliseumMoney = progress;
                txtColiseumMoney.setText(String.valueOf(coliseumMoney));
                // количество денег, которые повысят настроение на 1%
                int moneyIncreaseOnOnePercent = maxColiseumMoney / 11;
                // процент повышения настроения
                happinessIncrease = (int)Math.round((double)coliseumMoney / moneyIncreaseOnOnePercent) - 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    /*
     *  Обработчик кнопки: переход к результатам проведения игр на Колизее
     */
    public void toColiseumEvent(View view)
    {
        if(coliseumMoney > money)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Как вы расточительны!");
            builder.setMessage("На такие мероприятия даже в казне нет денег.");
            builder.show();
            return;
        }
        // пересчёт ресурсов
        money -= coliseumMoney;
        happiness += happinessIncrease; // настоящее настроение может быть < 0
        if(happiness > 200)
            happiness = 200; // максимум 200

        // запись ресурсов в БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        myDb.updateHappiness(happiness);
        myDb.updateMoney(money);
        myDb.close();

        // переход на следующую активность
        Intent intent = new Intent(this, ColiseumEvent.class);
        // передача повышения настроения
        intent.putExtra("happiness increase", happinessIncrease);
        startActivity(intent);
    }
}
