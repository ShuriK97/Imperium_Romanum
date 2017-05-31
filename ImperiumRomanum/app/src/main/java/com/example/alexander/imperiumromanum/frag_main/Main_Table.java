package com.example.alexander.imperiumromanum.frag_main;

import android.app.AlertDialog;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.imperiumromanum.DatabaseHelper;
import com.example.alexander.imperiumromanum.MainScreen;
import com.example.alexander.imperiumromanum.R;

import static android.content.Context.MODE_PRIVATE;


public class Main_Table extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View frag =  inflater.inflate(R.layout.main_table, container, false);
/*        TextView gold = (TextView)frag.findViewById(R.id.gold);
        TextView land = (TextView)frag.findViewById(R.id.land);
        TextView seed = (TextView)frag.findViewById(R.id.seed);
        TextView slaves = (TextView)frag.findViewById(R.id.slaves);
        TextView army = (TextView)frag.findViewById(R.id.army);

        DatabaseHelper myDb = new DatabaseHelper(getActivity());
        myDb.getWritableDatabase(); // если база не создана, вызывает конструктор
        Cursor result = myDb.getData();
        result.moveToNext();
        gold.setText(String.valueOf(result.getInt(5)));
        seed.setText(String.valueOf(result.getInt(6)));
        land.setText(String.valueOf(result.getInt(7)));
        slaves.setText(String.valueOf(result.getInt(8)));
        army.setText(String.valueOf(result.getInt(9)));
*/        return frag;
    }
}