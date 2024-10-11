package com.example.graduationdesign.guess;

import android.content.Context;
import android.widget.EditText;

public interface GranaryGuessStrategy {
    boolean ifIsEmpty();
    void getDouble(String weight,String water,String impurity);
    void judgeLevel();
    void getTotal();
    void setDialog(Context mContext, boolean Switch);
}
