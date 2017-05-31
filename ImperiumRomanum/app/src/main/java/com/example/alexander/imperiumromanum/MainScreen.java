package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/*
 *  Главная активность: состояние ресурсов
 */
public class MainScreen extends AppCompatActivity
{
    int goldRate, landRate, seedRate, slavesRate, armyRate;
    int goldPrice, landPrice, seedPrice, slavesPrice, armyPrice;
    int breadPercent, armyWages, armyLoss, cropsLevel, crops, disasterLevel,
        seedLoss, slavesIncrease, lastYearHappiness;
    int year, money, gold, seed, land, slaves, army;
    TextView txtGold, txtLand, txtSeed, txtSlaves, txtArmy, txtYear, txtMoney;
    public static boolean firstCalled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // нахождение текстовых полей
        txtGold = (TextView)findViewById(R.id.gold);
        txtLand = (TextView)findViewById(R.id.land);
        txtSeed = (TextView)findViewById(R.id.seed);
        txtSlaves = (TextView)findViewById(R.id.slaves);
        txtArmy = (TextView)findViewById(R.id.army);
        txtYear = (TextView)findViewById(R.id.year);
        txtMoney = (TextView)findViewById(R.id.money);

        // получение ресурсов из БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase(); // если база не создана, вызывает конструктор
        Cursor result = myDb.getData();
        result.moveToNext();
        year = result.getInt(1);
        gold = result.getInt(5);
        seed = result.getInt(6);
        land = result.getInt(7);
        slaves = result.getInt(8);
        army = result.getInt(9);
        result.close();
        myDb.close();


        String strYear = "Год правления: " + String.valueOf(year);
        String strMoney = "Имперская казна: " + String.valueOf(money) + " денариев";

        // заполнение текстовых полей: статистика ресурсов
        txtGold.setText(String.valueOf(gold));
        txtSeed.setText(String.valueOf(seed));
        txtLand.setText(String.valueOf(land));
        txtSlaves.setText(String.valueOf(slaves));
        txtArmy.setText(String.valueOf(army));
        txtYear.setText(strYear);
        txtMoney.setText(strMoney);

        // в первый год правления, а также когда главная активность вызывается первой
        // (не после других), активность "Сенат" не показываем (нет данных)
        if(year == 1 || firstCalled)
        {
            Button senat = (Button)findViewById(R.id.btn_senat);
            senat.setVisibility(View.INVISIBLE);
            senat.setEnabled(false);
        }
        // иначе получаем данные из сената, чтобы можно было снова туда перейти
        else
        {
            Bundle extras = getIntent().getExtras();
            breadPercent = extras.getInt("bread percent", 0);
            armyWages = extras.getInt("army wages", 0);
            armyLoss = extras.getInt("army loss", 0);
            cropsLevel = extras.getInt("crops level", 0);
            crops = extras.getInt("crops", 0);
            disasterLevel = extras.getInt("disaster level", 0);
            seedLoss = extras.getInt("seed loss", 0);
            slavesIncrease = extras.getInt("slaves increase", 0);
            lastYearHappiness = extras.getInt("happiness", 0);
        }

        // изначальные цены ресурсов по 100% курсу
        goldPrice = 1000;
        seedPrice = 10;
        landPrice = 500;
        slavesPrice = 50;
        armyPrice = 100;

        // назначение курсов за текущий год и соответствующих цен
        Random rand = new Random();
        goldRate = rand.nextInt(101) + 50;
        seedRate = rand.nextInt(101) + 50;
        landRate = rand.nextInt(101) + 50;
        slavesRate = rand.nextInt(101) + 50;
        armyRate = rand.nextInt(101) + 50;
        goldPrice = goldPrice * goldRate / 100;
        seedPrice = seedPrice * seedRate / 100;
        landPrice = landPrice * landRate / 100;
        slavesPrice = slavesPrice * slavesRate / 100;
        armyPrice = armyPrice * armyRate / 100;
    }

    /*
     *  При возобновлении активности (возвращение из активностей "Форум" и "Казначей")
     *  нужно отображать обновлённые данные о ресурсах
     */
    @Override
    protected void onResume()
    {
        super.onResume();

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
        myDb.close();

        String strYear = "Год правления: " + String.valueOf(year);
        String strMoney = "Имперская казна: " + String.valueOf(money) + " денариев";

        // заполнение текстовых полей: статистика ресурсов
        txtGold.setText(String.valueOf(gold));
        txtSeed.setText(String.valueOf(seed));
        txtLand.setText(String.valueOf(land));
        txtSlaves.setText(String.valueOf(slaves));
        txtArmy.setText(String.valueOf(army));
        txtYear.setText(strYear);
        txtMoney.setText(strMoney);
    }

    /*
     *  Обработчик кнопки: переход на активность "Форум"
     */
    public void toForum(View view)
    {
        Intent intent = new Intent(this, Forum.class);
        // передача текущих цен и курсов
        intent.putExtra("gold rate", goldRate);
        intent.putExtra("seed rate", seedRate);
        intent.putExtra("land rate", landRate);
        intent.putExtra("slaves rate", slavesRate);
        intent.putExtra("army rate", armyRate);
        intent.putExtra("gold price", goldPrice);
        intent.putExtra("seed price", seedPrice);
        intent.putExtra("land price", landPrice);
        intent.putExtra("slaves price", slavesPrice);
        intent.putExtra("army price", armyPrice);
        startActivity(intent);
    }

    /*
     *  Обработчик кнопки: переход на активность "Казначей"
     */
    public void toFinance(View view)
    {
        Intent intent = new Intent(this, Finance.class);
        startActivity(intent);
    }

    /*
     *  Обработчик кнопки: переход на активность "Сенат"
     *  Доступно, когда год правления не первый и
     *  перед главной активностью ранее вызывался "Сенат"
     */
    public void toSenat(View view)
    {
        Intent intent = new Intent(MainScreen.this, Senat.class);
        // передаём, что приходим на активность "Сенат" из главной активности
        intent.putExtra("from main", true);
        // предача информации о прошедшем годе
        intent.putExtra("year", year - 1);
        intent.putExtra("bread percent", breadPercent);
        intent.putExtra("army wages", armyWages);
        intent.putExtra("army loss", armyLoss);
        intent.putExtra("crops level", cropsLevel);
        intent.putExtra("crops", crops);
        intent.putExtra("disaster level", disasterLevel);
        intent.putExtra("seed loss", seedLoss);
        intent.putExtra("slaves increase", slavesIncrease);
        intent.putExtra("happiness", lastYearHappiness);
        startActivity(intent);
    }

    /*
     *  Обработчик кнопки: переход на активность информации об игре
     */
    public void toInfo(View view)
    {
        Intent intent = new Intent(MainScreen.this,Info.class);
        startActivity(intent);
    }

    /*
     *  Обработчик кнопки: переход на активность "Амбар", вернуться назад нельзя
     */
    public void toNextYear(View view)
    {
        firstCalled = false;
        Intent intent = new Intent(MainScreen.this, Ambar.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*
     *  Обработчик кнопки: выход из игры
     */
    public void Exit(View view)
    {
        finish();
    }

}
