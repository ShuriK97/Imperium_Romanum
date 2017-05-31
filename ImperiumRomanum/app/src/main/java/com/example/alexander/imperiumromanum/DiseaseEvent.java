package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*
 *  Результаты лечения: выздоровление или смерть
 */
public class DiseaseEvent extends AppCompatActivity
{
    boolean treat, withDoctor, corruptEvent, catchEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disease_event);

        // получение информации о лечении и достоверностей возможных событий
        Bundle extras = getIntent().getExtras();
        treat = extras.getBoolean("treat");
        withDoctor = extras.getBoolean("with doctor");
        corruptEvent = extras.getBoolean("corruptEvent");
        catchEvent = extras.getBoolean("catchEvent");

        // нахождение текстового поля
        TextView txtDiseaseEvent = (TextView)findViewById(R.id.txt_disease_event);

        // заполнение текстового поля в зависимости от информации о лечении
        if(treat)
        {
            if (withDoctor)
                txtDiseaseEvent.setText("      Лечение помогло, Вы благополучно выздоровели.");
            else
                txtDiseaseEvent.setText("      Ваш риск оправдался, Вы благополучно "
                                       + "выздоровели сами.");
        }
        else
        {
            if(withDoctor)
                txtDiseaseEvent.setText("      Лекарь, к сожалению, не всесилен, "
                                       + "Ваша смерть была предначертана судьбой.");
            else
                txtDiseaseEvent.setText("      К сожалению, болезнь оказалась сильнее вас. "
                        + "Ваш риск послужит хорошим уроком для последующих правителей.");
        }
    }

    /*
     *  Обработчик кнопки: переход к следующему событию или смерти
     */
    public void acceptDiseaseEvent(View view)
    {
        if(treat) // если лечение помогло: "Коррупция", "Поимка" или сразу "Колизей"
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
        }
        else // иначе - смерть
        {
            // получение информации о наследнике
            DatabaseHelper myDb = new DatabaseHelper(this);
            myDb.getWritableDatabase();
            Cursor result = myDb.getData();
            result.moveToNext();
            int heir = result.getInt(3);
            result.close();
            myDb.close();

            if(heir == 0) // если нет наследника, то конец игры
            {
                Intent intent = new Intent(this, GameOver.class);
                intent.putExtra("end cause", "disease");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else // иначе переход на активность "Смерть", последующее продолжение игры
            {
                Intent intent = new Intent(this, Death.class);
                intent.putExtra("corruptEvent", corruptEvent);
                intent.putExtra("catchEvent", catchEvent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
