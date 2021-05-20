package com.example.real_laddergame;

import android.app.TabActivity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TabHost;

public class TabViewActivity {
    EditText[] edit_p_name = new EditText[6];

    public void create_tab(TabHost mTabHost, final Context context, String type) {
        switch(type){
            case "persons":
                mTabHost.setup();

                TabHost.TabSpec tab1[] = new TabHost.TabSpec[5];

                for (int i = 0; i < 5; i++) { // main activity 에서 몇명 추가할껀지 추가하는것
                    String tab_spec = "tab1_tabs" + i;
                    int tab_id = context.getResources().getIdentifier("tab1_tabs" + i, "id", context.getPackageName());
                    tab1[i] = mTabHost.newTabSpec(tab_spec);
                    tab1[i].setContent(tab_id);
                }
                tab1[0].setIndicator("2명");
                tab1[1].setIndicator("3명");
                tab1[2].setIndicator("4명");
                tab1[3].setIndicator("5명");
                tab1[4].setIndicator("6명");

                for (int i = 0; i < 5; i++) {
                    mTabHost.addTab(tab1[i]);
                }
                mTabHost.setCurrentTab(0);
                break;
            case "wins":
                mTabHost.setup();

                TabHost.TabSpec tab2[] = new TabHost.TabSpec[5];

                for (int i = 0; i < 5; i++) {
                    String tab_spec = "tab2_tabs" + i;
                    int tab_id = context.getResources().getIdentifier("tab2_tabs" + i, "id", context.getPackageName());
                    tab2[i] = mTabHost.newTabSpec(tab_spec);
                    tab2[i].setContent(tab_id);
                }
                tab2[0].setIndicator("1개");
                tab2[1].setIndicator("2개");
                tab2[2].setIndicator("3개");
                tab2[3].setIndicator("4개");
                tab2[4].setIndicator("5개");

                for (int i = 0; i < 5; i++) {
                    mTabHost.addTab(tab2[i]);
                }
                mTabHost.setCurrentTab(0);
                break;
        }
    }
}
