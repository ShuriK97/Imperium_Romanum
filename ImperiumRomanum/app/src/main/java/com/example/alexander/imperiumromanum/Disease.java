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
 *  Болезнь: возможно лечиться у доктора и самостоятельно; выздороветь или умереть
 */
public class Disease extends AppCompatActivity
{
    boolean diseaseEvent, corruptEvent, catchEvent;
    int age, money, gold, seed, land, slaves, army;
    int goldPrice, seedPrice, landPrice, slavesPrice, armyPrice;
    int treatCost;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // получение достоверностей возможных событий
        Bundle extras = getIntent().getExtras();
        diseaseEvent = extras.getBoolean("diseaseEvent");
        corruptEvent = extras.getBoolean("corruptEvent");
        catchEvent = extras.getBoolean("catchEvent");

        // если текущее событие не должно произойти, переход на активность следующего
        // это может быть "Коррупция", "Поимка" или сразу "Колизей"
        if(!diseaseEvent)
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
            finish();
        }

        // получение ресурсов из БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        Cursor result = myDb.getData();
        result.moveToNext();
        age = result.getInt(2);
        money = result.getInt(4);
        gold = result.getInt(5);
        seed = result.getInt(6);
        land = result.getInt(7);
        slaves = result.getInt(8);
        army = result.getInt(9);
        result.close();
        myDb.close();

        // цены ресурсов по 100% курсу
        goldPrice = 1000;
        seedPrice = 10;
        landPrice = 500;
        slavesPrice = 50;
        armyPrice = 100;

        // стоимость лечения у доктора
        treatCost = (gold * goldPrice + seed * seedPrice + land * landPrice
                  + seed * seedPrice + slaves * slavesPrice + army * armyPrice) / 10000;

        setContentView(R.layout.disease);

        // заполнение текстового поля
        TextView txtTreatCost = (TextView)findViewById(R.id.txt_treat_cost);
        txtTreatCost.setText("      Император болен! За ваше лечение лекарь просит "
                + String.valueOf(treatCost) + " денариев.");
    }

    /*
     *  Обработчик кнопки: отказ от лечения у доктора
     */
    public void refuseTreat(View view)
    {
        // расчёт вероятности выздоровления
        Random rand = new Random();
        int treatProbability = 100 - age * 2;
        boolean treat = (rand.nextInt(101) <= treatProbability);

        // переход к результатам лечения
        Intent intent = new Intent(this, DiseaseEvent.class);
        // передача информации о лечении
        intent.putExtra("treat", treat);
        intent.putExtra("with doctor", false);
        // передача достоверностей возможных событий
        intent.putExtra("corruptEvent", corruptEvent);
        intent.putExtra("catchEvent", catchEvent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*
     *  Обработчик кнопки: лечение у доктора
     */
    public void treat(View view)
    {
        // нехватка денег на лечение
        if(money < treatCost)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Как вы расточительны!");
            builder.setMessage("К сожалению, текущее состояние казны не может позволить вам лечение.");
            builder.show();
            return;
        }

        // обновление количества денег в БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        myDb.updateMoney(money - treatCost);
        myDb.close();

        // расчёт вероятности выздоровления
        Random rand = new Random();
        int treatProbability = 100 - age;
        boolean treat = (rand.nextInt(100) <= treatProbability);

        // переход к результатам лечения
        Intent intent = new Intent(this, DiseaseEvent.class);
        // передача информации о лечении
        intent.putExtra("treat", treat);
        intent.putExtra("with doctor", true);
        // передача достоверностей возможных событий
        intent.putExtra("corruptEvent", corruptEvent);
        intent.putExtra("catchEvent", catchEvent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
