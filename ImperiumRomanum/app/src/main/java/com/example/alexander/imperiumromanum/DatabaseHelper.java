package com.example.alexander.imperiumromanum;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 *  Класс для работы с базой данных
 *  Все ресурсы хранятся в одной таблице
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "Statistics.db";
    public static final String TABLE_NAME = "Stats";
    public static final String COLUMN_USER = "UserID";
    public static final String COLUMN_YEAR = "Year"; // год правления вообще
    public static final String COLUMN_AGE = "Age"; // год правления текущего правителя
    public static final String COLUMN_HEIR = "Heir"; // есть ли наследник: 1 - есть, 0 - нет
    public static final String COLUMN_MONEY = "Money"; // деньги в казне (денарии)
    public static final String COLUMN_GOLD = "Gold"; // золото в казне
    public static final String COLUMN_SEED = "Seed"; // зерно в амбарах, тонны
    public static final String COLUMN_LAND = "Land"; // земля
    public static final String COLUMN_SLAVES = "Slaves"; // число рабов, тысячи
    public static final String COLUMN_ARMY = "Army"; // численность армии, сотни
    public static final String COLUMN_HAPPINESS = "Happiness"; // настроение: от 0 до 200
    public static final String COLUMN_CORRUPT = "Corrupt"; // обратный отсчёт числа лет от коррупции
    public static final String COLUMN_TAX = "Tax"; // текущий налог
    public static final String COLUMN_TAX_RATE = "TaxRate"; // текущая налоговая ставка
    public static final String COLUMN_STOLEN = "Stolen"; // украдено из казны при коррупции
    public static final String COLUMN_BREAD_PERCENT = "BreadPercent"; // сытость
    public static final String COLUMN_SEEDING = "Seeding"; // зерно, выделенное на засев полей

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // создание таблицы
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_YEAR + " INTEGER, " +
                    COLUMN_AGE + " INTEGER, " +
                    COLUMN_HEIR + " INTEGER, " +
                    COLUMN_MONEY + " INTEGER, " +
                    COLUMN_GOLD + " INTEGER, " +
                    COLUMN_SEED + " INTEGER, " +
                    COLUMN_LAND + " INTEGER, " +
                    COLUMN_SLAVES + " INTEGER, " +
                    COLUMN_ARMY + " INTEGER, " +
                    COLUMN_HAPPINESS + " INTEGER, " +
                    COLUMN_CORRUPT + " INTEGER, " +
                    COLUMN_TAX + " INTEGER, " +
                    COLUMN_TAX_RATE + " INTEGER, " +
                    COLUMN_STOLEN + " INTEGER, " +
                    COLUMN_BREAD_PERCENT + " INTEGER, " +
                    COLUMN_SEEDING + " INTEGER);");
        // стартовые значения при начале игры
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (1, 1, 1, 0, 100000, 10, 160000, 300, 300, 30, 100, 0, 0, 0, 0, 0, 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /*
     *  Получение всех данных через курсор
     */
    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return result;
    }

    /*
     *  Обновление денег и основных ресурсов
     */
    public void updateResources(int money, int gold, int seed, int land, int slaves, int army)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                   COLUMN_MONEY + " = " + String.valueOf(money) + ", " +
                   COLUMN_GOLD + " = " + String.valueOf(gold) + ", " +
                   COLUMN_SEED + " = " + String.valueOf(seed) + ", " +
                   COLUMN_LAND + " = " + String.valueOf(land) + ", " +
                   COLUMN_SLAVES + " = " + String.valueOf(slaves) + ", " +
                   COLUMN_ARMY + " = " + String.valueOf(army) + " WHERE " + COLUMN_USER + " = 1");
    }

    /*
     *  Обновление налогов и налоговой ставки
     */
    public void updateTaxes(int tax, int taxRate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                COLUMN_TAX + " = " + String.valueOf(tax) + ", " +
                COLUMN_TAX_RATE + " = " + String.valueOf(taxRate) + " WHERE " + COLUMN_USER + " = 1");
    }

    /*
     *  Обновление денег
     */
    public void updateMoney(int money)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                COLUMN_MONEY + " = " + String.valueOf(money) + " WHERE " + COLUMN_USER + " = 1");
    }

    /*
     *  Обновление настроения
     */
    public void updateHappiness(int happiness)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                COLUMN_HAPPINESS + " = " + String.valueOf(happiness) + " WHERE " + COLUMN_USER + " = 1");
    }

    /*
     *  Обновление счётчика лет от коррупции и украденных денег
     */
    public void updateCorruptAndStolen(int corrupt, int stolen)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                COLUMN_CORRUPT + " = " + String.valueOf(corrupt) + ", " +
                COLUMN_STOLEN + " = " + String.valueOf(stolen) + " WHERE " + COLUMN_USER + " = 1");
    }

    /*
     *  Обновление года правления вообще и для текущего правителя
     */
    public void updateYearAndAge(int year, int age)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                COLUMN_YEAR + " = " + String.valueOf(year) + ", " +
                COLUMN_AGE + " = " + String.valueOf(age) + " WHERE " + COLUMN_USER + " = 1");
    }

    /*
     *  Обновление сытости и зерна на засев
     */
    public void updateSeedingAndBreadPercent(int seeding, int breadPercent)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                COLUMN_SEEDING + " = " + String.valueOf(seeding) + ", " +
                COLUMN_BREAD_PERCENT + " = " + String.valueOf(breadPercent) + " WHERE " + COLUMN_USER + " = 1");
    }

    /*
     *  Обновление информации о наследнике
     */
    public void updateHeir(int heir)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                COLUMN_HEIR + " = " + String.valueOf(heir) + " WHERE " + COLUMN_USER + " = 1");
    }

    /*
     *  Обновление возраста - используется при начале правления наследника
     */
    public void updateAge(int age)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " +
                COLUMN_AGE + " = " + String.valueOf(age) + " WHERE " + COLUMN_USER + " = 1");
    }

    /*
     *  Установление стартовых значений при начале игры заново
     */
    public void clear()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        // начальные значения
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (1, 1, 1, 0, 100000, 10, 160000, 300, 300, 30, 100, 0, 0, 0, 0, 0, 0);");
    }

}
