package com.example.budgetbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.adapter.AdapterMenu;
import com.example.budgetbuddy.fragments.FragAddExpenses;
import com.example.budgetbuddy.fragments.FragMain;
import com.example.budgetbuddy.fragments.FragSettings;
import com.example.budgetbuddy.fragments.FragSmsList;
import com.example.budgetbuddy.helper.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.pixplicity.easyprefs.library.Prefs;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

public class ActMain extends ActBase implements AdapterMenu.OnRcvListener {

    Utils utils;
    MenuDrawer drawer;
    ImageView imgMenu, imgAdd;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    AppBarLayout mAppbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);


        initialization();
        initHomeFragment();
        initMenu();
        clickListeners();
        setUpToolbar();
    }

    private void setUpToolbar() {

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    private void initHomeFragment() {

        utils.initHomeFrag(getSupportFragmentManager(), new FragMain(), R.id.fragMainContainer);

    }

    private void initMenu() {
        drawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT);
        drawer.setContentView(R.layout.act_main);
        drawer.setMenuView(R.layout.menu_drawer);
        drawer.setMinimumWidth(100);

        String[] arrMenu = getResources().getStringArray(R.array.arrMenu);
        RecyclerView rcvMenu = findViewById(R.id.rcvMenu);
        rcvMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvMenu.setAdapter(new AdapterMenu(this, arrMenu, this));

//        Log.e("you have selected ", arrMenu[])

    }

    private void show_children(View v) {
        ViewGroup viewgroup = (ViewGroup) v;
        for (int i = 0; i < viewgroup.getChildCount(); i++) {
            View v1 = viewgroup.getChildAt(i);
            if (v1 instanceof ViewGroup) show_children(v1);
            Log.d("APPNAME", v1.toString());
        }
    }

    private void clickListeners() {


        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openMenu();
                Toast.makeText(ActMain.this, "Open Menu", Toast.LENGTH_SHORT).show();
            }
        });

       /* imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActMain.this, "Add", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void initialization() {
        //helper
        utils = new Utils();
        //view
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        imgMenu = findViewById(R.id.imgMainMenu);
        imgAdd = findViewById(R.id.imgAdd);
        mAppbar = findViewById(R.id.appbarLayout);


        //Firebase
        mAuth = FirebaseAuth.getInstance();



    }

    @Override
    public void onMenuClick(int position) {
        switch (position) {
            case 0:
                changeFragment(getSupportFragmentManager(), new FragMain(), R.id.fragMainContainer);
                Toast.makeText(this, getResources().getStringArray(R.array.arrMenu)[position], Toast.LENGTH_SHORT).show();
                break;
            case 1:
                changeFragment(getSupportFragmentManager(), new FragSmsList(), R.id.fragMainContainer);

                Toast.makeText(this, getResources().getStringArray(R.array.arrMenu)[position], Toast.LENGTH_SHORT).show();

                break;
            case 2:
                changeFragment(getSupportFragmentManager(), new FragAddExpenses(), R.id.fragMainContainer);
                Toast.makeText(this, getResources().getStringArray(R.array.arrMenu)[position], Toast.LENGTH_SHORT).show();
                break;

            case 3:
                Toast.makeText(this, getResources().getStringArray(R.array.arrMenu)[position], Toast.LENGTH_SHORT).show();
                break;
            case 4:
                changeFragment(getSupportFragmentManager(), new FragSettings(), R.id.fragMainContainer);
                Toast.makeText(this, getResources().getStringArray(R.array.arrMenu)[position], Toast.LENGTH_SHORT).show();
                break;
            case 5:
                callLogout();

        }
    }

    private void callLogout() {
        Intent loginIntent = new Intent(ActMain.this, ActLogin.class);
        Prefs.clear();
        mAuth.signOut();
        startActivity(loginIntent);
        finish();
    }

    private void changeFragment(FragmentManager supportFragmentManager, Fragment fragment, int containerUser) {
//        if (!checkFragmentAvailableOrNot(fragment.javaClass.simpleName)) {
//
//        }
        clearFragments();
        utils.addFragmentToActivity(supportFragmentManager, fragment, containerUser);
        drawer.closeMenu();
    }

    private void clearFragments() {

        for (int i = 0; i <= getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }
}