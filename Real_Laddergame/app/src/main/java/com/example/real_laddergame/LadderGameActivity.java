package com.example.real_laddergame;

// 전체 레이아웃
// 1. 참여인원 1명당 2개의 tv(이름tv, 공백tv)를 가짐
//       tv0        tv1         tv2        tv3       tv4        tv5
//   ┌-------┐┌-------┐┌-------┐┌-------┐┌-------┐┌-------┐
//   |  사람1 | |  공백  | |  사람2 | |  공백  | |  사람3 | |  공백  |
//  └-------┘└-------┘└-------┘└-------┘└-------┘└-------┘
//         ┌-------------------┐┌-------------------┐
//         |       사다리1      | |       사다리2      |
//        └-------------------┘└-------------------┘
//   ┌-------┐┌-------┐┌-------┐┌-------┐┌-------┐┌-------┐
//   |  결과1 | |  공백  | |  결과2 | |  공백  | |  결과3 | |  공백  |
//  └-------┘└-------┘└-------┘└-------┘└-------┘└-------┘


import android.app.ActionBar;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class LadderGameActivity extends AppCompatActivity {
    Context mCtx = LadderGameActivity.this;

    TableLayout tbl_1;      // 1. 참여인원
    TableLayout tbl_2;      // 2. 사다리
    TableLayout tbl_3;      // 3. 결과
    Button btn_result;
    ArrayList<ResultListitem> result_list_item;

    int persons;
    int wins;
    int lines;
    int ladder;
    int[] cnt_chk_border;
    String[] p_names = new String[6];

    final static int MAIN_SET = 1;
    final static int CHK_SET = 2;

    //ArrayList<String> list_person;
    ArrayList<String> list_result;

    ArrayList<Integer> list_border;
    final static int draw_bottom = R.drawable.left_bottom_right;
    final static int border_lr = R.drawable.left_right;

    TextView tv_cols[][];
    TableRow tableRow[];

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.play_layout);

        Intent intent = getIntent();
        persons = intent.getIntExtra("persons", 0);
        wins = intent.getIntExtra("wins", 0);
        ladder = intent.getIntExtra("ladder", 0);
        p_names = intent.getStringArrayExtra("p_names");
        lines = 9;
        cnt_chk_border = new int[persons-1];

        layout_set();
        set_layout_enter_person();
        set_layout_result();
        set_layout_ladder();
    }

    //private void setContentView(int play_layout) {
   // }

    public void layout_set(){
        tbl_1 = (TableLayout) findViewById(R.id.tbl_1);
        tbl_2 = (TableLayout) findViewById(R.id.tbl_2);
        tbl_3 = (TableLayout) findViewById(R.id.tbl_3);
        btn_result = (Button) findViewById(R.id.btn_result);
        //결과보기 버튼 이벤트
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                result_list_item = new ArrayList<>();

                for(int cols = 0; cols < persons*2; cols = cols +2) {
                    logic_result(cols);
                }

                LayoutInflater inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View list_layout = inflater.inflate(R.layout.result_layout, (ViewGroup) findViewById(R.id.result_layout), false);

                ListView listView = (ListView) list_layout.findViewById(R.id.result_list);
                LineViewArrayAdapter listAdapter = new LineViewArrayAdapter(mCtx.getApplicationContext(), R.layout.result_item, R.id.res_name, result_list_item);
                listView.setAdapter(listAdapter);

                /*
                //결과 출력 Log
                for(int index = 0 ; index < result_list_item.size(); index ++){
                    Log.d("Name/Result", result_list_item.get(index).getName() +" / "+ result_list_item.get(index).getResult());
                }
                */

                Dialog result_alert = new Dialog(mCtx);
                result_alert.setContentView(list_layout);
                result_alert.setCancelable(true);
                result_alert.show();
            }
        });
    }

    //1. 참여인원 레이아웃
    public void set_layout_enter_person(){

        //set_person_name();

        tbl_1.removeAllViews();

        int name_index = 0;

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1);

        TableRow tr_enter_person = new TableRow(mCtx);
        tr_enter_person.setLayoutParams(layoutParams);

        TextView tv_person_0 = new TextView(mCtx);
        //tv_person_0.setText("" + get_person(name_index));
        tv_person_0.setText(""+p_names[0]);
        tv_person_0.setLayoutParams(layoutParams);
        tr_enter_person.addView(tv_person_0);

        for(int i = 1; i < (persons-1) * 2; i ++){
            if( i % 2 == 1) {
                name_index++;
                TextView tv_person = new TextView(mCtx);
                tv_person.setLayoutParams(layoutParams);
                //tv_person.setText("" + get_person(name_index));
                tv_person.setText("" + p_names[name_index]);
                if(i == ((persons-1)*2)-1) {
                    tv_person.setGravity(Gravity.RIGHT);
                } else {
                    tv_person.setGravity(Gravity.CENTER);
                }
                tr_enter_person.addView(tv_person);
            }
        }
        tbl_1.addView(tr_enter_person);
    }
    // 참여인원의 이름 저장.
    /*
    public void set_person_name(){
        list_person = new ArrayList<String>();

        list_person.add(0,"1번");
        list_person.add(1,"2번");
        list_person.add(2,"3번");
        list_person.add(3,"4번");
        list_person.add(4,"5번");
        list_person.add(5,"6번");

    }
    public String get_person(int index){
        return list_person.get(index);
    }
    */

    //2. 사다리 레이아웃
    public void set_layout_ladder(){

        tbl_2.removeAllViews();

        tableRow = new TableRow[lines];
        tv_cols = new TextView[lines][persons-1];

        for(int cols = 0; cols < persons-1; cols++){
            cnt_chk_border[cols] = 0;
        }

        for(int rows = 0; rows < lines; rows ++) {
            tableRow[rows] = new TableRow(mCtx);

            tableRow[rows].setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1));

            for(int cols = 0; cols < persons-1; cols++) {
                tv_cols[rows][cols] = new TextView(mCtx);

                tv_cols[rows][cols] = (TextView) set_ladder(tv_cols[rows][cols], rows, cols, MAIN_SET);

                tv_cols[rows][cols].setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1
                ));
            }
        }
        for(int cols = 0 ; cols < persons-1; ){
            if (cnt_chk_border[cols] > ladder || cnt_chk_border[cols] == 0) {
                chk_layout_border(cols);
            } else {
                cols++;
            }
        }

        for(int rows = 0; rows < lines; rows++){
            for(int cols = 0; cols < persons-1; cols++){
                tableRow[rows].addView(tv_cols[rows][cols]);
            }
            tbl_2.addView(tableRow[rows]);
        }
    }

    // 사다리가 제대로 생성되었는지 확인.
    // Ex) 사다리가 없거나, 옆 사다리와 연결되어 있는 경우.
    public void chk_layout_border(int cols){
        cnt_chk_border[cols] = 0;
        for (int rows = 0; rows < lines; rows++) {
            set_ladder(tv_cols[rows][cols], rows, cols, CHK_SET);
        }
    }

    /*
            사다리를 그려주는 함수
        - Bottom 이 있는 사다리 Drawable -> draw_bottom



        - Bottom 이 없는 사다리 Drawable -> border_lr

        1. 사다리의 마지막 행은 border_lr
        2. 사다리의 열이 0일 경우,
        3. 사다리의 열이 마지막일 경우,
    */
    public View set_ladder(View tv, int rows, int cols, int where){

        int set_border =  get_shuffled_border();

        if(rows == lines-1) {
            tv.setBackgroundResource(border_lr);
            tv.setTag(border_lr);
        } else if(cols == 0) {
            switch(where) {
                // 사다리를 랜덤으로 그려줌
                case MAIN_SET:
                    tv.setBackgroundResource(set_border);
                    tv.setTag(set_border);
                    if (set_border == draw_bottom)
                        cnt_chk_border[cols] = cnt_chk_border[cols] + 1;
                    break;
                // 현재 행의 오른쪽 열이 draw_bottom 경우, 현재 행은 border_lr를 그려주어 중복을 피함
                case CHK_SET:
                    if (persons != 2 && Integer.parseInt(tv_cols[rows][cols + 1].getTag().toString()) == draw_bottom) {
                        tv.setBackgroundResource(border_lr);
                        tv.setTag(border_lr);
                    } else {
                        tv.setBackgroundResource(set_border);
                        tv.setTag(set_border);
                        if (set_border == draw_bottom)
                            cnt_chk_border[cols] = cnt_chk_border[cols] + 1;
                    }
                    break;
            }
        }else if(rows >= 0 && cols > 0){
            switch(where){
                case MAIN_SET:
                    if (Integer.parseInt(tv_cols[rows][cols - 1].getTag().toString()) == draw_bottom) {
                        tv.setBackgroundResource(border_lr);
                        tv.setTag(border_lr);
                    } else {
                        tv.setBackgroundResource(set_border);
                        tv.setTag(set_border);
                        if (set_border == draw_bottom)
                            cnt_chk_border[cols] = cnt_chk_border[cols] + 1;
                    }
                    break;
                case CHK_SET:
                    if(cols+1 >= tv_cols[rows].length) {
                        if (Integer.parseInt(tv_cols[rows][cols - 1].getTag().toString()) == draw_bottom) {
                            tv.setBackgroundResource(border_lr);
                            tv.setTag(border_lr);
                        } else {
                            tv.setBackgroundResource(set_border);
                            tv.setTag(set_border);
                            if (set_border == draw_bottom)
                                cnt_chk_border[cols] = cnt_chk_border[cols] + 1;
                        }
                    } else {
                        if (Integer.parseInt(tv_cols[rows][cols - 1].getTag().toString()) == draw_bottom || Integer.parseInt(tv_cols[rows][cols + 1].getTag().toString()) == draw_bottom) {
                            tv.setBackgroundResource(border_lr);
                            tv.setTag(border_lr);
                        } else {
                            tv.setBackgroundResource(set_border);
                            tv.setTag(set_border);
                            if (set_border == draw_bottom)
                                cnt_chk_border[cols] = cnt_chk_border[cols] + 1;
                        }
                    }
                    break;
            }
        }

        return tv;
    }
    //사다리를 랜덤으로 그려주기 위해 리스트를 셔플해주는 함수
    public int get_shuffled_border(){
        list_border = new ArrayList<Integer>();
        list_border.add(draw_bottom);
        list_border.add(border_lr);
        Collections.shuffle(list_border);
        return list_border.get(0);
    }

    // 3. 결과 레이아웃
    // 참여인원 레이아웃과 유사함.
    public void set_layout_result(){

        tbl_3.removeAllViews();

        set_result();

        int result_index = 0;

        TableRow tr_result = new TableRow(mCtx);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1
        );
        tr_result.setLayoutParams(layoutParams);

        TextView tv_result_0 = new TextView(mCtx);
        tv_result_0.setLayoutParams(layoutParams);
        tv_result_0.setText(""+get_result(result_index));
        tr_result.addView(tv_result_0);


        for(int i = 1; i < (persons-1) * 2; i ++)
            if (i % 2 == 1) {
                TextView tv_result = new TextView(mCtx);
                tv_result.setLayoutParams(layoutParams);

                if (i == ((persons - 1) * 2) - 1) {
                    tv_result.setGravity(Gravity.RIGHT);
                } else {
                    tv_result.setGravity(Gravity.CENTER);
                }

                result_index = result_index + 1;
                tv_result.setText("" + get_result(result_index));

                tr_result.addView(tv_result);
            }
        tbl_3.addView(tr_result);
    }

    // 사다리타기 결과 설정
    public void set_result(){

        list_result = new ArrayList<String>();

        for(int index_win = 0; index_win < persons; index_win++) {
            if(index_win >= wins) {
                list_result.add("꽝");
            }else{
                list_result.add("당첨");
            }
        }
        Collections.shuffle(list_result);
    }
    public String get_result(int index){
        return list_result.get(index);
    }

    // 사다리를 검사하는 로직
    // 참여인원1 부터 순서대로 검사.
    public void logic_result(int cols) {

        int tv_cols_tag_left = 0;
        int tv_cols_tag_right = 0;
        int tv_cols_tag = 0;
        int persons_cols = cols;
        int result_name_index = 0;
        boolean vec_switch = true;

        // 참여인원은 2개의 TextView를 가지므로 Index를 2로 나눔
        if ((cols %= 2) == 1) {
            persons_cols = (persons_cols - 1) / 2;
        } else {
            persons_cols = persons_cols / 2;
        }
        if (persons_cols == persons - 1) {
            result_name_index = persons_cols;
            persons_cols = persons_cols - 1;
            vec_switch = false;
        } else {
            result_name_index = persons_cols;
        }

        // 사다리판별 반복문
        for (int rows = 0; rows < lines; rows++) {
            tv_cols_tag = Integer.parseInt(tv_cols[rows][persons_cols].getTag().toString());
            //Log.d("Result_1", "Rows : " + rows + " / Cols : " + persons_cols + " / Switch : " + vec_switch + " / lbr : " + tv_cols_tag);

            //참여인원 2명일 경우.
            if (persons == 2) {
                if(tv_cols_tag == draw_bottom){
                    if(vec_switch){
                        vec_switch = false;
                    } else {
                        vec_switch = true;
                    }
                }
            } else {
                // 참여인원이 3명이상일 경우.
                // cols == 마지막 열일 경우,
                // 참여인원(6) => max_cols = 참여인원-2
                if (persons_cols == persons - 2) {
                    if (tv_cols_tag == draw_bottom) {
                        if (vec_switch) {
                            vec_switch = false;
                        } else {
                            persons_cols--;
                        }
                    } else {
                        if (vec_switch) {
                            tv_cols_tag_left = Integer.parseInt(tv_cols[rows][persons_cols - 1].getTag().toString());
                            if (tv_cols_tag_left == draw_bottom) {
                                persons_cols--;
                            }
                        }
                    }
                    // cols == 0 일 경우,
                } else if (persons_cols == 0) {
                    if (tv_cols_tag == draw_bottom) {
                        if (vec_switch) {
                            persons_cols++;
                        } else {
                            vec_switch = true;
                        }
                    } else {
                        if (!vec_switch) {
                            tv_cols_tag_right = Integer.parseInt(tv_cols[rows][persons_cols + 1].getTag().toString());
                            if (tv_cols_tag_right == draw_bottom) {
                                persons_cols++;
                            }
                        }
                    }
                } else {
                    // 현재 사다리가 draw_bottom 아닌 경우, 좌우 사다리를 확인.
                    if (tv_cols_tag == draw_bottom) {
                        if (vec_switch) {
                            persons_cols++;
                        } else {
                            persons_cols--;
                        }
                    } else {
                        if (vec_switch) {
                            tv_cols_tag_left = Integer.parseInt(tv_cols[rows][persons_cols - 1].getTag().toString());
                            if (tv_cols_tag_left == draw_bottom) {
                                persons_cols--;
                            }
                        } else {
                            tv_cols_tag_right = Integer.parseInt(tv_cols[rows][persons_cols + 1].getTag().toString());
                            if (tv_cols_tag_right == draw_bottom) {
                                persons_cols++;
                            }
                        }
                    }
                }
                //Log.d("Result_2", "Rows : " + rows + " / Cols : " + persons_cols + " / Switch : " + vec_switch + " / lbr : " + tv_cols_tag);
            }
        }

        // true일 경우, 현재 cols의 결과
        // false일 경우, 현재 cols의 오른쪽 결과를 출력.
        if (vec_switch) {
            ResultListitem item = new ResultListitem(p_names[result_name_index], list_result.get(persons_cols));
            result_list_item.add(item);
            //Log.d("이름/결과 True", "이름 : " + list_person.get(result_name_index) + "/결과 : " + list_result.get(persons_cols));
        } else {
            ResultListitem item = new ResultListitem(p_names[result_name_index], list_result.get(persons_cols + 1));
            result_list_item.add(item);
            //Log.d("이름/결과 False", "이름 : " + list_person.get(result_name_index) + "/결과 : " + list_result.get(persons_cols+1));
        }
    }
}

