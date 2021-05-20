package com.example.real_laddergame;
import android.app.Application;

public class ResultListitem {

    String res_name;
    String res_result;

    public String getName(){return res_name;}
    public String getResult(){return res_result;}

        public void setResName(String in_name){
            this.res_name = in_name;
        }
        public void setResResult(String in_result){
            this.res_result = in_result;
        }


    ResultListitem(String res_name, String res_result){
            this.res_name = res_name;
            this.res_result = res_result;
        }

}
