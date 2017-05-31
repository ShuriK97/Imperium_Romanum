package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

/*
 *  Выделение зерна на питание гражданам и на засев полей
 *  Также тут устанавливаются вероятности появления случайных событий
 */
public class Ambar extends AppCompatActivity
{
    int money, gold, seed, land, slaves, army, happiness, seedLeft, happinessDecrease,
        age, heir, corrupt, stolen;
    int bread, breadPercent, seeding, seedingPercent, slavesBusyPercent;
    boolean warEvent, annexEvent, heirEvent, diseaseEvent, corruptEvent, catchEvent;
    TextView txtSeedRemains, txtSeedBread, txtSeeding, txtSeedBreadPercent,
             txtSeedingPercent, txtSlavesBusy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambar);

        // нахождение текстовых полей и слайдеров
        txtSeedRemains = (TextView)findViewById(R.id.txt_seed_remains);
        txtSeedBread = (TextView)findViewById(R.id.txt_seed_bread);
        txtSeeding = (TextView)findViewById(R.id.txt_seeding);
        txtSeedBreadPercent = (TextView)findViewById(R.id.txt_seed_bread_percent);
        txtSeedingPercent = (TextView)findViewById(R.id.txt_seeding_percent);
        txtSlavesBusy = (TextView)findViewById(R.id.txt_slaves_busy);
        TextView txtAmbarSeed = (TextView)findViewById(R.id.txt_ambar_seed);
        TextView txtAmbarLand = (TextView)findViewById(R.id.txt_ambar_land);
        TextView txtAmbarSlaves = (TextView)findViewById(R.id.txt_ambar_slaves);
        TextView txtAmbarArmy = (TextView)findViewById(R.id.txt_ambar_army);
        SeekBar seekBarBread = (SeekBar)findViewById(R.id.seek_bar_bread);
        SeekBar seekBarSeeding = (SeekBar)findViewById(R.id.seek_bar_seeding);

        // получение ресурсов из БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        Cursor result = myDb.getData();
        result.moveToNext();
        age = result.getInt(2);
        heir = result.getInt(3);
        money = result.getInt(4);
        gold = result.getInt(5);
        seed = result.getInt(6);
        land = result.getInt(7);
        slaves = result.getInt(8);
        army = result.getInt(9);
        happiness = result.getInt(10);
        corrupt = result.getInt(11);
        stolen = result.getInt(14);
        result.close();
        myDb.close();

        // начальные значения переменных, связанных с распределением зерна
        seedLeft = seed;
        bread = 0;
        breadPercent = 0;
        seeding = 0;
        seedingPercent = 0;
        happinessDecrease = 100;
        slavesBusyPercent = 0;

        // начальное положение слайдеров
        seekBarBread.setProgress(breadPercent);
        seekBarSeeding.setProgress(seedingPercent);

        // изначальное заполнение текстовых полей
        txtSeedRemains.setText("После распределения остаётся " + String.valueOf(seed)
                              + " тонн зерна.");
        txtSeedBread.setText(String.valueOf(bread));
        txtSeedBreadPercent.setText(String.valueOf(breadPercent) + "%");
        txtSeeding.setText(String.valueOf(seeding));
        txtSeedingPercent.setText(String.valueOf(seedingPercent) + "%");
        txtAmbarSeed.setText(String.valueOf(seed));
        txtAmbarLand.setText(String.valueOf(land));
        txtAmbarSlaves.setText(String.valueOf(slaves));
        txtAmbarArmy.setText(String.valueOf(army));
        txtSlavesBusy.setText("Рабы загружены на " + String.valueOf(slavesBusyPercent) + "%");

        /*
         *  Обработчик положения слайдера, отвечающего за выделение зерна на хлеб
         */
        seekBarBread.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                breadPercent = progress;  // процент обеспеченности жителей хлебом
                bread = breadPercent * slaves * 5; // выделение зерна на хлеб
                seedLeft = seed - bread - seeding; // подсчёт оставшегося зерна
                txtSeedRemains.setText("После распределения остаётся " + String.valueOf(seedLeft)
                                      + " тонн зерна.");
                txtSeedBread.setText(String.valueOf(bread));
                txtSeedBreadPercent.setText(String.valueOf(breadPercent) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                happinessDecrease = 100 - breadPercent; // снижение настроения
            }
        });

        /*
         *  Обработчик положения слайдера, отвечающего за выделение зерна на засев
         */
        seekBarSeeding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                // установление шага: 4%
                int stepSize = 4;
                progress = (progress/stepSize)*stepSize;
                seekBar.setProgress(progress);

                seedingPercent = progress; // процент засеянности полей
                // берётся минимум, так как земли без рабов не будут приносить урожай
                seeding = seedingPercent * Math.min(slaves, land) / 4;
                seedLeft = seed - bread - seeding; // подсчёт оставшегося зерна
                txtSeedRemains.setText("После распределения остаётся " + String.valueOf(seedLeft)
                                      + " тонн зерна.");
                txtSeeding.setText(String.valueOf(seeding));
                txtSeedingPercent.setText(String.valueOf(seedingPercent) + "%");

                // процент занятости рабов засевом полей
                slavesBusyPercent = (int)Math.round((land * seedingPercent) / (double)slaves);
                if(slavesBusyPercent > 100)
                    slavesBusyPercent = 100;
                txtSlavesBusy.setText("Рабы загружены на " + String.valueOf(slavesBusyPercent) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    /*
     *  Обработчик кнопки: распределение ресурсов, генерация случайных событий,
     *  переход к следующему возможному случайному событию
     */
    public void allocate(View view)
    {
        if(seedLeft < 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Нехватка припасов");
            builder.setMessage("К сожалению, у Вас в распоряжении нет таких запасов зерна");
            builder.show();
            return;
        }

        // запись изменённых значений ресурсов в БД
        seed = seedLeft;
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        happiness -= happinessDecrease;
        myDb.updateHappiness(happiness);
        myDb.updateResources(money, gold, seed, land, slaves, army);
        myDb.updateSeedingAndBreadPercent(seeding, breadPercent);
        myDb.updateTaxes(0, 0);
        myDb.close();

        // расчёт вероятностей случайных событий в процентах
        int warProbability = 30;
        int annexProbability = 10;
        int heirProbability = age / 2;
        if(heir != 0) // если наследник уже есть, вероятность уменьшается в 2 раза
            heirProbability /= 2;
        int corruptProbability = 0;
        if(corrupt == 0) // если коррупции не было или уже прошло 5 лет, то она возможна
            corruptProbability = 7;
        int catchProbability = 0;
        if(corrupt > 0) // если коррупция была за последние 5 лет, возможна поимка
        {
            catchProbability = 40;
            myDb.updateCorruptAndStolen(corrupt - 1, stolen); // уменьшение счётчика лет от коррупции
        }

        // определение по вероятностям достоверности случайных событий в текущем году
        Random rand = new Random();
        warEvent = (rand.nextInt(101) <= warProbability);
        annexEvent = (rand.nextInt(101) <= annexProbability);
        heirEvent = (rand.nextInt(101) <= heirProbability);
        diseaseEvent = (rand.nextInt(101) <= age);
        corruptEvent = (rand.nextInt(101) <= corruptProbability);
        catchEvent = (rand.nextInt(101) <= catchProbability);

        // переход на активность события "Война" вне зависимости от того, произойдёт ли оно
        Intent intent = new Intent(this, War.class);
        // передача достоверностей возможных событий
        intent.putExtra("warEvent", warEvent);
        intent.putExtra("annexEvent", annexEvent);
        intent.putExtra("heirEvent", heirEvent);
        intent.putExtra("diseaseEvent", diseaseEvent);
        intent.putExtra("corruptEvent", corruptEvent);
        intent.putExtra("catchEvent", catchEvent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
