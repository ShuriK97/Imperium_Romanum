package com.example.alexander.imperiumromanum.frag_main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.imperiumromanum.R;

/*
 *  Верхний фрагмент главной активности (заголовок)
 */
public class Main_Top extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View frag = inflater.inflate(R.layout.main_top, container, false);
/*        TextView year = (TextView)frag.findViewById(R.id.year);
        TextView money = (TextView)frag.findViewById(R.id.money);
        DatabaseHelper myDb = new DatabaseHelper(getActivity());
        myDb.getWritableDatabase(); // если база не создана, вызывает конструктор
        Cursor result = myDb.getData();
        result.moveToNext();
        String strYear = "Год правления: " + String.valueOf(result.getInt(1));
        String strMoney = "Имперская казна: " + String.valueOf(result.getInt(4)) + " денариев";
        year.setText(strYear);
        money.setText(strMoney);
*/        return frag;
    }

}
