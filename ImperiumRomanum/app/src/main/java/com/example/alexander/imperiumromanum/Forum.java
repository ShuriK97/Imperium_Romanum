package com.example.alexander.imperiumromanum;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/*
 *  Покупка и продажа ресурсов
 */
public class Forum extends AppCompatActivity
{
    int gold, seed, land, slaves, army, money;
    int goldRate, seedRate, landRate, slavesRate, armyRate;
    int goldPrice, seedPrice, landPrice, slavesPrice, armyPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);

        // получение текстовых полей ресурсов
        TextView txtMoney = (TextView)findViewById(R.id.txt_description);
        EditText editGold = (EditText)findViewById(R.id.res_gold);
        EditText editLand = (EditText)findViewById(R.id.res_land);
        EditText editSeed = (EditText)findViewById(R.id.res_seed);
        EditText editSlaves = (EditText)findViewById(R.id.res_slaves);
        EditText editArmy = (EditText)findViewById(R.id.res_army);

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
        myDb.close();

        // подготовка к заполнению текстовых полей ресурсов
        String strMoney = String.valueOf(money);
        String strGold = String.valueOf(gold);
        String strSeed = String.valueOf(seed);
        String strLand = String.valueOf(land);
        String strSlaves = String.valueOf(slaves);
        String strArmy = String.valueOf(army);

        strMoney = "Имперская казна: " + strMoney + " денариев";

        // заполнение текстовых полей ресурсов
        txtMoney.setText(strMoney);
        editGold.setText(strGold);
        editSeed.setText(strSeed);
        editLand.setText(strLand);
        editSlaves.setText(strSlaves);
        editArmy.setText(strArmy);

        // получение цен и курсов из главной активности
        Bundle extras = getIntent().getExtras();
        goldRate = extras.getInt("gold rate");
        seedRate = extras.getInt("seed rate");
        landRate = extras.getInt("land rate");
        slavesRate = extras.getInt("slaves rate");
        armyRate = extras.getInt("army rate");
        goldPrice = extras.getInt("gold price");
        seedPrice = extras.getInt("seed price");
        landPrice = extras.getInt("land price");
        slavesPrice = extras.getInt("slaves price");
        armyPrice = extras.getInt("army price");

        // получение текстовых полей цен и курсов
        TextView txtGoldPrice = (TextView)findViewById(R.id.gold_price);
        TextView txtSeedPrice = (TextView)findViewById(R.id.seed_price);
        TextView txtLandPrice = (TextView)findViewById(R.id.land_price);
        TextView txtSlavesPrice = (TextView)findViewById(R.id.slaves_price);
        TextView txtArmyPrice = (TextView)findViewById(R.id.army_price);
        TextView txtGoldRate = (TextView)findViewById(R.id.gold_rate);
        TextView txtSeedRate = (TextView)findViewById(R.id.seed_rate);
        TextView txtLandRate = (TextView)findViewById(R.id.land_rate);
        TextView txtSlavesRate = (TextView)findViewById(R.id.slaves_rate);
        TextView txtArmyRate = (TextView)findViewById(R.id.army_rate);

        // заполнение текстовых полей цен и курсов
        txtGoldPrice.setText(String.valueOf(goldPrice));
        txtSeedPrice.setText(String.valueOf(seedPrice));
        txtLandPrice.setText(String.valueOf(landPrice));
        txtSlavesPrice.setText(String.valueOf(slavesPrice));
        txtArmyPrice.setText(String.valueOf(armyPrice));
        txtGoldRate.setText(String.valueOf(goldRate) + "%");
        txtSeedRate.setText(String.valueOf(seedRate) + "%");
        txtLandRate.setText(String.valueOf(landRate) + "%");
        txtSlavesRate.setText(String.valueOf(slavesRate) + "%");
        txtArmyRate.setText(String.valueOf(armyRate) + "%");

        /*
         *  Обработчик ввода текста: количество золота
         */
        editGold.addTextChangedListener(new TextWatcher()
        {
            int currentGold;

            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() == 0)
                    currentGold = 0;
                else
                    currentGold = Integer.parseInt(s.toString());
                int diffGold = gold - currentGold;
                money += goldPrice * diffGold;
                String strMoney = "Имперская казна: " + String.valueOf(money) + " денариев";
                TextView txtMoney = (TextView)findViewById(R.id.txt_description);
                txtMoney.setText(strMoney);
                gold = currentGold;
            }
        });

        /*
         *  Обработчик ввода текста: количество зерна
         */
        editSeed.addTextChangedListener(new TextWatcher()
        {
            int currentSeed;

            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() == 0)
                    currentSeed = 0;
                else
                    currentSeed = Integer.parseInt(s.toString());
                int diffSeed = seed - currentSeed;
                money += seedPrice * diffSeed;
                String strMoney = "Имперская казна: " + String.valueOf(money) + " денариев";
                TextView txtMoney = (TextView)findViewById(R.id.txt_description);
                txtMoney.setText(strMoney);
                seed = currentSeed;
            }
        });

        /*
         *  Обработчик ввода текста: количество земли
         */
        editLand.addTextChangedListener(new TextWatcher()
        {
            int currentLand;

            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() == 0)
                    currentLand = 0;
                else
                    currentLand = Integer.parseInt(s.toString());
                int diffLand = land - currentLand;
                money += landPrice * diffLand;
                String strMoney = "Имперская казна: " + String.valueOf(money) + " денариев";
                TextView txtMoney = (TextView)findViewById(R.id.txt_description);
                txtMoney.setText(strMoney);
                land = currentLand;
            }
        });

        /*
         *  Обработчик ввода текста: количество рабов
         */
        editSlaves.addTextChangedListener(new TextWatcher()
        {
            int currentSlaves;

            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() == 0)
                    currentSlaves = 0;
                else
                    currentSlaves = Integer.parseInt(s.toString());
                int diffSlaves = slaves - currentSlaves;
                money += slavesPrice * diffSlaves;
                String strMoney = "Имперская казна: " + String.valueOf(money) + " денариев";
                TextView txtMoney = (TextView)findViewById(R.id.txt_description);
                txtMoney.setText(strMoney);
                slaves = currentSlaves;
            }
        });

        /*
         *  Обработчик ввода текста: численность войска
         */
        editArmy.addTextChangedListener(new TextWatcher()
        {
            int currentArmy;

            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() == 0)
                    currentArmy = 0;
                else
                    currentArmy = Integer.parseInt(s.toString());
                int diffArmy = army - currentArmy;
                money += armyPrice * diffArmy;
                String strMoney = "Имперская казна: " + String.valueOf(money) + " денариев";
                TextView txtMoney = (TextView)findViewById(R.id.txt_description);
                txtMoney.setText(strMoney);
                army = currentArmy;
            }
        });

    }

    /*
     *  Обработчик кнопки: возврат на главную активность без сохранения изменений
     */
    public void backMain(View view)
    {
        finish();
    }

    /*
     *  Обработчик кнопки: подтверждение покупки и продажи
     */
    public void confirm(View view)
    {
        // денег может не хватить
        if(money < 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Как вы расточительны!");
            builder.setMessage("Вы не можете взять из казны больше денег, чем там есть.");
            builder.show();
        }
        else
        {
            // запись изменений ресурсов в БД
            DatabaseHelper myDb = new DatabaseHelper(this);
            myDb.getWritableDatabase();
            myDb.updateResources(money, gold, seed, land, slaves, army);
            myDb.close();
            finish();
        }
    }



}
