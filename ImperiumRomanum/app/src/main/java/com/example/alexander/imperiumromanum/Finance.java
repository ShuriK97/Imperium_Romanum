package com.example.alexander.imperiumromanum;

import  android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/*
 *  Казначей: установление налогов, пожертвования
 */
public class Finance extends AppCompatActivity
{
    int happiness, money, tax, taxPercent, land, slaves, donation;
    TextView txtMoney, txtTax, txtTaxPercent, txtHappiness, txtDiffHappiness, txtDonation, txtEuphoria;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance);

        // получение такстовых полей
        txtMoney = (TextView)findViewById(R.id.txt_fin_money);
        txtTax = (TextView)findViewById(R.id.txt_tax);
        txtTaxPercent = (TextView)findViewById(R.id.txt_tax_percent);
        txtHappiness = (TextView)findViewById(R.id.txt_happiness);
        txtDiffHappiness = (TextView)findViewById(R.id.txt_diff_happiness);
        txtDonation = (TextView)findViewById(R.id.txt_donation);
        txtEuphoria = (TextView)findViewById(R.id.txt_euphoria);

        // получение ресурсов из БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        Cursor result = myDb.getData();
        result.moveToNext();
        money = result.getInt(4);
        happiness = result.getInt(10);
        land = result.getInt(7);
        slaves = result.getInt(8);
        tax = result.getInt(12);
        taxPercent = result.getInt(13);
        result.close();
        myDb.close();

        // заполнение текстовых полей
        String strMoney = "Имперская казна: " + String.valueOf(money) + " денариев";
        txtMoney.setText(strMoney);
        txtTax.setText(String.valueOf(tax));
        txtTaxPercent.setText(String.valueOf(taxPercent) + "%");

        String strDiffHappiness = "      Установленные налоги изменяют настроение народа на " + taxPercent + "%.";
        String strHappiness = "      Уровень довольства в Вашей империи " + happiness + "%.";
        txtDiffHappiness.setText(strDiffHappiness);
        txtHappiness.setText(strHappiness);

        if(happiness >= 100)
            txtEuphoria.setText("Граждане находятся в эйфории.");
        else
            txtEuphoria.setText("");

        // подсчёт величины пожертвования для увеличения настроения на 1%
        donation = land * slaves / 10;
        txtDonation.setText("      Вы можете оказать материальную помощь бедным и нуждающимся "
                            + "гражданам и повысить настроение империи на 1%, выделив сумму в размере "
                            + donation + " денариев.");

        // получение и настройка слайдера
        SeekBar seekBarTax = (SeekBar)findViewById(R.id.seek_bar_tax);
        seekBarTax.setProgress(taxPercent);

        /*
         *  Обработчик положения слайдера: установление налогов
         */
        seekBarTax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                int diffPercent = progress - taxPercent;
                happiness -= diffPercent;
                if(happiness > 200)
                    happiness = 200;
                taxPercent = progress;
                tax = progress * land * 100;
                money += diffPercent * land * 100;
                String strMoney = "Имперская казна: " + String.valueOf(money) + " денариев";
                txtMoney.setText(strMoney);
                txtTax.setText(String.valueOf(tax));
                txtTaxPercent.setText(String.valueOf(taxPercent) + "%");
                String strDiffHappiness = "      Установленные налоги изменяют настроение народа на " + taxPercent + "%.";
                txtDiffHappiness.setText(strDiffHappiness);
                if(happiness >= 100)
                    txtEuphoria.setText("Граждане находятся в эйфории.");
                else
                    txtEuphoria.setText("");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    /*
     *  Обработчик кнопки: пожертвование, увеличение настроения
     */
    public void donate(View view)
    {
        if(money > donation)
        {
            money -= donation;
            String strMoney = "Имперская казна: " + String.valueOf(money) + " денариев";
            happiness++;
            if(happiness > 200)
                happiness = 200; // максимум 200
            String strHappiness = "      Уровень довольства в Вашей империи " + happiness + "%.";
            txtMoney.setText(strMoney);
            txtHappiness.setText(strHappiness);
            // обновление денег и настроения в БД
            DatabaseHelper myDb = new DatabaseHelper(this);
            myDb.getWritableDatabase();
            myDb.updateHappiness(happiness);
            myDb.updateMoney(money);
            myDb.close();
            if(happiness >= 100)
                txtEuphoria.setText("Граждане находятся в эйфории.");
            else
                txtEuphoria.setText("");
        }
        else // денег может не хватать на пожертвование
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Как вы расточительны!");
            builder.setMessage("Вы не можете взять из казны больше денег, чем там есть.");
            builder.show();
        }
    }

    /*
     *  Обработчик кнопки: установление налогов
     */
    public void setTaxes(View view)
    {
        // нельзя установить налоги, которые сразу же понизят настроение до нуля
        if(happiness < 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Как вы безжалостны!");
            builder.setMessage("Такой уровень недовольства просто недопустим.");
            builder.show();
            return;
        }

        // обновление денег, настроения, налогов в БД
        DatabaseHelper myDb = new DatabaseHelper(this);
        myDb.getWritableDatabase();
        myDb.updateHappiness(happiness);
        myDb.updateTaxes(tax, taxPercent);
        myDb.updateMoney(money);
        myDb.close();
        // закрытие активности
        finish();
    }
}
