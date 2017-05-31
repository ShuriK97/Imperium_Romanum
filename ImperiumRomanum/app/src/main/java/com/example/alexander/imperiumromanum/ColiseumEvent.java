package com.example.alexander.imperiumromanum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/*
 *  Подведение итогов проведения игр на Колизее
 */
public class ColiseumEvent extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coliseum_event);

        // получение повышения настроения
        Bundle extras = getIntent().getExtras();
        int happinessIncrease = extras.getInt("happiness increase");

        // получение текстового поля и поля изображения
        TextView txtColiseumEvent = (TextView)findViewById(R.id.txt_coliseum_event);
        ImageView imgColiseumEvent = (ImageView)findViewById(R.id.img_coliseum_event);

        // оформление в зависимости от повышения настроения
        if(happinessIncrease >= 1)
        {
            txtColiseumEvent.setText("      Праздник прошёл просто замечательно. "
                    + "Толпа ликовала. На арене были продемонстрированы "
                    + "различные исторические битвы, где римская армия "
                    + "одерживала победу над варварами. На скачках участники, "
                    + "не жалея себя и своих колесниц, показали поразительное "
                    + "представление.\n      Народ восхваляет и почитает Вас. "
                    + "При этом некоторые даже нажились благодаря ставкам.");
            imgColiseumEvent.setImageResource(R.drawable.coliseum_super);
        }
        else if(happinessIncrease == 0)
        {
            txtColiseumEvent.setText("      Было весело. Народ ожил и громко кричал, "
                    + "помогая Вам принимать решения дальнейших судеб "
                    + "гладиаторов на арене.\n      Ваше некоторое содействие "
                    + "в проведении этого мероприятия оставило граждан "
                    + "неравнодушными к Вам.");
            imgColiseumEvent.setImageResource(R.drawable.coliseum_ok);
        }
        else
        {
            txtColiseumEvent.setText("      Всё прошло без каких-либо особенностей: скучно и "
                    + "однообразно. Из-за того, что Вы остались в стороне, "
                    + "народ затаил на Вас обиду.");
            imgColiseumEvent.setImageResource(R.drawable.coliseum_bad);
        }

    }

    /*
     *  Обработчик кнопки: переход на активность "Сенат"
     */
    public void toSenat(View view)
    {
        Intent intent = new Intent(this, Senat.class);
        // передаём, что приходим на активность "Сенат" не из главной активности
        intent.putExtra("from main", false);
        startActivity(intent);
    }
}
