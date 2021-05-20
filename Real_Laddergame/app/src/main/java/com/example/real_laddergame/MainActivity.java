package com.example.real_laddergame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    Context mCtx = this;

    TabViewActivity tab_1_adapter;
    TabViewActivity tab_2_adapter;
    TabHost tab_1_host;
    TabHost tab_2_host;

    String p_names[] = new String[6];
    EditText edit_p_name[] = new EditText[6];

    LineSpinner lineSpinner;

    Button btn_play;
    Spinner sel_ladder;

    int persons;
    int wins;
    int ladder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab_1_adapter = new TabViewActivity();
        tab_1_host = (TabHost) findViewById(R.id.tabHost);
        tab_1_adapter.create_tab(tab_1_host, this, "persons");

        tab_2_adapter = new TabViewActivity();
        tab_2_host = (TabHost) findViewById(R.id.tabHost2);
        tab_2_adapter.create_tab(tab_2_host, this, "wins");

        btn_play = (Button) findViewById(R.id.btn_play);
        sel_ladder = (Spinner) findViewById(R.id.sp_lines);

        lineSpinner = new LineSpinner(this, sel_ladder);

        set_p_name();

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int host_index = tab_1_host.getCurrentTab();
                for(int index = 1; index <= host_index+2; index++) {
                    // View ID 동적할당
                    if(edit_p_name[index-1] == null){
                        int edit_p_name_id = mCtx.getResources().getIdentifier("tab"+host_index+"_persons_" + index, "id", mCtx.getPackageName());
                        edit_p_name[index-1] = (EditText) findViewById(edit_p_name_id);
                    }
                    if(edit_p_name[index-1].getText().toString().equals(null) || edit_p_name[index-1].getText().toString().equals("") ) {
                        p_names[index-1] = edit_p_name[index-1].getHint().toString();
                    } else {
                        p_names[index-1] = edit_p_name[index-1].getText().toString();
                    }
                    Log.d("p_names"+ (index-1) , p_names[index-1]);
                }

                persons = tab_1_host.getCurrentTab()+2;
                wins = tab_2_host.getCurrentTab()+1;
                ladder = lineSpinner.getLevel();

                if(persons > wins){
                    Intent intent = new Intent(getApplicationContext() , LadderGameActivity.class);
                    intent.putExtra("persons", persons);
                    intent.putExtra("wins", wins);
                    intent.putExtra("ladder", ladder);
                    intent.putExtra("p_names", p_names);
                    startActivity(intent);
                }else{
                    // tab2_tabs0
                    tab_2_host.setCurrentTab(persons-2);
                    Toast.makeText(getApplicationContext(), "당첨수는 참여인원을 초과할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    public void set_p_name(){
        tab_1_host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int host_index = tab_1_host.getCurrentTab();
                for(int index = 1; index <= host_index+2; index++) {
                    // View ID 동적할당
                    int edit_p_name_id = mCtx.getResources().getIdentifier("tab"+host_index+"_persons_" + index, "id", mCtx.getPackageName());
                    edit_p_name[index-1] = (EditText) findViewById(edit_p_name_id);
                    if (index != host_index + 2) {
                        edit_p_name[index-1].setNextFocusDownId(mCtx.getResources().getIdentifier("tab" + host_index + "_persons_" + (index+1), "id", mCtx.getPackageName()));
                    } else {
                        edit_p_name[index-1].setImeOptions(EditorInfo.IME_ACTION_DONE);
                    }
                }
            }
        });
    }
}