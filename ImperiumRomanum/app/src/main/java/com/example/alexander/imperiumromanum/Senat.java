package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/*
 *  Подведение статистики за прошедший год,
 *  в результате переход к новому году правления или конец игры
 */
public class Senat extends AppCompatActivity
{
    int money, gold, seed, land, slaves, army, happiness, year, age;
    int breadPercent, seeding, armyWages, armyLoss, crops, seedLoss, slavesIncrease,
        cropsLevel, disasterLevel;
    TextView txtYearStats, txtFoodInfo, txtArmyInfo, txtCropsInfo, txtDisasterInfo,
             txtSlavesInfo, txtHappinessInfo;
    boolean fromMain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.senat);

        // важно знать, перешли сюда из главной активности или нет
        Bundle extras = getIntent().getExtras();
        fromMain = extras.getBoolean("from main");

        // если из главной, то статистика уже известна, её не надо высчитывать
        if(fromMain)
        {
            year = extras.getInt("year", 0);
            breadPercent = extras.getInt("bread percent", 0);
            armyWages = extras.getInt("army wages", 0);
            armyLoss = extras.getInt("army loss", 0);
            cropsLevel = extras.getInt("crops level", 0);
            crops = extras.getInt("crops", 0);
            disasterLevel = extras.getInt("disaster level", 0);
            seedLoss = extras.getInt("seed loss", 0);
            slavesIncrease = extras.getInt("slaves increase", 0);
            happiness = extras.getInt("happiness", 0);
        }

        // иначе высчитываем статистику
        else
        {
            // получение ресурсов из БД
            DatabaseHelper myDb = new DatabaseHelper(this);
            myDb.getWritableDatabase();
            Cursor result = myDb.getData();
            result.moveToNext();
            year = result.getInt(1);
            age = result.getInt(2);
            money = result.getInt(4);
            gold = result.getInt(5);
            seed = result.getInt(6);
            land = result.getInt(7);
            slaves = result.getInt(8);
            army = result.getInt(9);
            happiness = result.getInt(10);
            breadPercent = result.getInt(15);
            seeding = result.getInt(16);
            result.close();
            myDb.close();

            // расчёт зарплат армии
            armyWages = army * 100 + 1000;
            if (money < armyWages && army > 0)
            {
                // если денег не хватает, в армии потери
                int deficit = armyWages - money;
                armyLoss = deficit / 100;
            }
            else
                armyLoss = 0;

            Random rand = new Random();

            // если поля не были засеяны вообще
            if (seeding == 0)
            {
                crops = 0; // нулевой урожай
                cropsLevel = 0;
            }
            else // расчёт уровня урожайности
            {
                cropsLevel = rand.nextInt(101);
                if (cropsLevel <= 10)
                    cropsLevel = 6; // самая высокая урожайность
                else if (cropsLevel <= 30)
                    cropsLevel = 5;
                else if (cropsLevel <= 65)
                    cropsLevel = 4;
                else if (cropsLevel <= 85)
                    cropsLevel = 3;
                else if (cropsLevel <= 95)
                    cropsLevel = 2;
                else
                    cropsLevel = 1; // самая низкая урожайность

                // расчёт урожая исходя из уровня урожайности
                int minCrops = 0;
                int maxCrops = 0;
                switch (cropsLevel) {
                    case 6:
                        minCrops = (int) Math.round(seeding * 0.9 * 10);
                        maxCrops = (int) Math.round(seeding * 1.1 * 10);
                        break;
                    case 5:
                        minCrops = (int) Math.round(seeding * 0.9 * 8);
                        maxCrops = (int) Math.round(seeding * 1.1 * 8);
                        break;
                    case 4:
                        minCrops = (int) Math.round(seeding * 0.9 * 5);
                        maxCrops = (int) Math.round(seeding * 1.1 * 5);
                        break;
                    case 3:
                        minCrops = (int) Math.round(seeding * 0.9 * 3);
                        maxCrops = (int) Math.round(seeding * 1.1 * 3);
                        break;
                    case 2:
                        minCrops = (int) Math.round(seeding * 0.9 * 1);
                        maxCrops = (int) Math.round(seeding * 1.1 * 1);
                        break;
                    case 1:
                        minCrops = (int) Math.round(seeding * 0.9 * 0.5);
                        maxCrops = (int) Math.round(seeding * 1.1 * 0.5);
                        break;
                }
                crops = rand.nextInt(maxCrops - minCrops + 1) + minCrops;
            }

            // расчёт уровня случайной катастрофы или её отсутствия
            disasterLevel = rand.nextInt(101);
            if (disasterLevel <= 5)
                disasterLevel = 1;
            else if (disasterLevel <= 10)
                disasterLevel = 2;
            else if (disasterLevel <= 15)
                disasterLevel = 3;
            else
                disasterLevel = 0;
            seedLoss = (int) Math.round(seed * 0.05 * disasterLevel);

            // прирост численности рабов
            int maxSlavesIncrease = (int) Math.round(slaves * 0.1);
            slavesIncrease = rand.nextInt(maxSlavesIncrease + 1);

            // обнуление настроения, если оно отрицательное
            if (happiness < 0)
                happiness = 0;
        }

        // нахождение текстовых полей
        txtYearStats = (TextView)findViewById(R.id.txt_year_stats);
        txtFoodInfo = (TextView)findViewById(R.id.txt_food_info);
        txtArmyInfo = (TextView)findViewById(R.id.txt_army_info);
        txtCropsInfo = (TextView)findViewById(R.id.txt_crops_info);
        txtDisasterInfo = (TextView)findViewById(R.id.txt_disaster_info);
        txtSlavesInfo = (TextView)findViewById(R.id.txt_slaves_info);
        txtHappinessInfo = (TextView)findViewById(R.id.txt_happiness_info);

        // заполнение текстовых полей
        txtYearStats.setText("      Статистика за " + String.valueOf(year) + " год правления.");

        txtFoodInfo.setText("      Норма продуктов на граждан составила "
                            + String.valueOf(breadPercent) + "% от необходимой.");
        if(breadPercent == 0)
            txtFoodInfo.append("\n      Толпа больше не в силах терпеть Вашу тиранию, "
                              + "сенат при поддержке граждан и армии с позором изгоняет Вас.");
        else if(breadPercent <= 50)
            txtFoodInfo.append("\n      Народ восстаёт против Вас во всех уголках империи.");
        else if(breadPercent <= 74)
            txtFoodInfo.append("\n      Народ в ярости, сенат предупреждает вас о возможном свержении.");
        else if(breadPercent <= 99)
            txtFoodInfo.append("\n      Народ недоволен, граждане осуждают вас.");

        txtArmyInfo.setText("      Жалование легионеров за прошедший год составило "
                           + String.valueOf(armyWages) + " денариев.");
        if(armyLoss > 0)
            txtArmyInfo.append("\n      Не хватило денег на содержание всех легионеров. "
                               + " Распущено " + String.valueOf(armyLoss) + " сотен человек.");

        switch(cropsLevel)
        {
            case 6:
                txtCropsInfo.setText("      Невероятно благополучная погода обеспечила "
                                    + " невиданно высокий урожай. Амбары заполнены "
                                    + " доверху - собрано " + String.valueOf(crops) + " тонн зерна!");
                break;
            case 5:
                txtCropsInfo.setText("      Высокая урожайность была в этом году. "
                                    + "Собрано " + String.valueOf(crops) + " тонн зерна.");
                break;
            case 4:
                txtCropsInfo.setText("      Урожайный год. Собрано " + String.valueOf(crops)
                                    + " тонн зерна.");
                break;
            case 3:
                txtCropsInfo.setText("      Средний по урожайности год. В империи собрано "
                                    + String.valueOf(crops) + " тонн зерна.") ;
                break;
            case 2:
                txtCropsInfo.setText("      Урожайность была низкая. Собрано "
                                    + String.valueOf(crops) + " тонн зерна.");
                break;
            case 1:
                txtCropsInfo.setText("      Страшная засуха поразила посевы. "
                                    + "Очень неурожайный год. Собрано всего "
                                    + String.valueOf(crops) + " тонн зерна.");
                break;
            case 0:
                txtCropsInfo.setText("      Вы не засевали поля в этом году. Урожая нет.");
                break;
        }

        switch(disasterLevel)
        {
            case 3:
                txtDisasterInfo.setText("      Сильнейшие бедствия обрушились на некоторые "
                        + "провинции империи! Гражданам, лишившимся "
                        + "собственного продовольствия, было выделено "
                        + String.valueOf(seedLoss) + " тонн зерна.");
                break;
            case 2:
                txtDisasterInfo.setText("      Коррупция и бюрократия! Империя лишилась "
                        + String.valueOf(seedLoss) + " тонн зерна.");
                break;
            case 1:
                txtDisasterInfo.setText("      Преступная халатность! Крысы сожрали "
                        + String.valueOf(seedLoss) + " тонн зерна.");
                break;
            case 0:
                txtDisasterInfo.setText("      Все запасы империи в целости и сохранности.");
                break;
        }

        txtSlavesInfo.setText("      Численность рабов в империи увеличилась на "
                             + String.valueOf(slavesIncrease) + " тысяч.");

        txtHappinessInfo.setText("      Уровень довольства в Вашей империи составляет "
                                + String.valueOf(happiness) + "%.");
        if(happiness >= 101)
            txtHappinessInfo.append("\n      Граждане находятся в эйфории.");
        else if(happiness >= 90)
            txtHappinessInfo.append("\n      Граждане невероятно счастливы.");
        else if(happiness >= 75)
            txtHappinessInfo.append("\n      Граждане удовлетворены и лояльны к Вам.");
        else if(happiness >= 50)
            txtHappinessInfo.append("\n      Граждане недовольны и сердятся на Вас.");
        else if(happiness >= 1)
            txtHappinessInfo.append("\n      Граждане в ярости, случаются восстания.");
        else
            txtHappinessInfo.append("\n      Разъярённые граждане врываются в Вашу резиденцию "
                                   + "и убивают Вас в Ваших покоях.");
    }

    /*
     *  Обработчик кнопки: возврат на главную активность
     */
    public void backMain(View view)
    {
        if(!fromMain)
        {
            // если пришли сюда не из главной активности, то данные в БД надо обновить
            DatabaseHelper myDb = new DatabaseHelper(this);
            myDb.getWritableDatabase();
            myDb.updateYearAndAge(year + 1, age + 1);
            // перерасчёт ресурсов
            money -= armyWages;
            boolean debt = false;
            if(money < 0)
            {
                money = 0;
                debt = true;
            }
            seed += crops;
            seed -= seedLoss;
            slaves += slavesIncrease;
            army -= armyLoss;
            myDb.updateResources(money, gold, seed, land, slaves, army);
            myDb.close();

            // конец игры от нулевого удовольствия
            if (happiness == 0)
            {
                Intent intent = new Intent(this, GameOver.class);
                intent.putExtra("end cause", "unhappiness");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            // конец игры от голода
            else if (breadPercent == 0)
            {
                Intent intent = new Intent(this, GameOver.class);
                intent.putExtra("end cause", "starvation");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            // конец игры от долгов
            else if (debt)
            {
                Intent intent = new Intent(this, GameOver.class);
                intent.putExtra("end cause", "debt");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            else
            {
                Intent intent = new Intent(this, MainScreen.class);
                // передача статистики о прошедшем годе на главную активность
                intent.putExtra("bread percent", breadPercent);
                intent.putExtra("army wages", armyWages);
                intent.putExtra("army loss", armyLoss);
                intent.putExtra("crops level", cropsLevel);
                intent.putExtra("crops", crops);
                intent.putExtra("disaster level", disasterLevel);
                intent.putExtra("seed loss", seedLoss);
                intent.putExtra("slaves increase", slavesIncrease);
                intent.putExtra("happiness", happiness);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        else // если пришли из главной активности, то в ней статистика уже есть
            finish();
    }
}
