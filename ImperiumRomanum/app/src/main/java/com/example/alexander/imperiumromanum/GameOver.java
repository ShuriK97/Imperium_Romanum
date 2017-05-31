package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/*
 *  Конец игры, можно начать заново
 */
public class GameOver extends AppCompatActivity
{
    int year, money, gold, seed, land, slaves, army, totalCapital;
    int goldPrice, seedPrice, landPrice, slavesPrice, armyPrice;
    String endCause;
    public static boolean firstCalled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // если эта активность уже вызывалась, но не была нажата кнопка
        // "Начать заново", то сразу переходим к главной
        if(firstCalled == false)
        {
            Intent intent = new Intent(this, MainScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.game_over);
        firstCalled = false;

        // получение текстовых полей
        TextView txtDeathCause = (TextView)findViewById(R.id.txt_death_cause);
        TextView txtYear = (TextView)findViewById(R.id.txt_year_final);
        TextView txtMoney = (TextView)findViewById(R.id.txt_money_final);
        TextView txtGold = (TextView)findViewById(R.id.txt_gold_final);
        TextView txtSeed = (TextView)findViewById(R.id.txt_seed_final);
        TextView txtLand = (TextView)findViewById(R.id.txt_land_final);
        TextView txtSlaves = (TextView)findViewById(R.id.txt_slaves_final);
        TextView txtArmy = (TextView)findViewById(R.id.txt_army_final);
        TextView txtEstimate = (TextView)findViewById(R.id.txt_estimate_final);

        // получение поля изображения
        ImageView imgGameOver = (ImageView)findViewById(R.id.img_game_over);

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

        // уровень цен ресурсов по 100% курсу
        goldPrice = 1000 ;
        seedPrice = 100;
        landPrice = 500;
        slavesPrice = 50;
        armyPrice = 100;

        // подсчёт стоимости накопленного капитала
        totalCapital = gold * goldPrice + seed * seedPrice + land * landPrice
                + seed * seedPrice + slaves * slavesPrice + army * armyPrice + money;

        // заполнение текстовых полей
        txtYear.setText("Ваше правление подошло к концу на " + String.valueOf(year) + " год.");
        txtMoney.setText(String.valueOf(money));
        txtGold.setText(String.valueOf(gold));
        txtSeed.setText(String.valueOf(seed));
        txtLand.setText(String.valueOf(land));
        txtSlaves.setText(String.valueOf(slaves));
        txtArmy.setText(String.valueOf(army));
        txtEstimate.setText("Ваш накопленный капитал был оценен в " + String.valueOf(totalCapital)
                            + " денариев.");

        // получение причины конца игры
        Bundle extras = getIntent().getExtras();
        endCause = extras.getString("end cause");
        // оформление в зависимости от причины конца игры
        if(endCause.equals("disease"))
        {
            txtDeathCause.setText("      Сильнее вас болезнь была, она вам смерть и принесла. "
                    + "Жаль, что у такого императора не оказалось наследника.");
            imgGameOver.setImageResource(R.drawable.death_gameover);
        }
        else if(endCause.equals("starvation"))
        {
            txtDeathCause.setText("      Народ, объевшись голодом, поднял восстание против Вас.");
            imgGameOver.setImageResource(R.drawable.overthrow);
        }
        else if(endCause.equals("unhappiness"))
        {
            txtDeathCause.setText("      Народу стало скучно, решил он Вас убить.");
            imgGameOver.setImageResource(R.drawable.revolution);
        }
        else if(endCause.equals("debt"))
        {
            txtDeathCause.setText("      У Вас не осталось денег даже на содержание своей охраны. "
                                 + "Стража совершила военный переворот, отстранив Вас от власти.");
            imgGameOver.setImageResource(R.drawable.army_debt);
        }

        myDb.clear(); // установление стартовых значений для новой игры
        myDb.close();
    }

    /*
     *  Обработчик кнопки: начать заново
     */
    public void startOver(View view)
    {
        Intent intent = new Intent(this, MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
