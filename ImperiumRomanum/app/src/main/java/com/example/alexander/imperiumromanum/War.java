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
 *  Война: предполагаемая численность врагов,
 *  возможность объявить войну или отказаться
 */
public class War extends AppCompatActivity
{
    boolean warEvent, annexEvent, heirEvent, diseaseEvent, corruptEvent, catchEvent;
    int enemyForces, estimateEnemyForces;
    int army, land;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // получение достоверностей возможных событий
        Bundle extras = getIntent().getExtras();
        warEvent = extras.getBoolean("warEvent");
        annexEvent = extras.getBoolean("annexEvent");
        heirEvent = extras.getBoolean("heirEvent");
        diseaseEvent = extras.getBoolean("diseaseEvent");
        corruptEvent = extras.getBoolean("corruptEvent");
        catchEvent = extras.getBoolean("catchEvent");

        // если текущее событие не должно произойти, переход на активность следующего
        if(!warEvent)
        {
            Intent intent = new Intent(this, Annexation.class);
            // передача достоверностей возможных событий
            intent.putExtra("annexEvent", annexEvent);
            intent.putExtra("heirEvent", heirEvent);
            intent.putExtra("diseaseEvent", diseaseEvent);
            intent.putExtra("corruptEvent", corruptEvent);
            intent.putExtra("catchEvent", catchEvent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.war);

        // нахождение текстовых полей
        TextView txtEmpireForces = (TextView)findViewById(R.id.txt_empire_forces);
        TextView txtEnemyForces = (TextView)findViewById(R.id.txt_enemy_forces);
        TextView txtWarEvent = (TextView)findViewById(R.id.txt_war_event);

        // получение ресурсов из БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        Cursor result = myDb.getData();
        result.moveToNext();
        land = result.getInt(7);
        army = result.getInt(9);
        result.close();
        myDb.close();

        if(army * 10 < land) // напали, нельзя отклонить
        {
            txtWarEvent.setText("      Варвары рядом с границей империи обнаружили, что Ваши земли недостаточно защищены. "
                                + "Вам объявили войну!");
            TextView txtCallWar = (TextView)findViewById(R.id.txt_call_war);
            txtCallWar.setText("");
            // блокировка кнопки отказа от войны
            Button btnRefuse = (Button)findViewById(R.id.btn_refuse);
            btnRefuse.setVisibility(View.INVISIBLE);
            btnRefuse.setEnabled(false);
        }
        else // можно и воевать, и отказаться
            txtWarEvent.setText("      Рядом с границей империи расположены варвары с отличной от римской культурой.");

        // расчёт численности вражеских войск, в том числе и предполагаемой
        Random rand = new Random();
        int minPossibleEnemyForces = (int)Math.round((double)land / 10 * 0.8);
        int maxPossibleEnemyForces = (int)Math.round((double)land / 10 * 1.2);
        enemyForces = rand.nextInt(maxPossibleEnemyForces - minPossibleEnemyForces + 1) + minPossibleEnemyForces;
        int minEstimateEnemyForces = (int)(0.9 * enemyForces);
        int maxEstimateEnemyForces = (int)(1.1 * enemyForces);
        estimateEnemyForces = rand.nextInt(maxEstimateEnemyForces - minEstimateEnemyForces + 1) + minEstimateEnemyForces;

        // заполнение предполагаемой численностью вражеских войск
        txtEnemyForces.setText("По оценкам военной разведки империи, численность врагов составляет "
                                + String.valueOf(estimateEnemyForces) + " тысяч человек.");
        txtEmpireForces.setText("Численность вашей армии составляет " + String.valueOf(army) + " сотен легионеров.");
    }

    /*
     *  Обработчик кнопки: отказ от войны, переход к следующему возможному случайному событию
     */
    public void refuse(View view)
    {
        Intent intent = new Intent(this, Annexation.class);

        intent.putExtra("annexEvent", annexEvent);
        // передача достоверностей возможных событий
        intent.putExtra("heirEvent", heirEvent);
        intent.putExtra("diseaseEvent", diseaseEvent);
        intent.putExtra("corruptEvent", corruptEvent);
        intent.putExtra("catchEvent", catchEvent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*
     *  Обработчик кнопки: объявление войны, переход к формированию войска
     */
    public void callWar(View view)
    {
        Intent intent = new Intent(this, WarBegin.class);
        // передача достоверностей возможных событий
        intent.putExtra("annexEvent", annexEvent);
        intent.putExtra("heirEvent", heirEvent);
        intent.putExtra("diseaseEvent", diseaseEvent);
        intent.putExtra("corruptEvent", corruptEvent);
        intent.putExtra("catchEvent", catchEvent);
        intent.putExtra("enemy forces", enemyForces);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
