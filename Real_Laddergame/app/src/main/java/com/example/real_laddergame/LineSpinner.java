package com.example.real_laddergame;

import android.content.Context;
import android.widget.AdapterView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class LineSpinner implements AdapterView.OnItemSelectedListener{

    ArrayList<Integer> arr_level;
    ArrayList<String> arr_name;
    int ini_level = 3;

    Spinner sp_level;



    public LineSpinner(Context mCtx, Spinner sp_level){
        ini();
        this.sp_level = sp_level;

        ArrayAdapter<String> level_adapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, arr_name);

        sp_level.setOnItemSelectedListener(this);

        sp_level.setAdapter(level_adapter);
        sp_level.setSelection(1);
    }
    public void ini(){
        arr_level = new ArrayList<>();
        arr_name = new ArrayList<>();

        for(int line = 1; line <= 3; line++){
            switch(line){
                case 1:
                    arr_name.add("적음");
                    break;
                case 2:
                    arr_name.add("보통");
                    break;
                case 3:
                    arr_name.add("많음");
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(sp_level.getSelectedItem().toString()){
            case "적음":
                ini_level = 2;
                break;
            case "보통":
                ini_level = 3;
                break;
            case "많음":
                ini_level = 4;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public int getLevel(){
        return ini_level;
    }
}
