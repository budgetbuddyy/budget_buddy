package com.example.budgetbuddy.activities;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.helper.Loader;
import com.example.budgetbuddy.helper.Utils;
import com.pixplicity.easyprefs.library.Prefs;


class ActBase extends AppCompatActivity {

    Loader loader;
    public Utils utils = new Utils();
    Activity getActivity(){
        return  this;
    }

    public String getUserId(){
        return Prefs.getString(String.valueOf(R.string.user_id),"");
    }

    public void showLoader() {
        if(loader == null)
            loader = new Loader(getActivity());

        loader.show();
    }

    public void closeLoader(){
        loader.dismiss();
    }

}
