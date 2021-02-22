package com.example.budgetbuddy.helper;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Utils {

    public Boolean isStringValidate(String text) {
        return !(text == null || text.isEmpty() || text.equals("null"));
    }

    public void yoyoAnimation(View view) {
        YoYo.with(Techniques.Shake).duration(700).playOn(view);
    }

    public boolean isEmailValidate(String email_id){

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email_id.matches(EMAIL_PATTERN);
    }

}
